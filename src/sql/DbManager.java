package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbManager {
	static Connection connection;
	public static Connection getConnection() throws Exception {
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "workshop"; 
        String userName = "root";
        String password = "";
        if(connection==null){
        		connection = DriverManager.getConnection(url + dbName, userName,password);
        }else if(connection.isClosed()){
        		connection = DriverManager.getConnection(url + dbName, userName,password);
        }
        return connection;
    }
	public static PreparedStatement getPreparedStatement(String sql, String[] columns){
		try {
			return getConnection().prepareStatement(sql, columns);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return null;
	}
	public static PreparedStatement getPreparedStatement(String sql){
		try {
			return getConnection().prepareStatement(sql);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return null;
	}
	public static void close(){
		try {
			connection.close();
	    } catch (SQLException e) {
	    	System.err.println(e.getMessage());
	    }
	}
}
