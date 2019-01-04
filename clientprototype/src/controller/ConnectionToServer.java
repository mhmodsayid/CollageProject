package controller;

import java.io.IOException;

import ocsf.client.ChatClient;
import ocsf.client.ChatIF;

public class ConnectionToServer {
	static int port;
	static String host;
	public static void sendData(ChatIF clientUI, Object message) throws IOException {//send the message to the server
		new ChatClient(host, port, clientUI).handleMessageFromClientUI(message);
	}
	public static void initializeServerConnection(int port, String host) {//insert the ip of the server and the port
		ConnectionToServer.port = port;
		ConnectionToServer.host = host;
	}

}
