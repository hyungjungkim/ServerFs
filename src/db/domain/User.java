package db.domain;

public class User {
	/** user id : email*/
	private String id;
	/** user name */
	private String name;
	/** user pw*/
	private String password;
	
	public User(String id, String password){
		this.id = id;
		this.password = password;
	}
	public User(String id, String password, String name){
		this(id, password);
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
