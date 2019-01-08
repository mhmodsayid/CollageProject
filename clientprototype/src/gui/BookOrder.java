package gui;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import javafx.scene.image.Image;
import entity.Book;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import ocsf.client.ChatIF;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
public class BookOrder implements Initializable, ChatIF {

    @FXML
    private ImageView bookPic;
    @FXML
    private Text bookName;

    @FXML
    private Text bookCatagory;

    @FXML
    private Text publisher;

    @FXML
    private Text catalogNumber;

    @FXML
    private Text bookEdition;

    @FXML
    private Text bookDiscription;

    @FXML
    private Text numberOfCopyes;

    @FXML
    private Text shelfNumber;

    @FXML
    private Text dateOfPurchased;

    @FXML
    private Text dateOfPrint;

    @FXML
    void OrderTheBook(ActionEvent event) {

    }
    @FXML
    void loadTheContentFolder(MouseEvent event) throws IOException {
    	System.out.println("hoo");
    	
    	Path path = Paths.get("temp.pdf");
    	Files.write(path, Book.getTheBook().getContentfile());
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " +path);
        
    
    	
    }
    
    
	


	@Override
	public void display(Object message) {
		
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		bookName.setText(Book.getTheBook().getBookName());
		publisher.setText(Book.getTheBook().getPublisherName());
		bookEdition.setText(Book.getTheBook().getBookEdite());
		catalogNumber.setText(Book.getTheBook().getCatalogNumber());
		bookCatagory.setText(Book.getTheBook().getBookCatagory());
		bookDiscription.setText(Book.getTheBook().getBookDescription());
		numberOfCopyes.setText(""+Book.getTheBook().getNumberOFCopies());
		shelfNumber.setText(Book.getTheBook().getPositionOnTheShelf());
		dateOfPurchased.setText(Book.getTheBook().getDatePurchased());
		dateOfPrint.setText(Book.getTheBook().getDateOfPrint());
			Image img = new Image(new ByteArrayInputStream(Book.getTheBook().getBookphoto()));
			bookPic.setImage(img);

	     
	}

}
