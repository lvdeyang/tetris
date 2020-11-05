define([
    'text!' + window.APPPATH + 'front/organization/page-front-organization.html',
    'config',
    'restfull',
    'jquery',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'mi-sub-title',
    'mi-user-dialog'
], function(tpl, config, ajax, $, context, commons, Vue){

    var pageId = 'page-front-organization';

    var init = function(){

        //设置标题
        commons.setTitle(pageId);

        ajax.post('/organization/list', null, function(data){

            var $page = document.getElementById(pageId);
            $page.innerHTML = tpl;

            new Vue({
                el:'#' + pageId + '-wrapper',
                data:{
                    menus:context.getProp('menus'),
                    user:context.getProp('user'),
                    groups:context.getProp('groups'),
                    table:{
                        rows:data || []
                    },
                    dialog:{
                        addOrganization:{
                            visible:false,
                            name:'',
                            loading:false
                        },
                        editOrganization:{
                            visible:false,
                            name:'',
                            loading:false,
                            rowId:''
                        }
                    }
                },
                methods:{
                    handleCreate:function(){
                        var self = this;
                        self.dialog.addOrganization.visible = true;
                    },
                    handleDelete:function(){

                    },
                    handleRowEdit:function(scope){
                        var self = this;
                        self.dialog.editOrganization.rowId = scope.row.id;
                        self.dialog.editOrganization.name = scope.row.name;
                        self.dialog.editOrganization.visible = true;
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
                                    h('p', null, ['此操作将永久删除数据，且不可恢复，是否继续?'])
                                ])
                            ]),
                            type:'wraning',
                            showCancelButton: true,
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            beforeClose:function(action, instance, done){
                                if(action === 'confirm'){
                                    ajax.post('/organization/delete/' + row.id, null, function(){
                                        for(var i=0; i<self.table.rows.length; i++){
                                            if(self.table.rows[i].id === row.id){
                                                self.table.rows.splice(i, 1);
                                                break;
                                            }
                                        }
                                        done();
                                    });
                                }else{
                                    done();
                                }
                            }
                        }).catch(function(){});
                    },
                    handleAddOrganizationClose:function(){
                        var self = this;
                        self.dialog.addOrganization.name = '';
                        self.dialog.addOrganization.visible = false;

                    },
                    handleAddOrganizationOk:function(){
                        var self = this;
                        self.dialog.addOrganization.loading = true;
                        ajax.post('/organization/add', {
                            name:self.dialog.addOrganization.name
                        }, function(data, status){
                            self.dialog.addOrganization.loading = false;
                            if(status !== 200) return;
                            self.table.rows.splice(0, 0, data);
                            self.dialog.addOrganization.visible = false;
                        }, null, ajax.NO_ERROR_CATCH_CODE);
                    },
                    handleEditOrganizationClose:function(){
                        var self = this;
                        self.dialog.editOrganization.name = '';
                        self.dialog.editOrganization.rowId = '';
                        self.dialog.editOrganization.visible = false;
                    },
                    handleEditOrganizationOk:function(){
                        var self = this;
                        self.dialog.editOrganization.loading = true;
                        var rowId = self.dialog.editOrganization.rowId;
                        ajax.post('/organization/edit/' + rowId, {
                            name:self.dialog.editOrganization.name
                        }, function(data, status){
                            self.dialog.editOrganization.loading = false;
                            if(status !== 200) return;
                            for(var i=0; i<self.table.rows.length; i++){
                                if(self.table.rows[i].id === rowId){
                                    self.table.rows[i].name = self.dialog.editOrganization.name;
                                    break;
                                }
                            }
                            self.dialog.editOrganization.visible = false;
                        }, null, ajax.NO_ERROR_CATCH_CODE);
                    },
                    handleUserUnbinding:function(user, scope){
                        var self = this;
                        var row = scope.row;
                        ajax.post('/organization/unbinding', {
                            id:row.id,
                            userId:user.uuid
                        }, function(){
                            var rows = self.table.rows;
                            for(var i=0; i<rows.length; i++){
                                var users = rows[i].users;
                                if(users && users.length>0){
                                    for(var j=0; j<users.length; j++){
                                        if(users[j].uuid === user.uuid){
                                            users.splice(j, 1);
                                            break;
                                        }
                                    }
                                }
                            }
                        });
                    },
                    handleUserBinding:function(scope){
                        var self = this;
                        var row = scope.row;
                        self.$refs.userDialog.setBuffer(row);
                        self.$refs.userDialog.open('/user/list/except/organization');
                    },
                    onUserDialogClose:function(users, startLoading, endLoading, close){
                        var self = this;
                        var orgnization = self.$refs.userDialog.getBuffer();
                        var userIds = [];
                        if(users && users.length>0){
                            for(var i=0; i<users.length; i++){
                                userIds.push(users[i].uuid);
                            }
                        }
                        startLoading();
                        ajax.post('/organization/binding', {
                            id:orgnization.id,
                            users: $.toJSON(userIds)
                        }, function(data, status){
                            endLoading();
                            if(status !== 200) return;
                            var rows = self.table.rows;
                            for(var i=0; i<rows.length; i++){
                                if(rows[i].id === orgnization.id){
                                    if(rows[i].users && rows[i].users.length>0){
                                        rows[i].users.splice(0, rows[i].users.length);
                                    } else{
                                        rows[i].users = [];
                                    }
                                    for(var j=0; j<data.length; j++){
                                        rows[i].users.push(data[j]);
                                    }
                                    break;
                                }
                            }
                            close();
                        }, null, ajax.NO_ERROR_CATCH_CODE);
                    },
                    rowKey:function(row){
                        return 'org-'+row.uuid
                    }
                }
            });

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