require.config({
    baseUrl: window.BASEPATH,
    paths: {
        /* lib */
        'text':window.LIBPATH + 'frame/requireJS/plugins/text',
        'css':window.LIBPATH + 'frame/requireJS/plugins/css',
        'vue':window.LIBPATH + 'frame/vue/vue-2.5.16',
        'vue-router':window.LIBPATH + 'frame/vue/vue-router-3.0.1',
        'jquery':window.LIBPATH + 'frame/jQuery/jquery-2.2.3.min',
        'json':window.LIBPATH + 'frame/jQuery/jquery.json',
        'element-ui':window.LIBPATH + 'ui/element-ui/element-ui-2.4.3.min',

        'TweenLite':window.LIBPATH + 'TweenMax/cmd/TweenLite',

        /* commons */
        'context':window.COMMONSPATH + 'context/context',
        'page-wrapper':window.COMMONSPATH + 'page/page-wrapper',
        'router':window.COMMONSPATH + 'router/router',
        'date':window.COMMONSPATH + 'date/date.ext',
        'string':window.COMMONSPATH + 'string/string.ext',
        'storage':window.COMMONSPATH + 'storage/storage.ext',
        'restfull':window.COMMONSPATH + 'restfull/restfull.api',
        'file':window.COMMONSPATH + 'uploader/File',
        'uploader':window.COMMONSPATH + 'uploader/Uploader',
        'menu':window.COMMONSPATH + 'menu/menu',
        'bpmn-ext':window.COMMONSPATH + 'bpmn/ext/BpmnExtension',

        /* app */
        'config':window.APPPATH + 'config',
        'commons':window.APPPATH + 'commons',

        /* error */
        'page-error':window.APPPATH + 'error/page-error',

        /* component */
        'mi-frame':window.APPPATH + 'component/frame/frame',
        'mi-sub-title':window.APPPATH + 'component/sub-title/sub-title',
        'mi-folder-dialog':window.APPPATH + 'component/dialog/folder/folder-dialog',
        'mi-task-view':window.APPPATH + 'component/view/task/task-view',
        'mi-upload-dialog':window.APPPATH + 'component/dialog/upload/upload-dialog',
        'mi-lightbox':window.APPPATH + 'component/lightbox/lightbox',
        'mi-user-dialog':window.APPPATH + 'component/dialog/user/user-dialog',
        'mi-process-dialog':window.APPPATH + 'component/dialog/process/process-dialog',

        /* pages */

        /* user-pages */
        'page-home':window.APPPATH + 'front/home/page-home',
        'page-material':window.APPPATH + 'front/material/page-material',
        'page-media-txt':window.APPPATH + 'front/media/txt/page-media-txt',
        'page-media-audio':window.APPPATH + 'front/media/audio/page-media-audio',
        'page-media-audio-stream':window.APPPATH + 'front/media/audio-stream/page-media-audio-stream',
        'page-media-picture':window.APPPATH + 'front/media/picture/page-media-picture',
        'page-media-video':window.APPPATH + 'front/media/video/page-media-video',
        'page-media-video-stream':window.APPPATH + 'front/media/video-stream/page-media-video-stream',
        'page-media-compress':window.APPPATH + 'front/media/compress/page-media-compress',
        'page-media-settings':window.APPPATH + 'front/media/settings/page-media-settings',

        'page-check-article':window.APPPATH + 'front/check/article/page-check-article',
        'page-check-audio':window.APPPATH + 'front/check/audio/page-check-audio',
        'page-check-audio-stream':window.APPPATH + 'front/check/audio-stream/page-check-audio-stream',
        'page-check-picture':window.APPPATH + 'front/check/picture/page-check-picture',
        'page-check-video':window.APPPATH + 'front/check/video/page-check-video',
        'page-check-video-stream':window.APPPATH + 'front/check/video-stream/page-check-video-stream',

        'page-front-production-article':window.APPPATH + 'front/production/article/page-front-production-article',
        'page-front-production-audio':window.APPPATH + 'front/production/audio/page-front-production-audio',
        'page-front-production-audio-stream':window.APPPATH + 'front/production/audio-stream/page-front-production-audio-stream',
        'page-front-production-picture':window.APPPATH + 'front/production/picture/page-front-production-picture',
        'page-front-production-video':window.APPPATH + 'front/production/video/page-front-production-video',
        'page-front-production-video-stream':window.APPPATH + 'front/production/video-stream/page-front-production-video-stream',

        'page-front-organization':window.APPPATH + 'front/organization/page-front-organization',
        'page-front-role':window.APPPATH + 'front/role/page-front-role',

        /* backstage-pages */
        'page-backstage-production-article':window.APPPATH + 'backstage/production/article/page-backstage-production-article',
        'page-backstage-production-audio':window.APPPATH + 'backstage/production/audio/page-backstage-production-audio',
        'page-backstage-production-audio-stream':window.APPPATH + 'backstage/production/audio-stream/page-backstage-production-audio-stream',
        'page-backstage-production-picture':window.APPPATH + 'backstage/production/picture/page-backstage-production-picture',
        'page-backstage-production-video':window.APPPATH + 'backstage/production/video/page-backstage-production-video',
        'page-backstage-production-video-stream':window.APPPATH + 'backstage/production/video-stream/page-backstage-production-video-stream'

    },
    shim:{
        'vue':{
            exports: 'Vue'
        },
        'vue-router':{
            deps: ['vue'],
            exports: 'VueRouter'
        },
        'element':{
            deps:['vue']
        },
        'jquery':{
        	exports:'jQuery'
        },
        'json':{
        	deps:['jquery'],
        	exports:'jQuery'
        }
    }
});

require([
    'storage',
    'vue',
    'router',
    'context',
    'menu',
    'config',
    'restfull',
    'element-ui',
    'css!' + window.APPPATH + 'reset.css'
], function(storage, Vue, router, context, menuUtil, config, ajax){

    var app = null;

    //缓存token
    storage.setItem(config.ajax.header_auth_token, window.TOKEN);
    storage.setItem(config.ajax.header_session_id, window.SESSIONID);

    //初始化ajax
    ajax.init({
        login:config.ajax.login,
        authname:config.ajax.header_auth_token,
        sessionIdName:config.ajax.header_session_id,
        debug:config.ajax.debug,
        messenger:{
            info:function(message, status){
                var app = context.getProp('app');
            	if(!app) return;
            	app.$message({
                    message: message,
                    type: 'info'
                });
            },
            success:function(message, status){
                var app = context.getProp('app');
            	if(!app) return;
                app.$message({
                    message: message,
                    type: 'success'
                });
            },
            warning:function(message, status){
                var app = context.getProp('app');
            	if(!app) return;
                app.$message({
                    message: message,
                    type: 'warning'
                });
            },
            error:function(message, status){
                var app = context.getProp('app');
            	if(!app) return;
                app.$message({
                    message: message,
                    type: 'error'
                });
            }
        }
    });

    ajax.get('/prepare/app', null, function(appInfo){

        //初始化全局vue实例
        var app = new Vue({
            router:router,
            data:function(){
                return {
                    loading:false
                };
            }
        }).$mount('#app');

        //初始化上下文环境
        context.setProp('app', app)
               .setProp('router', router)
               .setProp('user', appInfo.user)
               .setProp('groups', appInfo.groups || [])
               .setProp('token', window.TOKEN);

        //处理皮肤
        if(appInfo.user.themeUrl) $('head').prepend('<link rel="stylesheet" type="text/css" href="'+window.BASEPATH + appInfo.user.themeUrl+'"/>');

        //解析模板
        menuUtil.parseUrlTemplate(appInfo.menus);

        //缓存菜单
        context.setProp('menus', appInfo.menus || []);

        //获取活动页
        var activeMenu = menuUtil.getActiveMenu(appInfo.menus);

        //重置首页
        if(activeMenu){
            config.redirect.home = activeMenu.link.split('#/')[1];
        }

        //跳转首页
        if(!window.location.hash || window.location.hash==='#/') router.push('/' + config.redirect.home);

    });

});