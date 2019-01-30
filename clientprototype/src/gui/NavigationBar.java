package gui;

import java.io.IOException;

import controller.ConnectionToServer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ocsf.client.ChatIF;

public class NavigationBar {
	Parent ReturnScreen = null;
	 void moveTo(ActionEvent event) throws IOException {		
    	Scene scene = new Scene(ReturnScreen);
    	Stage primaryStage = (Stage)((Node) event.getSource()).getScene().getWindow();
    	primaryStage.hide();
		primaryStage.setScene(scene);
		primaryStage.show();
		System.out.println("worked");
	}
	
	 @FXML
	    void moveToBorrowScreen(ActionEvent event) throws IOException {
		 ReturnScreen = FXMLLoader.load(getClass().getResource("GUI_FXML/Borrow_Book.fxml"));
		 moveTo(event);
	 	}
	 
	 @FXML
	    void moveToHome(ActionEvent event) throws IOException {
		 ReturnScreen = FXMLLoader.load(getClass().getResource("GUI_FXML/Main_Page.fxml"));
		 moveTo(event);
	 	}
	 
	 @FXML
	    void moveToAddScreen(ActionEvent event) throws IOException {
	    	ReturnScreen = FXMLLoader.load(getClass().getResource("GUI_FXML/Add_Book.fxml"));
	    	moveTo(event);
	    }

	    @FXML
	    void moveToExtendScreen(ActionEvent event) throws IOException {
	    	ReturnScreen = FXMLLoader.load(getClass().getResource("GUI_FXML/Extend_Time.fxml"));
	    	moveTo(event);
	    }
	    @FXML
	    void moveToLogInScreen(ActionEvent event) throws IOException {
	    	try {
	    		String command ="26"+LoginController.userName2;
	    		ConnectionToServer.sendData((ChatIF) this,command);
	    		}catch (IOException e) {
	    		e.printStackTrace();
	    		}
	    	LoginController.userName2="null";
	    	LoginController.userType2="null";
	    	LoginController.UserInfo2="null";
	    	ReturnScreen = FXMLLoader.load(getClass().getResource("GUI_FXML/Login_Page.fxml"));
	    	moveTo(event);
	    }

	    @FXML
	    void moveToOrderScreen(ActionEvent event) throws IOException {
	    	ReturnScreen = FXMLLoader.load(getClass().getResource("GUI_FXML/Add_Book.fxml"));
	    	moveTo(event);
	    }

	    @FXML
	    void moveToReaderCardScreen(ActionEvent event) throws IOException {
	    	ReturnScreen = FXMLLoader.load(getClass().getResource("GUI_FXML/Reader_Card1.fxml"));
	    	moveTo(event);
	    }

	    @FXML
	    void moveToRegisterScreen(ActionEvent event) throws IOException {
	    	ReturnScreen = FXMLLoader.load(getClass().getResource("GUI_FXML/Register_Page.fxml"));
	    	moveTo(event);
	    }

	    @FXML
	    void moveToReturnScreen(ActionEvent event) throws IOException {
	    	ReturnScreen = FXMLLoader.load(getClass().getResource("GUI_FXML/Return_Book.fxml"));
	    	moveTo(event);
	    }
	    @FXML
	    void moveToSearchScreen(ActionEvent event) throws IOException {
	    	ReturnScreen = FXMLLoader.load(getClass().getResource("GUI_FXML/Search_Book.fxml"));
	    	moveTo(event);
	    }
}
