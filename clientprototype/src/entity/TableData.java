package entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.WritableObjectValue;

/**
 * store the history report data from reader card.
 * the class gets three parameters and stores them in the builder which is stored in an observable string array 
 * to show the data in a table view
 * @author bayan
 *
 */
public class TableData {

	private final SimpleStringProperty date;
	private final SimpleStringProperty description;
	private final SimpleStringProperty returnStatus;
	
	public TableData(String date, String description, String returnStatus) {
		this.date=new SimpleStringProperty(date);
		this.description= new SimpleStringProperty(description);
		this.returnStatus=new SimpleStringProperty(returnStatus);
	}
	public String getDate() { return date.get();}
	public void setDate(String date1) {date.set(date1);}
	public String getDescription() { return description.get();}
	public void setDescription(String description1) {description.set(description1);}
	public String getReturnStatus() { return returnStatus.get();}
	public void setReturnStatus(String rStatus) {returnStatus.set(rStatus);}
	
	


	
	
	

}
