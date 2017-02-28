package fileprocessor.server;

import java.io.File;
import java.io.IOException;
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

public class ChangeNameRunnable implements Runnable {
	//
	private Socket sock;
	private FileInfo fileInfo;
	private HandleInfo handleinfo;
	private QueueManager queuemanager;
	private DBStore dbStore;
	private DBStoreFactory factory;
	private OutputStreamWriter writer;
	private Object lockObject;
	
	public ChangeNameRunnable() {
		// Constructor
		queuemanager = QueueManager.getInstance();
		factory = DBStoreFactory.getInstance();
		this.lockObject = new Object();
		
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			
			try {

				this.handleinfo = queuemanager.getCngFileNameQueue().take();
				
				System.out.println("take °É¸²");
				this.sock = this.handleinfo.getSock();
				this.fileInfo = this.handleinfo.getFileInfo();
				this.dbStore = factory.getDBStoreInstance(fileInfo.getUserId());
//				synchronized (this.lockObject){
					writer = new OutputStreamWriter(sock.getOutputStream(), "UTF-8");
					this.ChangeName(fileInfo.getUserId(), this.fileInfo.getCurrentPath(), this.fileInfo.getNewPath());
//				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

	public List<DirFile> ChangeName(String userId, String currentPath, String newPath) throws IOException {
		String fileName = newPath.substring(newPath.lastIndexOf("/")+1, newPath.length());
		String[] fileStr = dbStore.ChangeName(currentPath, fileName);
		
		File currentFile = new File(fileStr[0]);
		File newnameFile = new File(fileStr[1]);
		
		if(currentFile.renameTo(newnameFile)){
			System.out.println(currentFile.getName() + " is changed!");
		}else{
			System.out.println("Change Operation is failed");
		}
		
		String parentPath = currentPath.substring(0, currentPath.lastIndexOf("/"));
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
		this.queuemanager.getUserQueue().put(this.handleinfo.getSocketAccessible());
		this.handleinfo.getSocketAccessible().setAccessible(true);
		return null;
	}
}