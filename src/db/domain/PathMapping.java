package db.domain;

public class PathMapping {
	private int dirIdx;
	private String userId;
	private String originFileName;
	private String serverFileName;
	private String serverPath;
	
	public PathMapping(){
		
	}
	
	public PathMapping(int dirIdx, String userId, String originFileName, String serverFileName, String serverPath) {
		super();
		this.dirIdx = dirIdx;
		this.userId = userId;
		this.originFileName = originFileName;
		this.serverFileName = serverFileName;
		this.serverPath = serverPath;
	}
	
	

	public int getDirIdx() {
		return dirIdx;
	}

	public void setDirIdx(int dirIdx) {
		this.dirIdx = dirIdx;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOriginFileName() {
		return originFileName;
	}

	public void setOriginFileName(String originFileName) {
		this.originFileName = originFileName;
	}
	
	public String getServerFileName() {
		return serverFileName;
	}

	public void setServerFileName(String serverFileName) {
		this.serverFileName = serverFileName;
	}

	public String getServerPath() {
		return serverPath;
	}

	public void setServerPath(String serverPath) {
		this.serverPath = serverPath;
	}

	
	@Override
	public String toString() {
		return (getDirIdx()+","+getUserId()+","+getOriginFileName()+","+getServerFileName()+","+getServerPath());
	}

	public void print() {
		System.out.println("PathMapping [userId=" + userId + ", originFileName=" + originFileName + ", serverPath=" + serverPath
				 + ", getUserId()=" + getUserId() + ", getOriginFileName()="
				+ getOriginFileName() +", getServerFileName()=" + getServerFileName() +", getServerPath()=" + getServerPath()+ "]");
	}
	
	
}
