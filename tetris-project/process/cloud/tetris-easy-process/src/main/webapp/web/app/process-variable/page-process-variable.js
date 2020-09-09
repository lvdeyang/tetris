/**
 * Created by lvdeyang on 2018/12/24 0024.
 */
define([
    'text!' + window.APPPATH + 'process-variable/page-process-variable.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'process-variable-dialog',
    'css!' + window.APPPATH + 'process-variable/page-process-variable.css'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-process-variable';

    var init = function(p){

        var processId = p.processId;
        var processName = p.processName;

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el: '#' + pageId + '-wrapper',
            data: {
                activeId:'page-process',
                menus: context.getProp('menus'),
                user: context.getProp('user'),
                groups: context.getProp('groups'),
                processName:processName,
                dataType:[],
                table:{
                    rows:[],
                    pageSize:50,
                    pageSizes:[50, 100, 200, 400],
                    currentPage:0,
                    total:0
                },
                dialog:{
                    editProcessVariable:{
                        visible:false,
                        id:'',
                        primaryKey:'',
                        name:'',
                        defaultValue:'',
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
                    return 'process-variable-' + row.uuid;
                },
                handleCreate:function(){
                    var self = this;
                    self.$refs.processVariable.open(processId);
                },
                handleDelete:function(){

                },
                handleRowEdit:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.editProcessVariable.id = row.id;
                    self.dialog.editProcessVariable.primaryKey = row.primaryKey;
                    self.dialog.editProcessVariable.name = row.name;
                    self.dialog.editProcessVariable.defaultValue = row.defaultValue;
                    self.dialog.editProcessVariable.visible = true;
                },
                handleEditProcessVariableClose:function(){
                    var self = this;
                    self.dialog.editProcessVariable.id = '';
                    self.dialog.editProcessVariable.primaryKey = '';
                    self.dialog.editProcessVariable.name = '';
                    self.dialog.editProcessVariable.defaultValue = '';
                    self.dialog.editProcessVariable.visible = false;
                },
                handleEditProcessVariableSubmit:function(){
                    var self = this;
                    self.dialog.editProcessVariable.loading = true;
                    ajax.post('/process/variable/edit/' + self.dialog.editProcessVariable.id, {
                        primaryKey:self.dialog.editProcessVariable.primaryKey,
                        name:self.dialog.editProcessVariable.name,
                        defaultValue:self.dialog.editProcessVariable.defaultValue
                    }, function(data, status){
                        self.dialog.editProcessVariable.loading = false;
                        if(status !== 200) return;
                        for(var i=0; i<self.table.rows.length; i++){
                            if(self.table.rows[i].id === self.dialog.editProcessVariable.id){
                                self.table.rows.splice(i, 1, data);
                                break;
                            }
                        }
                        self.handleEditProcessVariableClose();
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
                                h('p', null, ['此操作将永久删除该流程变量，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/process/variable/delete/' + row.id, null, function(data, status){
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
                    ajax.post('/process/variable/list', {
                        processId:processId,
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
                },
                onProcessVariableAdded:function(variable){
                    var self = this;
                    self.table.rows.push(variable);
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
        path:'/' + pageId + '/:processId/:processName',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});