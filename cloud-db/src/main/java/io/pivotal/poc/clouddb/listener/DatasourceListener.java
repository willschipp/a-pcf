package io.pivotal.poc.clouddb.listener;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;
import org.postgresql.ds.PGPoolingDataSource;

@WebListener
public class DatasourceListener implements ServletContextListener {

	private static final Logger logger = Logger.getLogger(DatasourceListener.class);
	
	public static final String DATASOURCE_ATTRIBUTE = "dataSource";
	
	public void contextInitialized(ServletContextEvent sce) {
		//get the datasource
		bindLocalDataSource(sce);
	}

	public void contextDestroyed(ServletContextEvent sce) { }

	
	private void bindLocalDataSource(ServletContextEvent sce) {
		//create a database
		PGPoolingDataSource source = new PGPoolingDataSource();
		source.setDataSourceName("A Data Source");
		source.setServerName("babar.elephantsql.com");
		source.setDatabaseName("eilzgtqy");
		source.setUser("eilzgtqy");
		source.setPassword("-HUlfGy9q76ArWT5RG3gLpbFgUIxDZKf");
		source.setMaxConnections(10);
		//set
		sce.getServletContext().setAttribute(DATASOURCE_ATTRIBUTE, source);
	}	
}
