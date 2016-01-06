package com.cip.ferrari.admin.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.cip.ferrari.admin.core.model.FerrariJobLog;
import com.cip.ferrari.admin.dao.IFerrariJobLogDao;

@Repository
public class FerrariJobLogDaoImpl implements IFerrariJobLogDao {
	
	@Resource
	public SqlSessionTemplate sqlSessionTemplate;

	@Override
	public int save(FerrariJobLog ferrariJobLog) {
		return sqlSessionTemplate.insert("FerraliJobLogMapper.save", ferrariJobLog);
	}

	@Override
	public FerrariJobLog load(int id) {
		return sqlSessionTemplate.selectOne("FerraliJobLogMapper.load", id);
	}

	@Override
	public int updateTriggerInfo(FerrariJobLog ferrariJobLog) {
		return sqlSessionTemplate.update("FerraliJobLogMapper.updateTriggerInfo", ferrariJobLog);
	}

	@Override
	public int updateHandleInfo(FerrariJobLog ferrariJobLog) {
		return sqlSessionTemplate.update("FerraliJobLogMapper.updateHandleInfo", ferrariJobLog);
	}

	@Override
	public List<FerrariJobLog> pageList(int offset, int pagesize,String jobName, Date triggerTimeStart, Date triggerTimeEnd) {
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
