define([
    'text!' + window.APPPATH + 'subordinate/role/page-subordinate-role.html',
    'config',
    'restfull',
    'jquery',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'mi-sub-title',
    'mi-user-dialog',
    'css!' + window.APPPATH + 'subordinate/role/page-subordinate-role.css'
], function (tpl, config, ajax, $, context, commons, Vue) {

    var pageId = 'page-subordinate-role';

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
                activeId: window.BASEPATH + 'index#/page-subordinate-role',

                company: {
                    id: "",
                    name: ""
                },
                tree: {
                    props: {
                        label: 'name',
                        children: 'sub',
                        isLeaf: 'isLeaf'
                    },
                    expandOnClickNode: false,
                    data: [],
                    current: ''
                },
                loading: {
                    roleGroup: false,
                    addRoot: false
                },
                table: {
                    user: {
                        rows: [],
                        pageSize: 10,
                        pageSizes: [10, 20, 50, 100],
                        currentPage: 0,
                        total: 0
                    },
                    folder: {
                        rows: [],
                        pageSize: 50,
                        pageSizes: [10, 20, 50, 100],
                        currentPage: 0,
                        total: 0
                    }
                },
                dialog: {
                    createRoleGroup: {
                        visible: false,
                        loading: false,
                        name: ''
                    },
                    editRole: {
                        visible: false,
                        loading: false,
                        id: '',
                        name: ''
                    }
                }
            },
            methods: {
                load: function () {
                    var self = this;
                    self.loading.roleGroup = true;
                    ajax.post('/subordinate/role/list', {
                        companyId: self.company.id
                    }, function (data, status) {
                        self.loading.roleGroup = false;
                        if (status !== 200) return;
                        if (data && data.length > 0) {
                            for (var i = 0; i < data.length; i++) {
                                data[i].isLeaf = true;
                                self.tree.data.push(data[i]);
                            }
                            self.currentNode(data[0]);
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                loadCompany: function () {
                    var self = this;
                    ajax.post('/company/subordinate', {}, function (data, status) {
                        if (status == 200 && data != null) {
                            self.company.id = data.id;
                            self.company.name = data.name;
                            self.load();
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE)
                },

                handleCreateRoleGroup: function () {
                    var self = this;
                    self.dialog.createRoleGroup.visible = true;
                },
                handleCreateRoleGroupClose: function () {
                    var self = this;
                    self.dialog.createRoleGroup.visible = false;
                    self.dialog.createRoleGroup.name = '';
                    self.dialog.createRoleGroup.loading = false;
                },
                handleCreateRoleGroupSubmit: function () {
                    var self = this;
                    self.dialog.createRoleGroup.loading = true;
                    ajax.post('/subordinate/role/add', {
                        roleName: self.dialog.createRoleGroup.name,
                        companyId: self.company.id
                    }, function (data, status) {
                        self.dialog.createRoleGroup.loading = false;
                        if (status !== 200) return;
                        self.tree.data.splice(0, 0, data);
                        self.handleCreateRoleGroupClose();
                        self.currentNode(data);
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                handleEditRole: function (node, data) {
                    var self = this;
                    self.dialog.editRole.id = data.id;
                    self.dialog.editRole.name = data.name;
                    self.dialog.editRole.visible = true;
                },
                handleEditRoleClose: function () {
                    var self = this;
                    self.dialog.editRole.id = '';
                    self.dialog.editRole.name = '';
                    self.dialog.editRole.visible = false;
                    self.dialog.editRole.loading = false;
                },
                handleEditRoleSubmit: function () {
                    var self = this;
                    self.dialog.editRole.loading = true;
                    ajax.post('/subordinate/role/edit', {
                        roleId: self.dialog.editRole.id,
                        roleName: self.dialog.editRole.name
                    }, function (data, status) {
                        self.dialog.editRole.loading = false;
                        if (status !== 200) return;
                        for (var i = 0; i < self.tree.data.length; i++) {
                            if (self.tree.data[i].id === data.id) {
                                self.tree.data[i].name = data.name;
                                break;
                            }
                        }
                        self.handleEditRoleClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE)
                },
                handleDeleteRole: function (node, data) {
                    var self = this;
                    var h = self.$createElement;
                    self.$msgbox({
                        title: '危险操作',
                        message: h('div', null, [
                            h('div', {class: 'el-message-box__status el-icon-warning'}, null),
                            h('div', {class: 'el-message-box__message'}, [
                                h('p', null, ['此操作将永久删除角色组以及组内所有角色，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type: 'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose: function (action, instance, done) {
                            instance.confirmButtonLoading = true;
                            if (action === 'confirm') {
                                ajax.post('/subordinate/role/remove', {
                                    roleId: data.id
                                }, function (response, status) {
                                    instance.confirmButtonLoading = false;
                                    if (status !== 200) return;
                                    for (var i = 0; i < self.tree.data.length; i++) {
                                        if (self.tree.data[i].id === data.id) {
                                            self.tree.data.splice(i, 1);
                                            break;
                                        }
                                    }
                                    done();
                                    self.currentNode(self.tree.data[0]);
                                }, null, ajax.NO_ERROR_CATCH_CODE);
                            } else {
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function () {
                    });
                },
                currentTreeNodeChange: function (data) {
                    var self = this;
                    self.tree.current = data;
                    self.currentNode(data);
                },

                userRowKey: function (row) {
                    return 'user-' + row.uuid;
                },
                currentNode: function (data) {
                    var self = this;
                    if (!data) {
                        self.tree.current = '';
                        return;
                    }
                    self.$nextTick(function () {
                        self.$refs.roleGroupTree.setCurrentKey(data.uuid);
                    });
                    self.tree.current = data;
                    self.loadUsers(data.id, 1);
                },
                loadUsers: function (id, currentPage) {
                    var self = this;
                    self.table.user.rows.splice(0, self.table.user.rows.length);
                    ajax.post('/subordinate/role/member/list', {
                        roleId: id,
                        currentPage: currentPage,
                        pageSize: self.table.user.pageSize
                    }, function (data) {
                        var total = data.total;
                        var rows = data.rows;
                        if (rows && rows.length > 0) {
                            for (var i = 0; i < rows.length; i++) {
                                self.table.user.rows.push(rows[i]);
                            }
                            self.table.user.total = total;
                        }
                        self.table.user.currentPage = currentPage;
                    });
                },
                handleRowDelete: function (scope) {
                    var self = this;
                    var row = scope.row;
                    var h = self.$createElement;
                    self.$msgbox({
                        title: '危险操作',
                        message: h('div', null, [
                            h('div', {class: 'el-message-box__status el-icon-warning'}, null),
                            h('div', {class: 'el-message-box__message'}, [
                                h('p', null, ['此操作将解除绑定该角色，是否继续?'])
                            ])
                        ]),
                        type: 'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose: function (action, instance, done) {
                            instance.confirmButtonLoading = true;
                            if (action === 'confirm') {
                                ajax.post('/subordinate/role/member/remove', {userId: row.id}, function (data, status) {
                                    instance.confirmButtonLoading = false;
                                    if (status !== 200) return;
                                    for (var i = 0; i < self.table.user.rows.length; i++) {
                                        if (self.table.user.rows[i].id === row.id) {
                                            self.table.user.rows.splice(i, 1);
                                            break;
                                        }
                                    }
                                    done();
                                }, null, ajax.NO_ERROR_CATCH_CODE);
                            } else {
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function () {
                    });
                },
                handleCurrentChange: function (currentPage) {
                    var self = this;
                    self.load(currentPage);
                },
                handleAddUser: function () {
                    var self = this;
                    var rows = self.table.user.rows;
                    var exceptIds = [];
                    for (var i = 0; i < rows.length; i++) {
                        exceptIds.push(rows[i].id);
                    }
                    self.$refs.userDialog.open('/subordinate/role/member/with/except/list' + "/" + this.company.id, exceptIds);
                },
                selectedUsers: function (users, buffer, startLoading, endLoading, done) {
                    var self = this;
                    var userIds = [];
                    for (var i = 0; i < users.length; i++) {
                        userIds.push(users[i].id);
                    }
                    startLoading();
                    ajax.post('/subordinate/role/member/bind', {
                        roleId: self.tree.current.id,
                        userIds: $.toJSON(userIds)
                    }, function (data, status) {
                        endLoading();
                        if (status !== 200) return;
                        if (data && data.length > 0) {
                            for (var i = 0; i < data.length; i++) {
                                self.table.user.rows.push(data[i]);
                            }
                        }
                        endLoading();
                        done();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                handleRemoveUser: function () {

                }
            },
            created: function () {
                var self = this;
                self.loadCompany();
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