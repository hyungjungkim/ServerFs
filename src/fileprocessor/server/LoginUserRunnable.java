package fileprocessor.server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.google.gson.Gson;

import db.domain.FileInfo;
import db.domain.HandleInfo;
import db.store.DBStore;
import db.store.DBStoreFactory;
import db.store.UserStore;
import network.server.QueueManager;

public class LoginUserRunnable implements Runnable {
   //
   private QueueManager queuemanager;
   private Socket sock;
   private DBStore dbStore;
   private OutputStreamWriter writer;
   private HandleInfo handleInfo;
   private FileInfo fileInfo;
   private DBStoreFactory factory;
   private Object lockObject;
   private UserStore userStore;

   public LoginUserRunnable() {
      //
      queuemanager = QueueManager.getInstance();
      factory = DBStoreFactory.getInstance();
      this.lockObject = new Object();
   }

   @Override
   public void run() {
      // TODO Auto-generated method stub
      while (true) {
         try {
            System.out.println("login runnable");
            this.handleInfo = queuemanager.getLoginUserQueue().take();
            this.fileInfo = this.handleInfo.getFileInfo();
            this.sock = this.handleInfo.getSock();
            // this.out = this.handleInfo.getOut();
            // synchronized (this.lockObject){
            writer = new OutputStreamWriter(sock.getOutputStream(), "UTF-8");
            this.userStore = new UserStore();
            this.loginUser(this.fileInfo.getUserId(), this.fileInfo.getCurrentPath());
            // }

         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }

   public void loginUser(String userId, String userPw) {
      //
      boolean loginresult = false;
      if(userStore.isExistID(userId))
         loginresult = userStore.logOn(userId, userPw);
      String success = new Gson().toJson(loginresult);
      System.out.println("login result :"+success);
      try {
         writer.write(success+"\n");
         writer.flush();
         System.out.println("login is OK");
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }      
      this.queuemanager.getUserQueue().put(this.handleInfo.getSocketAccessible());
      this.handleInfo.getSocketAccessible().setAccessible(true);
   }
}