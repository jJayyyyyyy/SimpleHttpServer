#	HTTP

###	1. 什么是 HTTP

*	关于 HTTP协议 的详细内容, 可以参考 `<HTTP权威指南>`, 本文只进行简单介绍.

*	首先, 我们来看看浏览器访问网页的时候发生了什么

	```
	...

	1. server创建socket, 监听某一端口port

	2. 浏览器访问server的ip:port, 请求建立连接

	3. server接受请求, 建立连接

	4. 浏览器发起GET类型的http request

	5. server返回http response

	6. 关闭连接

	...
	```

	以上是浏览器访问网页的部分过程, 也是接下来我们将用 `Java` 实现的内容

*	`HTTP协议` 是应用层协议, 建立在传输层之上. 利用 `HTTP` 进行通信是一个 `request - response` 的过程, 即 `请求 - 响应`, 客户端发起 `request`, 请求自己需要的资源, 服务端对 `request` 进行解析后, 返回相应的 `reponse`.

<br><br>

###	2. HTTP 报文格式

*	一般来说, `request 报文` 具有这样的格式

	```
	request line				# 请求行, 如 GET / HTTP/1.1
	request headers				# 如 Accept:text/html 指明可接收的文件类型
	blank line				# 即 '\r\n\r\n', 相当于分隔符, 表明头部信息的结束
	body					# 在 GET 请求中为空, 而在 POST 请求中, 这里存放的是要上传的数据
	```

*	而 `response 报文` 具有这样的格式

	```
	status-line				# 响应行, 如 HTTP/1.1 200 OK
	response headers			# 如 Content-Length:5, 表明body的大小为5个字节
	blank line
	body					# 这里存放服务器返回的响应数据
	```

*	下一节, 我们将通过 `Chrome浏览器` 来观察 `request` 和 `response`.

<br><br>

###	3. 利用Chrome开发者工具查看HTTP报文

*	首先打开 `Chrome`, 右键单击空白处, 选择 `Inspect` 进入开发者工具

*	然后选择 `Network` 选项卡, 这样就会自动开始记录网络日志

*	在地址栏输入一个网址, 如 `https://github.com`

*	我们会发现, 在开发者工具中出现了很多新的记录以及进度条。

*	单击每一条记录就可以看到更多详细信息

	![inspect](https://github.com/jJayyyyyyy/SimpleHttpServer/blob/master/Day02/assets/inspect.png)

<br><br>

###	4. 实现

*	我们可以沿用 `Day01` 的工程, 并将其中的 `MyServer.java` 删除

	接下来, 我们对 `MySocketServer.java` 中的 `readFromSocket()` 稍作修改, 把退出循环的条件修改为

	```java
	// if( line.equals("q") == true ) -->
	if( line.equals("") == true )
	```

	也就是说, 接收结束的条件, 从原来的结束符 'q', 变成了现在的内容为空

*	然后我们新建一个 `MyHttpServer.java` 文件, 这个类继承自 `MyServerSocket`, 

	```java
	public class MyHttpServer extends MyServerSocket {
		public static void main(String[] args) {
			MyHttpServer myHttpServer = new MyHttpServer();
			myHttpServer.test5();
			myHttpServer.close();
		}
	}
	```

	其中的 `test5()` 就是我们的测试方法, 它的前半部分和之前一样, 先接收和读取 `request`, 然后发送 `response`

	```java
	public void test5() {
		System.out.println("start test5---");
		try {
			Socket clientSocket = serverSocket.accept();

			readFromSocket(clientSocket);	// 接收 request
			sendResponse(clientSocket);		// 返回 response

			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("end test5---");
	}
	```

	其中的 `sendResponse()` 方法就是用二进制流向客户端(浏览器) 返回一个 HTTP 响应

	```java
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
	```

	接下来, 我们将构建一个 `Response` 对象

*	构建 `Response` 对象

	今天我们先实现一个简单的例子, 仅向客户端返回内容为 `hello` 的响应, 我们会在以后的内容中构造包含更多信息的 `Response` 对象

	首先新建一个 `Response.java` 文件, 输入如下内容

	```java
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
	}
	```

	从中我们看到,  `Response` 的 `strBody` 就是 `Hello`, 然后我们加上一些 `header`, 将其包装成一个 `HTTP` 报文

	第一步是加上 `status line`, 也就是 ` HTTP/1.0 200 OK\n `, 表明请求成功, 注意最后要加上换行符

	第二步是加上 `response headers`, 这里加上了 `content-type`, 让浏览器把 `body` 中的内容解读成 `utf-8` 编码的纯文本, 然后加上了 `body` 的长度

	第三步是加上 `blank line`, 用于分隔 `headers` 和 `body`

	最后是将 `strResponse` 进行 `utf-8` 编码, 转换成 `byteResponse`, 这样我们就可以在上一节中用 `os.write(response.byteHeader)` 和 `os.write(response.byteBody);` 把 `response` 发给客户端了

<br><br>

###	5. 测试

*	首先运行 `MyHttpServer.java`

*	然后打开浏览器, 输入 `127.0.0.1:8888`, 我们就可以在屏幕上看到服务器返回的 `Hello` 了。如果使用开发者工具进行分析, 我们还可以看到我们发送的 `headers` 的具体信息

<br><br>

###	6. 小结

今天我们学习了 `HTTP协议` 的基本知识, 并且通过 `Java` 实现了建立了一个简单的 `HTTPServer`, 结合浏览器完成了 `请求-响应` 的过程。

下一步, 我们将继续模仿 `Python` 中的 `SimpleHTTPServer`, 使我们的 `Java版 SimpleHTTPServer` 也能完成更多相似的功能。

<br><br>

###	7. 相关阅读:

*	[HTTP请求报文和HTTP响应报文](https://www.cnblogs.com/sjm19910902/p/6423181.html)

*	[network, simple_server.py](https://github.com/jJayyyyyyy/network/tree/master/application_layer/http/simple_server)

*	[HTTP权威指南](https://github.com/jJayyyyyyy/SimpleHttpServer/blob/master/Day02/assets/HTTP_The_Definitive_Guide.pdf)

<br><br>
