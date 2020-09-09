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

    <title>疫情期间打卡</title>
    
    <style type="text/css">
    	html,body{width:100%; height:auto; margin:0; padding:0; position:relative;}
    </style>
</head>
<body>
    <!---->
    <div id="blank" style="position:fixed; left:0; top:0; right:0; bottom:0; z-index:3; background-color:#fff;"></div>
    <div id="app">
        <div style="position:fixed; top:0; left:0; width:100%; height:62px; z-index:1;">
            <mu-appbar style="width: 100%;" title="疫情期间打卡" color="primary">
                <mu-switch v-if="status===1||status===2" slot="right" v-model="editForm" color="warning"></mu-switch>
            </mu-appbar>
        </div>
        <div style="width:100%; height:62px;"></div>
        <mu-container>
            <mu-form :model="form" label-position="top" label-width="100%">
                <mu-form-item label="姓名">
                    <mu-text-field v-model="form.name" :disabled="editForm?false:true"></mu-text-field>
                </mu-form-item>
                <mu-form-item label="手机号码">
                    <mu-text-field v-model="form.phone" :disabled="editForm?false:true"></mu-text-field>
                </mu-form-item>

                <mu-form-item label="单位部门">
                    <mu-text-field multi-line :rows="2" :rows-max="2" v-model="form.company" :disabled="editForm?false:true"></mu-text-field>
                </mu-form-item>
                <mu-form-item label="工作房间号">
                    <mu-text-field v-model="form.department" :disabled="editForm?false:true"></mu-text-field>
                </mu-form-item>
                <mu-form-item label="体温">
                    <mu-text-field v-model="form.temperature" type="number" :disabled="editForm?false:true"></mu-text-field>
                </mu-form-item>

                <!--<mu-form-item label="年龄">
                    <mu-text-field v-model="form.age" type="number" :disabled="editForm?false:true"></mu-text-field>
                </mu-form-item>
                <mu-form-item label="户籍地址（详细到房间号）">
                    <mu-text-field multi-line :rows="2" :rows-max="2" v-model="form.homeAddress" :disabled="editForm?false:true"></mu-text-field>
                </mu-form-item>
                <mu-form-item label="现居地址（详细到房间号）">
                    <mu-text-field multi-line :rows="2" :rows-max="2" v-model="form.liveAddress" :disabled="editForm?false:true"></mu-text-field>
                </mu-form-item>
                <mu-form-item label="单位地址（详细到房间号）">
                    <mu-text-field multi-line :rows="2" :rows-max="2" v-model="form.workAddress" :disabled="editForm?false:true"></mu-text-field>
                </mu-form-item>
                <mu-form-item label="前往单位出行方式">
                    <mu-text-field multi-line :rows="2" :rows-max="2" v-model="form.wayOfWork" :disabled="editForm?false:true"></mu-text-field>
                </mu-form-item>
                <mu-form-item label="2020年1月1后有无去过武汉">
                    <mu-radio v-model="form.beenToWuhanAfter20200101" value="true" label="是" :disabled="editForm?false:true"></mu-radio>
                    <mu-radio v-model="form.beenToWuhanAfter20200101" value="false" label="否" :disabled="editForm?false:true"></mu-radio>
                </mu-form-item>
                <mu-form-item label="2020年1月1后有无接触疑似确诊病人">
                    <mu-radio v-model="form.contactWithSuspectedOrConfirmedPatientsAfter20200101" value="true" label="是" :disabled="editForm?false:true"></mu-radio>
                    <mu-radio v-model="form.contactWithSuspectedOrConfirmedPatientsAfter20200101" value="false" label="否" :disabled="editForm?false:true"></mu-radio>
                </mu-form-item>
                <mu-form-item label="自2020年1月1日起，在湖北省停留（包括旅游、出差、转车、转机、经停当地有乘客上车或登机等情况）">
                    <mu-radio v-model="form.stayInHubeiSince20200101" value="true" label="是" :disabled="editForm?false:true"></mu-radio>
                    <mu-radio v-model="form.stayInHubeiSince20200101" value="false" label="否" :disabled="editForm?false:true"></mu-radio>
                </mu-form-item>
                <mu-form-item label="近期有无咳嗽发烧症状">
                    <mu-radio v-model="form.coughOrFever" value="true" label="有" :disabled="editForm?false:true"></mu-radio>
                    <mu-radio v-model="form.coughOrFever" value="false" label="无" :disabled="editForm?false:true"></mu-radio>
                </mu-form-item>
                <mu-form-item v-if="status===2||status===3">{{'上班时间：'+form.timeForWork}}</mu-form-item>
                <mu-form-item v-if="status===3">{{'下班时间：'+form.closingTime}}</mu-form-item>-->

                <mu-form-item>
                    <mu-button color="success" :disabled="(status===0||status===1)?false:true" @click="handleTimeForWork">进入</mu-button>
                    <!--<mu-button color="error" :disabled="(status===2)?false:true" @click="handleClosingTime">下班打卡</mu-button>-->
                </mu-form-item>
            </mu-form>
        </mu-container>
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
<script type="text/javascript" src="<%=basePath %>mobile/COVID-19/main.js"></script>
</html>