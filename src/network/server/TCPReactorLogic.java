package network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import db.domain.SocketAccessible;

public class TCPReactorLogic implements TCPReactor {

	private ServerSocket servSock;
	private QueueManager queueManager;
	public static Vector<Boolean> ISACCESSIBLE;
	public static int USERS = 0;
	public TCPReactorLogic() {
		// TODO Auto-generated constructor stub
		try {
			
			this.queueManager = QueueManager.getInstance();
			this.servSock = new ServerSocket(9900);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
	public void startServer() {
		// TODO Auto-generated method stub
		System.out.println("[Server] Server started..");
		try{
			Socket sock = null;
			while(true){
				
				sock = this.servSock.accept();
				SocketAccessible socketAccessible = new SocketAccessible(sock);
				this.queueManager.getUserQueue().put(socketAccessible);
//				ProcessRouterLogic processRouterLogic = new ProcessRouterLogic();
				System.out.println("[Server] " + sock.getInetAddress() + " logged in.");
//				Thread processRouterThread = new Thread(processRouterLogic);
				System.out.println("[Server] Router starts.");
//				processRouterThread.start();
				
			}
			
		}catch(IOException e){
			
			e.printStackTrace();
			
		}
		
	}
	
	
}
