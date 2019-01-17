/**
 * Created by lvdeyang on 2018/12/17 0017.
 */
define([
    'text!' + window.APPPATH + 'backstage/service/rest/page-backstage-service-rest.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-backstage-service-rest';

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
                table:{
                    rows:[],
                    pageSize:50,
                    pageSizes:[50, 100, 200, 400],
                    currentPage:0,
                    total:0
                },
                dialog:{
                    createService:{
                        visible:false,
                        name:'',
                        host:'',
                        port:'80',
                        contextPath:'',
                        remarks:'',
                        loading:false
                    },
                    editService:{
                        visible:false,
                        id:'',
                        uuid:'',
                        name:'',
                        host:'',
                        port:'',
                        contextPath:'',
                        remarks:'',
                        loading:false
                    }
                }
            },
            methods:{
                rowKey:function(row){
                    return 'rest-' + row.uuid;
                },
                handleCreate:function(){
                    var self = this;
                    self.dialog.createService.visible = true;
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
                                h('p', null, ['此操作将永久删除服务及所有接口，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/rest/service/delete/' + row.id, null, function(data, status){
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
                load:function(currentPage){
                    var self = this;
                    var pageSize = self.table.pageSize;
                    self.table.rows.splice(0, self.table.rows.length);
                    ajax.post('/rest/service/list', {
                        pageSize:pageSize,
                        currentPage:currentPage
                    }, function(data){
                        var total = data.total;
                        var rows = data.rows;
                        if(rows && rows.length>0){
                            for(var i=0; i<rows.length; i++){
                                self.table.rows.push(rows[i]);
                            }
                            self.table.total = total;
                            self.table.currentPage = currentPage;
                        }
                    });
                },
                handleCreateServiceClose:function(){
                    var self = this;
                    self.dialog.createService.name = '';
                    self.dialog.createService.host = '';
                    self.dialog.createService.port = 80;
                    self.dialog.createService.contextPath = '';
                    self.dialog.createService.remarks = '';
                    self.dialog.createService.loading = false;
                    self.dialog.createService.visible = false;
                },
                handleCreateServiceSubmit:function(){
                    var self = this;
                    self.dialog.createService.loading = true;
                    ajax.post('/rest/service/add', {
                        name:self.dialog.createService.name,
                        host:self.dialog.createService.host,
                        port:self.dialog.createService.port,
                        contextPath:self.dialog.createService.contextPath,
                        remarks:self.dialog.createService.remarks
                    }, function(data, status){
                        self.dialog.createService.loading = false;
                        if(status !== 200) return;
                        self.table.rows.splice(0, 0, data);
                        self.handleCreateServiceClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                handleRowEdit:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.editService.id = row.id;
                    self.dialog.editService.uuid = row.uuid;
                    self.dialog.editService.name = row.name;
                    self.dialog.editService.host = row.host;
                    self.dialog.editService.port = row.port;
                    self.dialog.editService.contextPath = row.contextPath;
                    self.dialog.editService.remarks = row.remarks;
                    self.dialog.editService.visible = true;
                },
                handleEditServiceSubmit:function(){
                    var self = this;
                    self.dialog.editService.loading = true;
                    ajax.post('/rest/service/edit/' + self.dialog.editService.id, {
                        name:self.dialog.editService.name,
                        host:self.dialog.editService.host,
                        port:self.dialog.editService.port,
                        contextPath:self.dialog.editService.contextPath,
                        remarks:self.dialog.editService.remarks
                    }, function(data, status){
                        self.dialog.editService.loading = false;
                        if(status !== 200) return;
                        for(var i=0; i<self.table.rows.length; i++){
                            var row = self.table.rows[i];
                            if(row.id === data.id){
                                row.name = data.name;
                                row.host = data.host;
                                row.port = data.port;
                                row.contextPath = data.contextPath;
                                row.remarks = data.remarks;
                                break;
                            }
                        }
                        self.handleEditServiceClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE)
                },
                handleEditServiceClose:function(){
                    var self = this;
                    self.dialog.editService.id = '';
                    self.dialog.editService.uuid = '';
                    self.dialog.editService.name = '';
                    self.dialog.editService.host = '';
                    self.dialog.editService.port = '';
                    self.dialog.editService.contextPath ='';
                    self.dialog.editService.remarks = '';
                    self.dialog.editService.loading = false;
                    self.dialog.editService.visible = false;
                },
                handleSubRows:function(scope){
                    var row = scope.row;
                    window.location.hash = '#/page-backstage-access-point/' + row.id + '/' + row.name + '/REST';
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