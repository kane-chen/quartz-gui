package cn.kane.jobs;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import cn.kane.quartz.SpringAppContextUtils;
import cn.kane.service.scan.IDemo4ScanService;

public class TestJob3 extends QuartzJobBean{

	private static final Logger logger = LoggerFactory.getLogger(TestJob3.class) ;
	
	private IDemo4ScanService demoService ;
	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		logger.info("TestJob3 executeInternal");
		this.getDemoServiceInAppContext() ;
		demoService.print();
	}

	private IDemo4ScanService getDemoServiceInAppContext(){
		if(null != demoService){
			return demoService ;
		}else{
			demoService = SpringAppContextUtils.getBeanInstance(IDemo4ScanService.class) ;
		}
		return demoService ;
	}

}
