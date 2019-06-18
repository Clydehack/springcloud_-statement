package com.uzone.settlement.framework.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

//@EnableAsync
//@Async
public class FileUtil {
	/**
	 * 单个文件上传
	 * @param file			文件数据
	 * @param filePath		文件存储路径
	 * @param fileName		文件存储名称
	 * @throws Exception
	 */
	public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
		File targetFile = new File(filePath);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		FileOutputStream out = new FileOutputStream(filePath + fileName);
		out.write(file);
		out.flush();
		out.close();
	}
	
	/**
	 * 从远端下载文件
	 */
	public static String GetHttpFile(String fileUrl) throws Exception {
		URL url = new URL(fileUrl);
		URLConnection connection = url.openConnection(); 	// openConnection()
		InputStream in = connection.getInputStream(); 		// getInputStream() 获取输入流
		
		InputStreamReader isr = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(isr); 		// 包装完成
		
		String line;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) { 			// 如果readLine()还在读取数据
			sb.append(line); 								// 将数据添加到sb
		}
		br.close();
		isr.close();
		in.close();
		
		return sb.toString();
	}

	// 删除指定文件夹下的所有文件
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);	// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);	// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	// 删除文件夹
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}