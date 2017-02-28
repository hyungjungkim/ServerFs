package db.domain;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class HandleInfo {
	
	private SocketAccessible socketAccessible;
//	private boolean isAccessible;
	private FileInfo fileInfo;
	//private ObjectInputStream in;	
	//private ObjectOutputStream out;	
//	private DataInputStream dis;
//	private DataOutputStream dos;
	//private JSONObject jobj;
//	private OutputStreamWriter writer = null;
	//private BufferedReader reader = null;
	public HandleInfo(SocketAccessible socketAccessible, FileInfo fileInfo) {
		
		this.socketAccessible = socketAccessible;
		this.fileInfo = fileInfo;
//		this.isAccessible = isAccessible;
		
	}

//	public BufferedReader getReader() {
//		return reader;
//	}
//
//	public void setReader(BufferedReader reader) {
//		this.reader = reader;
//	}

	public Socket getSock() {
		return socketAccessible.getSock();
	}

	public FileInfo getFileInfo() {
		return fileInfo;
	}

	public void setSock(Socket sock) {
		this.socketAccessible.setSock(sock);
	}

	public void setFileInfo(FileInfo fileInfo) {
		this.fileInfo = fileInfo;
	}

	public boolean isAccessible() {
		return this.socketAccessible.isAccessible();
	}

	public void setAccessible(boolean isAccessible) {
		this.socketAccessible.setAccessible(isAccessible);
	}

	public SocketAccessible getSocketAccessible() {
		return socketAccessible;
	}

	public void setSocketAccessible(SocketAccessible socketAccessible) {
		this.socketAccessible = socketAccessible;
	}
	
	
	
}
