define([
    'text!' + window.APPPATH + 'component/zk-leader/callUser/call-user.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'css!' + window.APPPATH + 'component/zk-leader/callUser/call-user.css'
], function (tpl, ajax, $, Vue, config) {

    //组件名称
    var pluginName = 'call-user';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data: function () {
            return {
                baseUrl: window.BASEPATH,
                qt: '',
                screenId: '', //在第几屏播放
                type: '', //打开哪个页面
                tree: {
                    props: {
                        children: 'children',
                        label: 'name'
                    },
                    data: [],
                    select: ''
                }
            }
        },
        methods: {
            handleWindowClose: function () {
                var self = this;
                self.qt.destroy();
            },
            callMemberCommit: function () {
                var self = this;
                if (!self.tree.select) {
                    self.qt.alert('提示信息', '您没有勾选任何用户');
                    return;
                }
                //TODO:接口需要。需要传给后台第几屏的参数
                if (self.type === 'call') {
                    console.log('call')
                    console.log(self.tree.select)
                } else if (self.type === 'orient') {
                    console.log('orient')
                } else if (self.type === 'voice') {
                    console.log('voice')
                }
                ajax.post('/command/call/user/start', {
                    userId: self.screenId
                }, function (data) {
                    console.log(data)
                    self.qt.invoke('callUsers', $.toJSON([data]));
                });
            }
        },
        mounted: function () {
            var self = this;
            self.qt = new QtContext('callUser', function () {
                var params = self.qt.getWindowParams();
                self.screenId = params.serial;
                self.type = params.type;

                //初始化ajax
                ajax.init({
                    login: config.ajax.login,
                    authname: config.ajax.authname,
                    debug: config.ajax.debug,
                    messenger: {
                        info: function (message, status) {
                            self.qt.info(message)
                        },
                        success: function (message, status) {
                            self.qt.success(message)
                        },
                        warning: function (message, status) {
                            self.qt.warning(message)
                        },
                        error: function (message, status) {
                            self.qt.error(message)
                        }
                    }
                });

                //获取树列表
                self.tree.data.splice(0, self.tree.data.length);
                ajax.post('/command/query/find/institution/tree/user/filter/0', null, function (data) {
                    if (data && data.length > 0) {
                        for (var i = 0; i < data.length; i++) {
                            self.tree.data.push(data[i]);
                        }
                    }
                });
            });
        }
    });

    return Vue;
});