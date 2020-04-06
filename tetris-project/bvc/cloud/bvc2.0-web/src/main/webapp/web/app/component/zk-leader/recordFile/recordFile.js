define([
    'text!' + window.APPPATH + 'component/zk-leader/recordFile/recordFile.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'css!' + window.APPPATH + 'component/zk-leader/recordFile/recordFile.css'
], function (tpl, ajax, $, Vue, config) {

    //组件名称
    var pluginName = 'record-file';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data: function () {
            return {
                baseUrl: window.BASEPATH,
                user: '',
                qt: '',
                props: {
                    children: 'children',
                    label: 'name'
                },
                //设备/用户树
                record: {
                    data: [
                        {
                            label: '一级 1',
                            children: [{
                                label: '二级 1-1',
                                children: [{
                                    label: '三级 1-1-1'
                                }]
                            }]
                        }, {
                            label: '一级 2',
                            children: [{
                                label: '二级 2-1',
                                children: [{
                                    label: '三级 2-1-1'
                                }]
                            }, {
                                label: '二级 2-2',
                                children: [{
                                    label: '三级 2-2-1'
                                }]
                            }]
                        }, {
                            label: '一级 3',
                            children: [{
                                label: '二级 3-1',
                                children: [{
                                    label: '三级 3-1-1'
                                }]
                            }, {
                                label: '二级 3-2',
                                children: [{
                                    label: '三级 3-2-1'
                                }]
                            }]
                        }
                    ],
                    select: ''
                },
                //录制文件树
                files: [],
                currentPage: 1,
                startTime: '',
                endTime: '',
                date: '',
                fileSelect:'' //选择的文件
            }
        },
        methods: {
            currentChange: function (val) {
                this.currentPage = val;
            },
            //获取左侧树形菜单的数据
            refreshRecordFile: function () {
                var self = this;
                self.record.data.splice(0, self.record.data.length);
                ajax.post('/monitor/device/find/institution/tree/0/false', null, function (data) {
                    if (data && data.length > 0) {
                        for (var i = 0; i < data.length; i++) {
                            self.record.data.push(data[i]);
                        }
                    }
                });
            },
            //确定选择 左侧
            confirm: function () {
                var self = this;
                console.log(self.record.select)
                var data = self.record.select;
                var id = data.type == 'BUNDLE' ? data.id : null;
                if (!self.record.select) {
                    self.qt.warning('请先选择左侧列表中设备或用户');
                    return;
                }
                ajax.post('/monitor/record/playback/find/by/condition', {
                    type: data.type,
                    bundleRealType: data.param.realType,//type为BUNDLE时，如果BUNDLE为XT设备传XT
                    bundleId: id, //设备id,type为BUNDLE时设置此字段
                    recordUserId: '', // 录制的用户id,type为USER时设置此字段
                    currentPage: self.currentPage,
                    pageSize: 10
                }, function (data) {
                    if(data.rows && data.rows.length>0){
                    self.files = data.rows;
                    }
                })
            },
            //日期筛选
            filtrate: function () {
                var self = this;
                var date = self.date;
            },
            //删除文件
            deleteFile: function (id) {
                ajax.post('/monitor/record/remove/file/' + id, null, function (data) {
                    console.log(data)
                })
            },
            //提交
            confirmSubmit:function () {
                //TODO:调播放
                console.log(this.fileSelect)
                //播放地址： this.fileSelect.previewUrl
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
            },
            // 关闭页面
            close: function () {
                this.qt.destroy();
            }
        },
        mounted: function () {
            var self = this;
            self.qt = new QtContext('recordFile', function () {
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

                self.refreshRecordFile();

                self.qt.get(['user'], function (variables) {
                    self.user = variables.user ? $.parseJSON(variables.user) : {};
                });
            });
        }
    });

    return Vue;
});