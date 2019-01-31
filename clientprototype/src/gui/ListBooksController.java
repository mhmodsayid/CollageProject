package gui;



import javafx.scene.control.TextArea;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import entity.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ocsf.client.ChatIF;

/**
 * @author Ibrahem Mrisat
 * 
 * This class is the controller of the FXML page List_Books.FXML 
 * it gets the search result from the SearchBookController as an ArrayList 
 * saves it and arranges it in the FXML page List_Books.fxml 
 */
public class ListBooksController extends NavigationBar implements Initializable, ChatIF,Serializable  {
	


	@FXML // fx:id="NextPageBtn"
    private Button NextPageBtn; // Value injected by FXMLLoader

    @FXML // fx:id="PrevPageBtn"
    private Button PrevPageBtn; // Value injected by FXMLLoader

    @FXML // fx:id="BookPhoto1"
    private ImageView BookPhoto1; // Value injected by FXMLLoader

    @FXML // fx:id="BookName1"
    private TextArea BookName1; // Value injected by FXMLLoader

    @FXML // fx:id="BookStatus1"
    private Text BookStatus1; // Value injected by FXMLLoader

    @FXML // fx:id="ChooseBook1"
    private Button ChooseBook1; // Value injected by FXMLLoader

    @FXML // fx:id="BookPhoto2"
    private ImageView BookPhoto2; // Value injected by FXMLLoader

    @FXML // fx:id="BookName2"
    private TextArea  BookName2; // Value injected by FXMLLoader

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
    private TextArea  BookName3; // Value injected by FXMLLoader
    
    @FXML // fx:id="infoText"
    private Text infoText; // Value injected by FXMLLoader
    
    @FXML // fx:id="searchbuttonTempReader"
    private Button searchbuttonTempReader; // Value injected by FXMLLoader
    
    @FXML
    private Text returnDate1;
    
    @FXML
    private Text returnDate2;
    
    @FXML
    private Text returnDate3;
 
    @FXML
    private Text return1;
    
    @FXML
    private Text return2;
    
    @FXML
    private Text return3;
    
    @FXML
    private ButtonBar WorkerMenu;
    @FXML
    private ButtonBar ReaderMenu;
    @FXML
    private Text UserInformation;
    
    private int pageNum=0;
   
    private List<Book> books = new ArrayList<Book>();
    
    JOptionPane frame;
   
	private int index1=0;

	private int index2=0;

	private int index3=0;
    @FXML
    private ButtonBar visitMenu;
	/**
     * 
     * @param event
     * 
     * The method decreases the index of the list to get the
     * Previous book in the list
     */
    @FXML
    void PrevPage(ActionEvent event) {
    	this.pageNum-=1;
    	if(this.pageNum>0)
    	{
    	setBooks();
    	}
    	else
    	{
    		 PrevPageBtn.setDisable(true);
    		JOptionPane.showMessageDialog(frame, "This is the First Result Page!");
    	}
    }
    /**
     * 
     * @param event
     * 
     * The method increases the index of the list to get the
     * next book in the list
     */
    @FXML
    void nextPage(ActionEvent event) {
      this.pageNum+=1;
    
	if(this.pageNum+2<this.books.size())
	{
	  PrevPageBtn.setDisable(false);
      setBooks();
	}
      else
    	  JOptionPane.showMessageDialog(frame, "No More Results!");
    }
    /**
     * 
     * @param event
     * @throws IOException
     * 
     * The method sets the chosen book in the static book in the class Book
     * then loads the FXML scene of Book_Order with the chosen book
     */
    @FXML
    void chooseBook1(ActionEvent event) throws IOException {
    	Book.setTheBook(this.books.get(this.index1));
    	FXMLLoader loader =new FXMLLoader(getClass().getResource("GUI_FXML/Book_Order.fxml"));
		Parent book1=loader.load();
    	
		Scene result=new Scene(book1);
    	Stage window =(Stage)(((Node) event.getSource()).getScene().getWindow());
    	window.setScene(result);
    	this.books=null;
    }
    /**
     * 
     * @param event
     * @throws IOException
     * 
     * The method sets the chosen book in the static book in the class Book
     * then loads the FXML scene of Book_Order with the chosen book
     */
    @FXML
    void chooseBook2(ActionEvent event) throws IOException {
    	Book.setTheBook(this.books.get(this.index2));
    	FXMLLoader loader =new FXMLLoader(getClass().getResource("GUI_FXML/Book_Order.fxml"));
		Parent book1=loader.load();
    	
		Scene result=new Scene(book1);
    	Stage window =(Stage)(((Node) event.getSource()).getScene().getWindow());
    	
    	window.setScene(result);
    	this.books=null;

    }
    /**
     * 
     * @param event
     * @throws IOException
     * 
     * The method sets the chosen book in the static book in the class Book
     * then loads the FXML scene of Book_Order with the chosen book
     */
    @FXML
    void chooseBook3(ActionEvent event) throws IOException { 
    	Book.setTheBook(this.books.get(this.index3));
    	FXMLLoader loader =new FXMLLoader(getClass().getResource("GUI_FXML/Book_Order.fxml"));
		Parent book1=loader.load();
		Scene result=new Scene(book1);
    	Stage window =(Stage)(((Node) event.getSource()).getScene().getWindow());
    	window.setScene(result);
    	this.books=null;

    }
    /**
	 * this function moved the user to search_book page as visitor by clicking on
	 * Search button
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void moveToSearchScreen(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI_FXML/Search_Book.fxml"));
		Parent searchScreen =loader.load();
		Scene scene = new Scene(searchScreen);
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		primaryStage.hide();
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	@Override
	public void display(Object msg) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
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
	
	/**
	 * Organizes the books into the FXML page for every book in ArrayList books 
	 * the method puts book's name,photo and status. 
	 * 
	 * if there is less than 3 books then it arrange them in the first two columns or 
	 * in the middle column if it was only one 
	 * */
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
			this.BookStatus1.setText(this.books.get(this.pageNum).getQuantity()>0 ? "Available":"Not Available");
			this.BookStatus1.setFill(this.books.get(this.pageNum).getQuantity()>0 ? Color.web("#00ff4d") : Color.RED);
			this.returnDate1.setText(this.books.get(this.pageNum).getDatePurchased());
			this.index1=this.pageNum;
			}
			/*this is the book number 2*/
			if(this.books.get(this.pageNum+1)!=null)
			{
			this.BookName2.setText(this.books.get(this.pageNum+1).getBookName());
			Image img2 = new Image(new ByteArrayInputStream(this.books.get(this.pageNum+1).getBookphoto()));
			this.BookPhoto2.setImage(img2);
			this.BookStatus2.setText(this.books.get(this.pageNum+1).getQuantity()>0 ? "Available":"Not Available");
			this.BookStatus2.setFill(this.books.get(this.pageNum+1).getQuantity()>0 ? Color.web("#00ff4d") : Color.RED);
			this.returnDate2.setText(this.books.get(this.pageNum+1).getDatePurchased());
			this.index2=this.pageNum+1;
			}
			if(this.books.get(this.pageNum+2)!=null)
			{
			/*this is the book number 3*/
			this.BookName3.setText(this.books.get(this.pageNum+2).getBookName());
			Image img3 = new Image(new ByteArrayInputStream(this.books.get(this.pageNum+2).getBookphoto()));
			this.BookPhoto3.setImage(img3);
			this.BookStatus3.setText(this.books.get(this.pageNum+2).getQuantity()>0 ? "Available":"Not Available");
			this.BookStatus3.setFill(this.books.get(this.pageNum+2).getQuantity()>0 ? Color.web("#00ff4d") : Color.RED);
			this.returnDate3.setText(this.books.get(this.pageNum+2).getDatePurchased());
			this.index3=this.pageNum+2;
			}
			
		}
			
		else if(this.books.size()==2)
		{
			this.ChooseBook3.setVisible(false);
			this.NextPageBtn.setDisable(true);
			this.return3.setVisible(false);
			this.returnDate3.setVisible(false);
			if(this.books.get(this.pageNum)!=null)
			{
			/*this is the book number 1*/
			this.BookName1.setText(this.books.get(this.pageNum).getBookName());
			Image img = new Image(new ByteArrayInputStream(this.books.get(this.pageNum).getBookphoto()));
			this.BookPhoto1.setImage(img);
			this.BookStatus1.setText(this.books.get(this.pageNum).getQuantity()>0 ? "Available":"Not Available");
			this.BookStatus1.setFill(this.books.get(this.pageNum).getQuantity()>0 ? Color.web("#00ff4d") : Color.RED);
			this.returnDate1.setText(this.books.get(this.pageNum).getDatePurchased());
			this.index1=this.pageNum;
			}
			if(this.books.get(this.pageNum+1)!=null)
			{
			/*this is the book number 2*/
			this.BookName2.setText(this.books.get(this.pageNum+1).getBookName());
			Image img2 = new Image(new ByteArrayInputStream(this.books.get(this.pageNum+1).getBookphoto()));
			this.BookPhoto2.setImage(img2);
			this.BookStatus2.setText(this.books.get(this.pageNum+1).getQuantity()>0 ? "Available":"Not Available");
			this.BookStatus2.setFill(this.books.get(this.pageNum+1).getQuantity()>0 ? Color.web("#00ff4d") : Color.RED);
			this.returnDate2.setText(this.books.get(this.pageNum+1).getDatePurchased());
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
			this.BookStatus2.setText(this.books.get(0).getQuantity()>0 ? "Available":"Not Available");
			this.BookStatus2.setFill(this.books.get(0).getQuantity()>0 ? Color.web("#00ff4d") : Color.RED);
			this.returnDate2.setText(this.books.get(this.pageNum).getDatePurchased());
			this.index2=0;
			this.ChooseBook1.setVisible(false);
			this.ChooseBook3.setVisible(false);
			this.return1.setVisible(false);
            this.return3.setVisible(false);
			this.NextPageBtn.setDisable(true);
			}
		}
	}
		
	/**
	 * 		
	 * @param books2 list of book class to save the search result
	 * 
	 * The method gets a list of books and saves it in the local list
	 * then sets the text of infoText in the FXML to show the number of books found
	 */
	public void loadBooks(List<Book> books2) {
		// TODO Auto-generated method stub
		this.books=books2;
		this.infoText.setText(this.books.size()+" Books Found! Click on book to choose it");
		setBooks();
	}



}

