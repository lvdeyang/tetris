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
                category: '', //用于区分是录制用户还是设备
                trueType: '',
                serial: '', //播放器上的录制的屏幕序号
                callbackId: '',  //录制成功后 qt要执行的方法
                callbackParam: '', //录制成功后，qt执行方法要传的参数
                pickerBeginDateBefore: {
                    disabledDate: function (time) {
                        //如果当天可选，就不用减8.64e7
                        return time.getTime() < Date.now() - 8.64e7;
                    }
                }
            }
        },
        methods: {
            //提交
            submit: function () {
                var self = this;
                if(!self.inputName){
                    self.qt.warning('名称不能为空！');
                    return;
                }
                if (self.category === 'USER') {
                    ajax.post('/monitor/record/add/record/user/task', {
                        targetUserId: self.recordId,
                        mode: self.recordMode,
                        fileName: self.inputName,
                        startTime: self.format(this.date[0]),
                        endTime: self.format(this.date[1])
                    }, function () {
                        if(self.format(self.date[0]) < new Date(Date.now())){
                            self.qt.warning('不能选当前时间之前的时间，请重新选择！');
                            return;
                        }
                        self.qt.success('添加录制任务成功!');
                        self.qt.destroy();
                    })
                } else if (self.category === 'BUNDLE') {
                    if (self.trueType === 'XT') {
                        ajax.post('/monitor/record/add/xt/device', {
                            mode: self.recordMode,
                            fileName: self.inputName,
                            startTime: self.format(this.date[0]),
                            endTime: self.format(this.date[1]),
                            bundleId: self.recordId
                        }, function () {
                            if(self.format(self.date[0]) < new Date(Date.now())){
                                self.qt.warning('不能选当前时间之前的时间，请重新选择！');
                                return;
                            }
                            self.qt.success('添加录制任务成功!!');
                            self.qt.destroy();
                        })
                    } else if (self.trueType === 'BVC') {
                        ajax.post('/monitor/record/add', {
                            mode: self.recordMode,
                            fileName: self.inputName,
                            startTime: self.format(this.date[0]),
                            endTime: self.format(this.date[1]),
                            bundleId: self.recordId
                        }, function () {
                            if(self.format(self.date[0]) < new Date(Date.now())){
                                self.qt.warning('不能选当前时间之前的时间，请重新选择！');
                                return;
                            }
                            self.qt.success('添加录制任务成功!!!');
                            self.qt.destroy();
                        })
                    }
                } else if (!self.category) { //播放器上的录制按钮
                    ajax.post('/command/record/player/start', {
                        serial: self.serial,
                        mode: self.recordMode,
                        fileName: self.inputName,
                        startTime: self.format(this.date[0]),
                        endTime: self.format(this.date[1])
                    }, function (data, status) {
                        if (status != 200) {
                            self.qt.invoke(self.callbackId, self.callbackParam, false);
                            return;
                        }
                        if(self.format(self.date[0]) < new Date(Date.now())){
                            self.qt.warning('不能选当前时间之前的时间，请重新选择！');
                            return;
                        }
                        self.qt.invoke(self.callbackId, self.callbackParam, true);
                        self.qt.success('录制任务开始!!!');
                        self.qt.destroy();
                    }, null, [403])
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
                    str = str.toString();
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
                self.recordId = params.paramId;
                self.category = params.paramType;
                self.trueType = params.realType;
                self.serial = params.serial;
                self.callbackId = params.callbackId;
                self.callbackParam = params.callbackParam;
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