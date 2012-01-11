package com.imaginea.scrumr.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {
	private static String url = "jdbc:mysql://localhost/scrumr";
	private static String driver = "com.mysql.jdbc.Driver";
	private static String username = "root";
	private static String password = "root";
	private static Connection con = null;
	
	private DatabaseConnection(){}

	public static Connection getConnectionInstance(){
		if(con == null){
			try {
				Class.forName(driver).newInstance();
				con = DriverManager.getConnection(url,username,password);
				return con;
			}catch( ClassNotFoundException e ) {
				e.printStackTrace();
				return null;
			}
			catch( SQLException e ) {
				e.printStackTrace();
				return null;
			}catch( Exception e ) {
				e.printStackTrace();
				return null;
			}
		}
		return con;
	}
	
	  protected void finalize()
      {
		  try {
			con.close();
		} catch (SQLException e) {
			 System.out.println("Connection close failed");
			e.printStackTrace();
		}
		  System.out.println("Connection is Closed");

      }
}
