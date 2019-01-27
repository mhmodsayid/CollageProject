package gui;


import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ocsf.client.ChatIF;

public class MainPage extends NavigationBar implements Initializable, ChatIF  {

    @FXML
    private Text UserInformation;
    @FXML
    private ButtonBar WorkerMenu;
    @FXML
    private ButtonBar ReaderMenu;

	@Override
	public void display(Object msg) {
		// TODO Auto-generated method stub	


	}
	/*public void update() throws IOException, InterruptedException {
		UserInformation.setText(LoginController.UserInfo2);
		if(LoginController.userType2.equals("Reader")) {
			WorkerMenu.setVisible(false);
			ReaderMenu.setVisible(true);
		}
		else 
			if(LoginController.userType2.equals("Librarian")||LoginController.userType2.equals("Manager")) {
			WorkerMenu.setVisible(true);
			ReaderMenu.setVisible(false);
		}

	}*/

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		UserInformation.setText(LoginController.UserInfo2);
		if(LoginController.UserInfo2==null)
		{
			UserInformation.setText("[Temprary Reader]");
		ReaderMenu.setVisible(false);
		WorkerMenu.setVisible(false);
		}
		else {
		UserInformation.setText(LoginController.UserInfo2);
		if(LoginController.userType2.equals("Reader")) {
		WorkerMenu.setVisible(false);
		ReaderMenu.setVisible(true);
		}
		else 
			if(LoginController.userType2.equals("Librarian")||LoginController.userType2.equals("Manager")) {
			WorkerMenu.setVisible(true);
			ReaderMenu.setVisible(false);
		}
	  }

	}

}
