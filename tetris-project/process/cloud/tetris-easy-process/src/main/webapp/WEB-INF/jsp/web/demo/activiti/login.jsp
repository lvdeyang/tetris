<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>工作流示例</title>
</head>
<body>
	<a href="<%=basePath%>demo/home?username=employee">员工登录</a>
	<a href="<%=basePath%>demo/home?username=employee1">员工1登录</a>
	<a href="<%=basePath%>demo/home?username=manager">经理登录</a>
</body>
</html>