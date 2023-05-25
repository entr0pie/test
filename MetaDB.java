package coisas_e_coisas;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MetaDB {
	public Object object;
	private String table_name;
    private Class<?> obj_class;
    private Map<String, Object> mapped_object;
    
    public void updateObjectAttributes(Object object) {
        this.object = object;
        this.mapped_object = new LinkedHashMap<String, Object>();
        this.obj_class = object.getClass();

        Field[] fields = this.obj_class.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Object field_value;
            try {
                field_value = field.get(object);
            } catch (IllegalAccessException e) {
                field_value = null;
            }

            this.mapped_object.put(field.getName(), field_value);
        }
        
    }

    public MetaDB(Object object, String table_name) {
    	this.table_name = table_name.replaceAll("[^a-zA-Z0-9_]", "");
    	this.updateObjectAttributes(object);
        
    }
    
    public Object findOne(int id) {
        Connection conn = Database.getConnection();
        String sql = "SELECT * FROM " + this.table_name + " WHERE id = ?";
       
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Constructor<?> constructor = this.obj_class.getDeclaredConstructor();
                constructor.setAccessible(true);
                Object newInstance = constructor.newInstance();

                for (String key : mapped_object.keySet()) {
                    Field field = this.obj_class.getDeclaredField(key);
                    field.setAccessible(true);
                    field.set(newInstance, rs.getObject(key));
                }

                this.updateObjectAttributes(newInstance);
                return newInstance;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } 
        
        return null;
    }
    
    
    public boolean save() {
		Integer id = (Integer) this.mapped_object.get("id");
    	Connection conn = Database.getConnection();
		
		if (this.findOne(id) != null) {
    		String query = "UPDATE " + this.table_name + " SET ";
    		
    		ArrayList<Integer> index = new ArrayList<Integer>();
    		int i = 1;
    		
    		for (String key: this.mapped_object.keySet()) {
    			if (key.equals("id")) {continue;}
    			query += key + " = ?, ";
    			index.add(i);
    			i += 1;
    		}
    		
    		query += "WHERE id = ?";
    		query = query.replaceAll(", WHERE", " WHERE");
			
    		try {

				PreparedStatement ps = conn.prepareStatement(query);
				System.out.println(ps.toString());
				i = 0;
				for (String key: this.mapped_object.keySet()) {
					System.out.println(index.get(i) + " " + this.mapped_object.get(key));
					ps.setObject(index.get(i), this.mapped_object.get(key));
					i++;
				}
				
				System.out.println(ps.toString());
				ps.executeQuery();

    		} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
    		}
    		
    		
    		System.out.println(query);
    		return true;
    		
//    		String query = "UPDATE " + this.table_name + " SET CPF = ?, name = ?, surname = ? WHERE id = ?;";
//			ps = conn.prepareStatement(query);
//			
//			ps.setString(1, this.CPF);
//			ps.setString(2, this.name);
//			ps.setString(3, this.surname);
//			ps.setInt(4, this.id);
//			ps.executeUpdate();
    	}

		return false;
    	
    }
}