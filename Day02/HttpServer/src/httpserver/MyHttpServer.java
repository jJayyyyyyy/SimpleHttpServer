package httpserver;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;

public class MyHttpServer extends MySocketServer {
	public void test5() {
		System.out.println("start test5---");
		try {
			Socket clientSocket = serverSocket.accept();
			
			readFromSocket(clientSocket);
			sendResponse(clientSocket);
			
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("end test5---");
	}
	
	private void sendResponse(Socket socket) {
		try {
			Response response = new Response();
			
			OutputStream os = socket.getOutputStream();
			os.write(response.byteHeader);
			os.write(response.byteBody);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		MyHttpServer myHttpServer = new MyHttpServer();
		myHttpServer.test5();
		myHttpServer.close();
	}
}

