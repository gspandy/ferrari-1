package com.cip.ferrari.admin.service.job;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.cip.ferrari.admin.core.model.FerrariJobLog;
import com.cip.ferrari.admin.core.util.DynamicSchedulerUtil;
import com.cip.ferrari.admin.core.util.HostUtil;
import com.cip.ferrari.admin.core.util.HttpUtil;
import com.cip.ferrari.admin.core.util.JacksonUtil;
import com.cip.ferrari.core.common.JobConstants;
import com.cip.ferrari.core.job.result.FerrariFeedback;

/**
 * ferrari任务版本，适用于：ferrari-core 
 * @author xuxueli 2015-12-17 18:20:34
 */
public class FerrariCoreJobBean extends QuartzJobBean {
	private static Logger logger = LoggerFactory.getLogger(FerrariCoreJobBean.class);
	
	private final String PORT = "8080";
	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		String triggerKeyName = context.getTrigger().getJobKey().getName();
		Map<String, Object> jobDataMap = context.getMergedJobDataMap().getWrappedMap();
		
		// save log
		FerrariJobLog jobLog = new FerrariJobLog();
		jobLog.setJobName(context.getTrigger().getJobKey().getName());
		jobLog.setJobCron((context.getTrigger() instanceof CronTriggerImpl)?(((CronTriggerImpl) context.getTrigger()).getCronExpression()):"");
		jobLog.setJobClass(FerrariCoreJobBean.class.getName());
		jobLog.setJobData(JacksonUtil.writeValueAsString(jobDataMap));
		DynamicSchedulerUtil.getFerrariJobLogDao().save(jobLog);
		if(logger.isInfoEnabled()){
			logger.info("############ferrari job trigger starting..., jobLog:{}", jobLog);
		}
		
		// request param
		Map<String, String> params = new HashMap<String, String>();
		String job_url = JobConstants.KEY_JOB_ADDRESS_TEMPLATE.replace("{job_address}", String.valueOf(jobDataMap.get(JobConstants.KEY_JOB_ADDRESS)));
		params.put(JobConstants.KEY_UUID, jobLog.getId()+"");
		params.put(JobConstants.KEY_RESULT_URL_LIST, HostUtil.getIP()+":"+PORT);
		params.put(JobConstants.KEY_ACTION, JobConstants.VALUE_ACTION_RUN_JOB);
		params.put(JobConstants.KEY_JOB_NAME, triggerKeyName);
		params.put(JobConstants.KEY_RUN_CLASS, String.valueOf(jobDataMap.get(JobConstants.KEY_RUN_CLASS)));
		params.put(JobConstants.KEY_RUN_METHOD, String.valueOf(jobDataMap.get(JobConstants.KEY_RUN_METHOD)));
		params.put(JobConstants.KEY_RUN_METHOD_ARGS, String.valueOf(jobDataMap.get(JobConstants.KEY_RUN_METHOD_ARGS)));
		
		String[] postResp = HttpUtil.post(job_url, params);
		if(logger.isInfoEnabled()){
			logger.info("############ferrari job trigger http response, jobLog.id:{}, response:{}", jobLog.getId(), postResp);
		}
		
		// parse trigger response
		String responseMsg = postResp[0];
		String exceptionMsg = postResp[1];
		jobLog.setTriggerTime(new Date());
		jobLog.setTriggerHost(HostUtil.getIP()+"__"+HostUtil.getHostname());
		jobLog.setTriggerStatus(HttpUtil.FAIL);
		jobLog.setTriggerMsg(exceptionMsg);
		if (StringUtils.isNotBlank(responseMsg)) {
			FerrariFeedback retVo = null;
			try {
				retVo = JacksonUtil.readValue(responseMsg, FerrariFeedback.class);
			} catch (Exception e) {
			}
			if (retVo!=null) {
				jobLog.setTriggerStatus(retVo.isStatus()?HttpUtil.SUCCESS:HttpUtil.FAIL);
				jobLog.setTriggerMsg(retVo.getErrormsg());
			}
		}
		
		// update trigger info
		if (jobLog.getTriggerMsg()!=null && jobLog.getTriggerMsg().length()>1500) {
			jobLog.setTriggerMsg(jobLog.getTriggerMsg().substring(0, 1500));
		}
		DynamicSchedulerUtil.getFerrariJobLogDao().updateTriggerInfo(jobLog);
		if(logger.isInfoEnabled()){
			logger.info("############ferrari job trigger end, jobLog.id:{}", jobLog.getId());
		}
		
		
    }
	
	
}