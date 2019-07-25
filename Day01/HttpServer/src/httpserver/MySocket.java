package httpserver;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.Charset;

public class MySocket {
	private final String LOCALHOST = "127.0.0.1";
	private final int DEFAULT_PORT = 8888;

	private InetAddress mHost;
	private int mPort;
	private Socket mSocket;

	public MySocket() {
		try {
			mHost = InetAddress.getByName(LOCALHOST);
			mPort = DEFAULT_PORT;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void send(String msg) {
		try {
			// 创建一个 Socket 实例
			mSocket = new Socket(mHost, mPort);
			// 开启快速重用
			mSocket.setReuseAddress(true);
			
			byte[] txbuf = msg.getBytes(Charset.forName("UTF-8"));
			// 获取输出流
			OutputStream os = mSocket.getOutputStream();
			os.write(txbuf);
			os.close();
			// 关闭  socket
			mSocket.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void recv() {
		try {
			// 创建一个 Socket 实例
			mSocket = new Socket(mHost, mPort);
			// 开启快速重用
			mSocket.setReuseAddress(true);
						
			// 构造一个字节数组作为接收区, 大小是 16 字节, 未填充的元素值默认初始化为 0
			byte[] rxbuf = new byte[16];
			// 获取输入流
			InputStream is = mSocket.getInputStream();
			// 写入接收区
			is.read(rxbuf);
			is.close();
			mSocket.close();
			
			// 转换成字符串
			String msg = new String(rxbuf, Charset.forName("UTF-8"));
			System.out.println(msg);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void recvLineByLine() {
		try {
			// 创建一个 Socket 实例
			mSocket = new Socket(mHost, mPort);
			// 开启快速重用
			mSocket.setReuseAddress(true);
			
			// 获取输入流
			InputStream is = mSocket.getInputStream();
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
			mSocket.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		MySocket mySocket = new MySocket();
		mySocket.send("Hello world!");
		mySocket.recv();
		mySocket.recvLineByLine();
	}
}

/*
# https://docs.python.org/3/library/socket.html#example
import socket

def mySocketTest(option):
	server_host = '127.0.0.1'
	port = 8888

	with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
		s.bind((server_host, port))
		s.listen(1)
		conn, addr = s.accept()

		if option == 1:
			print('1: 测试 send()')
			with conn:
				data = conn.recv(1024)
				print(data.decode('utf-8'))
			print('ok\n')

		elif option == 2:
			print('2: 测试 recv()')
			with conn:
				conn.sendall('hi from server\n'.encode('utf-8'))
			print('ok\n')

		elif option == 3:
			print('3: 测试 recvLineByLine()')
			with conn:
				for i in range(3):
					msg = input('请输入消息: ') + '\n'
					conn.sendall(msg.encode('utf-8'))
			print('ok\n')

if __name__ == '__main__':
	mySocketTest(1)
	mySocketTest(2)
	mySocketTest(3)
*/
