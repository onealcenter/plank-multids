package com.plank.ymlconfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "plank")
public class DataSourceConfig {
	private List<Map<String, String>> datasource = new ArrayList<Map<String, String>>();

	public List<Map<String, String>> getDatasource() {
		return datasource;
	}

	public void setDatasource(List<Map<String, String>> datasource) {
		this.datasource = datasource;
	}
}