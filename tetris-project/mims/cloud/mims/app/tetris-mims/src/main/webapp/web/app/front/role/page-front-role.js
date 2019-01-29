define([
    'text!' + window.APPPATH + 'front/role/page-front-role.html',
    'config',
    'restfull',
    'jquery',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'mi-sub-title',
    'mi-user-dialog',
    'css!' + window.APPPATH + 'front/role/page-front-role.css',
], function(tpl, config, ajax, $, context, commons, Vue){

    var pageId = 'page-front-role';

    var init = function(){

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el:'#' + pageId + '-wrapper',
            data:{
                menus:context.getProp('menus'),
                user:context.getProp('user'),
                groups:context.getProp('groups'),
                tree:{
                    props:{
                        label: 'name',
                        children: 'children',
                        isLeaf: 'leaf'
                    },
                    expandOnClickNode:false,
                    data:[]
                },
                table:{
                    permission:{
                        visible:false,
                        rows:[],
                        row:''
                    },
                    role:{
                        currentHighlight:true,
                        rows:[]
                    },
                    user:{
                        visible:false,
                        rows:[],
                        row:'',
                        pageSize:50,
                        currentPage:0,
                        total:0
                    }
                },
                dialog:{
                    role:{
                        add:{
                            visible:false,
                            loading:false,
                            name:''
                        },
                        edit:{
                            visible:false,
                            loading:false,
                            name:'',
                            row:''
                        }
                    },
                    permission:{
                        visible:false,
                        loading:false,
                        rows:[],
                        selected:[]
                    }
                }
            },
            methods:{
                //渲染函数
                renderTree:function(h, scope){
                    var data = scope.data;
                    return h('div', null, [
                        h('span', {class:'icon-folder-close'}, []),
                        h('span', {class:'node-text'}, data.name)
                    ]);
                },
                roleRowKey:function(row){
                    return 'role-' + row.id;
                },
                permissionRowKey:function(row){
                    return 'permission-' + row.id;
                },
                userRowKey:function(row){
                    return 'user-' + row.uuid;
                },
                treeNodeClick:function(nodeData){
                    var self = this;
                    self.table.permission.row = nodeData;
                    self.table.permission.visible = true;
                    self.table.permission.rows.splice(0, self.table.permission.rows.length);
                    ajax.post('/folder/role/permission/list', {
                        folderId:nodeData.id
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.table.permission.rows.push(data[i]);
                            }
                        }
                    });
                },
                handleRoleAdd:function(){
                    var self = this;
                    self.dialog.role.add.visible = true;
                },
                handleRoleAddClose:function(){
                    var self = this;
                    self.dialog.role.add.loading = false;
                    self.dialog.role.add.name = '';
                    self.dialog.role.add.visible = false;
                },
                handleRoleAddOk:function(){
                    var self = this;
                    var name = self.dialog.role.add.name;
                    self.dialog.role.add.loading = true;
                    ajax.post('/role/add', {
                        name:name
                    }, function(data, status){
                        self.dialog.role.add.loading = false;
                        if(status !== 200) return;
                        self.table.role.rows.splice(0, 0, data);
                        self.dialog.role.add.visible = false;
                        self.$message({
                            type:'success',
                            message:'操作成功！'
                        });
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                handleRoleEdit:function(scope){
                    var self = this;
                    self.dialog.role.edit.visible = true;
                    self.dialog.role.edit.row = scope.row;
                    self.dialog.role.edit.name = scope.row.name;
                },
                handleRoleEditClose:function(){
                    var self = this;
                    self.dialog.role.edit.loading = false;
                    self.dialog.role.edit.name = '';
                    self.dialog.role.edit.row = '';
                    self.dialog.role.edit.visible = false;
                },
                handleRoleEditOk:function(){
                    var self = this;
                    var row = self.dialog.role.edit.row;
                    var name = self.dialog.role.edit.name;
                    self.dialog.role.edit.laoding = true;
                    ajax.post('/role/edit/' + row.id, {
                        name:name
                    }, function(data, status){
                        self.dialog.role.edit.laoding = false;
                        if(status !== 200) return;
                        for(var i=0; i<self.table.role.rows.length; i++){
                            if(self.table.role.rows[i].id === row.id){
                                self.table.role.rows[i].name = name;
                                break;
                            }
                        }
                        self.dialog.role.edit.visible = false;
                        self.$message({
                            type:'success',
                            message:'操作成功！'
                        });
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                handleRoleDelete:function(scope){
                    var self = this;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'危险操作',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['此操作将永久删除角色以及权限，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            if(action === 'confirm'){
                                var row = scope.row;
                                ajax.post('/role/delete/' + row.id, null, function(data){
                                    for(var i=0; i<self.table.role.rows.length; i++){
                                        if(self.table.role.rows[i].id === row.id){
                                            self.table.role.rows.splice(i, 1);
                                            break;
                                        }
                                    }
                                    done();
                                    self.$message({
                                        type:'success',
                                        message:'操作成功！'
                                    });
                                });
                            }else{
                                done();
                            }
                        }
                    });
                },
                roleCurrentChange:function(currentRow, oldRow){
                    var self = this;
                    self.table.user.visible = true;
                    self.table.user.row = currentRow;
                    self.loadBindingUser(self.table.user.currentPage + 1);
                },
                loadBindingUser:function(currentPage){
                    var self = this;
                    var currentRow = self.table.user.row;
                    self.table.user.rows.splice(0, self.table.user.rows.length);
                    ajax.post('/role/get/binding/users/' + currentRow.id, {
                        pageSize:self.table.user.pageSize,
                        currentPage:currentPage,
                    }, function(data){
                        var total = data.total;
                        var users = data.users;
                        if(users && users.length>0){
                            for(var i=0; i<users.length; i++){
                                self.table.user.rows.push(users[i]);
                            }
                        }
                        self.table.user.total = total;
                        self.table.user.currentPag = currentPage;
                    });
                },
                handleUserBinding:function(){
                    var self = this;
                    var role = self.table.user.row;
                    self.$refs.userDialog.setBuffer(role);
                    self.$refs.userDialog.open('/user/list');
                },
                userSelected:function(users, startLoading, endLoading, close){
                    var self = this;
                    var role = self.$refs.userDialog.getBuffer();
                    var userIds = [];
                    for(var i=0; i<users.length; i++){
                        userIds.push(users[i].uuid);
                    }
                    startLoading();
                    ajax.post('/role/user/binding', {
                        roleId:role.id,
                        userIds: $.toJSON(userIds)
                    }, function(data, status){
                        endLoading();
                        if(status !== 200) return;
                        for(var i=0; i<data.length; i++){
                            self.table.user.rows.push(data[i]);
                        }
                        close();
                        self.$message({
                            type:'success',
                            message:'操作成功！'
                        });
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                handleUserCurrentChange:function(currentPage){
                    var self = this;
                    self.loadBindingUser(currentPage);
                },
                handleUserUnbinding:function(scope){
                    var self = this;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'危险操作',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['此操作将永久删除权限，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            if(action === 'confirm'){
                                var roleId = self.table.user.row.id;
                                var userId = scope.row.uuid;
                                ajax.post('/role/user/unbinding', {
                                    roleId:roleId,
                                    userId:userId
                                }, function(){
                                    for(var i=0; i<self.table.user.rows.length; i++){
                                        if(self.table.user.rows[i].uuid === userId){
                                            self.table.user.rows.splice(i, 1);
                                            break;
                                        }
                                    }
                                    done();
                                    self.$message({
                                        type:'success',
                                        message:'操作成功！'
                                    });
                                });
                            }else{
                                done();
                            }
                        }
                    });
                },
                handlePermissionBinding:function(){
                    var self = this;
                    self.dialog.permission.rows.splice(0, self.dialog.permission.rows.length);
                    ajax.post('/role/list/except/folder/permission', {
                        folderId:self.table.permission.row.id
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.dialog.permission.rows.push(data[i]);
                            }
                        }
                        self.dialog.permission.visible = true;
                    });
                },
                permissionRowKey:function(row){
                    return 'permission-'+row.uuid;
                },
                handlePermissionChange:function(val){
                    var self = this;
                    if(val && val.length>0) self.dialog.permission.selected = val;
                },
                handlePermissionOk:function(){
                    var self = this;
                    var roles = self.dialog.permission.selected;
                    if(!roles || roles.length<=0){
                        self.$message({
                            message:'您没有选择任何数据！',
                            type:'warning'
                        });
                        return;
                    }
                    var roleIds = [];
                    for(var i=0; i<roles.length; i++){
                        roleIds.push(roles[i].id);
                    }
                    var folder = self.table.permission.row;
                    self.dialog.permission.loading = true;
                    ajax.post('/folder/role/permission/add', {
                        folderId:folder.id,
                        roleIds: $.toJSON(roleIds)
                    }, function(data, status){
                        self.dialog.permission.loading = false;
                        if(status !== 200) return;
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.table.permission.rows.splice(0, 0, data[i]);
                            }
                        }
                        self.dialog.permission.visible = false;
                        self.$message({
                            type:'success',
                            message:'操作成功！'
                        });
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                handlePermissionClose:function(){
                    var self = this;
                    self.dialog.permission.visible = false;
                    self.dialog.permission.rows = [];
                    self.dialog.permission.selected = [];
                },
                handlePermissionUnbinding:function(scope){
                    var self = this;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'危险操作',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['此操作将永久删除权限，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            if(action === 'confirm'){
                                var folderId = self.table.permission.row.id;
                                ajax.post('/folder/role/permission/delete', {
                                    folderId:folderId,
                                    roleId:scope.row.id
                                }, function(){
                                    for(var i=0; i<self.table.permission.rows.length; i++){
                                        if(self.table.permission.rows[i].id === scope.row.id){
                                            self.table.permission.rows.splice(i, 1);
                                            break;
                                        }
                                    }
                                    done();
                                    self.$message({
                                        type:'success',
                                        message:'操作成功！'
                                    });
                                });
                            }else{
                                done();
                            }
                        }
                    }).catch(function(){});
                }
            },
            created:function(){
                var self = this;
                ajax.post('/folder/media/tree', null, function(data){
                    for(var i=0; i<data.length; i++){
                        self.tree.data.push(data[i]);
                    }
                });

                ajax.post('/role/list', null, function(data){
                    if(data && data.length>0){
                        for(var i=0; i<data.length; i++){
                            self.table.role.rows.push(data[i]);
                        }
                    }
                });
            }
        });

    };

    var destroy = function(){

    };

    var groupList = {
        path:'/' + pageId,
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;
});