package entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;

public class Book implements Serializable  {
	static Book book;
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		public Book() {
		super();
	}
		private String bookName;
		private String publisherName;
		private int bookEdite;
		private String CatalogNumber;
		private int NumberOFCopies=0;
		private String PositionOnTheShelf;
		private String bookCatagory;
		private String bookDescription;
		private String datePurchased;
		private String dateOfPrint;
		private byte[]contentfile;
		private byte[]bookphoto;
		private int bookStatus=0;
		
		
		public Book(String bookName, String publisherName, int bookEdite, String catalogNumber, int numberOFCopies,
				String positionOnTheShelf, String bookCatagory, String bookDescription, String datePurchased,
				String dateOfPrint, byte[] contentfile,
				byte[] bookphoto, int bookStatus) {
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
			
			this.contentfile = contentfile;
			this.bookphoto = bookphoto;
			this.bookStatus = bookStatus;
		}
		public String getBookName() {
			return bookName;
		}
		public void setBookName(String bookName) throws Exception {
			if(bookName.equals(""))
				throw new Exception("Please insert book name");
			this.bookName = bookName;
		}
		public String getPublisherName() {
			return publisherName;
		}
		public void setPublisherName(String publisherName)throws Exception {
			if(publisherName.equals(""))
				throw new Exception("Please insert publisher name");
			this.publisherName = publisherName;
		}
		public int getBookEdite() {
			return bookEdite;
		}
		public void setBookEdite(String bookEdite)throws Exception {
			try {
				this.bookEdite= Integer.parseInt(bookEdite);
			} catch (NumberFormatException e) {
				throw new Exception("please put a number in book edite");
			}
			
		}
		public String getCatalogNumber() {
			return CatalogNumber;
		}
		public void setCatalogNumber(String catalogNumber) throws Exception{
			if(catalogNumber.equals(""))
				throw new Exception("Please insert catalog number");
			CatalogNumber = catalogNumber;
		}
		public int getNumberOFCopies() {
			return NumberOFCopies;
		}
		public void setNumberOFCopies(String numberOFCopies) throws Exception {
			try {
				NumberOFCopies = Integer.parseInt(numberOFCopies);
			} catch (NumberFormatException e) {
				throw new Exception("please put a number in Number Of Copyes fild");
			}
		}
		public String getPositionOnTheShelf() {
			return PositionOnTheShelf;
		}
		public void setPositionOnTheShelf(String positionOnTheShelf) throws Exception{
			if(positionOnTheShelf.equals(""))
				throw new Exception("Please insert book position on the shelf");
			PositionOnTheShelf = positionOnTheShelf;
		}
		public String getBookCatagory() {
			return bookCatagory;
		}
		public void setBookCatagory(String bookCatagory)throws Exception {
			if(bookCatagory.equals(""))
				throw new Exception("Please insert book catagory");
			this.bookCatagory = bookCatagory;
		}
		public String getBookDescription() {
			return bookDescription;
		}
		public void setBookDescription(String bookDescription)throws Exception {
			if(bookDescription.equals(""))
				throw new Exception("Please insert book description");
			this.bookDescription = bookDescription;
		}
		public String getDatePurchased() {
			return datePurchased;
		}
		public void setDatePurchased(LocalDate datePurchased) throws Exception {
			
			try {
				this.datePurchased = datePurchased.toString();
			} catch (Exception e) {
				throw new Exception("please insert date of purchased");
			}
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
		
		public void setDateOfPrint(LocalDate dateOfPrint) throws Exception {
			try {
				this.dateOfPrint = dateOfPrint.toString();
			} catch (Exception e) {
				throw new Exception("please insert date of print");
			}
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
		public static Book getTheBook() {
			return book;
		}
		public static void setTheBook(Book book) {
			Book.book=book;
		}
		public int getBookStatus() {
			return bookStatus;
		}
		public void setBookStatus(int bookStatus) {
			this.bookStatus = bookStatus;
		}
		@Override
		public String toString() {
			return "Book [bookName=" + bookName + ", publisherName=" + publisherName + ", bookEdite=" + bookEdite
					+ ", CatalogNumber=" + CatalogNumber + ", NumberOFCopies=" + NumberOFCopies
					+ ", PositionOnTheShelf=" + PositionOnTheShelf + ", bookCatagory=" + bookCatagory
					+ ", bookDescription=" + bookDescription + ", datePurchased=" + datePurchased + ", dateOfPrint="
					+ dateOfPrint + ", contentfile="
					+ Arrays.toString(contentfile) + ", bookphoto=" + Arrays.toString(bookphoto) + ", bookStatus="
					+ bookStatus + "]";
		}
		
		
		
	

}
