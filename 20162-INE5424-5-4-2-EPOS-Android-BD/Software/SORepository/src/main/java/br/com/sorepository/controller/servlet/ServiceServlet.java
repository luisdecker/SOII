package br.com.sorepository.controller.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.sorepository.model.dao.InsertDAO;
import br.com.sorepository.model.pojo.SmartObjectList;

/**
 * @author Ercilio Nascimento
 */
public class ServiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ServiceServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		String out = null;

		String idservice = request.getParameter("idservice");
		String idsmartobject = request.getParameter("idsmartobject");
		String name = request.getParameter("name");

		InsertDAO dao;

		try {
			dao = new InsertDAO();
			writer.write(dao.addService(idservice, idsmartobject, name)+"");


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
