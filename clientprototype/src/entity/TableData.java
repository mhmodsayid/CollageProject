package entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.WritableObjectValue;

public class TableData {

	private final SimpleStringProperty bookName;
	private final SimpleStringProperty borrowDate;
	private final SimpleStringProperty returnDate;
	private final SimpleStringProperty returnStatus;
	
	public TableData(String bookName, String borrowDate, String returnDate, String borrowStatus) {
		this.bookName=new SimpleStringProperty(bookName);
		this.borrowDate= new SimpleStringProperty(borrowDate);
		this.returnDate=new SimpleStringProperty(returnDate);
		this.returnStatus=new SimpleStringProperty(borrowStatus);
	}
	public String getBookName() { return bookName.get();}
	public void setBookName(String bName) {bookName.set(bName);}
	public String getBorrowDate() { return borrowDate.get();}
	public void setBorrowDate(String bDate) {borrowDate.set(bDate);}
	public String getReturnDate() { return returnDate.get();}
	public void setReturnDate(String rDate) {returnDate.set(rDate);}
	public String getReturnStatus() { return returnStatus.get();}
	public void setReturnStatus(String bStatus) {returnStatus.set(bStatus);}
	
	


	
	
	

}
