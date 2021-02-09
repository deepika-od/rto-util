package com.officedepot.services.rto.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.config.ConfigurationManager;
import com.officedepot.services.search.elasticsearch.ElasticSearchHighLevelDAOImpl;

public class ElasticsearchUtil {

	private static final Logger logger = LoggerFactory.getLogger(ElasticsearchUtil.class);
	private static final String CLASS_NAME = "ElasticsearchUtil::";
	private static final String METHOD_MESSAGE = " ... writing record to elasticsearch: ";

	private static boolean ES_INDEX_ENABLED = false;
	private static boolean ES_ERROR_INDEX_ENABLED = false;
	private static boolean ES_EVENT_INDEX_ENABLED = false;
	private static boolean ES_DEADLETTER_INDEX_ENABLED = false;
	private static boolean ES_NOTIFY_INDEX_ENABLED = false;
	private static boolean ES_NOTIFY_RESPONSE_INDEX_ENABLED = false;
	private static boolean ES_LOG_INDEX_ENABLED = false;
	private static boolean ES_ORDER_MASTER_INDEX_ENABLED = false;
	private static boolean ES_TIMESTAMP_ENABLED = false;
	
	private static String ES_INDEX;
	private static String ES_ERROR_INDEX;
	private static String ES_EVENT_INDEX;
	private static String ES_DEADLETTER_INDEX;
	private static String ES_NOTIFY_INBOUND_INDEX;
	private static String ES_NOTIFY_RESPONSE_INDEX;
	private static String ES_LOG_INDEX;
	private static String ES_ORDER_MASTER_INDEX;
	private static String ES_TYPE;
	private static String ES_TIMESTAMP_PIPELINE;
	
	public ElasticsearchUtil(){
		init();
	}

	
	public String getNotifyResponseIndex(){
		return ElasticsearchUtil.ES_NOTIFY_RESPONSE_INDEX;
	}
		
		
		
	public void writeDeadLetterToElasticsearchIndex(String msg, String json){
		
		logger.error(CLASS_NAME + "writeDeadLetterToElasticsearchIndex" + METHOD_MESSAGE + ES_DEADLETTER_INDEX);
		PayloadUtil payloadUtil = new PayloadUtil();
		
		if ((ES_DEADLETTER_INDEX_ENABLED) 
				&& !payloadUtil.hasJsonValue( json, payloadUtil.KEY_PAYLOAD_ATTRIBUTES, payloadUtil.KEY_DEADLETTERRETRYSOURCE, payloadUtil.VALUE_DEADLETTERRETRYSOURCE)){
			
			try {
				
				json = payloadUtil.addProcessMessageInJSON( msg, json);
				
				String smallPayload = payloadUtil.getSmallPayload(json);
				
				String deadJSON = payloadUtil.addSmallOrderHeaderToJSON(smallPayload, json);
				
				writeToElasticsearchIndexWithJSON(deadJSON, ES_DEADLETTER_INDEX);
			} catch (Exception e){
				logger.error(CLASS_NAME + "writeDeadLetterToElasticsearchIndex ... FAILED WRITING DEADLETTER INDEX in elasticsearch: " + ES_DEADLETTER_INDEX);			
			}			
		}

	}	
	
	
	public void writeNotifyResponseToElasticsearchIndex(String time, String response, String msg, String json, String apiUrl){
		
		logger.debug(CLASS_NAME + "writeNotifyResponserToElasticsearchIndex" + METHOD_MESSAGE + ES_NOTIFY_RESPONSE_INDEX);
		
		if (ES_NOTIFY_RESPONSE_INDEX_ENABLED){
			logger.debug(CLASS_NAME + "writeNotifyResponserToElasticsearchIndex:ES_NOTIFY_RESPONSE_INDEX_ENABLED");
			try {
				PayloadUtil payloadUtil = new PayloadUtil();
				
				json = payloadUtil.addKeyValueToJSON(json, payloadUtil.KEY_PAYLOAD_ATTRIBUTES, payloadUtil.KEY_PROCESS_MESSAGE, msg );
				json = payloadUtil.addKeyValueToJSON(json, payloadUtil.KEY_PAYLOAD_ATTRIBUTES, payloadUtil.KEY_PROCESS_NOTIFY_RESPONES_TIME, time );
				json = payloadUtil.addKeyValueToJSON(json, payloadUtil.KEY_PAYLOAD_ATTRIBUTES, payloadUtil.KEY_PROCESS_NOTIFY_RESPONES_MSG, response );
				json = payloadUtil.addKeyValueToJSON(json, payloadUtil.KEY_PAYLOAD_ATTRIBUTES, payloadUtil.KEY_PROCESS_NOTIFY_URL, apiUrl );
				
				String smallPayload = payloadUtil.getSmallPayload(json);
				
				String notifyJSON = payloadUtil.addSmallOrderHeaderToJSON(smallPayload, json);
				
				writeToElasticsearchIndexWithJSON(notifyJSON, ES_NOTIFY_RESPONSE_INDEX);
				
			} catch (Exception e){
				logger.error(CLASS_NAME + "writeNotifyResponserToElasticsearchIndex ... FAILED WRITING NOTIFY RESPONSE INDEX in elasticsearch: " + ES_NOTIFY_RESPONSE_INDEX);			
			}			
		}
	}	
	
	public void writeEventsToElasticsearchIndex(String eventName, String destinationKey, String key){
		if (ES_EVENT_INDEX_ENABLED){
			logger.debug("ElasticsearchUtil::writeEventsToElasticsearchIndex ... writing events to elasticsearch: " + ES_EVENT_INDEX);
			
			String json = new PayloadUtil().createJSONWithEventInfo(eventName, destinationKey, key);
			
			writeToElasticsearchIndexWithJSON(json, ES_EVENT_INDEX);			
		}
	}
	
	public void writeInboundMasterToElasticsearchIndex(String json) {
		writeInboundMasterToElasticsearchIndex(json, "");
	}

	public void writeInboundMasterToElasticsearchIndex(String json, String indexSuffix) {

		if (ES_ORDER_MASTER_INDEX_ENABLED){
			logger.debug("ElasticsearchUtil::writeInboundMasterToElasticsearchIndex ... writing record to elasticsearch: " + ES_ORDER_MASTER_INDEX + indexSuffix);		
			writeInboundMasterToElasticsearchIndexWithJSON(json, ES_ORDER_MASTER_INDEX + indexSuffix, getESTimestampPipelineName());
		}		
	}
	
	public String getMasterIndexName(String indexSuffix) {
		String ret = "";
		if (ES_ORDER_MASTER_INDEX_ENABLED){
			
			logger.debug("ElasticsearchUtil::getMasterIndexName ...: " + ES_ORDER_MASTER_INDEX + indexSuffix);		
			ret = ES_ORDER_MASTER_INDEX + indexSuffix;
		}
		return ret;
	}
	

	public String getESTimestampPipelineName() {
		String ret = "";
		if (ES_TIMESTAMP_ENABLED){
			logger.debug("ElasticsearchUtil::getESTimestampPipelineName ...: " + ES_TIMESTAMP_PIPELINE);		
			ret = ES_TIMESTAMP_PIPELINE;
		}
		return ret;
	}
	public void writeInboundToElasticsearchIndex(String json){
		writeInboundToElasticsearchIndex(json, "");
	}
	
	public void writeInboundToElasticsearchIndex(String json, String indexSuffix){
		if (ES_INDEX_ENABLED){
			//logger.debug("ElasticsearchUtil::writeInboundToElasticsearchIndex ... writing record to elasticsearch: " + ES_INDEX + indexSuffix);		
			writeToElasticsearchIndexWithJSON(json, ES_INDEX + indexSuffix);
		}
	}
	
	public void writeNotifyInboundToElasticsearchIndex(String json){
		if (ES_NOTIFY_INDEX_ENABLED){
			logger.debug("ElasticsearchUtil::writeNotifyInboundToElasticsearchIndex ... writing record to elasticsearch: " + ES_NOTIFY_INBOUND_INDEX);		
			writeToElasticsearchIndexWithJSON(json, ES_NOTIFY_INBOUND_INDEX);
		}
	}

	public void writeLogToElasticsearchIndex(String json){
		if (ES_LOG_INDEX_ENABLED){
			logger.debug("ElasticsearchUtil::writeLogToElasticsearchIndex ... writing record to elasticsearch: " + ES_LOG_INDEX);		
			writeLogToElasticsearchIndexWithJSON(json, ES_LOG_INDEX);
		}
	}
	
	public void writeErrorToElasticsearchIndex(String errMessage){
		logger.error("#1 ElasticsearchUtil::writeErrorToElasticsearchIndex ... writing ERROR to elasticsearch INDEX: " + ES_ERROR_INDEX);
		if (ES_ERROR_INDEX_ENABLED){
			try {

				String json = new PayloadUtil().addProcessMessageInJSON(errMessage);
				
				writeToElasticsearchIndexWithJSON(json, ES_ERROR_INDEX);
				
			} catch (Exception e){
				logger.error("ElasticsearchUtil::writeErrorToElasticsearchIndex ... #1 FAILED WRITING ERROR INDEX in elasticsearch: " + ES_ERROR_INDEX);			
			
				//logger.error("ElasticsearchUtil::writeErrorToElasticsearchIndex ... #1 FAILED WRITING ERROR INDEX in elasticsearch stack: ", e);			
				
			}		
		}
	}
	
	public void writeErrorToElasticsearchIndex(String msg, String json){
		
		logger.error("#2 ElasticsearchUtil::writeErrorToElasticsearchIndex ... writing ERROR to elasticsearch INDEX in Payload: " + ES_ERROR_INDEX);
		
		if (ES_ERROR_INDEX_ENABLED){
			
			PayloadUtil payloadUtil = new PayloadUtil();
			
			try {
				json = payloadUtil.addProcessMessageInJSON( msg, json);
				
				String smallPayload = payloadUtil.getSmallPayload(json);
				
				String errorJSON = payloadUtil.addSmallOrderHeaderToJSON(smallPayload, json);
				
				writeToElasticsearchIndexWithJSON(errorJSON, ES_ERROR_INDEX);
			} catch (Exception e){
				logger.error("ElasticsearchUtil::writeErrorToElasticsearchIndex ... #2 FAILED WRITING ERROR INDEX in elasticsearch: " + ES_ERROR_INDEX);			
			
				//logger.error("ElasticsearchUtil::writeErrorToElasticsearchIndex ... #2 FAILED WRITING ERROR INDEX in elasticsearch stack: ", e);	
			}		
		}
	}
	private void writeLogToElasticsearchIndexWithJSON(String json, String index){

		String type = ES_TYPE;
		json = new PayloadUtil().addTimeStampInJSON(json);
		writeToElasticsearchIndex( json,  index,  type);
		
	}	
	
	
	private void writeToElasticsearchIndexWithJSON(String json, String index){
		
		String type = ES_TYPE;
		json = new PayloadUtil().addProcessTimeStampInJSON(json);
		json = new PayloadUtil().addProcessTimeStampTZInJSON(json);
		writeToElasticsearchIndex( json,  index,  type);
		
	}

	private void writeInboundMasterToElasticsearchIndexWithJSON(String json, String index, String pipelineName){
		
		String type = ES_TYPE;
		json = new PayloadUtil().addProcessTimeStampInJSON(json);
		json = new PayloadUtil().addProcessTimeStampTZInJSON(json);
		if (ES_TIMESTAMP_ENABLED) {
			json = new PayloadUtil().addElapsedTimesInJSON(json);
		}
		writeInboundMasterToElasticsearchIndex( json,  index,  type, pipelineName);
		
	}
	
	
	private void writeToElasticsearchIndex(String payload, String index, String type){

		//logger.debug("ElasticsearchUtil::writeToIndex::writing to index: " + index + ", type: " + type + ", payload: " + payload);
		
		ElasticSearchHighLevelDAOImpl elasticSearchHighLevelDAOImpl = new ElasticSearchHighLevelDAOImpl();
		try{
			
			elasticSearchHighLevelDAOImpl.indexDoc(index, type, "", payload);	
		} catch (Exception e){
			logger.error("ElasticsearchUtil::writeToIndex::FAILED writing to index " + index + " + " + payload);
			logger.error("ElasticsearchUtil::writeToIndex::EXCEPTION: " + e.getMessage(), e);
		}

		//logger.debug("ElasticsearchUtil::writeToIndex: AFTER writing to index... ");
	}

	
	private void writeInboundMasterToElasticsearchIndex(String payload, String index, String type, String pipeline){

		//logger.debug("ElasticsearchUtil::writeInboundMasterToElasticsearchIndex::writing to index: " + index + ", type: " + type + ", payload: " + payload);
		PayloadUtil payloadUtil = new PayloadUtil();
		
		String id = payloadUtil.getMasterIndexIdByValue(payload);
		String version = "";

		version = payloadUtil.getMasterIndexVersion(payload);
		String versionType = payloadUtil.getVersionType();
		
		ElasticSearchHighLevelDAOImpl elasticSearchHighLevelDAOImpl = new ElasticSearchHighLevelDAOImpl();
		
		try{
			
			elasticSearchHighLevelDAOImpl.indexDoc(index, type, id, version, versionType, payload, pipeline);	
		} catch (Exception e){
			logger.error("ElasticsearchUtil::writeInboundMasterToElasticsearchIndex::FAILED writing to index " + index + " + " + payload);
			logger.error("ElasticsearchUtil::writeInboundMasterToElasticsearchIndex::EXCEPTION: " + e.getMessage(), e);
		}
//
		logger.debug("ElasticsearchUtil::writeInboundMasterToElasticsearchIndex: AFTER writing to index... ");
	}
	
	private boolean getBooleanProperty(String pName) {
		boolean result = false;
		try {
			result = ConfigurationManager.getConfigInstance().getBoolean(pName);
		} catch (Exception e) {
			logger.error("Missing property " + pName + " from the configuration file");
		}
		return result;
	}
	
	private void init(){
		if (ES_INDEX==null){
			logger.info("ElasticsearchUtil::init");
						
			ES_INDEX_ENABLED = getBooleanProperty("rto.index.inbound.enabled");
			ES_ERROR_INDEX_ENABLED = getBooleanProperty("rto.index.error.enabled");
			ES_EVENT_INDEX_ENABLED = getBooleanProperty("rto.index.event.enabled");
			ES_DEADLETTER_INDEX_ENABLED = getBooleanProperty("rto.index.deadletter.enabled");
			ES_NOTIFY_INDEX_ENABLED = getBooleanProperty("rto.index.notify.enabled");
			ES_LOG_INDEX_ENABLED = getBooleanProperty("rto.index.log.enabled");
			ES_NOTIFY_RESPONSE_INDEX_ENABLED = getBooleanProperty("rto.index.notify.response.enabled");
			ES_ORDER_MASTER_INDEX_ENABLED = getBooleanProperty("rto.index.order.master.enabled");
			ES_TIMESTAMP_ENABLED = getBooleanProperty("rto.index.order.es.timestamp.pipeline.enabled");

			
			ES_TYPE = ConfigurationManager.getConfigInstance().getString("rto.index.type");
			
			if (ES_INDEX_ENABLED){
				ES_INDEX = ConfigurationManager.getConfigInstance().getString("rto.index.inbound");
				logger.info("----->>>> ES_INDEX = " + ES_INDEX);
				logger.info("----->>>> ES_TYPE = " + ES_TYPE);
			}
			
			if (ES_NOTIFY_INDEX_ENABLED){
				ES_NOTIFY_INBOUND_INDEX = ConfigurationManager.getConfigInstance().getString("rto.index.notify.inbound");
				logger.info("----->>>> ES_NOTIFY_INBOUND_INDEX = " + ES_NOTIFY_INBOUND_INDEX);
				logger.info("----->>>> ES_NOTIFY_INBOUND_TYPE = " + ES_TYPE);
			}			

			if (ES_LOG_INDEX_ENABLED){
				ES_LOG_INDEX = ConfigurationManager.getConfigInstance().getString("rto.index.log");
				logger.info("----->>>> ES_LOG_INDEX = " + ES_LOG_INDEX);
			}	
			
			if (ES_ERROR_INDEX_ENABLED){
				ES_ERROR_INDEX = ConfigurationManager.getConfigInstance().getString("rto.index.error");
				logger.info("----->>>> ES_ERROR_INDEX = " + ES_ERROR_INDEX);
				logger.info("----->>>> ES_ERROR_TYPE = " + ES_TYPE);	
			}
			if (ES_EVENT_INDEX_ENABLED){
				ES_EVENT_INDEX = ConfigurationManager.getConfigInstance().getString("rto.index.event");
				logger.info("----->>>> ES_EVENT_INDEX = " + ES_EVENT_INDEX);
				logger.info("----->>>> ES_EVENT_TYPE = " + ES_TYPE);
			}
			if (ES_DEADLETTER_INDEX_ENABLED){
				ES_DEADLETTER_INDEX = ConfigurationManager.getConfigInstance().getString("rto.index.deadletter");
				logger.info("----->>>> ES_DEADLETTER_INDEX = " + ES_DEADLETTER_INDEX);
				logger.info("----->>>> ES_DEADLETTER_TYPE = " + ES_TYPE);	
			}
			
			if (ES_NOTIFY_RESPONSE_INDEX_ENABLED){
				ES_NOTIFY_RESPONSE_INDEX = ConfigurationManager.getConfigInstance().getString("rto.index.notify.response");
				logger.info("----->>>> ES_NOTIFY_RESPONSE_INDEX = " + ES_NOTIFY_RESPONSE_INDEX);
				logger.info("----->>>> ES_NOTIFY_RESPONSE_TYPE = " + ES_TYPE);	
			}			
			
			if (ES_ORDER_MASTER_INDEX_ENABLED){
				ES_ORDER_MASTER_INDEX = ConfigurationManager.getConfigInstance().getString("rto.index.order.master");
				logger.info("----->>>> ES_ORDER_MASTER_INDEX = " + ES_ORDER_MASTER_INDEX);
				logger.info("----->>>> ES_ORDER_MASTER_TYPE = " + ES_TYPE);	
			}
			
			if (ES_TIMESTAMP_ENABLED){
				ES_TIMESTAMP_PIPELINE = ConfigurationManager.getConfigInstance().getString("rto.index.order.es.timestamp.pipeline");
				logger.info("----->>>> ES_TIMESTAMP_PIPELINE = " + ES_TIMESTAMP_PIPELINE);
			}
		}


	}
}
