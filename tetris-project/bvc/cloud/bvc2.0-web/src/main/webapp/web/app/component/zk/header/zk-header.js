define([
    'text!' + window.APPPATH + 'component/zk/header/zk-header.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'date',
    'css!' + window.APPPATH + 'component/zk/header/zk-header.css'
], function(tpl, ajax, $, Vue, config){

	//组件名称
    var pluginName = 'zk-header';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data:function(){
            return {
            	baseUrl:window.BASEPATH,
                messageNum:0,
                user:'',
                qt:'',
                timer:'',
                dateTime:''
            }
        },
        computed:{

        },
        watch:{

        },
        methods:{
            showSettings:function(){
                var self = this;
                self.qt.window('/router/zk/settings', null, {width:600, height:600, title:'业务管理'});
            },
            showMessagePanel:function(){
                var self = this;
                self.qt.invoke('openMessagePanel');
                self.qt.linkedWebview('message', {id:'messagePanelShow'});
            }
        },
        mounted:function(){
        	var self = this;

            var timeInterval = function(){
                self.dateTime = new Date().format('yyyy-MM-dd hh:mm:ss');
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

            ajax.post('/command/message/count/by/unconsumed/commands', null, function(messageNum){
                self.messageNum = messageNum;
            });
        }
    });

    return Vue;
});