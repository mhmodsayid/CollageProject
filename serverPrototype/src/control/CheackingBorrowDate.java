package control;

import java.io.IOException;
/**
 * @author Ammar Khutba
 * thread that work one time every day and send to server
 * to check if the returning day has bass and update all the details of the user
 * and to alert the reader if just remains one day for returning the book
 */


public class CheackingBorrowDate implements Runnable {
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			try {
				ServerController SC = new ServerController(5555);
				SC.handleMessageFromClient("11", null);
				Thread.sleep(100*60*60*24);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
}
