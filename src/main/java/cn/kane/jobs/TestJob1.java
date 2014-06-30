package cn.kane.jobs;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class TestJob1 extends QuartzJobBean {

	private static final Logger logger = LoggerFactory.getLogger(TestJob1.class) ;
	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		logger.info("JOB1-executeInternal");
		
	}

}
