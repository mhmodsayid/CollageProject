package gui;


import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
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
import javafx.fxml.Initializable;
import ocsf.client.ChatIF;
import javafx.event.ActionEvent;
public class BookOrder extends NavigationBar implements Initializable, ChatIF  {
	JOptionPane frame = null;
    @FXML
    private ImageView bookPic;
    @FXML
    private Text UserInformation;
    @FXML
    private TextArea bookName;

    @FXML
    private TextField bookEdition;

    @FXML
    private TextField bookCatagory;

    @FXML
    private TextField publisher;

    @FXML
    private TextField numberOfCopyes;

    @FXML
    private TextArea bookDiscription;
   

    @FXML
    private ButtonBar WorkerMenu;
    @FXML
    private ButtonBar ReaderMenu;

    @FXML
    private ButtonBar visitMenu;

    @FXML
    private Button orderbtn;

    
    @FXML
    void OrderTheBook(ActionEvent event) throws SQLException {
    	
    	
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
    void open_Contant(ActionEvent event) throws IOException {
  
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
		bookCatagory.setText(Book.getTheBook().getBookCatagory());
		bookDiscription.setText(Book.getTheBook().getBookDescription());
		numberOfCopyes.setText(""+Book.getTheBook().getNumberOFCopies());
			Image img = new Image(new ByteArrayInputStream(Book.getTheBook().getBookphoto()));
			bookPic.setImage(img);
			

			if(LoginController.UserInfo2==null){
				UserInformation.setText("[Temprary Reader]");
				ReaderMenu.setVisible(false);
				WorkerMenu.setVisible(false);
				orderbtn.setVisible(false);
				
				}
			else
			if(LoginController.userType2.equals("Reader")) {
					UserInformation.setText(LoginController.UserInfo2);

			WorkerMenu.setVisible(false);
			ReaderMenu.setVisible(true);
			visitMenu.setVisible(false);

			}
			else 
				if(LoginController.userType2.equals("Librarian")||LoginController.userType2.equals("Manager")) {
					UserInformation.setText(LoginController.UserInfo2);

				WorkerMenu.setVisible(true);
				ReaderMenu.setVisible(false);
				visitMenu.setVisible(false);
			}
				
				
		  }	
			

	     
	

}
