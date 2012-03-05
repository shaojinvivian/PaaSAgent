package org.seforge.paas.agent.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class ServletMonitorFilter implements Filter {

	public void destroy() {
		// TODO Auto-generated method stub		
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		String monitorUrl = "http://192.168.4.191:8080/PaaSMonitor/";
//		String monitorUrl = "http://monitor.seforge.org:8080/"
		if (monitorUrl != null) {
			/*
			HttpClient httpclient = new DefaultHttpClient();
			try {
				String strURL = monitorUrl + "/appservers";
				HttpPost httppost = new HttpPost(strURL);
				String content = getUsageInfo(request);
				System.out.println(content);
				StringEntity requestEntity = new StringEntity(
						content, "UTF-8");

				httppost.setEntity(requestEntity);
				httppost.setHeader("Accept", "application/json");

				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				String responseBody = httpclient.execute(httppost,
						responseHandler);
			} catch (Exception e) {
				System.out.println("Adding appserver failed!");
				e.printStackTrace();
			} finally {
				// When HttpClient instance is no longer needed,
				// shut down the connection manager to ensure
				// immediate deallocation of all system resources
				httpclient.getConnectionManager().shutdown();
			}
			*/
			System.out.println("Ok,filtered");
		} else {
			System.out.println("Cannot get monitor url!");
		}
		
		
		
	}

	public void init(FilterConfig config) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	
	public String getUsageInfo(ServletRequest request){
		StringBuilder info = new StringBuilder();
		info.append(request.getRemoteAddr());		
		return info.toString();	
	}
}
