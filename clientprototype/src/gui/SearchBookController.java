package gui;

/**
 * @author Ibrahem
 * 
 */
/*

*/
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.JOptionPane;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import entity.Book;
import controller.ConnectionToServer;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import ocsf.client.ChatIF;

public class SearchBookController extends NavigationBar implements Initializable, ChatIF {
	private ListBooksController lbc;
	List<Book> books = new ArrayList<Book>();
    @FXML // fx:id="authorName"
    private TextField authorName; // Value injected by FXMLLoader

    @FXML // fx:id="bookName"
    private TextField bookName; // Value injected by FXMLLoader

    @FXML // fx:id="bookSubject"
    private TextField bookSubject; // Value injected by FXMLLoader

    @FXML // fx:id="searchBtn"
    private Button searchBtn; // Value injected by FXMLLoader

    @FXML // fx:id="clearAllBtn"
    private Button clearAllBtn; // Value injected by FXMLLoader

    @FXML // fx:id="freeSearch"
    private TextArea freeSearch; // Value injected by FXMLLoader
    @FXML
    private ButtonBar WorkerMenu;
    @FXML
    private ButtonBar ReaderMenu;
    @FXML
    private Text UserInformation;
	JOptionPane frame;
	

    ActionEvent SaveEvent;
	
    @FXML
    void clearAll(ActionEvent event) {

    	 bookName.setText("");
    	 bookSubject.setText("");
    	 freeSearch.setText("");
    	 authorName.setText("");
    }

    @FXML
    void searchBook(ActionEvent event) throws IOException, InterruptedException {
    	String book_Name=bookName.getText()
    			,book_Subject=bookSubject.getText()
    			,author_Name=authorName.getText()
    			,free_Search=freeSearch.getText();
    	String command="05";
      if(book_Name.equals("")&&book_Subject.equals("") &&author_Name.equals("")&&free_Search.equals(""))
      	{
			JOptionPane.showMessageDialog(frame, "Please fill one field at least!!!");
      	}
      else {
      //adding the search fields to the command number to send to server 
      if(book_Name.equals(""))
         	  command += " "+",";
      else if(!book_Name.equals(""))
    	  command += book_Name+",";
      if(book_Subject.equals(""))
    	  command+=" "+",";
      else if(!book_Subject.equals(""))
    	  command+=book_Subject+",";
      if(author_Name.equals(""))
    	  command+=" "+",";
      else if(!author_Name.equals(""))
    	  command+=author_Name+",";
      if(free_Search.equals(""))
    	  command+=" "+",";
      else if(!free_Search.equals(""))
    	  command+=free_Search+",";
    	  /*now we have command=05book_name,book_subject,author_name,free_search
    	   if any of the fields is empty then we send to the server space . 
    	   in that way we will know what the user wanted to search.
    	   * */
     // this.SaveEvent=event;
		ConnectionToServer.sendData(this, command);
		
		while(books.isEmpty())
			Thread.sleep(10);
		
       MoveToResultPage(event);
       }
    }

	@Override
	public void display(Object msg) { 
		 try {
			 	books.clear();
		    	ByteArrayInputStream bis = new ByteArrayInputStream((byte[]) msg);
			    ObjectInput in = new ObjectInputStream(bis);
				books = (List <Book>) in.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		 
		}
			
	public void MoveToResultPage(ActionEvent event) throws IOException
	{
		FXMLLoader loader =new FXMLLoader(getClass().getResource("GUI_FXML/List_Books.fxml"));
		Parent searchResult=loader.load();
    	
		Scene result=new Scene(searchResult);
    	Stage window =(Stage)(((Node) event.getSource()).getScene().getWindow());
    	ListBooksController listbookscontroller = loader.<ListBooksController>getController();   
        listbookscontroller.loadBooks(books);
    	window.setScene(result);
	
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		UserInformation.setText(LoginController.UserInfo2);
		if(LoginController.userType2.equals("Reader")) {
			WorkerMenu.setVisible(false);
			ReaderMenu.setVisible(true);
		}
		else 
			if(LoginController.userType2.equals("Librarian")||LoginController.userType2.equals("Manager")) {
			WorkerMenu.setVisible(true);
			ReaderMenu.setVisible(false);
		}
	}
	}