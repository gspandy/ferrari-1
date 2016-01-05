package com.cip.ferrari.admin.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.cip.ferrari.admin.core.model.FerraliJobLog;
import com.cip.ferrari.admin.dao.IFerraliJobLogDao;

@Repository
public class FerraliJobLogDaoImpl implements IFerraliJobLogDao {
	
	@Resource
	public SqlSessionTemplate sqlSessionTemplate;

	@Override
	public int save(FerraliJobLog xxlJobLog) {
		return sqlSessionTemplate.insert("FerraliJobLogMapper.save", xxlJobLog);
	}

	@Override
	public FerraliJobLog load(int id) {
		return sqlSessionTemplate.selectOne("FerraliJobLogMapper.load", id);
	}

	@Override
	public int updateTriggerInfo(FerraliJobLog xxlJobLog) {
		return sqlSessionTemplate.update("FerraliJobLogMapper.updateTriggerInfo", xxlJobLog);
	}

	@Override
	public int updateHandleInfo(FerraliJobLog xxlJobLog) {
		return sqlSessionTemplate.update("FerraliJobLogMapper.updateHandleInfo", xxlJobLog);
	}

	@Override
	public List<FerraliJobLog> pageList(int offset, int pagesize,String jobName, Date triggerTimeStart, Date triggerTimeEnd) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("offset", offset);
		params.put("pagesize", pagesize);
		params.put("jobName", jobName);
		params.put("triggerTimeStart", triggerTimeStart);
		params.put("triggerTimeEnd", triggerTimeEnd);
		return sqlSessionTemplate.selectList("FerraliJobLogMapper.pageList", params);
	}

	@Override
	public int pageListCount(int offset, int pagesize,String jobName, Date triggerTimeStart, Date triggerTimeEnd) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("offset", offset);
		params.put("pagesize", pagesize);
		params.put("jobName", jobName);
		params.put("triggerTimeStart", triggerTimeStart);
		params.put("triggerTimeEnd", triggerTimeEnd);
		Integer result = sqlSessionTemplate.selectOne("FerraliJobLogMapper.pageListCount", params);
		if(result == null){
			return 0;
		}
		return result;
	}
	
}
