package gui;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import javax.swing.text.DateFormatter;

import controller.ConnectionToServer;
import entity.Book;
import entity.Reader;
import entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import ocsf.client.ChatIF;

public class ExtendController extends NavigationBar implements Initializable, ChatIF {

	Book book;
	@FXML
	private TextField readerID;
	@FXML
	private Text readerName;
	@FXML
	private Text readerStatus;
	@FXML
	private Text bookName;
	@FXML
	private TextField chooseBook;
	@FXML
	private TextField borrowDate;
	@FXML
	private TextField returnDate;
	@FXML
	private TextField newReturnDate;
	@FXML
	private Button clearAll;
	@FXML
	private Button extendDate;
	@FXML
	private Button search1;
	@FXML
	private Button search2;
	@FXML
	private Label result;
    @FXML
    private Text UserInformation;
    
	JOptionPane frame;
	Reader reader;
	public static String userType;
	public static String userType1;
	public static String userStatus;

    @FXML
    private ButtonBar WorkerMenu;
    @FXML
    private ButtonBar ReaderMenu;
	int statusFlag;
	int userFlag;
	

	/**
	 *clear all fields in the extend page when the 
	 *clear all button is pressed
	 * @author bayan
	 *
	 */
	@FXML
	void ClearAll(ActionEvent event) {
		readerID.setText("");
		readerName.setText("");
		borrowDate.setText("");
		returnDate.setText("");
		newReturnDate.setText("");
		readerStatus.setText("");
		chooseBook.setText("");
		bookName.setText("");
		statusFlag=0;
		
	}
	
	/**
	 * checks the input id numbers and checks that it is 
	 * 0 digits long and contains only numbers
	 * @param str
	 * @return
	 * @author bayan
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
	    * when the search button is pressed near the reader id filed
	    * the id is sent to the server to case 27.
	    * if the user id is not found a message is presented to the user.
	    * @param event
	    * @author bayan
	    */
	@FXML
	   public void SearchForReaderID(ActionEvent event) {	  
		  if(readerID.getText().equals("") ||isNumeric(readerID.getText())==false) {
			   JOptionPane.showMessageDialog(frame, "please fill reader ID");
		   }
		   else {
		try {
			reader.setStudent_id(readerID.getText());
			String command = "27"+reader.getStudent_id();
			 ConnectionToServer.sendData(this,command);
			
		} catch (IOException e) {
			e.printStackTrace();
				}
		   }

		  
	  }
	
	/**
	 * after the user press search button near the book catalog number
	 * if the field is empty a message is shown.
	 * send the book catalog number the case 28 in the server
	 * which checks if the book is borrowed by the user.
	 * @param event
	 * @author bayan
	 */
	@FXML
	public void ShowBorrowedBooks(ActionEvent event) {
		  try {
			  if(statusFlag==1||statusFlag==2) {
				  statusFlag=2;
				  if(chooseBook.getText().equals("")) {
					  JOptionPane.showMessageDialog(frame, "please fill book catalog number");
				  }
				  String command = "28"+chooseBook.getText();
				  ConnectionToServer.sendData(this,command);
				  
			  }
			  else {
				  JOptionPane.showMessageDialog(frame, "please fill reader ID");
			  }
		  } catch(IOException e) {
			  e.printStackTrace();
		  }
	}
	
	/**
	 * takes the new return date and sends it to case 29 in the server
	 * which will update the return date for the borrowed book in DB
	 * @param event
	 * @author bayan
	 */
	@FXML
	public void SetNewReturnDate(ActionEvent event) {
		
		try {
			if(newReturnDate.getText().equals("")) {
				JOptionPane.showMessageDialog(frame, "Enter User and book details");
			}
			else {
				statusFlag=3;
			String command="29"+newReturnDate.getText()+","+chooseBook.getText();
			ConnectionToServer.sendData(this, command);
			}
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * the answer from the server is handeled here according 
	 * to the statusFlag which is determined in the methods accordingly
	 * @author bayan
	 */
	@Override
	public void display(Object obj) {//the answer from the sever
		
		   String message=(String)obj;
		   List<String> data=Arrays.asList(message.split(","));
		   if(statusFlag==0) {//user id serach button
			   if(data.get(0).equals("UserIDFound")) {
				   readerName.setText(data.get(2)); 
				   userStatus=data.get(1);
				   readerStatus.setText(userStatus);
				   userType1=data.get(3);
				   if(userType1.equals("Librarian")||userType1.equals("Manager")) {
					   JOptionPane.showMessageDialog(frame, "A "+userType1+" can't borrow books!");
					   ClearAll(null);
				   }
				   else {
				   statusFlag=1;
				   }
				  
			   }
			   else {
				   JOptionPane.showMessageDialog(frame, "User not found");
			   }
			   
		   }
		   if(statusFlag==2) {//book catalog number search button

			  
			   if(data.get(0).equals("BookNotBorrowed")||data.get(0).equals("")) {
				   JOptionPane.showMessageDialog(frame, "Book not borrowed");
					borrowDate.setText("");
					returnDate.setText("");
					newReturnDate.setText("");
					chooseBook.setText("");
					bookName.setText("");
			   }
				if(data.get(0).equals("bookOrdered")) {
					JOptionPane.showMessageDialog(frame, "Book is Ordered, can't extend borrow");
					borrowDate.setText("");
					returnDate.setText("");
					newReturnDate.setText("");
					chooseBook.setText("");
					bookName.setText("");
				   }
			   else if(userStatus.equals("Active")) {
				   bookName.setText(data.get(0));
				   borrowDate.setText(data.get(1));
				   returnDate.setText(data.get(2));
				   LocalDate today=LocalDate.now();
				   LocalDate dateBorrow =LocalDate.parse(data.get(1));
				   LocalDate dateReturn =LocalDate.parse(data.get(2));
				   if(today.until(dateReturn,ChronoUnit.DAYS) > 7) {
					   JOptionPane.showMessageDialog(frame, "More than 7 days to return date, can't extend borrow");
				   }
				   else {
					   System.out.println(today.until(dateReturn,ChronoUnit.DAYS));
					   System.out.println(data.get(4));

					  if(data.get(4).equals("notOrdered")) {
							dateReturn=dateReturn.plusDays(7);
							newReturnDate.setText(dateReturn.format(DateTimeFormatter.ofPattern("yyy-MM-dd")));
							System.out.println("return Date");

					   }
					else if(data.get(3).equals("Indemand")) {
						 JOptionPane.showMessageDialog(frame, "Book is in demand, can't extend borrow");
					   }

				   }
			   }
				   
		   		else {
				   JOptionPane.showMessageDialog(frame, "Reader is"+" "+userStatus+", please refer to library");
				   ClearAll(null);
			   }
			 }
		   if(statusFlag==3) {//return date update confirmation
			   JOptionPane.showMessageDialog(frame, data.get(0));
		   }
		    }
		   


	

	/**
	 * initialize the GUI page at startup
	 * @author bayan
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		UserInformation.setText(LoginController.UserInfo2);   
		   if(LoginController.userType2.equals("Reader")) { 
			   readerID.setText(LoginController.userID2);
			   readerName.setText(LoginController.userName2);
			   readerStatus.setText(LoginController.userStatus2);
			   readerID.setEditable(false);
			   newReturnDate.setEditable(false);
		   }
		   else  {
			   newReturnDate.setEditable(true);  
		   }
		   if(LoginController.userType2.equals("Librarian")||LoginController.userType2.equals("Manager")) {
				WorkerMenu.setVisible(true);
				ReaderMenu.setVisible(false);
				}

			if(LoginController.userType2.equals("Reader")) {
				WorkerMenu.setVisible(false);
				ReaderMenu.setVisible(true);
			}
	}
}
