#	实现 Java 版本的 HttpServer

##	1. 编写 MySocketServer

*	HTTP 建立在 Socket 之上, 因此我们首先来编写低一层的 MySocketServer, 方便日后调用

*	新建一个工程，并创建 `MySocketServer.java`, 其主要内容分为监听和处理，其中监听 listen() 方法如下

	```java
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
	```

	而当 server 收到一个 socket 连接请求的时候，正常情况下我们需要接收这个请求并对其进行处理，也就是读取请求，再发送响应。这部分内容的代码如下

	```java
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
	```

*	接下来，我们需要编写 `MySocket.java`，它应当包含 `recv, send, close` 等方法，其中 `recv` 和 `send` 如下。这两个方法使用了对文本流读写的 `BufferedReader/BufferedWriter`，用来包裹 `socket` 的 `InputStream/OutputStream`

	```java
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
	```

