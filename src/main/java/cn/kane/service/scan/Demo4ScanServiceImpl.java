package cn.kane.service.scan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class Demo4ScanServiceImpl implements IDemo4ScanService{

	private static final Logger LOGGER = LoggerFactory.getLogger(Demo4ScanServiceImpl.class) ;
	
	public void print (){
		LOGGER.info("Demo4ScanServiceImpl.print");
	}
}
