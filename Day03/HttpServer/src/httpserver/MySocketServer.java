package httpserver;

import java.net.InetAddress;
import java.net.ServerSocket;

public class MySocketServer {
	private final String LOCALHOST = "127.0.0.1";
	private final int DEFAULT_PORT = 8888;
	private final int MAX_QUEUE_SIZE = 5;

	private InetAddress mHost;
	private int mPort;
	private ServerSocket serverSocket;

	public MySocketServer() {
		try {
			mHost = InetAddress.getByName(LOCALHOST);
			mPort = DEFAULT_PORT;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// serverSocket 专门用于监听
	public void listen() {
		try {
			// 创建一个 ServerSocket 实例
			// MAX_QUEUE_SIZE 指等待队列的最大长度
			serverSocket = new ServerSocket(mPort, MAX_QUEUE_SIZE, mHost);
			// 开启快速重用, 否则端口会被占用一段时间, https://docs.oracle.com/javase/7/docs/api/java/net/StandardSocketOions.html
			serverSocket.setReuseAddress(true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 当客户端发来一个连接请求的时候，serverSocket就accept()，并创建一个新的 clientSocket = serverSocket.accept();
	// 这个新的clientSocket用于和客户端进行通信，而原来的serverSocket则继续进行监听。
	public void handle() {
		try {
			MySocket mySocket = new MySocket(serverSocket.accept());
			mySocket.recv();
			mySocket.send();
			mySocket.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		MySocketServer mySocketServer = new MySocketServer();
		mySocketServer.listen();
		mySocketServer.handle();
	}
}
