/**
 * Created by sms on 2020/1/7.
 */
define([
    'text!' + window.APPPATH + 'cs/dialog/mediaPicker/media-picker-dialog.html',
    'restfull',
    'jquery',
    'vue',
    'element-ui',
    'css!' + window.APPPATH + 'cs/dialog/mediaPicker/media-picker-dialog.css'
], function (tpl, ajax, $, Vue) {
    var pluginName = 'cs-media-picker-dialog';

    var ON_MEDIA_PICKER_CLOSE = 'on-media-picker-close';

    Vue.component(pluginName, {
        template: tpl,
        data: function () {
            return {
                visible: false,
                loading: false,
                url: '',
                channelData: {},
                checked: [],
                checkedUuid: [],
                tree: {
                    loading: false,
                    props: {
                        label: 'name',
                        children: 'subColumns'
                    },
                    data: []
                }
            }
        },
        methods: {
            open: function (url, channelData, checked) {
                 var self = this;
                self.visible = true;
                self.url = url;
                self.channelData = channelData;
                self.checked = checked;
                if (checked) {
                    for (var i = 0; i < checked.length; i++) {
                        self.checkedUuid.push(checked[i].id);
                    }
                }
                self.load();
            },
            load: function () {
                var self = this;
                self.tree.loading = true;
                var postData = {
                    channelId: self.channelData.id
                };
                ajax.post(self.url, postData, function(data, status) {
                    if (status == 200) {
                        if (data) {
                            for (var i = 0;i < data.length; i++) {
                                self.tree.data.push(data[i]);
                            }
                        }
                    }
                    self.tree.loading = false;
                });
            },
            handleMediaPickerCheckChange: function () {
                var self = this;
            },
            handleInverse: function () {
                var self = this;
                var refs = self.$refs.mediaPickerTree;
                var nodes = self.tree.data;
                var selected = refs.getCheckedNodes(true, false);
                if (nodes) {
                    for (var i = 0; i< nodes.length; i++){
                        refs.setChecked(nodes[i], true, true);
                    }
                }

                if (selected) {
                    for (var j = 0; j< selected.length; j++){
                        refs.setChecked(selected[j], false, true);
                    }
                }
            },
            handleMediaPickerClose: function () {
                var self = this;
                self.visible = false;
                self.url = '';
                self.channelData = {};
                self.checked = [];
                self.checkedUuid = [];
                self.tree.loading = false;
                self.tree.data.splice(0, self.tree.data.length);
            },
            handleMediaPickerCommit: function () {
                var self = this;

                var checked = self.checked;
                var selected = [];
                var checkedNodes = self.$refs.mediaPickerTree.getCheckedNodes(true, false);

                if (checkedNodes && checkedNodes.length > 0) {
                    for (var i = 0; i < checkedNodes.length; i++) {
                        selected.push(checkedNodes[i]);
                    }
                //} else {
                //    self.$message({
                //        message: '您没有选择任何资源',
                //        type: 'warning'
                //    });
                //    return;
                }

                var startLoading = function () {
                    self.loading = true;
                };
                var endLoading = function () {
                    self.loading = false;
                };
                var close = function () {
                    self.handleMediaPickerClose();
                };

                self.$emit(ON_MEDIA_PICKER_CLOSE, self.channelData, checked, selected, startLoading, endLoading, close);
            }
        }
    })
});