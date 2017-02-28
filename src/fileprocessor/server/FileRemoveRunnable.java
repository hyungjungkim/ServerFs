package fileprocessor.server;

import java.io.File;
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

public class FileRemoveRunnable implements Runnable {
	//
	private FileInfo fileInfo = null;
	private Socket sock = null;
	private DBStore dbStore;
	private HandleInfo handleInfo;
	private QueueManager queuemanager;
	private DBStoreFactory factory;
	private OutputStreamWriter writer;
	private Object lockObject;
	
	public FileRemoveRunnable() {
		
		queuemanager = QueueManager.getInstance();
		factory = DBStoreFactory.getInstance();
		this.lockObject = new Object();
	}

	@Override
	public void run() {
	
		while (true) {
			try {

				this.handleInfo = queuemanager.getRmvFileQueue().take();
				this.fileInfo = this.handleInfo.getFileInfo();
				this.sock = this.handleInfo.getSock();
				this.dbStore = factory.getDBStoreInstance(fileInfo.getUserId());
				//this.out = this.handleInfo.getOut();
//				synchronized (this.lockObject){
					writer = new OutputStreamWriter(sock.getOutputStream(), "UTF-8");
					this.FileRemove(this.fileInfo.getUserId(), this.fileInfo.getCurrentPath());
//				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	public List<DirFile> FileRemove(String userId, String currentPath) throws IOException {
		// client requesting path => to DB
		String clientPath = currentPath;
		System.out.println("FileRemove clientPath: " + clientPath);
		String fileRemovePath = dbStore.FileRemove(clientPath); // from DB
		System.out.println("FileRemove fileRemovePath: " + fileRemovePath);
		File file = new File(fileRemovePath);
		if (file.delete()) {
			System.out.println(file.getName() + " is deleted!");
		} else {
			System.out.println("Delete operation is failed.");
		}
		// current list of current depth (from DB)
		String parentPath = clientPath.substring(0, clientPath.lastIndexOf("/"));
		
		// Serializable
		ListInfor retList = new ListInfor();
		retList.setListInfor(dbStore.ShowList(parentPath));
		String retListStr = new Gson().toJson(retList);
		try {
			//jobj.put("list", retList);
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
