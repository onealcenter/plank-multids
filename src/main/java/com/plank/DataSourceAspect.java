package com.plank;

import org.aspectj.lang.JoinPoint;

public class DataSourceAspect {
	public void setDataSourceKey(JoinPoint point) {
		DataSourceChoice dataSourceChoice = point.getTarget().getClass().getAnnotation(DataSourceChoice.class);
		DatabaseContextHolder.setDatabaseType(dataSourceChoice.dataSourceName());
	}
}