package coisas_e_coisas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Worker {
	private int id;
	public String CPF;
	public String name;
	public String surname;
	private String date_birth;
	public String phone_number;
	public String address;

	private static String table_name = "workers";
	
	public Worker (String CPF, String name, String surname, String date_birth, String phone_number, String address) {
        this.CPF = CPF;
        this.name = name;
        this.surname = surname;
        this.date_birth = date_birth;
        this.phone_number = phone_number;
        this.address = address;
    }
	

	public static Worker findOne(int id) {
	    Connection conn = Database.getConnection();
	    String sql = "SELECT * FROM " + Worker.table_name + " WHERE id = ?";
	    
	    try {
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setInt(1, id);
	        ResultSet rs = ps.executeQuery();
	        if (!rs.next()) {return null;}

	        Worker worker = new Worker(rs.getString("CPF"), rs.getString("name"),
	        						   rs.getString("surname"), rs.getString("date_birth"), 
	        						   rs.getString("phone_number"), rs.getString("address"));
	        
	        worker.id = rs.getInt("id");
	        return worker;
		
	    } catch (Exception e) {
	        e.printStackTrace();
	    } 
	    
	    return null;

	} 
	
	public boolean save() {
	    Connection conn = Database.getConnection();

	    try {
	        PreparedStatement ps = null;

	        if (Customer.findOne(this.id) != null) {
	        	String query = "UPDATE " + Worker.table_name + " SET CPF = ?, name = ?, surname = ?, date_birth = ?, phone_number = ?, address = ? WHERE id = ?;";
	            ps = conn.prepareStatement(query);

	            ps.setString(1, this.CPF);
	            ps.setString(2, this.name);
	            ps.setString(3, this.surname);
	            ps.setString(4, this.date_birth);
	            ps.setString(5,  this.phone_number);
	            ps.setString(6, this.address);
	            ps.setInt(7, this.id);

	            System.out.println(ps);
	            ps.executeUpdate();

	        } else {
	            String query = "INSERT INTO " + Worker.table_name + " (CPF, name, surname, date_birth, phone_number, address) VALUES (?, ?, ?, ?, ?, ?);";
	            ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

	            ps.setString(1, this.CPF);
	            ps.setString(2, this.name);
	            ps.setString(3, this.surname);
	            ps.setString(4, this.date_birth);
	            ps.setString(5,  this.phone_number);
	            ps.setString(6, this.address);
	            ps.setInt(7, this.id);
	            
	            
	            ps.executeUpdate();

	            ResultSet generatedKeys = ps.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                this.id = generatedKeys.getInt(1);
	            }
	        }

	        return true;

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return false;
	}
	
	
	public boolean delete() {
		Connection conn = Database.getConnection();
		String sql = "DELETE FROM " + Worker.table_name + " WHERE id = ?;";
		
		try {
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, this.id);
			System.out.println(ps.toString());
			ps.executeUpdate();
			
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
