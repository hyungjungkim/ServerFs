package network.server;

import java.io.FileInputStream;
import java.util.Properties;

public class ThreadProperty {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {

			// 프로퍼티 파일 위치
			String propFile = "D:/Project/Arointech/RedisTest/src/config.properties";

			// 프로퍼티 객체 생성
			Properties props = new Properties();

			// 프로퍼티 파일 스트림에 담기
			FileInputStream fis = new FileInputStream(propFile);

			// 프로퍼티 파일 로딩
			props.load(new java.io.BufferedInputStream(fis));

			// 항목 읽기
			String msg = props.getProperty("MSG");

			// 콘솔 출력
			System.out.println(msg);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
