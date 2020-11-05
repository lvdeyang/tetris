define([
    'text!' + window.APPPATH + 'component/zk-leader/set-fightTime/fightTime.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'date',
    'css!' + window.APPPATH + 'component/zk-leader/set-fightTime/fightTime.css'
], function (tpl, ajax, $, Vue, config) {

    //组件名称
    var pluginName = 'fight-time';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data: function () {
            return {
                baseUrl: window.BASEPATH,
                qt: '',
                //表单
                form: {
                    region: '',
                    date: '',
                    time: ''
                },
                command: [],
                fightCommand: ''
                //设置日期不能选当前日期之前的日期
                // pickerOptions1: {
                //     disabledDate:function(time) {
                //         return time.getTime() < Date.now() - 8.64e7;
                //     },
                // }
            }
        },
        methods: {
            //关闭弹框事件
            handleWindowClose: function () {
                var self = this;
                self.qt.destroy();
            },
            //确定按钮提交事件
            submit: function () {
                if(this.date ==='' || this.time ===''){
                    this.qt.warning('提示信息', '请设置作战时间');
                    return;
                }
                if(!this.fightCommand){
                    this.qt.warning('提示信息', '请选择作战指挥');
                    return;
                }
                var date=this.timeFormat(this.form.date).split(' ')[0];
                var time=this.timeFormat(this.form.time).split(' ')[1];
                //TODO:提交给后台
                console.log(date)
                console.log(time)
                console.log(this.fightCommand)
            },
            
            //  格式化时间
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
            self.qt = new QtContext('fightTime', function () {

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

                //    获取指挥列表
                ajax.post('/command/query/list', null, function (data) {
                    console.log(data)
                    if (data && data.length > 0) {
                        self.command = data[0].children;
                    }
                });
            });
        }
    });

    return Vue;
});