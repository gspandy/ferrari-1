/**
 * 
 */
package com.cip.ferrari.core;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cip.ferrari.core.common.JobConstants;
import com.cip.ferrari.core.job.ContainerJobManager;
import com.cip.ferrari.core.job.result.FerrariFeedback;

/**
 * @author yuantengkai
 * Ferrari job运行对外facade
 */
public class FerrariRunnerFacade {
	
	private static final Logger logger = LoggerFactory.getLogger(FerrariRunnerFacade.class);
	
	private ContainerJobManager     containerJobManager;
	
	
	public void init(){
		containerJobManager = new ContainerJobManager();
		containerJobManager.init();
		
	}
	
	public String request(Map<String, String> params){
		FerrariFeedback result = new FerrariFeedback();
        result.setStatus(true);
        final String command = params.remove(JobConstants.KEY_ACTION);
        try{
        	
        }catch(Throwable t){
        	
        }
		return null;
	}
	
	public void destroy(){
		containerJobManager.dispose();
	}

}
