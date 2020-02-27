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
                msgData: [], //消息数据
                currentTab:0,
                historyInstantMsg:[],
                page:{
                    currentPage:0,
                    pageSize:30,
                    total:0
                },
                currentGroupId:''
            }
        },
        watch:{
            currentTab:function(){
                var self = this;
                if(self.currentTab){
                    self.load(1);
                }else{
                    self.historyInstantMsg.splice(0, self.historyInstantMsg.length);
                }
            }
        },
        methods: {
            load:function(currentPage){
                var self = this;
                self.historyInstantMsg.splice(0, self.historyInstantMsg.length);
                ajax.post('/command/message/query/history/instant/message', {
                    commandId:self.currentGroupId,
                    currentPage:currentPage,
                    pageSize:self.page.pageSize
                }, function(data){
                    var total = data.total;
                    var rows = data.rows;
                    if(rows && rows.length>0){
                        for(var i=0; i<rows.length; i++){
                            var message = $.parseJSON( rows[i].message).message;
                            self.historyInstantMsg.push(message);
                        }
                    }
                    self.page.total = total;
                    self.page.currentPage = currentPage;
                });
            },
            handleCurrentChange:function(currentPage){
                var self = this;
                self.load(currentPage);
            },
            //忽略
            ignore: function (e, index,item) {
                var self = this;
                ajax.post('/command/message/consume/all', {
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
                        setTimeout(function () {
                            for(var i=0; i<self.msgData.length; i++){
                                if(self.msgData[i].id == command.id){
                                    self.msgData.splice(i, 1);
                                    self.closePop();
                                    break;
                                }
                            }
                        }, 2000);
                    });
                }else if(command.message.businessType === 'voiceIntercom'){
                    ajax.post('/command/voice/intercom/accept', {businessId:message.businessId}, function(data){
                        self.qt.linkedWebview('historyMessage', {id:'voiceIntercomsProxy', params:$.toJSON([data])});
                        setTimeout(function () {
                            for(var i=0; i<self.msgData.length; i++){
                                if(self.msgData[i].id == command.id){
                                    self.msgData.splice(i, 1);
                                    self.closePop();
                                    break;
                                }
                            }
                        }, 2000);
                    });
                }else if(command.message.businessType === 'cooperationGrant'){
                    ajax.post('/command/cooperation/agree', {id:message.businessId}, function () {
                        setTimeout(function () {
                            for(var i=0; i<self.msgData.length; i++){
                                if(self.msgData[i].id == command.id){
                                    self.msgData.splice(i, 1);
                                    self.closePop();
                                    break;
                                }
                            }
                        }, 2000);
                    });
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
                            setTimeout(function () {
                                for(var i=0; i<self.msgData.length; i++){
                                    if(self.msgData[i].id == command.id){
                                        self.msgData.splice(i, 1);
                                        self.closePop();
                                        break;
                                    }
                                }
                            }, 2000);
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
                            setTimeout(function () {
                                for(var i=0; i<self.msgData.length; i++){
                                    if(self.msgData[i].id == command.id){
                                        self.msgData.splice(i, 1);
                                        self.closePop();
                                        break;
                                    }
                                }
                            }, 2000);
                        }
                    });
                }else if(command.message.businessType === 'secretStart'){
                    ajax.post('/command/secret/accept', {id: message.businessId}, function(data){
                        self.qt.linkedWebview('historyMessage', {id:'secretStartProxy', params:$.toJSON([data])});
                        setTimeout(function () {
                            for(var i=0; i<self.msgData.length; i++){
                                if(self.msgData[i].id == command.id){
                                    self.msgData.splice(i, 1);
                                    self.closePop();
                                    break;
                                }
                            }
                        }, 2000);
                    });
                }
                $(e.target).parents(".btns").parents(".msg").addClass('animating');
            },
            //关闭弹框
            closePop:function () {
                this.qt.destroy();
            }
        },
        mounted: function () {
            var self = this;
            self.qt = new QtContext('popMessage', function () {

                var params = self.qt.getWindowParams();
                self.currentGroupId = params.currentGroupId;

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
                ajax.post('/command/message/find/unconsumed/commands', null, function (commands) {
                    console.log(commands)
                    if (commands && commands.length > 0) {
                        for (var i = 0; i < commands.length; i++) {
                            commands[i].message = $.parseJSON(commands[i].message);
                            if(commands[i].message.businessType =='callUser' || commands[i].message.businessType === 'voiceIntercom'
                            || commands[i].message.businessType === 'cooperationGrant' || commands[i].message.businessType === 'commandStart'
                            || commands[i].message.businessType === 'commandForwardDevice' || commands[i].message.businessType === 'commandForwardFile'
                            || commands[i].message.businessType === 'commandMessageReceive' || commands[i].message.businessType === 'secretStart'){
                                self.msgData.push(commands[i]);
                            }
                        }
                    }
                });
            })
        }
    });

    return Vue;
});