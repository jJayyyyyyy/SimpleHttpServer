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

###	4. 小结

今天我们学习了 `HTTP协议` 的基本知识, 明天我们将通过 `Java` 实现一个简单的 `HTTP Server`, 然后结合浏览器完成 `请求-响应` 的过程，并继续模仿 `Python` 中的 `SimpleHTTPServer`, 使我们的 `Java版 SimpleHTTPServer` 也能完成更多相似的功能。

<br><br>

###	5. 相关阅读:

*	[HTTP请求报文和HTTP响应报文](https://www.cnblogs.com/sjm19910902/p/6423181.html)

*	[network, simple_server.py](https://github.com/jJayyyyyyy/network/tree/master/application_layer/http/simple_server)

*	[HTTP权威指南](https://github.com/jJayyyyyyy/SimpleHttpServer/blob/master/Day02/assets/HTTP_The_Definitive_Guide.pdf)

<br><br>
