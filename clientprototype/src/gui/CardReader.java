package gui;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import ocsf.client.ChatIF;

public class CardReader extends NavigationBar implements Initializable, ChatIF  {



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
    public void ActionReportBtn(ActionEvent event) {
		WorkerDetails.setVisible(false);
	    ActionReport .setVisible(true);
	    BorrowReport .setVisible(false);
	    cardReader   .setVisible(false);
	    
		   AvailableSubscription1.setText("1");
		   UnavailableSubscription1.setText("2");
		   LockedSubscription1.setText("3");
		   CopiesNumber1.setText("4");
		   LateSubscription1.setText("5");
    }

    @FXML
    public void BorrowReportBtn(ActionEvent event) {
		WorkerDetails.setVisible(false);
	    ActionReport .setVisible(false);
	    BorrowReport .setVisible(true);
	    cardReader   .setVisible(false);
    }

    @FXML
    public void CardReaderBtn(ActionEvent event) {
		WorkerDetails.setVisible(false);
	    ActionReport .setVisible(false);
	    BorrowReport .setVisible(false);
	    cardReader   .setVisible(true);
    }

    @FXML
    public void WorkerDetailsBtn(ActionEvent event) {
		WorkerDetails.setVisible(true);
	    ActionReport .setVisible(false);
	    BorrowReport .setVisible(false);
	    cardReader   .setVisible(false);
    
    }
    @FXML
    public void CloseAll(ActionEvent event) {
		WorkerDetails.setVisible(false);
	    ActionReport .setVisible(false);
	    BorrowReport .setVisible(false);
	    cardReader   .setVisible(false);
    
    }


	@Override
	public void display(Object msg) {

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
			WorkerDetails.setVisible(false);
		    ActionReport .setVisible(false);
		    BorrowReport .setVisible(false);
		    cardReader   .setVisible(false);
	}

}
