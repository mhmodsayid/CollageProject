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
import javafx.scene.text.Text;
import ocsf.client.ChatIF;
/**
 * in this class the gui takes all the details that we put in the page and return the book
 * then we send to the server to update the return process
 * @author Ammar Khutb
 */
public class ReturnBookControler extends NavigationBar implements Initializable, ChatIF {

	    @FXML
	    private TextField BookID;

	    @FXML
	    private TextField BorrowDate;

	    @FXML
	    private TextField ReturnDate;

	    @FXML
	    private Text UserInformation;
	    
	    @FXML
	    private TextField ReturnedOn;
	    
        JOptionPane frame;
    
        Book book;
    
        private String ReaderID=null;
        /**
         * this function is to clear all the details that you entered by clicking in clearAll button
         * @param event
         */
    public void ClearAll(ActionEvent event) {
    	BookID.setText("");
    	BorrowDate.setText("");
    	ReturnDate.setText("");
    	ReturnedOn.setText("");
    }
    
    /**
     * in this function we give the gui Catalog Number and then  press button of search
     * @param event
     * @throws Exception
     * after that we send to the server to check if there is a book of that same Catalog Number that been borrowed 
     */
    public void SearchButton(ActionEvent event) throws Exception {
    	if (BookID.getText().equals(""))
			JOptionPane.showMessageDialog(frame, "please fill the fields");
		else {
			try {
				book.setCatalogNumber(BookID.getText());
				String command = "09"+book.getCatalogNumber();
				 ConnectionToServer.sendData(this,command);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
    /**
     * in this function if we want to return the book to the library we have to press button 
     * @param event
     * and then it well send to the server the Catalog Number and the Reader ID to update all the details
     */
    public void ReturnBookButton(ActionEvent event) {
    	if (book.getCatalogNumber()==null)
			JOptionPane.showMessageDialog(frame, "please fill the fields");
		else {
			try {
				String command = "10"+book.getCatalogNumber()+","+ReaderID;
				 ConnectionToServer.sendData(this,command);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
    
   /**
    * in this function we well receive an object that have a message inside from the server
    * @param obj
    * if we receive  a string = bookfound then it well but all the date details  the borrow date and the return date and the current date in a text
    * else if we receive done form the server then the book has returned
    */
	@Override
	public void display(Object obj) {
		 String message=(String)obj;
		// TODO Auto-generated method stub
		   List<String> data=Arrays.asList(message.split(","));
		   if(data.get(0).equals("BookFound")) {
			   ReaderID=data.get(1);
			   BorrowDate.setText(data.get(2));
			   ReturnDate.setText(data.get(3));
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
			   ReturnedOn.setText(dateFormat.format(date));
		   }
		   if(data.get(0).equals("done")) {
				JOptionPane.showMessageDialog(frame, "the book has returned successfuly");
		    	BookID.setText("");
		    	BorrowDate.setText("");
		    	ReturnDate.setText("");
		    	ReturnedOn.setText("");		   
		    	}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		book=new Book();
		UserInformation.setText(LoginController.UserInfo2);

	}
	
}