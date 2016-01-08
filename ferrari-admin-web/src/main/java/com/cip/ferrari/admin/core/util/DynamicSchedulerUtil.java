package com.cip.ferrari.admin.core.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.cip.ferrari.admin.dao.IFerrariJobLogDao;

/**
 * base quartz scheduler util
 * @author xuxueli 2015-12-19 16:13:53
 */
public final class DynamicSchedulerUtil implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(DynamicSchedulerUtil.class);
    
    public static IFerrariJobLogDao ferrariJobLogDao;
    
    private static Scheduler scheduler;
    
    @Resource
    public void setFerraliJobLogDao(IFerrariJobLogDao ferrariJobLogDao) {
		DynamicSchedulerUtil.ferrariJobLogDao = ferrariJobLogDao;
	}
    
    public static IFerrariJobLogDao getFerrariJobLogDao() {
		return ferrariJobLogDao;
	}
    
    public static void setScheduler(Scheduler scheduler) {
		DynamicSchedulerUtil.scheduler = scheduler;
	}
    
	@Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(scheduler, "quartz scheduler is null");
        if(logger.isInfoEnabled()){
        	logger.info("###### init quartz scheduler success.[{}]", scheduler);
        }
    }
	
	/** getJobKeys
	 * 
	 * @return
	 */
	public static List<Map<String, Object>> getJobList(){
		List<Map<String, Object>> jobList = new ArrayList<Map<String,Object>>();
		
		try {
			if (scheduler.getJobGroupNames()==null || scheduler.getJobGroupNames().size()==0) {
				return null;
			}
			String groupName = scheduler.getJobGroupNames().get(0);
			Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName));
			if (jobKeys !=null && jobKeys.size() > 0) {
				for (JobKey jobKey : jobKeys) {
			        TriggerKey triggerKey = TriggerKey.triggerKey(jobKey.getName(), Scheduler.DEFAULT_GROUP);
			        Trigger trigger = scheduler.getTrigger(triggerKey);
			        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
			        TriggerState triggerState = scheduler.getTriggerState(triggerKey);
			        Map<String, Object> jobMap = new HashMap<String, Object>();
			        jobMap.put("TriggerKey", triggerKey);
			        jobMap.put("Trigger", trigger);
			        jobMap.put("JobDetail", jobDetail);
			        jobMap.put("TriggerState", triggerState);
			        jobList.add(jobMap);
				}
			}
			
		} catch (SchedulerException e) {
			logger.error("######query joblist exception.", e);
			return null;
		}
		return jobList;
	}

	/**
	 * 新增一个job
	 * @param triggerKeyName
	 * @param cronExpression
	 * @param jobClass
	 * @param jobData
	 * @return
	 * @throws SchedulerException
	 */
    public static boolean addJob(String triggerKeyName, String cronExpression, Class<? extends Job> jobClass, Map<String, Object> jobData) throws SchedulerException {
    	// TriggerKey : name + group
    	String group = Scheduler.DEFAULT_GROUP;
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerKeyName, group);
        
        // TriggerKey valid if_exists
        if (scheduler.checkExists(triggerKey)) {
//            Trigger trigger = scheduler.getTrigger(triggerKey);
            logger.warn("###### Already exist trigger with key [" + triggerKey + "] in quartz scheduler");
            return false;
        }
        
        // CronTrigger : TriggerKey + cronExpression
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();

        // JobDetail : jobClass
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(triggerKeyName, group).build();
        if (jobData !=null && jobData.size() > 0) {
        	JobDataMap jobDataMap = jobDetail.getJobDataMap();
        	jobDataMap.putAll(jobData);	// JobExecutionContext context.getMergedJobDataMap().get("mailGuid");
		}
        
        // schedule : jobDetail + cronTrigger
        Date date = scheduler.scheduleJob(jobDetail, cronTrigger);

        if(logger.isInfoEnabled()){
        	logger.info("###### addJob success, jobDetail:{}, cronTrigger:{}, date:{}", jobDetail, cronTrigger, date);
        }
        
        return true;
    }
    
    /** reschedule 重置cron
     * 
     * @param triggerKeyName
     * @param cronExpression
     * @return
     * @throws SchedulerException
     */
    public static boolean rescheduleJob(String triggerKeyName, String cronExpression) throws SchedulerException {
        // TriggerKey : name + group
    	String group = Scheduler.DEFAULT_GROUP;
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerKeyName, group);
        
        if (scheduler.checkExists(triggerKey)) {
            // CronTrigger : TriggerKey + cronExpression
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
            
            Date date = scheduler.rescheduleJob(triggerKey, cronTrigger);
            if(date != null){
            	return true;
            }
            return false;
        } else {
        	logger.warn("######scheduler.rescheduleJob, triggerKey not exist,key="+triggerKeyName);
        }
        return false;
    }
    
    /**unscheduleJob 删除
     * 
     * @param triggerKeyName
     * @return
     * @throws SchedulerException
     */
    public static boolean removeJob(String triggerKeyName) throws SchedulerException {
    	// TriggerKey : name + group
    	String group = Scheduler.DEFAULT_GROUP;
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerKeyName, group);
        
        boolean result = true;
        if (scheduler.checkExists(triggerKey)) {
            result = scheduler.unscheduleJob(triggerKey);
        }
        return result;
    }

    /** Pause 暂停
     * 
     * @param triggerKeyName
     * @return
     * @throws SchedulerException
     */
    public static boolean pauseJob(String triggerKeyName) throws SchedulerException {
    	// TriggerKey : name + group
    	String group = Scheduler.DEFAULT_GROUP;
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerKeyName, group);
        
        boolean result = true;
        if (scheduler.checkExists(triggerKey)) {
            scheduler.pauseTrigger(triggerKey);
            return true;
        }else{
        	logger.warn("######scheduler.pauseTrigger, triggerKey not exist,key="+triggerKeyName);
        }
        return result;
    }
    
    /** resume 重启 
     * 
     * @param triggerKeyName
     * @return
     * @throws SchedulerException
     */
    public static boolean resumeJob(String triggerKeyName) throws SchedulerException {
        // TriggerKey : name + group
    	String group = Scheduler.DEFAULT_GROUP;
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerKeyName, group);
        
        if (scheduler.checkExists(triggerKey)) {
            scheduler.resumeTrigger(triggerKey);
            return true;
        } else {
        	logger.warn("######scheduler.resumeTrigger, triggerKey not exist,key="+triggerKeyName);
        }
        return false;
    }
    
    /** run 执行一次 
     * 
     * @param triggerKeyName
     * @return
     * @throws SchedulerException
     */
    public static boolean triggerJob(String triggerKeyName) throws SchedulerException {
        // TriggerKey : name + group
    	String group = Scheduler.DEFAULT_GROUP;
        JobKey jobKey = JobKey.jobKey(triggerKeyName, group);
        
        if (scheduler.checkExists(jobKey)) {
            scheduler.triggerJob(jobKey);
            return true;
        } else {
        	logger.warn("######scheduler.triggerJob, triggerKey not exist,key="+triggerKeyName);
        }
        return false;
    }

}