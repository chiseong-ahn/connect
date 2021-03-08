package com.scglab.connect.utils;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

public class ConvertUtils {
	public void pdfToImage(String pdfFilePath, String savePath) {
		try {
			File pdfFile = new File(pdfFilePath);
			PDDocument pdfDoc = PDDocument.load(pdfFile);
			PDFRenderer pdfRenderer = new PDFRenderer(pdfDoc);

			// 저장될 디렉토리가 존재하지 않을경우 생성.
			Files.createDirectories(Paths.get(savePath));

			// 순회하며 이미지로 변환 처리
			for (int i = 0; i < pdfDoc.getPages().getCount(); i++) {
				String imgFileName = savePath + "/" + i + ".png";
				BufferedImage bim = pdfRenderer.renderImageWithDPI(i, 300, ImageType.RGB);

				// 이미지로 만든다.
				ImageIOUtil.writeImage(bim, imgFileName, 80);
				
				//convertImageToText(bim);
				//convertImageToText(imgFileName);
			}
			pdfDoc.close(); // 모두 사용한 PDF 문서는 닫는다.
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String convertImageToText(BufferedImage bufferedImage) {
		Tesseract tesseract = new Tesseract();
		String result = "";
		
		try {
			result = tesseract.doOCR(bufferedImage);
			System.out.println(bufferedImage + " : " + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;	
	}
	
	public String convertImageToText(String imageFilePath) {
		String result = "";
		ITesseract instance = new Tesseract();
		
		System.out.println("imageFilePath : " + imageFilePath);
		File imageFile = new File(imageFilePath);
		
		if(imageFile.exists()) {
			try {
				result = instance.doOCR(imageFile);
				System.out.println(imageFilePath + " : " + result);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("파일이 존재하지 않아 처리할 수 없습니다.");
		}
		
		return result;	
	}
	
	public static void main(String[] args) {
		ConvertUtils convert = new ConvertUtils();
//		String pdfFilePath = "/Volumes/Data/test/test.pdf";
//		String savePath = "/Volumes/Data/test";
//		
//		convert.pdfToImage(pdfFilePath, savePath);
		
		String imagePath = "/Volumes/Data/test/0.png";
		convert.convertImageToText(imagePath);
	}
}
