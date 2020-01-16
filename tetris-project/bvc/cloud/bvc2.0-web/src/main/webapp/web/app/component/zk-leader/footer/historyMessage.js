define([
    'text!' + window.APPPATH + 'component/zk-leader/footer/historyMessage.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'css!' + window.APPPATH + 'component/zk-leader/footer/historyMessage.css'
], function (tpl, ajax, $, Vue, config) {

    //组件名称
    var pluginName = 'history-message';

    Vue.component(pluginName, {
        template: tpl,
        data: function () {
            return {
                baseUrl: window.BASEPATH,
                playerLayout:16,
                activeIndex:null, //当前哪个分屏按钮高亮
                callBoard: [],
                settings:{
                    //auto(自动)/manual(手动)
                    responseMode:'',
                    //auto(自动)/manual(手动)
                    sendAnswerMode:''
                },
                buttons:{
                    one:false,
                    two:false,
                    four:false,
                    six:false,
                    nine:false,
                    sixteen:false
                }
            }
        },
        methods: {
            //折叠按钮的点击事件
            showOrHide: function () {
                var self = this;
                self.qt.invoke('hideService');
            },
            //历史消息弹框
            showMessagePanel:function(){
                var self = this;
                self.qt.window('/router/zk/leader/footer/pop',null,{width:'100%',height:'100%'});
            },
            //分屏事件
            layoutChange:function(index){
                var self = this;
                $($('.screenCtrl span')[index]).css('color','#00e9ff').siblings().css('color','#fff');
                ajax.post('/command/split/change', {split:index}, function(data){
                    if(index === 0){
                        self.playerLayout = 1;
                        self.buttons.one=true;
                        self.buttons.two=false;
                        self.buttons.four=false;
                        self.buttons.six=false;
                        self.buttons.nine=false;
                        self.buttons.sixteen=false;
                    }else if(index === 1){
                        self.playerLayout = 4;
                        self.buttons.one=false;
                        self.buttons.two=false;
                        self.buttons.four=true;
                        self.buttons.six=false;
                        self.buttons.nine=false;
                        self.buttons.sixteen=false;
                    }else if(index === 2){
                        self.playerLayout = 6;
                        self.buttons.one=false;
                        self.buttons.two=false;
                        self.buttons.four=false;
                        self.buttons.six=true;
                        self.buttons.nine=false;
                        self.buttons.sixteen=false;
                    }else if(index === 3){
                        self.playerLayout = 9;
                        self.buttons.one=false;
                        self.buttons.two=false;
                        self.buttons.four=false;
                        self.buttons.six=false;
                        self.buttons.nine=true;
                        self.buttons.sixteen=false;
                    }else if(index === 4){
                        self.playerLayout = 16;
                        self.buttons.one=false;
                        self.buttons.two=false;
                        self.buttons.four=false;
                        self.buttons.six=false;
                        self.buttons.nine=false;
                        self.buttons.sixteen=true;
                    }
                    self.qt.invoke('changeSplit', index, data.settings);
                });
            }
        },
        mounted: function () {
            var self = this;
            self.qt = new QtContext('historyMessage', function () {

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

                // 获取屏幕个数
                ajax.post('/command/user/info/get/current', null, function(data){
                    if(data.playerSplitLayout == 0){
                        self.playerLayout = 1;
                        self.activeIndex=0;
                        self.buttons.one=true;
                    }else if(data.playerSplitLayout == 1){
                        self.playerLayout = 4;
                        self.activeIndex=1;
                        self.buttons.four=true;
                    }else if(data.playerSplitLayout == 2){
                        self.playerLayout = 6;
                        self.activeIndex=2;
                        self.buttons.six=true;
                    }else if(data.playerSplitLayout == 3){
                        self.playerLayout = 9;
                        self.activeIndex=3;
                        self.buttons.nine=true;
                    }else if(data.playerSplitLayout == 4){
                        self.playerLayout = 16;
                        self.activeIndex=4;
                        self.buttons.sixteen=true;
                    }
                    if(data.players && data.players.length>0){
                        self.qt.invoke('changeSplit', data.playerSplitLayout, data.players);
                        $($('.screenCtrl span')[self.activeIndex]).css('color','#00e9ff').siblings().css('color','#fff');
                    }
                });

                //查询设置
                ajax.post('/command/settings/query/all', null, function(data){
                    self.settings.responseMode = data.responseMode;
                    self.settings.sendAnswerMode = data.sendAnswerMode;
                });
                //--------视频界面上的按钮操作弹框 start-------
                //关联设备
                self.qt.on('castDevices', function(e){
                    self.qt.window('/router/zk/leader/cast/devices', {serial: e.serial}, {width:1024, height:600, title:'第'+ e.serial+'屏关联解码器'});
                });

                //云台控制
                self.qt.on('cloudControl', function(e){
                    self.qt.window('/router/zk/leader/cloud/control', {serial: e.serial}, {width:900, height:600, title:'第'+ e.serial+'屏关联解码器'});
                });

                //呼叫
                self.qt.on('dialStart', function(e){
                    self.qt.window('/router/zk/leader/callUser', {serial: e.serial,type:'call'}, {width:900, height:600, title:'第'+ e.serial+'屏关联解码器'});
                });
                //点播
                self.qt.on('orderStart', function(e){
                    self.qt.window('/router/zk/leader/order', {serial: e.serial}, {width:1000, height:600, title:'第'+ e.serial+'屏关联解码器'});
                });
                //专项指挥
                self.qt.on('vipWindow', function(e){
                    self.qt.window('/router/zk/leader/callUser', {serial: e.serial,type:'orient'}, {width:900, height:600, title:'第'+ e.serial+'屏关联解码器'});
                });
                //语音通话
                self.qt.on('voiceSpeak', function(e){
                    self.qt.window('/router/zk/leader/callUser', {serial: e.serial,type:'voicce'}, {width:900, height:600, title:'第'+ e.serial+'屏关联解码器'});
                });
                //--------视频界面上的按钮操作弹框 end-------


                //websocket 用户呼叫
                self.qt.on('callUserMessage', function(e){
                    var callUserInfo = e.params;
                    var businessInfo = callUserInfo.businessInfo;
                    var businessId = callUserInfo.businessId;
                    if(self.settings.responseMode === 'auto'){
                        setTimeout(function(){
                            ajax.post('/command/call/user/accept', {businessId:businessId}, function(data){
                                self.qt.invoke('callUsers', $.toJSON([data]));
                            });
                        }, 200);
                    }else{
                        self.qt.confirm('业务提示', businessInfo, '拒绝', '同意', function(){
                            ajax.post('/command/call/user/refuse', {businessId:businessId}, null);
                        }, function(){
                            ajax.post('/command/call/user/accept', {businessId:businessId}, function(data){
                                self.qt.invoke('callUsers', $.toJSON([data]));
                            });
                        });
                    }
                });
                //websocket 拒绝呼叫
                self.qt.on('callUserRefuseMessage', function(e){
                    var callUserInfo = e.params;
                    var businessInfo = callUserInfo.businessInfo;
                    var serial = callUserInfo.serial;
                    // self.qt.alert('业务提示', businessInfo);
                    self.callBoard.push(businessInfo);
                    self.qt.invoke('callUserStop', $.toJSON({serial:serial}));
                });
                //websocket 停止呼叫
                self.qt.on('callUserStopMessage', function(e){
                    var callUserInfo = e.params;
                    var businessInfo = callUserInfo.businessInfo;
                    var serial = callUserInfo.serial;
                    self.qt.alert('业务提示', businessInfo);
                    self.callBoard.push(businessInfo);
                    self.qt.invoke('callUserStop', $.toJSON({serial:serial}));
                });

                //websocket 语音对讲
                self.qt.on('voiceIntercomMessage', function(e){
                    var callUserInfo = e.params;
                    var businessInfo = callUserInfo.businessInfo;
                    var businessId = callUserInfo.businessId;
                    if(self.settings.responseMode === 'auto'){
                        setTimeout(function(){
                            ajax.post('/command/voice/intercom/accept', {businessId:businessId}, function(data){
                                self.qt.invoke('voiceIntercoms', $.toJSON([data]));
                            });
                        }, 200);
                    }else{
                        self.qt.confirm('业务提示', businessInfo, '拒绝', '同意', function(){
                            ajax.post('/command/voice/intercom/refuse', {businessId:businessId}, null);
                        }, function(){
                            ajax.post('/command/voice/intercom/accept', {businessId:businessId}, function(data){
                                self.qt.invoke('voiceIntercoms', $.toJSON([data]));
                            });
                        });
                    }
                });
                //websocket 拒绝语音对讲
                self.qt.on('voiceIntercomRefuseMessage', function(e){
                    var callUserInfo = e.params;
                    var businessInfo = callUserInfo.businessInfo;
                    var serial = callUserInfo.serial;
                    // self.qt.alert('业务提示', businessInfo);
                    self.callBoard.push(businessInfo);
                    self.qt.invoke('voiceIntercomStop', $.toJSON({serial:serial}));
                });
                //websocket 停止语音对讲
                self.qt.on('voiceIntercomStopMessage', function(e){
                    var callUserInfo = e.params;
                    var businessInfo = callUserInfo.businessInfo;
                    var serial = callUserInfo.serial;
                    // self.qt.alert('业务提示', businessInfo);
                    self.callBoard.push(businessInfo);
                    self.qt.invoke('voiceIntercomStop', $.toJSON({serial:serial}));
                });

                //授权协同指挥
                self.qt.on('cooperationGrant', function(e){
                    var e = e.params;
                    if(self.settings.responseMode === 'auto'){
                        setTimeout(function(){
                            ajax.post('/command/cooperation/agree', {id:e.businessId}, null);
                        }, 200);
                    }else{
                        self.qt.confirm('业务提示', e.businessInfo, '拒绝', '同意', function(){
                            ajax.post('/command/cooperation/refuse', {id: e.businessId}, null);
                        }, function(){
                            ajax.post('/command/cooperation/agree', {id:e.businessId}, null);
                        });
                    }
                });
                //同意授权指挥
                self.qt.on('cooperationAgree', function(e){
                    var e = e.params;
                    // self.qt.alert('业务提示', e.businessInfo);
                    self.callBoard.push(businessInfo);
                    if(e.splits && e.splits.length>0){
                        self.qt.invoke('cooperationGrant', e.splits);
                    }
                });
                //拒绝协同指挥
                self.qt.on('cooperationRefuse', function(e){
                    var e = e.params;
                    // self.qt.alert('业务提示', e.businessInfo);
                    self.callBoard.push(e.businessInfo);
                });
                //撤销协同指挥授权
                self.qt.on('cooperationRevoke', function(e){
                    var e = e.params;
                    // self.qt.alert('业务提示', e.businessInfo);
                    self.callBoard.push(e.businessInfo);
                    if(e.splits && e.splits.length>0){
                        self.qt.invoke('cooperationRevoke', e.splits);
                    }
                });

                //websocket 开始指挥
                self.qt.on('commandStart', function(e){
                    e = e.params;
                    self.qt.confirm('业务提示', e.businessInfo, '拒绝', '同意', function(){
                        //拒绝进入指挥
                        ajax.post('/command/basic/refuse', {id: e.businessId}, function(){});
                    }, function(){
                        //同意进入指挥
                        self.enterCommand([e.businessId]);
                    });
                });
                self.qt.on('enterCommand', function(e){
                    e = e.params;
                    self.enterCommand([e.businessId]);
                });
                //websocket 停止指挥
                self.qt.on('commandStop', function(e){
                    e = e.params;
                    for(var i=0; i<self.group.entered.length; i++){
                        if(self.group.entered[i].id == e.businessId){
                            self.group.entered.splice(i, 1);
                            break;
                        }
                    }
                    if(self.group.currentId == e.businessId){
                        if(self.group.entered.length > 0){
                            self.group.current = self.group.entered[0];
                            self.group.currentId = self.group.entered[0].id;
                            self.currentGroupChange(self.group.currentId);
                        }else{
                            self.group.current = '';
                            self.group.currentId = '';
                        }
                    }
                    // self.qt.alert('业务提示', e.businessInfo);
                    self.callBoard.push(e.businessInfo);
                    self.qt.invoke('commandExit', $.toJSON(e.splits));
                });

                self.qt.on('commandMemberOnline', function(e){
                    e = e.params;
                    // self.qt.alert('业务提示', e.businessInfo);
                    self.callBoard.push(e.businessInfo);
                    if(e.splits && e.splits.length>0){
                        self.qt.invoke('groupMembers', $.toJSON(e.splits));
                    }
                });

                self.qt.on('commandMemberOffline', function(e){
                    e = e.params;
                    // self.qt.alert('业务提示', e.businessInfo);
                    self.callBoard.push(e.businessInfo);
                    if(e.splits && e.splits.length>0){
                        self.qt.invoke('commandExit', e.splits);
                    }
                });

                self.qt.on('commandPause', function(e){
                    e = e.params;
                    // self.qt.alert('业务提示', e.businessInfo);
                    self.callBoard.push(e.businessInfo);
                });

                self.qt.on('commandPauseRecover', function(e){
                    e = e.params;
                    self.qt.invoke('commandPauseRecover', e.splits);
                });

                self.qt.on('commandMemberDelete', function(e){
                    e = e.params;
                    // self.qt.alert('业务提示', e.businessInfo);
                    self.callBoard.push(e.businessInfo);
                    var memberIds = e.memberIds;
                    var beDeleted = false;
                    if(memberIds && memberIds.length>0){
                        for(var i=0; i<memberIds.length; i++){
                            if(memberIds[i] == self.user.id){
                                beDeleted = true;
                                break;
                            }
                        }
                    }
                    if(beDeleted){
                        for(var i=0; i<self.group.entered.length; i++){
                            if(self.group.entered[i].id == e.businessId){
                                self.group.entered.splice(i, 1);
                                break;
                            }
                        }
                        if(self.group.currentId == e.businessId){
                            if(self.group.entered.length > 0){
                                self.group.currentId = self.group.entered[0].id;
                                self.group.current = self.group.entered[0];
                                self.currentGroupChange(self.group.currentId);
                            }else{
                                self.group.currentId = '';
                                self.group.current = '';
                            }
                        }
                    }
                    if(e.splits && e.splits.length>0){
                        self.qt.invoke('commandMemberDelete', e.splits);
                    }
                });

                self.qt.on('commandForwardDevice', function(e){
                    e = e.params;
                    var forwards = e.forwards;
                    var forwardIds = [];
                    if(forwards && forwards.length>0){
                        for(var i=0; i<forwards.length; i++){
                            forwardIds.push(forwards[i].id);
                        }
                    }
                    if(self.settings.sendAnswerMode === 'auto'){
                        setTimeout(function(){
                            ajax.post('/command/basic/forward/device/agree', {
                                id: e.businessId,
                                forwardIds: $.toJSON(forwardIds)
                            }, function(data){
                                if(data && data.length>0){
                                    self.qt.invoke('commandForwardDevice', $.toJSON(data));
                                }
                            });
                        }, 200);
                    }else{
                        self.qt.confirm('业务提示', e.businessInfo, '拒绝', '同意', function(){
                            ajax.post('/command/basic/forward/device/refuse', {
                                id: e.businessId,
                                forwardIds: $.toJSON(forwardIds)
                            });
                        }, function(){
                            ajax.post('/command/basic/forward/device/agree', {
                                id: e.businessId,
                                forwardIds: $.toJSON(forwardIds)
                            }, function(data){
                                if(data && data.length>0){
                                    self.qt.invoke('commandForwardDevice', $.toJSON(data));
                                }
                            });
                        });
                    }
                });

                self.qt.on('commandForwardFile', function(e){
                    e = e.params;
                    var forwards = e.forwards;
                    var forwardIds = [];
                    if(forwards && forwards.length>0){
                        for(var i=0; i<forwards.length; i++){
                            forwardIds.push(forwards[i].id);
                        }
                    }
                    if(self.settings.sendAnswerMode === 'auto'){
                        setTimeout(function(){
                            ajax.post('/command/basic/forward/file/agree', {
                                id: e.businessId,
                                forwardIds: $.toJSON(forwardIds)
                            }, function(data){
                                if(data && data.length>0){
                                    self.qt.invoke('commandForwardFile', $.toJSON(data));
                                }
                            });
                        }, 200);
                    }else{
                        self.qt.confirm('业务提示', e.businessInfo, '拒绝', '同意', function(){
                            ajax.post('/command/basic/forward/file/refuse', {
                                id: e.businessId,
                                forwardIds: $.toJSON(forwardIds)
                            });
                        }, function(){
                            ajax.post('/command/basic/forward/file/agree', {
                                id: e.businessId,
                                forwardIds: $.toJSON(forwardIds)
                            }, function(data){
                                if(data && data.length>0){
                                    self.qt.invoke('commandForwardFile', $.toJSON(data));
                                }
                            });
                        });
                    }
                });

                self.qt.on('commandForwardStop', function(e){
                    e = e.params;
                    // self.qt.alert('业务提示', e.businessInfo);
                    self.callBoard.push(e.businessInfo);
                    if(e.splits && e.splits.length>0){
                        self.qt.invoke('commandForwardStop', $.toJSON(e.splits));
                    }
                });

                self.qt.on('commandRemind', function(e){
                    e = e.params;
                    var finded = false;
                    for(var i=0; i<self.group.remindIds.length; i++){
                        if(self.group.remindIds[i] == e.businessId){
                            finded = true;
                            break;
                        }
                    }
                    if(!finded){
                        self.group.remindIds.push(e.businessId);
                    }
                    if(self.group.current.id == e.businessId){
                        self.group.current.status = 'remind';
                    }
                    self.qt.invoke('commandRemind', $.toJSON(e));
                });

                self.qt.on('commandRemindStop', function(e){
                    e = e.params;
                    for(var i=0; i<self.group.remindIds.length; i++){
                        if(self.group.remindIds[i] == e.businessId){
                            self.group.remindIds.splice(i, 1);
                            break;
                        }
                    }
                    if(self.group.current.id == e.businessId){
                        self.group.current.status = e.status;
                    }
                    self.qt.invoke('commandRemindStop', $.toJSON(e));
                });

                self.qt.on('commandMessageReceive', function(e){
                    e = e.params;
                    self.qt.invoke('commandMessageReceive', $.toJSON(e));
                });

                self.qt.on('commandMessageStop', function(e){
                    e = e.params;
                    // self.qt.alert('业务提示', e.businessInfo);
                    self.callBoard.push(e.businessInfo);
                    self.qt.invoke('commandMessageStop', $.toJSON({businessId:e.businessId}));
                });

                self.qt.on('secretStart', function(e){
                    e = e.params;
                    if(self.settings.responseMode === 'auto'){
                        setTimeout(function(){
                            ajax.post('/command/secret/accept', {id: e.businessId}, function(data){
                                self.qt.invoke('secretStart', $.toJSON([data]));
                            });
                        }, 200);
                    }else{
                        self.qt.confirm("业务提示", e.businessInfo, '拒绝', '同意', function(){
                            ajax.post('/command/secret/refuse', {id: e.businessId}, function(){});
                        }, function(){
                            ajax.post('/command/secret/accept', {id: e.businessId}, function(data){
                                self.qt.invoke('secretStart', $.toJSON([data]));
                            });
                        });
                    }
                });

                self.qt.on('secretRefuse', function(e){
                    e = e.params;
                    // self.qt.alert('业务提示', e.businessInfo);
                    self.callBoard.push(e.businessInfo);
                    self.qt.invoke('secretStop', $.toJSON([{serial:e.serial}]));
                });

                self.qt.on('secretStop', function(e){
                    e = e.params;
                    // self.qt.alert('业务提示', e.businessInfo);
                    self.callBoard.push(e.businessInfo);
                    self.qt.invoke('secretStop', $.toJSON([{serial:e.serial}]));
                });

                /***************
                 * 代理方法
                 ***************/
                self.qt.on('callUsersProxy', function(e){
                    self.qt.invoke('callUsers', e.params);
                });

                self.qt.on('voiceIntercomsProxy', function(e){
                    self.qt.invoke('voiceIntercoms', e.params);
                });

                self.qt.on('commandForwardDeviceProxy', function(e){
                    self.qt.invoke('commandForwardDevice', e.params);
                });

                self.qt.on('commandForwardFileProxy', function(e){
                    self.qt.invoke('commandForwardFile', e.params);
                });

                self.qt.on('secretStartProxy', function(e){
                    self.qt.invoke('secretStart', e.params);
                });
            });
        }
    });

    return Vue;
});