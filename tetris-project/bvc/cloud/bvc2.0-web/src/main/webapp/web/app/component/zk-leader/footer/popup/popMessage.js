define([
    'text!' + window.APPPATH + 'component/zk-leader/footer/popup/popMessage.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'css!' + window.APPPATH + 'component/zk-leader/footer/popup/popMessage.css'
], function (tpl, ajax, $, Vue, config) {

    //组件名称
    var pluginName = 'pop-message';

    Vue.component(pluginName, {
        template: tpl,
        data: function () {
            return {
                msgData: [] //消息数据
            }
        },
        methods: {
            //忽略
            ignore: function (e, index,item) {
                var self = this;
                ajax.post('/message/consume/all', {
                    ids: $.toJSON([item.id])
                }, function(){
                    $(e.target).parents(".btns").parents(".msg").addClass('animating');
                    setTimeout(function () {
                        for(var i=0; i<self.msgData.length; i++){
                            if(self.msgData[i].id == item.id){
                                self.msgData.splice(i, 1);
                                break;
                            }
                        }
                    }, 2000);
                });
            },
            //拒绝
            refuse:function(e, index,item){
                var self = this;
                var message = item.message;
                if(item.message.businessType === 'callUser'){
                    ajax.post('/command/call/user/refuse', {businessId:message.businessId}, null);
                }else if(item.message.businessType === 'voiceIntercom'){
                    ajax.post('/command/voice/intercom/refuse', {businessId:message.businessId}, null);
                }else if(item.message.businessType === 'cooperationGrant'){
                    ajax.post('/command/cooperation/refuse', {id: message.businessId}, null);
                }else if(item.message.businessType === 'commandStart'){
                    ajax.post('/command/basic/refuse', {id: message.businessId}, null);
                }else if(item.message.businessType === 'commandForwardDevice'){
                    var forwards = message.forwards;
                    var forwardIds = [];
                    if(forwards && forwards.length>0){
                        for(var i=0; i<forwards.length; i++){
                            forwardIds.push(forwards[i].id);
                        }
                    }
                    ajax.post('/command/basic/forward/device/refuse', {
                        id: e.businessId,
                        forwardIds: $.toJSON(forwardIds)
                    });
                }else if(item.message.businessType === 'commandForwardFile'){
                    var forwards = message.forwards;
                    var forwardIds = [];
                    if(forwards && forwards.length>0){
                        for(var i=0; i<forwards.length; i++){
                            forwardIds.push(forwards[i].id);
                        }
                    }
                    ajax.post('/command/basic/forward/file/refuse', {
                        id: e.businessId,
                        forwardIds: $.toJSON(forwardIds)
                    });
                }else if(item.message.businessType === 'secretStart'){
                    ajax.post('/command/secret/refuse', {id: message.businessId}, null);
                }
                $(e.target).parents(".btns").parents(".msg").addClass('animating');
                setTimeout(function () {
                    for(var i=0; i<self.msgData.length; i++){
                        if(self.msgData[i].id == item.id){
                            self.msgData.splice(i, 1);
                            break;
                        }
                    }
                }, 2000);
            },
            //同意
            accept:function(e, index,command){
                var self = this;
                var message = command.message;
                if(command.message.businessType === 'callUser'){
                    ajax.post('/command/call/user/accept', {businessId:message.businessId}, function(data){
                        self.qt.linkedWebview('historyMessage', {id:'callUsersProxy', params:$.toJSON([data])});
                    });
                }else if(command.message.businessType === 'voiceIntercom'){
                    ajax.post('/command/voice/intercom/accept', {businessId:message.businessId}, function(data){
                        self.qt.linkedWebview('historyMessage', {id:'voiceIntercomsProxy', params:$.toJSON([data])});
                    });
                }else if(command.message.businessType === 'cooperationGrant'){
                    ajax.post('/command/cooperation/agree', {id:message.businessId}, null);
                }else if(command.message.businessType === 'commandStart'){
                    self.qt.linkedWebview('historyMessage', {id:'enterCommand', params:message});
                }else if(command.message.businessType === 'commandForwardDevice'){
                    var forwards = message.forwards;
                    var forwardIds = [];
                    if(forwards && forwards.length>0){
                        for(var i=0; i<forwards.length; i++){
                            forwardIds.push(forwards[i].id);
                        }
                    }
                    ajax.post('/command/basic/forward/device/agree', {
                        id: e.businessId,
                        forwardIds: $.toJSON(forwardIds)
                    }, function(data){
                        if(data && data.length>0){
                            self.qt.linkedWebview('historyMessage', {id:'commandForwardDeviceProxy', params:$.toJSON(data)});
                        }
                    });
                }else if(command.message.businessType === 'commandForwardFile'){
                    var forwards = message.forwards;
                    var forwardIds = [];
                    if(forwards && forwards.length>0){
                        for(var i=0; i<forwards.length; i++){
                            forwardIds.push(forwards[i].id);
                        }
                    }
                    ajax.post('/command/basic/forward/file/agree', {
                        id: e.businessId,
                        forwardIds: $.toJSON(forwardIds)
                    }, function(data){
                        if(data && data.length>0){
                            self.qt.linkedWebview('historyMessage', {id:'commandForwardFileProxy', params:$.toJSON(data)});
                        }
                    });
                }else if(command.message.businessType === 'secretStart'){
                    ajax.post('/command/secret/accept', {id: message.businessId}, function(data){
                        self.qt.linkedWebview('historyMessage', {id:'secretStartProxy', params:$.toJSON([data])});
                    });
                }
                $(e.target).parents(".btns").parents(".msg").addClass('animating');
                setTimeout(function () {
                    for(var i=0; i<self.msgData.length; i++){
                        if(self.msgData[i].id == item.id){
                            self.msgData.splice(i, 1);
                            break;
                        }
                    }
                }, 2000);
            },
            //关闭弹框
            closePop:function () {
                this.qt.destroy();
            }
        },
        mounted: function () {
            var self = this;
            self.qt = new QtContext('popMessage', function () {

                // 初始化ajax
                ajax.init({
                    login: config.ajax.login,
                    redirectLogin: false,
                    authname: config.ajax.authname,
                    debug: config.ajax.debug,
                    messenger: {
                        info: function (message) {
                            self.qt.info(message);
                        },
                        success: function (message) {
                            self.qt.success(message);
                        },
                        warning: function (message) {
                            self.qt.warning(message);
                        },
                        error: function (message) {
                            self.qt.error(message);
                        }
                    }
                });

                self.msgData.splice(0, self.msgData.length);
                ajax.post('/message/find/unconsumed/commands', null, function (commands) {
                    if (commands && commands.length > 0) {
                        for (var i = 0; i < commands.length; i++) {
                            commands[i].message = $.parseJSON(commands[i].message);
                            self.msgData.push(commands[i]);
                        }
                    }
                });
            })
        }
    });

    return Vue;
});