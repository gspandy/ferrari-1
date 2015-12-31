package com.cip.ferrari.admin.dao.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cip.ferrari.admin.core.model.XxlJobLog;
import com.cip.ferrari.admin.core.util.HttpUtil;
import com.cip.ferrari.admin.dao.IXxlJobLogDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationcontext-*.xml")
public class XxlJobLogTest {
	
	@Resource
	private IXxlJobLogDao xxlJobLogDao;
	
	@Test
	public void save_load(){
		XxlJobLog xxlJobLog = new XxlJobLog();
		xxlJobLog.setJobName("job_name");
		xxlJobLog.setJobCron("jobCron");
		xxlJobLog.setJobClass("jobClass");
		xxlJobLog.setJobData("jobData");
		int count = xxlJobLogDao.save(xxlJobLog);
		System.out.println(count);
		System.out.println(xxlJobLog.getId());
		
		XxlJobLog item = xxlJobLogDao.load(xxlJobLog.getId());
		System.out.println(item);
	}
	
	@Test
	public void updateTriggerInfo(){
		XxlJobLog xxlJobLog = xxlJobLogDao.load(29);
		xxlJobLog.setTriggerTime(new Date());
		xxlJobLog.setTriggerStatus(HttpUtil.SUCCESS);
		xxlJobLog.setTriggerMsg("trigger msg");
		xxlJobLogDao.updateTriggerInfo(xxlJobLog);
	}
	
	@Test
	public void updateHandleInfo(){
		XxlJobLog xxlJobLog = xxlJobLogDao.load(29);
		xxlJobLog.setHandleTime(new Date());
		xxlJobLog.setHandleStatus(HttpUtil.SUCCESS);
		xxlJobLog.setHandleMsg("handle msg");
		xxlJobLogDao.updateHandleInfo(xxlJobLog);
	}
	
	@Test
	public void pageList(){
		List<XxlJobLog> list = xxlJobLogDao.pageList(0, 20, null, null, null);
		int list_count = xxlJobLogDao.pageListCount(0, 20, null, null, null);
		
		System.out.println(list);
		System.out.println(list_count);
	}
	
}
