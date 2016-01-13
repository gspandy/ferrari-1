/**
 * 
 */
package com.cip.ferrari.admin.dao.impl;

import java.util.HashMap;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.cip.ferrari.admin.core.model.FerrariJobInfo;
import com.cip.ferrari.admin.dao.IFerrariJobInfoDao;

/**
 * @author yuantengkai
 *
 */
@Repository(FerrariJobInfoDaoImpl.BeanName)
public class FerrariJobInfoDaoImpl implements IFerrariJobInfoDao {
	
	public static final String BeanName = "ferrariJobInfoDao";

	@Resource
	public SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public int save(FerrariJobInfo ferrariJobInfo) {
		return sqlSessionTemplate.insert("FerrariJobInfoMapper.save", ferrariJobInfo);
	}
	
	@Override
	public FerrariJobInfo loadJobInfoByGroupAndName(String jobGroup,String jobName){
		if(StringUtils.isBlank(jobGroup) || StringUtils.isBlank(jobName)){
			return null;
		}
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("jobGroup", jobGroup);
		params.put("jobName", jobName);
		return sqlSessionTemplate.selectOne("FerrariJobInfoMapper.loadByGroupAndName", params);
	}

}
