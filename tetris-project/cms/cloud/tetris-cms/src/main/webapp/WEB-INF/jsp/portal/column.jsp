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
    
    <div id="first-level" style="width:1200px;font-size:16px;height:40px;border-bottom:2px solid #456def;margin-top:20px;">
        
         <div style="width:120px;text-align:center;float:left;line-height:30px;">首页</div>
         <div style="width:120px;text-align:center;float:left;line-height:30px;">山地测试</div>
         <div style="width:120px;text-align:center;float:left;line-height:30px;">丘陵盆地</div>
         <div style="width:120px;text-align:center;float:left;line-height:30px;">耕地</div>
         <div style="width:120px;text-align:center;float:left;line-height:30px;">地震火山</div>
         <div style="width:120px;text-align:center;float:left;line-height:30px;">洪涝</div>
         <div style="width:120px;text-align:center;float:left;line-height:30px;">气象信息</div>
    </div>

    <div style="float:left;width:200px;margin-top:30px;font-size:18px;">
         <div style="width:200px;text-align:center;height:40px;line-height:40px;magin-top:10px;">推荐</div>
         <div style="width:200px;text-align:center;height:40px;line-height:40px;magin-top:10px;">高原</div>
         <div style="width:200px;text-align:center;height:40px;line-height:40px;magin-top:10px;">盆地</div>
    </div>
    
    
    <div style="float:left;width:670px;margin-top:20px;">
          <div style="width:100%;height:90px;margin-top:10px">
		      <image src="<%=basePath %>lib/images/4.jpg" style="width:115px;height:90px;float:left;"/>
		      <div style="width:430px;height:90px;float:left;margin-left:15px;padding-top:20px">
		          <span style="color:#172caa;font-size:18px;">北京数码视讯科技股份有限公司研发五部测试</span><br>
		          <span style="font-size:12px;color:#9f82bb">作者名称:Mr黄|已下载:1500</span>
		      </div>
	      </div>
	      
	      <div style="width:100%;height:90px;margin-top:10px">
		      <image src="<%=basePath %>lib/images/4.jpg" style="width:115px;height:90px;float:left;"/>
		      <div style="width:430px;height:90px;float:left;margin-left:15px;padding-top:20px">
		          <span style="color:#172caa;font-size:18px;">北京数码视讯科技股份有限公司研发五部测试</span><br>
		          <span style="font-size:12px;color:#9f82bb">作者名称:Mr黄|已下载:1500</span>
		      </div>
	      </div>
	      
	      <div style="width:100%;height:90px;margin-top:10px">
		      <image src="<%=basePath %>lib/images/4.jpg" style="width:115px;height:90px;float:left;"/>
		      <div style="width:430px;height:90px;float:left;margin-left:15px;padding-top:20px">
		          <span style="color:#172caa;font-size:18px;">北京数码视讯科技股份有限公司研发五部测试</span><br>
		          <span style="font-size:12px;color:#9f82bb">作者名称:Mr黄|已下载:1500</span>
		      </div>
	      </div>
	      
	      <div style="width:100%;height:90px;margin-top:10px">
		      <image src="<%=basePath %>lib/images/4.jpg" style="width:115px;height:90px;float:left;"/>
		      <div style="width:430px;height:90px;float:left;margin-left:15px;padding-top:20px">
		          <span style="color:#172caa;font-size:18px;">北京数码视讯科技股份有限公司研发五部测试</span><br>
		          <span style="font-size:12px;color:#9f82bb">作者名称:Mr黄|已下载:1500</span>
		      </div>
	      </div>
	      
	      <div style="width:100%;height:90px;margin-top:10px">
		      <image src="<%=basePath %>lib/images/4.jpg" style="width:115px;height:90px;float:left;"/>
		      <div style="width:430px;height:90px;float:left;margin-left:15px;padding-top:20px">
		          <span style="color:#172caa;font-size:18px;">北京数码视讯科技股份有限公司研发五部测试</span><br>
		          <span style="font-size:12px;color:#9f82bb">作者名称:Mr黄|已下载:1500</span>
		      </div>
	      </div>
	      
	      <div style="width:100%;height:90px;margin-top:10px">
		      <image src="<%=basePath %>lib/images/4.jpg" style="width:115px;height:90px;float:left;"/>
		      <div style="width:430px;height:90px;float:left;margin-left:15px;padding-top:20px">
		          <span style="color:#172caa;font-size:18px;">北京数码视讯科技股份有限公司研发五部测试</span><br>
		          <span style="font-size:12px;color:#9f82bb">作者名称:Mr黄|已下载:1500</span>
		      </div>
	      </div>

    
    
    </div>
    
    
    
    
    
    <div style="float:left;width:320px;">
    
    	<div style="width:320px;margin-top:20px;">
    	    <div class="third-header" style="width:150px;hegith:30px;float:left">
    	        <span style="font-weight:bold;color:#466cfe">|</span>&nbsp;&nbsp;热门列表</div>
    	    <a href="javascript:void(0)" style="float:right;width:100px;font-size:12px;display:none">更多></a>
    	    
    	    <ul style="width:100%;margin-left:-20px;float:left">
    	       <li style="color:#6283ff;padding:5px;"><a style="color:black" href="javascript:void(0)">湖北发布省寒流预警</a></li>
    	       <li style="color:#6283ff;padding:5px;"><a style="color:black" href="javascript:void(0)">湖北发布省寒流预警</a></li>
    	       <li style="color:#6283ff;padding:5px;"><a style="color:black" href="javascript:void(0)">湖北发布省寒流预警</a></li>
    	       <li style="color:#6283ff;padding:5px;"><a style="color:black" href="javascript:void(0)">北京数码视讯科技股份有限公司新媒</a></li>
    	       <li style="color:#6283ff;padding:5px;"><a style="color:black" href="javascript:void(0)">湖北发布省寒流预警</a></li>
    	       <li style="color:#6283ff;padding:5px;"><a style="color:black" href="javascript:void(0)">北京数码视讯科技股份有限公司新媒</a></li>
    	       <li style="color:#6283ff;padding:5px;"><a style="color:black" href="javascript:void(0)">湖北发布省寒流预警</a></li>
    	       <li style="color:#6283ff;padding:5px;"><a style="color:black" href="javascript:void(0)">湖北发布省寒流预警</a></li>
    	       <li style="color:#6283ff;padding:5px;"><a style="color:black" href="javascript:void(0)">北京数码视讯科技股份有限公司新媒</a></li>
    	    </ul>
	
    	</div>
    	
    	<div style="width:300px;float:left;margin-top:20px;">
            <div class="third-header" style="width:150px;hegith:30px;float:left;">
                <span style="font-weight:bold;color:#466cfe">|</span>&nbsp;&nbsp;下载排行榜</div>
            <div style="width:100%;margin-top:30px;">
                <a href="javascript:void(0)" style="width:100%;float:left;height:65px;margin-top:10px;">
	                <image src="<%=basePath %>lib/images/4.jpg" style="width:77px;height:65px;float:left"/>
	                <div style="width:160px;float:left;line-height:65px;margin-left:5px;">研发五部测试共享平台</div>
	                <div style="width:50px;float:left;
	                padding-left:10px;font-size:10px;line-height:65px;margin-left:5px;background:url('<%=basePath %>lib/images/u270.png') no-repeat 0 center;">
	                12500</div>
	            </a>   
            
                <a href="javascript:void(0)" style="width:100%;float:left;height:65px;margin-top:10px;">
	                <image src="<%=basePath %>lib/images/4.jpg" style="width:77px;height:65px;float:left"/>
	                <div style="width:160px;float:left;line-height:65px;margin-left:5px;">研发五部测试共享平台</div>
	                <div style="width:50px;float:left;
	                padding-left:10px;font-size:10px;line-height:65px;margin-left:5px;background:url('<%=basePath %>lib/images/u270.png') no-repeat 0 center;">
	                12500</div>
	            </a>   
	            <a href="javascript:void(0)" style="width:100%;float:left;height:65px;margin-top:10px;">
	                <image src="<%=basePath %>lib/images/4.jpg" style="width:77px;height:65px;float:left"/>
	                <div style="width:160px;float:left;line-height:65px;margin-left:5px;">研发五部测试共享平台</div>
	                <div style="width:50px;float:left;
	                padding-left:10px;font-size:10px;line-height:65px;margin-left:5px;background:url('<%=basePath %>lib/images/u270.png') no-repeat 0 center;">
	                12500</div>
	            </a>   
	            <a href="javascript:void(0)" style="width:100%;float:left;height:65px;margin-top:10px;">
	                <image src="<%=basePath %>lib/images/4.jpg" style="width:77px;height:65px;float:left"/>
	                <div style="width:160px;float:left;line-height:65px;margin-left:5px;">研发五部测试共享平台</div>
	                <div style="width:50px;float:left;
	                padding-left:10px;font-size:10px;line-height:65px;margin-left:5px;background:url('<%=basePath %>lib/images/u270.png') no-repeat 0 center;">
	                12500</div>
	            </a>   
            
            </div>
            
            
             
        </div>
    	
    	
    	
    
    </div>


    
</div>	 
</body>


</html>