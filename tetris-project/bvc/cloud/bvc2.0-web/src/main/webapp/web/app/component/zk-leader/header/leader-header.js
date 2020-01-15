define([
    'text!' + window.APPPATH + 'component/zk-leader/header/leader-header.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'date',
    'css!' + window.APPPATH + 'component/zk-leader/header/leader-header.css'
], function(tpl, ajax, $, Vue, config){

	//组件名称
    var pluginName = 'leader-header';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data:function(){
            return {
                user:'',
                timer:'',
                date:'',
                time:''
            }
        },
        methods:{
            //系统设置
            showSettings:function(){
                var self = this;
                self.qt.window('/router/zk/leader/settings', null, {width:980, height:600});
            }
        },
        mounted:function(){
        	var self = this;

            var timeInterval = function(){
                self.date = new Date().format('yyyy-MM-dd');
                self.time = new Date().format('HH : mm : ss');
            };
            timeInterval();
            self.timer = setInterval(timeInterval, 1000);

            self.qt = new QtContext('header', function(){

                //初始化ajax
                ajax.init({
                    login:config.ajax.login,
                    redirectLogin:false,
                    authname:config.ajax.authname,
                    debug:config.ajax.debug,
                    messenger:{
                        info:function(message, status){
                            self.qt.info(message);
                        },
                        success:function(message, status){
                            self.qt.success(message);
                        },
                        warning:function(message, status){
                            self.qt.warning(message);
                        },
                        error:function(message, status){
                            self.qt.error(message);
                        }
                    }
                });

                self.qt.get(['user'], function(variables){
                    self.user = variables.user?$.parseJSON(variables.user):{};
                    self.qt.on('signalRecvMsgFromHtml', function(msg){
                        alert('header'+msg);
                    });
                });
            });
        }
    });

    return Vue;
});