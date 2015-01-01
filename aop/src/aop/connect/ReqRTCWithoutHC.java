package aop.connect;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import aop.connect.common.AbstractRTCConnect;

import com.ibm.issac.toolkit.DevLog;

/**
 * ������TCP���RTC�������ӣ�ִ��ͨѶ����<br/>
 * ����httpclientʱ��ʹ��<b>��</b>����HttpContext��API��<br/>
 * 
 * <br/>
 * �����ڶ�ȡRTC�е����ݣ����״�ͨѶ�󣬻��ٴ�ͨѶ����ȡHTTP�ľ����������ݡ�
 * <br/>
 * @author issac
 *
 */
public class ReqRTCWithoutHC extends AbstractRTCConnect{
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
		DevLog.trace(">>>RequestRTC GET URI: " + hg.getURI().toString());
		final DefaultHttpClient httpClient = new DefaultHttpClient();
		httpClient.setCookieStore(s.getCookieStore());
		//�������󣬻��HEADER, ENTITY
		final HttpResponse rsp = httpClient.execute(hg);
		this.parseCookies(s.getCookieStore().getCookies());
		this.parseRsp(rsp, s);
		//�ٴη������󣬻��HTTP����
		final ResponseHandler<String> rspH = new BasicResponseHandler();
		String str = httpClient.execute(hg, rspH);
		hg.abort();
		httpClient.getConnectionManager().shutdown();
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
		if(hp==null || s ==null){
			DevLog.error("Error requesting POST RTC as one of requested params is null. hp: "+hp+", s: "+s);
			return null;
		}
		if(hp.getURI()==null){
			DevLog.error("Error requesting POST RTC as URI for the request is null. ");
			return null;
		}
		//��ǰ�汾ÿ�ζ��ᴴ��HTTP CLIENT���������µ�HTTP����
		DevLog.trace(">>>RequestRTC POST URI: "+hp.getURI().toString());
		final DefaultHttpClient httpClient = new DefaultHttpClient();
		httpClient.setCookieStore(s.getCookieStore());
		final ResponseHandler<String> rspH = new BasicResponseHandler();
		String str = httpClient.execute(hp, rspH);
		hp.abort();
		httpClient.getConnectionManager().shutdown();
		return str;
	}


}
