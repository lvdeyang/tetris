/**
 * Created by lzp on 2019/3/16.
 */
define([
    'text!' + window.APPPATH + 'cs/management/page-cs-management.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'mi-lightbox',
    'cs-user-dialog',
    'program-screen',
    'cs-media-picker',
    'cs-area-picker',
    'mi-compress-dialog',
    'css!' + window.APPPATH + 'cs/management/page-cs-management.css'
], function (tpl, config, $, ajax, context, commons, Vue) {

    var pageId = 'page-cs-management';

    var init = function () {

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el: '#' + pageId + '-wrapper',
            data: {
                test: {
                    visible: false
                },
                menus: context.getProp('menus'),
                user: context.getProp('user'),
                groups: context.getProp('groups'),
                loading: false,
                loadingText: "",
                time: "1970-01-01 00:00:00",
                channel: {
                    page: {
                        currentPage: 1,
                        sizes: [10, 15, 20, 50],
                        size: 10,
                        total: 0
                    },
                    data: [],
                    multipleSelection: []
                },
                options: {
                    level: [
                        {
                            value: '9',
                            label: '一般'
                        }, {
                            value: '8',
                            label: '较大'
                        }, {
                            value: '7',
                            label: '重大'
                        }, {
                            value: '6',
                            label: '很重大'
                        }, {
                            value: '5',
                            label: '特别重大'
                        }
                    ]
                },
                dialog: {
                    addProgram: {
                        visible: false,
                        loading: false,
                        name: "",
                        broadWay: "",
                        outputQtUsers: [],
                        outputPushUsers: [],
                        outputUserPort: '',
                        outputUserEndPort: '',
                        outputCount: 1,
                        output: [],
                        remark: "",
                        encryption: false,
                        autoBroad: false,
                        autoBroadShuffle: false,
                        autoBroadDuration: 1,
                        autoBroadStart: "",
                        date: "",
                        level: '',
                        hasFile: true
                    },
                    editChannel: {
                        visible: false,
                        loading: false,
                        data: "",
                        name: "",
                        broadWay: "",
                        outputQtUsers: [],
                        outputPushUsers: [],
                        outputUserPort: "",
                        outputUserEndPort: '',
                        outputCount: 1,
                        output: [],
                        remark: "",
                        encryption: false,
                        autoBroad: false,
                        autoBroadShuffle: false,
                        autoBroadDuration: 1,
                        autoBroadStart: "",
                        level: '',
                        hasFile: true
                    },
                    setAutoBroad: {
                        visible: false,
                        loading: false,
                        data: {},
                        autoBroadShuffle: false,
                        autoBroadDuration: 1,
                        autoBroadStart: ""
                    },
                    setOutput: {
                        visible: false,
                        loading: false,
                        data: {},
                        broadWay: '',
                        outputQtUsers: [],
                        outputPushUsers: [],
                        outputUserPort: '',
                        outputUserEndPort: '',
                        outputCount: 1,
                        output: []
                    },
                    editMenu: {
                        visible: false,
                        loading: false,
                        data: "",
                        tree: {
                            props: {
                                label: 'name',
                                children: 'subColumns'
                            },
                            expandOnClickNode: false,
                            data: [],
                            current: '',
                            loading: false
                        },
                        resource: {
                            data: []
                        },
                        dialog: {
                            editNode: {
                                visible: false,
                                loading: false,
                                data: "",
                                name: "",
                                remark: ""
                            },
                            chooseResource: {
                                visible: false,
                                loading: false,
                                tree: {
                                    props: {
                                        label: "name",
                                        children: "children"
                                    },
                                    expandOnClickNode: false,
                                    data: [],
                                    current: '',
                                    loading: false
                                },
                                chooseNode: []
                            }
                        }
                    },
                    editSchedules: {
                        visible: false,
                        data: {},
                        table: {
                            loading: false,
                            page: {
                                currentPage: 1,
                                sizes: [10, 15, 20, 50],
                                size: 10,
                                total: 0
                            },
                            data: [],
                            multipleSelection: []
                        },
                        dialog: {
                            addSchedule: {
                                visible: false,
                                loading: false,
                                broadDate: "",
                                remark: ""
                            },
                            editSchedule: {
                                visible: false,
                                loading: false,
                                data: {},
                                broadDate: "",
                                remark: ""
                            },
                            editProgram: {

                            }
                        }
                    },
                    editProgram: {
                        visible: false,
                        loading: false,
                        data: "",
                        audioIndex: "",
                        previewData: {},
                        commitData: {
                            screenNum: "",
                            screenInfo: [],
                            currentSerialNum: "",
                            currentSerialInfo: []
                        },
                        options: {
                            list: [{
                                id: 1,
                                name: "一分屏"
                            }, {
                                id: 4,
                                name: "四分屏",
                                disabled: true
                            }, {
                                id: 6,
                                name: "六分屏",
                                disabled: true
                            }, {
                                id: 9,
                                name: "九分屏",
                                disabled: true
                            }],
                            current: "一分屏"
                        },
                        screen: {
                            one: {
                                resources: {}
                            },
                            four: {
                                resources: []
                            },
                            six: {
                                resources: []
                            },
                            nine: {
                                resources: []
                            }
                        },
                        dialog: {
                            chooseResource: {
                                visible: false,
                                loading: false,
                                tree: {
                                    loading: false,
                                    props: {
                                        label: "name",
                                        children: "subColumns"
                                    },
                                    expandOnClickNode: false,
                                    data: [],
                                    current: {}
                                },
                                resources: {
                                    data: [],
                                    chooses: []
                                }
                            }
                        }
                    },
                    manageBroadcastArea: {
                        visible: false,
                        data: "",
                        tree: {
                            loading: false,
                            props: {
                                lable: "name",
                                children: "subColumns"
                            },
                            expandOnclickNode: false,
                            data: [],
                            current: []
                        }
                    },
                    manageBroad: {
                        visible: false,
                        loading: false,
                        data: [],
                        dialog: {
                            addBroad: {
                                visible: false,
                                loading: false,
                                tree: {
                                    props: {
                                        label: "name",
                                        children: "subColumns"
                                    },
                                    expandOnClickNode: false,
                                    data: "",
                                    current: '',
                                    loading: false
                                },
                                chooseNode: []
                            }
                        }
                    },
                    seekBroadcast: {
                        visible: false,
                        loading: false,
                        data: {},
                        duration: ""
                    },
                    deleteProgram: {
                        visible: false,
                        loading: false,
                        name: "",
                        remark: "",
                        date: ""
                    },
                    chooseBroadMedia: {
                        visible: false,
                        loading: false,
                        data: {}
                    },
                    upgrade: {
                        visible: false,
                        loading: false,
                        version: '',
                        way:'',
                        tarCheck: {
                            name: '',
                            previewUrl: '',
                            size: ''
                        },
                        areaCheck: [],
                        wayOptions: [
                            '4G','DTMB'
                        ]
                    }
                },
                broadWayStream: '轮播推流',
                broadWayFile: '下载文件',
                broadWayTerminal: '终端播发',
                qtEquipType: 'QT_MEDIA_EDITOR',
                pushEquipType: 'PUSH'
            },
            computed: {},
            watch: {},
            methods: {
                getChannelList: function () {
                    var self = this;
                    self.loading = true;
                    var requestData = {
                        currentPage: self.channel.page.currentPage,
                        pageSize: self.channel.page.size
                    };
                    ajax.post('/cs/channel/list', requestData, function (data, status) {
                        self.loading = false;
                        if (status != 200) return;
                        self.channel.data = data.rows;
                        self.channel.page.total = data.total;
                    }, null, ajax.NO_ERROR_CATCH_CODE)
                },
                multiDelete: function () {
                    var self = this;

                    //if (self.channel.multipleSelection.length > 0) {
                    //    self.showTip('', '此操作将永久删除频道，且不可恢复，是否继续?', function(callback) {
                    //        var idList = [];
                    //        for (var i = 0; i < self.channel.multipleSelection; i++) {
                    //            idList.push(self.channel.multipleSelection[i]);
                    //        }
                    //        var questData = {id: idList};
                    //        ajax.post('/cs/channel/remove', questData, function (data, status) {
                    //            callback();
                    //            self.channel.multipleSelection = [];
                    //            slef.getChannelList();
                    //        }, null, ajax.NO_ERROR_CATCH_CODE);
                    //    });
                    //}
                },
                handleResetZoneUrl: function () {
                    var self = this;
                    self.showTip('', '此操作将重置所有终端补包地址，是否继续?', function(callback) {
                        ajax.post('/cs/channel/reset/zone/url', null, function(data, status) {
                            if (status == 200) {
                                self.$message({
                                    message: '重置成功',
                                    type: 'success'
                                });
                            }
                            callback();
                        })
                    });
                },

                // add channel dialog event
                handleAddProgram: function(){
                    var self = this;
                    self.dialog.addProgram.broadWay = self.broadWayStream;
                    self.dialog.addProgram.level = '一般';
                    self.dialog.addProgram.hasFile = true;
                    var t = new Date();
                    self.dialog.addProgram.date = t.getFullYear()+"-"+(t.getMonth()+1)+"-"+t.getDate()+" "+t.getHours()+":"+t.getMinutes()+":"+t.getSeconds();
                    var output = {
                        previewUrlIp : '',
                        previewUrlPort : '',
                        previewUrlEndPort: ''
                    };
                    self.dialog.addProgram.outputCount = 1;
                    self.dialog.addProgram.output.push(output);
                    self.dialog.addProgram.visible = true;
                },
                handleAddProgramClose: function () {
                    var self = this;
                    self.dialog.addProgram.name = "";
                    self.dialog.addProgram.date = "";
                    self.dialog.addProgram.broadWay = "";
                    self.dialog.addProgram.level = '';
                    self.dialog.addProgram.hasFile = true;
                    self.dialog.addProgram.outputQtUsers = [];
                    self.dialog.addProgram.outputPushUsers = [];
                    self.dialog.addProgram.outputUserPort = '';
                    self.dialog.addProgram.outputUserEndPort = '';
                    self.dialog.addProgram.outputCount = 1;
                    self.dialog.addProgram.output.splice(0, self.dialog.addProgram.output.length);
                    self.dialog.addProgram.remark = "";
                    self.dialog.addProgram.encryption = false;
                    self.dialog.addProgram.autoBroad = false;
                    self.dialog.addProgram.autoBroadShuffle = false;
                    self.dialog.addProgram.autoBroadDuration = 1;
                    self.dialog.addProgram.autoBroadStart = "";
                    self.dialog.addProgram.visible = false;
                    self.dialog.addProgram.value = "";
                },
                //更换优先级监听
                handleAddProgramLevelOptionsChange: function (data) {
                    var self = this;
                    self.dialog.addProgram.level = data;
                },
                handleAddProgramCommit: function () {
                    var self = this;
                    if (self.dialog.addProgram.autoBroad && !self.dialog.addProgram.autoBroadStart) {
                        this.$message({
                            message: '请选择自动播发起始时间',
                            type: 'warning'
                        });
                        return;
                    }
                    if (self.dialog.addProgram.broadWay == self.broadWayStream && self.dialog.addProgram.outputQtUsers.length <= 0 && self.dialog.addProgram.outputPushUsers.length <= 0) {
                        var noOut = true;
                        for (var i = 0; i < self.dialog.addProgram.output.length; i++){
                            if (self.dialog.addProgram.output[i].previewUrlIp.trim()
                                && self.dialog.addProgram.output[i].previewUrlPort.trim()){
                                noOut = false;
                                break;
                            }
                        }
                        if (noOut) {
                            this.$message({
                                message: '请完善至少一个输出或选择一个播发用户',
                                type: 'warning'
                            });
                            return;
                        }
                    }
                    self.dialog.addProgram.loading = true;
                    var newData = {
                        name: self.dialog.addProgram.name,
                        date: self.dialog.addProgram.date,
                        broadWay: self.dialog.addProgram.broadWay,
                        outputUsers: JSON.stringify(self.dialog.addProgram.broadWay == self.broadWayStream ? self.dialog.addProgram.outputQtUsers : self.dialog.addProgram.outputQtUsers.concat(self.dialog.addProgram.outputPushUsers)),
                        outputUserPort: self.dialog.addProgram.outputUserPort || '9999',
                        ouputUserEndPort: self.dialog.addProgram.outputUserEndPort,
                        output: JSON.stringify(self.dialog.addProgram.output),
                        encryption: self.dialog.addProgram.encryption,
                        autoBroad: self.dialog.addProgram.broadWay == self.broadWayStream ? self.dialog.addProgram.autoBroad : false,
                        autoBroadShuffle: self.dialog.addProgram.autoBroadShuffle,
                        autoBroadDuration: self.dialog.addProgram.autoBroadDuration,
                        autoBroadStart: self.dialog.addProgram.autoBroadStart,
                        remark: self.dialog.addProgram.remark,
                        level: self.dialog.addProgram.broadWay == self.broadWayTerminal ? self.dialog.addProgram.level : '',
                        hasFile: self.dialog.addProgram.hasFile
                    };
                    ajax.post('/cs/channel/add', newData, function (data, status) {
                        self.dialog.addProgram.loading = false;
                        if (status != 200) return;
                        if (self.channel.data.length < self.channel.page.size) {
                            self.channel.data.push(data);
                        }
                        self.channel.page.total += 1;
                        self.handleAddProgramClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },

                // edit channel dialog event
                editChannel: function (scope) {
                    var self = this;
                    var row = scope.row;
                    self.dialog.editChannel.data = row;
                    self.dialog.editChannel.name = row.name;
                    self.dialog.editChannel.broadWay = row.broadWay;
                    if (row.outputUsers) {
                        if (row.broadWay == self.broadWayFile) {
                            for (var i = 0; i < row.outputUsers.length; i++) {
                                var item = row.outputUsers[i];
                                switch(item.equipType) {
                                    case self.qtEquipType:
                                        self.dialog.editChannel.outputQtUsers.push(item);
                                        break;
                                    case self.pushEquipType:
                                        self.dialog.editChannel.outputPushUsers.push(item);
                                        break;
                                    default :
                                        break;
                                }
                            }
                        } else {
                            for (var i = 0; i < row.outputUsers.length; i++) {
                                self.dialog.editChannel.outputQtUsers.push(row.outputUsers[i]);
                            }
                        }
                    }
                    self.dialog.editChannel.outputUserPort = row.outputUserPort;
                    self.dialog.editChannel.outputUserEndPort = row.outputUserEndPort;
                    if (row.output) self.dialog.editChannel.outputCount = row.output.length;
                    self.dialog.editChannel.output = row.output;
                    self.dialog.editChannel.remark = row.remark;
                    self.dialog.editChannel.level = row.level;
                    self.dialog.editChannel.hasFile = (row.hasFile != null) ? row.hasFile : true;
                    self.dialog.editChannel.encryption = row.encryption;
                    self.dialog.editChannel.autoBroad = row.autoBroad;
                    self.dialog.editChannel.autoBroadShuffle = row.autoBroadShuffle;
                    self.dialog.editChannel.autoBroadDuration = row.autoBroadDuration;
                    self.dialog.editChannel.autoBroadStart = row.autoBroadStart;
                    self.dialog.editChannel.visible = true;
                },
                handleEditChannelClose: function () {
                    var self = this;
                    self.dialog.editChannel.visible = false;
                    self.dialog.editChannel.data = "";
                    self.dialog.editChannel.name = "";
                    self.dialog.editChannel.broadWay = "";
                    self.dialog.editChannel.outputQtUsers = [];
                    self.dialog.editChannel.outputPushUsers = [];
                    self.dialog.editChannel.outputUserPort = '';
                    self.dialog.editChannel.outputUserEndPort = '';
                    self.dialog.editChannel.outputCount = 1;
                    self.dialog.editChannel.output = [];
                    self.dialog.editChannel.remark = "";
                    self.dialog.editChannel.level = '';
                    self.dialog.editChannel.hasFile = true;
                    self.dialog.editChannel.encryption  = false;
                    self.dialog.editChannel.autoBroad = false;
                    self.dialog.editChannel.autoBroadShuffle = false;
                    self.dialog.editChannel.autoBroadDuration = 1;
                    self.dialog.editChannel.autoBroadStart = "";
                },
                handleEditChannelLevelOptionsChange: function (data) {
                    var self = this;
                    self.dialog.editChannel.level = data;
                },
                handleEditChannelCommitSend: function (listener) {
                    var self = this;
                    self.dialog.editChannel.loading = true;
                    var newName = self.dialog.editChannel.name;
                    var newRemark = self.dialog.editChannel.remark;
                    var level = self.dialog.editChannel.broadWay == self.broadWayTerminal ? self.dialog.editChannel.level : '';
                    var hasFile = self.dialog.editChannel.hasFile;
                    var outputQtUsers = self.dialog.editChannel.outputQtUsers;
                    var outputPushUsers = self.dialog.editChannel.outputPushUsers;
                    var outputUserPort = self.dialog.editChannel.outputUserPort || '9999';
                    var outputUserEndPort = self.dialog.editChannel.outputUserEndPort;
                    var output = self.dialog.editChannel.output;
                    var encryption = self.dialog.editChannel.encryption;
                    var autoBroad = self.dialog.editChannel.broadWay == self.broadWayStream ? self.dialog.editChannel.autoBroad : false;
                    var autoBroadShuffle = self.dialog.editChannel.autoBroadShuffle;
                    var autoBroadDuration = self.dialog.editChannel.autoBroadDuration;
                    var autoBroadStart = self.dialog.editChannel.autoBroadStart;
                    var questData = {
                        id: self.dialog.editChannel.data.id,
                        name: newName,
                        outputUsers: JSON.stringify(outputQtUsers.concat(outputPushUsers)),
                        outputUserPort: outputUserPort,
                        outputUserEndPort: outputUserEndPort,
                        output: JSON.stringify(output),
                        encryption: encryption,
                        autoBroad: autoBroad,
                        autoBroadShuffle: autoBroadShuffle,
                        autoBroadDuration: autoBroadDuration,
                        autoBroadStart: autoBroadStart,
                        remark: newRemark,
                        level: level,
                        hasFile: hasFile
                    };
                    ajax.post('/cs/channel/edit', questData, function (data, status) {
                        self.dialog.editChannel.loading = false;
                        if (listener) listener();
                        if (status != 200) return;
                        self.$message({
                            message: '保存成功',
                            type: 'success'
                        });
                        self.dialog.editChannel.data.name = newName;
                        self.dialog.editChannel.data.remark = newRemark;
                        self.dialog.editChannel.data.level = level;
                        self.dialog.editChannel.data.hasFile = hasFile;
                        self.dialog.editChannel.data.outputUsers = outputQtUsers.concat(outputPushUsers);
                        self.dialog.editChannel.data.output = output;
                        self.dialog.editChannel.data.outputUserPort = outputUserPort;
                        self.dialog.editChannel.data.outputUserEndPort = outputUserEndPort;
                        self.dialog.editChannel.data.encryption = encryption;
                        self.dialog.editChannel.data.autoBroad = autoBroad;
                        self.dialog.editChannel.data.autoBroadShuffle = autoBroadShuffle;
                        self.dialog.editChannel.data.autoBroadDuration = autoBroadDuration;
                        self.dialog.editChannel.data.autoBroadStart = autoBroadStart;
                        self.dialog.editChannel.data.broadcastStatus = data.broadcastStatus;
                        self.handleEditChannelClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE)
                },
                handleEditChannelCommit: function () {
                    var self = this;
                    if (self.dialog.editChannel.autoBroad && !self.dialog.editChannel.autoBroadStart) {
                        this.$message({
                            message: '请选择自动播发起始时间',
                            type: 'warning'
                        });
                        return;
                    }
                    if (self.dialog.editChannel.broadWay == self.broadWayStream && self.dialog.editChannel.outputQtUsers.length <= 0 && self.dialog.editChannel.outputPushUsers.length <= 0) {
                        var noOut = true;
                        for (var i = 0; i < self.dialog.editChannel.output.length; i++){
                            if (self.dialog.editChannel.output[i].previewUrlIp.trim()
                                && self.dialog.editChannel.output[i].previewUrlPort.trim()){
                                noOut = false;
                                break;
                            }
                        }
                        if (noOut) {
                            this.$message({
                                message: '请完善至少一个输出或选择一个播发用户',
                                type: 'warning'
                            });
                            return;
                        }
                    }
                    if (self.dialog.editChannel.autoBroad) {
                        if ((!self.dialog.editChannel.autoBroadStart || !self.dialog.editChannel.autoBroadStart.trim())){
                            this.$message({
                                message: '请选择自动播发起始时间',
                                type: 'warning'
                            });
                            return;
                        }
                        self.showTip('', '此操作将替换该频道先前设置的所有自动播发节目单，是否继续?', function(callback) {
                            self.handleEditChannelCommitSend(callback);
                        });
                    } else {
                        self.handleEditChannelCommitSend(function () {});
                    }
                },

                //set autoBroad dialog event
                handleSetAutoBroad: function(data) {
                    var self = this;
                    self.dialog.setAutoBroad.data = data;
                    self.dialog.setAutoBroad.visible = true;
                    self.dialog.setAutoBroad.loading = false;
                    self.dialog.setAutoBroad.autoBroadShuffle = data.autoBroadShuffle;
                    self.dialog.setAutoBroad.autoBroadStart = data.autoBroadStart;
                    self.dialog.setAutoBroad.autoBroadDuration = data.autoBroadDuration;
                },
                handleSetAutoBroadClose: function() {
                    var self = this;
                    self.dialog.setAutoBroad.visible = false;
                    self.dialog.setAutoBroad.loading = false;
                    self.dialog.setAutoBroad.data = {};
                    self.dialog.setAutoBroad.autoBroadShuffle = false;
                    self.dialog.setAutoBroad.autoBroadStart = '';
                    self.dialog.setAutoBroad.autoBroadDuration = 1;
                },
                handleSetAutoBroadCommit: function() {
                    var self = this;
                    self.dialog.setAutoBroad.data.autoBroadShuffle = self.dialog.setAutoBroad.autoBroadShuffle;
                    self.dialog.setAutoBroad.data.autoBroadStart = self.dialog.setAutoBroad.autoBroadStart;
                    self.dialog.setAutoBroad.data.autoBroadDuration = self.dialog.setAutoBroad.autoBroadDuration;
                    self.handleSetAutoBroadClose();
                },

                //set output dialog event
                handleSetOutput: function(data) {
                    var self = this;
                    self.dialog.setOutput.data = data;
                    self.dialog.setOutput.visible = true;
                    self.dialog.setOutput.loading = false;
                    self.dialog.setOutput.broadWay = self.dialog.setOutput.data.broadWay;
                    self.dialog.setOutput.outputQtUsers = [];
                    self.dialog.setOutput.outputPushUsers = [];
                    self.dialog.setOutput.outputUserPort = self.dialog.setOutput.data.outputUserPort;
                    self.dialog.setOutput.outputUserEndPort = self.dialog.setOutput.data.outputUserEndPort;
                    self.dialog.setOutput.outputCount = self.dialog.setOutput.data.outputCount;
                    self.dialog.setOutput.output = [];

                    var i = 0;
                    if (self.dialog.setOutput.data.outputQtUsers) {
                        for (i = 0; i < self.dialog.setOutput.data.outputQtUsers.length; i++) {
                            self.dialog.setOutput.outputQtUsers.push(self.dialog.setOutput.data.outputQtUsers[i]);
                        }
                    }

                    if (self.dialog.setOutput.data.outputPushUsers) {
                        for (i = 0; i < self.dialog.setOutput.data.outputPushUsers.length; i++) {
                            self.dialog.setOutput.outputPushUsers.push(self.dialog.setOutput.data.outputPushUsers[i]);
                        }
                    }

                    if (self.dialog.setOutput.data.output) {
                        for (i = 0; i < self.dialog.setOutput.data.output.length; i++) {
                            self.dialog.setOutput.output.push(self.dialog.setOutput.data.output[i]);
                        }
                    }
                },
                handleSetOutputClose: function () {
                    var self = this;
                    self.dialog.setOutput.visible = false;
                    self.dialog.setOutput.loading = false;
                    self.dialog.setOutput.data = {};
                    self.dialog.setOutput.broadWay = '';
                    self.dialog.setOutput.outputQtUsers = [];
                    self.dialog.setOutput.outputPushUsers = [];
                    self.dialog.setOutput.outputUserPort = '';
                    self.dialog.setOutput.outputUserEndPort = '';
                    self.dialog.setOutput.outputCount = 1;
                    self.dialog.setOutput.output = [];
                },
                //添加流输出数的监听
                handleOutputCountChange: function(currentValue) {
                    var self = this;
                    if (self.dialog.setOutput.output.length <= currentValue){
                        for (var i = 0; i < currentValue - self.dialog.setOutput.output.length; i++) {
                            var output = {
                                previewUrlIp : '',
                                previewUrlPort : '',
                                previewUrlEndPort: ''
                            };
                            self.dialog.setOutput.output.push(output);
                        }
                    } else {
                        self.dialog.setOutput.output.splice(currentValue, self.dialog.setOutput.output.length - currentValue);
                    }
                },
                handleUserRemove:function(user, value){
                    var index = user.indexOf(value);
                    if(index != -1){
                        user.splice(index, 1);
                    }
                },
                handleSetOutputUserSet: function(data, type) {
                    var self = this;
                    self.$refs.selectUserDialog.open('/cs/channel/quest/user/list', data, type);
                },
                selectedUsers: function (buff, users, startLoading, endLoading, close) {
                    var self = this;
                    startLoading();
                    buff.splice(0,buff.length);
                    for(var i=0; i<users.length; i++){
                        buff.push(users[i]);
                    }
                    endLoading();
                    close();
                },
                handleSetOutputCommit: function() {
                    var self = this;
                    self.dialog.setOutput.loading = true;
                    var i = 0;

                    if (self.dialog.setOutput.data.outputQtUsers) {
                        self.dialog.setOutput.data.outputQtUsers.splice(0, self.dialog.setOutput.data.outputQtUsers.length);
                        for (i = 0; i < self.dialog.setOutput.outputQtUsers.length; i++) {
                            self.dialog.setOutput.data.outputQtUsers.push(self.dialog.setOutput.outputQtUsers[i]);
                        }
                    }

                    if (self.dialog.setOutput.data.outputPushUsers) {
                        self.dialog.setOutput.data.outputPushUsers.splice(0, self.dialog.setOutput.data.outputPushUsers.length);
                        for (i = 0; i < self.dialog.setOutput.outputPushUsers.length; i++) {
                            self.dialog.setOutput.data.outputPushUsers.push(self.dialog.setOutput.outputPushUsers[i]);
                        }
                    }

                    if (self.dialog.setOutput.data.output) {
                        self.dialog.setOutput.data.output.splice(0, self.dialog.setOutput.data.output.length);
                        for (i = 0; i < self.dialog.setOutput.output.length; i++) {
                            self.dialog.setOutput.data.output.push(self.dialog.setOutput.output[i]);
                        }
                    }

                    self.dialog.setOutput.data.outputUserPort = self.dialog.setOutput.outputUserPort;
                    self.dialog.setOutput.data.outputUserEndPort = self.dialog.setOutput.outputUserEndPort;
                    self.dialog.setOutput.data.outputCount = self.dialog.setOutput.outputCount;

                    self.handleSetOutputClose();
                },

                updateMenuToTerminal: function (scope) {
                    var self = this;
                    var row = scope.row;
                    self.showTip('', '此操作将对频道下的所有地区终端做资源同步，是否继续?', function(callback) {
                        var questData = {
                            channelId: row.id
                        };
                        ajax.post('/cs/channel/update/to/terminal', questData, function (data, status) {
                            callback();
                        }, null, ajax.NO_ERROR_CATCH_CODE);
                    })
                },
                rowDelete: function (scope) {
                    var self = this;
                    var row = scope.row;
                    self.showTip('', '此操作将永久删除频道，且不可恢复，是否继续?', function(callback) {
                        var questData = {
                            id: row.id
                        };
                        ajax.post('/cs/channel/remove', questData, function (data, status) {
                            callback();
                            if (status != 200) return;
                            self.getChannelList();
                        }, null, ajax.NO_ERROR_CATCH_CODE);
                    });
                },
                toggleSelection: function (rows) {
                    if (rows) {
                        for (var i = 0; i < rows.length; i++) {
                            this.$refs.multipleChannelTable.toggleRowSelection(rows[i]);
                        }
                    } else {
                        this.$refs.multipleChannelTable.clearSelection();
                    }
                },
                handleSelectionChange: function (val) {
                    this.channel.multipleSelection = val;
                },
                handlePageCurrentChange: function (val) {
                    var self = this;
                    self.channel.page.currentPage = val;
                    self.getChannelList();
                },
                handleSizeChange: function (val) {
                    var self = this;
                    self.channel.page.size = val;
                    self.getChannelList();
                },

                //menu dialog event
                editMenu: function (scope) {
                    var self = this;
                    self.dialog.editMenu.data = scope.row;
                    self.dialog.editMenu.visible = true;
                    self.loadMenuTree();
                },
                handleEditMenuClose: function () {
                    var self = this;
                    self.dialog.editMenu.data = "";
                    self.dialog.editMenu.visible = false;
                },
                loadMenuTree: function () {
                    var self = this;
                    self.dialog.editMenu.tree.loading = true;
                    self.dialog.editMenu.tree.data.splice(0, self.dialog.editMenu.tree.data.length);
                    self.dialog.editMenu.tree.data.push({
                        id: -1,
                        uuid: '-1',
                        name: '资源目录',
                        icon: 'icon-tag',
                        style: 'font-size:15px; position:relative; top:1px; margin-right:1px;'
                    });
                    var questData = {channelId: self.dialog.editMenu.data.id};
                    ajax.post('/cs/menu/list/tree', questData, function (data, status) {
                        self.dialog.editMenu.tree.loading = false;
                        if (data && data.length > 0) {
                            for (var i = 0; i < data.length; i++) {
                                self.dialog.editMenu.tree.data.push(data[i]);
                            }
                        }
                        self.currentNode(self.dialog.editMenu.tree.data[0]);
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                treeNodeAllowDrag: function (node) {
                    return node.data.id !== -1;
                },
                treeNodeAllowDrop: function (dragNode, dropNode, type) {
                    return type === 'inner' && dropNode.data.id !== -1;
                },
                treeNodeDrop: function (dragNode, dropNode, type) {
                    var self = this;
                    self.dialog.editMenu.tree.loading = true;
                    var nodeData = dragNode.data;
                    var parentData = dropNode.data;
                    var questData = {
                        id: nodeData.id,
                        newParentId: parentData.id
                    };
                    ajax.post('/cs/menu/move', questData, function (data, status) {
                        self.dialog.editMenu.tree.loading = false;
                        if (status != 200) return;
                        self.loadMenuTree();
                    }, null, ajax.NO_ERROR_CATCH_CODE)
                },
                addRootTreeNode: function () {
                    var self = this;
                    var questData = {
                        channelId: self.dialog.editMenu.data.id,
                        name: "新建的节点"
                    };
                    self.dialog.editMenu.tree.loading = true;
                    ajax.post('/cs/menu/add/root', questData, function (data, status) {
                        self.dialog.editMenu.tree.loading = false;
                        if (status != 200) return;
                        self.dialog.editMenu.tree.data.push(data);
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                currentTreeNodeChange: function (data) {
                    var self = this;
                    self.currentNode(data);
                },
                treeNodeTop: function (node, parentData) {
                    var self = this;
                    self.dialog.editMenu.tree.loading = true;
                    var questData = {
                        id: node.data.id,
                        newParentId: parentData.parentId
                    };
                    ajax.post('/cs/menu/move', questData, function (data, status) {
                        self.dialog.editMenu.tree.loading = false;
                        if (status != 200) return;
                        self.loadMenuTree();
                    }, null, ajax.NO_ERROR_CATCH_CODE)
                },
                treeNodeEdit: function (node, data) {
                    var self = this;
                    self.dialog.editMenu.dialog.editNode.data = data;
                    self.dialog.editMenu.dialog.editNode.name = data.name;
                    self.dialog.editMenu.dialog.editNode.remark = data.remark;
                    self.dialog.editMenu.dialog.editNode.visible = true;
                },
                treeNodeAppend: function (parentNode, parent) {
                    var self = this;
                    self.dialog.editMenu.tree.loading = true;
                    var questData = {
                        id: parent.id,
                        channelId: parent.channelId,
                        name: "新建的节点"
                    };
                    ajax.post('/cs/menu/append', questData, function (data, status) {
                        self.dialog.editMenu.tree.loading = false;
                        if (status != 200) return;
                        if (!parent.subColumns) {
                            Vue.set(parent, 'subColumns', []);
                        }
                        parent.subColumns.push(data);
                    }, null, ajax.NO_ERROR_CATCH_CODE)
                },
                treeNodeDelete: function (node, data) {
                    var self = this;
                    self.showTip('', '此操作将永久删除标签以及子标签，且不可恢复，是否继续?', function(callback) {
                        var questData = {id: data.id};
                        ajax.post('/cs/menu/remove', questData, function (data, status) {
                            callback();
                            if (status != 200) return;
                            self.loadMenuTree();
                        }, null, ajax.NO_ERROR_CATCH_CODE);
                    });
                },
                handleEditNodeClose: function () {
                    var self = this;
                    self.dialog.editMenu.dialog.editNode.data = '';
                    self.dialog.editMenu.dialog.editNode.name = '';
                    self.dialog.editMenu.dialog.editNode.remark = '';
                    self.dialog.editMenu.dialog.editNode.visible = false;
                },
                handleEditNodeCommit: function () {
                    var self = this;
                    var nodeData = self.dialog.editMenu.dialog.editNode;
                    nodeData.loading = true;
                    if (nodeData.name == nodeData.data.name && nodeData.remark == nodeData.data.remark) {
                        nodeData.loading = false;
                        self.handleEditNodeClose();
                    } else {
                        var questData = {
                            id: nodeData.data.id,
                            name: nodeData.name,
                            remark: nodeData.remark
                        };
                        ajax.post('/cs/menu/edit', questData, function (data, status) {
                            nodeData.loading = false;
                            if (status != 200) return;
                            self.handleEditNodeClose();
                            self.loadMenuTree();
                        }, null, ajax.NO_ERROR_CATCH_CODE)
                    }
                },
                currentNode: function (data) {
                    var self = this;
                    if (!data || data.id == -1) {
                        self.dialog.editMenu.tree.current = '';
                        return;
                    }
                    self.dialog.editMenu.tree.current = data;
                    self.$nextTick(function () {
                        self.$refs.tagTree.setCurrentKey(data.uuid);
                    });
                    self.loadingMenuResource();
                },
                loadingMenuResource: function () {
                    var self = this;
                    if (self.dialog.editMenu.resource.data)
                        self.dialog.editMenu.resource.data.splice(0, self.dialog.editMenu.resource.data.length);
                    var questData = {
                        id: self.dialog.editMenu.tree.current.id
                    };
                    ajax.post('/cs/menu/resource/get', questData, function (data, status) {
                        if (status != 200) return;
                        if (data && data.length > 0) {
                            for (var i = 0; i < data.length; i++) {
                                self.dialog.editMenu.resource.data.push(data[i])
                            }
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE)
                },
                loadingMenuResourceTree: function () {
                    var self = this;
                    self.dialog.editMenu.dialog.chooseResource.visible = true;
                    self.dialog.editMenu.dialog.chooseResource.tree.data.splice(0, self.dialog.editMenu.dialog.chooseResource.tree.data.length);
                    var questData = {
                        id: self.dialog.editMenu.tree.current.id,
                        channelId: self.dialog.editMenu.data.id
                    };
                    ajax.post('/cs/menu/resource/get/mims', questData, function (data, status) {
                        if (status != 200) return;
                        if (data && data.length > 0) {
                            for (var i = 0; i < data.length; i++) {
                                self.dialog.editMenu.dialog.chooseResource.tree.data.push(data[i]);
                            }
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                handleMenuResourceCheckChange: function (data, checked, indeterminate) {
                    var self = this;
                    self.dialog.editMenu.dialog.chooseResource.chooseNode = this.$refs.menuResourceTree.getCheckedNodes(true, false);
                },
                handleChooseResourceClose: function () {
                    var self = this;
                    self.dialog.editMenu.dialog.chooseResource.chooseNode = [];
                    self.dialog.editMenu.dialog.chooseResource.visible = false;
                },
                handleChooseResourceCommit: function () {
                    var self = this;
                    self.dialog.editMenu.dialog.chooseResource.loading = true;
                    var questData = {
                        resourcesListStr: JSON.stringify(self.dialog.editMenu.dialog.chooseResource.chooseNode),
                        parentId: self.dialog.editMenu.tree.current.id,
                        channelId: self.dialog.editMenu.tree.current.channelId
                    };
                    ajax.post('/cs/menu/resource/add', questData, function (data, status) {
                        self.dialog.editMenu.dialog.chooseResource.loading = false;
                        self.handleChooseResourceClose();
                        if (status != 200) return;
                        self.dialog.editMenu.resource.data.splice(0, self.dialog.editMenu.resource.data.length);
                        if (data && data.length > 0) {
                            for (var i = 0; i < data.length; i++) {
                                self.dialog.editMenu.resource.data.push(data[i]);
                            }
                        }
                        self.$message({
                            message: '添加成功',
                            type: 'success'
                        });
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                menuResourceDelete: function (scope) {
                    var self = this;
                    var row = scope.row;
                    self.showTip('', '删除资源操作，是否继续?', function(callback) {
                        var questData = {id: row.id};
                        ajax.post('/cs/menu/resource/remove', questData, function (data, status) {
                            callback();
                            if (status != 200) return;
                            self.$message({
                                message: '删除成功',
                                type: 'success'
                            });
                            for (var i = 0; i < self.dialog.editMenu.resource.data.length; i++) {
                                if (self.dialog.editMenu.resource.data[i].id == data.id) {
                                    self.dialog.editMenu.resource.data.splice(i, 1);
                                    break;
                                }
                            }
                            //self.loadingMenuResource();
                        }, null, ajax.NO_ERROR_CATCH_CODE)
                    });
                },

                //schedule dialog event
                editSchedule: function (scope) {
                    var self = this;
                    var row = scope.row;
                    self.dialog.editSchedules.visible = true;
                    self.dialog.editSchedules.data = row;
                    self.loadSchedule();
                },
                handleEditSchedulesClose : function () {
                    var self = this;
                    self.dialog.editSchedules.visible = false;
                    self.dialog.editSchedules.data = {};
                },
                loadSchedule: function(){
                    var self = this;
                    self.dialog.editSchedules.table.loading = true;
                    self.dialog.editSchedules.table.data.splice(0, self.dialog.editSchedules.table.data.length);
                    var questData = {
                        channelId: self.dialog.editSchedules.data.id,
                        currentPage: self.dialog.editSchedules.table.page.currentPage,
                        pageSize: self.dialog.editSchedules.table.page.size
                    };
                    ajax.post('/cs/schedule/get', questData, function(data, status){
                        if (status == 200){
                            if (data.data) {
                                for(var i=0; i < data.data.length; i++){
                                    self.dialog.editSchedules.table.data.push(data.data[i]);
                                }
                            }
                            self.dialog.editSchedules.table.page.total = data.total;
                        }
                        self.dialog.editSchedules.table.loading = false;
                    }, null, ajax.NO_ERROR_CATCH_CODE)
                },
                handleAddSchedule: function() {
                    var self = this;
                    self.dialog.editSchedules.dialog.addSchedule.visible = true;
                },
                handleAddScheduleClose: function(){
                    var self = this;
                    self.dialog.editSchedules.dialog.addSchedule.visible = false;
                    self.dialog.editSchedules.dialog.addSchedule.broadDate = "";
                    self.dialog.editSchedules.dialog.addSchedule.remark = "";
                },
                handleAddScheduleCommit: function() {
                    var self = this;
                    self.dialog.editSchedules.dialog.addSchedule.loading = true;
                    if (self.dialog.editSchedules.data.broadWay == self.broadWayStream && !self.dialog.editSchedules.dialog.addSchedule.broadDate) {
                        self.$message({
                            message: '请选择播发时间',
                            type: 'warning'
                        });
                        self.dialog.editSchedules.dialog.addSchedule.loading = false;
                        return;
                    }
                    var questData = {
                        channelId: self.dialog.editSchedules.data.id,
                        broadDate: self.dialog.editSchedules.dialog.addSchedule.broadDate,
                        remark: self.dialog.editSchedules.dialog.addSchedule.remark
                    };
                    ajax.post('/cs/schedule/add', questData, function(data, status){
                        if (status == 200){
                            if (self.dialog.editSchedules.table.data.length < self.dialog.editSchedules.table.page.size){
                                self.dialog.editSchedules.table.data.push(data);
                            }
                            self.dialog.editSchedules.table.page.total += 1;
                        }
                        self.dialog.editSchedules.dialog.addSchedule.loading = false;
                        self.handleAddScheduleClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                scheduleEdit: function (scope) {
                    var self = this;
                    self.dialog.editSchedules.dialog.editSchedule.data = scope.row;
                    self.dialog.editSchedules.dialog.editSchedule.broadDate = scope.row.broadDate;
                    self.dialog.editSchedules.dialog.editSchedule.remark = scope.row.remark;
                    self.dialog.editSchedules.dialog.editSchedule.visible = true;
                },
                handleEditScheduleClose: function(){
                    var self = this;
                    self.dialog.editSchedules.dialog.editSchedule.visible = false;
                    self.dialog.editSchedules.dialog.editSchedule.data = {};
                    self.dialog.editSchedules.dialog.editSchedule.broadDate = "";
                    self.dialog.editSchedules.dialog.editSchedule.remark = "";
                },
                handleEditScheduleCommit: function(){
                    var self = this;
                    self.dialog.editSchedules.dialog.editSchedule.loading = true;
                    var questData = {
                        id: self.dialog.editSchedules.dialog.editSchedule.data.id,
                        broadDate: self.dialog.editSchedules.dialog.editSchedule.broadDate,
                        remark: self.dialog.editSchedules.dialog.editSchedule.remark
                    };
                    ajax.post('/cs/schedule/edit', questData, function(data, status){
                        if (status == 200){
                            self.dialog.editSchedules.dialog.editSchedule.data.broadDate = data.broadDate;
                            self.dialog.editSchedules.dialog.editSchedule.data.remark = data.remark;
                        }
                        self.handleEditScheduleClose();
                        self.dialog.editSchedules.dialog.editSchedule.loading = false;
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                scheduleDelete: function (scope) {
                    var self = this;
                    var row = scope.row;
                    var index = scope.$index;
                    self.dialog.editSchedules.table.loading = true;
                    var questData = {
                        id: row.id
                    };
                    ajax.post('/cs/schedule/remove', questData, function (data, status) {
                        if (status == 200){
                            if(self.dialog.editSchedules.table.data.length == self.dialog.editSchedules.table.page.size){
                                self.loadSchedule();
                            }else{
                                self.dialog.editSchedules.table.data.splice(index, 1);
                            }
                            self.dialog.editSchedules.table.page.total -= 1;
                        }
                        self.dialog.editSchedules.table.loading = false;
                    }, null, ajax.NO_ERROR_CATCH_CODE)
                },

                seekBroadcast: function (scope) {
                    var self = this;
                    self.dialog.seekBroadcast.data = scope.row;
                    self.dialog.seekBroadcast.visible = true;
                },
                handleSeekBroadcastClose: function () {
                    var self = this;
                    self.dialog.seekBroadcast.loading = false;
                    self.dialog.seekBroadcast.visible = false;
                    self.dialog.seekBroadcast.data = {};
                    self.dialog.seekBroadcast.duration = "";
                },
                handleSeekBroadcastCommit: function () {
                    var self = this;
                    self.dialog.seekBroadcast.loading = true;
                    var questData = {
                        channelId: self.dialog.seekBroadcast.data.id,
                        duration: self.dialog.seekBroadcast.duration
                    };
                    ajax.post('/cs/channel/seek', questData, function(data,status){
                        self.dialog.seekBroadcast.loading = false;
                        self.handleSeekBroadcastClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },

                handlePreview: function (scope) {
                    var self = this;
                    var row = scope.row;
                    if(row.type == "AUDIO"){
                        self.$refs.Lightbox.preview(row.previewUrl, 'audio');
                    }else if(row.type == "VIDEO"){
                        self.$refs.Lightbox.preview(row.previewUrl, 'video');
                    }else if(row.type == 'PICTURE'){
                        self.$refs.Lightbox.preview(row.previewUrl, 'image');
                    }
                },
                editProgram: function (scope) {
                    var self = this;
                    self.test.visible = true;
                    self.$refs.programScreen.open('/cs/channel/template', self.dialog.editSchedules.data, scope.row);
                },

                //area dialog event
                manageBroadcastArea: function (scope) {
                    var self = this;
                    var row = scope.row;
                    self.dialog.manageBroadcastArea.visible = true;
                    self.dialog.manageBroadcastArea.data = row;
                    self.loadingArea();
                },
                loadingArea: function () {
                    var self = this;
                    var questData = {
                        channelId: self.dialog.manageBroadcastArea.data.id
                    };
                    self.dialog.manageBroadcastArea.tree.data = [];
                    self.dialog.manageBroadcastArea.tree.loading = true;
                    ajax.post('/cs/area/list', questData, function (data, status) {
                        self.dialog.manageBroadcastArea.tree.loading = false;
                        if (status != 200) return;
                        self.dialog.manageBroadcastArea.tree.data = data.treeData;
                        self.$refs.broadcastAreaTree.setCheckedKeys(data.checkList);
                        setTimeout(function () {
                            self.dialog.manageBroadcastArea.tree.current = self.$refs.broadcastAreaTree.getCheckedNodes(false, false);
                        }, 100);
                    }, null, ajax.NO_ERROR_CATCH_CODE)
                },
                manageBroadcastDivision: function (node, resolve) {
                    var self = this;
                    self.dialog.manageBroadcastArea.tree.loading = true;
                    if (node.level != 0) {
                        var questData = {
                            channelId: self.dialog.manageBroadcastArea.data.id,
                            divisionId: node.data.areaId,
                            disabled: node.data.disabled
                        };
                        ajax.post('/cs/area/children', questData, function (data, status) {
                            self.dialog.manageBroadcastArea.tree.loading = false;
                            if (status != 200) return;
                            resolve(data.treeData);
                            var checkList = self.$refs.broadcastAreaTree.getCheckedKeys(false, false);
                            if (!checkList) {
                                checkList = []
                            }
                            if (data.checkList) {
                                for (var i = 0; i < data.checkList.length; i++) {
                                    checkList.push(data.checkList[i])
                                }
                            }
                            self.$refs.broadcastAreaTree.setCheckedKeys(checkList);
                            setTimeout(function () {
                                self.dialog.manageBroadcastArea.tree.current = self.$refs.broadcastAreaTree.getCheckedNodes(false, false);
                            }, 100);
                        }, null, ajax.NO_ERROR_CATCH_CODE)
                    }
                },
                handleManageBroadcastAreaClose: function () {
                    var self = this;
                    self.dialog.manageBroadcastArea.visible = false;
                    self.dialog.manageBroadcastArea.data = "";
                    self.dialog.manageBroadcastArea.tree.data = [];
                    self.dialog.manageBroadcastArea.tree.current.splice(0, self.dialog.manageBroadcastArea.tree.current.length);
                },
                handleManageBroadcastAreaCommit: function () {
                    var self = this;
                    self.dialog.manageBroadcastArea.loading = true;
                    var questData = {
                        channelId: self.dialog.manageBroadcastArea.data.id,
                        areaListStr: JSON.stringify(self.dialog.manageBroadcastArea.tree.current)
                    };
                    ajax.post('/cs/area/set', questData, function (data, status, message) {
                        if (status == 200) {
                            self.dialog.manageBroadcastArea.loading = false;
                            self.handleManageBroadcastAreaClose();
                            self.$message({
                                message: '保存成功',
                                type: 'success'
                            });
                        } else if (status == 409) {
                            self.showTip('地区被占用', '是否强制使用已被占用地区，确定则取消其他频道占用?', function(callback) {
                                var questData = {
                                    channelId: self.dialog.manageBroadcastArea.data.id,
                                    areaListStr: JSON.stringify(self.dialog.manageBroadcastArea.tree.current)
                                };
                                ajax.post('/cs/area/set/force', questData, function (data, status) {
                                    callback();
                                    self.dialog.manageBroadcastArea.loading = false;
                                    if (status == 200) {
                                        self.handleManageBroadcastAreaClose();
                                        self.$message({
                                            message: '保存成功',
                                            type: 'success'
                                        });
                                    }
                                }, null, ajax.NO_ERROR_CATCH_CODE);
                            });
                        } else if (status == 403) {
                            this.$alert(message, '地区被占用', {
                                confirmButtonText: '确定'
                            });
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                handleManageBroadcastAreaCheckChange: function (data, checked, indeterminate) {
                    var self = this;
                    self.dialog.manageBroadcastArea.tree.current = self.$refs.broadcastAreaTree.getCheckedNodes(false, false);
                },

                //upgrade dialog event
                handleTerminalUpgrade: function() {
                    var self = this;
                    self.dialog.upgrade.visible = true;
                },
                handleCompressSelect: function(buff) {
                    var self = this;
                    self.$refs.selectCompress.setBuffer(buff);
                    self.$refs.selectCompress.open();
                },
                selectedCompress: function(check, buff, startLoading, endLoading, done) {
                    Vue.set(buff, 'name', check.name);
                    //buff.name = check.name;
                    buff.previewUrl = check.previewUrl;
                    buff.size = check.size;
                    done();
                },
                handleAreaSelect: function(channelData, check) {
                    var self = this;
                    self.$refs.areaPicker.open(check, channelData);
                },
                handleAreaPickerClose: function(check, channelData, choice, closeFunc) {
                    var self = this;
                    if (check) {
                        check.splice(0, check.length);
                        if (choice) {
                            for(var i = 0; i < choice.length; i++) {
                                check.push(choice[i])
                            }
                        }
                    } else if (channelData) {

                    }
                    closeFunc();
                },
                handleUpgradeClose: function() {
                    var self = this;
                    self.dialog.upgrade.visible = false;
                    self.dialog.upgrade.loading = false;
                    self.dialog.upgrade.version = '';
                    self.dialog.upgrade.way = '';
                    self.dialog.upgrade.tarCheck = {};
                    self.dialog.upgrade.areaCheck = [];
                },
                handleUpgradeCommit: function() {
                    var self = this;
                    self.dialog.upgrade.loding = true;
                    if (!self.dialog.upgrade.version) {
                        self.$message({
                            message: '请输入版本号',
                            type: 'warning'
                        });
                        return
                    }
                    if (!self.dialog.upgrade.way) {
                        self.$message({
                            message: '请选择播发方式',
                            type: 'warning'
                        });
                        return
                    }
                    if (!self.dialog.upgrade.tarCheck.name || !self.dialog.upgrade.tarCheck.previewUrl) {
                        self.$message({
                            message: '请选择下发升级包',
                            type: 'warning'
                        });
                        return
                    }
                    if (!self.dialog.upgrade.areaCheck || self.dialog.upgrade.areaCheck.length <= 0) {
                        self.$message({
                            message: '请选择至少一个地区升级',
                            type: 'warning'
                        });
                        return
                    }
                    var questData = {
                        version: self.dialog.upgrade.version,
                        way: self.dialog.upgrade.way,
                        compress: JSON.stringify(self.dialog.upgrade.tarCheck),
                        areaList: JSON.stringify(self.dialog.upgrade.areaCheck)
                    };
                    ajax.post('/cs/upgrade/start', questData, function (data, status) {
                        if (status == 200) {
                            self.$message({
                                message: '升级请求下发成功',
                                type: 'success'
                            });
                            self.handleUpgradeClose();
                        }
                        self.dialog.upgrade.loading = false;
                    })
                },

                //broad event
                chooseBroadMedia: function () {
                    var self = this;
                    self.$refs.mediaPicker.open('/cs/menu/list/tree/with/resource', self.dialog.chooseBroadMedia.data, []);
                },
                chooseBroadMediaClose: function () {
                    var self = this;
                    self.dialog.chooseBroadMedia.visible = false;
                    self.dialog.chooseBroadMedia.data = {};
                },
                chooseBroadMediaConfirm: function() {
                    var self = this;
                    var questData = {
                        channelId: self.dialog.chooseBroadMedia.data.id
                    };
                    self.loading = true;
                    self.loadingText = "正在请求播发";
                    ajax.post('/cs/channel/broadcast/start', questData, function (data, status) {
                        self.loading = false;
                        if (status == 200) {
                            self.$message({
                                message: '请求播发成功',
                                type: 'success'
                            });
                        }
                        self.getChannelList();
                        self.dialog.chooseBroadMedia.visible = false;
                    }, null, ajax.NO_ERROR_CATCH_CODE)
                },
                handleMediaPickerClose: function (channelData, buff, checked, startLoading, endLoading, close) {
                    var self = this;
                    startLoading();
                    buff.splice(0,buff.length);
                    var resourceIds = [];
                    for(var i=0; i<checked.length; i++){
                        buff.push(checked[i]);
                        resourceIds.push(checked[i].id);
                    }
                    endLoading();
                    close();
                    self.loading = true;
                    self.loadingText = "正在请求播发";
                    var questData = {
                        channelId: channelData.id,
                        resourceIds: JSON.stringify(resourceIds)
                    };
                    ajax.post('/cs/channel/broadcast/start', questData, function (data, status) {
                        self.loading = false;
                        if (status == 200) {
                            self.$message({
                                message: '请求播发成功',
                                type: 'success'
                            });
                        }
                        self.getChannelList();
                        self.chooseBroadMediaClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                startBroadcast: function (scope) {
                    var self = this;
                    var row = scope.row;
                    var questData = {
                        channelId: row.id
                    };
                    if (row.broadWay == self.broadWayTerminal) {
                        self.loadingText = "正在更新播发状态";
                        self.loading = true;
                        ajax.post('/cs/channel/broadcast/status', questData, function (data, status) {
                            self.loading = false;
                            self.loadingText = "";
                            if (status == 200) {
                                switch (data) {
                                    case "发送中":
                                    {
                                        self.$message.error("当前频道正在播发，已为您更新播发状态");
                                        self.getChannelList();
                                        break;
                                    }
                                    case "":
                                    case "发送完成":
                                    {
                                        //self.$confirm("当前状态正常，是否执行播发任务?", "提示", {
                                        //    type: 'wraning',
                                        //    confirmButtonText: '确定',
                                        //    cancelButtonText: '取消'
                                        //}).then(
                                        //    function () {
                                            //    self.loading = true;
                                            //    self.loadingText = "正在请求播发";
                                            //    ajax.post('/cs/channel/broadcast/start', questData, function (data, status) {
                                            //        self.loading = false;
                                            //        if (status == 200) {
                                            //            self.$message({
                                            //                message: '请求播发成功',
                                            //                type: 'success'
                                            //            });
                                            //        }
                                            //        self.getChannelList();
                                            //    }, null, ajax.NO_ERROR_CATCH_CODE)
                                        //    }
                                        //).catch(function () {
                                        //        self.getChannelList();
                                        //    });
                                        //self.chooseBroadMedia(row);
                                        self.dialog.chooseBroadMedia.visible = true;
                                        self.dialog.chooseBroadMedia.data = row;
                                        break;
                                    }
                                    case "发送停止":
                                    {
                                        self.$confirm("当前频道播发状态已被停止，是否继续播发任务?", "提示", {
                                            type: 'wraning',
                                            confirmButtonText: '确定',
                                            cancelButtonText: '取消'
                                        }).then(
                                            function () {
                                                self.loading = true;
                                                self.loadingText = "正在请求播发";
                                                ajax.post('/cs/channel/broadcast/restart', questData, function (data, status) {
                                                    self.loading = false;
                                                    if (status == 200) {
                                                        self.$message({
                                                            message: '请求播发成功',
                                                            type: 'success'
                                                        });
                                                    }
                                                    self.getChannelList();
                                                }, null, ajax.NO_ERROR_CATCH_CODE)
                                            }
                                        ).catch(function () {
                                                self.getChannelList();
                                            });
                                        break;
                                    }
                                    default :
                                    {
                                        self.getChannelList();
                                        break;
                                    }
                                }
                            }
                        }, null, ajax.NO_ERROR_CATCH_CODE);
                    }else{
                        self.$confirm("当前状态正常，是否执行播发任务?", "提示", {
                            type: 'wraning',
                            confirmButtonText: '确定',
                            cancelButtonText: '取消'
                        }).then(
                            function () {
                                self.loading = true;
                                self.loadingText = "正在请求播发";
                                ajax.post('/cs/channel/broadcast/start', questData, function (data, status) {
                                    self.loading = false;
                                    if (status == 200) {
                                        self.$message({
                                            message: '请求播发成功',
                                            type: 'success'
                                        });
                                    }
                                    self.getChannelList();
                                }, null, ajax.NO_ERROR_CATCH_CODE)
                            }
                        ).catch(function () {
                                self.getChannelList();
                            });
                    }
                },
                stopBroadcast: function (scope) {
                    var self = this;
                    var row = scope.row;
                    var questData = {
                        channelId: row.id
                    };
                    self.loadingText = "正在请求停止播发";
                    self.loading = true;
                    ajax.post('/cs/channel/broadcast/stop', questData, function (data, status) {
                        self.loading = false;
                        self.loadingText = "";
                        if (status == 200) {
                            self.$message({
                                message: '请求停止播发成功',
                                type: 'success'
                            });
                        }
                        self.getChannelList();
                    }, null, ajax.NO_ERROR_CATCH_CODE)
                },


                manageBroad: function (scope) {
                    var self = this;
                    var row = scope.row;
                    self.dialog.manageBroad.visible = true;
                },
                handleManageBroadClose: function () {
                    var self = this;
                    self.dialog.manageBroad.visible = false;
                },
                handleManageBroadCommit: function () {

                },
                addBroad: function () {
                    var self = this;
                    self.dialog.manageBroad.dialog.addBroad.visible = true;
                },
                startBroad: function (scope) {

                },
                stopBroad: function (scope) {

                },
                handleBroadCheckChange: function (data, checked, indeterminate) {
                    var self = this;
                    self.dialog.manageBroad.dialog.addBroad.chooseNode = this.$refs.broadTree.getCheckedNodes(true, false);
                },
                handleAddBroadClose: function () {
                    var self = this;
                    self.dialog.manageBroad.dialog.addBroad.chooseNode = [];
                    self.dialog.manageBroad.dialog.addBroad.visible = false;
                },
                handleAddBroadCommit: function () {
                    var self = this;
                    self.dialog.manageBroad.dialog.addBroad.loading = true;
                    setTimeout(function () {
                        self.dialog.manageBroad.dialog.addBroad.loading = false;
                        self.handleAddBroadClose()
                    }, 1000)
                },
                tarTest: function () {
                    ajax.post('/cs/channel/tarTest', null, function (data, status) {

                    }, null, ajax.NO_ERROR_CATCH_CODE)
                },
                getServerTime: function() {
                    var self = this;
                    ajax.post('/cs/channel/time', null, function(data, status) {
                        self.time = data;
                        setInterval(function () {
                            var date = new Date(self.time);
                            var longDate = date.getTime() + 1000;
                            var newDate = new Date(longDate);
                            var year=newDate.getFullYear();
                            var month=self.dateCheck(newDate.getMonth() + 1);
                            var day=self.dateCheck(newDate.getDate());

                            //获取时分秒
                            var h=newDate.getHours();
                            var m=newDate.getMinutes();
                            var s=newDate.getSeconds();

                            //检查是否小于10
                            h=self.dateCheck(h);
                            m=self.dateCheck(m);
                            s=self.dateCheck(s);
                            self.time = year + '-' + month + '-' + day + ' ' + h + ':' + m + ':' + s;
                        }, 1000)
                    })
                },
                dateCheck: function(data) {
                    return data < 10 ? '0' + data : data
                },
                showTip: function(title, text, confirmListener) {
                    var self = this;
                    var h = self.$createElement;
                    self.$msgbox({
                        title: title ? title : '危险操作',
                        message: h('div', null, [
                            h('div', {class: 'el-message-box__status el-icon-warning'}, null),
                            h('div', {class: 'el-message-box__message'}, [
                                h('p', null, [text])
                            ])
                        ]),
                        type: 'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose: function (action, instance, done) {
                            instance.confirmButtonLoading = true;
                            if (action === 'confirm') {
                                confirmListener(function() {
                                    instance.confirmButtonLoading = false;
                                    done();
                                })
                            } else {
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function () {
                    });
                }
            },
            created: function () {
                var self = this;
                self.getServerTime();
                self.getChannelList();
            },
            mounted: function () {

            }
        });
    };

    var destroy = function () {

    };

    var groupList = {
        path: '/' + pageId,
        component: {
            template: '<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init: init,
        destroy: destroy
    };

    return groupList;

});