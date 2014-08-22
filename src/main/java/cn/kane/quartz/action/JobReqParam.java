package cn.kane.quartz.action;

import java.util.ArrayList;
import java.util.List;

public class JobReqParam {

	private String jobName ;
	private String groupName ;
	private String jobClassName ;
	private String cronExp ;
	private List<JobDataMapVO> jobDataMap = new ArrayList<JobDataMapVO>() ;

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getJobClassName() {
		return jobClassName;
	}

	public void setJobClassName(String jobClassName) {
		this.jobClassName = jobClassName;
	}

	public String getCronExp() {
		return cronExp;
	}

	public void setCronExp(String cronExp) {
		this.cronExp = cronExp;
	}

	public List<JobDataMapVO> getJobDataMap() {
		return jobDataMap;
	}

	public void setJobDataMap(List<JobDataMapVO> jobDataMap) {
		this.jobDataMap = jobDataMap;
	}

	
}
