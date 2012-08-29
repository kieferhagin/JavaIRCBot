package javaBot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import exceptions.NotConnectedException;

public class IRCInputReader extends Thread {
	
	private BufferedReader in;
	private IRCConnection connection;
	private boolean disconnect = false;
	
	public IRCInputReader(IRCConnection connection) throws IOException {
		this.connection = connection;
		this.in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	}
	
	@Override
	public void run() {
		try {
			String line;
			while ((line = in.readLine()) != null && !disconnect) {
				this.onReceive(line);
			}
			
		} catch (IOException e) {}
		
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void onReceive(String line) {
		try {
			connection.parse(line);
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect() {
		this.disconnect = true;
	}

}
