package aop.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import aop.connect.RTCSession;
import aop.connect.ReqRTCWithHC;
import aop.connect.ReqRTCWithoutHC;
import aop.service.common.AbstractAOPService;

import com.ibm.issac.toolkit.DevLog;

/**
 * 登录RTC，获得登录COOKIE
 * 
 * @author issac
 *
 */
public class Owhoami extends AbstractAOPService {
	public void whoami(RTCSession s) throws ClientProtocolException, IOException {
		// 生成请求对象
		final HttpGet req = new HttpGet(this.gReqURI("/whoami"));
		req.addHeader("Accept", "application/json");
		req.addHeader("OSLC-Core-Version", "2.0");
		// 发起请求
		final ReqRTCWithoutHC r = new ReqRTCWithoutHC();
		String str = r.reqByGET(req, s);
		this.printRTCResponse(str);
	}

	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, KeyStoreException, CertificateException, FileNotFoundException, IOException {
		final RTCSession s = new RTCSession();
		s.init();
		// 登录
		final Oidentity i = new Oidentity();
		i.identity(s);
		// 发出业务请求
		final Owhoami w = new Owhoami();
		w.whoami(s);
	}
}
