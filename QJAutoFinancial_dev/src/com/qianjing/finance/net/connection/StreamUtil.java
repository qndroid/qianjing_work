package com.qianjing.finance.net.connection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * <p>StreamUtil</p>
 * <p>Description: IO工具类</p>
 * @author fangyan
 * @date 2015年2月5日
 */
public class StreamUtil {

	public static byte[] getData(String path) {
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5 *1000);
			int code = conn.getResponseCode();
			if(code==200){
				InputStream inStream = conn.getInputStream();
				return StreamUtil.readStream(inStream);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] readStream(InputStream inStream) throws IOException{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while( (len=inStream.read(buffer)) != -1 ){
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}
}
