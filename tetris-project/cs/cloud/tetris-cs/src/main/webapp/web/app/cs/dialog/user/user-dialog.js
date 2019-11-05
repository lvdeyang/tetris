/**
 * Created by sms on 2019/10/31.
 */
define([
    'text!' + window.APPPATH + 'cs/dialog/user/user-dialog.html',
    'restfull',
    'jquery',
    'vue',
    'element-ui',
    'css!' + window.APPPATH + 'cs/dialog/user/user-dialog.css'
], function (tpl, ajax, $, Vue) {
    var pluginName = 'cs-user-dialog';

    var ON_USER_DIALOG_CLOSE = 'on-user-dialog-close';

    Vue.component(pluginName, {
        template: tpl,
        data: function () {
            return {
                visible: false,
                loading: false,
                props: {
                    label: 'nickname',
                    disabled: 'disabled'
                },
                data: [],
                uri: '',
                checked: [],
                checkedId: [],
                selected: [],
                checkStrictly: true
            }
        },
        methods: {
            open: function (uri, checked) {
                var self = this;
                self.visible = true;
                self.uri = uri;
                self.checked = checked;
                for (var i = 0; i < checked.length; i++) {
                    self.checkedId.push(checked[i].id)
                }
                self.load();
            },
            load: function () {
                var self = this;
                var uri = self.uri;
                self.data.splice(0, self.data.length);
                ajax.post(uri, null, function (data) {
                    if (data && data.length > 0) {
                        self.data = data.concat();
                    }

                    self.$nextTick(function () {
                        self.checkStrictly = false;
                    })
                })
            },
            handleUserClose: function () {
                var self = this;
                self.visible = false;
                self.loading = false;
                self.data.splice(0, self.data.length);
                self.checked = [];
                self.checkedId.splice(0, self.checkedId.length);
                self.selected.splice(0, self.selected.length);
                self.uri = ""
            },
            handleUserBindingOk: function () {
                var self = this;
                var checked = self.checked;
                var selected = [];
                var checkedNodes = self.$refs.userTree.getCheckedNodes();

                if (checkedNodes && checkedNodes.length > 0) {
                    for (var i = 0; i < checkedNodes.length; i++) {
                        selected.push(checkedNodes[i]);
                    }
                } else {
                    self.$message({
                        message: '您没有选择任何数据',
                        type: 'warning'
                    })
                }

                var startLoading = function () {
                    self.loading = true;
                };
                var endLoading = function () {
                    self.loading = false;
                };
                var close = function () {
                    self.$nextTick(function () {
                        self.checkStrictly = true;
                    });
                    self.handleUserClose();
                };

                self.$emit(ON_USER_DIALOG_CLOSE, checked, selected, startLoading, endLoading, close);
            }
        }
    })
});