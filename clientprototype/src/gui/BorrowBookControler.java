package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import controller.ConnectionToServer;
import entity.Book;
import entity.Reader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import ocsf.client.ChatIF;

public class BorrowBookControler extends NavigationBar implements Initializable, ChatIF  {


	    @FXML
	    private TextField BookCatalogNumber;

	    @FXML
	    private TextField ReaderID;

	    @FXML
	    private Text SubscriberStatus;

	    @FXML
	    private Text BookStatus;

	    @FXML
	    private Text SubscriberName;

	    @FXML
	    private Text BookName;

	    @FXML
	    private TextField BorrowDate;

	    @FXML
	    private TextField ReturnDate;
	    
         JOptionPane frame;
    
         Book book;

         Reader reader;
    
    
   public void ClearAll(ActionEvent event) {
	   ReaderID.setText(null);
	   BookCatalogNumber.setText(null);
	   SubscriberStatus.setText("______________________");
	   BookName.setText("_________________________");
	   SubscriberName.setText("______________________");
	   BookStatus.setText("_________________________");
	   ReturnDate.setText(null);
	   BorrowDate.setText(null);
   }
   
   public void SearchForReaderID(ActionEvent event) {
	  	reader.setStudent_id(ReaderID.getText());
	   if(reader.getStudent_id()==null ) {
		   JOptionPane.showMessageDialog(frame, "please fill the fields");
	   }
	   else {
			try {
				String command = "07"+reader.getStudent_id();
				 ConnectionToServer.sendData(this,command);
			} catch (IOException e) {
				e.printStackTrace();
			}
	   }
   }
   
   public void SearchForBookID(ActionEvent event) throws Exception {
   	book.setCatalogNumber(BookCatalogNumber.getText());
	   if ( book.getCatalogNumber()==null)
			JOptionPane.showMessageDialog(frame, "please fill the fields");
		else {
			try {
				String command = "08"+book.getCatalogNumber();
				 ConnectionToServer.sendData(this,command);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
   }
   
   public void BorrowConfirm(ActionEvent event) {
	   
   }
   
   @Override
   public void display(Object obj) {
	   String message=(String)obj;
	   List<String> data=Arrays.asList(message.split(","));
	   if(data.get(0).equals("UserIDFound")) {
		   SubscriberStatus.setText(data.get(1));
		   SubscriberName.setText(data.get(2));  
	   }
	   if(data.get(0).equals("BookFound")) {
	   BookName.setText(data.get(1));
	   BookStatus.setText(data.get(2));
	   }
   }
   
   @Override
   public void initialize(URL location, ResourceBundle resources) {
     	book = new Book();
    	reader = new Reader();
   }  
}

