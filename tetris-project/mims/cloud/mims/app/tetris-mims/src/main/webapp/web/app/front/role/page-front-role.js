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
                    }
                },
                dialog:{
                    permission:{
                        visible:false,
                        loading:false,
                        rows:[],
                        selected:[]
                    }
                }
            },
            methods:{
                renderTree:function(h, scope){
                    var data = scope.data;
                    return h('div', null, [
                        h('span', {class:'icon-folder-close'}, []),
                        h('span', {class:'node-text'}, data.name)
                    ]);
                },
                permissionRowKey:function(row){
                    return 'permission-' + row.id;
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
                handlePermissionBinding:function(){
                    var self = this;
                    self.dialog.permission.rows.splice(0, self.dialog.permission.rows.length);
                    ajax.post('/folder/role/permission/query/role/with/folder/permission/except/'+self.table.permission.row.id, null, function(data){
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
                    var folder = self.table.permission.row;
                    self.dialog.permission.loading = true;
                    ajax.post('/folder/role/permission/add', {
                        folderId:folder.id,
                        roles: $.toJSON(roles)
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
                                ajax.post('/folder/role/permission/delete/' + scope.row.id, null, function(){
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