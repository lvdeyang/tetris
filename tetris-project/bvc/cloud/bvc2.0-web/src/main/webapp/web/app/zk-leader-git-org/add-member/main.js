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
        'add-member':window.APPPATH + 'component/zk-leader/add-member-new/add-member-new'

    },
    shim:{
        'vue':{
            exports: 'Vue'
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
        },
        'native-record-player':{
            deps:['jquery'],
            exports:'jQuery'
        },
        'player':{
            deps:['jquery'],
            exports:'jQuery'
        },
        'player-panel':{
            deps:['player'],
            exports:'jQuery'
        }
    }
});

require([
    'storage',
    'vue',
    'config',
    'restfull',
    'element-ui',
    'add-member-new'
], function(storage, Vue, config, ajax){

    var app = null;

    //缓存token
    storage.setItem(config.ajax.authname, window.TOKEN);

    app = new Vue({
        data:function(){
            return {
                loading:false
            };
        }
    }).$mount('#app');

});