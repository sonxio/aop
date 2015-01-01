package aop.connect;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import com.ibm.issac.toolkit.DevLog;
import com.ibm.issac.toolkit.param.SysProp;

/**
 * 创建、缓存、管理和RTC登录有关的各种变量
 * 
 * @author issac
 *
 */
public class RTCSession {
	private CookieStore cookieStore;
	private SSLContext sslContext;
	private HttpContext httpContext;

	public void init() throws NoSuchAlgorithmException, NoSuchProviderException, KeyStoreException, CertificateException, FileNotFoundException, IOException, KeyManagementException {
		// 初始化SSL配置，加载信任库
		sslContext = SSLContext.getInstance(SysProp.b_str("aop.rtc.sslProtocol", "TLSv1"));
		final TrustManagerFactory tmf = TrustManagerFactory.getInstance(SysProp.b_str("aop.rtc.sslAlgorithm", "IbmX509"), SysProp.b_str("song.oslc.rtc.sslProvider", "IBMJSSE2"));
		final KeyStore ts = KeyStore.getInstance(SysProp.b_str("aop.ssl.trustStore.type", "JKS"));
		ts.load(new FileInputStream(SysProp.b_str("aop.ssl.trustStore.file", "doc/soc_ts.jks")), SysProp.b_str("aop.ssl.trustStore.password", "111111").toCharArray());
		tmf.init(ts);		
		sslContext.init(null, tmf.getTrustManagers(), new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
		//使用当前SSL配置，创建HTTP CONTEXT
		this.cookieStore = new BasicCookieStore();
		this.httpContext = new BasicHttpContext();
		httpContext.setAttribute("http.cookie-store", cookieStore);
	}

	public CookieStore getCookieStore() {
		return cookieStore;
	}

	public SSLContext getSslContext() {
		return sslContext;
	}

	public HttpContext getHttpContext() {
		return httpContext;
	}
	
}
