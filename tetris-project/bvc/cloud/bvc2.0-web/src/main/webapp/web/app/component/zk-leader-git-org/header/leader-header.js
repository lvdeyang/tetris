define([
    'text!' + window.APPPATH + 'component/zk-leader/header/leader-header.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'date',
    'css!' + window.APPPATH + 'component/zk-leader/header/leader-header.css'
], function (tpl, ajax, $, Vue, config) {

    //组件名称
    var pluginName = 'leader-header';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data: function () {
            return {
                user: '',
                usernameLength:'',
                timer: '',
                date: '',
                time: '',
                fightDate: '---',
                fightTime: '---',
                //弹出对话框的表单
                form: {
                    region: '',
                    date1: '',
                    date2: ''
                },
                dialogVisible: false
            }
        },
        methods: {
            //回到首页
            goHome:function(){
                var self = this;
                self.qt.invoke('slotGoHomeDisabled');
            },
            //系统设置
            showSettings: function () {
                var self = this;
                self.qt.window('/router/zk/leader/settings', null, {width: 980, height: 600});
            },
            // exitCommand:function(){
            //     var self = this;
            //     self.qt.confirm('提示', '确定关闭软件？', '是', function(){
            //         self.qt.linkedWebview('rightBar', {id:'exitCurrentCommandAndCloseWindow'});
            //     });
            // },
            //退出
            closeExit: function () {
                var self = this;
                self.qt.invoke('closeWindow');
            },
            //打开设置作战时间
            openSetTime: function () {
                var self = this;
                self.qt.window('/router/zk/leader/set/fightTime', null, {width: 980, height: 600})
            },
            //把时间戳改成时间格式
            timeFormat: function (dateStr) {
                var date = new Date(dateStr);
                Y = date.getFullYear() + '-';
                M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
                D = date.getDate() + ' ';
                h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
                m = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
                s = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds();
                return Y + M + D + h + m + s;
            }
        },
        mounted: function () {
            var self = this;
            //获取天文时间
            var timeInterval = function () {
                self.date = new Date().format('yyyy-MM-dd');
                self.time = new Date().format('HH : mm : ss');
            };
            timeInterval();
            self.timer = setInterval(timeInterval, 1000);

            self.qt = new QtContext('header', function () {

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

                self.qt.get(['user'], function (variables) {
                    self.user = variables.user ? $.parseJSON(variables.user) : {};
                    self.usernameLength=self.user.name.length;
                    self.qt.on('signalRecvMsgFromHtml', function (msg) {
                        alert('header' + msg);
                    });
                });

                self.qt.on('closeWindow', function(){
                    self.closeExit();
                });

                // TODO: 获取作战时间 linkwebview
                // var d = new Date(2019, 11, 31, 23, 59, 45);//2015-1-1 1:1:1
                // var c = new Date('2020-01-20 12:23:23');
                // console.log(c)
                // console.log(d)
                // self.fightDate = self.timeFormat(d).split(' ')[0];
                // self.fightTime = self.timeFormat(d).split(' ')[1];
                // setInterval(function () {
                //     d.setSeconds(d.getSeconds() + 1);//+1s
                //     self.fightDate = self.timeFormat(d).split(' ')[0];
                //     self.fightTime = self.timeFormat(d).split(' ')[1];
                // }, 1000);
            });
            self.fightDate='2020-01-20'
        }
    });

    return Vue;
});