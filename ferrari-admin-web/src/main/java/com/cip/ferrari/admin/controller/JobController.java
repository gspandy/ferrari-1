package com.cip.ferrari.admin.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.quartz.CronExpression;
import org.quartz.Job;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cip.ferrari.admin.common.FerrariConstantz;
import com.cip.ferrari.admin.core.model.ReturnT;
import com.cip.ferrari.admin.core.util.DynamicSchedulerUtil;
import com.cip.ferrari.admin.service.job.FerrariCoreJobBean;
import com.cip.ferrari.admin.service.job.HttpJobBean;
import com.cip.ferrari.core.common.JobConstants;

/**
 * index controller
 * @author xuxueli 2015-12-19 16:13:16
 */
@Controller
@RequestMapping("/job")
public class JobController {
	
	private static Logger Logger = LoggerFactory.getLogger(JobLogController.class);
	
	@RequestMapping
	public String index(Model model) {
		List<Map<String, Object>> jobList = DynamicSchedulerUtil.getJobList();
		model.addAttribute("jobList", jobList);
		return "job/index";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/add")
	@ResponseBody
	public ReturnT<String> add(HttpServletRequest request) {
		String triggerKeyName = null;
		String cronExpression = null;
		Map<String, Object> jobData = new HashMap<String, Object>();
		
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		Set<Map.Entry<String, String[]>> paramSet = request.getParameterMap().entrySet();
		for (Entry<String, String[]> param : paramSet) {
			if (param.getKey().equals("triggerKeyName")) {
				triggerKeyName = param.getValue()[0];
			} else if (param.getKey().equals("cronExpression")) {
				cronExpression = param.getValue()[0];
			} else {
				jobData.put(param.getKey(), param.getValue().length>0?param.getValue()[0]:param.getValue());
			}
		}
		
		// triggerKeyName
		if (StringUtils.isBlank(triggerKeyName)) {
			return new ReturnT<String>(500, "请输入“任务key”");
		}
		
		// cronExpression
		if (StringUtils.isBlank(cronExpression)) {
			return new ReturnT<String>(500, "请输入“任务cron”");
		}
		if (!CronExpression.isValidExpression(cronExpression)) {
			return new ReturnT<String>(500, "“任务cron”不合法");
		}
		
		// jobData
		if (jobData.get(FerrariConstantz.job_desc)==null || jobData.get(FerrariConstantz.job_desc).toString().trim().length()==0) {
			return new ReturnT<String>(500, "请输入“任务描述”");
		}
		if (jobData.get(FerrariConstantz.job_url)==null || jobData.get(FerrariConstantz.job_url).toString().trim().length()==0) {
			return new ReturnT<String>(500, "请输入“任务URL”");
		}
		if (jobData.get(FerrariConstantz.handleName)==null || jobData.get(FerrariConstantz.handleName).toString().trim().length()==0) {
			return new ReturnT<String>(500, "请输入“任务handler”");
		}
		
		// jobClass
		Class<? extends Job> jobClass = HttpJobBean.class;
		
		try {
			boolean result = DynamicSchedulerUtil.addJob(triggerKeyName, cronExpression, jobClass, jobData);
			if (!result) {
				return new ReturnT<String>(500, "任务ID重复，请更换确认");
			}
			return ReturnT.SUCCESS;
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return ReturnT.FAIL;
	}
	
	/**ferrali定制版
	 * 新增一个任务
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/addFerrari")
	@ResponseBody
	public ReturnT<String> addFerrari(HttpServletRequest request) {
		String triggerKeyName = null;
		String cronExpression = null;
		Map<String, Object> jobData = new HashMap<String, Object>();
		
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
		}
		Set<Map.Entry<String, String[]>> paramSet = request.getParameterMap().entrySet();
		for (Entry<String, String[]> param : paramSet) {
			if (param.getKey().equals("triggerKeyName")) {
				triggerKeyName = param.getValue()[0];
			} else if (param.getKey().equals("cronExpression")) {
				cronExpression = param.getValue()[0];
			} else {
				jobData.put(param.getKey(), param.getValue().length>0?param.getValue()[0]:param.getValue());
			}
		}
		
		// triggerKeyName
		if (StringUtils.isBlank(triggerKeyName)) {
			return new ReturnT<String>(500, "请输入“任务key”");
		}
		
		// cronExpression
		if (StringUtils.isBlank(cronExpression)) {
			return new ReturnT<String>(500, "请输入“任务cron”");
		}
		if (!CronExpression.isValidExpression(cronExpression)) {
			return new ReturnT<String>(500, "“任务cron”不合法");
		}
		// jobClass
		Class<? extends Job> jobClass = FerrariCoreJobBean.class;
		
		// jobData
		if (jobData.get(JobConstants.KEY_JOB_DESC)==null || jobData.get(JobConstants.KEY_JOB_DESC).toString().trim().length()==0) {
			return new ReturnT<String>(500, "请输入“任务描述”");
		}
		if (jobData.get(JobConstants.KEY_JOB_ADDRESS)==null || jobData.get(JobConstants.KEY_JOB_ADDRESS).toString().trim().length()==0) {
			return new ReturnT<String>(500, "请输入“机器地址”");
		}
		if (jobData.get(JobConstants.KEY_RUN_CLASS)==null || jobData.get(JobConstants.KEY_RUN_CLASS).toString().trim().length()==0) {
			return new ReturnT<String>(500, "请输入“期望执行的类”");
		}
		if (jobData.get(JobConstants.KEY_RUN_METHOD)==null || jobData.get(JobConstants.KEY_RUN_METHOD).toString().trim().length()==0) {
			return new ReturnT<String>(500, "请输入“期望运行的方法”");
		}
		
		try {
			boolean result = DynamicSchedulerUtil.addJob(triggerKeyName, cronExpression, jobClass, jobData);
			if (!result) {
				return new ReturnT<String>(500, "任务ID重复，请更换确认");
			}
			return ReturnT.SUCCESS;
		} catch (SchedulerException e) {
			Logger.error("新增任务失败,triggerKeyName="+triggerKeyName+",jobData="+jobData,e);
		}
		return ReturnT.FAIL;
	}
	
	/**
	 * 更新任务执行时间
	 * @param triggerKeyName
	 * @param cronExpression
	 * @return
	 */
	@RequestMapping("/reschedule")
	@ResponseBody
	public ReturnT<String> reschedule(String triggerKeyName, String cronExpression) {
		// triggerKeyName
		if (StringUtils.isBlank(triggerKeyName)) {
			return new ReturnT<String>(500, "请输入“任务key”");
		}
		// cronExpression
		if (StringUtils.isBlank(cronExpression)) {
			return new ReturnT<String>(500, "请输入“任务cron”");
		}
		if (!CronExpression.isValidExpression(cronExpression)) {
			return new ReturnT<String>(500, "“任务cron”不合法");
		}
		try {
			boolean result = DynamicSchedulerUtil.rescheduleJob(triggerKeyName, cronExpression);
			if(result){
				return ReturnT.SUCCESS;
			}
			return ReturnT.FAIL;
		} catch (SchedulerException e) {
			Logger.error("更新任务执行时间失败,triggerKeyName="+triggerKeyName,e);;
		}
		return ReturnT.FAIL;
	}
	
	/**
	 * 删除任务
	 * @param triggerKeyName
	 * @return
	 */
	@RequestMapping("/remove")
	@ResponseBody
	public ReturnT<String> remove(String triggerKeyName) {
		try {
			boolean result = DynamicSchedulerUtil.removeJob(triggerKeyName);
			if(result){
				return ReturnT.SUCCESS;
			}
			return ReturnT.FAIL;
		} catch (SchedulerException e) {
			Logger.error("删除任务失败,triggerKeyName="+triggerKeyName,e);
			return ReturnT.FAIL;
		}
	}
	
	/**
	 * 暂停任务调度
	 * @param triggerKeyName
	 * @return
	 */
	@RequestMapping("/pause")
	@ResponseBody
	public ReturnT<String> pause(String triggerKeyName) {
		try {
			boolean result = DynamicSchedulerUtil.pauseJob(triggerKeyName);
			if(result){
				return ReturnT.SUCCESS;
			}
			return ReturnT.FAIL;
		} catch (SchedulerException e) {
			Logger.error("暂停任务调度失败,triggerKeyName="+triggerKeyName,e);
			return ReturnT.FAIL;
		}
	}
	
	/**
	 * 恢复任务调度
	 * @param triggerKeyName
	 * @return
	 */
	@RequestMapping("/resume")
	@ResponseBody
	public ReturnT<String> resume(String triggerKeyName) {
		try {
			boolean result = DynamicSchedulerUtil.resumeJob(triggerKeyName);
			if(result){
				return ReturnT.SUCCESS;
			}
			return ReturnT.FAIL;
		} catch (SchedulerException e) {
			Logger.error("恢复任务调度失败,triggerKeyName="+triggerKeyName,e);
			return ReturnT.FAIL;
		}
	}
	
	/**
	 * 手动触发一次任务
	 * @param triggerKeyName
	 * @return
	 */
	@RequestMapping("/trigger")
	@ResponseBody
	public ReturnT<String> triggerJob(String triggerKeyName) {
		try {
			boolean result = DynamicSchedulerUtil.triggerJob(triggerKeyName);
			if(result){
				return ReturnT.SUCCESS;
			}
			return ReturnT.FAIL;
		} catch (SchedulerException e) {
			Logger.error("手动触发执行任务失败,triggerKeyName="+triggerKeyName,e);
			return ReturnT.FAIL;
		}
	}
	
}
