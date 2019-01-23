package controller;

import control.CheackingBorrowDate;
import gui.LoginController;
import gui.MainPage;
import gui.UpdateStatusController;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientConsole extends Application {

	public static void main(String[] args) throws Exception {
	String host = "";
		//test
		//add from pc
		//ad ded
		//added 5
	//fdgdfg
		try {
			host = args[0];
		} catch (ArrayIndexOutOfBoundsException e) {
			host = "localhost";
		}
		ConnectionToServer.initializeServerConnection(5555, host);
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		LoginController aframe= new LoginController();
		//UpdateStatusController aFrame = new UpdateStatusController(); // create StudentFrame and start the gui
		aframe.start(arg0);
		
	}
}
