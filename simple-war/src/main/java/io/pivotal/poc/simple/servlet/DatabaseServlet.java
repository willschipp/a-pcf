package io.pivotal.poc.simple.servlet;

import io.pivotal.poc.simple.listener.DatasourceListener;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet(urlPatterns={"/database"})
public class DatabaseServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//get writer
		PrintWriter out = resp.getWriter();
		DataSource dataSource = (DataSource) req.getServletContext().getAttribute(DatasourceListener.DATASOURCE_ATTRIBUTE);
		//check
		try {
			out.println(dataSource.getConnection().isClosed());
			//now check which database it is
			out.println(dataSource.getConnection().createStatement().executeQuery(System.getenv("sql")).next());
		} catch (SQLException e) {
			out.println(e);
		}
	}

	
	
}
