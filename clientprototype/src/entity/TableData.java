package entity;

import javafx.beans.property.SimpleStringProperty;

public class TableData<StringProperty> {
	private SimpleStringProperty BookName;
	private SimpleStringProperty BorrowDate;
	private SimpleStringProperty ReturnDate;
	private SimpleStringProperty ReturnStatus;
	
	public TableData(String BookName, String BorrowDate, String ReturnDate, String ReturnStatus) {
		this.BookName=new SimpleStringProperty( BookName);
		this.BorrowDate=new SimpleStringProperty(BorrowDate);
		this.ReturnDate=new SimpleStringProperty(ReturnDate);
		this.ReturnStatus=new SimpleStringProperty(ReturnStatus);
		
		
	}
	public String getBookName() {
		return BookName.get();
	}
	public StringProperty BookNameProperty() {
		return (StringProperty) BookName;
	}
	public String getBorrowDate() {
		return BorrowDate.get();
	}
	public StringProperty BorrowDateProperty() {
		return (StringProperty) BorrowDate;
	}
	public String getReturnDate() {
		return ReturnDate.get();
	}
	public StringProperty ReturnDateProperty() {
		return (StringProperty) ReturnDate;
	}
	public String getReturnstatus() {
		return ReturnStatus.get();
	}
	public StringProperty ReturnStatusProperty() {
		return (StringProperty) ReturnStatus;
	}
	

}
