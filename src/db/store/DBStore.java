package db.store;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.domain.DirFile;
import db.domain.PathMapping;

public class DBStore{
   private static final int SimpleDateFormat = 0;
   private String userID;
   /**table */
   private List<DirFile> dirFileList;
   private List<PathMapping> pathMappingList;

   private final String root="C:\\FileServer";               //Server PC root
   public final String MAPPINGInfo = "MappingInfo.txt";
   public final String DBInfo ="DBInfo.txt";
   public final String INDEX ="Index.txt";
   
   /**
    * DirFile (int index, String fileName, String userId, int parentDirIdx, String modifiedDate, int flag, String clientPath)
    * PathMapping (int idx, String userId, String originFileName, String serverFileName, String serverPath)
    */

   public DBStore(String userId){
      this.userID = userId;
      DBInit(userID);
      MappingInit(userID);
      if(dirFileList.size() == 0) {         
         DirFile dirFile = new DirFile(0, userID, userID, -1, "", 0, userId);
         dirFileList.add(dirFile);
         writeInfoFile();
      }
      //updateInfoFile(dirFile, null);
      
   }
   
   public void DBInit(String userId){
      //DBInfo.txt load
      BufferedReader br=null;
      dirFileList = new ArrayList<>();
      try {
         System.out.println("DBStore 생성자 ");
         br = new BufferedReader(new FileReader(root + "/"+ userId + "/"+DBInfo));
         String str = null;
         while((str = br.readLine())!=null){
            //Parsing
            String[] parse_str = str.split(",");
            DirFile dirFile = new DirFile(Integer.parseInt(parse_str[0]), parse_str[1], parse_str[2], Integer.parseInt(parse_str[3]), parse_str[4], Integer.parseInt(parse_str[5]), parse_str[6]);
            dirFileList.add(dirFile);
         }
      } catch (FileNotFoundException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (IOException e2){
         e2.printStackTrace();
      }finally{
         try {
            br.close();
         } catch (IOException e3) {
            // TODO: handle exception
            e3.printStackTrace();
         }
      }
   }

   public void MappingInit(String userId){
      //MappingInfo.txt load
      BufferedReader br=null;
      pathMappingList = new ArrayList<>();    
      try {
         br = new BufferedReader(new FileReader(root+ "\\"+ userId + "\\"+MAPPINGInfo));         
         String str = null;
         while((str = br.readLine())!=null){
            //Parsing
            String[] parse_str = str.split(",");
            PathMapping pathMapping = new PathMapping(Integer.parseInt(parse_str[0]), parse_str[1], parse_str[2], parse_str[3],parse_str[4]);
            pathMappingList.add(pathMapping);
         }
      } catch (FileNotFoundException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (IOException e2){
         e2.printStackTrace();
      }finally{
         try {
            br.close();
         } catch (IOException e3) {
            // TODO: handle exception
            e3.printStackTrace();
         }
      }
   }

   public String FileUpload(String filePath){
      String serverPath =root+"\\"+userID;
      String[] parse = filePath.split("/");
      //parentIndex search
      System.out.println("FileUpload filePath: " + filePath);
      int parentIdx = getParentIndex(filePath);

      //ModifiedDate
      Date today = new Date();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

      //DirFile add
      DirFile dirFile = new DirFile(dirFileList.size(), parse[parse.length-1], userID, parentIdx,sdf.format(today) , 1, filePath);
      dirFileList.add(dirFile);

      String newFileName =  System.currentTimeMillis()+"_"+dirFile.getFileName();

      serverPath += "/"+parse[parse.length-1].charAt(0)+"/"+newFileName;
      //PathMapping add
      PathMapping pathMapping = new PathMapping(dirFile.getIndex(), userID, dirFile.getFileName(),newFileName, serverPath);
      pathMappingList.add(pathMapping);
      
      updateInfoFile(dirFile, pathMapping);//dbFile update

      //System.out.println("DBStore FileUpload Return : "+serverPath);
      return serverPath;
   }
   
   public String FileDownload(String filePath){
      DirFile file = FilePathSearch(filePath);
      return pathMappingList.get(file.getIndex()-1).getServerPath();
   }

   public String FileRemove(String filePath){
      DirFile file = FilePathSearch(filePath);
      String serverPath=null;
      
      if(file.getFlag()==1){
         serverPath = pathMappingList.get(file.getIndex()-1).getServerPath();
         pathMappingList.remove(pathMappingList.get(file.getIndex()-1));
         dirFileList.remove(file);
      }
      
      writeInfoFile();
      System.out.println("dbstore:"+serverPath);
      return serverPath;
   }
   
   public List<DirFile> FileSearch(String fileName){
      List<DirFile> resultFile = new ArrayList<>();
      System.out.println("FileSearch filname: " + fileName);
      if(fileName.equals(userID)) {
         resultFile.add(dirFileList.get(0));
      } else {
         BufferedReader br=null;
         try {
            br = new BufferedReader(new FileReader(root+"/"+userID+"/"+fileName.charAt(0)+"/Index.txt"));
            String idx="";
            while((idx=br.readLine())!=null){
               String[] parse =idx.split(",");
               if(parse[1].contains(fileName)){
                  resultFile.add(dirFileList.get(Integer.parseInt(parse[0])));
               }
            }
         } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }finally{
            try {
               br.close();
            } catch (IOException e2) {
               // TODO: handle exception
            }
         }
      }
      
      return resultFile;
      
   }
   
   private DirFile FilePathSearch(String filePath){
      String[] pars = filePath.split("/");
      System.out.println("FilePathSearch fileName for FileSearch: " + pars[pars.length-1]);
      List<DirFile> resultList = FileSearch(pars[pars.length-1]);
      for(DirFile result : resultList){
         if(filePath.equals(result.getClientPath())){
            return result;
         }
      }
      return null;
   }

   public String DirectoryCreate(String filePath){
      if(filePath.equals(userID)){
         DirFile dirFile = new DirFile(0, userID, userID, -1, "", 0, filePath);
         dirFileList.add(dirFile);
         updateInfoFile(dirFile, null);
         return null;
      }
      String[] parse = filePath.split("/");
      int parentIdx = getParentIndex(filePath);
      //ModifiedDate
      Date today = new Date();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

      String[] pars =filePath.split("/");
      String dirName =pars[pars.length-1];

      DirFile dirFile = new DirFile(dirFileList.size(), dirName, userID, parentIdx, sdf.format(today), 0, filePath);
      dirFileList.add(dirFile);

      String serverFileName =  System.currentTimeMillis()+"_"+dirFile.getFileName();
      String serverPath =root+"\\"+userID+"\\"+parse[parse.length-1].charAt(0)+"\\"+serverFileName;
      
      PathMapping pathMapping = new PathMapping(dirFile.getIndex(), userID, dirFile.getFileName(), serverFileName, serverPath );
      pathMappingList.add(pathMapping);
      
      updateInfoFile(dirFile, pathMapping);

      return serverPath;
   }

   public String[] ChangeName(String filePath, String newName){
      DirFile d = FilePathSearch(filePath);      
      String newClientPath = filePath.substring(0, (filePath.length()-d.getFileName().length()))+newName;
      String[] path = new String[2];
      path[0] = pathMappingList.get(d.getIndex()-1).getServerPath();      // before filePath;
      
      String fileName = path[0].substring(path[0].lastIndexOf("/")+1, path[0].length());
      String pars2[] = fileName.split("_");
      System.out.println("\n pars2[0] : "+pars2[0] + "FileName : "+ fileName);
      String newFilePath = pars2[0]+"_"+newName;
     
      dirFileList.get(d.getIndex()).setFileName(newName);            //client fileName change
      dirFileList.get(d.getIndex()).setClientPath(newClientPath);         //create & change Client FileName
   
      
      String serverPath = root + "/"+userID+"/"+newName.charAt(0)+"/"+newFilePath;      //server path change;
      System.out.println("newName : "+newName + "   serverPath : "+serverPath);
      pathMappingList.get(d.getIndex()-1).setServerPath(serverPath);
   
      path[1] = pathMappingList.get(d.getIndex()-1).getServerPath();      // after filePath;
      System.out.println("path1 :"+path[1]);
      writeInfoFile(); //update DBInfo, MappingInfo, Index.txt
      
      updateClientPath(dirFileList.get(d.getIndex()).getClientPath());
   
      return path;
   }
   
   
   public void updateClientPath(String parentPath) {
      List<DirFile> children = ShowList(parentPath);
      if(children.size() == 0){
         return;
      }else{
         for(DirFile d : children) {
            dirFileList.get(d.getIndex()).setClientPath(parentPath + "/"+d.getFileName());
            writeInfoFile(); //update DBInfo, MappingInfo, Index.txt
            if(d.getFlag() == 0) {
               updateClientPath(d.getClientPath());
            }
         }
      }
   
   }

   public List<DirFile> DirecotryAllRemove(String filePath){
      return ShowList(filePath);
   }

   public String DirecotryRemove(String filePath){
      DirFile file = FilePathSearch(filePath);
      String serverPath=null;
      if(file.getFlag()==0){
         serverPath = pathMappingList.get(file.getIndex()-1).getServerPath();
         pathMappingList.remove(pathMappingList.get(file.getIndex()-1));
         dirFileList.remove(file);
      }
      writeInfoFile();
      return serverPath;
   }
   
   public List<DirFile> ShowList(String filePath){
      List<DirFile> result =new ArrayList<>();
      int parentIdx = FilePathSearch(filePath).getIndex();
      for(DirFile d: dirFileList){
         if(d.getParentDirIdx()==parentIdx){
            result.add(d);
         }
      }
      return result;
   }
   public String[] FileCopy(String filePath, String newFilePath){   //Only File copy
      String[] result =new String[2];
      DirFile file = FilePathSearch(filePath);
      result[0] = pathMappingList.get(file.getIndex()-1).getServerPath();      
      result[1] = FileUpload(newFilePath);
      
      writeInfoFile();
      return result;
      
   }
   public void FileMove(String filePath, String newFilePath){
      DirFile file = FilePathSearch(filePath);
      FileCopy(filePath, newFilePath);
      FileRemove(file.getClientPath());
   }
   private int getParentIndex(String filePath){
      String[] parse = filePath.split("/");
      String str = parse[0];
      for(int i=1; i<parse.length-1; i++){
         str += ("/"+parse[i]);
      }
      System.out.println("getPasrentIndex filePath: " + str);
      return FilePathSearch(str).getIndex();
   }   
   // if create File or folder
   private void updateInfoFile(DirFile dirFile, PathMapping pathMapping) {
      
      FileWriter dbFileWriter;
      FileWriter pathFileWriter;

      try {
         if(dirFile != null)
         {            
            dbFileWriter = new FileWriter(root + "\\"+ userID + "\\"+DBInfo, true);
            dbFileWriter.write(dirFile.toString() + "\n");
            writeIndexFile(dirFile, true);
            dbFileWriter.close();
         }
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      try {
         if(pathMapping != null)
         {
            pathFileWriter = new FileWriter(root + "\\"+ userID + "\\"+MAPPINGInfo, true);
            pathFileWriter.write(pathMapping.toString() + "\n");
            pathFileWriter.close();            
         }
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      DBInit(userID);
      MappingInit(userID);

   }

   // if delete file of remove Dir (changeName), Write Info Files
   public void writeInfoFile() {

      FileWriter dbFileWriter;
      FileWriter pathFileWriter;
      Map<Integer, Integer> swapParentIdx = new HashMap<>();

      try {
         dbFileWriter = new FileWriter((root + "\\"+ userID + "\\"+DBInfo), false);
         cleanIndexFile();
      /*   int cnt =0;
         for(DirFile d : dirFileList){
            if(d.getIndex()==0){
              swapParentIdx.put(d.getIndex(), -1);
            }else{
               swapParentIdx.put(d.getIndex(), cnt);
               cnt++;
            }
           
         }*/
         for(int i=0; i<dirFileList.size();i++){
            if(i==0) {
              swapParentIdx.put(dirFileList.get(i).getIndex(), 0);
               dirFileList.get(i).setIndex(0);
               dirFileList.get(i).setParentDirIdx(-1);               
            } else {
               swapParentIdx.put(dirFileList.get(i).getIndex(), i);
               dirFileList.get(i).setIndex(i);
               dirFileList.get(i).setParentDirIdx(swapParentIdx.get(dirFileList.get(i).getParentDirIdx()));
            }
           
            dbFileWriter.write(dirFileList.get(i).toString()+"\n"); //List �엳�뒗 紐⑤뱺 �궡�슜�뱾�쓣 �뙆�씪�뿉 write //DirFile, PathMapping -> toString

            writeIndexFile(dirFileList.get(i), true);
         }
         dbFileWriter.close();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      try {
         pathFileWriter = new FileWriter((root + "\\"+ userID + "\\"+MAPPINGInfo), false);
         for(int i=0; i<pathMappingList.size(); i++){
            pathMappingList.get(i).setDirIdx(i+1);
            pathFileWriter.write(pathMappingList.get(i).toString()+"\n");
         }
         pathFileWriter.close();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
      DBInit(userID);
      MappingInit(userID);

   }
   
   public void cleanIndexFile() {
      String[] str = {"a", "b", "c", "d", "F", "1", "2", "3", "4"};
      FileWriter indexFileWriter;
      for(int i=0; i<str.length; i++){
         try {
               indexFileWriter = new FileWriter((root + "\\"+ userID + "\\"+str[i]+"\\"+INDEX),false);
               indexFileWriter.write("");
               indexFileWriter.close();
            } catch (IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
      }
   }
   
   public void writeIndexFile(DirFile dirFile, boolean append){
      FileWriter indexFileWriter;
      if(dirFile.getFileName().equals(userID)) {
         return;
      } else {         
         try {
            indexFileWriter = new FileWriter((root + "\\"+ userID + "\\"+dirFile.getFileName().charAt(0)+"\\"+INDEX),append);
            indexFileWriter.write(dirFile.getIndex()+","+dirFile.getFileName()+"\n");
            indexFileWriter.close();
         } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
   }

   public void readFile(){
      BufferedReader dbFileReader;
      BufferedReader pathFileReader;

      try {
         dbFileReader = new BufferedReader(new FileReader(root + "\\"+ userID + "\\"+DBInfo));
         String str="";
         while((str=dbFileReader.readLine())!=null){
            System.out.println("DBInfo File : "+str);
         }
      } catch (IOException e) {
         // TODO: handle exception
      }
      System.out.println();
      
      try {
         pathFileReader = new BufferedReader(new FileReader(root + "\\"+ userID + "\\"+MAPPINGInfo));
         String str="";
         while((str=pathFileReader.readLine())!=null){
            System.out.println("MappingInfo File : "+str);
         }
         System.out.println();
      } catch (IOException e) {
         // TODO: handle exception
      }
   }
}