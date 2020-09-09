/**
 * Created by lvdeyang on 2020/6/5.
 */
define([
    'text!' + window.APPPATH + 'tetris/model/terminal/page-terminal.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'tetris/model/terminal/page-terminal.css'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-terminal';

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
                terminalTypes:[],
                table:{
                    rows:[],
                    pageSize:50,
                    pageSizes:[50, 100, 200, 400],
                    currentPage:0,
                    total:0
                },
                dialog: {
                    createTerminal:{
                        visible:false,
                        loading:false,
                        name:'',
                        typeName:''
                    },
                    editTerminal:{
                        visible:false,
                        loading:false,
                        id:'',
                        name:'',
                        typeName:''
                    }
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                rowKey:function(row){
                    return 'terminal-' + row.uuid;
                },
                handleCreate:function(){
                    var self = this;
                    self.dialog.createTerminal.visible = true;
                },
                handleCreateTerminalClose:function(){
                    var self = this;
                    self.dialog.createTerminal.name = '';
                    self.dialog.createTerminal.loading = false;
                    self.dialog.createTerminal.visible = false;
                },
                handleCreateTerminalSubmit:function(){
                    var self = this;
                    self.dialog.createTerminal.loading = true;
                    ajax.post('/tetris/bvc/model/terminal/create', {
                        name:self.dialog.createTerminal.name,
                        typeName:self.dialog.createTerminal.typeName
                    }, function(data, status){
                        self.dialog.createTerminal.loading = false;
                        if(status !== 200) return;
                        self.table.rows.push(data);
                        self.table.total += 1;
                        self.handleCreateTerminalClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleDelete:function(){

                },
                handleRowEdit:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.editTerminal.id = row.id;
                    self.dialog.editTerminal.name = row.name;
                    self.dialog.editTerminal.typeName = row.typeName;
                    self.dialog.editTerminal.visible = true;
                },
                handleEditTerminalClose:function(){
                    var self = this;
                    self.dialog.editTerminal.id = '';
                    self.dialog.editTerminal.name = '';
                    self.dialog.editTerminal.loading = false;
                    self.dialog.editTerminal.visible = false;
                },
                handleEditTerminalSubmit:function(){
                    var self = this;
                    self.dialog.editTerminal.loading = true;
                    ajax.post('/tetris/bvc/model/terminal/edit', {
                        id:self.dialog.editTerminal.id,
                        name:self.dialog.editTerminal.name,
                        typeName:self.dialog.editTerminal.typeName
                    }, function(data, status){
                        self.dialog.editTerminal.loading = false;
                        if(status!==200) return;
                        for(var i=0; i<self.table.rows.length; i++){
                            if(self.table.rows[i].id == data.id){
                                self.table.rows.splice(i, 1, data);
                            }
                        }
                        self.handleEditTerminalClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleEditBundle:function(scope){
                    var row = scope.row;
                    window.location.hash = '#/page-terminal-bundle/'+row.id + '/' + row.name;
                },
                handleEditChannel:function(scope){
                    var row = scope.row;
                    window.location.hash = '#/page-terminal-channel/'+row.id + '/' + row.name;
                },
                handleEditScreen:function(scope){
                    var row = scope.row;
                    window.location.hash = '#/page-terminal-screen/'+row.id + '/' + row.name;
                },
                handleEditLayout:function(scope){
                    var row = scope.row;
                    window.location.hash = '#/page-terminal-layout/'+row.id + '/' + row.name;
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
                                h('p', null, ['此操作将永久删除该终端，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/terminal/delete', {
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
                loadTypes:function(){
                    var self = this;
                    self.terminalTypes.splice(0, self.terminalTypes.length);
                    ajax.post('/tetris/bvc/model/terminal/query/types', null, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.terminalTypes.push(data[i]);
                            }
                        }
                    });
                },
                load:function(currentPage){
                    var self = this;
                    self.table.rows.splice(0, self.table.rows.length);
                    ajax.post('/tetris/bvc/model/terminal/load', {
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
                self.loadTypes();
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