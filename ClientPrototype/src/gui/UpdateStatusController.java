package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import controller.ConnectionToServer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ocsf.client.ChatIF;
import entity.Reader.StatusMemberShip;

public class UpdateStatusController implements Initializable, ChatIF {

	ObservableList<String> list;
	@FXML
	private ComboBox<String> statusmembership;
	@FXML
	private TextField readerName;
	@FXML
	private Button updateStatus;
	@FXML
	private Button find;
	
	JOptionPane frame;

	public void findReader(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("FindReader.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void updateStatus() {
		String readername = readerName.getText();
		String status = statusmembership.getValue();
		String command = "03" + readername + "," + status;
		if (readername.equals("") || status == null)
			JOptionPane.showMessageDialog(frame, "please fill the fields");
		else {
			try {
				ConnectionToServer.sendData(this, command);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void setstatusmembershipComboBox() {
		ArrayList<String> al = new ArrayList<String>();
		for (StatusMemberShip name : StatusMemberShip.values()) {
			al.add(name.name());
		}

		list = FXCollections.observableArrayList(al);
		statusmembership.setItems(list);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setstatusmembershipComboBox();

	}

	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("UpdateStatus.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("OBL Prototype");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	@Override
	public void display(String message) {//the message from the server
		if (message.equals("1"))
			JOptionPane.showMessageDialog(frame, "student updated successfully");
		else
			JOptionPane.showMessageDialog(frame, "failed to update the reader the reader is not exists");

	}

}
