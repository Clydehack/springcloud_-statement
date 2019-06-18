package com.uzone.settlement.framework.task.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DispatchTaskMapper {
	
	/** 表是否已存在 0-不存在 1-已存在 */
	@Select("select count(*) from information_schema.tables where table_name = #{tableName}")
	int exitTable(@Param("tableName") String tableName);
	
	/** 字段是否已存在 0-不存在 1-已存在 */
	@Select("select count(*) from bu_checkaccount_log where checkaccount_time = #{yesterday}")
	int exitColumn(@Param("yesterday") String yesterday);

	/** 对账结果 0-失败 1-成功 */
	@Select("select case checkaccount_status when 'S' then 1 else 0 end from bu_checkaccount_log where checkaccount_time = #{yesterday}")
	boolean queryReconciliationStatus(@Param("yesterday") String yesterday);
	
	int insertYesterdayInit();
}
