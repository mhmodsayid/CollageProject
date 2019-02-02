package control;
// This file contains material supporting section 3.7 of the textbook:

// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
import control.CheackingBorrowDate;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import SendingMail.SendMailToClient;
import entity.Book;
import entity.OrderBook;
import entity.Reader;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
/**
 * This class is the thread which works every 24 hours
 *  checking if the reader did not pick the order for 24 hours the order will be deleted and an email will be sent to the order owner
 *  and then this event will be registered in the history of the reader 
 * @author Mahmoud Sayid
 *
 */
class OrderTimeOut implements Runnable {
	public DbContoller db;

	public OrderTimeOut(DbContoller db) {
		this.db = db;
	}
	
	/**
	 * run on all the orders and check if there is orders ready for 24 hours
	 * if yes delete the order send email and insert to history of the reader
	 */
	@Override
	public void run() {
		while (true) {
			/**
			 * create a connection to database
			 */
			Connection con = db.initalizeDataBase();
			PreparedStatement pstmt;
			String query;
/**
 * scan all the order table where the order ready time is not null 
 */
			query = "SELECT * FROM OrderBook  WHERE true";
			try {
				pstmt = con.prepareStatement(query);
				ResultSet s = pstmt.executeQuery();
				while (s.next()) {
					Object orderdateready = s.getObject("OrderBookReady");
					if (orderdateready == null)
						continue;
					java.sql.Timestamp currentTime = new java.sql.Timestamp(new java.util.Date().getTime());
					java.sql.Timestamp readyTime = (Timestamp) orderdateready;
					System.out.println((currentTime.getTime() - readyTime.getTime()) / 3600000);
					if ((currentTime.getTime() - readyTime.getTime()) / 3600000 > 24) {
						/**
						 * take the email of the reader to send him email that his order was deleted
						 */
						query = "select email from user where UserID=?";
						pstmt = con.prepareStatement(query);
						pstmt.setString(1, s.getString("ReaderID"));
						ResultSet object1 = pstmt.executeQuery();
						object1.next();
						SendMailToClient.SendingMail(object1.getString(1),
								"your order: #" + s.getInt("OrderID") + " has been deleted");
						System.out.println("order need to be deleted");
						query = "delete FROM OrderBook  WHERE OrderID=?";
						pstmt = con.prepareStatement(query);
						pstmt.setInt(1, s.getInt("OrderID"));
						pstmt.executeUpdate();
						/**
						 * insert the activity in the history of the reader 
						 */
						
						query = "INSERT INTO `history` (`readerID`,`bookName`,`date`,`description`)" + 
								"VALUES(?,?,?,?);";
						pstmt = con.prepareStatement(query);
						pstmt.setString(1, s.getString("ReaderID"));
						pstmt.setString(2, s.getString("BookName"));
						pstmt.setString(3, LocalDate.now().toString());
						pstmt.setString(4,"24H pass order deleted");
						pstmt.executeUpdate();
						System.out.println("inserted to history");
			
						
					}

				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				Thread.sleep(1000 * 60 * 60 * 12);//sleep for 24 hours
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	

}
/**
 * this class is the server controller send data and receive from the client 
 * @author all
 *
 */
public class ServerController extends AbstractServer  {
/**
 * the data base connection settings
 */
	public static DbContoller db;
/**
 * start this class and start listing  to connections 
 * @param port
 */
	public ServerController(int port) {
		super(port);
	}
	/**
	 * handle the message of the clients if its string call getmessagecommand and send the message 
	 * thats means it a string protocol if cannot cast to string that means it bytes array another protool
	 * and will be handled in catch
	 * @param msg,client
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {

		try {
			String s = (String) msg;
			getmessagecommand(msg, client);
		} catch (Exception e1) {// need file special care
			/**
			 * read the message that sent from the client as object
			 */
			ByteArrayInputStream bis = new ByteArrayInputStream((byte[]) msg);
			ObjectInput in = null;
			Object obj = null;
			try {
				in = new ObjectInputStream(bis);
				obj = in.readObject();
			} catch (ClassNotFoundException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			} catch (IOException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			/** if this is a add book page
			 * the casting to book will success and insert the book into the DB
			 * if its not a book that means it order object will handle in catch
			 */
			try {
				/**
				 * create file in server to store the data of the book the photo and the content table
				 * every book create new file like his name and store the spatial info
				 * the data of the book will stored in 2 tables in db the first one is library to store the
				 * book info and the second one book to store the copys of the book
				 */
				Book book = (Book) obj;
				new File("booksDataFolder/" + book.getBookName()).mkdirs();
				Path path1 = Paths.get("booksDataFolder/" + book.getBookName() + "/Contant_table.pdf");
				Files.write(path1, book.getContentfile());
				path1 = Paths.get("booksDataFolder/" + book.getBookName() + "/book_picture.jpg");
				Files.write(path1, book.getBookphoto());
				if (in != null)
					in.close();
				Connection con = db.initalizeDataBase();
				PreparedStatement pstmt;
				String query;

				query = "INSERT INTO `book` values(?,?,?,?,?,?)";
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, book.getCatalogNumber());
				pstmt.setString(2, book.getBookName());
				pstmt.setString(3, book.getDateOfPrint());
				pstmt.setString(4, book.getDatePurchased());
				pstmt.setString(5, book.getPositionOnTheShelf());
				pstmt.setString(6, book.getBorrowStatus());
				pstmt.executeUpdate();

				query = "INSERT INTO `library` values(?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE NumberOfCopyes=NumberOfCopyes+1,quantity=quantity+1;";
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, book.getBookName());
				pstmt.setString(2, book.getPublisherName());
				pstmt.setString(3, book.getBookEdite());
				pstmt.setInt(4, book.getQuantity());
				pstmt.setString(5, book.getBookCatagory());
				pstmt.setString(6, book.getBookDescription());
				pstmt.setString(7, book.getBookStatus());
				pstmt.setInt(8, book.getNumberOFCopies());
				pstmt.executeUpdate();

				client.sendToClient("addBookSuccess");//send message to the client that book added
				con.close();
			} catch (Exception e) {
/**
 * adding book fails test if the error is duplicate that means the book is already exists
 * send message to client the book already exists
 * or its orderbook abject
 */
				try {
					if (e.toString().contains("Duplicate")) {
						System.out.println("the book already exists");
						client.sendToClient("the book already exists");
					} else {
						if(!e.toString().contains("cast"))
						System.out.println(e.toString());
						else
							System.out.println("not book going to book order");
					}

				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
			// if its another page
			try {
				/**
				 * cast the object to orderbook if success it is add order request 
				 * before adding order first need to check if the number of order in this book is less
				 * than the available number of copies in the library the aprove the add order
				 * and insert it to db send conformation message to the client 
				 * 
				 */

				OrderBook order = (OrderBook) obj;
				Connection con = db.initalizeDataBase();
				String query;
				PreparedStatement pstmt1;
				//get the number of copies of the book
				query = "SELECT NumberOfCopyes from library WHERE BookName =?;";
				pstmt1 = con.prepareStatement(query);
				pstmt1.setString(1, order.getBookName());
				ResultSet s = pstmt1.executeQuery();
				s.next();
				int numberofcopyes = s.getInt(1);
				//count the number of orders on the ook
				query = "select count(BookName) from OrderBook where OrderBook.BookName =?";
				pstmt1 = con.prepareStatement(query);
				pstmt1.setString(1, order.getBookName());
				s = pstmt1.executeQuery();
				s.next();
				int numberoforders = s.getInt(1);
				/**
				 * check if the number of orders on the book less than the number of copies available
				 */
				if (numberofcopyes > numberoforders) {
					/**
					 * check if the reader already order this book
					 */
					query = "SELECT * FROM OrderBook WHERE OrderBook.ReaderID =? AND OrderBook.BookName =?;";
					pstmt1 = con.prepareStatement(query);
					pstmt1.setString(2, order.getBookName());
					pstmt1.setString(1, order.getReaderId());
					s = pstmt1.executeQuery();
					/**
					 * if his order exists send message to client saying his order already exists
					 */
					if (!s.next()) {
						/**
						 * insert the order and send success message 
						 */
						query = "INSERT INTO OrderBook (BookName,ReaderID,OrderDate,OrderStatus) values(?,?,?,?)";
						pstmt1 = con.prepareStatement(query);
						pstmt1.setString(1, order.getBookName());
						pstmt1.setString(2, order.getReaderId());
						pstmt1.setString(3, order.getOrderDate());
						pstmt1.setString(4, order.getOrderStatus());
						pstmt1.executeUpdate();
						System.out.println("Your order added successfully");
						client.sendToClient("Your order added successfully");
						/**
						 * if there is more than 1 order change the status of the book to Indemand
						 */
						query = "SELECT count(BookName) FROM OrderBook" + "    WHERE true";
						pstmt1 = con.prepareStatement(query);
						s = pstmt1.executeQuery();
						if (s.next()) {
							if (s.getInt(1) >= 2) {
								PreparedStatement pstmt2;
								query = "UPDATE library SET `BookStatus` ='Indemand' WHERE BookName='"
										+ order.getBookName() + "'";
								pstmt2 = con.prepareStatement(query);
								pstmt2.executeUpdate();
								System.out.println("more than 2");
							}
						}
						/**
						 * insert the order activity in history table
						 */
						
						query = "INSERT INTO `history` (`readerID`,`bookName`,`date`,`description`)" + 
								"VALUES(?,?,?,?);";
						pstmt1 = con.prepareStatement(query);
						pstmt1.setString(1, order.getReaderId());
						pstmt1.setString(2, order.getBookName());
						pstmt1.setString(3, LocalDate.now().toString());
						pstmt1.setString(4,"Order");
						pstmt1.executeUpdate();
						System.out.println("inserted to history");
					//	client.sendToClient("inserted to history");

						con.close();
					} else {
						System.out.println("Your order already exists");
						client.sendToClient("Your order already exists");

					}
				} else {
					System.out.println("The max number of book order exeded");
					client.sendToClient("The max number of book order exeded");
				}

			} catch (Exception e) {
				// TODO: handle exception
				e.getStackTrace();
				System.out.println(e.toString());
			}

		}

	}
/**
 * this message will convert the data from the db result to the appropriate object and fill the data
 * @param result the data from the DB
 * @param obj the object to fill the data in 
 * @throws Exception
 */
	public void createObject(ResultSet result, Object obj) throws Exception {
		if (obj instanceof Reader) {
			while (result.next()) {
				Reader student = (Reader) obj;
				Reader.setStudent_id(result.getString(1));
				student.setStudent_name(result.getString(2));
				student.setStatus(Reader.StatusMemberShip.valueOf(result.getString(3)));
				student.setOperation(Reader.Operation.valueOf(result.getString(4)));
				student.setFreeze(Integer.parseInt(result.getString(5)));
			}
		}

		if (obj instanceof Book) {
			while (result.next()) {
				Book book = (Book) obj;
				book.setBookName(result.getString(1));
				book.setPublisherName(result.getString(2));
				book.setBookEdite(result.getString(3));
				book.setDateOfPrint(result.getString(4));
				book.setBookCatagory(result.getString(5));
				book.setBookDescription(result.getString(6));
				book.setCatalogNumber(result.getString(7));
				book.setNumberOFCopies(result.getString(8));
				book.setDatePurchased(result.getString(9));
				book.setPositionOnTheShelf(result.getString(10));
				book.setBookStatus(result.getString(11));
			}
		}

	}

	/**
	 * @author Ammar Khutba
	 * @param message the message we take from the client that have the case that we
	 *                want to work on
	 * @param client  the client send message to handle in sever and update all the
	 *                details case 7: in case 7 we Searching for user and checking
	 *                the status of the user case 8: in case 8 we Searching for the
	 *                book and checking the status of the book to give him a date
	 *                case 9: in case 9 we Searching for the book and get all the
	 *                date info case 10: in case 10 we returning the book to the
	 *                library and update all the info case 11: in case 11 we
	 *                Checking the date of the borrow book in loop in a thread case
	 *                12: in case 12 we Checking the user status and if the user not
	 *                blocked and not frozen give him the book
	 * @throws IOException 
	 */
	@SuppressWarnings("resource")
	public void getmessagecommand(Object message, ConnectionToClient client) throws IOException {
		try {
			String str = (String) message;
		} catch (Exception e1) {
			System.out.println("not a string");
		}
		String SendMassege = "";
		int quantity=0;
		ResultSet object;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
		Connection con = db.initalizeDataBase();
		PreparedStatement pstmt;
		String s = (String) message;
		List<String> ComparDate;
		String query;
		List<String> data;
		int command = Integer.parseInt(s.substring(0, 2));
		s = s.substring(2, s.length());
		try {
			switch (command) {
			case 1:
				data = Arrays.asList(s.split(","));
				if (data.size() < 5) {
					System.out.println("data size is not legal");
					break;
				}
				query = "INSERT INTO student values(?,?,?,?,?)";
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, data.get(0));
				pstmt.setString(2, data.get(1));
				pstmt.setString(3, data.get(2));
				pstmt.setString(4, data.get(3));
				pstmt.setString(5, data.get(4));
				pstmt.executeUpdate();
				client.sendToClient("01success");

				break;
			case 2:

				data = Arrays.asList(s.split(","));
				query = "select * from student where student_id=?";
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, data.get(0));
				Reader student = new Reader();
				createObject(pstmt.executeQuery(), student);
				if (Reader.getStudent_id() == null)
					client.sendToClient("-1");
				else
					client.sendToClient(student.toStringClient());
				System.out.println("sending " + student + " to client");

				break;
			case 3:
				String frozen = "0";
				data = Arrays.asList(s.split(","));
				query = "update student set statusmembership=?,freeze=? where student_id=?";
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, data.get(1));
				if (data.get(1).equals("Frozen"))
					frozen = "1";
				else
					frozen = "0";
				pstmt.setString(3, data.get(0));
				pstmt.setString(2, frozen);
				client.sendToClient(pstmt.executeUpdate());
				break;
			case 4:// book order
				data = Arrays.asList(s.split(","));
				query = "select * from book where `BookName`=?";
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, data.get(0));
				Book book = new Book();
				createObject(pstmt.executeQuery(), book);

				Path path1 = Paths.get("booksDataFolder/" + book.getBookName() + "/Contant_table.pdf");
				byte[] content = Files.readAllBytes(path1);
				path1 = Paths.get("booksDataFolder/" + book.getBookName() + "/book_picture.jpg");
				byte[] photo = Files.readAllBytes(path1);
				book.setBookphoto(photo);
				book.setContentfile(content);

				ObjectOutput out = null;
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				out = new ObjectOutputStream(bos);
				out.writeObject(book);
				out.flush();
				byte[] objbyte = bos.toByteArray();
				if (book.getBookName() == null)
					client.sendToClient("-1");
				else
					client.sendToClient(objbyte);
				System.out.println("sending " + book + " to client");
				break;
			case 5:

				ResultSet resultSet = null;
				data = Arrays.asList(s.split(","));
				query = "select * from library where library.BookName=? or library.Category=? or library.Author=? or library.`BookDecrebtion` LIKE ?";
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, data.get(0));
				pstmt.setString(2, data.get(1));
				pstmt.setString(3, data.get(2));
				if (!data.get(3).equals(" "))
					pstmt.setString(4, "%" + data.get(3) + "%");
				else
					pstmt.setString(4, "");

				resultSet = pstmt.executeQuery();
				List<Book> books = new ArrayList<Book>();
				if (!resultSet.next()) {
					Book nbook = new Book();
					nbook.setBookStatus("-1");
					books.add(nbook);
					// client.sendToClient("-1");
				}
				/* get book from result set and save the catalog number of every book found */

				resultSet.previous();
				while (resultSet.next()) {
					Book resbook = new Book();
					resbook.setBookName(resultSet.getString(1));
					resbook.setPublisherName(resultSet.getString(2));
					resbook.setBookEdite(resultSet.getString(3));
					resbook.setNumberOFCopies(resultSet.getString(8));
					resbook.setBookCatagory(resultSet.getString(5));
					resbook.setBookDescription(resultSet.getString(6));
					resbook.setBookStatus(resultSet.getString(7));
					/*
					 * if the book status is not Available then get its
					 * catalogNumber,printDate,purchaseDateangshelfPosition from book table
					 */
					if (Integer.parseInt(resultSet.getString(4))==0) {
						query = "select book.catalogNumber,book.printDate,book.shelfPosition,borrowbook.ReturnDate from book INNER JOIN borrowbook on book.catalogNumber=borrowbook.CatalogNumber where book.BookName=? ORDER BY borrowbook.ReturnDate ASC";
						pstmt = con.prepareStatement(query);
                        pstmt.setString(1, resbook.getBookName());
						ResultSet catalog = pstmt.executeQuery();
						catalog.next();
						resbook.setCatalogNumber(catalog.getString(1));
						resbook.setDateOfPrint(catalog.getString(2));
						resbook.setDatePurchased(catalog.getString(4));
						resbook.setPositionOnTheShelf(catalog.getString(3));
					}
					resbook.setQuantity(Integer.parseInt(resultSet.getString(4)));

					Path path01 = Paths.get("booksDataFolder/" + resbook.getBookName() + "/Contant_table.pdf");
					byte[] contentFile = Files.readAllBytes(path01);
					path01 = Paths.get("booksDataFolder/" + resbook.getBookName() + "/book_picture.jpg");
					byte[] bookPhoto = Files.readAllBytes(path01);
					resbook.setBookphoto(bookPhoto);
					resbook.setContentfile(contentFile);
					books.add(resbook);
				}

				if (books.size() > 0) {
					ObjectOutput out1 = null;
					ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
					out1 = new ObjectOutputStream(bos1);
					out1.writeObject(books);
					out1.flush();
					byte[] objbyte1 = bos1.toByteArray();
					client.sendToClient(objbyte1);
					System.out.println("sending" + books.size() + "books to client");
				}
				break;

				// in this case we look for userID in the DB for the borrow gui
			case 7:
				SendMassege = "UserIDFound,";
				data = Arrays.asList(s.split(","));
				query = "select * from user where UserID=?";//search for reader by his ID
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, data.get(0));
				object = pstmt.executeQuery();
				while (object.next()) {
					SendMassege += object.getString(9)+"," + object.getString(4) + " " + object.getString(5)+","+object.getString(6);//we get the first name and the last name 
				}
				if (SendMassege.equals("UserIDFound,")) {
					SendMassege = "notFound,UserID Not Found";//if there is no user by the id that we put in the borrow gui
				}
				client.sendToClient(SendMassege);

				break;
				//in this case we search for the book by the catalogNumber for the borrow book gui
			case 8:
				String orderName = null;
				SendMassege = "BookFound,";
				data = Arrays.asList(s.split(","));
				query = "select * from book where catalogNumber=?";//searching in the DB for the book by catalogNumber
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, data.get(0));
				object = pstmt.executeQuery();
				while (object.next()) {
					SendMassege += object.getString(2);//get the book name
					orderName = object.getString(2);
				}
				if (SendMassege.equals("BookFound,")) {
					SendMassege = "notFound,catalogNumber Not Found";//if there no book by the catalogNumber
					client.sendToClient(SendMassege);
				}
				query = "select * from library where BookName=?";//search for the book in the libarary DB by the name
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, orderName);
				object = pstmt.executeQuery();
				while (object.next()) {
					SendMassege += "," + object.getString(7);//get the book status
				}
				client.sendToClient(SendMassege);
				break;
				//in this case we search for the book that has been borrowed in the DB for the returnBook gui
			case 9:
				SendMassege = "BookFound,";
				data = Arrays.asList(s.split(","));
				query = "select * from borrowbook where CatalogNumber=?";//search for the book in the borrow DB by the CatalogNumber
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, data.get(0));
				object = pstmt.executeQuery();
				while (object.next()) {
					SendMassege += object.getString(2) + "," + object.getString(4) + "," + object.getString(5);//get the reader id and the borrow date and the return date
				}
				if (SendMassege.equals("BookFound,")) {
					SendMassege = "NoBookFound";//if there is no book has been borrowed
				}
				client.sendToClient(SendMassege);
				break;
				//in this case we return the book in the library for the returnBook gui
			case 10:
				String bookName = null;
				String bookType=null;
				data = Arrays.asList(s.split(","));
				query = "select * from book where CatalogNumber=?";//search for the book in the DB by the CatalogNumber
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, data.get(0));
				object = pstmt.executeQuery();
				while (object.next()) {
					bookName = object.getString(2);//get the book name
				}
				query = "select * from library where BookName=?";//search for the book in the DB by the CatalogNumber
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, bookName);
				object = pstmt.executeQuery();
				while (object.next()) {
					bookType = object.getString(7);//get the book name
				}
				
				query = "INSERT INTO history  values(?,?,?,?,?,?,?,?)";//insert in history 
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, data.get(1));
				pstmt.setString(2, bookName);
				pstmt.setString(3,dateFormat.format(date).toString());
				pstmt.setString(4, "Returned");
				pstmt.setInt(5, Integer.parseInt(data.get(2)));
				pstmt.setString(6, bookType);
				pstmt.setString(7, data.get(4));
				pstmt.setInt(8, Integer.parseInt(data.get(3)));
				pstmt.executeUpdate();
				
				query = "select * from `OrderBook` where BookName=?";////search for the book in the OrderBook DB by the name of the book
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, bookName);
				object = pstmt.executeQuery();
				if (!object.next()) {//if there no book in OrderBook DB 
					query = "update book set borrowStatus=? where catalogNumber=?";//then update in book DB borrowStatus form  indemand to normal 
					pstmt = con.prepareStatement(query);
					pstmt.setString(1, "Normal");
					pstmt.setString(2, data.get(0));
					pstmt.executeUpdate();
				}
				
				query = "update library set quantity=quantity + 1 where BookName=?";//update the libarary quantity of the book to +1
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, bookName);
				pstmt.executeUpdate();

				
				query = "SELECT MIN(OrderID) from OrderBook where OrderBookReady is null"; //seach for the first book that been ordered where the reader date for the wait time to take the book is null
				pstmt = con.prepareStatement(query);
				object = pstmt.executeQuery();
				object.next();
				int x = object.getInt(1);

				query = "UPDATE OrderBook SET OrderBookReady=?  WHERE BookName=? and OrderID=? ";//put the date of the return book in OrderBookReady
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, date.toString());
				pstmt.setString(2, bookName);
				pstmt.setInt(3, x);
				pstmt.executeUpdate();

				query = "DELETE FROM borrowbook WHERE CatalogNumber=?";//DELETE the borrow from the borrowbook DB
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, data.get(0));
				pstmt.executeUpdate();

				String ReturnReaderStatus = null;
				query = "select * from user where UserID=?";//search for the status of the reader 
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, data.get(1));
				object = pstmt.executeQuery();
				while (object.next()) {
					ReturnReaderStatus = object.getString(9);
				}
				if (ReturnReaderStatus.equals("Frozen")) {//if the reader is frozen the make him active
					query = "update user set userStatus=? where UserID=?";
					pstmt = con.prepareStatement(query);
					pstmt.setString(1, "Active");
					pstmt.setString(2, data.get(1));
					pstmt.executeUpdate();

					query = "update reader set ReaderStatus=? where UserID=?";
					pstmt = con.prepareStatement(query);
					pstmt.setString(1, "Active");
					pstmt.setString(2, data.get(1));
					pstmt.executeUpdate();
				}
				client.sendToClient("done");//send to the client that the borrowed has been done
				break;
				//in this case we send massege to the client if remain one day or if the user has not return the book in the right time update his status
				//and inset to the history the procees of the update
			case 11:
				String UserID = null;
				String Email = null;
				ComparDate = Arrays.asList(dateFormat.format(date).split("-"));
				int currentday = Integer.parseInt(ComparDate.get(2));
				query = "select * from borrowbook ";
				pstmt = con.prepareStatement(query);
				object = pstmt.executeQuery();
				while (object.next()) {
					UserID = object.getString(2);
					ComparDate = Arrays.asList(object.getString(5).split("-"));
					int ReturnDate = Integer.parseInt(ComparDate.get(2));
					if (currentday + 1 == ReturnDate && dateFormat.format(date).compareTo(object.getString(5)) < 0) {// If
																														// just
																														// remains
																														// one
																														// day
																														// for
																														// returning
																														// the
																														// book
						query = "select * from user where UserID=?";
						pstmt = con.prepareStatement(query);
						pstmt.setString(1, UserID);
						ResultSet object1 = pstmt.executeQuery();
						while (object1.next()) {
							Email = object1.getString(2);
						}
						SendMailToClient.SendingMail(Email, "One day left to return the book back to the library!!!");
					}
					// if the reader has not return the book in time then update all the details of
					// the reader
					else {
						if (dateFormat.format(date).compareTo(object.getString(5)) > 0) {
							int LateReturn = 0;
							String borrowStatus = object.getString(6);//to control the flow of the update allowing you to check for the update if has been done while your function progresses
							query = "select * from reader where UserID=?";//serach for the number of time that the reader has been late
							pstmt = con.prepareStatement(query);
							pstmt.setString(1, UserID);
							ResultSet object2 = pstmt.executeQuery();
							while (object2.next()) {
								LateReturn = object2.getInt(3);//get the time of how much the reader has been late
							}
							if (borrowStatus.equals("borrowed") && LateReturn < 2) {//if the update has not been done  and the time of the late return is <2
								query = "update reader set LateReturn=? , ReaderStatus=?  where UserID=?";//then update the time of the late return to +1 and the ReaderStatus to frozen
								pstmt = con.prepareStatement(query);
								pstmt.setInt(1, LateReturn + 1);
								pstmt.setString(2, "Frozen");
								pstmt.setString(3, UserID);
								pstmt.executeUpdate();

								query = "INSERT INTO history (readerID,bookName,date,description) values(?,?,?,?)";//insert the process of the frezing to the history DB
								pstmt = con.prepareStatement(query);
								pstmt.setString(1, UserID);
								pstmt.setString(2, "");
								pstmt.setString(3, dateFormat.format(date).toString());
								pstmt.setString(4, "Frozen");
								pstmt.executeUpdate();

								query = "update borrowbook set borrowStatus=? where ReaderID=?";//UPDATE the borrow Status to LateReturn
								pstmt = con.prepareStatement(query);
								pstmt.setString(1, "LateReturn");
								pstmt.setString(2, UserID);
								pstmt.executeUpdate();

								query = "update user set userStatus=? where UserID=?";//update userStatus in user to Frozen
								pstmt = con.prepareStatement(query);
								pstmt.setString(1, "Frozen");
								pstmt.setString(2, UserID);
								pstmt.executeUpdate();
							} else if (borrowStatus.equals("borrowed") && LateReturn == 2) {// if the update has not been done and the reader has been
																							// two time late and has
																							// late anther time he will
																							// bee blocked
								query = "INSERT INTO history (readerID,bookName,date,description) values(?,?,?,?)";//insert the process of the Blocked to the history DB
								pstmt = con.prepareStatement(query);
								pstmt.setString(1, UserID);
								pstmt.setString(2, "");
								pstmt.setString(3, dateFormat.format(date).toString());
								pstmt.setString(4, "Blocked");
								pstmt.executeUpdate();
								
								query = "update reader set LateReturn=? , ReaderStatus=?  where UserID=?";// update the LateReturn to +1 and ReaderStatus to blocked
								pstmt = con.prepareStatement(query);
								pstmt.setInt(1, LateReturn + 1);
								pstmt.setString(2, "Blocked");
								pstmt.setString(3, UserID);
								pstmt.executeUpdate();

								query = "update borrowbook set borrowStatus=? where ReaderID=?";//update the borrowStatus to LateReturn in borrow book
								pstmt = con.prepareStatement(query);
								pstmt.setString(1, "LateReturn");
								pstmt.setString(2, UserID);
								pstmt.executeUpdate();

								query = "update user set userStatus=? where UserID=?";//update the userStatus to blocked in the user table
								pstmt = con.prepareStatement(query);
								pstmt.setString(1, "Blocked");
								pstmt.setString(2, UserID);
								pstmt.executeUpdate();
							}
						}
					}
				}
				break;
				//in this case we do the borrow update
			case 12:
				int cnt=0;
				data = Arrays.asList(s.split(","));
				if (data.get(7).equals("Blocked") || data.get(7).equals("Frozen")) {//if the user is blocked or frozen 
					client.sendToClient("notFound," +"The user is :"+ data.get(7));//then send to the client that he is blocked or frozen 
					break;
				}
				String OrderStatus = null;
				String ReaderID = null;
				int FlagOrderStatus = 0;
				query = "select * from library where BookName=?";//search form library by the name
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, data.get(6));
				ResultSet object2 = pstmt.executeQuery();
				while (object2.next()) {
					quantity = object2.getInt(4);//get the quantity
				}
				if (quantity == 0) {
					client.sendToClient("notFound,There is no book by this name in the library at this moment back in anther time ");
					break;
				}
				query = "select * from OrderBook where BookName=?";//search form OrderBook by the name
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, data.get(6));
				object = pstmt.executeQuery();
				while (object.next()) {
					OrderStatus = object.getString(4);//get the OrderStatus
					ReaderID = object.getString(2);//get the ReaderID
					if (OrderStatus.equals("Waiting for Book") && ReaderID.equals(data.get(1))) {//if the reader is waiting the book 
						FlagOrderStatus = 1;
					} 
					else {
						FlagOrderStatus = 2;//if he's not in the order list or he's not waiting for the book in the order list 
					}
					cnt++;
				}
				if(cnt<quantity) {
					FlagOrderStatus=3;
				}
				if (FlagOrderStatus == 2) {
					client.sendToClient("notFound,He can't borrow the book ");
					break;
				}
				if (FlagOrderStatus == 1) {
					query = "DELETE FROM OrderBook WHERE BookName=? and ReaderID = ? ";//delete the order 
					pstmt = con.prepareStatement(query);
					pstmt.setString(1, data.get(6));
					pstmt.setString(2, ReaderID);
					pstmt.executeUpdate();
				}
				//if there no order
				query = "INSERT INTO borrowbook values(?,?,?,?,?,?)";//insert in borrowbook
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, data.get(0));
				pstmt.setString(2, data.get(1));
				pstmt.setString(3, data.get(2));
				pstmt.setString(4, data.get(3));
				pstmt.setString(5, data.get(4));
				pstmt.setString(6, data.get(5));
				pstmt.executeUpdate();

				query = "INSERT INTO history (readerID,bookName,date,description,borrowPeriod,bookType) values(?,?,?,?,?,?)";//insert in history 
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, data.get(1));
				pstmt.setString(2, data.get(6));
				pstmt.setString(3, dateFormat.format(date).toString());
				pstmt.setString(4, "borrowed");//insert the procees
				if(data.get(8).equals("Indemand"))
					pstmt.setInt(5, 3);	
				else
					pstmt.setInt(5, 14);
				pstmt.setString(6, data.get(8));
				pstmt.executeUpdate();

				query = "update library set quantity=? where BookName=?";//update the quantity in the library -1
				pstmt = con.prepareStatement(query);
				pstmt.setInt(1, quantity - 1);
				pstmt.setString(2, data.get(6));
				pstmt.executeUpdate();

				query = "update book set borrowStatus=? where catalogNumber=?";//update the borrowstatus in book
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, "borrowed");
				pstmt.setString(2, data.get(0));
				pstmt.executeUpdate();
				client.sendToClient("notFound,Borrowed has been done");//after finishing the borrow send massege
				break;
								case 22:///////login
									SendMassege="false";
									String userTypeAndID="false";
									String userStatus="false";
									String LoginStatus="false";
									data=Arrays.asList(s.split(","));
									query="select * from user where username=?";
									pstmt=con.prepareStatement(query);
									pstmt.setString(1,data.get(0));
									object = pstmt.executeQuery();
									while(object.next()) {
										SendMassege=object.getString(3);
										LoginStatus=object.getString(7);
										userStatus=object.getString(9);
										userTypeAndID=object.getString(9)+","+object.getString(6)+","+object.getString(1);
										}
									if(SendMassege.equals(data.get(1))&&LoginStatus.equals("0")&&!userStatus.equals("Blocked")&&!userStatus.equals("Frozen")) {
										SendMassege=("user exsist");
										client.sendToClient(userTypeAndID);
										query="update user set LogInStatus=? where username=?";
										pstmt=con.prepareStatement(query);
										pstmt.setInt(1, 1);
										pstmt.setString(2,data.get(0));
										pstmt.executeUpdate();
										break;
									}
									else if(!SendMassege.equals(data.get(1))){
										SendMassege=("user not exsist");
										client.sendToClient(SendMassege);
										break;
									}
									else if(userStatus.equals("Blocked")){
										SendMassege=("user was blocked");
										client.sendToClient(SendMassege);
										break;
									}
									else if(userStatus.equals("Frozen")&&LoginStatus.equals("0")){
										SendMassege=("user was frozen");
										client.sendToClient(userTypeAndID);
										query="update user set LogInStatus=? where username=?";
										pstmt=con.prepareStatement(query);
										pstmt.setInt(1, 1);
										pstmt.setString(2,data.get(0));
										pstmt.executeUpdate();
										break;
									}
									else if(LoginStatus.equals("1")){
										SendMassege=("this account is logged in already");
										client.sendToClient(SendMassege);
										break;
									}
									break;

									
									
							
								case 23://////////register user
									String SendMassege1;
									SendMassege="old user";
									data=Arrays.asList(s.split(","));
									query="select * from user where username=?";
									pstmt=con.prepareStatement(query);
									pstmt.setString(1,data.get(9));
									object = pstmt.executeQuery();
									while(object.next()) {
										SendMassege=object.getString(10);
										}
									if(SendMassege.equals(data.get(9))) {
										SendMassege=("user exsist already");
										System.out.println (SendMassege);
										client.sendToClient(SendMassege);
										break;
									}
									else 
										SendMassege1="old user";
										data=Arrays.asList(s.split(","));
										query="select * from user where email=?";
										pstmt=con.prepareStatement(query);
										pstmt.setString(1,data.get(1));
										object = pstmt.executeQuery();
										while(object.next()) {
											SendMassege1=object.getString(2);
										}
										if(SendMassege1.equals(data.get(1))) {
											SendMassege=("email exsist already");
											System.out.println (SendMassege);
											client.sendToClient(SendMassege);
											break;
										}
										else 
										SendMassege1="old user";
										data=Arrays.asList(s.split(","));
										query="select * from user where UserID=?";
										pstmt=con.prepareStatement(query);
										pstmt.setString(1,data.get(0));
										object = pstmt.executeQuery();
										while(object.next()) {
											SendMassege1=object.getString(1);
										}
										if(SendMassege1.equals(data.get(0))) {
											SendMassege=("UserID exsist already");
											System.out.println (SendMassege);
											client.sendToClient(SendMassege);
										break;
										}
									else			
									data=Arrays.asList(s.split(","));
									query="INSERT INTO user values(?,?,?,?,?,?,?,?,?,?)";
									pstmt=con.prepareStatement(query);
									pstmt.setString(1,data.get(0));
									pstmt.setString(2,data.get(1));
									pstmt.setString(3,data.get(2));
									pstmt.setString(4,data.get(3));
									pstmt.setString(5,data.get(4));
									pstmt.setString(6,data.get(5));
									pstmt.setString(7,data.get(6));
									pstmt.setString(8,data.get(7));
									pstmt.setString(9,data.get(8));
									pstmt.setString(10,data.get(9));
									pstmt.executeUpdate();
									System.out.println ("register in user table done");
									client.sendToClient("register done");
									if(data.get(5).equals("Reader")) {
										int SendMassege4 = 0;
										data=Arrays.asList(s.split(","));
									    query="select * from reader ";
										pstmt=con.prepareStatement(query);
										object = pstmt.executeQuery();
										while(object.next()) {
											SendMassege4=object.getInt(4);
										}
										SendMassege4+=1;	
									data=Arrays.asList(s.split(","));
									query="INSERT INTO reader values(?,?,?,?,?)";
									pstmt=con.prepareStatement(query);
									pstmt.setString(1,data.get(0));
									pstmt.setString(2,"Active");
									pstmt.setString(3,"0");			
									pstmt.setInt(4,SendMassege4);	
									pstmt.setString(5,"0");			
									pstmt.executeUpdate();
									System.out.println ("register in reader table done");
									}
									if(data.get(5).equals("Manager")||data.get(5).equals("Librarian")) {
										
										int SendMassege3 = 0;
										data=Arrays.asList(s.split(","));
									    query="select * from worker ";
										pstmt=con.prepareStatement(query);
										object = pstmt.executeQuery();
										while(object.next()) {
											SendMassege3=object.getInt(1);
										}
										SendMassege3+=1;
									data=Arrays.asList(s.split(","));
									query="INSERT INTO worker values(?,?)";
									pstmt=con.prepareStatement(query);
									pstmt.setInt(1, SendMassege3);
									pstmt.setString(2,data.get(0));			
									pstmt.executeUpdate();
									System.out.println ("register in worker table done");
									}
									break;
									
								case 25:
									SendMassege="false";
									data=Arrays.asList(s.split(","));
									query="select * from user where email=?";
									pstmt=con.prepareStatement(query);
									pstmt.setString(1,data.get(0));
									object = pstmt.executeQuery();
									while(object.next()) {
										SendMassege=object.getString(3);
									}
									if(SendMassege.equals("false")) {
										SendMassege=("Wrong email");
										client.sendToClient(SendMassege);
										break;
									}
									else {
									SendMailToClient.SendingMail(data.get(0),"Your password is :" +SendMassege);
									client.sendToClient(SendMassege);
									}
									break;
									
								case 26:///////LogOut
									data=Arrays.asList(s.split(","));
									query="update user set LogInStatus=? where username=?";
									pstmt=con.prepareStatement(query);
									pstmt.setInt(1, 0);
									pstmt.setString(2,data.get(0));
									pstmt.executeUpdate();
								break;
								case 27:// Extend return date
									
									SendMassege = "UserIDFound,";
									data = Arrays.asList(s.split(","));
									query = "select * from reader where UserID=?";
									pstmt = con.prepareStatement(query);
									pstmt.setString(1, data.get(0));
									object = pstmt.executeQuery();
									while (object.next()) {
										SendMassege += object.getString(2);
									}
									query = "select * from user where UserID=?";
									pstmt = con.prepareStatement(query);
									pstmt.setString(1, data.get(0));
									object = pstmt.executeQuery();
									while (object.next()) {
										SendMassege += "," + object.getString(4) + " " + object.getString(5)+","+object.getString(6);
									}
									if (SendMassege.equals("UserIDFound,")) {
										SendMassege = "notFound,UserID Not Found";
									}
					
									client.sendToClient(SendMassege);
					
									break;
					
								case 28: // check if book is borrowed
					
									boolean borrowflag = false;
									boolean orderflag = false;
									String SendMessage2 = "";
									data = Arrays.asList(s.split(","));
									query = "select * from book where catalogNumber=?";//get book name
									pstmt = con.prepareStatement(query);
									pstmt.setString(1, data.get(0));
									object = pstmt.executeQuery();
									while (object.next()) {
										SendMessage2 += object.getString(2);
										String bookName2 = object.getString(2);
										query = "select * from borrowbook where catalogNumber=?";//search in borrowbook table
										pstmt = con.prepareStatement(query);
										pstmt.setString(1, data.get(0));
										object = pstmt.executeQuery();
										while (object.next()) {
											SendMessage2 += "," + object.getString(4) + "," + object.getString(5);
											borrowflag = true;
										}
										if (!borrowflag) {
											SendMessage2 = "BookNotBorrowed";
											break;
										}
										data = Arrays.asList(bookName2);
										query = "select * from OrderBook where BookName=?";//check if book is ordered
										pstmt = con.prepareStatement(query);
										pstmt.setString(1, data.get(0));
										object = pstmt.executeQuery();
										while (object.next()) {
											SendMessage2 = "bookOrdered";
											orderflag = true;
										}
										if (!orderflag) {
											query = "select * from library where BookName=?";//check if book is on demand
											pstmt = con.prepareStatement(query);
											pstmt.setString(1, data.get(0));
											object = pstmt.executeQuery();
											while (object.next()) {
												SendMessage2 += "," + object.getString(7) + "," + "notOrdered";
											}
										}
					
									}
									System.out.println(SendMessage2);
									client.sendToClient(SendMessage2);
									break;
					
								case 29:// confirm extend date
									String Email1="";
									String librarianID="";
									String userID="";
									String catalogNum="";
									String userName="";
									String borrowBookName="";
									String newReturn=("");
									data = Arrays.asList(s.split(","));
									
									query = "update borrowbook set ReturnDate=? where CatalogNumber=?";//update return date
									pstmt = con.prepareStatement(query);
									pstmt.setString(1, data.get(0));
									newReturn=data.get(0);
									pstmt.setString(2, data.get(1));
									pstmt.executeUpdate();
									
									query="select * from borrowbook where CatalogNumber=?";//get user and librairan id
									pstmt = con.prepareStatement(query);
									pstmt.setString(1, data.get(1));
									object = pstmt.executeQuery();
									while(object.next()) {
										librarianID=object.getString(3);
										userID=object.getString(2);
										catalogNum=object.getString(1);
									}
									
									query="select * from book where catalogNumber=?";//get book name
									pstmt = con.prepareStatement(query);
									pstmt.setString(1, catalogNum);
									object = pstmt.executeQuery();
									while(object.next()) {
										borrowBookName=object.getString(2);
									}
									
									query = "select * from user where UserID=?";//get user name
									pstmt = con.prepareStatement(query);
									pstmt.setString(1,userID);
									object = pstmt.executeQuery();
									while (object.next()) {
										userName = object.getString(10);
									}
									
									
									query = "select * from user where UserID=?";//get librarian email and send message
									pstmt = con.prepareStatement(query);
									pstmt.setString(1, librarianID);
									object = pstmt.executeQuery();
									while (object.next()) {
										Email1 = object.getString(2);
									}
									 String mailMessage="Borrow Extended:\n"+"Reader name: "+userName+"\nReader ID:"+userID+"\nBook: "+borrowBookName+"\nNew return date:"+newReturn;
									System.out.println(mailMessage);
									SendMailToClient.SendingMail(Email1,  mailMessage);
									SendMassege = "Return date updated";
									client.sendToClient(SendMassege);
									break;
					
								case 30://fill reader card info
									SendMassege="";
									data=Arrays.asList(s.split(","));
									query="select * from user where UserID=?";//get user details from user table
									pstmt=con.prepareStatement(query);
									pstmt.setString(1,data.get(0));
								    object = pstmt.executeQuery();
									while(object.next()) {
										SendMassege="UserIDFound,"+object.getString(4)+","+object.getString(5)+","+object.getString(9)+","+object.getString(2)+","+object.getString(8);
									}
									 System.out.println(SendMassege);
									client.sendToClient(SendMassege);
											
											break;
					
								case 31://populate history table
									String flag="noData";
									int rowCnt=0;
									SendMassege1="rowCounter";
									data=Arrays.asList(s.split(","));
									query="select * from history where readerID=?";//get all user history
									pstmt=con.prepareStatement(query);
									pstmt.setString(1,data.get(0));
								    object = pstmt.executeQuery();
									while(object.next()) {
										flag="isData";
										rowCnt++;
										SendMassege1+=","+object.getString(3)+","+object.getString(4)+","+object.getString(2)+","+object.getString(7);
									}
									SendMassege=SendMassege1.replaceFirst("rowCounter", Integer.toString(rowCnt));
									if(flag.equals("isData"))
										client.sendToClient(SendMassege);
									else
										client.sendToClient(flag);//no user found
											
											break;
											
								case 32://save info change
									SendMassege="";
									data=Arrays.asList(s.split(","));
									query="update user set email=? where UserID=?";//update email
									pstmt=con.prepareStatement(query);
									pstmt.setString(2,data.get(0));
									pstmt.setString(1,data.get(1));
									pstmt.executeUpdate();
									query="update user set phone=? where UserID=?";//update phone
									pstmt=con.prepareStatement(query);
									pstmt.setString(1,data.get(2));
									pstmt.setString(2,data.get(0));
									pstmt.executeUpdate();
									client.sendToClient("Info Updated");
											
											break;
											
								case 33://activate account
									SendMassege="";
									LocalDate today1=LocalDate.now();
									DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
									String formattedString = today1.format(formatter);
									data=Arrays.asList(s.split(","));
									query="update user set userStatus=? where UserID=?";//update userstatus in user table
									pstmt=con.prepareStatement(query);
									pstmt.setString(2,data.get(0));
									pstmt.setString(1,data.get(1));
									pstmt.executeUpdate();
									query="update reader set ReaderStatus=? where UserID=?";//update reader status in reader table
									pstmt=con.prepareStatement(query);
									pstmt.setString(2,data.get(0));
									pstmt.setString(1,data.get(1));
									pstmt.executeUpdate();
									query="insert into history values(?,?,?,?,?,?,?,?)";//insert status change into history table
									pstmt=con.prepareStatement(query);
									pstmt.setString(1,data.get(0));
									pstmt.setString(2,"");
									pstmt.setString(3,formattedString);
									pstmt.setString(4,"Active");
									pstmt.setString(5,"0");
									pstmt.setString(6,"");
									pstmt.setString(7,"0");
									pstmt.setString(8,"0");
									pstmt.executeUpdate();
									client.sendToClient("Account is Active");
											
											break;
								
								case 34://populate worker table
									SendMassege1="";
									data=Arrays.asList(s.split(","));
									query="select * from user where UserType='Manager' OR UserType='Librarian'";//get workers details from user table
									pstmt=con.prepareStatement(query);
								    object = pstmt.executeQuery();
									while(object.next()) {
										SendMassege1=object.getString(4)+","+object.getString(5)+","+object.getString(1)+","+object.getString(2)+","+object.getString(8)+","+object.getString(6)+","+object.getString(7);
										client.sendToClient(SendMassege1);
										 System.out.println(SendMassege1);
									}
									break;
									
								case 36:///////actions reports
									int activeCount=0;
									int BlockedCount=0;
									int FreezeCount=0;
									String SendMassege9=null;
									data=Arrays.asList(s.split(","));
									query="select * from reader";//get readers status details
									pstmt=con.prepareStatement(query);
								    object = pstmt.executeQuery();
								    while(object.next()) {//add counters
										if(object.getString(2).equals("Active"))
											activeCount++;	
										if(object.getString(2).equals("Blocked"))
											BlockedCount++;	
										if(object.getString(2).equals("Frozen"))
											FreezeCount++;	
								    }
								    int countCopy=0;
					
									query="select * from library";//count library copies
									pstmt=con.prepareStatement(query);
								    object = pstmt.executeQuery();
								    while(object.next()) {
										if(!object.getString(8).equals(0))
											countCopy+=Integer.parseInt(object.getString(8));	
								    }
								    
								    int countLate=0;
					
									query="select * from reader";//count late returns 
									pstmt=con.prepareStatement(query);
								    object = pstmt.executeQuery();
								    while(object.next()) {
										if(!object.getString(5).equals(0))
											countLate+=Integer.parseInt(object.getString(5));	
								    }
								    
								    SendMassege9=Integer.toString(activeCount)+","+Integer.toString(BlockedCount)+","+Integer.toString(FreezeCount)+","+Integer.toString(countCopy)+","+Integer.toString(countLate);
									client.sendToClient(SendMassege9);
									System.out.println(SendMassege9);
									
								    break;
								    
								case 37://save data to actionsreport table at end of month
									LocalDate today=LocalDate.now();
									int activeCount1=0;
									int BlockedCount1=0;
									int FreezeCount1=0;
									SendMassege9=null;
									data=Arrays.asList(s.split(","));
									query="select * from reader";
									pstmt=con.prepareStatement(query);
								    object = pstmt.executeQuery();
								    while(object.next()) {
										if(object.getString(2).equals("Active"))
											activeCount1++;	
										if(object.getString(2).equals("Blocked"))
											BlockedCount1++;	
										if(object.getString(2).equals("Frozen"))
											FreezeCount1++;	
								    }
								    int countCopy1=0;
					
									query="select * from library";
									pstmt=con.prepareStatement(query);
								    object = pstmt.executeQuery();
								    while(object.next()) {
										if(!object.getString(8).equals(0))
											countCopy1+=Integer.parseInt(object.getString(8));	
								    }
								    
								    int countLate1=0;
					
									query="select * from reader";
									pstmt=con.prepareStatement(query);
								    object = pstmt.executeQuery();
								    while(object.next()) {
											countLate1+=Integer.parseInt(object.getString(5));	
								    }
								    String month=today.format(DateTimeFormatter.ofPattern("MM"));
								    query="insert into actionsReport values(?,?,?,?,?,?,?)";
									pstmt = con.prepareStatement(query);
									pstmt.setString(1, Integer.toString(today.getYear()));
									pstmt.setString(2, month);
									pstmt.setString(3, Integer.toString(activeCount1));
									pstmt.setString(4, Integer.toString(BlockedCount1));
									pstmt.setString(5, Integer.toString(FreezeCount1));
									pstmt.setString(6, Integer.toString(countCopy1));
									pstmt.setString(7,Integer.toString(countLate1));
									pstmt.executeUpdate();
					
									
								    break;
								    
								case 38://set lateFlag in reader to 0 in first day of month
									query="update reader set lateFlag=0 where lateFlag=1";
									pstmt=con.prepareStatement(query);
									pstmt.executeUpdate();
									client.sendToClient("lateflagInitialize");
									break;
									
								case 39://load actions event
									SendMassege1="ReportNotFound";
									data=Arrays.asList(s.split(","));
									query="select * from actionsReport where year=? AND month=?";//search report at entered year and month
									pstmt=con.prepareStatement(query);
									pstmt.setString(1,data.get(0));
									pstmt.setString(2,data.get(1));
								    object = pstmt.executeQuery();
									while(object.next()) {
										SendMassege1=object.getString(3)+","+object.getString(4)+","+object.getString(5)+","+object.getString(6)+","+object.getString(7);
										client.sendToClient(SendMassege1);
									}
								    break;
								    
								case 40://populate late returns tables
									int rowCnt1=0;
									SendMassege1="rowCounter"+",";
									SendMassege="";
									String bookString="";
									int bCnt=0;
									String bookN="";
									String whileFlag="no";
									String average="";
									String median="";
									data=Arrays.asList(s.split(","));
									String searchDate=data.get(0)+"-"+data.get(1)+"-"+"01";
									DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
									//convert String to LocalDate
									LocalDate localDate1 = LocalDate.parse(searchDate, formatter2);
									LocalDate today2=LocalDate.now();
									LocalDate first = localDate1.with(TemporalAdjusters.firstDayOfMonth());
									LocalDate last = localDate1.with(TemporalAdjusters.lastDayOfMonth());
									String searchDate1 = first.format(formatter2);
									String searchDate2 = last.format(formatter2);
					
									
					
									//avg,median and decimal distribution to all late books in the given month
								    query="select avg(late) from (select latePeriod as late from history where "
								    		+ "lateFlag > 0 and date>=? and date<= ? and description='Returned') as av";
									pstmt=con.prepareStatement(query);
									pstmt.setString(1,searchDate1);
									pstmt.setString(2,searchDate2);
								    object = pstmt.executeQuery();
								    if(object.next())
								    SendMassege="total"+","+object.getString(1);
								    
								    query="select (max(latePeriod)+min(latePeriod))/2 from history where lateFlag > 0"
								    		+ " and  date>=? and date<= ? and description='Returned'";
									pstmt=con.prepareStatement(query);
									pstmt.setString(1,searchDate1);
									pstmt.setString(2,searchDate2);
								    object = pstmt.executeQuery();
								    if(object.next())
								    SendMassege+=","+object.getString(1)+","+"0";
								    
								    
								    
								  //avg,median and decimal distribution to each one of the late books in  given month
								    query="select * from history where description='Returned' and lateFlag > 0 and date>=? and date<= ?";
									pstmt=con.prepareStatement(query);
									pstmt.setString(1,searchDate1);
									pstmt.setString(2,searchDate2);
								    object = pstmt.executeQuery();
								    String latePeriod="";
								    while(object.next()) {
								    	latePeriod+=object.getString(8)+",";
								    	bookString+=","+object.getString(2)+",";
								    	bCnt++;
								    }
								    List<String> bookList=Arrays.asList(bookString.split(","));
							        // Create a new LinkedHashSet 
							        Set<String> set = new LinkedHashSet<>(); 
							  
							        // Add the elements to set 
							        set.addAll(bookList); 
							  
							        // Clear the list 
							        ArrayList<String> clearList = new ArrayList<String>();						  
							        // add the elements of set 
							        // with no duplicates to the list 
							        clearList.addAll(set);
							        bCnt=clearList.size();
								    rowCnt=0;
									while((bCnt-1)>0) {
										whileFlag="yes";
										bookN=clearList.get(bCnt-1);
										System.out.println(bookN);
										rowCnt1++;
										query="select avg(late) from(select latePeriod as late from history where lateFlag>0"
												+ " and date>=? and date<= ? and bookName=? and description='Returned') as av";
										pstmt=con.prepareStatement(query);
										pstmt.setString(1,searchDate1);
										pstmt.setString(2,searchDate2);
										pstmt.setString(3,bookN);
									    object = pstmt.executeQuery();
									    while(object.next())
									    SendMassege1+=bookN+","+object.getString(1);
									    
									    query="select (max(latePeriod)+min(latePeriod))/2 from history where lateFlag > 0"
									    		+ " and  date>=? and date<= ? and description='Returned' and bookName=?";
										pstmt=con.prepareStatement(query);
										pstmt.setString(1,searchDate1);
										pstmt.setString(2,searchDate2);
										pstmt.setString(3,bookN);
									    object = pstmt.executeQuery();
									    while(object.next())
									    SendMassege1+=","+object.getString(1)+","+"0"+",";
									    bCnt--;
									}
									SendMassege1=SendMassege1.replaceFirst("rowCounter", Integer.toString(rowCnt1));
									client.sendToClient(SendMassege+","+SendMassege1+","+latePeriod+","+"END");
									
									
									
									break;
			    
								case 50://in this case we get all the deteils from histroy for the borrow statistic
									String borrowPeriod ="";
									data=Arrays.asList(s.split(","));
									query = "select * from history where description='Returned' AND bookType='Avalible'";//serach for the books that returned and status is Avalible
									pstmt = con.prepareStatement(query);
									object = pstmt.executeQuery();
									while (object.next()) {
										ComparDate = Arrays.asList(object.getString(3).split("-"));//get the date of the given date from the client
										if(Integer.parseInt(ComparDate.get(0))==Integer.parseInt(data.get(2))&&Integer.parseInt(ComparDate.get(1))==Integer.parseInt(data.get(1))) {
											borrowPeriod+=object.getString(5)+",";//fill in the borrow period for every book that returned and status is Avalible in a string 
										}
									}
									borrowPeriod+="Indemand,";//cut the string into to places the first place for bookType='Avalible' ant the second is for bookType='Indemand'
									query = "select * from history where description='Returned' AND bookType='Indemand'";
									pstmt = con.prepareStatement(query);
									object = pstmt.executeQuery();
									while (object.next()) {
										ComparDate = Arrays.asList(object.getString(3).split("-"));//get the date of the given date from the client
										if(Integer.parseInt(ComparDate.get(0))==Integer.parseInt(data.get(2))&&Integer.parseInt(ComparDate.get(1))==Integer.parseInt(data.get(1))) {
											borrowPeriod+=object.getString(5)+",";//fill in the borrow period for every book that returned and status is Indemand in a string 
										}
									}
									client.sendToClient(borrowPeriod);
									break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			client.sendToClient(e.toString());
		}
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			client.sendToClient(e.toString());
		}

	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}
	
	

	public void startThreads() {
		Thread thrad1 = new Thread(new OrderTimeOut(db));
		Thread thrad2 = new Thread(new CheackingBorrowDate());
		try {
			thrad2.start();
			thrad1.start();
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		}
	}
}
// End of EchoServer class
