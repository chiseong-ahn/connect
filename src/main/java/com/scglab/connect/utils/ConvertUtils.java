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
				ImageIOUtil.writeImage(bim, imgFileName, 300);
			}
			pdfDoc.close(); // 모두 사용한 PDF 문서는 닫는다.
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ConvertUtils convert = new ConvertUtils();
		String pdfFilePath = "/Volumes/Data/test/test.pdf";
		String savePath = "/Volumes/Data/test";
		
		convert.pdfToImage(pdfFilePath, savePath);
	}
}
