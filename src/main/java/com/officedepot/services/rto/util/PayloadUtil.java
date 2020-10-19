package com.officedepot.services.rto.util;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.config.ConfigurationManager;

public class PayloadUtil extends JSONUtil {
	private static final Logger logger = LoggerFactory.getLogger(PayloadUtil.class);
	
	private static final int PROCESS_LIMIT = ConfigurationManager.getConfigInstance().getInt("rto.maxAttemptsToProcess");
	private static final String CLASS_NAME = "PayloadUtil";

	
	//Destination Values
	public String VALUE_DESTINATION_KEY_ECOM = "ecom";
	public String VALUE_DESTINATION_KEY_SCM = "scm";
	public String VALUE_DESTINATION_KEY_PERFECT = "perfect";
	public String VALUE_DESTINATION_KEY_WARP = "warp";
	public String VALUE_DESTINATION_KEY_DTS = "dts";
	
	//Level 1
	public String KEY_PAYLOAD_ATTRIBUTES = "payloadAttributes";
	public String KEY_ORDER_HEADER = "orderHeader";
	public String KEY_POM_HEADER = "pomHeader";
	public String KEY_ORDER_LINES = "orderLines";
	public String KEY_TRACKING_NUMBERS = "lineTrackingNumbers";
	public String KEY_ADD_VALUES = "addValues";	
	public String KEY_DTS_CARTON = "carton";
	public String KEY_DTS_EVENT = "event";

	//Level 2
	String KEY_PROCESS_COUNT = "processCount";
	public String KEY_PROCESS_EVENT = "processEvent";
	public String KEY_PROCESS_EVENT_DESTINATION_KEY = "processDestinationKey";
	public String KEY_PROCESS_MESSAGE = "processMessage";
	public String KEY_PROCESS_TIMESTAMP = "processTimeStamp";
	public String KEY_ORDER_KEY = "processOrderKey";
	public String KEY_PROCESS_NOTIFY_RESPONES_TIME = "processNotifyResponseTime";
	public String KEY_PROCESS_NOTIFY_RESPONES_MSG = "processNotifyResponseMsg";
	public String KEY_PROCESS_NOTIFY_URL = "processNotifyURL";
	public String KEY_PAYLOAD_ATTRIBUTES_SOURCE = "source";
	public String KEY_PAYLOAD_ATTRIBUTES_CUST_CUSTOMER_TYPE = "custCustomerType";
	public String KEY_RECORD_ID = "docID";
	public String KEY_RECORD_DOC_TYPE = "docType";
	public String KEY_RECORD_INDEX_NAME = "indexName";
	
	public String KEY_ORDERDATE = "orderDate";
	public String KEY_ORDERTYPE = "orderType";
	public String KEY_VWORDERTYPE = "vwOrderType";
	public String KEY_BILLCOMPLETEFLAG = "billCompleteFlag";
	public String KEY_ORDERSTATUS = "orderStatus";
	public String KEY_ACTIONCODE = "actionCode";
	public String KEY_FORCESENT = "forceSent";
	public String KEY_DROPSHIPFLAG = "dropShipFlag";
	public String KEY_DEADLETTERRETRYSOURCE = "deadLetterRetrySource";
	public String KEY_SALELOCID ="saleLocId";
	public String KEY_ORDERDELCODE ="orderDelCode";
	public String KEY_TRACKINGID ="trackingId";	
	public String KEY_DTS_TRACKINGNUMBER ="trackingNumber";
	public String KEY_CARRIER ="carrier";	
	public String KEY_ISWHOLESALE ="isWholeSale";
	public String KEY_ISDROPSHIP ="isDropShip";
	public String KEY_ORDERNUMBER ="orderNumber";
	//public String KEY_EXTORDERNUMBER ="extOrdNumber";
	public String KEY_THIRD_PARTY_ORDER ="thirdPartyOrder";
	public String KEY_IS_OFFLINE_ORDER ="isOffLineWARPOrder";
	public String KEY_TIMESTAMP = "timestamp";

	 
	public String KEY_BACKORDER_QUANTITY = "backorderQuantity";
	public String KEY_SHIP_QUANTITY = "shipQuantity";
	public String KEY_QUANTITY = "quantity";
	
	public String KEY_SOURCE_APP = "SOURCE-APP";
	public String KEY_SENDER = "sender";
	public String KEY_SOURCE = "source";
	public String KEY_DTS_EVENT_SCANTIMESTAMP = "scanTimestamp";
	
	// VALUES
	public String VALUE_SENDER_DTS = "dts";
	public String VALUE_TECHSALESNC = "TECHSALENC";
	public String VALUE_ELYNXX		= "ELYNXX";
	
	
	public String VALUE_KEY_FORCESENT_SCM ="scm";
	public String VALUE_EVENT_FORCESENT_BYPASS_EVENTS = "bypass-events";
	public String VALUE_EVENT_FORCESENT_LATEST_RECORD = "latest-record";
	
	public String VALUE_DEADLETTERRETRYSOURCE = "rto-admin-service";
	public String VALUE_PAYLOAD_ATTRIBUTES_SOURCE_POS = "pos";
	public String VALUE_PAYLOAD_ATTRIBUTES_SOURCE_WARP = "warp";
		
	public String VALUE_EXTERNAL = "external";
	
	public String VALUE_DIRECT = "direct";
	public String VALUE_CONTRACT = "contract";
	public String VALUE_UNDEFINED = "undefinedtype";
	
	public static final String R_CUST_CUSTOMER_TYPE = "r";
	public static final String C_CUST_CUSTOMER_TYPE = "c";
	public static final String BLANK_CUST_CUSTOMER_TYPE = "";	
			
	//http respone objects
	public String KEY_HTTP_REPONSEOBJECT = "responseObject";
	public String VALUE_HTTP_REPONSEOBJECT = "success";
	public String KEY_RESPONSE = "response";
	public String VALUE_RESPONSE = "ok";
	
	String KEY_PARENT_ORDER_NUMBER = "parentOrder";
	String KEY_ORDER_NUMBER = "orderNumber";
	String KEY_ORDER_SUBNUMBER = "ordersubNumber";
	String KEY_SENT_TIMESTAMP_KEYWORD = "sentTimestamp.keyword";
	String KEY_SENT_TIMESTAMP = "sentTimestamp";
	String KEY_ACCOUNTID = "accountId";
	String KEY_UNIQUEID = "uniqueID";
	String DEFAULT_VALUE_KINESIS_KEY = "1234567";
	String KEY_LOYALTYID = "loyaltyId";
	
	private static final String DATE_FORMAT = "yyyy-MM-dd.HH.mm:ss.SSSSSS";
	public final String BAD_DATE_SUBSTITUTE = "9999-01-01";
	
	/*
	* Accepts a timestamp mask: YYYY-MM-DD-hh.mm.ss.yyyyyy
	* e.g:                      2019-07-15-03.15.36.940144
	* UTC:                      2019-07-15T03:15:36.940144
	* returns number of microsecs since midnight Jan 1st 1970
	*/
	String timestamp2microsec (String timestamp){
	String utcTimestamp = timestamp.substring(0, 10) + "T" + 
			timestamp.substring(11, 13) + ":" + 
			timestamp.substring(14, 16) + ":" + 
			timestamp.substring(17, 19) + "." + 
			timestamp.substring(20, 26) + "Z";
	String micro = timestamp.substring(23, 26);

	Instant inst = Instant.parse(utcTimestamp);
	String instMicro = "" + inst.toEpochMilli() + micro;
	return instMicro;
	}
	
	public String getSmallPayload(String json){
		
 		String retJson = "";
		JSONObject jsonObject = new JSONObject(json);
 		
 		retJson = jsonObject.getJSONObject(KEY_PAYLOAD_ATTRIBUTES).toString();
		retJson = "{\"" + KEY_PAYLOAD_ATTRIBUTES + "\": " + retJson + ","
				 + "\"" + KEY_ORDER_HEADER + "\": {}"
				+ "}";
 		
 		logger.debug(CLASS_NAME + "::getSmallPayload::retJson = " + retJson);
 		
 		return retJson;
	}
	
	public String addSmallOrderHeaderToJSON(String smallJSON, String orderJSON){

		if (isJsonValuePresent(orderJSON, KEY_ORDER_HEADER, KEY_ORDER_NUMBER)){
			String orderNumber = getValueFromJSON(orderJSON, KEY_ORDER_HEADER, KEY_ORDER_NUMBER);
			smallJSON = addKeyValueToJSON(smallJSON, KEY_ORDER_HEADER, KEY_ORDER_NUMBER, orderNumber);
		}
		
		if (isJsonValuePresent(orderJSON, KEY_ORDER_HEADER, KEY_ORDER_NUMBER)){
			String orderNumber = getValueFromJSON(orderJSON, KEY_ORDER_HEADER, KEY_ORDER_NUMBER);
			smallJSON = addKeyValueToJSON(smallJSON, KEY_ORDER_HEADER, KEY_ORDER_NUMBER, orderNumber);
		}
		
		if (isJsonValuePresent(orderJSON, KEY_ORDER_HEADER, KEY_ORDER_SUBNUMBER)){
			String orderSubNumber = getValueFromJSON(orderJSON, KEY_ORDER_HEADER, KEY_ORDER_SUBNUMBER);
			smallJSON = addKeyValueToJSON(smallJSON, KEY_ORDER_HEADER, KEY_ORDER_SUBNUMBER, orderSubNumber);
		}

		if(isJsonValuePresent( smallJSON, KEY_ORDER_HEADER, KEY_ACCOUNTID)){
			String accountId = getValueFromJSON(orderJSON, KEY_ORDER_HEADER, KEY_ACCOUNTID);
			smallJSON = addKeyValueToJSON(smallJSON, KEY_ORDER_HEADER, KEY_ACCOUNTID, accountId);
		}
	
 		logger.debug(CLASS_NAME + "::getSmallOrderHeader::smallJSON = " + smallJSON);

 		return smallJSON;
	}

	public String createJSONWithEventInfo(String event, String destinationKey, String key){
		
		JSONObject jsonObject1 = new JSONObject();
		jsonObject1.put(KEY_PROCESS_EVENT, event);
		jsonObject1.put(KEY_PROCESS_EVENT_DESTINATION_KEY, destinationKey);
		jsonObject1.put(KEY_UNIQUEID, key);
		
		JSONObject jsonObject2 = new JSONObject();
		jsonObject2.put(KEY_PAYLOAD_ATTRIBUTES, jsonObject1);

		return jsonObject2.toString();
	}

	
	public String addProcessMessageInJSON(String msg){
		
		JSONObject jsonObject1 = new JSONObject();
		jsonObject1.put(KEY_PROCESS_MESSAGE, msg);

		JSONObject jsonObject2 = new JSONObject();
		jsonObject2.put(KEY_PAYLOAD_ATTRIBUTES, jsonObject1);
		
		return jsonObject2.toString();
	}
	
	
	public String addProcessTimeStampInJSON(String json){
		JSONObject jsonObject = new JSONObject(json);
		
		TimeZone.setDefault(TimeZone.getTimeZone("EST"));

		jsonObject.getJSONObject(KEY_PAYLOAD_ATTRIBUTES).put(KEY_PROCESS_TIMESTAMP, new SimpleDateFormat(DATE_FORMAT).format(new Date()));

		return jsonObject.toString();
	}	
	
	public String addTimeStampInJSON(String json){
		JSONObject jsonObject = new JSONObject(json);
		
		TimeZone.setDefault(TimeZone.getTimeZone("EST"));

		jsonObject.put(KEY_TIMESTAMP, new SimpleDateFormat(DATE_FORMAT).format(new Date()));

		return jsonObject.toString();
	}
	
	
	public String createPayloadForDestinationKey(String json, String destinationKey){
		
		String returnJSON = json;
				
		if (destinationKey.equalsIgnoreCase(VALUE_DESTINATION_KEY_WARP)) {

			ElasticsearchUtil elasticUtil = new ElasticsearchUtil();

			String id = getMasterIndexIdByOrderField(json, KEY_THIRD_PARTY_ORDER, VALUE_DESTINATION_KEY_WARP.toUpperCase());
			String docType = "_doc";	
			String indexName = elasticUtil.getMasterIndexName(getMasterRecordSuffix(json));
			
			JSONObject jsonObject = new JSONObject("{}");
			jsonObject.put(KEY_RECORD_ID, id);
			jsonObject.put(KEY_RECORD_DOC_TYPE, docType);
			jsonObject.put(KEY_RECORD_INDEX_NAME, indexName);
			
			logger.debug(CLASS_NAME + "::createPayloadForDestinationKey::KEY_RECORD_ID = " + id);
			logger.debug(CLASS_NAME + "::createPayloadForDestinationKey::KEY_RECORD_DOC_TYPE = " + docType);
			logger.debug(CLASS_NAME + "::createPayloadForDestinationKey::KEY_RECORD_INDEX_NAME = " + indexName);
			
			returnJSON = jsonObject.toString();
			
			logger.debug(CLASS_NAME + "::createPayloadForDestinationKey::returnJSON = " + returnJSON);
			
		}
		
		return returnJSON;
	}	
	
	public String getMasterRecordSuffix(String recordJson) {
		String indexSuffix = "";
		String dateSuffix = "";
		
		if (isDTS(recordJson)) {
			dateSuffix = getScanDate(recordJson);
		} else {
			dateSuffix = getOrderDate(recordJson);
		}
		
		indexSuffix = indexSuffix + dateSuffix.substring(0, 4);
		
		logger.debug("getMasterRecordSuffix: " + indexSuffix);
		
		String custCustomerTypeSuffix = getIndexNamePrefixForCustCustomerType(recordJson);
		indexSuffix = custCustomerTypeSuffix + "_" + indexSuffix;
		
		return indexSuffix;
		
	}
	
	public String addProcessMessageInJSON(String msg, String json){
		JSONObject jsonObject = new JSONObject(json);
		
		jsonObject.getJSONObject(KEY_PAYLOAD_ATTRIBUTES).put(KEY_PROCESS_MESSAGE, msg);

		return jsonObject.toString();
	}
	
	
	public String incrementProcessCountInJSON(String json){
		JSONObject jsonObject = new JSONObject(json);
		
		if (jsonObject.getJSONObject(KEY_PAYLOAD_ATTRIBUTES).has(KEY_PROCESS_COUNT)){
			jsonObject.getJSONObject(KEY_PAYLOAD_ATTRIBUTES).increment(KEY_PROCESS_COUNT);
		}else{		
			jsonObject.getJSONObject(KEY_PAYLOAD_ATTRIBUTES).put(KEY_PROCESS_COUNT, 1);				
		}
		
		//logger.debug(jsonObject.toString());
		return jsonObject.toString();
	}
	
	

	public boolean maxProcessAttempsExceeded(String json){
		
		boolean ret = false;
		int maxRetries = 1;
		JSONObject jsonObject = new JSONObject(json);
		
		if (jsonObject.getJSONObject(KEY_PAYLOAD_ATTRIBUTES).has(KEY_PROCESS_COUNT)){	
			maxRetries = jsonObject.getJSONObject(KEY_PAYLOAD_ATTRIBUTES).getInt(KEY_PROCESS_COUNT);				
		}
		
		logger.debug(KEY_PROCESS_COUNT + ": " + maxRetries);
		
		if (maxRetries >= PROCESS_LIMIT ){
			ret = true;
		}
		
		return ret;
	}
	
	public String addEventInJSON(String json, String eventName, String destinationKey){
		
		JSONObject jsonObject = new JSONObject(json);
		
		jsonObject.getJSONObject(KEY_PAYLOAD_ATTRIBUTES).put(KEY_PROCESS_EVENT, eventName);
		jsonObject.getJSONObject(KEY_PAYLOAD_ATTRIBUTES).put(KEY_PROCESS_EVENT_DESTINATION_KEY, destinationKey);
			
		//logger.debug(jsonObject.toString());	
		return jsonObject.toString();
	}
	
	public Map<String, String> getDocumentIdentifiers(String json){
		logger.debug("inside getDocumentIdentifiers");
		logger.debug("json: " + json);
		JSONObject jsonObject = new JSONObject(json);
		Map<String,String> map = new HashMap<String,String>();
		logger.debug("isDTS " + isDTS(json));
		
		if(isDTS(json)) {
			map.put(getFullyQualifiedName(KEY_DTS_CARTON, KEY_DTS_TRACKINGNUMBER), jsonObject.getJSONObject(KEY_DTS_CARTON).getString(KEY_DTS_TRACKINGNUMBER));
		} else {
			map.put(getFullyQualifiedName(KEY_ORDER_HEADER, KEY_ORDER_NUMBER), jsonObject.getJSONObject(KEY_ORDER_HEADER).getString(KEY_ORDER_NUMBER));
			map.put(getFullyQualifiedName(KEY_ORDER_HEADER, KEY_ORDER_SUBNUMBER), jsonObject.getJSONObject(KEY_ORDER_HEADER).getString(KEY_ORDER_SUBNUMBER));
			map.put(getFullyQualifiedName(KEY_ORDER_HEADER, KEY_ACCOUNTID), jsonObject.getJSONObject(KEY_ORDER_HEADER).getString(KEY_ACCOUNTID));
		}
		
		logger.debug("getDocumentIdentifiers: " + map); 
		return map;
	}
	
	public String getOrderSourceWithDefault(String json, String matchString, String defaultString){

		String ret = defaultString;
		String source = getOrderSource(json);
		
		if(source.equalsIgnoreCase(matchString) ) {
			ret = matchString.toLowerCase().trim();
		}
		
		//logger.debug("---->>>"+CLASS_NAME+"::getOrderSourceWithDefault:source " + source); 
		return ret;
	}

	public String getIndexNamePrefixForCustCustomerType(String json){

		String returnValue;
		
		if(isDTS(json)) {
			returnValue="";
		}else {
		
			String lCustCustomerType = getCustCustomerType(json).toLowerCase();
			
			switch(lCustCustomerType) {
				case R_CUST_CUSTOMER_TYPE: 
					returnValue = VALUE_DIRECT;
					break;
				case C_CUST_CUSTOMER_TYPE:
					returnValue = VALUE_CONTRACT;
					break;
				case BLANK_CUST_CUSTOMER_TYPE:
				default:
					returnValue =  VALUE_UNDEFINED;
			}
		}
		
		return returnValue;
	}
	
	public String getCustCustomerType(String json){

		String custCustomerType = "";
		JSONObject jsonObject = new JSONObject(json);
		try {
			custCustomerType = jsonObject.getJSONObject(KEY_ORDER_HEADER).getString(KEY_PAYLOAD_ATTRIBUTES_CUST_CUSTOMER_TYPE);
		} catch (JSONException e) {
			logger.info("custCustomerType attribute missing from the payload, " + json);
		}
		
		//logger.debug(CLASS_NAME+"::getCustCustomerType:custCustomerType " + custCustomerType); 
		return custCustomerType.trim();
	}
    
	public String getOrderSource(String json){

		JSONObject jsonObject = new JSONObject(json);
		String source = jsonObject.getJSONObject(KEY_PAYLOAD_ATTRIBUTES).getString(KEY_PAYLOAD_ATTRIBUTES_SOURCE);
		
		//logger.debug(CLASS_NAME+"::getOrderSource:source " + source); 
		return source.trim();
	}
	
	public boolean isDTS(String json){
		
		boolean ret = false;
		JSONObject jsonObject = new JSONObject(json);
		String sender = "";
		
		try {
			sender = jsonObject.getJSONObject(KEY_PAYLOAD_ATTRIBUTES).getString(KEY_SENDER);
			logger.debug("sender: " + sender);
			if(sender != null && sender.equalsIgnoreCase(VALUE_SENDER_DTS)) {
				ret = true;	
			}
		} catch (Exception e){
			//KEY_SENDER not mandatory in RTO & mandatory in RTT so just check.
			ret = false;
		}

		return ret;
	}
	
	public String getMasterIndexIdByValue(String json){
		
		String id = "";
		String source = "";
		String variableID = "";
		JSONObject jsonObject = new JSONObject(json);
		
		if(isDTS(json)) {
			 source = jsonObject.getJSONObject(KEY_PAYLOAD_ATTRIBUTES).getString(KEY_SENDER);
			 source = source + "-" + jsonObject.getJSONObject(KEY_PAYLOAD_ATTRIBUTES).getString(KEY_SOURCE);
			 variableID = jsonObject.getJSONObject(KEY_DTS_CARTON).getString(KEY_DTS_TRACKINGNUMBER);
			 id = source + "-"+variableID;
		}else {
			 String orderNumber = jsonObject.getJSONObject(KEY_ORDER_HEADER).getString(KEY_ORDER_NUMBER);
			 String orderSubNumber = jsonObject.getJSONObject(KEY_ORDER_HEADER).getString(KEY_ORDER_SUBNUMBER);
			
			 source = jsonObject.getJSONObject(KEY_PAYLOAD_ATTRIBUTES).getString(KEY_PAYLOAD_ATTRIBUTES_SOURCE);
			 variableID = jsonObject.getJSONObject(KEY_ORDER_HEADER).getString(KEY_ACCOUNTID); 
			 id = source + "-"+variableID + "-"+orderNumber + "-" + orderSubNumber;
		}
		
		//logger.debug("getMasterIndexIdByValue: " + id); 
		return id;

	}
	
	
	public String getMasterIndexIdByOrderField(String json, String orderField, String orderSource){
		JSONObject jsonObject = new JSONObject(json);
		String accountId = jsonObject.getJSONObject(KEY_ORDER_HEADER).getString(KEY_ACCOUNTID);
		String orderNumber = jsonObject.getJSONObject(KEY_ORDER_HEADER).getString(orderField);
		String orderSubNumber = jsonObject.getJSONObject(KEY_ORDER_HEADER).getString(KEY_ORDER_SUBNUMBER);
		String id = orderSource + "-"+accountId + "-"+orderNumber + "-" + orderSubNumber;
		
		//logger.debug("getMasterIndexIdByOrderField: " + id); 
		return id;
	}
	
	
	public String getMasterIndexVersion(String json){
		
		String versionTimeStamp = "";
		JSONObject jsonObject = new JSONObject(json);
		
		if(isDTS(json)) {
			 versionTimeStamp =  jsonObject.getJSONObject(KEY_DTS_EVENT).getString(KEY_DTS_EVENT_SCANTIMESTAMP);
		}else {
			 versionTimeStamp =  jsonObject.getJSONObject(KEY_PAYLOAD_ATTRIBUTES).getString(KEY_SENT_TIMESTAMP);
		}
		
		String version = timestamp2microsec(versionTimeStamp);
		//logger.debug("getMasterIndexVersion: " + version); 
		return version;

	}

	public String getVersionType() {
		return VALUE_EXTERNAL;
	}
	
	public String getSentTimestampKeywordField(){
		return getFullyQualifiedName(KEY_PAYLOAD_ATTRIBUTES,KEY_SENT_TIMESTAMP_KEYWORD);		
	}
	
	private String getFullyQualifiedName(String parent, String element) {
		return parent + "." + element;
	}
	
	public String getOrderDate(String json) {
		
		String returnValue = "";
		boolean fail = false;
		
		try {
			JSONObject jsonObject = new JSONObject(json);

			returnValue = jsonObject.getJSONObject(KEY_ORDER_HEADER).getString(KEY_ORDERDATE).trim();

		} catch (Exception e) {
			fail = true;
		}
		
		if ((fail) || (returnValue == null || returnValue.trim().length() < 7) ) {
			returnValue = BAD_DATE_SUBSTITUTE;
		}

		
		return returnValue;
	}
	
	public String getScanDate(String json) {
		
		String returnValue = "";
		boolean fail = false;
		
		try {
			JSONObject jsonObject = new JSONObject(json);

			returnValue = jsonObject.getJSONObject(KEY_DTS_EVENT).getString(KEY_DTS_EVENT_SCANTIMESTAMP).trim();

		} catch (Exception e) {
			fail = true;
		}
		
		if ((fail) || (returnValue == null || returnValue.trim().length() < 7) ) {
			returnValue = BAD_DATE_SUBSTITUTE;
		}

		
		return returnValue;
	}
//	**** USE getMasterIndexIdByValue instead ****
//	public String getMasterIndexId(String json){
//
//		String id = getMasterIndexIdByOrderField(json, KEY_ORDER_NUMBER, getOrderSource(json));
//		
//		//logger.debug("getMasterIndexId: " + id); 
//		return id;
//	}
	
	public String getSentTimestamp(String json) {
		JSONObject jsonObject = new JSONObject(json);
		return jsonObject.getJSONObject(KEY_PAYLOAD_ATTRIBUTES).getString(KEY_SENT_TIMESTAMP);
	}

    
	public String getRecordKeyForLogging(String json){
		JSONObject jsonObject = new JSONObject(json);
		
		String key=jsonObject.getJSONObject(KEY_PAYLOAD_ATTRIBUTES).getString(KEY_UNIQUEID);
		
		return key;
	}

	public String getPartitionKey(String json){
		JSONObject jsonObject = new JSONObject(json);
		String key = "";
		boolean hasElement = hasJsonElement(json, KEY_ORDER_HEADER, KEY_ACCOUNTID);
		if (hasElement) {
			key = jsonObject.getJSONObject(KEY_ORDER_HEADER).getString(KEY_ACCOUNTID);
			if (!key.isEmpty())
				return key;
		}
		hasElement = hasJsonElement(json, KEY_ORDER_HEADER, KEY_LOYALTYID);
		if (hasElement) {
			key = jsonObject.getJSONObject(KEY_ORDER_HEADER).getString(KEY_LOYALTYID);
			if (!key.isEmpty())
				return key;
		}
		return DEFAULT_VALUE_KINESIS_KEY;
	}
}
