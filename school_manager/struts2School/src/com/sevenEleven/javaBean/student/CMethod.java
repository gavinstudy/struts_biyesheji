package com.sevenEleven.javaBean.student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;


public class CMethod  {
	private Connection conn;
	private Statement smt;
	private ResultSet res;

	public CMethod() {
		connection();
		// TODO Auto-generated constructor stub
	}

	public void connection() {
		// TODO Auto-generated method stub
			//Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver").newInstance();
			try {
				DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			//String ur1 = "jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=school";
			String url1 = "jdbc:sqlserver://127.0.0.1:1433;databaseName=school";
			String user1 = "sa";
			String pwd1 = "123456";
			try {
				conn = DriverManager.getConnection(url1, user1, pwd1);
				//conn = DriverManager.getConnection(url);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public ResultSet query(String sql) {
		try {
			res = conn.createStatement().executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	
	public int Update(String sql) throws SQLException {
		int update = 0;
		try {
			update = conn.createStatement().executeUpdate(sql);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return update;

	}
	
	public void closeConn() throws SQLException {
		if (res != null)
			res.close();
	}
}
