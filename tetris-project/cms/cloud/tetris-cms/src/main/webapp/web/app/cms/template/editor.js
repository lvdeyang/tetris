/**
 * Created by Administrator on 2019/2/21 0021.
 */
define([
    'text!' + window.APPPATH + 'cms/template/editor.html',
    'restfull',
    'jquery',
    'vue',
    'ace-for-website',
    'juicer',
    'element-ui',
    'css!' + window.APPPATH + 'cms/template/editor.css'
], function(tpl, ajax, $, Vue, ace, juicer){

    var fmt = ace.require('ace/ext/beautify');

    var pluginName = 'template-editor';

    Vue.component(pluginName, {
        props:['id'],
        template: tpl,
        data:function(){
            return {
                template:'',
                editor:{
                    el:'',
                    html:'',
                    css:'',
                    js:''
                },
                editors:[],
                dialog:{
                    selectText:{
                        visible:false,
                        data:[],
                        current:''
                    },
                    selectImage:{
                        visible:false,
                        data:[],
                        current:''
                    },
                    selectMultipleImage:{
                        visible:false,
                        data:[],
                        select:[]
                    },
                    viewportSize:{
                        visible:false,
                        width:'',
                        height:''
                    },
                    editVariable:{
                        visible:false,
                        target:'',
                        key:'',
                        editor:'',
                        icon:'',
                        style:'',
                        comment:'',
                        value:''
                    }
                },
                arraySimple:{
                   visible:false,
                   value:''
                },
                arrayImage:{

                },
                tree:{
                    props:{
                        label: 'key',
                        children: 'sub'
                    },
                    expandOnClickNode:false,
                    data:[
                        /*{key:'变量名',
                         editor:'编辑器类型',
                         icon:'编辑器图标',
                         style:'编辑器图标样式',
                         comment:'变量注释',
                         value:'变量测试值'}*/
                    ]
                }

            }
        },
        methods:{
            show:function(template){
                var self = this;
                self.template = template;
                var html = template.html || '<!-- html编辑器 -->\n<div>\n    \n</div>';
                self.editor.html.setValue(html);
                self.editor.html.selection.clearSelection();
                self.editor.el.show();
            },
            hide:function(){
                var self = this;
                self.editor.el.hide();
            },
            doPreview:function(){
                var self = this;
                var $editor = self.editor.el;
                var $viewport = $editor.find('.preview-viewport');
                var html = self.editor.html.getValue();
                var json = {};
                var variables = self.tree.data;
                if(variables && variables.length>0){
                    for(var i=0; i<variables.length; i++){
                        var variable = variables[i];
                        if(variable.editor === 'ARRAY_IMAGE'){
                            json[variable.key] = variable.value;
                        }else if(variable.editor === 'ARRAY_SIMPLE'){
                            json[variable.key] = variable.value;
                        }else if(variable.editor === 'ARRAY_OBJECT'){

                        }else{
                            json[variable.key] = variable.value;
                        }
                    }
                }
                try{
                    html = juicer(html).render(json);
                }catch(e){
                    console.log(e);
                }
                $viewport.empty().append(html);
            },
            handleSelectTextClose:function(){
                var self = this;
                self.dialog.selectText.data.splice(0, self.dialog.selectText.data.length);
                self.dialog.selectText.current = '';
                self.dialog.selectText.visible = false;
            },
            currentSelectTextChange:function(row){
                var self = this;
                self.dialog.selectText.current = row;
            },
            handleSelectTextCommit:function(){
                var self = this;
                var row = self.dialog.selectText.current;
                self.dialog.editVariable.value = row.content;
                self.handleSelectTextClose();
            },
            handleSelectImageClose:function(){
                var self = this;
                self.dialog.selectImage.data.splice(0, self.dialog.selectImage.data.length);
                self.dialog.selectImage.current = '';
                self.dialog.selectImage.visible = false;
            },
            currentSelectImageChange:function(row){
                var self = this;
                self.dialog.selectImage.current = row;
            },
            handleSelectImageCommit:function(){
                var self = this;
                var row = self.dialog.selectImage.current;
                self.dialog.editVariable.value = window.BASEPATH + row.previewUrl;
                self.handleSelectImageClose();
            },
            handleSelectMultipleImageClose:function(){
                var self = this;
                self.dialog.selectMultipleImage.data.splice(0, self.dialog.selectMultipleImage.data.length);
                self.dialog.selectMultipleImage.select.splice(0, self.dialog.selectMultipleImage.select.length);
                self.dialog.selectMultipleImage.visible = false;
            },
            handleSelectMultipleImageChange:function(select){
                var self = this;
                self.dialog.selectMultipleImage.select.splice(0, self.dialog.selectMultipleImage.select.length);
                if(select && select.length>0){
                    for(var i=0; i<select.length; i++){
                        self.dialog.selectMultipleImage.select.push(select[i]);
                    }
                }
            },
            handleSelectMultipleImageCommit:function(){
                var self = this;
                self.dialog.editVariable.value.splice(0, self.dialog.editVariable.value.length);
                var select = self.dialog.selectMultipleImage.select;
                if(select && select.length>0){
                    for(var i=0; i<select.length; i++){
                        /*var url = window.BASEPATH + select[i].previewUrl;
                        if(self.dialog.editVariable.value.indexOf(url) < 0) */
                        self.dialog.editVariable.value.push(window.BASEPATH + select[i].previewUrl);
                    }
                }
                self.handleSelectMultipleImageClose();
            },
            handleViewportSizeClose:function(){
                var self = this;
                self.dialog.viewportSize.width = '';
                self.dialog.viewportSize.height = '';
                self.dialog.viewportSize.visible = false;
            },
            handleViewportSizeCommit:function(){
                var self = this;
                var $viewport = self.editor.el.find('.preview-viewport');
                $viewport.parent().css({
                    width:(''+self.dialog.viewportSize.width.indexOf('%'))>=0?self.dialog.viewportSize.width:(self.dialog.viewportSize.width + 'px'),
                    height:(''+self.dialog.viewportSize.height.indexOf('%'))>=0?self.dialog.viewportSize.height:(self.dialog.viewportSize.height + 'px')
                });
                self.handleViewportSizeClose();
            },
            handleViewportMaximize:function(){
                var self = this;
                self.dialog.viewportSize.width = '100%';
                self.dialog.viewportSize.height = '100%';
            },
            handleViewportSD:function(){
                var self = this;
                self.dialog.viewportSize.width = '1280';
                self.dialog.viewportSize.height = '720';
            },
            handleViewportHD:function(){
                var self = this;
                self.dialog.viewportSize.width = '1920';
                self.dialog.viewportSize.height = '1080';
            },
            handleViewportPhone:function(){
                var self = this;
                self.dialog.viewportSize.width = '414';
                self.dialog.viewportSize.height = '738';
            },
            destroy:function(){
                var self = this;
                //销毁编辑器
                self.editor.html.destroy();
            },
            addRootTreeNode:function(){
                var self = this;
                var data = {
                    key: '新建的变量',
                    editor: '',
                    icon: '',
                    style: '',
                    comment: '',
                    value: ''
                };
                self.tree.data.push(data);
            },
            treeNodeAppend:function(node, data){

            },
            treeNodeEdit:function(node, data){
                var self = this;
                self.dialog.editVariable.target = data;
                self.dialog.editVariable.key = data.key;
                self.dialog.editVariable.editor = data.editor;
                self.dialog.editVariable.icon = data.icon;
                self.dialog.editVariable.style = data.style;
                self.dialog.editVariable.comment = data.comment;
                self.dialog.editVariable.value = data.value;
                self.dialog.editVariable.visible = true;
            },
            treeNodeDelete:function(node, data){
                var self = this;
                var h = self.$createElement;
                self.$msgbox({
                    title:'危险操作',
                    message:h('div', null, [
                        h('div', {class:'el-message-box__status el-icon-warning'}, null),
                        h('div', {class:'el-message-box__message'}, [
                            h('p', null, ['此操作将永久删除变量，且不可恢复，是否继续?'])
                        ])
                    ]),
                    type:'wraning',
                    showCancelButton: true,
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    beforeClose:function(action, instance, done){
                        if(action === 'confirm'){
                            var variables = self.tree.data;
                            for(var i=0; i<variables.length; i++){
                                if(variables[i] === data){
                                    variables.splice(i, 1);
                                    break;
                                }
                            }
                            done();
                        }else{
                            done();
                        }
                    }
                }).catch(function(){});
            },
            handleEditVariableClose:function(){
                var self = this;
                self.dialog.editVariable.visible = false;
                self.dialog.editVariable.target = '';
                self.dialog.editVariable.key = '';
                self.dialog.editVariable.editor = '';
                self.dialog.editVariable.icon = '';
                self.dialog.editVariable.style = '';
                self.dialog.editVariable.comment = '';
                self.dialog.editVariable.value = '';
            },
            handleEditVariableCommit:function(){
                var self = this;
                self.dialog.editVariable.target.key = self.dialog.editVariable.key;
                self.dialog.editVariable.target.editor = self.dialog.editVariable.editor;
                self.dialog.editVariable.target.icon = self.dialog.editVariable.icon;
                self.dialog.editVariable.target.style = self.dialog.editVariable.style;
                self.dialog.editVariable.target.comment = self.dialog.editVariable.comment;
                self.dialog.editVariable.target.value = self.dialog.editVariable.value;
                self.doPreview();
                self.handleEditVariableClose();
            },
            handleEditorChange:function(editorKey){
                var self = this;
                for(var i=0; i<self.editors.length; i++){
                    if(self.editors[i].key === editorKey){
                        self.dialog.editVariable.icon = self.editors[i].icon;
                        self.dialog.editVariable.style = self.editors[i].style;
                        break;
                    }
                }
                if(editorKey.indexOf('ARRAY') >= 0){
                    Vue.set(self.dialog.editVariable, 'value', []);
                }else{
                    Vue.set(self.dialog.editVariable, 'value', '');
                }
            },
            handleInsertText:function(){
                var self = this;
                self.dialog.selectText.visible = true;
                ajax.post('/cms/resource/list/text', null, function(data){
                    if(data && data.length>0){
                        for(var i=0; i<data.length; i++){
                            self.dialog.selectText.data.push(data[i]);
                        }
                    }
                });
            },
            handleInsertImage:function(){
                var self = this;
                self.dialog.selectImage.visible = true;
                ajax.post('/cms/resource/list/image', null, function(data){
                    if(data && data.length>0){
                        for(var i=0; i<data.length; i++){
                            self.dialog.selectImage.data.push(data[i]);
                        }
                    }
                });
            },
            handleInsertMultipleImage:function(){
                var self = this;
                self.dialog.selectMultipleImage.visible = true;
                ajax.post('/cms/resource/list/image', null, function(data){
                    if(data && data.length>0){
                        for(var i=0; i<data.length; i++){
                            self.dialog.selectMultipleImage.data.push(data[i]);
                        }
                        var value = self.dialog.editVariable.value;
                        if(value && value.length>0){
                            for(var i=0; i<value.length; i++){
                                for(var j=0; j<self.dialog.selectMultipleImage.data.length; j++){
                                    if((window.BASEPATH + self.dialog.selectMultipleImage.data[j].previewUrl) === value[i]){
                                        self.dialog.selectMultipleImage.select.push(self.dialog.selectMultipleImage.data[j]);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                });
            },
            handleArraySimpleEdit:function(){
                var self = this;
                self.arraySimple.visible = true;
                self.$nextTick(function(){
                    self.$refs.arraySimpleAddInput.$refs.input.focus();
                });
            },
            handleArraySimpleAdd:function(){
                var self = this;
                if(self.arraySimple.value){
                    if(self.dialog.editVariable.value.indexOf(self.arraySimple.value) < 0){
                        self.dialog.editVariable.value.push(self.arraySimple.value);
                    }
                }
                self.arraySimple.visible = false;
                self.arraySimple.value = '';
            },
            handleArraySimpleRemove:function(tag){
                var self = this;
                self.dialog.editVariable.value.splice(self.dialog.editVariable.value.indexOf(tag), 1);
            }
        },
        created:function(){
            var self = this;
            ajax.post('/cms/resource/list/editor', null, function(data){
                for(var i=0; i<data.length; i++){
                    self.editors.push(data[i]);
                }
            });
        },
        mounted:function(){
            var self = this;
            self.$nextTick(function(){
                self.editor.el = $('#'+self.id);
                var htmlEditor = self.editor.html = ace.edit(self.editor.el.find('.html-editor')[0]);
                htmlEditor.setOptions({
                    enableBasicAutocompletion:true,
                    enableSnippets:true,
                    enableLiveAutocompletion:true
                });
                htmlEditor.setTheme("ace/theme/idle_fingers");
                htmlEditor.getSession().setMode("ace/mode/html");
                htmlEditor.setFontSize(16);

                //编辑器事件
                self.editor.el.on('click.toolbar.show', '.editor-toolbar', function(e){
                    e.stopPropagation();
                    var $this = $(this);
                    $this.removeClass('mini').addClass('max');
                });
                self.editor.el.on('click.toolbar.exit', '.toolbar-exit', function(e){
                    e.stopPropagation();
                    var $this = $(this);
                    var $toolbar = $this.closest('.editor-toolbar');
                    $toolbar.removeClass('max').addClass('mini');
                });
                self.editor.el.on('click.toolbar.max', '.change-max', function(e){
                    e.stopPropagation();
                    var $this = $(this);
                    var $toolbar = $this.closest('.editor-toolbar');
                    $toolbar.closest('.split').addClass('max');
                });
                self.editor.el.on('click.toolbar.mini', '.change-mini', function(e){
                    e.stopPropagation();
                    var $this = $(this);
                    var $toolbar = $this.closest('.editor-toolbar');
                    $toolbar.closest('.split').removeClass('max');
                });
                self.editor.el.on('click.insert.variable', '.insert-variable', function(e){
                    e.stopPropagation();
                    self.editor.html.insert('${_'+self.template.id+new Date().getTime()+'}');
                    fmt.beautify(self.editor.html.session);
                });
                self.editor.el.on('click.insert.if', '.insert-if', function(e){
                    e.stopPropagation();
                    self.editor.html.insert('\n{@if _'+self.template.id+new Date().getTime()+' == 0}\n{@else}\n{@/if}\n');
                    fmt.beautify(self.editor.html.session);
                });
                self.editor.el.on('click.insert.for', '.insert-for', function(e){
                    e.stopPropagation();
                    self.editor.html.insert('\n{@each _'+self.template.id+new Date().getTime()+' as scope, index}\n${scope}\n{@/each}\n');
                    fmt.beautify(self.editor.html.session);
                });
                self.editor.html.getSession().on('change', function(e) {
                    self.doPreview();
                });
                self.editor.el.on('click.preview.viewport.size', '.change-size', function(e){
                    e.stopPropagation();
                    var $viewport = self.editor.el.find('.preview-viewport');
                    self.dialog.viewportSize.width = $viewport[0].clientWidth;
                    self.dialog.viewportSize.height = $viewport[0].clientHeight;
                    self.dialog.viewportSize.visible = true;
                });
            });
        }
    });

    //删除js中的注释
    var removeNotes = function(value){
        var reg = /("([^\\\"]*(\\.)?)*")|('([^\\\']*(\\.)?)*')|(\/{2,}.*?(\r|\n|$))|(\/\*(\n|.)*?\*\/)/g;
        var js = value.replace(reg, function(word) {
            // 去除注释后的文本
            return /^\/{2,}/.test(word) || /^\/\*/.test(word) ? "" : word;
        });
    }

});