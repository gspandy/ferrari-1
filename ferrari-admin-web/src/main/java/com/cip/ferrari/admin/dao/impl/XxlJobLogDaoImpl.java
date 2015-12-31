package com.cip.ferrari.admin.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.cip.ferrari.admin.core.model.XxlJobLog;
import com.cip.ferrari.admin.dao.IXxlJobLogDao;

@Repository
public class XxlJobLogDaoImpl implements IXxlJobLogDao {
	
	@Resource
	public SqlSessionTemplate sqlSessionTemplate;

	@Override
	public int save(XxlJobLog xxlJobLog) {
		return sqlSessionTemplate.insert("XxlJobLogMapper.save", xxlJobLog);
	}

	@Override
	public XxlJobLog load(int id) {
		return sqlSessionTemplate.selectOne("XxlJobLogMapper.load", id);
	}

	@Override
	public int updateTriggerInfo(XxlJobLog xxlJobLog) {
		return sqlSessionTemplate.update("XxlJobLogMapper.updateTriggerInfo", xxlJobLog);
	}

	@Override
	public int updateHandleInfo(XxlJobLog xxlJobLog) {
		return sqlSessionTemplate.update("XxlJobLogMapper.updateHandleInfo", xxlJobLog);
	}

	@Override
	public List<XxlJobLog> pageList(int offset, int pagesize,String jobName, Date triggerTimeStart, Date triggerTimeEnd) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("offset", offset);
		params.put("pagesize", pagesize);
		params.put("jobName", jobName);
		params.put("triggerTimeStart", triggerTimeStart);
		params.put("triggerTimeEnd", triggerTimeEnd);
		return sqlSessionTemplate.selectList("XxlJobLogMapper.pageList", params);
	}

	@Override
	public int pageListCount(int offset, int pagesize,String jobName, Date triggerTimeStart, Date triggerTimeEnd) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("offset", offset);
		params.put("pagesize", pagesize);
		params.put("jobName", jobName);
		params.put("triggerTimeStart", triggerTimeStart);
		params.put("triggerTimeEnd", triggerTimeEnd);
		return sqlSessionTemplate.selectOne("XxlJobLogMapper.pageListCount", params);
	}
	
}
