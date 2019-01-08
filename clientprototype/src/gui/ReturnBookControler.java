package gui;


import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;

public class ReturnBookControler {

    @FXML
    private MenuButton BookStatus;

    @FXML
    private Button ClearAll;

    @FXML
    private Button Return;

    @FXML
    private TextField BookID;

    @FXML
    private TextField BookName;

    @FXML
    private TextField Category;
    
    JOptionPane frame;
    
    private String bookID=null;
    private String bookName=null;
    private String category=null;
    private String bookStatus=null;
    
    public void UpdateStatus() {
    	bookID = BookID.getText();
    	bookName = BookName.getText();
    	category = Category.getText();
    	bookStatus = BookStatus.toString();
   }
    public void ClearAll(ActionEvent event) {
    	BookID.setText(null);
    	BookName.setText(null);
    	Category.setText(null);
    }
    public void Confirm_Massege(ActionEvent event) {
    	if (bookID==null || bookName == null||category==null||bookStatus==null)
			JOptionPane.showMessageDialog(frame, "please fill the fields");
		/*else {
			try {
				ConnectionToServer.sendData(this, command);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}*/
    }

}