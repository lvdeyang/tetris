/**
 * Created by lvdeyang on 2018/12/24 0024.
 */
define([
    'text!' + window.APPPATH + 'cms/template/page-cms-template.html',
    'text!' + window.APPPATH + 'cms/template/editor.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'editor',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'cms/template/page-cms-template.css',
    'css!' + window.APPPATH + 'cms/template/editor.css'
], function(tpl, tpl_editors, config, $, ajax, context, commons, editor, Vue){

    var pageId = 'page-cms-template';

    var instance = null;

    var init = function(){

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        instance = new Vue({
            el: '#' + pageId + '-wrapper',
            data: {
                menus:context.getProp('menus'),
                user:context.getProp('user'),
                groups:context.getProp('groups'),
                types:[],
                tree:{
                    props:{
                        label: 'name',
                        children: 'subTags'
                    },
                    expandOnClickNode:false,
                    data:[],
                    current:''
                },
                table:{
                    data:[]
                },
                dialog:{
                    editTag:{
                        visible:false,
                        data:'',
                        name:'',
                        remark:''
                    },
                    addTemplate:{
                        visible:false,
                        name:'',
                        type:'',
                        remark:'',
                        loading:false
                    },
                    editTemplate:{
                        visible:false,
                        id:'',
                        name:'',
                        type:'',
                        remark:'',
                        loading:false
                    },
                    selectTag:{
                        visible:false,
                        template:'',
                        tree:{
                            props:{
                                label: 'name',
                                children: 'subTags'
                            },
                            expandOnClickNode:false,
                            data:[],
                            current:''
                        },
                        loading:false
                    },
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
                    viewportSize:{
                        visible:false,
                        width:'',
                        height:''
                    }
                },
                loading:{
                    tree:false,
                    addRoot:false
                },
                editor:{
                    el:'',
                    html:'',
                    css:'',
                    js:'',
                    show:editorShow,
                    hide:editorHide
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                loadTagTree:function(){
                    var self = this;
                    self.tree.data.splice(0, self.tree.data.length);
                    ajax.post('/cms/template/tag/list/tree', null, function(data){
                        self.tree.data.push({
                            id:-1,
                            uuid:'-1',
                            name:'无标签',
                            icon:'icon-tag',
                            style:'font-size:15px; position:relative; top:1px; margin-right:1px;'
                        });
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.tree.data.push(data[i]);
                            }
                        };
                        self.currentNode(self.tree.data[0]);
                    });
                },
                loadSelectTagTree:function(){
                    var self = this;
                    self.dialog.selectTag.tree.data.splice(0, self.dialog.selectTag.tree.data.length);
                    ajax.post('/cms/template/tag/list/tree', null, function(data){
                        self.dialog.selectTag.tree.data.push({
                            id:-1,
                            uuid:'-1',
                            name:'无标签',
                            icon:'icon-tag',
                            style:'font-size:15px; position:relative; top:1px; margin-right:1px;'
                        });
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.dialog.selectTag.tree.data.push(data[i]);
                            }
                        };
                    });
                },
                treeNodeAllowDrag:function(node){
                    return node.data.id !== -1;
                },
                treeNodeAllowDrop:function(dragNode, dropNode, type) {
                    return type === 'inner' && dropNode.data.id !== -1;
                },
                treeNodeDrop:function(dragNode, dropNode, type){
                    var self = this;
                    self.loading.tree = true;
                    ajax.post('/cms/template/tag/move', {
                        sourceId:dragNode.data.id,
                        targetId:dropNode.data.id
                    }, function(data, status){
                        self.loading.tree = false;
                        if(status !== 200) return;
                        self.loadTagTree();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                addRootTreeNode:function(){
                    var self = this;
                    self.loading.tree = true;
                    self.loading.addRoot = true;
                    ajax.post('/cms/template/tag/add/root', null, function(data, status){
                        self.loading.tree = false;
                        self.loading.addRoot = false;
                        if(status !== 200) return;
                        self.tree.data.push(data);
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                currentTreeNodeChange:function(data){
                    var self = this;
                    self.currentNode(data);
                },
                treeNodeTop:function(node, nodeData){
                    var self = this;
                    var parentData = node.parent.data;
                    self.loading.tree = true;
                    ajax.post('/cms/template/tag/top/' + nodeData.id, null, function(data, status){
                        self.loading.tree = false;
                        if(status !== 200) return;
                        if(data){
                            self.loadTagTree();
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                treeNodeEdit:function(node, data){
                    var self = this;
                    self.dialog.editTag.data = data;
                    self.dialog.editTag.name = data.name;
                    self.dialog.editTag.remark = data.remark;
                    self.dialog.editTag.visible = true;
                },
                treeNodeAppend:function(parentNode, parent){
                    var self = this;
                    self.loading.tree = true;
                    ajax.post('/cms/template/tag/append', {
                        parentId:parent.id
                    }, function(data, status){
                        self.loading.tree = false;
                        if(status !== 200) return;
                        if(!parent.subTags){
                            Vue.set(parent, 'subTags', []);
                        }
                        parent.subTags.push(data);
                    }, null, ajax.NO_ERROR_CATCH_CODE)
                },
                treeNodeDelete:function(node, nodeData){
                    var self = this;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'危险操作',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['此操作将永久删除标签以及子标签，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                self.loading.tree = true;
                                ajax.post('/cms/template/tag/remove/' + nodeData.id, null, function(data, status){
                                    self.loading.tree = false;
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    self.$refs.tagTree.remove(nodeData);
                                    if(nodeData.id === self.tree.current.id){
                                        self.currentNode(self.tree.data[0]);
                                    }
                                }, null, ajax.NO_ERROR_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                handleEditTagClose:function(){
                    var self = this;
                    self.dialog.editTag.data = '';
                    self.dialog.editTag.name = '';
                    self.dialog.editTag.remark = '';
                    self.dialog.editTag.visible = false;
                },
                handleEditTagCommit:function(){
                    var self = this;
                    self.loading.tree = true;
                    ajax.post('/cms/template/tag/update/' + self.dialog.editTag.data.id, {
                        name:self.dialog.editTag.name,
                        remark:self.dialog.editTag.remark
                    }, function(data, status){
                        self.loading.tree = false;
                        if(status !== 200) return;
                        self.dialog.editTag.data.name = data.name;
                        self.dialog.editTag.data.remark = data.remark;
                        self.handleEditTagClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                currentNode:function(data){
                    if(!data) return;
                    var self = this;
                    self.tree.current = data;
                    self.$nextTick(function(){
                        self.$refs.tagTree.setCurrentKey(data.uuid);
                    });
                    self.loadTemplates(data.id);
                },
                loadTemplateTypes:function(){
                    var self = this;
                    ajax.post('/cms/template/query/types', null, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.types.push(data[i]);
                            }
                        }
                    });
                },
                loadTemplates:function(tagId){
                    var self = this;
                    self.table.data.splice(0, self.table.data.length);
                    ajax.post('/cms/template/load', {
                        tagId:tagId
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.table.data.push(data[i]);
                            }
                        }
                    });
                },
                templateEdit:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.editor.show({});
                },
                rowEdit:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.editTemplate.visible = true;
                    self.dialog.editTemplate.id = row.id;
                    self.dialog.editTemplate.name = row.name;
                    self.dialog.editTemplate.type = row.type;
                    self.dialog.editTemplate.remark = row.remark;
                    self.dialog.editTemplate.loading = false;
                },
                rowDelete:function(scope){
                    var self = this;
                    var row = scope.row;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'危险操作',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['此操作将永久删除模板，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/cms/template/remove/' + row.id, null, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    for(var i=0; i<self.table.data.length; i++){
                                        if(self.table.data[i].id === row.id){
                                            self.table.data.splice(i ,1);
                                            break;
                                        }
                                    }
                                }, null, ajax.NO_ERROR_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});

                },
                handleAddTemplateClose:function(){
                    var self = this;
                    self.dialog.addTemplate.name = '';
                    self.dialog.addTemplate.remark = '';
                    self.dialog.addTemplate.type = '';
                    self.dialog.addTemplate.visible = false;
                },
                handleAddTemplateCommit:function(){
                    var self = this;
                    self.dialog.addTemplate.loading = true;
                    ajax.post('/cms/template/add', {
                        name:self.dialog.addTemplate.name,
                        type:self.dialog.addTemplate.type,
                        remark:self.dialog.addTemplate.remark,
                        tagId:self.tree.current.id
                    }, function(data, status){
                        self.dialog.addTemplate.loading = false;
                        if(status !== 200) return;
                        self.table.data.push(data);
                        self.handleAddTemplateClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                handleEditTemplateClose:function(){
                    var self = this;
                    self.dialog.editTemplate.visible = false;
                    self.dialog.editTemplate.id = '';
                    self.dialog.editTemplate.name = '';
                    self.dialog.editTemplate.type = '';
                    self.dialog.editTemplate.remark = '';
                    self.dialog.editTemplate.loading = false;
                },
                handleEditTemplateCommit:function(){
                    var self = this;
                    self.dialog.editTemplate.loading = true;
                    ajax.post('/cms/template/update/' + self.dialog.editTemplate.id, {
                        name:self.dialog.editTemplate.name,
                        type:self.dialog.editTemplate.type,
                        remark:self.dialog.editTemplate.remark,
                    }, function(data, status){
                        self.dialog.editTemplate.loading = false;
                        if(status !== 200) return;
                        for(var i=0; i<self.table.data.length; i++){
                            if(self.table.data[i].id === self.dialog.editTemplate.id){
                                self.table.data[i].name = self.dialog.editTemplate.name;
                                self.table.data[i].type = self.dialog.editTemplate.type;
                                self.table.data[i].remark = self.dialog.editTemplate.remark;
                                break;
                            }
                        }
                        self.handleEditTemplateClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                changeTag:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.loadSelectTagTree();
                    self.dialog.selectTag.visible = true;
                    self.dialog.selectTag.template = row;
                },
                handleSelectTagClose:function(){
                    var self = this;
                    self.dialog.selectTag.visible = false;
                    self.dialog.selectTag.template = '';
                    self.dialog.selectTag.tree.data.splice(self.dialog.selectTag.tree.data.length);
                    self.dialog.selectTag.tree.current = '';
                    self.dialog.selectTag.loading = false;
                },
                handleSelectTagCommit:function(){
                    var self = this;
                    ajax.post('/cms/template/change/tag/' + self.dialog.selectTag.template.id, {
                        tagId:self.dialog.selectTag.tree.current.id
                    }, function(data){
                        if(data){
                            for(var i=0; i<self.table.data.length; i++){
                                if(self.table.data[i].id === self.dialog.selectTag.template.id){
                                    self.table.data.splice(i, 1);
                                    break;
                                }
                            }
                        }
                        self.handleSelectTagClose();
                    });
                },
                currentSelectedTagChange:function(data){
                    var self = this;
                    self.dialog.selectTag.tree.current = data;
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
                    self.editor.js.insert('"'+row.content+'"');
                    self.editor.js.resize();
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
                    self.editor.js.insert('"'+window.BASEPATH + row.previewUrl+'"');
                    self.editor.js.resize();
                    self.handleSelectImageClose();
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
                    $viewport.css({
                        width:self.dialog.viewportSize.width.indexOf('%')>=0?self.dialog.viewportSize.width:(self.dialog.viewportSize.width + 'px'),
                        height:self.dialog.viewportSize.height.indexOf('%')>=0?self.dialog.viewportSize.height:(self.dialog.viewportSize.height + 'px')
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
                    self.dialog.viewportSize.width = '1080';
                    self.dialog.viewportSize.height = '1920';
                }
            },
            created:function(){
                var self = this;
                self.loadTagTree();
                self.loadTemplateTypes();
            },
            mounted:function(){
                var self = this;
                self.editor.el = $(tpl_editors);
                $('body').append(self.editor.el);

                var htmlEditor = self.editor.html = editor.edit(self.editor.el.find('.html-editor')[0]);
                htmlEditor.setOptions({
                    enableBasicAutocompletion:true,
                    enableSnippets:true,
                    enableLiveAutocompletion:true
                });
                htmlEditor.setTheme("ace/theme/idle_fingers");
                htmlEditor.getSession().setMode("ace/mode/html");
                htmlEditor.setFontSize(16);

                var cssEditor = self.editor.css = editor.edit(self.editor.el.find('.css-editor')[0]);
                cssEditor.setOptions({
                    enableBasicAutocompletion:true,
                    enableSnippets:true,
                    enableLiveAutocompletion:true
                });
                cssEditor.setTheme("ace/theme/xcode");
                cssEditor.getSession().setMode("ace/mode/css");
                cssEditor.setFontSize(16);

                var jsEditor = self.editor.js = editor.edit(self.editor.el.find('.js-editor')[0]);
                jsEditor.setOptions({
                    enableBasicAutocompletion:true,
                    enableSnippets:true,
                    enableLiveAutocompletion:true
                });
                jsEditor.setTheme("ace/theme/idle_fingers");
                jsEditor.getSession().setMode("ace/mode/javascript");
                jsEditor.setFontSize(16);

                //格式化
                var fmt = editor.require('ace/ext/beautify');

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
                    $toolbar.siblings('.editor-container').closest('.split').addClass('max');
                });
                self.editor.el.on('click.toolbar.mini', '.change-mini', function(e){
                    e.stopPropagation();
                    var $this = $(this);
                    var $toolbar = $this.closest('.editor-toolbar');
                    $toolbar.siblings('.editor-container').closest('.split').removeClass('max');
                });
                self.editor.el.on('click.insert.variable', '.insert-variable', function(e){
                    e.stopPropagation();
                    self.editor.html.insert('${_'+new Date().getTime()+'}');
                    fmt.beautify(self.editor.html.session);
                });
                self.editor.el.on('click.insert.if', '.insert-if', function(e){
                    e.stopPropagation();
                    self.editor.html.insert('\n{@if _'+new Date().getTime()+' == 0}\n{@else}\n{@/if}\n');
                    fmt.beautify(self.editor.html.session);
                });
                self.editor.el.on('click.insert.for', '.insert-for', function(e){
                    e.stopPropagation();
                    self.editor.html.insert('\n{@each _'+new Date().getTime()+' as scope, index}\n${scope}\n{@/each}\n');
                    fmt.beautify(self.editor.html.session);
                });
                self.editor.el.on('click.insert.image', '.insert-image', function(e){
                    e.stopPropagation();
                    self.dialog.selectImage.visible = true;
                    ajax.post('/cms/resource/list/image', null, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.dialog.selectImage.data.push(data[i]);
                            }
                        }
                    });
                });
                self.editor.el.on('click.insert.text', '.insert-text', function(e){
                    e.stopPropagation();
                    self.dialog.selectText.visible = true;
                    ajax.post('/cms/resource/list/text', null, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.dialog.selectText.data.push(data[i]);
                            }
                        }
                    });
                });
                self.editor.html.getSession().on('change', function(e) {
                    doPreview();
                });
                self.editor.css.getSession().on('change', function(e) {
                    doPreview();
                });
                self.editor.js.getSession().on('change', function(e) {
                    doPreview();
                });
                self.editor.el.on('click.preview.viewport.size', '.change-size', function(e){
                    e.stopPropagation();
                    var $viewport = self.editor.el.find('.preview-viewport');
                    self.dialog.viewportSize.width = $viewport[0].clientWidth;
                    self.dialog.viewportSize.height = $viewport[0].clientHeight;
                    self.dialog.viewportSize.visible = true;
                });
            }
        });

    };

    var destroy = function(){
        //销毁组件
        instance.editor.html.destroy();
        instance.editor.css.destroy();
        instance.editor.js.destroy();
        instance.editor.el.remove();
    };

    /**
     * @param code:{
     *     html:'',
     *     css:'',
     *     js:''
     * }
     */
    var editorShow = function(code){
        var html = code.html || '<!-- html编辑器 -->\n<!-- 使用class和id时要注意命名空间避免冲突 -->\n<div>\n    \n</div>';
        var css = code.css || '/* css编辑器 */\n/* 使用class和id时要注意命名空间避免冲突 */\n';
        var js = code.js || '//测试json数据\n//key与value必须使用双引号\n{\n    \n}';
        this.html.setValue(html);
        this.html.selection.clearSelection();
        this.css.setValue(css);
        this.css.selection.clearSelection();
        this.js.setValue(js);
        this.js.selection.clearSelection();
        this.el.show();
    };

    /**
     * @returns code:{
     *     html:'',
     *     css:'',
     *     js:''
     * }
     */
    var editorHide = function(){
        var code = {};
        this.el.hide();
        return code;
    };

    var doPreview = function(){
        var $editor = instance.editor.el;
        var $viewport = $editor.find('.preview-viewport');
        var html = instance.editor.html.getValue();
        var css = instance.editor.css.getValue();
        var reg = /("([^\\\"]*(\\.)?)*")|('([^\\\']*(\\.)?)*')|(\/{2,}.*?(\r|\n|$))|(\/\*(\n|.)*?\*\/)/g;
        var js = instance.editor.js.getValue().replace(reg, function(word) {
            // 去除注释后的文本
            return /^\/{2,}/.test(word) || /^\/\*/.test(word) ? "" : word;
        });

        var json = null;
        try{
            json = $.parseJSON(js);
            html = juicer(html).render(json);
        }catch(e){
            console.log(e);
        }
        $viewport.empty().append('<style type="text/css">'+css+'</style>').append(html);
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