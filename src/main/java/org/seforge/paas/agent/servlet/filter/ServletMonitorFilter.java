package org.seforge.paas.agent.servlet.filter;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.seforge.paas.agent.utils.Util;

public class ServletMonitorFilter implements Filter {
	FilterConfig config;
	ServletContext context;

	public void destroy() {
		this.config = null;	
	}	

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String webappFolder = (new File(context.getRealPath("/"))).getParent(); 
		String monitorPropPath = webappFolder + "\\PaaSAgentWeb\\WEB-INF\\monitor.properties";	
		
		Properties props =  Util.retrieveProperties(new File(monitorPropPath));
		String monitorUrl = props.getProperty("url");
		if (monitorUrl != null) {
			
			HttpClient httpclient = new DefaultHttpClient();
			try {
				String strURL = monitorUrl + "/servletmonitor/addhit";			
				HttpPost httppost = new HttpPost(strURL);	
				List<NameValuePair> params = generatePostParams(request, props);								
				
				httppost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
				httppost.setHeader("Accept", "application/json");
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				String responseBody = httpclient.execute(httppost,
						responseHandler);
			} catch (Exception e) {
				System.out.println("Adding hit failed!");
				e.printStackTrace();
			} finally {
				// When HttpClient instance is no longer needed,
				// shut down the connection manager to ensure
				// immediate deallocation of all system resources
				httpclient.getConnectionManager().shutdown();
			}			
		} else {
			System.out.println("Cannot get monitor url!");
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig config) throws ServletException {		
		this.config = config;
		this.context = config.getServletContext();
	}
	
	
	//Generate params posted to monitoring center
	public List<NameValuePair> generatePostParams(HttpServletRequest request, Properties props){
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		String name = context.getContextPath().substring(1);
		params.add(new BasicNameValuePair("name",name));
		String ip;
		try {
			ip = InetAddress.getLocalHost().getHostAddress();			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			ip= "unknown";
			e.printStackTrace();
		}
		params.add(new BasicNameValuePair("ip",ip));
		String jmxPort = props.getProperty("jmxPort", "8999");
		params.add(new BasicNameValuePair("jmxPort",jmxPort));
		
		//TODO 目前只能获得request的uri
		
		String uri = request.getRequestURI();
		
		params.add(new BasicNameValuePair("uri",uri));

		//new param can be added here, for example, remote ip
		
		return params;
	}

	
}
