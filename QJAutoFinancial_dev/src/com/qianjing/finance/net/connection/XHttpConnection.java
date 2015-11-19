
package com.qianjing.finance.net.connection;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.qianjing.finance.model.common.UserDevice;
import com.qianjing.finance.util.PrefUtil;
import com.qianjing.finance.util.helper.StringHelper;

/**
 * <p>XHttpConnection</p>
 * <p>Description: 处理Http/Https的网络请求</p>
 * @author fangyan
 * @date 2015年2月5日
 */
public class XHttpConnection {

	public static final String TAG = "XHttpConnection";

	private Context mContext;

	private String mStrCookies;

	public XHttpConnection(Context context) {
		mContext = context;
		initCookies(context);
	}

	private void initCookies(Context context) {

		mStrCookies = "bcookie=" + UserDevice.getIMEI(context);
		if (PrefUtil.hasCookies(mContext)) {
			mStrCookies += ("; ucookie=" + PrefUtil.getUCookie(mContext));
			mStrCookies += ("; tcookie=" + PrefUtil.getTCookie(mContext));
		}
	}

	private void initHttpsConnection() {

		XX509TrustManager mTrustManager = new XX509TrustManager();
		XHostnameVerifier mVerifier = new XHostnameVerifier();

		SSLContext sslContext = null;
		try {
			sslContext = SSLContext.getInstance("SSL"); //TLS/SSL
			X509TrustManager[] xtmArray = new X509TrustManager[] { mTrustManager };
			sslContext.init(null, xtmArray, new SecureRandom());
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		if (sslContext != null) {
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
		}
		HttpsURLConnection.setDefaultHostnameVerifier(mVerifier);
	}

	public void doGet(String strUrl, Hashtable<String, String> tableProperty, HttpCallBack callback) {

		try {
			URL url = new URL(strUrl);

			// 如果是HTTPS请求，对连接进行相应设置
			if (url.getProtocol().compareTo("https") == 0)
				initHttpsConnection();

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("GET");
			conn.setConnectTimeout(10 * 1000);
			conn.setReadTimeout(5 * 1000);
			conn.setDoInput(true);
			conn.setUseCaches(false);

			conn.setRequestProperty("Host", url.getHost());
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Accept-Encoding", "identity");

			// 添加自定义消息头
			if (!setSpecialProperty(conn, tableProperty)) {
				// 如果添加自定义消息头失败，则返回参数错误
				if (callback != null)
					callback.back(null, HttpConst.STATUS_ERROR_PARAM);
				return;
			}

			InputStream inStream = conn.getInputStream();

			byte[] bytes = StreamUtil.readStream(inStream);
			String strData = new String(bytes);

			if (callback != null)
				callback.back(strData, HttpConst.STATUS_OK);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			if (callback != null)
				callback.back(null, HttpConst.STATUS_EXCEPTION_MALFORMED_URL);
		} catch (IOException e) {
			e.printStackTrace();
			if (callback != null)
				callback.back(null, HttpConst.STATUS_EXCEPTION_IO);
		}

	}

	public void doPost(String strUrl, Hashtable<String, Object> tableEntity, Hashtable<String, String> tableProperty, HttpCallBack callback) {

		try {

			URL url = new URL(strUrl);

			// 如果是HTTPS请求，对连接实例进行SSL加密设置
			if (url.getProtocol().compareTo("https") == 0)
				initHttpsConnection();

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			// 设定请求的方法为"POST"，默认是GET 
			conn.setRequestMethod("POST");
			// 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在http正文内，因此需要设为true, 默认情况下是false; 
			conn.setDoOutput(true);
			// 设置是否从httpUrlConnection读入，默认情况下是true; 
			conn.setDoInput(true);
			// Post请求不能使用缓存 
			conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(true);
			conn.setConnectTimeout(30 * 1000);
			conn.setReadTimeout(15 * 1000);

			// 添加通用消息头
			conn.setRequestProperty("Host", url.getHost());
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Accept-Encoding", "identity");
			conn.setRequestProperty("Cookie", mStrCookies);

			// 添加自定义消息头
			if (!setSpecialProperty(conn, tableProperty)) {
				// 如果添加自定义消息头失败，则返回参数错误
				if (callback != null)
					callback.back(null, HttpConst.STATUS_ERROR_PARAM);
				return;
			}

			// 如果没有Content数据，则只进行connect操作
			if (tableEntity != null && !tableEntity.isEmpty()) {
				String strEntity = getStrEntity(tableEntity);
				if (strEntity != null) {
					// 转为字节数组
					byte[] bytes = strEntity.getBytes();

					conn.setRequestProperty("Content-Length", String.valueOf(bytes.length));
					conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

					// 此处getOutputStream会隐含的进行connect(即如同调用connect()方法
					// outputStream不是一个网络流，充其量是个字符串流，往里面写入的东西不会立即发送到网络，
					// 而是存在于内存缓冲区中，待outputStream流关闭时，根据输入的内容生成http正文
					// 至此，http请求的东西已经全部准备就绪。在getInputStream()函数调用的时候，就会把准备好的http请求 
					// 正式发送到服务器了，然后返回一个输入流，用于读取服务器对于此次http请求的返回信息
					OutputStream os = conn.getOutputStream();
					DataOutputStream dos = new DataOutputStream(os);

					dos.write(bytes);

					dos.flush();
					dos.close();
					os.flush();
					os.close();
				} else {
					// 如果实体数据拼装结果有误，则返回参数错误
					if (callback != null)
						callback.back(null, HttpConst.STATUS_ERROR_PARAM);
					return;
				}
			} else {
				// connect()函数实际上只是建立了一个与服务器的tcp连接，并没有实际发送http请求
				conn.connect();
			}

			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				// getInputStream()函数将内存缓冲区中封装好的完整的HTTP请求报文发送到服务端，此次Http请求结束
				InputStream is = conn.getInputStream();
				byte[] bytes = StreamUtil.readStream(is);
				String strData = new String(bytes);

				if (callback != null)
					callback.back(strData, HttpConst.STATUS_OK);

				// 登录接口会返回两个cookie，一般只返回tcookie，但有些接口没有cookie返回
				List<HttpCookie> listCookie = getHttpCookies(conn);
				if (listCookie != null) {
					// 保存cookie
					if (!saveHttpCookies(listCookie)) {
						// 如果保存失败则返回通用错误码
						callback.back(null, HttpConst.STATUS_ERROR_COMMON);
						return;
					}
				}
			} else {
				// 如果状态码不是200，则抛出异常
				throw new IllegalResponseStateException("response code is " + conn.getResponseCode() + ", " + strUrl);
			}

			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			if (callback != null)
				callback.back(null, HttpConst.STATUS_EXCEPTION_MALFORMED_URL);
		} catch (IOException e) {
			e.printStackTrace();
			if (callback != null)
				callback.back(null, HttpConst.STATUS_EXCEPTION_IO);
		} catch (IllegalResponseStateException e) {
			e.printStackTrace();
			if (callback != null)
				callback.back(null, HttpConst.STATUS_EXCEPTION_ILLEGAL_RESPONSE_STATE);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static boolean setSpecialProperty(HttpURLConnection conn, Hashtable<String, String> tableProperty) {

		if (tableProperty != null && !tableProperty.isEmpty()) {
			Iterator keyValuePairs = tableProperty.entrySet().iterator();
			for (int i = 0; i < tableProperty.size(); i++) {
				Map.Entry<String, String> entry = (Map.Entry) keyValuePairs.next();
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}
			return true;
		}
		return false;
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private boolean saveHttpCookies(List<HttpCookie> listCookie) {
		if (listCookie != null && !listCookie.isEmpty()) {
			for (HttpCookie cookie : listCookie) {
				if (TextUtils.equals("ucookie", cookie.getName())) {
					// 如果保存失败则返回false
					if (!PrefUtil.setUCookie(mContext, cookie.getValue()))
						return false;
				} else if (TextUtils.equals("tcookie", cookie.getName())) {
					if (!PrefUtil.setTCookie(mContext, cookie.getValue()))
						return false;
				}
			}
			// 都保存成功则返回true
			return true;
		}
		return false;
	}

	/**
	 * <p>getHttpCookies</p>
	 * <p>Description: 从HttpsURLconnection实例中获取返回的Cookie</p>
	 * @param conn
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private static List<HttpCookie> getHttpCookies(HttpURLConnection conn) {

		Map<String, List<String>> map = conn.getHeaderFields();
		if (map != null && !map.isEmpty()) {
			List<String> listStrCookie = map.get("Set-Cookie");
			if (listStrCookie != null && !listStrCookie.isEmpty()) {
				List<HttpCookie> listCookie = new ArrayList<HttpCookie>();
				for (String strCookie : listStrCookie) {
					List<HttpCookie> listTempCookie = HttpCookie.parse(strCookie);
					for (HttpCookie cookie : listTempCookie) {
						HttpCookie tempCookie = new HttpCookie(cookie.getName(), cookie.getValue());
						listCookie.add(tempCookie);
					}
				}
				return listCookie;
			}
		}
		return null;
	}

	/**
	 * <p>getStrEntity</p>
	 * <p>Description: 拼接POST请求实体</p>
	 * @param mapEntity
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings("rawtypes")
	private String getStrEntity(Hashtable<String, Object> mapEntity) throws UnsupportedEncodingException {

		if (mapEntity != null && !mapEntity.isEmpty()) {
			String strEntity = new String();
			Iterator keyValuePairs = mapEntity.entrySet().iterator();
			for (int i = 0; i < mapEntity.size(); i++) {
				Map.Entry entry = (Map.Entry) keyValuePairs.next();
				if (entry.getValue() instanceof String) {
					String strValue = (String) entry.getValue();
					// encode the paramter if it has chines char
					if (StringHelper.isChinese(strValue))
						strValue = URLEncoder.encode(strValue, "utf-8");
					strEntity += (entry.getKey() + "=" + strValue + "&");
				} else {
					strEntity += (entry.getKey() + "=" + entry.getValue() + "&");
				}
			}
			if (strEntity.length() > 0 && strEntity.endsWith("&"))
				strEntity = strEntity.substring(0, strEntity.length() - 1);
			return strEntity;
		}
		return null;
	}

}
