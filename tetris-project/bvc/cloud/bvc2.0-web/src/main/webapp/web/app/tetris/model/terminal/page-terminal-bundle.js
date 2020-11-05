/**
 * Created by lvdeyang on 2020/6/5.
 */
define([
    'text!' + window.APPPATH + 'tetris/model/terminal/page-terminal-bundle.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'tetris/model/terminal/page-terminal-bundle.css'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-terminal-bundle';

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
                terminalId:p.terminalId,
                terminalName:p.terminalName,
                groups: context.getProp('groups'),
                bundleTypes:[],
                types:[],
                table:{
                    rows:[],
                    pageSize:50,
                    pageSizes:[50, 100, 200, 400],
                    currentPage:0,
                    total:0
                },
                dialog: {
                    addDevice:{
                        visible:false,
                        loading:false,
                        name:'',
                        bundleType:'',
                        type:'',
                        number:1
                    },
                    editDevice:{
                        visible:false,
                        loading:false,
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
                    return 'terminal-' + row.uuid;
                },
                handleCreate:function(){
                    var self = this;
                    self.dialog.addDevice.visible = true;
                },
                handleAddDeviceClose:function(){
                    var self = this;
                    self.dialog.addDevice.name = '';
                    self.dialog.addDevice.bundleType = '';
                    self.dialog.addDevice.type = '';
                    self.dialog.addDevice.number = 1;
                    self.dialog.addDevice.loading = false;
                    self.dialog.addDevice.visible = false;
                },
                handleAddDeviceSubmit:function(){
                    var self = this;
                    if(!self.dialog.addDevice.name){
                        self.$message({
                            type:'error',
                            message:'名称前缀不能为空'
                        });
                    }
                    if(!self.dialog.addDevice.bundleType){
                        self.$message({
                            type:'error',
                            message:'请选择设备类型'
                        });
                    }
                    if(!self.dialog.addDevice.type){
                        self.$message({
                            type:'error',
                            message:'请选设置编解码类型'
                        });
                    }
                    self.dialog.addDevice.loading = true;
                    ajax.post('/tetris/bvc/model/terminal/bundle/add', {
                        terminalId:self.terminalId,
                        name:self.dialog.addDevice.name,
                        bundleType:self.dialog.addDevice.bundleType,
                        type:self.dialog.addDevice.type,
                        number:self.dialog.addDevice.number
                    }, function(data, status){
                        self.dialog.addDevice.loading = false;
                        if(status !== 200) return;
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.table.rows.push(data[i]);
                            }
                            self.table.total += data.length;
                        }
                        self.handleAddDeviceClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleDelete:function(){

                },
                handleRowEdit:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.editDevice.id = row.id;
                    self.dialog.editDevice.name = row.name;
                    self.dialog.editDevice.visible = true;
                    self.dialog.editDevice.loading = false;
                },
                handleEditDeviceClose:function(){
                    var self = this;
                    self.dialog.editDevice.id = '';
                    self.dialog.editDevice.name = '';
                    self.dialog.editDevice.loading = false;
                    self.dialog.editDevice.visible = false;
                },
                handleEditDeviceSubmit:function(){
                    var self = this;
                    self.dialog.editDevice.loading = true;
                    ajax.post('/tetris/bvc/model/terminal/bundle/edit/name', {
                        id:self.dialog.editDevice.id,
                        name:self.dialog.editDevice.name
                    }, function(data, status){
                        self.dialog.editDevice.loading = false;
                        if(status!==200) return;
                        if(data){
                            for(var i=0; i<self.table.rows.length; i++){
                                if(self.table.rows[i].id == data.id){
                                    self.table.rows.splice(i, 1, data);
                                    break;
                                }
                            }
                        }
                        self.handleEditDeviceClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleListChannel:function(scope){
                    var self = this;
                    var row = scope.row;
                    window.location.hash = '#/page-terminal-bundle-channel/' + self.terminalId + '/' + self.terminalName + '/' + row.id + '/' + row.name
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
                                ajax.post('/tetris/bvc/model/terminal/bundle/delete', {
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
                    ajax.post('/tetris/bvc/model/terminal/bundle/load', {
                        terminalId:self.terminalId,
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
                    self.bundleTypes.splice(0, self.bundleTypes.length);
                    self.types.splice(0, self.types.length);
                    ajax.post('/tetris/bvc/model/terminal/bundle/query/types', null, function(data){
                        var bundleTypes = data.bundleTypes;
                        var types = data.types;
                        if(bundleTypes && bundleTypes.length>0){
                            for(var i=0; i<bundleTypes.length; i++){
                                self.bundleTypes.push(bundleTypes[i]);
                            }
                        }
                        for(var i in types){
                            self.types.push({
                                id:i,
                                name:types[i]
                            })
                        }
                    });
                }
            },
            created:function(){
                var self = this;
                self.load(1);
                self.queryTypes();
            }
        });

    };

    var destroy = function(){

    };

    var groupList = {
        path:'/' + pageId + '/:terminalId/:terminalName',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});