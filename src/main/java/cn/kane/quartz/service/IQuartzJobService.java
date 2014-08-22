package cn.kane.quartz.service;

import java.util.Date;
import java.util.Map;

import org.quartz.SchedulerException;

public interface IQuartzJobService {

	/**
	 * 添加job
	 * @param jobName
	 * @param groupName
	 * @param jobClassName
	 * @param cronExp
	 * @param jobDataMap
	 * @return
	 * @author chenqingxiang
	 * @Date 2014年8月21日
	 */
	Date addJob(String jobName,String groupName,String jobClassName,String cronExp,Map<String,Object> jobDataMap)  throws SchedulerException; 
	
	/**
	 * 更新
	 * @param jobName
	 * @param groupName
	 * @param jobClassName
	 * @param cronExp
	 * @param jobDataMap
	 * @return
	 * @author chenqingxiang
	 * @Date 2014年8月21日
	 */
	void updateJob(String jobName,String groupName,String jobClassName,String cronExp,Map<String,Object> jobDataMap)  throws SchedulerException;
	
	/**
	 * 删除
	 * @param jobName
	 * @param groupName
	 * @author chenqingxiang
	 * @Date 2014年8月21日
	 */
	boolean deleteJob(String jobName,String groupName)  throws SchedulerException;
	
	/**
	 * 暂停
	 * @param jobName
	 * @param groupName
	 * @author chenqingxiang
	 * @Date 2014年8月21日
	 */
	void pauseJob(String jobName,String groupName)  throws SchedulerException;
	
	/**
	 * 恢复
	 * @param jobName
	 * @param groupName
	 * @author chenqingxiang
	 * @Date 2014年8月21日
	 */
	void resumeJob(String jobName,String groupName)  throws SchedulerException;

	/**
	 * 立即执行（一次）
	 * @param jobName
	 * @param groupName
	 * @param jobClassName
	 * @param cronExp
	 * @author chenqingxiang
	 * @Date 2014年8月21日
	 */
	void execJobNow(String jobName,String groupName,Map<String,Object> jobDataMap)  throws SchedulerException;
	
}
