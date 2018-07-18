# 简介

如果你用过Python，肯定知道其中有一个非常方便好用的内建模块，叫做SimpleHTTPServer，运行方式如下:

```bash
python3 -m http.server
```

使用这个模块既可以搭建一个简单的web server，也可以用于局域网内的文件共享，省得拔插U盘。

另一方面，这个模块很小，用其他语言进行仿写也比较容易。因此，我们不妨用 Java 中的 socket 类来实现一个简单的 HttpServer, 作为 Java 学习过程中的练手项目。

项目地址: [https://github.com/jJayyyyyyy/SimpleHttpServer](https://github.com/jJayyyyyyy/SimpleHttpServer)

<br><br>

# 知识点

本项目总体代码量为500行左右，仅实现了GET方法。项目涉及的知识点包括

* 计算机网络(socket, HTTP协议)

* 流(Stream)

* 多线程

<br><br>

# 参考资料

* [面向对象程序设计——Java语言, 浙江大学, 翁恺](https://www.icourse163.org/course/0809ZJU012-1001542001)

* [Java程序设计, 北京大学, 唐大仕](https://www.icourse163.org/course/0809PKU008-1001941004#/info)

* ps: 以上课程建议去bilibili观看，因为icourse上的快进快退经常会出现问题
