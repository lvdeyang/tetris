/**
 * Created by lvdeyang on 2020/6/30.
 */
define([
    'text!' + window.APPPATH + 'tetris/model/agenda/page-agenda.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'tetris/model/agenda/page-agenda.css'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-agenda';

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
                audioOperationTypes:[],
                businessInfoTypes:[],
                modeTypes:[],
                table:{
                    rows:[],
                    pageSize:50,
                    pageSizes:[50, 100, 200, 400],
                    currentPage:0,
                    total:0
                },
                dialog: {
                    createAgenda:{
                        visible:false,
                        loading:false,
                        name:'',
                        remark:'',
                        volume:0,
                        audioOperationType:'',
                        businessInfoTypeName:'',
                        agendaModeTypeName:''
                    },
                    editAgenda:{
                        visible:false,
                        loading:false,
                        id:'',
                        name:'',
                        remark:'',
                        volume:0,
                        audioOperationType:'',
                        businessInfoTypeName:'',
                        agendaModeTypeName:''
                    }
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                rowKey:function(row){
                    return 'agenda-' + row.uuid;
                },
                handleCreate:function(){
                    var self = this;
                    self.dialog.createAgenda.visible = true;
                },
                handleCreateAgendaClose:function(){
                    var self = this;
                    self.dialog.createAgenda.name = '';
                    self.dialog.createAgenda.remark = '';
                    self.dialog.createAgenda.volume = 0;
                    self.dialog.createAgenda.audioOperationType = '';
                    self.dialog.createAgenda.businessInfoTypeName = '';
                    self.dialog.createAgenda.agendaModeTypeName = '';
                    self.dialog.createAgenda.loading = false;
                    self.dialog.createAgenda.visible = false;
                },
                handleCreateAgendaSubmit:function(){
                    var self = this;
                    self.dialog.createAgenda.loading = true;
                    ajax.post('/tetris/bvc/model/agenda/add', {
                        name:self.dialog.createAgenda.name,
                        remark:self.dialog.createAgenda.remark,
                        volume:self.dialog.createAgenda.volume,
                        audioOperationType:self.dialog.createAgenda.audioOperationType,
                        businessInfoTypeName:self.dialog.createAgenda.businessInfoTypeName,
                        agendaModeTypeName:self.dialog.createAgenda.agendaModeTypeName
                    }, function(data, status){
                        self.dialog.createAgenda.loading = false;
                        if(status !== 200) return;
                        self.table.rows.push(data);
                        self.table.total += 1;
                        self.handleCreateAgendaClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleDelete:function(){

                },
                handleRowEdit:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.editAgenda.id = row.id;
                    self.dialog.editAgenda.name = row.name;
                    self.dialog.editAgenda.remark = row.remark;
                    self.dialog.editAgenda.volume = row.volume;
                    self.dialog.editAgenda.audioOperationType = row.audioOperationType;
                    self.dialog.editAgenda.businessInfoTypeName = row.businessInfoTypeName;
                    self.dialog.editAgenda.agendaModeTypeName = row.agendaModeTypeName;
                    self.dialog.editAgenda.visible = true;
                },
                handleEditAgendaClose:function(){
                    var self = this;
                    self.dialog.editAgenda.id = '';
                    self.dialog.editAgenda.name = '';
                    self.dialog.editAgenda.remark = '';
                    self.dialog.editAgenda.volume = 0;
                    self.dialog.editAgenda.audioOperationType = '';
                    self.dialog.editAgenda.businessInfoTypeName = '';
                    self.dialog.editAgenda.agendaModeTypeName = '';
                    self.dialog.editAgenda.loading = false;
                    self.dialog.editAgenda.visible = false;
                },
                handleEditAgendaSubmit:function(){
                    var self = this;
                    self.dialog.editAgenda.loading = true;
                    ajax.post('/tetris/bvc/model/agenda/edit', {
                        id:self.dialog.editAgenda.id,
                        name:self.dialog.editAgenda.name,
                        remark:self.dialog.editAgenda.remark,
                        volume:self.dialog.editAgenda.volume,
                        audioOperationType:self.dialog.editAgenda.audioOperationType,
                        businessInfoTypeName:self.dialog.editAgenda.businessInfoTypeName,
                        agendaModeTypeName:self.dialog.editAgenda.agendaModeTypeName
                    }, function(data, status){
                        self.dialog.editAgenda.loading = false;
                        if(status!==200) return;
                        for(var i=0; i<self.table.rows.length; i++){
                            if(self.table.rows[i].id == data.id){
                                self.table.rows.splice(i, 1, data);
                            }
                        }
                        self.handleEditAgendaClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleEditRole:function(scope){
                    var row = scope.row;
                    window.location.hash = '#/page-role-agenda-permission/'+row.id + '/' + row.name;
                },
                handleEditLayout:function(scope){
                    var row = scope.row;
                    window.location.hash = '#/page-agenda-layout-template/'+row.id + '/' + row.name;
                },
                handleEditCombineVideo:function(scope){
                    var row = scope.row;
                    window.location.hash = '#/page-combine-video/'+row.id + '/' + row.name;
                },
                handleEditCombineAudio:function(scope){
                    var row = scope.row;
                    window.location.hash = '#/page-combine-audio/'+row.id + '/' + row.name;
                },
                handleEditForward:function(scope){
                    var row = scope.row;
                    window.location.hash = '#/page-agenda-forward/'+row.id + '/' + row.name;
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
                                h('p', null, ['此操作将永久删除该议程，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/agenda/delete', {
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
                    ajax.post('/tetris/bvc/model/agenda/load', {
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
                    ajax.post('/tetris/bvc/model/agenda/query/types', null, function(data){
                        for(var i in data){
                            self.audioOperationTypes.push({
                                id:i,
                                name:data[i]
                            });
                        }
                    });
                },
                queryBusinessTypes:function(){
                    var self = this;
                    ajax.post('/tetris/bvc/model/agenda/query/business/types', null, function(data){
                        for(var i=0; i<data.length; i++){
                            self.businessInfoTypes.push(data[i]);
                        }
                    });
                },
                queryModeTypes:function(){
                    var self = this;
                    ajax.post('/tetris/bvc/model/agenda/query/mode/types', null, function(data){
                       if(data && data.length>0){
                           for(var i=0; i<data.length; i++){
                               self.modeTypes.push(data[i]);
                           }
                       }
                    });
                }
            },
            created:function(){
                var self = this;
                self.load(1);
                self.queryTypes();
                self.queryBusinessTypes();
                self.queryModeTypes();
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