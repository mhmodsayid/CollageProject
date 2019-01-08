package gui;


import javafx.event.ActionEvent;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainPage extends NavigationBar {

    
   

    
    
    public void start(Stage primaryStage) throws IOException {
		System.out.println("t--------------------------------est before crash");
		Parent root = FXMLLoader.load(getClass().getResource("GUI_FXML/Main_Page.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("OBL Prototype");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

}
