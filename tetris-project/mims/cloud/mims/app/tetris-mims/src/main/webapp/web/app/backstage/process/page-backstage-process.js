/**
 * Created by lvdeyang on 2018/12/24 0024.
 */
define([
    'text!' + window.APPPATH + 'backstage/process/page-backstage-process.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'backstage/process/page-backstage-process.css'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-backstage-process';

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
                processTypes:[],
                table:{
                    rows:[],
                    pageSize:50,
                    pageSizes:[50, 100, 200, 400],
                    currentPage:0,
                    total:0
                },
                dialog:{
                    createProcess:{
                        visible:false,
                        type:'',
                        processId:'',
                        name:'',
                        remarks:'',
                        loading:false
                    },
                    editProcess:{
                        visible:false,
                        id:'',
                        type:'',
                        processId:'',
                        name:'',
                        remarks:'',
                        loading:false
                    }
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                rowKey:function(row){
                    return 'process-' + row.uuid;
                },
                gotoProcessDesign:function(scope){
                    var row = scope.row;
                    window.location.hash = '#/page-backstage-process-design/' + row.id + '/' + row.name;
                },
                gotoProcessVariable:function(scope){
                    var row = scope.row;
                    window.location.hash = '#/page-backstage-process-variable/' + row.id + '/' + row.name;
                },
                publishProcess:function(scope){
                    var self = this;
                    var row = scope.row;
                    ajax.post('/api/process/publish/' + row.id, null, function(){
                        self.$message({
                            type:'success',
                            message:'发布成功！'
                        });
                    });
                },
                handleCreate:function(){
                    var self = this;
                    self.dialog.createProcess.visible = true;
                },
                handleCreateProcessClose:function(){
                    var self = this;
                    self.dialog.createProcess.type = '';
                    self.dialog.createProcess.processId = '';
                    self.dialog.createProcess.name = '';
                    self.dialog.createProcess.remarks = '';
                    self.dialog.createProcess.visible = false;
                },
                handleCreateProcessSubmit:function(){
                    var self = this;
                    self.dialog.createProcess.loading = true;
                    ajax.post('/process/add', {
                        type:self.dialog.createProcess.type,
                        processId:self.dialog.createProcess.processId,
                        name:self.dialog.createProcess.name,
                        remarks:self.dialog.createProcess.remarks
                    }, function(data, status){
                        self.dialog.createProcess.loading = false;
                        if(status !== 200) return;
                        self.table.rows.push(data);
                        self.handleCreateProcessClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                handleDelete:function(){

                },
                handleRowEdit:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.editProcess.id = row.id;
                    self.dialog.editProcess.type = row.type;
                    self.dialog.editProcess.processId = row.processId;
                    self.dialog.editProcess.name = row.name;
                    self.dialog.editProcess.remarks = row.remarks;
                    self.dialog.editProcess.visible = true;
                },
                handleEditProcessClose:function(){
                    var self = this;
                    self.dialog.editProcess.id = '';
                    self.dialog.editProcess.type = '';
                    self.dialog.editProcess.processId = '';
                    self.dialog.editProcess.name = '';
                    self.dialog.editProcess.remarks = '';
                    self.dialog.editProcess.visible = false;
                },
                handleEditProcessSubmit:function(){
                    var self = this;
                    self.dialog.editProcess.loading = true;
                    ajax.post('/process/edit/' + self.dialog.editProcess.id, {
                        name:self.dialog.editProcess.name,
                        remarks:self.dialog.editProcess.remarks
                    }, function(data, status){
                        self.dialog.editProcess.loading = false;
                        if(status !== 200) return;
                        for(var i=0; i<self.table.rows.length; i++){
                            if(self.table.rows[i].id === self.dialog.editProcess.id){
                                self.table.rows.splice(i, 1, data);
                                break;
                            }
                        }
                        self.handleEditProcessClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
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
                                h('p', null, ['此操作将永久删除该流程，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/process/delete/' + row.id, null, function(data, status){
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
                    self.table.rows.splice(0, self.table.rows.length);
                    ajax.post('/process/list', {
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
                ajax.post('/process/query/types', null, function(data){
                    if(data && data.length>0){
                        for(var i=0; i<data.length; i++){
                            self.processTypes.push(data[i]);
                        }
                    }
                });
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