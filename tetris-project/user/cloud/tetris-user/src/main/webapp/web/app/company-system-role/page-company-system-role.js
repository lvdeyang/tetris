/**
 * Created by lvdeyang on 2018/12/24 0024.
 */
define([
    'text!' + window.APPPATH + 'company-system-role/page-company-system-role.html',
    window.APPPATH + 'company-system-role/page-company-system-role.i18n',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'company-system-role/page-company-system-role.css'
], function(tpl, i18n, config, ajax, context, commons, Vue){

    var locale = context.getProp('locale');
    var i18n = !locale?i18n.default:i18n[locale]?i18n[locale]:i18n.default;

    var pageId = 'page-company-system-role';

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
                i18n:i18n,
                tree:{
                    props:{
                        label: 'name',
                        children: 'sub',
                        isLeaf: 'isLeaf'
                    },
                    expandOnClickNode:false,
                    data:[],
                    current:''
                },
                loading:{
                    roleGroup:false,
                    addRoot:false
                },
                table:{
                    rows:[],
                    pageSize:50,
                    pageSizes:[50, 100, 200, 400],
                    currentPage:0,
                    total:0
                },
                dialog:{
                    createSystemRoleGroup:{
                        visible:false,
                        name:'',
                        companyShow:false,
                        companys:[],
                        companyId:'',
                        loading:false
                    },
                    editSystemRoleGroup:{
                        visible:false,
                        id:'',
                        name:'',
                        loading:false
                    },
                    createSystemRole:{
                        visible:false,
                        name:'',
                        loading:false
                    },
                    editSystemRole:{
                        visible:false,
                        id:'',
                        name:'',
                        loading:false
                    },
                    bindMenu:{
                        visible:false,
                        menus:[],
                        props:{
                            label: 'title',
                            children: 'sub',
                            isLeaf: 'isLeaf'
                        },
                        authorized:[],
                        homePage:'',
                        role:'',
                        loading:false
                    }
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                loadGroups:function(){
                    var self = this;
                    self.loading.roleGroup = true;
                    ajax.post('/system/role/group/company/list', null, function(data, status){
                        self.loading.roleGroup = false;
                        if(status !== 200) return;
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                data[i].isLeaf = true;
                                self.tree.data.push(data[i]);
                            }
                            self.currentNode(data[0]);
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                currentTreeNodeChange:function(data){
                    var self = this;
                    self.tree.current = data;
                    self.loadRoles(data.id, 1);
                },
                handleCreateSystemRoleGroup:function(){
                    var self = this;
                    self.dialog.createSystemRoleGroup.visible = true;
                },
                handleCreateSystemRoleGroupClose:function(){
                    var self = this;
                    self.dialog.createSystemRoleGroup.visible = false;
                    self.dialog.createSystemRoleGroup.name = '';
                    self.dialog.createSystemRoleGroup.loading = false;
                    self.handleCompanyClose();
                },
                handleCompanyShowClick:function(){
                    var self = this;
                    if(self.dialog.createSystemRoleGroup.companyShow){
                        self.handleCompanyClose();
                        return;
                    }
                    ajax.post('/company/list/all', null, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.dialog.createSystemRoleGroup.companys.push(data[i]);
                            }
                        }
                        self.dialog.createSystemRoleGroup.companyShow = true;
                    })
                },
                handleCompanyClose:function(){
                    var self = this;
                    self.dialog.createSystemRoleGroup.companys.splice(0, self.dialog.createSystemRoleGroup.companys.length);
                    self.dialog.createSystemRoleGroup.companyId = '';
                    self.dialog.createSystemRoleGroup.companyShow = false;
                },
                handleCompanyChange:function(currentRow){
                    var self = this;
                    self.dialog.createSystemRoleGroup.companyId = currentRow.id;
                },
                handleCreateSystemRoleGroupSubmit:function(){
                    var self = this;
                    self.dialog.createSystemRoleGroup.loading = true;
                    var params = {
                        name:self.dialog.createSystemRoleGroup.name
                    };
                    ajax.post('/system/role/group/company/add', params, function(data, status){
                        self.dialog.createSystemRoleGroup.loading = false;
                        if(status !== 200) return;
                        self.tree.data.splice(0, 0, data);
                        self.handleCreateSystemRoleGroupClose();
                        self.currentNode(data);
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                handleEditSystemRoleGroup:function(node, data){
                    var self = this;
                    self.dialog.editSystemRoleGroup.id = data.id;
                    self.dialog.editSystemRoleGroup.name = data.name;
                    self.dialog.editSystemRoleGroup.visible = true;
                },
                handleEditSystemRoleGroupClose:function(){
                    var self = this;
                    self.dialog.editSystemRoleGroup.id = '';
                    self.dialog.editSystemRoleGroup.name = '';
                    self.dialog.editSystemRoleGroup.visible = false;
                    self.dialog.editSystemRoleGroup.loading = false;
                },
                handleEditSystemRoleGroupSubmit:function(){
                    var self = this;
                    self.dialog.editSystemRoleGroup.loading = true;
                    ajax.post('/system/role/group/edit/' + self.dialog.editSystemRoleGroup.id, {
                        name:self.dialog.editSystemRoleGroup.name
                    }, function(data, status){
                        self.dialog.editSystemRoleGroup.loading = false;
                        if(status !== 200) return;
                        for(var i=0; i<self.tree.data.length; i++){
                            if(self.tree.data[i].id === data.id){
                                self.tree.data[i].name = data.name;
                                break;
                            }
                        }
                        self.handleEditSystemRoleGroupClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE)
                },
                handleDeleteSystemRoleGroup:function(node, data){
                    var self = this;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'危险操作',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['此操作将永久删除角色组以及组内所有角色，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/system/role/group/delete/' + data.id, null, function(response, status){
                                    instance.confirmButtonLoading = false;
                                    if(status !== 200) return;
                                    for(var i=0; i<self.tree.data.length; i++){
                                        if(self.tree.data[i].id === data.id){
                                            self.tree.data.splice(i, 1);
                                            break;
                                        }
                                    }
                                    done();
                                    self.currentNode(self.tree.data[0]);
                                }, null, ajax.NO_ERROR_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                currentNode:function(data){
                    if(!data) return;
                    var self = this;
                    self.$nextTick(function(){
                        self.$refs.roleGroupTree.setCurrentKey(data.uuid);
                    });
                    self.tree.current = data;
                    self.loadRoles(data.id, 1);
                },
                rowKey:function(row){
                    return 'business-role-' + row.id;
                },
                loadRoles:function(groupId, currentPage){
                    var self = this;
                    self.table.rows.splice(0, self.table.rows.length);
                    ajax.post('/system/role/list', {
                        groupId:groupId,
                        currentPage:currentPage,
                        pageSize:self.table.pageSize
                    }, function(data){
                        var total = data.total;
                        var rows = data.rows;
                        self.table.total = total;
                        if(rows && rows.length>0){
                            for(var i=0; i<rows.length; i++){
                                self.table.rows.push(rows[i]);
                            }
                        }
                    });
                },
                handleCurrentChange:function(currentPage){
                    var self = this;
                    self.loadRoles(self.tree.current.id, currentPage)
                },
                handleCreateSystemRole:function(){
                    var self = this;
                    self.dialog.createSystemRole.visible = true;
                },
                handleDeleteSystemRole:function(){

                },
                handleCreateSystemRoleClose:function(){
                    var self = this;
                    self.dialog.createSystemRole.visible = false;
                    self.dialog.createSystemRole.name = '';
                    self.dialog.createSystemRole.loading = false;
                },
                handleCreateSystemRoleSubmit:function(){
                    var self = this;
                    var params = {
                        groupId:self.tree.current.id,
                        name:self.dialog.createSystemRole.name,
                        createType:self.tree.current.companyId?'COMPANY_ADMIN':'SYSTEM_ADMIN'
                    };
                    if(self.tree.current.companyId){
                        params.companyId = self.tree.current.companyId
                    }
                    self.dialog.createSystemRole.loading = true;
                    ajax.post('/system/role/add', params, function(data, status){
                        self.dialog.createSystemRole.loading = false;
                        if(status !== 200) return;
                        self.table.rows.push(data);
                        self.handleCreateSystemRoleClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE)
                },
                handleRowEdit:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.editSystemRole.visible = true;
                    self.dialog.editSystemRole.id = row.id;
                    self.dialog.editSystemRole.name = row.name;
                },
                handleEditSystemRoleSubmit:function(){
                    var self = this;
                    self.dialog.editSystemRole.loading = true;
                    ajax.post('/system/role/edit/' + self.dialog.editSystemRole.id, {
                        name:self.dialog.editSystemRole.name
                    }, function(data, status){
                        self.dialog.editSystemRole.loading = false;
                        if(status !== 200) return;
                        for(var i=0; i<self.table.rows.length; i++){
                            if(self.table.rows[i].id === data.id){
                                self.table.rows[i].name = data.name;
                                break;
                            }
                        }
                        self.handleEditSystemRoleClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                handleEditSystemRoleClose:function(){
                    var self = this;
                    self.dialog.editSystemRole.visible = false;
                    self.dialog.editSystemRole.id = '';
                    self.dialog.editSystemRole.name = '';
                    self.dialog.editSystemRole.loading = false;
                },
                handleBindMenu:function(scope){
                    var self = this;
                    var row = scope.row;
                    var params = {roleId:row.id};

                    params.companyId = self.tree.current.companyId;
                    params.handleCompanyQuery = true;
                    /*if(self.tree.current.companyId){
                        params.companyId = self.tree.current.companyId;
                        params.handleCompanyQuery = true;
                    }else{
                        params.handleCompanyQuery = false;
                    }*/
                    ajax.post('/menu/feign/query/menus/by/role/id', params, function(data){
                        var menus = data.menus;
                        var authorized = data.authorized;
                        if(menus && menus.length>0){
                            for(var i=0; i<menus.length; i++){
                                self.dialog.bindMenu.menus.push(menus[i]);
                            }
                        }
                        if(authorized && authorized.length>0){
                            for(var i=0; i<authorized.length; i++){
                                self.dialog.bindMenu.authorized.push(authorized[i]);
                            }
                        }
                        self.dialog.bindMenu.role = row;
                        if(data.homePage){
                            self.dialog.bindMenu.homePage = data.homePage;
                        }else{
                            self.dialog.bindMenu.homePage = '';
                        }
                        self.dialog.bindMenu.visible = true;
                    });
                },
                handleSetHomePage:function(scope){
                    var self = this;
                    var data = scope.data;

                    var h = self.$createElement;
                    self.$msgbox({
                        title:'危险操作',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['是否切换当前首页?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/system/role/menu/permission/feign/set/home/page', {
                                    roleId:self.dialog.bindMenu.role.id,
                                    menuId:data.id
                                }, function(response, status){
                                    instance.confirmButtonLoading = false;
                                    if(status !== 200) return;
                                    self.dialog.bindMenu.homePage = data.id;
                                    done();
                                }, null, ajax.NO_ERROR_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                handleBindMenuClose:function(){
                    var self = this;
                    self.dialog.bindMenu.visible = false;
                    self.dialog.bindMenu.menus.splice(0, self.dialog.bindMenu.menus.length);
                    self.dialog.bindMenu.authorized.splice(0, self.dialog.bindMenu.authorized.length);
                    self.dialog.bindMenu.role = '';
                },
                handleMenuCheckChange:function(data, checked){
                    var self = this;
                    if(data.isGroup) return;
                   /* if(data.autoGeneration){
                        self.$refs.menuTree.setChecked(data, !checked);
                        return false;
                    }*/
                    if(checked){
                        ajax.post('/system/role/menu/permission/feign/add', {
                            roleId:self.dialog.bindMenu.role.id,
                            menuId:data.id
                        }, function(){
                            self.$message({
                                type:'success',
                                message:'操作成功'
                            });
                        });
                    }else{
                        console.log(data);
                        ajax.post('/system/role/menu/permission/feign/remove', {
                            roleId:self.dialog.bindMenu.role.id,
                            menuId:data.id
                        }, function(){
                            if(data.id === self.dialog.bindMenu.homePage) self.dialog.bindMenu.homePage = '';
                            self.$message({
                                type:'success',
                                message:'操作成功'
                            });
                        });
                    }
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
                                h('p', null, ['此操作将永久删除角色以及角色授权，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/system/role/delete/' + row.id, null, function(response, status){
                                    instance.confirmButtonLoading = false;
                                    if(status !== 200) return;
                                    for(var i=0; i<self.table.rows.length; i++){
                                        if(self.table.rows[i].id === row.id){
                                            self.table.rows.splice(i, 1);
                                            break;
                                        }
                                    }
                                    done();
                                }, null, ajax.NO_ERROR_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                }
            },
            created:function(){
                var self = this;
                self.loadGroups();
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