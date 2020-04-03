define([
    'text!' + window.APPPATH + 'component/zk-leader/addRecord/addRecord.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'css!' + window.APPPATH + 'component/zk-leader/addRecord/addRecord.css'
], function (tpl, ajax, $, Vue, config) {

    //组件名称
    var pluginName = 'add-record';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data: function () {
            return {
                baseUrl: window.BASEPATH,
                recordMode: 'MANUAL', //录制模式
                date: '', //查询日期
                inputName: '',
                recordId: '', //录制用户/设备的id
                category:'', //用于区分是录制用户还是设备
                trueType:''
            }
        },
        methods: {
            //提交
            submit: function () {
                var self=this;
                if(self.category === 'USER'){
                    ajax.post('/monitor/record/add/record/user/task',{
                        userId:self.recordId,
                        mode:self.recordMode,
                        fileName:self.inputName,
                        startTime:self.format(this.date[0]),
                        endTime:self.format(this.date[1])
                    },function () {
                        self.qt.success('添加录制任务成功!');
                        self.qt.destroy();
                    })
                }else if(self.category ==='BUNDLE'){
                    if(self.trueType === 'XT'){
                        ajax.post('/monitor/record/add/xt/device',{
                             mode:self.recordMode,
                             fileName:self.inputName,
                             startTime:self.format(this.date[0]),
                             endTime:self.format(this.date[1]),
                            bundleId:self.recordId
                        },function () {
                            self.qt.success('添加录制任务成功!!');
                            self.qt.destroy();
                        })
                    }else if(self.trueType === 'BVC'){
                        ajax.post('/monitor/record/add',{
                            mode:self.recordMode,
                            fileName:self.inputName,
                            startTime:self.format(this.date[0]),
                            endTime:self.format(this.date[1]),
                            bundleId:self.recordId
                        },function () {
                            self.qt.success('添加录制任务成功!!!');
                            self.qt.destroy();
                        })
                    }
                }
            },
            //关闭页面
            cancel: function () {
                var self = this;
                self.qt.destroy();
            },
            //格式化日期
            format: function (str) {
                if (str) {
                    str=str.toString();
                    var str = str.substring(0, 24);
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
            self.qt = new QtContext('addRecord', function () {
                var params = self.qt.getWindowParams();
                console.log(params)
                self.recordId = params.paramId;
                self.category=params.paramType;
                self.trueType=params.realType;
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
            });
        }
    });

    return Vue;
});