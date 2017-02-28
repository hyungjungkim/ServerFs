package db.domain;

import java.io.Serializable;


public class FileInfo implements Serializable{
	
	private String userId;
	private String currentPath;
	private String newPath;
	private ServiceNum serviceNum;
	
	public FileInfo(){
		
	}
	public FileInfo(String userId, String currentPath, String newPath, ServiceNum serviceNum) {
		this.userId = userId;
		this.currentPath = currentPath;
		this.newPath = newPath;
		this.serviceNum = serviceNum;
	}

	public String getCurrentPath() {
		return currentPath;
	}

	public void setCurrentPath(String currentPath) {
		this.currentPath = currentPath;
	}

	public String getNewPath() {
		return newPath;
	}

	public void setNewPath(String newPath) {
		this.newPath = newPath;
	}

	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public ServiceNum getServiceNum() {
		return serviceNum;
	}

	public void setServiceNum(ServiceNum serviceNum) {
		this.serviceNum = serviceNum;
	}
	
}
