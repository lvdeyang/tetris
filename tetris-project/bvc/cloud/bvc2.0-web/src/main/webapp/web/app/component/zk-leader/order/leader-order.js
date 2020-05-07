define([
    'text!' + window.APPPATH + 'component/zk-leader/order/leader-order.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'css!' + window.APPPATH + 'component/zk-leader/order/leader-order.css'
], function(tpl, ajax, $, Vue,config){

	//组件名称
    var pluginName = 'leader-order';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data:function(){
            return {
            	baseUrl:window.BASEPATH,
                currentTab:'0',
                qt:'',
                screenId:'',
                tree:{
                    props:{
                        children:'children',
                        label:'name'
                    },
                    resource:{
                        data:[],
                        select:''
                    },
                    device:{
                        data:[],
                        select:''
                    },
                    record:{
                        data:[],
                        select:''
                    },
                    user:{
                        data:[],
                        select:''
                    }
                }
            }
        },
        watch:{
            //切换tab
            currentTab:function(){
                var self = this;
                if(self.currentTab == 0){
                    //文件资源
                    if(self.tree.resource.data.length === 0){
                        self.refreshFile();
                    }
                }else if(self.currentTab == 1){
                    //设备资源
                    if(self.tree.device.data.length === 0){
                        self.refreshDevice();
                    }
                }else if(self.currentTab == 2){
                    //录像资源
                    if(self.tree.record.data.length === 0){
                        self.refreshRecord();
                    }
                }else if(self.currentTab == 3){
                    //用户
                    if(self.tree.user.data.length === 0){
                        self.refreshUser();
                    }
                }
            }
        },
        methods:{
            //关闭弹窗
            handleClose:function(){
                var self = this;
                self.qt.destroy();
            },
            //获取设备数据
            refreshDevice:function(){
                var self = this;
                self.tree.device.data.splice(0, self.tree.device.data.length);
                ajax.post('/command/query/find/institution/tree/bundle/2/false/0', null, function(data){
                    if(data && data.length>0){
                        for(var i=0; i<data.length; i++){
                            self.tree.device.data.push(data[i]);
                        }
                    }
                });
            },
            //获取文件数据
            refreshFile:function(){
                var self = this;
                self.tree.resource.data.splice(0, self.tree.resource.data.length);
                ajax.post('/monitor/vod/query/resource/tree', null, function(data){
                    if(data && data.length>0){
                        for(var i=0; i<data.length; i++){
                            self.tree.resource.data.push(data[i]);
                        }
                    }
                });
            },
            //获取录像回放数据
            refreshRecord:function(){
                var self=this;
                ajax.post('/command/record/query',null,function (data) {
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
                        self.tree.record.data=JSON.parse(JSON.stringify(data.groups));
                    }
                })
            },
            //获取组织机构数据
            refreshUser:function () {
                var self = this;
                self.tree.resource.data.splice(0, self.tree.resource.data.length);
                ajax.post('/command/query/find/institution/tree/user/filter/0', null, function(data){
                    if(data && data.length>0){
                        for(var i=0; i<data.length; i++){
                            self.tree.user.data.push(data[i]);
                        }
                    }
                });
            },
            //确定按钮
            handleOrder:function(){
                var self = this;
                if(self.currentTab == 0){
                    if(!self.tree.resource.select.length){
                        self.qt.warning('您还没有选择资源文件');
                        return;
                    }
                    ajax.post('/command/vod/resource/file/start/player', {
                        resourceFileId: self.tree.resource.select,
                        serial:self.screenId
                    }, function (data) {
                        self.qt.linkedWebview('hidden',{id:'playVodFile', params:$.toJSON([data])});
                        self.handleClose();
                    });
                }else if(self.currentTab == 1){
                    if(!self.tree.device.select.length){
                        self.qt.warning('您还没有选择设备资源');
                        return;
                    }
                    ajax.post('/command/vod/device/start/player', {
                        deviceId: self.tree.device.select,
                        serial:self.screenId
                    }, function (data) {
                        self.qt.linkedWebview('hidden',{id:'playVodDevice', params:$.toJSON([data])});
                        self.handleClose();
                    });
                }else if(self.currentTab == 2){
                    if(!self.tree.record.select.length){
                        self.qt.warning('您还没有选择录像资源');
                        return;
                    }
                }else if(self.currentTab == 3){
                    if(!self.tree.user.select.length){
                        self.qt.warning('您还没有选择用户资源');
                        return;
                    }
                    ajax.post('/command/vod/user/start/player', {
                        userId: self.tree.user.select,
                        serial:self.screenId
                    }, function (data) {
                        self.qt.linkedWebview('hidden',{id:'playVodUsers', params:$.toJSON([data])});
                        self.handleClose();
                    });
                }
            }
        },
        mounted:function(){
            var self = this;
            self.qt = new QtContext('leaderOrder', function(){
                var params = self.qt.getWindowParams();
               self.screenId=params.serial; //获取到第几屏

                //初始化ajax
                ajax.init({
                    login:config.ajax.login,
                    authname:config.ajax.authname,
                    debug:config.ajax.debug,
                    messenger:{
                        info:function(message, status){
                           self.qt.info(message)
                        },
                        success:function(message, status){
                            self.qt.success(message)
                        },
                        warning:function(message, status){
                            self.qt.warning(message)
                        },
                        error:function(message, status){
                           self.qt.error(message)
                        }
                    }
                });

                self.refreshFile();
            });
        }
    });

    return Vue;
});