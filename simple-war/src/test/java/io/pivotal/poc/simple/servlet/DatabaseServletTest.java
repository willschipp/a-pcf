package io.pivotal.poc.simple.servlet;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

public class DatabaseServletTest {

	private DatabaseServlet servlet;
	
	@Before
	public void before() {
		servlet = new DatabaseServlet();
	}
	
	@Test
	public void testDoGetHttpServletRequestHttpServletResponse() throws Exception {
		ServletContext servletContext = mock(ServletContext.class);
		DataSource dataSource = mock(DataSource.class);
		Connection connection = mock(Connection.class);
		Statement statement = mock(Statement.class);
		ResultSet resultSet = mock(ResultSet.class);
		when(resultSet.isBeforeFirst()).thenReturn(true);
		when(statement.executeQuery(anyString())).thenReturn(resultSet);
		when(connection.createStatement()).thenReturn(statement);
		when(dataSource.getConnection()).thenReturn(connection);
		when(servletContext.getAttribute(anyString())).thenReturn(dataSource);
		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getServletContext()).thenReturn(servletContext);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		when(resp.getWriter()).thenReturn(new PrintWriter(System.out));
		servlet.doGet(req, resp);
	}

}
