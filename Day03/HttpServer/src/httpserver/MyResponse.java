package httpserver;

public class MyResponse {
	String msg1 = "helloworld";
	String msg2 = "你好";
	String body;
	String headers;

	public MyResponse(MyRequest myRequest) {
		body = "hello world";
		setHeaders();
	}

	private void setHeaders() {
		int length = body.getBytes().length;
		headers = "HTTP/1.0 200 OK\n"
					+ "content-type: text/plain; charset=utf-8\n"
					+ "content-length: " + length
					+ "\r\n\r\n";
	}

	public String getResponse() {
		return headers + body;
	}
}
