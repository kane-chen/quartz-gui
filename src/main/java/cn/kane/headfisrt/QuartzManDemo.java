package cn.kane.headfisrt;

import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzManDemo {

	private static final String fileName = "quartz.properties";
	private static final String jobName = "testJob";
	private static final String groupName = "testGroup";

	public static void main(String[] args) throws SchedulerException, InterruptedException {
		//quartz-start
		SchedulerFactory schedulerFactory = new StdSchedulerFactory(fileName);
		Scheduler scheduler = schedulerFactory.getScheduler();
		scheduler.start();
		System.out.println("======= quartz started ======");
		//add job
		boolean isExisted = scheduler.checkExists(JobKey.jobKey(jobName, groupName));
		if(isExisted){
			System.out.println("======= job existed ======");
		}else{
			//jobDatail
			JobDetail jobDetail = JobBuilder.newJob(JobWorker.class).withIdentity(jobName, groupName).build();
			jobDetail.getJobDataMap().put("param1", "value1");
			jobDetail.getJobDataMap().put("param2", "value2");
			jobDetail.getJobDataMap().put("param3", "value3");
			//trigger
			Trigger jobTrigger = TriggerBuilder.newTrigger().withIdentity(jobName, groupName).forJob(jobName, groupName).startAt(new Date()).
					withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(3).repeatForever()).build();
			Date startTime = scheduler.scheduleJob(jobDetail, jobTrigger) ;
			System.out.println("===== job started at====="+startTime);
			isExisted = scheduler.checkExists(JobKey.jobKey(jobName, groupName));
			System.out.println("======= job exist ? ======"+isExisted);
		}
		//-----------pause Job-----------
		scheduler.pauseJob(JobKey.jobKey(jobName, groupName));
		System.out.println("===== job paused at ====="+System.currentTimeMillis());
		//sleep random
		long sleepMills = 5000+new Random().nextInt(5000) ;
		Thread.sleep(sleepMills);
		System.out.println("====== sleep end at ======"+System.currentTimeMillis());

		// ---------- resume ---------
		scheduler.resumeJob(JobKey.jobKey(jobName, groupName));
		System.out.println("==== job resume at ===="+System.currentTimeMillis());
		
		// ---------- manual-exec-now ------------ 
		JobDataMap jobDataMap = new JobDataMap() ;
		jobDataMap.put("param1", "newValue1");
		scheduler.triggerJob(JobKey.jobKey(jobName, groupName), jobDataMap);
		System.out.println("===== job execed manual at=====" + System.currentTimeMillis());
		
		
		//---------- refresh-conf ---------
		JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobName, groupName));
		jobDetail.getJobDataMap().put("param1", "parma1-new");
		Trigger newJobTrigger = TriggerBuilder.newTrigger().withIdentity(jobName, groupName).forJob(jobName, groupName).startAt(new Date()).
				withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever()).build();
		Set<Trigger> triggers = new HashSet<Trigger>() ;
		triggers.add(newJobTrigger);
		scheduler.scheduleJob(jobDetail, triggers, true);
		System.out.println("==== job conf updated at ===="+System.currentTimeMillis());
	}

}
