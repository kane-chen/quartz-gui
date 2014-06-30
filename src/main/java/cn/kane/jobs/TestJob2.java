package cn.kane.jobs;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class TestJob2 extends QuartzJobBean{

	private static final Logger logger = LoggerFactory.getLogger(TestJob2.class) ;
	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		logger.info("TestJob2 executeInternal");
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap() ;
		if(null!=jobDataMap){
			logger.info("DESTPATH-"+jobDataMap.get("destPath"));
			for(String key : jobDataMap.getKeys()){
				logger.info("Context JobDataMap:{}-{}",key,jobDataMap.get(key));
			}
		}
		logger.info("JobDetail JobDataMap:{}",jobDataMap);
	}

}
