/**
 * 
 */
package com.cip.ferrari.admin.dao.impl;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.cip.ferrari.admin.core.model.FerrariJobInfo;
import com.cip.ferrari.admin.dao.IFerrariJobInfoDao;

/**
 * @author yuantengkai
 *
 */
@Repository
public class FerrariJobInfoDaoImpl implements IFerrariJobInfoDao {

	@Resource
	public SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public int save(FerrariJobInfo ferrariJobInfo) {
		return sqlSessionTemplate.insert("FerrariJobInfoMapper.save", ferrariJobInfo);
	}

}
