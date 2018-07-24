#	Socket

###	1. 什么是 socket

通过 `IP` 地址可以找到主机, 而要找到特定的进程, 则还需要一个端口号`port`, 这种 `<ip:port>` 的结构就叫做 `socket`, 用于两个进程之间的通信。

实际上 `socket` 是一个比较复杂的结构, 同学们可以自行搜索相关资料。下面讲讲怎么使用 `Java` 中的 `socket` 类。

>	注意, 本实验需要用到 `Linix/Unix` 中的工具 `nc`

<br>

###	2. 如何创建 socket

首先我们在 Eclipse 中新建一个`工程 HttpServer`, 并新建一个叫做 `httpserver 的包(package)`。

然后我们新建一个类, 叫做 `MySocket.java` , 内容如下

```java
public class MySocket{
	private String host = "127.0.0.1";
	private int port = 8888;
	private Socket socket;

	public MySocket() {
		try {
			this.socket = new Socket(InetAddress.getByName(host), port); // 创建一个 Socket 实例

			socket.setReuseAddress(true); // 开启快速重用

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			socket.close();	// 用完之后需要关闭 socket
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
```

可以看到, 要创建一个 `socket实例` 非常简单, 只需指定 `host` 和 `port` 即可。

接下来, 我们还需要开启`快速重用 socket`, 否则端口会被占用一段时间。

最后, 当 `socket` 使用完成之后, 还需要将其关闭以释放资源。

<br>

###	3. socket 通信测试

下面我们将进行4组测试，以便快速上手 `socket`.

*	test1, 数据发送

	添加一个测试方法 `test1()`, 待发送的数据是 `"hello world"`, 编码方式是 UTF-8, 再编写一个 `main()` 方法作为程序入口

	```java
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

	public static void main(String[] args) {
		MySocket testSocket = new MySocket();
		testSocket.test1();
		testSocket.close();
	}
	```

	此时如果我们直接运行程序, 则会报错 `java.net.ConnectException: Connection refused (Connection refused)`, 因为目前没有程序在监听 `8888` 端口.

	为此, 我们将借助 `nc`. 打开 `terminal`, 输入如下指令

	```bash
	nc -l 8888
	```

	这样就新建了一个监听本机 `8888` 端口的 `server`。这之后, 我们再运行 `test1()` , 就可以在terminal中看到 `"hello world"` 了。

*	test2, 数据接收

	添加一个测试方法 `test2()`

	```java
	public void test2() {
		System.out.println("start test2---");
		try {
			byte[] rxbuf = new byte[16];	// 构造一个字节数组作为接收区, 大小是 16 字节, 未填充的元素值默认为 0
			
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
	```

	接着, 先在 terminal 中先运行 `nc`

	```bash
	nc -l 8888
	```

	然后运行 `test2()`, 接着回到 `nc`, 输入 `"hi"` , 就可以在 Eclipse 的 Console 中看到 `"hi"` 了

*	test3, 数据接收, 逐行读取

	在 test2 中, 我们用于接收数据的 `rxbuf` 的大小与实际数据的大小不一定相同, 这就导致 `rxbuf` 末尾可能存在多余的 `0`. 有两种解决办法: 1. 如果事先知道数据大小, 那么就创建相应大小的空间。2. 如果数据是文本, 那么可以先把数据读到缓冲区, 然后一行一行地读出来。下面是第 2 种方法的代码。

	```java
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
	```

*	test4, 持续监听端口

	在前面的例子中, 如果没有事先运行 `nc` 就运行 `test1()` 或 `test2()`, 那么会得到错误信息`Connection refused`。

	而一般的 server 会有一个专门用于监听(`listen`) 的 `serversocket`, 它会 `accept()` 客户端发来的连接请求, 并产生一个新的 `socket` 与客户端进行通信(原来的 `serversocket` 则继续进行监听)。为了实现上述功能, 我们需要用到 `ServerSocket` 类。

	我们新建一个 `MyServerSocket.java`

	```java
	public class MyServerSocket {
		private String host = "127.0.0.1";
		private int port = 8888;
		public ServerSocket serverSocket;

		public MyServerSocket() {
			try {
				serverSocket = new ServerSocket(port, 5, InetAddress.getByName(host));	// 创建一个 ServerSocket 实例
				System.out.println(serverSocket.getLocalPort());

				serverSocket.setReuseAddress(true);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	```
	
	有了 `serverSocket` , 我们就可以进行 `test4()` 了

	```java
	public void test4() {
		System.out.println("start test4---");
		try {
			Socket clientSocket = serverSocket.accept();
			readFromSocket(clientSocket);
			
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("end test4---");
	}
	```
	
	当有客户端发起连接请求时, 负责监听的 `serverSocket` 会对其 `accept()`, 于是我们便得到了一个 `clientSocket` , 用于和客户端通信。接下来的 `readFromSocket()` 就是之前的 `test3()`, 这里不再赘述。

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
