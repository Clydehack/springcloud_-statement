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
	
	/**
	 * 关于Sql和NoSql选型:
	 * 
	 * 		日志系统分析
	 * 			① 日志的数据量通常都很大，且分布式情况下数据来自很多服务。
	 * 			② 日志增长数量无法预期
	 * 			③ 日志数据结构变化无法预期
	 * 			④ 日志属于“低价值”数据
	 * 
	 * 		MongoDB
	 * 			① 被设计为侧重高性能数据写入（先将数据写入虚拟内存，再从虚拟内存入库 有关测试统计称，写入性能至少是sql型的2倍，至多可达60倍以上）
	 * 			② 自带了数据分片特性，容易水平扩展，被设计为满足适应大数据量增长需求
	 * 			③ 结构易扩展（这里的扩展，主要是指当实体对象新增了字段，数据库自动扩展新字段，或者新增了一个类型，可以直接调用insert语句自动建文档，无需手动在库里建文档）
	 * 			④ 自带高可用（例如主备双节点切换）
	 * 
	 * 		MySql
	 * 			① 侧重事务一致性（InnoDB，其他不了解）
	 * 			② 侧重结构化查询（join多张有关联的表）
	 * 
	 */

}
