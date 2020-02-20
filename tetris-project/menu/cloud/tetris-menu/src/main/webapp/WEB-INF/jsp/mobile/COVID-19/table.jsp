<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String host = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
	String scheme = request.getContextPath();
	String basePath = host + scheme + "/";
	/* String token = (String)request.getAttribute("token");
	token = token==null?"":token;
	String sessionId = (String)request.getAttribute("sessionId");
	sessionId = sessionId==null?"":sessionId; */
%>
<!DOCTYPE HTML>
<html lang="zh-cmn-Hans">
<head>

    <!-- 声明文档使用的字符编码 -->
    <meta charset='utf-8'>
    <!-- 优先使用 IE 最新版本和 Chrome -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <!-- 页面描述 -->
    <meta name="description" content=""/>
    <!-- 页面关键词 -->
    <meta name="keywords" content=""/>
    <!-- 网页作者 -->
    <meta name="author" content="name, email@gmail.com"/>
    <!-- 搜索引擎抓取 -->
    <meta name="robots" content="index,follow"/>
    <!-- 为移动设备添加 viewport -->
    <meta name="viewport" content="initial-scale=1, maximum-scale=3, minimum-scale=1, user-scalable=no">
    <!-- `width=device-width` 会导致 iPhone 5 添加到主屏后以 WebApp 全屏模式打开页面时出现黑边 http://bigc.at/ios-webapp-viewport-meta.orz -->

    <!-- iOS 设备 begin -->
    <meta name="apple-mobile-web-app-title" content="标题">
    <!-- 添加到主屏后的标题（iOS 6 新增） -->
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <!-- 是否启用 WebApp 全屏模式，删除苹果默认的工具栏和菜单栏 -->

    <meta name="apple-itunes-app" content="app-id=myAppStoreID, affiliate-data=myAffiliateData, app-argument=myURL">
    <!-- 添加智能 App 广告条 Smart App Banner（iOS 6+ Safari） -->
    <meta name="apple-mobile-web-app-status-bar-style" content="black"/>
    <!-- 设置苹果工具栏颜色 -->
    <meta name="format-detection" content="telphone=no, email=no"/>
    <!-- 忽略页面中的数字识别为电话，忽略email识别 -->
    <!-- 启用360浏览器的极速模式(webkit) -->
    <meta name="renderer" content="webkit">
    <!-- 避免IE使用兼容模式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- 不让百度转码 -->
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <!-- 针对手持设备优化，主要是针对一些老的不识别viewport的浏览器，比如黑莓 -->
    <meta name="HandheldFriendly" content="true">
    <!-- 微软的老式浏览器 -->
    <meta name="MobileOptimized" content="320">
    <!-- uc强制竖屏 -->
    <meta name="screen-orientation" content="portrait">
    <!-- QQ强制竖屏 -->
    <meta name="x5-orientation" content="portrait">
    <!-- UC强制全屏 -->
    <meta name="full-screen" content="yes">
    <!-- QQ强制全屏 -->
    <meta name="x5-fullscreen" content="true">
    <!-- UC应用模式 -->
    <meta name="browsermode" content="application">
    <!-- QQ应用模式 -->
    <meta name="x5-page-mode" content="app">
    <!-- windows phone 点击无高光 -->
    <meta name="msapplication-tap-highlight" content="no">

    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/lib/ui/muse-ui/muse-ui.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/lib/ui/muse-ui/plugins/muse-ui-loading/muse-ui-loading.all.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/lib/ui/muse-ui/plugins/muse-ui-message/muse-ui-message.all.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/lib/ui/muse-ui/plugins/muse-ui-progress/muse-ui-progress.all.css"/>

    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/lib/animate/animate.css">

    <!-- icons -->
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/lib/icon/Font-Awesome-3.2.1/css/font-awesome.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/lib/icon/feather/style.css"/>

    <script type="text/javascript">
        window.HOST = '<%=host%>';
        window.SCHEMA = '<%=scheme%>';
        window.BASEPATH = '<%=basePath%>';
    </script>

    <title>疫情期间打卡统计</title>
    
    <style type="text/css">
    	html,body{width:100%; height:100%; margin:0; padding:0; position:relative;}
        .mu-table th{white-space:normal;}
    </style>
</head>
<body>
    <div id="app" style="width:100%; height:100%;">
        <div style="position:absolute; left:0; top:0; right:0; bottom:50px;">
            <mu-data-table
                border
                height="100%"
                :columns="table.columns"
                :loading="table.loading"
                :data="table.data">
                <template slot-scope="scope">
                    <td>{{scope.row.name}}</td>
                    <td>{{scope.row.updateTime.split(' ')[0]}}</td>
                    <td>
                        <span v-if="scope.row.temperature && parseFloat(scope.row.temperature)>37.2" style="color:#f44336;">{{scope.row.temperature}}</span>
                        <span v-else>{{scope.row.temperature}}</span>
                    </td>
                    <td>{{scope.row.company}}</td>
                    <td>{{scope.row.department}}</td>
                    <td>{{scope.row.phone}}</td>
                    <td>{{scope.row.timeForWork}}</td>
                    <!--
                    <td>{{scope.row.age}}岁</td>
                    <td>{{scope.row.homeAddress}}</td>
                    <td>{{scope.row.liveAddress}}</td>
                    <td>{{scope.row.workAddress}}</td>
                    <td>{{scope.row.wayOfWork}}</td>
                    <td>
                        <span v-if="scope.row.beenToWuhanAfter20200101" style="color:#f44336;">是</span>
                        <span v-else>否</span>
                    </td>
                    <td>
                        <span v-if="scope.row.contactWithSuspectedOrConfirmedPatientsAfter20200101" style="color:#f44336;">是</span>
                        <span v-else>否</span>
                    </td>
                    <td>
                        <span v-if="scope.row.stayInHubeiSince20200101" style="color:#f44336;">是</span>
                        <span v-else>否</span>
                    </td>
                    <td>
                        <span v-if="scope.row.coughOrFever" style="color:#f44336;">是</span>
                        <span v-else>否</span>
                    </td>
                    <td>{{scope.row.closingTime}}</td>
                    -->
                </template>
            </mu-data-table>
        </div>
        <div style="position:absolute; bottom:0; left:0; height:50px; width:100%; padding-top:9px;">
            <mu-flex justify-content="center">
                <mu-pagination
                    raised
                    circle
                    :total="table.page.total"
                    :current.sync="table.page.currentPage"
                    :page-size="table.page.pageSize"
                    @change="currentPageChange"></mu-pagination>
            </mu-flex>
        </div>
    </div>
</body>
<script type="text/javascript" src="<%=basePath %>web/lib/frame/vue/vue-2.5.16.js"></script>
<script type="text/javascript" src="<%=basePath %>web/lib/frame/jQuery/jquery-2.2.3.js"></script>
<script type="text/javascript" src="<%=basePath %>web/lib/frame/jQuery/jquery.json.js"></script>
<script type="text/javascript" src="<%=basePath %>web/lib/ui/muse-ui/muse-ui.js"></script>
<script type="text/javascript" src="<%=basePath %>web/lib/ui/muse-ui/plugins/muse-ui-loading/muse-ui-loading.js"></script>
<script type="text/javascript" src="<%=basePath %>web/lib/ui/muse-ui/plugins/muse-ui-message/muse-ui-message.js"></script>
<script type="text/javascript" src="<%=basePath %>web/lib/ui/muse-ui/plugins/muse-ui-toast/muse-ui-toast.js"></script>
<script type="text/javascript" src="<%=basePath %>web/lib/ui/muse-ui/plugins/muse-ui-progress/muse-ui-progress.js"></script>
<script type="text/javascript" src="<%=basePath %>web/commons/date/date.ext.js"></script>
<script type="text/javascript" src="<%=basePath %>mobile/COVID-19/table.js"></script>
</html>