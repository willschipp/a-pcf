package io.pivotal.poc.simple.service;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.config.java.AbstractCloudConfig;

public class CloudConfiguration extends AbstractCloudConfig {

	private static final Logger LOGGER = Logger.getLogger(CloudConfiguration.class);
	
	private boolean initialized = true;
	
	public CloudConfiguration() {
		super();
		BeanFactory factory = new DefaultListableBeanFactory();
		try {
			this.setBeanFactory(factory);
		}
		catch (CloudException e) {
			LOGGER.warn("no cloud",e);//TODO clean up message
			initialized = false;
		}
	}
	
	public DataSource inventoryDataSource() {
		ServiceConnectionFactory connectionFactory = connectionFactory();
		if (connectionFactory != null) {
			return new ServiceConnectionFactory().dataSource(System.getenv("database-service"));
		}//end if
		return null;
	}

	public boolean isInitialized() {
		return initialized;
	}
	
}
