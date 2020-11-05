/**
 * Created by lvdeyang on 2020/6/29.
 */
define([
    'text!' + window.APPPATH + 'tetris/model/role/page-role-channel-terminal-channel-permission.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'tetris/model/role/page-role-channel-terminal-channel-permission.css'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-role-channel-terminal-channel-permission';

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
                groups: context.getProp('groups'),
                roleId:p.roleId,
                roleName:p.roleName,
                roleChannelId: p.roleChannelId,
                roleChannelName: p.roleChannelName,
                roleChannelType: p.roleChannelType,
                terminals:[],
                table:{
                    rows:[]
                },
                dialog: {
                    addPermission:{
                        visible:false,
                        loading:false,
                        terminalId:'',
                        selectChannelVisible:false,
                        props:{
                            label:'name',
                            children:'children',
                            isLeaf:'isLeaf'
                        },
                        channels:[],
                        selectedChannels:[]
                    }
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                rowKey:function(row){
                    return 'permission-' + row.uuid;
                },
                handleCreate:function(){
                    var self = this;
                    self.dialog.addPermission.visible = true;
                },
                handleAddPermissionClose:function(){
                    var self = this;
                    self.dialog.addPermission.terminalId = '';
                    self.dialog.addPermission.selectedChannels.splice(0, self.dialog.addPermission.selectedChannels.length);
                    self.dialog.addPermission.loading = false;
                    self.dialog.addPermission.visible = false;
                },
                handleSelectChannel:function(){
                    var self = this;
                    if(!self.dialog.addPermission.terminalId) return;
                    ajax.post('/tetris/bvc/model/terminal/channel/load', {
                        terminalId:self.dialog.addPermission.terminalId
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                if(data[i].type === self.roleChannelType){
                                    var finded = false;
                                    for(var j=0; j<self.dialog.addPermission.selectedChannels.length; j++){
                                        if(self.dialog.addPermission.selectedChannels[j].id == data[i].id){
                                            finded = true;
                                            break;
                                        }
                                    }
                                    if(!finded) self.dialog.addPermission.channels.push(data[i]);
                                }
                            }
                        }
                        self.dialog.addPermission.selectChannelVisible = true;
                    });
                },
                handleRemoveSelectedChannel:function(channel){
                    var self = this;
                    for(var i=0; i<self.dialog.addPermission.selectedChannels.length; i++){
                        if(self.dialog.addPermission.selectedChannels[i] === channel){
                            self.dialog.addPermission.selectedChannels.splice(i, 1);
                            break;
                        }
                    }
                },
                handleAddPermissionSelectChannelClose:function(){
                    var self = this;
                    var nodes = self.$refs.addPermissionChannelsTree.getCheckedNodes();
                    if(nodes && nodes.length>0){
                        for(var i=0; i<nodes.length; i++){
                            self.dialog.addPermission.selectedChannels.push(nodes[i]);
                        }
                    }
                    self.dialog.addPermission.selectChannelVisible = false;
                    self.dialog.addPermission.channels.splice(0, self.dialog.addPermission.channels.length);
                },
                handleAddPermissionSubmit:function(){
                    var self = this;
                    if(!self.dialog.addPermission.terminalId){
                        self.$message({
                            type:'error',
                            message:'请选择终端'
                        });
                        return;
                    }
                    var terminalChannelIds = [];
                    for(var i=0; i<self.dialog.addPermission.selectedChannels.length; i++){
                        terminalChannelIds.push(self.dialog.addPermission.selectedChannels[i].id);
                    }
                    if(terminalChannelIds.length <= 0){
                        self.$message({
                            type:'error',
                            message:'请选择通道'
                        });
                        return;
                    }
                    self.dialog.addPermission.loading = true;
                    ajax.post('/tetris/bvc/model/role/channel/terminal/bundle/channel/permission/add', {
                        roleId:self.roleId,
                        roleChannelId:self.roleChannelId,
                        terminalId:self.dialog.addPermission.terminalId,
                        terminalChannelIds: $.toJSON(terminalChannelIds),
                    }, function(data, status){
                        self.dialog.addPermission.loading = false;
                        if(status !== 200) return;
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.table.rows.push(data[i]);
                            }
                        }
                        self.handleAddPermissionClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleDelete:function(){

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
                                h('p', null, ['此操作将永久删除该关联，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/role/channel/terminal/bundle/channel/permission/delete', {
                                    id:row.id
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    for(var i=0; i<self.table.rows.length; i++){
                                        if(self.table.rows[i].id == row.id){
                                            self.table.rows.splice(i, 1);
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
                    ajax.post('/tetris/bvc/model/role/channel/terminal/bundle/channel/permission/load', {
                        roleChannelId:self.roleChannelId
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.table.rows.push(data[i]);
                            }
                        }
                    });
                },
                queryTerminals:function(){
                    var self = this;
                    ajax.post('/tetris/bvc/model/terminal/load/all', null, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.terminals.push(data[i]);
                            }
                        }
                    });
                }
            },
            created:function(){
                var self = this;
                self.load();
                self.queryTerminals();
            }
        });

    };

    var destroy = function(){

    };

    var groupList = {
        path:'/' + pageId + '/:roleId/:roleName/:roleChannelId/:roleChannelName/:roleChannelType',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});