package util;

import java.sql.*;

/**
 * Tiago Henrique Iwamoto
 * tiago.iwamoto@telefonica.com
 * System Analyst
 * 41 9 9513-0230
 **/
public class MySqlConnection {

    public Connection getConnection(String url, String db, String user, String pass) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        //Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String database = String.format("jdbc:mysql://%s:3306/%s?autoReconnect=true&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC", url, db);
        //String database = String.format("jdbc:mysql://10.40.197.137:3306/%s?autoReconnect=true&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC", db);
        Connection conn = DriverManager.getConnection(database, user, pass);
        //Connection conn = DriverManager.getConnection(database, "root", "efika123@gvt");
        return conn;
    }

    /**
     * Metodo recebe uma conexao e uma query
     * e a executa sem tratamento no comando passado
     * @param conn
     * @param sql
     * @return
     * @throws SQLException
     */
    public ResultSet getRs(Connection conn, String sql) throws SQLException {
        Statement st = conn.createStatement();
        return st.executeQuery(sql);
    }

    /**
     * Metodo recebe uma conexao e uma query
     * e a executa fazendo o tratamento no comando passado
     * @param conn
     * @param sql
     * @return
     */
    public PreparedStatement getSt(Connection conn, String sql) throws SQLException {
        return conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    }


}
