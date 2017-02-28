package fileprocessor.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.google.gson.Gson;

import db.domain.FileInfo;
import db.domain.HandleInfo;
import db.store.DBStore;
import db.store.DBStoreFactory;
import db.store.UserStore;
import network.server.QueueManager;

public class RegistUserRunnable implements Runnable {
	//
	private QueueManager queuemanager;
	private Socket sock;
	// private DBStore dbStore;
	private OutputStreamWriter writer;
	private HandleInfo handleInfo;
	private FileInfo fileInfo;
	private DBStoreFactory factory;
	private Object lockObject;
	// private UserStore userStore;
	private BufferedReader reader;
	private UserStore userStore;

	public RegistUserRunnable() {
		queuemanager = QueueManager.getInstance();
		factory = DBStoreFactory.getInstance();
		this.lockObject = new Object();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {

				this.handleInfo = queuemanager.getRegisterUserQueue().take();
				this.fileInfo = this.handleInfo.getFileInfo();
				this.sock = this.handleInfo.getSock();
				// this.out = this.handleInfo.getOut();
				// synchronized (this.lockObject){
				writer = new OutputStreamWriter(sock.getOutputStream(), "UTF-8");
				reader = new BufferedReader(new InputStreamReader(sock.getInputStream(), "UTF-8"));
				this.registUser();
				// }

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void registUser() throws IOException {
		//
		/*
		 * UserStore, regist new User, Constructor(id,pw,name); success ->
		 * return true mewssage fail -> do not exist
		 */
		String line = reader.readLine();
		FileInfo fileinfo = new Gson().fromJson(line, FileInfo.class);
		String ret = "true";
		// ID , Password, Name
		//if()
		userStore = new UserStore();
		userStore.registerUser(fileinfo.getUserId(), fileinfo.getCurrentPath(), fileinfo.getNewPath());
		
		
		String success = new Gson().toJson(ret);
		writer.write(success);
		writer.flush();
		this.queuemanager.getUserQueue().put(this.handleInfo.getSocketAccessible());
		this.handleInfo.getSocketAccessible().setAccessible(true);
	}
}
