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
                    }
                },
                oneButtonCreateLoading:false
            },
            computed:{

            },
            watch:{

            },
            methods:{
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
                    });
                }
            },
            mounted:function(){
                var self = this;

                ajax.post('/service/type/find/all', null, function(data){
                    self.tree.data.splice(0, self.tree.data.length);
                    if(data && data.length>0){
                        for(var i=0; i<data.length; i++){
                            self.tree.data.push(data[i]);
                        }
                    }
                });

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
                                console.log(self.columns[editor.name].value);
                                $codeScope.toggleClass('edit');
                                editor.editor.setReadOnly(true);
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