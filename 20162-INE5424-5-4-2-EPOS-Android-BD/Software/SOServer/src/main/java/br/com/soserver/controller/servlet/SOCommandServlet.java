package br.com.soserver.controller.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.soserver.comm.Communication;
import br.com.soserver.comm.PortManager;
import br.com.soserver.serialization.Message;
import br.com.soserver.serialization.Serialization;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * @author Ercilio Nascimento
 */
public class SOCommandServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SOCommandServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		Serialization s = new Serialization();
//		Message m3 = s.deserialize(":080300BA0009CE\r\n");
//		if (m3 != null) {
//			response.getWriter().write("Reconhecido!");
//		} else {
//			response.getWriter().write("Não Reconhecido!");
//		}
//
//		boolean run = false;
//		for (int address=0; address < 255 && run; address++) {
//			for (int i=0; i < 255 && run; i++) {
//				for (int i2=0; i2 < 255 && run; i2++) {
//					Message m = new Message();
//					m.setAddress(address);
//					m.setFunctionCode(Message.MessageType.READ_HOLDING_REGISTERS);
//					m.setDataSlice(i, 0, 4);
//					m.setDataSlice(i2, 4, 8);
//              		String r = s.serialize(m);
//					System.out.println("Codificado '"+r+"'");
//					Message m2 = s.deserialize(r);
//					if (m2 == null) {
//						response.getWriter().write("Problema na codificacao: '"+r+"'\n");
//						response.getWriter().write("address="+address+"\n");
//						response.getWriter().write("i="+i+"\n");
//						response.getWriter().write("i2="+i2+"\n");
//						response.getWriter().write("data='"+m.getData()+"'\n");
//						run = false;
//					}
//				}
//			}
//		}
//		return;
		/*PrintWriter writer = response.getWriter();
		StringBuffer sb = new StringBuffer();
		BufferedReader bufferedReader = null;
		String ack = "";
		ParserCommand parser = new ParserCommand();

		try {
			bufferedReader = request.getReader();
			char[] charBuffer = new char[128];
			int bytesRead;
			while ((bytesRead = bufferedReader.read(charBuffer)) != -1) {
				sb.append(charBuffer, 0, bytesRead);
			}

			ack = parser.send(sb.toString());

		} catch (IOException ex) {
			throw ex;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}

		writer.write(StringEscapeUtils.escapeJava(ack));

		System.out.println(ack);*/
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		StringBuffer sb = new StringBuffer();
		Communication comm = Communication.getInstance();
		String idsomodbus = request.getParameter("idSOModbus");
		Map<String, String[]> parameters = request.getParameterMap();
		int successCount, failedCount;
        successCount = 0;
        failedCount = 0;
		for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
			String key = entry.getKey();
			String[] values = entry.getValue();
            String concatenatedValue = "";
            for(int i=0; i<values.length; i++) {
                concatenatedValue += values[i]+", ";
            }
            System.out.printf("Chave: %s - Valor: %s\n", key, concatenatedValue);
			if (values.length > 1 || values.length == 0 || key.equals("idSOModbus") || key.equals("unknown")) {
				continue;
			}
			System.out.printf(
			        "Enviando mensagem para %s: Alterando registrador %s com valor %s\n",
                    idsomodbus,
                    key,
                    values[0]
            );
			Message msg = new Message();
			msg.setAddress(idsomodbus);
			msg.setFunctionCode(Message.MessageType.WRITE_SINGLE_REGISTER);
			msg.setDataSlice(key, 0, 4);
			try {
				msg.setDataSlice(Integer.parseInt(values[0]), 4, 8);
			} catch (NumberFormatException e) {
				continue;
			}
			Message ack = comm.send(msg);
			if (ack != null) {
				System.out.println("Recebido ack!");
				System.out.println(ack.getFunctionCode());
			}
			if (ack == null ||
					Message.fromHex(ack.getFunctionCode()) == (128+Message.fromHex(msg.getFunctionCode()))) {
				failedCount++;
			} else {
                successCount++;
            }
		}
		String msg = String.format("%d comandos processados com sucessos e %d comandos com erro no processamento", successCount, failedCount);

		System.out.println(msg);

		writer.write(StringEscapeUtils.escapeJava(msg));

		System.out.println(sb);
	}
}
