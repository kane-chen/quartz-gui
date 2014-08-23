package cn.kane.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtils {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(HttpClientUtils.class) ;
	
	private static final String CHARSET_UTF8 = "UTF-8" ; 
	/** socket-timeout,unit:milliseconds */
	private static int  soTimeout = 5000 ; 
	/** request-timeout,unit:milliseconds */
	private static int reqTimeout = 3000 ;

	public static String call(String url,Map<String,String> params) throws Exception{
		String resp = null ;
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().build() ;
		//params
		List<NameValuePair> paramPairs = new ArrayList<NameValuePair>() ;
		for(String key : params.keySet()){
			paramPairs.add(new BasicNameValuePair(key, params.get(key))) ;
		}
		//execute
        try {
        	HttpPost httpPost = new HttpPost(url) ;
        	//config(timeout)
        	RequestConfig reqConfig =  RequestConfig.custom()  
        		    .setConnectTimeout(soTimeout)  
        		    .setSocketTimeout(reqTimeout)
        		    .build();   ;
		    httpPost.setConfig(reqConfig);
        	//header
        	UrlEncodedFormEntity entity;
            entity = new UrlEncodedFormEntity(paramPairs, CHARSET_UTF8);
            httpPost.setEntity(entity);
            //execute
            HttpResponse httpResponse = httpClient.execute(httpPost);
            //response
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
            	resp = EntityUtils.toString(httpEntity, CHARSET_UTF8) ;
            }
        } catch (Exception e) {
        	LOGGER.error("Call Error",e);
        	throw new Exception(e) ;
        }finally{
        	//release
        	try{
        		httpClient.close();
        	}catch(IOException e){
        		LOGGER.error("httpClient release-resource error",e);
        	}
        }
        return resp ;
	}
	
	public static void main(String[] args) throws Exception{
		String url = "http://pk.baidu.com/i/ajax_user_signin.xhtml" ;
		Map<String,String> params = new HashMap<String,String>() ;
		params.put("c", "signin") ;
		params.put("plat", "13") ;
		String resp = call(url,params) ;
		System.out.println(resp);
	}
	
}
