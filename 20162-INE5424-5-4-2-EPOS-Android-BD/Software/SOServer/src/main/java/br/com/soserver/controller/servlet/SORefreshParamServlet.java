package br.com.soserver.controller.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.com.soserver.comm.Communication;
import br.com.soserver.comm.Communication;
import br.com.soserver.serialization.Message;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * @author Ercilio Nascimento
 */
public class SORefreshParamServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SORefreshParamServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Write to socat.
		PrintWriter writer = response.getWriter();
		Communication comm = Communication.getInstance();
		String idsomodbus = request.getParameter("idSOModbus");
		String[] parameters = request.getParameterValues("idRegisterModbus");
		String out = "";

		for (String params : parameters) {
			System.out.printf("Valor: %s\n", params);
			System.out.printf(
					"Enviando mensagem para %s: Lendo registrador %s\n",
					idsomodbus,
					params
			);
			Message msg = new Message();
			msg.setAddress(idsomodbus);
			msg.setFunctionCode(Message.MessageType.READ_HOLDING_REGISTERS);
			msg.setDataSlice(params, 0, 4);
			msg.setDataSlice(1, 4, 8);
			Message ack = comm.send(msg);
			if (ack == null ||
					Message.fromHex(ack.getFunctionCode()) == (128+Message.fromHex(msg.getFunctionCode()))) {
				System.out.println("Excecao detectada :(");
				continue;
			}
			// Byte count
			int count = Message.fromHex(ack.getDataSlice(0,2));
			System.out.println("count="+count);
			if (count == 2) {

				int value = Message.fromHex(ack.getDataSlice(2,6));
				System.out.println("value="+value);
				out += params+","+value+";";
			}
		}

		writer.write(StringEscapeUtils.escapeJava(out));

		System.out.println(out);

		writer.write(StringEscapeUtils.escapeJava(out));
		
		//write ack.
	}
}
