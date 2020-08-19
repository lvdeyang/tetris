/**
 * Created by sms on 2020/4/3.
 */
define([
    'text!' + window.APPPATH + 'front/operation/dialog/package-picker/operation-dialog-package-picker.html',
    'restfull',
    'json',
    'vue',
    'element-ui',
    'css!' + window.APPPATH + 'front/operation/dialog/package-picker/operation-dialog-package-picker.css'
], function(tpl, ajax, $, Vue) {
    var pluginName = 'mi-package-picker';

    var ON_DIALOG_CLOSE = 'on-dialog-close';

    Vue.component(pluginName, {
        template: tpl,
        data: function() {
            return {
                visible: false,
                loading: false,
                tree: {
                    loading: false,
                    data: [],
                    _buff: [],
                    checked: []
                }
            }
        },
        methods: {
            open: function(check) {
                var self = this;
                self.visible = true;
                self.tree.loading = true;
                self._buff = check;
                if (check && check.length > 0) {
                    for(var i = 0; i < check.length; i++) {
                        self.tree.checked.push(check[i].packageInfo.id);
                    }
                }
                ajax.post('/operation/package/list/get', null, function(data, status) {
                    if (status == 200 && data && data.length > 0) {
                        for (var i = 0; i < data.length; i++) {
                            var item = data[i];
                            if (self.tree.checked.indexOf(item.id) == -1) {
                                self.tree.data.push(item);
                            }
                        }
                    }
                    self.tree.loading = false;
                })
            },
            handlePackageChooseChange: function() {
                var self = this;
            },
            handlePackageChooseClose: function() {
                var self = this;
                self.visible = false;
                self.loading = false;
                self.tree.loading = false;
                self.tree.data = [];
                self.tree._buff = [];
                self.tree.checked = []
            },
            handlePackageChooseCommit: function() {
                var self = this;
                self.loading = true;
                var choice = self.$refs.packageChooseTree.getCheckedNodes(true, false);
                self.$emit(ON_DIALOG_CLOSE, self._buff, choice, self.handlePackageChooseClose);
            }
        }
    })
});