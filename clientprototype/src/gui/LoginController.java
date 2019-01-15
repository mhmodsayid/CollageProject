package gui;


import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import controller.ConnectionToServer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ocsf.client.ChatIF;

public class LoginController  implements Initializable, ChatIF{
	 
    @FXML
    private TextField UserName;

    @FXML
    private PasswordField Password;

    @FXML
    private Button SignIn;

    @FXML
    private Button ForgetPassword;

    @FXML
    private Button searchbutton;
    
    @FXML
    private Button ClearAll;

    JOptionPane frame;
    private String userName = null;
    String userNameLabel = null;
    private String password = null;
    private int Flag5 = 0;
    
    public void AddText() {
    	userName = UserName.getText();
    	password = Password.getText();
    	userNameLabel = UserName.getText();
   	
   	System.out.println(userName +" "+ password);
   } 
    
    public void ClearAll(ActionEvent event) {
    	UserName.setText(null);
    	Password.setText(null);
    }     
    
	Parent ReturnScreen = null;

    @FXML
    void moveToSearchScreen(ActionEvent event) throws Exception {
    	ReturnScreen = FXMLLoader.load(getClass().getResource("GUI_FXML/Add_Book.fxml"));
    	moveTo(event);

    }
    
    @FXML
    void moveToForgetPassword(ActionEvent event) throws Exception {
    	ReturnScreen = FXMLLoader.load(getClass().getResource("GUI_FXML/Get_Password.fxml"));
    	moveTo(event);

    }
    
	public void moveTo(ActionEvent event) {		
		Scene scene = new Scene(ReturnScreen);
		Stage primaryStage = (Stage)((Node) event.getSource()).getScene().getWindow();
		primaryStage.hide();
		primaryStage.setScene(scene);
		primaryStage.show();
		System.out.println("worked454545");
	}

	  @FXML
		void moveToMainPage(ActionEvent event){	
		  Object message = null;
		  	if(userName==null && password!=null) 
				JOptionPane.showMessageDialog(frame, "Please enter a username"); 
		  		else 
		  			if(userName!=null && password==null) 
		  				JOptionPane.showMessageDialog(frame, "Please enter a password");		
		  				else 
		  					if(userName==null && password==null) 
		  						JOptionPane.showMessageDialog(frame, "Please enter a username and password");
		  	
		  	
			else {
				try {
					String command = "22" + userName+","+password;
					ConnectionToServer.sendData(this, command);
				}catch (IOException e) {
				e.printStackTrace();
				}
				if(Flag5==1) {
				  try {		
						ReturnScreen = FXMLLoader.load(getClass().getResource("GUI_FXML/Main_Page.fxml"));

						moveTo(event);
			    		} 
			    	catch (IOException e) {
			    		// TODO Auto-generated catch block
			    		e.printStackTrace();
			    	}	
				}
			}
	  	}
	    
	    
	@Override
	public void display(Object message) {	
		// TODO Auto-generated method stub
		if(message.equals("Login Done")) {
			System.out.println("Login Done");
			Flag5=1;
	    }
		else 
			if(message.equals("Error Login")) {
				System.out.println("Error Login");	
				JOptionPane.showMessageDialog(frame, "User name or Password incorrect");
			}
		}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

    public void start(Stage primaryStage) throws IOException {
		System.out.println("test before crash");
		Parent root = FXMLLoader.load(getClass().getResource("GUI_FXML/Login_Page.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("OBL Prototype");
		primaryStage.setScene(scene);
		primaryStage.show();

	}
    @FXML
    private CheckBox viewpass;
    public void ViewPassword(ActionEvent event) {
    	  if (viewpass.isSelected()){
    		  Password.setPromptText(Password.getText());
    		  Password.setText(""); 
    		  Password.setDisable(true);

    	  }
    	  else {
    		  Password .setText(Password.getPromptText());
    		  Password.setPromptText("");
    		  Password.setDisable(false);
    	  		}
    }
}