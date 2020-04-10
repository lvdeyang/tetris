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
                switchStatus1: true, //树1的显示状态
                switchStatus2: false, //树2的显示状态
                switchStatus3: false, //树3的显示状态
                people: false, //会议人员列表
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
                    enterCommand: true,
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
                    //退出指挥
                    exitCommand: true,
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
                //给指挥起名字
                dialog: {
                    resetName: {
                        visible: false,
                        name: ''
                    }
                },
                //窗口高度
                firstMenuHeight: '',
                //滚动外容器的高度，动态的
                contentStyleObj: {
                    height: ''
                },
                //会议列表相关
                filterText: '', //搜索
                myCommand: {
                    totalData: [],
                    filterData: []
                },
                joinCommand: {
                    totalData: [],
                    filterData: []
                },
                meetName: '', //会议名称
            }
        },
        methods: {
            //顶部切换标题点击事件
            toggleMenu1: function () {
                $('.triggerLi1').addClass('focusLight');
                $('.triggerLi2').removeClass('focusLight');
                $('.triggerLi3').removeClass('focusLight');
                $('.highLight').css('left', '16px');
                this.switchStatus1 = true;
                this.switchStatus2 = false;
                this.switchStatus3 = false;
                this.people = false;
                //进会后在切回来，group.current没变，还是会议的值，所以在从刷一次
                this.currentGroupChange(this.group.currentId)
            },
            toggleMenu2: function () {
                var self = this;
                $('.triggerLi1').removeClass('focusLight');
                $('.triggerLi2').addClass('focusLight');
                $('.triggerLi3').removeClass('focusLight');
                $('.highLight').css('left', '113px');
                this.switchStatus1 = false;
                this.switchStatus2 = true;
                this.switchStatus3 = false;
                this.people = false;
                self.refreshCommand();
                //切换时要重新获取高度
                this.$nextTick(function () {
                    //在下次 DOM 更新循环结束之后执行这个回调。在修改数据之后立即使用这个方法，获取更新后的DOM.
                    self.footerHeight = window.getComputedStyle(self.$refs.footer).height;
                    self.firstMenuHeight = window.getComputedStyle(self.$refs.secondMenu).height;
                    self.contentStyleObj.height = (parseInt(self.firstMenuHeight) - parseInt(self.footerHeight)) + 'px';
                    $('.scrollTab').css('height', self.contentStyleObj.height);
                })
            },
            toggleMenu3: function () {
                $('.triggerLi1').removeClass('focusLight');
                $('.triggerLi2').removeClass('focusLight');
                $('.triggerLi3').addClass('focusLight');
                $('.highLight').css('left', '225px');
                this.switchStatus1 = false;
                this.switchStatus2 = false;
                this.switchStatus3 = true;
                this.people = false;
                this.refreshCommand("meeting");
            },

            //-----第一个树形菜单的复选框的勾选事件 start -----
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
            //-----第一个树形菜单的复选框的勾选事件 end -----

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
                self.buttons.enterCommand = false;
                self.buttons.addCommand = false;
                self.buttons.removeCommand = false;
            },
            //获取指挥列表的数据以及刷新对应按钮，type取值为meeting/command，如果为空则相当于command
            refreshCommand: function (type) {
                var self = this;
                self.command.data.splice(0, self.command.data.length);
                //先清空
                self.myCommand.totalData = [];
                self.myCommand.filterData = [];
                self.joinCommand.totalData = [];
                self.joinCommand.filterData = [];
                ajax.post('/command/query/list', {type: type}, function (data) {
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
                                    //我的会议
                                    self.myCommand.totalData.push(commands[i]);
                                    self.myCommand.filterData.push(commands[i]);
                                } else {
                                    joinCommand.children.push(commands[i]);
                                    //我加入的会议
                                    self.joinCommand.totalData.push(commands[i]);
                                    self.joinCommand.filterData.push(commands[i]);
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
                self.buttons.enterCommand = true;
                self.buttons.addCommand = false;
                self.buttons.removeCommand = true;
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

                    //区分下是不是当前用户建的指挥,目的是只有主席才能改名字
                    if (data.param) {
                        var param = JSON.parse(data.param);
                        self.contextMenu.currentNode.creator = param.creator;
                    }

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
                    ajax.post('/command/record/start/playback', {recordId: recordId}, function (data) {
                        self.qt.invoke('vodRecordFileStart', $.toJSON(data));
                    });
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
                ajax.post('/command/basic/vod/member/start', {
                    id: self.group.current.id,
                    userId: self.dialog.resetName.commandId
                }, function (data) {
                    self.group.current.status = 'start';
                    var splits = data.splits;
                    if (splits && splits.length > 0) {
                        self.qt.invoke('groupMembers', $.toJSON(splits));
                    }
                    self.handleContextMenuClose();
                });
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


            //进入指挥
            enterCommand: function (commandIds, type) {
                var self = this;
                ajax.post('/command/basic/enter', {ids: $.toJSON(commandIds)}, function (data) {
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
                        //进会时 调返回的数据，为了不影响指挥的代码
                        if (type === 'meet') {
                            self.group.currentId = data[0].id;
                            self.group.current = data[0];
                            self.currentGroupChange(self.group.currentId);
                            if (data[0].splits && data[0].splits.length > 0) {
                                for (var j = 0; j < data[0].splits.length; j++) {
                                    playerSettings.push(data[0].splits[j]);
                                }
                            }
                        }
                        if (playerSettings.length > 0) {
                            self.qt.invoke('enterGroups', $.toJSON(playerSettings));
                        }
                    }
                });
            },
            //获取指挥工作台树形菜单的数据
            refreshEnteredGroups: function () {
                var self = this;
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
            },

            //----------第一个树形菜单底部的按钮图标 start----------
            // 过滤在线
            filterOnline: function () {
                var self = this;
                if (self.currentTab == 1) {
                    self.institution.data.splice(0, self.institution.data.length);
                    ajax.post('/command/query/find/institution/tree/user/filter/1', null, function (data) {
                        if (data && data.length > 0) {
                            for (var i = 0; i < data.length; i++) {
                                self.institution.data.push(data[i]);
                            }
                        }
                    });
                } else if (self.currentTab == 3) {
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
                if (self.currentTab == 1) {
                    self.institution.data.splice(0, self.institution.data.length);
                    ajax.post('/command/query/find/institution/tree/user/filter/2', null, function (data) {
                        if (data && data.length > 0) {
                            for (var i = 0; i < data.length; i++) {
                                self.institution.data.push(data[i]);
                            }
                        }
                    });
                } else if (self.currentTab == 3) {
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
                if (self.currentTab == 1) {
                    //点播用户
                    if (self.institution.select.length <= 0) {
                        self.qt.warning('消息提示：您还没有选中用户');
                        return;
                    }
                    var userIds = [];
                    for (var i = 0; i < self.institution.select.length; i++) {
                        userIds.push(self.institution.select[i].id);
                    }
                    ajax.post('/command/vod/user/start/batch', {userIds: $.toJSON(userIds)}, function (data) {
                        self.cancelChoose();
                        self.qt.invoke('vodUsers', $.toJSON(data));
                    });
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
                    //点播设备
                    if (self.device.select.length <= 0) {
                        self.qt.warning('消息提示：您还没有选中设备');
                        return;
                    }
                    var deviceIds = [];
                    for (var i = 0; i < self.device.select.length; i++) {
                        deviceIds.push(self.device.select[i].id);
                    }
                    ajax.post('/command/vod/device/start/batch', {deviceIds: $.toJSON(deviceIds)}, function (data) {
                        self.qt.invoke('vodDevices', $.toJSON(data));
                    });
                }
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
            //添加指挥
            addCommand: function () {
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
                self.handleResetName('new');
            },
            //进入指挥
            enterCommandBatch: function () {
                var self = this;
                if (!self.buttons.enterCommand) return;
                if (self.command.select.length <= 0) {
                    self.qt.warning('消息提示：您还没有选择任何指挥组');
                }
                var ids = [];
                for (var i = 0; i < self.command.select.length; i++) {
                    ids.push(self.command.select[i].id);
                }
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
                            $('.triggerLi1').addClass('focusLight');
                            $('.triggerLi2').removeClass('focusLight');
                            $('.triggerLi3').removeClass('focusLight');
                            $('.highLight').css('left', '16px');
                            self.switchStatus1 = true;
                            self.switchStatus2 = false;
                            self.switchStatus3 = false;
                            self.people = false;
                            if (data[i].splits && data[i].splits.length > 0) {
                                for (var j = 0; j < data[i].splits.length; j++) {
                                    playerSettings.push(data[i].splits[j]);
                                }
                            }
                        }
                        if (playerSettings.length > 0) {
                            self.qt.invoke('enterGroups', $.toJSON(playerSettings));
                        }
                    }
                });
            },
            //删除指挥
            removeCommand: function () {
                var self = this;
                if (!self.buttons.removeCommand) return;
                if (self.command.select.length <= 0) {
                    self.qt.warning('信息提示：您没有选择任何指挥组');
                    return;
                }
                var ids = [];
                for (var i = 0; i < self.command.select.length; i++) {
                    ids.push(self.command.select[i].id);
                }
                ajax.post('/command/basic/remove', {ids: $.toJSON(ids)}, function (data) {
                    self.command.select.splice(0, self.command.select.length);
                    self.refreshCommand();

                    for (var i = 0; i < ids.length; i++) {
                        for (var j = 0; j < self.group.entered.length; j++) {
                            if (ids[i] == self.group.entered[j].id) {
                                self.group.entered.splice(j, 1);
                                if (self.group.currentId == ids[i]) {
                                    self.group.currentId = '';
                                    self.group.current = '';
                                }
                                break;
                            }
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
                var self = this;
                var currentGroup = null;
                if (typeof v === 'object') {
                    currentGroup = v;
                } else {
                    for (var i = 0; i < self.group.entered.length; i++) {
                        if (self.group.entered[i].id == v) {
                            currentGroup = self.group.entered[i];
                            break;
                        }
                    }
                }
                if (currentGroup.type === 'command') {
                    ajax.post('/command/basic/query/members', {id: currentGroup.id}, function (data, status) {
                        if (status !== 200) return;
                        data.type = 'command';
                        data.select = [];
                        var tree = [];
                        data.members.forEach(function (value) {
                            tree = tree.concat(value.children);
                        });
                        //去掉根目录那层
                        self.group.tree = tree;
                        self.group.current = data;
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
                            self.buttons.exitCommand = true;
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
                            self.buttons.exitCommand = true;
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
                } else {

                }
            },
            //刷新指挥工作台的树形菜单
            refreshCurrentGroupMembers: function () {
                var self = this;
                if (self.group.current.type === 'command') {
                    ajax.post('/command/basic/query/members', {id: self.group.current.id}, function (data) {
                        data.type = 'command';
                        data.select = [];
                        self.group.current = data;
                    });
                }
            },
            //指挥工作台树形菜单的复选框
            onCommandUserCheckChange: function (data) {
                var self = this;
                for (var i = 0; i < self.group.current.select.length; i++) {
                    if (self.group.current.select[i] === data) {
                        self.group.current.select.splice(i, 1);
                        return;
                    }
                }
                self.group.current.select.push(data);
            },


            //-------指挥工作台的按钮底部按钮图标 start-------
            //添加成员
            addCommandMember: function () {
                var self = this;
                if (!self.buttons.addMember) return;
                self.qt.window('/router/zk/leader/add/member', {
                    id: self.group.current.id,
                    type: self.group.current.type
                }, {
                    width: 1366,
                    height: 740
                });
            },
            //强退成员
            removeCommandMember: function () {
                var self = this;
                if (!self.buttons.removeMember) return;
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
                        self.currentGroupChange(self.group.current.id);
                        if (data && data.length > 0) {
                            self.qt.invoke('commandMemberDelete', data);
                        }
                    }, null, [403]);
                });
            },
            //协同指挥/会议
            cooperativeCommand: function (name) {
                var self = this;
                if (!self.buttons.cooperativeCommand) return;
                self.qt.window('/router/zk/leader/cooperation', {
                    id: self.group.current.id,
                    type: self.group.current.type,
                    name: name,
                    page: 'add'
                }, {width: 980, height: 600});
            },
            //取消协同指挥/会议
            cancelCooperativeCommand: function (name) {
                var self = this;
                if (!self.buttons.cancelCooperative) return;
                self.qt.window('/router/zk/leader/cooperation', {
                    id: self.group.current.id,
                    type: self.group.current.type,
                    name: name,
                    page: 'cancel'
                }, {width: 980, height: 600});

            },
            //会议成员
            commandMembers: function () {
                var self = this;
                if (!self.buttons.cooperativeCommand) return;
                self.qt.window('/router/zk/leader/cooperation', {
                    id: self.group.current.id,
                    type: self.group.current.type,
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
                if (!self.buttons.commandForward) return;
                self.qt.window('/router/zk/leader/forward', {id: self.group.current.id, name: name}, {
                    width: 1024,
                    height: 768,
                    title: '指挥转发'
                });
            },
            //对上静默
            silenceUp: function () {
                var self = this;
                if (!self.buttons.silenceUp) return;
                if (!self.buttons.silenceUpStart) {
                    ajax.post('/command/basic/silence/up/start', {id: self.group.current.id}, function () {
                        self.buttons.silenceUpStart = true;
                        self.qt.warning('提示消息：操作成功');
                    });
                } else {
                    ajax.post('/command/basic/silence/up/stop', {id: self.group.current.id}, function () {
                        self.buttons.silenceUpStart = false;
                        self.qt.warning('提示消息：操作成功');
                    });
                }
            },
            //对下静默
            silenceDown: function () {
                var self = this;
                if (!self.buttons.silenceDown) return;
                if (!self.buttons.silenceDownStart) {
                    ajax.post('/command/basic/silence/down/start', {id: self.group.current.id}, function () {
                        self.buttons.silenceDownStart = true;
                        self.qt.warning('提示消息：操作成功');
                    });
                } else {
                    ajax.post('/command/basic/silence/down/stop', {id: self.group.current.id}, function () {
                        self.buttons.silenceDownStart = false;
                        self.qt.warning('提示消息：操作成功');
                    });
                }
            },
            //发送通知
            sendMessage: function () {
                var self = this;
                if (!self.buttons.sendMessage) return;
                self.qt.window('/router/zk/leader/send/message', {id: self.group.current.id}, {
                    width: 1224,
                    height: 768
                });
            },
            //指挥录制
            commandRecord: function () {
                var self = this;
                if (self.buttons.isRecord) {
                    ajax.post('/command/record/stop', {id: self.group.current.id}, function (data) {
                        self.buttons.isRecord = false;
                        self.qt.warning('业务提示：操作成功');
                    });
                } else {
                    ajax.post('/command/record/start', {id: self.group.current.id}, function (data) {
                        self.buttons.isRecord = true;
                        self.qt.warning('业务提示：操作成功');
                    });
                }
            },
            //指挥提醒
            commandRemind: function () {
                var self = this;
                if (!self.buttons.commandReminder) return;
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
            },
            //开始指挥
            startCommand: function () {
                var self = this;
                if (self.group.current.status === 'pause') {
                    ajax.post('/command/basic/pause/recover', {
                        id: self.group.current.id
                    }, function (data) {
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
            },
            //暂停指挥
            pauseCommand: function () {
                var self = this;
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
            },
            //退出指挥
            exitCommand: function (name) {
                var self = this;
                if (name === '会议') {
                    self.switchStatus1 = false;
                    self.switchStatus2 = false;
                    self.switchStatus3 = true;
                    self.people = false;
                }
                if (self.user.id == self.group.current.creator) {
                    ajax.post('/command/basic/stop', {
                        id: self.group.current.id
                    }, function (data) {
                        if (self.group.current.creator != self.user.id) {
                            for (var i = 0; i < self.group.entered.length; i++) {
                                if (self.group.entered[i].id == self.group.current.id) {
                                    self.group.entered.splice(i, 1);
                                    break;
                                }
                            }
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
                        if (name === '会议') {
                            self.refreshCommand("meeting");
                        } else {
                            self.refreshCommand();
                        }
                    });
                } else {
                    ajax.post('/command/basic/exit', {
                        id: self.group.current.id
                    }, function (data) {
                        for (var i = 0; i < self.group.entered.length; i++) {
                            if (self.group.entered[i].id == self.group.current.id) {
                                self.group.entered.splice(i, 1);
                                break;
                            }
                            if (self.group.entered.length > 0) {
                                self.group.currentId = self.group.entered[i].id;
                                self.group.current = self.group.entered[i];
                                self.currentGroupChange(self.group.currentId);
                            } else {
                                self.group.currentId = '';
                                self.group.current = '';
                            }
                            if (data && data.length > 0) {
                                self.qt.invoke('commandExit', data);
                            }
                        }
                    });
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

            //关闭给指挥起名字弹框事件
            resetNameClose: function () {
                var self = this;
                self.dialog.resetName.visible = false;
                self.dialog.resetName.name = '';
                self.cancelChoose();
            },
            //打开 给指挥起名字 的对话框显示
            handleResetName: function (type) {
                var self = this;
                //为了提交时区分是新建还是修改
                self.dialog.resetName.type = type;
                //修改时，把原有值赋过去
                if (type === 'edit') {
                    if (self.command.select.length <= 0) {
                        self.qt.warning('信息提示：请先选择要修改的项');
                        return;
                    } else if (self.command.select.length > 1) {
                        self.qt.warning('信息提示：只能选择一项进行修改名称');
                        return;
                    }
                    //报错要修改的那项的名字
                    self.dialog.resetName.name = self.command.select[0].name;
                }
                self.dialog.resetName.visible = true;
            },
            //给指挥起名字的添加事件
            resetNameCommit: function () {
                var self = this;
                if (!self.dialog.resetName.name) {
                    self.$message({
                        type: 'error',
                        message: '名称不能为空！'
                    });
                    return;
                }
                var userIds = [];
                for (var i = 0; i < self.institution.select.length; i++) {
                    userIds.push(self.institution.select[i].id);
                }
                self.dialog.resetName.name.replace(/^\s*|\s*$/g, ''); //去除头尾空格
                if (self.dialog.resetName.type === 'new') {
                    // 新建
                    ajax.post('/command/basic/save', {
                        members: $.toJSON(userIds),
                        name: self.dialog.resetName.name
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
                        self.currentGroupChange(self.group.currentId);
                        self.qt.success('创建指挥工作组成功');
                        self.resetNameClose();
                    });
                } else {
                    //   修改
                    ajax.post('/command/basic/modify/name', {
                        id: self.command.select[0].id,
                        name: self.dialog.resetName.name
                    }, function (data) {
                        self.qt.success('修改成功！');
                        self.resetNameClose();
                        self.refreshCommand();
                    });
                }
            },

            //   ----------第三个tab相关的-----------------
            filterCommandUserNode: function (value, data) {
                if (!value) return true;
                return data.name.indexOf(value) !== -1;
            },
            //删除会议
            removeMyCommand: function (id) {
                var self = this;
                ajax.post('/command/basic/remove', {ids: $.toJSON([id])}, function (data) {
                    for (var i = 0; i < self.myCommand.totalData.length; i++) {
                        if (self.myCommand.totalData[i].id === id) {
                            self.myCommand.totalData.splice(i, 1);
                        }
                    }
                    for (var i = 0; i < self.myCommand.filterData.length; i++) {
                        if (self.myCommand.filterData[i].id === id) {
                            self.myCommand.filterData.splice(i, 1);
                        }
                    }
                });
            },
            //进入会议
            enterMyCommand: function (id, action) {
                var self = this;
                ajax.post('/command/basic/enter', {ids: $.toJSON([id])}, function (data) {
                    self.meetName = data[0].name;
                    self.switchStatus1 = false;
                    self.switchStatus2 = false;
                    self.switchStatus3 = false;
                    self.people = true;

                    //进入自己建的会议
                    if (action === 'start') {
                        self.group.current.id = id;
                        self.qt.set('currentGroupId', id);
                        ajax.post('/command/basic/start', {
                            id: id
                        }, function (datas) {
                            var splits = datas.splits;
                            //防止从他人的会出来进自己会的时候 底部按钮显示不正确
                            self.qt.linkedWebview('rightBar', {id: 'currentGroupChange', params: $.toJSON(data)});
                            if (splits && splits.length > 0) {
                                // 主席web通知qt进入指挥
                                self.qt.invoke('groupMembers', $.toJSON(splits));
                            }
                        });
                    } else {
                        //进入他人会议
                        self.qt.linkedWebview('rightBar', {id: 'currentGroupChange', params: $.toJSON(data)});
                    }
                });
            },
            //新建会议
            makeCommand: function () {
                var self = this;
                self.qt.window('/router/zk/leader/dialog/create/group', {}, {
                    width: 1366,
                    height: 700
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
            //悬浮按钮鼠标移动事件
            itemMousemove: function (e) {
                var odiv = e.target;        //获取目标元素

                //算出鼠标相对元素的位置
                var disY = e.clientY - odiv.offsetTop;
                document.onmousemove = function (e) {       //鼠标按下并移动的事件
                    //用鼠标的位置减去鼠标相对元素的位置，得到元素的位置
                    var top = e.clientY - disY;
                    //防止拖拽超过范围
                    if (top <= 113 || top > 700) {
                        return;
                    }
                    //移动当前元素
                    odiv.style.top = top + 'px';
                };
                document.onmouseup = function (e) {
                    document.onmousemove = null;
                    document.onmouseup = null;
                };
            },
            //申请发言
            applySpeak: function () {
                var self = this;
                if (!self.buttons.isSpeaking) {
                    ajax.post('/command/meeting/speak/apply', {id: self.group.current.id}, function (data) {
                        self.qt.success('已向主席发出申请');
                    })
                }
            },
            //成员自己停止发言
            stopSpeak: function () {
                var self = this;
                if (self.buttons.isSpeaking) {
                    ajax.post('/command/meeting/speak/stop/by/member', {id: self.group.current.id}, function (data) {
                        self.qt.success('您停止了自己的发言');
                        self.buttons.isSpeaking = false;
                    })
                }
            },
            //指定发言
            assignSpeak: function () {
                var self = this;
                var ids = [];
                if (!self.group.current.select.length) {
                    self.qt.warning('提示信息：您没有勾选任何用户');
                    return;
                }
                self.group.current.select.forEach(function (value) {
                    ids.push(value.id);
                });
                ajax.post('/command/meeting/speak/appoint', {
                    id: self.group.current.id,
                    userIds: $.toJSON(ids)
                }, function (data) {
                    self.qt.success('操作成功');
                });

            },
            //主席停止指定发言
            stopAssignSpeak: function () {
                var self = this;
                var ids = [];
                if (!self.group.current.select.length) {
                    self.qt.warning('提示信息：您没有勾选任何用户');
                    return;
                }
                self.group.current.select.forEach(function (value) {
                    ids.push(value.id);
                });
                ajax.post('/command/meeting/speak/stop/by/chairman', {
                    id: self.group.current.id,
                    userIds: $.toJSON(ids)
                }, function (data) {
                    self.qt.success('操作成功');
                    self.group.current.select.forEach(function (value) {
                        value.checked = false;
                    });
                    self.group.current.select = [];
                });
            },
            //全员讨论
            allDiscuss: function () {
                var self = this;
                if (!self.buttons.discussion) { //讨论
                    ajax.post('/command/meeting/discuss/start', {id: self.group.current.id}, function (data) {
                        self.qt.success('操作成功');
                        self.buttons.discussion = true;
                    })
                } else { //停止讨论
                    ajax.post('/command/meeting/discuss/stop', {id: self.group.current.id}, function (data) {
                        self.qt.success('操作成功');
                        self.buttons.discussion = false;
                    })
                }
            },
            //退出会议并关闭窗体---暂时好像没用到
            exitCurrentCommandAndCloseWindow: function () {
                var self = this;
                if (!self.group.current) {
                    self.qt.linkedWebview('header', {
                        id: 'closeWindow'
                    });
                } else {
                    if (self.user.id == self.group.current.creator) {
                        ajax.post('/command/basic/stop', {
                            id: self.group.current.id
                        }, function (data) {
                            self.qt.linkedWebview('header', {
                                id: 'closeWindow'
                            });
                        });
                    } else {
                        ajax.post('/command/basic/exit', {
                            id: self.group.current.id
                        }, function (data) {
                            self.qt.linkedWebview('header', {
                                id: 'closeWindow'
                            });
                        });
                    }
                }
            },
        },
        computed: {
            groupCurrent: function () {
                var self = this;
                return self.group.current;
            }
        },
        watch: {
            currentTab: function () {
                var self = this;
                if (self.currentTab == 0) {
                    //指挥列表
                    self.refreshCommand();
                    self.refreshCommandButtonAction();
                } else if (self.currentTab == 1) {
                    //组织结构
                    if (self.institution.data.length === 0) {
                        self.refreshInstitution();
                    }
                    self.refreshInstitutionButtonAction();
                } else if (self.currentTab == 2) {
                    //文件资源
                    if (self.file.data.length === 0) {
                        self.refreshFile();
                    }
                    self.refreshFileButtonAction();
                } else if (self.currentTab == 3) {
                    self.refreshDevice();
                    self.refreshDeviceButtonAction();
                } else if (self.currentTab == 4) {
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
                    self.buttons.exitCommand = true;
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
                    self.buttons.exitCommand = true;
                    self.buttons.speak = true;
                    self.buttons.meetBtn = false;
                }
            },
            // 监听浏览器窗口宽度变化，这个值就会变化,
            firstMenuHeight: function (val) {
                // 这个tableHeight就是最后的height
                this.contentStyleObj.height = (parseInt(val) - parseInt(this.footerHeight)) + 'px';
                $('.scrollTab').css('height', this.contentStyleObj.height);
            },
            filterText: function (val) {
                var self = this;
                var myFilterData = [];
                var joinFilterData = [];

                if (self.myCommand.totalData && self.myCommand.totalData.length) {
                    myFilterData = self.myCommand.totalData.filter(function (item) {
                        if (item && item.name) {
                            return item.name.indexOf(val) > -1;
                        }
                        return false;
                    })
                }

                if (self.joinCommand.totalData && self.joinCommand.totalData.length) {
                    joinFilterData = self.joinCommand.totalData.filter(function (item) {
                        if (item && item.name) {
                            return item.name.indexOf(val) > -1;
                        }
                        return false;
                    })
                }
                self.myCommand.filterData = myFilterData;
                self.joinCommand.filterData = joinFilterData;
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
                self.refreshCommand();
                self.refreshCommandButtonAction();
                self.refreshCurrentGroupMembers();

                //动态获取firsetMenu和footer的高度
                window.onresize = function () {
                    self.firstMenuHeight = window.getComputedStyle(self.$refs.secondMenu).height;
                    self.footerHeight = window.getComputedStyle(self.$refs.footer).height;
                };

                //给scrollTab 设置初始高度
                self.contentStyleObj.height = Math.abs((parseInt(window.getComputedStyle(self.$refs.secondMenu).height) - parseInt(window.getComputedStyle(self.$refs.footer).height))) + 'px';

                self.qt.get(['user'], function (variables) {
                    self.user = variables.user ? $.parseJSON(variables.user) : {};
                    self.refreshEnteredGroups();
                });

                //从新建会议页面来，到这个页面时改变
                self.qt.on('showData', function (e) {
                    var group = $.parseJSON(e.params)[0];
                    self.switchStatus1 = false;
                    self.switchStatus2 = false;
                    self.switchStatus3 = false;
                    self.people = true;
                    self.meetName = group.name;
                    group.type = 'command';
                    self.currentGroupChange(group);
                    self.qt.set('currentGroupId', group.id);
                });

                //从安排会议页面来，到这个页面时在刷新下数据
                self.qt.on('refreshData', function (e) {
                    self.switchStatus1 = false;
                    self.switchStatus2 = false;
                    self.switchStatus3 = true;
                    self.people = false;
                    self.refreshCommand("meeting");
                });

                //监听到添加成员，刷新树
                self.qt.on('commandMemberAdd', function (e) {
                    e = e.params;
                    self.currentGroupChange(self.group.current.id);
                    self.qt.invoke('groupMembers', e);
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
                    self.enterCommand([e.params]);
                });
                //成员同意进入会议
                self.qt.on('linkEnterMeeting', function (e) {
                    self.switchStatus1 = false;
                    self.switchStatus2 = false;
                    self.switchStatus3 = false;
                    self.people = true;
                    $('.triggerLi1').removeClass('focusLight');
                    $('.triggerLi2').removeClass('focusLight');
                    $('.triggerLi3').addClass('focusLight');
                    $('.highLight').css('left', '225px');
                    //加参数是为了区分进会时 ，不影响指挥的代码
                    self.enterCommand([e.params], 'meet');
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
                    }
                    self.refreshCommand("meeting");
                });

                //右上角叉的关闭按钮
                self.qt.on('exitCurrentCommandAndCloseWindow', function () {
                    self.exitCurrentCommandAndCloseWindow();
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

                //通知申请人被同意
                self.qt.on('speakApplyAgree', function (e) {
                    var e = e.params;
                    self.qt.info(e.businessInfo);
                    self.buttons.isSpeaking = true;
                });
                //通知申请人被拒绝
                self.qt.on('speakApplyDisagree', function (e) {
                    var e = e.params;
                    self.qt.info(e.businessInfo);
                    self.buttons.isSpeaking = false;
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
                })
            });
        }
    });
    return Vue;
});