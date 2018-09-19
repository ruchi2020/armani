package com.chelseasystems.cr.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

public class DBConnect {
	public static void main(String[] main) {
		String host = "jdbc:oracle:thin:@10.201.101.69:1521:posprd";
		String uname="rposprd";
		String password="rposprd123";
		String query ="Select * from AS_ITM";
		String dbtime;
		try {
			DriverManager.registerDriver (new oracle.jdbc.OracleDriver());
			Connection con = DriverManager.getConnection(host,uname,password);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()){
			dbtime=rs.getString(1);
			System.out.println(dbtime);
			}
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
