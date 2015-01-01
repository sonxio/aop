package aop.service.common;

import com.ibm.issac.toolkit.DevLog;
import com.ibm.issac.toolkit.param.SysProp;

public abstract class AbstractAOPService {
	/**
	 * 返回RTC URI
	 */
	protected String gRTCURI(){
		return SysProp.b_str("aop.rtc.uri", "https://localhost:9443/ccm");
	}
	
	/**
	 * 返回带有指定后缀的RTC URI
	 * @param surfix
	 * @return
	 */
	protected String gReqURI(String surfix){
		final StringBuffer sb = new StringBuffer(this.gRTCURI());
		sb.append(surfix);
		return sb.toString();
	}
	
	protected void printRTCResponse(String str){
		DevLog.trace("RTC RESPONSE: "+str);
	}
}
