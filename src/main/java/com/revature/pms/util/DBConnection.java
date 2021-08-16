package com.revature.pms.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

	public static Connection getDBConnection() {
		Connection connection = null;
		Properties properties = new Properties();
		try {
			FileReader reader = new FileReader("C:\\Users\\MeenaGovindaraj\\eclipse-workspace\\Revature_traning\\DB.Properties");
			properties.load(reader);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String driver =null;
		String url =null;
		String username =null;
		String password=null;
		
		driver= properties.getProperty("driver");
		url= properties.getProperty("url");
		username= properties.getProperty("username");
		password= properties.getProperty("password");
		
		try {
			Class.forName(driver); // Type 4 driver

			connection = DriverManager.getConnection(url,username,password);
		}
		catch (SQLException e) {
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return connection;
	}
}
