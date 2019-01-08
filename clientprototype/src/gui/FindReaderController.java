package gui;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import javafx.application.Platform;
import controller.ConnectionToServer;
import controller.CreateObject;
import entity.Reader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ocsf.client.ChatIF;
import javafx.fxml.Initializable;

public class FindReaderController implements ChatIF, Initializable {
	@FXML
	private Button search;
	@FXML
	private Button updatestatus;
	@FXML
	private TextField readerID;
	@FXML
	private Label result;
	JOptionPane frame;
	
	
	
	public void searchByID() {
		String readerid = readerID.getText();
		if (readerid.equals(""))
			JOptionPane.showMessageDialog(frame, "please fill reader id");
		else {
			String command = "02" + readerid;
			try {
				ConnectionToServer.sendData(this, command);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public void UpdateStatusGui(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("GUI_FXML/UpdateStatus.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void display(Object Omessage) {//the answer from the sever
		String message=(String)Omessage;
		if (message.equals("-1")) {
			JOptionPane.showMessageDialog(frame, "The reader Id is not exists");
		} else {
			List<String> data = Arrays.asList(message.split(","));

			try {
				Reader student = new Reader();
				CreateObject.createObject(student, data);
				Platform.runLater(() -> {
					result.setText(student.getStudent_name());
				});
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

}
