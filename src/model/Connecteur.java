package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Connecteur {
	private static final String URL = "jdbc:mysql://localhost/";
	private static final String LOGIN = "root";
	private static final String PASSWORD = "";
	private static final String DBNAME = "db_projet_vulnerabilites";

	public static Connection getConnection() {
		Connection c = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver OK !!! ");

		} catch (ClassNotFoundException e) {
			System.out.println("Driver introuvable !!! ");
			e.printStackTrace();
		}
		try {
			c = DriverManager.getConnection(URL + DBNAME, LOGIN, PASSWORD);			
			System.out.println("!Connection établie !!!");
		} catch (SQLException e) {
			System.out.println("Connexion impossible!!! ");
			e.printStackTrace();
		}
		return c;
	}

}
