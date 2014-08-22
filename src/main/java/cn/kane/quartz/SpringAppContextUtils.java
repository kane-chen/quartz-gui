package cn.kane.quartz;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringAppContextUtils  implements ApplicationContextAware{

	private static ApplicationContext applicationContext ;
	
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		SpringAppContextUtils.applicationContext = applicationContext ;
	}
	
	public static ApplicationContext getAppContext(){
		return applicationContext ;
	}
	
	public static <T> T getBeanInstance(String name,Class<T> clazz){
		return applicationContext.getBean(name, clazz) ;
	}

	public static <T> T getBeanInstance(Class<T> clazz){
		return applicationContext.getBean(clazz) ;
	}
	
	public static Object getBeanInstance(String name){
		return applicationContext.getBean(name) ;
	}
}
