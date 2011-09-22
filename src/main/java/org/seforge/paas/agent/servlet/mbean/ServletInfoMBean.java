package org.seforge.paas.agent.servlet.mbean;


public interface ServletInfoMBean {
	 /**
     * @jmx:managed-attribute
     */ 
    public int getAvailable();
    
   
    
    /**
     * @jmx:managed-attribute
     */ 
    public String getServletPath();
    /**
     * @jmx:managed-attribute
     */ 
    public void setServletPath(String servletPath);

    /**
     * @jmx:managed-attribute
     */ 
    public long getTotalTime() ;

    /**
     * @jmx:managed-attribute
     */ 
    public int getMinTime() ;

    /**
     * @jmx:managed-attribute
     */ 
    public int getMaxTime() ;

    /**
     * @jmx:managed-attribute
     */ 
    public int getAvgTime();
    
    /**
     * @jmx:managed-attribute
     */ 
    public int getRequestCount();
    
    /**
     * @jmx:managed-attribute
     */ 
    public int getErrorCount() ;
 
}
