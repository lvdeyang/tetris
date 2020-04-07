define([
    'text!' + window.APPPATH + 'component/zk-leader/recManage/rec.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'css!' + window.APPPATH + 'component/zk-leader/recManage/rec.css'
], function (tpl, ajax, $, Vue, config) {

    //组件名称
    var pluginName = 'leader-rec';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data: function () {
            return {
                baseUrl: window.BASEPATH,
                filterText: '',
                recordMode: 'MANUAL', //录制模式  MANUAL(手动)|SCHEDULING(排期)
                date: '', //查询日期
                deviceCurrentPage: 1,
                deviceData: {
                    totalData: [],
                    filterData: []
                }
            }
        },
        computed: {
            //设备的分页
            pageData: function () {
                return this.deviceData.totalData.slice((this.deviceCurrentPage - 1) * 3, this.deviceCurrentPage * 3);
            }
        },
        methods: {
            //当前页改变
            deviceCurrentChange: function (val) {
                this.deviceCurrentPage = val;
            },
            //过滤设备数据
            // filterDeviceCommand:function(){
            //     var self = this;
            //     self.deviceData.filterData.splice(0, self.deviceData.filterData.length);
            //     for(var i=0; i<self.deviceData.totalData.length; i++){
            //         if(self.deviceData.totalData[i].fileName.indexOf(self.filterText)>=0){
            //             self.deviceData.filterData.push(self.deviceData.totalData[i]);
            //         }
            //     }
            // },
            search: function () {
                var mode = this.recordMode;
                var name = this.filterText;
                var date = this.date;
                var startDate = this.format(date[0]);
                var endDate = this.format(date[1]);
                var page = this.deviceCurrentPage;
                this.deviceData.totalData.splice(0, this.deviceData.totalData.length);
                this.refreshRecord(mode, name, startDate, endDate, page);
            },
            //停止任务
            stopTask: function (id) {
                var self = this;
                ajax.post('/monitor/record/stop/' + id, null, function () {
                        self.qt.success('成功停止此条数据的录制');
                })
            },
            //获取数据
            refreshRecord: function (text1, text2, text3, text4, text5) {
                var self = this;
                ajax.post('/monitor/record/load', {
                    mode: text1,
                    fileName: text2,
                    deviceType: '',
                    device: '',
                    startTime: text3,
                    endTime: text4,
                    currentPage: text5,
                    pageSize: '10'
                }, function (data) {
                    if (data.rows && data.rows.length > 0) {
                        var commands = data.rows;
                        for (var i = 0; i < commands.length; i++) {
                            self.deviceData.totalData.push(commands[i]);
                        }
                    }
                });
            },
            cancel: function () {
                var self = this;
                self.qt.destroy();
            },
            //格式化时间
            format: function (str) {
                if (str) {
                    str = str.toString();
                    var str = str.replace(/ GMT.+$/, '');// Or str = str.substring(0, 24)
                    var d = new Date(str);
                    var a = [d.getFullYear(), d.getMonth() + 1, d.getDate(), d.getHours(), d.getMinutes(), d.getSeconds()];
                    for (var i = 0, len = a.length; i < len; i++) {
                        if (a[i] < 10) {
                            a[i] = '0' + a[i];
                        }
                    }
                    str = a[0] + '-' + a[1] + '-' + a[2] + ' ' + a[3] + ':' + a[4] + ':' + a[5];
                    return str;
                }
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

                self.refreshRecord(self.recordMode, self.filterText, self.format(self.date[0]), self.format(self.date[1]), self.deviceCurrentPage);

                self.qt.on('close', function () {
                    self.qt.destroy();
                });
            });
        }
    });

    return Vue;
});