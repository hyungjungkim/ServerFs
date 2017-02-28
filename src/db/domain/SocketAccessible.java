package db.domain;

import java.net.Socket;

public class SocketAccessible{
	
	private Socket sock;
	private boolean isAccessible;

	
	
	public SocketAccessible(Socket sock) {
		
		this.sock = sock;
		this.isAccessible = true;
	}

	public Socket getSock() {
		return sock;
	}

	public void setSock(Socket sock) {
		this.sock = sock;
	}

	public boolean isAccessible() {
		return isAccessible;
	}

	public void setAccessible(boolean isAccessible) {
		this.isAccessible = isAccessible;
	}
	
	
	
}
