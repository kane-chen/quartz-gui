package cn.kane.quartz;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.kane.tools.HttpClientUtils;

public class QuartzSchedulerFactory {

	private final static Logger LOGGER = LoggerFactory.getLogger(QuartzSchedulerFactory.class);
	
	/** heart-beat print or not */
	private boolean isPrintHeartBeat = false ;
	/** heart-beat-period */
	private long heartbeatMills = 5*60*1000L;
	/** wait running-job done when shutdown */
	private boolean waitJobToComplete = true ;
	/** quartz-config-file */
	private String quartzPropFileName = "quartz.properties" ;
	
	/** sync-url in quartz-man */
	private String quartzManSyncUrl ;
	/** jmx-host at local */
	private String host ;
	/** jmx-port at local */
	private int port ;
	
	/** singleton */
	private Scheduler schedulerInstance ;
	
	/** instance-lock */
	private Object mutex = new Object();
	/** running-flag */
	private AtomicBoolean isRunning = new AtomicBoolean(false) ;
	
	/**
	 * start up scheduler,ready to service
	 */
	public void startup(){
		LOGGER.info("try to startup Scheduler with[propFileName={}]",quartzPropFileName);
		if(null == schedulerInstance){
			//scheduler instance&start
			synchronized (mutex) {
				if(null==schedulerInstance){//double-check
					try {
						LOGGER.info("try to init quartz-prop from " + quartzPropFileName);
						SchedulerFactory schedulerFactory = new StdSchedulerFactory(quartzPropFileName) ;
						LOGGER.info("try to init-scheduler ");
						schedulerInstance = schedulerFactory.getScheduler() ;
						if(null == schedulerInstance ){
							LOGGER.error("NOT FOUND SCHEDULER IN FACTORY ");
						}else{
							LOGGER.info("try to start scheduler");
							schedulerInstance.start();
							isRunning.set(true);
							LOGGER.info("Scheduler started with[propFileName={}]",quartzPropFileName);
							//heart-beat
							this.startHeartbeatThread();
							//scheduler-sync
							this.syncJobsWhenRestart();
						}
					} catch (SchedulerException e) {
						LOGGER.error("QuartzSchedulerFactory startUp error",e);
					}
				}
			}
		}else{
			LOGGER.warn("quartz-scheduler start already");
		}
	}
	
	private void syncJobsWhenRestart(){
		Map<String,String> params = new HashMap<String,String>() ;
		params.put("host", host) ;
		params.put("port", port+"") ;
		try {
			String resp = HttpClientUtils.call(quartzManSyncUrl, params);
			LOGGER.info("SYNC-RESP:"+resp);
		} catch (Exception e) {
			LOGGER.warn("SCHEDULER-SYNC ERROR",e);
		}
	}
	
	private void startHeartbeatThread(){
		if(isPrintHeartBeat){
			new Thread(new Runnable() {
				public void run() {
					while(isRunning.get()){
						try {
							if(null!=schedulerInstance && schedulerInstance.isStarted()){
								LOGGER.info("Scheduler is running with[propFileName={} ]",quartzPropFileName);
								try {
									Thread.sleep(heartbeatMills);
								} catch (InterruptedException e) {
									LOGGER.warn("Scheduler HeartbeatThread interrupted",e);
								}
							}else{
								LOGGER.warn("Scheduler is not running with[propFileName={} ]",quartzPropFileName);
								break ;
							}
						} catch (SchedulerException e) {
							LOGGER.warn("Scheduler HeartbeatThread error",e);
						}
					}
				}
			},"Scheduler-HeartBeat-Thread").start() ;
		}
	}
	
	@Deprecated
	/**
	 * for test
	 * @return
	 */
	public Scheduler getInstance(){
		return schedulerInstance ;
	}
	
	public void shutdown(){
		LOGGER.info("try to shutdown QuartzSchedulerFactory with[propFileName={} ]",quartzPropFileName);
		if(null!=schedulerInstance){
			try {
				if(!schedulerInstance.isShutdown()){
					schedulerInstance.shutdown(waitJobToComplete);
					isRunning.set(true);
					LOGGER.warn("Scheduler shutdown with[propFileName={} ]",quartzPropFileName);
				}else{
					LOGGER.warn("Scheduler already shutdown with[propFileName={} ]",quartzPropFileName);
				}
			} catch (SchedulerException e) {
				LOGGER.error("Scheduler shutdown error",e);
			}
		}else{
			LOGGER.warn("schedulerInstance is null");
		}
	}

	public boolean isWaitJobToComplete() {
		return waitJobToComplete;
	}

	public void setWaitJobToComplete(boolean waitJobToComplete) {
		this.waitJobToComplete = waitJobToComplete;
	}

	public String getQuartzPropFileName() {
		return quartzPropFileName;
	}

	public void setQuartzPropFileName(String quartzPropFileName) {
		this.quartzPropFileName = quartzPropFileName;
	}

	public boolean isPrintHeartBeat() {
		return isPrintHeartBeat;
	}

	public void setIsPrintHeartBeat(boolean isPrintHeartBeat) {
		this.isPrintHeartBeat = isPrintHeartBeat;
	}

	public long getHeartbeatMills() {
		return heartbeatMills;
	}

	public void setHeartbeatMills(long heartbeatMills) {
		this.heartbeatMills = heartbeatMills;
	}

	public String getQuartzManSyncUrl() {
		return quartzManSyncUrl;
	}

	public void setQuartzManSyncUrl(String quartzManSyncUrl) {
		this.quartzManSyncUrl = quartzManSyncUrl;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
}
