package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import controller.ConnectionToServer;
import entity.Reader;
import entity.TableData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import ocsf.client.ChatIF;


public class ReaderCardController extends NavigationBar implements Initializable, ChatIF {
	@FXML
	private TextField readerID;
	@FXML
	private Text readerName;
	@FXML
	private Text readerStatus;
	@FXML
	private Button activate;
	@FXML
	private TextField email;
	@FXML
	private TextField phone;
	@FXML
	private Button edit;
	@FXML
	private Button clearAll;
	@FXML
	private Button showHistory;
	@FXML
	private TableView table;
	@FXML
	private Text UserInformation;
	@FXML
	private TableColumn<TableData,String> bookNameColumn;
	@FXML
	private TableColumn<TableData,String> borrowDateColumn;
	@FXML
	private TableColumn<TableData,String> returnDateColumn;
	@FXML
	private TableColumn<TableData,String> returnStatusColumn;
	public ObservableList<ObservableList> tableData;
	JOptionPane frame;
	Reader reader;
	int statusFlag=0;
	public static String userType;
	public static String userStatus;
	
	
	public void initTable(ActionEvent event) {
		  if(readerID.getText().equals("") ||isNumeric(readerID.getText())==false) {
			   JOptionPane.showMessageDialog(frame, "please fill reader ID");
		   }
		   else {
		try {
			reader.setStudent_id(readerID.getText());
			String command = "30"+reader.getStudent_id();
			 ConnectionToServer.sendData(this,command);
			
		} catch (IOException e) {
			e.printStackTrace();
				}
		   }

		  
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

	
	
	
	


	@Override
	public void display(Object obj) {
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
		   }
		   if(statusFlag==1) {

			 tableData=FXCollections.observableArrayList();
			 tableData.add((ObservableList) obj);
			 table.getItems().add(obj);
			 //String message=(String)obj;
			 //List<String> data=Arrays.asList(message.split(","));
		   }
		   

	}
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		UserInformation.setText(LoginController.UserInfo2);
		
	}
	

}

