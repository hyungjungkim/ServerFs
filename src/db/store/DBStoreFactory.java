package db.store;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DBStoreFactory {
	private static final Map<String, DBStore> factoryMap = new HashMap<String, DBStore>();
	private final String root = "C:\\FileServer";
	private static final DBStoreFactory factory = new DBStoreFactory();
	
	public static DBStoreFactory getInstance(){
		return factory;
	}
	
	private DBStoreFactory(){
		//userid => Key add/ value => null;
		BufferedReader br=null;
		try {
			br = new BufferedReader(new FileReader(root+"/UserInfo.txt"));
			String userid="";
			while((userid=br.readLine())!=null){
				String[] pars = userid.split(",");
				factoryMap.put(pars[0], null);
			}
		} catch (IOException e) {
			// TODO: handle exception
		}finally{
			try {
				br.close();
			} catch (IOException e2) {
				// TODO: handle exception
			}
		}
	}
	
	public DBStore getDBStoreInstance(String userid){
		DBStore store =null;
		if(factoryMap.get(userid)==null){
			store = new DBStore(userid);
			factoryMap.put(userid, store);
		}else{
			store = factoryMap.get(userid);
		}
		return store;
	}
	public void removeDBStoreInstacne(String userid){
		factoryMap.put(userid, null);
	}
}
