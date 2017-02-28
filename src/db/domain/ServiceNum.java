package db.domain;

public enum ServiceNum {

	UPLOAD("Upload"),
	DOWNLOAD("Download"),
	MKDIR("MkDir"),
	RMVDIR("RmvDir"),
	RMFILE("RmvFile"),
	CNGFILENAME("CngFileName"),
	CNGDIRNAME("CngDirName"),
	SEARCH("Search"),
	SHOWLIST("ShowList"),
	REGISTUSER("RegisterUser"),
	LOGINUSER("LoginUser"),
	ISEXISTID("IsExistId"),
	COPYPASTE("CopyPaste"),
	CUTPASTE("CutPaste");
	
	public String service;
	
	private ServiceNum(String service){
		
		this.service = service;
		
	}
	
}
