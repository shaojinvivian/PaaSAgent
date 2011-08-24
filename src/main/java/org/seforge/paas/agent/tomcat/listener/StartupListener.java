package org.seforge.paas.agent.tomcat.listener;

import org.apache.catalina.Container;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;

import javax.management.Notification;
import javax.management.NotificationListener;
import java.io.*;

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

public class StartupListener implements LifecycleListener, NotificationListener {

	private String stateFile;

	private static final String APPCLOUD_STATE_FILE = "tomcat.state";
	private static final String J2EE_RUNNING_STATE = "j2ee.state.running";

	public void lifecycleEvent(LifecycleEvent event) {
		Lifecycle lifecycle = event.getLifecycle();

		if (lifecycle instanceof StandardContext) {
			StandardContext context = (StandardContext) lifecycle;
			if (event.getType().equals(Lifecycle.BEFORE_START_EVENT)) {
				Container grandParent = context.getParent().getParent();
				if (grandParent instanceof StandardEngine) {
					StandardEngine engine = (StandardEngine) grandParent;
					String relativePath = new StringBuilder().append("..")
							.append(File.separator).append(APPCLOUD_STATE_FILE)
							.toString();
					stateFile = new File(engine.getBaseDir(), relativePath)
							.getAbsolutePath();
					context.addNotificationListener(this, null, null);
				}
			} else if (event.getType().equals(Lifecycle.AFTER_STOP_EVENT)) {
				System.err
						.println("Stopping Tomcat because the context stopped.");
				System.exit(1);
			}
		}
	}

	public void handleNotification(Notification notification, Object handback) {
		if (J2EE_RUNNING_STATE.equals(notification.getType())) {

			HttpClient httpclient = new DefaultHttpClient();
			try {
				String strURL = "http://localhost:8080/PaaSMonitor/appservers";
				HttpPost httppost = new HttpPost(strURL);
				String content = "{'name':'appserver','ip':'127.0.0.2','jmxPort':8999}";
				StringEntity requestEntity = new StringEntity(content, "UTF-8");

				httppost.setEntity(requestEntity);
				httppost.setHeader("Accept", "application/json");

				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				String responseBody = httpclient.execute(httppost,
						responseHandler);
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
}
