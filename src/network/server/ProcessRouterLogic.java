package network.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;

import com.google.gson.Gson;

import db.domain.FileInfo;
import db.domain.HandleInfo;
import db.domain.RequestInfo;
import db.domain.ServiceNum;
import db.domain.SocketAccessible;

public class ProcessRouterLogic extends Thread implements ProcessRouter {
	
	private String userId;
	private HandleInfoQueue infoQueue;
	private ServiceNum serviceNum;
	private FileInfo fileInfo;
	private HandleInfo handleInfo;
	private Socket sock;
	private BufferedReader reader;
	private OutputStreamWriter writer;
	private DataInputStream dis;
	private DataOutputStream dos;
	private Object lockObject;
	private QueueManager queuemanager;
	public boolean isAccessible;
	
	public ProcessRouterLogic(){
		
		this.lockObject = new Object();
		queuemanager = QueueManager.getInstance();
		
	}
	
	/***
	 * 
	 */
	
	public void run() {
		// TODO Auto-generated method stub
//		super.run();
		int requestGetCnt = 0;
		int requestCnt = 0;
		while(true){
			
			String line = null;	
			try {	
				
				SocketAccessible socketAccessible= queuemanager.getUserQueue().take(); 
				this.sock = socketAccessible.getSock();	
//				this.isAccessible = queuemanager.getUserQueue().take().isAccessible();
				
				System.out.println("[Router] <" + sock + "> logged in." );
				this.reader = new BufferedReader(new InputStreamReader(this.sock.getInputStream(), "UTF-8"));
				requestCnt++;
				System.out.println("[Router] Waiting request......." + requestCnt);
				if(!socketAccessible.isAccessible()){
					
//					Thread.yield();
					continue;
//					System.out.println("isAcceeible = " + this.isAccessible);
					
				}
				synchronized (this.lockObject){
					
					line = reader.readLine();
					requestGetCnt++;
					System.out.println("[Router] Got request!!" + requestGetCnt);
					this.lockObject.notifyAll();
					
				}
				this.fileInfo = new Gson().fromJson(line, FileInfo.class);
				this.serviceNum = this.fileInfo.getServiceNum();
				System.out.println("[Router] " + this.fileInfo.getUserId() + " Requested service: " + this.serviceNum);
				this.handleInfo = new HandleInfo(socketAccessible, this.fileInfo);
						
				if(this.serviceNum.equals(ServiceNum.UPLOAD)){
					
					socketAccessible.setAccessible(false);
					queuemanager.getUploadQueue().put(this.handleInfo);
				
				}
				else if(this.serviceNum.equals(ServiceNum.DOWNLOAD)){
	
					socketAccessible.setAccessible(false);
					queuemanager.getDownloadQueue().put(this.handleInfo);
					
				}
				else if(this.serviceNum.equals(ServiceNum.MKDIR)){
	
					socketAccessible.setAccessible(false);
					queuemanager.getMkDirQueue().put(this.handleInfo);
	
				}
				else if(this.serviceNum.equals(ServiceNum.RMVDIR)){
				
					socketAccessible.setAccessible(false);
					queuemanager.getRmvDirQueue().put(this.handleInfo);
					
				}
				else if(this.serviceNum.equals(ServiceNum.RMFILE)){
					
					socketAccessible.setAccessible(false);
					queuemanager.getRmvFileQueue().put(this.handleInfo);
					
				}
				else if(this.serviceNum.equals(ServiceNum.CNGFILENAME)){
					
					socketAccessible.setAccessible(false);
					queuemanager.getCngFileNameQueue().put(this.handleInfo);
					
				}
				else if(this.serviceNum.equals(ServiceNum.SEARCH)){
					
					socketAccessible.setAccessible(false);
					queuemanager.getSearchQueue().put(this.handleInfo);
					
					//when Searching you can use the information at CurrentPath as fileName.
				}
				else if(this.serviceNum.equals(ServiceNum.SHOWLIST)){
	
					socketAccessible.setAccessible(false);
					queuemanager.getShowlistQueue().put(this.handleInfo);
					System.out.println("쇼리스트에 넣엇다.");
					
				}
				else if(this.serviceNum.equals(ServiceNum.REGISTUSER)){
					
					socketAccessible.setAccessible(false);
					queuemanager.getRegisterUserQueue().put(this.handleInfo);
					
				}
				else if(this.serviceNum.equals(ServiceNum.LOGINUSER)){
					
					socketAccessible.setAccessible(false);
					queuemanager.getLoginUserQueue().put(this.handleInfo);
					
				}
				else if(this.serviceNum.equals(ServiceNum.COPYPASTE)){
					
					socketAccessible.setAccessible(false);
					queuemanager.getCopyPasteQueue().put(this.handleInfo);
					
				}
				else if(this.serviceNum.equals(ServiceNum.CUTPASTE)){
					
					socketAccessible.setAccessible(false);
					queuemanager.getCutPasteQueue().put(this.handleInfo);
					
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("[Server] " + this.fileInfo.getUserId() + "  disconnected.");
				break;
				
			}	
			
			
		}
		
	}

	@Override
	public void depacketizer(RequestInfo requestInfo) {
		// TODO Auto-generated method stub
		
	}

}