package aop.connect.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.cookie.Cookie;

import aop.connect.RTCSession;

import com.ibm.issac.toolkit.DevLog;
import com.ibm.issac.toolkit.logging.Tlog;

public class AbstractRTCConnect {
	protected void parseCookies(List<Cookie> cookieL) {
		Tlog.enter2("printCookies");
		for (Cookie cookie : cookieL) {
			DevLog.trace("\trsp cookie: " + cookie.getName() + " : " + cookie.getValue());
		}
		Tlog.exit2("printCookies");
	}

	protected String parseRsp(final HttpResponse rsp, RTCSession s) {
		this.parseHeader(rsp);
		this.parseEntity(rsp);
		return null;
	}

	private void parseEntity(final HttpResponse response) {
		Tlog.enter2("parseEntity");
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			BufferedReader reader;
			try {
				reader = new BufferedReader(new InputStreamReader(entity.getContent()));
				String line = reader.readLine();
				if (line == null)
					return;
				StringBuffer sb = new StringBuffer(line);
				while (line != null) {
					DevLog.trace("\trsp entity: " + line);
					line = reader.readLine();
					sb.append(line).append('\n');
				}
				reader.close();
				Tlog.exit2("parseEntity");
				return;
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		DevLog.debug("Entity returned is null.");
		Tlog.exit2("parseEntity");
	}

	private void parseHeader(final HttpResponse response) {
		Tlog.enter2("parseHeader");
		final Header[] hA = response.getAllHeaders();
		for (final Header h : hA) {
			DevLog.trace("\trsp header: " + h.getName() + " - " + h.getValue());
		}
		Tlog.exit2("parseHeader");
	}
}
