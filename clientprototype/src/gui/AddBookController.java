package gui;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import controller.ConnectionToServer;
import entity.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import ocsf.client.ChatIF;
/**
 * This class is the controller of add book GUI page 
 * @author Mahmoud Sayid
 *
 */
public class AddBookController extends NavigationBar implements Initializable, ChatIF {
	Book book;
	@FXML
	private DatePicker datePurchased;
	@FXML
	private TextField bookName;
	@FXML
	private TextField publisherName;
	@FXML
	private TextField bookEdite;
	@FXML
	private TextArea bookDescription;
	@FXML
	private TextField catalogNumber;
	//@FXML
	//private TextField numberOFCopies;
	@FXML
	private TextField positionOnTheShelf;
	@FXML
	private DatePicker dateOfPrint;
	@FXML
	private TextField contentTableFileLocation;
	@FXML
	private TextField bookPhotoFileLocation;
	@FXML
	private TextField bookCatagory;
    @FXML
    private Text UserInformation;
	JOptionPane frame;
	
	/**
	 * This method is to upload the content Pdf file and convert it to byte array and insert it to the new book 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void uploadContentFile(ActionEvent event) throws IOException {
		FileChooser fileChooser = new FileChooser();
		File path = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
		contentTableFileLocation.setText(path.getAbsolutePath());
		Path pdfPath = Paths.get(contentTableFileLocation.getText());
		byte[] pdf = Files.readAllBytes(pdfPath);
		book.setContentfile(pdf);
	}
	/**
	 * This method is to load the book picture from the client computer and convert it to byte array and insert it to the new book 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void uploadPhoto(ActionEvent event) throws IOException {
		FileChooser fileChooser = new FileChooser();
		File path = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
		System.out.println(path.getAbsolutePath());
		bookPhotoFileLocation.setText(path.getAbsolutePath());
		Path pdfPath = Paths.get(bookPhotoFileLocation.getText());
		byte[] photo = Files.readAllBytes(pdfPath);
		book.setBookphoto(photo);
	}
	
	
	/**
	 * clear all the fields to Insert different book 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void clearAll(ActionEvent event) throws IOException { 
		
		//datePurchased.setv;
		bookName.setText("");
		publisherName.setText("");
		bookEdite.setText("");
		bookDescription.setText("");
		catalogNumber.setText("");
		//numberOFCopies.setText("");
		positionOnTheShelf.setText("");
		contentTableFileLocation.setText("");
		bookPhotoFileLocation.setText("");
		bookCatagory.setText("");
		dateOfPrint.getEditor().clear();
		datePurchased.getEditor().clear();
	}
	
	@FXML
	void addBook(ActionEvent event){  
		try {
			book.setBookName(bookName.getText());
			book.setPublisherName(publisherName.getText());
			book.setBookEdite(bookEdite.getText());
			book.setCatalogNumber(catalogNumber.getText());
			//book.setNumberOFCopies(numberOFCopies.getText());
			book.setPositionOnTheShelf(positionOnTheShelf.getText());
			book.setBookCatagory(bookCatagory.getText());
			book.setBookDescription(bookDescription.getText());
			book.setDatePurchased(dateOfPrint.getValue());
			book.setDateOfPrint(datePurchased.getValue());
			if(book.getBookphoto()==null)
				throw new Exception("Please insert the book picture");
			
			if(book.getContentfile()==null)
				throw new Exception("Please insert the book contant");
			
			
			ObjectOutput out = null;
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			out = new ObjectOutputStream(bos);   
			  out.writeObject(book);
			  out.flush();
			  byte[] objbyte = bos.toByteArray();
			 ConnectionToServer.sendData(this, objbyte);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			JOptionPane.showMessageDialog(frame,e.getMessage());
		}
	}
/**
 * Display the message from the server in pop up screen if the add success or it was a problem
 */
	@Override
	public void display(Object message) {
			JOptionPane.showMessageDialog(frame, message);
	
				
		
	}
/**
 * create a new book to fill it with data in this class
 * take the user information his name and his roll from login page and view it in add book gui
 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		book=new Book();
		UserInformation.setText(LoginController.UserInfo2);

	}

}
