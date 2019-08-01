package com.uzone.settlement.framework.log;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.BasicDBObject;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

/**
 * 日志存入mongo
 * extends UnsynchronizedAppenderBase	异步的、普通的、不加锁的
 */
public class MongoDBAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

	@Override
	protected void append(ILoggingEvent eventObject) {
		MongoTemplate mongoTemplate = ApplicationContextProvider.getBean(MongoTemplate.class); 
	    if (mongoTemplate != null) { 
	      final BasicDBObject doc = new BasicDBObject(); 
	      doc.append("level", eventObject.getLevel().toString()); 
	      doc.append("logger", eventObject.getLoggerName()); 
	      doc.append("thread", eventObject.getThreadName()); 
	      doc.append("message", eventObject.getFormattedMessage()); 
	      mongoTemplate.save(doc, "log");
	    } 
	}

}
