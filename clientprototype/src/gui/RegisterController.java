package gui;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import controller.ConnectionToServer;
import entity.Book;
import entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import ocsf.client.ChatIF;
/**
 * this class for Register process
 * in this class we check the input from the user(empty fields,illegal entered informations,data length)
 * the librarian or the manager must enter all the data of the new user
 * then the server check if the entered email or id or username are founded in the database
 * if the entered data founded in the database then the user see an error massage(user exist already)
 * if the entered data not founded in the database then the system build a new account and save data in the database
 * @author Fares Bsoul
 */
public class RegisterController extends NavigationBar implements Initializable, ChatIF{

	User user;
	JOptionPane frame;
	
    @FXML
    private TextField FirstName;

    
    @FXML
    private TextField LastName;

    @FXML
    private TextField Email;

    @FXML
    private TextField Phone;

    @FXML
    private TextField UserName;

    @FXML
    private PasswordField  Password;

    @FXML
    private PasswordField  ConfirmPassword;

    @FXML
    private Button Register;

    @FXML
    private Button ClearAll;

    @FXML
    private Button searchbutton;

    @FXML
    private Button addbutton;

    @FXML
    private Button returnbutton;

    @FXML
    private Button borrowbutton;


    
    @FXML
    private Button registerbutton;

    @FXML
    private Button extendbutton;

    @FXML
    private Button readercardbutton;

    @FXML
    private Button logoutbutton;

    @FXML
    private TextField UserID;
    
    @FXML
    private ComboBox<String> combobox1; 

    @FXML
    private Text UserInformation;

	ObservableList<String> list1 = FXCollections.observableArrayList("Librarian","Reader","Manager");

    private int logInStatus=0;
    
	private String combobox11;
	ActionEvent event;
	ActionEvent event1;
	 /**
	  * this function clear all the fields when the user press on clear all button
	  * @param event
	  */
	@FXML
    public void ClearAll(ActionEvent event) throws IOException { 
		FirstName.setText(null);
    	Phone.setText(null);
    	Email.setText(null);
    	LastName.setText(null);
    	Password.setText(null);
    	ConfirmPassword.setText(null);
    	UserID.setText(null);
    	UserName.setText(null);
    	combobox1.setValue("Select Role");
    }     
	
	/**
	 * this function get the data that the user entered
	 * check the input data , and show massages error when the user missing a field empty or entered illegal input and check the input length
	 * then we build a string that include ("case number in the server =23 "+ all the parameters)
	 * then we send this string to the server
	 * then the server check:
	 * 1-if the entered data is (the username or userid or email) are founded in the DB then the server send an error massage
	 * else :save the entered data in the DB ,and build a new account and sent massage (register done) 
	 * @param event
	 * @throws IOException
	 * @throws InterruptedException 
	 */
    @FXML
    void RegisterDone(ActionEvent event) throws IOException, InterruptedException {
    	user.setUserID(UserID.getText());//
    	user.setFirstName(FirstName.getText());//
    	user.setLastName(LastName.getText());//
    	user.setEmail(Email.getText());//
    	user.setPhone(Phone.getText());//
    	user.setPassword(Password.getText());//
    	user.setconfirmpassword(ConfirmPassword.getText());//
    	user.setUsername(UserName.getText());//
    	user.setLogInStatus(logInStatus);//
    	user.setuserStatus("Active");//
    	combobox11=combobox1.getSelectionModel().getSelectedItem();//
    	if(combobox11!="Reader"&&combobox11!="Manager"&&combobox11!="Librarian")
        user.setUserType("");//
    	else
    	user.setUserType(combobox11);//
    
    	if(user.getUserID().equals("")  ||user.getUsername().equals("")  ||user.getEmail().equals("")	||user.getPhone().equals("")
    	|| user.getPassword().equals("")||user.getFirstName().equals("")||user.getLastName().equals("")
    	||user.getconfirmpassword().equals("")||user.getUserType().equals("")) 
    		JOptionPane.showMessageDialog(frame, "You must fill in all of the fields"); 
    	else
      		if(user.getUserID().matches("[a-zA-Z]*")) 
				JOptionPane.showMessageDialog(frame, "User ID must contains digits only"); 
      	else
          	if(user.getUserID().length()!=9) 
          		JOptionPane.showMessageDialog(frame, "User ID must contains 9 digits exactly"); 
      	else
      		if(user.getFirstName().matches("[0-9]*")|| user.getLastName().matches("[0-9]*"))
    			JOptionPane.showMessageDialog(frame, "First name and Last name must contains letters only"); 
      	else
        	 if(user.getPhone().matches("[a-zA-Z]*"))
        	  	JOptionPane.showMessageDialog(frame, "Phone number must contains digits only"); 
        else
             if(user.getPhone().length()!=10&&user.getPhone().length()!=9)
            	JOptionPane.showMessageDialog(frame, "Phone number must contains 10 digits or 9 digits only"); 
    	else
    		 if(!user.getPassword().equals(user.getconfirmpassword()))
    		 	JOptionPane.showMessageDialog(frame, "Confirm password must match a password field");
    	else {
			Thread.sleep(10);
			//ClearAll(null);   
    		try { 		  
 	 		String command = "23" + user.getUserID()+","+user.getEmail()+","+user.getPassword()+","+user.getFirstName()+","+user.getLastName()+","+user.getUserType()+","+user.getLogInStatus()+","+user.getPhone()+","+user.getuserStatus()+","+user.getUsername();
 			ConnectionToServer.sendData(this, command);
 			}catch (IOException e) {
 			e.printStackTrace();
 			} 
    		 	
    	}
    }
   
	/**
	 * this function get massage from the server
	 * if the server send that UserID exsist already or email exsist already or user exsist already then this function show to the user a error massage
	 * else show the username and the password (for the new account) .
	 */
    private String massage=null;
	@Override
	public void display(Object msg) {
		massage=(String) msg;
		// TODO Auto-generated method stub
		if(msg.equals("user exsist already"))
			JOptionPane.showMessageDialog(frame, "Sorry , this username is already taken");
		else 
			if(msg.equals("UserID exsist already"))
				JOptionPane.showMessageDialog(frame, "Sorry , this ID is already taken");
		else 
			if(msg.equals("email exsist already"))
				JOptionPane.showMessageDialog(frame, "Sorry , this Email is already taken");
		else  {	
			JOptionPane.showMessageDialog(frame,"Register Done  \n username is " + user.getUsername()+ ",\n Password is " + user.getPassword());
			FirstName.setText(null);
	    	Phone.setText(null);
	    	Email.setText(null);
	    	LastName.setText(null);
	    	Password.setText(null);
	    	ConfirmPassword.setText(null);
	    	UserID.setText(null);
	    	UserName.setText(null);
	    	combobox1.setValue("Select Role");
		}
	}
	
	/**
	 * initialize a combobox that include a parameters from the list
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
     	user = new User();
		combobox1.setItems(list1);
		UserInformation.setText(LoginController.UserInfo2);
	}

}