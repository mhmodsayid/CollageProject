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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
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
	private Button search;
	@FXML
	private Button clearAll;
	@FXML
	private Button showHistory;
	@FXML
	private Button save;
	@FXML
	private Button managerReaderCard;
	@FXML
	private Button workerDetails;
	@FXML
	private Button actionsReport;
	@FXML
	private Button borrowreport;
	@FXML
	private Button lateReturns;
	@FXML
	private TableView<TableData> table;
	@FXML
	private Text UserInformation;
	@FXML
	private TableColumn bookNameCol;
	@FXML
	private TableColumn borrowDateCol;
	@FXML
	private TableColumn returnDateCol;
	@FXML
	private TableColumn returnStatusCol;
	@FXML
	private Pane managerTab;
	
	private final ObservableList<TableData> tableData=FXCollections.observableArrayList();
	JOptionPane frame;
	Reader reader;
	int statusFlag=0;
	int rowCnt=0;
	public static String userType;
	public static String userStatus;
	

	
	/*
	 *this function clears all the fields when clear all button is pressed 
	 */
	@FXML
	void ClearAll(ActionEvent event) {
		readerID.setText("");
		readerName.setText("");
		email.setText("");
		phone.setText("");
		readerStatus.setText("");
		statusFlag=0;
		rowCnt=0;
		tableData.clear();
		activate.setVisible(false);
		
	}
	
	
	public void ReaderCardInfo(ActionEvent event) {
	
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
	   
	   public void PopulateTable(ActionEvent event) {
		   if(readerID.equals("")) {
			   JOptionPane.showMessageDialog(frame, "Enter reader id");
		   }
		   if(statusFlag==1) {
			   statusFlag=2;
		   try {
		   String command = "31"+reader.getStudent_id();
		   ConnectionToServer.sendData(this, command);
		   } catch (IOException e) {
			   e.printStackTrace();
		   }
		   }
		   
	   }
	   public void EditInfo(ActionEvent event) {
		   if(statusFlag==1) {
		   //email.setText("");
		  // phone.setText("");
		   edit.setVisible(false);
		   save.setVisible(true);
		   email.setEditable(true);
		   phone.setEditable(true);
		   }
		   else {
			   JOptionPane.showMessageDialog(frame, "Enter reader ID and press Search");
		   }
		   
	   }
	   public void SaveInfo(ActionEvent event) {
		   if(email.getText().equals("")||phone.getText().equals("")) {
			   JOptionPane.showMessageDialog(frame, "Enter email and phone");
		   }
		   else {
			  JOptionPane.showMessageDialog(frame, "Confirm info change?");
		   try {
		   String command = "32"+reader.getStudent_id()+","+email.getText()+","+phone.getText();
		   ConnectionToServer.sendData(this, command);
		   } catch (IOException e) {
			   e.printStackTrace();
		   }
		   }
		   
	   }
	   public void ChangeStatusToActive(ActionEvent event) {
		   
		   int action = JOptionPane.showOptionDialog(frame, "if reader status is 'BLOCKED', ask for manager approval.\nif reader status is 'FROZEN' return book first.\nconfirm activating account? ", null, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		   if(action==0) {
			   try {
				   String command = "33"+reader.getStudent_id()+","+"Active";
				   ConnectionToServer.sendData(this, command);
				   } catch (IOException e) {
					   e.printStackTrace();
				   }
		   }
	   }
		   

	
	
	
	


	@Override
	public void display(Object obj) {
		   String message=(String)obj;
		   List<String> data=Arrays.asList(message.split(","));
		   //if the reader ID is correct
		   //if(data.get(3).equals("Librarian")) {
	
		   if(statusFlag==0) {
			   if(data.get(0).equals("UserIDFound")) {
				   readerName.setText(data.get(1)+" "+data.get(2)); 
				   userStatus=data.get(3);
				   readerStatus.setText(userStatus);
				   if(!(userStatus.equals("Active")&&!(userType.equals("Reader")))) {
					   activate.setVisible(true);
				   }
				   email.setText(data.get(4));
				   phone.setText(data.get(5));
				   statusFlag=1;
				  
			   }
			   else {
				   JOptionPane.showMessageDialog(frame, "reader not found");
			   }
		   }
		   if(statusFlag==2) {
			   if(data.get(0).equals("noData")) {
				   JOptionPane.showMessageDialog(frame, "no history found");
			   }
			   else if(data.get(0).equals("END")){
					table.setItems(tableData);
			   }
			   else {
			   tableData.add(new TableData(data.get(0),data.get(1),data.get(2),data.get(3)));
			   rowCnt++;
			   System.out.println(rowCnt);
			   }
		   }
		   if(data.get(0).equals("Info Updated")) {
			   JOptionPane.showMessageDialog(frame, "Reader information updated");
			   save.setVisible(false);
			   edit.setVisible(true);
			   email.setEditable(false);
			   phone.setEditable(false);
		   }
		   if(data.get(0).equals("Account is Active")) {
			   JOptionPane.showMessageDialog(frame, "Reader status is 'Active'");
			   activate.setVisible(false);
			   readerStatus.setText("Active");
		   }
		   

	}
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//managerTab.setVisible(false);
		UserInformation.setText(LoginController.UserInfo2);   

		bookNameCol.setCellValueFactory(new PropertyValueFactory<TableData,String>("bookName"));
		borrowDateCol.setCellValueFactory(new PropertyValueFactory<TableData,String>("borrowDate"));
		returnDateCol.setCellValueFactory(new PropertyValueFactory<TableData,String>("returnDate"));
		returnStatusCol.setCellValueFactory(new PropertyValueFactory<TableData,String>("returnStatus"));
		String loggedUser=UserInformation.getText();
		email.setEditable(false);
		phone.setEditable(false);
		List<String> logInData=Arrays.asList(loggedUser.split(","));
		userType=logInData.get(1);
		activate.setVisible(false);
		save.setVisible(false);
		if(logInData.get(1).equals("Manager")) {
			managerTab.setVisible(true);
		}
		if(!logInData.get(1).equals("Reader")) {
			readerID.setEditable(true);
		}
		else {//get user id from login
			readerName.setText(logInData.get(0));
			/*try {
			String command = "30"+reader.getStudent_id();
			 ConnectionToServer.sendData(this,command);
			} catch (IOException e) {
				e.printStackTrace();
			}*/
	   }
			
		}
		
	}
	



