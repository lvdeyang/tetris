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
                    currentScreen: {
                        data: [{
                            content: ''
                        }]
                    }
                },
                orientOptions: {
                    list: [
                        {
                            name: '横屏',
                            value: 'horizontal'
                        },{
                            name: '竖屏',
                            value: 'vertical'
                        }
                    ],
                    currentName: '',
                    current: {}
                },
                contentOptions: {
                    list: []
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
                },
                broadWayStream: '轮播推流',
                broadWayFile: '下载文件',
                broadWayTerminal: '终端播发',
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
                ajax.post('/cs/program/content/type/get', {channelId: channelData.id}, function(data, status) {
                    self.contentOptions.list.splice(0, self.contentOptions.list.length);
                    if (status == 200 && data && data.length) {
                        self.contentOptions.list = self.contentOptions.list.concat(data);
                    }
                });
                ajax.post(url, questData, function(data, status) {
                    if (status == 200 && data) {
                        var currentScreenIndex = 0;
                        for (var i = 0; i < data.length; i++){
                            if (currentScreenIndex == 0
                                && self.scheduleData.program
                                && self.scheduleData.program.screenNum == data[i].screenNum
                                && self.scheduleData.program.screenId == data[i].id) {
                                currentScreenIndex = i;
                                for (var j = 0; j < self.scheduleData.program.screenInfo.length; j++) {
                                    for (var k = 0; k < data[i].screen.length; k++) {
                                        if (self.scheduleData.program.screenInfo[j].serialNum == data[i].screen[k].no){
                                            if (!data[i].screen[k].data) data[i].screen[k].data = [];
                                            data[i].screen[k].data.push(self.scheduleData.program.screenInfo[j]);
                                            if (!data[i].screen[k].contentType) {
                                                if (self.channelData.broadWay == self.broadWayStream) {
                                                    data[i].screen[k].contentType = self.scheduleData.program.screenInfo[j].contentType || '视频资源';
                                                } else {
                                                    data[i].screen[k].contentType = self.scheduleData.program.screenInfo[j].contentType || '仓库资源';
                                                }
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                            for (var k = 0; k < data[i].screen.length; k++) {
                            	if(self.channelData.autoBroad){
                            		data[i].screen[k].contentType="音频资源";
                            	}else{
                                    if (!data[i].screen[k].contentType) data[i].screen[k].contentType = self.channelData.broadWay == self.broadWayStream ? '视频资源' : '仓库资源';
                            	}
                            }
                            self.options.list.push(data[i]);
                        }
                        if (self.options.list && self.options.list.length > 0) {
                            self.options.currentName = self.options.list[currentScreenIndex].name;
                            self.handleScreenOptionsChange(self.options.currentName);
                        }
                        self.handleOrientOptionChange(null, self.scheduleData.program && self.scheduleData.program.orient ? self.scheduleData.program.orient : 'horizontal');
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
            handleOrientOptionChange: function(name, value) {
                var self = this;
                for (var i = 0; i < self.orientOptions.list.length; i++){
                    var item = self.orientOptions.list[i];
                    if ((name && item.name == name) || (value && item.value == value)) {
                        self.orientOptions.current = self.orientOptions.list[i];
                        self.orientOptions.currentName = item.name;
                        break;
                    }
                }
            },
            handleContentOptionChange: function(item, name) {
                var self = this;
                self.handleScreenChoose(item);
                self.options.currentScreen.data.splice(0, self.options.currentScreen.data.length);
                if (name != '图片资源' && name != '音频资源' && name != '视频资源' && name != '仓库资源') {
                    self.options.currentScreen.data.push({
                        serialNum: self.options.currentScreen.no,
                        index: 1,
                        textContent: '',
                        contentType: name
                    })
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
                var currentScreenLastSource = self.options.currentScreen.data[self.options.currentScreen.data.length - 1];
                //当前资源不是直播且当前资源不在倒二个
                //return (row.type != "PUSH_LIVE" && self.options.currentScreen.data.length != row.index + 1)
                //    || (self.options.currentScreen.data.length == row.index + 1 && currentScreenLastSource.type != "PUSH_LIVE");
                //当前资源不是最后一个 && 如果是倒数第二个资源，当最后一个资源是音视频时可往下排
                //return !(self.options.currentScreen.data.length == row.index
                //    || (self.options.currentScreen.data.length == row.index + 1 && currentScreenLastSource.type != 'AUDIO' && currentScreenLastSource.type != 'VIDEO'));
                return true;
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
                        //if (item.type != 'AUDIO' && item.type != 'VIDEO') {
                        //    this.$message.error('非音视频资源要求位于播放列表末尾');
                        //    return;
                        //}
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
                            var last = self.options.currentScreen.data[self.options.currentScreen.data.length - 1];
                            //if (data[i].type != 'AUDIO' && data[i].type != 'VIDEO') {
                            //    if ((self.options.currentScreen.data.length > 0 && last.type != 'AUDIO' && last.type != 'VIDEO') || (data[i].type != 'PUSH_LIVE' && self.channelData.hasFile === false))
                            //        data[i].disabled = true;
                            //} else {
                                if (self.channelData.hasFile === false && data[i].type != 'PUSH_LIVE') data[i].disabled = true;
                            //}

                            if ((self.options.currentScreen.contentType == '图片资源' && data[i].type != 'PICTURE')
                                || (self.options.currentScreen.contentType == '视频资源' && (data[i].type != 'VIDEO' && data[i].type != 'VIDEO_STREAM'&& data[i].type != 'PUSH_LIVE'))
                                || (self.options.currentScreen.contentType == '音频资源' && (data[i].type != 'AUDIO' && data[i].type != 'AUDIO_STREAM'))
                                || (self.options.currentScreen.contentType == '视频资源' && data[i].type != 'VIDEO_STREAM' && data[i].type != 'PUSH_LIVE' && (data[i].duration < 10000))) {
                                data[i].disabled = true;
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
                //var hasLive = false;

				if (self.channelData.hasFile === false && self.options.currentScreen.data.length > 0) {
					self.$message({
						message: '不携带文件仅可播发单个直播',
						type: 'warning'
					})
					return;
				}
                ////排查被选中的资源是否有两个以上非音视频文件
                //var index = -1;
                //for (var i = 0; i < chooseResources.length; i++) {
                //    if (chooseResources[i].type != "AUDIO" && chooseResources[i].type != 'VIDEO') {
                //        if (hasLive) {
                //            self.dialog.chooseResource.loading = false;
                //            this.$message({
                //                message: '除音视频外，其他资源不可多选！',
                //                type: 'warning'
                //            });
                //            return;
                //        } else {
                //            hasLive = true;
                //            index = i;
                //        }
                //    }
                //}
                //
                ////把非音视频文件取出，添加到最后
                //var addLast;
                //if (index != -1) {
                //    addLast = chooseResources.splice(index, 1)[0];
                //}

                //排查已被添加进分屏的资源中是否有非音视频，有则移出后再注入
                if (chooseResources && chooseResources.length > 0) {
                    //var pop = null;
                    //if (self.options.currentScreen.data.length > 0) {
                    //    var last = self.options.currentScreen.data[self.options.currentScreen.data.length - 1];
                    //    if (last.type != "AUDIO" && last.type != "VIDEO") pop = self.options.currentScreen.data.pop();
                    //}
                    for (var i = 0; i < chooseResources.length; i++) {
                        chooseResources[i].index = self.options.currentScreen.data.length + 1;
                        chooseResources[i].resourceId = chooseResources[i].id;
                        chooseResources[i].serialNum = self.options.currentScreen.no;
                        chooseResources[i].contentType = self.options.currentScreen.contentType;
                        if (chooseResources[i].type != 'VIDEO' && chooseResources[i].type != 'AUDIO') {
                            chooseResources[i].duration = 60000;
                        }
                        self.options.currentScreen.data.push(chooseResources[i]);
                    }
                    //if (pop) last.index = self.options.currentScreen.data.push(pop);
                }

                //预添加列表中如有取出的非音视频文件，在此处加入
                //if (addLast) {
                //    addLast.index = self.options.currentScreen.data.length + 1;
                //    addLast.resourceId = addLast.id;
                //    addLast.serialNum = self.options.currentScreen.no;
                //    addLast.contentType = self.options.currentScreen.contentType;
                //    self.options.currentScreen.data.push(addLast);
                //}

                self.dialog.chooseResource.loading = false;
                self.handleChooseResourcesClose();
            },
            handleDurationChange: function(row, value) {
                var self = this;
                if (value) {
                    row.duration = value * 1000;
                } else {
                    row.duration = null;
                }
            },
            handleRotationChange: function(row, value){
                if (value) {
                    row.rotation = value;
                } else {
                    row.rotation = null;
                }
            },
            handleStartTimeChange(row, value){
            	if (value) {
                    row.startTime = value;
                } else {
                    row.startTime = null;
                }
            },
            handleEndTimeChange(row, value){
            	if (value) {
                    row.endTime = value;
                } else {
                    row.endTime = null;
                }
            },
            handleCommit: function () {
                var self = this;
                self.loading = true;
                var questData = {
                    scheduleId: self.scheduleData.id,
                    programInfo: JSON.stringify(self.options.current),
                    orient: self.orientOptions.current.value || 'horizontal'
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
                self.orientOptions.current = {};
                self.orientOptions.currentName = '';
                self.options.list.splice(0, self.options.list.length);
                self.$emit(ON_PROGRAM_SCREEN_CLOSE);
            }
        },
        filters: {
            resourceType: function (row) {
                switch (row.type) {
                    case 'PUSH_LIVE':
                        return 'push直播';
                    case 'AUDIO':
                        return '音频文件';
                    case 'VIDEO':
                        return '视频文件';
                    case 'AUDIO_STREAM':
                        return '音频流';
                    case 'VIDEO_STREAM':
                        return '视频流';
                    case 'PICTURE':
                        return '图片文件';
                    default:
                        return ''
                }
            }
        }
    })
});