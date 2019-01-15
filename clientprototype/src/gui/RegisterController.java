package gui;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import controller.ConnectionToServer;
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
import ocsf.client.ChatIF;
public class RegisterController extends NavigationBar implements Initializable, ChatIF{

    @FXML
    private TextField FirstName;

    @FXML
    private TextField Phone;

    @FXML
    private TextField UserName;

    @FXML
    private PasswordField  Password;

    @FXML
    private Button Register;

    @FXML
    private TextField LastName;

    @FXML
    private PasswordField  ConfirmPassword;

    @FXML
    private Button ClearAll;

    @FXML
    private TextField Email;

    @FXML
    private Button searchbutton;

    @FXML
    private Button addbutton;

    @FXML
    private Button returnbutton;

    @FXML
    private Button borrowbutton;


    @FXML
    private CheckBox viewpass2;
    
    @FXML
    private Button registerbutton;

    @FXML
    private Button extendbutton;

    @FXML
    private Button readercardbutton;

    @FXML
    private Button logoutbutton;

    @FXML
    private ComboBox<String> combobox1;
    
    JOptionPane frame;
    private String userName=null;
    public  String firstName=null;
    private String password1=null;
    public  String phone=null;
    private String lastName=null;
    public  String confirmPassword=null;
    private String email=null;
	private String combobox11;
	private int combobox111;

    
    @FXML
    public void AddText() throws IOException{
    	userName=UserName.getText();
    	password1 = Password.getText();
    	firstName=FirstName.getText();
    	lastName = LastName.getText();
    	email=Email.getText();
    	phone = Phone.getText();
    	confirmPassword=ConfirmPassword.getText();
    	combobox11=combobox1.getSelectionModel().getSelectedItem();
    	if(combobox11=="Reader") {
    		combobox111=66;
    	
    	}
   	System.out.println(firstName+" "+lastName+""+userName+""+email+""+phone+""+password1+""+confirmPassword+""+combobox11);
   } 

    @FXML
    public void ClearAll(ActionEvent event)throws IOException {
    	FirstName.setText(null);
    	Phone.setText(null);
    	Email.setText(null);
    	LastName.setText(null);
    	UserName.setText(null);
    	Password.setText(null);
    	ConfirmPassword.setText(null);
    	combobox1.setValue("Select Role");
    }     

	  @FXML
	    void RegisterDone(ActionEvent event) {
		  	if(userName==null || password1==null|| firstName==null|| lastName==null|| email==null|| phone==null|| confirmPassword==null) 
				JOptionPane.showMessageDialog(frame, "You must fill in all of the fields"); 
		  	else
		  		if(firstName.matches("[0-9]*")|| lastName.matches("[0-9]*")) {
					JOptionPane.showMessageDialog(frame, "First name and Last name must contains letters only"); 
		  		}
			  	else
			  		if(phone.matches("[a-zA-Z]*")) {
						JOptionPane.showMessageDialog(frame, "Phone number must contains digits only"); 
			  		}
					else
				 		if(!password1.equals(confirmPassword)){
						JOptionPane.showMessageDialog(frame, "Confirm password must match a password field"); 
				  }
		  		
		  		
		  	
		  	else {
				try {
					String command = "23" + userName+","+email+","+phone+","+ firstName+","+lastName+","+combobox111+","+password1+","+event;
					ConnectionToServer.sendData(this, command);
				}catch (IOException e) {
				e.printStackTrace();
				}
			
				}
	}
    


	@Override
	public void display(Object msg) {
				// TODO Auto-generated method stub

	}

ObservableList<String> list1 = FXCollections.observableArrayList("Librarian","Reader","Manager");
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		combobox1.setItems(list1);
	}

	    public void ViewPassword(ActionEvent event) {
	    	  if (viewpass2.isSelected()){
	    		  Password.setPromptText(Password.getText());
	    		  Password.setText(""); 
	    		  Password.setDisable(true);
	    		  ConfirmPassword.setPromptText(ConfirmPassword.getText());
	    		  ConfirmPassword.setText(""); 
	    		  ConfirmPassword.setDisable(true);

	    	  }
	    	  else {
	    		  Password .setText(Password.getPromptText());
	    		  Password.setPromptText("");
	    		  Password.setDisable(false);
	    		  ConfirmPassword .setText(ConfirmPassword.getPromptText());
	    		  ConfirmPassword.setPromptText("");
	    		  ConfirmPassword.setDisable(false);
	    	  		}
	    }
}
