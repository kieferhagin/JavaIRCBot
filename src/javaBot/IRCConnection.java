package javaBot;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import exceptions.NotConnectedException;

public class IRCConnection {
	
	private PrintStream out;
	private String host;
	private int port;
	private Socket socket;
	private Thread inThread;
	private IRCInputReader in;
	
	public static final int DEFAULT_PORT = 6667;
	
	public IRCConnection(String host, int port) {
		this.host = host;
		this.port = port;
		this.inThread = null;
	}
	
	protected void connect() throws UnknownHostException, IOException {
		this.socket = new Socket(host, port);
		this.out = new PrintStream(socket.getOutputStream());
		this.in = new IRCInputReader(this);
		this.inThread = new Thread(in);
		this.inThread.start();
	}
	
	protected void send(String data) throws NotConnectedException {
		if (!socket.isConnected()) {
			throw new NotConnectedException();
		}
		System.out.println("TO SERVER: " + data);
		this.out.println(data);
	}
	
	public InputStream getInputStream() throws IOException {
		return this.socket.getInputStream();
	}
	
	public void parse(String line) throws NotConnectedException {
		System.out.println(line);
	}
	
	public String getHost() {
		return this.host;
	}
	
	public int getPort() {
		return this.port;
	}
	
	public boolean isConnected() {
		return this.socket.isConnected();
	}
	
	protected void disconnect() {
		this.in.disconnect();
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
