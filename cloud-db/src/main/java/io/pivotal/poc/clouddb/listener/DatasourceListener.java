package io.pivotal.poc.clouddb.listener;


import io.pivotal.poc.clouddb.support.Environment;
import io.pivotal.poc.clouddb.support.SystemEnvironment;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;
import org.postgresql.ds.PGPoolingDataSource;

/**
 * Example of a datasource listener in a Servlet 3.0 container.
 * 
 * Listener builds a datasource object (in this case, based on Postgres)
 * and then injects it into the servlet context for use by servlets downstream
 * 
 * Connection parameters are hard coded and need to be fixed
 * 
 * @author wschipp
 *
 */
@WebListener
public class DatasourceListener implements ServletContextListener {

	private static final Logger logger = Logger.getLogger(DatasourceListener.class);
	
	public static final String DATASOURCE_ATTRIBUTE = "dataSource";
	
	private Environment environment;
	
	public DatasourceListener() {
		environment = new SystemEnvironment();
	}
	
	public void contextInitialized(ServletContextEvent sce) {
		//get the datasource
		bindLocalDataSource(sce);
	}

	public void contextDestroyed(ServletContextEvent sce) { }

	/**
	 * build a datasource to the cloud
	 * @param sce
	 */
	private void bindLocalDataSource(ServletContextEvent sce) {
		//create a database
		PGPoolingDataSource source = new PGPoolingDataSource();
		source.setDataSourceName("A Data Source");
		source.setServerName(environment.getEvnironmentVariable("database.server"));//"babar.elephantsql.com");
		source.setDatabaseName(environment.getEvnironmentVariable("database.name"));//"eilzgtqy");
		source.setUser(environment.getEvnironmentVariable("database.user"));//"eilzgtqy");
		source.setPassword(environment.getEvnironmentVariable("database.password"));//"-HUlfGy9q76ArWT5RG3gLpbFgUIxDZKf");
		source.setMaxConnections(10);
		//set
		sce.getServletContext().setAttribute(DATASOURCE_ATTRIBUTE, source);
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}	
	
}
