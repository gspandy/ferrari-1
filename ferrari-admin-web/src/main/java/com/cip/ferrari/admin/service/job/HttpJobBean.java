package com.cip.ferrari.admin.service.job;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.cip.ferrari.admin.core.model.FerraliJobLog;
import com.cip.ferrari.admin.core.util.Constantz;
import com.cip.ferrari.admin.core.util.DynamicSchedulerUtil;
import com.cip.ferrari.admin.core.util.HttpUtil;
import com.cip.ferrari.admin.core.util.JacksonUtil;
import com.cip.ferrari.admin.core.util.PropertiesUtil;

/**
 * http job bean
 * @author xuxueli 2015-12-17 18:20:34
 */
public class HttpJobBean extends QuartzJobBean {
	private static Logger logger = LoggerFactory.getLogger(HttpJobBean.class);

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		
		String triggerKey = context.getTrigger().getJobKey().getName();
		// jobDataMap 2 params
		Map<String, Object> jobDataMap = context.getMergedJobDataMap().getWrappedMap();
		Map<String, String> params = new HashMap<String, String>();
		if (jobDataMap!=null && jobDataMap.size()>0) {
			for (Entry<String, Object> item : jobDataMap.entrySet()) {
				params.put(item.getKey(), String.valueOf(item.getValue()));
			}
		}
		
		// corn
		String cornExp = null;
		if (context.getTrigger() instanceof CronTriggerImpl) {
			CronTriggerImpl trigger = (CronTriggerImpl) context.getTrigger();
			cornExp = trigger.getCronExpression();
		}
		
		// save log
		FerraliJobLog jobLog = new FerraliJobLog();
		jobLog.setJobName(triggerKey);
		jobLog.setJobCron(cornExp);
		jobLog.setJobClass(HttpJobBean.class.getName());
		jobLog.setJobData(JacksonUtil.writeValueAsString(params));
		DynamicSchedulerUtil.getFerraliJobLogDao().save(jobLog);
		logger.info(">>>>>>>>>>> xxl-job trigger start, jobLog:{}", jobLog);
		
		// trigger request
		params.put(Constantz.triggerLogId, String.valueOf(jobLog.getId()));
		params.put(Constantz.triggerLogUrl, PropertiesUtil.getString(Constantz.triggerLogUrl));
		String[] postResp = HttpUtil.post(params.get(Constantz.job_url), params);
		logger.info(">>>>>>>>>>> xxl-job trigger http response, jobLog.id:{}, jobLog:{}", jobLog.getId(), jobLog);
		
		// parse trigger response
		String responseMsg = postResp[0];
		String exceptionMsg = postResp[1];
		
		jobLog.setTriggerTime(new Date());
		jobLog.setTriggerStatus(HttpUtil.FAIL);
		jobLog.setTriggerMsg(exceptionMsg);
		if (StringUtils.isNotBlank(responseMsg)) {
			@SuppressWarnings("unchecked")
			Map<String, String> responseMap = JacksonUtil.readValue(responseMsg, Map.class);
			if (responseMap!=null && StringUtils.isNotBlank(responseMap.get(HttpUtil.status))) {
				jobLog.setTriggerStatus(responseMap.get(HttpUtil.status));
				jobLog.setTriggerMsg(responseMap.get(HttpUtil.msg));
			}
		}
		
		// update trigger info
		DynamicSchedulerUtil.getFerraliJobLogDao().updateTriggerInfo(jobLog);
		logger.info(">>>>>>>>>>> xxl-job trigger end, jobLog.id:{}, jobLog:{}", jobLog.getId(), jobLog);
		
    }
	
}