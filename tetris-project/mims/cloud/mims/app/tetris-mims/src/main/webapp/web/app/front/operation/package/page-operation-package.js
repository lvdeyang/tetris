/**
 * Created by lzp on 2020/2/19.
 */
define([
    'text!' + window.APPPATH + 'front/operation/package/page-operation-package.html',
    'config',
    'context',
    'commons',
    'restfull',
    'json',
    'file',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'front/operation/package/page-operation-package.css'
], function(tpl, config, context, commons, ajax, $, File, Vue){

    var pageId = 'page-operation-package';

    var init = function(){

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el:'#' + pageId + '-wrapper',
            data:{
                menus:context.getProp('menus'),
                user:context.getProp('user'),
                groups:context.getProp('groups'),
                loading:true,
                multiDeal:false,
                table:{
                    tooltip:false,
                    rows:[],
                    page:{
                        current:1
                    }
                },
                dialog:{
                    createPackage: {
                        loading: false,
                        visible: false,
                        name: '',
                        price: 0,
                        remark: ''
                    },
                    editPackage: {
                        loading: false,
                        visible: false,
                        index: 0,
                        id: '',
                        name: '',
                        price: 0,
                        remark: ''
                    }
                }
            },
            methods:{
                loadPackageList: function() {
                    var self = this;
                    self.loading = true;
                    self.table.rows.splice(0, self.table.rows.length);
                    ajax.post('/operation/package/list/get', null, function(data, status) {
                        if (status == 200) {
                            if (data && data.length > 0) {
                                for(var i = 0; i < data.length; i++) {
                                    self.table.rows.push(data[i]);
                                }
                            }
                        }
                        self.loading = false;
                    })
                },
                handleCreate: function() {
                    var self = this;
                    self.dialog.createPackage.visible = true;
                },
                handleMultiDelete: function() {
                    var self = this;
                },
                handleRowDetail: function(item) {
                    var self = this;
                    window.location.hash = '#/page-operation-package-media/' + item.id + '/first';
                },
                handleRowDelete: function(item) {
                    var self = this;
                    var questData = {
                        id: item.id
                    };
                    self.showTip('此操作将删除该套餐，是否继续?', function(callback) {
                        ajax.post('/operation/package/remove', questData, function(data, status) {
                            if (status == 200) {
                                var index = self.table.rows.indexOf(item);
                                if (index > -1) {
                                    self.table.rows.splice(index, 1);
                                }
                            }
                            callback();
                        })
                    })
                },
                handleRowEdit: function(item) {
                    var self = this;
                    self.dialog.editPackage.id = item.id;
                    self.dialog.editPackage.name = item.name;
                    self.dialog.editPackage.price = item.price;
                    self.dialog.editPackage.remark = item.remark;
                    self.dialog.editPackage.index = self.table.rows.indexOf(item);
                    self.dialog.editPackage.visible = true;
                },
                handleCreatePackageClose: function() {
                    var self = this;
                    self.dialog.createPackage.visible = false;
                    self.dialog.createPackage.name = '';
                    self.dialog.createPackage.price = 0;
                    self.dialog.createPackage.remark = '';
                },
                handleCreatePackageSubmit: function() {
                    var self = this;
                    var questData = {
                        name: self.dialog.createPackage.name,
                        price: self.dialog.createPackage.price,
                        remark: self.dialog.createPackage.remark
                    };
                    self.dialog.createPackage.loading = true;
                    ajax.post('/operation/package/add', questData, function(data, status) {
                        if (status == 200) {
                            self.table.rows.unshift(data);
                            self.handleCreatePackageClose();
                        }
                        self.dialog.createPackage.loading = false;
                    })
                },
                handleEditPackageClose: function() {
                    var self = this;
                    self.dialog.editPackage.visible = false;
                    self.dialog.editPackage.id = '';
                    self.dialog.editPackage.name = '';
                    self.dialog.editPackage.price = 0;
                    self.dialog.editPackage.remark = '';
                    self.dialog.editPackage.index = 0;
                },
                handleEditPackageSubmit: function() {
                    var self = this;
                    self.dialog.editPackage.loading = true;
                    var questData = {
                        id: self.dialog.editPackage.id,
                        name: self.dialog.editPackage.name,
                        price: self.dialog.editPackage.price,
                        remark: self.dialog.editPackage.remark
                    };
                    ajax.post('/operation/package/edit', questData, function(data, status) {
                        if (status == 200 && data) {
                            var item = self.table.rows[self.dialog.editPackage.index];
                            item.id = data.id;
                            item.name = data.name;
                            item.price = data.price;
                            item.remark = data.remark;
                        }
                        self.dialog.editPackage.loading = false;
                        self.handleEditPackageClose();
                    })
                },
                showTip: function(text, confirmListener) {
                    var self = this;
                    var h = self.$createElement;
                    self.$msgbox({
                        title: '危险操作',
                        message: h('div', null, [
                            h('div', {class: 'el-message-box__status el-icon-warning'}, null),
                            h('div', {class: 'el-message-box__message'}, [
                                h('p', null, [text])
                            ])
                        ]),
                        type: 'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose: function (action, instance, done) {
                            instance.confirmButtonLoading = true;
                            if (action === 'confirm') {
                                confirmListener(function() {
                                    instance.confirmButtonLoading = false;
                                    done();
                                })
                            } else {
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function () {
                    });
                }
            },
            created:function(){
                var self = this;
                self.loadPackageList();
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