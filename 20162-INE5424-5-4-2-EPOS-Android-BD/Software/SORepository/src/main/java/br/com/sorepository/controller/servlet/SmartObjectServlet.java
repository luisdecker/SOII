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
public class SmartObjectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SmartObjectServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		String out = null;

		String idsmartobject = request.getParameter("idsmartobject");
		String serverurl = request.getParameter("serverurl");
		String idsomodbus = request.getParameter("idsomodbus");
		String name = request.getParameter("name");

		InsertDAO dao;

		try {
			dao = new InsertDAO();
			writer.write(dao.addSO(idsmartobject, serverurl, idsomodbus, name)+"");


		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
