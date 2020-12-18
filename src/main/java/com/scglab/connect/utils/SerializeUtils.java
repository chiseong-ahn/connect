package com.scglab.connect.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

import com.scglab.connect.services.socket.SocketData;
import com.scglab.connect.services.socket.SocketService.EventName;

public class SerializeUtils {
	public static String serialize(Object article) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// 아래와 같은 형태의 try-with-resources 구문은 java 9 버전부터 지원한다.
        try (bos; ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(article);
        } catch (Exception e) {
            // ... 생략
        }
        
        // 바이트 배열로 생성된 데이터를 정상 출력하기 위해 base64 인코딩 
        return Base64.getEncoder().encodeToString(bos.toByteArray());
	}
	
	public static Object deserialize(String str) {
		byte[] decodedData = Base64.getDecoder().decode(str);
        ByteArrayInputStream bis = new ByteArrayInputStream(decodedData);
        try (bis; ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (Object) ois.readObject();
        } catch (Exception e) {
        	e.printStackTrace();
        	throw new RuntimeException("failed deserialize!");
        }
	}
	
	public static void main(String[] args) {
		SocketData article = new SocketData();
		article.setEventName(EventName.ASSIGN);
		article.setCompanyId("1");
		article.setRoomId("147");
		article.setData(null);
		System.out.println("article : " + article.toString());
		
		String serializedText = SerializeUtils.serialize(article);
		System.out.println("serializedText : " + serializedText);
		
		SocketData deserializedArticle = (SocketData) SerializeUtils.deserialize(serializedText);
		System.out.println("deserializedArticle : " + deserializedArticle.toString());
	}
}
