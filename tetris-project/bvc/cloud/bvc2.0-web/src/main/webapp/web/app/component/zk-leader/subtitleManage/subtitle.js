define([
    'text!' + window.APPPATH + 'component/zk-leader/subtitleManage/subtitle.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'css!' + window.APPPATH + 'component/zk-leader/subtitleManage/subtitle.css'
], function (tpl, ajax, $, Vue, config) {

    //组件名称
    var pluginName = 'subtitle';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data: function () {
            return {
                baseUrl: window.BASEPATH,
                currentPage: 1,
                data: [],
                fontData: [],
                dialogFormVisible: false,
                form: {
                    name: '',
                    font: '黑体',
                    fontsize: 20,
                    color: '#409EFF',
                    textarea: ''
                },
                formLabelWidth: '120px',
                btnShow:false,
                editId:''
            }
        },
        computed: {
            //设备的分页
            pageData: function () {
                return this.data.slice((this.currentPage - 1) * 10, this.currentPage * 10);
            }
        },
        methods: {
            //当前页改变
            currentChange: function (val) {
                this.currentPage = val;
            },
            //获取数据
            refreshData: function () {
                var self = this;
                self.data.splice(0,self.data.length);
                ajax.post('/monitor/subtitle/load', {
                    currentPage: self.currentPage,
                    pageSize: '10'
                }, function (data) {
                    if (data.rows && data.rows.length > 0) {
                        var commands = data.rows;
                        for (var i = 0; i < commands.length; i++) {
                            self.data.push(commands[i]);
                        }
                    }
                });
            },
            //获取字体数据
            getFontData: function () {
                var self = this;
                ajax.post('/monitor/subtitle/query/fonts', null, function (data) {
                    self.fontData = data;
                });
            },
            //添加提交
            addSubtitle: function () {
                var self = this;
                ajax.post('/monitor/subtitle/add', {
                    name: self.form.name,
                    content: self.form.textarea,
                    font: self.form.font,
                    height: self.form.fontsize,
                    color: self.form.color.split('#')[1]
                }, function () {
                    self.qt.success('添加成功');
                    self.dialogFormVisible = false;
                    self.refreshData();
                });
                self.btnShow=false;
            },
            //修改
            editRow: function (row) {
                this.dialogFormVisible = true;
                this.form.name = row.name;
                this.form.font = row.font;
                this.form.fontsize = parseInt(row.height);
                this.form.color = '#' + row.color;
                this.form.textarea = row.content;
                this.btnShow=true;
                this.editId=row.id;
            },
            editSubtitle:function () {
                var self = this;
                ajax.post('/monitor/subtitle/edit/' + self.editId, {
                    name: self.form.name,
                    content: self.form.textarea,
                    font: self.form.font,
                    height: self.form.fontsize,
                    color: self.form.color.split('#')[1]
                }, function (data) {
                    self.qt.success('修改成功');
                    self.dialogFormVisible = false;
                    self.refreshData();
                })
            },
            //删除
            deleteRow:function (id) {
                var self=this;
                ajax.post('/monitor/subtitle/remove/'+id,null,function (data) {
                    self.qt.success('删除成功');
                    self.refreshData();
                })
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

                //获取用户数据
                self.qt.get(['user'], function (variables) {
                    self.user = variables.user ? $.parseJSON(variables.user) : {};
                    self.gateIp = variables.user ? $.parseJSON(variables.user).gateIp : '';
                    self.gatePort = variables.user ? $.parseJSON(variables.user).gatePort : '';

                });

                self.refreshData();
                self.getFontData();

                self.qt.on('close', function () {
                    self.qt.destroy();
                });
            });
        }
    });

    return Vue;
});