package com.officedepot.services.rto.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.officedepot.coreservice.http.ClientProperty;
import org.apache.commons.configuration.AbstractConfiguration;
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
	private static final String EVENT_P44 = "TRACS_P44_EVENT";
	private static final String EVENT_COOL = "COOL_EVENT_FINISHED";
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
			isGood = postHttp(apiUrlDebug, json, false, false, eventKey, event);
		}	
	
		if(isApiUrlEnabled){
			isGood = postHttp(apiUrl, json, true, true, eventKey, event);
		}	
		return isGood;
	}
	
    public boolean postHttp(String apiUrl, String json, boolean withBasicAuth, boolean nonValidationPost, String eventKey, final String event) throws UnsupportedEncodingException
    {
    	String logMessage = CLASS_NAME + "postHttp::";
    	String time = "";
    	long startTime = 0;
    	String data = "";
    	String errMsg = "";
    	boolean hasERRMsg = false;
    	boolean hasOKResponse = false;
    	boolean retValue = false;
    	Map<String, String> params = new HashMap<String, String>();
    	params.put("payload", json);

		Map<ClientProperty, String> clientProperties = null;
		// Populating client connection properties only for "cool", "p44" events (not to affect other events)
		if(EVENT_COOL.equals(event) || EVENT_P44.equals(event)) {
			LOGGER.debug(logMessage + "getClientConnectionProperties for: " + event);
			clientProperties = getClientConnectionProperties();
		}

    	startTime = new Date().getTime();

    	
    	LOGGER.debug(logMessage + "REQUEST JSON PAYLOAD: " + json + "::url:" + apiUrl);
    	LOGGER.debug(logMessage + "REQUEST URL: " + "::url:" + apiUrl);
    	
	    try{
	    	if(withBasicAuth){
		    	String auth = apiUrlUsername + ":" + apiUrlPassword;
		    	String encoding = Base64.getEncoder().encodeToString(( auth ).getBytes());
				Header[] headers = { new BasicHeader("Authorization", "Basic " + encoding) };
	    		data = HttpClientExecutor.executePostAndReturnString("rto-notify-service", apiUrl, params, clientProperties, headers);
	    	}else {
	    		data = HttpClientExecutor.executePostAndReturnString("rto-notify-service", apiUrl, params, clientProperties, null);
	    	}
	    	
	    	hasOKResponse = payloadUtil.hasJsonValue(data, payloadUtil.KEY_RESPONSE, payloadUtil.VALUE_RESPONSE);
	    	errMsg = getReturnErrorMessage(data, withBasicAuth, hasOKResponse);
	    	
	    	if (!StringUtils.isEmpty(errMsg)){
	    		LOGGER.error(logMessage + errMsg + data + " ::url: " + apiUrl);
	    		
	    		if (nonValidationPost) {
	    			throw new DataProviderException(logMessage + errMsg + data + " ::url: " + apiUrl);
	    		}
	    	}
	    	
	    	LOGGER.debug(logMessage + "response payload: " + data + "::url:" + apiUrl);
	    	
		} catch (Exception e) {
			
			errMsg = errMsg + " - " + e.getMessage() + " :: ERROR payload : " + data;
			hasERRMsg = true;
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
				
				if (!hasERRMsg && hasOKResponse) {
					retValue = true;
					eventKey = eventKey + "-" + payloadUtil.VALUE_RESPONSE;
				}
				
				new ElasticsearchUtil().writeNotifyResponseToElasticsearchIndex(time, data, errMsg, json, apiUrl, eventKey);
			}
		}
	    
    	return retValue;
    }
    
    public String getReturnErrorMessage(String returnPayload, boolean withBasicAuth, boolean hasOKResponse){
    	
    	String ret = "";
    	
    	if(withBasicAuth){
        	if (StringUtils.isEmpty(returnPayload) || !hasOKResponse){
        		ret = "(Basic Auth):Bad Response from HTTP POST. ";
        	}	
    	}else{
    		
        	if (StringUtils.isEmpty(returnPayload) || !payloadUtil.hasJsonValue(returnPayload, payloadUtil.KEY_HTTP_REPONSEOBJECT, payloadUtil.VALUE_HTTP_REPONSEOBJECT)){
        		ret = "(No Basic Auth):Bad Response from HTTP POST. ";
        	}
    	}
    	return ret;
    }

	// ODNA-214234 --- change to address request timeout issue
	private Map<ClientProperty, String> getClientConnectionProperties() {

		AbstractConfiguration config = ConfigurationManager.getConfigInstance();
		String socketTimeout = config.getString("httpclient.socketTimeout");
		String connectionTimeout = config.getString("httpclient.connectionTimeout");
		String keepAlive = config.getString("httpclient.enableKeepAlive");
		String keepAliveTimeout = config.getString("httpclient.keepAliveTimeout");

		LOGGER.info(CLASS_NAME + "getClientConnectionProperties::socketTimeout: " + socketTimeout);
		LOGGER.info(CLASS_NAME + "getClientConnectionProperties::connectionTimeout: " + connectionTimeout);
		LOGGER.info(CLASS_NAME + "getClientConnectionProperties::enableKeepAlive: " + keepAlive);
		LOGGER.info(CLASS_NAME + "getClientConnectionProperties::keepAliveTimeout: " + keepAliveTimeout);
		// Added client connection properties
		Map<ClientProperty, String> clientProperties = null;
		if(socketTimeout != null) {
			clientProperties = new HashMap<>();
			clientProperties.put(ClientProperty.SOCKET_TIMEOUT, socketTimeout);
		}

		if(connectionTimeout != null) {
			if(clientProperties != null){
				clientProperties = new HashMap<>();
			}
			clientProperties.put(ClientProperty.CONNECTION_TIMEOUT, connectionTimeout);
		}

		if(keepAlive != null) {
			if(clientProperties != null){
				clientProperties = new HashMap<>();
			}
			clientProperties.put(ClientProperty.KEEP_ALIVE, keepAlive);
		}

		if(keepAliveTimeout != null) {
			if(clientProperties != null){
				clientProperties = new HashMap<>();
			}
			clientProperties.put(ClientProperty.KEEP_ALIVE_TIMEOUT, keepAliveTimeout);
		}

		LOGGER.info(CLASS_NAME + "getClientConnectionProperties::clientProperties map: " + clientProperties);
		if(clientProperties != null) {
			LOGGER.info(CLASS_NAME + "getClientConnectionProperties::clientProperties socket timeout: " + clientProperties.get(ClientProperty.SOCKET_TIMEOUT));
			LOGGER.info(CLASS_NAME + "getClientConnectionProperties::clientProperties connection timeout: " + clientProperties.get(ClientProperty.CONNECTION_TIMEOUT));
			LOGGER.info(CLASS_NAME + "getClientConnectionProperties::clientProperties keepAlive: " + clientProperties.get(ClientProperty.KEEP_ALIVE));
			LOGGER.info(CLASS_NAME + "getClientConnectionProperties::clientProperties keepAlive timeout: " + clientProperties.get(ClientProperty.KEEP_ALIVE_TIMEOUT));
		}

		return clientProperties;
	}
 
}
