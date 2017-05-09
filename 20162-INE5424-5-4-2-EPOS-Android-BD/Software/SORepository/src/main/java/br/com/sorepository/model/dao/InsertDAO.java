/**
 * 
 */
package br.com.sorepository.model.dao;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.mindrot.jbcrypt.BCrypt;
import br.com.sorepository.model.pojo.SmartObject;
import br.com.sorepository.model.pojo.SmartObjectList;

/**
 * @author Ercilio Nascimento
 */
public class InsertDAO extends BaseDAO {

	public InsertDAO() throws ClassNotFoundException, SQLException {
		super();
	}

	public int addParameter(String idparameter, String idservice, String register_modbus, String name, String type, String minvalue, String maxvalue, String options) throws SQLException {
        PreparedStatement prst;
        if (idparameter != null) {
            prst = conn.prepareStatement("INSERT INTO  `parameter` (`idparameter`, `idservice`, `register_modbus`, `name`, `type`, `minvalue`, `maxvalue`, `options`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            prst.setString(1, idparameter);
            prst.setString(2, idservice);
            prst.setString(3, register_modbus);
            prst.setString(4, name);
            prst.setString(5, type);
            prst.setString(6, minvalue);
            prst.setString(7, maxvalue);
            prst.setString(8, options);
        } else {
            prst = conn.prepareStatement("INSERT INTO  `parameter` (`idservice`, `register_modbus`, `name`, `type`, `minvalue`, `maxvalue`, `options`) VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            prst.setString(1, idservice);
            prst.setString(2, register_modbus);
            prst.setString(3, name);
            prst.setString(4, type);
            prst.setString(5, minvalue);
            prst.setString(6, maxvalue);
            prst.setString(7, options);
        }

        int result = -1;
        ResultSet rs = prst.getGeneratedKeys();
        if (idparameter != null) {
            result = Integer.parseInt(idparameter);
        } else if (rs != null && rs.next()) {
            result = rs.getInt(1);
        }
		prst.executeUpdate();
        return result;
	}
	
	public int addService(String idservice,  String idsmartobject, String name) throws SQLException {
		PreparedStatement prst;
        if (idservice != null) {
            prst = conn.prepareStatement("INSERT INTO  `service` (`idservice`, `idsmartobject`, `name`) VALUES (?, ?, ?)");
            prst.setString(1, idservice);
            prst.setString(2, idsmartobject);
            prst.setString(3, name);
        } else {
            prst = conn.prepareStatement("INSERT INTO  `service` (idsmartobject`, `name`) VALUES (?, ?)");
            prst.setString(1, idsmartobject);
            prst.setString(2, name);
        }
        prst.executeUpdate();
        int result = -1;
        ResultSet rs = prst.getGeneratedKeys();
        if (idservice != null) {
            result = Integer.parseInt(idservice);
        } else if (rs != null && rs.next()) {
            result = rs.getInt(1);
        }
        prst.executeUpdate();
        return result;
	}
	
	public int addSO(String idsmartobject, String serverurl, String idsomodbus, String name) throws SQLException {
        PreparedStatement prst;
        if (idsmartobject != null) {
            prst = conn.prepareStatement("INSERT INTO  `smartobject` (`idsmartobject`, `serverurl`,`idsomodbus`, `name`) VALUES (?, ?, ?, ?)");
            prst.setString(1, idsmartobject);
            prst.setString(2, serverurl);
            prst.setString(3, idsomodbus);
            prst.setString(4, name);
        } else {
            prst = conn.prepareStatement("INSERT INTO  `smartobject` (`serverurl`,`idsomodbus`, `name`) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            prst.setString(1, serverurl);
            prst.setString(2, idsomodbus);
            prst.setString(3, name);
        }
        int result = -1;
        ResultSet rs = prst.getGeneratedKeys();
        if (idsmartobject != null) {
            result = Integer.parseInt(idsmartobject);
        } else if (rs != null && rs.next()) {
            result = rs.getInt(1);
        }
        return result;
	}

	private void log(String info, StringBuilder sql, Object... params) {
		for (int i = 0; i < params.length; i++) {
			int index = sql.indexOf("?");
			if (params[i] instanceof String) {
				sql = sql.replace(index, ++index, "'" + String.valueOf(params[i]) + "'");
			} else {
				sql = sql.replace(index, ++index, String.valueOf(params[i]));
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		System.out.println(sdf.format(Calendar.getInstance().getTime()) + " " + info + " " + sql.toString());
	}

	private String getConcatValues(ResultSet rs) throws SQLException {
		String s = "";
		for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
			s += rs.getString(i) + ",";
		}
		s = s.substring(0, s.length() - 1);

		return s;
	}
}