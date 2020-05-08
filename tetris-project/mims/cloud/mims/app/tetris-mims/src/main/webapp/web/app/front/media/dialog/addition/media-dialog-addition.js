/**
 * Created by sms on 2020/3/31.
 */
define([
    'text!' + window.APPPATH + 'front/media/dialog/addition/media-dialog-addition.html',
    'restfull',
    'json',
    'vue',
    'element-ui',
    'css!' + window.APPPATH + 'front/media/dialog/addition/media-dialog-addition.css'
], function(tpl, ajax, $, Vue) {
    var pluginName = 'mi-addition-dialog';

    var ON_DIALOG_CLOSE = 'on-dialog-close';

    Vue.component(pluginName, {
        template: tpl,
        data: function () {
            return {
                visible: false,
                loading: {
                    confirm: false,
                    add: false,
                    deleteAll: false
                },
                addition: '',
                rows: [{
                    key: '',
                    value: ''
                }],
                keyId: 1
            }
        },
        methods: {
            open: function(addition) {
                var self = this;
                self.addition = addition;
                var additionJson = {};
                if (addition) {
                    if (typeof addition==='string') {
                        additionJson = JSON.parse(addition);
                    } else {
                        additionJson = addition;
                    }
                }
                self.rows.splice(0, self.rows.length);
                for(var key in additionJson) {
                    if (additionJson.hasOwnProperty(key)){
                        self.rows.push({
                            key: key,
                            value: additionJson[key]
                        });
                    }
                }
                self.visible = true;
            },
            handleAddItem: function() {
                var self = this;
                self.loading.add = true;
                self.rows.push({
                    key: 'key_' + self.keyId,
                    value: 'value'
                });
                self.keyId++;
                self.loading.add = false;
            },
            handelDeleteAll: function() {
                var self = this;
                self.loading.deleteAll = true;
                self.rows.splice(0, self.rows.length);
                self.loading.deleteAll = false;
            },
            handleDeleteItem: function(scope) {
                var self = this;
                var row = scope.row;
                var index = 0;
                for (var i = 0; i < self.rows.length;i++) {
                    var item = self.rows[i];
                    if (item.key == row.key && item.value == row.value) {
                        index = i;
                        break;
                    }
                }
                self.rows.splice(index, 1);
            },
            handleAdditionClose: function() {
                var self = this;
                self.visible = false;
                self.loading.confirm = false;
                self.loading.add = false;
                self.loading.deleteAll = false;
                self.addition = '';
                self.rows = [{
                    key: '',
                    value: ''
                }];
                self.keyId = 1;
            },
            handleAdditionEditOk: function() {
                var self = this;
                self.loading.confirm = true;
                var additionJson = {};
                for(var i = 0; i < self.rows.length; i++) {
                    var item = self.rows[i];
                    if (additionJson.hasOwnProperty(item.key)) {
                        self.$message({
                            message: '属性名重复：' + item.key,
                            type: 'warning'
                        });
                        self.loading.confirm = false;
                        return;
                    }
                    additionJson[item.key] = item.value;
                }
                self.$emit(ON_DIALOG_CLOSE, self.addition, additionJson, self.handleAdditionClose);
            }
        }
    })
});