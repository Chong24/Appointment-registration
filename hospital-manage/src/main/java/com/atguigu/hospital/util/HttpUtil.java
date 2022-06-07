package com.atguigu.hospital.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 */
@Slf4j
public final class HttpUtil {

	static final String POST = "POST";
	static final String GET = "GET";
	static final int CONN_TIMEOUT = 30000;// ms
	static final int READ_TIMEOUT = 30000;// ms

	/**
	 * post 方式发送http请求：post是带请求头的.
	 * 
	 * @param strUrl
	 * @param reqData
	 * @return
	 */
	public static byte[] doPost(String strUrl, byte[] reqData) {
		return send(strUrl, POST, reqData);
	}

	/**
	 * get方式发送http请求.
	 * 
	 * @param strUrl
	 * @return
	 */
	public static byte[] doGet(String strUrl) {
		return send(strUrl, GET, null);
	}

	/**
	 * //通过HttpURLConnection获取连接，远程调用，基本是固定写法
	 //HttpURLConnection：JDK自带的HTTP请求客户端。
	 * @param strUrl
	 * @param reqmethod
	 * @param reqData
	 * @return
	 */
	public static byte[] send(String strUrl, String reqmethod, byte[] reqData) {

		try {
			//创建连接对象
			URL url = new URL(strUrl);
			//创建连接
			HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
			//设置是否可读取
			// DoOutput设置是否向httpUrlConnection输出，DoInput设置是否从httpUrlConnection读入，
			// 此外发送post请求必须设置这两个
			httpcon.setDoOutput(true);
			httpcon.setDoInput(true);
			//是否用缓存
			httpcon.setUseCaches(false);
			//是否连接遵循重定向，设置为true可以自动跳转，但对于对此跳转的只能跳一次
			httpcon.setInstanceFollowRedirects(true);
			//设置连接超时时间
			httpcon.setConnectTimeout(CONN_TIMEOUT);
			//设置读取超时时间
			httpcon.setReadTimeout(READ_TIMEOUT);
			//请求方法
			httpcon.setRequestMethod(reqmethod);
			//连接
			httpcon.connect();
			if (reqmethod.equalsIgnoreCase(POST)) {
				//将参数（请求参数，拼接在路径上的）用输出流传给被调用的方法，并执行方法
				OutputStream os = httpcon.getOutputStream();
				os.write(reqData);
				os.flush();
				os.close();
			}
			//设置输入流，远程调用完，将数据返回给调用者
			BufferedReader in = new BufferedReader(new InputStreamReader(httpcon.getInputStream(),"utf-8"));
			String inputLine;
			StringBuilder bankXmlBuffer = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {  
			    bankXmlBuffer.append(inputLine);  
			}
			//关闭资源
			in.close();  
			httpcon.disconnect();
			return bankXmlBuffer.toString().getBytes();
		} catch (Exception ex) {
			log.error(ex.toString(), ex);
			return null;
		}
	}
	
	/**
	 * 从输入流中读取数据
	 * 
	 * @param inStream
	 * @return
	 * @throws Exception
	 */
	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();// 网页的二进制数据
		outStream.close();
		inStream.close();
		return data;
	}
}
