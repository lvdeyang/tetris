define([
    'text!' + window.APPPATH + 'component/zk-leader/settings/leader-settings.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'css!' + window.APPPATH + 'component/zk-leader/settings/leader-settings.css'
], function(tpl, ajax, $, Vue, config){

	//组件名称
    var pluginName = 'zk-settings';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data:function(){
            return {
            	baseUrl:window.BASEPATH,
                callResponseMode:'0',
                vodMode:'0',
                videoSendAnswerMode:'0',
                commandMeetingAutoStart:'0',
                subtitle:'0',
                visibleRange:'0',
                recordMode:'0',
                dedicatedMode:'0'
            }
        },
        methods:{
            handleCallResponseModeChange:function(v){
                var self = this;
                ajax.post('/command/settings/call/response/mode', {mode:parseInt(v)?'auto':'manual'}, function(data, status){
                    if(status !== 200){
                        self.callResponseMode = self.callResponseMode?'0':'1';
                    }else{
                        self.qt.linkedWebview('rightBar', {id:'responseModeChange', params:parseInt(v)?'auto':'manual'});
                    }
                }, null, [403,404,408,409,500]);
            },
            handleVodModeChange:function(v){
                var self = this;
                ajax.post('/command/settings/vod/mode', {mode:parseInt(v)?'multicast':'unicast'}, function(data, status){
                    if(status !== 200){
                        self.vodMode = self.vodMode?'0':'1';
                    }
                }, null, [403,404,408,409,500]);
            },
            handleVideoSendAnswerModeChange:function(v){
                var self = this;
                ajax.post('/command/settings/video/send/answer/mode', {mode:parseInt(v)?'auto':'manual'}, function(data, status){
                    if(status !== 200){
                        self.videoSendAnswerMode = self.videoSendAnswerMode?'0':'1';
                    }else{
                        self.qt.linkedWebview('rightBar', {id:'sendAnswerModeChange', params:parseInt(v)?'auto':'manual'});
                    }
                }, null, [403,404,408,409,500]);
            },
            handleCommandMeetingAutoStartChange:function(v){
                var self = this;
                ajax.post('/command/settings/command/meeting/auto/start', {mode:parseInt(v)?'auto':'manual'}, function(data, status){
                    if(status !== 200){
                        self.commandMeetingAutoStart = self.commandMeetingAutoStart?'0':'1';
                    }
                }, null, [403,404,408,409,500]);
            },
            handleSubtitleChange:function(v){
                var self = this;
                ajax.post('/command/settings/subtitle', {mode:parseInt(v)?'close':'open'}, function(data, status){
                    if(status !== 200){
                        self.subtitle = self.subtitle?'0':'1';
                    }
                }, null, [403,404,408,409,500]);
            },
            handleVisibleRangeChange:function(v){
                var self = this;
                ajax.post('/command/settings/visible/range', {mode:parseInt(v)?'all':'group'}, function(data, status){
                    if(status !== 200){
                        self.visibleRange = self.visibleRange?'0':'1';
                    }
                }, null, [403,404,408,409,500]);
            },
            handleRecordModeChange:function(v){
                var self = this;
                ajax.post('/command/settings/record/mode', {mode:parseInt(v)?'auto':'manual'}, function(data, status){
                    if(status !== 200){
                        self.recordMode = self.recordMode?'0':'1';
                    }
                }, null, [403,404,408,409,500]);
            },
            handleDedicatedModeChange:function(v){
                var self = this;
                ajax.post('/command/settings/dedicated/mode', {mode:parseInt(v)?'noAudio':'noAudioAndVideo'}, function(data, status){
                    if(status !== 200){
                        self.dedicatedMode = self.dedicatedMode?'0':'1';
                    }
                }, null, [403,404,408,409,500]);
            },
            closeMask:function () {
                this.qt.destroy();
            }
        },
        mounted:function(){
            var self = this;
            self.qt = new QtContext('leaderSetting', function(){
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
                ajax.post('/command/settings/query/all', null, function(data){
                    self.callResponseMode = data.responseMode==='auto'?'1':'0';
                    self.videoSendAnswerMode = data.sendAnswerMode==='auto'?'1':'0';
                });
            });
        }
    });

    return Vue;
});