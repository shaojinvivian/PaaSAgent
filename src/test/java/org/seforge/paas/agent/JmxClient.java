package org.seforge.paas.agent;

import java.util.HashSet;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.seforge.paas.monitor.domain.AppInstance;
import org.seforge.paas.monitor.domain.AppServer;

public class JmxClient {
	public static void main(String[] args) throws Exception {
		AppServer appServer = new AppServer();
		appServer.setIp("10.117.4.96");
		appServer.setJmxPort(8999);
		addAppInstances(appServer);

	}
	
	public static void addAppInstances(AppServer appServer) {
		try {			
			Set<AppInstance> appInstances = new HashSet<AppInstance>();
			
			String ip = appServer.getIp();
			Integer port = appServer.getJmxPort();
			JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + ip
					+ ":"+ port +"/jmxrmi");
			JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
			MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
			String[] domains = mbsc.getDomains();
			for(String s : domains){
				System.out.println(s);
			}
			ObjectName obName = new ObjectName(
					"Catalina:j2eeType=WebModule,name=*,J2EEApplication=none,J2EEServer=none");
			Set<ObjectName> set = mbsc.queryNames(obName, null);			
			for (ObjectName name : set) {
				AppInstance appInstance = new AppInstance();
				appInstance.setName((String) mbsc.getAttribute(name, "name"));
				appInstance.setDisplayName((String) mbsc.getAttribute(name,
						"displayName"));
				appInstance.setDocBase((String) mbsc.getAttribute(name,
						"docBase"));
				appInstance.setAppServer(appServer);
				appInstances.add(appInstance);
			}
			appServer.setAppInstances(appInstances);
			jmxc.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		}
	}

}
