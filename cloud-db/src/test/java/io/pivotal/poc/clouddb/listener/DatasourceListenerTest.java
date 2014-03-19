package io.pivotal.poc.clouddb.listener;

import static org.mockito.Mockito.mock;
import io.pivotal.poc.clouddb.support.MockEnvironment;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.junit.Before;
import org.junit.Test;

public class DatasourceListenerTest {

	private DatasourceListener listener;
	
	private ServletContextEvent sce;
	
	private ServletContext servletContext;
	
	private MockEnvironment environment = new MockEnvironment();
	
	@Before
	public void before() {
		listener = new DatasourceListener();
		servletContext = mock(ServletContext.class);
		sce = new ServletContextEvent(servletContext);
		listener.setEnvironment(environment);
	}
	
	@Test
	public void testContextInitialized() {
		listener.contextInitialized(sce);
		//check that the datasource is there
		
	}

}
