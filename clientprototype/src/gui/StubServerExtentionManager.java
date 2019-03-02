package gui;

import java.util.Arrays;
import java.util.List;

import ocsf.client.ChatIF;

public class StubServerExtentionManager implements IServerExtentionManager {

	public static String username;
	public static String password;
	public static String status;

	@Override
	public void sendData(ChatIF clientUI, Object message) {
		String s = (String) message;
		LoginController loginanswer = new LoginController();
		s = s.substring(2, s.length());
		List<String> data = Arrays.asList(s.split(","));

		if (status.equals("user was blocked") || status.equalsIgnoreCase("this account is logged in already")) {
			if (username.equals(data.get(0))) {
				if (data.get(1).equals(password))
					loginanswer.display(status);
				else
					loginanswer.display("user not exsist");
			}
		} else {
			if (username.equals(data.get(0))) {
				if (data.get(1).equals(password))
					loginanswer.display(status + ",Libraryan,312317894");
				else
					loginanswer.display("user not exsist");
			}
		}
	}

	@Override
	public void initializeServerConnection(int port, String host) {
		// TODO Auto-generated method stub

	}

}
