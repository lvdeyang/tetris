
define([
    'text!' + window.APPPATH + 'login-variable/page-login-variable.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'mi-folder-dialog',
    'mi-task-view',
    'mi-upload-dialog',
    'mi-lightbox',
    'mi-tag-dialog',
    'date',
    'css!' + window.APPPATH + 'login-variable/page-login-variable.css'
], function(tpl, config, ajax, context, commons, Vue){

    var locale = context.getProp('locale');

    var pageId = 'page-login-variable';

    var init = function(){


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
                variableTypes:[],
                dialogBindRole: {
                    bindRoleDialogTableVisible: false,
                    bindRoleSelection: []
                },

                roleOption:[],
                accessNodeTable:[],
                dialog:{
                    add:{
                        loading:false,
                        visible: false,
                        value:'',
                        type:'',
                        variableKey:'',
                        variableType:'',
                        file:'',
                        text:'',
                        variableTypeId:'',
                    },
                    edit:{
                        loading:false,
                        visible: false,
                        id:'',
                        value:'',
                        type:'',
                        text:'',
                        variableKey:'',
                        variableType:'',
                        variableTypeId:'',
                        typeName:'',
                        file:'',
                    },
                    upload:{
                        fileType:['image'],
                        multiple:false
                    },
                    dialogVariableTypes:{
                        variableTypes:[],
                        dialogVariableTypesVisible:false,
                    },
                },

                value:'',
            },
            methods:{
                uploadImg:function(){

                },
                handleUpload:function(){
                    var self = this;
                    self.$refs.uploadDialog.open();
                },
                fileSelected:function(files, done){
                    var self = this;
                    var file = files[0];
                    var task = {
                        name:file.name,
                        size:file.size,
                        mimetype:file.type,
                        lastModified:file.lastModified || file.lastModifiedDate.getTime(),
                        file:file
                    };
                    self.dialog.add.task = task;
                    done();
                },
                load:function(){
                    var self = this;
                    ajax.post('/variable/query', null, function(data,status){
                        if (status != 200) return;
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.table.data.push(data[i]);
                            }
                        }
                    },null, ajax.NO_ERROR_CATCH_CODE);
                },
                queryVariableType: function () {
                    var self = this;
                    ajax.post('/variableType/query', {
                        //currentPage: 1,
                        //pageSize: 10000,
                    }, function (data) {
                        //var rows = data.rows;
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.variableTypes.push(data[i]);
                            }
                        }
                    });
                },
                handleCreate:function(){
                    var self = this;
                    //self.dialog.add.variableType=self.variableTypes[0].name;
                    self.dialog.add.visible = true;

                },
                handleSelectFile:function(){
                    var self = this;
                    document.querySelector('#file').click();
                },
                handleFileSelected:function(){
                    var self = this;
                    var file = document.querySelector('#file').files[0];
                    self.dialog.add.file = file;

                },
                handleSelectFileUpdate:function(){
                    var self = this;
                    document.querySelector('#fileUpdate').click();
                },
                handleFileSelectedUpdate:function(){
                    var self = this;
                    var file = document.querySelector('#fileUpdate').files[0];
                    self.dialog.edit.file = file;

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
                                ajax.post('/variable/delete', {
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
                    self.dialog.add.loading = false;
                    self.dialog.edit.value = '';
                    self.dialog.edit.type = '';
                    self.dialog.add.variableKey = '';
                    self.dialog.add.variableType='';
                    self.dialog.add.file='';
                    self.dialog.add.variableTypeId='';
                    self.dialog.add.text='';
                    self.dialog.add.visible = false;
                },
                handleAddVariableSubmit:function(){
                    var self = this;
                    self.dialog.add.loading = true;
                    if (!self.dialog.add.variableTypeId) {
                        self.$message({
                            'type': 'warning',
                            'message': "请选择类型"

                        })
                        self.dialog.add.loading = false;
                        return
                    }else if(!self.dialog.add.file && !self.dialog.add.text){
                        self.$message({
                            'type': 'warning',
                            'message': "请输入变量"

                        })
                        self.dialog.add.loading = false;
                        return
                    }
                    var params = new FormData();
                    params.append('variableTypeId', self.dialog.add.variableTypeId);
                    params.append('file', self.dialog.add.file);
                    params.append('text', self.dialog.add.text);
                    ajax.upload('/variable/add', params, function(data, status){
                        self.dialog.add.loading = false;
                        if(status !== 200) return;
                        self.table.data.push(data);
                        self.handleAddVariableClose();
                        self.handleEditVariableClose();
                    }, ajax.TOTAL_CATCH_CODE);
                },
                checkVariable:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.edit.id = row.id;
                    self.dialog.edit.value = row.value;
                    self.dialog.edit.type = row.type;
                    self.dialog.edit.variableKey = row.variableKey;
                    self.dialog.edit.text = row.value;
                    self.dialog.edit.variableType = row.type;
                    self.dialog.edit.typeName = row.typeName;
                    self.dialog.edit.variableTypeId = row.variableTypeId;
                    self.dialog.edit.visible = true;
                },
                handleEditVariableSubmit:function(){
                    var self = this;
                    self.dialog.edit.loading = true;
                    if (!self.dialog.edit.variableTypeId) {
                        self.$message({
                            'type': 'warning',
                            'message': "请选择类型"

                        })
                        self.dialog.edit.loading = false;
                        return
                    }else if(!self.dialog.edit.file && !self.dialog.edit.text){
                        self.$message({
                            'type': 'warning',
                            'message': "请输入变量"

                        })
                        self.dialog.edit.loading = false;
                        return
                    }
                    var params = new FormData();
                    params.append('id', self.dialog.edit.id);
                    params.append('variableTypeId', self.dialog.edit.variableTypeId);
                    params.append('file', self.dialog.edit.file);
                    params.append('text', self.dialog.edit.text);
                    ajax.upload('/variable/edit', params, function(data, status){
                    self.dialog.edit.loading = false;
                    if(status != 200) return;
                    for(var i=0; i<self.table.data.length; i++){
                        if(self.table.data[i].id === self.dialog.edit.id){
                            self.table.data.splice(i, 1, data);
                            break;
                            }
                        }
                        self.handleEditVariableClose();
                        self.handleAddVariableClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                handleEditVariableClose:function(){
                    var self = this;
                    self.dialog.edit.value = '';
                    self.dialog.edit.type = '';
                    self.dialog.edit.variableKey = '';
                    self.dialog.edit.text = '';
                    self.dialog.edit.variableType = '';
                    self.dialog.edit.variableTypeId = '';
                    self.dialog.edit.id = '';
                    self.dialog.edit.visible = false;
                },
                handlePreview:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.value = row.value;
                    self.$refs.Lightbox.preview(self.value,'image');
                },
                handleChangeType: function () {
                    var self = this;
                    self.dialog.dialogVariableTypes.dialogVariableTypesVisible = true;
                    self.dialog.dialogVariableTypes.variableTypes = [];
                    ajax.post('/variableType/query',{}, function (data) {
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.dialog.dialogVariableTypes.variableTypes.push(data[i]);
                            }
                        }
                    });
                    self.dialog.dialogVariableTypes.variableTypes;

                },
                currentRowChange: function (currentRow, oldRow) {
                    var self = this;
                    self.tbindType = currentRow;
                },
                handleBindType: function () {
                    this.dialog.dialogVariableTypes.dialogVariableTypesVisible = false;
                    this.dialog.add.variableType = this.tbindType.name;
                    this.dialog.add.variableTypeId = this.tbindType.id;
                    this.dialog.add.type = this.tbindType.type;
                    this.dialog.edit.variableType = this.tbindType.name;
                    this.dialog.edit.variableTypeId = this.tbindType.id;
                    this.dialog.edit.type = this.tbindType.type;
                },
            },
            created:function(){
                var self = this;
                self.load();
                self.queryVariableType()
            },
            //rules:{
            //    typeInput:[
            //        { required: true, message: '请选择变量类型', trigger: 'blur' },
            //    ],
            //    textInput:[
            //        { required: true, message: '请选择变量类型', trigger: 'blur' }
            //    ],
            //},
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