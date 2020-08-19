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

        /* components */
        'bvc2-header':window.APPPATH + 'component/bvc2-header/bvc2-header',
        'bvc2-nav-side':window.APPPATH + 'component/bvc2-nav-side/bvc2-nav-side',

        /** pages */
        'page-error': window.APPPATH + 'error/page-error',
        'bvc2-add-repeater': window.APPPATH + 'bvc2-signal-control/bvc2-add-repeater/bvc2-add-repeater',
        'bvc2-internet-access': window.APPPATH + 'bvc2-signal-control/bvc2-internet-access/bvc2-internet-access',
        'bvc2-repeater-mapping': window.APPPATH + 'bvc2-signal-control/bvc2-repeater-mapping/bvc2-repeater-mapping',
        'bvc2-terminal-repeater': window.APPPATH + 'bvc2-signal-control/bvc2-terminal-repeater/bvc2-terminal-repeater',
        'bvc2-repeater-task': window.APPPATH + 'bvc2-signal-control/bvc2-repeater-task/bvc2-repeater-task',
        'bvc2-task-control': window.APPPATH + 'bvc2-signal-control/bvc2-task-control/bvc2-task-control'

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
    'restfull',
    'element-ui'
], function(storage, Vue, router, context, config, ajax){

    var app = null;

    //缓存token
    storage.setItem(config.ajax.authname, window.TOKEN);

    //初始化ajax
    ajax.init({
        login:config.ajax.login,
        authname:config.ajax.authname,
        debug:config.ajax.debug,
        messenger:{
            info:function(message, status){
            	if(!app) return;
            	app.$message({
                    message: message,
                    type: 'info'
                });
            },
            success:function(message, status){
            	if(!app) return;
                app.$message({
                    message: message,
                    type: 'success'
                });
            },
            warning:function(message, status){
            	if(!app) return;
                app.$message({
                    message: message,
                    type: 'warning'
                });
            },
            error:function(message, status){
            	if(!app) return;
                app.$message({
                    message: message,
                    type: 'error'
                });
            }
        }
    });

    app = new Vue({
        router:router,
        data:function(){
            return {
                loading:false
            };
        }
    }).$mount('#app');

    //初始化上下文环境
    context.setProp('app', app)
        .setProp('router', router);

});