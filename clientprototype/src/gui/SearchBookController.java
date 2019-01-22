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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.JOptionPane;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import entity.Book;
import controller.ConnectionToServer;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import ocsf.client.ChatIF;

public class SearchBookController extends NavigationBar implements ChatIF {
	private ListBooksController lbc;

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
    void searchBook(ActionEvent event) throws IOException {
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
          MoveToResultPage(event);
       }
    }

	@Override
	public void display(Object msg) {
		
		if(msg.equals("-1"))
			JOptionPane.showMessageDialog(frame, "No Match Found!");
		
			
		}
			
	public void MoveToResultPage(ActionEvent event) throws IOException
	{
		FXMLLoader loader =new FXMLLoader(getClass().getResource("GUI_FXML/List_Books.fxml"));
		Parent searchResult=loader.load();
    	
		Scene result=new Scene(searchResult);
    	Stage window =(Stage)(((Node) event.getSource()).getScene().getWindow());
    	ListBooksController listbookscontroller = loader.<ListBooksController>getController();
    	
		ArrayList<Book> books = new ArrayList<Book>();
        
        try
        {
            FileInputStream fis = new FileInputStream("BookData");
            ObjectInputStream ois = new ObjectInputStream(fis);
 
            books = (ArrayList) ois.readObject();
 
            ois.close();
            fis.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
            return;
        }
        catch (ClassNotFoundException c)
        {
            System.out.println("Class not found");
            c.printStackTrace();
            return;
        }
        
        listbookscontroller.loadBooks(books);
    	window.setScene(result);
	
	}
	}




