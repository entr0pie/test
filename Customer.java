package coisas_e_coisas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer {
	public int id;
	public String CPF;
	public String name;
	public String surname;
	private static String table_name = "customers";
	
	public Customer (String CPF, String name, String surname) {
		this.CPF = CPF;
		this.name = name;
		this.surname = surname;
	}

	public static Customer findOne(int id) {
	    Connection conn = Database.getConnection();
	    String sql = "SELECT * FROM " + Customer.table_name + " WHERE id = ?";
	    
	    try {
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setInt(1, id);
	        ResultSet rs = ps.executeQuery();
	        if (!rs.next()) {return null;}

	        Customer customer = new Customer(rs.getString("CPF"), rs.getString("name"), rs.getString("surname"));
	        customer.id = rs.getInt("id");
	        return customer;
		
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
	            String query = "UPDATE " + Customer.table_name + " SET CPF = ?, name = ?, surname = ? WHERE id = ?;";
	            ps = conn.prepareStatement(query);

	            ps.setString(1, this.CPF);
	            ps.setString(2, this.name);
	            ps.setString(3, this.surname);
	            ps.setInt(4, this.id);

	            System.out.println(ps);
	            ps.executeUpdate();

	        } else {
	            String query = "INSERT INTO " + Customer.table_name + " (CPF, name, surname) VALUES (?, ?, ?);";
	            ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

	            ps.setString(1, this.CPF);
	            ps.setString(2, this.name);
	            ps.setString(3, this.surname);
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
		String sql = "DELETE FROM " + Customer.table_name + " WHERE id = ?;";
		
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
