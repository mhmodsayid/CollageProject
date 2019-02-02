package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import controller.ConnectionToServer;
import entity.BorrowReportTable;
import entity.Reader;
import entity.ReportData;
import entity.TableData;
import entity.TableWorker;
import gui.LoginController;
import gui.NavigationBar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import ocsf.client.ChatIF;


public class ReaderCardController extends NavigationBar implements Initializable, ChatIF {
	@FXML
	private TextField readerID;
	@FXML
	private TextArea DecimalTotal;
    @FXML
    private TextArea DecimalRegular;

    @FXML
    private TextArea DecimalIndemand;
	@FXML
	private TextField readerName;
	@FXML
	private TextField readerStatus;
	@FXML
	private Button activate;
	@FXML
	private TextField email;
	@FXML
	private TextField phone;
	@FXML
	private Button edit;
	@FXML
	private Button search1;
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
    private TableColumn dateCol;

    @FXML
    private TableColumn descriptionCol;

    @FXML
    private TableColumn otherCol;
    @FXML
    private Pane lateReportPane;
	@FXML
	private Pane managerTab;
	
	
    @FXML
    private Pane WorkerDetails;

    @FXML
    private Pane ActionReport;

    @FXML
    private Pane BorrowReport;
    @FXML
    private AnchorPane cardReader;

    @FXML
    private Button card;

    @FXML
    private Button worker;

    @FXML
    private Button action;

    @FXML
    private Button borrow;
    
    @FXML
    private Button closeall;
    @FXML
    private Text totalDecimal;

    @FXML
    private TableView<ReportData> lateStatistic;

    @FXML
    private TableColumn<ReportData,String> bookcol;

    @FXML
    private TableColumn<ReportData,String> averagecol;

    @FXML
    private TableColumn<ReportData,String> mediancol;

    @FXML
    private TableColumn decimalcol;

    @FXML
    private Text numOflateReturns;

    @FXML
    private Text totalAverage;

    @FXML
    private Text totalMedian;
    
    
    
    
    @FXML
    private TableView<BorrowReportTable> BorrowTableView;

    @FXML
    private TableColumn col1;

    @FXML
    private TableColumn col2;

    
    
    
    
    @FXML
    private TableView<TableWorker> WorkerTable;

    @FXML
    private TableColumn firstnamecol;
    @FXML
    private TableColumn lastnamecol;

    @FXML
    private TableColumn useridcol;

    @FXML
    private TableColumn emailcol;

    @FXML
    private TableColumn phonecol;

    @FXML
    private TableColumn usertypecol;

    @FXML
    private TableColumn userstatuscol;
    
    @FXML
    private Text currentDate;

    @FXML
    private Text AvailableSubscription1;

    @FXML
    private Text UnavailableSubscription1;

    @FXML
    private Text LockedSubscription1;

    @FXML
    private Text CopiesNumber1;

    @FXML
    private Text LateSubscription1;
    
    @FXML
    private TextField Year;
    @FXML
    private TextField Month;

    @FXML
    private ComboBox<String> monthBorrowReport1;

    @FXML
    private Text AvailableSubscription11;

    @FXML
    private Text UnavailableSubscription11;

    @FXML
    private Text LockedSubscription11;

    @FXML
    private Text CopiesNumber11;

    @FXML
    private Text LateSubscription11;
    
    @FXML
    private Button ShowActionsReport;
    
    public int 	clickflag=0;
    @FXML
    private ButtonBar ReaderMenu;
   
    @FXML
    private ButtonBar WorkerMenu;
    
    @FXML
    private Button searchRegular;

    @FXML
    private Text quantityRegular;

    @FXML
    private Text averageRegular;

    @FXML
    private Text medianRegular;

    @FXML
    private Text decimalRegular;
    

    @FXML
    private Text quantityIndemand;

    @FXML
    private Text averageIndemand;

    @FXML
    private Text medianIndemand;

    @FXML
    private Text decimalIndemand;

    @FXML
    private TextField monthBorrowReport;

    @FXML
    private TextField yearBorrowReport;
    
    Entry<Integer, Integer> value;
    

    @FXML
    private Text key;
    @FXML
    private Text value1;

    @FXML
    private Text key7;

    @FXML
    private Text value7;
    
    @FXML
    private Button reportbtn;

    @FXML
    private TextField month;

    @FXML
    private TextField year;

	JOptionPane frame;
	Reader reader;
	int statusFlag=0;
	int rowCnt=0;
	public static String userType;
	public static String userStatus;
  
	private final ObservableList<TableData> tableData=FXCollections.observableArrayList();//history table view
	private final ObservableList<TableWorker> workerTable=FXCollections.observableArrayList();//worker report table view
	private final ObservableList<ReportData> lateDataStats=FXCollections.observableArrayList();//late report table view
	ObservableList<String> listMonth = FXCollections.observableArrayList("01","02","03","04","05","06","07","08","09","10","11","12");
	ObservableList<String> listMonth1 = FXCollections.observableArrayList("01","02","03","04","05","06","07","08","09","10","11","12");
	private String combobox11;
	private String combobox12;

	
    /**
     * report button in late report is pressed.
     * gather the year and month input from user and send to case 40 
     * in the server. check if the input is correct
     * @param event
     * @author bayan
     */
    @FXML
    void LateReport(ActionEvent event) {
 
      	statusFlag=7;
      	lateDataStats.clear();
  	  if(year.getText().equals("")&&month.getText().equals("")) 
			JOptionPane.showMessageDialog(frame,"You must fill in month and year");	    			
	    	else {
	    		if(year.getText().equals("")) 
	    			JOptionPane.showMessageDialog(frame,"You must fill a year field");
	    		else {
	    				if(month.getText().equals("")) 
	    					JOptionPane.showMessageDialog(frame,"You must fill a month field");
	    				else {
	  	    				if(month.getText().length()!=2||month.getText().matches("[a-zA-Z]*")) 
	  	    					JOptionPane.showMessageDialog(frame,"Month field must contain exactly 2 digits");
	  	    				else {
		  	    				if(year.getText().length()!=4||year.getText().matches("[a-zA-Z]*")) 
		  	    					JOptionPane.showMessageDialog(frame,"Year field must contain exactly 2 digits");
	  	    				
	  	    				
	  	    				}
	    				}
	    			}
	    	}
  	  try {
  		  String command="40"+year.getText()+","+month.getText();
  		  ConnectionToServer.sendData(this, command);
  		  
  	  }catch(IOException e) {
  		  e.printStackTrace();
  	  }
    }
	
    
    /**
     * if the show report is pressed in the actions report
     * gather the year and month input from the user and send to case 39 in the server.
     * check if the input is correct
     * @param event
     * @author bayan
     */
    @FXML
    void ShowActionsReport1(ActionEvent event) {
      	String year=Year.getText();
      	String month=Month.getText();
      	statusFlag=6;
    	  if(Year.getText().equals("")&&Month.getText().equals("")) 
  			JOptionPane.showMessageDialog(frame,"You must fill in month and year");	    			
  	    	else {
  	    		if(Year.getText().equals("")) 
  	    			JOptionPane.showMessageDialog(frame,"You must fill a year field");
  	    		else {
	    				if(Month.getText().equals("")) 
	    					JOptionPane.showMessageDialog(frame,"You must fill a month field");
	    				else {
	  	    				if(Month.getText().length()!=2||Month.getText().matches("[a-zA-Z]*")) 
	  	    					JOptionPane.showMessageDialog(frame,"Month field must contain exactly 2 digits");
	  	    				else {
		  	    				if(Year.getText().length()!=4||Year.getText().matches("[a-zA-Z]*")) 
		  	    					JOptionPane.showMessageDialog(frame,"Year field must contain exactly 2 digits");
	  	    				
	  	    				
	  	    				}
	    				}
  	    			}
  	    	}
    		try {
          		String command="39"+year+","+month;
    			ConnectionToServer.sendData(this, command);
    			   } catch (IOException e) {
    				   e.printStackTrace();
    			   }
    }
    
    
    /**
     * click on the actions report button.
     * send to case 36 in the server which will return the current data.
     * @param event
     * @author bayan
     */
    @FXML
    public void ActionReportBtn(ActionEvent event) {
		LocalDate today=LocalDate.now();
		currentDate.setText(today.format(DateTimeFormatter.ofPattern("yyy-MM-dd")));
    	WorkerDetails.setVisible(false);
	    ActionReport .setVisible(true);
	    BorrowReport .setVisible(false);
	    cardReader   .setVisible(false);
	    lateReportPane.setVisible(false);
		statusFlag=5;

	    try {
			   String command = "36";
			   ConnectionToServer.sendData(this, command);
			   } catch (IOException e) {
				   e.printStackTrace();
			   }
    }
    
    /**
     * press the late report button. 
     * show the late report and hide the others
     * @param event
     */
    @FXML
    public void LateReportBtn(ActionEvent event) {
		WorkerDetails.setVisible(false);
	    ActionReport .setVisible(false);
	    BorrowReport .setVisible(false);
	    cardReader   .setVisible(false);
	    lateReportPane.setVisible(true);
    }
    
    
    /**
     * press the Reader card  button. 
     * show the reader card and hide the others
     * @param event
     */
    @FXML
    public void CardReaderBtn(ActionEvent event) {
		WorkerDetails.setVisible(false);
	    ActionReport .setVisible(false);
	    BorrowReport .setVisible(false);
	    cardReader   .setVisible(true);
	    lateReportPane.setVisible(false);
    }

    /**
     * press the worker details button. 
     * send to server case 34 which will return the workers detailes.
     * @param event
     */
    @FXML
    public void WorkerDetailsBtn(ActionEvent event) {

    	WorkerDetails.setVisible(true);
	    ActionReport .setVisible(false);
	    BorrowReport .setVisible(false);
	    cardReader   .setVisible(false);
	    lateReportPane.setVisible(false);
	   if( clickflag==0) {
		statusFlag=4;
		   try {
		   String command = "34";
		   ConnectionToServer.sendData(this, command);
		   } catch (IOException e) {
			   e.printStackTrace();
		   }
		   clickflag=1;
	   }
	    
    
    }
    
    /**
     * close all button pressed.
     * closes all the panes.
     * @param event
     */
    @FXML
    public void CloseAll(ActionEvent event) {
		WorkerDetails.setVisible(false);
	    ActionReport .setVisible(false);
	    BorrowReport .setVisible(false);
	    cardReader   .setVisible(false);
	    lateReportPane.setVisible(false);
    
    }
    

	

	
    /**
     * clear all fields in reader card
     * @param event
     * @author bayan
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

    /**
     * press the borrow's report button. 
     * show the borrow report and hide the others
     * @param event
     */
    @FXML
    public void BorrowReportBtn(ActionEvent event) {
		WorkerDetails.setVisible(false);
	    ActionReport .setVisible(false);
	    BorrowReport .setVisible(true);
	    cardReader   .setVisible(false); 
	    lateReportPane.setVisible(false);
    }
    
    public boolean isNumeric1(String str)  
   	{  		  

   	  try  
   	  {  
   		double d = Double.parseDouble(str);  
   	  }  
   	  catch(NumberFormatException nfe)  
   	  {  
   	    return false;  
   	  }  
   	  return true;  
   	}
    
    /**
     * this func for search for the borrowReport
     * @author Ammar khutba
     */
       @FXML
       void BorrowSearch(ActionEvent event) {
       	if(monthBorrowReport.getText().equals("")||yearBorrowReport.getText().equals("")) {
       		JOptionPane.showMessageDialog(frame,"You must fill in month and year");
       	}
       	else if(isNumeric1(monthBorrowReport.getText())==false||isNumeric1(yearBorrowReport.getText())==false) {
       		JOptionPane.showMessageDialog(frame,"You must fill in month and year currect");
       	}
       	else {
       		statusFlag=8;

       	    try {  	    	
       			   String command = "50"+","+monthBorrowReport.getText()+","+yearBorrowReport.getText();
       			   ConnectionToServer.sendData(this, command);
       			   } catch (IOException e) {
       				   e.printStackTrace();
       			   }
       	}
       }

	
   	/**
   	 * get the entered id and send to server case 30.
   	 * check if an ID was entered
   	 * @param event
   	 * @author bayan
   	 */
	public void ReaderCardInfo(ActionEvent event) {
		statusFlag=0;

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
       @FXML

	   /**
	    * Reader Card:
	    * show history button is pressed, check that id was entered
	    * and send to server case 31 that will return the history for the id.
	    * @param event
	    * @author bayan
	    */
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
       
	   
	   /**
	    * Reader Card:
	    * edit info button is pressed, check that id was entered
	    * change the email and phone text fields to editable.
	    * save button is visible.
	    * @param event
	    * @author bayan
	    */
       @FXML

	   public void EditInfo(ActionEvent event) {
		   if(statusFlag==1) {
		   edit.setVisible(false);
		   save.setVisible(true);
		   email.setEditable(true);
		   phone.setEditable(true);
		   }
		   else {
			   JOptionPane.showMessageDialog(frame, "Enter reader ID and press Search");
		   }
		   
	   }
       
	   /**
	    * Reader Card:
	    * save info button was pressed.
	    * check that the fields are not empty and send to 
	    * server case 32 that will update the details. 
	    * @param event
	    * @author bayan
	    */
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
	   
	   /**
	    * Reader Card:
	    * if librarian or manager is logged in, check if the searched id
	    * is frozen or blocked the show the activate button.
	    * press on activate send details to server case 33 that will update
	    * the user status
	    * @param event
	    * @author bayan
	    */ 
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
	   /**
	    *  function to sort hashmap by values 
	    * @author Ammar khutb
	    * @param hmRegular the map to sort
	    * @return sorted map
	    */
	    public  HashMap<Integer, Integer> sortByValue(Map<Integer, Integer> hmRegular) 
	    { 
	        // Create a list from elements of HashMap 
	        List<Map.Entry<Integer, Integer> > list = 
	               new LinkedList<Map.Entry<Integer, Integer> >(hmRegular.entrySet()); 
	  
	        // Sort the list 
	        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer> >() { 
	            public int compare(Map.Entry<Integer, Integer> o1,  
	                               Map.Entry<Integer, Integer> o2) 
	            { 
	                return (o1.getValue()).compareTo(o2.getValue()); 
	            } 
	        }); 
	        value = list.get((list.size()/2));
	        // put data from sorted list to hashmap  
	        HashMap<Integer, Integer> temp = new LinkedHashMap<Integer, Integer>(); 
	        for (Map.Entry<Integer, Integer> aa : list) { 
	            temp.put(aa.getKey(), aa.getValue()); 
	        } 
	        return temp; 
	    } 
	    /**
	     * handle answer from server.
	     * enter to cases according to statusFlag or data.get(0) value.
		 * if statusFlag=0 handle reader id search.
		 * if statusFlag=2 handle the data from server to populate the history tableview
		 * data.get(0).equals("Info Updated") show message that reader info was updates.
		 *  if(data.get(0).equals("Account is Active")) show message that the account was activated and change status to active
		 *  if statusFlag=4 populate worker detatils table with data from server.
		 *  if statusFlag=5, set text the current report on the actionsReport.
		 *  if statusFlag=7, set text to the saved information report on the actions report.
		 *  if statusFlag=7, populate table and fields on the late report page.
	     * @author khutb statusFlag==8
	     * in this statusFlag case we work on the boorowStatistic the quantity of the books from the Indemand and not Indemand
	     * and the AVG for Indemand and not Indemand
	     * and the miden for Indemand and not Indemand
	     * and the decimal distribution for Indemand and not Indemand
	     */
	@Override
	public void display(Object obj) {
		   String message=(String)obj;
		   List<String> data=Arrays.asList(message.split(","));
	
		   if(statusFlag==0) {
			   if(data.get(0).equals("UserIDFound")) {
				   readerName.setText(data.get(1)+" "+data.get(2)); 
				   userStatus=data.get(3);
				   readerStatus.setText(userStatus);
				   if(!(userStatus.equals("Active"))&&!LoginController.userType2.equals("Reader")) {
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
			   else {
				   int rowCnt=Integer.parseInt(data.get(0));
				   int whileCnt=1;
				   while(whileCnt<(rowCnt*4+1)) {
				   if(data.get(whileCnt+1).equals("borrowed")||data.get(whileCnt+1).equals("Returned")||data.get(whileCnt+1).equals("ordered")) {
					   if(!data.get(whileCnt+3).equals("0"))
						   tableData.add(new TableData(data.get(whileCnt),(data.get(whileCnt+1)+" "+data.get(whileCnt+2)),"late return"));
					   else
						   tableData.add(new TableData(data.get(whileCnt),(data.get(whileCnt+1)+" "+data.get(whileCnt+2)),""));
				   }
				   else {
					   tableData.add(new TableData(data.get(whileCnt),data.get(whileCnt+1)+ " "+data.get(whileCnt+2),""));
				   }
				   whileCnt+=4;
				   }
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
		   
		   if(statusFlag==4) {
			  workerTable.add(new TableWorker(data.get(0),data.get(1),data.get(2),data.get(3),data.get(4),data.get(5),data.get(6)));
			   }
		    
		   if(statusFlag==5) {
			   AvailableSubscription1.setText(data.get(0));
			   UnavailableSubscription1.setText(data.get(1));
			   LockedSubscription1.setText(data.get(2));
			   CopiesNumber1.setText(data.get(3));
			   LateSubscription1.setText(data.get(4));
			   }
		   
		   if(statusFlag==6) {
			   if(data.get(0).equals("ReportNotFound")) {
				 	JOptionPane.showMessageDialog(frame, "Report Not Found");
					   AvailableSubscription11.setText("---");
					   UnavailableSubscription11.setText("---");
					   LockedSubscription11.setText("---");
					   CopiesNumber11.setText("---");
					   LateSubscription11.setText("---");
			   }
			   else {
			   AvailableSubscription11.setText(data.get(0));
			   UnavailableSubscription11.setText(data.get(1));
			   LockedSubscription11.setText(data.get(2));
			   CopiesNumber11.setText(data.get(3));
			   LateSubscription11.setText(data.get(4));
		   }
		   }
		   
		   if(statusFlag==7) {
			   int rowCnt=Integer.parseInt(data.get(4));
			   if(data.get(1).equals("null")) {
				   totalAverage.setText("");
				   totalMedian.setText("");
				   //totalDecimal.setText("");
				   lateDataStats.clear();
				   JOptionPane.showMessageDialog(frame, "No data available for the selected month");
			   }
			   else if(data.get(0).equals("total")||!(data.get(1).equals(null))) {
				   totalAverage.setText(data.get(1));
				   totalMedian.setText(data.get(2));
				   //totalDecimal.setText(data.get(3));
				   int whileCnt=5;
				   while(whileCnt<(rowCnt*4+5)) {
					   if(!data.get(whileCnt+1).equals(null))
						   lateDataStats.add(new ReportData(data.get(whileCnt),data.get(whileCnt+1),data.get(whileCnt+2)));
						   whileCnt+=4;
				   }
				   String Decimal ="";
				   int i=0;
				   int cnt=0;
				   int x;
				   double decimal=0;
				   whileCnt+=1;
				   Map< Integer,Integer> totalhm =  new HashMap< Integer,Integer>(); //this map is for the total decimal distribution
				   while(cnt<rowCnt) {
					   //then we insert to map to calculator the static 
					   if(totalhm.containsKey(Integer.parseInt(data.get(whileCnt)))){//if there the same key 
						   x=totalhm.get(Integer.parseInt(data.get(whileCnt)));//get the value
						   totalhm.put(Integer.parseInt(data.get(whileCnt)),x+1);//and add 1 to the value
					   }
					   else {
						   totalhm.put(Integer.parseInt(data.get(whileCnt)), 1);//put the new key with value = 1

					   }
					   whileCnt++;
					   cnt++;
					   i++;
					   

				   }
				   Map<Integer, Integer> hm2 = sortByValue(totalhm);//sort the map

			        for (Entry<Integer, Integer> entry1 : totalhm.entrySet())  {
			        	double x1 =entry1.getValue();
			        	decimal = x1/i;
			        	Decimal+="the number of late days :"+entry1.getKey().toString()+"  the Distribution : "+String.format ("%.3f", decimal)+"\n";
			        }
			        DecimalTotal.setText(Decimal);
					
			   }
	
		   }
		   if(statusFlag==8) {
			   String RegularDecimal = "";
			   String IndemandDecimal ="";
			   int i=0;
			   int cnt=0;
			   int x;
			   double decimal=0; 
			   double AvgRegular=0;
			   double AvgIndemand=0;
			   Map< Integer,Integer> hmRegular =  new HashMap< Integer,Integer>(); //this map is for the Regular demand book
			   Map< Integer,Integer> hmIndemand =  new HashMap< Integer,Integer>(); //this map is for the Indemand book
			   while(!data.get(i).equals("Indemand")) {//the client receved  array of a time borrow Period of string form the server and the array is divided into two Sections
				   //the first Section is for the avileble books and the second Section is for the Indemand books 
				   //then we insert to map to calculator the static 
				   if(hmRegular.containsKey(Integer.parseInt(data.get(i)))){//if there the same key 
					   x=hmRegular.get(Integer.parseInt(data.get(i)));//get the value
					   hmRegular.put(Integer.parseInt(data.get(i)),x+1);//and add 1 to the value
				   }
				   else
					   hmRegular.put(Integer.parseInt(data.get(i)), 1);//put the new key with value = 1
				   
				   AvgRegular+=Integer.parseInt(data.get(i));//get the sum of all borrow Period time
				   i++;
			   }
			   Map<Integer, Integer> hm1 = sortByValue(hmRegular);//sort the map
			   AvgRegular =  AvgRegular/i;//the avg
			   quantityRegular.setText(String.valueOf(i));
			   averageRegular.setText(String.format ("%.3f", AvgRegular));
			   medianRegular.setText(Long.toString(value.getValue()));//we get the medin value from the sorted map 
		        for (Entry<Integer, Integer> entry : hmRegular.entrySet())  {
		        	double x1 =entry.getValue();
		        	decimal = x1/i;
		        	RegularDecimal+="the number of the day :"+entry.getKey().toString()+"  the Distribution : "+String.format ("%.3f", decimal)+"\n";
		        }
		        DecimalRegular.setText(RegularDecimal);
				   i++; 
			   for (int j = i; j < data.size(); j++,cnt++) {
				   if(hmIndemand.containsKey(Integer.parseInt(data.get(j)))){
					   x=hmIndemand.get(Integer.parseInt(data.get(j)));
					   hmIndemand.put(Integer.parseInt(data.get(j)), x+1); 
				   }
				   else
					   hmIndemand.put(Integer.parseInt(data.get(j)), 1);
				   AvgIndemand+=Integer.parseInt(data.get(j));
			}
			   Map<Integer, Integer> hm2 = sortByValue(hmIndemand);
			   AvgIndemand=AvgIndemand/cnt;
			   quantityIndemand.setText(String.valueOf(cnt));
			   averageIndemand.setText(String.format ("%.3f", AvgIndemand));
			   medianIndemand.setText(Long.toString(value.getValue()));
		        for (Entry<Integer, Integer> entry : hmIndemand.entrySet()) {
		        	double x1 =entry.getValue();
		        	decimal = x1/cnt;
		        	IndemandDecimal+="the number of the day :"+entry.getKey().toString()+"  the Distribution : "+String.format ("%.3f", decimal)+"\n";
		        }	
		        DecimalIndemand.setText(IndemandDecimal);
		}
		        
		        
		}
	
	/**
	 * initialize info at start.
	 * get user type and shoe buttons accordingly.
	 * initialize table views columns.
	 * if reader is logged in send data to server case 30 to ge reader details.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		if(LoginController.userType2.equals("Reader")) {
				UserInformation.setText(LoginController.UserInfo2);

		WorkerMenu.setVisible(false);
		ReaderMenu.setVisible(true);

		}
		else 
			if(LoginController.userType2.equals("Librarian")||LoginController.userType2.equals("Manager")) {
				UserInformation.setText(LoginController.UserInfo2);

			WorkerMenu.setVisible(true);
			ReaderMenu.setVisible(false);
		}
		clickflag=0;
		firstnamecol.setCellValueFactory(new PropertyValueFactory<TableData,String>("firstName"));
		lastnamecol.setCellValueFactory(new PropertyValueFactory<TableData,String>("lastName"));
		useridcol.setCellValueFactory(new PropertyValueFactory<TableData,String>("userID"));
		emailcol.setCellValueFactory(new PropertyValueFactory<TableData,String>("email"));
		phonecol.setCellValueFactory(new PropertyValueFactory<TableData,String>("phone"));
		usertypecol.setCellValueFactory(new PropertyValueFactory<TableData,String>("userType"));
		userstatuscol.setCellValueFactory(new PropertyValueFactory<TableData,String>("userStatus"));
		dateCol.setCellValueFactory(new PropertyValueFactory<TableData,String>("date"));
		descriptionCol.setCellValueFactory(new PropertyValueFactory<TableData,String>("description"));
		otherCol.setCellValueFactory(new PropertyValueFactory<TableData,String>("returnStatus"));
		
		bookcol.setCellValueFactory(new PropertyValueFactory<ReportData,String>("bookName"));
		averagecol.setCellValueFactory(new PropertyValueFactory<ReportData,String>("average"));
		mediancol.setCellValueFactory(new PropertyValueFactory<ReportData,String>("median"));
		//decimalcol.setCellValueFactory(new PropertyValueFactory<ReportData,String>("decimal"));
		

		

		lateStatistic.setItems(lateDataStats);
		table.setItems(tableData);
		WorkerTable.setItems(workerTable);
		
		UserInformation.setText(LoginController.UserInfo2);	
		UserInformation.textProperty().unbindBidirectional(LoginController.UserInfo2);
		WorkerDetails.setVisible(false);
	    ActionReport .setVisible(false);
	    BorrowReport .setVisible(false);
	    cardReader   .setVisible(false);
	    lateReportPane.setVisible(false);

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
		
		if(!LoginController.userType2.equals("Reader")) {
			readerID.setEditable(true);
			readerStatus.setEditable(false);
			readerName.setEditable(false);
		}
		
		else if(LoginController.userType2.equals("Reader")) {//get user id from login
			card.setVisible(false);
			worker .setVisible(false);
			action .setVisible(false);
			borrow   .setVisible(false);
			closeall .setVisible(false);
		    
			activate.setVisible(false);
			readerName.setEditable(false);
			readerStatus.setEditable(false);
			clearAll.setVisible(false);
			search1.setVisible(false);
		    cardReader.setVisible(true);
			readerID.setText(LoginController.userID2);
			readerID.setEditable(false);
			System.out.println("new");

			if(LoginController.userStatus2.equals("Frozen")) {
				edit.setVisible(false);
				activate.setVisible(false);

			}
			try {
			String command = "30"+LoginController.userID2;
			 ConnectionToServer.sendData(this,command);
			} catch (IOException e) {
				e.printStackTrace();
			}
	   }
			
	
	
		}
	}
	



