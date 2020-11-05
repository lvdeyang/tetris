define([
    'text!' + window.APPPATH + 'component/zk-leader/cast-decoder-devices/leader-cast-decoder-devices.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'css!' + window.APPPATH + 'component/zk-leader/cast-decoder-devices/leader-cast-decoder-devices.css'
], function(tpl, ajax, $, Vue, config){

	//组件名称
    var pluginName = 'leader-cast-decoder-devices';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data:function(){
            return {
            	baseUrl:window.BASEPATH,
                qt:'',
                dialog:{
                    decodeBind:{
                        visible:false,
                        serial:'',
                        tree:{
                            data:[],
                            props:{
                                children:'children',
                                label:'name'
                            },
                            currentBundle:''
                        },
                        table:{
                            data:[]
                        },
                        loading:false
                    }
                }
            }
        },
        methods:{
            //取消关闭事件
            handleDecodeBindClose:function(){
                var self = this;
                self.dialog.decodeBind.visible = false;
                self.dialog.decodeBind.serial = '';
                self.dialog.decodeBind.tree.data.splice(0, self.dialog.decodeBind.tree.data.length);
                self.dialog.decodeBind.tree.currentBundle = '';
                self.dialog.decodeBind.table.data.splice(0, self.dialog.decodeBind.table.data.length);
                self.qt.destroy();
            },
            //绑定按钮提交事件
            handleDecodeBindCommit:function(){
                var self = this;
                if(!self.dialog.decodeBind.tree.currentBundle){
                    self.$message({
                        type:'success',
                        message:'您还没有选择设备！'
                    });
                    return;
                }
                var currentBundle = $.parseJSON(self.dialog.decodeBind.tree.currentBundle);
                var bundleId = currentBundle.bundleId;
                var bundleName = currentBundle.bundleName;
                self.dialog.decodeBind.loading = true;
                ajax.post('/command/decoder/add/screen/cast/device', {
                    screenId:self.dialog.decodeBind.serial,
                    bundleId:bundleId
                }, function(data, status){
                    console.log(data)
                    self.dialog.decodeBind.loading = false;
                    if(status!=200) return;
                    self.dialog.decodeBind.table.data.push({
                        bundleId:bundleId,
                        name:bundleName
                    });
                    self.refreshDecodeBindTree();
                    self.dialog.decodeBind.tree.currentBundle = '';
                    self.qt.linkedWebview('historyMessage',{id:'showDecoderTitle', params:data});
                }, null, [403, 404, 408, 409, 500]);
            },
            //删除事件
            handleDecodeBindRemove:function(scope){
                var self = this;
                var row = scope.row;
                ajax.post('/command/decoder/remove/screen/cast/device', {
                    screenId:self.dialog.decodeBind.serial,
                    bundleId:row.bundleId
                }, function(data){
                    for(var i=0; i<self.dialog.decodeBind.table.data.length; i++){
                        if(self.dialog.decodeBind.table.data[i].bundleId == row.bundleId){
                            self.dialog.decodeBind.table.data.splice(i, 1);
                            break;
                        }
                    }
                    self.refreshDecodeBindTree();
                    self.qt.linkedWebview('historyMessage',{id:'showDecoderTitle', params:data});
                });
            },
            //获取树形结构的数据
            refreshDecodeBindTree:function(){
                var self = this;
                var serial = self.dialog.decodeBind.serial;
                ajax.post('/command/query/find/institution/tree/bundle/for/screen', {
                    screenId:serial,
                    withChannel:false
                }, function(data){
                    self.dialog.decodeBind.tree.data.splice(0, self.dialog.decodeBind.tree.data.length);
                    for(var i=0; i<data.length; i++){
                        self.dialog.decodeBind.tree.data.push(data[i]);
                    }
                });
            },
            //获取绑定设备的数据
            refreshDecodeBindTable:function(){
                var self = this;
                var screenId = self.dialog.decodeBind.serial;
                ajax.post('/command/decoder/query/screen/cast/devices', {
                	screenId:screenId
                }, function(data){
                    self.dialog.decodeBind.table.data.splice(0, self.dialog.decodeBind.table.data.length);
                    if(data && data.length>0){
                        for(var i=0; i<data.length; i++){
                            self.dialog.decodeBind.table.data.push(data[i]);
                        }
                    }
                });
            }
        },
        mounted:function(){
            var self = this;
            self.qt = new QtContext('leaderCastDevices', function(){
                //初始化ajax
                ajax.init({
                    login:config.ajax.login,
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

                var params = self.qt.getWindowParams();
                self.dialog.decodeBind.serial = params.serial;
                self.refreshDecodeBindTree();
                self.refreshDecodeBindTable();
            });
        }
    });

    return Vue;
});