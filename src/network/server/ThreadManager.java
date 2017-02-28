package network.server;

import fileprocessor.server.ChangeNameRunnable;
import fileprocessor.server.CopypasteRunnable;
import fileprocessor.server.CutpasteRunnable;
import fileprocessor.server.DirectoryCreateRunnable;
import fileprocessor.server.DirectoryRemoveRunnable;
import fileprocessor.server.FileDownloadRunnable;
import fileprocessor.server.FileRemoveRunnable;
import fileprocessor.server.FileSearchRunnable;
import fileprocessor.server.FileUploadRunnable;
import fileprocessor.server.LoginUserRunnable;
import fileprocessor.server.RegistUserRunnable;
import fileprocessor.server.ShowListRunnable;

public class ThreadManager {
	//
//	private List<Thread> changeNameRunnables;
//	private List<Thread> directoryCreateRunnables;
//	private List<Thread> directoryRemoveRunnables;
//	private List<Thread> fileDownloadRunnables;
//	private List<Thread> fileRemoveRunnables;
//	private List<Thread> fileSearchRunnables;
//	private List<Thread> fileUploadRunnables;
//	private List<Thread> showListRunnables;
	
	
	
	public ThreadManager(){
		
	}
	
	public void runThread(){
		
		Thread handlerThread; 
				
		for(int i = 0; i < 3; i++){
			
			ChangeNameRunnable changeNameRunnable = new ChangeNameRunnable();
			handlerThread = new Thread(changeNameRunnable);
//			this.changeNameRunnables.add(handlerThread);
			handlerThread.start();
			
		}
		
		for(int i = 0; i < 3; i++){
			
			DirectoryCreateRunnable directoryCreateRunnable = new DirectoryCreateRunnable();
			handlerThread = new Thread(directoryCreateRunnable);
//			this.directoryCreateRunnables.add(handlerThread);
			handlerThread.start();
			
		}
		
		for(int i = 0; i < 3; i++){
			
			DirectoryRemoveRunnable directoryRemoveRunnable = new DirectoryRemoveRunnable();
			handlerThread = new Thread(directoryRemoveRunnable);
//			this.directoryRemoveRunnables.add(handlerThread);
			handlerThread.start();
			
		}
		
		for(int i = 0; i < 3; i++){
			
			FileDownloadRunnable fileDownloadRunnable = new FileDownloadRunnable();
			handlerThread = new Thread(fileDownloadRunnable);
//			this.fileDownloadRunnables.add(handlerThread);
			handlerThread.start();
			
		}
		
		for(int i = 0; i < 3; i++){
			
			FileRemoveRunnable fileRemoveRunnable = new FileRemoveRunnable();
			handlerThread = new Thread(fileRemoveRunnable);
//			this.fileRemoveRunnables.add(handlerThread);
			handlerThread.start();
			
		}
		
		for(int i = 0; i < 3; i++){
			
			FileSearchRunnable fileSearchRunnable = new FileSearchRunnable();
			handlerThread = new Thread(fileSearchRunnable);
//			this.fileSearchRunnables.add(handlerThread);
			handlerThread.start();
			
		}
		
		for(int i = 0; i < 3; i++){
			
			FileUploadRunnable fileUploadRunnable = new FileUploadRunnable();
			handlerThread = new Thread(fileUploadRunnable);
//			this.fileUploadRunnables.add(handlerThread);
			handlerThread.start();
			
		}
		
		for(int i = 0; i < 3; i++){
			
			RegistUserRunnable registUserRunnable = new RegistUserRunnable();
			handlerThread = new Thread(registUserRunnable);
//			this.showListRunnables.add(handlerThread);
			handlerThread.start();
		
		}
		
		for(int i = 0; i < 3; i++){
			
			LoginUserRunnable loginUserRunnable = new LoginUserRunnable();
			handlerThread = new Thread(loginUserRunnable);
//			this.showListRunnables.add(handlerThread);
			handlerThread.start();
		
		}
		
		for(int i = 0; i < 3; i++){
			
			CutpasteRunnable cutpasteRunnable = new CutpasteRunnable();
			handlerThread = new Thread(cutpasteRunnable);
//			this.showListRunnables.add(handlerThread);
			handlerThread.start();
		
		}
		
		for(int i = 0; i < 3; i++){
			
			CopypasteRunnable copypasteRunnable = new CopypasteRunnable();
			handlerThread = new Thread(copypasteRunnable);
//			this.showListRunnables.add(handlerThread);
			handlerThread.start();
		
		}
		
		for(int i = 0; i < 3; i++){
			
			ShowListRunnable showListRunnable = new ShowListRunnable();
			handlerThread = new Thread(showListRunnable);
//			this.showListRunnables.add(handlerThread);
			handlerThread.start();
		
		}
		
		for(int i = 0; i < 3; i++){
			
			ProcessRouterLogic processRouterLogic = new ProcessRouterLogic();
			handlerThread = new Thread(processRouterLogic);
//			this.showListRunnables.add(handlerThread);
			handlerThread.start();
		
		}
		
		


		
	}
	
}
