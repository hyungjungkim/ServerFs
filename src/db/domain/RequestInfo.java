package db.domain;

import java.io.Serializable;

public class RequestInfo implements Serializable{
	
	private static final long serialVersionUID = 2486787412931438059L;
	
	private FileInfo fileInfo;
	private String userId;
	private ServiceNum serviceNum;
	
	public RequestInfo(FileInfo fileInfo, String userId, ServiceNum serviceNum) {
		//super();
		this.fileInfo = fileInfo;
		this.userId = userId;
		this.serviceNum = serviceNum;
	}

	public FileInfo getFileInfo() {
		return fileInfo;
	}
	
	public void setFileInfo(FileInfo fileInfo) {
		this.fileInfo = fileInfo;
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
