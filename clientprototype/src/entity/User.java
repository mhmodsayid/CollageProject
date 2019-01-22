package entity;

public class User {
	public enum status{
	NotRegistered,Active,Frozen,Locked
}
	private String userID=null;
	private String Email=null;
	private String Password=null;
	private String firstName=null;
	private String lastName=null;
	private String userType=null;
	private int logInStatus=-1;
	private String phone=null;
	private String Username=null;
	private String ConfirmPassword=null;
	private String UserStatus=null;
	
	public User() {
		super();
	}
	public User(String UserID, String email, String password, String confirmpassword, String FirstName,
			String LastName,String UserType,int LogInStatus ,String Phone , String username, String userStatus) {
		super();
		this.userID = UserID;
		this.Email = email;
		this.Password = password;
		this.ConfirmPassword = confirmpassword;
		this.firstName = FirstName;
		this.lastName = LastName;
		this.userType = UserType;
		this.logInStatus = LogInStatus;
		this.phone = Phone;
		this.Username = username;
		this.UserStatus = userStatus;
	}
	public String getuserStatus() {
		return UserStatus;
	}
	
	public void setuserStatus(String userStatus) {
		this.UserStatus = userStatus;
		
	}
	
	public String getconfirmpassword() {
		return ConfirmPassword;
	}
	
	public void setconfirmpassword(String confirmpassword) {
		this.ConfirmPassword = confirmpassword;
		
	}
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public int getLogInStatus() {
		return logInStatus;
	}
	public void setLogInStatus(int logInStatus) {
		this.logInStatus = logInStatus;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getUsername() {
		return Username;
	}
	public void setUsername(String username) {
		Username = username;
	}
}