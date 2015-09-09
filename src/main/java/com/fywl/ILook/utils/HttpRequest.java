package com.fywl.ILook.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import com.fywl.ILook.bean.FormFieldKeyValuePair;
import com.fywl.ILook.bean.UploadFileItem;

public class HttpRequest {
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			// connection.setRequestProperty("accept", "*/*");
			// connection.setRequestProperty("connection", "Keep-Alive");
			// connection.setRequestProperty("user-agent",
			// "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	// 每个post参数之间的分隔。随意设定，只要不会和其他的字符串重复即可。
	private static final String BOUNDARY = "----------HV2ymHFg03ehbqgZCaKO6jyH";

	public static String sendHttpPostRequest(String serverUrl,
			ArrayList<FormFieldKeyValuePair> generalFormFields,
			ArrayList<UploadFileItem> filesToBeUploaded) throws Exception {
		// 向服务器发送post请求
		URL url = new URL(serverUrl/* "http://127.0.0.1:8080/test/upload" */);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// 发送POST请求必须设置如下两行
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setUseCaches(false);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Connection", "Keep-Alive");
		connection.setRequestProperty("Charset", "UTF-8");
		connection.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + BOUNDARY);

		// 头
		String boundary = BOUNDARY;
		// 传输内容
		StringBuffer contentBody = new StringBuffer("--" + BOUNDARY);
		// 尾
		String endBoundary = "\r\n--" + boundary + "--\r\n";

		OutputStream out = connection.getOutputStream();

		// 1. 处理普通表单域(即形如key = value对)的POST请求
		for (FormFieldKeyValuePair ffkvp : generalFormFields) {
			contentBody.append("\r\n")
					.append("Content-Disposition: form-data; name=\"")
					.append(ffkvp.getKey() + "\"").append("\r\n")
					.append("\r\n").append(ffkvp.getValue()).append("\r\n")
					.append("--").append(boundary);
		}
		String boundaryMessage1 = contentBody.toString();
		out.write(boundaryMessage1.getBytes("utf-8"));

		// 2. 处理文件上传
		for (UploadFileItem ufi : filesToBeUploaded) {
			contentBody = new StringBuffer();
			contentBody
					.append("\r\n")
					.append("Content-Disposition:form-data; name=\"")
					.append(ufi.getFormFieldName() + "\"; ")
					// form中field的名称
					.append("filename=\"")
					.append(ufi.getFileName() + "\"")
					// 上传文件的文件名，包括目录
					.append("\r\n")
					.append("Content-Type:application/octet-stream")
					.append("\r\n\r\n");

			String boundaryMessage2 = contentBody.toString();
			out.write(boundaryMessage2.getBytes("utf-8"));

			// 开始真正向服务器写文件
			File file = new File(ufi.getFileName());
			DataInputStream dis = new DataInputStream(new FileInputStream(file));
			int bytes = 0;
			byte[] bufferOut = new byte[(int) file.length()];
			bytes = dis.read(bufferOut);
			out.write(bufferOut, 0, bytes);
			dis.close();

			// contentBody.append("------------HV2ymHFg03ehbqgZCaKO6jyH");
			//
			// String boundaryMessage = contentBody.toString();
			// out.write(boundaryMessage.getBytes("utf-8"));
			// System.out.println(boundaryMessage);
			out.write("------------HV2ymHFg03ehbqgZCaKO6jyH--\r\n"
					.getBytes("UTF-8"));
		}
		out.write("------------HV2ymHFg03ehbqgZCaKO6jyH--\r\n"
				.getBytes("UTF-8"));

		// 3. 写结尾
		out.write(endBoundary.getBytes("utf-8"));
		out.flush();
		out.close();

		// 4. 从服务器获得回答的内容
		String strLine = "";
		String strResponse = "";

		InputStream in = connection.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		while ((strLine = reader.readLine()) != null) {
			strResponse += strLine + "\n";
		}
		// System.out.print(strResponse);

		return strResponse;
	}

	// public static void upload(String serverUrl) {
	// try {
	// URL url = new URL("serverUrl"); // 文件接收的CGI,不一定是JSP的
	//
	// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	// conn.setRequestMethod("POST");
	// conn.setDoOutput(true);
	//
	// String BOUNDARY = "---------------------------7d4a6d158c9"; // 分隔符
	//
	// StringBuffer sb = new StringBuffer();
	// sb.append("--");
	// sb.append(BOUNDARY);
	// sb.append("\r\n");
	// sb.append("Content-Disposition: form-data; name=\"myfile\"; filename=\"test.txt\"\r\n");
	// sb.append("Content-Type: application/octet-stream\r\n\r\n");
	//
	// byte[] data = sb.toString().getBytes();
	// byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
	//
	// conn.setRequestProperty("Content-Type",
	// "multipart/form-data; boundary=" + BOUNDARY); // 设置表单类型和分隔符
	// conn.setRequestProperty("Content-Length",
	// String.valueOf(data.length + buf.length + end_data.length)); // 设置内容长度
	//
	// os = conn.getOutputStream();
	// os.write(data);
	//
	// FileInputStream fis = new FileInputStream(new File(
	// "E:/badboy/test.txt")); // 要上传的文件
	//
	// int rn2;
	// byte[] buf2 = new byte[1024];
	// while ((rn2 = fis.read(buf2, 0, 1024)) > 0) {
	// os.write(buf2, 0, rn2);
	//
	// }
	//
	// os.write(end_data);
	// os.flush();
	// os.close();
	// fis.close();
	//
	// // 得到返回的信息
	// InputStream is = conn.getInputStream();
	//
	// byte[] inbuf = new byte[1024];
	// int rn;
	// while ((rn = is.read(inbuf, 0, 1024)) > 0) {
	//
	// System.out.write(inbuf, 0, rn);
	//
	// }
	// is.close();
	//
	// } catch (Exception ee) {
	// System.out.println("上传出错.");
	// }
	//
	// }

	/* 上传文件至Server的方法 */
	public static String uploadFile(String actionUrl, String uploadFile) {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		String newName = "123.mp4";
		// String uploadFile = "storage/sdcard1/bagPictures/102.jpg";
		;
		// String actionUrl =
		// "http://192.168.1.123:8080/upload/servlet/UploadServlet";
		try {
			URL url = new URL(actionUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			/* 允许Input、Output，不使用Cache */
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			/* 设置传送的method=POST */
			con.setRequestMethod("POST");
			/* setRequestProperty */
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			/* 设置DataOutputStream */
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; "
					+ "name=\"file1\";filename=\"" + newName + "\"" + end);
			ds.writeBytes(end);
			/* 取得文件的FileInputStream */
			FileInputStream fStream = new FileInputStream(uploadFile);
			/* 设置每次写入1024bytes */
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			/* 从文件读取数据至缓冲区 */
			while ((length = fStream.read(buffer)) != -1) {
				/* 将资料写入DataOutputStream中 */
				ds.write(buffer, 0, length);
			}
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			/* close streams */
			fStream.close();
			ds.flush();
			/* 取得Response内容 */
//			String strLine = "";
//			String strResponse = "";
//
//			InputStream in = con.getInputStream();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//			while ((strLine = reader.readLine()) != null) {
//				strResponse += strLine + "\n";
//			}
			InputStream is = con.getInputStream();
			int ch;
			byte[] b = new byte[1024];
			// while ((ch = is.read()) != -1) {
			// b.append((char) ch);
			// }
			while ((ch = is.read(b, 0, 1024)) > 0) {
				System.out.println(111);
				System.out.write(b, 0, ch);

			}
			/* 将Response显示于Dialog */
			System.out.println("上传成功" + new String(b));
			/* 关闭DataOutputStream */
			ds.close();
			return  new String(b);
		} catch (Exception e) {
			System.out.println("上传失败" + e);
			return "error";
		}
	}
}
