package cn.kane.quartz.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class QuartzJobServiceImpl implements IQuartzJobService {

	/**
	 * log
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(QuartzJobServiceImpl.class);
	
	private Scheduler scheduler  ;
	
	@SuppressWarnings("unchecked")
	public Date addJob(String jobName, String groupName, String jobClassName,String cronExp, Map<String, Object> jobDataMap) throws SchedulerException {
		Date nextFireTime = null ;
		boolean isExisted = scheduler.checkExists(JobKey.jobKey(jobName, groupName));
		if(!isExisted){
			Class<Job> jobClazz = null ;
			try {
				jobClazz = (Class<Job>)Class.forName(jobClassName) ;
			} catch (ClassNotFoundException e) {
				LOGGER.error("job-clazz not found", e);
			}
			if(null == jobClazz){
				return null ;
			}
			//jobDatail
			JobDetail jobDetail = JobBuilder.newJob(jobClazz).withIdentity(jobName, groupName).build();
			if(null!=jobDataMap){
				for(String key : jobDataMap.keySet()){
					jobDetail.getJobDataMap().put(key, jobDataMap.get(key));
				}
			}
			//trigger
			Trigger jobTrigger = TriggerBuilder.newTrigger().withIdentity(jobName, groupName).forJob(jobName, groupName).startAt(new Date()).
					withSchedule(CronScheduleBuilder.cronSchedule(cronExp)).build();
			nextFireTime = scheduler.scheduleJob(jobDetail, jobTrigger) ;
		}
		return nextFireTime ;
	}

	public void updateJob(String jobName, String groupName, String jobClassName,String cronExp, Map<String, Object> jobDataMap) throws SchedulerException {
		boolean isExisted = scheduler.checkExists(JobKey.jobKey(jobName, groupName));
		if(isExisted){
			JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobName, groupName));
			Trigger jobTrigger = TriggerBuilder.newTrigger().withIdentity(jobName, groupName).forJob(jobName, groupName).startAt(new Date()).
					withSchedule(CronScheduleBuilder.cronSchedule(cronExp)).build();
			Set<Trigger> triggers = new HashSet<Trigger>() ;
			triggers.add(jobTrigger);
			scheduler.scheduleJob(jobDetail, triggers, true);
		}
	}

	public boolean deleteJob(String jobName, String groupName) throws SchedulerException {
		boolean result = false ;
		boolean isExisted = scheduler.checkExists(JobKey.jobKey(jobName, groupName));
		if(isExisted){
			result = scheduler.deleteJob(JobKey.jobKey(jobName, groupName));
		}
		return result ;
	}

	public void pauseJob(String jobName, String groupName) throws SchedulerException {
		scheduler.pauseJob(JobKey.jobKey(jobName, groupName));
	}

	public void resumeJob(String jobName, String groupName) throws SchedulerException {
		scheduler.resumeJob(JobKey.jobKey(jobName, groupName));

	}

	public void execJobNow(String jobName, String groupName,Map<String,Object> dataMap) throws SchedulerException {
		JobDataMap jobDataMap = null ;
		if(null!=dataMap){
			jobDataMap = new JobDataMap() ;
			for(String key : dataMap.keySet()){
				jobDataMap.put(key, dataMap.get(key));
			}
		}
		scheduler.triggerJob(JobKey.jobKey(jobName, groupName), jobDataMap);
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

}
