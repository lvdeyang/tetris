/**
 * Created by ldy on 2018/12/28 0028.
 */
define([
    'text!' + window.APPPATH + 'cms/region/page-cms-region.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'region-dialog',
    'css!' + window.APPPATH + 'cms/region/page-cms-region.css'
], function(tpl, config, $, ajax, context, commons, Vue){

    var pageId = 'page-cms-region';

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
                        children: 'subRegions'
                    },
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
                        ip:''
                    },
                    selectTag:{
                        visible:false,
                        template:'',
                        tree:{
                            props:{
                                label: 'name',
                                children: 'subRegions'
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
                loadRegionTree:function(){
                    var self = this;
                    self.tree.data.splice(0, self.tree.data.length);
                    ajax.post('/cms/region/list/tree', null, function(data){
                        self.tree.data.push({
                            id:-1,
                            uuid:'-1',
                            name:'地区列表',
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
                    ajax.post('/cms/region/move', {
                        sourceId:dragNode.data.id,
                        targetId:dropNode.data.id
                    }, function(data, status){
                        self.loading.tree = false;
                        if(status !== 200) return;
                        self.loadRegionTree();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                addRootTreeNode:function(){
                    var self = this;
                    self.loading.tree = true;
                    self.loading.addRoot = true;
                    ajax.post('/cms/region/add/root', null, function(data, status){
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
                    ajax.post('/cms/region/top/' + nodeData.id, null, function(data, status){
                        self.loading.tree = false;
                        if(status !== 200) return;
                        if(data){
                            self.loadRegionTree();
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                treeNodeEdit:function(node, data){
                    var self = this;
                    self.dialog.editTag.data = data;
                    self.dialog.editTag.name = data.name;
                    self.dialog.editTag.code = data.code;
                    self.dialog.editTag.ip = data.ip;
                    self.dialog.editTag.visible = true;
                },
                treeNodeAppend:function(parentNode, parent){
                    var self = this;
                    self.loading.tree = true;
                    ajax.post('/cms/region/append', {
                        parentId:parent.id
                    }, function(data, status){
                        self.loading.tree = false;
                        if(status !== 200) return;
                        if(!parent.subRegions){
                            Vue.set(parent, 'subRegions', []);
                        }
                        parent.subRegions.push(data);
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
                                ajax.post('/cms/region/remove/' + nodeData.id, null, function(data, status){
                                    self.loading.tree = false;
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    self.$refs.regionTree.remove(nodeData);
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
                    self.dialog.editTag.ip = '';
                    self.dialog.editTag.visible = false;
                },
                handleEditTagCommit:function(){
                    var self = this;
                    self.loading.tree = true;
                    ajax.post('/cms/region/update/' + self.dialog.editTag.data.id, {
                        name:self.dialog.editTag.name,
                        code:self.dialog.editTag.code,
                        ip:self.dialog.editTag.ip
                    }, function(data, status){
                        self.loading.tree = false;
                        if(status !== 200) return;
                        self.dialog.editTag.data.name = data.name;
                        self.dialog.editTag.data.code = data.code;
                        self.dialog.editTag.data.ip = data.ip;
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
                        self.$refs.regionTree.setCurrentKey(data.uuid);
                    });
                },
            },
            created:function(){
                var self = this;
                self.loadRegionTree();
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
