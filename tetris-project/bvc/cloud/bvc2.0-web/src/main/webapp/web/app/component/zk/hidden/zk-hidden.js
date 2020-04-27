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
                        //self.qt.warning('请求失败', message);
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
                    } catch (e) {
                    }
                    ajax.post(url, param, function (data, status, message) {
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
                
                //视频界面上的点播文件资源
                self.qt.on('playVodFile', function (e) {
                    self.qt.invoke('vodResourceFiles', e.params);
                });
                //视频界面上的点播设备资源
                self.qt.on('playVodDevice', function (e) {
                    self.qt.invoke('vodDevices', e.params);
                });
                //视频界面上的点播用户资源
                self.qt.on('playVodUsers', function (e) {
                    self.qt.invoke('vodUsers', e.params);
                });
                //视频界面上的呼叫
                self.qt.on('playCall', function (e) {
                    self.qt.invoke('callUsers', e.params);
                });
                //视频界面上的专项
                self.qt.on('playSecret', function (e) {
                    self.qt.invoke('secretStart', e.params);
                });
                //视频界面上的语音
                self.qt.on('playVoice', function (e) {
                    self.qt.invoke('voiceIntercoms', e.params);
                });

                //播放录像文件
                self.qt.on('playRecordFile', function (e) {
                    self.qt.invoke('vodRecordFileStart', e.params);
                });
		
                self.qt.get(['user'], function (variables) {
                    self.user = variables.user ? $.parseJSON(variables.user) : {};
                    self.gateIp = variables.user ? $.parseJSON(variables.user).gateIp : '';
                    self.gatePort = variables.user ? $.parseJSON(variables.user).gatePort : '';
                
                    //请求websocket连接地址
                    ajax.editPsdPost('http://' + self.gateIp + ':' + self.gatePort + '/tetris-user/api/zk/websocket/server/addr',
                            null,
                            function (addr, status) {
                        var onmessage = function(e){
                            var e = $.parseJSON(e.data);
                            console.log(e)
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

                            //开启指挥  需要成员同意
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
                            }else if(e.businessType === 'commandStartNow'){ //开启指挥，不需要成员同意
                                self.qt.linkedWebview('rightBar', {id:'autoCommandStart', params:e});
                            }else if(e.businessType === 'exitApply'){
                                self.qt.linkedWebview('rightBar', {id:'agreeExitApply', params:e}); //通知主席有人申请退出
                            }else if(e.businessType === 'exitApplyAgree'){ //通知申请人被同意
                                self.qt.linkedWebview('rightBar', {id:'applyExitAgree', params:e});

                            }else if(e.businessType === 'exitApplyDisagree'){ //通知申请人被拒绝
                                self.qt.linkedWebview('rightBar', {id:'applyExitDisagree', params:e});

                            }

                            //开始会议 需要成员同意
                            if(e.businessType === 'meetingStart'){
                                //监听呼叫消息，消息状态要在底部滚动
                                self.qt.linkedWebview('historyMessage', {id:'meetingStart', params:e});
                            }else if(e.businessType === 'meetingStartNow'){ //开始指挥 不需要成员同意
                                self.qt.linkedWebview('rightBar', {id:'autoMeetingStart', params:e});
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

                            //主席指定发言，通知发言人
                            if(e.businessType === 'speakAppoint'){
                                self.qt.info(e.businessInfo)
                            }else if(e.businessType === 'speakApply'){ //通知主席有人申请发言
                                //监听呼叫消息，消息状态要在底部滚动
                                self.qt.linkedWebview('rightBar', {id:'agreeSpeakApply', params:e});

                            }else if(e.businessType === 'speakApplyAgree'){ //通知申请人被同意
                                self.qt.linkedWebview('rightBar', {id:'speakApplyAgree', params:e});

                            }else if(e.businessType === 'speakApplyDisagree'){ //通知申请人被拒绝
                                self.qt.linkedWebview('rightBar', {id:'speakApplyDisagree', params:e});

                            }else if(e.businessType === 'speakStop'){ //通知全员停止发言/停止讨论
                                self.qt.linkedWebview('rightBar', {id:'speakStop', params:e});

                            }else if(e.businessType === 'speakStart'){ //通知观看发言/开始讨论
                                self.qt.linkedWebview('rightBar', {id:'speakStart', params:e});
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
                            }else if(e.businessType === 'commandForward'){ //转发不需要成员同意
                                self.qt.linkedWebview('historyMessage',{id:'commandForward',params:e});
                            }

                            //指挥提醒
                            if(e.businessType === 'commandRemind'){
                                self.qt.linkedWebview('business', {id:'commandRemind', params:e});
                                //监听呼叫消息，消息状态要在底部滚动
                                self.qt.linkedWebview('rightBar', {id:'commandRemind', params:e});
                            }else if(e.businessType === 'commandRemindStop'){
                                self.qt.linkedWebview('business', {id:'commandRemindStop', params:e});
                                //监听呼叫消息，消息状态要在底部滚动
                                self.qt.linkedWebview('rightBar', {id:'commandRemindStop', params:e});
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
                        //停止客户端的音/视频
                        if (e.businessType === 'stopVideoSend') {
                            self.qt.invoke('stopVideoSend', {});
                            self.qt.warning('主席已将您的视频关闭');
                        } else if (e.businessType === 'stopAudioSend') {
                            self.qt.invoke('stopAudioSend', {});
                            self.qt.warning('主席已将您的音频关闭');
                        }

                        //收到即时消息
                        if(e.businessType === 'receiveInstantMessage'){
                            self.qt.linkedWebview('historyMessage', {id: 'receiveInstantMessage', params: e});
                        }
                        };
                        var onopen = function(){
                            console.log('已成功连接websocket...');
                        };
                        var onerror = function(){
                            console.log('websocket异常断开，30秒后重连...');
                            setTimeout(createWebsocket, 5*1000);
                        };
                        var createWebsocket = function(){
                        	//把IP替换为虚拟IP
                        	var virtualAddr = addr.replace(/[0-9]*\.[0-9]*\.[0-9]*\.[0-9]*/,self.gateIp);
                            var webSocket = new WebSocket(virtualAddr + window.TOKEN);
                            webSocket.onopen = onopen;
                            webSocket.onmessage = onmessage;
                            webSocket.onerror = onerror;
                            webSocket.onclose = onerror;
                        };
                        createWebsocket();

                        //用户心跳--25秒一次
                        //self.heartbeat();
                        //var interval_heartBeat = setInterval(self.heartbeat, 25000);
                    }, null, [403]);
                });
            });
        }
    });

    return Vue;
});