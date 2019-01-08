package gui;

import java.time.LocalDate;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class BorrowBookControler  {

    @FXML
    private TextField ReaderName;

    @FXML
    private Button Confirm;

    @FXML
    private TextField BookCatalogNumber;

    @FXML
    private TextField ReaderID;

    @FXML
    private DatePicker BorrowDate;

    @FXML
    private DatePicker ReturnDate;

    @FXML
    private Button ClearALL;

    JOptionPane frame;
    
    private String readerID=null;
    private String readerName=null;
    private String bookCatalogNumber=null;
    private LocalDate borrowDate;
    private LocalDate returnDate;

  
    public void UpdateStatus() {
    	 readerID=ReaderID.getText();
    	 readerName = ReaderName.getText();
    	 bookCatalogNumber = BookCatalogNumber.getText();
    	 borrowDate = BorrowDate.getValue();
    	 returnDate = ReturnDate.getValue();
    	System.out.println(readerID+" "+readerName+" "+bookCatalogNumber+" "+borrowDate+" "+returnDate);
    }
   public void ClearAll(ActionEvent event) {
	   ReaderID.setText(null);
	   ReaderName.setText(null);
	   BookCatalogNumber.setText(null);
	   BorrowDate.setValue(null);
	   ReturnDate.setValue(null);

   }
   public void Confirm_Massege(ActionEvent event) {
	   if (readerID==null || readerName == null || BookCatalogNumber==null)
			JOptionPane.showMessageDialog(frame, "please fill the fields");
	/*	else {
			try {
				ConnectionToServer.sendData(this, command);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}*/
   }
}

