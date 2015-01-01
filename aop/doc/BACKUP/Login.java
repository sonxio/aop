package com.ibm.song.soc.unit;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import com.ibm.issac.toolkit.DevLog;
import com.ibm.song.soc.connect.RequestRTC;

public class Login {
	
	public void login() throws ClientProtocolException, IOException{
		//生成请求对象
		HttpPost httpreq = new HttpPost("https://localhost:9443/ccm/whoami");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		NameValuePair name = new BasicNameValuePair("j_username", "user2");
		NameValuePair pass = new BasicNameValuePair("j_password", "user2");
		nvps.add(name);
		nvps.add(pass);
		UrlEncodedFormEntity reqFormEntity = new UrlEncodedFormEntity(nvps, "UTF-8");
		httpreq.setEntity(reqFormEntity);
		//发出请求
		RequestRTC r = new RequestRTC();
		String str = r.requestRTC(httpreq);
		DevLog.debug("str"+str);
		//----------------------
		HttpPost httpreq2 = new HttpPost("https://localhost:9443/ccm/whoami");
		String str2 = r.requestRTC(httpreq2);
		DevLog.debug("str2------"+str2);
	}
	
	public static void main(String[] args) throws ClientProtocolException, IOException{
		Login l = new Login();
		l.login();
	}
}
