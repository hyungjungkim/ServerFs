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

public class DirectoryRemoveRunnable implements Runnable {
   //
   private FileInfo fileInfo = null;
   private Socket sock = null;
   private HandleInfo handleInfo;
   private QueueManager queuemanager;
   private DBStore dbStore;
   private DBStoreFactory factory;
   private OutputStreamWriter writer;
   private Object lockObject;

   
   public DirectoryRemoveRunnable() {
      
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
            
            this.handleInfo = queuemanager.getRmvDirQueue().take();
            this.fileInfo = this.handleInfo.getFileInfo();
            this.sock = this.handleInfo.getSock();
            this.dbStore = factory.getDBStoreInstance(fileInfo.getUserId());
//            synchronized (this.lockObject){
               writer = new OutputStreamWriter(sock.getOutputStream(), "UTF-8");
               this.DirectoryRemove(this.fileInfo.getUserId(), this.fileInfo.getCurrentPath());
//            }
            
         } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
   }

   public List<DirFile> DirectoryRemove(String userId, String currentPath) throws IOException {
      dirRemove(userId,currentPath);
      String parentPath = currentPath.substring(0, currentPath.lastIndexOf("/"));
      // Serializable
      ListInfor retList = new ListInfor();
      retList.setListInfor(dbStore.ShowList(parentPath));
      String retListStr = new Gson().toJson(retList);
      try {         
         writer.write(retListStr+"\n");
         writer.flush();
      } catch (IOException e) {
         e.getStackTrace();
      }
      this.queuemanager.getUserQueue().put(this.handleInfo.getSocketAccessible());
      this.handleInfo.getSocketAccessible().setAccessible(true);
      return null;
   }
   
   public void dirRemove(String userId, String currentPath){
      List<DirFile> removeFiles = dbStore.DirecotryAllRemove(currentPath);
//      if(removeFiles==null){
//         String serverPath = dbStore.DirecotryRemove(currentPath);
//         serverFileRemove(serverPath);
//      }else{
//         for(int i=0;i<removeFiles.size();i++){
//            if(removeFiles.get(i).getFlag()==0){
//               // folder
//               dirRemove(userId, removeFiles.get(i).getClientPath());
//            }else{
//               // files
//               String serverPath = dbStore.FileRemove(removeFiles.get(i).getClientPath());
//               serverFileRemove(serverPath);
//            }
//         }
//      }
      if(removeFiles!=null){
    	  for(int i=0;i<removeFiles.size();i++){
    		  if(removeFiles.get(i).getFlag()==0){
    			  // folder
    			  dirRemove(userId, removeFiles.get(i).getClientPath());
    		  }else{
    			  // files
    			  String serverPath = dbStore.FileRemove(removeFiles.get(i).getClientPath());
    			  serverFileRemove(serverPath);
    		  }
    	  }
      }
      String serverPath = dbStore.DirecotryRemove(currentPath);
      serverFileRemove(serverPath);
   }
   
   public void serverFileRemove(String serverPath){
      File file = new File(serverPath);
      if (file.delete()) {
         System.out.println(file.getName() + " is deleted!");
      } else {
         System.out.println("Delete operation is failed.");
      }
   }
}