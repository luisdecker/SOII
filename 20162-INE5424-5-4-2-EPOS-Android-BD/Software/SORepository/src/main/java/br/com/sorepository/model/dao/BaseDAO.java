package br.com.sorepository.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by fernando on 07/11/16.
 */
abstract class BaseDAO {
    private final String user = "root";
    private final String password = "projeto";
    private final String jdbc = "com.mysql.cj.jdbc.Driver";
    private final String url = "jdbc:mysql://localhost:3306/sodb_novo";
    protected Connection conn;

    public BaseDAO() throws ClassNotFoundException, SQLException {
        Class.forName(this.jdbc);
        this.conn = DriverManager.getConnection(this.url, this.user, this.password);
    }

    public void closeConnection() {
        try {
            this.conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
