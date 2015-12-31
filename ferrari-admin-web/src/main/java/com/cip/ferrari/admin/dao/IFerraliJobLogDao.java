package com.cip.ferrari.admin.dao;


import java.util.Date;
import java.util.List;

import com.cip.ferrari.admin.core.model.FerraliJobLog;

public interface IFerraliJobLogDao {
	
	public int save(FerraliJobLog xxlJobLog);
	
	public FerraliJobLog load(int id);
	
	public int updateTriggerInfo(FerraliJobLog xxlJobLog);
	
	public int updateHandleInfo(FerraliJobLog xxlJobLog);
	
	public List<FerraliJobLog> pageList(int offset, int pagesize,String jobName, Date triggerTimeStart, Date triggerTimeEnd);
	
	public int pageListCount(int offset, int pagesize,String jobName, Date triggerTimeStart, Date triggerTimeEnd);
	
}
