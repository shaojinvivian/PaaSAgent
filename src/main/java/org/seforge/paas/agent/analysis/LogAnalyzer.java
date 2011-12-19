package org.seforge.paas.agent.analysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LogAnalyzer {
	private String fileName;
	private static Map<String, Integer> hitNum = new HashMap();
	private static Map<String, Integer> ipMap = new HashMap();
	private static ArrayList filteredList = new ArrayList();
	
	static{
		filteredList.add("css");
		filteredList.add("js");
		filteredList.add("gif");
		filteredList.add("png");
		filteredList.add("ico");
	}
	
	public static void parseLogForHitNum(String fileName) throws Exception{
		FileReader reader = new FileReader(fileName);  
        BufferedReader br = new BufferedReader(reader);  
        String s = null;  
        int lineNum = 0;
        while((s = br.readLine()) != null) {  
            String[] log = s.split(" ");
            String date = log[0];
            String time = log[1];
            String url = log[2];
            String[] tokens = url.split("\\.");
            if(tokens.length>1){
            	String appendix = tokens[1];
                if(filteredList.contains(appendix)){
                	continue;
                }
            }            
            String arriveTime = log[3];
            String responseTime = log[4];
            String code = log[5];
            String ip = log[6];
            Integer num = hitNum.get(url);
            if(num == null){
            	hitNum.put(url, 1);
            }else{
            	hitNum.put(url, num+1);
            }
            lineNum++;
        }  
        for(String s1 : hitNum.keySet()){
        	System.out.println("URL " + s1 + ":" + hitNum.get(s1));
        }       
        br.close();  
        reader.close();  
	}
	
	
	public static void parseLogForIpDistribution(String fileName) throws Exception{
		FileReader reader = new FileReader(fileName);  
        BufferedReader br = new BufferedReader(reader);  
        String s = null;  
        int lineNum = 0;
        while((s = br.readLine()) != null) {  
            String[] log = s.split(" ");
            String url = log[2];
            String[] tokens = url.split("\\.");
            if(tokens.length>1){
            	String appendix = tokens[1];
                if(filteredList.contains(appendix)){
                	continue;
                }
            }
            /*
            String date = log[0];
            String time = log[1];                       
            
            String arriveTime = log[3];
            String responseTime = log[4];
            String code = log[5];
            */
            String ip = log[6];
            Integer num = ipMap.get(ip);
            if(num == null){
            	ipMap.put(ip, 1);
            }else{
            	ipMap.put(ip, num+1);
            }
            lineNum++;
        }  
        for(String s1 : ipMap.keySet()){
        	System.out.println("IP " + s1 + ":" + ipMap.get(s1));
        }       
        br.close();  
        reader.close();  
	}
	
	
	public static void main(String[] args) {
		try {
			LogAnalyzer.parseLogForHitNum("ROOT_PaaSMonitorRT.log");
			LogAnalyzer.parseLogForIpDistribution("ROOT_PaaSMonitorRT.log");
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
}
