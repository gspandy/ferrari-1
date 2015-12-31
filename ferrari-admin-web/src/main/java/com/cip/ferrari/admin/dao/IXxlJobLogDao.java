package com.cip.ferrari.admin.dao;


import java.util.Date;
import java.util.List;

import com.cip.ferrari.admin.core.model.XxlJobLog;

public interface IXxlJobLogDao {
	
	public int save(XxlJobLog xxlJobLog);
	
	public XxlJobLog load(int id);
	
	public int updateTriggerInfo(XxlJobLog xxlJobLog);
	
	public int updateHandleInfo(XxlJobLog xxlJobLog);
	
	public List<XxlJobLog> pageList(int offset, int pagesize,String jobName, Date triggerTimeStart, Date triggerTimeEnd);
	
	public int pageListCount(int offset, int pagesize,String jobName, Date triggerTimeStart, Date triggerTimeEnd);
	
}
