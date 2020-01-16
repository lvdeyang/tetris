define([
    'text!' + window.APPPATH + 'component/zk/hidden/zk-hidden.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'date',
    'css!' + window.APPPATH + 'component/zk/hidden/zk-hidden.css'
], function(tpl, ajax, $, Vue, config){

	//组件名称
    var pluginName = 'zk-hidden';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data:function(){
            return {
            	baseUrl:window.BASEPATH

            }
        },
        computed:{

        },
        watch:{

        },
        methods:{
            heartbeat:function(){
                var self = this;
                ajax.post('/heart/beat', null, function(data, status, message){
                    if(status !== 200){
                        //self.qt.alert('请求失败', message);
                    }
                }, null, [404, 403, 408, 409, 500]);
            }
        },
        mounted:function(){
        	var self = this;

            self.qt = new QtContext('hidden', function(){

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

                //处理qt http请求
                self.qt.on('ajax', function(e){
                    var url = e.url,
                        param = e.param,
                        callbackId = e.callbackId,
                        webviewLinked = e.webviewLinked;
                    param = !param?null:typeof param==='object'?param:$.parseJSON(param);
                    try{
                        webviewLinked = $.parseJSON(webviewLinked);
                    }catch(e){}
                    ajax.post(url, param, function(data, status, message){
                        //处理qt回调
                        var response = {
                            status:status,
                            message:message
                        };
                        if(status === 200){
                            response.data = data;
                        }
                        self.qt.invoke(callbackId, $.toJSON(response));

                        //处理webview联动
                        if(status===200 && webviewLinked.length>0){
                            var webviewResponse = {
                                id:callbackId,
                                param:param,
                                result:data
                            };
                            for(var i=0; i<webviewLinked.length; i++){
                                self.qt.linkedWebview(webviewLinked[i], webviewResponse);
                            }
                        }
                    }, null, [403, 404, 408, 409, 500]);
                });

                //处理websocket,监听消息
                ajax.post('/websocket/server/addr', null, function(addr){
                    var onmessage = function(e){
                        var e = $.parseJSON(e.data);
                        //呼叫用户消息
                        if(e.businessType === 'callUser'){
                            self.qt.linkedWebview('business', {id:'callUserMessage', params:e});
                            //监听呼叫消息，消息状态要在底部滚动
                            self.qt.linkedWebview('historyMessage', {id:'callUserMessage', params:e});
                        }else if(e.businessType === 'callUserRefuse'){
                            self.qt.linkedWebview('business', {id:'callUserRefuseMessage', params:e});
                            //监听呼叫消息，消息状态要在底部滚动
                            self.qt.linkedWebview('historyMessage', {id:'callUserRefuseMessage', params:e});
                        }else if(e.businessType === 'callUserStop'){
                            self.qt.linkedWebview('business', {id:'callUserStopMessage', params:e});
                            //监听呼叫消息，消息状态要在底部滚动
                            self.qt.linkedWebview('historyMessage', {id:'callUserStopMessage', params:e});
                        }

                        //语音对讲
                        if(e.businessType === 'voiceIntercom'){
                            self.qt.linkedWebview('business', {id:'voiceIntercomMessage', params:e});
                            //监听呼叫消息，消息状态要在底部滚动
                            self.qt.linkedWebview('historyMessage', {id:'voiceIntercomMessage', params:e});
                        }else if(e.businessType === 'voiceIntercomRefuse'){
                            self.qt.linkedWebview('business', {id:'voiceIntercomRefuseMessage', params:e});
                            //监听呼叫消息，消息状态要在底部滚动
                            self.qt.linkedWebview('historyMessage', {id:'voiceIntercomRefuseMessage', params:e});
                        }else if(e.businessType === 'voiceIntercomStop'){
                            self.qt.linkedWebview('business', {id:'voiceIntercomStopMessage', params:e});
                            //监听呼叫消息，消息状态要在底部滚动
                            self.qt.linkedWebview('historyMessage', {id:'voiceIntercomStopMessage', params:e});
                        }

                        //协同指挥
                        if(e.businessType === 'cooperation'){
                            self.qt.linkedWebview('business', {id:'cooperationMessage', params:e});
                            //监听呼叫消息，消息状态要在底部滚动
                            self.qt.linkedWebview('historyMessage', {id:'cooperationMessage', params:e});
                        }else if(e.businessType === 'cooperationRefuse'){
                            self.qt.linkedWebview('business', {id:'cooperationRefuseMessage', params:e});
                            //监听呼叫消息，消息状态要在底部滚动
                            self.qt.linkedWebview('historyMessage', {id:'cooperationRefuseMessage', params:e});
                        }else if(e.businessType === 'cooperationStop'){
                            self.qt.linkedWebview('business', {id:'cooperationStopMessage', params:e});
                            //监听呼叫消息，消息状态要在底部滚动
                            self.qt.linkedWebview('historyMessage', {id:'cooperationStopMessage', params:e});
                        }

                        //指挥
                        if(e.businessType === 'commandStart'){
                            self.qt.linkedWebview('business', {id:'commandStart', params:e});
                            //监听呼叫消息，消息状态要在底部滚动
                            self.qt.linkedWebview('historyMessage', {id:'commandStart', params:e});
                        }else if(e.businessType === 'commandStop'){
                            self.qt.linkedWebview('business', {id:'commandStop', params:e});
                            //监听呼叫消息，消息状态要在底部滚动
                            self.qt.linkedWebview('historyMessage', {id:'commandStop', params:e});
                        }else if(e.businessType === 'commandMemberOnline'){
                            self.qt.linkedWebview('business', {id:'commandMemberOnline', params:e});
                            //监听呼叫消息，消息状态要在底部滚动
                            self.qt.linkedWebview('historyMessage', {id:'commandMemberOnline', params:e});
                        }else if(e.businessType === 'commandMemberOffline'){
                            self.qt.linkedWebview('business', {id:'commandMemberOffline', params:e});
                            //监听呼叫消息，消息状态要在底部滚动
                            self.qt.linkedWebview('historyMessage', {id:'commandMemberOffline', params:e});
                        }else if(e.businessType === 'commandPause'){
                            self.qt.linkedWebview('business', {id:'commandPause', params:e});
                            //监听呼叫消息，消息状态要在底部滚动
                            self.qt.linkedWebview('historyMessage', {id:'commandPause', params:e});
                        }else if(e.businessType === 'commandPauseRecover'){
                            self.qt.linkedWebview('business', {id:'commandPauseRecover', params:e});
                            //监听呼叫消息，消息状态要在底部滚动
                            self.qt.linkedWebview('historyMessage', {id:'commandPauseRecover', params:e});
                        }else if(e.businessType === 'commandMemberDelete'){
                            self.qt.linkedWebview('business', {id:'commandMemberDelete', params:e});
                            //监听呼叫消息，消息状态要在底部滚动
                            self.qt.linkedWebview('historyMessage', {id:'commandMemberDelete', params:e});
                        }

                        //协同指挥
                        if(e.businessType === 'cooperationGrant'){
                            self.qt.linkedWebview('business', {id:'cooperationGrant', params:e});
                            //监听呼叫消息，消息状态要在底部滚动
                            self.qt.linkedWebview('historyMessage', {id:'cooperationGrant', params:e});
                        }else if(e.businessType === 'cooperationAgree'){
                            self.qt.linkedWebview('business', {id:'cooperationAgree', params:e});
                            //监听呼叫消息，消息状态要在底部滚动
                            self.qt.linkedWebview('historyMessage', {id:'cooperationAgree', params:e});
                        }else if(e.businessType === 'cooperationRefuse'){
                            self.qt.linkedWebview('business', {id:'cooperationRefuse', params:e});
                            //监听呼叫消息，消息状态要在底部滚动
                            self.qt.linkedWebview('historyMessage', {id:'cooperationRefuse', params:e});
                        }else if(e.businessType === 'cooperationRevoke'){
                            self.qt.linkedWebview('business', {id:'cooperationRevoke', params:e});
                            //监听呼叫消息，消息状态要在底部滚动
                            self.qt.linkedWebview('historyMessage', {id:'cooperationRevoke', params:e});
                        }

                        //指挥转发
                        if(e.businessType === 'commandForwardDevice'){
                            self.qt.linkedWebview('business', {id:'commandForwardDevice', params:e});
                            //监听呼叫消息，消息状态要在底部滚动
                            self.qt.linkedWebview('historyMessage', {id:'commandForwardDevice', params:e});
                        }else if(e.businessType === 'commandForwardFile'){
                            self.qt.linkedWebview('business', {id:'commandForwardFile', params:e});
                            //监听呼叫消息，消息状态要在底部滚动
                            self.qt.linkedWebview('historyMessage', {id:'commandForwardFile', params:e});
                        }else if(e.businessType === 'commandForwardStop'){
                            self.qt.linkedWebview('business', {id:'commandForwardStop', params:e});
                            //监听呼叫消息，消息状态要在底部滚动
                            self.qt.linkedWebview('historyMessage', {id:'commandForwardStop', params:e});
                        }

                        //指挥提醒
                        if(e.businessType === 'commandRemind'){
                            self.qt.linkedWebview('business', {id:'commandRemind', params:e});
                            //监听呼叫消息，消息状态要在底部滚动
                            self.qt.linkedWebview('historyMessage', {id:'commandRemind', params:e});
                        }else if(e.businessType === 'commandRemindStop'){
                            self.qt.linkedWebview('business', {id:'commandRemindStop', params:e});
                            //监听呼叫消息，消息状态要在底部滚动
                            self.qt.linkedWebview('historyMessage', {id:'commandRemindStop', params:e});
                        }

                        //指挥消息
                        if(e.businessType === 'commandMessageReceive'){
                            self.qt.linkedWebview('business', {id:'commandMessageReceive', params:e});
                            //监听呼叫消息，消息状态要在底部滚动
                            self.qt.linkedWebview('historyMessage', {id:'commandMessageReceive', params:e});
                        }else if(e.businessType === 'commandMessageStop'){
                            self.qt.linkedWebview('business', {id:'commandMessageStop', params:e});
                            //监听呼叫消息，消息状态要在底部滚动
                            self.qt.linkedWebview('historyMessage', {id:'commandMessageStop', params:e});
                        }

                        //专向指挥
                        if(e.businessType === 'secretStart'){
                            self.qt.linkedWebview('business', {id:'secretStart', params:e});
                            //监听呼叫消息，消息状态要在底部滚动
                            self.qt.linkedWebview('historyMessage', {id:'secretStart', params:e});
                        }else if(e.businessType === 'secretRefuse'){
                            self.qt.linkedWebview('business', {id:'secretRefuse', params:e});
                            //监听呼叫消息，消息状态要在底部滚动
                            self.qt.linkedWebview('historyMessage', {id:'secretRefuse', params:e});
                        }else if(e.businessType === 'secretStop'){
                            self.qt.linkedWebview('business', {id:'secretStop', params:e});
                            //监听呼叫消息，消息状态要在底部滚动
                            self.qt.linkedWebview('historyMessage', {id:'secretStop', params:e});
                        }
                    };
                    var onopen = function(){
                        console.log('已成功连接websocket...');
                    };
                    var onerror = function(){
                        console.log('websocket异常断开，30秒后重连...');
                        setTimeout(createWebsocket, 30*1000);
                    };
                    var createWebsocket = function(){
                        var webSocket = new WebSocket(addr + window.TOKEN);
                        webSocket.onopen = onopen;
                        webSocket.onmessage = onmessage;
                        webSocket.onerror = onerror;
                        webSocket.onclose = onerror;
                    };
                    createWebsocket();

                    //用户心跳--25秒一次
                    self.heartbeat();
                    var interval_heartBeat = setInterval(self.heartbeat, 25000);
                });
            });
        }
    });

    return Vue;
});