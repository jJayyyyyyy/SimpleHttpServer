package httpserver;

import java.nio.charset.Charset;

public class Response{
	public byte[] byteHeader;
	public byte[] byteBody;
	public Response() {
		String strBody = "Hello";
		String strHeader = "HTTP/1.0 200 OK\n"
						+ "content-type: text/plain; charset=utf-8\n"
						+ "content-length: " + strBody.length()
						+ "\r\n\r\n";

		byteHeader = strHeader.getBytes(Charset.forName("UTF-8"));
		byteBody = strBody.getBytes(Charset.forName("UTF-8"));
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}