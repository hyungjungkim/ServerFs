package db.domain;

import java.io.Serializable;
import java.util.Date;

public class DirFile implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7244744784677482997L;
	/** file or folder index*/
	private int index;
	/** file or folder name*/
	private String fileName;
	/** User Id*/
	private String userId;
	/** upper direcotry's index root=0 else >0*/
	private int parentDirIdx;
	/** Last Modified Date*/
	private String modifiedDate;
	/** folder = 0 , file =1*/
	private FileObjectIdentifier flag;
	/** Client File path*/
	private String clientPath;
	public DirFile(){
		
	}
	
	public DirFile(int index, String fileName, String userId, int parentDirIdx, String modifiedDate,
			int flag, String clientPath) {
		super();
		this.index = index;
		this.fileName = fileName;
		this.userId = userId;
		this.parentDirIdx = parentDirIdx;
		this.modifiedDate = modifiedDate;
		
		if(flag==0){
			this.flag = this.flag.Folder;
		}else if(flag==1){
			this.flag= this.flag.File;
		}else{
			this.flag = this.flag.Parent;
		}
		this.clientPath = clientPath;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getParentDirIdx() {
		return parentDirIdx;
	}

	public void setParentDirIdx(int parentDirIdx) {
		this.parentDirIdx = parentDirIdx;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public int getFlag() {
		return flag.getFlag();
	}

	/*public void setFlag(FileObjectIdentifier flag) {
		this.flag = flag;
	}*/
	
	public String getClientPath() {
		return clientPath;
	}

	@Override
	public String toString() {
		return (getIndex()+","+getFileName()+","+getUserId()+","+getParentDirIdx()+","+getModifiedDate()+","+getFlag()+","+getClientPath());
	}

	public void setClientPath(String clientPath) {
		this.clientPath = clientPath;
	}

	public void print(){
		System.out.println("DirFile [index=" + index + ", fileName=" + fileName + ", userId=" + userId + ", parentDirIdx="
				+ parentDirIdx + ", modifiedDate=" + modifiedDate + ", flag=" + flag + ", getIndex()=" + getIndex()
				+ ", getFileName()=" + getFileName() + ", getUserId()=" + getUserId() + ", getParentDirIdx()="
				+ getParentDirIdx() + ", getModifiedDate()=" + getModifiedDate() + ", getFlag()=" + getFlag() + ", getClientPath()=" + getClientPath() + "]");
	}
	
	
}
