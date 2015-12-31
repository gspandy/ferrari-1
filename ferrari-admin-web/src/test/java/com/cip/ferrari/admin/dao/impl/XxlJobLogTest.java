package com.cip.ferrari.admin.dao.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cip.ferrari.admin.core.model.FerraliJobLog;
import com.cip.ferrari.admin.core.util.HttpUtil;
import com.cip.ferrari.admin.dao.IFerraliJobLogDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationcontext-*.xml")
public class XxlJobLogTest {
	
	@Resource
	private IFerraliJobLogDao xxlJobLogDao;
	
	@Test
	public void save_load(){
		FerraliJobLog xxlJobLog = new FerraliJobLog();
		xxlJobLog.setJobName("job_name");
		xxlJobLog.setJobCron("jobCron");
		xxlJobLog.setJobClass("jobClass");
		xxlJobLog.setJobData("jobData");
		int count = xxlJobLogDao.save(xxlJobLog);
		System.out.println(count);
		System.out.println(xxlJobLog.getId());
		
		FerraliJobLog item = xxlJobLogDao.load(xxlJobLog.getId());
		System.out.println(item);
	}
	
	@Test
	public void updateTriggerInfo(){
		FerraliJobLog xxlJobLog = xxlJobLogDao.load(29);
		xxlJobLog.setTriggerTime(new Date());
		xxlJobLog.setTriggerStatus(HttpUtil.SUCCESS);
		xxlJobLog.setTriggerMsg("trigger msg");
		xxlJobLogDao.updateTriggerInfo(xxlJobLog);
	}
	
	@Test
	public void updateHandleInfo(){
		FerraliJobLog xxlJobLog = xxlJobLogDao.load(29);
		xxlJobLog.setHandleTime(new Date());
		xxlJobLog.setHandleStatus(HttpUtil.SUCCESS);
		xxlJobLog.setHandleMsg("handle msg");
		xxlJobLogDao.updateHandleInfo(xxlJobLog);
	}
	
	@Test
	public void pageList(){
		List<FerraliJobLog> list = xxlJobLogDao.pageList(0, 20, null, null, null);
		int list_count = xxlJobLogDao.pageListCount(0, 20, null, null, null);
		
		System.out.println(list);
		System.out.println(list_count);
	}
	
}
