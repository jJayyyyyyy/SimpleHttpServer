#	Socket

###	1. 什么是 socket

通过 `IP` 地址可以找到主机, 而要找到特定的进程, 则还需要一个端口号`port`, 这种 `<ip:port>` 的结构就叫做 `socket`, 用于两个进程之间的通信。

实际上 `socket` 是一个比较复杂的结构, 同学们可以自行搜索相关资料。下面讲讲怎么使用 `Java` 中的 `socket` 类。

>	注意, 本实验需要用到 `Linix/Unix` 中的工具 `nc`

<br>

###	2. 如何创建 socket

首先我们在 Eclipse 中新建一个`工程 HttpServer`, 并新建一个叫做 `httpserver` 的包(package)。

然后我们新建一个类, 叫做 `MySocket.java` , 内容如下

```java
public class MySocket{
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

	public void socketTest() {
		try {
			// 创建一个 Socket 实例
			mSocket = new Socket(mHost, mPort);
			// 开启快速重用
			mSocket.setReuseAddress(true);

			// do something

			// socket 用完之后需要关闭
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
```

可以看到, 要创建一个 `socket实例` 非常简单, 只需指定 `host` 和 `port` 即可。

接下来, 我们还需要开启`快速重用 socket`, 否则端口会被占用一段时间。

最后, 当 `socket` 使用完成之后, 还需要将其关闭以释放资源。

<br>

###	3. socket client 发送和接收数据

下面我们将进行4组测试，以便快速上手 `socket`.

*	3.1 数据发送

	添加一个方法 `send()`, 待发送的数据是 `"Hello world!"`, 编码方式是 `UTF-8`

	```java
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
			mSocket.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		MySocket mySocket = new MySocket();
		mySocket.send("Hello world!");
	}
	```

	此时如果我们直接运行程序, 则会报错 `java.net.ConnectException: Connection refused (Connection refused)`, 因为目前没有程序在监听 `8888` 端口.

	为此, 我们将借助 `nc`. 打开 `terminal`, 输入如下指令

	```bash
	nc -l 8888
	```

	这样就新建了一个监听本机 `8888` 端口的 `server`。这之后, 我们再运行 `test1()` , 就可以在terminal中看到 `"hello world"` 了。

	PS: 如果你的电脑上没有 nc （比如Windows）也没关系，只要有 `Python 3` 就好了

	```
	python -m http.server 8888
	```

	就会启动一个监听 8888 端口的 server，同样可以帮助我们测试

*	3.2, 数据接收

	添加一个方法 `recv()`

	```java
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
	```

	接着, 先在 terminal 中先运行 `nc`

	```bash
	nc -l 8888
	```

	然后运行 `test2()`, 接着回到 `nc`, 输入 `"hi"` , 就可以在 Eclipse 的 Console 中看到 `"hi"` 了

	PS: 也可以使用最下面参考资料中的 `Python Server with echo` 进行测试

*	3.3, 数据接收, 逐行读取

	在 test2 中, 我们用于接收数据的 `rxbuf` 的大小与实际数据的大小不一定相同, 这就导致 `rxbuf` 末尾可能存在多余的 `0` (Windows下会显示为小方框). 有两种解决办法: 1. 如果事先知道数据大小, 那么就创建相应大小的空间。2. 如果数据是文本, 那么可以先把数据读到缓冲区, 然后一行一行地读出来。下面是第 2 种方法的代码。

	```java
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
	```

	结束测试时，在 nc 中发送 `q`，这样即可满足 `if( line.equals("q") == true )` 的判断条件，从而退出循环。

###	4. socket server, 持续监听端口

*	在前面的例子中, 如果没有提先运行 `nc` 就运行 `send()`, `recv()` 或者 `recvLineByLine`, 那么会得到错误信息 `Connection refused`。也就是说，我们前面写的都是 `client` 向 `server` 发消息的过程。下面我们来尝试写一下 `server`

	一般来说 `server` 会有一个专门用于监听 (`listen`) 的 `serversocket`, 它会 `accept()` 客户端发来的连接请求, 并产生一个新的 `socket` 与客户端进行通信(原来的 `serversocket` 则继续进行监听)。为了实现上述功能, 我们需要用到 `ServerSocket` 类。

	我们新建一个 `MySocketServer.java`，然后包装出一个 `listen()`

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

	当客户端发来一个连接请求的时候，`serverSocket` 就 `accept()` ，并创建一个新的 `clientSocket = serverSocket.accept();`

	这个新的clientSocket用于和客户端进行通信，而原来的serverSocket则继续进行监听

	```java
	public void recvLineByLine() {
		try {
			Socket socket = serverSocket.accept();

			// 类似于 MySocket.java 里面的  recvLineByLine()

			socket.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	```

	测试的时候, 首先运行 `main()`, 然后使用 `nc 127.0.0.1 8888` 发送数据, 这样我们就能在 `Eclipse` 的 `console` 中接收到数据了.

	当有客户端发起连接请求时, 负责监听的 `serverSocket` 会对其 `accept()`, 于是我们便得到了一个 `clientSocket`, 用于和客户端通信

	PS: 也可以使用 `Python3` 进行测试，详见最下方的 【测试 `MySocketServer.java`】

<br>

###	小结

以上就是关于 `socket` 的简介、使用与测试。下一篇，我们将学习 `HTTP 协议`，并结合本篇内容，与客户端(浏览器)进行基本的 `request and response`, 即请求与响应。

<br>

###	相关阅读:

*	[计算机网络](https://github.com/jJayyyyyyy/network)

*	[nc](https://linux.die.net/man/1/nc)

*	[Java 流的应用 --- socket](https://www.bilibili.com/video/av20359445/?p=58)

*	[开启快速重用socket](https://docs.oracle.com/javase/7/docs/api/java/net/StandardSocketOions.html)

*	[输入输出流](https://github.com/jJayyyyyyy/JavaNotes/blob/master/note16.md)

*	[ServerSocket, 工作流程](https://bbs.csdn.net/topics/390649992?page=1)

*	[Python Docs, Socket](https://docs.python.org/3/library/socket.html)

	[server with echo](https://github.com/jJayyyyyyy/network/blob/master/transport_layer/tcp/server_with_echo.py)

	测试 `MySocket.java`

	```python
	# https://docs.python.org/3/library/socket.html#example
	import socket

	def myServer(option):
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
		myServer(1)
		myServer(2)
		myServer(3)
	```

	测试 `MySocketServer.java`

	```python
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
	````
