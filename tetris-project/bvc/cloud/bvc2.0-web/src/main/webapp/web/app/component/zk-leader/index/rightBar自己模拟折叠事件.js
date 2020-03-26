define([
    'text!' + window.APPPATH + 'component/zk-leader/index/rightBar.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'css!' + window.APPPATH + 'component/zk-leader/index/rightBar.css'
], function (tpl, ajax, $, Vue, config) {

//   组件名称
    var pluginName = 'leader-rightbar';

    Vue.component(pluginName, {
        template: tpl,
        data: function () {
            return {
                baseUrl: window.BASEPATH,
                user: '',
                qt: '',
                currentTab: '0',
                switchStatus:true,
                //控制折叠的状态
                // isCollapse: true,

                clickChange:null,//顶部标题的切换,防止多次点击出错
                buttons: {
                    //过滤在线
                    filterOnline: true,
                    //过滤离线
                    filterOffline: true,
                    //批量呼叫
                    batchCall: true,
                    //批量点播
                    batchVod: true,
                    //批量语音呼叫
                    batchIntercom: true,
                    //进入指挥
                    enterCommand:true,
                    //添加指挥
                    addCommand:true,
                    //删除指挥
                    removeCommand:true,
                    //添加成员
                    addMember:true,
                    //强退成员
                    removeMember:true,
                    //指挥提醒
                    commandReminder:true,
                    isRemind:false,
                    //协同指挥
                    cooperativeCommand:true,
                    //指挥转发
                    commandForward:true,
                    //对上静默
                    silenceUp:true,
                    silenceUpStart:true,
                    //对下静默
                    silenceDown:true,
                    silenceDownStart:true,
                    //发送通知
                    sendMessage:true,
                    //指挥录像
                    commandRecord:true,
                    isRecord:false,
                    //暂停指挥
                    commandPause:true,
                    //恢复指挥
                    commandRestart:true,
                    //指挥状态
                    isPause:false,
                    //退出指挥
                    exitCommand:true
                },
                props: {
                    children: 'children',
                    label: 'name'
                },
                //组织机构
                institution: {
                    data: [],
                    select: []
                },
                //设备资源
                device: {
                    data: [],
                    select: []
                },
                //文件资源
                file: {
                    data: [],
                    select: []
                },
                //录像回放
                record: {
                    data: [],
                    select: []
                },
                //指挥列表
                command: {
                    data: [],
                    select: []
                },
                contextMenu:{
                    visible:false,
                    scope:'',
                    currentNode:'',
                    left:'',
                    top:'',
                    call:true,
                    vod:true,
                    intercom:true,
                    dedicatedCommand:true,
                    enterCommand:true,
                    recordRecord:true
                },
                //指挥组
                group:{
                    currentId:'',
                    current:'',
                    isChairman:'',
                    entered:[],
                    remindIds:[],
                    loading:false
                }
            }
        },
        methods: {
            //底部球形按钮的点击事件
            ballClick: function () {
                $('.ball').toggleClass('activeBall');
                if ($('.ball').hasClass('activeBall')) {
                    $('.ball').removeClass('animating');
                } else {
                    $('.ball').addClass('animating');
                }
            },
            //顶部切换标题点击事件
            toggleMenu:function (index) {
                if(index === 1){
                    $($('.triggerLi')[0]).addClass('focusLight');
                    $($('.triggerLi')[1]).removeClass('focusLight');
                }else{
                    $($('.triggerLi')[0]).removeClass('focusLight');
                    $($('.triggerLi')[1]).addClass('focusLight');
                }
                $('.highLight').css('left',160*(index-1)+16+'px');
                if(this.clickChange==index){
                    return false;
                }
                this.switchStatus=!this.switchStatus;
                this.clickChange=index;
                //确保来回切的时候 球形按钮始终是初始状态
                $('.ball').removeClass('activeBall');
            },

            //-----第一个树形菜单的复选框的勾选事件-----
            onUserCheckChange: function (data) {
                var self = this;
                for (var i = 0; i < self.institution.select.length; i++) {
                    if (self.institution.select[i] === data) {
                        self.institution.select.splice(i, 1);
                        return;
                    }
                }
                self.institution.select.push(data);
            },
            onDviceCheckChange: function (data) {
                var self = this;
                for (var i = 0; i < self.device.select.length; i++) {
                    if (self.device.select[i] === data) {
                        self.device.select.splice(i, 1);
                        return;
                    }
                }
                self.device.select.push(data);
            },
            onFileCheckChange: function (data) {
                var self = this;
                for (var i = 0; i < self.file.select.length; i++) {
                    if (self.file.select[i] === data) {
                        self.file.select.splice(i, 1);
                        return;
                    }
                }
                self.file.select.push(data);
            },
            //录制回放的复选框的change事件
            onRecordCheckChange: function (data) {
                var self = this;
                var index = self.record.select.indexOf(data); //返回所在的索引

                if (index > -1) {
                    self.record.select.splice(index, 1);
                } else {
                    self.record.select.push(data);
                }
                //判断选中的中的level是否都相同
                self.buttons.batchVod = self.isSameLevel(self.record.select);
            },
            //方法：判断选中的数组中的level是否都相同 add by yan
            isSameLevel: function (select) {
                for (var i = 0; i < select.length; i++) {
                    var base = select[i];
                    for (var j = i + 1; j < select.length; j++) {
                        var compare = select[j];
                        if (base.level !== compare.level) {
                            return false;
                        }
                    }
                }
                return true;
            },
            onCommandCheckChange: function (data) {
                console.log(data)
                var self = this;
                for (var i = 0; i < self.command.select.length; i++) {
                    if (self.command.select[i] === data) {
                        self.command.select.splice(i, 1);
                        return;
                    }
                }
                self.command.select.push(data);
                var disable = false;
                for (var i = 0; i < self.command.select.length; i++) {
                    var creator = $.parseJSON(self.command.select[i].param).creator;
                    if (creator != self.user.id) {
                        disable = true;
                        break;
                    }
                }
                if (disable) {
                    self.buttons.removeCommand = false;
                } else {
                    self.buttons.removeCommand = true;
                }
            },

            //获取组织结构的数据以及刷新对应按钮
            refreshInstitution: function () {
                var self = this;
                self.institution.data.splice(0, self.institution.data.length);
                ajax.post('/command/query/find/institution/tree/user/filter/0', null, function (data) {
                    if (data && data.length > 0) {
                        for (var i = 0; i < data.length; i++) {
                            self.institution.data.push(data[i]);
                        }
                    }
                });
            },
            refreshInstitutionButtonAction: function () {
                var self = this;
                self.buttons.filterOnline = true;
                self.buttons.filterOffline = true;
                self.buttons.batchCall = true;
                self.buttons.batchVod = true;
                self.buttons.batchIntercom = true;
                self.buttons.enterCommand = false;
                self.buttons.addCommand = true;
                self.buttons.removeCommand = false;
            },
            //获取设备资源的数据以及刷新对应按钮
            refreshDevice: function () {
                var self = this;
                self.device.data.splice(0, self.device.data.length);
                ajax.post('/command/query/find/institution/tree/bundle/2/false/0', null, function (data) {
                    if (data && data.length > 0) {
                        for (var i = 0; i < data.length; i++) {
                            self.device.data.push(data[i]);
                        }
                    }
                });
            },
            refreshDeviceButtonAction: function () {
                var self = this;
                self.buttons.filterOnline = true;
                self.buttons.filterOffline = true;
                self.buttons.batchCall = false;
                self.buttons.batchVod = true;
                self.buttons.batchIntercom = false;
                self.buttons.enterCommand = false;
                self.buttons.addCommand = false;
                self.buttons.removeCommand = false;
            },
            //获取文件资源的数据以及刷新对应按钮
            refreshFile: function () {
                var self = this;
                self.file.data.splice(0, self.file.data.length);
                ajax.post('/monitor/vod/query/resource/tree', null, function (data) {
                    if (data && data.length > 0) {
                        for (var i = 0; i < data.length; i++) {
                            self.file.data.push(data[i]);
                        }
                    }
                });
            },
            refreshFileButtonAction: function () {
                var self = this;
                self.buttons.filterOnline = false;
                self.buttons.filterOffline = false;
                self.buttons.batchCall = false;
                self.buttons.batchVod = true;
                self.buttons.batchIntercom = false;
                self.buttons.enterCommand = false;
                self.buttons.addCommand = false;
                self.buttons.removeCommand = false;
            },
            //获取录像回放的数据以及刷新对应按钮
            refreshRecord: function () {
                var self = this;
                ajax.post('/command/record/query', null, function (data) {
                    if (data.groups.length) {
                        // 转换数据格式：给最外层加type属性，给records加groupName（用开始时间）把records和fragments属性改成children，fragments里的info就是名称
                        for (var i = 0; i < data.groups.length; i++) {
                            data.groups[i].type = 'FOLDER';
                            if (data.groups[i].records.length) {
                                data.groups[i].children = data.groups[i].records;
                                for (var j = 0; j < data.groups[i].records.length; j++) {
                                    //给每项加上type和数据本身所在层级
                                    data.groups[i].records[j].type = 'RECORD_PLAYBACK';
                                    data.groups[i].records[j].level = 2;
                                    data.groups[i].records[j].children = data.groups[i].records[j].fragments;
                                    data.groups[i].children[j].groupName = data.groups[i].records[j].startTime;
                                    if (data.groups[i].records[j].fragments.length) {
                                        for (var k = 0; k < data.groups[i].records[j].fragments.length; k++) {
                                            data.groups[i].records[j].fragments[k].groupName = data.groups[i].records[j].fragments[k].info;
                                            data.groups[i].records[j].fragments[k].type = 'RECORD_PLAYBACK';
                                            data.groups[i].records[j].fragments[k].level = 3;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    self.record.data = JSON.parse(JSON.stringify(data.groups));
                })
            },
            refreshRecordButtonAction: function () {
                var self = this;
                self.buttons.filterOnline = false;
                self.buttons.filterOffline = false;
                self.buttons.batchCall = false;
                self.buttons.batchVod = true;
                self.buttons.batchIntercom = false;
                self.buttons.enterCommand = false;
                self.buttons.addCommand = false;
                self.buttons.removeCommand = false;
            },
            //获取指挥列表的数据以及刷新对应按钮
            refreshCommand: function () {
                var self = this;
                self.command.data.splice(0, self.command.data.length);
                ajax.post('/command/query/list', null, function (data) {
                    if (data && data.length > 0) {
                        var commands = data[0].children;
                        var joinCommand = {
                            icon: 'icon-folder-close',
                            id: '-5',
                            key: 'FOLDER@@-5',
                            name: '可参加的指挥',
                            type: 'FOLDER',
                            children: []
                        };
                        var myCommand = {
                            icon: 'icon-folder-close',
                            id: '-6',
                            key: 'FOLDER@@-6',
                            name: '我创建的指挥',
                            type: 'FOLDER',
                            children: []
                        };
                        if (commands && commands.length > 0) {
                            for (var i = 0; i < commands.length; i++) {
                                var creator = $.parseJSON(commands[i].param).creator;
                                if (creator == self.user.id) {
                                    myCommand.children.push(commands[i]);
                                } else {
                                    joinCommand.children.push(commands[i]);
                                }
                            }
                        }
                        self.command.data.push(joinCommand);
                        self.command.data.push(myCommand);
                    }
                });
            },
            refreshCommandButtonAction: function () {
                var self = this;
                self.buttons.filterOnline = false;
                self.buttons.filterOffline = false;
                self.buttons.batchCall = false;
                self.buttons.batchVod = false;
                self.buttons.batchIntercom = false;
                self.buttons.enterCommand = false;
                self.buttons.addCommand = false;
                self.buttons.removeCommand = false;
            },
            
           // -----点击呼出出菜单-----
            handleContextMenuClose:function(){
                var self = this;
                self.contextMenu.visible = false;
                self.contextMenu.scope = '';
                self.contextMenu.currentNode = '';
                self.contextMenu.left = '';
                self.contextMenu.top = '';
            },
            handleContextMenuShow:function(e, data, type) {
                console.log(e.target)
                if(e.target.className === 'headline'){
                if (data.type === 'FOLDER') return;
                var self = this;
                self.contextMenu.visible = true;
                self.contextMenu.scope = type;
                self.contextMenu.currentNode = data;
                self.contextMenu.top = e.clientY + 'px';

                if (data.type === 'USER') {
                    self.contextMenu.call = true;
                    self.contextMenu.vod = true;
                    self.contextMenu.intercom = true;
                    self.contextMenu.dedicatedCommand = true;
                    self.contextMenu.enterCommand = false;
                    self.contextMenu.removeRecord = false;
                } else if (data.type === 'BUNDLE') {
                    self.contextMenu.call = false;
                    self.contextMenu.vod = true;
                    self.contextMenu.intercom = false;
                    self.contextMenu.dedicatedCommand = false;
                    self.contextMenu.enterCommand = false;
                    self.contextMenu.removeRecord = false;
                } else if (data.type === 'VOD_RESOURCE') {
                    self.contextMenu.call = false;
                    self.contextMenu.vod = true;
                    self.contextMenu.intercom = false;
                    self.contextMenu.dedicatedCommand = false;
                    self.contextMenu.enterCommand = false;
                    self.contextMenu.removeRecord = false;
                } else if (data.type === 'COMMAND') {
                    self.contextMenu.call = false;
                    self.contextMenu.vod = false;
                    self.contextMenu.intercom = false;
                    self.contextMenu.dedicatedCommand = false;
                    self.contextMenu.enterCommand = true;
                    self.contextMenu.removeRecord = false;
                } else if (data.type === 'RECORD_PLAYBACK') {
                    self.contextMenu.call = false;
                    self.contextMenu.vod = true;
                    self.contextMenu.intercom = false;
                    self.contextMenu.dedicatedCommand = false;
                    self.contextMenu.enterCommand = false;
                    if (self.contextMenu.currentNode.level == 2) {
                        self.contextMenu.removeRecord = true;
                    } else {
                        self.contextMenu.removeRecord = false;
                    }
                }
                }
            },
           //------第一个树形菜单底部呼出菜单上的点击事件-----
            callUser:function(){
                var self = this;
                var userId = self.contextMenu.currentNode.id;
                ajax.post('/command/call/user/start', {userId:userId}, function(data){
                    self.qt.invoke('callUsers', $.toJSON([data]));
                });
            },
            // 一减呼叫
            callUserBatch: function () {
                var self = this;
                if (self.institution.select.length <= 0) {
                    self.qt.alert('消息提示', '您还没有选中用户');
                    return;
                }
                var userIds = [];
                for (var i = 0; i < self.institution.select.length; i++) {
                    userIds.push(self.institution.select[i].id);
                }
                ajax.post('/command/call/user/start/batch', {userIds: $.toJSON(userIds)}, function (data) {
                    self.qt.invoke('callUsers', $.toJSON(data));
                });
            },
            voiceIntercom:function(){
                var self = this;
                var userId = self.contextMenu.currentNode.id;
                ajax.post('/command/voice/intercom/start', {userId:userId}, function(data){
                    self.qt.invoke('voiceIntercoms', $.toJSON([data]));
                });
            },
            // 一键语音通话
            voiceIntercomBatch:function(){
                var self = this;
                if(self.institution.select.length <= 0){
                    self.qt.alert('消息提示', '您还没有选中用户');
                    return;
                }
                var userIds = [];
                for(var i=0; i<self.institution.select.length; i++){
                    userIds.push(self.institution.select[i].id);
                }
                ajax.post('/command/voice/intercom/start/batch', {userIds: $.toJSON(userIds)}, function(data){
                    self.qt.invoke('voiceIntercoms', $.toJSON(data));
                });
            },
            vod:function(){
                var self = this;
                if(self.contextMenu.currentNode.type === 'USER'){
                    var userId = self.contextMenu.currentNode.id;
                    if(self.contextMenu.scope==='command' && self.group.current.creator==self.user.id){
                        ajax.post('/command/basic/vod/member/start', {
                            id:self.group.current.id,
                            userId:userId
                        }, function(data){
                            self.qt.invoke('groupMembers', $.toJSON([data]));
                        });
                    }else{
                        ajax.post('/command/vod/user/start', {userId:userId}, function(data){
                            self.qt.invoke('vodUsers', $.toJSON([data]));
                        });
                    }
                }else if(self.contextMenu.currentNode.type === 'BUNDLE'){
                    var deviceId = self.contextMenu.currentNode.id;
                    ajax.post('/command/vod/device/start', {deviceId:deviceId}, function(data){
                        self.qt.invoke('vodDevices', $.toJSON([data]));
                    });
                }else if(self.contextMenu.currentNode.type === 'VOD_RESOURCE'){
                    var resourceFileId = self.contextMenu.currentNode.id;
                    ajax.post('/command/vod/resource/file/start', {resourceFileId:resourceFileId}, function(data){
                        self.qt.invoke('vodResourceFiles', $.toJSON([data]));
                    });
                } else if (self.contextMenu.currentNode.type === 'RECORD_PLAYBACK'){
                    var recordId = self.contextMenu.currentNode.id;
                    ajax.post('/command/record/start/playback', {recordId: recordId}, function (data) {
                        self.qt.invoke('vodRecordFileStart', $.toJSON(data));
                    });
                }
            },
            // 一键点播
            vodBatch:function() {
                var self = this;
                if (self.currentTab == 0) {
                    //点播用户
                    if (self.institution.select.length <= 0) {
                        self.qt.alert('消息提示', '您还没有选中用户');
                        return;
                    }
                    var userIds = [];
                    for (var i = 0; i < self.institution.select.length; i++) {
                        userIds.push(self.institution.select[i].id);
                    }
                    ajax.post('/command/vod/user/start/batch', {userIds: $.toJSON(userIds)}, function (data) {
                        self.qt.invoke('vodUsers', $.toJSON(data));
                    });
                } else if (self.currentTab == 1) {
                    //点播设备
                    if (self.device.select.length <= 0) {
                        self.qt.alert('消息提示', '您还没有选中设备');
                        return;
                    }
                    var deviceIds = [];
                    for (var i = 0; i < self.device.select.length; i++) {
                        deviceIds.push(self.device.select[i].id);
                    }
                    ajax.post('/command/vod/device/start/batch', {deviceIds: $.toJSON(deviceIds)}, function (data) {
                        self.qt.invoke('vodDevices', $.toJSON(data));
                    });
                } else if (self.currentTab == 2) {
                    //点播文件
                    if (self.file.select.length <= 0) {
                        self.qt.alert('消息提示', '您还没有选中文件');
                        return;
                    }
                    var resourceFileIds = [];
                    for (var i = 0; i < self.file.select.length; i++) {
                        resourceFileIds.push(self.file.select[i].id);
                    }
                    ajax.post('/command/vod/resource/file/start/batch', {resourceFileIds: $.toJSON(resourceFileIds)}, function (data) {
                        self.qt.invoke('vodResourceFiles', $.toJSON(data));
                    });
                } else if (self.currentTab == 3) {
                    //点播录像
                    if (self.record.select.length <= 0) {
                        self.qt.alert('消息提示', '您还没有选中文件');
                        return;
                    }
                    var recordIds = [];
                    for (var i = 0; i < self.record.select.length; i++) {
                        recordIds.push(self.record.select[i].id);
                    }
                    // ajax.post('/command/record/start/playback', {recordId: recordId}, function (data) {
                    //     console.log(data)
                    //     // self.qt.invoke('vodResourceFiles', $.toJSON([data]));
                    // });
                }
            },
            secretCommand:function(){
                var self = this;
                var userId = self.contextMenu.currentNode.id;
                ajax.post('/command/secret/start', {userId:userId}, function(data){
                    self.qt.invoke('secretStart', $.toJSON(data));
                });
            },
            handleEnterCommand:function(){
                var self = this;
                if($.parseJSON(self.contextMenu.currentNode.param).creator == self.user.id){
                    for(var i=0; i<self.group.entered.length; i++){
                        if(self.group.entered.id == self.contextMenu.currentNode.id){
                            self.group.currentId = self.group.entered[i].id;
                            self.currentGroupChange(self.group.currentId);
                        }
                    }
                }else{
                    self.enterCommand([self.contextMenu.currentNode.id]);
                }
            },
            removeRecord:function(){
                var self  = this;
                self.qt.confirm('业务提示', '是否删除当前录制?', '确认', function(){
                    ajax.post('/command/record/remove', {
                        recordId:self.contextMenu.currentNode.id
                    }, function(){
                        self.refreshRecord();
                    });
                });
            },
            enterCommand:function(commandIds){
                var self = this;
                ajax.post('/command/basic/enter', {ids: $.toJSON(commandIds)}, function(data){
                    if(data && data.length>0){
                        var playerSettings = [];
                        for(var i=0; i<data.length; i++){
                            var finded = false;
                            for(var j=0; j<self.group.entered.length; j++){
                                if(self.group.entered[j].id==data[i].id && self.group.entered[j].type==='command'){
                                    finded = true;
                                    break;
                                }
                            }
                            if(!finded){
                                self.group.entered.push({
                                    id:data[i].id,
                                    name:data[i].name,
                                    creator:data.creator,
                                    type:'command'
                                });
                            }
                            if(i===0 && !self.group.current){
                                self.group.currentId = data[i].id;
                                self.group.current = data[i];
                                self.currentGroupChange(self.group.currentId);
                            }
                            if(data[i].splits && data[i].splits.length>0){
                                for(var j=0; j<data[i].splits.length; j++){
                                    playerSettings.push(data[i].splits[j]);
                                }
                            }
                        }
                        if(playerSettings.length > 0){
                            self.qt.invoke('enterGroups', $.toJSON(playerSettings));
                        }
                    }
                });
            },
            refreshEnteredGroups:function(){
                var self = this;
                self.group.entered.splice(0, self.group.entered.length);
                ajax.post('/command/query/entered', null, function(data){
                    if(data && data.length>0){
                        for(var i=0; i<data.length; i++){
                            if(data[i].status === 'remind'){
                                var finded = false;
                                for(var j=0; j<self.group.remindIds.length; j++){
                                    if(self.group.remindIds[j] == data[i].id){
                                        finded = true;
                                        break;
                                    }
                                }
                                if(!finded){
                                    self.group.remindIds.push(data[i].id);
                                }
                            }
                            self.group.entered.push(data[i]);
                            if(i===0 && !self.group.current){
                                self.group.currentId = data[i].id;
                                self.currentGroupChange(self.group.currentId);
                            }
                        }
                    }
                });
            },

            addCommand:function(){
                var self = this;
                if(!self.buttons.addCommand) return;

                var userIds = [];
                for(var i=0; i<self.tree.institution.select.length; i++){
                    userIds.push(self.tree.institution.select[i].id);
                }
                ajax.post('/command/basic/save', {members: $.toJSON(userIds)}, function(data){
                    data.type = 'command';
                    data.select = [];
                    self.group.current = data;
                    self.group.entered.push({
                        id:data.id,
                        name:data.name,
                        creator:data.creator,
                        type:'command'
                    });
                    self.group.currentId = data.id;
                    self.currentGroupChange(self.group.currentId);
                });
            },
            removeCommand:function(){
                var self = this;
                if(!self.buttons.removeCommand) return;
                if(self.tree.command.select.length <= 0){
                    self.qt.alert('信息提示', '您没有选择任何指挥组');
                    return
                };
                var ids = [];
                for(var i=0; i<self.tree.command.select.length; i++){
                    ids.push(self.tree.command.select[i].id);
                }
                ajax.post('/command/basic/remove', {ids: $.toJSON(ids)}, function(data){
                    self.tree.command.select.splice(0, self.tree.command.select.length);
                    self.refreshCommand();
                    for(var i=0; i<ids.length; i++){
                        for(var j=0; j<self.group.entered.length; j++){
                            if(ids[i] == self.group.entered[j].id){
                                self.group.entered.splice(j, 1);
                                if(self.group.currentId == ids[i]){
                                    self.group.currentId = '';
                                    self.group.current = '';
                                }
                                break;
                            }
                        }
                    }
                    if(!self.group.current && self.group.entered.length>0){
                        self.group.current = self.group.entered[0];
                        self.group.currentId = self.group.entered[0].id;
                        self.currentGroupChange(self.group.currentId);
                    }
                });
            },
            currentGroupChange:function(v){
                var self = this;
                var currentGroup = null;
                for(var i=0; i<self.group.entered.length; i++){
                    if(self.group.entered[i].id == v){
                        currentGroup = self.group.entered[i];
                        break;
                    }
                }
                if(currentGroup.type === 'command'){
                    self.group.loading = true;
                    ajax.post('/command/basic/query/members', {id:v}, function(data, status){
                        self.group.loading = false;
                        if(status !== 200) return;
                        data.type = 'command';
                        data.select = [];
                        self.group.current = data;
                        if(self.user.id == data.creator){
                            self.buttons.addMember = true;
                            self.buttons.removeMember = true;
                            self.buttons.commandReminder = true;
                            if(data.status === 'remind'){
                                self.buttons.isRemind = true;
                            }else{
                                self.buttons.isRemind = false;
                            }
                            self.buttons.cooperativeCommand = true;
                            self.buttons.commandForward = true;
                            self.buttons.silenceUp = true;
                            self.buttons.silenceUpStart = true;
                            self.buttons.silenceDown = true;
                            self.buttons.silenceDownStart = true;
                            self.buttons.sendMessage = true;
                            self.buttons.commandRecord = true;
                            self.buttons.isRecord = data.isRecord;
                            self.buttons.commandRestart = true;
                            if(data.status === 'pause'){
                                self.buttons.commandPause = false;
                                self.buttons.isPause = true;
                            }else{
                                self.buttons.commandPause = true;
                                self.buttons.isPause = false;
                            }
                            self.buttons.exitCommand = true;
                        }else{
                            self.buttons.addMember = false;
                            self.buttons.removeMember = false;
                            self.buttons.commandReminder = false;
                            self.buttons.cooperativeCommand = true;
                            self.buttons.commandForward = false;
                            self.buttons.silenceUp = true;
                            self.buttons.silenceUpStart = false;
                            self.buttons.silenceDown = true;
                            self.buttons.silenceDownStart = false;
                            self.buttons.sendMessage = false;
                            self.buttons.commandRecord = false;
                            self.buttons.isRecord = false;
                            self.buttons.commandPause = false;
                            self.buttons.commandRestart = false;
                            self.buttons.isPause = false;
                            self.buttons.exitCommand = true;
                        }
                        if(data.status === 'remind'){
                            var finded = false;
                            for(var i=0; i<self.group.remindIds.length; i++){
                                if(self.group.remindIds[i] == data.id){
                                    finded = true;
                                    break;
                                }
                            }
                            if(!finded){
                                self.group.remindIds.push(data.id);
                            }
                        }
                    }, null, [403, 404, 409, 500, 408]);
                }else{

                }
            },
            refreshCurrentGroupMembers:function(){
                var self = this;
                if(self.group.current.type === 'command'){
                    ajax.post('/command/basic/query/members', {id:self.group.current.id}, function(data){
                        data.type = 'command';
                        data.select = [];
                        self.group.current = data;
                    });
                }
            },
            onCommandUserCheckChange:function(data){
                var self = this;
                for(var i=0; i<self.group.current.select.length; i++){
                    if(self.group.current.select[i] === data){
                        self.group.current.select.splice(i, 1);
                        return;
                    }
                }
                self.group.current.select.push(data);
            },

          //指挥工作台的按钮
            addCommandMember:function(){
                var self = this;
                if(!self.buttons.addMember) return;
                self.qt.window('/router/zk/business/add/member', {id:self.group.current.id, type:self.group.current.type}, {width:500, height:600, title:'添加成员'});
            },
            removeCommandMember:function(){
                var self = this;
                if(!self.buttons.removeMember) return;
                if(self.group.current.select.length <= 0){
                    self.qt.alert('提示信息', '您未选择任何成员');
                    return;
                }
                self.qt.confirm('提示信息', '当前强退'+self.group.current.select.length+'个成员', '取消', '确定', null, function(){
                    var members = [];
                    for(var i=0; i<self.group.current.select.length; i++){
                        members.push(self.group.current.select[i].id);
                    }
                    ajax.post('/command/basic/remove/members', {
                        id:self.group.current.id,
                        members: $.toJSON(members)
                    }, function(data){
                        self.refreshCurrentGroupMembers();
                        if(data && data.length>0){
                            self.qt.invoke('commandMemberDelete', data);
                        }
                    });
                });
            },
            commandRemind:function(){
                var self = this;
                if(!self.buttons.commandReminder) return;
                if(self.buttons.isRemind){
                    ajax.post('/command/basic/remind/stop', {id:self.group.current.id}, function(data){
                        self.qt.alert('提示消息', '操作成功');
                        self.buttons.isRemind = false;
                        for(var i=0; i<self.group.remindIds.length; i++){
                            if(self.group.remindIds[i] == self.group.current.id){
                                self.group.remindIds.splice(i, 1);
                                break;
                            }
                        }
                        if(data.businessId == self.group.current.id){
                            self.group.current.status = data.status;
                        }
                        self.qt.invoke('commandRemindStop', $.toJSON(data));
                    });
                }else{
                    ajax.post('/command/basic/remind', {id:self.group.current.id}, function(data){
                        self.qt.alert('提示消息', '操作成功');
                        self.buttons.isRemind = true;
                        var finded = false;
                        for(var i=0; i<self.group.remindIds.length; i++){
                            if(self.group.remindIds[i] == self.group.current.id){
                                finded = true;
                                break;
                            }
                        }
                        if(!finded){
                            self.group.remindIds.push(self.group.current.id);
                        }
                        if(data.businessId == self.group.current.id){
                            self.group.current.status = data.status;
                        }
                        self.qt.invoke('commandRemind', $.toJSON(data));
                    });
                }
            },
            cooperativeCommand:function(){
                var self = this;
                if(!self.buttons.cooperativeCommand) return;
                self.qt.window('/router/zk/business/cooperation', {id:self.group.current.id, type:self.group.current.type}, {width:500, height:600, title:'协同指挥'});
            },
            commandForward:function(){
                var self = this;
                if(!self.buttons.commandForward) return;
                self.qt.window('/router/zk/business/forward', {id:self.group.current.id}, {width:1024, height:768, title:'指挥转发'});
            },
            silenceUp:function(){
                var self = this;
                if(!self.buttons.silenceUp) return;
                if(self.buttons.silenceUpStart){
                    ajax.post('/command/basic/silence/up/start', {id:self.group.current.id}, function(){
                        self.buttons.silenceUpStart = false;
                        self.qt.alert('提示消息', '操作成功');
                    });
                }else{
                    ajax.post('/command/basic/silence/up/stop', {id:self.group.current.id}, function(){
                        self.buttons.silenceUpStart = true;
                        self.qt.alert('提示消息', '操作成功');
                    });
                }
            },
            silenceDown:function(){
                var self = this;
                if(!self.buttons.silenceDown) return;
                if(self.buttons.silenceDownStart){
                    ajax.post('/command/basic/silence/down/start', {id:self.group.current.id}, function(){
                        self.buttons.silenceDownStart = false;
                        self.qt.alert('提示消息', '操作成功');
                    });
                }else{
                    ajax.post('/command/basic/silence/down/stop', {id:self.group.current.id}, function(){
                        self.buttons.silenceDownStart = true;
                        self.qt.alert('提示消息', '操作成功');
                    });
                }
            },
            sendMessage:function(){
                var self = this;
                if(!self.buttons.sendMessage) return;
                self.qt.window('/router/zk/business/command/message', {id:self.group.current.id}, {width:1024, height:768, title:'发送通知'});
            },
            commandRecord:function(){
                var self = this;
                if(self.buttons.isRecord){
                    ajax.post('/command/record/stop', {id:self.group.current.id}, function(data){
                        self.buttons.isRecord = false;
                        self.qt.alert('业务提示', '操作成功');
                    });
                }else{
                    ajax.post('/command/record/start', {id:self.group.current.id}, function(data){
                        self.buttons.isRecord = true;
                        self.qt.alert('业务提示', '操作成功');
                    });
                }
            },
            startCommand:function(){
                var self = this;
                if(self.group.current.status === 'pause'){
                    ajax.post('/command/basic/pause/recover', {
                        id:self.group.current.id
                    }, function(data){
                        self.group.current.status = 'start';
                        for(var i=0; i<self.group.entered.length; i++){
                            if(self.group.entered[i].id == self.group.current.id){
                                self.group.entered[i].status = 'start';
                                break;
                            }
                        }
                        self.buttons.commandPause = true;
                        self.buttons.isPause = false;
                    });
                }else{
                    ajax.post('/command/basic/start', {
                        id:self.group.current.id
                    }, function(data){
                        self.group.current.status = 'start';
                        var splits = data.splits;
                        if(splits && splits.length>0){
                            self.qt.invoke('groupMembers', $.toJSON(splits));
                        }
                    });
                }
            },
            pauseCommand:function(){
                var self = this;
                ajax.post('/command/basic/pause', {
                    id:self.group.current.id
                }, function(data){
                    self.group.current.status = 'pause';
                    for(var i=0; i<self.group.entered.length; i++){
                        if(self.group.entered[i].id == self.group.current.id){
                            self.group.entered[i].status = 'pause';
                            break;
                        }
                    }
                    self.buttons.commandPause = false;
                    self.buttons.isPause = true;
                    //self.qt.invoke('commandPause', $.toJSON(data));
                });
            },
            exitCommand:function(){
                var self = this;
                if(self.user.id == self.group.current.creator){
                    ajax.post('/command/basic/stop', {
                        id:self.group.current.id
                    }, function(data){
                        if(self.group.current.creator != self.user.id){
                            for(var i=0; i<self.group.entered.length; i++){
                                if(self.group.entered[i].id == self.group.current.id){
                                    self.group.entered.splice(i, 1);
                                    break;
                                }
                            }
                            if(self.group.entered.length > 0){
                                self.group.current = self.group.entered[0];
                                self.group.currentId = self.group.entered[0].id;
                                self.currentGroupChange(self.group.currentId);
                            }else{
                                self.group.current = '';
                                self.group.currentId = '';
                            }
                        }else{
                            self.group.current.status = 'stop';
                        }
                        self.qt.invoke('commandExit', $.toJSON(data));
                    });
                }else{
                    ajax.post('/command/basic/exit', {
                        id:self.group.current.id
                    }, function(data){
                        for(var i=0; i<self.group.entered.length; i++){
                            if(self.group.entered[i].id == self.group.current.id){
                                self.group.entered.splice(i, 1);
                                break;
                            }
                        }
                        if(self.group.entered.length > 0){
                            self.group.currentId = self.group.entered[i].id;
                            self.group.current = self.group.entered[i];
                            self.currentGroupChange(self.group.currentId);
                        }else{
                            self.group.currentId = '';
                            self.group.current = '';
                        }
                        if(data && data.length>0){
                            self.qt.invoke('commandExit', data);
                        }
                    });
                }
            },
        },
        computed:{
            groupCurrent:function(){
                var self = this;
                return self.group.current;
            }
        },
        watch: {
            currentTab: function () {
                var self = this;
                if (self.currentTab == 0) {
                    //组织结构
                    if (self.institution.data.length === 0) {
                        self.refreshInstitution();
                    }
                    self.refreshInstitutionButtonAction();
                } else if (self.currentTab == 1) {
                    //设备资源
                    if (self.device.data.length === 0) {
                        self.refreshDevice();
                    }
                    self.refreshDeviceButtonAction();
                } else if (self.currentTab == 2) {
                    //文件资源
                    if (self.file.data.length === 0) {
                        self.refreshFile();
                    }
                    self.refreshFileButtonAction();
                } else if (self.currentTab == 3) {
                    //录像回放
                    if (self.record.data.length === 0) {
                        self.refreshRecord();
                    }
                    self.refreshRecordButtonAction();
                } else if (self.currentTab == 4) {
                    //指挥列表
                    if (self.command.data.length === 0) {
                        self.refreshCommand();
                    }
                    self.refreshCommandButtonAction();
                }
            },
            groupCurrent:function(v){
                var self = this;
                if(self.user.id == v.creator){
                    self.group.isChairman = true;
                    self.buttons.addMember = true;
                    self.buttons.removeMember = true;
                    self.buttons.commandReminder = true;
                    if(v.status === 'remind'){
                        self.buttons.isRemind = true;
                    }else{
                        self.buttons.isRemind = false;
                    }
                    self.buttons.cooperativeCommand = true;
                    self.buttons.commandForward = true;
                    self.buttons.silenceUp = false;
                    self.buttons.silenceDown = true;
                    self.buttons.sendMessage = true;
                    self.buttons.commandRecord = true;
                    self.buttons.commandRestart = true;
                    if(v.status === 'pause'){
                        self.buttons.commandPause = false;
                        self.buttons.isPause = true;
                    }else{
                        self.buttons.commandPause = true;
                        self.buttons.isPause = false;
                    }
                    self.buttons.exitCommand = true;
                }else{
                    self.group.isChairman = false;
                    self.buttons.addMember = false;
                    self.buttons.removeMember = false;
                    self.buttons.commandReminder = false;
                    self.buttons.cooperativeCommand = false;
                    self.buttons.commandForward = false;
                    self.buttons.silenceUp = true;
                    self.buttons.silenceDown = true;
                    self.buttons.sendMessage = false;
                    self.buttons.commandRecord = false;
                    self.buttons.commandPause = false;
                    self.buttons.commandRestart = false;
                    self.buttons.isPause = false;
                    self.buttons.exitCommand = true;
                }
            }
        },
        mounted: function () {
            var self = this;
            // closest() 完成事件委托。当被最接近的列表元素或其子后代元素被点击时
            $(document).on('mousedown.contextMenu', function(e){
                var $target = $(e.target);
                // console.log($('.context-menu').is(':visible'))
                if(!($target.is('.context-menu') || $target.closest('.context-menu')[0])){
                    self.handleContextMenuClose();
                }
            });

            self.qt = new QtContext('rightBar', function () {
                //初始化ajax
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
                self.refreshInstitution();
                self.refreshInstitutionButtonAction();

                self.qt.get(['user'], function(variables){
                    self.user = variables.user?$.parseJSON(variables.user):{};
                    self.refreshEnteredGroups();
                });

                //接收折叠按钮的点击事件(shan)
                // self.qt.on('sideBarShowOrHide', function(){
                //     self.isCollapse = !self.isCollapse;
                // });

                self.qt.on('commandMemberAdd', function(e){
                    e = e.params;
                    self.refreshCurrentGroupMembers();
                    self.qt.invoke('groupMembers', e);
                });

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
                    self.qt.alert('业务提示', businessInfo);
                    self.qt.invoke('callUserStop', $.toJSON({serial:serial}));
                });
                //websocket 停止呼叫
                self.qt.on('callUserStopMessage', function(e){
                    var callUserInfo = e.params;
                    var businessInfo = callUserInfo.businessInfo;
                    var serial = callUserInfo.serial;
                    self.qt.alert('业务提示', businessInfo);
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
                    self.qt.alert('业务提示', businessInfo);
                    self.qt.invoke('voiceIntercomStop', $.toJSON({serial:serial}));
                });
                //websocket 停止语音对讲
                self.qt.on('voiceIntercomStopMessage', function(e){
                    var callUserInfo = e.params;
                    var businessInfo = callUserInfo.businessInfo;
                    var serial = callUserInfo.serial;
                    self.qt.alert('业务提示', businessInfo);
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
                    self.qt.alert('业务提示', e.businessInfo);
                    if(e.splits && e.splits.length>0){
                        self.qt.invoke('cooperationGrant', e.splits);
                    }
                });
                //拒绝协同指挥
                self.qt.on('cooperationRefuse', function(e){
                    var e = e.params;
                    self.qt.alert('业务提示', e.businessInfo);
                });
                //撤销协同指挥授权
                self.qt.on('cooperationRevoke', function(e){
                    var e = e.params;
                    self.qt.alert('业务提示', e.businessInfo);
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
                        /*ajax.post('/command/basic/enter', {ids: $.toJSON([e.businessId])}, function(data){
                            data = data[0];
                            var finded = false;
                            if(self.group.entered && self.group.entered.length>0){
                                for(var i=0; i<self.group.entered.length; i++){
                                    if(self.group.entered[i].id == data.id){
                                        finded = true;
                                        break;
                                    }
                                }
                            }
                            if(!finded){
                                self.group.entered.push({
                                    id:data.id,
                                    name:data.name,
                                    creator:data.creator,
                                    type:'command'
                                });
                            }
                            if(self.group.currentId != data.id){
                                self.group.currentId = data.id;
                                self.group.current = data;
                                self.currentGroupChange(self.group.currentId);
                            }
                            if(data.splits && data.splits.length > 0){
                                self.qt.invoke('enterGroups', $.toJSON(data.splits));
                            }
                        });*/
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
                    self.qt.alert('业务提示', e.businessInfo);
                    self.qt.invoke('commandExit', $.toJSON(e.splits));
                });

                self.qt.on('commandMemberOnline', function(e){
                    e = e.params;
                    self.qt.alert('业务提示', e.businessInfo);
                    if(e.splits && e.splits.length>0){
                        self.qt.invoke('groupMembers', $.toJSON(e.splits));
                    }
                });

                self.qt.on('commandMemberOffline', function(e){
                    e = e.params;
                    self.qt.alert('业务提示', e.businessInfo);
                    if(e.splits && e.splits.length>0){
                        self.qt.invoke('commandExit', e.splits);
                    }
                });

                self.qt.on('commandPause', function(e){
                    e = e.params;
                    self.qt.alert('业务提示', e.businessInfo);
                    //self.qt.invoke('commandPause', e.splits);
                });

                self.qt.on('commandPauseRecover', function(e){
                    e = e.params;
                    self.qt.invoke('commandPauseRecover', e.splits);
                });

                self.qt.on('commandMemberDelete', function(e){
                    e = e.params;
                    self.qt.alert('业务提示', e.businessInfo);
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
                    self.qt.alert('业务提示', e.businessInfo);
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
                    self.qt.alert('业务提示', e.businessInfo);
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
                    self.qt.alert('业务提示', e.businessInfo);
                    self.qt.invoke('secretStop', $.toJSON([{serial:e.serial}]));
                });

                self.qt.on('secretStop', function(e){
                    e = e.params;
                    self.qt.alert('业务提示', e.businessInfo);
                    self.qt.invoke('secretStop', $.toJSON([{serial:e.serial}]));
                });

                self.qt.on('responseModeChange', function(e){
                    self.settings.responseMode = e.params;
                });

                self.qt.on('sendAnswerModeChange', function(e){
                    self.settings.sendAnswerMode = e.params;
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