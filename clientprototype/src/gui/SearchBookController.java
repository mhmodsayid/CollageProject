package gui;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import ocsf.client.ChatIF;
/**
 * @author Ibrahem Mrisat
 * 
 * this class is the controller of the FXML page Search_Book.fxml
 * it gets the input from the FXML text fields and sends it to the server 
 * then gets the result and add it to an ArrayList and send it to List_Books.fxml
 * Controller to display them. 
 * 
 */
public class SearchBookController extends NavigationBar implements Initializable ,ChatIF {
	/**
	 * @param ArraiList of books to save the books from search result
	 */
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
	private boolean noBookFound=true;
	    
    @FXML
    private ButtonBar visitMenu;

  
    
	/**
	 * clears all FXML text fields 
	 * 
	 * @param event
	 */
    @FXML
    void clearAll(ActionEvent event) {

    	 bookName.setText("");
    	 bookSubject.setText("");
    	 freeSearch.setText("");
    	 authorName.setText("");
    }
    /**
     * gets the input from the FXML text fields and arranges a command to send to
     * the server with the input in it
     *  
     * @param event
     * @throws IOException
     * @throws InterruptedException
     */
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
		ConnectionToServer.sendData(this, command);
		books.clear();
		while(books.isEmpty())
			Thread.sleep(100);
		if(!this.noBookFound)
       MoveToResultPage(event);
		this.noBookFound=false;
       }
    }
    /**
     * gets an object from the server and convert it to an ArrayList of books to fill 
     * the local list books
     * if its empty it displays a message dialog on a frame that is no match found
     * 
     */
	@Override
	public void display(Object msg) 
	{ 	
				
		 try {
			 	
		    	ByteArrayInputStream bis = new ByteArrayInputStream((byte[]) msg);
			    ObjectInput in = new ObjectInputStream(bis);
				books = (List <Book>) in.readObject();
				if(books.get(0).getBookStatus().equals("-1"))
				{
					JOptionPane.showMessageDialog(frame, "No Match Found!");
					this.noBookFound=true;
				}
				else
				{
					this.noBookFound=false;
					return;
				}
					
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	
		 
		}
	/**
	 * This method changes the scene to the FXML page List_Books.fxml 
	 * and sends the local list books to its controller.  
	 * @param event
	 * @throws IOException
	 */
	public void MoveToResultPage(ActionEvent event) throws IOException
	{
		FXMLLoader loader =new FXMLLoader(getClass().getResource("GUI_FXML/List_Books_new.fxml"));
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
		if(LoginController.UserInfo2==null){
			UserInformation.setText("[Temprary Reader]");
			ReaderMenu.setVisible(false);
			WorkerMenu.setVisible(false);

			
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




