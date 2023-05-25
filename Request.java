package coisas_e_coisas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Request {
	public String contract_date;
	
	private int id;
	private int customer_id;
	private int services;
	
	private static String table_name = "requests";

	public static Request findOne(int id) {
	    Connection conn = Database.getConnection();
	    String sql = "SELECT * FROM " + Request.table_name + " WHERE id = ?";
	    
	    try {
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setInt(1, id);
	        ResultSet rs = ps.executeQuery();
	        if (!rs.next()) {return null;}

	        Request req = new Request();
	        req.id = rs.getInt("id");
	        req.contract_date = rs.getString("contract_date");
	        req.customer_id = rs.getInt("customer_id");
	        req.services = rs.getInt("services");
	        
	        return req;
		
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    
	    
	    return null;

	} 
}
