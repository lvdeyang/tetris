/**
 * Created by lvdeyang on 2018/12/24 0024.
 */
define([
    'text!' + window.APPPATH + 'company/page-company.html',
    window.APPPATH + 'company/page-company.i18n',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'mi-user-dialog',
    'css!' + window.APPPATH + 'company/page-company.css'
], function(tpl, i18n, config, $, ajax, context, commons, Vue){

    var locale = context.getProp('locale');
    var i18n = !locale?i18n.default:i18n[locale]?i18n[locale]:i18n.default;

    var pageId = 'page-company';

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
                table:{
                    rows:[],
                    pageSize:50,
                    pageSizes:[50, 100, 200, 400],
                    currentPage:0,
                    total:0
                },
                dialog:{
                    editSystemRole:{
                        visible:false,
                        loading:false,
                        company:'',
                        rows:[],
                        current:''
                    }
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                rowKey:function(row){
                    return 'company-' + row.uuid;
                },
                handleDelete:function(){

                },
                handleSizeChange:function(size){
                    var self = this;
                    self.table.pageSize = size;
                    self.load(self.table.currentPage);
                },
                handleCurrentChange:function(currentPage){
                    var self = this;
                    self.load(currentPage);
                },
                handleEditSystemRole:function(scope){
                    var self = this;
                    var row = scope.row;
                    ajax.post('/system/role/query/by/create/type', {
                        createType:'SYSTEM_ADMIN'
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.dialog.editSystemRole.rows.push(data[i]);
                            }
                        }
                        self.dialog.editSystemRole.company = row;
                        self.dialog.editSystemRole.visible = true;
                        self.$nextTick(function(){
                            for(var i=0; i<data.length; i++){
                                if(data[i].id == row.systemRoleId){
                                    self.$refs.systemRoleTable.setCurrentRow(data[i]);
                                }
                            }
                        });
                    });
                },
                handleSelectSystemRoleClose:function(){
                    var self = this;
                    self.dialog.editSystemRole.visible = false;
                    self.dialog.editSystemRole.loading = false;
                    self.dialog.editSystemRole.company = '';
                    self.dialog.editSystemRole.rows.splice(0, self.dialog.editSystemRole.rows.length);
                },
                handleSelectSystemRoleSubmit:function(){
                    var self = this;
                    ajax.post('/company/edit/system/role', {
                        companyId:self.dialog.editSystemRole.company.id,
                        systemRoleId:self.dialog.editSystemRole.current.id
                    }, function(){
                        self.dialog.editSystemRole.company.systemRoleId = self.dialog.editSystemRole.current.id;
                        self.dialog.editSystemRole.company.systemRoleName = self.dialog.editSystemRole.current.name;
                        self.handleSelectSystemRoleClose();
                    });
                },
                systemRoleKey:function(row){
                    return row.id;
                },
                systemRoleChange:function(current){
                    var self = this;
                    self.dialog.editSystemRole.current = current;
                },
                gotoOrganization:function(scope){
                    var self = this;
                    var row = scope.row;
                    window.location.hash = '#/page-organization/' + row.id + '/' + row.name
                },
                load:function(currentPage){
                    var self = this;
                    self.table.rows.splice(0, self.table.rows.length);
                    ajax.post('/company/list', {
                        currentPage:currentPage,
                        pageSize:self.table.pageSize
                    }, function(data){
                        var total = data.total;
                        var rows = data.rows;
                        if(rows && rows.length>0){
                            for(var i=0; i<rows.length; i++){
                                self.table.rows.push(rows[i]);
                            }
                            self.table.total = total;
                        }
                        self.table.currentPage = currentPage;
                    });
                }
            },
            created:function(){
                var self = this;
                self.load(1);
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