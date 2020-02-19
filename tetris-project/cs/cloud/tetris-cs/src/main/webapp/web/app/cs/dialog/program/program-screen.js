/**
 * Created by sms on 2019/12/5.
 */
define([
    'text!' + window.APPPATH + 'cs/dialog/program/program-screen.html',
    'restfull',
    'jquery',
    'vue',
    'element-ui',
    'css!' + window.APPPATH + 'cs/dialog/program/program-screen.css'
], function (tpl, ajax, $, Vue) {
    var pluginName = 'cs-program-screen';
    
    var ON_PROGRAM_SCREEN_CLOSE = 'on-program-screen-close';

    Vue.component(pluginName, {
        template: tpl,
        data: function () {
            return {
                visible: false,
                loading: false,
                channelData: {},
                scheduleData: {},
                url: '',
                options: {
                    //下拉列表可选项
                    list: [],
                    //下拉框当前选择名称
                    currentName: '',
                    //当前选择分屏
                    current: {},
                    //当前选择屏幕
                    currentScreen: {}
                },
                dialog: {
                    chooseResource: {
                        visible: false,
                        loading: false,
                        tree: {
                            loading: false,
                            props: {
                                label: 'name',
                                children: 'subColumns'
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
            }
        },
        methods: {
            open: function (url, channelData, scheduleData) {
                var self = this;
                self.url = url;
                self.channelData = channelData;
                self.scheduleData = scheduleData;
                self.options.list.splice(0,self.options.list.length);
                var questData = {
                    channelId: channelData.id,
                    scheduleId: scheduleData.id
                };
                ajax.post(url, questData, function(data, status) {
                    if (status == 200 && data) {
                        var currentScreenIndex = 0;
                        for (var i = 0; i < data.length; i++){
                            if (currentScreenIndex == 0 && self.scheduleData.program && self.scheduleData.program.screenNum == data[i].screenNum && self.scheduleData.program.screenId == data[i].id) {
                                currentScreenIndex = i;
                                for (var j = 0; j < self.scheduleData.program.screenInfo.length; j++) {
                                    for (var k = 0; k < data[i].screen.length; k++) {
                                        if (self.scheduleData.program.screenInfo[j].serialNum == data[i].screen[k].no){
                                            if (!data[i].screen[k].data) data[i].screen[k].data = [];
                                            data[i].screen[k].data.push(self.scheduleData.program.screenInfo[j]);
                                            break;
                                        }
                                    }
                                }
                            }
                            self.options.list.push(data[i]);
                        }
                        if (self.options.list && self.options.list.length > 0) {
                            self.options.currentName = self.options.list[currentScreenIndex].name;
                            self.handleScreenOptionsChange(self.options.currentName);
                        }
                    }
                });
                self.visible = true;
            },
            handleScreenOptionsChange: function (data) {
                var self = this;
                for (var i = 0; i < self.options.list.length; i++){
                    if (self.options.list[i].name == data) {
                        self.options.current = self.options.list[i];
                        self.handleScreenChoose(self.options.current.screen[0]);
                        break;
                    }
                }
            },
            handleScreenChoose: function (item, event) {
                var self = this;
                self.options.currentScreen = item;
                if (!item.data) {
                    Vue.set(item, 'data', []);
                }
            },
            handlePreview: function (scope) {
                var self = this;
                self.$emit('preview',scope);
            },
            handleIfDown: function (scope) {
                var self  = this;
                var row = scope.row;
                return (row.type != "PUSH_LIVE" && self.options.currentScreen.data.length != row.index + 1)
                    || (self.options.currentScreen.data.length == row.index + 1 && self.options.currentScreen.data[self.options.currentScreen.data.length - 1].type != "PUSH_LIVE");
            },
            programResourceUp: function (scope) {
                var self = this;
                var row = scope.row;
                var index = row.index;
                if (index == 1 || !self.options.currentScreen.data || self.options.currentScreen.data.length <= 1) return;
                for (var i = 0; i < self.options.currentScreen.data.length; i++) {
                    var item = self.options.currentScreen.data[i];
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
                if (!self.options.currentScreen.data) return;
                var length = self.options.currentScreen.data.length;
                if (index == length || length <= 1) return;
                for (var i = 0; i < length; i++) {
                    var item = self.options.currentScreen.data[i];
                    if (item.index == index + 1) {
                        if (item.type == 'PUSH_LIVE') {
                            this.$message.error('直播资源要求位于播放列表末尾');
                            return;
                        }
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
                var index = 0;
                for (var i = 0; i < self.options.currentScreen.data.length;i++) {
                    if (row.id == self.options.currentScreen.data[i].id) {
                        index = i;
                        break;
                    }
                }
                self.options.currentScreen.data.splice(index, 1);
                for (var j = 0; j < self.options.currentScreen.data.length; j++) {
                    if (self.options.currentScreen.data[j].index > index) {
                        self.options.currentScreen.data[j].index -= 1;
                    }
                }
            },


            chooseResources: function (serialNum) {
                var self = this;
                self.dialog.chooseResource.visible = true;
                self.loadProgramMenuTree();
            },
            loadProgramMenuTree: function () {
                var self = this;
                self.dialog.chooseResource.tree.loading = true;
                self.dialog.chooseResource.tree.data.splice(0, self.dialog.chooseResource.tree.data.length);
                self.dialog.chooseResource.tree.data.push({
                    id: -1,
                    uuid: '-1',
                    name: '资源目录',
                    icon: 'icon-tag',
                    style: 'font-size:15px; position:relative; top:1px; margin-right:1px;'
                });
                var questData = {channelId: self.channelData.id};
                ajax.post('/cs/menu/list/tree', questData, function (data, status) {
                    self.dialog.chooseResource.tree.loading = false;
                    if (data && data.length > 0) {
                        for (var i = 0; i < data.length; i++) {
                            self.dialog.chooseResource.tree.data.push(data[i]);
                        }
                    }
                    self.currentMenuNode(self.dialog.chooseResource.tree.data[0]);
                }, null, ajax.NO_ERROR_CATCH_CODE);
            },
            currentResourceTreeNodeChange: function (data) {
                var self = this;
                self.currentMenuNode(data);
            },
            currentMenuNode: function (data) {
                var self = this;
                if (!data || data.id == -1) {
                    self.dialog.chooseResource.tree.current = '';
                    if (self.dialog.chooseResource.resources.data)
                        self.dialog.chooseResource.resources.data.splice(0, self.dialog.chooseResource.resources.data.length);
                    return;
                }
                self.dialog.chooseResource.tree.current = data;
                self.$nextTick(function () {
                    self.$refs.resourceTree.setCurrentKey(data.uuid);
                });
                self.loadingMenuResource();
            },
            checkSelectable: function (row) {
                return !row.disabled;
            },
            resourceType: function (row) {
                return row.type == 'PUSH_LIVE' ? '直播' : "文件"
            },
            loadingMenuResource: function () {
                var self = this;
                if (self.dialog.chooseResource.resources.data)
                    self.dialog.chooseResource.resources.data.splice(0, self.dialog.chooseResource.resources.data.length);
                var questData = {
                    id: self.dialog.chooseResource.tree.current.id
                };
                ajax.post('/cs/menu/resource/get', questData, function (data, status) {
                    if (status != 200) return;
                    if (data && data.length > 0) {
                        for (var i = 0; i < data.length; i++) {
                            if (data[i].type == 'PUSH_LIVE') {
                                if (self.options.currentScreen.data.length > 0 && self.options.currentScreen.data[self.options.currentScreen.data.length - 1].type == 'PUSH_LIVE') data[i].disabled = true;
                            } else {
                                if (self.channelData.hasFile === false) data[i].disabled = true;
                            }
                            self.dialog.chooseResource.resources.data.push(data[i]);
                        }
                    }
                }, null, ajax.NO_ERROR_CATCH_CODE)
            },
            handleResourceCheckChange: function (val) {
                var self = this;
                self.dialog.chooseResource.resources.chooses = val;
            },
            getRowKeys: function (row) {
                return row.id;
            },
            handleChooseResourcesClose: function () {
                var self = this;
                self.dialog.chooseResource.visible = false;
                self.dialog.chooseResource.resources.chooses = [];
                this.$refs.resourceTable.clearSelection();
            },
            handleChooseResourcesCommit: function () {
                var self = this;
                self.dialog.chooseResource.loading = true;

                var chooseResources = self.dialog.chooseResource.resources.chooses;
                var hasLive = false;
                for (var i = 0; i < chooseResources.length; i++) {
                    if (chooseResources[i].type == "PUSH_LIVE") {
                        if (hasLive) {
                            self.dialog.chooseResource.loading = false;
                            this.$message({
                                message: '直播资源不可多选！',
                                type: 'warning'
                            });
                            return;
                        } else {
                            hasLive = true;
                        }
                    }
                }
                if (chooseResources && chooseResources.length > 0) {
                    var pop = null;
                    if (self.options.currentScreen.data.length > 0) {
                        var last = self.options.currentScreen.data[self.options.currentScreen.data.length - 1];
                        if (last.type == "PUSH_LIVE") pop = self.options.currentScreen.data.pop();
                    }
                    for (var i = 0; i < chooseResources.length; i++) {
                        chooseResources[i].index = self.options.currentScreen.data.length + 1;
                        chooseResources[i].resourceId = chooseResources[i].id;
                        chooseResources[i].serialNum = self.options.currentScreen.no;
                        self.options.currentScreen.data.push(chooseResources[i]);
                    }
                    if (pop) last.index = self.options.currentScreen.data.push(pop);
                }
                self.dialog.chooseResource.loading = false;
                self.handleChooseResourcesClose();
            },

            handleCommit: function () {
                var self = this;
                self.loading = true;
                var questData = {
                    scheduleId: self.scheduleData.id,
                    programInfo: JSON.stringify(self.options.current)
                };
                ajax.post('/cs/program/set', questData, function (data, status) {
                    self.loading = false;
                    if (status != 200) return;
                    self.$message({
                        message: '保存成功',
                        type: 'success'
                    });
                    self.scheduleData.program = data;
                }, null, ajax.NO_ERROR_CATCH_CODE);
            },
            handleClose: function () {
                var self = this;
                self.visible = false;
                self.loading = false;
                self.channelData = {};
                self.scheduleData = {};
                self.url = '';
                self.options.currenName = '';
                self.options.current = {};
                self.options.currentScreen = {};
                self.options.list.splice(0, self.options.list.length);
                self.$emit(ON_PROGRAM_SCREEN_CLOSE);
            }
        }
    })
});