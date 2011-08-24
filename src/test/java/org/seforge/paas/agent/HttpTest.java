package org.seforge.paas.agent;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpTest {

	public static void main(String[] args) {
		HttpClient httpclient = new DefaultHttpClient();
		try {
			String strURL = "http://localhost:8080/PaaSMonitor/appservers";
			HttpPost httppost = new HttpPost(strURL);
			String content = "{'name':'appserver','ip':'127.0.0.2','jmxPort':8999}";
			StringEntity requestEntity = new StringEntity(content, "UTF-8");
			
			httppost.setEntity(requestEntity);
			httppost.setHeader("Accept", "application/json");
			
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = httpclient.execute(httppost, responseHandler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpclient.getConnectionManager().shutdown();
		}

	}
}
