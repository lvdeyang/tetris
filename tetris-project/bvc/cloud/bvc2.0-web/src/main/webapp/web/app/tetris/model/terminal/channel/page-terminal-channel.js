/**
 * Created by lvdeyang on 2020/6/18.
 */
define([
    'text!' + window.APPPATH + 'tetris/model/terminal/channel/page-terminal-channel.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'tetris/model/terminal/channel/page-terminal-channel.css'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-terminal-channel';

    var init = function(p){

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el: '#' + pageId + '-wrapper',
            data: {
                menus: context.getProp('menus'),
                user: context.getProp('user'),
                terminalId:p.terminalId,
                terminalName:p.terminalName,
                groups: context.getProp('groups'),
                bundleTypes:[],
                types:[],
                table:{
                    rows:[]
                },
                dialog: {
                    addChannel:{
                        visible:false,
                        loading:false,
                        name:'',
                        type:'',
                        bundlesVisible:false,
                        bundles:[],
                        channelsVisible:false,
                        channels:[],
                        terminalBundleId:'',
                        terminalBundleName:'',
                        realChannelId:''
                    },
                    editChannel:{
                        visible:false,
                        loading:false,
                        id:'',
                        name:'',
                        type:'',
                        bundlesVisible:false,
                        bundles:[],
                        channelsVisible:false,
                        channels:[],
                        terminalBundleId:'',
                        terminalBundleName:'',
                        realChannelId:''
                    }
                }
            },
            computed:{
                dialogAddChannelType:function(){
                    var self = this;
                    return self.dialog.addChannel.type;
                },
                dialogAddChannelTerminalBundleId:function(){
                    var self = this;
                    return self.dialog.addChannel.terminalBundleId;
                },
                dialogEditChannelType:function(){
                    var self = this;
                    return self.dialog.editChannel.type;
                },
                dialogEditChannelTerminalBundleId:function(){
                    var self = this;
                    return self.dialog.editChannel.terminalBundleId;
                }
            },
            watch:{
                dialogAddChannelType:function(val){
                    if(!val) return;
                    var self = this;
                    self.dialog.addChannel.terminalBundleId = '';
                    self.dialog.addChannel.terminalBundleName = '';
                    self.dialog.addChannel.realChannelId = '';
                    self.loadBundles(self.dialog.addChannel);
                },
                dialogAddChannelTerminalBundleId:function(val){
                    if(!val) return;
                    var self = this;
                    self.dialog.addChannel.realChannelId = '';
                    self.loadChannels(self.dialog.addChannel);
                },
                dialogEditChannelType:function(val){
                    if(!val) return;
                    var self = this;
                    self.dialog.editChannel.terminalBundleId = '';
                    self.dialog.editChannel.terminalBundleName = '';
                    self.dialog.editChannel.realChannelId = '';
                    self.loadBundles(self.dialog.editChannel);
                },
                dialogEditChannelTerminalBundleId:function(val){
                    if(!val) return;
                    var self = this;
                    self.dialog.editChannel.realChannelId = '';
                    self.loadChannels(self.dialog.editChannel);
                }
            },
            methods:{
                rowKey:function(row, fn){
                    return 'terminal-' + row.uuid;
                },
                loadBundles:function(scope){
                    var self = this;
                    scope.bundles.splice(0, scope.bundles.length);
                    scope.channels.splice(0, scope.channels.length);
                    ajax.post('/tetris/bvc/model/terminal/bundle/load/by/type', {
                        terminalId:self.terminalId,
                        type:scope.type
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                scope.bundles.push(data[i]);
                            }
                        }
                        if(typeof fn === 'function') fn();
                    });
                },
                bundleRowKey:function(row){
                    return 'bundle-' + row.id;
                },
                loadChannels:function(scope, fn){
                    scope.channels.splice(0, scope.channels.length);
                    ajax.post('/tetris/bvc/model/terminal/bundle/channel/load/by/type', {
                        terminalBundleId:scope.terminalBundleId,
                        type:scope.type
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                scope.channels.push(data[i]);
                            }
                        }
                        if(typeof fn === 'function') fn();
                    });
                },
                channelRowKey:function(row){
                    return 'channel-' + row.channelId;
                },
                handleCreate:function(){
                    var self = this;
                    self.dialog.addChannel.type = self.types[0].id;
                    self.dialog.addChannel.visible = true;
                },
                handleAddChannelSelectBundle:function(){
                    var self = this;
                    self.dialog.addChannel.bundlesVisible = true;
                },
                handleAddChannelSelectBundleChange:function(currentRow){
                    if(!currentRow) return;
                    var self = this;
                    self.dialog.addChannel.terminalBundleId = currentRow.id;
                    self.dialog.addChannel.terminalBundleName = currentRow.name;
                },
                handleAddChannelSelecChannel:function(){
                    var self = this;
                    self.dialog.addChannel.channelsVisible = true;
                },
                handleAddChannelSelectChannelChange:function(currentRow){
                    if(!currentRow) return;
                    var self = this;
                    self.dialog.addChannel.realChannelId = currentRow.channelId;
                },
                handleAddChannelClose:function(){
                    var self = this;
                    self.dialog.addChannel.name = '';
                    self.dialog.addChannel.type = '';
                    self.dialog.addChannel.terminalBundleId = '';
                    self.dialog.addChannel.terminalBundleName = '';
                    self.dialog.addChannel.realChannelId = '';
                    self.dialog.addChannel.bundles.splice(0, self.dialog.addChannel.bundles.length);
                    self.dialog.addChannel.channels.splice(0, self.dialog.addChannel.channels.length);
                    self.dialog.addChannel.loading = false;
                    self.dialog.addChannel.visible = false;
                },
                handleAddChannelSubmit:function(){
                    var self = this;
                    if(!self.dialog.addChannel.name){
                        self.$message({
                            type:'error',
                            message:'名称能为空'
                        });
                        return;
                    }
                    if(!self.dialog.addChannel.type){
                        self.$message({
                            type:'error',
                            message:'请选择通道类型'
                        });
                        return;
                    }
                    if(!self.dialog.addChannel.terminalBundleId){
                        self.$message({
                            type:'error',
                            message:'请选择设备'
                        });
                        return;
                    }
                    if(!self.dialog.addChannel.realChannelId){
                        self.$message({
                            type:'error',
                            message:'请选择通道'
                        });
                        return;
                    }
                    self.dialog.addChannel.loading = true;
                    ajax.post('/tetris/bvc/model/terminal/channel/add', {
                        name:self.dialog.addChannel.name,
                        type:self.dialog.addChannel.type,
                        terminalId:self.terminalId,
                        terminalBundleId:self.dialog.addChannel.terminalBundleId,
                        realChannelId:self.dialog.addChannel.realChannelId
                    }, function(data, status){
                        self.dialog.addChannel.loading = false;
                        if(status !== 200) return;
                        if(data){
                            self.table.rows.push(data);
                        }
                        self.handleAddChannelClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleDelete:function(){

                },
                handleRowEdit:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.editChannel.id = row.id;
                    self.dialog.editChannel.name = row.name;
                    self.dialog.editChannel.type = row.type;
                    self.$nextTick(function(){
                        self.dialog.editChannel.terminalBundleId = row.terminalBundleId;
                        self.dialog.editChannel.terminalBundleName =  row.terminalBundleName;
                        self.$nextTick(function(){
                            self.dialog.editChannel.realChannelId = row.realChannelId;
                            self.dialog.editChannel.visible = true;
                            self.dialog.editChannel.loading = false;
                        });
                    });
                },
                handleEditChannelSelectBundle:function(){
                    var self = this;
                    self.dialog.editChannel.bundlesVisible = true;
                    self.$nextTick(function(){
                        if(self.dialog.editChannel.terminalBundleId){
                            for(var i=0; i<self.dialog.editChannel.bundles.length; i++){
                                if(self.dialog.editChannel.bundles[i].id == self.dialog.editChannel.terminalBundleId){
                                    self.$refs.dialogEditChannelBundles.setCurrentRow(self.dialog.editChannel.bundles[i]);
                                    break;
                                }
                            }
                        }
                    });
                },
                handleEditChannelSelectBundleChange:function(currentRow){
                    if(!currentRow) return;
                    var self = this;
                    self.dialog.editChannel.terminalBundleId = currentRow.id;
                    self.dialog.editChannel.terminalBundleName = currentRow.name;
                },
                handleEditChannelSelecChannel:function(){
                    var self = this;
                    self.dialog.editChannel.channelsVisible = true;
                    self.$nextTick(function(){
                        if(self.dialog.editChannel.realChannelId){
                            for(var i=0; i<self.dialog.editChannel.channels.length; i++){
                                if(self.dialog.editChannel.channels[i].channelId == self.dialog.editChannel.realChannelId){
                                    self.$refs.dialogEditChannelChannels.setCurrentRow(self.dialog.editChannel.channels[i]);
                                    break;
                                }
                            }
                        }
                    });
                },
                handleEditChannelSelectChannelChange:function(currentRow){
                    if(!currentRow) return;
                    var self = this;
                    self.dialog.editChannel.realChannelId = currentRow.channelId;
                },
                handleEditChannelClose:function(){
                    var self = this;
                    self.dialog.editChannel.id = '';
                    self.dialog.editChannel.name = '';
                    self.dialog.editChannel.type = '';
                    self.dialog.editChannel.terminalBundleId = '';
                    self.dialog.editChannel.terminalBundleName = '';
                    self.dialog.editChannel.realChannelId = '';
                    self.dialog.editChannel.loading = false;
                    self.dialog.editChannel.visible = false;
                },
                handleEditChannelSubmit:function(){
                    var self = this;
                    self.dialog.editChannel.loading = true;
                    ajax.post('/tetris/bvc/model/terminal/channel/edit', {
                        id:self.dialog.editChannel.id,
                        name:self.dialog.editChannel.name,
                        type:self.dialog.editChannel.type,
                        terminalId:self.terminalId,
                        terminalBundleId:self.dialog.editChannel.terminalBundleId,
                        realChannelId:self.dialog.editChannel.realChannelId
                    }, function(data, status){
                        self.dialog.editChannel.loading = false;
                        if(status!==200) return;
                        if(data){
                            for(var i=0; i<self.table.rows.length; i++){
                                if(self.table.rows[i].id == data.id){
                                    self.table.rows.splice(i, 1, data);
                                    break;
                                }
                            }
                        }
                        self.handleEditChannelClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleRowDelete:function(scope){
                    var self = this;
                    var row = scope.row;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'危险操作',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['此操作将永久删除该通道，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/terminal/channel/delete', {
                                    id:row.id
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    for(var i=0; i<self.table.rows.length; i++){
                                        if(self.table.rows[i].id == row.id){
                                            self.table.rows.splice(i, 1);
                                            break;
                                        }
                                    }
                                }, null, ajax.TOTAL_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                load:function(){
                    var self = this;
                    self.table.rows.splice(0, self.table.rows.length);
                    ajax.post('/tetris/bvc/model/terminal/channel/load', {
                        terminalId:self.terminalId
                    }, function(data, status){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.table.rows.push(data[i]);
                            }
                        }
                    });
                },
                queryTypes:function(){
                    var self = this;
                    self.bundleTypes.splice(0, self.bundleTypes.length);
                    self.types.splice(0, self.types.length);
                    ajax.post('/tetris/bvc/model/terminal/channel/query/types', null, function(data){
                        for(var i in data){
                            self.types.push({
                                id:i,
                                name:data[i]
                            });
                        }
                    });
                }
            },
            created:function(){
                var self = this;
                self.load();
                self.queryTypes();
            }
        });

    };

    var destroy = function(){

    };

    var groupList = {
        path:'/' + pageId + '/:terminalId/:terminalName',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});