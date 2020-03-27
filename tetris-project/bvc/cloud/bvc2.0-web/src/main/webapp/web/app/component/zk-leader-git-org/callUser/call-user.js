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
            //关闭弹框事件
            handleWindowClose: function () {
                var self = this;
                self.qt.destroy();
            },
            //呼叫等的按钮共用事件
            callMemberCommit: function () {
                var self = this;
                if (!self.tree.select) {
                    self.qt.alert('提示信息', '您没有勾选任何用户');
                    return;
                }
                if (self.type === 'call') { //呼叫
                    ajax.post('/command/call/user/start/player', {
                        userId: self.tree.select,
                        serial:self.screenId
                    }, function (data) {
                        //动态弹窗，qt无法判断是哪个弹框，需要连到已有channel上，然后执行。解决播放器不实时更新，需要手动刷新的问题
                        self.qt.linkedWebview('hidden',{id:'playCall', params:$.toJSON([data])});
                        self.handleWindowClose();
                    });
                } else if (self.type === 'orient') { //专项
                    ajax.post('/command/secret/start/player', {
                        userId: self.tree.select,
                        serial:self.screenId
                    }, function (data) {
                        self.qt.linkedWebview('hidden',{id:'playSecret', params:$.toJSON(data)});
                        self.handleWindowClose();
                    });
                } else if (self.type === 'voice') { //语音
                    ajax.post('/command/voice/intercom/start/player', {
                        userId: self.tree.select,
                        serial:self.screenId
                    }, function (data) {
                        self.qt.linkedWebview('hidden',{id:'playVoice', params:$.toJSON([data])});
                        self.handleWindowClose();
                    });
                }

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