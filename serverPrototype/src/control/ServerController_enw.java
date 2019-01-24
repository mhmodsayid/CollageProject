package control;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import SendingMail.SendMailToClient;
import entity.Book;
import entity.Reader;
import ocsf.server.*;

public class ServerController_enw extends AbstractServer 
{
 
  public static DbContoller db;
 
  public ServerController_enw(int port) 
  {
    super(port);
  }

  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	 
	  try {
		String s=(String) msg;
		getmessagecommand(msg,client);
	} catch (Exception e1) {//need file special care
		    ByteArrayInputStream bis = new ByteArrayInputStream((byte[]) msg);
		    ObjectInput in = null;
		      
				//if this is a add book page
				 try {
					 in = new ObjectInputStream(bis);
					Book book = (Book) in.readObject(); 
					  new File("booksDataFolder/"+book.getBookName()).mkdirs();
					  Path path1 = Paths.get("booksDataFolder/"+book.getBookName()+"/Contant_table.pdf");
					  Files.write(path1,book.getContentfile());
					  path1 = Paths.get("booksDataFolder/"+book.getBookName()+"/book_picture.jpg");
					  Files.write(path1,book.getBookphoto());
					    if (in != null)
					      in.close();
					    Connection con=db.initalizeDataBase();
					    PreparedStatement pstmt;
					    String query;
					    
					    query="INSERT INTO `book` values(?,?,?,?,?,?)";
						pstmt=con.prepareStatement(query);
						pstmt.setString(1,book.getCatalogNumber());
						pstmt.setString(2,book.getBookName());
						pstmt.setString(3,book.getDateOfPrint());
						pstmt.setString(4,book.getDatePurchased());
						pstmt.setString(5,book.getPositionOnTheShelf());
						pstmt.setString(6,book.getBorrowStatus());
						pstmt.executeUpdate();
						
						
					    query="INSERT INTO `library` values(?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE NumberOfCopyes=NumberOfCopyes+1,quantity=quantity+1;";
						pstmt=con.prepareStatement(query);
						pstmt.setString(1,book.getBookName());
						pstmt.setString(2,book.getPublisherName());
						pstmt.setString(3,book.getBookEdite());
						pstmt.setInt(4,book.getQuantity());
						pstmt.setString(5,book.getBookCatagory());
						pstmt.setString(6,book.getBookDescription());
						pstmt.setString(7,book.getBookStatus());
						pstmt.setInt(8,book.getNumberOFCopies());
						pstmt.executeUpdate();
						
						
						
						client.sendToClient("addBookSuccess");
				} catch (Exception e) {
					
					
					try {
						if(e.toString().contains("Duplicate")) {
							System.out.println("the book already exists");
							client.sendToClient("the book already exists");
						}
						else {
							System.out.println("unKnown error");
							client.sendToClient("unKnown error");
						}
						
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				}
			//if its another page 
		
		
		
		
	}
	  	
	   
	      
	    
	  }
 
  public void createObject(ResultSet result,Object obj) throws Exception {
	  if(obj instanceof Reader){
		  while(result.next()){
				Reader student=(Reader) obj;
				student.setStudent_id(result.getString(1));
				student.setStudent_name(result.getString(2));
				student.setStatus(Reader.StatusMemberShip.valueOf(result.getString(3)));
				student.setOperation(Reader.Operation.valueOf(result.getString(4)));
				student.setFreeze(Integer.parseInt(result.getString(5)));
		  }
	  }
	  
	  
	  if(obj instanceof Book){
		  while(result.next()){
				Book book=(Book) obj;
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
	 * @param message the message we take from the client that have the case that we want to work on
	 * @param client the client send message to handle in sever and update all the details
	 * case 7: in case 7 we Searching for user and checking the status of the user
	 * case 8: in case 8 we Searching for the book and checking the status of the book to give him a date	
	 * case 9: in case 9 we Searching for the book and get all the date info
	 * case 10: in case 10 we returning the book to the library and update all the info
	 * case 11: in case 11 we Checking the date of the borrow book in loop in a thread
	 * case 12: in case 12 we Checking the user status and if the user not blocked and not frozen give him the book
	 */ 
  @SuppressWarnings("resource")
public void getmessagecommand(Object message, ConnectionToClient client) {
	  try {
		String str=(String) message;
	} catch (Exception e1) {
		System.out.println("not a string");
	}
	  String SendMassege=""; 
	  ResultSet object;
	  Connection con=db.initalizeDataBase();
	  PreparedStatement pstmt;
	  String s=(String) message;
	  String query;
	  List<String> data;
	 int command=Integer.parseInt(s.substring(0, 2));
	 s=s.substring(2,s.length());
	  try {
		switch (command) {
		case 1:
				data=Arrays.asList(s.split(","));
				if(data.size()<5) {System.out.println("data size is not legal");break;}
				query="INSERT INTO student values(?,?,?,?,?)";
				pstmt=con.prepareStatement(query);
				pstmt.setString(1,data.get(0));
			    	pstmt.setString(2,data.get(1));
				pstmt.setString(3,data.get(2));
				pstmt.setString(4,data.get(3));
				pstmt.setString(5,data.get(4));
				pstmt.executeUpdate();
				client.sendToClient("01success");
			
			break;
		case 2:
			
				data=Arrays.asList(s.split(","));
				query="select * from student where student_id=?";
				pstmt=con.prepareStatement(query);
				pstmt.setString(1,data.get(0));
				Reader student=new Reader();
				createObject(pstmt.executeQuery(),student);
				if(student.getStudent_id()==null)
					client.sendToClient("-1");
				else
					client.sendToClient(student.toStringClient());
				System.out.println("sending "+student+" to client");
			
			break;
		case 3:
				String frozen="0";
				data=Arrays.asList(s.split(","));
				query="update student set statusmembership=?,freeze=? where student_id=?";
				pstmt=con.prepareStatement(query);
				pstmt.setString(1,data.get(1));
				if(data.get(1).equals("Frozen"))
					frozen="1";
				else
					frozen="0";
				pstmt.setString(3,data.get(0));
				pstmt.setString(2,frozen);
				client.sendToClient(pstmt.executeUpdate());
			break;
		case 4://book order
			data=Arrays.asList(s.split(","));
			query="select * from book where `BookName`=?";
			pstmt=con.prepareStatement(query);
			pstmt.setString(1,data.get(0));
			Book book =new Book();
			createObject(pstmt.executeQuery(),book);
			
			
			
		
			 Path path1 = Paths.get("booksDataFolder/"+book.getBookName()+"/Contant_table.pdf");
			 byte[] content = Files.readAllBytes(path1);
			  path1 = Paths.get("booksDataFolder/"+book.getBookName()+"/book_picture.jpg");
			  byte[] photo = Files.readAllBytes(path1);
				book.setBookphoto(photo);
				book.setContentfile(content);
					
			
			ObjectOutput out = null;
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			out = new ObjectOutputStream(bos);   
			  out.writeObject(book);
			  out.flush();
			  byte[] objbyte = bos.toByteArray();
			if(book.getBookName()==null)
				client.sendToClient("-1");
			else
				client.sendToClient(objbyte);
			System.out.println("sending "+book+" to client");
			break;
		
		case 5:
			ResultSet resultSet = null;
			data=Arrays.asList(s.split(","));
			query="select * from library where library.BookName=? or library.Category=? or library.Author=? or library.`BookDecrebtion` LIKE ?";
			pstmt=con.prepareStatement(query);
			pstmt.setString(1,data.get(0));
			pstmt.setString(2,data.get(1));
			pstmt.setString(3,data.get(2));
			if(!data.get(3).equals(" "))
			pstmt.setString(4,"%"+data.get(3)+"%");
			else
			pstmt.setString(4,"");

			resultSet=pstmt.executeQuery();
			/* get book from result set and save the catalog number of every book found*/
			List <Book> books = new ArrayList<Book>(); 
			while (resultSet.next()) 
			  	   {
				Book resbook=new Book();
				resbook.setBookName(resultSet.getString(1));
				resbook.setPublisherName(resultSet.getString(2));
				resbook.setBookEdite(resultSet.getString(3));
				resbook.setNumberOFCopies(resultSet.getString(8));
				resbook.setBookCatagory(resultSet.getString(5));
				resbook.setBookDescription(resultSet.getString(6));
				resbook.setBookStatus(resultSet.getString(7));
				/*if the book status is not Available then get its 
				 * catalogNumber,printDate,purchaseDateangshelfPosition 
				 * from book table */
				if(!resultSet.getString(7).equals("Available"))
				{
					query="select catalogNumber,printDate,purchaseDate,shelfPosition from book where bookName=?";
					pstmt=con.prepareStatement(query);
					pstmt.setString(1, resbook.getBookName());
					
					ResultSet catalog=pstmt.executeQuery();
					catalog.next();
					resbook.setCatalogNumber(catalog.getString(1));
					resbook.setDateOfPrint(catalog.getString(2));
					resbook.setDatePurchased(catalog.getString(3));
					resbook.setPositionOnTheShelf(catalog.getString(4));
				}
				resbook.setQuantity(Integer.parseInt(resultSet.getString(4)));
				
			
				
				Path path01 = Paths.get("booksDataFolder/"+resbook.getBookName()+"/Contant_table.pdf");
				byte[] contentFile = Files.readAllBytes(path01);
				path01 = Paths.get("booksDataFolder/"+resbook.getBookName()+"/book_picture.jpg");
				byte[] bookPhoto = Files.readAllBytes(path01);
				resbook.setBookphoto(bookPhoto);
				resbook.setContentfile(contentFile);
				books.add(resbook);
			  	 }
			  if(books.size()==0)
				  client.sendToClient("-1");
			  else
			  	{
				  ObjectOutput out1 = null;
					ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
					out1 = new ObjectOutputStream(bos1);   
					  out1.writeObject(books);
					  out1.flush();
					  byte[] objbyte1 = bos1.toByteArray();
				  client.sendToClient(objbyte1);
				  System.out.println("sending"+books.size()+"books to client");
			  	}
			break;
		
		case 7:
			 SendMassege="UserIDFound,";
			data=Arrays.asList(s.split(","));
			query="select * from reader where UserID=?";
			pstmt=con.prepareStatement(query);
			pstmt.setString(1,data.get(0));
			 object = pstmt.executeQuery();
			while(object.next()) {
				SendMassege+=object.getString(2);
			}
			query="select * from user where UserID=?";
			pstmt=con.prepareStatement(query);
			pstmt.setString(1,data.get(0));
		    object = pstmt.executeQuery();
			while(object.next()) {
				SendMassege+=","+object.getString(4)+" "+object.getString(5);
			}
			if(SendMassege.equals("UserIDFound,")) {
				SendMassege="notFound,UserID Not Found";
			}
			client.sendToClient(SendMassege);
			
			break;
		case 8:
	         String orderName=null;
			 SendMassege="BookFound,";
			data=Arrays.asList(s.split(","));
			query="select * from book where catalogNumber=?";
			pstmt=con.prepareStatement(query);
			pstmt.setString(1,data.get(0));
			 object = pstmt.executeQuery();
			while(object.next()) {
				SendMassege+=object.getString(2);
				orderName=object.getString(2);
			}
			if(SendMassege.equals("BookFound,")) {
				SendMassege="notFound,catalogNumber Not Found";
				client.sendToClient(SendMassege);
			}
			query="select * from library where BookName=?";
			pstmt=con.prepareStatement(query);
			pstmt.setString(1,orderName);
			object = pstmt.executeQuery();
			while(object.next()) {
				SendMassege+=","+object.getString(7);
			}
			client.sendToClient(SendMassege);
			break;
		case 9:
			 SendMassege="BookFound,";
				data=Arrays.asList(s.split(","));
				query="select * from borrowbook where CatalogNumber=?";
				pstmt=con.prepareStatement(query);
				pstmt.setString(1,data.get(0));
				 object = pstmt.executeQuery();
				while(object.next()) {
					SendMassege+=object.getString(2)+","+object.getString(4)+","+object.getString(5);
				}
				if(SendMassege.equals(null)) {
					SendMassege="NoBookFound";
				}
				client.sendToClient(SendMassege);
			break;
		case 10:
			Date date = new Date();
			String bookName=null;
			int quantity=0;
			data=Arrays.asList(s.split(","));
			query="select * from book where CatalogNumber=?";
			pstmt=con.prepareStatement(query);
			pstmt.setString(1,data.get(0));
			object = pstmt.executeQuery();
			while(object.next()) {
				bookName=object.getString(2); 
			}
			query="select * from library where BookName=?";
			pstmt=con.prepareStatement(query);
			pstmt.setString(1,bookName);
			object = pstmt.executeQuery();
			while(object.next()) {
				quantity=object.getInt(4);
			}
			query="update library set quantity=? where BookName=?";
			pstmt=con.prepareStatement(query);
			pstmt.setInt(1, quantity+1);
			pstmt.setString(2,bookName);
			pstmt.executeUpdate();
			
			query="update book set borrowStatus=? where catalogNumber=?";
			pstmt=con.prepareStatement(query);
			pstmt.setString(1, "not borrowed");
			pstmt.setString(2,data.get(0));
			pstmt.executeUpdate();
			
			query="update history set ReturnStatus=? where ReaderID=?";
			pstmt=con.prepareStatement(query);
			pstmt.setString(1,"returned");
			pstmt.setString(2,data.get(1));
			pstmt.executeUpdate();
			
			query="update OrderBook set OrderBookReady=? where ReaderID=?,BookName=?";
			pstmt=con.prepareStatement(query);
			pstmt.setDouble(1, date.getTime());
			pstmt.setString(2,data.get(1));
			pstmt.setString(3,bookName);
			pstmt.executeUpdate();
			
			query="DELETE FROM borrowbook WHERE CatalogNumber=?";
			pstmt=con.prepareStatement(query);
			pstmt.setString(1,data.get(0));
			pstmt.executeUpdate();
			
			String ReturnReaderStatus=null;
			query="select * from reader where UserID=?";
			pstmt=con.prepareStatement(query);
			pstmt.setString(1,data.get(1));
			object = pstmt.executeQuery();
			while(object.next()) {
				ReturnReaderStatus=object.getString(2);
			}
			if(ReturnReaderStatus.equals("Frozen")) {
				query="update user set userStatus=? where UserID=?";
				pstmt=con.prepareStatement(query);
				pstmt.setString(1,"Active");
				pstmt.setString(2,data.get(1));
				pstmt.executeUpdate();
				
				query="update reader set ReaderStatus=? where UserID=?";
				pstmt=con.prepareStatement(query);
				pstmt.setString(1,"Active");
				pstmt.setString(2,data.get(1));
				pstmt.executeUpdate();
			}
			client.sendToClient("done");
			break;
		case 11:
			String UserID=null;
			String Email = null;
			List<String> ComparDate;
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date1 = new Date();
			ComparDate=data=Arrays.asList(dateFormat.format(date1).split("-"));
			int currentday=Integer.parseInt(ComparDate.get(2));
		    query="select * from borrowbook ";
			pstmt=con.prepareStatement(query);
			 object = pstmt.executeQuery();
			while(object.next()) {
				UserID=object.getString(2);
				ComparDate=data=Arrays.asList(object.getString(5).split("-"));
				int ReturnDate = Integer.parseInt(ComparDate.get(2));	
				if(currentday+1==ReturnDate) {//If just remains one day for returning the book
					query="select * from user where UserID=?";
					pstmt=con.prepareStatement(query);
					pstmt.setString(1,UserID);
					ResultSet object1 = pstmt.executeQuery();
					while(object1.next()) {
						 Email=object1.getString(2);
					}
					SendMailToClient.SendingMail(Email,"One day left to return the book back to the library!!!");
				}
				//if the reader has not return the book in time then update all the details of the reader
				else {
					if(dateFormat.format(date1).compareTo(object.getString(5))>0) {
						int LateReturn=0;
						String borrowStatus = object.getString(6);
						query="select * from reader where UserID=?";
						pstmt=con.prepareStatement(query);
						pstmt.setString(1,UserID);
						ResultSet object2 = pstmt.executeQuery();
						while(object2.next()) {
							LateReturn=object2.getInt(3);
						}
						if(borrowStatus.equals("borrowed")&&LateReturn<2) {
							query="update reader set LateReturn=? , ReaderStatus=?  where UserID=?";
							pstmt=con.prepareStatement(query);
							pstmt.setInt(1,LateReturn+1);
							pstmt.setString(2,"Frozen");
							pstmt.setString(3,UserID);
							pstmt.executeUpdate();
							
							query="update borrowbook set borrowStatus=? where ReaderID=?";
							pstmt=con.prepareStatement(query);
							pstmt.setString(1,"LateReturn");
							pstmt.setString(2,UserID);
							pstmt.executeUpdate();
							
							query="update user set userStatus=? where UserID=?";
							pstmt=con.prepareStatement(query);
							pstmt.setString(1,"Frozen");
							pstmt.setString(2,UserID);
							pstmt.executeUpdate();
						}
						else if(borrowStatus.equals("borrowed")&&LateReturn==2) {//if the reader has been two time late and has late anther time he will bee blocked
							query="update reader set LateReturn=? , ReaderStatus=?  where UserID=?";
							pstmt=con.prepareStatement(query);
							pstmt.setInt(1,LateReturn+1);
							pstmt.setString(2,"Blocked");
							pstmt.setString(3,UserID);
							pstmt.executeUpdate();
							
							query="update borrowbook set borrowStatus=? where ReaderID=?";
							pstmt=con.prepareStatement(query);
							pstmt.setString(1,"LateReturn");
							pstmt.setString(2,UserID);
							pstmt.executeUpdate();
							
							query="update user set userStatus=? where UserID=?";
							pstmt=con.prepareStatement(query);
							pstmt.setString(1,"Blocked");
							pstmt.setString(2,UserID);
							pstmt.executeUpdate();
						}
					}
				}
			}
			break;
		case 12:
			data=Arrays.asList(s.split(","));
			if(data.get(7).equals("Blocked")||data.get(7).equals("Frozen")) {
				client.sendToClient("notFound,"+data.get(7));
				break;
			}
			String OrderStatus=null;
			String ReaderID=null;
			int FlagOrderStatus=0;
			query="select * from OrderBook where BookID=?";
			pstmt=con.prepareStatement(query);
			pstmt.setString(1,data.get(0));
			object = pstmt.executeQuery();
			while(object.next()) {
				OrderStatus=object.getString(5); 
				ReaderID=object.getString(2);
				if(OrderStatus.equals("waiting") && ReaderID.equals(data.get(1))) {
					FlagOrderStatus=1;
				}else {
					FlagOrderStatus=2;
				}
			}
			if(FlagOrderStatus==2) {
				client.sendToClient("notFound,He can't borrow the book ");
				break;
			}
			if(FlagOrderStatus==1) {
				query="DELETE FROM OrderBook WHERE BookID=?";
				pstmt=con.prepareStatement(query);
				pstmt.setString(1,data.get(0));
				pstmt.executeUpdate();
			}
			System.out.println(FlagOrderStatus);
		query="INSERT INTO borrowbook values(?,?,?,?,?,?)";
			pstmt=con.prepareStatement(query);
			pstmt.setString(1,data.get(0));
		    pstmt.setString(2,data.get(1));
			pstmt.setString(3,data.get(2));
			pstmt.setString(4,data.get(3));
			pstmt.setString(5,data.get(4));
			pstmt.setString(6,data.get(5));
			pstmt.executeUpdate();
			query="INSERT INTO history values(?,?,?,?,?)";
			pstmt=con.prepareStatement(query);
			pstmt.setString(1,data.get(1));
		    pstmt.setString(2,data.get(6));
			pstmt.setString(3,data.get(3));
			pstmt.setString(4,data.get(4));
			pstmt.setString(5,"borrowed");
			pstmt.executeUpdate();
			int quantitydown=0;
			query="select * from library where BookName=?";
			pstmt=con.prepareStatement(query);
			pstmt.setString(1,data.get(6));
			object = pstmt.executeQuery();
			while(object.next()) {
				 quantitydown=object.getInt(4); 
			}
			query="update library set quantity=? where BookName=?";
			pstmt=con.prepareStatement(query);
			pstmt.setInt(1,quantitydown-1);
			pstmt.setString(2,data.get(6));
			pstmt.executeUpdate();
			
			query="update book set borrowStatus=? where catalogNumber=?";
			pstmt=con.prepareStatement(query);
			pstmt.setString(1, "borrowed");
			pstmt.setString(2,data.get(0));
			pstmt.executeUpdate();
			client.sendToClient("notFound,Borrowed has been done");
		break;
	/*	case 22:
			 SendMassege="User not existed";
			data=Arrays.asList(s.split(","));
			query="select * from reader where UserID=?and ReaderStatus=?";
			pstmt=con.prepareStatement(query);
			pstmt.setString(1,data.get(0));
			pstmt.setString(2,data.get(1));
			 object = pstmt.executeQuery();
			while(object.next()) {
				SendMassege+=object.getString(1)+","+object.getString(2);
			}
			if(SendMassege.equals("User not existed")) {
				System.out.println ("Error Login");
				client.sendToClient("Error Login");
			}
			else {
			System.out.println ("Login Done");
			client.sendToClient("Login Done");
			}
			break;
			
			
	
		case 23:
			data=Arrays.asList(s.split(","));
			query="INSERT INTO user values(?,?,?,?,?,?,?,?)";
			pstmt=con.prepareStatement(query);
			pstmt.setString(1, data.get(0));
			pstmt.setString(2, "1");
			pstmt.setString(3, "1");
			pstmt.setString(4, data.get(4));
			pstmt.setString(5, data.get(5));
			pstmt.setString(6, data.get(6));
			pstmt.setString(7, data.get(1));
			pstmt.setString(8,"99");
			pstmt.executeUpdate();
			System.out.println("register done");
			client.sendToClient("okkkkkk");
			break;*/
		
		case 22:///////login
			SendMassege="false";
			String userType="false";
			String LoginStatus="false";
			data=Arrays.asList(s.split(","));
			query="select * from user where username=?";
			pstmt=con.prepareStatement(query);
			pstmt.setString(1,data.get(0));
			object = pstmt.executeQuery();
			while(object.next()) {
				SendMassege=object.getString(3);
				LoginStatus=object.getString(7);
				userType=object.getString(6);
				}
			if(SendMassege.equals(data.get(1))&&LoginStatus.equals("0")) {
				SendMassege=("user exsist");
				client.sendToClient(userType);
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
			query="INSERT INTO reader values(?,?,?,?)";
			pstmt=con.prepareStatement(query);
			pstmt.setString(1,data.get(0));
			pstmt.setString(2,"Active");
			pstmt.setString(3,"0");			
			pstmt.setInt(4,SendMassege4);			
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
			SendMailToClient.SendingMail(data.get(0),SendMassege);
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
			
		default:
			break;
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	  try {
		con.close();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  
  }

    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
 
  public static void main(String[] args) 
  {
    int port = 5555; //Port to listen on

    try
    {
    // String test=args[3];//tempt off 
     //db=new DbContoller(args[0],args[1],args[2],args[3]);
     db=new DbContoller("root","password","collageproject","77.138.40.146");
    }
    catch(ArrayIndexOutOfBoundsException t)
    {
     db=new DbContoller(args[0],args[1],args[2]);
    }
	
    ServerController_enw sv = new ServerController_enw(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
