package coisas_e_coisas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Service {
	private int id;
	public String name;
	public String type;
	public double price;
	
	private static String table_name = "Services";
	
	public Service (String name, String type, double price) { 
		this.name = name;
		this.type = type;
		this.price = price;
	}
	
	public static Service findOne(int id) {
	    Connection conn = Database.getConnection();
	    String sql = "SELECT * FROM " + Service.table_name + " WHERE id = ?";
	    
	    try {
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setInt(1, id);
	        ResultSet rs = ps.executeQuery();
	        if (!rs.next()) {return null;}
	        
	        Service service = new Service(rs.getString("name"), rs.getString("type"), rs.getDouble("price"));
	        service.id = rs.getInt("id");
	        return service;
		
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
                String query = "UPDATE " + Service.table_name + " SET name = ?, type = ?, price = ? WHERE id = ?;";
                ps = conn.prepareStatement(query);

                ps.setString(1, this.name);
                ps.setString(2, this.type);
                ps.setDouble(3, this.price);
                ps.setInt(4, this.id);

                ps.executeUpdate();
                

	        } else {
                String query = "INSERT INTO " + Service.table_name + " (name, type, price) VALUES (?, ?, ?);";
                ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

                ps.setString(1, this.name);
                ps.setString(2, this.type);
                ps.setDouble(3, this.price);
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
		String sql = "DELETE FROM " + Service.table_name + " WHERE id = ?;";
		
		try {
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, this.id);
			ps.executeUpdate();
			
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
