package controller;

import gui.UpdateStatusController;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientConsole extends Application {

	public static void main(String[] args) throws Exception {
	String host = "";
		//test
		//test3
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
		UpdateStatusController aFrame = new UpdateStatusController(); // create StudentFrame and start the gui
		aFrame.start(arg0);
	}
}
