package cn.kane.service.scan;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.kane.tools.JsonUtil;
import cn.kane.tools.Md5Util;

@Service
public class DataHandlerServiceImpl implements IDataHandlerService {

	private static final Logger logger = LoggerFactory.getLogger(DataHandlerServiceImpl.class) ;
	private static final String ENCRYPT_KEY = "BAIDU_GAME";
	
	public void handle(String jsonStr,String infosName) {
		Map<String,Object> respMap = this.buildInfoInstrance(jsonStr,infosName) ;
		if(null!=respMap){
			logger.info(respMap.toString());
		}
		
	}

	@SuppressWarnings("unchecked")
	private Map<String,Object> buildInfoInstrance(String respInJson,String infosName){
		Map<String,Object> respMap = null ;
		if(StringUtils.isNotBlank(respInJson)){
			respMap = (Map<String,Object>) JsonUtil.json2Object(respInJson, Map.class) ;
			String checkSumInResp = null ;
			try {
				checkSumInResp = Md5Util.md5Encrypted(ENCRYPT_KEY, respMap.get("retCode"),respMap.get("errDesc"),respMap.get(infosName));
			} catch (Exception e) {
				logger.warn("encrypt failed:{}:{}",respInJson,e);
			}
			boolean checkSumValid = false ;
			if(null!=checkSumInResp && checkSumInResp.equals(respMap.get("checkSum"))){
				checkSumValid = true ; 
			}
			
			if(!checkSumValid){
				logger.warn("checkSum failed:{}",respInJson);
				respMap = null ;
			}
		}
		return respMap ;
	}
	
}
