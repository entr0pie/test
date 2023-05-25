package coisas_e_coisas;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
	
	static Connection instance = null;
	private Database() {}
	
	public static Connection getConnection() {		
		if (instance == null) {
			String url = "jdbc:mysql://localhost:3306/coisas_e_coisas";
			String user = "root";
			String passwd = "senhafoda";
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection conn = DriverManager.getConnection(url, user, passwd);
				Database.instance = conn;
			} catch (Exception e) {
				e.printStackTrace();
			}		
		}
		
		return instance;
	}
	
}
