package cn.kane.jobs;

import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.kane.service.scan.IDataHandlerService;
import cn.kane.tools.HttpClientUtils;
import cn.kane.tools.SpringAppContextUtils;

public class RemoteDataJob implements Job{

	private static final Logger logger = LoggerFactory.getLogger(RemoteDataJob.class);
	
	private IDataHandlerService dataHandlerServie ;
	
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		logger.info("RemoteDataJob start");
		JobDataMap jobDataMap = context.getMergedJobDataMap() ;
		String remServerUrl = jobDataMap.getString("remServerUrl");
		String infosName = jobDataMap.getString("infosName") ;
		try {
			String respInJson = HttpClientUtils.call(remServerUrl, null) ;
			if(StringUtils.isNotBlank(respInJson)){
				this.getDdatHandlerServiceInAppContext().handle(respInJson, infosName);
			}
		} catch (Exception e) {
			logger.error("DataHandler error",e);
		}
		logger.info("RemoteDataJob end");
	}

	private IDataHandlerService getDdatHandlerServiceInAppContext(){
		if(null != dataHandlerServie){
			return dataHandlerServie ;
		}else{
			dataHandlerServie = SpringAppContextUtils.getBeanInstance(IDataHandlerService.class) ;
		}
		return dataHandlerServie ;
	}

}
