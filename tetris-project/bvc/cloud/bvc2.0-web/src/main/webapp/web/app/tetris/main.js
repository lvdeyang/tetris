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

        /* commons */
        'context':window.COMMONSPATH + 'context/context',
        'page-wrapper':window.COMMONSPATH + 'page/page-wrapper',
        'router':window.COMMONSPATH + 'router/router',
        'date':window.COMMONSPATH + 'date/date.ext',
        'string':window.COMMONSPATH + 'string/string.ext',
        'storage':window.COMMONSPATH + 'storage/storage.ext',
        'restfull':window.COMMONSPATH + 'restfull/restfull.api',

        'config':window.APPPATH + 'config',
        'commons':window.APPPATH + 'commons',
        'menu':window.COMMONSPATH + 'menu/menu',

        /* components */
        'mi-frame':window.APPPATH + 'component/frame/frame',
        'mi-sub-title':window.APPPATH + 'component/sub-title/sub-title',

        /* page */
        'page-terminal':window.APPPATH + 'tetris/model/terminal/page-terminal',
        'page-terminal-bundle':window.APPPATH + 'tetris/model/terminal/page-terminal-bundle',
        'page-terminal-bundle-channel':window.APPPATH + 'tetris/model/terminal/page-terminal-bundle-channel',
        'page-terminal-channel':window.APPPATH + 'tetris/model/terminal/channel/page-terminal-channel',
        'page-screen-primary-key':window.APPPATH + 'tetris/model/terminal/screen/page-screen-primary-key',
        'page-terminal-screen':window.APPPATH + 'tetris/model/terminal/screen/page-terminal-screen',
        'page-terminal-layout':window.APPPATH + 'tetris/model/terminal/layout/page-terminal-layout'

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
    'config',
    'menu',
    'jquery',
    'restfull',
    'element-ui',
    'css!' + window.APPPATH + 'reset.css'
], function(storage, Vue, router, context, config, menuUtil, $, ajax){

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

        appInfo.menus = appInfo.menus || [];

        //初始化上下文环境
        context.setProp('app', app)
            .setProp('router', router)
            .setProp('user', appInfo.user)
            .setProp('groups', appInfo.groups || [])
            .setProp('token', window.TOKEN)
            .setProp('locale', appInfo.locale);

        //处理皮肤
        if(appInfo.user.themeUrl) $('head').prepend('<link rel="stylesheet" type="text/css" href="'+window.BASEPATH + appInfo.user.themeUrl+'"/>');

        //解析模板
        menuUtil.parseUrlTemplate(appInfo.menus);

        //缓存菜单
        context.setProp('menus', appInfo.menus);

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