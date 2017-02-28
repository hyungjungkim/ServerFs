package fileprocessor.server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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

public class FileUploadRunnable implements Runnable {
	//
	private FileInfo fileInfo = null;
	private Socket sock = null;
	private DataInputStream dis = null;
	private BufferedInputStream bis = null;
	private FileOutputStream fos = null;
	private DBStore dbStore;
	private HandleInfo handleinfo;
	private QueueManager queuemanager;
	private DBStoreFactory factory;
	private OutputStreamWriter writer;
	private Object lockObject;

	public FileUploadRunnable() {
		queuemanager = QueueManager.getInstance();
		factory = DBStoreFactory.getInstance();
		this.lockObject = new Object();
	}

	@Override
	public void run() {
		//
		while (true) {
			try {

				this.handleinfo = queuemanager.getUploadQueue().take();
				System.out.println("[FileUpload] 큐로부터 획득");
				this.fileInfo = this.handleinfo.getFileInfo();
				this.sock = this.handleinfo.getSock();
				this.dbStore = factory.getDBStoreInstance(fileInfo.getUserId());
//				synchronized (this.lockObject) {
					writer = new OutputStreamWriter(sock.getOutputStream(), "UTF-8");
					this.FileUpload(this.fileInfo);
//				}

			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public synchronized List<DirFile> FileUpload(FileInfo fileInfor) throws IOException {
		//
		String clientPath = fileInfor.getNewPath();
		System.out.println("Server FileUpload Path: " + clientPath);

		String serverSavePath = dbStore.FileUpload(clientPath); // fromDBStore
		byte[] contentBytes = new byte[1024];

		try {
			System.out.println("Before Stream");
			dis = new DataInputStream(sock.getInputStream());
			bis = new BufferedInputStream(sock.getInputStream());
			fos = new FileOutputStream(serverSavePath);
			System.out.println("After Stream");
			int bytes = dis.readInt();
			System.out.println("bytes = " + bytes);
			// while ((dis.available()>0)) {
			int count = 0;
			try {
				while (true) {
					
					int bytess = bis.read(contentBytes, 0, contentBytes.length); 
//					System.out.println("FileUpload in Server Count : " + bytess);
					count += bytess;
					fos.write(contentBytes, 0, bytess);
					
					if(count >= bytes){
						System.out.println("빠져나간다아아아아~~~");
						fos.close();
						break;
					}
					
				}
			} catch (EOFException e1) {
				e1.printStackTrace();
			}

			System.out.println("okaokokokokokokokok count == -1");
		} catch (IOException e) {
			e.getStackTrace();
		} finally {
			System.out.println("in finally");
			

		}		
		System.out.println("okokokoko after finally");
		// current list of current depth (from DB) // only directorypath from
		// dbstore
		String parentPath = clientPath.substring(0, clientPath.lastIndexOf("/"));
		System.out.println(parentPath);
		// Serializable
		ListInfor retList = new ListInfor();
		retList.setListInfor(dbStore.ShowList(parentPath));
		String retListStr = new Gson().toJson(retList);
		System.out.println();
		try {
			// jobj.put("list", retList);
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
