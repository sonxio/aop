package com.ibm.song.oslc.unit;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import com.ibm.issac.toolkit.DevLog;
import com.ibm.issac.toolkit.app.AbstractIREApp;
import com.ibm.issac.toolkit.param.SysProp;

/**
 * 用HTTPS连接到RTC
 * 
 * @author issac
 *
 */
public class ConnectToRTC extends AbstractIREApp {
	public static void main(String[] args) throws ClientProtocolException, IOException, KeyManagementException, NoSuchAlgorithmException {
		final ConnectToRTC c = new ConnectToRTC();
		c.process();
	}

	private void process() throws NoSuchAlgorithmException, KeyManagementException, ClientProtocolException, IOException {
		// 打印测试参数
		SysProp.pstr("javax.net.ssl.keyStore");
		SysProp.pstr("javax.net.ssl.keyStorePassword");
		SysProp.pstr("com.ibm.ssl.trustStoreType");
		SysProp.pstr("javax.net.ssl.trustStore");
		SysProp.pstr("javax.net.ssl.trustStorePassword");
		// 验证密码
		final CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		final HttpContext httpContext = new BasicHttpContext();
		List<NameValuePair> authFormParams = new ArrayList<NameValuePair>();
		authFormParams.add(new BasicNameValuePair("j_username", "user2"));
		authFormParams.add(new BasicNameValuePair("j_password", "user211"));
		UrlEncodedFormEntity encodedentity = new UrlEncodedFormEntity(authFormParams, "UTF-8");
		HttpPost httpPost = new HttpPost("https://localhost:9443/ccm/whoami");
		httpPost.setEntity(encodedentity);
		httpPost.addHeader("Accept", "application/json");
		httpPost.addHeader("OSLC-Core-Version", "2.0");
		HttpResponse response = httpClient.execute(httpPost, httpContext);
		Header[] ooo = response.getAllHeaders();
		for (Header header : ooo) {
			DevLog.debug(header.getName() + " - " + header.getValue());
		}
		HttpEntity entity = response.getEntity();
		DevLog.debug(entity + "");
		if (entity != null) {
			BufferedReader reader;
			try {
				reader = new BufferedReader(new InputStreamReader(entity.getContent()));
				String line = reader.readLine();
				while (line != null) {
					System.out.println(line);
					line = reader.readLine();
				}
				reader.close();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
