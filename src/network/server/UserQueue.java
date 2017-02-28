package network.server;

import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

import db.domain.SocketAccessible;

public class UserQueue {
	//
	private ArrayBlockingQueue<SocketAccessible> queue;
	
	public UserQueue() {
		
		this.queue = new ArrayBlockingQueue<>(100, true);
		
	}

	public void put(SocketAccessible sock) {
		//
		this.queue.add(sock);
		
	}
	
	public SocketAccessible take() {
		
		try {
			
			return this.queue.take();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return null;
		
	}
	
}
