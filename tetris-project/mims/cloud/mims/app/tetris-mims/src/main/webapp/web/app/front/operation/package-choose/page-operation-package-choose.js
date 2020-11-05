/**
 * Created by sms on 2020/3/26.
 */
define([
    'text!' + window.APPPATH + 'front/operation/package-choose/page-operation-package-choose.html',
    'config',
    'context',
    'commons',
    'restfull',
    'json',
    'vue',
    'element-ui',
    'mi-frame',
    'mi-package-picker',
    'mi-package-detail',
    'css!' + window.APPPATH + 'front/operation/package-choose/page-operation-package-choose.css'
], function(tpl, config, context, commons, ajax, $, Vue){

    var pageId = 'page-operation-package-choose';

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
                tree: {
                    loading: false,
                    data: [],
                    current: {}
                },
                table: {
                    loading: false,
                    data: []
                }
            },
            methods:{
                load: function() {
                    var self = this;
                    self.tree.loading = true;
                    self.tree.data.splice(0, self.tree.data.length);
                    self.tree.data.push({
                        id: -1,
                        nickname: '用户列表'
                    });
                    ajax.post('/operation/statistic/strategy/quest/user/list', null, function(data, status) {
                        if (status == 200) {
                            if (data && data.length > 0) {
                                self.tree.data = self.tree.data.concat(data);
                            }
                        }
                        self.tree.loading = false;
                    })
                },
                handleCreate: function() {
                    var self = this;
                },
                currentTreeNodeChange: function(data) {
                    var self = this;
                    if (data.id != -1) {
                        self.tree.current = data;
                        self.getPackagePermission();
                    } else {
                        self.tree.current = {}
                    }
                },
                getPackagePermission: function() {
                    var self = this;
                    self.table.loading = true;
                    self.table.data.splice(0, self.table.data.length);
                    var questData = {
                        userId: self.tree.current.id
                    };
                    ajax.post('/operation/package/user/permission/query/package/by/user', questData, function(data, status) {
                        if (status == 200 && data && data.length > 0) {
                            self.table.data = self.table.data.concat(data);
                        }
                    })
                },
                handleRemoveBindPackage: function(scope) {
                    var self = this;
                    self.table.loading = true;
                    var row = scope.row;
                    self.showTip('删除绑定', '该操作将永久移除用户对套餐的绑定，是否继续？', function(callback) {
                        ajax.post('/operation/package/user/permission/remove/' + row.id, null, function(data, status) {
                            if (status == 200) {
                                self.table.data.splice(scope.$index, 1);
                            }
                            callback();
                        });
                    });
                },

                handleDetailPackage: function(scope) {
                    var self = this;
                    var row = scope.row;
                    self.$refs.packageDetail.open(row.packageInfo.id);
                },

                handleBindPackage: function() {
                    var self = this;
                    self.$refs.packagePicker.open(self.table.data);
                },
                handlePackagePickerCommit: function(_buff, choise, closeFunc) {
                    var self = this;
                    var packageIdList = [];
                    for (var j = 0; j < choise.length; j++) {
                        packageIdList.push(choise[j].id);
                    }
                    var questData = {
                        userId: self.tree.current.id,
                        packageIds: JSON.stringify(packageIdList)
                    };
                    ajax.post('/operation/package/user/permission/add/list', questData, function(data, status) {
                        if(status == 200) {
                            if (data && data.length > 0) {
                                for(var i = 0; i < data.length; i++) {
                                    _buff.push(data[i]);
                                }
                            }
                            closeFunc();
                        }
                    });
                },

                showTip: function(title, text, confirmListener) {
                    var self = this;
                    var h = self.$createElement;
                    self.$msgbox({
                        title: title ? title : '危险操作',
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
                self.load();
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