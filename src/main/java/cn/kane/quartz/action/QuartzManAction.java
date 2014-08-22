package cn.kane.quartz.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;

import cn.kane.quartz.QuartzSchedulerFactory;
import cn.kane.quartz.service.IQuartzJobService;
import cn.kane.quartz.service.QuartzJobServiceImpl;

@Controller
@RequestMapping("/")
public class QuartzManAction {
	
	private IQuartzJobService quartzJobService  ;
	
	//spring-ioc
	private void setJobService(HttpServletRequest request){
		ServletContext context = request.getSession().getServletContext();    
		WebApplicationContext ctx  = WebApplicationContextUtils.getWebApplicationContext(context); 
		QuartzSchedulerFactory scheduler = ctx.getBean("schedulerFactory", QuartzSchedulerFactory.class) ;
		QuartzJobServiceImpl quartzJobServiceImpl = new QuartzJobServiceImpl() ;
		quartzJobServiceImpl.setScheduler(scheduler.getInstance());
		quartzJobService = quartzJobServiceImpl ;
	}
	
	//new-instance
//	{
//		QuartzJobServiceImpl quartzJobServiceImpl = new QuartzJobServiceImpl() ;
//		try {
//			quartzJobServiceImpl.setScheduler(new QuartzMan().createScheduler());
//		} catch (SchedulerException e) {
//			e.printStackTrace();
//		}
//		quartzJobService = quartzJobServiceImpl ;
//	}

	@RequestMapping({ "index.xhtml"})
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response) throws Exception{
		if(null == this.quartzJobService){
			this.setJobService(request);
		}
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
		if(null == this.quartzJobService){
			this.setJobService(request);
		}
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
		if(null == this.quartzJobService){
			this.setJobService(request);
		}
		this.quartzJobService.execJobNow(jobName, groupName, null);
		return new ModelAndView("index","msg","exec-success");
	}
	
	@RequestMapping({ "pause.xhtml"})
	public ModelAndView pause(
			@RequestParam(value="jobName")String jobName,
			@RequestParam(value="groupName")String groupName,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
		if(null == this.quartzJobService){
			this.setJobService(request);
		}
		this.quartzJobService.pauseJob(jobName, groupName);
		return new ModelAndView("index","msg","pause-success");
	}
	
	@RequestMapping({ "resume.xhtml"})
	public ModelAndView resume(
			@RequestParam(value="jobName")String jobName,
			@RequestParam(value="groupName")String groupName,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
		if(null == this.quartzJobService){
			this.setJobService(request);
		}
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
		if(null == this.quartzJobService){
			this.setJobService(request);
		}
		Map<String,Object> jobDataMap = this.getJobDataMapByVO(jobParams.getJobDataMap()) ;
		this.quartzJobService.updateJob(jobName, groupName,jobClassName,cronExp,jobDataMap);
		return new ModelAndView("index","msg","resume-success");
	}
	
}
