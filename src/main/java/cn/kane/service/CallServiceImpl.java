package cn.kane.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CallServiceImpl implements ICallService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CallServiceImpl.class) ;
	
	
	public void call() {
		LOGGER.info("CallServiceImpl.call");
	}

}
