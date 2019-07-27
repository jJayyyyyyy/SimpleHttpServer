package httpserver;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class MySocketServer {
	private final String LOCALHOST = "127.0.0.1";
	private final int DEFAULT_PORT = 8888;
	private final int MAX_QUEUE_SIZE = 5;

	private InetAddress mHost;
	private int mPort;
	public ServerSocket serverSocket;
	
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
	public void recvLineByLine() {
		try {
			Socket socket = serverSocket.accept();
			
			// 接下来类似于 MySocket.java 里面的  recvLineByLine()
			// 获取输入流
			InputStream is = socket.getInputStream();
			// 二进制流转变成文本流
			InputStreamReader isReader = new InputStreamReader(is, Charset.forName("UTF-8"));
			// 缓冲区
			BufferedReader bufReader = new BufferedReader(isReader);
			
			while(true) {
				String line = bufReader.readLine();
				// readLine会自动去掉\r\n, 相当于rstrip("\r\n")
				if (line != null) {
					if(line.equals("q")) {
						// 注意在判断字符串内容是否相同的时候要用 .equals() 方法
						System.out.println("quit");
						break;
					}
					else {
						System.out.println(line);
					}
				}
				else {
					break;
				}
			}
			
			bufReader.close();
			isReader.close();
			is.close();
			socket.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		MySocketServer mySocketServer = new MySocketServer();
		mySocketServer.listen();
		mySocketServer.recvLineByLine();
	}
}

/*
# https://docs.python.org/3/library/socket.html#example
import socket

def myClient():
	host = '127.0.0.1'
	port = 8888
	with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
		s.connect((host, port))
		s.sendall('Hello, world'.encode('utf-8'))

if __name__ == '__main__':
	myClient()
*/
