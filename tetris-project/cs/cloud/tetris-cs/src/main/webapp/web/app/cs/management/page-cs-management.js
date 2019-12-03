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
                menus: context.getProp('menus'),
                user: context.getProp('user'),
                groups: context.getProp('groups'),
                loading: false,
                loadingText: "",
                screen: {
                    one: "一分屏",
                    four: "四分屏",
                    six: "六分屏",
                    nine: "九分屏"
                },
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
                dialog: {
                    addProgram: {
                        visible: false,
                        loading: false,
                        name: "",
                        broadWay: "",
                        outputQtUsers: [],
                        outputPushUsers: [],
                        outputUserPort: "",
                        outputCount: 1,
                        output: [],
                        remark: "",
                        encryption: false,
                        autoBroad: false,
                        autoBroadShuffle: false,
                        autoBroadDuration: 1,
                        autoBroadStart: "",
                        date: ""
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
                        outputCount: 1,
                        output: [],
                        remark: "",
                        encryption: false,
                        autoBroad: false,
                        autoBroadShuffle: false,
                        autoBroadDuration: 1,
                        autoBroadStart: ""
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
                    }
                }
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
                    //    var h = self.$createElement;
                    //    self.$msgbox({
                    //        title: '危险操作',
                    //        message: h('div', null, [
                    //            h('div', {class: 'el-message-box__status el-icon-warning'}, null),
                    //            h('div', {class: 'el-message-box__message'}, [
                    //                h('p', null, ['此操作将永久删除频道，且不可恢复，是否继续?'])
                    //            ])
                    //        ]),
                    //        type: 'wraning',
                    //        showCancelButton: true,
                    //        confirmButtonText: '确定',
                    //        cancelButtonText: '取消',
                    //        beforeClose: function (action, instance, done) {
                    //            instance.confirmButtonLoading = true;
                    //            if (action === 'confirm' && self.channel.multipleSelection) {
                    //                var idList = [];
                    //                for (var i = 0; i < self.channel.multipleSelection; i++) {
                    //                    idList.push(self.channel.multipleSelection[i]);
                    //                }
                    //                var questData = {id: idList};
                    //                ajax.post('/cs/channel/remove', questData, function (data, status) {
                    //                    instance.confirmButtonLoading = false;
                    //                    done();
                    //                    self.channel.multipleSelection = [];
                    //                    slef.getChannelList();
                    //                }, null, ajax.NO_ERROR_CATCH_CODE);
                    //            } else {
                    //                instance.confirmButtonLoading = false;
                    //                done();
                    //            }
                    //        }
                    //    }).catch(function () {
                    //    });
                    //}
                },
                handleAddProgram: function(){
                    var self = this;
                    self.dialog.addProgram.broadWay = "轮播推流";
                    var t = new Date();
                    self.dialog.addProgram.date = t.getFullYear()+"-"+(t.getMonth()+1)+"-"+t.getDate()+" "+t.getHours()+":"+t.getMinutes()+":"+t.getSeconds();
                    var output = {
                        "previewUrlIp" : "",
                        "previewUrlPort" : ""
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
                    self.dialog.addProgram.outputQtUsers = [];
                    self.dialog.addProgram.outputPushUsers = [];
                    self.dialog.addProgram.outputUserPort = "";
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
                //添加流输出数的监听
                handleAddProgramOutputCount: function (currentValue, oldValue) {
                    var self = this;
                    if (self.dialog.addProgram.output.length <= currentValue){
                        for (var i = 0; i < currentValue - self.dialog.addProgram.output.length; i++) {
                            var output = {
                                "previewUrlIp" : "",
                                "previewUrlPort" : ""
                            };
                            self.dialog.addProgram.output.push(output);
                        }
                    } else {
                        self.dialog.addProgram.output.splice(currentValue, self.dialog.addProgram.output.length - currentValue);
                    }
                },
                handleUserRemove:function(user, value){
                    var index = user.indexOf(value);
                    if(index != -1){
                        user.splice(index, 1);
                    }
                },
                handleAddProgramQtUserSet: function () {
                    var self = this;
                    self.$refs.selectUserDialog.open('/cs/channel/quest/user/list', self.dialog.addProgram.outputQtUsers, "QT");
                },
                handleAddProgramPushUserSet: function () {
                    var self = this;
                    self.$refs.selectUserDialog.open('/cs/channel/quest/user/list', self.dialog.addProgram.outputPushUsers, "PUSH");
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
                handleAddProgramCommit: function () {
                    var self = this;
                    if (self.dialog.addProgram.autoBroad && !self.dialog.addProgram.autoBroadStart) {
                        this.$message({
                            message: '请选择自动播发起始时间',
                            type: 'warning'
                        });
                        return;
                    }
                    if (self.dialog.addProgram.broadWay == "轮播推流" && self.dialog.addProgram.outputQtUsers.length <= 0) {
                        var noOut = true;
                        for (var i = 0; i < self.dialog.addProgram.outputQtUsers.length; i++){
                            if (self.dialog.addProgram.outputQtUsers.previewUrlIp.trim() != "" && self.dialog.addProgram.outputQtUsers.previewUrlPort.trim() != ""){
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
                    if (self.dialog.addProgram.broadWay == "下载文件" && !self.dialog.addProgram.outputQtUsers.length && !self.dialog.addProgram.outputPushUsers.length){
                        this.$message({
                            message: '请选择至少一个播发用户',
                            type: 'warning'
                        });
                        return;
                    }
                    self.dialog.addProgram.loading = true;
                    var newData = {
                        name: self.dialog.addProgram.name,
                        date: self.dialog.addProgram.date,
                        broadWay: self.dialog.addProgram.broadWay,
                        outputUsers: JSON.stringify(self.dialog.addProgram.broadWay == "轮播推流" ? self.dialog.addProgram.outputQtUsers : self.dialog.addProgram.outputQtUsers.concat(self.dialog.addProgram.outputPushUsers)),
                        outputUserPort: self.dialog.addProgram.outputUserPort,
                        output: JSON.stringify(self.dialog.addProgram.output),
                        encryption: self.dialog.addProgram.encryption,
                        autoBroad: self.dialog.addProgram.autoBroad,
                        autoBroadShuffle: self.dialog.addProgram.autoBroadShuffle,
                        autoBroadDuration: self.dialog.addProgram.autoBroadDuration,
                        autoBroadStart: self.dialog.addProgram.autoBroadStart,
                        remark: self.dialog.addProgram.remark
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
                editChannel: function (scope) {
                    var self = this;
                    var row = scope.row;
                    self.dialog.editChannel.data = row;
                    self.dialog.editChannel.name = row.name;
                    self.dialog.editChannel.broadWay = row.broadWay;
                    if (row.broadWay == "下载文件" && row.outputUsers) {
                        for (var i = 0; i < row.outputUsers.length; i++) {
                            var item = row.outputUsers[i];
                            switch(item.equipType) {
                                case "QT":
                                    self.dialog.editChannel.outputQtUsers.push(item);
                                    break;
                                case "PUSH":
                                    self.dialog.editChannel.outputPushUsers.push(item);
                                    break;
                                default :
                                    break;
                            }
                        }
                    }
                    self.dialog.editChannel.outputUserPort = row.outputUserPort;
                    if (row.output) self.dialog.editChannel.outputCount = row.output.length;
                    self.dialog.editChannel.output = row.output;
                    self.dialog.editChannel.remark = row.remark;
                    self.dialog.editChannel.encryption = row.encryption;
                    self.dialog.editChannel.autoBroad = row.autoBroad;
                    self.dialog.editChannel.autoBroadShuffle = row.autoBroadShuffle;
                    self.dialog.editChannel.autoBroadDuration = row.autoBroadDuration;
                    self.dialog.editChannel.autoBroadStart = row.autoBroadStart;
                    self.dialog.editChannel.visible = true;
                },
                handleEditChannelOutputCount: function (currentValue, oldValue) {
                    var self = this;
                    if (self.dialog.editChannel.output.length <= currentValue){
                        for (var i = 0; i < currentValue - self.dialog.editChannel.output.length; i++) {
                            var output = {
                                "previewUrlIp" : "",
                                "previewUrlPort" : ""
                            };
                            self.dialog.editChannel.output.push(output);
                        }
                    } else {
                        self.dialog.editChannel.output.splice(currentValue, self.dialog.editChannel.output.length - currentValue);
                    }
                },
                handleEditChannelClose: function () {
                    var self = this;
                    self.dialog.editChannel.visible = false;
                    self.dialog.editChannel.data = "";
                    self.dialog.editChannel.name = "";
                    self.dialog.editChannel.broadWay = "";
                    self.dialog.editChannel.outputQtUsers = [];
                    self.dialog.editChannel.outputPushUsers = [];
                    self.dialog.editChannel.outputUserPort = "";
                    self.dialog.editChannel.outputCount = 1;
                    self.dialog.editChannel.output = [];
                    self.dialog.editChannel.remark = "";
                    self.dialog.editChannel.encryption  = false;
                    self.dialog.editChannel.autoBroad = false;
                    self.dialog.editChannel.autoBroadShuffle = false;
                    self.dialog.editChannel.autoBroadDuration = 1;
                    self.dialog.editChannel.autoBroadStart = "";
                },
                handleEditChannelQtUserSet: function () {
                    var self = this;
                    self.$refs.selectUserDialog.open('/cs/channel/quest/user/list', self.dialog.editChannel.outputQtUsers, "QT");
                },
                handleEditChannelCommitSend: function (listener) {
                    var self = this;
                    self.dialog.editChannel.loading = true;
                    var newName = self.dialog.editChannel.name;
                    var newRemark = self.dialog.editChannel.remark;
                    var outputQtUsers = self.dialog.editChannel.outputQtUsers;
                    var outputPushUsers = self.dialog.editChannel.outputPushUsers;
                    var outputUserPort = self.dialog.editChannel.outputUserPort;
                    var output = self.dialog.editChannel.output;
                    var encryption = self.dialog.editChannel.encryption;
                    var autoBroad = self.dialog.editChannel.autoBroad;
                    var autoBroadShuffle = self.dialog.editChannel.autoBroadShuffle;
                    var autoBroadDuration = self.dialog.editChannel.autoBroadDuration;
                    var autoBroadStart = self.dialog.editChannel.autoBroadStart;
                    var questData = {
                        id: self.dialog.editChannel.data.id,
                        name: newName,
                        outputUsers: JSON.stringify(outputQtUsers.concat(outputPushUsers)),
                        outputUserPort: outputUserPort,
                        output: JSON.stringify(output),
                        encryption: encryption,
                        autoBroad: autoBroad,
                        autoBroadShuffle: autoBroadShuffle,
                        autoBroadDuration: autoBroadDuration,
                        autoBroadStart: autoBroadStart,
                        remark: newRemark
                    };
                    ajax.post('/cs/channel/edit', questData, function (data, status) {
                        self.dialog.editChannel.loading = false;
                        listener();
                        if (status != 200) return;
                        self.$message({
                            message: '保存成功',
                            type: 'success'
                        });
                        self.dialog.editChannel.data.name = newName;
                        self.dialog.editChannel.data.remark = newRemark;
                        self.dialog.editChannel.data.outputUsers = outputQtUsers.concat(outputPushUsers);
                        self.dialog.editChannel.data.output = output;
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
                    if (self.dialog.editChannel.broadWay == "轮播推流" && self.dialog.editChannel.outputQtUsers.length <= 0) {
                        var noOut = true;
                        for (var i = 0; i < self.dialog.editChannel.outputQtUsers.length; i++){
                            if (self.dialog.editChannel.outputQtUsers.previewUrlIp.trim() != "" && self.dialog.editChannel.outputQtUsers.previewUrlPort.trim() != ""){
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
                    if (self.dialog.editChannel.broadWay == "下载文件" && !self.dialog.editChannel.outputQtUsers.length && !self.dialog.editChannel.outputPushUsers.length){
                        this.$message({
                            message: '请选择至少一个播发用户',
                            type: 'warning'
                        });
                        return;
                    }
                    if (self.dialog.editChannel.autoBroad) {
                        if ((!self.dialog.editChannel.autoBroadStart || !self.dialog.editChannel.autoBroadStart.trim())){
                            this.$message({
                                message: '请选择自动播发起始时间',
                                type: 'warning'
                            });
                            return;
                        }
                        var h = self.$createElement;
                        self.$msgbox({
                            title: '危险操作',
                            message: h('div', null, [
                                h('div', {class: 'el-message-box__status el-icon-warning'}, null),
                                h('div', {class: 'el-message-box__message'}, [
                                    h('p', null, ['此操作将替换该频道先前设置的所有自动播发节目单，是否继续?'])
                                ])
                            ]),
                            type: 'wraning',
                            showCancelButton: true,
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            beforeClose: function (action, instance, done) {
                                instance.confirmButtonLoading = true;
                                if (action === 'confirm') {
                                    self.handleEditChannelCommitSend(function () {
                                        instance.confirmButtonLoading = false;
                                        done();
                                    });
                                } else {
                                    instance.confirmButtonLoading = false;
                                    done();
                                }
                            }
                        }).catch(function () {
                        });
                    } else {
                        self.handleEditChannelCommitSend(function () {});
                    }
                },
                rowDelete: function (scope) {
                    var self = this;
                    var row = scope.row;
                    var h = self.$createElement;
                    self.$msgbox({
                        title: '危险操作',
                        message: h('div', null, [
                            h('div', {class: 'el-message-box__status el-icon-warning'}, null),
                            h('div', {class: 'el-message-box__message'}, [
                                h('p', null, ['此操作将永久删除频道，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type: 'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose: function (action, instance, done) {
                            instance.confirmButtonLoading = true;
                            if (action === 'confirm') {
                                var questData = {
                                    id: row.id
                                };
                                ajax.post('/cs/channel/remove', questData, function (data, status) {
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if (status != 200) return;
                                    self.getChannelList();
                                }, null, ajax.NO_ERROR_CATCH_CODE);
                            } else {
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function () {
                    });
                },
                toggleSelection: function (rows) {
                    if (rows) {
                        for (var i = 0; i < rows.length; i++) {
                            this.$refs.multipleChannelTable.toggleRowSelection(row);
                        }
                        //rows.forEach(row => {
                        //  this.$refs.multipleChannelTable.toggleRowSelection(row);
                        //});
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
                    var h = self.$createElement;
                    self.$msgbox({
                        title: '危险操作',
                        message: h('div', null, [
                            h('div', {class: 'el-message-box__status el-icon-warning'}, null),
                            h('div', {class: 'el-message-box__message'}, [
                                h('p', null, ['此操作将永久删除标签以及子标签，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type: 'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose: function (action, instance, done) {
                            instance.confirmButtonLoading = true;
                            if (action === 'confirm') {
                                var questData = {id: data.id};
                                ajax.post('/cs/menu/remove', questData, function (data, status) {
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if (status != 200) return;
                                    self.loadMenuTree();
                                }, null, ajax.NO_ERROR_CATCH_CODE);
                            } else {
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function () {
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
                    var questData = {id: self.dialog.editMenu.tree.current.id};
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
                    var h = self.$createElement;
                    self.$msgbox({
                        title: '危险操作',
                        message: h('div', null, [
                            h('div', {class: 'el-message-box__status el-icon-warning'}, null),
                            h('div', {class: 'el-message-box__message'}, [
                                h('p', null, ['删除资源操作，是否继续?'])
                            ])
                        ]),
                        type: 'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose: function (action, instance, done) {
                            instance.confirmButtonLoading = true;
                            if (action === 'confirm') {
                                var questData = {id: row.id};
                                ajax.post('/cs/menu/resource/remove', questData, function (data, status) {
                                    instance.confirmButtonLoading = false;
                                    done();
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
                            } else {
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function () {
                    });
                },

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

                editProgram: function (scope) {
                    var self = this;
                    self.dialog.editProgram.data = scope.row;
                    self.getPreviewScreenData();
                    self.dialog.editProgram.visible = true;
                },
                getPreviewScreenData: function () {
                    //获取之前的屏幕配置信息,设置初始显示分屏信息;
                    var self = this;

                    var questData = {id: self.dialog.editProgram.data.id};
                    ajax.post('/cs/program/get', questData, function (data, status) {
                        if (status != 200 || !data) {
                            self.dialog.editProgram.options.current = self.screen.one;
                            self.releaseScreenCommitInfo(1);
                        } else {
                            self.dialog.editProgram.previewData = data;
                            switch (data.screenNum) {
                                case 1:
                                    self.dialog.editProgram.options.current = self.screen.one;
                                    self.handleScreenOptionsChange(self.screen.one);
                                    break;
                                case 4:
                                    self.dialog.editProgram.options.current = self.screen.four;
                                    self.handleScreenOptionsChange(self.screen.four);
                                    break;
                                case 6:
                                    self.dialog.editProgram.options.current = self.screen.six;
                                    self.handleScreenOptionsChange(self.screen.six);
                                    break;
                                case 9:
                                    self.dialog.editProgram.options.current = self.screen.nine;
                                    self.handleScreenOptionsChange(self.screen.nine);
                                    break;
                            }
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                handleEditProgramClose: function () {
                    var self = this;
                    self.dialog.editProgram.visible = false;
                    self.dialog.editProgram.commitData.screenNum = 1;
                    self.dialog.editProgram.data = "";
                    self.dialog.editProgram.previewData = {};
                    self.dialog.editProgram.commitData.screenNum = "";
                    self.dialog.editProgram.commitData.currentSerialNum = "";
                    self.dialog.editProgram.commitData.screenInfo = [];
                    self.dialog.editProgram.commitData.currentSerialInfo = [];
                    self.dialog.editProgram.options.current = self.screen.one;
                },
                handlePreview: function (scope) {
                    var self = this;
                    var row = scope.row;
                    if(row.type == "AUDIO"){
                        self.$refs.Lightbox.preview(row.previewUrl, 'audio');
                    }else if(row.type == "VIDEO"){
                        self.$refs.Lightbox.preview(row.previewUrl, 'video');
                    }
                },
                handleEditProgramCommit: function () {
                    var self = this;
                    self.dialog.editProgram.loading = true;
                    var screenInfo = [];
                    for (var i = 0; i < self.dialog.editProgram.commitData.screenInfo.length; i++) {
                        var item = self.dialog.editProgram.commitData.screenInfo[i];
                        if (item.subColumns && item.subColumns.length > 0) {
                            for (var j = 0; j < item.subColumns.length; j++) {
                                item.subColumns[j].serialNum = item.serialNum;
                                screenInfo.push(item.subColumns[j])
                            }
                        }
                    }
                    var programInfo = {
                        scheduleId: self.dialog.editProgram.data.id,
                        screenNum: self.dialog.editProgram.commitData.screenNum,
                        screenInfo: screenInfo
                    };
                    var questData = {
                        programInfo: JSON.stringify(programInfo)
                    };
                    ajax.post('/cs/program/set', questData, function (data, status) {
                        self.dialog.editProgram.loading = false;
                        if (status != 200) return;
                        self.$message({
                            message: '保存成功',
                            type: 'success'
                        });
                        self.dialog.editProgram.previewData = data;
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                handleScreenOptionsChange: function (data) {
                    var self = this;
                    self.dialog.editProgram.audioIndex = 1;
                    switch (data) {
                        case self.screen.one:
                            self.releaseScreenCommitInfo(1);
                            break;
                        case self.screen.four:
                            self.releaseScreenCommitInfo(4);
                            break;
                        case self.screen.six:
                            self.releaseScreenCommitInfo(6);
                            break;
                        case self.screen.nine:
                            self.releaseScreenCommitInfo(9);
                            break;
                    }
                    self.clickScreen(1);
                },
                releaseScreenCommitInfo: function (num) {
                    var self = this;
                    self.dialog.editProgram.commitData.screenNum = num;
                    self.dialog.editProgram.commitData.screenInfo.splice(0, self.dialog.editProgram.commitData.screenInfo.length);
                    self.dialog.editProgram.commitData.currentSerialInfo.splice(0, self.dialog.editProgram.commitData.currentSerialInfo.length);
                    var i = 0;
                    var data = {};
                    if (self.dialog.editProgram.previewData.screenNum != num) {
                        for (i = 0; i < num; i++) {
                            data = {
                                serialNum: i + 1,
                                subColumns: []
                            };
                            self.dialog.editProgram.commitData.screenInfo.push(data);
                        }
                    } else {
                        for (i = 0; i < num; i++) {
                            data = {
                                serialNum: i + 1,
                                subColumns: []
                            };
                            for (var j = 0; j < self.dialog.editProgram.previewData.screenInfo.length; j++) {
                                if (self.dialog.editProgram.previewData.screenInfo[j].serialNum == data.serialNum) {
                                    data.subColumns.push(self.dialog.editProgram.previewData.screenInfo[j]);
                                }
                            }
                            self.dialog.editProgram.commitData.screenInfo.push(data);
                        }
                    }
                },
                clickScreen: function (serialNum) {
                    var self = this;
                    self.dialog.editProgram.commitData.currentSerialNum = serialNum;
                    for (var i = 0; i < self.dialog.editProgram.commitData.screenInfo.length; i++) {
                        var item = self.dialog.editProgram.commitData.screenInfo[i];
                        if (item.serialNum == serialNum) {
                            self.dialog.editProgram.commitData.currentSerialInfo = item.subColumns;
                            break;
                        }
                    }
                },
                chooseResources: function (serialNum) {
                    var self = this;
                    self.dialog.editProgram.dialog.chooseResource.visible = true;
                    self.loadProgramMenuTree();
                },
                loadProgramMenuTree: function () {
                    var self = this;
                    self.dialog.editProgram.dialog.chooseResource.tree.loading = true;
                    self.dialog.editProgram.dialog.chooseResource.tree.data.splice(0, self.dialog.editProgram.dialog.chooseResource.tree.data.length);
                    self.dialog.editProgram.dialog.chooseResource.tree.data.push({
                        id: -1,
                        uuid: '-1',
                        name: '资源目录',
                        icon: 'icon-tag',
                        style: 'font-size:15px; position:relative; top:1px; margin-right:1px;'
                    });
                    var questData = {channelId: self.dialog.editSchedules.data.id};
                    ajax.post('/cs/menu/list/tree', questData, function (data, status) {
                        self.dialog.editProgram.dialog.chooseResource.tree.loading = false;
                        if (data && data.length > 0) {
                            for (var i = 0; i < data.length; i++) {
                                self.dialog.editProgram.dialog.chooseResource.tree.data.push(data[i]);
                            }
                        }
                        self.currentProgramMenuNode(self.dialog.editProgram.dialog.chooseResource.tree.data[0]);
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                currentProgramResourceTreeNodeChange: function (data) {
                    var self = this;
                    self.currentProgramMenuNode(data);
                },
                currentProgramMenuNode: function (data) {
                    var self = this;
                    if (!data || data.id == -1) {
                        self.dialog.editProgram.dialog.chooseResource.tree.current = '';
                        if (self.dialog.editProgram.dialog.chooseResource.resources.data)
                            self.dialog.editProgram.dialog.chooseResource.resources.data.splice(0, self.dialog.editProgram.dialog.chooseResource.resources.data.length);
                        return;
                    }
                    self.dialog.editProgram.dialog.chooseResource.tree.current = data;
                    self.$nextTick(function () {
                        self.$refs.programResourceTree.setCurrentKey(data.uuid);
                    });
                    self.loadingProgramMenuResource();
                },
                loadingProgramMenuResource: function () {
                    var self = this;
                    if (self.dialog.editProgram.dialog.chooseResource.resources.data)
                        self.dialog.editProgram.dialog.chooseResource.resources.data.splice(0, self.dialog.editProgram.dialog.chooseResource.resources.data.length);
                    var questData = {
                        id: self.dialog.editProgram.dialog.chooseResource.tree.current.id
                    };
                    ajax.post('/cs/menu/resource/get', questData, function (data, status) {
                        if (status != 200) return;
                        if (data && data.length > 0) {
                            for (var i = 0; i < data.length; i++) {
                                self.dialog.editProgram.dialog.chooseResource.resources.data.push(data[i])
                            }
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE)
                },
                handleProgramMenuResourceCheckChange: function (val) {
                    var self = this;
                    self.dialog.editProgram.dialog.chooseResource.resources.chooses = val;
                },
                getRowKeys: function (row) {
                    return row.id;
                },
                handleChooseResourcesClose: function () {
                    var self = this;
                    self.dialog.editProgram.dialog.chooseResource.visible = false;
                    self.dialog.editProgram.dialog.chooseResource.resources.chooses = [];
                    this.$refs.programResourceTable.clearSelection();
                },
                handleChooseResourcesCommit: function () {
                    var self = this;
                    self.dialog.editProgram.dialog.chooseResource.loading = true;

                    var chooseResources = self.dialog.editProgram.dialog.chooseResource.resources.chooses;
                    if (chooseResources && chooseResources.length > 0) {
                        for (var i = 0; i < chooseResources.length; i++) {
                            chooseResources[i].index = self.dialog.editProgram.commitData.currentSerialInfo.length + 1;
                            chooseResources[i].resourceId = chooseResources[i].id;
                            self.dialog.editProgram.commitData.currentSerialInfo.push(chooseResources[i])
                        }
                        for (var j = 0; j < self.dialog.editProgram.commitData.screenInfo.length; j++) {
                            if (self.dialog.editProgram.commitData.screenInfo[j].serialNum == self.dialog.editProgram.commitData.currentSerialNum) {
                                self.dialog.editProgram.commitData.screenInfo[j].subColumns = self.dialog.editProgram.commitData.currentSerialInfo;
                                break;
                            }
                        }
                    }
                    self.dialog.editProgram.dialog.chooseResource.loading = false;
                    self.handleChooseResourcesClose();
                },
                programResourceUp: function (scope) {
                    var self = this;
                    var row = scope.row;
                    var index = row.index;
                    if (index == 1 || !self.dialog.editProgram.commitData.currentSerialInfo || self.dialog.editProgram.commitData.currentSerialInfo.length <= 1) return;
                    for (var i = 0; i < self.dialog.editProgram.commitData.currentSerialInfo.length; i++) {
                        var item = self.dialog.editProgram.commitData.currentSerialInfo[i];
                        if (item.index == index - 1) {
                            item.index += 1;
                            break;
                        }
                    }
                    row.index -= 1;
                    this.$refs.programTable.sort('index', 'ascending');
                },
                programResourceDown: function (scope) {
                    var self = this;
                    var row = scope.row;
                    var index = row.index;
                    if (!self.dialog.editProgram.commitData.currentSerialInfo) return;
                    var length = self.dialog.editProgram.commitData.currentSerialInfo.length;
                    if (index == length || length <= 1) return;
                    for (var i = 0; i < length; i++) {
                        var item = self.dialog.editProgram.commitData.currentSerialInfo[i];
                        if (item.index == index + 1) {
                            item.index -= 1;
                            break;
                        }
                    }
                    row.index += 1;
                    this.$refs.programTable.sort('index', 'ascending');
                },
                programResourceDelete: function (scope) {
                    var self = this;
                    var row = scope.row;
                    for (var i = 0; i < self.dialog.editProgram.commitData.screenInfo.length; i++) {
                        if (self.dialog.editProgram.commitData.currentSerialNum == self.dialog.editProgram.commitData.screenInfo[i].serialNum) {
                            var index = 0;
                            for (var j = 0; j < self.dialog.editProgram.commitData.screenInfo[i].subColumns.length; j++) {
                                if (row.id == self.dialog.editProgram.commitData.screenInfo[i].subColumns[j].id) {
                                    index = j;
                                    break;
                                }
                            }
                            self.dialog.editProgram.commitData.screenInfo[i].subColumns.splice(index, 1);
                            for (var m = 0; m < self.dialog.editProgram.commitData.screenInfo[i].subColumns.length; m++) {
                                self.dialog.editProgram.commitData.screenInfo[i].subColumns[m].index = m + 1;
                            }
                            break;
                        }
                    }
                },


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
                    ajax.post('/cs/area/set', questData, function (data, status) {
                        self.dialog.manageBroadcastArea.loading = false;
                        self.handleManageBroadcastAreaClose();
                        if (status == 200) {
                            self.$message({
                                message: '保存成功',
                                type: 'success'
                            });
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                handleManageBroadcastAreaCheckChange: function (data, checked, indeterminate) {
                    var self = this;
                    self.dialog.manageBroadcastArea.tree.current = self.$refs.broadcastAreaTree.getCheckedNodes(false, false);
                },


                startBroadcast: function (scope) {
                    var self = this;
                    var row = scope.row;
                    var questData = {
                        channelId: row.id
                    };
                    if (row.broadWay == "终端播发") {
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
                }
            },
            created: function () {
                var self = this;
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