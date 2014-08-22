package cn.kane.quartz;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerListener;
import org.quartz.SchedulerMetaData;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuartzMan {

	private final static Logger LOGGER = LoggerFactory.getLogger(QuartzMan.class);

	private boolean isPrint = false;
	private String propFileName = "quartz.properties";
	private SchedulerListener schedulerListener;

	private Scheduler scheduler;

	public Scheduler createScheduler() throws SchedulerException{
		this.init(); 
		scheduler.start();
		return scheduler ;
	}
	
	public void init() {
		LOGGER.info("ScheduleServer :: init");
		try {
			SchedulerFactory sf = new StdSchedulerFactory(propFileName);
			scheduler = sf.getScheduler();
			if (getSchedulerListener() != null) {
				scheduler.getListenerManager().addSchedulerListener(getSchedulerListener());
			}
		} catch (SchedulerException ex) {
			LOGGER.error(ex.getMessage(), ex);
		}
	}

	public void start() {
		if (scheduler == null) {
			this.init();
		}
		try {
			LOGGER.info("ScheduleServer :: start");
			scheduler.start();
		} catch (SchedulerException ex) {
			LOGGER.error(ex.getMessage(), ex);
		}
		if (isPrint) {
			long counter = 0L;
			while (scheduler != null) {
				try {
					Thread.sleep(5 * 1000L);
					if (counter > 0xFFFFFFFFL)
						counter = 0L;
					++counter;
					if (counter % 720 == 0)
						LOGGER.info("ScheduleServer :: living");
				} catch (InterruptedException ex) {
					LOGGER.error(ex.getMessage(), ex);
				}
			}
		}
	}

	public void stop() {
		if (scheduler != null) {
			LOGGER.info("ScheduleServer :: stop");
			try {
				scheduler.shutdown(true);
				SchedulerMetaData metaData = scheduler.getMetaData();
				LOGGER.info("ScheduleServer :: Executed {} jobs.", metaData.getNumberOfJobsExecuted());
			} catch (SchedulerException ex) {
				LOGGER.error(ex.getMessage(), ex);
			}
			scheduler = null;
		}
	}

	public void restart() {
		if (scheduler != null) {
			try {
				if (!scheduler.isInStandbyMode()) {
					scheduler.standby();
				}
				scheduler.start();
			} catch (SchedulerException ex) {
				LOGGER.error(ex.getMessage(), ex);
			}
		}
	}

	public String getPropFileName() {
		return propFileName;
	}

	public void setPropFileName(String propFileName) {
		this.propFileName = propFileName;
	}

	public SchedulerListener getSchedulerListener() {
		return schedulerListener;
	}

	public void setSchedulerListener(SchedulerListener schedulerListener) {
		this.schedulerListener = schedulerListener;
	}

}
