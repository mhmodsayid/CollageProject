package gui;

import java.io.IOException;
import javafx.scene.control.ProgressIndicator;
import java.net.URL;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import controller.ConnectionToServer;
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
import javafx.scene.Group;

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

	@FXML
	private TextField UserName;

	@FXML
	private Button SignIn;

	@FXML
	private Button searchbutton;

	@FXML
	private Button ClearAll;

	@FXML
	private Button ForgetPassword;

	@FXML
	private PasswordField Password;

	@FXML
	private CheckBox viewpass;

	JOptionPane frame;

	public static String userType2;

	public static String userName2;

	public static String UserInfo2;
	ProgressIndicator progressIndicator;
	public int flag;
	ActionEvent event;
	Parent root;

	public void start(Stage primaryStage) throws IOException, InterruptedException {
		System.out.println("t--------------------------------est before crash");
		root = FXMLLoader.load(getClass().getResource("GUI_FXML/Login_Page.fxml"));

		Scene scene = new Scene(root);
		primaryStage.setTitle("OBL Prototype");
		primaryStage.setScene(scene);
		primaryStage.show();

		
		
		
		Group g = new Group();

		ProgressIndicator p1 = new ProgressIndicator();

		g.getChildren().addAll(root, p1);

		scene.setRoot(g);
		primaryStage.show();
		/*
		 * ProgressIndicator progressIndicator = new ProgressIndicator(); final HBox vb
		 * = new HBox(); vb.setSpacing(5); vb.getChildren().addAll(progressIndicator);
		 * scene.setRoot(vb); primaryStage.show();
		 */

		/*
		 * ProgressIndicator progressIndicator = new ProgressIndicator();
		 * 
		 * FlowPane root1 = new FlowPane(); root1.setPadding(new Insets(10));
		 * root1.setHgap(10); root1.getChildren().addAll(progressIndicator);
		 * 
		 * Scene scene1 = new Scene(root1, 400, 300);
		 * 
		 * primaryStage.
		 * setTitle("JavaFX ProgressBar & ProgressIndicator (o7planning.org)");
		 * 
		 * primaryStage.setScene(scene1); primaryStage.show();
		 */

	}

	/**
	 * this function clear all the fields when the user press on clear all button
	 * 
	 * @param event
	 */
	@FXML
	void ClearAll(ActionEvent event) {
		Password.setText(null);
		UserName.setText(null);
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

	/*
	 * void moveTo(ActionEvent event) throws IOException { Scene scene = new
	 * Scene(ReturnScreen); Stage primaryStage = (Stage) ((Node)
	 * event.getSource()).getScene().getWindow(); primaryStage.hide();
	 * primaryStage.setScene(scene); primaryStage.show();
	 * System.out.println("worked"); }
	 */

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
	void moveToMainPage(ActionEvent event) throws IOException, InterruptedException {
		

		if (UserName.getText().equals("") && Password.getText().equals(""))
			JOptionPane.showMessageDialog(frame, "You must fill in all of the fields");
		else {
			if (UserName.getText().equals(""))
				JOptionPane.showMessageDialog(frame, "You must fill a username field");
			else {
				if (Password.getText().equals(""))
					JOptionPane.showMessageDialog(frame, "You must fill a Password field");
				else {
					userName2 = UserName.getText();
					try {
						String command = "22" + UserName.getText() + "," + Password.getText();
						ConnectionToServer.sendData(this, command);
						flag = 0;
						while (flag == 0)
							Thread.sleep(10);
						if(flag==2)
							return;
						
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
		}
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
		UserInfo2=null;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI_FXML/Search_Book.fxml"));
		Parent searchScreen =loader.load();
		Scene scene = new Scene(searchScreen);
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		primaryStage.hide();
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * this function get massage from the server if the server send that user not
	 * exist or the account is logged in or the user is blocked then this function
	 * show to the user a error massage else save his information to preview them in
	 * all the system pages .
	 */
	@Override
	public void display(Object msg) {
		if (msg.equals("user not exsist")) {
			JOptionPane.showMessageDialog(frame, "User Not Exist");
			flag = 2;
			return;
		}
		if (msg.equals("this account is logged in already")) {
			JOptionPane.showMessageDialog(frame, "Sorry , this account is logged in already");
			flag = 2;
			return;
		}

		userType2 = (String) msg;
		UserInfo2 = ("[ " + userName2 + " , " + userType2 + " ]");
		System.out.println(UserInfo2);
		Reader.setStudent_id(userName2);
		flag = 1;

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}

	/**
	 * this function can show the user the password the he entered (change from
	 * password type to text type) and by click one more time turn the text to
	 * password(change from text type to password type)
	 */
	public void ViewPassword(ActionEvent event) {
		if (viewpass.isSelected()) {
			Password.setPromptText(Password.getText());
			Password.setText("");
			Password.setDisable(true);
		} else {
			Password.setText(Password.getPromptText());
			Password.setPromptText("");
			Password.setDisable(false);
		}
	}
}