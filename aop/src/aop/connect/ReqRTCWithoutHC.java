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
 * 用于在TCP层和RTC建立连接，执行通讯操作<br/>
 * 调用httpclient时，使用<b>不</b>带有HttpContext的API。<br/>
 * 
 * <br/>
 * 适用于读取RTC中的数据，在首次通讯后，会再次通讯来获取HTTP的具体数据内容。
 * <br/>
 * @author issac
 *
 */
public class ReqRTCWithoutHC extends AbstractRTCConnect{
	/**
	 * 用POST方式发送请求到RTC，读取RTC的反馈<br/>
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
		// 当前版本每次都会创建HTTP CLIENT，即创建新的HTTP连接
		DevLog.trace(">>>RequestRTC GET URI: " + hg.getURI().toString());
		final DefaultHttpClient httpClient = new DefaultHttpClient();
		httpClient.setCookieStore(s.getCookieStore());
		//发出请求，获得HEADER, ENTITY
		final HttpResponse rsp = httpClient.execute(hg);
		this.parseCookies(s.getCookieStore().getCookies());
		this.parseRsp(rsp, s);
		//再次发出请求，获得HTTP报文
		final ResponseHandler<String> rspH = new BasicResponseHandler();
		String str = httpClient.execute(hg, rspH);
		hg.abort();
		httpClient.getConnectionManager().shutdown();
		return str;
	}

	/**
	 * 用POST方式发送请求到RTC，读取RTC的反馈<br/>
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
		//当前版本每次都会创建HTTP CLIENT，即创建新的HTTP连接
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
