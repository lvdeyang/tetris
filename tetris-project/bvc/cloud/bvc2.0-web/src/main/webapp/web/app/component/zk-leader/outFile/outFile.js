define([
    'text!' + window.APPPATH + 'component/zk-leader/outFile/outFile.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'css!' + window.APPPATH + 'component/zk-leader/outFile/outFile.css'
], function (tpl, ajax, $, Vue, config) {

    //组件名称
    var pluginName = 'out-file';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data: function () {
            return {
                baseUrl: window.BASEPATH,
                //表格相关的
                fileDatas: [],//表格数据
                currentPage: 1,
                //添加 对话框相关的
                dialogVisible: false,
                form: {
                    alias: '', //别名
                    ip: '',
                    port: '',
                    deal: 'http协议',
                    path: '',
                    username: '',
                    password: ''
                },
                formLabelWidth: '80px',
                editId: '', //修改项的id
                editButton:false
            }
        },
        computed: {
            //设备的分页
            pageData: function () {
                return this.fileDatas.slice((this.currentPage - 1) * 10, this.currentPage * 10);
            }
        },
        methods: {
            //当前页改变
            CurrentChange: function (val) {
                this.currentPage = val;
            },
            search: function () {
                this.dialogVisible = true;
            },
            //添加
            addSubmit: function () {
                var self = this;
                ajax.post('/monitor/external/static/resource/folder/add', {
                    name: self.form.alias,
                    ip: self.form.ip,
                    port: self.form.port,
                    folderPath: self.form.path,
                    protocolType: self.form.deal,
                    username: self.form.username,
                    password: self.form.password
                }, function (data) {
                    if(data){
                        self.qt.success('添加数据成功');
                        self.getFileDatas();
                        self.dialogVisible=false;
                    }
                })
            },
            //点击修改按钮
            editRow: function (row) {
                var self = this;
                self.editId=row.id;
                self.editButton=true;
                self.dialogVisible = true;
                self.form.alias = row.name;//别名
                self.form.ip = row.ip;
                self.form.port = row.port;
                self.form.deal = 'http协议';
                self.form.path = row.folderPath;
                self.form.username = row.username;
                self.form.password = row.password;
            },
            //修改提交
            editSubmit: function () {
                var self = this;
                ajax.post('/monitor/external/static/resource/folder/edit/' + self.editId, {
                    name: self.form.alias,
                    ip: self.form.ip,
                    port: self.form.port,
                    folderPath: self.form.path,
                    protocolType: self.form.deal,
                    username: self.form.username,
                    password: self.form.password
                }, function (data) {
                    self.dialogVisible = false;
                    self.getFileDatas();
                })
            },
            //删除
            removeRow: function (id) {
                var self = this;
                ajax.post('/monitor/external/static/resource/folder/remove/' + id, null, function (data) {
                    self.qt.info('删除成功');
                    self.getFileDatas();
                })
            },

            //关闭对话框
            closeDialog:function () {
                this.dialogVisible = false;
                this.editButton=false;
            },

            //获取协议类型
            getDealType: function () {
                ajax.post('/monitor/record/stop/', null, function (data) {
                    console.log(data)
                })
            },

            //获取数据
            getFileDatas: function () {
                var self = this;
                self.fileDatas=[];
                ajax.post('/monitor/external/static/resource/folder/load', {
                    currentPage: self.currentPage,
                    pageSize: '10'
                }, function (data) {
                    var commands = data.rows;
                    for (var i = 0; i < commands.length; i++) {
                        self.fileDatas.push(commands[i]);
                    }
                });
            },

            cancel: function () {
                var self = this;
                self.qt.destroy();
            }
        },
        mounted: function () {
            var self = this;
            self.qt = new QtContext('outFile', function () {
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