package aop.connect;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import aop.connect.common.AbstractRTCConnect;

import com.ibm.issac.toolkit.DevLog;
import com.ibm.issac.toolkit.logging.Tlog;

/**
 * ������TCP���RTC�������ӣ�ִ��ͨѶ����<br/>
 * ����httpclientʱ��ʹ�ô���HttpContext��API��<br/>
 * 
 * <br/>
 * ����HttpContext��API������RTC��¼���������ﲻ�������ȡHTTP���صľ����������ݡ�
 * <br/>
 * 
 * @author issac
 *
 */
public class ReqRTCWithHC extends AbstractRTCConnect {
	/**
	 * ��POST��ʽ��������RTC����ȡRTC�ķ���<br/>
	 * 
	 * @param hg
	 * @param s
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public String reqByGET(HttpGet hg, RTCSession s) throws ClientProtocolException, IOException {
		if (hg == null || s == null) {
			DevLog.error("Error requesting GET RTC as one of requested params is null. hp: " + hg + ", s: " + s);
			return null;
		}
		if (hg.getURI() == null) {
			DevLog.error("Error requesting GET RTC as URI for the request is null. ");
			return null;
		}
		// ��ǰ�汾ÿ�ζ��ᴴ��HTTP CLIENT���������µ�HTTP����
		Tlog.p1("ReqRTCWithHC.reqByGET URI: " + hg.getURI().toString());
		final DefaultHttpClient httpClient = new DefaultHttpClient();
		httpClient.setCookieStore(s.getCookieStore());
		final HttpResponse rsp = httpClient.execute(hg, s.getHttpContext());
		this.parseCookies(s.getCookieStore().getCookies());
		String str = this.parseRsp(rsp, s);
		httpClient.getConnectionManager().shutdown();
		hg.abort();
		return str;
	}

	/**
	 * ��POST��ʽ��������RTC����ȡRTC�ķ���<br/>
	 * 
	 * @param hp
	 * @param s
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public String reqByPOST(HttpPost hp, RTCSession s) throws ClientProtocolException, IOException {
		if (hp == null || s == null) {
			DevLog.error("Error requesting POST RTC as one of requested params is null. hp: " + hp + ", s: " + s);
			return null;
		}
		if (hp.getURI() == null) {
			DevLog.error("Error requesting POST RTC as URI for the request is null. ");
			return null;
		}
		// ��ǰ�汾ÿ�ζ��ᴴ��HTTP CLIENT���������µ�HTTP����
		Tlog.p1("ReqRTCWithHC.reqByPOST URI: " + hp.getURI().toString());
		final DefaultHttpClient httpClient = new DefaultHttpClient();
		httpClient.setCookieStore(s.getCookieStore());
		final HttpResponse rsp = httpClient.execute(hp, s.getHttpContext());
		this.parseCookies(s.getCookieStore().getCookies());
		String str = this.parseRsp(rsp, s);
		hp.abort();
		httpClient.getConnectionManager().shutdown();
		return str;
	}
}
