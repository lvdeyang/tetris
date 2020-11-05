/**
 * Created by lzp on 2019/7/25.
 */
define([
    'text!' + window.APPPATH + 'front/media/tag/page-media-tag.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'front/media/tag/page-media-tag.css'
], function (tpl, config, $, ajax, context, commons, Vue) {

    var pageId = 'page-media-tag';

    var init = function () {

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el: '#' + pageId + '-wrapper',
            data: {
                menus: context.getProp('menus'),
                user: context.getProp('user'),
                groups: context.getProp('groups'),
                loading: {
                    tree: false,
                    addRoot: false
                },
                tree: {
                    data: [],
                    props: {
                        label: "name",
                        children: "subColumns"
                    },
                    current: {}
                },
                dialog: {
                    addRoot: {
                        visible: false,
                        loading: false,
                        name: "",
                        remark: "",
                        hotCount:"",
                        data: {}
                    },
                    editTag: {
                        visible: false,
                        loading: false,
                        name: "",
                        remark: "",
                        hotCount:"",
                        data: {}
                    }
                }
            },
            computed: {},
            watch: {},
            methods: {
                getTagList: function () {
                    var self = this;
                    self.loading.tree = true;
                    self.tree.data.splice(0, self.tree.data.length);
                    self.tree.data.push({
                        id: -1,
                        name: "标签列表",
                        remark: "",
                        hotCount:"",
                        parentId: null
                    });
                    ajax.post('/media/tag/list/get', null, function (data, status) {
                        if (status == 200 && data) {
                            self.tree.data = self.tree.data.concat(data);
                            self.loading.tree = false;
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },

                addRootTreeNode: function () {
                    var self = this;
                    self.dialog.addRoot.data = {};
                    self.dialog.addRoot.visible = true;
                },
                handleAddRootClose: function () {
                    var self = this;
                    self.dialog.addRoot.visible = false;
                    self.dialog.addRoot.name = "";
                    self.dialog.addRoot.remark = "";
                    self.dialog.addRoot.hotCount="";
                    self.dialog.addRoot.data = {};
                },
                handleAddRootCommit: function () {
                    var self = this;
                    self.loading.addRoot = true;
                    var requestData = {
                        name: self.dialog.addRoot.name,
                        remark: self.dialog.addRoot.remark,
                        hotCount:self.dialog.addRoot.hotCount
                    };
                    var parentId = self.dialog.addRoot.data.id;
                    if (parentId) requestData.parentId = parentId;
                    ajax.post('/media/tag/add', requestData, function (data, status) {
                        if (status == 200) {
                            if(parentId) {
                                self.dialog.addRoot.data.subColumns.push(data);
                            }else{
                                self.tree.data.push(data);
                            }
                            self.handleAddRootClose();
                        }
                        self.loading.addRoot = false;
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },

                currentTreeNodeChange: function (data) {
                    var self = this;
                    self.currentNode(data);
                },
                currentNode: function (data) {

                },

                treeNodeEdit: function (node, data) {
                    var self = this;
                    var names=data.name.split("(");
                    names.pop();
                    var name=names.join('(');
                    self.dialog.editTag.name = name;
                    self.dialog.editTag.remark = data.remark;
                    self.dialog.editTag.hotCount=data.hotCount;
                    self.dialog.editTag.visible = true;
                    self.dialog.editTag.data = data;
                },
                treeNodeEditClose: function () {
                    var self = this;
                    self.dialog.editTag.visible = false;
                    self.dialog.editTag.name = "";
                    self.dialog.editTag.remark = "";
                    self.dialog.editTag.hotCount="";
                    self.dialog.editTag.data = {};
                },
                treeNodeEditCommit: function () {
                    var self = this;
                    self.dialog.editTag.loading = true;
                    var requestData = {
                        id: self.dialog.editTag.data.id,
                        name: self.dialog.editTag.name,
                        remark: self.dialog.editTag.remark,
                        hotCount:self.dialog.editTag.hotCount
                    };
                    ajax.post('/media/tag/edit', requestData, function (data, status) {
                        if (status == 200) {
                            self.dialog.editTag.data.name = data.name;
                            self.dialog.editTag.data.remark = data.remark;
                            self.dialog.editTag.data.hotCount=data.hotCount;
                            self.treeNodeEditClose();
                        }
                        self.dialog.editTag.loading = false;
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },

                treeNodeAppend: function (node, data) {
                    var self = this;
                    self.dialog.addRoot.data = data;
                    self.dialog.addRoot.visible = true;
                },

                treeNodeDelete: function (node, data) {
                    var self = this;
                    self.loading.tree = true;
                    var requestData = {
                        id: data.id
                    };
                    ajax.post('/media/tag/remove', requestData, function (data, status) {
                        if (status == 200) {
                            self.$refs.tagTree.remove(node);
                        }
                        if (self.tree.data.length > 1) {
                            self.$refs.tagTree.setCurrentKey(self.tree.data[1].uuid);
                        }
                        self.loading.tree = false;
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                }
            },
            created: function () {
                var self = this;
                self.getTagList();
            },
            mounted: function () {

            }
        });

    };

    var destroy = function () {

    };

    var groupList = {
        path: '/' + pageId,
        component: {
            template: '<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init: init,
        destroy: destroy
    };

    return groupList;
});