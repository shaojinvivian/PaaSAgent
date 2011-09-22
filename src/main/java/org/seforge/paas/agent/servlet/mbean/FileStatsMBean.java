package org.seforge.paas.agent.servlet.mbean;

public interface FileStatsMBean {
	  /**
     * @jmx:managed-attribute
     */ 
    public Long getLastModified() ;
    /**
     * @jmx:managed-attribute
     */ 
    public Long getSize();

    /**
     * @jmx:managed-attribute
     */ 
    public Long getAvailability();

}
