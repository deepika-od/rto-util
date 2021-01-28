package com.officedepot.services.rto.util;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONUtil {

	private static final Logger logger = LoggerFactory.getLogger(JSONUtil.class);
	private static final String CLASS_NAME = "JSONUtil";

//---other methods
	private boolean isEmptyString(String value){
		boolean ret = false;
		if(value == null || value.isEmpty()) {
			ret = true;
		}
		return ret;
	}


//-----JSON methods
	public boolean isJsonValuePresent(String json, String levelKey, String key){
		boolean ret = false;
		String value = "";
		JSONObject jsonObject = new JSONObject(json);
		if (jsonObject.has(levelKey)){
			if (jsonObject.getJSONObject(levelKey).has(key)){
				value = jsonObject.getJSONObject(levelKey).getString(key);
				   if(!isEmptyString(value)) {
					   	ret = true;
				   }
			}
		}
		return ret;
	}

	public boolean isJsonKeyPresent(String json, String levelKey, String key){
		boolean ret = false;
		JSONObject jsonObject = new JSONObject(json);
		if (jsonObject.has(levelKey)){
			if (jsonObject.getJSONObject(levelKey).has(key)){
				ret = true;
			}
		}
		return ret;
	}
	
	public boolean getBooleanFromJSON(String json, String level, String key){
		
		boolean value = false;
		
		if(isJsonKeyPresent(json,level,key )) {
			JSONObject jsonObject = new JSONObject(json);
			value = jsonObject.getJSONObject(level).getBoolean(key);
		}
		
		return value;
	}
	
	public String getValueFromJSON(String json, String level, String key){
		
		JSONObject jsonObject = new JSONObject(json);
		
		String value = jsonObject.getJSONObject(level).getString(key);

		return value;
	}
	
	public String addKeyValueToJSON( String json, String level, String key, String value){
		JSONObject jsonObject = new JSONObject(json);
		
		jsonObject.getJSONObject(level).put(key, value);

		return jsonObject.toString();
	}
	
	public boolean isJsonValue(String json, String levelKey, String key, boolean expectedValue){
		boolean ret = false;
		boolean value = false;
		JSONObject jsonObject = new JSONObject(json);
		if (jsonObject.has(levelKey)){
			if (jsonObject.getJSONObject(levelKey).has(key)){
				value = jsonObject.getJSONObject(levelKey).getBoolean(key);
				if(value) {
					ret = true;
				}
			}	
		}
		return ret;
	}
	
	public boolean hasJsonValue(String json, String levelKey, String key, String expectedValue){
		boolean ret = false;
		String value = "";
		JSONObject jsonObject = new JSONObject(json);
		if (jsonObject.has(levelKey)){
			if (jsonObject.getJSONObject(levelKey).has(key)){
				value = jsonObject.getJSONObject(levelKey).getString(key);
				   if(!isEmptyString(value) && value.toLowerCase().equals(expectedValue)) {
					   	ret = true;
				   }
			}
		}
		return ret;
	}
	
	public boolean hasJsonElement(String json, String levelKey, String key){
		boolean ret = false;
		JSONObject jsonObject = new JSONObject(json);
		if (jsonObject.has(levelKey))
			return jsonObject.getJSONObject(levelKey).has(key);
		return ret;
	}
	

	
	public boolean hasValueInJson(String json, String levelKey, String key, String expectedValue){
		
		String logmsg = CLASS_NAME+"::hasValueInJson:***" ;
		
		//logger.debug(logmsg + "key=" + key); 
		//logger.debug(logmsg + "expectedValue=" + expectedValue); 
	
		
		boolean ret = false;
		String value = "";
		JSONObject jsonObject = new JSONObject(json);
		if (jsonObject.has(levelKey)){
			if (jsonObject.getJSONObject(levelKey).has(key)){
				value = jsonObject.getJSONObject(levelKey).getString(key);
				//logger.debug(logmsg + "value=" + value);
				if(!isEmptyString(value) && value.trim().toLowerCase().contains(expectedValue.toLowerCase())){
				 //  if(!isEmptyString(value) && expectedValue.trim().toLowerCase().contains(value.toLowerCase())){
					   	ret = true;
				   }
			}
		}
		return ret;
	}
	
	public boolean hasJsonValueInString(String json, String levelKey, String key, String expectedValue){
		
		//String logmsg = CLASS_NAME+"::hasJsonValueInString:" ;
		boolean ret = false;
		String value = "";
		JSONObject jsonObject = new JSONObject(json);
		if (jsonObject.has(levelKey)){
			if (jsonObject.getJSONObject(levelKey).has(key)){
				value = jsonObject.getJSONObject(levelKey).getString(key);
				   if(!isEmptyString(value) && expectedValue.trim().contains(value.toLowerCase())){
					   	ret = true;
				   }
			}
		}
		return ret;
	}
	
	public boolean hasJsonValue(String json, String key, String expectedValue){
		boolean ret = false;
		String value = "";
		
		if (!StringUtils.isEmpty(json)){
			
			JSONObject jsonObject = new JSONObject(json);
			
			if (jsonObject.has(key)){
				value = jsonObject.getString(key);
				if(!StringUtils.isEmpty(value) && value.toLowerCase().equals(expectedValue)) {
					ret = true;
				}
			}
		}
		return ret;
	}
	
	public boolean hasJsonValuesInArray(String json, String levelKey, String key, String expectedValue, String key2, String expectedValue2){
		boolean ret = false;
		String value = "";
		String value2 = "";
		JSONObject jsonObject = new JSONObject(json);

         JSONArray orderLines = (JSONArray) jsonObject.get(levelKey);
         
         for (int i = 0; i < orderLines.length(); i++) {
 
        	 JSONObject orderLine = orderLines.getJSONObject(i); 
   	 
        	 value = orderLine.getString(key);
        	 value2 = orderLine.getString(key2);
        	
        	 if(!isEmptyString(value) && !isEmptyString(value2) ) {  
        		 
        		 int valueInt = Integer.parseInt(value);
        		 int valueInt2 = Integer.parseInt(value2);	
	   
        		 if(valueInt > 0 && valueInt2 == 0) {
        			 ret = true;
        			 return ret;
        		 }
        	 }
         }
      
		return ret;
	}
	
	public boolean hasJsonValuesInArray(String json, String levelKey, String key, int expectedValue, String key2, int expectedValue2){
		boolean ret = false;
		String value = "";
		String value2 = "";
		JSONObject jsonObject = new JSONObject(json);

         JSONArray orderLines = (JSONArray) jsonObject.get(levelKey);
         
         for (int i = 0; i < orderLines.length(); i++) {
 
        	 JSONObject orderLine = orderLines.getJSONObject(i); 
   	 
        	 value = orderLine.getString(key);
        	 value2 = orderLine.getString(key2);
        	
        	 if(!isEmptyString(value) && !isEmptyString(value2) ) {  
        		 
        		 int valueInt = Integer.parseInt(value);
        		 int valueInt2 = Integer.parseInt(value2);	
	   
        		 if(valueInt == expectedValue && valueInt2 == expectedValue2) {
        			 ret = true;
        			 return ret;
        		 }
        	 }
         }
      
		return ret;
	}
	
	public boolean hasJsonValuesInArrayInArray(String json, String levelKey, String levelKey2, String key, String key2){
		
		String logMessage = "PayloadUtil::hasJsonValuesInArrayInArray: ";

		//logger.debug(logMessage); 
		
		boolean ret = false;
		String array2value = "";
		String array2value2 = "";
		JSONObject jsonObject = new JSONObject(json);

         JSONArray array1 = (JSONArray) jsonObject.get(levelKey);
         
         for (int i = 0; i < array1.length(); i++) {
        	 
        	 //logger.debug(logMessage + "inside: array1"); 
        	 
        	 JSONObject array1Element = array1.getJSONObject(i); 
        
        	 //logger.debug(logMessage + "array1Element.toString() :::: " + array1Element.toString());
        	 
        	 JSONArray array2 = (JSONArray) array1Element.get(levelKey2);
        	 
            //JSONArray array2 = (JSONArray) jsonObject.get(levelKey2);
        	
        	 
        	 //logger.debug(logMessage + "array2.toString() :::: " + array2.toString());
        	 
             for (int j = 0; j < array2.length(); j++) {
            	 
            	 //logger.debug(logMessage + "inside: array2"); 
            	 
            	 JSONObject array2Element = array2.getJSONObject(i); 
               	            	 
            	 array2value = array2Element.getString(key);
            	 array2value2 = array2Element.getString(key2);
            	 
            	 //logger.debug(logMessage + "::" + key + ":" + array2value); 
            	 //logger.debug(logMessage + "::" + key2 + ":" + array2value2); 
            	
            	 if(!isEmptyString(array2value) && !isEmptyString(array2value2) ) {  
            			 ret = true;
            			 logger.debug(logMessage + ret); 
            			 return ret;
            	 }          	 
             }
         }
         logger.debug(logMessage + ret); 
		return ret;
	}
	
	
	public boolean hasJsonValueIn3Levels1Array(String json, String levelKey, String levelKey2, String levelKey3, String value){
		
		boolean ret = false;
		JSONObject jsonObject = new JSONObject(json);
		String logMessage = "PayloadUtil::hasJsonValueIn3Levels1Array: ";

		//logger.debug(logMessage); 

		if (jsonObject.has(levelKey)){
			JSONObject jsonObject2  = jsonObject.getJSONObject(levelKey);
			
			if (jsonObject2.has(levelKey2)){
				JSONObject jsonObject3  = jsonObject2.getJSONObject(levelKey2);
				JSONArray arrayItems = jsonObject3.getJSONArray(levelKey3);

				for (int i = 0; i < arrayItems.length(); i++) {
					String arrValue = (String) arrayItems.get(i);
				   // logger.debug(logMessage + "arrValue: " + arrValue);
				    
				    if (!StringUtils.isEmpty(arrValue) && arrValue.equalsIgnoreCase(value)) {
				    	ret = true;
				    	i = 9999;
				    	//logger.debug(logMessage + "MATCH: " + arrValue);
				    }
				}
				
			}
		}

//         logger.debug(logMessage + ret); 
		return ret;
	}
	

}
