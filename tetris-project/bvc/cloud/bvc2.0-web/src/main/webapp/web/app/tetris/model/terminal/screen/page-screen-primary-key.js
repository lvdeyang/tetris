/**
 * Created by lvdeyang on 2020/6/19.
 */
define([
    'text!' + window.APPPATH + 'tetris/model/terminal/screen/page-screen-primary-key.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'tetris/model/terminal/screen/page-screen-primary-key.css'
], function(tpl, config, ajax, context, commons, Vue) {

    var pageId = 'page-screen-primary-key';

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
                table: {
                    rows: []
                },
                dialog: {
                    addPrimaryKey: {
                        visible: false,
                        loading: false,
                        name:'',
                        primaryKey: ''
                    },
                    editPrimaryKey: {
                        visible: false,
                        loading: false,
                        id: '',
                        name:'',
                        primaryKey: ''
                    }
                }
            },
            computed: {},
            watch: {},
            methods: {
                rowKey: function (row) {
                    return 'primaryKey-' + row.uuid;
                },
                handleCreate: function () {
                    var self = this;
                    self.dialog.addPrimaryKey.visible = true;
                },
                handleAutoCreate: function(){
                    var self = this;
                    ajax.post('/tetris/bvc/model/terminal/screen/primary/key/auto/add', null, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.table.rows.push(data[i]);
                            }
                        }
                    });
                },
                handleAddPrimaryKeyClose: function () {
                    var self = this;
                    self.dialog.addPrimaryKey.name = '';
                    self.dialog.addPrimaryKey.primaryKey = '';
                    self.dialog.addPrimaryKey.loading = false;
                    self.dialog.addPrimaryKey.visible = false;
                },
                handleAddPrimaryKeySubmit: function () {
                    var self = this;
                    self.dialog.addPrimaryKey.loading = true;
                    ajax.post('/tetris/bvc/model/terminal/screen/primary/key/add', {
                        name:self.dialog.addPrimaryKey.name,
                        primaryKey: self.dialog.addPrimaryKey.primaryKey
                    }, function (data, status) {
                        self.dialog.addPrimaryKey.loading = false;
                        if (status !== 200) return;
                        self.table.rows.push(data);
                        self.table.total += 1;
                        self.handleAddPrimaryKeyClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleDelete: function () {

                },
                handleRowEdit: function (scope) {
                    var self = this;
                    var row = scope.row;
                    self.dialog.editPrimaryKey.id = row.id;
                    self.dialog.editPrimaryKey.name = row.name;
                    self.dialog.editPrimaryKey.primaryKey = row.screenPrimaryKey;
                    self.dialog.editPrimaryKey.visible = true;
                },
                handleEditPrimaryKeyClose: function () {
                    var self = this;
                    self.dialog.editPrimaryKey.id = '';
                    self.dialog.editPrimaryKey.name = '';
                    self.dialog.editPrimaryKey.primaryKey = '';
                    self.dialog.editPrimaryKey.loading = false;
                    self.dialog.editPrimaryKey.visible = false;
                },
                handleEditPrimaryKeySubmit: function () {
                    var self = this;
                    self.dialog.editPrimaryKey.loading = true;
                    ajax.post('/tetris/bvc/model/terminal/screen/primary/key/edit', {
                        id: self.dialog.editPrimaryKey.id,
                        name:self.dialog.editPrimaryKey.name,
                        primaryKey: self.dialog.editPrimaryKey.primaryKey
                    }, function (data, status) {
                        self.dialog.editPrimaryKey.loading = false;
                        if (status !== 200) return;
                        for (var i = 0; i < self.table.rows.length; i++) {
                            if (self.table.rows[i].id == data.id) {
                                self.table.rows.splice(i, 1, data);
                            }
                        }
                        self.handleEditPrimaryKeyClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
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
                                h('p', null, ['此操作将永久删除该屏幕，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type: 'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose: function (action, instance, done) {
                            instance.confirmButtonLoading = true;
                            if (action === 'confirm') {
                                ajax.post('/tetris/bvc/model/terminal/screen/primary/key/delete', {
                                    id: row.id
                                }, function (data, status) {
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if (status !== 200) return;
                                    for (var i = 0; i < self.table.rows.length; i++) {
                                        if (self.table.rows[i].id == row.id) {
                                            self.table.rows.splice(i, 1);
                                        }
                                    }
                                }, null, ajax.TOTAL_CATCH_CODE);
                            } else {
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function () {
                    });
                },
                load: function () {
                    var self = this;
                    self.table.rows.splice(0, self.table.rows.length);
                    ajax.post('/tetris/bvc/model/terminal/screen/primary/key/load', null, function (data, status) {
                        if (data && data.length > 0) {
                            for (var i = 0; i < data.length; i++) {
                                self.table.rows.push(data[i]);
                            }
                        }
                    });
                }
            },
            created: function () {
                var self = this;
                self.load();
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