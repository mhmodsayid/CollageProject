package gui;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import controller.ConnectionToServer;
import javafx.scene.image.Image;
import entity.Book;
import entity.OrderBook;
import entity.Reader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import ocsf.client.ChatIF;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
public class BookOrder implements Initializable, ChatIF {
	JOptionPane frame = null;
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
    void OrderTheBook(ActionEvent event) throws SQLException {
    	Reader.setStudent_id("testid");
    	
    	java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
    	OrderBook order = new OrderBook();
    	order.setBookName(Book.getTheBook().getBookName());
    	order.setBookId(Book.getTheBook().getCatalogNumber());
    	order.setOrderDate(date.toString());
    	order.setReaderId(Reader.getStudent_id());
    	order.setOrderStatus("Waiting for Book");
    	try {
    	ObjectOutput out = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		out = new ObjectOutputStream(bos);   
		  out.writeObject(order);
		  out.flush();
		  byte[] objbyte = bos.toByteArray();
		 ConnectionToServer.sendData(this, objbyte);
	} catch (Exception e) {
		System.out.println(e.getMessage());
		JOptionPane.showMessageDialog(frame,e.getMessage());
	}
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
		JOptionPane.showMessageDialog(frame,(String)message);
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		bookName.setText(Book.getTheBook().getBookName());
		publisher.setText(Book.getTheBook().getPublisherName());
		bookEdition.setText(Book.getTheBook().getBookEdite()+"");
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
