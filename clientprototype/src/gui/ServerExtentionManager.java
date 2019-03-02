package gui;

import java.io.IOException;

import controller.ConnectionToServer;
import ocsf.client.ChatIF;

public class ServerExtentionManager implements IServerExtentionManager {

	@Override
	public void sendData(ChatIF clientUI, Object message) throws IOException {
		ConnectionToServer.sendData(clientUI,message);

	}

	@Override
	public void initializeServerConnection(int port, String host) {
		ConnectionToServer.initializeServerConnection(port, host);

	}

}
