package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList; 

import sql.DbManager;

public class Excercise {
	private int id;
	private String title;
	private String description; 
	 
	public Excercise(){	}
	
	public Excercise(String title, String description){
		this.id = 0;
		setTitle(title).setDescription(description);
	}
	public Excercise setTitle(String title) {
		this.title = title;
		return this;
	}
	public String getTitle() {
		return title;
	}
	public Excercise setDescription(String description) {
		this.description = description;
		return this;
	}
	public String getDescription() {
		return description;
	}
	 
	public int getId() {
		return id;
	}
	@Override
	public String toString(){
		return "id: "+this.id+" title: "+this.title+" description:"+this.description;
	}
	// non-static DB methods
	public void saveToDB(){
		if(this.id==0){
			try {
				String generatedColumns[] = { "ID" };
				PreparedStatement stmt = DbManager.getPreparedStatement("INSERT INTO excercise(title,description) VALUES (?,?)",generatedColumns);
				stmt.setString(1, this.title);
				stmt.setString(2, this.description);
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
				PreparedStatement stmt = DbManager.getPreparedStatement("UPDATE excercise SET title = ?, description = ? WHERE id = ?");
				stmt.setString(1, this.title);
				stmt.setString(2, this.description); 
				stmt.setInt(3, this.id);
				stmt.executeUpdate();
			}catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		}
	}
	public void delete(){
		String sql = "DELETE FROM excercise WHERE id= ?";
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
	public static ArrayList<Excercise> loadAllByUserId(int userId){
		try{
			String sql = "SELECT * FROM excercise "
					+ "JOIN solution ON solution.excercise_id = excercise.id "
					+ "JOIN users ON solution.users_id = users.id "
					+ "WHERE users.id = ?";
			PreparedStatement stmt = DbManager.getPreparedStatement(sql); 
			stmt.setInt(1, userId);
			return getExcerciseFromStatement(stmt);
		}catch(SQLException e){
			System.err.println(e.getMessage());
		}
		return null;
	}
	public static ArrayList<Excercise> loadAll(){
		String sql = "SELECT * FROM excercise"; 
		PreparedStatement stmt = DbManager.getPreparedStatement(sql); 
		
		return getExcerciseFromStatement(stmt);
	}

	private static ArrayList<Excercise> getExcerciseFromStatement(PreparedStatement stmt) {
		try {
			ArrayList<Excercise> excercises = new ArrayList<Excercise>();
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				Excercise loadedEx = new Excercise();
				loadedEx.id = resultSet.getInt("id"); 
				loadedEx.title = resultSet.getString("title"); 
				loadedEx.description = resultSet.getString("description"); 
				 
				excercises.add(loadedEx);
			}
			return excercises;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} 
		
		return null;
	}
	public static Excercise loadById(int id){
		try { 
			String sql = "SELECT * FROM excercise where id=?";
			PreparedStatement stmt = DbManager.getPreparedStatement(sql); 
			stmt.setInt(1, id);
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				Excercise loadedEx = new Excercise();
				loadedEx.id = resultSet.getInt("id"); 
				loadedEx.title = resultSet.getString("title"); 
				loadedEx.description = resultSet.getString("description"); 
				return loadedEx;
			}
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} 
		
		return null;
	}
	
}
