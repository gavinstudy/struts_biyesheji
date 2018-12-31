package com.sevenEleven.javaBean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CDBConnection {
	private final static String URL = "jdbc:sqlserver://127.0.0.1:1433;databaseName=school";
	private final static String UNAME = "sa";
	private final static String UPWD = "123456";
	protected static Connection con;
	protected static ResultSet rs;
	protected static Statement stmt;
	protected static String sql;

	public static Connection getConnection() {
		try {
			DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
			con = DriverManager.getConnection(URL, UNAME, UPWD);
			stmt=con.createStatement();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return con;
	}

	public ResultSet myExecuteQuery(String sql) {
		ResultSet rs = null;
		try {
			con = getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
		} catch (SQLException ex) {

			System.err.println("executeQuery():" + ex.getMessage());
		}
		return rs;
	}
	
	
	public static void close()
	{
		
			try {
				if(rs!=null)
				   rs.close();
				if(stmt!=null)
					stmt.close();
				if(con!=null)
					con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}	
}


