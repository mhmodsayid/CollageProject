package entity;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.WritableObjectValue;

/**
 * store the late report data from reader card.
 * the class gets four parameters and stores them in the builder which is stored in an observable string array 
 * to show the data in a table view
 * @author bayan
 *
 */

public class ReportData {






		private final SimpleStringProperty bookName;
		private final SimpleStringProperty average;
		private final SimpleStringProperty median;
		
		public ReportData(String bookName, String average, String median) {
			this.bookName=new SimpleStringProperty(bookName);
			this.average= new SimpleStringProperty(average);
			this.median=new SimpleStringProperty(median);

		}
		public String getBookName() { return bookName.get();}
		public void setBookName(String bName) {bookName.set(bName);}
		public String getAverage() { return average.get();}
		public void setAverage(String newAverage) {average.set(newAverage);}
		public String getMedian() { return median.get();}
		public void setMedian(String newMedian) {median.set(newMedian);}

		
		


		


}
