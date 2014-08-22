package cn.kane.quartz.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.quartz.SchedulerException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cn.kane.quartz.QuartzMan;
import cn.kane.quartz.service.IQuartzJobService;
import cn.kane.quartz.service.QuartzJobServiceImpl;

@Controller
@RequestMapping("/")
public class QuartzManAction {
	
	private IQuartzJobService quartzJobService  ;
	
	{
		QuartzJobServiceImpl quartzJobServiceImpl = new QuartzJobServiceImpl() ;
		try {
			quartzJobServiceImpl.setScheduler(new QuartzMan().createScheduler());
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		quartzJobService = quartzJobServiceImpl ;
	}

	@RequestMapping({ "index.xhtml"})
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		return new ModelAndView("index","msg","hello-index");
	}

	@RequestMapping({ "toadd.xhtml"})
	public ModelAndView toAdd(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		return new ModelAndView("add");
	}

	@RequestMapping({ "add.xhtml"})
	public ModelAndView add(
			JobReqParam jobParams ,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String,Object> jobDataMap = this.getJobDataMapByVO(jobParams.getJobDataMap()) ;
		this.quartzJobService.addJob(jobParams.getJobName(), jobParams.getGroupName(), jobParams.getJobClassName(), jobParams.getCronExp(), jobDataMap);
		return new ModelAndView("index","msg","add-success");
	}
	
	private Map<String,Object> getJobDataMapByVO(List<JobDataMapVO> jobDataMapVos){
		Map<String,Object> jobDataMap = null ;
		if(null!=jobDataMapVos){
			jobDataMap = new HashMap<String,Object>() ;
			for(JobDataMapVO vo : jobDataMapVos){
				if(null!=vo && null!=vo.getKey()){
					jobDataMap.put(vo.getKey(), vo.getValue()) ;
				}
			}
		}
		return jobDataMap ;
	}
	
	@RequestMapping({ "exec.xhtml"})
	public ModelAndView exec(
			@RequestParam(value="jobName")String jobName,
			@RequestParam(value="groupName")String groupName,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
		this.quartzJobService.execJobNow(jobName, groupName, null);
		return new ModelAndView("index","msg","exec-success");
	}
	
	@RequestMapping({ "pause.xhtml"})
	public ModelAndView pause(
			@RequestParam(value="jobName")String jobName,
			@RequestParam(value="groupName")String groupName,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
		this.quartzJobService.pauseJob(jobName, groupName);
		return new ModelAndView("index","msg","pause-success");
	}
	
	@RequestMapping({ "resume.xhtml"})
	public ModelAndView resume(
			@RequestParam(value="jobName")String jobName,
			@RequestParam(value="groupName")String groupName,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
		this.quartzJobService.resumeJob(jobName, groupName);
		return new ModelAndView("index","msg","resume-success");
	}

	@RequestMapping({ "update.xhtml"})
	public ModelAndView update(
			@RequestParam(value="jobName")String jobName,
			@RequestParam(value="groupName")String groupName,
			@RequestParam(value="jobClassName")String jobClassName,
			@RequestParam(value="cronExp")String cronExp,
			@RequestParam(value="jobParams",required=false)JobReqParam jobParams,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String,Object> jobDataMap = this.getJobDataMapByVO(jobParams.getJobDataMap()) ;
		this.quartzJobService.updateJob(jobName, groupName,jobClassName,cronExp,jobDataMap);
		return new ModelAndView("index","msg","resume-success");
	}
	
}
