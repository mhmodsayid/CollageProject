
	package entity;

	import javafx.beans.property.SimpleStringProperty;
	import javafx.beans.property.StringProperty;
	import javafx.beans.value.WritableObjectValue;

	public class TableWorker {

		private final SimpleStringProperty firstName;
		private final SimpleStringProperty lastName;
		private final SimpleStringProperty userID;
		private final SimpleStringProperty email;
		private final SimpleStringProperty phone;
		private final SimpleStringProperty userType;
		private final SimpleStringProperty userStatus;
		
		
		public TableWorker(String FirstName,String LastName, String userID, String email, String phone,String userType,String usreStatus) {
			this.firstName=new SimpleStringProperty(FirstName);
			this.lastName=new SimpleStringProperty(LastName);
			this.userID= new SimpleStringProperty(userID);
			this.email=new SimpleStringProperty(email);
			this.phone=new SimpleStringProperty(phone);
			this.userType=new SimpleStringProperty(userType);
			this.userStatus=new SimpleStringProperty(usreStatus);
		}
		public String getFirstName() { return firstName.get();}
		public void FirstName(String fName) {firstName.set(fName);}
		public String getLastName() { return lastName.get();}
		public void LastName(String lName) {lastName.set(lName);}
		public String getUserID() { return userID.get();}
		public void setUserID(String uID) {userID.set(uID);}
		public String getEmail() { return email.get();}
		public void setEmail(String mail) {email.set(mail);}
		public String getPhone() { return phone.get();}
		public void setPhone(String phone1) {phone.set(phone1);}
		public String getUserType() { return userType.get();}
		public void setUserType(String uType) {userType.set(uType);}
		public String getUserStatus() { return userStatus.get();}
		public void setUserStatus(String uStatus) {userStatus.set(uStatus);}
		


		
		
		

	}
