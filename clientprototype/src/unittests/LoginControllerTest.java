package unittests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import gui.LoginController;
import gui.StubServerExtentionManager;

public class LoginControllerTest {

	LoginController loginController;

	@Before
	public void setUp() throws Exception {
		loginController = new LoginController(new StubServerExtentionManager());// Initialize the server type in
																				// loginController to Stubserver
	}

	@Test
	public void testClearAll() {
		//Test the clear all method insert a value in the fields run the method check if all the fields are empty
		loginController = new LoginController(new StubServerExtentionManager());
		loginController.setUserName("mhmod");
		loginController.setPassword("123");
		loginController.ClearAll(null);
		assertTrue("clear all fillds",
				loginController.getPassword().equals("") && loginController.getUserName().equals(""));
	}

	@Test 
	public void testConnectToServer() {
		//connect to server method needed IP and port
		
		//run the method with IP and port available should return true
		//run the method with stub server not the real server method
		loginController = new LoginController(new StubServerExtentionManager());
		loginController.setIpAddress("127.0.0.1");
		loginController.setPortNumber("5555");
		assertTrue("all the filds is avalible", loginController.connectToServer(null));

		//run the method without IP but with a port should return false
		loginController.setIpAddress("");
		loginController.setPortNumber("5555");
		assertFalse("ip null", loginController.connectToServer(null));

		//run the method without Port should return false
		loginController.setIpAddress("127.0.0.1");
		loginController.setPortNumber("");
		assertFalse("port null", loginController.connectToServer(null));
 
	}

	@Test
	public void testMoveToMainPage() {
			//login user name and password method and send the data to the server
		
			//trying to login without password should return false
		try {
			loginController.setUserName("mahmoud");
			loginController.setPassword("");
			assertFalse("true", loginController.moveToMainPage(null));
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			//trying to login without username and password should return false
		try {
			loginController.setUserName("");
			loginController.setPassword("");
			assertFalse("true", loginController.moveToMainPage(null));
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void display() {//This method is for the information about the user from the server who trying to login
		
		loginController = new LoginController(new StubServerExtentionManager());
		
		//The Server has username and password with active account
		StubServerExtentionManager.username="mahmoud";
		StubServerExtentionManager.password="123";
		StubServerExtentionManager.status="Active";
		
		//trying to login with the active user the display method should get in active mode 
		try {	
			loginController.setUserName("mahmoud");
			loginController.setPassword("123");
			assertTrue("true", loginController.moveToMainPage(null));
			assertTrue("true", LoginController.message.equals("Active")); 
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Trying to login with active user with wrong password the display method should get into not exists mode and a message should popup
		try {
			loginController.setUserName("mahmoud");
			loginController.setPassword("453");
			assertTrue("true", loginController.moveToMainPage(null));
			assertTrue("true", LoginController.message.equals("not exsist"));
		} catch (IOException | InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//The server has user with frozen account
		StubServerExtentionManager.username="ibrahem";
		StubServerExtentionManager.password="124";
		StubServerExtentionManager.status="Frozen";
		
		//Frozen account trying to login the server return its a Frozen account and display method should get into Frozen account mode and a message will appear
		try {
			loginController.setUserName("ibrahem");
			loginController.setPassword("124");
			assertTrue("true", loginController.moveToMainPage(null));
			assertTrue("true", LoginController.message.equals("Frozen")); 
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}
		
		//Server has Blocked account
		StubServerExtentionManager.username="byan";
		StubServerExtentionManager.password="125";
		StubServerExtentionManager.status="user was blocked";
		
		//Blocked account trying to login the server return it blocked account the display method will get into blocked mode and a message will appear
		try {
			loginController.setUserName("byan");
			loginController.setPassword("125");
			assertTrue("true", loginController.moveToMainPage(null));
			assertTrue("true", LoginController.message.equals("blocked")); 
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//the server has account already login
		StubServerExtentionManager.username="faris";
		StubServerExtentionManager.password="126";
		StubServerExtentionManager.status="this account is logged in already";
		
		//already login account trying to login the server return it already login the display method will get in already login mode and message will appear
		try {
			loginController.setUserName("faris");
			loginController.setPassword("126");
			assertTrue("true", loginController.moveToMainPage(null));
			assertTrue("true", LoginController.message.equals("already logged in")); 
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	

}
