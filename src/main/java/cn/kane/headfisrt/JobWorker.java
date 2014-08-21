package cn.kane.headfisrt;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public class JobWorker implements Job {
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
	
		JobDataMap dataMap = context.getMergedJobDataMap();
		System.out.println(System.currentTimeMillis()+":"+dataMap.getString("param1"));
		
	}
	
}	
	
