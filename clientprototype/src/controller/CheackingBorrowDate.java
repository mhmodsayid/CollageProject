package controller;

import java.io.IOException;
/**
 * @author Ammar Khutba
 * thread that work one time every day and send to server
 * to check if the returning day has bass and update all the details of the user
 * and to alert the reader if just remains one day for returning the book
 */

import ocsf.client.ChatIF;

public class CheackingBorrowDate implements Runnable, ChatIF {
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			try {
				ConnectionToServer.sendData(this,"11");
				Thread.sleep(100*60*60*24);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	@Override
	public void display(Object msg) {
		// TODO Auto-generated method stub
		
	}
}
