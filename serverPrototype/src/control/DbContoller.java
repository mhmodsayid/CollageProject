package control;

import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * This class contains the information to connect to the DB
 * @author mahmoud sayid
 *
 */
public class DbContoller {
	 String username;
	 String password;
	 String db;
	 String ip;	 
	public DbContoller() {
		super();
	}
	public DbContoller(String username, String password, String db, String ip) {
		super();
		this.username = username;
		this.password = password;
		this.db = db;
		this.ip = ip;
	}
	
	public DbContoller(String username, String password, String db) {
		super();
		this.username = username;
		this.password = password;
		this.db = db;
		this.ip = "localhost";
	}
	
	/**
	 * Initialize the data base connection
	 * @return
	 */
	public  java.sql.Connection initalizeDataBase() {
		 try {
			return DriverManager.getConnection("jdbc:mysql://"+ip+"/"+db,username,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDb() {
		return db;
	}
	public void setDb(String db) {
		this.db = db;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	
}
