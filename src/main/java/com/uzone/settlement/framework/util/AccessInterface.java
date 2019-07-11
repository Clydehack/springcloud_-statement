package com.uzone.settlement.framework.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.http.util.TextUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by anmuxi on 2018-11-21.
 * 
 * @author anmuxi
 * @date 2018.11.21
 */
public class AccessInterface {

	// private static final String APPLICATION_JSON = "application/json";

	// private static final String CONTENT_TYPE_TEXT_JSON = "text/json";

	/**
	 * 发送JSON字符串 如果成功则返回成功标识。
	 * 
	 * @param urlPath
	 * @param json
	 * @return
	 */
	public static String doJsonPost(String urlPath, String json) {
		// HttpClient 6.0被抛弃了
		String result = "";
		BufferedReader reader = null;
		try {
			URL url = new URL(urlPath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			// 设置文件类型:
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			// 设置接收类型否则返回415错误
			// conn.setRequestProperty("accept","*/*")此处为暴力方法设置接受所有类型，以此来防范返回415;
			conn.setRequestProperty("accept", "application/json");
			// 往服务器里面发送数据
			if (json != null && !TextUtils.isEmpty(json)) {
				byte[] writebytes = json.getBytes();
				// 设置文件长度
				conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
				OutputStream outwritestream = conn.getOutputStream();
				outwritestream.write(json.getBytes());
				outwritestream.flush();
				outwritestream.close();
			}
			if (conn.getResponseCode() == 200) {
				reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				result = reader.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * 发送post请求
	 * 
	 * @param url
	 * @param parameters
	 * @return
	 */
	public static String sendPost(String url, Map<String, String> parameters) {
		String result = "";
		BufferedReader in = null;
		PrintWriter out = null;
		StringBuffer sb = new StringBuffer();
		String params = "";
		try {
			// 拼凑参数
			if (parameters != null && parameters.size() != 0) {
				for (String name : parameters.keySet()) {
					sb.append(name).append("=").append(URLEncoder.encode(parameters.get(name), "UTF-8")).append("&");
				}
				String tempParams = sb.toString();
				params = tempParams.substring(0, tempParams.length() - 1);
			}
			URL connURL = new URL(url);
			// 创建http请求连接
			HttpURLConnection httpConn = (HttpURLConnection) connURL.openConnection();
			// 设置请求的属性
			httpConn.setRequestProperty("Accept", "*/*");
			httpConn.setRequestProperty("Connection", "Keep-Alive");
			httpConn.setRequestProperty("VertexUser-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
			// MediaType.parse("application/x-www-form-urlencoded; charset=utf-8")
			// httpConn.addRequestProperty("content-type","application/json;charset=UTF-8");

			// 设置是否向connection输出，因为这个是post请求，参数要放在http正文内，因此需要设为true
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setConnectTimeout(50000);
			httpConn.setReadTimeout(50000);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(httpConn.getOutputStream());
			// 发送请求参数
			out.write(params);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			result = "{}";
			e.printStackTrace();
		} finally {
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

	/**
	 * 发送get请求
	 * 
	 * @param url
	 * @param parameters
	 * @return
	 */
	public static String sendGet(String url, Map<String, String> parameters) {
		String result = "";
		BufferedReader in = null;
		StringBuffer sb = new StringBuffer();
		String params = "";
		String fullUrl = "";
		try {
			// 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码
			if (parameters != null && parameters.size() != 0) {
				for (String name : parameters.keySet()) {
					sb.append(name).append("=").append(URLEncoder.encode(parameters.get(name), "UTF-8")).append("&");
				}
				String tempParams = sb.toString();
				params = tempParams.substring(0, tempParams.length() - 1);
				fullUrl = url + "?" + params;
			} else {
				fullUrl = url;
			}
			URL connURL = new URL(fullUrl);
			// 根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型，
			// 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
			HttpURLConnection httpConn = (HttpURLConnection) connURL.openConnection();
			// 设置请求属性
			httpConn.setRequestProperty("Accept", "*/*");
			httpConn.setRequestProperty("Connection", "Keep-Alive");
			httpConn.setRequestProperty("VertexUser-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
			httpConn.setConnectTimeout(5000);
			httpConn.setReadTimeout(5000);
			try {
				// 进行连接，但是实际上get request要在下一句的connection.getInputStream()函数中才会真正发到服务器
				httpConn.connect();
			} catch (SocketTimeoutException e) {
				return result;
			}
			in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * @author anmuxi
	 * @description 执行从cookie获取会话sessionID的方法，用于保持与服务器的会话
	 * @param actionURL
	 *            远程服务器的URL
	 * @return sessionId
	 */
	public static String getSessionID(String actionURL) {
		String sessionID;
		try {
			URL url = new URL(actionURL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			String cookieValue = connection.getHeaderField("set-cookie");
			if (cookieValue != null) {
				sessionID = cookieValue.substring(0, cookieValue.indexOf(";"));
			} else {
				sessionID = "";
			}
		} catch (IOException e) {
			e.printStackTrace();
			sessionID = "";
		}
		return sessionID;
	}

	/**
	 * json数组转List
	 * 
	 * @param arrayS
	 * @return
	 */
	public static List<Map<String, Object>> jsonArrayList(String arrayS) {
		if (arrayS == null || Objects.equals("", arrayS)) {
			return null;
		}
		JSONArray array = JSONArray.fromObject(arrayS);
		List<Map<String, Object>> list = new ArrayList<>();
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> iterator = array.iterator();
		while (iterator.hasNext()) {
			JSONObject json = iterator.next();
			list.add(jsonObjectMap(String.valueOf(json)));
		}
		return list;
	}

	/**
	 * json对象转map
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Map<String, Object> jsonObjectMap(String jsonString) {
		if (jsonString == null || "".equals(jsonString)) {
			return null;
		}
		JSONObject json = JSONObject.fromObject(jsonString);
		Map<String, Object> map = new HashMap<String, Object>(6);
		@SuppressWarnings("unchecked")
		Iterator<String> iterator = json.keys();
		while (iterator.hasNext()) {
			String key = iterator.next();
			// 判断是否嵌套了一个json对象
			try {
				JSONObject.fromObject(json.get(key));
				map.put(key, jsonObjectMap(json.getString(key)));
				continue;
			} catch (Exception e) {
				// 不是json对象，继续
			}
			// 判断是否嵌套了一个json数组
			try {
				JSONArray.fromObject(json.get(key));
				map.put(key, jsonArrayList(json.getString(key)));
				continue;
			} catch (Exception e) {
				// 不是json数组，继续
			}
			map.put(key, json.get(key));
		}
		return map;
	}

	/**
	 * 将 List<Map>对象转化为List<JavaBean>
	 * 
	 * @return Object对象
	 */
	@SuppressWarnings("rawtypes")
	public static <T> List<T> convertListMap2ListBean(List<Map<String, Object>> listMap, Class aClass)
			throws Exception {
		List<T> beanList = new ArrayList<T>();
		for (int i = 0, n = listMap.size(); i < n; i++) {
			Map<String, Object> map = listMap.get(i);
			T bean = convertMap2Bean(map, aClass);
			beanList.add(bean);
		}
		return beanList;
	}

	/**
	 * 将 Map对象转化为JavaBean
	 * 
	 * @return Object对象
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T convertMap2Bean(Map map, Class aClass) throws Exception {
		if (map == null || map.size() == 0) {
			return null;
		}
		BeanInfo beanInfo = Introspector.getBeanInfo(aClass);
		T bean = (T) aClass.newInstance();
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0, n = propertyDescriptors.length; i < n; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			if (map.containsKey(propertyName)) {
				Object value = map.get(propertyName);
				try {
					BeanUtils.copyProperty(bean, propertyName, value);
				} catch (Exception e) {

				}
			}
		}
		return bean;
	}

}
