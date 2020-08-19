/**
 * Created by lvdeyang on 2020/6/28.
 */
define([
    'text!' + window.APPPATH + 'tetris/model/role/page-role.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'tetris/model/role/page-role.css'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-role';

    var init = function(){

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
                roleTypes:[],
                mappingTypes:[],
                table:{
                    rows:[],
                    pageSize:50,
                    pageSizes:[50, 100, 200, 400],
                    currentPage:0,
                    total:0
                },
                dialog: {
                    createRole:{
                        visible:false,
                        loading:false,
                        name:'',
                        internalRoleType:'',
                        roleUserMappingType:''
                    },
                    editRole:{
                        visible:false,
                        loading:false,
                        id:'',
                        name:'',
                        internalRoleType:'',
                        roleUserMappingType:''
                    }
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                rowKey:function(row){
                    return 'role-' + row.uuid;
                },
                handleCreate:function(){
                    var self = this;
                    self.dialog.createRole.visible = true;
                },
                handleCreateRoleClose:function(){
                    var self = this;
                    self.dialog.createRole.name = '';
                    self.dialog.createRole.internalRoleType = '';
                    self.dialog.createRole.roleUserMappingType = '';
                    self.dialog.createRole.loading = false;
                    self.dialog.createRole.visible = false;
                },
                handleCreateRoleSubmit:function(){
                    var self = this;
                    self.dialog.createRole.loading = true;
                    ajax.post('/tetris/bvc/model/role/add/internal', {
                        name:self.dialog.createRole.name,
                        internalRoleType:self.dialog.createRole.internalRoleType,
                        roleUserMappingType:self.dialog.createRole.roleUserMappingType
                    }, function(data, status){
                        self.dialog.createRole.loading = false;
                        if(status !== 200) return;
                        self.table.rows.push(data);
                        self.table.total += 1;
                        self.handleCreateRoleClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleDelete:function(){

                },
                handleRowEdit:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.editRole.id = row.id;
                    self.dialog.editRole.name = row.name;
                    self.dialog.editRole.internalRoleType = row.internalRoleType;
                    self.dialog.editRole.roleUserMappingType = row.roleUserMappingType;
                    self.dialog.editRole.visible = true;
                },
                handleEditChannel:function(scope){
                    var row = scope.row;
                    window.location.hash = '#/page-role-channel/'+row.id + '/' + row.name;
                },
                handleEditRoleClose:function(){
                    var self = this;
                    self.dialog.editRole.id = '';
                    self.dialog.editRole.name = '';
                    self.dialog.editRole.internalRoleType = '';
                    self.dialog.editRole.roleUserMappingType = '';
                    self.dialog.editRole.loading = false;
                    self.dialog.editRole.visible = false;
                },
                handleEditRoleSubmit:function(){
                    var self = this;
                    self.dialog.editRole.loading = true;
                    ajax.post('/tetris/bvc/model/role/edit/internal', {
                        id:self.dialog.editRole.id,
                        name:self.dialog.editRole.name,
                        internalRoleType:self.dialog.editRole.internalRoleType,
                        roleUserMappingType:self.dialog.editRole.roleUserMappingType
                    }, function(data, status){
                        self.dialog.editRole.loading = false;
                        if(status!==200) return;
                        for(var i=0; i<self.table.rows.length; i++){
                            if(self.table.rows[i].id == data.id){
                                self.table.rows.splice(i, 1, data);
                            }
                        }
                        self.handleEditRoleClose();
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
                                h('p', null, ['此操作将永久删除该角色，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/role/delete', {
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
                                    self.table.total -= 1;
                                    if(self.table.rows.length==0 && self.table.total>0){
                                        self.load(self.table.currentPage-1);
                                    }
                                }, null, ajax.TOTAL_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                handleSizeChange:function(size){
                    var self = this;
                    self.table.pageSize = size;
                    self.load(1);
                },
                handleCurrentChange:function(currentPage){
                    var self = this;
                    self.load(currentPage);
                },
                load:function(currentPage){
                    var self = this;
                    self.table.rows.splice(0, self.table.rows.length);
                    ajax.post('/tetris/bvc/model/role/load/internal', {
                        currentPage:currentPage,
                        pageSize:self.table.pageSize
                    }, function(data, status){
                        var total = data.total;
                        var rows = data.rows;
                        if(rows && rows.length>0){
                            for(var i=0; i<rows.length; i++){
                                self.table.rows.push(rows[i]);
                            }
                        }
                        self.table.total = total;
                        self.table.currentPage = currentPage;
                    });
                },
                queryTypes:function(){
                    var self = this;
                    ajax.post('/tetris/bvc/model/role/query/types', null, function(data){
                        var roleTypes = data.roleTypes;
                        var mappingTypes = data.mappingTypes;
                        for(var i in roleTypes){
                            self.roleTypes.push({
                                id:i,
                                name:roleTypes[i]
                            });
                        }
                        for(var i in mappingTypes){
                            self.mappingTypes.push({
                                id:i,
                                name:mappingTypes[i]
                            });
                        }
                    });
                }
            },
            created:function(){
                var self = this;
                self.load(1);
                self.queryTypes();
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