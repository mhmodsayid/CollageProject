package gui;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import entity.Book;
import javafx.event.ActionEvent;

/**
 * Sample Skeleton for 'List_Books.fxml' Controller Class
 */

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ocsf.client.ChatIF;

public class ListBooksController extends NavigationBar implements Initializable, ChatIF,Serializable  {
 


	@FXML // fx:id="NextPageBtn"
    private Button NextPageBtn; // Value injected by FXMLLoader

    @FXML // fx:id="PrevPageBtn"
    private Button PrevPageBtn; // Value injected by FXMLLoader

    @FXML // fx:id="BookPhoto1"
    private ImageView BookPhoto1; // Value injected by FXMLLoader

    @FXML // fx:id="BookName1"
    private Text BookName1; // Value injected by FXMLLoader

    @FXML // fx:id="BookStatus1"
    private Text BookStatus1; // Value injected by FXMLLoader

    @FXML // fx:id="ChooseBook1"
    private Button ChooseBook1; // Value injected by FXMLLoader

    @FXML // fx:id="BookPhoto2"
    private ImageView BookPhoto2; // Value injected by FXMLLoader

    @FXML // fx:id="BookName2"
    private Text BookName2; // Value injected by FXMLLoader

    @FXML // fx:id="BookStatus2"
    private Text BookStatus2; // Value injected by FXMLLoader

    @FXML // fx:id="ChooseBook2"
    private Button ChooseBook2; // Value injected by FXMLLoader

    @FXML // fx:id="BookPhoto3"
    private ImageView BookPhoto3; // Value injected by FXMLLoader

    @FXML // fx:id="ChooseBook3"
    private Button ChooseBook3; // Value injected by FXMLLoader

    @FXML // fx:id="BookStatus3"
    private Text BookStatus3; // Value injected by FXMLLoader

    @FXML // fx:id="BookName3"
    private Text BookName3; // Value injected by FXMLLoader
    
    private int pageNum=0;
    private ArrayList<Book> books = new ArrayList<Book>();
    JOptionPane frame;

	private int index1=0;

	private int index2=0;

	private int index3=0;


    @FXML
    void PrevPage(ActionEvent event) {
    	if(this.pageNum>0)
    	{
    	this.pageNum-=1;
    	setBooks();
    	}
    	else
    	{
    		 PrevPageBtn.setDisable(true);
    		JOptionPane.showMessageDialog(frame, "This is the First Result Page!");
    	}
    }
    @FXML
    void nextPage(ActionEvent event) {
      this.pageNum+=1;
    
	if(this.pageNum<(this.books.size()/3)+(this.books.size()%3))
	{
	  PrevPageBtn.setDisable(false);
      setBooks();
	}
      else
    	  JOptionPane.showMessageDialog(frame, "No More Results!");
    }
    @FXML
    void chooseBook1(ActionEvent event) throws IOException {
    	FXMLLoader loader =new FXMLLoader(getClass().getResource("GUI_FXML/Book_Order.fxml"));
		Parent book1=loader.load();
    	
		Scene result=new Scene(book1);
    	Stage window =(Stage)(((Node) event.getSource()).getScene().getWindow());
    	Book.setTheBook(this.books.get(this.index1));
    	window.setScene(result);
    	this.books=null;
    }

    @FXML
    void chooseBook2(ActionEvent event) throws IOException {
    	FXMLLoader loader =new FXMLLoader(getClass().getResource("GUI_FXML/Book_Order.fxml"));
		Parent book1=loader.load();
    	
		Scene result=new Scene(book1);
    	Stage window =(Stage)(((Node) event.getSource()).getScene().getWindow());
    	Book.setTheBook(this.books.get(this.index2));
    	window.setScene(result);
    	this.books=null;

    }

    @FXML
    void chooseBook3(ActionEvent event) throws IOException {
    	FXMLLoader loader =new FXMLLoader(getClass().getResource("GUI_FXML/Book_Order.fxml"));
		Parent book1=loader.load();
		Scene result=new Scene(book1);
    	Stage window =(Stage)(((Node) event.getSource()).getScene().getWindow());
    	Book.setTheBook(this.books.get(this.index3));
    	window.setScene(result);
    	this.books=null;

    }
	@Override
	public void display(Object msg) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		//setBooks();
		//this.BookName1.setText(msg);
		
	}
	private void setBooks() {
		// TODO Auto-generated method stub
		if(this.books.size()>=3)
		{
			/*this is the book number 1*/
			if(this.books.get(this.pageNum)!=null)
			{
			this.BookName1.setText(this.books.get(this.pageNum).getBookName());
			Image img = new Image(new ByteArrayInputStream(this.books.get(this.pageNum).getBookphoto()));
			this.BookPhoto1.setImage(img);
			this.BookStatus1.setText(this.books.get(this.pageNum).getBookStatus() );
			this.BookStatus1.setFill(this.books.get(this.pageNum).getBookStatus().equals("Available") ? Color.web("#00ff4d") : Color.RED);
			this.index1=this.pageNum;
			}
			/*this is the book number 2*/
			if(this.books.get(this.pageNum+1)!=null)
			{
			this.BookName2.setText(this.books.get(this.pageNum+1).getBookName());
			Image img2 = new Image(new ByteArrayInputStream(this.books.get(this.pageNum+1).getBookphoto()));
			this.BookPhoto2.setImage(img2);
			this.BookStatus2.setText(this.books.get(this.pageNum+1).getBookStatus() );
			this.BookStatus2.setFill(this.books.get(this.pageNum+1).getBookStatus().equals("Available") ? Color.web("#00ff4d") : Color.RED);
			this.index2=this.pageNum+1;
			}
			if(this.books.get(this.pageNum+2)!=null)
			{
			/*this is the book number 3*/
			this.BookName3.setText(this.books.get(this.pageNum+2).getBookName());
			Image img3 = new Image(new ByteArrayInputStream(this.books.get(this.pageNum+2).getBookphoto()));
			this.BookPhoto3.setImage(img3);
			this.BookStatus3.setText(this.books.get(this.pageNum+2).getBookStatus() );
			this.BookStatus3.setFill(this.books.get(this.pageNum+2).getBookStatus().equals("Available") ? Color.web("#00ff4d") : Color.RED);
			this.index3=this.pageNum+2;
			}
			
		}
			
		else if(this.books.size()==2)
		{
			if(this.books.get(this.pageNum)!=null)
			{
			/*this is the book number 1*/
			this.BookName1.setText(this.books.get(this.pageNum).getBookName());
			Image img = new Image(new ByteArrayInputStream(this.books.get(this.pageNum).getBookphoto()));
			this.BookPhoto1.setImage(img);
			this.BookStatus1.setText(this.books.get(this.pageNum).getBookStatus() );
			this.BookStatus1.setFill(this.books.get(this.pageNum).getBookStatus().equals("Available") ? Color.web("#00ff4d") : Color.RED);
			this.index1=this.pageNum;
			}
			if(this.books.get(this.pageNum+1)!=null)
			{
			/*this is the book number 2*/
			this.BookName2.setText(this.books.get(this.pageNum+1).getBookName());
			Image img2 = new Image(new ByteArrayInputStream(this.books.get(this.pageNum+1).getBookphoto()));
			this.BookPhoto2.setImage(img2);
			this.BookStatus2.setText(this.books.get(this.pageNum+1).getBookStatus());
			this.BookStatus2.setFill(this.books.get(this.pageNum+1).getBookStatus().equals("Available") ? Color.web("#00ff4d") : Color.RED);
			this.index2=this.pageNum+1;
			}
		}
		else if(this.books.size()==1)
		{
			/*this is the book number 2*/
			if(this.books.get(0)!=null)
			{
			this.BookName2.setText(this.books.get(0).getBookName());
			Image img2 = new Image(new ByteArrayInputStream(this.books.get(0).getBookphoto()));
			this.BookPhoto2.setImage(img2);
			this.BookStatus2.setText(this.books.get(0).getBookStatus());
			this.BookStatus2.setFill(this.books.get(0).getBookStatus().equals("Available") ? Color.web("#00ff4d") : Color.RED);
			this.index2=0;
			}
		}
	}
		
			
	public void loadBooks(ArrayList<Book> books) {
		// TODO Auto-generated method stub
		this.books=books;
		setBooks();
	}



}

