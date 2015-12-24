/**
 * 
 */
package com.cip.ferrari.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.cip.ferrari.core.common.JobConstants;
import com.cip.ferrari.core.job.ContainerJobManager;
import com.cip.ferrari.core.job.DirectionType;
import com.cip.ferrari.core.job.result.FerrariFeedback;

/**
 * @author yuantengkai Ferrari job运行对外facade
 */
public class FerrariRunnerFacade {

	private static final Logger logger = LoggerFactory
			.getLogger(FerrariRunnerFacade.class);

	private ContainerJobManager containerJobManager;

	public void init() {
		containerJobManager = new ContainerJobManager();
		containerJobManager.init();
	}

	public String request(Map<String, String> param) {
		// 保护性拷贝
		Map<String, String> jobParam = new HashMap<String, String>(param);
		FerrariFeedback result = new FerrariFeedback();
		result.setStatus(true);
		final String command = jobParam.remove(JobConstants.KEY_ACTION);
		try {
			// 运行job
			if (JobConstants.VALUE_ACTION_RUN_JOB.equalsIgnoreCase(command)) {
				String uuid = jobParam.remove(JobConstants.KEY_UUID);
				result.setDirectionType(DirectionType.RUN_JOB);
				result.setUuid(uuid);
				String jobName = jobParam.remove(JobConstants.KEY_JOB_NAME);
				String returnUrls = jobParam
						.remove(JobConstants.KEY_RESULT_URL_LIST);

				List<String> returnUrllist = new ArrayList<String>();
				String[] returnUrlArray = returnUrls.split(",");
				for (String s : returnUrlArray) {
					returnUrllist.add(s);
				}
				String beginTimeStr = jobParam
						.remove(JobConstants.KEY_BEGIN_TIME);
				Long beginTime = null;
				if (StringUtils.isBlank(beginTimeStr)) {
					beginTime = new Date().getTime();
				} else {
					try {
						beginTime = Long.parseLong(beginTimeStr);
					} catch (Exception e) {
						beginTime = new Date().getTime();
					}
				}
				containerJobManager.runJob(uuid, jobName, returnUrllist,
						beginTime, jobParam);
			}
			// 终止job
			else if (JobConstants.VALUE_ACTION_KILL_JOB
					.equalsIgnoreCase(command)) {
				String uuid = jobParam.remove(JobConstants.KEY_UUID);
				result.setDirectionType(DirectionType.KILL_JOB);
				result.setUuid(uuid);
				containerJobManager.killJob(uuid);
			}
			// 未知指令
			else {
				result.setStatus(false);
				result.setDirectionType(null);
				result.setErrormsg("Unknow command(" + command + ")!");
			}
		} catch (Throwable t) {
			logger.error("Exception when doing command:" + command + param, t);
			result.setStatus(false);
			result.setErrormsg(StringUtils.substring(t.toString(), 0, 2048));
		}
		return JSON.toJSONString(result);
	}

	public void destroy() {
		containerJobManager.dispose();
	}

}
