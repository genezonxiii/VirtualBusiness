package tw.com.aber.util;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Database {
	private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	private static final String URL = "jdbc:mysql://192.168.112.164:3306/db_virtualbusiness?" + 
			"useSSL=false&useUnicode=true&characterEncoding=UTF-8";
	private static final String PASSWORD = "admin123";
	private static final String USER_NAME = "root";
	private static final Logger logger = LogManager.getLogger(Database.class);
	
	public static Connection getConnection() {
		try {
			Class.forName(MYSQL_DRIVER);
			Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
			return connection;
		} catch (Exception ex) {
			logger.error("Database.getConnection() Error -->" + ex.getMessage());
			return null;
		}
	}

	public static void close(Connection connection) {
		try {
			connection.close();
		} catch (Exception ex) {
		}
	}
}
