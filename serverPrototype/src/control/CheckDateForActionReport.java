package control;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
/**

 * tread that runs once a day and checks the date to perform changes for the actions
 * report that the manager can watch.
 * this class calls the server in two case, if the last day of month
 * calls case 37 that gathers data and puts it in the DB.
 * if first day of month calls case 38 that initialize the count of late returns in the DB
 * @author Bayan
 */


public class CheckDateForActionReport implements Runnable {
	
	@Override
	public void run() {

		while(true) {
			try {
				LocalDate today=LocalDate.now(); 
				LocalDate first = today.with(TemporalAdjusters.firstDayOfMonth());
				LocalDate last = today.with(TemporalAdjusters.lastDayOfMonth());
				ServerController SC = new ServerController(5555);
				if(today.equals(last)) {
					SC.handleMessageFromClient("37", null);
				}
				else if(today.equals(first)) {
					SC.handleMessageFromClient("38", null);
				}
				Thread.sleep(100*60*60*24);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}
	}
}
