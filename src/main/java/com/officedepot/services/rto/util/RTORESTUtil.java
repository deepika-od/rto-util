package com.officedepot.services.rto.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.config.ConfigurationManager;
import com.officedepot.coreservice.http.HttpClientExecutor;
import com.officedepot.servicecore.exceptions.CoreServiceException;
import com.officedepot.servicecore.exceptions.DataProviderException;

public class RTORESTUtil  {
	protected final static Logger LOGGER = LoggerFactory.getLogger(RTORESTUtil.class);
	
	private String apiUrl = ConfigurationManager.getConfigInstance().getString("rto.baseuri");
	private String apiUrlUsername = ConfigurationManager.getConfigInstance().getString("rto.baseuri.username");
	private String apiUrlPassword = System.getenv("RTO_EAI_PASSWORD");
	private String apiUrlDebug = ConfigurationManager.getConfigInstance().getString("rto.baseuri.debug");
	private boolean isApiUrlDebugEnabled = ConfigurationManager.getConfigInstance().getBoolean("rto.baseuri.debug.enabled");
	private boolean isApiUrlEnabled = ConfigurationManager.getConfigInstance().getBoolean("rto.baseuri.enabled");
	
	private static final String CLASS_NAME = "RTORESTUtil::";
	
	private PayloadUtil payloadUtil = new PayloadUtil();

	public RTORESTUtil(){
		init();
	}
	private void init(){
		LOGGER.info(CLASS_NAME + "init::apiUrl: " + apiUrl);
		LOGGER.info(CLASS_NAME + "init::apiUrlUsername: " + apiUrlUsername);
		LOGGER.info(CLASS_NAME + "init::apiUrlDebug: " + apiUrlDebug);
		LOGGER.info(CLASS_NAME + "init::isApiUrlDebugEnabled: " + isApiUrlDebugEnabled);
		LOGGER.info(CLASS_NAME + "init::isApiUrlEnabled: " + isApiUrlEnabled);
	}
	
	public boolean executeNotify(String json, String eventKey) throws Exception,CoreServiceException {
		
		boolean isGood = false;
		JSONObject jsonObject = new JSONObject(json);
		String event = jsonObject.getJSONObject(payloadUtil.KEY_PAYLOAD_ATTRIBUTES).getString(payloadUtil.KEY_PROCESS_EVENT);
		LOGGER.debug(CLASS_NAME + "executeNotify::event: " + event);
		
		if(isApiUrlDebugEnabled){
			isGood = postHttp(apiUrlDebug, json, false, false, eventKey);
		}	
	
		if(isApiUrlEnabled){
			isGood = postHttp(apiUrl, json, true, true, eventKey);
		}	
		return isGood;
	}
	
    public boolean postHttp(String apiUrl, String json, boolean withBasicAuth, boolean nonValidationPost, String eventKey) throws UnsupportedEncodingException
    {
    	String logMessage = CLASS_NAME + "postHttp::";
    	String time = "";
    	long startTime = 0;
    	String data = "";
    	String errMsg = "";
    	Map<String, String> params = new HashMap<String, String>();
    	params.put("payload", json);
    	startTime = new Date().getTime();
    	boolean isGood = false;
    	
    	LOGGER.debug(logMessage + "request json payload: " + json + "::url:" + apiUrl);
    	LOGGER.debug(logMessage + "request url: " + "::url:" + apiUrl);
    	
	    try{
	    	if(withBasicAuth){
		    	String auth = apiUrlUsername + ":" + apiUrlPassword;
		    	String encoding = Base64.getEncoder().encodeToString(( auth ).getBytes());
				Header[] headers = { new BasicHeader("Authorization", "Basic " + encoding) };
	    		data = HttpClientExecutor.executePostAndReturnString("rto-notify-service", apiUrl, params, null, headers);
	    	}else {
	    		data = HttpClientExecutor.executePostAndReturnString("rto-notify-service", apiUrl, params, null, null);
	    	}
	    	
	    	isGood = payloadUtil.hasJsonValue(data, payloadUtil.KEY_RESPONSE, payloadUtil.VALUE_RESPONSE);
	    	errMsg = getReturnErrorMessage(data, withBasicAuth, isGood);
	    	
	    	if (!StringUtils.isEmpty(errMsg)){
	    		LOGGER.error(logMessage + errMsg + data + " ::url: " + apiUrl);
	    		
	    		if (nonValidationPost) {
	    			throw new DataProviderException(logMessage + errMsg + data + " ::url: " + apiUrl);
	    		}
	    	}
	    	
	    	LOGGER.debug(logMessage + "response payload: " + data + "::url:" + apiUrl);
	    	
		} catch (Exception e) {
			
			errMsg = errMsg + " - " + e.getMessage() + " :: ERROR payload : " + data;
			
	    	LOGGER.error(logMessage + errMsg);
	    	LOGGER.error(logMessage + e.getMessage(), e);
			
	    	if (nonValidationPost) {
	    		throw new DataProviderException(errMsg);
	    	}

		} finally {
			LOGGER.debug(logMessage + "finally: ");
			if (nonValidationPost) {
				LOGGER.debug(logMessage + "finally:writeNotifyResponse ");
				time = "0";
				long endTime = new Date().getTime();
				time = new Long(endTime - startTime).toString();
				new ElasticsearchUtil().writeNotifyResponseToElasticsearchIndex(time, data, errMsg, json, apiUrl, eventKey);
			}
		
		}
		return isGood;
    }
    
    public String getReturnErrorMessage(String returnPayload, boolean withBasicAuth, boolean isGood){
    	
    	String ret = "";
    	
    	if(withBasicAuth){
        	if (StringUtils.isEmpty(returnPayload) || !isGood){
        		ret = "(Basic Auth):Bad Response from HTTP POST. ";
        	}	
    	}else{
    		
        	if (StringUtils.isEmpty(returnPayload) || !payloadUtil.hasJsonValue(returnPayload, payloadUtil.KEY_HTTP_REPONSEOBJECT, payloadUtil.VALUE_HTTP_REPONSEOBJECT)){
        		ret = "(No Basic Auth):Bad Response from HTTP POST. ";
        	}
    	}
    	return ret;
    }
 
}
