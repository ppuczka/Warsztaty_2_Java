package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import sql.DbManager;

public class Group {
	private int id;
	private String name; 
	 
	public Group(){	}
	
	public Group(String name){
		this.id = 0;
		setName(name);
	}
	public Group setName(String name) {
		this.name = name;
		return this;
	}
	public String getName() {
		return name;
	}
	 
	 
	public int getId() {
		return id;
	}
	@Override
	public String toString(){
		return "id: "+this.id+" name: "+this.name;
	}
	// non-static DB methods
	public void saveToDB(){
		if(this.id==0){
			try {
				String generatedColumns[] = { "ID" };
				PreparedStatement stmt = DbManager.getPreparedStatement("INSERT INTO user_group(name) VALUES (?)",generatedColumns);
				stmt.setString(1, this.name); 
				stmt.executeUpdate();
				ResultSet rs = stmt.getGeneratedKeys(); 
				if (rs.next()) {
					this.id = rs.getInt(1);
				}
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		}else{
			try{
				PreparedStatement stmt = DbManager.getPreparedStatement("UPDATE user_group SET name = ? WHERE id = ?");
				stmt.setString(1, this.name);  
				stmt.setInt(2, this.id);
				stmt.executeUpdate();
			}catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		}
	}
	public void delete(){
		String sql = "DELETE FROM user_group WHERE id= ?";
		try{
			PreparedStatement stmt = DbManager.getPreparedStatement(sql);
			stmt.setInt(1, this.id); 
			stmt.executeUpdate();
			this.id=0;
		}catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}
	// static DB methods
	public static ArrayList<Group> loadAll(){
		try {
			ArrayList<Group> groups = new ArrayList<Group>();
			String sql = "SELECT * FROM user_group"; 
			PreparedStatement stmt = DbManager.getPreparedStatement(sql); 
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				Group loadedGroup = new Group();
				loadedGroup.id = resultSet.getInt("id"); 
				loadedGroup.name = resultSet.getString("name");   
				 
				groups.add(loadedGroup);
			}
			return groups;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} 
		
		return null;
	}
	public static Group loadById(int id){
		try { 
			String sql = "SELECT * FROM user_group where id=?";
			PreparedStatement stmt = DbManager.getPreparedStatement(sql); 
			stmt.setInt(1, id);
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				Group loadedGroup = new Group();
				loadedGroup.id = resultSet.getInt("id"); 
				loadedGroup.name = resultSet.getString("name");  
				return loadedGroup;
			}
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} 
		
		return null;
	}
}
