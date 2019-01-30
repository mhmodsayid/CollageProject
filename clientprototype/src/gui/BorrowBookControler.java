package gui;


import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
/**
 * this class is for the borrow process
 * you have to give the reader ID
 * and Book Catalog Number
 * and then check if you can borrow the book if the reader are blocked or frozen
 * and then give the Book Catalog Number check if there is a book and check if the book is in demand or not
 * and give you time for the borrow
 * @author Ammar khutba
 */
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
         @FXML
         private Text UserInformation;
    
    /**
     * this function is to clear all the details that you entered by clicking in clearAll
     * @param event
     * this function receive  in event press key of Clear All
     */
   public void ClearAll(ActionEvent event) {
	   ReaderID.setText(" ");
	   BookCatalogNumber.setText(" ");
	   SubscriberStatus.setText("______________________");
	   BookName.setText("_________________________");
	   SubscriberName.setText("______________________");
	   BookStatus.setText("_________________________");
	   ReturnDate.setText(" ");
	   BorrowDate.setText(" ");
   }
/**
 * this function is for check if the string is number and have 9 digital
 * @param str the string that we want to check
 * @return if the string is number and have 9 digital return true else false
 */
   public boolean isNumeric(String str)  
	{  
	  try  
	  {  
		int i = str.length();
		  if(i!=9) {
			  throw new NumberFormatException();
		  }	
		double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
   /**
    * in this function you but the reader ID 
    * @param ReaderID
    * and after that you press the button 
    * @param event
    * and then send to the server to check all the details of the reader
    * the sending is by command  = 07 and we add the reader ID to the message to check the details
    * if the reader is blocked or frozen or active or if he in the DB 
    */
   public void SearchForReaderID(ActionEvent event) {	  
	  if(ReaderID.getText().equals("") ||isNumeric(ReaderID.getText())==false) {
		   JOptionPane.showMessageDialog(frame, "please fill the fields");
	   }
	   else {
			try {
				reader.setStudent_id(ReaderID.getText());
				String command = "07"+reader.getStudent_id();
				 ConnectionToServer.sendData(this,command);
			} catch (IOException e) {
				e.printStackTrace();
			}
	   }
   }
   /**
    * in this function you but the Book Catalog Number 
    * @param BookCatalogNumber
    * and after that you press the button 
    * @param event
    * and then send to the server to check all the details of the reader
    * the sending is by command = 08 and we add the Book Catalog Number to the message to check the details
    * if the Book are borrowed or not or to check if the book is in demand or not 
    */
   public void SearchForBookID(ActionEvent event) throws Exception {
	   if ( BookCatalogNumber.getText().equals("")||isNumeric(BookCatalogNumber.getText())==false)
			JOptionPane.showMessageDialog(frame, "please fill the fields");
		else {
			try {
				book.setCatalogNumber(BookCatalogNumber.getText());
				String command = "08"+book.getCatalogNumber();
				 ConnectionToServer.sendData(this,command);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
   }
   /**
    * in this function we do all the checking and updating all the details for borrowing
    * @param event
    * this function receive  in event press key of borrow
    * then we check if we but the Catalog Number and the student ID
    * after that we send all the details that we need to the server in case :12
    * and we update all the borrow process in the server
    */
   public void BorrowConfirm(ActionEvent event) {
	   if(book.getCatalogNumber()==null || reader.getStudent_id()==null) {
		   JOptionPane.showMessageDialog(frame, "please fill the fields");
	   }
	   else {
		 try {
				String command = "12"+book.getCatalogNumber()+","+reader.getStudent_id()+","+LoginController.userID2+","+BorrowDate.getText()+","+ReturnDate.getText()+","+"borrowed"+","+BookName.getText()+","+SubscriberStatus.getText();
				 ConnectionToServer.sendData(this,command);
			} catch (IOException e) {
				e.printStackTrace();
			}
	   }
   }
   /**
    * in this function we receive message from the server 
    * @param obj the message we receive in object in the message there is information on:
    * if there is a student with the same id that you send from the client
    * if there  is we well get all the information and updating the gui
    * and we can also get  a message about the information of the book if there is a book with the same  Catalog Number that we send from the client
    * if there is we well get all the info and update time for the borrow process
    */
   @Override
   public void display(Object obj) {
	   String message=(String)obj;
	   List<String> data=Arrays.asList(message.split(","));
	   //if there is a user that have the same ID
	   if(data.get(0).equals("UserIDFound")) {
		   SubscriberStatus.setText(data.get(1));
		   SubscriberName.setText(data.get(2));  
	   }
	   //IF there is a book found in the library
	   if(data.get(0).equals("BookFound")) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Date date = new Date();
	   BookName.setText(data.get(1));
	   BookStatus.setText(data.get(2));
	   BorrowDate.setText(dateFormat.format(date));
	   if(data.get(2).equals("Indemand")) {
		   Calendar c = Calendar.getInstance();
	        c.setTime(date);
	        c.add(Calendar.DAY_OF_MONTH, 3);
	        Date currentDatePlusThree = c.getTime();
		    ReturnDate.setText(dateFormat.format(currentDatePlusThree));
	   }else {
		   Calendar c = Calendar.getInstance();
	        c.setTime(date);
	        c.add(Calendar.DAY_OF_MONTH, 14);
	        Date currentDatePlusTwoWeeks = c.getTime();
		    ReturnDate.setText(dateFormat.format(currentDatePlusTwoWeeks));
	   }
	   //if there is a user or a book 
	 }else if(data.get(0).equals("notFound")) {
		  JOptionPane.showMessageDialog(frame,data.get(1));
		  ReaderID.setText(" ");
		   BookCatalogNumber.setText(" ");
		   SubscriberStatus.setText("______________________");
		   BookName.setText("_________________________");
		   SubscriberName.setText("______________________");
		   BookStatus.setText("_________________________");
		   ReturnDate.setText(" ");
		   BorrowDate.setText(" ");
	   }
   }
   
   @Override
   public void initialize(URL location, ResourceBundle resources) {
     	book = new Book();
    	reader = new Reader();

		UserInformation.setText(LoginController.UserInfo2);
   }  
}

