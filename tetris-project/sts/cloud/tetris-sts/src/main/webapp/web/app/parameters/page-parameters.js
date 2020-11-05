/**
 * Created by lzp on 2019/3/16.
 */
define([
    'text!' + window.APPPATH + 'parameters/page-parameters.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'mi-lightbox',
    'css!' + window.APPPATH + 'parameters/page-parameters.css'
], function (tpl, config, $, ajax, context, commons, Vue) {

    var pageId = 'page-parameters';

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
                activeName: 'first',
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
                task:{
                	taskGroup: [{
                        value: '默认分组',
                        label: '默认分组'
                      }, {
                        value: '任务分组1',
                        label: '任务分组1'
                      }, {
                        value: '任务分组2',
                        label: '任务分组2'
                      }, {
                        value: '任务分组3',
                        label: '任务分组3'
                    }],
                    taskTemplate: [{
                    	value: '任务模板1',
                        label: '任务模板1'
                      }, {
                        value: '任务模板2',
                        label: '任务模板2'
                      },{
                        value: '任务模板3',
                        label: '任务模板3'
                    }],
                    deviceGroup: [{
                    	value: '设备分组1',
                        label: '设备分组1'
                      }, {
                        value: '设备分组2',
                        label: '设备分组2'
                    }],
                    deviceNode: [{
                    	value: '10.10.40.58',
                        label: '10.10.40.58'
                      }, {
                        value: '10.10.40.104',
                        label: '10.10.40.104'
                    }],
                    nCardIndex: [{
                    	value: 'auto',
                        label: '自动选卡'
                      }, {
                        value: '1',
                        label: '卡1'
                      }, {
                          value: '2',
                          label: '卡2'
                    }]
                },
                source:{
                	url:'111',
                	name:'222',
//                	data:[{ 'id': 152, 'uuid': 'bd131038854e488883b3de176b0000fa', 'updateTime': '2019-11-03 15:55:38', 'previewUrl': [ 'udp://@192.165.56.84:1234' ], 'name': 'test2', 'authorName': '应急广播管理员用户', 'createTime': '', 'remarks': '', 'tags': [ '' ], 'keyWords': [ '' ], 'igmpv3Status': 'close', 'igmpv3Mode': null, 'igmpv3IpArray': null, 'type': 'VIDEO_STREAM', 'resourceType': null, 'removeable': true, 'icon': 'icon-film', 'style': 'font-size:16px; position:relative; top:1px;', 'reviewStatus': '', 'processInstanceId': null, 'addition': null, 'children': null }]
                	data:[],
                },
                program:{
                	programName:'DONGFANG HD',
                	videoMsg:'h264 1920x1080 16:9 25fps 4358kbps',
                	audioMsg:'mpeg2-audio 48kHz 2channels 183kbps|ac3 48kHz 6channels 625kbps',
                	programMsg:{
                		programProvider:'Harmonic',
                    	protoType:'TSUDP',
                    	programNum:'4004',
                    	sourceType:'',
                    	pmtPid:'5378',
                    	pcrPid:'5376'
                	},
                	videoes:[
                	   {index:1, pid:512, type:'h264', fps:25, width:1920, height:1080, ratio:'16:9'},
                	   {index:2, pid:513, type:'h264', fps:25, width:1920, height:1080, ratio:'16:9'},
                	],
                	audioes:[
                	    {index:1, pid:514, type:'ac3', sampleRate:4800, channels:6},
                	]
                },
                dialog: {
                	sourceSelect:{
                		visible:false,
                		keyword:'',
                		source:[],
                		loading:false
                	},
                	taskDetail:{
                        visible:false,
                        editPassword:false,
                        taskId:11,
                        taskName:'测试',
                        taskGroupName:'分组1',
                        taskType:'转码',
                        loading:false
                    },
                    createTask:{
                        taskName:'',
                        taskGroupName:'',
                        taskTemplate:'',
                        deviceGroup:'',
                        deviceNode:'',
                        nCardIndex:'auto',
                        useInpub:true,
                        status:'run'
                    },
                    outputInfo:{
                    	visible:false,
                    	output:'10.10.40.103:1258',
                    	url:'rtp://@10.10.40.103:1258',
                    	loading:false
                    },
                    exchangeProgram:{
                    	visible:false,
                    	keyword:'',
                    	program:[],
                    	loading:false
                    },
                    taskCover:{
                    	visible:false,
                    	startDate:'',
                    	startTime:'',
                    	endDate:'',
                    	endTime:'',
                    	file:[{
                    		name:'test.mp4',
                    		url:'ftp://ftper:ftper@10.10.40.103/media'
                    		},{
                    		name:'tt.ts',
                        	url:'ftp://ftper:ftper@10.10.40.103/media'
                    	}],
                    	loading:false
                    },
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
            	handleSourceSelect: function(){
            		var self = this;
            		self.loading = true;
            		var newData = {
            				keyWord: self.dialog.sourceSelect.keyword,
            		};
            		ajax.post('/task/source/getVideoStream',newData , function(data, status){
            			self.loading = false;
                        if (status != 200) return;
                        self.source.data = data.rows;
                    });
                    self.dialog.sourceSelect.visible = true;
            	},
            	handleSourceSelectClose: function(){
            		var self = this;
            		self.source.data = '';
                    self.dialog.sourceSelect.visible = false;
            	},
            	handleSourceSelectSubmit: function(){
            		var self = this;
            		var selected = this.$refs.multipleSourceSelectTable.store.states.selection;
            		if(selected.length > 1){
            			 alert("只可选择一条源作为输入，请重新选择");
            			 return;
            		}else if(selected.length === 1){
            			self.source.name = selected[0].name;
            			self.source.url = selected[0].previewUrl;
            			
            		}
            		self.dialog.sourceSelect.visible = false;
            	},
            	handleRefreshTask: function(){
            		var self = this;
            		self.loading = true;
            		var newData = {
            				keyWord: "",
            				pageNum: self.channel.page.currentPage,
            				pageSize: self.channel.page.size,
                            taskGroupId: 0,
                            sdmDeviceId: 0,
                            alarmFlag: null,
                            linkStatus: null,
                            deviceNodeId: 0,
            		};
//            		ajax.post('/task/taskManage/getTasks', newData, function(data, status){
//            			self.loading = false;
//            			if (status != 200) return;
//                        self.channel.data = data.taskVoList;
//                        self.channel.page.total = data.total;
//            		})
            		
            		ajax.post('/task/taskManage/getTasks',newData , function(data, status){
            			self.loading = false;
                        if (status != 200) return;
                        self.channel.data = data.rows;
                        self.channel.page.total = data.total;
                    });
            	},
            	handleRowDetail: function(scope){
            		var self = this;
                    var row = scope.row;
                    self.dialog.taskDetail.taskId = row.taskId;
                    self.dialog.taskDetail.taskName = row.taskName;
                    self.dialog.taskDetail.taskGroupName = row.taskGroupName;
                    self.dialog.taskDetail.taskType = row.taskType;
                    self.dialog.taskDetail.visible = true;
            	},
            	handleTaskDetailClose: function(){
            		var self = this;
            		self.dialog.taskDetail.taskId = '';
                    self.dialog.taskDetail.taskName = '';
                    self.dialog.taskDetail.taskGroupName = '';
                    self.dialog.taskDetail.taskType = '';
                    self.dialog.taskDetail.visible = false;
            	},
            	handleRowEdit: function(scope){
            		
            	},
            	handleRowDelete: function(scope){
            		var self = this;
                    var row = scope.row;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'危险操作',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['此操作将永久删除该任务，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/task/taskManage/delete/' + row.id, null, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    if(status !== 200) return;
                                    for(var i=0; i<self.table.rows.length; i++){
                                        if(self.table.rows[i].id === row.id){
                                            self.table.rows.splice(i, 1);
                                            break;
                                        }
                                    }
                                    done();
                                }, null, ajax.NO_ERROR_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
            	},
            	handleRowOutputInfo: function(scope){
            		var self = this;
                    var row = scope.row;
                    self.dialog.outputInfo.output = row.output;
                    self.dialog.outputInfo.url = row.url;
                    self.dialog.outputInfo.visible = true;
            	},
            	handleOutputInfoClose: function(){
            		var self = this;
            		self.dialog.outputInfo.output = '';
                    self.dialog.outputInfo.url = '';
                    self.dialog.outputInfo.visible = false;
            	},
            	handleRowExchangeProgram: function(scope){
            		var self = this;
                    var row = scope.row;
                    var keyword = self.dialog.exchangeProgram.keyword;
                    self.dialog.exchangeProgram.visible = true;
            	},
            	handleExchangeProgramClose: function(){
            		var self = this;
            		self.dialog.exchangeProgram.keyword = '';
                    self.dialog.exchangeProgram.visible = false;
            	},
            	handleRowCover: function(scope){
            		var self = this;
                    var row = scope.row;
                    self.dialog.taskCover.visible = true;
            	},
            	handleAddTask: function(){
            		this.$router.handleAddTask();
            	},
            	handleAddGroupTask: function(){
            		this.$router.handleAddGroupTask();
            	},
            	handleDelGroupTask: function(){
            		this.$router.handleDelGroupTask();
            	},
            	handleImportTask: function(){
            		this.$router.handleImportTask();
            	},
            	handleExportTask: function(){
            		this.$router.handleExportTask();
            	},
            	handleClick: function(){
            		this.$router.handleClick();
            	},
            	handleAddProgramQtUserSet:function(){
            		this.$router.handleAddProgramQtUserSet();
            	},
            	handleAddProgramPushUserSet:function(){
            		this.$router.handleAddProgramPushUserSet();
            	},
            	handleAddProgramCommit:function(){
            		this.$router.handleAddProgramCommit();
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
                }
            },
            created: function () {
                var self = this;
//                self.getChannelList();
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