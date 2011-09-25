package org.seforge.paas.agent.servlet.mbean;

public interface ContextInfoMBean {
	    /**
	     * @jmx:managed-attribute
	     */ 
	    public int getAvailable();  
	    
	  
	    /**
	     * @jmx:managed-attribute
	     */ 
	    public long getTotalTime() ;

	    /**
	     * @jmx:managed-attribute
	     */ 
	    public int getMinTime();

	    /**
	     * @jmx:managed-attribute
	     */ 
	    public int getMaxTime() ;

	    
	    /**
	     * @jmx:managed-attribute
	     */ 
	    public int getAvgTime() ;
	    
	    /**
	     * @jmx:managed-attribute
	     */ 
	    public int getBytesSent();

	    /**
	     * @jmx:managed-attribute
	     */ 
	    public int getBytesReceived();

	    /**
	     * @jmx:managed-attribute
	     */ 
	    public int getRequestCount();
	    
	    /**
	     * @jmx:managed-attribute
	     */ 
	    public int getErrorCount() ;
	    


	    /**
	     * @jmx:managed-attribute
	     */ 
	    public int getSessionsCreated() ;
	    
	    /**
	     * @jmx:managed-attribute
	     */ 
	    public int getSessionsDestroyed() ;
	    
	    /**
	     * @jmx:managed-attribute
	     */
	    public String getDocBase() ;	   

	    /**
	     * @jmx:managed-attribute
	     */
	    public String getContextName() ;

	    /**
	     * @jmx:managed-attribute
	     */
	    public String getResponseTimeLogDir() ;

	    /**
	     * @jmx:managed-attribute
	     */
//	    public void setResponseTimeLogDir(String logDir);
	    
	    /**
	     * @jmx:managed-attribute
	     */
	    public String getDisplayName();
	    
	    public String getErrorLogDir();

}
