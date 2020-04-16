/**
 * Created by sms on 2019/12/19.
 */
define([
    'text!' + window.APPPATH + 'data-warehouse/page-data-warehouse.html',
    window.APPPATH + 'data-warehouse/page-data-warehouse.i18n',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'data-warehouse/page-data-warehouse.css'
], function (tpl, i18n, config, $, ajax, context, commons, Vue) {

    var locale = context.getProp('locale');
    var i18n = !locale?i18n.default:i18n[locale]?i18n[locale]:i18n.default;

    var pageId = 'page-data-warehouse';

    var init = function () {
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el: '#' + pageId + '-wrapper',
            data: {
                menus: context.getProp('menus'),
                user: context.getProp('user'),
                groups: context.getProp('groups'),
                i18n:i18n,
                activeId: window.BASEPATH + 'index#/page-data-warehouse',
                token: '',
                tree: {
                    loading: false,
                    props: {
                        label: 'name',
                        children: 'sub',
                        isLeaf: 'isLeaf'
                    },
                    data: [],
                    current: ''
                },
                table: {
                    rows: []
                },
                dialog: {
                    addProject: {
                        loading: false,
                        visible: false,
                        name: '',
                        path: '',
                        remark: ''
                    },
                    addData: {
                        loading: false,
                        visible: false,
                        name: '',
                        remark: ''
                    }
                }
            },
            methods: {
                load: function () {
                    var self = this;
                    if (self.tree.data) {
                        self.tree.data.splice(0, self.tree.data.length);
                    }
                    ajax.post('/data/warehouse/do/auth', null, function(data, status) {
                        if (status == 200 && data) {
                            self.token = data;
                            self.questProjectList();
                        }
                    })
                },
                questProjectList: function () {
                    var self = this;
                    self.tree.data.splice(0, self.tree.data.length);
                    ajax.post('/data/warehouse/project/list/get', null, function(data, status) {
                        if (status == 200) {
                            if (data) {
                                self.tree.data = self.tree.data.concat(data);
                            }
                        }
                    })
                },

                rowKey:function(row){
                    return 'user-' + row.id;
                },
                currentTreeNodeChange: function (scope, node) {
                    var self = this;
                    if (self.tree.current && self.tree.current.id == node.data.id) {
                        return;
                    }
                    self.tree.current = node.data;
                    self.table.rows.splice(0, self.table.rows.length);
                    var requestData = {
                        projectId: node.data.id
                    };
                    ajax.post('/data/warehouse/file/list/get', requestData, function(data, status) {
                        if (status == 200 && data) {
                            self.table.rows = self.table.rows.concat(data);
                        }
                    })
                },
                treeNodeDelete: function (node, data) {
                    var self = this;
                    var h = self.$createElement;
                    self.$msgbox({
                        title: '危险操作',
                        message: h('div', null, [
                            h('div', {class: 'el-message-box__status el-icon-warning'}, null),
                            h('div', {class: 'el-message-box__message'}, [
                                h('p', null, ['删除项目操作，项目下文件也都删除，是否继续?'])
                            ])
                        ]),
                        type: 'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose: function (action, instance, done) {
                            instance.confirmButtonLoading = true;
                            if (action === 'confirm') {
                                var questData = {
                                    projectId: data.id
                                };
                                ajax.post('/data/warehouse/project/delete', questData, function (deletedata, status) {
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if (status != 200) return;
                                    self.$message({
                                        message: '删除成功',
                                        type: 'success'
                                    });
                                    for (var i = 0; i < self.tree.data.length; i++) {
                                        if (self.tree.data[i].id == data.id) {
                                            self.tree.data.splice(i, 1);
                                            break;
                                        }
                                    }
                                }, null, ajax.NO_ERROR_CATCH_CODE)
                            } else {
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function () {
                    });
                },
                handleAddProject: function () {
                    var self = this;
                    self.dialog.addProject.visible = true;
                },
                handleAddProjectClose: function () {
                    var self = this;
                    self.dialog.addProject.visible = false;
                    self.dialog.addProject.name = '';
                    self.dialog.addProject.path = '';
                    self.dialog.addProject.remark = '';
                },
                handleAddProjectCommit: function () {
                    var self = this;
                    self.dialog.addProject.loading = true;
                    var requestData = {
                        name: self.dialog.addProject.name,
                        path: self.dialog.addProject.path,
                        remark: self.dialog.remark
                    };
                    ajax.post('/data/warehouse/project/create', requestData, function(data, status) {
                        if (status == 200 && data) {
                            self.tree.data.push(data);
                            self.handleAddProjectClose();
                        }
                        self.dialog.addProject.loading = false;
                    })
                },

                handleAddData: function () {
                    var self = this;
                    self.dialog.addData.visible = true;
                },
                handleDownload: function (scope) {
                    var self = this;
                    var row = scope.row;
                    var h = self.$createElement;
                    self.$msgbox({
                        title: '危险操作',
                        message: h('div', null, [
                            h('div', {class: 'el-message-box__status el-icon-warning'}, null),
                            h('div', {class: 'el-message-box__message'}, [
                                h('p', null, ['下载备份，将覆盖之前下载的备份文件，是否继续?'])
                            ])
                        ]),
                        type: 'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose: function (action, instance, done) {
                            instance.confirmButtonLoading = true;
                            if (action === 'confirm') {
                                var questData = {
                                    projectId: self.tree.current.id,
                                    filePath: row.path
                                };
                                ajax.post('/data/warehouse/file/download', questData, function (data, status) {
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if (status != 200) return;
                                    self.$message({
                                        message: '下载成功',
                                        type: 'success'
                                    });
                                }, null, ajax.NO_ERROR_CATCH_CODE)
                            } else {
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function () {
                    });
                },
                handleDeleteData: function (scope) {
                    var self = this;
                    var row = scope.row;
                    var h = self.$createElement;
                    self.$msgbox({
                        title: '危险操作',
                        message: h('div', null, [
                            h('div', {class: 'el-message-box__status el-icon-warning'}, null),
                            h('div', {class: 'el-message-box__message'}, [
                                h('p', null, ['删除备份操作，是否继续?'])
                            ])
                        ]),
                        type: 'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose: function (action, instance, done) {
                            instance.confirmButtonLoading = true;
                            if (action === 'confirm') {
                                var questData = {
                                    projectId: self.tree.current.id,
                                    filePath: row.path
                                };
                                ajax.post('/data/warehouse/file/delete', questData, function (data, status) {
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if (status != 200) return;
                                    self.$message({
                                        message: '删除成功',
                                        type: 'success'
                                    });
                                    for (var i = 0; i < self.table.rows.length; i++) {
                                        if (self.table.rows[i].id == row.id) {
                                            self.table.rows.splice(i, 1);
                                            break;
                                        }
                                    }
                                }, null, ajax.NO_ERROR_CATCH_CODE)
                            } else {
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function () {
                    });
                },
                handleAddDataClose: function () {
                    var self = this;
                    self.dialog.addData.visible = false;
                    self.dialog.addData.name = '';
                    self.dialog.addData.remark = '';
                },
                handleAddDataCommit: function () {
                    var self = this;
                    self.dialog.addData.loading = true;
                    var requestData = {
                        projectId: self.tree.current.id,
                        fileName: self.dialog.addData.name,
                        remark: self.dialog.addData.remark
                    };
                    ajax.post('/data/warehouse/file/upload', requestData, function(data, status) {
                        if (status == 200 && data) {
                            self.table.rows.push(data);
                            self.handleAddDataClose();
                        }
                        self.dialog.addData.loading = false;
                    })
                }
            },
            created: function () {
                var self = this;
                self.load();
            }
        })
    };

    var destroy = function () {

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