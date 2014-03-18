package io.pivotal.poc.clouddb.servlet;

import io.pivotal.poc.clouddb.listener.DatasourceListener;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet("/database")
public class DatabaseServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//get writer
		PrintWriter out = response.getWriter();
		//get the datasource
		DataSource dataSource = (DataSource) request.getServletContext().getAttribute(DatasourceListener.DATASOURCE_ATTRIBUTE);
		//query
		try {
			out.print(dataSource.getConnection().createStatement().executeQuery("select count(*) from twitter_account").isBeforeFirst());
		} catch (SQLException e) {
			throw new ServletException(e);
		}
	}

}
