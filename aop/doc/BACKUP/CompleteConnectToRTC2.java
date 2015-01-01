package com.ibm.song.soc.unit;

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
import org.apache.http.protocol.HTTP;
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
public class CompleteConnectToRTC2 extends AbstractIREApp {
	public static void main(String[] args) throws ClientProtocolException, IOException, KeyManagementException, NoSuchAlgorithmException {
		final CompleteConnectToRTC2 c = new CompleteConnectToRTC2();
		c.process();
	}

	private void process() throws NoSuchAlgorithmException, KeyManagementException, ClientProtocolException, IOException {
		// Setup the HttClient
		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		// Disabling SSL Certificate Validation
		// HttpUtils.setupLazySSLSupport(httpclient);
		// Setup the HTTP GET method
		HttpPost httpreq = new HttpPost("https://localhost:9443/ccm/process-about");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		NameValuePair name = new BasicNameValuePair("j_username", "user2");
		NameValuePair pass = new BasicNameValuePair("j_password", "user2");
		nvps.add(name);
		nvps.add(pass);
		UrlEncodedFormEntity reqFormEntity = new UrlEncodedFormEntity(nvps, "UTF-8");
		httpreq.setEntity(reqFormEntity);
		HttpResponse response;
		try {
			// Execute the request
			response = httpclient.execute(httpreq);
			System.out.println(">> HTTP Status code: " + response.getStatusLine());
			if (response.getStatusLine().getStatusCode() == 200) {
				System.out.println(">> HTTP Response Headers: ");
				Header[] ooo = response.getAllHeaders();
				for (Header header : ooo) {
					DevLog.debug(header.getName() + " - " + header.getValue());
				}
				System.out.println(">> HTTP Response Body: ");
				HttpEntity entity = response.getEntity();
				DevLog.debug(entity + "");
				if (entity != null) {
					BufferedReader reader;
					try {
						reader = new BufferedReader(new InputStreamReader(entity.getContent()));
						String line = reader.readLine();
						while (line != null) {
							System.out.println("--->"+line);
							line = reader.readLine();
						}
						reader.close();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else {
				// Release allocated resources
				response.getEntity().consumeContent();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Shutdown the HTTP connection
			httpclient.getConnectionManager().shutdown();
		}
	}
}
