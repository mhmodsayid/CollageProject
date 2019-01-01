package control;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.sql.*;
import java.util.Arrays;
import java.util.List;

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
	  	getmessagecommand(msg,client);
	    System.out.println("Message received: " + msg + " from " + client);
	    
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
	  
  }
	 
  
  public void getmessagecommand(Object message, ConnectionToClient client) {
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
		case 4:
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
     String test=args[3];
      db=new DbContoller(args[0],args[1],args[2],args[3]);
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