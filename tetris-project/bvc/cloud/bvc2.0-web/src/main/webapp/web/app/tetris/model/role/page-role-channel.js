/**
 * Created by lvdeyang on 2020/6/28.
 */
define([
    'text!' + window.APPPATH + 'tetris/model/role/page-role-channel.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'tetris/model/role/page-role-channel.css'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-role-channel';

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
                channelTypes:[],
                table:{
                    rows:[]
                },
                dialog: {
                    createRoleChannel:{
                        visible:false,
                        loading:false,
                        name:'',
                        type:''
                    },
                    editRoleChannel:{
                        visible:false,
                        loading:false,
                        id:'',
                        name:'',
                        type:''
                    }
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                rowKey:function(row){
                    return 'role-channel-' + row.uuid;
                },
                handleCreate:function(){
                    var self = this;
                    self.dialog.createRoleChannel.visible = true;
                },
                handleCreateRoleChannelClose:function(){
                    var self = this;
                    self.dialog.createRoleChannel.name = '';
                    self.dialog.createRoleChannel.type = '';
                    self.dialog.createRoleChannel.loading = false;
                    self.dialog.createRoleChannel.visible = false;
                },
                handleCreateRoleChannelSubmit:function(){
                    var self = this;
                    if(!self.dialog.createRoleChannel.name){
                        self.$message({
                            type:'error',
                            message:'名称不能为空'
                        });
                        return;
                    }
                    if(!self.dialog.createRoleChannel.type){
                        self.$message({
                            type:'error',
                            message:'类型不能为空'
                        });
                        return;
                    }
                    self.dialog.createRoleChannel.loading = true;
                    ajax.post('/tetris/bvc/model/role/channel/add', {
                        name:self.dialog.createRoleChannel.name,
                        type:self.dialog.createRoleChannel.type,
                        roleId:self.roleId
                    }, function(data, status){
                        self.dialog.createRoleChannel.loading = false;
                        if(status !== 200) return;
                        self.table.rows.push(data);
                        self.handleCreateRoleChannelClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleDelete:function(){

                },
                handleRowEdit:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.editRoleChannel.id = row.id;
                    self.dialog.editRoleChannel.name = row.name;
                    self.dialog.editRoleChannel.type = row.type;
                    self.dialog.editRoleChannel.visible = true;
                },
                handleEditRoleChannelClose:function(){
                    var self = this;
                    self.dialog.editRoleChannel.id = '';
                    self.dialog.editRoleChannel.name = '';
                    self.dialog.editRoleChannel.type = '';
                    self.dialog.editRoleChannel.loading = false;
                    self.dialog.editRoleChannel.visible = false;
                },
                handleEditRoleChannelSubmit:function(){
                    var self = this;
                    if(!self.dialog.editRoleChannel.name){
                        self.$message({
                            type:'error',
                            message:'名称不能为空'
                        });
                        return;
                    }
                    if(!self.dialog.editRoleChannel.type){
                        self.$message({
                            type:'error',
                            message:'类型不能为空'
                        });
                        return;
                    }
                    self.dialog.editRoleChannel.loading = true;
                    ajax.post('/tetris/bvc/model/role/channel/edit', {
                        id:self.dialog.editRoleChannel.id,
                        name:self.dialog.editRoleChannel.name,
                        type:self.dialog.editRoleChannel.type,
                        roleId:self.roleId
                    }, function(data, status){
                        self.dialog.editRoleChannel.loading = false;
                        if(status!==200) return;
                        for(var i=0; i<self.table.rows.length; i++){
                            if(self.table.rows[i].id == data.id){
                                self.table.rows.splice(i, 1, data);
                            }
                        }
                        self.handleEditRoleChannelClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleEditTerminalChannel:function(scope){
                    var self = this;
                    var row = scope.row;
                    window.location.hash = '#/page-role-channel-terminal-channel-permission/'+self.roleId+'/'+self.roleName+'/'+row.id + '/'+row.name + '/' + row.type;
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
                                ajax.post('/tetris/bvc/model/role/channel/delete', {
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
                    ajax.post('/tetris/bvc/model/role/channel/load', {
                        roleId:self.roleId
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.table.rows.push(data[i]);
                            }
                        }
                    });
                },
                queryTypes:function(){
                    var self = this;
                    ajax.post('/tetris/bvc/model/role/channel/query/types', null, function(data){
                        for(var i in data){
                            self.channelTypes.push({
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
        path:'/' + pageId + '/:roleId/:roleName',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});