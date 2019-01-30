package controller;

import gui.LoginController;
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
			host = "localhost1";
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
