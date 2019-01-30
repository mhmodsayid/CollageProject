package gui;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import control.DbContoller;
import control.ServerController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

@SuppressWarnings("restriction")
public class ServerGuiController extends Application implements Initializable {
	@FXML
	private TextField PasswordSQL;

	@FXML
	private TextField NameDB;

	@FXML
	private TextField IpAddress;

	@FXML
	private TextField UserNameSQL;
	@FXML
	private Pane WorkerDetails;
	@FXML
	private Pane successfullyConnected;
	ServerController sc;
	public static void main(String[] args) {

		launch(args);
	}

	@FXML
	void ConnectServer(ActionEvent event)  {
		JOptionPane frame = null;
		int port = 5555; // Port to listen on
		PasswordSQL.setText("password");
		NameDB.setText("collageproject");
		IpAddress.setText("77.138.40.146");
		UserNameSQL.setText("root");
		WorkerDetails.setVisible(false);

		try {
			ServerController.db = new DbContoller(UserNameSQL.getText(), PasswordSQL.getText(), NameDB.getText(),
			IpAddress.getText());
			 sc = new ServerController(port);
			sc.startThreads();
			sc.listen(); // Start listening for connections
			successfullyConnected.setVisible(true);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(frame,e.getMessage());
			e.printStackTrace();
		}
		

	}

	@FXML
	void DisconectButton1(ActionEvent event) throws SQLException {
		System.exit(0);
	}

	@Override
	public void start(Stage primaryStage) throws IOException, InterruptedException {
		Parent root = FXMLLoader.load(getClass().getResource("Server.fxml"));

		Scene scene = new Scene(root);
		primaryStage.setTitle("OBL Prototype");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		successfullyConnected.setVisible(false);
		WorkerDetails.setVisible(true);

		PasswordSQL.setText("password");
		NameDB.setText("collageproject");
		IpAddress.setText("77.138.40.146");
		UserNameSQL.setText("root");

	}

}
