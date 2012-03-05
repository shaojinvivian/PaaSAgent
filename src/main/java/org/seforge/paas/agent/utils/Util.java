package org.seforge.paas.agent.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Util {

	public static Properties retrieveProperties(File file) {
		String url = null;
		FileInputStream is;
		Properties properties = null;
		try {
			properties = new Properties();
			is = new FileInputStream(file);
			properties.load(is);			
			is.close();
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find monitor configuration file!");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return properties;
	}
	
	
}
