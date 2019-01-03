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

public class MainPage {

    @FXML
    private Button searchbutton;

    @FXML
    private Button addbutton;

    @FXML
    private Button returnbutton;

    @FXML
    private Button borrowbutton;

    @FXML
    private Button registerbutton;

    @FXML
    private Button orderbutton;

    @FXML
    private Button extendbutton;

    @FXML
    private Button readercardbutton;

    @FXML
    private Button logoutbutton;

    @FXML
    private Text readerinformation;

    @FXML
    void moveToAddScreen(ActionEvent event) {

    }

    @FXML
    void moveToBorrowScreen(ActionEvent event) {

    }

    @FXML
    void moveToExtendScreen(ActionEvent event) {

    }



    @FXML
    void moveToLogInScreen(ActionEvent event) {

    }

    @FXML
    void moveToOrderScreen(ActionEvent event) {

    }

    @FXML
    void moveToReaderCardScreen(ActionEvent event) {

    }

    @FXML
    void moveToRegisterScreen(ActionEvent event) {

    }

    @FXML
    void moveToReturnScreen(ActionEvent event) {

    }

    @FXML
    void moveToSearchScreen(ActionEvent event) throws IOException {
    
    }
    
    public void start(Stage primaryStage) throws IOException {
		System.out.println("t--------------------------------est before crash");
		Parent root = FXMLLoader.load(getClass().getResource("GUI_FXML/Main_Page.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("OBL Prototype");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

}
