/**
 * 
 */
package com.cip.ferrari.core.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yuantengkai job容器管理
 */
public class ContainerJobManager {

	private static final Logger logger = LoggerFactory
			.getLogger(ContainerJobManager.class);

	/**
	 * jobid和job实体的map key-uuid
	 */
	private final ConcurrentHashMap<String, ManagedJob> jobId2JobMap = new ConcurrentHashMap<String, ManagedJob>(
			64);

	/**
	 * job信息和jobid的map key:class_method
	 */
	private final ConcurrentHashMap<String, CopyOnWriteArrayList<String>> jobInfo2JobIdMap = new ConcurrentHashMap<String, CopyOnWriteArrayList<String>>(
			64);

	/**
	 * job实体和运行线程的map
	 */
	private final ConcurrentHashMap<ManagedJob, Thread> job2ThreadMap = new ConcurrentHashMap<ManagedJob, Thread>(
			64);

	private int corePoolSize = 5;
	private int maxPoolSize = 20000;
	private long keepAliveTime = 60;

	private ThreadPoolExecutor threadPoolExecutor;

	public void init() {
		threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
				keepAliveTime, TimeUnit.SECONDS,
				new SynchronousQueue<Runnable>(), new ThreadFactory() {
					AtomicInteger index = new AtomicInteger();

					public Thread newThread(Runnable r) {
						Thread thread = new Thread(r);
						thread.setDaemon(true);
						thread.setName("Ferrari-Job-Thread#"
								+ (index.incrementAndGet()));
						return thread;
					}
				}) {

			@Override
			protected void beforeExecute(Thread t, Runnable r) {
				ContainerJobManager.this.beforeRun(t, (ManagedJob) r);
				super.beforeExecute(t, r);
			}

			@Override
			protected void afterExecute(Runnable r, Throwable t) {
				ContainerJobManager.this.afterRun((ManagedJob) r, t);
				super.afterExecute(r, t);
			}

		};
	}

	/**
	 * job运行前的预操作
	 * 
	 * @param thread
	 * @param job
	 */
	private void beforeRun(Thread thread, ManagedJob job) {
		if (jobInfo2JobIdMap.contains(job.getJobInfo())) {
			jobInfo2JobIdMap.get(job.getJobInfo()).add(job.getUuid());
		} else {
			CopyOnWriteArrayList<String> jobIdList = new CopyOnWriteArrayList<String>();
			jobIdList.add(job.getUuid());
			List<String> list = jobInfo2JobIdMap.putIfAbsent(job.getJobInfo(),
					jobIdList);
			if (list != null) {
				list.add(job.getUuid());
			}
		}

		job2ThreadMap.put(job, thread);// 只在这里做put
	}

	/**
	 * job运行后的尾操作
	 * 
	 * @param job
	 * @param throwable
	 */
	private void afterRun(ManagedJob job, Throwable throwable) {
		jobId2JobMap.remove(job.getUuid());
		if (job2ThreadMap.contains(job)) {
			// TODO
		}
	}

	/**
	 * 运行 job(异步)
	 * 
	 * @param uuid
	 * @param jobName
	 * @param returnUrllist
	 * @param beginTime
	 * @param jobParam
	 */
	public void runJob(String uuid, String jobName, List<String> returnUrllist,
			Date beginTime, Map<String, String> jobParam) {
		if (jobId2JobMap.contains(uuid)) {
			logger.warn("job already in running,jobid=" + uuid + ",jobName="
					+ jobName);
			return;
		}
		ManagedJob job = new ManagedJob(uuid, jobName, returnUrllist,
				beginTime, jobParam);
		job.init();
		jobId2JobMap.put(job.getUuid(), job);// 只在这里做put

		threadPoolExecutor.execute(job);
	}

	/**
	 * 终止job(同步)
	 * 
	 * @param jobuuid
	 */
	public void killJob(String jobuuid) {

	}

	/**
	 * 释放资源
	 */
	public void dispose() {
		List<Runnable> list = threadPoolExecutor.shutdownNow();
		// WARNING 还没有运行的Job，在停止时，只是打Log。
		for (Runnable r : list) {
			ManagedJob job = (ManagedJob) r;
			logger.warn("Cancel Job when shutdown," + job.toString());
		}

		// 对于正在运行的Job，先不处理
	}

}
