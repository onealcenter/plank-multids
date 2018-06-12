package com.plank.ymlconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "plank.pagehelper")
public class PageHelperConfig {

	public String getAutoRuntimeDialect() {
		return autoRuntimeDialect;
	}

	public void setAutoRuntimeDialect(String autoRuntimeDialect) {
		this.autoRuntimeDialect = autoRuntimeDialect;
	}

	public String getRowBoundsWithCount() {
		return rowBoundsWithCount;
	}

	public void setRowBoundsWithCount(String rowBoundsWithCount) {
		this.rowBoundsWithCount = rowBoundsWithCount;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	private Boolean enabled;
	private String autoRuntimeDialect;
	private String rowBoundsWithCount;
}