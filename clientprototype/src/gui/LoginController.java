package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import entity.Reader;
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
import javafx.stage.Stage;
import ocsf.client.ChatIF;
import javafx.scene.layout.Pane;

/**
 * this class for login process in this class we check the input from the
 * user(empty fields,illegal entered informations) the user must enter his
 * username and password then the server check if the entered values are founded
 * in the database if the entered data founded in the database then the user be
 * logged in and moved to the main page if the entered data not founded in the
 * database then the user see an error massage
 * 
 * @author Fares Bsoul
 */
public class LoginController extends NavigationBar implements Initializable, ChatIF {

	private java.awt.TextField UserName;

	@FXML
	private Button SignIn;

	@FXML
	private Button searchbutton;

	@FXML
	private Button ClearAll;

	@FXML
	private Button ForgetPassword;

	@FXML
	private java.awt.TextField Password;

	@FXML
	private CheckBox viewpass;
	@FXML
	private Pane Connection1;

	@FXML
	private java.awt.TextField portNumber;

	@FXML
	private java.awt.TextField ipAddress;

	JOptionPane frame;

	public static String userType2;

	public static String userStatus2 = "null"; 

	public static String userName2;

	public static String UserInfo2;

	public int flag = 0;

	public int flag1 = 1;

	IServerExtentionManager server;

	ActionEvent event;
	
	public static String message;

	public LoginController(IServerExtentionManager server) {// Constructor added to inject the object server
		setServer(server);

	}

	public LoginController() {
		super();
	}

	public void start(Stage primaryStage) throws IOException {
		System.out.println("t--------------------------------est before crash");
		Parent root = FXMLLoader.load(getClass().getResource("GUI_FXML/Login_Page.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("OBL Prototype");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	/**
	 * this function clear all the fields when the user press on clear all button
	 * 
	 * @param event
	 */
	@FXML
	public void ClearAll(ActionEvent event) {
		Password.setText("");
		UserName.setText("");
	}

	@FXML
	public boolean connectToServer(ActionEvent event) {
		if (portNumber.getText().equals("") || ipAddress.getText().equals("")) {
			JOptionPane.showMessageDialog(frame, "Please fill the fields");
			return false;
		} else {
			int port = Integer.parseInt(portNumber.getText());
			String ip = ipAddress.getText();
			server.initializeServerConnection(port, ip);
			// Connection1.setVisible(false);
		}
		return true;

	}

	/**
	 * this function moved the user to get_password page by clicking on forget
	 * password button
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void moveToForgetPassword(ActionEvent event) throws IOException {
		ReturnScreen = FXMLLoader.load(getClass().getResource("GUI_FXML/Get_Password.fxml"));
		moveTo(event);
	}

	Parent ReturnScreen = null;

	void moveTo(ActionEvent event) throws IOException {
		Scene scene = new Scene(ReturnScreen);
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		primaryStage.hide();
		primaryStage.setScene(scene);
		primaryStage.show();
		System.out.println("worked");
	}

	/**
	 * this function get the data that the user entered check the input data , and
	 * show massages error when the user missing a field empty then we build a
	 * string that include ("case number in the server =22 "+ username
	 * parameter+password parameter) then we send this string to the server then the
	 * server check: 1-if the entered data is correct (the user name and the
	 * password are founded in the DB) 2-check if the login status==1 then the user
	 * can't login(because the user can't logged in in two pc in the same time)
	 * 3-check if the UserStatus==blocked then the user can't logged in to the
	 * system after that the user moved to the main page if all the test succeeded.
	 * 
	 * @param event
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@FXML
	public boolean moveToMainPage(ActionEvent event) throws IOException, InterruptedException {

		if (UserName.getText().equals("")) {
			JOptionPane.showMessageDialog(frame, "You must fill a username field");
			return false;

		} else {
			if (Password.getText().equals("")) {
				JOptionPane.showMessageDialog(frame, "You must fill a Password field");
				return false;
			} else {

				userName2 = UserName.getText();
				try {
					String command = "22" + UserName.getText() + "," + Password.getText();
					server.sendData(this, command);
					flag = 0;
					userStatus2 = "null";
					int count = 0;
					while (flag == 0) {
						if (++count == 7) {

							return true;
						}
						//Thread.sleep(1000);
						if (flag == 2)
							return true;
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
				ReturnScreen = FXMLLoader.load(getClass().getResource("GUI_FXML/Main_Page.fxml"));
				Scene scene = new Scene(ReturnScreen);
				Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				primaryStage.hide();
				primaryStage.setScene(scene);
				primaryStage.show();

			}
		}
		return true;

	}

	/**
	 * this function moved the user to search_book page as visitor by clicking on
	 * Search button
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void moveToSearchScreen(ActionEvent event) throws IOException {
		ReturnScreen = FXMLLoader.load(getClass().getResource("GUI_FXML/Search_Book.fxml"));
		moveTo(event);
	}
	
	
	

	/**
	 * this function get massage from the server if the server send that user not
	 * exist or the account is logged in or the user is blocked then this function
	 * show to the user a error massage else save his information to preview them in
	 * all the system pages .
	 */
	public static String userID2;

	@Override
	public void display(Object msg) {
		message="";
		if (msg.equals("user not exsist")) {
			message="not exsist";
			JOptionPane.showMessageDialog(frame,message);
			flag = 2;
			
			return;
		}

		else if (msg.equals("this account is logged in already")) {
			message="already logged in";
			JOptionPane.showMessageDialog(frame, "Sorry , this account is logged in already");
			flag = 2;
			return;
		} else if (msg.equals("user was blocked")) {
			message="blocked";
			JOptionPane.showMessageDialog(frame, "Sorry , this account was blocked");
			flag = 2;
			return;
		} else {
			String s = (String) msg;
			String[] data = s.split(",", 3);
			System.out.println("status = " + data[0]);
			System.out.println("type = " + data[1]);
			System.out.println("id = " + data[2]);
			userType2 = data[1];
			userID2 = data[2];
			Reader.setStudent_id(userID2); 

			if (data[0].equals("Frozen")) {
				message="Frozen";
				JOptionPane.showMessageDialog(frame, "frozen account , you can search book only");
				userStatus2 = "Frozen";
				UserInfo2 = ("[ " + userName2 + " , " + "Reader" + " ]" + " > Freeze account <");
				System.out.println(userStatus2 + "................." + userType2 + "................." + userID2);
				System.out.println(userStatus2);
				flag = 1;
				return;
			}

			else if (data[0].equals("Active")) {
				userStatus2 = "Active";
				UserInfo2 = ("[ " + userName2 + " , " + userType2 + " ]");
				System.out.println(UserInfo2 + userID2);
				flag = 1;
				message="Active";
			}
		}
	}

	public IServerExtentionManager getServer() {
		return server;
	}

	public void setServer(IServerExtentionManager server) {
		this.server = server;
	}

	public String getUserName() {
		return UserName.getText();
	}

	public String getPassword() {
		return Password.getText();
	}

	public void setUserName(String username) {
		UserName = new java.awt.TextField();
		UserName.setText(username); 
	}

	public void setPassword(String password) {
		Password = new java.awt.TextField();
		Password.setText(password);
	}

	public String getPortNumber() {
		return portNumber.getText();
	}

	public String getIpAddress() {
		return ipAddress.getText();
	}

	public void setPortNumber(String portNumber) {
		this.portNumber = new java.awt.TextField();
		this.portNumber.setText(portNumber);
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = new java.awt.TextField();
		this.ipAddress.setText(ipAddress);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		if (NavigationBar.loginFlag == 1) {
			Connection1.setVisible(false);
		}
		ipAddress.setText("localhost");
		portNumber.setText("5555");
		setServer(new ServerExtentionManager());// when the GUI called initialize the server interface to
												// ServerExtentionManager

	}

}