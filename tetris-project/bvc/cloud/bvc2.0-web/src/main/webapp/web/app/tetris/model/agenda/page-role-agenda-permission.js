/**
 * Created by lvdeyang on 2020/7/1.
 */
define([
    'text!' + window.APPPATH + 'tetris/model/agenda/page-role-agenda-permission.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'tetris/model/agenda/page-role-agenda-permission.css'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-role-agenda-permission';

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
                agendaId:p.agendaId,
                agendaName:p.agendaName,
                groups: context.getProp('groups'),
                roles:[],
                table:{
                    rows:[]
                },
                dialog: {
                    addRole:{
                        visible:false,
                        loading:false,
                        roles:[],
                        props:{
                            label:'name',
                            children:'children',
                            isLeaf:'isLeaf'
                        }
                    }
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                rowKey:function(row){
                    return 'screen-' + row.uuid;
                },
                handleCreate:function(){
                    var self = this;
                    self.dialog.addRole.roles.splice(0, self.dialog.addRole.roles.length);
                    for(var i=0; i<self.roles.length; i++){
                        var role = self.roles[i];
                        var exist = false;
                        for(var j=0; j<self.table.rows.length; j++){
                            var row = self.table.rows[j];
                            if(row.roleId == role.id){
                                exist = true;
                                break;
                            }
                        }
                        if(!exist){
                            self.dialog.addRole.roles.push(role);
                        }
                    }
                    self.dialog.addRole.visible = true;
                },
                handleAddRoleClose:function(){
                    var self = this;
                    self.dialog.addRole.loading = false;
                    self.dialog.addRole.visible = false;
                    self.dialog.addRole.roles.splice(0, self.dialog.addRole.roles.length);
                },
                handleAddRoleSubmit:function(){
                    var self = this;
                    var roles = self.$refs.addRoleTree.getCheckedNodes();
                    if(!roles || roles.length<=0){
                        self.$message({
                            type:'error',
                            message:'您没有选择角色'
                        });
                        return;
                    }
                    var roleIds = [];
                    for(var i=0; i<roles.length; i++){
                        roleIds.push(roles[i].id);
                    }
                    self.dialog.addRole.loading = true;
                    ajax.post('/tetris/bvc/model/role/agenda/permission/add', {
                        agendaId:self.agendaId,
                        roleIds: $.toJSON(roleIds)
                    }, function(data, status){
                        self.dialog.addRole.loading = false;
                        if(status !== 200) return;
                        for(var i=0; i<data.length; i++){
                            self.table.rows.push(data[i]);
                        }
                        self.handleAddRoleClose();
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
                                h('p', null, ['是否移除该角色?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/role/agenda/permission/delete', {
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
                    ajax.post('/tetris/bvc/model/role/agenda/permission/load', {
                        agendaId:self.agendaId
                    }, function(data, status){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.table.rows.push(data[i]);
                            }
                        }
                    });
                },
                queryRoles:function(){
                    var self = this;
                    ajax.post('/tetris/bvc/model/role/load/all/internal', null, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.roles.push(data[i]);
                            }
                        }
                    });
                }
            },
            created:function(){
                var self = this;
                self.load();
                self.queryRoles();
            }
        });

    };

    var destroy = function(){

    };

    var groupList = {
        path:'/' + pageId + '/:agendaId/:agendaName',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});