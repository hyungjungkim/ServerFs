package fileprocessor.server;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

import db.domain.FileInfo;
import db.domain.HandleInfo;
import db.store.DBStore;
import db.store.DBStoreFactory;
import network.server.ProcessRouterLogic;
import network.server.QueueManager;

public class FileDownloadRunnable implements Runnable {
	//
	private FileInfo fileInfo = null;
	private Socket sock = null;
	private DataOutputStream dos = null;
	private FileInputStream fis = null;
	private DBStore dbStore;
	private QueueManager queuemanager;
	private HandleInfo handleInfo;
	private DBStoreFactory factory;
	private Object lockObject;
	
	public FileDownloadRunnable() {
		//
		queuemanager = QueueManager.getInstance();
		factory = DBStoreFactory.getInstance();
		this.lockObject = new Object();
		
	}

	@Override
	public void run() {
		//
		while (true) {
			try {

				this.handleInfo = queuemanager.getDownloadQueue().take();
				this.fileInfo = this.handleInfo.getFileInfo();
				this.sock = this.handleInfo.getSock();
				this.dbStore = factory.getDBStoreInstance(fileInfo.getUserId());
//				synchronized (this.lockObject){
					this.FileDownload(this.fileInfo);
//				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				// TODO stop()
				e.printStackTrace();
			}
		}
	}

	public void FileDownload(FileInfo fileInfor) {
		// client requesting path => to DB
		String clientPath = fileInfor.getCurrentPath();
		String serverDownPath = dbStore.FileDownload(clientPath); // from DB
		byte[] contentBytes = new byte[1024];

		try {
			dos = new DataOutputStream(sock.getOutputStream());
			fis = new FileInputStream(serverDownPath);
			int bytes = 0;
			while (true) {
				int count = fis.read(contentBytes);
				if (count == -1) {
					break;
				}
				bytes += count;
			}
			dos.writeInt(bytes);
			fis.close();
			fis = new FileInputStream(serverDownPath);
			while (true) {

				int count = fis.read(contentBytes);
				System.out.println("count = " + count);

				if (count == -1) {
					break;
				}
				dos.write(contentBytes, 0, count);

			}
			System.out.println("server filedownload is finished");
		} catch (IOException e) {
			e.getStackTrace();
		}
		this.queuemanager.getUserQueue().put(this.handleInfo.getSocketAccessible());
		this.handleInfo.getSocketAccessible().setAccessible(true);
	}
}
