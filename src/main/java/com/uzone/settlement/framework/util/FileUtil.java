package com.uzone.settlement.framework.util;

import java.io.File;
import java.io.FileOutputStream;

//@EnableAsync
//@Async
public class FileUtil {

	/* 单个上传 如果bo为真，则进行压缩处理 */
	public static void uploadFile(byte[] file, String filePath, String fileName, 
			Boolean bo, int height, int width) throws Exception {
		File targetFile = new File(filePath);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		FileOutputStream out = new FileOutputStream(filePath + fileName);
		out.write(file);
		out.flush();
		out.close();
//		if(bo) {	//进行4:3压缩处理 1024x768   图片存储后，做压缩处理 2018-5-12 添加此api
//			ImageThread it = new ImageThread(filePath, fileName, height, width);
//			it.start();
//			ImageUtil iu = new ImageUtil();
//			ImageUtil.thumbnailsImg(filePath, fileName, height, width);
//			Thumbnails.of(filePath + fileName).size(height, width).keepAspectRatio(false).toFile(filePath + fileName);
			//图片尺寸不变，压缩图片文件大小outputQuality实现,参数1为最高质量
//	        Thumbnails.of(filePath + fileName).scale(1f).outputQuality(0.1f).toFile(filePath + fileName);
//		}
	}
	
	/* 批量上传 
	public static void batchUploadFile(byte[] file, String filePath, String fileName) throws Exception {
		File targetFile = new File(filePath);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		FileOutputStream out = new FileOutputStream(filePath + fileName);
		BufferedOutputStream stream = new BufferedOutputStream(out);
		stream.write(file);
		stream.close();
	}
	*/
	/**
	 * 图片上传
	 * 
	 * @RequestMapping(value="/upload", method = RequestMethod.POST)
	 *                                  public @ResponseBody String upload(String
	 *                                  weibo,@RequestParam("file") MultipartFile
	 *                                  file, HttpServletRequest request) { try {
	 *                                  String filePath = "D:\\"; String fileName =
	 *                                  "遥远的理想乡.png";
	 *                                  FileUtil.uploadFile(file.getBytes(),
	 *                                  filePath, fileName); } catch (Exception e) {
	 * 
	 *                                  } return "微博：" + weibo; }
	 */

	/**
	 * 批量上传图片
	 * 
	 * @RequestMapping(value = "/batch/upload", method = RequestMethod.POST)
	 * @ResponseBody public String handleFileUpload(HttpServletRequest request) {
	 * 
	 *               MultipartHttpServletRequest params =
	 *               ((MultipartHttpServletRequest) request); List<MultipartFile>
	 *               files = ((MultipartHttpServletRequest)
	 *               request).getFiles("file"); String
	 *               name=params.getParameter("name"); String
	 *               id=params.getParameter("id"); System.out.println("name:"+name);
	 *               System.out.println("id:"+id);
	 *               System.out.println("files.size():"+files.size()); MultipartFile
	 *               file = null; for (int i = 0; i < files.size(); ++i) { file =
	 *               files.get(i); if (!file.isEmpty()) { if(files.size()<2) {
	 *               //如果上传单张图片 try { String filePath = "D:\\"; String fileName =
	 *               "遥远的理想乡.png"; FileUtil.uploadFile(file.getBytes(), filePath,
	 *               fileName); } catch (Exception e) { return "上传失败：" +
	 *               e.getMessage(); } } else { //如果上传多张图片 try { byte[] bytes =
	 *               file.getBytes(); String newFilePath = "D:\\"; String
	 *               newFileName = "遥远的理想乡" + i + ".png";
	 *               FileUtil.batchUploadFile(bytes, newFilePath, newFileName);
	 *               String fileName = file.getOriginalFilename(); String filePath =
	 *               request.getSession().getServletContext().getRealPath("imgupload/");
	 *               String contentType = file.getContentType(); } catch (Exception
	 *               e) { return "上传失败：" + i + " => " + e.getMessage(); } } } else {
	 *               return "上传失败：" + i + "为空"; } } return "upload successful"; }
	 */

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
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
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