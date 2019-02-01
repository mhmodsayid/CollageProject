package entity;

public class logIn {
	
	
	private String Password=null;
	private String Username=null;
	
	public logIn() {
		super();
	}
	public logIn(String password,String username) {
		super();
		
		this.Password = password;
		this.Username = username;
	}
	
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getUsername() {
		return Username;
	}
	public void setUsername(String username) {
		Username = username;
	}
}