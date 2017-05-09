/**
 * 
 */
package br.com.sorepository.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.mindrot.jbcrypt.BCrypt;
import br.com.sorepository.model.pojo.SmartObject;
import br.com.sorepository.model.pojo.SmartObjectList;

/**
 * @author Ercilio Nascimento
 */
public class RequestDAO extends BaseDAO {
    public RequestDAO() throws ClassNotFoundException, SQLException {
        super();
    }

    public boolean validateUser(String user, String password) throws SQLException {
		boolean validate = false;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT `u`.`password` FROM `sodb_novo`.`user` u ");
		sql.append("WHERE `u`.`username` = ? ");
		// sql.append("AND `u`.`password` = ? ");
		PreparedStatement prst = conn.prepareStatement(sql.toString());
		prst.setString(1, user);
		// prst.setString(2, password);
		ResultSet rs = prst.executeQuery();

		if (rs.next()) {

			validate = BCrypt.checkpw(password, rs.getString(1));
		}

		System.out.println("Validating user...");
		log("SQL - VALIDATE USER: ", sql, new Object[] { user });

		return validate;
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

	public SmartObjectList getSOList(String user) throws SQLException {
		SmartObjectList solist = new SmartObjectList();
		StringBuilder sql = new StringBuilder();
        sql.append("SELECT  `so`.`idsmartobject`, `so`.`idsomodbus`, `so`.`serverurl`, ");
        sql.append("`so`.`name`, `p`.`register_modbus`, `ser`.`name`, `p`.`name`, `p`.`type`,  `p`.`minvalue`, ");
        sql.append("`p`.`maxvalue`, `p`.`options` FROM `sodb_novo`.`user` AS `u` INNER JOIN `sodb_novo`.`souserjoin` ");
        sql.append("souj ON `souj`.`iduser` = `u`.`iduser`  JOIN `sodb_novo`.`smartobject` so ");
        sql.append("ON `so`.`idsmartobject` = `souj`.`idsmartobject` JOIN `sodb_novo`.`service` ser ");
        sql.append("ON `ser`.`idsmartobject` = `so`.`idsmartobject` LEFT OUTER JOIN `sodb_novo`.`parameter` p ");
        sql.append("ON `p`.`idservice` = `ser`.`idservice`  WHERE `u`.`username` = ? ");
        sql.append("ORDER BY `so`.`idsmartobject`,  `ser`.`idservice`,  `p`.`idparameter`;");
		PreparedStatement prst = conn.prepareStatement(sql.toString());
		prst.setString(1, user);
		ResultSet rs = prst.executeQuery();

		while (rs.next()) {
			solist.getList().add(getConcatValues(rs));
		}

		System.out.println("Getting smart object list...");
		log("SQL - GET SO LIST: ", sql, new Object[] { user });

		return solist;

	}

	public String refreshSO(String command) throws SQLException {
		StringBuilder sql = new StringBuilder();
		SmartObject so = SmartObject.splitCommandToSmartObject(command);
		if (this.existsSO(so.getSoName())) {
			// TODO atualizar SO
		}
		return "objeto inteligente refrescado com sucesso!";
	}

	private boolean existsSO(String name) throws SQLException {
		boolean exists = false;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT 1 FROM `smartobject` AS `so` WHERE `so`.`name`= ?");
		PreparedStatement prst = conn.prepareStatement(sql.toString());
		prst.setString(1, name);
		ResultSet rs = prst.executeQuery();

		if (rs.next()) {
			exists = true;
		}

		log("SQL - EXISTS SO: ", sql, new Object[] { name });

		return exists;
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
