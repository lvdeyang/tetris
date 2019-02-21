/**
 * Created by lvdeyang on 2018/12/24 0024.
 */
define([
    'text!' + window.APPPATH + 'cms/template/page-cms-template.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'editor',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'cms/template/page-cms-template.css'
], function(tpl, config, $, ajax, context, commons, editor, Vue){

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
                    }
                },
                loading:{
                    tree:false,
                    addRoot:false
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
                    self.$refs.templateEditor.show(row);
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
                }
            },
            created:function(){
                var self = this;
                self.loadTagTree();
                self.loadTemplateTypes();
            },
            mounted:function(){
                var self = this;

            }
        });

    };

    var destroy = function(){
        //销毁组件
        instance.$refs.templateEditor.destroy();
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