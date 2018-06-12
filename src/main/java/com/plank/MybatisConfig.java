package com.plank;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageInterceptor;
import com.plank.ymlconfig.DataSourceConfig;
import com.plank.ymlconfig.DruidConfig;
import com.plank.ymlconfig.PageHelperConfig;

@ComponentScan
@Configuration
public class MybatisConfig {
	@Autowired
	private DataSourceConfig dataSourceConfig;

	@Autowired
	private DruidConfig druidConfig;

	@Autowired
	private PageHelperConfig pageHelperConfig;

	@Autowired
	private ApplicationContext applicationContext;

	@Bean
	public DataSourceInjection dataSourceInjection() throws Exception {
		ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
		for (Map<String, String> s : dataSourceConfig.getDatasource()) {
			BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
					.rootBeanDefinition(DruidDataSource.class);
			beanDefinitionBuilder.addPropertyValue("url", s.get("url"));
			beanDefinitionBuilder.addPropertyValue("username", s.get("username"));
			beanDefinitionBuilder.addPropertyValue("password", s.get("password"));
			beanDefinitionBuilder.addPropertyValue("initialSize", druidConfig.getInitialSize());
			beanDefinitionBuilder.addPropertyValue("maxActive", druidConfig.getMaxActive());
			beanDefinitionBuilder.addPropertyValue("minIdle", druidConfig.getMinIdle());
			beanDefinitionBuilder.addPropertyValue("maxWait", druidConfig.getMaxWait());
			beanDefinitionBuilder.addPropertyValue("timeBetweenEvictionRunsMillis",
					druidConfig.getTimeBetweenEvictionRunsMillis());
			beanDefinitionBuilder.addPropertyValue("minEvictableIdleTimeMillis",
					druidConfig.getMinEvictableIdleTimeMillis());
			beanDefinitionBuilder.addPropertyValue("maxPoolPreparedStatementPerConnectionSize",
					druidConfig.getMaxPoolPreparedStatementPerConnectionSize());
			beanDefinitionBuilder.addPropertyValue("testWhileIdle", druidConfig.getTestWhileIdle());
			beanDefinitionBuilder.addPropertyValue("testOnBorrow", druidConfig.getTestOnBorrow());
			beanDefinitionBuilder.addPropertyValue("testOnReturn", druidConfig.getTestOnReturn());
			beanDefinitionBuilder.addPropertyValue("poolPreparedStatements", druidConfig.getPoolPreparedStatements());
			beanFactory.registerBeanDefinition(s.get("name"), beanDefinitionBuilder.getBeanDefinition());
		}
		return new DataSourceInjection();
	}

	@Bean
	public DynamicDataSource dataSource(@Qualifier("dataSourceInjection") DataSourceInjection dataSourceInjection) {
		Map<Object, Object> targetDataSources = new HashMap<>();
		for (Map<String, String> s : dataSourceConfig.getDatasource()) {
			String name = s.get("name");
			targetDataSources.put(name, applicationContext.getBean(name, DruidDataSource.class));
		}
		DynamicDataSource dynamicDataSource = new DynamicDataSource();
		dynamicDataSource.setTargetDataSources(targetDataSources);
		return dynamicDataSource;
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory(DynamicDataSource ds) throws Exception {
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		if (pageHelperConfig.getEnabled() == true) {
			PageInterceptor interceptor = new PageInterceptor();
			Properties properties = new Properties();
			properties.setProperty("autoRuntimeDialect", pageHelperConfig.getAutoRuntimeDialect());
			properties.setProperty("rowBoundsWithCount", pageHelperConfig.getRowBoundsWithCount());
			interceptor.setProperties(properties);
			sqlSessionFactoryBean.setPlugins(new Interceptor[] { interceptor });
		}
		sqlSessionFactoryBean.setDataSource(ds);
		sqlSessionFactoryBean.setMapperLocations(resolver.getResources(druidConfig.getMapperLocations()));
		return sqlSessionFactoryBean.getObject();
	}

}