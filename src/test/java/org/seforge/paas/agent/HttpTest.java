package org.seforge.paas.agent;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import javax.management.ObjectName;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;


public class HttpTest {

	public static void main(String[] args) {
		HttpClient httpclient = new DefaultHttpClient();
		try {
			String strURL = "http://localhost:8080/PaaSMonitor/appservers";
			HttpPost httppost = new HttpPost(strURL);
			String content = HttpTest.getAppServerInfo();
			StringEntity requestEntity = new StringEntity(content, "UTF-8");
			
			httppost.setEntity(requestEntity);
			httppost.setHeader("Accept", "application/json");
			
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = httpclient.execute(httppost, responseHandler);
			System.out.println(responseBody);
		} catch (Exception e) {			
			e.printStackTrace();		
		} finally {			
			httpclient.getConnectionManager().shutdown();
		}

	}
	
	public static String getAppServerInfo() throws Exception{
//		String port = System.getProperty("com.sun.management.jmxremote.port");	
		ObjectName objectName = new ObjectName("java.lang:type=Runtime");
		String name = (String)ManagementFactory.getPlatformMBeanServer().getAttribute(objectName, "VmVendor");

		
		
		String port = "8999";
		InetAddress local;		
			local = InetAddress.getLocalHost();
			String ip = local.getHostAddress();
			StringBuilder sb = new StringBuilder();
			sb.append("{'name':'");
			sb.append(name);
			sb.append("','ip':'");
			sb.append(ip);
			sb.append("','jmxPort':");
			sb.append(port);
			sb.append("}");
			return sb.toString();
			
			
			
		
	}
}
