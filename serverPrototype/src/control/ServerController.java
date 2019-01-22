package control;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

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
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import SendingMail.SendMailToClient;
import entity.Book;
import entity.Reader;
import ocsf.server.*;

public class ServerController extends AbstractServer 
{
 
  public static DbContoller db;
 
  public ServerController(int port) 
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
					
					if(e.toString().contains("Duplicate")) {
						System.out.println("the book already exists");
					}
					try {
						client.sendToClient("the book already exists");
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
			if(SendMassege.equals(null)) {
				SendMassege="UserIDNotFound";
			}
			client.sendToClient(SendMassege);
			break;
			
		case 8:
			 SendMassege="BookFound,";
			data=Arrays.asList(s.split(","));
			query="select * from book where CatalogeNumber=?";
			pstmt=con.prepareStatement(query);
			pstmt.setString(1,data.get(0));
			 object = pstmt.executeQuery();
			while(object.next()) {
				SendMassege+=object.getString(1)+","+object.getString(11);
			}
			if(SendMassege.equals(null)) {
				SendMassege="NoBookFound";
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
			int quantity=0;
			data=Arrays.asList(s.split(","));
			query="select * from book where CatalogeNumber=?";
			pstmt=con.prepareStatement(query);
			pstmt.setString(1,data.get(0));
			 object = pstmt.executeQuery();
			while(object.next()) {
				 quantity+=object.getInt(8); 
			}
			if(SendMassege.equals(null)) {
				SendMassege="NoBookFound";
			}
			query="update book set quantity=? where CatalogeNumber=?";
			pstmt=con.prepareStatement(query);
			pstmt.setInt(1, quantity+1);
			pstmt.setString(2,data.get(0));
			pstmt.executeUpdate();
			/*query="delete from borrowbook where CatalogeNumber = ?";
			pstmt=con.prepareStatement(query);
			pstmt.setString(1,data.get(0));
			pstmt.executeUpdate();*/
			client.sendToClient("done");
			break;
		case 11:
			List<String> ComparDate;
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			ComparDate=data=Arrays.asList(dateFormat.format(date).split("-"));
			int currentday=Integer.parseInt(ComparDate.get(2));
		    query="select * from borrowbook ";
			pstmt=con.prepareStatement(query);
			 object = pstmt.executeQuery();
			while(object.next()) {
				ComparDate=data=Arrays.asList(object.getString(5).split("-"));
				int ReturnDate = Integer.parseInt(ComparDate.get(2));	
				if(currentday+1==ReturnDate) {
					System.out.println("yes");
					SendMailToClient.SendingMail("m","b");
				}
				else {
					if(dateFormat.format(date).compareTo(object.getString(5))>0) {
						System.out.println("si");
					}
				}
			}
			break;
			
			
		case 22:
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
			break;
			
		default:
			break;
		}
	} catch (Exception e) {
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
	
    ServerController sv = new ServerController(port);
    
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
