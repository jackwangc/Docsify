> 本文由 [简悦 SimpRead](http://ksria.com/simpread/) 转码， 原文地址 https://juejin.im/post/5ad7e6c35188252ebd06acfa

前言
==

*   计算机网络基础 该是程序猿需掌握的知识，**也是面试过程中常常考察的内容**，但往往会被忽略
*   今天，我将献上一份详细 & 清晰的计算机网络基础 学习指南，涵盖 `TCP / UDP`协议、`Http`协议、`Socket`等，希望你们会喜欢。

目录
==

![](https://user-gold-cdn.xitu.io/2018/4/19/162db5e912aef10b?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

1. 计算机网络体系结构
============

### 1.1 简介

*   定义 计算机网络的各层 + 其协议的集合
*   作用 定义该计算机网络的所能完成的功能

### 1.2 结构介绍

*   计算机网络体系结构分为 3 种：`OSI`体系结构、`TCP` / `IP`体系结构、五层体系结构

> *   `OSI`体系结构：概念清楚 & 理念完整，但复杂 & 不实用
> *   `TCP` / `IP`体系结构：含了一系列构成互联网基础的网络协议，是`Internet`的核心协议 & 被广泛应用于局域网 和 广域网
> *   五层体系结构：融合了`OSI` 与 `TCP` / `IP`的体系结构，目的是为了学习 & 讲解计算机原理

![](https://user-gold-cdn.xitu.io/2018/4/19/162db5e913e9edfe?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

低三层为通信子网，负责数据传输 高三层为资源子网，相当于计算机系统，完成数据处理； 传输层承上启下

*   `TCP` / `IP`的体系结构详细介绍 由于 `TCP` / `IP`体系结构较为广泛，故主要讲解

![](https://user-gold-cdn.xitu.io/2018/4/19/162db5e91476d2c1?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)![](https://user-gold-cdn.xitu.io/2018/4/19/162db5e915708d49?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

*   `OSI`的体系结构详细介绍

![](https://user-gold-cdn.xitu.io/2018/4/19/162db5e917432fa2?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)![](https://user-gold-cdn.xitu.io/2018/4/19/162db5e946d039a3?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

2. TCP 协议
=========

`Transmission Control Protocol`，即 传输控制协议

> 1.  属于 传输层通信协议
> 2.  基于`TCP`的应用层协议有`HTTP`、`SMTP`、`FTP`、`Telnet` 和 `POP3`

关于`TCP`的其他知识：如三次握手、四次挥手、无差错控制原理等，具体请看文章：[计算机网络：这是一份全面 & 详细 的 TCP 协议攻略](https://link.juejin.im?target=https%3A%2F%2Fwww.jianshu.com%2Fp%2F65605622234b)

3. UDP 协议
=========

### 3.1 定义

`User Datagram Protocol`，即 用户数据报协议

> 1.  属于 传输层通信协议
> 2.  基于`UDP`的应用层协议有 `TFTP`、`SNMP` 与 `DNS`

### 3.2 特点

无连接的、不可靠的、面向报文、无拥塞控制，具体介绍如下：

![](https://user-gold-cdn.xitu.io/2018/4/19/162db5e948bc326b?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

### 3.3 优缺点

*   优点：速度快
*   缺点：消息易丢失（特别是 网络较差时）

### 3.4 应用场景（对应应用层协议）

要求通信速度高

> 如： 域名转换：`DNS`协议 文件传输：`FTP`协议 网络管理：`SNMP`协议 远程文件服务器：`NFS`协议

### 3.5 报文段格式

*   UDP 的报文段共有 2 个字段：数据字段 & 首部字段
*   下面主要介绍首部（8 字节、4 个字段）

![](https://user-gold-cdn.xitu.io/2018/4/19/162db5e96bce4e36?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)![](https://user-gold-cdn.xitu.io/2018/4/19/162db5e974cca78a?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

### 3.6 TCP、UDP 协议的区别

![](https://user-gold-cdn.xitu.io/2018/4/19/162db5e97e9a9e01?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

4. HTTP 协议
==========

*   简介
    
    ![](https://user-gold-cdn.xitu.io/2018/4/19/162db5e983291134?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)
*   关于其工作方式、报文段等知识，请看文章：[这是一份全面 & 详细 HTTP 知识讲解](https://link.juejin.im?target=https%3A%2F%2Fwww.jianshu.com%2Fp%2Fa6d086a3997d)
    

5. Socket
=========

### 5.1 简介

*   即套接字，**是应用层 与 `TCP/IP` 协议族通信的中间软件抽象层，表现为一个封装了 TCP / IP 协议族 的编程接口（API）**

![](https://user-gold-cdn.xitu.io/2018/4/19/162db5e9855957b5?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

> 1.  `Socket`不是一种协议，而是一个编程调用接口（`API`），属于传输层（主要解决数据如何在网络中传输）
> 2.  即：通过`Socket`，我们才能在 Andorid 平台上通过 `TCP/IP`协议进行开发
> 3.  对用户来说，只需调用 Socket 去组织数据，以符合指定的协议，即可通信

*   成对出现，一对套接字：

```
Socket ={(IP地址1:PORT端口号)，(IP地址2:PORT端口号)}
复制代码

```

*   一个 `Socket` 实例 唯一代表一个主机上的一个应用程序的通信链路

### 5.2 其他知识

关于`Socket`的原理、机制过程 & 使用方式，具体请看文章：[Android：这是一份很详细的 Socket 使用攻略](https://link.juejin.im?target=https%3A%2F%2Fwww.jianshu.com%2Fp%2F089fb79e308b)

6. 其他知识
=======

### 6.1 在浏览器中输入`url`地址 ->> 显示主页的过程

> 打开一个网页，整个过程会使用哪些协议

![](https://user-gold-cdn.xitu.io/2018/4/19/162db5e985aabdbe?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

### 6.2 IP 地址（IPv4 地址）

*   定义 连接在`Internet`中的每一台主机（或 路由器）的全球唯一的标识符
    
*   组成 IP 地址 = 32 位 = 网络号 + 主机号；即 IP 地址::={<网络号>，< 主机号 >}
    

> 其中：
> 
> 1.  网络号：标志主机（或路由器）所连接到的网络。一个网络号在整个因特网范围内必须是唯一的。
> 2.  主机号：标志该主机（或路由器）。一个主机号在它面前的网络号所指明的网络范围必须是唯一的。

不同类型的 IP 地址，其主机号 & 网络号所占字节数不同；**故：一个 IP 地址在整个网络范围内是唯一的**

*   分类 传统的 IP 地址是分类的地址，分为 A，B，C，D，E 五类

> 区别在于网络号 & 主机号占的字节数不同

![](https://user-gold-cdn.xitu.io/2018/4/19/162db5e98d2e3d6a?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

*   特别注意：在各类 IP 地址中，有一些 IP 地址用于特殊用途，不能用于做主机 IP 地址

![](https://user-gold-cdn.xitu.io/2018/4/19/162db5e9a173b9d4?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

### 6.3 ICMP 协议

*   定义 `Internet Control Message Protocol`，即 网际控制报文协议

> 1.  属于 IP 层协议
> 2.  注：ICMP 报文不是高层协议，而是作为 IP 层数据报的数据，加上数据报首部，组成 IP 数据报发出去

*   作用 更有效地转发 IP 数据包 & 提高交付成功的机会

> 同时允许主机 / 路由器报告差错 & 异常情况

*   分类 `ICMP`差错报告报文 & `ICMP`询问报文
    
*   主要应用 PING（分组网间探测）、Traceroute（跟踪 1 个分组从源点到终点的路径，原理 = 从源主机向目的主机发送一连串的 IP 数据报）
    

下面，将主要介绍`Ping`的过程

### 6.4 Ping 的过程

*   定义 `Packet InterNet Groper`，即分组网间探测

> 1.  是 `ICMP`报文的 1 个重要应用：使用了 IPCM 回送请求 & 回送回答报文

2.  是应用层直接使用网络层 ICMP 的 1 个例子，无经过传输层的 TCP、UDP

*   作用 测试 2 个主机的连通性
    
*   原理
    
    1.  向目的主机发送多个 ICMP 回送请求报文
    2.  根据 目的主机返回的 ICMP 回送回答报文中的时间戳，从而计算出往返时间
    3.  最终显示的结果：发送到目的主机的 IP 地址、发送 & 收到 & 丢失的分组数、往返时间的最小、最大 & 平均值
*   过程 假设有两台主机： （目的主机）PC1：IP = 192.168.1.1 （源主机）PC2：IP = 192.168.1.2
    

![](https://user-gold-cdn.xitu.io/2018/4/19/162db5e9a52710a9?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

### 6.5 路由器与交换机的区别

![](https://user-gold-cdn.xitu.io/2018/4/19/162db5e9ab2eab94?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

### 6.6 Cookie 与 Session

*   简介

![](https://user-gold-cdn.xitu.io/2018/4/19/162db5e9b2002169?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

*   区别 & 对比

![](https://user-gold-cdn.xitu.io/2018/4/19/162db5e9dd713e70?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

### 6.7 Cookie 与 Token

*   简介

![](https://user-gold-cdn.xitu.io/2018/4/19/162db5e9dffa0091?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

*   基于`Cookie`的身份验证 & 验证流程

![](https://user-gold-cdn.xitu.io/2018/4/19/162db5e9e45bf3ed?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)![](https://user-gold-cdn.xitu.io/2018/4/19/162db5e9e6241d4e?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

*   基于`Token`的身份验证 & 验证流程

![](https://user-gold-cdn.xitu.io/2018/4/19/162db5e9e9a30955?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)![](https://user-gold-cdn.xitu.io/2018/4/19/162db5ea08e76322?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

7. 总结
=====

*   本文全面讲解了 开发的基础知识：计算机网络
    
*   下面我将继续讲解 **编程开发**的基础知识，有兴趣可以继续关注 [Carson_Ho 的开发笔记](https://juejin.im/timeline)
    

请点赞！因为你的鼓励是我写作的最大动力！
====================

欢迎关注 carson_ho 的微信公众号
=====================

![](https://user-gold-cdn.xitu.io/2019/4/21/16a3eba97173a8e8?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)![](https://user-gold-cdn.xitu.io/2019/4/21/16a3eba9735e6393?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)