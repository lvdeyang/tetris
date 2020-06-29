/**
 * Created by lvdeyang on 2020/6/24.
 */
define([
    'text!' + window.APPPATH + 'tetris/model/terminal/layout/page-layout.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'tetris/model/terminal/layout/page-layout.css'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-layout';

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
                table:{
                    rows:[],
                    pageSize:50,
                    pageSizes:[50, 100, 200, 400],
                    currentPage:0,
                    total:0
                },
                dialog: {
                    createLayout:{
                        visible:false,
                        loading:false,
                        name:''
                    },
                    editLayout:{
                        visible:false,
                        loading:false,
                        id:'',
                        name:''
                    }
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                rowKey:function(row){
                    return 'layout-' + row.uuid;
                },
                handleCreate:function(){
                    var self = this;
                    self.dialog.createLayout.visible = true;
                },
                handleCreateLayoutClose:function(){
                    var self = this;
                    self.dialog.createLayout.name = '';
                    self.dialog.createLayout.loading = false;
                    self.dialog.createLayout.visible = false;
                },
                handleCreateLayoutSubmit:function(){
                    var self = this;
                    self.dialog.createLayout.loading = true;
                    ajax.post('/tetris/bvc/model/terminal/layout/add', {
                        name:self.dialog.createLayout.name
                    }, function(data, status){
                        self.dialog.createLayout.loading = false;
                        if(status !== 200) return;
                        self.table.rows.push(data);
                        self.table.total += 1;
                        self.handleCreateLayoutClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleDelete:function(){

                },
                handleRowEdit:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.editLayout.id = row.id;
                    self.dialog.editLayout.name = row.name;
                    self.dialog.editLayout.visible = true;
                },
                handleEditLayoutClose:function(){
                    var self = this;
                    self.dialog.editLayout.id = '';
                    self.dialog.editLayout.name = '';
                    self.dialog.editLayout.loading = false;
                    self.dialog.editLayout.visible = false;
                },
                handleEditLayoutSubmit:function(){
                    var self = this;
                    self.dialog.editLayout.loading = true;
                    ajax.post('/tetris/bvc/model/terminal/layout/edit', {
                        id:self.dialog.editLayout.id,
                        name:self.dialog.editLayout.name
                    }, function(data, status){
                        self.dialog.editLayout.loading = false;
                        if(status!==200) return;
                        for(var i=0; i<self.table.rows.length; i++){
                            if(self.table.rows[i].id == data.id){
                                self.table.rows.splice(i, 1, data);
                            }
                        }
                        self.handleEditLayoutClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleEditLayout:function(scope){
                    var row = scope.row;
                    window.location.hash = '#/page-layout-position/'+row.id + '/' + row.name;
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
                                h('p', null, ['此操作将永久删除该布局，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/terminal/layout/delete', {
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
                    ajax.post('/tetris/bvc/model/terminal/layout/load', {
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