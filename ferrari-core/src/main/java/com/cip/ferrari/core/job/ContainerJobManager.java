/**
 * 
 */
package com.cip.ferrari.core.job;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yuantengkai job容器管理
 */
public class ContainerJobManager {

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
				}){

					@Override
					protected void beforeExecute(Thread t, Runnable r) {
						// TODO Auto-generated method stub
						super.beforeExecute(t, r);
					}

					@Override
					protected void afterExecute(Runnable r, Throwable t) {
						// TODO Auto-generated method stub
						super.afterExecute(r, t);
					}
			
		};
	}
	
	public void runJob() {

	}

	public void dispose() {

	}

}
