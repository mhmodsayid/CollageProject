package gui;


import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import controller.ConnectionToServer;
import entity.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import ocsf.client.ChatIF;

public class ReturnBookControler extends NavigationBar implements Initializable, ChatIF {

	    @FXML
	    private TextField BookID;

	    @FXML
	    private TextField BorrowDate;

	    @FXML
	    private TextField ReturnDate;

	    @FXML
	    private TextField ReturnedOn;
	    
        JOptionPane frame;
    
        Book book;
    
    
    public void ClearAll(ActionEvent event) {
    	BookID.setText(null);
    	BorrowDate.setText(null);
    	ReturnDate.setText(null);
    	ReturnedOn.setText(null);
    }
    
    public void SearchButton(ActionEvent event) throws Exception {
		book.setCatalogNumber(BookID.getText());
    	if (book.getCatalogNumber()==null)
			JOptionPane.showMessageDialog(frame, "please fill the fields");
		else {
			try {
				String command = "09"+book.getCatalogNumber();
				 ConnectionToServer.sendData(this,command);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
    
    public void ReturnBookButton(ActionEvent event) {
    	if (book.getCatalogNumber()==null)
			JOptionPane.showMessageDialog(frame, "please fill the fields");
		else {
			try {
				String command = "10"+book.getCatalogNumber();
				 ConnectionToServer.sendData(this,command);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
    
   
	@Override
	public void display(Object obj) {
		 String message=(String)obj;
		// TODO Auto-generated method stub
		   List<String> data=Arrays.asList(message.split(","));
		   if(data.get(0).equals("BookFound")) {
			   BorrowDate.setText(data.get(2));
			   ReturnDate.setText(data.get(3));
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
			   ReturnedOn.setText(dateFormat.format(date));
		   }
		   if(data.get(0).equals("done")) {
		    	BookID.setText(null);
		    	BorrowDate.setText(null);
		    	ReturnDate.setText(null);
		    	ReturnedOn.setText(null);		   }
		/*if(message.equals("ReturningTheBookisSuccess")) 
			JOptionPane.showMessageDialog(frame, "the book has returned successfuly");
			else
				JOptionPane.showMessageDialog(frame, "faild to Returning the book");
		*/
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		book=new Book();
	}
	
}