package com.scglab.connect.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

	public void create(String fileName, String[] titles, String[] keys, List<Map<String, Object>> datas, HttpServletResponse response) throws IOException {
		// flush되기 전까지 메모리에 들고있는 행의 갯수
		int ROW_ACCESS_WINDOW_SIZE = 500;

		XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
		SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(xssfWorkbook, ROW_ACCESS_WINDOW_SIZE);

		SXSSFSheet objSheet = null;
		SXSSFRow objRow = null;
		SXSSFCell objCell = null; // 셀 생성

		// 제목 폰트
		Font font = sxssfWorkbook.createFont();
		font.setFontHeightInPoints((short) 9);
		font.setBold(Boolean.TRUE);
		font.setFontName("맑은고딕");

		// 제목 스타일에 폰트 적용, 정렬
		CellStyle styleHd = sxssfWorkbook.createCellStyle(); // 제목 스타일
		styleHd.setFont(font);
		styleHd.setAlignment(CellStyle.ALIGN_CENTER);
		styleHd.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		styleHd.setBorderTop(CellStyle.BORDER_THIN);
		styleHd.setBorderBottom(CellStyle.BORDER_THIN);
		styleHd.setBorderRight(CellStyle.BORDER_THIN);
		styleHd.setBorderLeft(CellStyle.BORDER_THIN);
		styleHd.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		styleHd.setTopBorderColor(IndexedColors.BLACK.getIndex());
		styleHd.setRightBorderColor(IndexedColors.BLACK.getIndex());
		styleHd.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		styleHd.setLocked(true);
		
		CellStyle styleBd = sxssfWorkbook.createCellStyle(); // 제목 스타일
		styleBd.setFont(font);
		styleBd.setAlignment(CellStyle.ALIGN_CENTER);
		styleBd.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		objSheet = sxssfWorkbook.createSheet("Sheet1"); // 워크시트 생성


		// 스타일 미리 적용
		for( int i = 0; i < 1; i++ ) {
			objRow = objSheet.createRow(i);
			for( int j = 0; j < 35; j++ ) {
				objCell = objRow.createCell(j);
				objCell.setCellStyle(styleHd);
				objSheet.setColumnWidth(j, (short) 6000);
			}
		}

		// 0번째 열
		objRow = objSheet.getRow(0);
		objRow.setHeight((short) 0x150);

		objCell = objRow.getCell(0);
		objCell.setCellValue("No.");
		
		int k = 1;
		for(String title : titles) {
			objCell = objRow.getCell(k);
			objCell.setCellValue(title);
			k++;
		}

		CellStyle cellStyle = sxssfWorkbook.createCellStyle(); // 제목 스타일
		cellStyle.setFillForegroundColor( HSSFColor.GREY_25_PERCENT.index );
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		int m = 1;
		for(Map<String, Object> data : datas) {
			objRow = objSheet.createRow(m);
			objRow.setHeight((short) 0x150);
			
			objCell = objRow.createCell(0);
			objCell.setCellValue(m + "");
			objCell.setCellStyle(styleBd);
			
			int n = 1;
			Object val;
			String value;
			for(String key : keys) {
				objCell = objRow.createCell(n);
				
				if(data.containsKey(key) == true) {
					val = data.get(key);
					
					if(val instanceof Integer) {
						value = Integer.toString((int)val) + "";
						
					}else if(val instanceof Double){
						value = Double.toString((Double)val) + "";
						
					}else if(val instanceof BigDecimal){
						value = (BigDecimal) val + "";
						
					}else if(val instanceof String){
						value = (String) val;
						
					}else {
						value = val + "";
					}
					objCell.setCellValue(value);
				}else {
					objCell.setCellValue("");
				}
				
				objCell.setCellStyle(styleBd);
				
				n++;
			}
			m++;
		}
		
		if(response != null) {
			response.setContentType("Application/Msexcel");
			response.setHeader("Content-Disposition", "ATTachment; Filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");

			OutputStream fileOut = response.getOutputStream();
			sxssfWorkbook.write(fileOut);
			fileOut.close();

			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
		
		sxssfWorkbook.dispose();
	}
	
	public static void main(String[] args) {
		ExcelUtils excelUtils = new ExcelUtils();
		
		
		String[] titles = {"번호", "고객명", "채팅상담일시", "상담원", "평점"};
		String[] keys = {"id", "customerName", "createDate", "memberName", "reviewScore"};
		
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		Map<String, Object> data1 = new HashMap<String, Object>();
		data1.put(keys[0], 1);
		data1.put(keys[1], "안치성");
		data1.put(keys[2], "2021-02-25 13:23:09.0");
		data1.put(keys[3], "서울도시가스");
		data1.put(keys[4], 5);
		datas.add(data1);
		
		Map<String, Object> data2 = new HashMap<String, Object>();
		data2.put(keys[0], 1);
		data2.put(keys[1], "홍길동");
		data2.put(keys[2], "2021-02-25 13:23:09.0");
		data2.put(keys[3], "서울도시가스");
		data2.put(keys[4], 3);
		datas.add(data2);
		
		try {
			excelUtils.create("고객만족도현황", titles, keys, datas, null);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
