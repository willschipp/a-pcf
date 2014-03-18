package io.pivotal.poc.simple.listener;

import io.pivotal.poc.simple.service.CloudConfiguration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

@WebListener
public class DatasourceListener implements ServletContextListener {
	
	private static final Logger LOGGER = Logger.getLogger(DatasourceListener.class);

	private CloudConfiguration cloudConfiguration = new CloudConfiguration();
	
	public static final String DATASOURCE_ATTRIBUTE = "dataSource";
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		if (!bindCloudDataSource(sce)) {
			LOGGER.warn("not running in the cloud");
			bindLocalDataSource(sce);
		}//end if
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) { }

	//creates a "local" (in-memory) binding for testing purposes
	private void bindLocalDataSource(ServletContextEvent sce) {
		//create a database
		final BasicDataSource factory = new BasicDataSource();
		factory.setDriverClassName("org.h2.Driver");//TODO - remove hardcoding
		factory.setUrl("jdbc:h2:mem:a");
		factory.setUsername("sa");
		factory.setPassword("");
		//set
		sce.getServletContext().setAttribute(DATASOURCE_ATTRIBUTE, factory);
	}
	
	//runs the cloud configuration component
	private boolean bindCloudDataSource(ServletContextEvent sce) {
		boolean result = false;
		final DataSource dataSource = cloudConfiguration.inventoryDataSource();
		if (dataSource != null) {
			sce.getServletContext().setAttribute(DATASOURCE_ATTRIBUTE, dataSource);
			result = true;
		}//end if
		return result;
	}
}
