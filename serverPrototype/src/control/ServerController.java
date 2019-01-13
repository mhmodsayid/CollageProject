package control;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

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
					    query="INSERT INTO book values(?,?,?,?,?,?,?,?,?,?,?)";
						pstmt=con.prepareStatement(query);
						pstmt.setString(1,book.getBookName());
						pstmt.setString(2,book.getPublisherName());
						pstmt.setString(3,book.getBookEdite());
						pstmt.setString(4,book.getDateOfPrint());
						pstmt.setString(5,book.getBookCatagory());
						pstmt.setString(6,book.getBookDescription());
						pstmt.setString(7,book.getCatalogNumber());
						pstmt.setInt(8,book.getNumberOFCopies());
						pstmt.setString(9,book.getDatePurchased());
						pstmt.setString(10,book.getPositionOnTheShelf());
						pstmt.setInt(11,book.getBookStatus());
						pstmt.executeUpdate();
						client.sendToClient("addBookSuccess");
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			//if its another page 
		
		
		
		
	}
	  	
	   
	      
	    
	  }
 
  public void createObject(ResultSet result,Object obj) throws SQLException {
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
				book.setNumberOFCopies(Integer.parseInt(result.getString(8)));
				book.setDatePurchased(result.getString(9));
				book.setPositionOnTheShelf(result.getString(10));
				book.setBookStatus(Integer.parseInt(result.getString(11)));
		  }
	  }
	  
  }
	 
  
  public void getmessagecommand(Object message, ConnectionToClient client) {
	  try {
		String str=(String) message;
	} catch (Exception e1) {
		System.out.println("not a string");
	}
	  
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
			query="select * from book where `Book Name`=?";
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
