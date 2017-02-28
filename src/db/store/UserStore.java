package db.store;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import db.domain.User;

public class UserStore {
   private User user;
   private List<User> userList;
   private static final File userInfo = new File("C:/FileServer/UserInfo.txt");

   public UserStore() {
      userList = new ArrayList<>();
      BufferedReader br = null;
      try {
         br = new BufferedReader(new FileReader(userInfo));
         String str = "";
         while ((str = br.readLine()) != null) {
            String[] pars = str.split(","); // userid , user password, name
            System.out.println("userstore :" + pars[0] + "  " + pars[1] + "  " + pars[2]);
            user = new User(pars[0], pars[1], pars[2]);
            userList.add(user);
         }
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } finally {
         try {
            br.close();
         } catch (IOException e2) {
            // TODO: handle exception
         }
      }
   }

   public boolean registerUser(String userId, String password, String userName) {
      boolean result = false;
      user = new User(userId, password, userName);
      FileWriter userWriter;
      try {
         userWriter = new FileWriter(userInfo, true);
         userWriter.write(user.getId() + "," + user.getName() + "," + user.getPassword() + "\n");
         userWriter.close();
         userList.add(user);
         result = true;
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return result;
   }

   public boolean logOn(String userId, String password) {
      for (User u : userList) {
         if (userId.equals(u.getId()) && password.equals(u.getPassword())) {
            return true;
         }
      }
      return false;
   }

   public boolean isExistID(String userId) {
      for (User u : userList) {
         if (userId.equals(u.getId())) {
            return true;
         }
      }
      return false;
   }
}