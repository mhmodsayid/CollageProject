package gui;


import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import javafx.scene.image.*;
import entity.Book;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import ocsf.client.ChatIF;

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
    void loadTheContentFolder(MouseEvent event) {

    }

	@Override
	public void display(String message) {
		
		
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
		ByteArrayInputStream bis = new ByteArrayInputStream(Book.getTheBook().getBookphoto());
	      try {
			BufferedImage bImage2 = ImageIO.read(bis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
	}

}
