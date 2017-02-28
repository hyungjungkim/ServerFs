package db.domain;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class ListInfor implements Serializable{
	//
	/*
	 * Class ListInfor for returning list
	 * Serializable
	 */
	
	//Version
	private static final long serialVersionUID = 132111111015102313L;
	
	private List<DirFile> listInfor;
	
	//Constructor
	public ListInfor(){
		//
	}
	public List<DirFile> getListInfor() {
		return listInfor;
	}
	public void setListInfor(List<DirFile> listInfor) {
		this.listInfor = listInfor;
	}
	
}
