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

public class DirectoryCreateRunnable implements Runnable {
	//
	private Socket sock = null;
	private FileInfo fileInfo = null;
	private ObjectOutputStream out = null;
	private QueueManager queuemanager;
	private HandleInfo handleInfo;
	private DBStore dbStore;
	private DBStoreFactory factory;
	private OutputStreamWriter writer;
	private Object lockObject;

	public DirectoryCreateRunnable() {
		
		queuemanager = QueueManager.getInstance();
		factory = DBStoreFactory.getInstance();
		this.lockObject = new Object();
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//
		while (true) {
			try {

				this.handleInfo = queuemanager.getMkDirQueue().take();
				this.fileInfo = this.handleInfo.getFileInfo();
				this.sock = this.handleInfo.getSock();
				this.dbStore = factory.getDBStoreInstance(fileInfo.getUserId());
//				synchronized (this.lockObject){
					writer = new OutputStreamWriter(sock.getOutputStream(), "UTF-8");
					this.DirectoryCreate(this.fileInfo);
//				}			
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	public List<DirFile> DirectoryCreate(FileInfo fileInfor) throws IOException {
		// TODO client requesting path => to DB
		String clientPath = fileInfor.getCurrentPath();
		String dirCreatePath = dbStore.DirectoryCreate(clientPath); // from DB
		File file = new File(dirCreatePath);
		if (!file.exists()) {
			file.mkdirs();
			System.out.println("Directory Name = " + dirCreatePath); // dedug
		}
		String parentPath = clientPath.substring(0, clientPath.lastIndexOf("/"));

		// Serializable
		ListInfor retList = new ListInfor();
		retList.setListInfor(dbStore.ShowList(parentPath));
		String retListStr = new Gson().toJson(retList);
		try {
			writer.write(retListStr+"\n");
			writer.flush();
		} catch (IOException e) {
			// TODO: handle exception
		}
		this.queuemanager.getUserQueue().put(this.handleInfo.getSocketAccessible());
		this.handleInfo.getSocketAccessible().setAccessible(true);
		return null;
	}
}
