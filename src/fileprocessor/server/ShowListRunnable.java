package fileprocessor.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

import com.google.gson.Gson;

import db.domain.DirFile;
import db.domain.FileInfo;
import db.domain.HandleInfo;
import db.domain.ListInfor;
import db.store.DBStore;
import db.store.DBStoreFactory;
import network.server.ProcessRouterLogic;
import network.server.QueueManager;

public class ShowListRunnable implements Runnable {
	//
	private FileInfo fileInfo;
	private Socket sock;
	private DBStore dbStore;
	private HandleInfo handleInfo;
	private QueueManager queuemanager;
	private DBStoreFactory factory;
	private OutputStreamWriter writer;
	private Object lockObject;

	public ShowListRunnable() {
		//
		queuemanager = QueueManager.getInstance();
		factory = DBStoreFactory.getInstance();
		this.lockObject = new Object();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {

				this.handleInfo = queuemanager.getShowlistQueue().take();
				System.out.println("[ShowList] ≈•∑Œ∫Œ≈Õ »πµÊ");
				this.fileInfo = this.handleInfo.getFileInfo();
				this.sock = this.handleInfo.getSock();
				this.dbStore = factory.getDBStoreInstance(fileInfo.getUserId());
				System.out.println("servershowlist called");
//				synchronized (this.lockObject){
					this.writer = new OutputStreamWriter(sock.getOutputStream(), "UTF-8");
					this.ShowList(this.fileInfo.getUserId(), this.fileInfo.getCurrentPath());
//				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}

	public List<DirFile> ShowList(String userId, String currentPath) throws IOException {
		//
		ListInfor retList = new ListInfor();
		retList.setListInfor(dbStore.ShowList(currentPath));
		String retListStr = new Gson().toJson(retList);
		try {
			// jobj.put("list", retList);
			writer.write(retListStr + "\n");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.queuemanager.getUserQueue().put(this.handleInfo.getSocketAccessible());
		this.handleInfo.getSocketAccessible().setAccessible(true);
		return null;
	}
}
