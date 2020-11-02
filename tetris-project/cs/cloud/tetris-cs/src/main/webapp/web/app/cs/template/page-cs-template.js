/**
 * Created by lzp on 2019/3/16.
 */
define([
    'text!' + window.APPPATH + 'cs/template/page-cs-template.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'mi-lightbox',
    'cs-user-dialog',
    'program-screen',
    'cs-media-picker',
    'cs-area-picker',
    'mi-compress-dialog',
    'css!' + window.APPPATH + 'cs/template/page-cs-template.css'
], function (tpl, config, $, ajax, context, commons, Vue) {

    var pageId = 'page-cs-template';

    var init;
    init = function () {

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
                loading: false,
                loadingText: "",
                template: {
                    page: {
                        currentPage: 1,
                        sizes: [10, 15, 20, 50],
                        size: 10,
                        total: 0
                    },
                    data: [],
                    multipleSelection: []
                },
                dialog: {
                    addTemplate: {
                        visible: false,
                        loading: false,
                        name: "",
                      
                    }
                }
            },
            computed: {},
            watch: {},
            methods: {
                
                getTemplateList: function () {
                    var self = this;
                    self.loading = true;
                    var requestData = {
                        currentPage: self.template.page.currentPage,
                        pageSize: self.template.page.size
                    };
                    ajax.post('/cs/channel/template/list', requestData, function (data, status) {
                        self.loading = false;
                        if (status != 200) return;
                        self.template.data = data.rows;
                        self.template.page.total = data.total;
                    }, null, ajax.NO_ERROR_CATCH_CODE)
                },
                multiDelete: function () {
                    var self = this;

                    
                },
                handleAddTemplate: function () {
                    var self = this;
                    self.dialog.addTemplate.name = "";
                    self.dialog.addTemplate.visible = true;
                },
                handleAddTemplateClose: function () {
                    var self = this;
                    self.dialog.addTemplate.visible = false;
                },
                handleAddTemplateCommit: function () {
                    var self = this;
                   
                    self.dialog.addTemplate.loading = true;
                    var newData = {
                        name: self.dialog.addTemplate.name,
                    };
                    ajax.post('/cs/channel/template/add', newData, function (data, status) {
                        self.dialog.addTemplate.loading = false;
                        if (status != 200) return;
                        if (self.template.data.length < self.template.page.size) {
                            self.template.data.push(data);
                        }
                        self.template.page.total += 1;
                        self.handleAddTemplateClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                rowDelete: function (scope) {
                    var self = this;
                    var row = scope.row;
                    self.showTip('', '此操作将永久删除模板，是否继续?', function (callback) {
                        var questData = {
                            id: row.id
                        };
                        ajax.post('/cs/channel/template/remove', questData, function (data, status) {
                            if (status != 200) return;
                            callback();
                            self.getTemplateList();
                        }, null, ajax.NO_ERROR_CATCH_CODE);
                    });
                },
                editPrograme: function (scope) {
                    var self = this;
                    var row = scope.row;
                    location.href='#/page-cs-templatepro/'+row.id
                },
                handleSelectionChange: function (val) {
                    this.template.multipleSelection = val;
                },
                handleTemplatePageCurrentChange: function (val) {
                    var self = this;
                    self.template.page.currentPage = val;
                    self.getTemplateList();
                },
                handleTemplateSizeChange: function (val) {
                    var self = this;
                    self.template.page.size = val;
                    self.getTemplateList();
                },
                showTip: function (title, text, confirmListener) {
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
                                confirmListener(function () {
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
            created: function () {
                var self = this;
                self.getTemplateList();
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