define([
    'text!' + window.APPPATH + 'component/zk-leader/fileImport/file-import.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'css!' + window.APPPATH + 'component/zk-leader/fileImport/file-import.css'
], function (tpl, ajax, $, Vue, config) {

    //组件名称
    var pluginName = 'file-import';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data: function () {
            return {
                baseUrl: window.BASEPATH,
                //树相关的
                treeDatas: [],
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                checkedId: '',
                //表格数据
                fileDatas: [],
                currentPage: 1,
                filterInput: '',
                //对话框相关的
                dialogVisible: false,
                gridData: [], //对话框的数据
                dialogCurrent: 1,
                chooseRow: '',
                checkBoxData: [] //选中的数据
            }
        },
        watch: {
            //监听table页面变化，保持勾选的状态
            pageData: function () {
                var self = this;
                this.$nextTick(function () {
                    self.checkBoxData.forEach(function (item) {
                        if (self.pageData.indexOf(item) > -1) {
                            //保证翻页后，勾选状态不变
                            self.$refs.multipleTable.toggleRowSelection(item, true);
                        }
                    })
                })
            }
        },
        computed: {
            //大table分页
            pageData: function () {
                return this.fileDatas.slice((this.currentPage - 1) * 10, this.currentPage * 10);
            },
            //对话框的分页
            dialogPageData: function () {
                return this.gridData.slice((this.dialogCurrent - 1) * 5, this.dialogCurrent * 5);
            }
        },
        methods: {
            //当前页改变
            CurrentChange: function (val) {
                this.currentPage = val;
            },
            handleCurrentChange: function (val) {
                this.dialogCurrent = val;
            },

            //--------对话框表格----------
            //点击表格的一行
            clickRow: function (val) {
                this.chooseRow = val;
            },
            //对话框的确定
            addSubmit: function () {
                var self = this;
                var param = {
                    fullPath: self.chooseRow.fullPath,
                    currentPage: self.currentPage,
                    pageSize: 10
                };
                ajax.post('/monitor/external/static/resource/folder/scanning', param, function (data) {
                    self.dialogVisible = false;
                    self.fileDatas = data.rows;
                })
            },

            //----------右侧表格的相关的-----------
            //table的 复选框点击事件
            handleSelect: function (selectArr, val) {
                var self = this;
                var idx = self.checkBoxData.indexOf(val);
                if (idx > -1) {
                    self.checkBoxData.splice(idx, 1);
                } else {
                    self.checkBoxData.push(val);
                }
            },

            //表格上的全选功能
            selectCurrentPage: function (val) {
                var self = this;
                if (val.length === 0) { //取消全选
                    self.checkBoxData = self.checkBoxData.filter(function (item) {
                        return self.pageData.indexOf(item) === -1;
                    })
                } else if (val.length === self.pageData.length) { //全选，有的话不管，没有的话添加
                    self.pageData.forEach(function (item) {
                        if (self.checkBoxData.indexOf(item) === -1) {
                            self.checkBoxData.push(item)
                        }
                    })
                }
            },
            //点击右侧选择目录按钮
            search: function () {
                var self = this;
                this.dialogVisible = true;
                ajax.post('/monitor/external/static/resource/folder/load/all', {
                    currentPage: self.dialogCurrent,
                    pageSize: 5
                }, function (data) {
                    self.gridData = data.rows;
                })
            },

            //筛选按钮
            filter: function () {
                var self = this;
                if (!self.filterInput) {
                    self.qt.warning('您还没有输入要查找的路径');
                    return;
                }
                var param = {
                    fullPath: self.filterInput,
                    currentPage: self.currentPage,
                    pageSize: 10
                };
                ajax.post('/monitor/external/static/resource/folder/scanning', param, function (data) {
                    self.dialogVisible = false;
                    self.fileDatas = data.rows;
                })
            },

            //点击单个上传按钮
            uploadRow: function (row) {
                var self = this;
                var arr = [];
                arr.push(row);
                if (!self.checkedId) {
                    self.qt.warning('请先选择要上传的文件夹');
                    return;
                }
                ajax.post('/monitor/vod/add', {
                    folderId: self.checkedId,
                    resources: JSON.stringify(arr)
                }, function (data) {
                    self.qt.info('上传成功');
                    self.getFileDatas();
                })
            },

            //确定导入
            confirmImport: function () {
                var self = this;
                if (!self.checkedId) {
                    self.qt.warning('请先选择要上传的文件夹');
                    return;
                }
                if (!self.checkBoxData.length) {
                    self.qt.warning('请选择右侧表格的文件');
                    return;
                }
                ajax.post('/monitor/vod/add', {
                    folderId: self.checkedId,
                    resources: JSON.stringify(self.checkBoxData)
                }, function (data) {
                    self.qt.success('操作成功');
                    self.getFileDatas();
                })
            },

            //左侧树的删除
            remove: function (id) {
                var self = this;
                var arr = [];
                arr.push(id);
                ajax.post('/monitor/vod/remove', {resourceIds: JSON.stringify(arr)}, function (data) {
                    self.qt.info('删除成功');
                    self.getFileDatas();
                })
            },

            //获取树形组件数据
            getFileDatas: function () {
                var self = this;
                self.treeDatas.splice(0, self.treeDatas.length);
                ajax.post('/monitor/vod/query/resource/tree/for/edit', null, function (data) {
                    self.treeDatas = data;
                });
            },

            //树形组件的复选框实现单选
            handleNodeClick: function (data, checked, node) {
                if (checked === true) {
                    this.checkedId = data.id;
                    this.$refs.treeForm.setCheckedKeys([data.id]);
                } else {
                    if (this.checkedId == data.id) {
                        this.$refs.treeForm.setCheckedKeys([data.id]);
                    }
                }
            },

            //关闭整个页面
            cancel: function () {
                var self = this;
                self.qt.destroy();
            }
        },
        mounted: function () {
            var self = this;
            self.qt = new QtContext('fileImport', function () {
                //初始化ajax
                ajax.init({
                    login: config.ajax.login,
                    redirectLogin: false,
                    authname: config.ajax.authname,
                    debug: config.ajax.debug,
                    messenger: {
                        info: function (message, status) {
                            self.qt.info(message);
                        },
                        success: function (message, status) {
                            self.qt.success(message);
                        },
                        warning: function (message, status) {
                            self.qt.warning(message);
                        },
                        error: function (message, status) {
                            self.qt.error(message);
                        }
                    }
                });

                //获取用户数据
                self.qt.get(['user'], function (variables) {
                    self.user = variables.user ? $.parseJSON(variables.user) : {};
                    self.gateIp = variables.user ? $.parseJSON(variables.user).gateIp : '';
                    self.gatePort = variables.user ? $.parseJSON(variables.user).gatePort : '';

                });

                self.getFileDatas();

                self.qt.on('close', function () {
                    self.qt.destroy();
                });
            });
        }
    });

    return Vue;
});