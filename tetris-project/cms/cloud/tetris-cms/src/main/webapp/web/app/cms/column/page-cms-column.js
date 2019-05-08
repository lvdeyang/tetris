/**
 * Created by lvdeyang on 2018/12/26 0026.
 */
define([
    'text!' + window.APPPATH + 'cms/column/page-cms-column.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'article-dialog',
    'css!' + window.APPPATH + 'cms/column/page-cms-column.css'
], function(tpl, config, $, ajax, context, commons, Vue){

    var pageId = 'page-cms-column';

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
                        children: 'subColumns'
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
                        code:'',
                        remark:''
                    },
                    selectTag:{
                        visible:false,
                        template:'',
                        tree:{
                            props:{
                                label: 'name',
                                children: 'subColumns'
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
                mouseEnter:function(){
                    var self = this;
                    self.showButton30=false;
                },
                mouseLeave: function(){
                    var self = this;
                    self.showButton30=true;
                },
                loadTagTree:function(){
                    var self = this;
                    self.tree.data.splice(0, self.tree.data.length);
                    ajax.post('/cms/column/list/tree', null, function(data){
                        self.tree.data.push({
                            id:-1,
                            uuid:'-1',
                            name:'栏目列表',
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
                treeNodeAllowDrag:function(node){
                    return node.data.id !== -1;
                },
                treeNodeAllowDrop:function(dragNode, dropNode, type) {
                    return type === 'inner' && dropNode.data.id !== -1;
                },
                treeNodeDrop:function(dragNode, dropNode, type){
                    var self = this;
                    self.loading.tree = true;
                    ajax.post('/cms/column/move', {
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
                    ajax.post('/cms/column/add/root', null, function(data, status){
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
                    ajax.post('/cms/column/top/' + nodeData.id, null, function(data, status){
                        self.loading.tree = false;
                        if(status !== 200) return;
                        if(data){
                            self.loadTagTree();
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                treeNodeUp: function(node, nodeData){
                    var self = this;
                    var parentData = node.data;
                    ajax.post('/cms/column/up', {
                        columnId: parentData.id
                    }, function(data, status){
                        if(status !== 200) return;
                        self.tree.data.splice(0, self.tree.data.length);
                        self.tree.data.push({
                            id:-1,
                            uuid:'-1',
                            name:'栏目列表',
                            icon:'icon-tag',
                            style:'font-size:15px; position:relative; top:1px; margin-right:1px;'
                        });
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.tree.data.push(data[i]);
                            }
                        }
                        //self.currentNode(self.tree.data[0]);
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                treeNodeEdit:function(node, data){
                    var self = this;
                    self.dialog.editTag.data = data;
                    self.dialog.editTag.name = data.name;
                    self.dialog.editTag.code = data.code;
                    self.dialog.editTag.remark = data.remark;
                    self.dialog.editTag.visible = true;
                },
                treeNodeAppend:function(parentNode, parent){
                    var self = this;
                    self.loading.tree = true;
                    ajax.post('/cms/column/append', {
                        parentId:parent.id
                    }, function(data, status){
                        self.loading.tree = false;
                        if(status !== 200) return;
                        if(!parent.subColumns){
                            Vue.set(parent, 'subColumns', []);
                        }
                        parent.subColumns.push(data);
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
                                ajax.post('/cms/column/remove/' + nodeData.id, null, function(data, status){
                                    self.loading.tree = false;
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    self.$refs.columnTree.remove(nodeData);
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
                    self.dialog.editTag.code = '';
                    self.dialog.editTag.remark = '';
                    self.dialog.editTag.visible = false;
                },
                handleEditTagCommit:function(){
                    var self = this;
                    self.loading.tree = true;
                    ajax.post('/cms/column/update/' + self.dialog.editTag.data.id, {
                        name:self.dialog.editTag.name,
                        code:self.dialog.editTag.code,
                        remark:self.dialog.editTag.remark
                    }, function(data, status){
                        self.loading.tree = false;
                        if(status !== 200) return;
                        self.dialog.editTag.data.name = data.name;
                        self.dialog.editTag.data.code = data.code;
                        self.dialog.editTag.data.remark = data.remark;
                        self.handleEditTagClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                currentNode:function(data){
                    var self = this;
                    if(!data || data.id == -1){
                        self.tree.current = '';
                        return;
                    }
                    self.tree.current = data;
                    self.$nextTick(function(){
                        self.$refs.columnTree.setCurrentKey(data.uuid);
                    });
                    self.loadArticles(data.id);
                },
                loadArticles:function(columnId){
                    var self = this;
                    self.table.data.splice(0, self.table.data.length);
                    ajax.post('/cms/column/relation/article/load', {
                        columnId:columnId
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.table.data.push(data[i]);
                            }
                        }
                    });
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
                                h('p', null, ['此操作将下架该文章，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/cms/column/relation/article/remove/' + row.id, null, function(data, status){
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
                rowCommand: function (scope) {
                    var self = this;
                    var row = scope.row;
                    ajax.post('/cms/column/relation/article/command/' + row.id, null, function(data, status){
                        if(status !== 200) return;
                        for(var i=0; i<self.table.data.length; i++){
                            if(self.table.data[i].id === data.id){
                                self.table.data.splice(i, 1, data);
                                break;
                            }
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                rowNotCommand: function (scope) {
                    var self = this;
                    var row = scope.row;
                    ajax.post('/cms/column/relation/article/not/command/' + row.id, null, function(data, status){
                        if(status !== 200) return;
                        for(var i=0; i<self.table.data.length; i++){
                            if(self.table.data[i].id === data.id){
                                self.table.data.splice(i, 1, data);
                                break;
                            }
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                rowInform: function (scope) {
                    var self = this;
                    var row = scope.row;
                    ajax.post('/cms/column/relation/article/inform', {
                        columnId: row.columnId,
                        articleId: row.articleId
                    }, function(data, status){

                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                rowTop: function(scope){
                    var self = this;
                    var row = scope.row;
                    ajax.post('/cms/column/relation/article/top', {
                        columnId: row.columnId,
                        articleId: row.articleId
                    }, function(data, status){
                        if(status !== 200) return;
                        if(data && data.length>0){
                            self.table.data.splice(0, self.table.data.length);
                            for(var i=0; i<data.length; i++){
                                self.table.data.push(data[i]);
                            }
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                rowBottom: function(scope){
                    var self = this;
                    var row = scope.row;
                    ajax.post('/cms/column/relation/article/bottom', {
                        columnId: row.columnId,
                        articleId: row.articleId
                    }, function(data, status){
                        if(status !== 200) return;
                        if(data && data.length>0){
                            self.table.data.splice(0, self.table.data.length);
                            for(var i=0; i<data.length; i++){
                                self.table.data.push(data[i]);
                            }
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                rowUp: function(scope){
                    var self = this;
                    var row = scope.row;
                    ajax.post('/cms/column/relation/article/up', {
                        columnId: row.columnId,
                        articleId: row.articleId
                    }, function(data, status){
                        if(status !== 200) return;
                        if(data && data.length>0){
                            self.table.data.splice(0, self.table.data.length);
                            for(var i=0; i<data.length; i++){
                                self.table.data.push(data[i]);
                            }
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                rowDown: function(scope){
                    var self = this;
                    var row = scope.row;
                    ajax.post('/cms/column/relation/article/down', {
                        columnId: row.columnId,
                        articleId: row.articleId
                    }, function(data, status){
                        if(status !== 200) return;
                        if(data && data.length>0){
                            self.table.data.splice(0, self.table.data.length);
                            for(var i=0; i<data.length; i++){
                                self.table.data.push(data[i]);
                            }
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                bindArticles:function(){
                    var self = this;
                    var rows = self.table.data;
                    var exceptIds = [];
                    for(var i=0; i<rows.length; i++){
                        exceptIds.push(rows[i].articleId);
                    }
                    self.$refs.articleDialog.open('/cms/article/list/with/except', exceptIds);
                },
                selectedArticles:function(articles, buff, startLoading, endLoading){
                    var self = this;
                    var articleIds = [];
                    for(var i=0; i<articles.length; i++){
                        articleIds.push(articles[i].id);
                    }
                    startLoading();
                    ajax.post('/cms/column/relation/article/bind', {
                        columnId:self.tree.current.id,
                        articleIds: $.toJSON(articleIds)
                    }, function(data, status){
                        endLoading();
                        if(status !== 200) return;
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.table.data.push(data[i]);
                            }
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
            },
            created:function(){
                var self = this;
                self.loadTagTree();
            },
            mounted:function(){

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