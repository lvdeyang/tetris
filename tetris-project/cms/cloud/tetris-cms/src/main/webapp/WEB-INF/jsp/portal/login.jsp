<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML>
<html lang="zh-cmn-Hans">
<head>

<!-- 声明文档使用的字符编码 -->
<meta charset='utf-8'>
<!-- 优先使用 IE 最新版本和 Chrome -->
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<!-- 页面描述 -->
<meta name="description" content="" />
<!-- 页面关键词 -->
<meta name="keywords" content="" />
<!-- 网页作者 -->
<meta name="author" content="name, email@gmail.com" />
<!-- 搜索引擎抓取 -->
<meta name="robots" content="index,follow" />
<!-- 启用360浏览器的极速模式(webkit) -->
<meta name="renderer" content="webkit">
<!-- 避免IE使用兼容模式 -->
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!-- 微软的老式浏览器 -->
<meta name="MobileOptimized" content="320">

<title>湖北应急广播节目融合共享平台</title>

<style type="text/css">
a {
	cursor: pointer !important;
}

a, a:link, a:active, a:visited, a:hover {
	color: inherit;
	text-decoration: none;
}

html, body {
	width: 100%;
	min-height: 100%;
	background-color: #fff;
	position: relative;
	-webkit-text-size-adjust: none;
	background-color: #fbfbfb;
	text-decoration: none !important;
	margin :0;
	padding:0;
}

* {
	box-sizing: border-box;
}

</style>
<script type="text/javascript" src="<%=basePath %>mobile/lib/jQuery-weui-v1.2.0/lib/jquery-2.1.4.js"></script>
<script type="text/javascript" src="<%=basePath %>mobile/lib/jQuery-weui-v1.2.0/js/jquery-weui.js"></script>
</head>


<script>
   
	$(function() {
	
	
	
	});

</script>





<body>
<div id="header" style="width:100%;height:60px;background:#2a2a2a"></div>
<div id="body" style="margin:0 auto;width:1200px;">
    
    <div style="color:#FFF;font-size:18px;font-weight:bold;width:300px;height:70px;background:#456def;
        position:absolute;margin-left:0px;top:0px;line-height:70px;text-align:center">湖北新媒体广播共享门户</div>
    <div style="color:#FFF;font-size:18px;font-weight:bold;width:200px;height:70px;
        background:url('<%=basePath %>lib/images/u667.png') no-repeat 0 center;
        position:absolute;margin-left:1000px;top:0px;line-height:70px;padding-left:50px;">登录
        <image src="<%=basePath %>lib/images/u669.png" style="width:20px;height:20px;position:absolute;top:25px;margin-left:-78px;"/>
        </div>
    
     <div style="width:1200px;background:#f2f9ff;height:800px;">
     
          <div style="font-size:16px;width:1200px;height:60px;text-align:center;margin-top:100px;font-weight:bold">欢迎登录应急广播共享平台</div>
          <table style="width:300px;margin:0 auto">
             <tr><td style="width:80px;color:#8e9fc0;height:40px;">用户名</td><td><input type="text" style="border:1px solid #CCC;height:30px;width:200px;"/><td></tr>
             <tr><td style="width:80px;color:#8e9fc0;height:40px;">密码</td><td><input type="password" style="border:1px solid #CCC;height:30px;width:200px;"/><td></tr>
          
          </table>
          <div style="width:100%;height:50px;"></div>
          <a href="javascript:void(0)" id="login" style="display:block;margin:0 auto;
             font-size:18px;font-weight:bold;color:#FFF;width:280px;height:40px;line-height:40px;
             text-align:center;background:#456def;border-radius:20px 20px 20px 20px;">登录</a>
     </div>
     
     
     
    
</div>	 
</body>


</html>