define([
    'text!' + window.APPPATH + 'login-variable-type/page-login-variable-type.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'login-variable-type/page-login-variable-type.css'
], function(tpl, config, ajax, context, commons, Vue){

    var locale = context.getProp('locale');

    var pageId = 'page-login-variable-type';

    var init = function(){

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el:'#' + pageId + '-wrapper',
            data:{
                loadingText: "",
                menus:context.getProp('menus'),
                user:context.getProp('user'),
                groups:context.getProp('groups'),
                loading:false,
                table:{
                    data:[],
                },
                types:[],
                dialog:{
                    add:{
                        loading:false,
                        visible: false,
                        type:'',
                        name:'',
                        variableKey:'',
                    },
                    edit:{
                        loading:false,
                        visible: false,
                        id:'',
                        type:'',
                        name:'',
                        variableKey:'',
                    },
                }
            },
            methods:{
                rowKey:function(row){
                    return 'variableType-' + row.uuid;
                },
                load:function(){
                    var self = this;
                    ajax.post('/variableType/query', null, function(data,status){
                        if (status != 200) return;
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.table.data.push(data[i]);
                            }
                        }
                    },null, ajax.NO_ERROR_CATCH_CODE);
                },
                handleCreate:function(){
                    var self = this;
                    self.dialog.add.type=self.types[0].name;
                    self.dialog.add.visible = true;
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
                                h('p', null, ['此操作将永久该变量类型，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/variableType/delete', {
                                    id:row.id
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    if(status !== 200) return;
                                    for(var i=0; i<self.table.data.length; i++){
                                        if(self.table.data[i].id === row.id){
                                            self.table.data.splice(i, 1);
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
                handleAddVariableClose:function(){
                    var self = this;
                    self.dialog.add.name = '';
                    self.dialog.add.variableKey = '';
                    self.dialog.add.type = '';
                    self.dialog.add.loading = false;
                    self.dialog.add.visible = false;
                },
                handleAddVariableSubmit:function(){
                    var self = this;
                    self.dialog.add.loading = true;
                    if (!self.dialog.add.type) {
                        self.$message({
                            'type': 'warning',
                            'message': "请选择类型"

                        })
                        self.dialog.add.loading = false;
                        return
                    }else if(!self.dialog.add.name || !self.dialog.add.variableKey){
                        self.$message({
                            'type': 'warning',
                            'message': "请输入完整内容"

                        })
                        self.dialog.add.loading = false;
                        return
                    }
                    ajax.post('/variableType/add',
                        {
                            name:self.dialog.add.name,
                            variableKey:self.dialog.add.variableKey,
                            type:self.dialog.add.type
                        },function(data,status){
                            self.dialog.add.loading = false;
                            if(status != 200) return;
                            self.table.data.push(data);
                            self.handleAddVariableClose();
                        }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                editVariable:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.edit.id = row.id;
                    self.dialog.edit.name = row.name;
                    self.dialog.edit.variableKey = row.variableKey;
                    self.dialog.edit.type = row.type;
                    self.dialog.edit.visible = true;
                },
                handleEditVariableSubmit:function(){
                    var self = this;
                    self.dialog.edit.loading = true;
                    if (!self.dialog.edit.type) {
                        self.$message({
                            'type': 'warning',
                            'message': "请选择类型"

                        })
                        self.dialog.edit.loading = false;
                        return
                    }else if(!self.dialog.edit.name || !self.dialog.edit.type){
                        self.$message({
                            'type': 'warning',
                            'message': "请输入完整内容"

                        })
                        self.dialog.edit.loading = false;
                        return
                    }
                    id:self.dialog.edit.id;
                    name:self.dialog.edit.name;
                    variableKey:self.dialog.edit.variableKey;
                    ajax.post('/variableType/edit',
                        {
                            id:self.dialog.edit.id,
                            name:self.dialog.edit.name,
                            variableKey:self.dialog.edit.variableKey,
                            type:self.dialog.edit.type
                        },function(data,status){
                            self.dialog.edit.loading = false;
                            if(status != 200) return;
                            for(var i=0; i<self.table.data.length; i++){
                                if(self.table.data[i].id === self.dialog.edit.id){
                                    self.table.data.splice(i, 1, data);
                                    break;
                                }
                            }
                            self.handleEditVariableClose();
                        }, null, ajax.NO_ERROR_CATCH_CODE);

                },
                handleEditVariableClose:function(){
                    var self = this;
                    self.dialog.edit.name = '';
                    self.dialog.edit.variableKey = '';
                    self.dialog.edit.type = '';
                    self.dialog.edit.loading = false;
                    self.dialog.edit.visible = false;
                },
                loadAllType:function(){
                    var self = this;
                    ajax.post('/variableType/find/type', null, function(data){
                        for(var i=0; i<data.length; i++){
                            self.types.push(data[i]);
                        }
                    });
                }
            },
            created:function(){
                var self = this;
                self.load();
                self.loadAllType();

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