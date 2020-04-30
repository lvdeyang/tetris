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
                currentTab: '-1', //默认不高亮
                switchStatus1: true, //树1的显示状态
                switchStatus2: false, //树2的显示状态
                switchStatus3: false, //树3的显示状态
                differentiate: 1, //区分是指挥还是会议，1：指挥，2：会议
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
                    //是否可用进入指挥
                    enterCommand: true,
                    //是否可用 进入会议
                    enterMeet: true,
                    //添加指挥
                    addCommand: true,
                    //删除指挥
                    removeCommand: true,
                    //添加成员
                    addMember: true,
                    //强退成员
                    removeMember: true,
                    //指挥提醒
                    commandReminder: true, //是否禁用
                    isRemind: false, //更改状态
                    //协同指挥
                    cooperativeCommand: true,
                    //取消协同
                    cancelCooperative: true,
                    //指挥转发
                    commandForward: true,
                    //对上静默
                    silenceUp: true,
                    silenceUpStart: false, //对上静默是否已开始
                    //对下静默
                    silenceDown: true,
                    silenceDownStart: false, //对下静默是否已开始
                    //发送通知
                    sendMessage: true,
                    //指挥录像
                    commandRecord: true, //是否禁用
                    isRecord: false, //更改状态
                    //暂停指挥
                    commandPause: true,
                    //恢复指挥
                    commandRestart: true,
                    //指挥状态
                    isPause: false,
                    //是否可点退出指挥按钮
                    exitCommand: false,
                    //是否可点 退出 会议
                    exitMeet: false,
                    //发言
                    speak: true, //按钮是否显示
                    isSpeaking: false, //控制显示哪个按钮,是否正在发言
                    //控制指定发言，全员讨论按钮是否显示
                    meetBtn: true,
                    discussion: false, //false:没在讨论，应该显示讨论按钮，true：正在讨论，应该显示停止讨论按钮
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
                //当前下拉菜单选中的项
                currentSelectId: '',
                contextMenu: {
                    visible: false,
                    scope: '',
                    currentNode: '',
                    left: '',
                    top: '',
                    call: true,
                    vod: true,
                    intercom: true,
                    dedicatedCommand: true,
                    enterCommand: true,
                    recordRecord: true
                },
                //指挥组
                group: {
                    currentId: '',
                    current: '',
                    isChairman: '',
                    entered: [],
                    remindIds: []
                },
                //会议组
                meet: {
                    currentId: '', //当前会议的id
                    current: '',
                    entered: [],
                    remindIds: []
                },

                //给指挥起名字
                dialogTitle: '',
                dialog: {
                    resetName: {
                        visible: false,
                        name: ''
                    }
                },
                dialogVisible: false,
                dialogInput: '',
                //窗口高度
                firstMenuHeight: '',
                //滚动外容器的高度，动态的
                contentStyleObj: {
                    height: ''
                },
                filterText: '',
                //退出时退的是指挥还是会议，1：指挥，2：会议
                agreeExitCommand: 0
            }
        },
        methods: {
            //顶部切换标题点击事件
            toggleMenu1: function () {
                $('.triggerLi1').addClass('focusLight');
                $('.triggerLi2').removeClass('focusLight');
                $(".highLight").css({"display": "block", "left": "7px"});
                this.switchStatus1 = true;
                this.switchStatus2 = false;
                this.switchStatus3 = false;
                this.differentiate = 1;
                this.currentTab = '-1';
                this.refreshCommand(null);
            },
            toggleMenu2: function () {
                var self = this;
                $('.triggerLi1').removeClass('focusLight');
                $('.triggerLi2').addClass('focusLight');
                $(".highLight").css({"display": "block", "left": "167px"});
                this.switchStatus1 = false;
                this.switchStatus2 = false;
                this.switchStatus3 = true;
                this.differentiate = 2;
                this.currentTab = '-1';
                self.refreshCommand('meeting',function () {
                    self.refreshEnteredGroups();
                });

            },
            //从头部2个tab切到左侧的时候
            handleClick: function (tab) {
                if (tab.index == 0) {
                    this.switchStatus1 = false;
                    this.switchStatus2 = true;
                    this.switchStatus3 = false;
                }
            },
            //树形组件自定义过滤方法, 触发页面显示配置的筛选
            filterNode: function (value, data, node) {
                // 如果什么都没填就直接返回
                if (!value) return true;
                // 如果传入的value和data中的label相同说明是匹配到了
                if (data.name.indexOf(value) !== -1) {
                    return true;
                }
                // 否则要去判断它是不是选中节点的子节点
                return this.checkBelongToChooseNode(value, data, node);
            },
            // 判断传入的节点是不是选中节点的子节点
            checkBelongToChooseNode: function (value, data, node) {
                var level = node.level;
                // 如果传入的节点本身就是一级节点就不用校验了
                if (level === 1) {
                    return false;
                }
                // 先取当前节点的父节点
                var parentData = node.parent;
                // 遍历当前节点的父节点
                var index = 0;
                while (index < level - 1) {
                    // 如果匹配到直接返回
                    if (parentData.data.name.indexOf(value) !== -1) {
                        return true;
                    }
                    // 否则的话再往上一层做匹配
                    parentData = parentData.parent;
                    index++;
                }
                // 没匹配到返回false
                return false;
            },
            //-----第二部分 菜单 复选框的勾选事件 start -----
            onUserCheckChange: function (data) {
                var self = this;
                for (var i = 0; i < self.institution.select.length; i++) {
                    if (self.institution.select[i] === data) {
                        self.institution.select.splice(i, 1);
                        return;
                    }
                }
                self.institution.select.push(data);
                self.institution.select.forEach(function (value) {
                    value.checked = true;
                });
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
            //-----第二部分 菜单 复选框的勾选事件 end -----

            //获取组织结构的数据以及刷新对应按钮
            refreshInstitution: function () {
                var self = this;
                self.institution.data.splice(0, self.institution.data.length);
                ajax.post('/command/query/find/institution/tree/user/filter/0', null, function (data) {
                    if (data && data.length > 0) {
                        for (var i = 0; i < data.length; i++) {
                            data[i].children.forEach(function (value) {
                                self.institution.data.push(value);
                            })
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
                        self.record.data = JSON.parse(JSON.stringify(data.groups));
                    }
                })
            },
            refreshRecordButtonAction: function () {
                var self = this;
                self.buttons.filterOnline = false;
                self.buttons.filterOffline = false;
                self.buttons.batchCall = false;
                self.buttons.batchVod = false;
                self.buttons.batchIntercom = false;
                self.buttons.addCommand = false;
                self.buttons.removeCommand = false;
            },
            //获取指挥/会议列表的数据，type取值为meeting/command，如果为空则相当于command
            refreshCommand: function (type, callback) {
                var self = this;
                self.command.data.splice(0, self.command.data.length);
                ajax.post('/command/query/list', {type: type}, function (data) {
                    if (data && data.length > 0) {
                        var commands = data[0].children;
                        if (commands && commands.length > 0) {
                            self.command.data = commands;
                            if (typeof callback == 'function') {
                                callback();
                            }
                        }
                    }
                });
            },

            // --------点击呼出出菜单-----
            handleContextMenuClose: function () {
                var self = this;
                self.contextMenu.visible = false;
                self.contextMenu.scope = '';
                self.contextMenu.currentNode = '';
                self.contextMenu.left = '';
                self.contextMenu.top = '';
            },
            handleContextMenuShow: function (e, data, type, name) {
                if ($(e.target).hasClass('handDown')) {
                    if (data.type === 'FOLDER') return;
                    var self = this;
                    self.contextMenu.visible = true;
                    self.contextMenu.scope = type;
                    self.contextMenu.currentNode = data;

                    //在指挥里把点播改成观看
                    self.contextMenu.diff = name;
                    //增加修改指挥名称功能
                    self.contextMenu.resetName = name;
                    //记住指挥的id,观看的时候的时候用到
                    self.dialog.resetName.commandId = data.id;

                    if (data.type === 'USER') {
                        self.contextMenu.call = true;
                        self.contextMenu.vod = true;
                        self.contextMenu.intercom = true;
                        self.contextMenu.dedicatedCommand = true;
                        self.contextMenu.enterCommand = false;
                        self.contextMenu.removeRecord = false;
                        self.contextMenu.recordRecord = true;
                    } else if (data.type === 'BUNDLE') {
                        self.contextMenu.call = false;
                        self.contextMenu.vod = true;
                        self.contextMenu.intercom = false;
                        self.contextMenu.dedicatedCommand = false;
                        self.contextMenu.enterCommand = false;
                        self.contextMenu.removeRecord = false;
                        self.contextMenu.recordRecord = true;
                    } else if (data.type === 'VOD_RESOURCE') {
                        self.contextMenu.call = false;
                        self.contextMenu.vod = true;
                        self.contextMenu.intercom = false;
                        self.contextMenu.dedicatedCommand = false;
                        self.contextMenu.enterCommand = false;
                        self.contextMenu.removeRecord = false;
                        self.contextMenu.recordRecord = false;
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
                        self.contextMenu.recordRecord = false;
                        if (self.contextMenu.currentNode.level == 2) {
                            self.contextMenu.removeRecord = true;
                        } else {
                            self.contextMenu.removeRecord = false;
                        }
                    }
                    //区分下是不是当前用户建的指挥,目的是只有主席才能改名字
                    // if (typeof data.param=='object') {
                    //     var param = JSON.parse(data.param);
                    //     self.contextMenu.currentNode.creator = param.creator;
                    // }
                }
            },


            //------弹出的菜单上的点击事件 start -----
            // 视频呼叫
            callUser: function () {
                var self = this;
                var userId = self.contextMenu.currentNode.id;
                if (!self.contextMenu.call) return;
                ajax.post('/command/call/user/start', {userId: userId}, function (data) {
                    self.qt.invoke('callUsers', $.toJSON([data]));
                    self.handleContextMenuClose();
                });
            },
            //语音通话
            voiceIntercom: function () {
                var self = this;
                var userId = self.contextMenu.currentNode.id;
                if (!self.contextMenu.intercom) {
                    return;
                }
                ajax.post('/command/voice/intercom/start', {userId: userId}, function (data) {
                    self.qt.invoke('voiceIntercoms', $.toJSON([data]));
                    self.handleContextMenuClose();
                });
            },
            //点播
            vod: function () {
                var self = this;
                if (!self.contextMenu.vod) {
                    return;
                }
                if (self.contextMenu.currentNode.type === 'USER') {
                    var userId = self.contextMenu.currentNode.id;
                    if (self.contextMenu.scope === 'command' && self.group.current.creator == self.user.id) {
                        ajax.post('/command/basic/vod/member/start', {
                            id: self.group.current.id,
                            userId: userId
                        }, function (data) {
                            self.qt.invoke('groupMembers', $.toJSON([data]));
                        });
                    } else {
                        ajax.post('/command/vod/user/start', {userId: userId}, function (data) {
                            self.qt.invoke('vodUsers', $.toJSON([data]));
                        });
                    }
                } else if (self.contextMenu.currentNode.type === 'BUNDLE') {
                    var deviceId = self.contextMenu.currentNode.id;
                    ajax.post('/command/vod/device/start', {deviceId: deviceId}, function (data) {
                        self.qt.invoke('vodDevices', $.toJSON([data]));
                    });
                } else if (self.contextMenu.currentNode.type === 'VOD_RESOURCE') {
                    var resourceFileId = self.contextMenu.currentNode.id;
                    ajax.post('/command/vod/resource/file/start', {resourceFileId: resourceFileId}, function (data) {
                        self.qt.invoke('vodResourceFiles', $.toJSON([data]));
                    });
                } else if (self.contextMenu.currentNode.type === 'RECORD_PLAYBACK') {
                    var recordId = self.contextMenu.currentNode.id;
                    if (self.contextMenu.currentNode.level == 2) {
                        ajax.post('/command/record/start/playback', {recordId: recordId}, function (data) {
                            self.qt.invoke('vodRecordFileStart', $.toJSON(data));
                        });
                    } else if (self.contextMenu.currentNode.level == 3) { //播放片段
                        ajax.post('/command/record/start/playback/fragments', {fragmentIds: $.toJSON([recordId])}, function (data) {
                            self.qt.invoke('vodRecordFileStart', $.toJSON(data));
                        });
                    }
                }
                self.handleContextMenuClose();
            },
            //观看
            view: function () {
                var self = this;
                //id:指挥的id，usesrid:每个用户的Id
                if (!self.contextMenu.vod) {
                    return;
                }
                if (this.differentiate == 1) {
                    ajax.post('/command/basic/vod/member/start', {
                        id: self.group.current.id,
                        userId: self.dialog.resetName.commandId
                    }, function (data) {
                        self.group.current.status = 'start';
                        self.qt.invoke('groupMembers', $.toJSON([data]));
                        self.handleContextMenuClose();
                    });
                } else {
                    ajax.post('/command/basic/vod/member/start', {
                        id: self.meet.current.id,
                        userId: self.dialog.resetName.commandId
                    }, function (data) {
                        self.meet.current.status = 'start';
                        self.qt.invoke('groupMembers', $.toJSON([data]));
                        self.handleContextMenuClose();
                    });
                }


            },
            //专项指挥
            secretCommand: function () {
                var self = this;
                var userId = self.contextMenu.currentNode.id;
                if (!self.contextMenu.dedicatedCommand) {
                    return;
                }
                ajax.post('/command/secret/start', {userId: userId}, function (data) {
                    self.qt.invoke('secretStart', $.toJSON(data));
                    self.handleContextMenuClose();
                });
            },
            //进入指挥
            handleEnterCommand: function () {
                var self = this;
                if (!self.contextMenu.enterCommand) {
                    return;
                }
                if ($.parseJSON(self.contextMenu.currentNode.param).creator == self.user.id) {
                    for (var i = 0; i < self.group.entered.length; i++) {
                        if (self.group.entered.id == self.contextMenu.currentNode.id) {
                            self.group.currentId = self.group.entered[i].id;
                            self.currentGroupChange(self.group.currentId);
                        }
                    }
                } else {
                    self.enterCommand([self.contextMenu.currentNode.id]);
                }
                self.handleContextMenuClose();
            },
            //删除
            removeRecord: function () {
                var self = this;
                if (!self.contextMenu.removeRecord) {
                    return;
                }
                self.qt.confirm('业务提示', '是否删除当前录制?', '确认', function () {
                    ajax.post('/command/record/remove', {
                        recordId: self.contextMenu.currentNode.id
                    }, function () {
                        self.refreshRecord();
                        self.handleContextMenuClose();
                    });
                });
            },
            //录制
            recordTask: function () {
                var self = this;
                var realType = JSON.parse(self.contextMenu.currentNode.param).realType;
                if (!self.contextMenu.recordRecord) {
                    return;
                }
                //在树上设备节点有个param字段  里面有realType  这个字段如果是XT 就调用“添加录制xt设备任务”  如果是BVC就调“添加录制本地设备任务"
                self.qt.window('/router/zk/leader/addRecord', {
                    paramId: self.contextMenu.currentNode.id,
                    paramType: self.contextMenu.currentNode.type,
                    realType: realType
                }, {width: 700, height: 500});
                self.contextMenu.visible = false;
            },
            //------弹出的菜单上的点击事件 end -----

            //点击 同意 进入指挥/会议,此版本不会走这
            enterCommand: function (commandIds, type) {
                var self = this;
                ajax.post('/command/basic/enter', {ids: $.toJSON(commandIds)}, function (data) {
                    if (data && data.length > 0) {
                        var playerSettings = [];
                        for (var i = 0; i < data.length; i++) {
                            var finded = false;
                            if (type == 'meet') {
                                for (var j = 0; j < self.meet.entered.length; j++) {
                                    if (self.meet.entered[j].id == data[i].id && self.meet.entered[j].type === 'command') {
                                        finded = true;
                                        break;
                                    }
                                }
                                if (!finded) {
                                    self.meet.entered.push({
                                        id: data[i].id,
                                        name: data[i].name,
                                        creator: data.creator,
                                        type: 'command'
                                    });
                                }
                                if (i === 0 && !self.meet.current) {
                                    //类型一样，渲染的时候才会显示文字，而不是id
                                    self.meet.currentId = data[i].id.toString();
                                    self.meet.current = data[i];
                                    self.currentGroupChange(self.meet.currentId);
                                }
                            } else {
                                for (var j = 0; j < self.group.entered.length; j++) {
                                    if (self.group.entered[j].id == data[i].id && self.group.entered[j].type === 'command') {
                                        finded = true;
                                        break;
                                    }
                                }
                                if (!finded) {
                                    self.group.entered.push({
                                        id: data[i].id,
                                        name: data[i].name,
                                        creator: data.creator,
                                        type: 'command'
                                    });
                                }
                                //进会时 调返回的数据，为了不影响指挥的代码
                                if (i === 0 && !self.group.current) {
                                    self.group.currentId = data[i].id;
                                    self.group.current = data[i];
                                    self.currentGroupChange(self.group.currentId);
                                }
                            }
                            if (data[i].splits && data[i].splits.length > 0) {
                                for (var j = 0; j < data[i].splits.length; j++) {
                                    playerSettings.push(data[i].splits[j]);
                                }
                            }
                            if (playerSettings.length > 0) {
                                self.qt.invoke('enterGroups', $.toJSON(playerSettings));
                            }
                        }
                    }
                });
            },
            //不需要成员同意  自动进入指挥/会议
            autoEnterCommand: function (data, type) {
                var self = this;
                if (data) {
                    var playerSettings = [];
                    var finded = false;
                    if (type == 'meet') {
                        for (var j = 0; j < self.meet.entered.length; j++) {
                            if (self.meet.entered[j].id == data.id && self.meet.entered[j].type === 'command') {
                                finded = true;
                                break;
                            }
                        }
                        if (!finded) {
                            self.meet.entered.push({
                                id: data.id,
                                name: data.name,
                                creator: data.creator,
                                type: 'command'
                            });
                        }
                        if (!self.meet.current) {
                            //类型一样，渲染的时候才会显示文字，而不是id
                            self.meet.currentId = data.id.toString();
                            self.meet.current = data;
                            self.currentGroupChange(self.meet.currentId);
                        }
                    } else {
                        for (var j = 0; j < self.group.entered.length; j++) {
                            if (self.group.entered[j].id == data.id && self.group.entered[j].type === 'command') {
                                finded = true;
                                break;
                            }
                        }
                        if (!finded) {
                            self.group.entered.push({
                                id: data.id,
                                name: data.name,
                                creator: data.creator,
                                type: 'command'
                            });
                        }
                        //进会时 调返回的数据，为了不影响指挥的代码
                        if (!self.group.current) {
                            self.group.currentId = data.id;
                            self.group.current = data;
                            self.currentGroupChange(self.group.currentId);
                        }
                    }
                    if (data.splits && data.splits.length > 0) {
                        for (var j = 0; j < data.splits.length; j++) {
                            playerSettings.push(data.splits[j]);
                        }
                    }
                    if (playerSettings.length > 0) {
                        self.qt.invoke('enterGroups', $.toJSON(playerSettings));
                    }
                }
            },

            //获取指挥工作台树形菜单已进入的指挥数据
            refreshEnteredGroups: function () {
                var self = this;
                if (self.differentiate === 1) {
                    self.group.entered.splice(0, self.group.entered.length);
                    ajax.post('/command/query/entered', null, function (data) {
                        if (data && data.length > 0) {
                            // 把每项放到entered中
                            data.forEach(function (item) {
                                self.group.entered.push(item);
                            });

                            // 过滤出status为'remind'的id
                            var remindIds = data.filter(function (item) {
                                return item.status === 'remind';
                            }).map(function (item) {
                                return item.id;
                            });
                            // self.group.remindIds里没有这个id的话，
                            remindIds.forEach(function (id) {
                                if (self.group.remindIds.indexOf(id) === -1) {
                                    self.group.remindIds.push(id);
                                }
                            });
                            // 如果没有默认值
                            if (!self.group.currentId) {
                                // 优先是开启的
                                var defaultItem = data.find(function (item) {
                                    return item.status === 'start';
                                });
                                // 其次是暂停的
                                if (!defaultItem) {
                                    defaultItem = data.find(function (item) {
                                        return item.status === 'pause';
                                    })
                                }

                                // 最后是随便
                                if (!defaultItem) {
                                    defaultItem = data[0];
                                }
                                self.group.currentId = defaultItem.id;
                                self.currentGroupChange(self.group.currentId);
                            }
                        }
                    });
                } else {
                    self.meet.entered.splice(0, self.meet.entered.length);
                    ajax.post('/command/query/entered', {type: 'meeting'}, function (data) {
                        if (data && data.length > 0) {
                            // 把每项放到entered中
                            data.forEach(function (item) {
                                self.meet.entered.push(item);
                            });
                            // 过滤出status为'remind'的id
                            var remindIds = data.filter(function (item) {
                                return item.status === 'remind';
                            }).map(function (item) {
                                return item.id;
                            });
                            // self.group.remindIds里没有这个id的话，
                            remindIds.forEach(function (id) {
                                if (self.meet.remindIds.indexOf(id) === -1) {
                                    self.meet.remindIds.push(id);
                                }
                            });
                            // 如果没有默认值
                            if (!self.meet.currentId) {
                                // 优先是开启的
                                var defaultItem = data.find(function (item) {
                                    return item.status === 'start';
                                });
                                // 其次是暂停的
                                if (!defaultItem) {
                                    defaultItem = data.find(function (item) {
                                        return item.status === 'pause';
                                    })
                                }
                                // 最后是随便
                                if (!defaultItem) {
                                    defaultItem = data[0];
                                }
                                self.meet.currentId = defaultItem.id;
                                self.qt.set('currentGroupId', self.meet.currentId);
                                self.currentGroupChange(self.meet.currentId);
                            }
                        }
                    })
                }
            },

            //----------第一个树形菜单底部的按钮图标 start----------
            // 过滤在线
            filterOnline: function () {
                var self = this;
                if (self.currentTab == 0) {
                    self.institution.data.splice(0, self.institution.data.length);
                    ajax.post('/command/query/find/institution/tree/user/filter/1', null, function (data) {
                        if (data && data.length > 0) {
                            for (var i = 0; i < data.length; i++) {
                                self.institution.data.push(data[i]);
                            }
                        }
                    });
                } else if (self.currentTab == 1) {
                    self.device.data.splice(0, self.device.data.length);
                    ajax.post('/command/query/find/institution/tree/bundle/2/false/1', null, function (data) {
                        if (data && data.length > 0) {
                            for (var i = 0; i < data.length; i++) {
                                self.device.data.push(data[i]);
                            }
                        }
                    });
                }
            },
            //过滤离线
            filterOffline: function () {
                var self = this;
                if (self.currentTab == 0) {
                    self.institution.data.splice(0, self.institution.data.length);
                    ajax.post('/command/query/find/institution/tree/user/filter/2', null, function (data) {
                        if (data && data.length > 0) {
                            for (var i = 0; i < data.length; i++) {
                                self.institution.data.push(data[i]);
                            }
                        }
                    });
                } else if (self.currentTab == 1) {
                    self.device.data.splice(0, self.device.data.length);
                    ajax.post('/command/query/find/institution/tree/bundle/2/false/2', null, function (data) {
                        if (data && data.length > 0) {
                            for (var i = 0; i < data.length; i++) {
                                self.device.data.push(data[i]);
                            }
                        }
                    });
                }
            },
            // 一键呼叫
            callUserBatch: function () {
                var self = this;
                if (self.institution.select.length <= 0) {
                    self.qt.warning('消息提示：您还没有选中用户');
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
            // 一键点播
            vodBatch: function () {
                var self = this;
                var num = null;  //指定播放器点播，返回来的是播放器的位置
                //右键点播放器会有个框表示选中
                self.qt.invoke('getChosenPlayer', function (e) {
                    num = e;
                    if (self.currentTab == 0) {
                        //点播用户
                        if (self.institution.select.length <= 0) {
                            self.qt.warning('消息提示：您还没有选中用户');
                            return;
                        }
                        var userIds = [];
                        for (var i = 0; i < self.institution.select.length; i++) {
                            userIds.push(self.institution.select[i].id);
                        }
                        if (num < 0) {
                            ajax.post('/command/vod/user/start/batch', {userIds: $.toJSON(userIds)}, function (data) {
                                self.qt.invoke('vodUsers', $.toJSON(data));
                                self.cancelChoose();
                            });
                        } else { //指定播放器点播用户
                            if (self.institution.select.length > 1) {
                                self.qt.warning('只能选择一项');
                                return;
                            }
                            ajax.post('/command/vod/user/start/player', {
                                userId: userIds[0],
                                serial: num
                            }, function (data) {
                                self.qt.invoke('vodUsers', $.toJSON([data]));
                                self.cancelChoose();
                            });
                        }

                    } else if (self.currentTab == 1) {
                        //点播设备
                        if (self.device.select.length <= 0) {
                            self.qt.warning('消息提示：您还没有选中设备');
                            return;
                        }
                        var deviceIds = [];
                        for (var i = 0; i < self.device.select.length; i++) {
                            deviceIds.push(self.device.select[i].id);
                        }
                        if (num < 0) {
                            ajax.post('/command/vod/device/start/batch', {deviceIds: $.toJSON(deviceIds)}, function (data) {
                                self.qt.invoke('vodDevices', $.toJSON(data));
                            });
                        } else {
                            if (self.device.select.length > 1) {
                                self.qt.warning('只能选择一项');
                                return;
                            }
                            ajax.post('/command/vod/device/start/player', {
                                deviceId: deviceIds[0],
                                serial: num
                            }, function (data) {
                                self.qt.invoke('vodDevices', $.toJSON([data]));
                            });
                        }

                    } else if (self.currentTab == 2) {
                        //点播文件
                        if (self.file.select.length <= 0) {
                            self.qt.warning('消息提示：您还没有选中文件');
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
                            self.qt.warning('消息提示', '您还没有选中文件');
                            return;
                        }
                        var recordIds = [];
                        for (var i = 0; i < self.record.select.length; i++) {
                            recordIds.push(self.record.select[i].id);
                        }
                        ajax.post('/command/record/start/playback/fragments', {fragmentIds: $.toJSON(recordIds)}, function (data) {
                            self.qt.invoke('vodResourceFiles', $.toJSON(data));
                        });
                    }
                });
            },
            // 一键语音通话
            voiceIntercomBatch: function () {
                var self = this;
                if (self.institution.select.length <= 0) {
                    self.qt.warning('消息提示：您还没有选中用户');
                    return;
                }
                var userIds = [];
                for (var i = 0; i < self.institution.select.length; i++) {
                    userIds.push(self.institution.select[i].id);
                }
                ajax.post('/command/voice/intercom/start/batch', {userIds: $.toJSON(userIds)}, function (data) {
                    self.cancelChoose();
                    self.qt.invoke('voiceIntercoms', $.toJSON(data));
                });
            },
            //添加指挥/会议
            addCommand: function (type) {  //type区分是建指挥还是建会议，1：指挥，2：会议
                var self = this;
                if (!self.buttons.addCommand) return;
                var userIds = [];
                for (var i = 0; i < self.institution.select.length; i++) {
                    userIds.push(self.institution.select[i].id);
                }
                if (!userIds.length) {
                    self.qt.warning('您还没有选择成员呢');
                    return false;
                }
                self.handleResetName('new', type);
            },
            //关闭给指挥起名字弹框事件
            resetNameClose: function () {
                var self = this;
                self.dialog.resetName.visible = false;
                self.dialog.resetName.name = '';
                self.cancelChoose();
            },
            //打开 给指挥起名字 的对话框显示 type1:区分修改还是新建，type2:区分是指挥还是会议
            handleResetName: function (type1, type2) {
                var self = this;
                //为了提交时区分是新建还是修改
                self.dialog.resetName.type = type1;
                self.dialog.resetName.createType = type2;
                //根据不同类型标题显示不同，修改时，把原有值赋过去
                if (type2 == 1) {
                    self.dialogTitle = "设置指挥名称";
                    if (type1 === 'edit') {
                        //报错要修改的那项的名字
                        self.dialog.resetName.name = self.group.current.name;
                    }
                } else {
                    self.dialogTitle = "设置会议名称";
                    if (type1 === 'edit') {
                        self.dialog.resetName.name = self.meet.current.name;
                    }
                }
                self.dialog.resetName.visible = true;
            },
            //给指挥起名字的添加事件
            resetNameCommit: function () {
                var self = this;
                if (!self.dialog.resetName.name || !(self.dialog.resetName.name.trim())) {
                    self.$message({
                        type: 'error',
                        message: '名称不能为空！'
                    });
                    return;
                } else {
                    if (self.dialog.resetName.name && self.dialog.resetName.name.length > 255) {
                        self.qt.error('名称字数不能超过255个字符');
                        return;
                    }
                }
                var userIds = [];
                for (var i = 0; i < self.institution.select.length; i++) {
                    userIds.push(self.institution.select[i].id);
                }
                self.dialog.resetName.name.replace(/^\s*|\s*$/g, ''); //去除头尾空格

                if (self.dialog.resetName.createType == 1) {  //指挥
                    if (self.dialog.resetName.type === 'new') {
                        // 新建指挥
                        ajax.post('/command/basic/save', {
                            members: $.toJSON(userIds),
                            name: self.dialog.resetName.name.trim()
                        }, function (data) {
                            data.type = 'command';
                            data.select = [];
                            self.group.current = data;
                            self.group.entered.push({
                                id: data.id,
                                name: data.name,
                                creator: data.creator,
                                type: 'command'
                            });
                            self.group.currentId = data.id;
                            self.qt.success('创建指挥工作组成功');
                            self.resetNameClose();
                        });
                    } else {
                        //   修改
                        ajax.post('/command/basic/modify/name', {
                            id: self.group.currentId,
                            name: self.dialog.resetName.name.trim()
                        }, function () {
                            self.qt.success('修改成功！');
                            self.resetNameClose();
                            self.refreshCommand();
                        });
                    }
                } else {
                    //新建会议
                    if (self.dialog.resetName.type === 'new') {
                        ajax.post('/command/meeting/save', {
                            members: $.toJSON(userIds),
                            name: self.dialog.resetName.name.trim()
                        }, function (data) {
                            // error代表有重名的情况
                            if (data.status == 'error') {
                                self.dialogVisible = true;
                                self.dialogInput = data.errorInfo.recommendedName;
                                return;
                            }
                            self.meet.current = data;
                            self.meet.entered.push({
                                id: data.id,
                                name: data.name,
                                creator: data.creator,
                                type: 'command'
                            });
                            self.meet.currentId = data.id;
                            self.dialogVisible = false;
                            self.qt.success('创建会议成功！');
                            self.resetNameClose();
                        });
                    } else {
                        ajax.post('/command/basic/modify/name', {
                            id: self.meet.currentId,
                            name: self.dialog.resetName.name.trim()
                        }, function () {
                            self.qt.success('修改成功！');
                            self.resetNameClose();
                            self.refreshCommand('meeting');
                        });
                    }
                }
            },
            // 同意修改会议名称按钮
            agreeEditName: function () {
                var self = this;
                if (!self.dialogInput || !(self.dialogInput.trim())) {
                    self.qt.error('会议名称不能为空！');
                    return;
                } else {
                    if (self.dialogInput && self.dialogInput.length > 255) {
                        self.qt.error('会议名称字数不能超过255个字符');
                        return;
                    }
                }

                var userIds = [];
                for (var i = 0; i < self.institution.select.length; i++) {
                    userIds.push(self.institution.select[i].id);
                }

                ajax.post('/command/meeting/save', {
                    members: $.toJSON(userIds),
                    name: self.dialogInput.trim()
                }, function (data) {
                    // error代表有重名的情况
                    if (data.status == 'error') {
                        self.dialogVisible = true;
                        self.dialogInput = data.errorInfo.recommendedName;
                        return;
                    }
                    self.dialog.resetName.visible = false;
                    self.dialogVisible = false;
                });
            },
            //第一排上面的按钮成员进入指挥
            enterCommandBatch: function () {
                var self = this;
                var ids = [];
                ids.push(self.group.currentId);
                ajax.post('/command/basic/enter', {ids: $.toJSON(ids)}, function (data) {
                    if (data && data.length > 0) {
                        var playerSettings = [];
                        for (var i = 0; i < data.length; i++) {
                            var finded = false;
                            for (var j = 0; j < self.group.entered.length; j++) {
                                if (self.group.entered[j].id == data[i].id && self.group.entered[j].type === 'command') {
                                    finded = true;
                                    break;
                                }
                            }
                            if (!finded) {
                                self.group.entered.push({
                                    id: data[i].id,
                                    name: data[i].name,
                                    creator: data.creator,
                                    type: 'command'
                                });
                            }
                            //当前没有指挥的话 就进入第一项
                            if (i === 0 && !self.group.current) {
                                self.group.currentId = data[i].id;
                                self.group.current = data[i];
                                self.currentGroupChange(self.group.currentId);
                            }
                            if (data[i].splits && data[i].splits.length > 0) {
                                for (var j = 0; j < data[i].splits.length; j++) {
                                    playerSettings.push(data[i].splits[j]);
                                }
                            }
                        }
                        if (playerSettings.length > 0) {
                            self.qt.invoke('enterGroups', $.toJSON(playerSettings));
                            self.buttons.exitCommand = true;
                            self.buttons.enterCommand = false;
                        }
                        self.refreshCommand();
                    }
                });
            },
            //删除指挥
            removeCommand: function () {
                var self = this;
                var id = self.group.currentId;
                ajax.post('/command/basic/remove', {ids: $.toJSON([id])}, function (data) {
                    self.qt.success('删除成功');
                    self.refreshCommand(null);
                    for (var j = 0; j < self.group.entered.length; j++) {
                        if (id == self.group.entered[j].id) {
                            self.group.entered.splice(j, 1);
                            if (self.group.currentId == id) {
                                self.group.currentId = '';
                                self.group.current = '';
                            }
                            break;
                        }
                    }
                    if (!self.group.current && self.group.entered.length > 0) {
                        self.group.current = self.group.entered[0];
                        self.group.currentId = self.group.entered[0].id;
                        self.currentGroupChange(self.group.currentId);
                    }
                });
            },
            //点播录制文件
            playRecordFile: function () {
                this.qt.window('/router/zk/leader/record/file', null, {width: 1366, height: 700})
            },
            //外部文件管理
            openOutFile: function () {
                this.qt.window('/router/zk/leader/out/file', null, {width: 1000, height: 700})
            },
            //文件导入
            fileImport: function () {
                this.qt.window('/router/zk/leader/file/import', null, {width: 1000, height: 700})
            },
            //----------第一个树形菜单底部的按钮图标 end----------

            //指挥工作台中的 下拉菜单
            currentGroupChange: function (v) {
                console.log('下拉：' + v)
                if (!v) return;
                var self = this;
                var currentGroup = null;
                if (typeof v === 'object') {
                    currentGroup = v;
                } else {
                    var param = null;
                    if (self.differentiate === 2) {
                        param = 'meeting';
                    }
                    //点击指挥\会议列表下拉框时，默认进行查询操作，实时显示已开启、停止的指挥\会议
                    self.refreshCommand(param, function () {
                        for (var i = 0; i < self.command.data.length; i++) {
                            if (self.command.data[i].id == v) {
                                currentGroup = {
                                    id: self.command.data[i].id,
                                    name: self.command.data[i].name,
                                    status: "start",
                                    type: self.command.data[i].type,
                                    creator: JSON.parse(self.command.data[i].param).creator,
                                    entered: JSON.parse(self.command.data[i].param).entered
                                };
                                if (JSON.parse(self.command.data[i].param).entered) {
                                    if (self.differentiate === 1) {
                                        self.buttons.exitCommand = true;
                                        self.buttons.enterCommand = false;
                                    } else {
                                        self.buttons.exitMeet = true;
                                        self.buttons.enterMeet = false;
                                    }
                                }
                                break;
                            }
                        }
                        if (currentGroup.type.toLowerCase() === 'command') {
                            ajax.post('/command/basic/query/members/list', {id: currentGroup.id}, function (data) {
                                data.type = 'command';
                                data.select = [];
                                var tree = [];
                                data.members.forEach(function (value) {
                                    tree = tree.concat(value.children);
                                });
                                //去掉根目录那层
                                if (self.differentiate === 1) {
                                    self.group.tree = tree;
                                    self.group.current = data;
                                } else {
                                    self.meet.tree = tree;
                                    self.meet.current = data;
                                }
                                if (self.user.id == data.creator) {
                                    self.buttons.addMember = true;
                                    self.buttons.removeMember = true;
                                    self.buttons.commandReminder = true;
                                    if (data.status === 'remind') {
                                        self.buttons.isRemind = true;
                                    } else {
                                        self.buttons.isRemind = false;
                                    }
                                    self.buttons.cooperativeCommand = true;
                                    self.buttons.cancelCooperative = true;
                                    self.buttons.commandForward = true;
                                    self.buttons.silenceUp = true;
                                    self.buttons.silenceDown = true;
                                    self.buttons.sendMessage = true;
                                    self.buttons.commandRecord = true;
                                    self.buttons.isRecord = data.isRecord;
                                    self.buttons.commandRestart = true;
                                    if (data.status === 'pause') {
                                        self.buttons.commandPause = false;
                                        self.buttons.isPause = true;
                                    } else {
                                        self.buttons.commandPause = true;
                                        self.buttons.isPause = false;
                                    }
                                } else {
                                    self.buttons.addMember = false;
                                    self.buttons.removeMember = false;
                                    self.buttons.commandReminder = false;
                                    self.buttons.cooperativeCommand = true;
                                    self.buttons.cancelCooperative = true;
                                    self.buttons.commandForward = false;
                                    self.buttons.silenceUp = false;
                                    self.buttons.silenceDown = false;
                                    self.buttons.sendMessage = false;
                                    self.buttons.commandRecord = false;
                                    self.buttons.isRecord = false;
                                    self.buttons.commandPause = false;
                                    self.buttons.commandRestart = false;
                                    self.buttons.isPause = false;
                                }
                                if (data.status === 'remind') {
                                    var finded = false;
                                    for (var i = 0; i < self.group.remindIds.length; i++) {
                                        if (self.group.remindIds[i] == data.id) {
                                            finded = true;
                                            break;
                                        }
                                    }
                                    if (!finded) {
                                        self.group.remindIds.push(data.id);
                                    }
                                }
                            }, null, [403, 404, 409, 500, 408]);
                        }
                    })
                }
            },
            //指挥工作台树形菜单的复选框
            onCommandUserCheckChange: function (data, type) {
                var self = this;
                if (type === '指挥') {
                    for (var i = 0; i < self.group.current.select.length; i++) {
                        if (self.group.current.select[i] === data) {
                            self.group.current.select.splice(i, 1);
                            return;
                        }
                    }
                    self.group.current.select.push(data);
                } else {
                    for (var i = 0; i < self.meet.current.select.length; i++) {
                        if (self.meet.current.select[i] === data) {
                            self.meet.current.select.splice(i, 1);
                            return;
                        }
                    }
                    self.meet.current.select.push(data);
                }
            },

            //-------指挥工作台的按钮底部按钮图标 start-------
            //添加成员
            addCommandMember: function () {
                var self = this;
                var id = '';
                var type = '';
                if (!self.buttons.addMember) return;
                if (self.differentiate === 1) {
                    id = self.group.current.id;
                    type = 'command';
                } else {
                    id = self.meet.current.id;
                    type = 'meeting';
                }
                self.qt.window('/router/zk/leader/add/member', {
                    id: id,
                    type: type
                }, {
                    width: 1000,
                    height: 600
                });
            },
            //强退成员
            removeCommandMember: function () {
                var self = this;
                if (!self.buttons.removeMember) return;
                if (self.differentiate === 1) {
                    if (self.group.current.select.length <= 0) {
                        self.qt.warning('提示信息：您未选择任何成员');
                        return;
                    }
                    self.qt.confirm('提示信息', '当前强退' + self.group.current.select.length + '个成员', '确定', function () {
                        var members = [];
                        for (var i = 0; i < self.group.current.select.length; i++) {
                            members.push(self.group.current.select[i].id);
                        }
                        ajax.post('/command/basic/remove/members', {
                            id: self.group.current.id,
                            members: $.toJSON(members)
                        }, function (data) {
                            if (data) {
                                self.qt.invoke('commandMemberDelete', data);
                                self.currentGroupChange(self.group.current.id);
                            }
                        }, null, [403]);
                    });
                } else {
                    if (self.meet.current.select.length <= 0) {
                        self.qt.warning('提示信息：您未选择任何成员');
                        return;
                    }
                    self.qt.confirm('提示信息', '当前强退' + self.meet.current.select.length + '个成员', '确定', function () {
                        var members = [];
                        for (var i = 0; i < self.meet.current.select.length; i++) {
                            members.push(self.meet.current.select[i].id);
                        }
                        ajax.post('/command/basic/remove/members', {
                            id: self.meet.current.id,
                            members: $.toJSON(members)
                        }, function (data) {
                            if (data) {
                                self.qt.invoke('commandMemberDelete', data);
                                self.currentGroupChange(self.meet.current.id);
                            }
                        }, null, [403]);
                    });
                }
            },
            //协同指挥/会议
            cooperativeCommand: function (name) {
                var self = this;
                if (!self.buttons.cooperativeCommand) return;
                if (self.differentiate === 1) {
                    self.qt.window('/router/zk/leader/cooperation', {
                        id: self.group.current.id,
                        type: self.group.current.type,
                        name: name,
                        page: 'add'
                    }, {width: 980, height: 600});
                }
            },
            //取消协同指挥/会议
            cancelCooperativeCommand: function (name) {
                var self = this;
                if (!self.buttons.cancelCooperative) return;
                if (self.differentiate === 1) {
                    self.qt.window('/router/zk/leader/cooperation', {
                        id: self.group.current.id,
                        type: self.group.current.type,
                        name: name,
                        page: 'cancel'
                    }, {width: 980, height: 600});
                }
            },
            //会议成员
            commandMembers: function () {
                var self = this;
                if (!self.buttons.cooperativeCommand) return;
                self.qt.window('/router/zk/leader/cooperation', {
                    id: self.meet.current.id,
                    type: self.meet.current.type,
                    page: 'add',
                    tag: 'members'
                }, {
                    width: 980,
                    height: 600
                });
            },
            //指挥/会议转发
            commandForward: function (name) {
                var self = this;
                var id = '';
                if (!self.buttons.commandForward) return;
                if (self.differentiate === 1) {
                    id = self.group.current.id;
                } else {
                    id = self.meet.current.id;
                }
                self.qt.window('/router/zk/leader/forward', {id: id, name: name}, {
                    width: 1024,
                    height: 768,
                    title: '指挥转发'
                });
            },
            //对上静默
            silenceUp: function () {
                var self = this;
                var id = '';
                if (!self.buttons.silenceUp) return;
                if (self.differentiate === 1) {
                    id = self.group.current.id;
                } else {
                    id = self.meet.current.id;
                }
                if (!self.buttons.silenceUpStart) {
                    ajax.post('/command/basic/silence/up/start', {id: id}, function () {
                        self.buttons.silenceUpStart = true;
                        self.qt.warning('提示消息：操作成功');
                    });
                } else {
                    ajax.post('/command/basic/silence/up/stop', {id: id}, function () {
                        self.buttons.silenceUpStart = false;
                        self.qt.warning('提示消息：操作成功');
                    });
                }
            },
            //对下静默
            silenceDown: function () {
                var self = this;
                var id = '';
                if (!self.buttons.silenceDown) return;
                if (self.differentiate === 1) {
                    id = self.group.current.id;
                } else {
                    id = self.meet.current.id;
                }
                if (!self.buttons.silenceDownStart) {
                    ajax.post('/command/basic/silence/down/start', {id: id}, function () {
                        self.buttons.silenceDownStart = true;
                        self.qt.warning('提示消息：操作成功');
                    });
                } else {
                    ajax.post('/command/basic/silence/down/stop', {id: id}, function () {
                        self.buttons.silenceDownStart = false;
                        self.qt.warning('提示消息：操作成功');
                    });
                }
            },
            //发送通知
            sendMessage: function () {
                var self = this;
                var id = '';
                if (!self.buttons.sendMessage) return;
                if (self.differentiate === 1) {
                    id = self.group.current.id;
                } else {
                    id = self.meet.current.id;
                }
                self.qt.window('/router/zk/leader/send/message', {id: id}, {
                    width: 1224,
                    height: 768
                });
            },
            //指挥录制
            commandRecord: function () {
                var self = this;
                var id = '';
                if (self.differentiate === 1) {
                    id = self.group.current.id;
                } else {
                    id = self.meet.current.id;
                }
                if (self.buttons.isRecord) {
                    ajax.post('/command/record/stop', {id: id}, function () {
                        self.buttons.isRecord = false;
                        self.qt.warning('业务提示：录制任务操作成功');
                    });
                } else {
                    ajax.post('/command/record/start', {id: id}, function () {
                        self.buttons.isRecord = true;
                        self.qt.warning('业务提示：录制任务操作成功');
                    });
                }
            },
            //指挥提醒
            commandRemind: function () {
                var self = this;
                if (!self.buttons.commandReminder) return;
                if (self.differentiate === 1) {
                    if (self.buttons.isRemind) {
                        ajax.post('/command/basic/remind/stop', {id: self.group.current.id}, function (data) {
                            self.qt.warning('提示消息：操作成功');
                            self.buttons.isRemind = false;
                            for (var i = 0; i < self.group.remindIds.length; i++) {
                                if (self.group.remindIds[i] == self.group.current.id) {
                                    self.group.remindIds.splice(i, 1);
                                    break;
                                }
                            }
                            if (data.businessId == self.group.current.id) {
                                self.group.current.status = data.status;
                            }
                            self.qt.invoke('commandRemindStop', $.toJSON(data));
                        });
                    } else {
                        ajax.post('/command/basic/remind', {id: self.group.current.id}, function (data) {
                            self.qt.warning('提示消息：操作成功');
                            self.buttons.isRemind = true;
                            var finded = false;
                            for (var i = 0; i < self.group.remindIds.length; i++) {
                                if (self.group.remindIds[i] == self.group.current.id) {
                                    finded = true;
                                    break;
                                }
                            }
                            if (!finded) {
                                self.group.remindIds.push(self.group.current.id);
                            }
                            if (data.businessId == self.group.current.id) {
                                self.group.current.status = data.status;
                            }
                            self.qt.invoke('commandRemind', $.toJSON(data));
                        });
                    }
                } else {
                    if (self.buttons.isRemind) {
                        ajax.post('/command/basic/remind/stop', {id: self.meet.current.id}, function (data) {
                            self.qt.warning('提示消息：操作成功');
                            self.buttons.isRemind = false;
                            for (var i = 0; i < self.meet.remindIds.length; i++) {
                                if (self.meet.remindIds[i] == self.meet.current.id) {
                                    self.meet.remindIds.splice(i, 1);
                                    break;
                                }
                            }
                            if (data.businessId == self.meet.current.id) {
                                self.meet.current.status = data.status;
                            }
                            self.qt.invoke('commandRemindStop', $.toJSON(data));
                        });
                    } else {
                        ajax.post('/command/basic/remind', {id: self.meet.current.id}, function (data) {
                            self.qt.warning('提示消息：操作成功');
                            self.buttons.isRemind = true;
                            var finded = false;
                            for (var i = 0; i < self.meet.remindIds.length; i++) {
                                if (self.meet.remindIds[i] == self.meet.current.id) {
                                    finded = true;
                                    break;
                                }
                            }
                            if (!finded) {
                                self.meet.remindIds.push(self.meet.current.id);
                            }
                            if (data.businessId == self.meet.current.id) {
                                self.meet.current.status = data.status;
                            }
                            self.qt.invoke('commandRemind', $.toJSON(data));
                        });
                    }
                }
            },
            //开始指挥
            startCommand: function () {
                var self = this;
                if (self.differentiate === 1) {
                    if (self.group.current.status === 'pause') {
                        ajax.post('/command/basic/pause/recover', {
                            id: self.group.current.id
                        }, function () {
                            self.group.current.status = 'start';
                            for (var i = 0; i < self.group.entered.length; i++) {
                                if (self.group.entered[i].id == self.group.current.id) {
                                    self.group.entered[i].status = 'start';
                                    break;
                                }
                            }
                            self.buttons.commandPause = true;
                            self.buttons.isPause = false;
                        });
                    } else {
                        ajax.post('/command/basic/start', {
                            id: self.group.current.id
                        }, function (data) {
                            self.group.current.status = 'start';
                            var splits = data.splits;
                            if (splits && splits.length > 0) {
                                // 主席web通知qt进入指挥
                                self.qt.invoke('groupMembers', $.toJSON(splits));
                            }
                        });
                    }
                } else {
                    self.qt.set('currentGroupId', self.meet.current.id);
                    if (self.meet.current.status === 'pause') {
                        ajax.post('/command/basic/pause/recover', {
                            id: self.meet.current.id
                        }, function () {
                            self.meet.current.status = 'start';
                            for (var i = 0; i < self.meet.entered.length; i++) {
                                if (self.meet.entered[i].id == self.group.current.id) {
                                    self.meet.entered[i].status = 'start';
                                    break;
                                }
                            }
                            self.buttons.commandPause = true;
                            self.buttons.isPause = false;
                        });
                    } else {
                        ajax.post('/command/basic/start', {
                            id: self.meet.current.id
                        }, function (data) {
                            self.meet.current.status = 'start';
                            var splits = data.splits;
                            if (splits && splits.length > 0) {
                                // 主席web通知qt进入指挥
                                self.qt.invoke('groupMembers', $.toJSON(splits));
                            }
                        });
                    }
                }
            },
            //暂停指挥
            pauseCommand: function () {
                var self = this;
                if (self.differentiate === 1) {
                    ajax.post('/command/basic/pause', {
                        id: self.group.current.id
                    }, function (data) {
                        self.group.current.status = 'pause';
                        for (var i = 0; i < self.group.entered.length; i++) {
                            if (self.group.entered[i].id == self.group.current.id) {
                                self.group.entered[i].status = 'pause';
                                break;
                            }
                        }
                        self.buttons.commandPause = false;
                        self.buttons.isPause = true;
                    });
                } else {
                    ajax.post('/command/basic/pause', {
                        id: self.meet.current.id
                    }, function () {
                        self.meet.current.status = 'pause';
                        for (var i = 0; i < self.meet.entered.length; i++) {
                            if (self.meet.entered[i].id == self.meet.current.id) {
                                self.meet.entered[i].status = 'pause';
                                break;
                            }
                        }
                        self.buttons.commandPause = false;
                        self.buttons.isPause = true;
                    });
                }
            },
            //退出指挥
            exitCommand: function (name) {
                var self = this;
                if (name === '会议') {
                    if (self.user.id == self.meet.current.creator) {
                        ajax.post('/command/basic/stop', {
                            id: self.meet.currentId
                        }, function (data) {
                            if (self.meet.current.creator != self.user.id) {
                                self.meet.entered = self.meet.entered.filter(function (value) {
                                    return value.id != self.meet.current.id;
                                });
                                if (self.meet.entered.length > 0) {
                                    self.meet.current = self.meet.entered[0];
                                    self.meet.currentId = self.meet.entered[0].id;
                                    self.currentGroupChange(self.meet.currentId);
                                } else {
                                    self.meet.current = '';
                                    self.meet.currentId = '';
                                }
                            } else {
                                self.meet.current.status = 'stop';
                            }
                            self.qt.success('停止会议成功');
                            self.qt.invoke('commandExit', $.toJSON(data));
                            self.refreshCommand("meeting");
                        });
                    } else {
                        ajax.post('/command/basic/exit/apply', {
                            id: self.meet.current.id
                        }, function () {
                            self.qt.success('已向主席发出退出会议申请');
                            self.agreeExitCommand = 2;
                        });
                    }
                } else {
                    if (self.user.id == self.group.current.creator) {
                        ajax.post('/command/basic/stop', {
                            id: self.group.currentId
                        }, function (data) {
                            if (self.group.current.creator != self.user.id) {
                                self.group.entered = self.group.entered.filter(function (value) {
                                    return value.id != self.group.current.id;
                                });
                                if (self.group.entered.length > 0) {
                                    self.group.current = self.group.entered[0];
                                    self.group.currentId = self.group.entered[0].id;
                                    self.currentGroupChange(self.group.currentId);
                                } else {
                                    self.group.current = '';
                                    self.group.currentId = '';
                                }
                            } else {
                                self.group.current.status = 'stop';
                            }
                            self.qt.invoke('commandExit', $.toJSON(data));
                            self.refreshCommand();
                            self.qt.success('停止指挥成功');
                        })
                    } else {
                        ajax.post('/command/basic/exit/apply', {
                            id: self.group.current.id
                        }, function () {
                            self.qt.success('已向主席发出退出指挥申请');
                            self.agreeExitCommand = 1;
                        });
                    }
                }
            },
            //按钮操作完后 取消组织结构的勾选
            cancelChoose: function () {
                var self = this;
                self.institution.select.forEach(function (value) {
                    value.checked = false;
                });
                self.institution.select = [];
            },
            //发布字幕
            publish: function () {
                this.qt.window('/router//zk/leader/subtitle/layer', null, {width: '85%', height: '93%'});
            },

            //   ----------第三个tab相关的-----------------
            //删除会议
            removeMyCommand: function () {
                var self = this;
                var id = self.meet.currentId;
                ajax.post('/command/basic/remove', {ids: $.toJSON([id])}, function () {
                    // 重置currentId
                    self.refreshCommand('meeting', function () {
                        self.meet.currentId = self.command.data[0].id;
                        self.meet.current = {
                            creator: JSON.parse(self.command.data[0].param).creator,
                            id: self.command.data[0].id,
                            name: self.command.data[0].name,
                            status: self.command.data[0].status,
                            type: self.command.data[0].type
                        };
                        self.currentGroupChange(self.meet.currentId);
                    });
                    self.qt.success('删除成功');
                });
            },
            //成员进入会议
            enterMyCommand: function () {
                var self = this;
                var id = self.meet.currentId;
                ajax.post('/command/basic/enter', {ids: $.toJSON([id])}, function (data) {
                    //进入他人会议
                    self.qt.linkedWebview('rightBar', {id: 'currentGroupChange', params: $.toJSON(data)});
                    self.buttons.exitMeet = true;
                    self.buttons.enterMeet = false;
                });
            },
            //安排会议
            planCommand: function () {
                var self = this;
                self.qt.window('/router/zk/leader/dialog/prepare/group', {}, {
                    width: 1366,
                    height: 700
                });
            },
            //申请发言
            applySpeak: function () {
                var self = this;
                if (!self.buttons.isSpeaking) {
                    ajax.post('/command/meeting/speak/apply', {id: self.meet.current.id}, function () {
                        self.qt.success('已向主席发出申请');
                    })
                }
            },
            //成员自己停止发言
            stopSpeak: function () {
                var self = this;
                if (self.buttons.isSpeaking) {
                    ajax.post('/command/meeting/speak/stop/by/member', {id: self.meet.current.id}, function (data) {
                        self.qt.success('您停止了自己的发言');
                        self.buttons.isSpeaking = false;
                    })
                }
            },
            //指定发言
            assignSpeak: function () {
                var self = this;
                var ids = [];
                if (!self.meet.current.select.length) {
                    self.qt.warning('提示信息：您没有勾选任何用户');
                    return;
                }
                self.meet.current.select.forEach(function (value) {
                    ids.push(value.id);
                });
                ajax.post('/command/meeting/speak/appoint', {
                    id: self.meet.current.id,
                    userIds: $.toJSON(ids)
                }, function (data) {
                    self.qt.success('指定发言操作成功');
                });

            },
            //主席停止指定发言
            stopAssignSpeak: function () {
                var self = this;
                var ids = [];
                if (!self.meet.current.select.length) {
                    self.qt.warning('提示信息：您没有勾选任何用户');
                    return;
                }
                self.meet.current.select.forEach(function (value) {
                    ids.push(value.id);
                });
                ajax.post('/command/meeting/speak/stop/by/chairman', {
                    id: self.meet.current.id,
                    userIds: $.toJSON(ids)
                }, function () {
                    self.qt.success('停止指定发言操作成功');
                    self.meet.current.select.forEach(function (value) {
                        value.checked = false;
                    });
                    self.meet.current.select = [];
                });
            },
            //全员讨论
            allDiscuss: function () {
                var self = this;
                if (!self.buttons.discussion) { //讨论
                    ajax.post('/command/meeting/discuss/start', {id: self.meet.current.id}, function () {
                        self.qt.success('全员讨论操作成功');
                        self.buttons.discussion = true;
                    })
                } else { //停止讨论
                    ajax.post('/command/meeting/discuss/stop', {id: self.meet.current.id}, function () {
                        self.qt.success('停止全员讨论操作成功');
                        self.buttons.discussion = false;
                    })
                }
            }
        },
        computed: {
            groupCurrent: function () {
                var self = this;
                if (self.differentiate === 1) {
                    return self.group.current;
                } else {
                    return self.meet.current;
                }
            }
        },
        watch: {
            filterText: function (val) {
                this.$refs.tree.filter(val);
            },
            currentTab: function () {
                var self = this;
                if (self.currentTab == -1) return;
                this.switchStatus1 = false;
                this.switchStatus2 = true;
                this.switchStatus3 = false;
                $('.triggerLi1').removeClass('focusLight');
                $('.triggerLi2').removeClass('focusLight');
                $('.highLight').css('display', 'none');
                if (self.currentTab == 0) {
                    //组织结构
                    self.refreshInstitution();
                    self.refreshInstitutionButtonAction();
                } else if (self.currentTab == 2) {
                    //文件资源
                    self.refreshFile();
                    self.refreshFileButtonAction();
                } else if (self.currentTab == 1) {
                    //设备资源
                    self.refreshDevice();
                    self.refreshDeviceButtonAction();
                } else if (self.currentTab == 3) {
                    //录像回放
                    self.refreshRecord();
                    self.refreshRecordButtonAction();
                }
                this.$nextTick(function () {
                    //在下次 DOM 更新循环结束之后执行这个回调。在修改数据之后立即使用这个方法，获取更新后的DOM.
                    self.footerHeight = window.getComputedStyle(self.$refs.footer).height;
                    self.firstMenuHeight = window.getComputedStyle(self.$refs.secondMenu).height;
                    self.contentStyleObj.height = (parseInt(self.firstMenuHeight) - parseInt(self.footerHeight)) + 'px';
                    $('.scrollTab').css('height', self.contentStyleObj.height);
                })
            },
            groupCurrent: function (v) {
                var self = this;
                if (self.user.id == v.creator) {
                    self.group.isChairman = true;
                    self.buttons.addMember = true;
                    self.buttons.removeMember = true;
                    self.buttons.commandReminder = true;
                    if (v.status === 'remind') {
                        self.buttons.isRemind = true;
                    } else {
                        self.buttons.isRemind = false;
                    }
                    self.buttons.cooperativeCommand = true;
                    self.buttons.cancelCooperative = true;
                    self.buttons.commandForward = true;
                    self.buttons.silenceUp = false;
                    self.buttons.silenceDown = true;
                    self.buttons.sendMessage = true;
                    self.buttons.commandRecord = true;
                    self.buttons.commandRestart = true;
                    if (v.status === 'pause') {
                        self.buttons.commandPause = false;
                        self.buttons.isPause = true;
                    } else {
                        self.buttons.commandPause = true;
                        self.buttons.isPause = false;
                    }
                    self.buttons.speak = false;
                    self.buttons.meetBtn = true;
                } else {
                    self.group.isChairman = false;
                    self.buttons.addMember = false;
                    self.buttons.removeMember = false;
                    self.buttons.commandReminder = false;
                    self.buttons.cooperativeCommand = false;
                    self.buttons.cancelCooperative = false;
                    self.buttons.commandForward = false;
                    self.buttons.silenceUp = true;
                    self.buttons.silenceDown = false;
                    self.buttons.sendMessage = false;
                    self.buttons.commandRecord = false;
                    self.buttons.commandPause = false;
                    self.buttons.commandRestart = false;
                    self.buttons.isPause = false;
                    self.buttons.speak = true;
                    self.buttons.meetBtn = false;
                }
            },
            // 监听浏览器窗口宽度变化，这个值就会变化,
            firstMenuHeight: function (val) {
                // 这个tableHeight就是最后的height
                this.contentStyleObj.height = (parseInt(val) - parseInt(this.footerHeight)) + 'px';
                $('.scrollTab').css('height', this.contentStyleObj.height);
            }
        },
        mounted: function () {
            var self = this;
            // closest() 完成事件委托。当被最接近的列表元素或其子后代元素被点击时
            $(document).on('mousedown.contextMenu', function (e) {
                var $target = $(e.target);
                if (!($target.is('.context-menu') || $target.closest('.context-menu')[0])) {
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

                self.refreshCommand(null, function () {
                    self.refreshEnteredGroups();
                });
                self.refreshInstitutionButtonAction();

                //动态获取firsetMenu和footer的高度
                window.onresize = function () {
                    self.firstMenuHeight = window.getComputedStyle(self.$refs.secondMenu).height;
                    self.footerHeight = window.getComputedStyle(self.$refs.footer).height;
                };

                //给scrollTab 设置初始高度
                self.contentStyleObj.height = Math.abs((parseInt(window.getComputedStyle(self.$refs.secondMenu).height) - parseInt(window.getComputedStyle(self.$refs.footer).height))) + 'px';

                self.qt.get(['user'], function (variables) {
                    self.user = variables.user ? $.parseJSON(variables.user) : {};
                });

                //从新建会议页面来，到这个页面时改变
                self.qt.on('showData', function (e) {
                    var group = $.parseJSON(e.params)[0];
                    self.switchStatus1 = false;
                    self.switchStatus2 = false;
                    self.switchStatus3 = false;
                    group.type = 'command';
                    self.currentGroupChange(group);
                    self.qt.set('currentGroupId', group.id);
                });

                //监听到添加成员，刷新树
                self.qt.on('commandMemberAdd', function (e) {
                    var type = e.type;
                    var x = e.params;
                    self.qt.invoke('groupMembers', x);
                    if (type === 'command') {
                        self.currentGroupChange(self.group.current.id);
                    } else if (type === 'meeting') {
                        self.currentGroupChange(self.meet.current.id);
                    }
                });

                // 监听退成员时
                self.qt.on('reduceMembers', function (e) {
                    e = e.params.params;
                    var memberIds = e.memberIds;
                    var beDeleted = false;
                    if (memberIds && memberIds.length > 0) {
                        for (var i = 0; i < memberIds.length; i++) {
                            if (memberIds[i] == self.user.id) {
                                beDeleted = true;
                                break;
                            }
                        }
                    }
                    if (beDeleted) {
                        for (var i = 0; i < self.group.entered.length; i++) {
                            if (self.group.entered[i].id == e.businessId) {
                                self.group.entered.splice(i, 1);
                                break;
                            }
                        }
                        if (self.group.currentId == e.businessId) {
                            if (self.group.entered.length > 0) {
                                self.group.currentId = self.group.entered[0].id;
                                self.group.current = self.group.entered[0];
                                self.currentGroupChange(self.group.currentId);
                            } else {
                                self.group.currentId = '';
                                self.group.current = '';
                            }
                        } else if (self.meet.currentId == e.businessId) {
                            if (self.group.entered.length > 0) {
                                self.meet.currentId = self.meet.entered[0].id;
                                self.meet.current = self.meet.entered[0];
                                self.currentGroupChange(self.meet.currentId);
                            } else {
                                self.meet.currentId = '';
                                self.meet.current = '';
                            }
                        }
                    }
                    if (e.splits && e.splits.length > 0) {
                        self.qt.invoke('commandMemberDelete', e.splits);
                    }
                });

                self.qt.on('responseModeChange', function (e) {
                    self.settings.responseMode = e.params;
                });

                self.qt.on('sendAnswerModeChange', function (e) {
                    self.settings.sendAnswerMode = e.params;
                });

                //成员同意 进入指挥
                self.qt.on('linkEnterCommand', function (e) {
                    //加参数是为了区分进会时 ，不影响指挥的代码
                    self.switchStatus1 = true;
                    self.switchStatus2 = false;
                    self.switchStatus3 = false;
                    self.differentiate = 1;
                    $('.triggerLi1').addClass('focusLight');
                    $('.triggerLi2').removeClass('focusLight');
                    $('.highLight').css('left', '7px');
                    self.refreshCommand(null, function () {
                        self.enterCommand([e.params]);
                    })
                });

                //成员同意进入会议
                self.qt.on('linkEnterMeeting', function (e) {
                    self.switchStatus1 = false;
                    self.switchStatus2 = false;
                    self.switchStatus3 = true;
                    self.differentiate = 2;
                    $('.triggerLi1').removeClass('focusLight');
                    $('.triggerLi2').addClass('focusLight');
                    $('.highLight').css('left', '167px');
                    //加参数是为了区分进会时 ，不影响指挥的代码
                    self.refreshCommand('meet', function () {
                        self.enterCommand([e.params], 'meet');
                    })
                });

                // 监听有成员进入会议
                self.qt.on('currentGroupChange', function (e) {
                    var group = $.parseJSON(e.params)[0];
                    group.type = 'command';
                    self.currentGroupChange(group);
                    self.qt.set('currentGroupId', group.id);
                });

                //websocket 停止指挥  成员监听到主席停止指挥，关闭成员播放器
                self.qt.on('usercommandStop', function (e) {
                    e = e.params;
                    self.qt.warning(e.businessInfo);
                    for (var i = 0; i < self.group.entered.length; i++) {
                        if (self.group.entered[i].id == e.businessId) {
                            self.group.entered.splice(i, 1);
                            break;
                        }
                    }
                    if (self.group.currentId == e.businessId) {
                        if (self.group.entered.length > 0) {
                            self.group.current = self.group.entered[0];
                            self.group.currentId = self.group.entered[0].id;
                            self.currentGroupChange(self.group.currentId);
                        } else {
                            self.group.current = '';
                            self.group.currentId = '';
                        }
                    } else if (self.meet.currentId == e.businessId) {
                        if (self.meet.entered.length > 0) {
                            self.meet.current = self.meet.entered[0];
                            self.meet.currentId = self.meet.entered[0].id;
                            self.currentGroupChange(self.meet.currentId);
                        } else {
                            self.meet.current = '';
                            self.meet.currentId = '';
                        }
                    }
                    self.refreshCommand("meeting");
                    self.qt.invoke('commandExit', $.toJSON(e.splits));
                });

                //监听到成员退出
                self.qt.on('memberOffline', function (e) {
                    self.currentGroupChange(e.params);
                });

                // 授权协同会议
                self.qt.on('refreshCurrentGroupMembers', function (e) {
                    self.currentGroupChange(self.group.current);
                });

                //成员收到指挥提醒，播放音乐
                self.qt.on('commandRemind', function (e) {
                    e = e.params;
                    var finded = false;
                    for (var i = 0; i < self.group.remindIds.length; i++) {
                        if (self.group.remindIds[i] == e.businessId) {
                            finded = true;
                            break;
                        }
                    }
                    if (!finded) {
                        self.group.remindIds.push(e.businessId);
                    }
                    if (self.group.current.id == e.businessId) {
                        self.group.current.status = 'remind';
                    }
                    self.qt.info(e.businessInfo);
                    self.qt.invoke('commandRemind', $.toJSON(e));
                });

                //成员收到指挥提醒停止，停止播放音乐
                self.qt.on('commandRemindStop', function (e) {
                    e = e.params;
                    for (var i = 0; i < self.group.remindIds.length; i++) {
                        if (self.group.remindIds[i] == e.businessId) {
                            self.group.remindIds.splice(i, 1);
                            break;
                        }
                    }
                    if (self.group.current.id == e.businessId) {
                        self.group.current.status = e.status;
                    }
                    self.qt.info(e.businessInfo);
                    self.qt.invoke('commandRemindStop', $.toJSON(e));
                });

                //通知观看发言和讨论
                self.qt.on('speakStart', function (e) {
                    var e = e.params;
                    self.qt.info(e.businessInfo);
                    if (e.splits && e.splits.length > 0) {
                        self.qt.invoke('speakStart', e.splits);
                    }
                    self.buttons.isSpeaking = false;
                });
                //通知停止发言/停止讨论
                self.qt.on('speakStop', function (e) {
                    var e = e.params;
                    self.qt.warning(e.businessInfo);
                    if (e.splits && e.splits.length > 0) {
                        self.qt.invoke('speakStop', e.splits);
                    }
                    self.buttons.isSpeaking = false;
                });

                //通知申请人发言被同意
                self.qt.on('speakApplyAgree', function (e) {
                    var e = e.params;
                    self.qt.info(e.businessInfo);
                    self.buttons.isSpeaking = true;
                });
                //通知申请人发言被拒绝
                self.qt.on('speakApplyDisagree', function (e) {
                    var e = e.params;
                    self.qt.info(e.businessInfo);
                    self.buttons.isSpeaking = false;
                });

                //通知申请人退出指挥/会议被同意
                self.qt.on('applyExitAgree', function (e) {
                    var e = e.params;
                    self.qt.info(e.businessInfo);
                    if (e.splits && e.splits.length > 0) {
                        self.qt.invoke('commandExit', e.splits);
                        if (self.agreeExitCommand === 1) {
                            self.buttons.exitCommand = false;
                            self.buttons.enterCommand = true;
                            self.group.entered = self.group.entered.filter(function (value) {
                                return value.id != self.group.current.id;
                            });
                            if (self.group.entered.length > 0) {
                                self.group.currentId = self.group.entered[0].id;
                                self.group.current = self.group.entered[0];
                                self.currentGroupChange(self.group.currentId);
                            } else {
                                self.group.currentId = '';
                                self.group.current = '';
                            }
                        } else {
                            self.buttons.exitMeet = false;
                            self.buttons.enterMeet = true;
                            for (var i = 0; i < self.meet.entered.length; i++) {
                                self.meet.entered = self.meet.entered.filter(function (value) {
                                    return value.id != self.meet.current.id;
                                });
                                if (self.meet.entered.length > 0) {
                                    self.meet.currentId = self.meet.entered[0].id;
                                    self.meet.current = self.meet.entered[0];
                                    setTimeout(function () {
                                        self.currentGroupChange(self.meet.currentId);
                                    }, 500)
                                } else {
                                    self.meet.currentId = '';
                                    self.meet.current = '';
                                }
                            }
                        }
                    }
                });

                //通知申请人退出/会议被拒绝
                self.qt.on('applyExitDisagree', function (e) {
                    var e = e.params;
                    self.qt.info(e.businessInfo);
                    self.agreeExitCommand = 0;
                    if (self.differentiate === 1) {
                        self.buttons.exitCommand = true;
                        self.buttons.enterCommand = false;
                    } else {
                        self.buttons.exitMeet = true;
                        self.buttons.enterMeet = false;
                    }
                });

                //通知 主席 同意/拒绝成员发言申请
                self.qt.on('agreeSpeakApply', function (e) {
                    var e = e.params;
                    var ids = e.businessId.split('-'); //返回的格式是:会议id-成员id
                    self.qt.confirm('业务提示', e.businessInfo, '拒绝', '同意', function () {
                        ajax.post('/command/meeting/speak/apply/disagree', {
                            id: ids[0],
                            userIds: $.toJSON([ids[1]])
                        }, null);
                    }, function () {
                        ajax.post('/command/meeting/speak/apply/agree', {
                            id: ids[0],
                            userIds: $.toJSON([ids[1]])
                        }, null);
                    });
                });

                //通知主席  同意/拒绝成员退出申请
                self.qt.on('agreeExitApply', function (e) {
                    var e = e.params;
                    var ids = e.businessId.split('-');
                    self.qt.confirm('业务提示', e.businessInfo, '拒绝', '同意', function () {
                        ajax.post('/command/basic/exit/apply/disagree', {
                            id: ids[0],
                            userIds: $.toJSON([ids[1]])
                        }, null);
                    }, function () {
                        //主席界面销毁成员播放器
                        ajax.post('/command/basic/exit/apply/agree', {
                                id: ids[0],
                                userIds: $.toJSON([ids[1]])
                            }, function (data) {
                                self.currentGroupChange(self.meet.current.id);
                                if (data && data.length > 0) {
                                    self.qt.invoke('commandMemberDelete', data);
                                }
                            }, null, [403]
                        );
                    });
                });

                //监听换一批人员
                self.qt.on('replacePeople', function () {
                    var id = self.differentiate === 1 ? self.group.currentId : self.meet.currentId;
                    ajax.post('/command/basic/roll/all/vod/members', {id: id}, function (data) {
                        var splits = data.splits;
                        if (splits && splits.length > 0) {
                            // 主席web通知qt进入指挥
                            self.qt.invoke('groupMembers', $.toJSON(splits));
                        }
                    })
                });

                //开始指挥，不需要成员同意自动进入指挥
                self.qt.on('autoCommandStart', function (e) {
                    //加参数是为了区分进会时 ，不影响指挥的代码
                    self.switchStatus1 = true;
                    self.switchStatus2 = false;
                    self.switchStatus3 = false;
                    self.differentiate = 1;
                    $('.triggerLi1').addClass('focusLight');
                    $('.triggerLi2').removeClass('focusLight');
                    $('.highLight').css('left', '7px');
                    self.refreshCommand(null, function () {
                        self.autoEnterCommand(e.params);
                    });
                    self.qt.success(e.params.businessInfo);
                });

                //开始会议，不需要成员同意自动进入指挥
                self.qt.on('autoMeetingStart', function (e) {
                    self.switchStatus1 = false;
                    self.switchStatus2 = false;
                    self.switchStatus3 = true;
                    self.differentiate = 2;
                    $('.triggerLi1').removeClass('focusLight');
                    $('.triggerLi2').addClass('focusLight');
                    $('.highLight').css('left', '167px');
                    //加参数是为了区分进会时 ，不影响指挥的代码
                    self.refreshCommand('meet', function () {
                        self.autoEnterCommand(e.params, 'meet');
                    });
                    self.qt.success(e.params.businessInfo);
                });

                //下载录制文件
                self.qt.on('downloadFile', function (e) {
                    self.qt.invoke('slotDownload', e.param);
                })
            });
        }
    });
    return Vue;
});