package gui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.plaf.SliderUI;

import controller.ConnectionToServer;
import entity.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ocsf.client.ChatIF;

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
	@FXML
	private TextField numberOFCopies;
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
	JOptionPane frame;
	@FXML
	void uploadContentFile(ActionEvent event) throws IOException {
		FileChooser fileChooser = new FileChooser();
		File path = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
		contentTableFileLocation.setText(path.getAbsolutePath());
		Path pdfPath = Paths.get(contentTableFileLocation.getText());
		byte[] pdf = Files.readAllBytes(pdfPath);
		book.setContentfile(pdf);
	}

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
	
	
	@FXML
	void goToOrder(ActionEvent event) throws IOException, InterruptedException { 
		//inisalize the book
		
		//temp =================================================
		String command = "04123";
		
		ConnectionToServer.sendData(this, command);
		while(Book.getTheBook()==null || !(Book.getTheBook().getBookName().equals("123"))) {
			Thread.sleep(10);
		}
	//	Book.setTheBook(new Book());
		
		//temp+====================================================
		 ReturnScreen = FXMLLoader.load(getClass().getResource("GUI_FXML/Book_Order.fxml"));
		Scene scene = new Scene(ReturnScreen);
    	Stage primaryStage = (Stage)((Node) event.getSource()).getScene().getWindow();
    	primaryStage.hide();
		primaryStage.setScene(scene);
		primaryStage.show();
		System.out.println("worked");
		
	}
	
	@FXML
	void clearAll(ActionEvent event) throws IOException { 
		
		//datePurchased.setv;
		bookName.setText("");
		publisherName.setText("");
		bookEdite.setText("");
		bookDescription.setText("");
		catalogNumber.setText("");
		numberOFCopies.setText("");
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
			book.setNumberOFCopies(numberOFCopies.getText());
			book.setPositionOnTheShelf(positionOnTheShelf.getText());
			book.setBookCatagory(bookCatagory.getText());
			book.setBookDescription(bookDescription.getText());
			LocalDate dateofprint = dateOfPrint.getValue();
			LocalDate datepurchased = datePurchased.getValue();
			book.setDatePurchased(datepurchased.toString());
			book.setDateOfPrint(dateofprint.toString());
			
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

	@Override
	public void display(Object message) {
		if(message.equals("addBookSuccess")) 
			JOptionPane.showMessageDialog(frame, "the book added successfuly");
	//========================================================================
		Book booke=new Book();
			 
			 
			    try {
			    	ByteArrayInputStream bis = new ByteArrayInputStream((byte[]) message);
				    ObjectInput in = new ObjectInputStream(bis);
					booke = (Book) in.readObject();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			  
		
		  Book.setTheBook(booke);
		
		
		
	//========================================================================
				
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		book=new Book();
		
	}

}
