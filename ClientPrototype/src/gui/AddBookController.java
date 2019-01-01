package gui;

import controller.ConnectionToServer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AddBookController {

    @FXML
    private DatePicker datePurchased;

    @FXML
    private TextField bookName;

    @FXML
    private TextField publisherName;

    @FXML
    private Button addBookButton;

    @FXML
    private TextField bookEdite;

    @FXML
    private TextArea bookDescription;

    @FXML
    private TextField CatalogNumber;

    @FXML
    private TextField NumberOFCopies;

    @FXML
    private TextField PositionOnTheShelf;

    @FXML
    private DatePicker dateOfPrint;

    @FXML
    private Button clearAllButton;

    @FXML
    private TextField contentTable;

    @FXML
    private Button contentUploadButton;

    @FXML
    private Button bookPhotoButton;

    @FXML
    private TextField bookPhoto;

    @FXML
    private TextField bookCatagory;

    @FXML
    void addBook(ActionEvent event) {
    	
    	
    	ConnectionToServer.sendData(this, command);
    }

}

