package httpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.Charset;

public class MySocket{
	private String host = "127.0.0.1";
	private int port = 8888;
	private Socket socket;
	
	public MySocket() {
		try {
			this.socket = new Socket(InetAddress.getByName(host), port);
			socket.setReuseAddress(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		MySocket testSocket = new MySocket();

		testSocket.test1();
//		testSocket.test2();
//		testSocket.test3();

		testSocket.close();
	}

	public void test1() {
		System.out.println("start test1---");
		try {
			String msg = "hello world";
			byte[] txbuf = msg.getBytes(Charset.forName("UTF-8"));
			
			OutputStream os = socket.getOutputStream();	// 获取输出流
			os.write(txbuf);
			os.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("end test1---");
	}
	
	public void test2() {
		System.out.println("start test2---");
		try {
			byte[] rxbuf = new byte[16];	// 构造一个字节数组作为接收区，大小是 16 字节，未填充的元素值默认为 0
			
			InputStream is = socket.getInputStream();	// 获取输入流
			is.read(rxbuf);	// 写入接收区
			is.close();
			
			String msg = new String(rxbuf, Charset.forName("UTF-8"));	// 转换成字符串
			System.out.println(msg);
		}catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("end test2---");
	}
	
	public void test3() {
		System.out.println("start test3---");
		try {
			InputStream is = socket.getInputStream();
			InputStreamReader isReader = new InputStreamReader(is, Charset.forName("UTF-8"));	// 二进制流转变成文本流
			BufferedReader bufReader = new BufferedReader(isReader);	// 缓冲区
			
			String line = "";
			while( true ) {
				line = bufReader.readLine(); // readLine会自动去掉\r\n, 相当于rstrip("\r\n")
				if( line.equals("q") == true ) {
					// 注意在判断字符串内容是否相同的时候要用 .equals() 方法
					break;
				}else {
					System.out.println(line);
				}
			}
			bufReader.close();
			isReader.close();
			is.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("end test3---");
	}
}