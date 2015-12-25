/**
 * 
 */
package com.cip.ferrari.core.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.cip.ferrari.core.common.JobConstants;
import com.cip.ferrari.core.job.result.FerrariFeedback;

/**
 * @author yuantengkai
 * job执行完后的反馈处理handler
 */
public class FeedbackHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(FeedbackHandler.class);
	
	private static final FeedbackHandler instance = new FeedbackHandler();
	
	private final BlockingQueue<ManagedJob> feedbackQueue = new LinkedBlockingQueue<ManagedJob>(20000);
	
	private FeedbackHandler(){
		this.init();
	}
	
	private void init(){
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				FeedbackHandler.this.dealSendBack();
			}
		});
		t.setName("Ferrari-Feedback-Thread");
		t.setDaemon(true);
		t.start();
	}
	
	public static FeedbackHandler getInstance(){
		return instance;
	}
	
	/**
	 * job执行完后的反馈
	 * @param job
	 * @return
	 */
	public boolean jobFinished2Feedback(ManagedJob job){
		boolean success = feedbackQueue.offer(job);
		if(success){
			return true;
		}else{
			try {
				return feedbackQueue.offer(job, 200, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				logger.error("job finished to offer feedbackQueue failed,"+job.toString(),e);
				return false;
			}
		}
	}
	
	/**
	 * 反馈处理
	 */
	private void dealSendBack(){
		while(true){
			try{
				ManagedJob job = feedbackQueue.poll();
				String result = getFeedbackJson(job);
				Map<String, String> httpParams = new HashMap<String, String>();
				httpParams.put(JobConstants.KEY_FEEDBACK_RESULT, result);
				
				if(logger.isInfoEnabled()){
					logger.info("start send job finish feedback:"+result);
				}
				List<String> urlList = job.getReturnUrllist();
				//TODO
				
			}catch(Throwable t){
				logger.error("job finished, feedback deal happens exception.",t);
			}
		}
	}

	private String getFeedbackJson(ManagedJob job) {
		FerrariFeedback feedback = new FerrariFeedback();
		feedback.setStatus(true);
		feedback.setUuid(job.getUuid());
		feedback.setDirectionType(DirectionType.RETURN_JOB_RESULT);
		if(job.getRunThrowable() != null){
			feedback.setStatus(false);
			 // 使用Exception更详细的说明信息（包含了cause exception的信息）
		    StringBuilder builder = new StringBuilder(512);
		    for (Throwable t = job.getRunThrowable(); null != t; t = t.getCause()) {
		        builder.append(t.toString()).append("\n");
		    }
		    feedback.setErrormsg(StringUtils.substring(builder.toString().toString(), 0, 2048));
		}
		
		// 当run method 的返回值类型是 void时，result是null
		if(job.getReturnObject() != null){
			feedback.setContent(job.getReturnObject().toString());
		}
		return JSON.toJSONString(feedback);
	}

}
