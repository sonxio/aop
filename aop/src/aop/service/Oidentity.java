package aop.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;

import aop.connect.RTCSession;
import aop.connect.ReqRTCWithHC;
import aop.service.common.AbstractAOPService;

/**
 * ��¼RTC����õ�¼COOKIE
 * 
 * @author issac
 *
 */
public class Oidentity extends AbstractAOPService {
	public void identity(RTCSession s) throws ClientProtocolException, IOException {
		//��¼����1����ȡID
		// �����������		
		final HttpGet reqGetId = new HttpGet(this.gReqURI("/authenticated/identity"));
		// ��������
		final ReqRTCWithHC r = new ReqRTCWithHC();
		r.reqByGET(reqGetId, s);
		//-------------------------------
		//��¼����2����֤����
		final List<NameValuePair> authFormParams = new ArrayList<NameValuePair>();
		authFormParams.add(new BasicNameValuePair("j_username", "user2"));
		authFormParams.add(new BasicNameValuePair("j_password", "user2"));
		// Listing 2. Build an UrlEncodedFormEntity
		UrlEncodedFormEntity encodedentity = new UrlEncodedFormEntity(authFormParams, "UTF-8");
		final HttpPost httpPostAuth = new HttpPost(this.gReqURI("/authenticated/j_security_check"));
		httpPostAuth.setEntity(encodedentity);
		r.reqByPOST(httpPostAuth, s);
	}

	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, KeyStoreException, CertificateException, FileNotFoundException, IOException {
		final RTCSession s = new RTCSession();
		s.init();
		final Oidentity l = new Oidentity();
		l.identity(s);
	}
}
