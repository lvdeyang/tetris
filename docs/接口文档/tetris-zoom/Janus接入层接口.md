# Janus接入层接口

[TOC]

## 接入接口(给业务)

### 创建会议室
http-url: 

http://xxx.xxx.xxx.xxx:8288/action/create_room

method: POST

请求：

~~~json
{
	roomId:"会议室ID"
}
~~~

成功响应：
~~~json
{
	ret:"success"
}
~~~
失败响应：
~~~json
{
	ret:"failed"
}
~~~

### 销毁会议室

http-url

http://xxx.xxx.xxx.xxx:8288/action/destroy_room

method: POST

请求：

~~~json
{
	roomId:"会议室ID"
}
~~~

成功响应：
~~~json
{
	ret:"success"
}
~~~
失败响应：
~~~json
{
	ret:"failed"
}
~~~