package network.server;

import db.domain.RequestInfo;

public interface ProcessRouter {
	
	public void depacketizer(RequestInfo requestInfo);
		
}
