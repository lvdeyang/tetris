<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String host = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
	String scheme = request.getContextPath();
	String basePath = host + scheme + "/";
	//String token = request.getParameter("token");
	//String scope = request.getParameter("scope");
	String token = (String)request.getAttribute("token");
	token = token==null?"":token;
	String scope = (String)request.getAttribute("scope");
	scope = scope==null?"":scope;
	String sessionId = (String)request.getAttribute("sessionId");
	sessionId = sessionId==null?"":sessionId;

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

    <!-- icons -->
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/lib/icon/Font-Awesome-3.2.1/css/font-awesome.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>web/app/icons/header/style.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/lib/icon/feather/style.css"/>
    
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/lib/ui/element-ui/element-ui-2.4.3.min.css"/>

    <!-- components -->
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-header/bvc2-header.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-card/bvc2-card.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-tab/bvc2-tab.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-tab-buttons/bvc2-tab-buttons.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-auto-layout/bvc2-auto-layout.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-auto-layout/bvc2-audo-layout-polling-dialog.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-layout-buttons/bvc2-layout-buttons.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-layout-table-source/bvc2-layout-table-source.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-layout-table-destination/bvc2-layout-table-destination.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-table-agenda/bvc2-table-agenda.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-tree-meeting-member/bvc2-tree-meeting-member.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-tree-source-list/bvc2-tree-source-list.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-tree-member-channel-encode/bvc2-tree-member-channel-encode.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-system-nav-side/bvc2-system-nav-side.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-group-nav-side/bvc2-group-nav-side.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-system-table-base/bvc2-system-table-base.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-group-table-base/bvc2-group-table-base.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-dialog-auto-layout/bvc2-dialog-auto-layout.css"/>
	<link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-tab-agenda-or-scheme/bvc2-tab-agenda-or-scheme.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-transfer-audio-target/bvc2-transfer-audio-target.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-transfer-source-target/bvc2-transfer-source-target.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-transfer-role-target/bvc2-transfer-role-target.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-dialog-edit-role/bvc2-dialog-edit-role.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-dialog-set-audio/bvc2-dialog-set-audio.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-dialog-set-forward/bvc2-dialog-set-forward.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-group-param-aside/bvc2-group-param-aside.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-update-spokesman/bvc2-update-spokesman.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-update-roles/bvc2-update-roles.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-preview-layout/bvc2-preview-layout.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-preview-layout/bvc2-preview-layout-polling-dialog.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-dialog-authorization-list/bvc2-dialog-authorization-list.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-set-small-screen/bvc2-set-small-screen.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-transfer-authority-target/bvc2-transfer-authority-target.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/bvc2-dialog-quick-group/bvc2-dialog-quick-group.css"/>

    <!-- page -->
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/error/page-error.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/group/resource/page-group-resource.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/group/resource/update-spokesman/page-group-param-update-spokesman.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/group/resource/update-roles/page-group-param-update-roles.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/group/resource/virtual-source/page-group-virtual-source.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/group/record/page-group-record.css"/>

    <!-- jquery-components -->
    <link rel="stylesheet" type="text/css" href="<%=basePath %>web/app/component/jQuery/jQuery.layout.auto/css/jQuery.layout.auto.css"/>

    <script type="text/javascript">
        window.HOST = '<%=host%>';
        window.SCHEMA = '<%=scheme%>';
        window.BASEPATH = '<%=basePath%>';
        window.LIBPATH = 'web/lib/';
        window.APPPATH = 'web/app/';
        window.COMMONSPATH = 'web/commons/';
        window.TOKEN = '<%=token%>';
        window.SESSIONID = '<%=sessionId%>';
        window.SCOPE = '<%=scope%>';
    </script>

    <style type="text/css">
        /* reset */
        html,
        body{width:100%; height:100%; margin:0; padding:0;}
        body{background-color:#F2F3F7;}
        aside,main{height:100%!important;}
        .page-wrapper{width:100%; height:100%; padding:0; border:0; position:relative;}
        .page-body{position:absolute; top:92px; left:0; right:0; bottom:0; padding:15px 0 15px 15px;}
        .page-body-new{position:absolute; top:50px; left:0; right:0; bottom:0;}
        .page-body>.el-row{width:100%!important;}
        .page-body>.el-row,
        .page-body>.el-row>.el-col{height:100%!important;}
        .el-loading-mask{transition:none !important;}
        /* 把资源的横向导航条隐藏 */
        .el-menu--horizontal{display:none!important;}
        ul.el-menu{overflow-y:auto!important;}
        ul.el-menu ul.el-menu{overflow-y:hidden!important;}
        #page-group-list .bvc2-system-table-base-head{width:auto;}
        .el-tree{position: absolute; min-width:100%; overflow: auto;}
        .el-tree .bvc2-tree-node-custom{font-size: 15px;}
    </style>

    <title>视频监控平台</title>
</head>
<body>
    <div id="app" class="page-wrapper" v-loading.fullscreen.lock="loading">
        <router-view></router-view>
        <bvc2-monitor-call></bvc2-monitor-call>
    </div>
</body>
<script type="text/javascript" src="<%=basePath %>web/lib/frame/requireJS/require.js"  defer async="true" data-main="<%=basePath %>web/app/main"></script>
</html>