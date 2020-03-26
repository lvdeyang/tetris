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
        'extral':window.LIBPATH + 'extral/extral',

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

        /* components */
        'rightBar': window.APPPATH + 'component/zk-leader/index/rightBar'
    },
    shim: {
        'vue': {
            exports: 'Vue'
        },
        'element-ui': {
            deps: ['vue']
        },
        'jquery': {
            exports: 'jQuery'
        },
        'json': {
            deps: ['jquery'],
            exports: 'jQuery'
        }
    }
});

require([
    'storage',
    'vue',
    'config',
    'restfull',
    'element-ui',
    'rightBar'
], function (storage, Vue, config) {
    var app = null;
    window.showTab = 1; // 1 显示 x  2  显示  y
//    缓存token
    storage.setItem(config.ajax.authname, window.TOKEN);

    app = new Vue({
        data: function () {
            return {
                loading: false
            }
        }
    }).$mount('#app');
});