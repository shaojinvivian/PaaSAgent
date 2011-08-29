package org.seforge.paas.agent.tomcat.listener;

import org.apache.catalina.Container;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.Server;
import org.apache.catalina.Service;
import org.apache.catalina.core.StandardEngine;

import javax.management.ObjectName;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.util.Properties;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class StartupListener implements LifecycleListener {

	private String monitorUrl = null;

	private static final String MONITOR_FILE = "monitor.properties";	

	public void lifecycleEvent(LifecycleEvent event) {
		Lifecycle lifecycle = event.getLifecycle();
		if (lifecycle instanceof Server) {
			Server server = (Server) lifecycle;
			if (event.getType().equals(Lifecycle.AFTER_START_EVENT)) {
				Service catalina = server.findService("Catalina");
				Container container = catalina.getContainer();
				if (container instanceof StandardEngine) {
					StandardEngine engine = (StandardEngine) container;
					File monitorFile = new File(engine.getBaseDir(),
							MONITOR_FILE);					
					monitorUrl = prepareUrl(monitorFile);
					if (monitorUrl != null) {
						HttpClient httpclient = new DefaultHttpClient();
						try {
							String strURL = monitorUrl + "/appservers";
							HttpPost httppost = new HttpPost(strURL);
							String content = getAppServerInfo();
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
					} else {
						System.out.println("Cannot get monitor url!");
					}
				}
			} else if (event.getType().equals(Lifecycle.AFTER_STOP_EVENT)) {
				System.err
						.println("Stopping Tomcat because the context stopped.");
				System.exit(1);
			}
		}
	}

	public String prepareUrl(File file) {
		String url = null;
		FileInputStream is;
		try {
			Properties properties = new Properties();
			is = new FileInputStream(file);
			properties.load(is);
			if (properties.containsKey("url")) {
				url = properties.getProperty("url");
			}
			is.close();
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find monitor configuration file!");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
	}

	public static String getAppServerInfo() {
		String info = null;
		ObjectName objectName;
		try {
			objectName = new ObjectName("Catalina:type=Server");
			String name = (String) ManagementFactory.getPlatformMBeanServer()
					.getAttribute(objectName, "serverInfo");
			String port = System
					.getProperty("com.sun.management.jmxremote.port");
			InetAddress local;
			local = InetAddress.getLocalHost();
			String ip = local.getHostAddress();
			StringBuilder sb = new StringBuilder();
			sb.append("{'name':'");
			sb.append(name);
			sb.append("@");
			sb.append(ip);
			sb.append("','ip':'");
			sb.append(ip);
			sb.append("','jmxPort':");
			sb.append(port);
			sb.append(",'isMonitee':false}");			
			info = sb.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return info;
	}
}
