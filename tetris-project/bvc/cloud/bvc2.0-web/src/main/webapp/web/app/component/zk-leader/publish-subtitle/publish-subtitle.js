define([
    'text!' + window.APPPATH + 'component/zk-leader/publish-subtitle/publish-subtitle.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'css!' + window.APPPATH + 'component/zk-leader/publish-subtitle/publish-subtitle.css'
], function (tpl, ajax, $, Vue, config) {

    //组件名称
    var pluginName = 'publish-subtitle';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data: function () {
            return {
                baseUrl: window.BASEPATH,
                leftCurrentPage: 1,
                leftData: [],
                osdId: '',  //要传的
                serial: ''
            }
        },
        computed: {
            //左侧的分页
            pageData: function () {
                return this.leftData.slice((this.leftCurrentPage - 1) * 10, this.leftCurrentPage * 10);
            }
        },
        methods: {
            //当前页改变
            leftCurrentChange: function (val) {
                this.leftCurrentPage = val;
            },
            //获取表格数据
            refreshLeftData: function () {
                var self = this;
                self.leftData.splice(0, self.leftData.length);
                ajax.post('/monitor/osd/load', {
                    currentPage: self.leftCurrentPage,
                    pageSize: '10'
                }, function (data) {
                    if (data.rows && data.rows.length > 0) {
                        var commands = data.rows;
                        for (var i = 0; i < commands.length; i++) {
                            self.leftData.push(commands[i]);
                        }
                    }
                });
            },
            //添加提交
            leftConfirm: function () {
                var self = this;
                ajax.post('/command/basic/set/osd', {
                    serial: self.serial,
                    osdId: self.osdId
                }, function (data) {
                    self.qt.success('添加成功');
                    self.cancel();
                });
            },
            //左侧点击 右侧出现
            clickRow: function (row) {
                var self = this;
                self.osdId = row.id;
            },
            cancel: function () {
                var self = this;
                self.qt.destroy();
            }
        },
        mounted: function () {
            var self = this;
            self.qt = new QtContext('rec', function () {
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
                var params = self.qt.getWindowParams();
                self.serial = params.serial;
                //获取用户数据
                self.qt.get(['user'], function (variables) {
                    self.user = variables.user ? $.parseJSON(variables.user) : {};
                    self.gateIp = variables.user ? $.parseJSON(variables.user).gateIp : '';
                    self.gatePort = variables.user ? $.parseJSON(variables.user).gatePort : '';

                });

                self.refreshLeftData();

                self.qt.on('close', function () {
                    self.qt.destroy();
                });
            });
        }
    });

    return Vue;
});