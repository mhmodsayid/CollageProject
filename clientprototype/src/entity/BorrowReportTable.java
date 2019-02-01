package entity;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.WritableObjectValue;

public class BorrowReportTable {


	private final SimpleStringProperty colm1;
	private final SimpleStringProperty colm2;
	
	public BorrowReportTable(String decimal, String decimal2) {
		this.colm1=new SimpleStringProperty(decimal);
		this.colm2= new SimpleStringProperty(decimal2);
		
	}
	public String getcolm1() { return colm1.get();}
	public void setcolm1(String c1) {colm1.set(c1);}
	public String getcolm2() { return colm2.get();}
	public void setcolm2(String c2) {colm2.set(c2);}
	}