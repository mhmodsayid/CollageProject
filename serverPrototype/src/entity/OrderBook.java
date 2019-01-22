package entity;

import java.io.Serializable;

public class OrderBook implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String bookName;
	private String readerId;
	private String orderDate;
	private String orderStatus;
	private String bookId;
	
	
	public OrderBook() {
		super();
	}
	public OrderBook(String bookName, String readerId, String orderDate, String orderStatus,
			String bookId) {
		super();
		this.bookName = bookName;
		this.readerId = readerId;
		
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
		this.bookId = bookId;
	}
	public String getBookName() {
		return bookName;
	}
	public String getReaderId() {
		return readerId;
	}
	
	public String getOrderDate() {
		return orderDate;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public String getBookId() {
		return bookId;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public void setReaderId(String readerId) {
		this.readerId = readerId;
	}
	
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	@Override
	public String toString() {
		return "OrderBook [bookName=" + bookName + ", readerId=" + readerId + ", orderDate="
				+ orderDate + ", orderStatus=" + orderStatus + ", bookId=" + bookId + "]";
	}
	
	

}
