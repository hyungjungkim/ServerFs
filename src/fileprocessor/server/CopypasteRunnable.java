package fileprocessor.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import com.google.gson.Gson;

import db.domain.FileInfo;
import db.domain.HandleInfo;
import db.store.DBStore;
import db.store.DBStoreFactory;
import network.server.QueueManager;

public class CopypasteRunnable implements Runnable{
   //
   private File file;
   private QueueManager queuemanager;
   private HandleInfo handleInfo;
   private FileInfo fileInfo;
   private Object lockObject;
   private FileInputStream fis;
   private FileOutputStream fos;
   private DBStoreFactory factory;
   private DBStore dbStore;
   private Socket sock;
   private OutputStreamWriter writer;
   
   public CopypasteRunnable(){
      queuemanager = QueueManager.getInstance();
      factory = DBStoreFactory.getInstance();
      this.lockObject = new Object();
   }
   
   @Override
   public void run() {
      // TODO Auto-generated method stub
      while (true) {
         this.handleInfo = queuemanager.getRmvFileQueue().take();
         this.fileInfo = this.handleInfo.getFileInfo();
         this.sock = this.handleInfo.getSock();
         this.dbStore = factory.getDBStoreInstance(fileInfo.getUserId());
         // this.out = this.handleInfo.getOut();
         // synchronized (this.lockObject){
         
         try {
            writer = new OutputStreamWriter(sock.getOutputStream(), "UTF-8");
         } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         try {
            this.copyPaste(this.fileInfo.getCurrentPath(), this.fileInfo.getNewPath());
         } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         //}
      }
   }
   
   public void copyPaste(String currentPath, String newPath) throws IOException{
      // only file Copy
      // [0] -> currentPath , [1] -> newPath
      String[] path = dbStore.FileCopy(currentPath, newPath);
      String cPath = path[0];
      String nPath = path[1];
      
      file = new File(cPath);
      try {
         file = new File(cPath);
         fis = new FileInputStream(cPath);
         fos = new FileOutputStream(nPath);
         
         byte[] contestBytes = new byte[1024];
         int count = 0;
         while(true){
            count = fis.read(contestBytes);
            if(count == -1)
               break;
            fos.write(contestBytes,0,count);
         }
         
         String retWriter = new Gson().toJson("true");
         writer.write(retWriter);
         writer.flush();
         
         fis.close();
         fos.close();
         
         
      } catch (FileNotFoundException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } 
      
      this.queuemanager.getUserQueue().put(this.handleInfo.getSocketAccessible());
      this.handleInfo.getSocketAccessible().setAccessible(true);

   }
}