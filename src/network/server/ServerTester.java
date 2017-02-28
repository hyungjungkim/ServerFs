package network.server;

public class ServerTester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		QueueManager queueManager = QueueManager.getInstance();
		ThreadManager threadManager = new ThreadManager();
		threadManager.runThread();
		
		TCPReactorLogic tcpReactorLogic = new TCPReactorLogic();
		tcpReactorLogic.startServer();
	}

}
