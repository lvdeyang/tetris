/**
 * Created by lvdeyang on 2020/7/9.
 */
define([
    'text!' + window.APPPATH + 'tetris/business/terminal/hall/page-hall-role-permission.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'tetris/business/terminal/hall/page-hall-role-permission.css'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-hall-role-permission';

    var init = function(){

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el: '#' + pageId + '-wrapper',
            data: {
                menus: context.getProp('menus'),
                userInfo: context.getProp('user'),
                groups: context.getProp('groups'),
                role:{
                    data:[],
                    props:{
                        label:'name'
                    },
                    page:{
                        currentPage:0,
                        pageSize:50,
                        total:0
                    },
                    current:''
                },
                permission:{
                    data:[],
                    filter:{
                        name:''
                    },
                    page:{
                        currentPage:0,
                        pageSize:50,
                        total:0
                    }
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                loadRoles:function(currentPage){
                    var self = this;
                    self.role.data.splice(0, self.role.data.length);
                    ajax.post('/business/role/list', {
                        currentPage:currentPage,
                        pageSize:self.role.page.pageSize
                    }, function(data){
                        var total = data.total;
                        var rows = data.rows;
                        if(rows && rows.length>0){
                            for(var i=0; i<rows.length; i++){
                                self.role.data.push(rows[i]);
                            }
                            self.role.page.total = total;
                            self.role.page.currentPage = currentPage;
                        }
                    });
                },
                handleRoleCurrentChange:function(currentPage){
                    var self = this;
                    self.loadRoles(currentPage);
                },
                handleRoleCurrentNodeChange:function(currentNode){
                    var self = this;
                    self.role.current = currentNode;
                    self.loadPermissions(1);
                },
                loadPermissions:function(currentPage){
                    var self = this;
                    self.permission.data.splice(0, self.permission.data.length);
                    ajax.post('/conference/hall/role/permission/load', {
                        name:self.permission.filter.name,
                        roleId:self.role.current.id,
                        currentPage:currentPage,
                        pageSize:self.permission.page.pageSize
                    }, function(data){
                        var total = data.total;
                        var rows = data.rows;
                        if(rows && rows.length>0){
                            for(var i=0; i<rows.length; i++){
                                var row = rows[i];
                                row.RECORD = row.permissions&&row.permissions['RECORD']?true:false;
                                row.VOD = row.permissions&&row.permissions['VOD']?true:false;
                                row.CALL = row.permissions&&row.permissions['CALL']?true:false;
                                row.COMMAND = row.permissions&&row.permissions['COMMAND']?true:false;
                                self.permission.data.push(row);
                            }
                            self.permission.page.total = total;
                            self.permission.page.currentPage = currentPage;
                        }
                    });
                },
                handlePermissionCurrentPage:function(currentPage){
                    var self = this;
                    self.loadPermissions(currentPage);
                },
                permissionChange:function(scope, privilege){
                    var self = this;
                    var row = scope.row;
                    if(row[privilege]){
                        //添加权限
                        ajax.post('/conference/hall/role/permission/add', {
                            roleId:self.role.current.id,
                            conferenceHallId:scope.row.id,
                            privilege:privilege
                        }, function(data){
                            if(!scope.row.permissions) scope.row.permissions = {};
                            scope.row.permissions[data.key] = data.value;
                        });
                    }else{
                        //删除权限
                        ajax.post('/conference/hall/role/permission/remove', {
                            roleId:self.role.current.id,
                            conferenceHallId:scope.row.id,
                            privilege:privilege
                        }, function(data){
                            if(!scope.row.permissions) scope.row.permissions = {};
                            scope.row.permissions[data.key] = null;
                        });
                    }
                }
            },
            created:function(){
                var self = this;
                self.loadRoles(1);
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