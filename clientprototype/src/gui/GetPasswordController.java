package gui;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import controller.ConnectionToServer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ocsf.client.ChatIF;
/**
 * this class for get password process
 * the user must enter his email
 * then the server check if the entered values are founded in the database
 * if the entered data founded in the database then the user get his forgotten password to his email
 * if the entered data not founded in the database then the user see an error massage
 * @author Fares Bsoul
 */
public class GetPasswordController  extends NavigationBar implements Initializable, ChatIF{
	
    JOptionPane frame;

    @FXML
    private Button search;

    @FXML
    private Button clear;

    @FXML
    private TextField Email;

    @FXML
    private ButtonBar VIsitMenu;
    

  
	/**
	 * this function get the data that the user entered (the email)
	 * check the input data , and show massages error when the user missing a field empty
	 * then we build a string that include ("case number in the server =25 "+ email parameter)
	 * then we send this string to the server
	 * then the server check:
	 * 1-if the entered data is correct (the email is founded in the DB) the user get the password to his email
	 * @param event
	 * @throws IOException
	 */
    @FXML
    void searchByEmail(ActionEvent event) throws IOException {
    	if(Email.getText().equals("")||Email.getText().equals(null)) 
		    JOptionPane.showMessageDialog(frame,"You must fill in Your email fields");	    			
    	else
    		try {
    		String command ="25"+Email.getText();
    		ConnectionToServer.sendData(this,command);
    		}catch (IOException e) {
    		e.printStackTrace();
    		}
    }

	/**
	 * this function get massage from the server
	 * if the server send that the email entered is wrong then this function show to the user a error massage
	 * else this function show to the user a massage ("the password sent to your email that entered").
	 */
	@Override
	public void display(Object msg) {
		// TODO Auto-generated method stub
		if(msg.equals("Wrong email"))
			JOptionPane.showMessageDialog(frame, "Sorry , this Email is not exsist already !!");
		else 
			if(!msg.equals("Wrong email")){	
			JOptionPane.showMessageDialog(frame,"the password sent to your email that entered");
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}
}