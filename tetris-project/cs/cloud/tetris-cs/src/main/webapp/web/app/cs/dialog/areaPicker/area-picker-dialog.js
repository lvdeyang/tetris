/**
 * Created by sms on 2020/4/13.
 */
define([
    'text!' + window.APPPATH + 'cs/dialog/areaPicker/area-picker-dialog.html',
    'restfull',
    'jquery',
    'vue',
    'element-ui',
    'css!' + window.APPPATH + 'cs/dialog/areaPicker/area-picker-dialog.css'
], function(tpl, ajax, $, Vue) {
    var pluginName = 'cs-area-picker-dialog';

    var ON_MEDIA_PICKER_CLOSE = 'on-area-picker-close';

    Vue.component(pluginName, {
        template: tpl,
        data: function () {
            return {
                visible: false,
                loading: false,
                tree: {
                    props: {
                        lable: "name",
                        children: "subColumns"
                    },
                    expandOnclickNode: false,
                    data: []
                },
                channelData: {},
                checkAreaIdList: [],
                check: []
            }
        },
        methods: {
            open: function(check, channelData) {
                var self = this;
                self.visible = true;
                self.loading = true;
                this.channelData = channelData;
                this.check = check;
                var questData = {
                    channelId: channelData ? channelData.id : null
                };
                self.tree.data = [];
                ajax.post('/cs/area/list', questData, function (data, status) {
                    if (status != 200) return;
                    self.tree.data = data.treeData;
                    if (check) {
                        for(var i = 0; i< check.length; i++) {
                            self.checkAreaIdList.push(check[i].areaId);
                        }
                    }
                    self.$refs.areaPickerTree.setCheckedKeys(self.checkAreaIdList.concat(data.checkList));
                    self.loading = false;
                }, null, ajax.NO_ERROR_CATCH_CODE)
            },
            manageDivision: function(node, resolve) {
                var self = this;
                self.loading = true;
                if (node.level != 0) {
                    var questData = {
                        channelId: self.channelData ? self.channelData.id : null,
                        divisionId: node.data.areaId,
                        disabled: node.data.disabled
                    };
                    ajax.post('/cs/area/children', questData, function (data, status) {
                        if (status != 200) return;
                        resolve(data.treeData);
                        self.$refs.areaPickerTree.setCheckedKeys(self.checkAreaIdList.concat(data.checkList));
                        self.loading = false;
                    }, null, ajax.NO_ERROR_CATCH_CODE)
                }
            },
            handleAreaPickerClose: function() {
                var self = this;
                this.visible = false;
                this.loading = false;
                this.channelData = {};
                this.check = [];
                this.tree.data = [];
            },
            handleAreaPickerChange: function() {

            },
            handleAreaPickerCommit: function() {
                var self = this;
                var choice = self.$refs.areaPickerTree.getCheckedNodes(false, false);
                this.$emit(ON_MEDIA_PICKER_CLOSE, self.check,self.channelData,  choice, self.handleAreaPickerClose);
            }
        }
    })
});