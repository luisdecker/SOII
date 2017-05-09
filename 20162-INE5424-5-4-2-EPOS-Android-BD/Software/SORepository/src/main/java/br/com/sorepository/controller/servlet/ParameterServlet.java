package br.com.sorepository.controller.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.io.BufferedReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.sorepository.model.dao.InsertDAO;
import br.com.sorepository.model.pojo.SmartObjectList;

/**
 * @author Ercilio Nascimento
 */
public class ParameterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ParameterServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();

		String idparameter = request.getParameter("idparameter");
		String idservice = request.getParameter("idservice");
		String register_modbus = request.getParameter("register_modbus");
		String name = request.getParameter("name");
		String type = request.getParameter("type");
		String minvalue = request.getParameter("minvalue");
		String maxvalue = request.getParameter("maxvalue");
		String options = request.getParameter("options");

		InsertDAO dao;

		try {
			dao = new InsertDAO();
			writer.write(dao.addParameter(idparameter, idservice, register_modbus, name, type, minvalue, maxvalue, options)+"");

		} catch (Exception e) {
			e.printStackTrace();
			writer.write("-1");
		}
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
