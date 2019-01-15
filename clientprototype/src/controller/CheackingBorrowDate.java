package controller;

import java.io.IOException;

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
