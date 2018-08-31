package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import sql.DbManager;

public class Solution {
	
	private int id;
	private Date created;
	private Date updated;
	private String description;
	
	public Solution(){}
	
	public Solution(Date created, Date updated, String description){
		this.id = 0;
		setCreated(created).setUpdated(updated).setDescription(description);
	}
	
	public Date getCreated() {
		return created;
	}
	public Solution setCreated(Date created) {
		this.created = created;
		return this;
	}
	public Date getUpdated() {
		return updated;
	}
	public Solution setUpdated(Date updated) {
		this.updated = updated;
		return this;
	}
	public String getDescription() {
		return description;
	}
	public Solution setDescription(String description) {
		this.description = description;
		return this;
	}
	public int getId() {
		return id;
	}
	
	@Override
	public String toString(){
		return "id: "+this.id+" description: "+this.description;
	}
	
	// non-static DB methods
	public void saveToDB(){
		if(this.id==0){
			try {
				String generatedColumns[] = { "ID" };
				PreparedStatement stmt = DbManager.getPreparedStatement("INSERT INTO solution(created,updated,description) VALUES (?,?,?)",generatedColumns);
				stmt.setDate(1, (java.sql.Date) this.created); 
				stmt.setDate(2, (java.sql.Date) this.updated);
				stmt.setString(3, description);
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
				PreparedStatement stmt = DbManager.getPreparedStatement("UPDATE solution SET created = ?,updated = ?,description = ?, WHERE id = ?");
				stmt.setDate(1, (java.sql.Date) this.created); 
				stmt.setDate(2, (java.sql.Date) this.updated);
				stmt.setString(3, description);
				stmt.setInt(3, id);
				stmt.executeUpdate();
			}catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		}
	}
	public void delete(){
		String sql = "DELETE FROM solution WHERE id= ?";
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
	public static ArrayList<Solution> loadAllByExerciseId(int excerciseId){
		try{
			String sql = "SELECT * FROM solution WHERE excercise_id = ?"; 
			PreparedStatement stmt = DbManager.getPreparedStatement(sql);
			stmt.setInt(1, excerciseId);
			return getSolutionsFromStatement(stmt);
		}catch(SQLException e){
			System.err.println(e.getMessage());
		}	
		return null;
	}
	public static ArrayList<Solution> loadAll(){
		String sql = "SELECT * FROM solution"; 
		PreparedStatement stmt = DbManager.getPreparedStatement(sql);
		return getSolutionsFromStatement(stmt);
	}

	private static ArrayList<Solution> getSolutionsFromStatement(PreparedStatement stmt) {
		try {
			ArrayList<Solution> solutions = new ArrayList<Solution>();
			 
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				Solution loadedSolution = new Solution();
				loadedSolution.id = resultSet.getInt("id"); 
				loadedSolution.created = resultSet.getDate("created"); 
				loadedSolution.updated = resultSet.getDate("updated");   
				loadedSolution.description = resultSet.getString("description");  
				solutions.add(loadedSolution);
			}
			return solutions;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} 
		
		return null;
	}
	public static Solution loadById(int id){
		try { 
			String sql = "SELECT * FROM solution where id=?";
			PreparedStatement stmt = DbManager.getPreparedStatement(sql); 
			stmt.setInt(1, id);
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				Solution loadedSolution = new Solution();
				loadedSolution.id = resultSet.getInt("id"); 
				loadedSolution.created = resultSet.getDate("created"); 
				loadedSolution.updated = resultSet.getDate("updated");   
				   
				return loadedSolution;
			}
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} 
		
		return null;
	}

}
