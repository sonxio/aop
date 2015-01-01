package aop.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

import net.sf.json.JSONObject;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import aop.common.JSONFormat;
import aop.connect.RTCSession;
import aop.connect.ReqRTCWithoutHC;
import aop.service.common.AbstractAOPService;

import com.ibm.issac.toolkit.DevLog;

/**
 * ��¼RTC����õ�¼COOKIE
 * 
 * @author issac
 *
 */
public class Oresource_wi extends AbstractAOPService {
	public void whoami(RTCSession s) throws ClientProtocolException, IOException {
		// �����������
		final HttpGet req = new HttpGet(this.gReqURI("/resource/itemName/com.ibm.team.workitem.WorkItem/1"));
		req.addHeader("Accept", "application/json");
		req.addHeader("OSLC-Core-Version", "2.0");
		// ��������
		final ReqRTCWithoutHC r = new ReqRTCWithoutHC();
		String str = r.reqByGET(req, s);
		this.printRTCResponse(str);
		// ��������
		JSONObject j = JSONObject.fromObject(str);
		JSONFormat f = new JSONFormat();
		DevLog.debug("parsed json obj: " + f.formatJson(j.toString()));
	}

	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, KeyStoreException, CertificateException, FileNotFoundException, IOException {
		final RTCSession s = new RTCSession();
		s.init();
		// ��¼
		final Oidentity i = new Oidentity();
		i.identity(s);
		// ����ҵ������
		final Oresource_wi w = new Oresource_wi();
		w.whoami(s);
	}
}
