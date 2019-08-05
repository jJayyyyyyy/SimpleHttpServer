package httpserver;

import java.nio.charset.Charset;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MySocket{
	private Socket socket;
	Request request;
	Response response;
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
			request = new Request(bufferedReader);
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
			response = new Response(request);
			bufferedWriter.write(response.getResponse());
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
