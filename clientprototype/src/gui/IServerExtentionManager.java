package gui;

import java.io.IOException;

import ocsf.client.ChatIF;

public interface IServerExtentionManager {
	public void sendData(ChatIF clientUI, Object message) throws IOException;
	public void initializeServerConnection(int port, String host);
}
