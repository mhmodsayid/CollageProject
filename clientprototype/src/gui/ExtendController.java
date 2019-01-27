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
	public static String userStatus;

	
	int statusFlag;
	int userFlag;
	

	/*
	 *this function clears all the fields when clear all button is pressed 
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
	   
	   @FXML
	   public void CheckUserType(ActionEvent event) {

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
	@Override
	public void display(Object obj) {//the answer from the sever
		
		   String message=(String)obj;
		   List<String> data=Arrays.asList(message.split(","));
		   //if the reader ID is correct
		   //if(data.get(3).equals("Librarian")) {
		   if(statusFlag==0) {
			   if(data.get(0).equals("UserIDFound")) {
				   readerName.setText(data.get(2)); 
				   userStatus=data.get(1);
				   readerStatus.setText(userStatus);
				   statusFlag=1;
				  
			   }
			   else {
				   JOptionPane.showMessageDialog(frame, "User not found");
			   }
			   
		   //}
		   }
		   if(statusFlag==2) {

			  
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

					  if(data.get(3).equals("Available")) {
							dateReturn=today.plusDays(7);
							newReturnDate.setText(dateReturn.format(DateTimeFormatter.ofPattern("yyy-MM-dd")));

					   }
					else if(data.get(3).equals("Indemand")) {
						dateReturn=today.plusDays(3);
						newReturnDate.setText(dateReturn.format(DateTimeFormatter.ofPattern("yyy-MM-dd")));
					   }

				   }
			   }
				   
		   		else {
				   JOptionPane.showMessageDialog(frame, "Reader is"+" "+userStatus+", please refer to library");
				   ClearAll(null);
			   }
			 }
		   if(statusFlag==3) {
			   JOptionPane.showMessageDialog(frame, data.get(0));
		   }
		    }
		   


	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		UserInformation.setText(LoginController.UserInfo2);   
		/*userType=User.getUserType();
		   if(userType.equals("Manager")) { 
			   userFlag=1;
		   }
		   else if(userType.equals("Librarian")) {
			   userFlag=2;
		   }
		   else if(userType.equals("Reader")) {
			   userFlag=3;
			   readerID.setEditable(false);
			   readerID.setText(User.getUserID());
			   readerName.setText(User.getFirstName()+" "+User.getLastName());
			   readerStatus.setText(User.getuserStatus());
		   }*/
		
	}

	
}
