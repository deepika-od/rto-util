package com.officedepot.services.rto.util;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.config.ConfigurationManager;
import java.lang.management.ManagementFactory;

public class PayloadUtil extends JSONUtil {
	private static final Logger logger = LoggerFactory.getLogger(PayloadUtil.class);
	
	private static final int PROCESS_LIMIT = ConfigurationManager.getConfigInstance().getInt("rto.maxAttemptsToProcess");
	private static final String CLASS_NAME = "PayloadUtil";

	
	private static final String jvmName = ManagementFactory.getRuntimeMXBean().getName();
	private String threadID = Long.toString(Thread.currentThread().getId());
	
	private static final String id = UUID.randomUUID().toString();
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
	public String KEY_PROCESS_TIMESTAMPTZ = "processTimeStampTZ";
	public String KEY_ORDER_KEY = "processOrderKey";
	public String KEY_PROCESS_NOTIFY_RESPONES_TIME = "processNotifyResponseTime";
	public String KEY_PROCESS_NOTIFY_RESPONES_MSG = "processNotifyResponseMsg";
	public String KEY_PROCESS_NOTIFY_URL = "processNotifyURL";
	public String KEY_PROCESS_EVENT_KEY = "processEventKey";
	public String KEY_PAYLOAD_ATTRIBUTES_SOURCE = "source";
	public String KEY_PAYLOAD_ATTRIBUTES_CUST_CUSTOMER_TYPE = "custCustomerType";
	public String KEY_RECORD_ID = "docID";
	public String KEY_RECORD_DOC_TYPE = "docType";
	public String KEY_RECORD_INDEX_NAME = "indexName";
	public String KEY_SUBMIT_ADDRESS_EXTENSION = "submitAddressExtensionFlag";
	
	public String KEY_ORDERDATE = "orderDate";
	public String KEY_ORDERDATE_TIMESTAMP = "orderDateTimestamp";
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
	public String KEY_EXTENDED_ADDRESS = "extendedAddress";

	 
	public String KEY_BACKORDER_QUANTITY = "backorderQuantity";
	public String KEY_SHIP_QUANTITY = "shipQuantity";
	public String KEY_QUANTITY = "quantity";
	
	public String KEY_SOURCE_APP = "SOURCE-APP";
	public String KEY_SENDER = "sender";
	public String KEY_SOURCE = "source";
	public String KEY_DTS_EVENT_SCANTIMESTAMP = "scanTimestamp";
	
	// VALUES
	public String VALUE_SENDER_DTS = "dts";
	public String VALUE_SENDER_AOPS = "aops";
	public String VALUE_TECHSALESNC = "TECHSALENC";
	public String VALUE_ELYNXX		= "ELYNXX";
	public String VALUE_TRUE		= "true";
	
	
	public String VALUE_KEY_FORCESENT_SCM ="scm";
	public String VALUE_EVENT_FORCESENT_BYPASS_EVENTS = "bypass-events";
	public String VALUE_EVENT_FORCESENT_LATEST_RECORD = "latest-record";
	
	public String VALUE_DEADLETTERRETRYSOURCE = "rto-admin-service";
	public String VALUE_PAYLOAD_ATTRIBUTES_SOURCE_POS = "pos";
	public String VALUE_PAYLOAD_ATTRIBUTES_SOURCE_WARP = "warp";
	public String VALUE_PAYLOAD_ATTRIBUTES_SOURCE_AOPS = "aops";
		
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
	public String KEY_ORDER_NUMBER = "orderNumber";
	public String KEY_ORDER_SUBNUMBER = "ordersubNumber";
	String KEY_SENT_TIMESTAMP_KEYWORD = "sentTimestamp.keyword";
	String KEY_SENT_TIMESTAMP = "sentTimestamp";
	String KEY_SENT_TIMESTAMPTZ = "sentTimestampTZ";
	public String KEY_ACCOUNTID = "accountId";
	String KEY_UNIQUEID = "uniqueID";
	String DEFAULT_VALUE_KINESIS_KEY = "1234567";
	String KEY_LOYALTYID = "loyaltyId";
	String KEY_SENT_TO_PROCESS_DURATION = "sentT2ProcessT";
	String KEY_SENT_TO_PROCESS_DURATION_FORMATTED = "sentT2ProcessTFmt";
	String KEY_ORDERDATE_TO_SENT_DURATION = "orderDate2SentT";
	String KEY_ORDERDATE_TO_SENT_DURATION_FORMATTED = "orderDate2SentTFmt";
	String KEY_DURATIONS = "durations";
	
	//extended address fields
	String KEY_SHIPPING = "Shipping";
	String KEY_SHIP_TO_ID = "ShipToID";
	String KEY_ADDRESS_LINE_1 = "line1";
	String KEY_ADDRESS_LINE_2 = "line2";
	String KEY_SOLDTO = "soldTo";
	String KEY_CONTACT_FIRST_NAME = "contactFirstName";
	String KEY_CONTACT_LAST_NAME = "contactLastName";
	String KEY_CONTACT_PHONE_EXT = "contactPhoneExt";
	
	
	
	private static final String DATE_FORMAT = "yyyy-MM-dd.HH.mm:ss.SSSSSS";
	public final String BAD_DATE_SUBSTITUTE = "9999-01-01";
    public final String ISO_DATE_PATTERN = "uuuu-MM-dd'T'HH:mm:ss.SSSSSSz";
    public final String ZONED_DATE_PATTERN = "uuuu-MM-dd'T'HH:mm:ss.SSSSSS[XXX]";
	
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
			
		if (isJsonValuePresent(orderJSON, KEY_ORDER_HEADER, KEY_ORDER_SUBNUMBER)){
			String orderSubNumber = getValueFromJSON(orderJSON, KEY_ORDER_HEADER, KEY_ORDER_SUBNUMBER);
			smallJSON = addKeyValueToJSON(smallJSON, KEY_ORDER_HEADER, KEY_ORDER_SUBNUMBER, orderSubNumber);
		}

		if(isJsonValuePresent( orderJSON, KEY_ORDER_HEADER, KEY_ACCOUNTID)){
			String accountId = getValueFromJSON(orderJSON, KEY_ORDER_HEADER, KEY_ACCOUNTID);
			smallJSON = addKeyValueToJSON(smallJSON, KEY_ORDER_HEADER, KEY_ACCOUNTID, accountId);
		}
		
		smallJSON = addKeyValueToJSON(smallJSON, KEY_PAYLOAD_ATTRIBUTES, "jvmName", PayloadUtil.jvmName);
		
		smallJSON = addKeyValueToJSON(smallJSON, KEY_PAYLOAD_ATTRIBUTES, "threadID", this.threadID);
		
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

	private ZonedDateTime getZoneDateTime(String inputDate) {
        DateTimeFormatter formatterTimezone = DateTimeFormatter.ofPattern(ISO_DATE_PATTERN);
        ZonedDateTime inputDateZDT = ZonedDateTime.parse(inputDate, formatterTimezone);
        return inputDateZDT;
	}
	
	public String addElapsedTimesInJSON(String json){
		
		json = addOrderDate2SentTDuration(json);
		json = addSentT2ProcessTDuration(json);

		return json;
	}
	
	public ZonedDateTime asTimeZoneDate(String json, String levelKey, String key) {
		ZonedDateTime response = null;
		
		if (isJsonValuePresent(json, levelKey, key)) {
			String value = getValueFromJSON(json, levelKey, key);
			response = getZoneDateTime(value);
		}
		return response;
	}
	
	public JSONObject durationAsJSON(ZonedDateTime startDate, ZonedDateTime endDate, String key1, String key2) {
		JSONObject jObject = null;
		if (startDate != null && endDate != null) {
			long duration = temporalDifference(startDate, endDate, ChronoUnit.MICROS);
			String formattedDuration = formatMicroSec(duration);

			jObject = new JSONObject();
			jObject.put(key1, duration);
			jObject.put(key2, formattedDuration);			
		}
		return jObject;
	}
		
	public JSONObject addDurationObject(String json, JSONObject durationJSON) {
		JSONObject jsonObject = new JSONObject(json);
		if (durationJSON != null) {
			JSONObject payloadAttributes = (JSONObject) jsonObject.getJSONObject(KEY_PAYLOAD_ATTRIBUTES);
			if (isJsonKeyPresent(json, KEY_PAYLOAD_ATTRIBUTES, KEY_DURATIONS)) { //
				durationJSON.keySet().forEach(keyStr ->
			    {
			        Object keyvalue = durationJSON.get(keyStr);
			        payloadAttributes.getJSONObject(KEY_DURATIONS).put(keyStr, keyvalue);
			    });
			} else {
				payloadAttributes.put(KEY_DURATIONS, durationJSON);
			}

		}
		return jsonObject;

	}
	public String addOrderDate2SentTDuration(String json){
		JSONObject jsonObject = new JSONObject(json);
		
		ZonedDateTime orderDateTimestampZDT = asTimeZoneDate(json, KEY_ORDER_HEADER, KEY_ORDERDATE_TIMESTAMP);
		ZonedDateTime sentTimestampZDT = asTimeZoneDate(json, KEY_PAYLOAD_ATTRIBUTES, KEY_SENT_TIMESTAMPTZ);
		
		JSONObject durationObject = durationAsJSON(orderDateTimestampZDT, sentTimestampZDT, KEY_ORDERDATE_TO_SENT_DURATION, KEY_ORDERDATE_TO_SENT_DURATION_FORMATTED);
		
		jsonObject = addDurationObject(json, durationObject);
		
		return jsonObject.toString();
	}
	
	public String addSentT2ProcessTDuration(String json){
		JSONObject jsonObject = new JSONObject(json);
		
		ZonedDateTime sentTimestampZDT = asTimeZoneDate(json, KEY_PAYLOAD_ATTRIBUTES, KEY_SENT_TIMESTAMPTZ);
		ZonedDateTime processTimestampZDT = asTimeZoneDate(json, KEY_PAYLOAD_ATTRIBUTES, KEY_PROCESS_TIMESTAMPTZ);
		
		JSONObject durationObject = durationAsJSON(sentTimestampZDT, processTimestampZDT, KEY_SENT_TO_PROCESS_DURATION, KEY_SENT_TO_PROCESS_DURATION_FORMATTED);
		
		jsonObject = addDurationObject(json, durationObject);
		
		return jsonObject.toString();
	}	
	
	
	public String addProcessTimeStampTZInJSON(String json){
		JSONObject jsonObject = new JSONObject(json);
		
		ZonedDateTime zoneDateTime = ZonedDateTime.now();
        String processTimeTZ = zoneDateTime.format(DateTimeFormatter.ofPattern(ZONED_DATE_PATTERN));
        
		jsonObject.getJSONObject(KEY_PAYLOAD_ATTRIBUTES).put(KEY_PROCESS_TIMESTAMPTZ, processTimeTZ);

		return jsonObject.toString();
	}

	  //Since both ZonedDateTime and LocalDateTime implements Temporal interface, you can write also universal method for those date-time types:
	
	public long temporalDifference(Temporal d1, Temporal d2, ChronoUnit unit){
	   return unit.between(d1, d2);
	}
	
	String formatMicroSec(long microseconds) {						
		final long dy  = TimeUnit.MICROSECONDS.toDays(microseconds);
		final long hr  = TimeUnit.MICROSECONDS.toHours(microseconds)   - TimeUnit.DAYS.toHours(TimeUnit.MICROSECONDS.toDays(microseconds));
		final long min = TimeUnit.MICROSECONDS.toMinutes(microseconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MICROSECONDS.toHours(microseconds));
		final long sec = TimeUnit.MICROSECONDS.toSeconds(microseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MICROSECONDS.toMinutes(microseconds));
		final long ms  = TimeUnit.MICROSECONDS.toMillis(microseconds)  - TimeUnit.SECONDS.toMillis(TimeUnit.MICROSECONDS.toSeconds(microseconds));
		final long us  = TimeUnit.MICROSECONDS.toMicros(microseconds)  - TimeUnit.MILLISECONDS.toMicros(TimeUnit.MICROSECONDS.toMillis(microseconds));
		
		//return String.format("%d Days %d Hours %d Minutes %d Seconds %d Milliseconds %d Microseconds", dy, hr, min, sec, ms, us);
		return String.format("%dd, %dh, %dm, %ds, %dms, %dµs", dy, hr, min, sec, ms, us);
	}
	
	public String addTimeStampInJSON(String json){
		JSONObject jsonObject = new JSONObject(json);
		
		TimeZone.setDefault(TimeZone.getTimeZone("EST"));

		jsonObject.put(KEY_TIMESTAMP, new SimpleDateFormat(DATE_FORMAT).format(new Date()));

		return jsonObject.toString();
	}
	
	public JSONObject copyObject(JSONObject input, JSONObject output, String objectName) {		
		JSONObject jsonObject = null;
		try {
			jsonObject = input.getJSONObject(objectName);
			output.put(objectName, jsonObject);
		} catch (JSONException e) {
			logger.info(objectName + " attribute missing from the payload, " + e.getMessage());
		}
		return output;
	}
	
	public JSONObject copyAttribute(JSONObject input, JSONObject output, String objectName) {		
		String name = null;
		try {
			name = input.getString(objectName);
			output.put(objectName, name);
		} catch (JSONException e) {
			logger.info(objectName + " attribute missing from the payload, " + e.getMessage());
		}
		return output;
	}
	
	//the extendedAddress object will contain the _id, and the required elements to save will mimic the structure from rto_order
	//create ExtendedAddressObject. instantiate it. add attributes. getJSONObject
	public String createExtendedAddressPayload(String json) {
		JSONObject jsonObject = new JSONObject(json);
		
		JSONObject outpuJsonObject = new JSONObject("{}");
		
		outpuJsonObject = copyObject(jsonObject, outpuJsonObject, KEY_PAYLOAD_ATTRIBUTES);
		
		JSONObject orderHeader = jsonObject.getJSONObject(KEY_ORDER_HEADER);	
		JSONObject shippingObject =  orderHeader.getJSONObject(KEY_SHIPPING);
		
		JSONObject outputShippingObject = new JSONObject("{}"); 
		outputShippingObject = copyAttribute(shippingObject, outputShippingObject, KEY_SHIP_TO_ID);
		outputShippingObject = copyAttribute(shippingObject, outputShippingObject, KEY_ADDRESS_LINE_1);
		outputShippingObject = copyAttribute(shippingObject, outputShippingObject, KEY_ADDRESS_LINE_2);

		JSONObject soldToObject =  orderHeader.getJSONObject(KEY_SOLDTO);
		
		JSONObject outputSoldToObject = new JSONObject("{}"); 
		outputSoldToObject = copyAttribute(soldToObject, outputSoldToObject, KEY_CONTACT_FIRST_NAME);
		outputSoldToObject = copyAttribute(soldToObject, outputSoldToObject, KEY_CONTACT_LAST_NAME);
		outputSoldToObject = copyAttribute(soldToObject, outputSoldToObject, KEY_CONTACT_PHONE_EXT);
		
		JSONObject outputHeaderObject = new JSONObject("{}"); 
		outputHeaderObject = copyAttribute(orderHeader, outputHeaderObject, KEY_ORDER_NUMBER);
		outputHeaderObject = copyAttribute(orderHeader, outputHeaderObject, KEY_ORDER_SUBNUMBER);
		outputHeaderObject = copyAttribute(orderHeader, outputHeaderObject, KEY_ACCOUNTID);
		
		outputHeaderObject.put(KEY_SHIPPING, outputShippingObject);
		outputHeaderObject.put(KEY_SOLDTO, outputSoldToObject);		
		outpuJsonObject.put(KEY_ORDER_HEADER, outputHeaderObject);
		
		String returnJSON = outpuJsonObject.toString();
		
		logger.debug(CLASS_NAME + "::createExtendedAddressPayload::returnJSON = " + returnJSON);
		
		return returnJSON;
	}
	
	public boolean getSubmitAddressExtension(String json) {
		boolean submitAddressExtensionFlag = false;

		JSONObject jsonObject = new JSONObject(json);
		try {
			submitAddressExtensionFlag = jsonObject.getJSONObject(KEY_ORDER_HEADER).getBoolean(KEY_SUBMIT_ADDRESS_EXTENSION);
		} catch (JSONException e)
		{
			logger.debug("payload doesn't have submitAddressExtension ", e);
		}
		logger.debug("submitAddressExtensionFlag: " + submitAddressExtensionFlag);
		return submitAddressExtensionFlag;
	}
	
	public boolean isNonAOPSender(String json) {
		boolean isNonAOPSender = false;

		JSONObject jsonObject = new JSONObject(json);
		try {
			String sender = jsonObject.getJSONObject(KEY_PAYLOAD_ATTRIBUTES).getString(KEY_SENDER);
			if (sender != null) {
				sender=sender.trim();
				isNonAOPSender = !(sender.equalsIgnoreCase(VALUE_SENDER_AOPS));
			}
		} catch (JSONException e)
		{
			logger.debug("payload doesn't have payloadAttributes.sender ", e);
		}
		logger.debug("isNonAOPSender: " + isNonAOPSender);
		return isNonAOPSender;
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
	
	
	public boolean hasExtendedAddressIndicator(String json) {
		logger.debug("hasExtendedAddressIndicator: " + json);
		boolean hasElement = hasJsonValueIn3Levels1Array(
				json, 
				KEY_ORDER_HEADER,
				KEY_ADD_VALUES,
				KEY_EXTENDED_ADDRESS,
				VALUE_TRUE
				);
		logger.debug("hasExtendedAddressIndicator response: " + hasElement);
			
		return hasElement;
	}
	
	//overlay the address on top of existing document
	public String updateOrderWithExtendedAddress(String order, String extededAddressJson) {
		logger.debug("updateOrderWithExtendedAddress");
		logger.debug("order: " + order);
		JSONObject jsonObject = new JSONObject(order);
		//soldTo
		
		//shipping
		
		return order;
	}
	
	//lookup address in extended address index
	//overlay the address on top of existing document
	//return the new document
	public String enrichOrderWithExtendedAddress(String json) {
		String id = getMasterIndexIdByValue(json);
		
		ElasticsearchUtil elasticUtil = new ElasticsearchUtil();
		String extededAddressJson = elasticUtil.getAddressExtensionById(id);
		String response = updateOrderWithExtendedAddress(json, extededAddressJson);
		return response;
	}
}
