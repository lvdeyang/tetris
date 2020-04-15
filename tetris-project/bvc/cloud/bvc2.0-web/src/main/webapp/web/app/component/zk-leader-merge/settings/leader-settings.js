define([
    'text!' + window.APPPATH + 'component/zk-leader/settings/leader-settings.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'css!' + window.APPPATH + 'component/zk-leader/settings/leader-settings.css'
], function (tpl, ajax, $, Vue, config) {

    //组件名称
    var pluginName = 'zk-settings';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data: function () {
            var self = this;
            var checkOldPass = function (rule, value, callback) {
                if (!value) {
                    return callback(new Error('请输入旧密码'));
                }
                if(/[^a-zA-Z0-9_]/.test(value)) {
                    return callback(new Error('只能输入数字、英文，_，不能输入特殊字符'))
                }
                callback();
            };
            var validatePass = function (rule, value, callback) {
                if (value === '') {
                    callback(new Error('请输入密码'));
                } else {
                    if(/[^a-zA-Z0-9_]/.test(value)) {
                        return callback(new Error('只能输入数字、英文，_，不能输入特殊字符'))
                    }
                    if (self.ruleForm.checkPass !== '') {
                        self.$refs.ruleForm.validateField('checkPass');
                    }
                    callback();
                }
            };
            var validatePass2 = function (rule, value, callback) {
                if (value === '') {
                    callback(new Error('请再次输入密码'));
                } else if (value !== self.ruleForm.newPass) {
                    callback(new Error('两次输入密码不一致!'));
                } else {
                    if(/[^a-zA-Z0-9_]/.test(value)) {
                        return callback(new Error('只能输入数字、英文,_，不能输入特殊字符'))
                    }
                    callback();
                }
            };
            return {
                baseUrl: window.BASEPATH,
                callResponseMode: '0',
                vodMode: '0',
                videoSendAnswerMode: '0',
                commandMeetingAutoStart: '0',
                subtitle: '0',
                visibleRange: '0',
                recordMode: '0',
                dedicatedMode: '0',
                user:'',
                //获取网关信息
                gateIp:'',  //网关ip
                gatePort:'',  //网关端口
                //修改密码
                ruleForm: {
                    oldPass: '',
                    newPass: '',
                    checkPass: ''
                },
                rules: {
                    newPass: [
                        {validator: validatePass, trigger: 'change', required: "*必填"}
                    ],
                    checkPass: [
                        {validator: validatePass2, trigger: 'change', required: "*必填"}
                    ],
                    oldPass: [
                        {validator: checkOldPass, trigger: 'change', required: "*必填"}
                    ]
                },
                //转发关系
                table2:{
                    data:[
                    ],
                    total:0,
                    currentPage:1,
                    pageSize:50
                },
                loading: true
            }
        },
        methods: {
            //呼叫应答方式的选择
            handleCallResponseModeChange: function (v) {
                var self = this;
                ajax.post('/command/settings/call/response/mode', {mode: parseInt(v) ? 'auto' : 'manual'}, function (data, status) {
                    if (status !== 200) {
                        self.callResponseMode = self.callResponseMode ? '0' : '1';
                    } else {
                        self.qt.linkedWebview('rightBar', {
                            id: 'responseModeChange',
                            params: parseInt(v) ? 'auto' : 'manual'
                        });
                    }
                }, null, [403, 404, 408, 409, 500]);
            },
            //设置点播方式
            handleVodModeChange: function (v) {
                var self = this;
                ajax.post('/command/settings/vod/mode', {mode: parseInt(v) ? 'multicast' : 'unicast'}, function (data, status) {
                    if (status !== 200) {
                        self.vodMode = self.vodMode ? '0' : '1';
                    }
                }, null, [403, 404, 408, 409, 500]);
            },
            //视频转发应答方式
            handleVideoSendAnswerModeChange: function (v) {
                var self = this;
                ajax.post('/command/settings/video/send/answer/mode', {mode: parseInt(v) ? 'auto' : 'manual'}, function (data, status) {
                    if (status !== 200) {
                        self.videoSendAnswerMode = self.videoSendAnswerMode ? '0' : '1';
                    } else {
                        self.qt.linkedWebview('rightBar', {
                            id: 'sendAnswerModeChange',
                            params: parseInt(v) ? 'auto' : 'manual'
                        });
                    }
                }, null, [403, 404, 408, 409, 500]);
            },
            //设置启动模式
            handleCommandMeetingAutoStartChange: function (v) {
                var self = this;
                ajax.post('/command/settings/command/meeting/auto/start', {mode: parseInt(v) ? 'auto' : 'manual'}, function (data, status) {
                    if (status !== 200) {
                        self.commandMeetingAutoStart = self.commandMeetingAutoStart ? '0' : '1';
                    }
                }, null, [403, 404, 408, 409, 500]);
            },
            //设置字幕设置
            handleSubtitleChange: function (v) {
                var self = this;
                ajax.post('/command/settings/subtitle', {mode: parseInt(v) ? 'close' : 'open'}, function (data, status) {
                    if (status !== 200) {
                        self.subtitle = self.subtitle ? '0' : '1';
                    }
                }, null, [403, 404, 408, 409, 500]);
            },
            //设置可见范围
            handleVisibleRangeChange: function (v) {
                var self = this;
                ajax.post('/command/settings/visible/range', {mode: parseInt(v) ? 'all' : 'group'}, function (data, status) {
                    if (status !== 200) {
                        self.visibleRange = self.visibleRange ? '0' : '1';
                    }
                }, null, [403, 404, 408, 409, 500]);
            },
            //设置录像模式
            handleRecordModeChange: function (v) {
                var self = this;
                ajax.post('/command/settings/record/mode', {mode: parseInt(v) ? 'auto' : 'manual'}, function (data, status) {
                    if (status !== 200) {
                        self.recordMode = self.recordMode ? '0' : '1';
                    }
                }, null, [403, 404, 408, 409, 500]);
            },
            //设置专向方式
            handleDedicatedModeChange: function (v) {
                var self = this;
                ajax.post('/command/settings/dedicated/mode', {mode: parseInt(v) ? 'noAudio' : 'noAudioAndVideo'}, function (data, status) {
                    if (status !== 200) {
                        self.dedicatedMode = self.dedicatedMode ? '0' : '1';
                    }
                }, null, [403, 404, 408, 409, 500]);
            },
            //关闭弹框
            closeMask: function () {
                this.qt.destroy();
                this.resetForm();
            },
            //-------------密码相关
            //提交修改密码
            submitForm: function (formName) {
                var self=this;
                this.$refs[formName].validate(function (valid) {
                    if (valid) {
                        // $.ajax({
                        //     url: 'http://'+self.gateIp+':'+self.gatePort+'/user/api/modifyPassword',
                        //     type: 'post',
                        //     // 设置的是请求参数
                        //     data:{
                        //         userId:self.user.id,
                        //         oldPassword:self.ruleForm.oldPass,
                        //         newPassword:self.ruleForm.newPass
                        //     },
                        //     // 用于设置响应体的类型 注意 跟 data 参数没关系！！！
                        //     // dataType: 'json',
                        //     success: function (res) {
                        //         console.log(res)
                        //         // 一旦设置的 dataType 选项，就不再关心 服务端 响应的 Content-Type 了
                        //         // 客户端会主观认为服务端返回的就是 JSON 格式的字符串
                        //         console.log(res)
                        //     },
                        //     error:function (res) {
                        //         console.log(res)
                        //     }
                        // })

                        ajax.editPsdPost('http://' + self.gateIp + ':' + self.gatePort + '/user/api/modifyPassword',
                            {
                                userId: self.user.id,
                                oldPassword: self.ruleForm.oldPass,
                                newPassword: self.ruleForm.newPass
                            },
                            function (data, status) {
                            	if(status == 200){
                            		self.qt.success('修改密码成功');
                                   self.closeMask();
                            	}
                            }, null, [403]);
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            //重置密码
            resetForm: function (formName) {
                this.$refs[formName].resetFields();
            },
            //-------------转发关系
            //当前页改变
            currentChange: function (currentPage) {
                var self = this;
                self.load(currentPage);
            },
            //获取转发关系数据
            loadAllForward: function () {
                var self = this;
                self.loading=true;
                ajax.post('/command/system/all/forward', {}, function (data, status) {
                    self.loading=false;
                    if(status !== 200) return;
                    var rows = data.rows;
                    self.table2.data.splice(0, self.table2.data.length);
                    if (rows && rows.length > 0) {
                        for (var i = 0; i < rows.length; i++) {
                            self.table2.data.push(rows[i]);
                        }
                    }
                    self.table2.total = data.total;
                }, null, [403]);
            }
        },
        mounted: function () {
            var self = this;
            self.qt = new QtContext('leaderSetting', function () {
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
                //查询设置
                ajax.post('/command/settings/query/all', null, function (data) {
                    self.callResponseMode = data.responseMode === 'auto' ? '1' : '0';
                    self.videoSendAnswerMode = data.sendAnswerMode === 'auto' ? '1' : '0';
                });

                self.loadAllForward();
            });
        }
    });

    return Vue;
});