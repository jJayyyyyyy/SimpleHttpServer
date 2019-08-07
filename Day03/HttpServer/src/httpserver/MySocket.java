package httpserver;

import java.nio.charset.Charset;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MySocket{
	private Socket socket;
	MyRequest myRequest;
	MyResponse myResponse;
	BufferedReader bufferedReader;
	BufferedWriter bufferedWriter;

	public MySocket(Socket socket){
		this.socket = socket;
	}

	public Socket getSocket() {
		return this.socket;
	}

	public void recv(){
		try {
			bufferedReader = new BufferedReader(
				new InputStreamReader(
					socket.getInputStream(),
					Charset.forName("UTF-8")
				)
			);
			myRequest = new MyRequest(bufferedReader);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void send(){
		try {
			bufferedWriter = new BufferedWriter(
				new OutputStreamWriter(
					socket.getOutputStream(),
					Charset.forName("UTF-8")
				)
			);
			myResponse = new MyResponse();
			bufferedWriter.write(myResponse.getResponse());
			// 如果用 buffer，就需要 flush 来发送
			bufferedWriter.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			this.bufferedReader.close();
			this.bufferedWriter.close();
			this.socket.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
}
