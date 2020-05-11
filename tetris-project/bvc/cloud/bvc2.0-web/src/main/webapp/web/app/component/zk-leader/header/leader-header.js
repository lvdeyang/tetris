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
                usernameLength: '',
                cleartimer1: null,
                cleartimer2: null,
                timer1: null,
                timer2: null,
                date: '---',
                time: '---',
                // systemDate:'',//系统日期
                // systemTime:'', //系统时间
                systemTimer: null,
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
            //系统设置
            showSettings: function () {
                var self = this;
                self.qt.window('/router/zk/leader/settings', null, {width: 980, height: 600});
            },
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
                D = (date.getDate()  < 10 ? '0' + date.getDate(): date.getDate())+ ' ';
                h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
                m = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
                s = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds();
                return Y + M + D + h + m + s;
            },
            refreshPreview:function(){
            	var self = this;
            	ajax.post('/command/vod/see/oneself/user/start', null, function(data){
            		self.qt.linkedWebview('rightBar', $.toJSON({id:'vodUserLocal', params:data}));
            		self.qt.success('操作成功！');
            	});
            }
        },
        mounted: function () {
            var self = this;
            self.qt = new QtContext('header', function () {
                var timeInterval = function () {
                    self.fightDate = new Date().format('yyyy-MM-dd');
                    self.date = new Date().format('yyyy-MM-dd');
                    self.fightTime = new Date().format('HH : mm : ss');
                    self.time = new Date().format('HH : mm : ss');
                };
                timeInterval();
                clearInterval(self.systemTimer);
                self.systemTimer = setInterval(timeInterval, 1000);

                //作战时间
                self.qt.invoke('slotGetCommandTime');
                self.qt.on('commandTime', function (e) {
                    console.log(e)
                    if (e) {
                        var c = new Date(e.data);
                        clearInterval(self.systemTimer);

                        self.fightDate = self.timeFormat(c).split(' ')[0];
                        self.fightTime = self.timeFormat(c).split(' ')[1];
                        clearInterval(self.cleartimer1);
                        self.cleartimer1 = setInterval(function () {
                            c.setSeconds(c.getSeconds() + 1);//+1s
                            self.fightDate = self.timeFormat(c).split(' ')[0];
                            self.fightTime = self.timeFormat(c).split(' ')[1];
                        }, 1000);
                    }
                });

                //天文时间
                self.qt.invoke('slotGetAstroTime');
                self.qt.on('astroTime', function (e) {
                    if (e) {
                        var c = new Date(e.data);
                        clearInterval(self.systemTimer);
                        self.date = self.timeFormat(c).split(' ')[0];
                        self.time = self.timeFormat(c).split(' ')[1];
                        clearInterval(self.cleartimer2);
                        self.cleartimer2 = setInterval(function () {
                            c.setSeconds(c.getSeconds() + 1);//+1s
                            self.date = self.timeFormat(c).split(' ')[0];
                            self.time = self.timeFormat(c).split(' ')[1];
                        }, 1000);
                    }
                });

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
                    self.usernameLength = self.user.name.length;
                    self.qt.on('signalRecvMsgFromHtml', function (msg) {
                        alert('header' + msg);
                    });
                });

                clearInterval(self.timer1);
                self.timer1 = setInterval(function () {
                    self.qt.invoke('slotGetCommandTime');
                }, 30000);

                clearInterval(self.timer2);
                self.timer2 = setInterval(function () {
                    self.qt.invoke('slotGetAstroTime');
                }, 30000);
            });
        }
    });

    return Vue;
});