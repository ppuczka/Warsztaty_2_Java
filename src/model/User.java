package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.mindrot.jbcrypt.BCrypt;

import sql.DbManager;

public class User {

	private long id;
	private String username;
	private String email;
	private String password;
	private String salt;
	private int person_group_id;
	
	public User(){
		
	}
	
	public User(String username, String email, String password, int person_group_id){
		this.id = 0;
		setUsername(username).
		setEmail(email).
		setPassword(password).
		setPersonGroupId(person_group_id);
	}
	
	public User setUsername(String username) {
		this.username = username;
		return this;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getEmail() {
		return email;
	}
	
	public User setEmail(String email) {
		this.email = email;
		return this;
	}
	public String getPassword() {
		return password;
	}
	public User setPassword(String password) {
		this.salt = BCrypt.gensalt();
		this.password = BCrypt.hashpw(password, salt);
		return this;
	}
	public long getId() {
		return id;
	}
	public User setPersonGroupId(int id){
		this.person_group_id = id;
		return this;
	}
	public int getPersonGroupId(){
		return this.person_group_id;
	}
	@Override
	public String toString(){
		return "id: "+this.id+" username: "+this.username+" email:"+this.email+" password:" + this.password;
	}
	
	
	// non-static DB methods
	public void saveToDB(){
		if(this.id==0){
			try {
				String generatedColumns[] = { "ID" };
				PreparedStatement stmt = DbManager.getPreparedStatement("INSERT INTO users(username,email,password,salt,person_group_id) VALUES (?,?,?,?,?)",generatedColumns);
				stmt.setString(1, this.username);
				stmt.setString(2, this.email);
				stmt.setString(3, this.password);
				stmt.setString(4, this.salt);
				stmt.setInt(5, this.person_group_id);
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
				PreparedStatement stmt = DbManager.getPreparedStatement("UPDATE users SET username=?, email=?, person_group_id=?, password=?, salt=? WHERE id=?");
				stmt.setString(1, this.username);
				stmt.setString(2, this.email);
				stmt.setInt(3, this.person_group_id);
				stmt.setString(4, this.password);
				stmt.setString(5, this.salt);
				stmt.setLong(6, this.id);
//				System.out.println(stmt);
				stmt.executeUpdate();
			}catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		}
	}
	public void delete(){
		String sql = "DELETE FROM users WHERE id= ?";
		try{
			if(this.id!=0){
				PreparedStatement stmt = DbManager.getPreparedStatement(sql);
				stmt.setLong(1, this.id); 
				stmt.executeUpdate();
				this.id=0;
			}
		}catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}
	// static DB methods
	public static ArrayList<User> loadAllByGroupId(int groupId){
		String sql = "SELECT * FROM users "
				+ "JOIN user_group ON users.person_group_id = user_group.id "
				+ "WHERE user_group.id = ?";
		PreparedStatement stmt = DbManager.getPreparedStatement(sql);
		try {
			stmt.setInt(1, groupId);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return getUsersFromStatement(stmt);
	}

	public static ArrayList<User> loadAll(){
		String sql = "SELECT * FROM users"; 
		PreparedStatement stmt = DbManager.getPreparedStatement(sql); 
		return getUsersFromStatement(stmt);
	}
	private static ArrayList<User> getUsersFromStatement(PreparedStatement stmt) {
		try {
			ArrayList<User> users = new ArrayList<User>(); 
			ResultSet resultSet = stmt.executeQuery();
//			resultSet.last();
//			int size = resultSet.getRow();
//			resultSet.beforeFirst();
//			System.out.println("LOG COUNT Result: "+size);
			while (resultSet.next()) {
				User loadedUser = new User();
				loadedUser.id = resultSet.getLong("id"); 
				loadedUser.username = resultSet.getString("username"); 
				loadedUser.password = resultSet.getString("password"); 
				loadedUser.email = resultSet.getString("email"); 
				loadedUser.salt = resultSet.getString("salt"); 
				loadedUser.person_group_id = resultSet.getInt("person_group_id");
				users.add(loadedUser);
			}
			return users;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} 
		return null;
	}
	public static User loadById(int id){
		try { 
			String sql = "SELECT * FROM users where id=?";
			PreparedStatement stmt = DbManager.getPreparedStatement(sql); 
			stmt.setInt(1, id);
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				User loadedUser = new User();
				loadedUser.id = resultSet.getInt("id"); 
				loadedUser.username = resultSet.getString("username"); 
				loadedUser.password = resultSet.getString("password"); 
				loadedUser.email = resultSet.getString("email"); 
				loadedUser.salt = resultSet.getString("salt"); 
				return loadedUser;
			}
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} 
		
		return null;
	}
	
}
