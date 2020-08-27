/**
 * Created by lvdeyang on 2018/12/24 0024.
 */
define([
    'text!' + window.APPPATH + 'omms/software/service/type/page-omms-software-service-type.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'ace-for-shell',
    'element-ui',
    'mi-frame',
    'date',
    'css!' + window.APPPATH + 'omms/software/service/type/page-omms-software-service-type.css'
], function(tpl, config, $, ajax, context, commons, Vue, ace){

    var pageId = 'page-omms-software-service-type';

    var charts = {};

    var init = function(){

        //璁剧疆鏍囬
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el: '#' + pageId + '-wrapper',
            data: {
                basePath:window.BASEPATH,
                menus: context.getProp('menus'),
                user: context.getProp('user'),
                groups: context.getProp('groups'),
                propertyValueTypes:[],
                tree:{
                    props:{
                        label:'name',
                        children:'children',
                        isLeaf:'isLeaf'
                    },
                    data:[],
                    current:''
                },
                columns:{
                    name:{
                        name:'服务类型',
                        value:'媒资服务'
                    },
                    installationDirectory:{
                        name:'安装包路径',
                        value:''
                    },
                    logFile:{
                        name:'日志路径',
                        value:''
                    },
                    properties:{
                        name:'服务属性',
                        show:false,
                        rows:[]
                    },
                    installScript:{
                        name:'安装脚本',
                        value:'',
                        path:''
                    },
                    startupScript:{
                        name:'启动脚本',
                        value:'',
                        path:''
                    },
                    shutdownScript:{
                        name:'停止脚本',
                        value:'',
                        path:''
                    }
                },
                editors:[{
                    name:'installScript',
                    height:0,
                    editor:''
                },{
                    name:'startupScript',
                    height:0,
                    editor:''
                },{
                    name:'shutdownScript',
                    height:0,
                    editor:''
                }],
                dialog:{
                    editColumn:{
                        visible:false,
                        key:'',
                        column:''
                    },
                    addProperty:{
                        visible:false,
                        loading:false,
                        serviceTypeId:'',
                        propertyKey:'',
                        propertyName:'',
                        valueType:'',
                        propertyDefaultValue:''
                    },
                    editProperty:{
                        visible:false,
                        loading:false,
                        id:'',
                        propertyKey:'',
                        propertyName:'',
                        valueType:'',
                        propertyDefaultValue:''
                    }
                },
                oneButtonCreateLoading:false
            },
            computed:{

            },
            watch:{

            },
            methods:{
                loadValueTypes:function(){
                    var self = this;
                    self.propertyValueTypes.splice(0, self.propertyValueTypes.length);
                    ajax.post('/service/properties/find/value/types', null, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.propertyValueTypes.push(data[i]);
                            }
                        }
                    });
                },
                loadAllServiceTypes:function(){
                    var self = this;
                    self.tree.data.splice(0, self.tree.data.length);
                    ajax.post('/service/type/find/all', null, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.tree.data.push(data[i]);
                            }
                        }
                    });
                },
                handleCreateProperty:function(){
                    var self = this;
                    self.dialog.addProperty.serviceTypeId = self.tree.current.id;
                    self.dialog.addProperty.visible = true;
                },
                handleCreatePropertyClose:function(){
                    var self = this;
                    self.dialog.addProperty.visible = false;
                    self.dialog.addProperty.loading = false;
                    self.dialog.addProperty.serviceTypeId = '';
                    self.dialog.addProperty.propertyKey = '';
                    self.dialog.addProperty.propertyName = '';
                    self.dialog.addProperty.valueType = '';
                    self.dialog.addProperty.propertyDefaultValue = '';
                },
                handleCreatePropertySubmit:function(){
                    var self = this;
                    self.dialog.addProperty.loading = true;
                    ajax.post('/service/properties/add', {
                        serviceTypeId:self.dialog.addProperty.serviceTypeId,
                        propertyKey:self.dialog.addProperty.propertyKey,
                        propertyName:self.dialog.addProperty.propertyName,
                        valueType:self.dialog.addProperty.valueType,
                        propertyDefaultValue:self.dialog.addProperty.propertyDefaultValue
                    }, function(data, status){
                        self.dialog.addProperty.loading = false;
                        if(status !== 200) return;
                        self.columns.properties.rows.push(data);
                        if(!self.tree.current.params.properties) self.tree.current.params.properties = [];
                        self.tree.current.params.properties.push(data);
                        self.handleCreatePropertyClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleEditProperty:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.editProperty.id = row.id;
                    self.dialog.editProperty.propertyKey = row.propertyKey;
                    self.dialog.editProperty.propertyName = row.propertyName;
                    self.dialog.editProperty.valueType = row.valueTypeName;
                    self.dialog.editProperty.propertyDefaultValue = row.propertyDefaultValue;
                    self.dialog.editProperty.visible = true;
                },
                handleEditPropertyClose:function(){
                    var self = this;
                    self.dialog.editProperty.id = '';
                    self.dialog.editProperty.propertyKey = '';
                    self.dialog.editProperty.propertyName = '';
                    self.dialog.editProperty.valueType = '';
                    self.dialog.editProperty.propertyDefaultValue = '';
                    self.dialog.editProperty.visible = false;
                    self.dialog.editProperty.loading = false;
                },
                handleEditPropertySubmit:function(){
                    var self = this;
                    self.dialog.editProperty.loading = true;
                    ajax.post('/service/properties/edit', {
                        id:self.dialog.editProperty.id,
                        propertyKey:self.dialog.editProperty.propertyKey,
                        propertyName:self.dialog.editProperty.propertyName,
                        valueType:self.dialog.editProperty.valueType,
                        propertyDefaultValue:self.dialog.editProperty.propertyDefaultValue
                    }, function(data, status){
                        self.dialog.editProperty.loading = false;
                        if(status !== 200) return;
                        for(var i=0; i<self.columns.properties.rows.length; i++){
                            if(self.columns.properties.rows[i].id === data.id){
                                self.columns.properties.rows.splice(i, 1, data);
                                break;
                            }
                        }
                        for(var i=0; i<self.tree.current.params.properties.length; i++){
                            if(self.tree.current.params.properties[i].id === data.id){
                                self.tree.current.params.properties.splice(i, 1, data);
                                break;
                            }
                        }
                        self.handleEditPropertyClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleRemoveProperty:function(scope){
                    var self = this;
                    var row = scope.row;
                    ajax.post('/service/properties/remove', {
                        id:row.id
                    }, function(){
                        for(var i=0; i<self.columns.properties.rows.length; i++){
                            if(self.columns.properties.rows[i].id === row.id){
                                self.columns.properties.rows.splice(i, 1);
                                break;
                            }
                        }
                        for(var i=0; i<data.params.properties.length; i++){
                            if(data.params.properties[i].id === row.id){
                                data.params.properties.splice(i, 1);
                                break;
                            }
                        }
                    });
                },
                currentTreeNodeChange:function(data){
                    var self = this;
                    if(data.type === 'FOLDER'){
                        self.tree.current = '';
                        self.columns.name.value = '';
                        self.columns.installationDirectory.value = '';
                        self.columns.logFile.value = '';
                        self.columns.installScript.value = '';
                        self.columns.installScript.path = '';
                        self.columns.startupScript.value = '';
                        self.columns.startupScript.path = '';
                        self.columns.shutdownScript.value = '';
                        self.columns.shutdownScript.path = '';
                        return;
                    }
                    self.tree.current = data;
                    self.columns.name.value = data.name;
                    self.columns.installationDirectory.value = data.params.installationDirectory;
                    self.columns.logFile.value = data.params.logFile;
                    self.columns.installScript.value = data.params.installScript;
                    var properties = self.tree.current.params.properties;
                    self.columns.properties.rows.splice(0, self.columns.properties.rows.length);
                    if(properties && properties.length>0){
                        for(var i=0; i<properties.length; i++){
                            self.columns.properties.rows.push(properties[i]);
                        }
                    }

                    for(var i=0; i<self.editors.length; i++){
                        if(self.editors[i].name === 'installScript'){
                            if(self.editors[i].editor){
                                self.editors[i].editor.setValue(self.columns.installScript.value);
                            }
                            break;
                        }
                    }
                    self.columns.installScript.path = data.params.installScriptPath;
                    self.columns.startupScript.value = data.params.startupScript;
                    for(var i=0; i<self.editors.length; i++){
                        if(self.editors[i].name === 'startupScript'){
                            if(self.editors[i].editor){
                                self.editors[i].editor.setValue(self.columns.startupScript.value);
                            }
                            break;
                        }
                    }
                    self.columns.startupScript.path = data.params.startupScriptPath;
                    self.columns.shutdownScript.value = data.params.shutdownScript;
                    for(var i=0; i<self.editors.length; i++){
                        if(self.editors[i].name === 'shutdownScript'){
                            if(self.editors[i].editor){
                                self.editors[i].editor.setValue(self.columns.shutdownScript.value);
                            }
                            break;
                        }
                    }
                    self.columns.shutdownScript.path = data.params.shutdownScriptPath;
                },
                oneButtonCreate:function(){
                    var self = this;
                    self.oneButtonCreateLoading = true;
                    ajax.post('/service/type/one/button/create', null, function(data, status){
                        self.oneButtonCreateLoading = false;
                        if(status !== 200){
                            return;
                        }else{
                            for(var i=0; i<data.length; i++){
                                for(var j=0; j<self.tree.data.length; j++){
                                    if(self.tree.data[j].name === data[i].parent){
                                        self.tree.data[j].children.push(data[i]);
                                        break;
                                    }
                                }
                            }
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                editColumn:function(columnKey, column){
                    var self = this;
                    self.dialog.editColumn.visible = true;
                    self.dialog.editColumn.key = columnKey;
                    self.dialog.editColumn.column = {
                        name:column.name,
                        value:typeof column.path === 'undefined'?column.value:column.path
                    };
                },
                handleEditColumnClose:function(){
                    var self = this;
                    self.dialog.editColumn.visible = false;
                    self.dialog.editColumn.key = '';
                    self.dialog.editColumn.column = '';
                },
                handleEditColumnCommit:function(){
                    var self = this;
                    var param = {
                        id:self.tree.current.id,
                        columnKey:self.dialog.editColumn.key,
                        columnValue:self.dialog.editColumn.column.value
                    };
                    if(self.dialog.editColumn.key === 'installScript'){
                        param.columnKey = 'installScriptPath';
                    }else if(self.dialog.editColumn.key === 'startupScript'){
                        param.columnKey = 'startupScriptPath';
                    }else if(self.dialog.editColumn.key === 'shutdownScript'){
                        param.columnKey = 'shutdownScriptPath';
                    }
                    ajax.post('/service/type/save/column', param, function(data){
                        var column = self.columns[self.dialog.editColumn.key];
                        if(column.path===''|| column.path){
                            column.path = self.dialog.editColumn.column.value;
                        }else{
                            column.value = self.dialog.editColumn.column.value;
                        }
                        self.handleEditColumnClose();
                        self.tree.current.params[param.columnKey] = param.columnValue;
                        self.$message({
                            type:'success',
                            message:'保存成功'
                        });
                    });
                }
            },
            mounted:function(){
                var self = this;
                self.loadValueTypes();
                self.loadAllServiceTypes();

                self.$nextTick(function(){

                    $('#page-omms-software-service-type-wrapper').on('click.action.show.code', '.action-bar .action-show-code', function(){
                        var $codeScope = $(this).closest('.code-scope');
                        var $editor = $codeScope.find('.editor');
                        $codeScope.toggleClass('show');
                        for(var i=0; i<self.editors.length; i++){
                            var editor = self.editors[i];
                            if($codeScope.hasClass(editor.name)){
                                editor.height = editor.height + 200;
                                $editor.css('height', editor.height+'px');
                                if(!editor.editor){
                                    editor.editor = ace.edit($editor[0]);
                                    editor.editor.setOptions({
                                        enableBasicAutocompletion:true,
                                        enableSnippets:true,
                                        enableLiveAutocompletion:true
                                    });
                                    editor.editor.setTheme("ace/theme/xcode");
                                    editor.editor.getSession().setMode("ace/mode/sh");
                                    editor.editor.setReadOnly(true);
                                    editor.editor.setFontSize(14);
                                    editor.editor.setValue(self.columns[editor.name].value);
                                    editor.editor.clearSelection();
                                }else{
                                    setTimeout(function(){editor.editor.resize();}, 100);
                                }
                                break;
                            }
                        }
                    });

                    $('#page-omms-software-service-type-wrapper').on('click.action.hide.code', '.action-bar .action-hide-code', function(){
                        var $codeScope = $(this).closest('.code-scope');
                        for(var i=0; i<self.editors.length; i++){
                            var editor = self.editors[i];
                            if($codeScope.hasClass(editor.name)){
                                if($codeScope.is('.edit')){
                                    if(self.columns[editor.name].value !== editor.editor.getValue()){
                                        self.$message({
                                            type:'error',
                                            message:'您有尚未保存的代码！'
                                        });
                                        return;
                                    }else{
                                        $codeScope.removeClass('edit');
                                        editor.editor.setReadOnly(true);
                                    }
                                }
                                editor.height = 0;
                                $codeScope.find('.editor').css('height', editor.height);
                                $codeScope.toggleClass('show');
                            }
                        }
                    });

                    $('#page-omms-software-service-type-wrapper').on('click.action.edit.code', '.action-bar .action-edit-code', function(){
                        var $codeScope = $(this).closest('.code-scope');
                        $codeScope.toggleClass('edit');
                        for(var i=0; i<self.editors.length; i++){
                            var editor = self.editors[i];
                            if($codeScope.hasClass(editor.name)){
                                editor.editor.setReadOnly(false);
                                break;
                            }
                        }
                    });

                    $('#page-omms-software-service-type-wrapper').on('click.action.save.code', '.action-bar .action-save-code', function(){
                        var $codeScope = $(this).closest('.code-scope');
                        for(var i=0; i<self.editors.length; i++){
                            var editor = self.editors[i];
                            if($codeScope.hasClass(editor.name)){
                                var value = editor.editor.getValue();
                                self.columns[editor.name].value = value;
                                ajax.post('/service/type/save/column', {
                                    id:self.tree.current.id,
                                    columnKey:editor.name,
                                    columnValue:value
                                }, function(data){
                                    self.tree.current.params[editor.name] = self.columns[editor.name].value;
                                    $codeScope.toggleClass('edit');
                                    editor.editor.setReadOnly(true);
                                    self.$message({
                                        type:'success',
                                        message:'保存成功'
                                    });
                                });
                                break;
                            }
                        }
                    });

                    $('#page-omms-software-service-type-wrapper').on('click.action.slide.unit.length', '.action-bar .action-slide-unit-length', function(){
                        var $codeScope = $(this).closest('.code-scope');
                        for(var i=0; i<self.editors.length; i++){
                            var editor = self.editors[i];
                            if($codeScope.hasClass(editor.name)){
                                editor.height += 200;
                                $codeScope.find('.editor').css('height', editor.height+'px');
                                setTimeout(function(){editor.editor.resize();}, 100);
                                break;
                            }
                        }
                    });

                });
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