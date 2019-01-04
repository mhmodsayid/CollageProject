package entity;

import java.io.Serializable;
import java.util.Arrays;

public class Book implements Serializable  {
	
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		public Book() {
		super();
	}
		private String bookName;
		private String publisherName;
		private String bookEdite;
		private String CatalogNumber;
		private int NumberOFCopies;
		private String PositionOnTheShelf;
		private String bookCatagory;
		private String bookDescription;
		private String datePurchased;
		private String dateOfPrint;
		private String contentTableFileLocation;
		private String bookPhotoFileLocation;
		private byte[]contentfile;
		private byte[]bookphoto;
		public Book(String bookName, String publisherName, String bookEdite, String catalogNumber,
				int numberOFCopies, String positionOnTheShelf, String bookCatagory, String bookDescription,
				String datePurchased, String dateOfPrint, String contentTableFileLocation, String bookPhotoFileLocation,
				byte[] contentfile, byte[] bookphoto) {
			super();
			this.bookName = bookName;
			this.publisherName = publisherName;
			this.bookEdite = bookEdite;
			CatalogNumber = catalogNumber;
			NumberOFCopies = numberOFCopies;
			PositionOnTheShelf = positionOnTheShelf;
			this.bookCatagory = bookCatagory;
			this.bookDescription = bookDescription;
			this.datePurchased = datePurchased;
			this.dateOfPrint = dateOfPrint;
			this.contentTableFileLocation = contentTableFileLocation;
			this.bookPhotoFileLocation = bookPhotoFileLocation;
			this.contentfile = contentfile;
			this.bookphoto = bookphoto;
		}
		public String getBookName() {
			return bookName;
		}
		public void setBookName(String bookName) {
			this.bookName = bookName;
		}
		public String getPublisherName() {
			return publisherName;
		}
		public void setPublisherName(String publisherName) {
			this.publisherName = publisherName;
		}
		public String getBookEdite() {
			return bookEdite;
		}
		public void setBookEdite(String bookEdite) {
			this.bookEdite = bookEdite;
		}
		public String getCatalogNumber() {
			return CatalogNumber;
		}
		public void setCatalogNumber(String catalogNumber) {
			CatalogNumber = catalogNumber;
		}
		public int getNumberOFCopies() {
			return NumberOFCopies;
		}
		public void setNumberOFCopies(int numberOFCopies) {
			NumberOFCopies = numberOFCopies;
		}
		public String getPositionOnTheShelf() {
			return PositionOnTheShelf;
		}
		public void setPositionOnTheShelf(String positionOnTheShelf) {
			PositionOnTheShelf = positionOnTheShelf;
		}
		public String getBookCatagory() {
			return bookCatagory;
		}
		public void setBookCatagory(String bookCatagory) {
			this.bookCatagory = bookCatagory;
		}
		public String getBookDescription() {
			return bookDescription;
		}
		public void setBookDescription(String bookDescription) {
			this.bookDescription = bookDescription;
		}
		public String getDatePurchased() {
			return datePurchased;
		}
		public void setDatePurchased(String datePurchased) {
			this.datePurchased = datePurchased;
		}
		public String getDateOfPrint() {
			return dateOfPrint;
		}
		public void setDateOfPrint(String dateOfPrint) {
			this.dateOfPrint = dateOfPrint;
		}
		public String getContentTableFileLocation() {
			return contentTableFileLocation;
		}
		public void setContentTableFileLocation(String contentTableFileLocation) {
			this.contentTableFileLocation = contentTableFileLocation;
		}
		public String getBookPhotoFileLocation() {
			return bookPhotoFileLocation;
		}
		public void setBookPhotoFileLocation(String bookPhotoFileLocation) {
			this.bookPhotoFileLocation = bookPhotoFileLocation;
		}
		public byte[] getContentfile() {
			return contentfile;
		}
		public void setContentfile(byte[] contentfile) {
			this.contentfile = contentfile;
		}
		public byte[] getBookphoto() {
			return bookphoto;
		}
		public void setBookphoto(byte[] bookphoto) {
			this.bookphoto = bookphoto;
		}
		@Override
		public String toString() {
			return "AddBook [bookName=" + bookName + ", publisherName=" + publisherName + ", bookEdite=" + bookEdite
					+ ", CatalogNumber=" + CatalogNumber + ", NumberOFCopies=" + NumberOFCopies
					+ ", PositionOnTheShelf=" + PositionOnTheShelf + ", bookCatagory=" + bookCatagory
					+ ", bookDescription=" + bookDescription + ", datePurchased=" + datePurchased + ", dateOfPrint="
					+ dateOfPrint + ", contentTableFileLocation=" + contentTableFileLocation
					+ ", bookPhotoFileLocation=" + bookPhotoFileLocation + ", contentfile="
					+ Arrays.toString(contentfile) + ", bookphoto=" + Arrays.toString(bookphoto) + "]";
		}
		
		
	

}
