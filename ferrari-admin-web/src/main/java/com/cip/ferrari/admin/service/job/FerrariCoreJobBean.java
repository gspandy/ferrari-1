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

import com.cip.ferrari.admin.core.model.FerraliJobLog;
import com.cip.ferrari.admin.core.util.Constantz;
import com.cip.ferrari.admin.core.util.DynamicSchedulerUtil;
import com.cip.ferrari.admin.core.util.HttpUtil;
import com.cip.ferrari.admin.core.util.JacksonUtil;
import com.cip.ferrari.admin.core.util.PropertiesUtil;
import com.cip.ferrari.core.common.JobConstants;
import com.cip.ferrari.core.job.result.FerrariFeedback;

/**
 * //点评ferrali定制版，适用于：ferrali-core 
 * http job bean
 * @author xuxueli 2015-12-17 18:20:34
 */
public class FerrariCoreJobBean extends QuartzJobBean {
	private static Logger logger = LoggerFactory.getLogger(FerrariCoreJobBean.class);
	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		String triggerKey = context.getTrigger().getJobKey().getName();
		Map<String, Object> jobDataMap = context.getMergedJobDataMap().getWrappedMap();
		
		// save log
		FerraliJobLog jobLog = new FerraliJobLog();
		jobLog.setJobName(context.getTrigger().getJobKey().getName());
		jobLog.setJobCron((context.getTrigger() instanceof CronTriggerImpl)?(((CronTriggerImpl) context.getTrigger()).getCronExpression()):"");
		jobLog.setJobClass(FerrariCoreJobBean.class.getName());
		jobLog.setJobData(JacksonUtil.writeValueAsString(jobDataMap));
		DynamicSchedulerUtil.xxlJobLogDao.save(jobLog);
		logger.info(">>>>>>>>>>> xxl-job trigger start, jobLog:{}", jobLog);
		
		// request param
		Map<String, String> params = new HashMap<String, String>();
		String job_url = JobConstants.KEY_JOB_ADDRESS_TEMPLATE.replace("{job_address}", String.valueOf(jobDataMap.get(JobConstants.KEY_JOB_ADDRESS)));
		params.put(JobConstants.KEY_UUID, jobLog.getId()+"");
		params.put(JobConstants.KEY_RESULT_URL_LIST, PropertiesUtil.getString(Constantz.triggerLogUrl));
		params.put(JobConstants.KEY_ACTION, JobConstants.VALUE_ACTION_RUN_JOB);
		params.put(JobConstants.KEY_JOB_NAME, triggerKey);
		params.put(JobConstants.KEY_RUN_CLASS, String.valueOf(jobDataMap.get(JobConstants.KEY_RUN_CLASS)));
		params.put(JobConstants.KEY_RUN_METHOD, String.valueOf(jobDataMap.get(JobConstants.KEY_RUN_METHOD)));
		params.put(JobConstants.KEY_RUN_METHOD_ARGS, String.valueOf(jobDataMap.get(JobConstants.KEY_RUN_METHOD_ARGS)));
		
		String[] postResp = HttpUtil.post(job_url, params);
		logger.info(">>>>>>>>>>> xxl-job trigger http response, jobLog.id:{}, jobLog:{}", jobLog.getId(), jobLog);
		
		// parse trigger response
		String responseMsg = postResp[0];
		String exceptionMsg = postResp[1];
		if (exceptionMsg!=null && exceptionMsg.length()>1500) {
			exceptionMsg = exceptionMsg.substring(0, 1500);
		}
		jobLog.setTriggerTime(new Date());
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
		DynamicSchedulerUtil.xxlJobLogDao.updateTriggerInfo(jobLog);
		logger.info(">>>>>>>>>>> xxl-job trigger end, jobLog.id:{}, jobLog:{}", jobLog.getId(), jobLog);
		
    }
	
	
}