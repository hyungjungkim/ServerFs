package network.server;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import db.domain.FileInfo;
import db.domain.HandleInfo;

public class HandleInfoQueue {
	//
	private ArrayBlockingQueue<HandleInfo> queue;
	
	public HandleInfoQueue() {
		
		this.queue = new ArrayBlockingQueue<>(100, true);
		
	}

	public void put(HandleInfo fileInfo) {
		//
		this.queue.add(fileInfo);
		
	}
	
	public HandleInfo take() {
		
		try {
			
			return this.queue.take();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return null;
		
	}
	
}
