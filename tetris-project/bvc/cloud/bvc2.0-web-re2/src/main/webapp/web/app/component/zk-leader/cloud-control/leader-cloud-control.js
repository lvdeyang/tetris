define([
    'text!' + window.APPPATH + 'component/zk-leader/cloud-control/leader-cloud-control.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'date',
    'css!' + window.APPPATH + 'component/zk-leader/cloud-control/leader-cloud-control.css'
], function (tpl, ajax, $, Vue, config) {

    //组件名称
    var pluginName = 'leader-cloud-control';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data: function () {
            return {
                protocolUp: false,
                protocolDown: false,
                protocolLeft: false,
                protocolRight: false,
                speed: 8,
                direct: 0,
                points: [],
                serial: '',
                dialog: {
                    addPoint: {
                        visible: false,
                        name: '',
                        loading: false
                    }
                }
            }
        },
        methods: {
            //关闭弹窗
            closeDialog: function () {
                this.qt.destroy();
            },
            //qt实现鼠标抬起事件
            qtFunction: function () {
                var self = this;
                console.log(self.protocolUp)
                self.qt.on('mouseup', function (e) {
                    if(self.protocolUp || self.protocolDown || self.protocolLeft || self.protocolRight) {
                        self.stop();
                        self.direct = 0;
                        self.protocolUp=false;
                        self.protocolDown=false;
                        self.protocolLeft= false;
                        self.protocolRight=false;
                    }
                    self.qt.unbind('mouseup');

                });
            },
            //垂直事件
            vertical: function (direction) {
                var self = this;
                if(direction == 'UP'){
                    self.protocolUp=true;
                    self.protocolDown=false;
                    self.protocolLeft= false;
                    self.protocolRight=false;
                }else{
                    self.protocolUp=false;
                    self.protocolDown = true;
                    self.protocolLeft= false;
                    self.protocolRight=false;
                }
                ajax.post('/zk/cloud/control/vertical', {
                    direction: direction,
                    serial: self.serial,
                    speed: self.speed
                }, function () {
                    self.qtFunction();
                });
            },
            //水平事件
            horizontal: function (direction) {
                var self = this;
                if(direction == 'LEFT'){
                    self.protocolUp=false;
                    self.protocolDown = false;
                    self.protocolLeft=true;
                    self.protocolRight=false;
                }else{
                    self.protocolUp=false;
                    self.protocolDown = false;
                    self.protocolLeft=false;
                    self.protocolRight = true;
                }
                ajax.post('/zk/cloud/control/horizontal', {
                    direction: direction,
                    serial: self.serial,
                    speed: self.speed
                }, function () {
                    self.qtFunction();
                });
            },
            //镜头变倍控制
            zoom: function (direction) {
                var self = this;
                self.protocolUp=false;
                self.protocolDown = false;
                self.protocolLeft=false;
                self.protocolRight = false;
                if (direction == 'OUT') {
                    self.direct = 1;
                } else if (direction == 'IN') {
                    self.direct = 2;
                }
                ajax.post('/zk/cloud/control/zoom', {
                    direction: direction,
                    serial: self.serial,
                    speed: self.speed
                }, function () {
                    self.qtFunction();
                });
            },
            // 焦距控制
            focus: function (direction) {
                var self = this;
                self.protocolUp=false;
                self.protocolDown = false;
                self.protocolLeft=false;
                self.protocolRight = false;
                if (direction == 'NEAR') {
                    self.direct = 3;
                } else if (direction == 'FAR') {
                    self.direct = 4;
                }
                ajax.post('/zk/cloud/control/focus', {
                    direction: direction,
                    serial: self.serial,
                    speed: self.speed
                }, function () {
                    self.qtFunction();
                });
            },
            // 光圈控制
            aperture: function (direction) {
                var self = this;
                self.protocolUp=false;
                self.protocolDown = false;
                self.protocolLeft=false;
                self.protocolRight = false;
                if (direction == 'MINUS') {
                    self.direct = 5;
                } else if (direction == 'PLUS') {
                    self.direct = 6;
                }
                ajax.post('/zk/cloud/control/aperture', {
                    direction: direction,
                    serial: self.serial,
                    speed: self.speed
                }, function () {
                    self.qtFunction();
                });
            },
            //停止
            stop: function (fn) {
                var self = this;
                ajax.post('/zk/cloud/control/stop', {
                    serial: self.serial
                }, function () {
                    self.protocolUp=false;
                    self.protocolDown = false;
                    self.protocolLeft=false;
                    self.protocolRight = false;
                    if (typeof fn === 'function') fn();
                });
            },
            //删除预置点
            handlePointRemove: function (point) {
                var self = this;
                var h = self.$createElement;
                self.$msgbox({
                    title: '操作提示',
                    message: h('div', null, [
                        h('div', {class: 'el-message-box__status el-icon-warning'}, null),
                        h('div', {class: 'el-message-box__message'}, [
                            h('p', null, ['是否要删除此接入点?'])
                        ])
                    ]),
                    type: 'wraning',
                    showCancelButton: true,
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    beforeClose: function (action, instance, done) {
                        instance.confirmButtonLoading = true;
                        if (action === 'confirm') {
                            ajax.post('/zk/cloud/control/remove/point', {
                                id: point.id,
                                serial: self.serial
                            }, function (data, status) {
                                instance.confirmButtonLoading = false;
                                done();
                                if (status !== 200) return;
                                for (var i = 0; i < self.points.length; i++) {
                                    if (self.points[i].id === point.id) {
                                        self.points.splice(i, 1);
                                        break;
                                    }
                                }
                            }, null, [403, 404, 409, 500]);
                        } else {
                            instance.confirmButtonLoading = false;
                            done();
                        }
                    }
                }).catch(function () {
                });
            },
            //添加预置点的对话框显示
            handlePointAdd: function () {
                var self = this;
                self.dialog.addPoint.visible = true;
            },
            //关闭预置点弹框事件
            handleAddPointClose: function () {
                var self = this;
                self.dialog.addPoint.visible = false;
                self.dialog.addPoint.loading = false;
                self.dialog.addPoint.name = '';
            },
            //添加预置点的添加事件
            handleAddPointCommit: function () {
                var self = this;
                if (!self.dialog.addPoint.name) {
                    self.$message({
                        type: 'error',
                        message: '预置点名称不能为空！'
                    });
                    return;
                }
                self.dialog.addPoint.loading = true;
                ajax.post('/zk/cloud/control/add/point', {
                    serial: self.serial,
                    name: self.dialog.addPoint.name
                }, function (data, status) {
                    self.dialog.addPoint.loading = false;
                    if (status !== 200) return;
                    if (data) {
                        self.points.push(data);
                    }
                    self.handleAddPointClose();
                }, null, [403, 404, 408, 409]);
            },
            //设置预置点
            handlePointInvoke: function (point) {
                var self = this;
                var h = self.$createElement;
                self.$msgbox({
                    title: '操作提示',
                    message: h('div', null, [
                        h('div', {class: 'el-message-box__status el-icon-warning'}, null),
                        h('div', {class: 'el-message-box__message'}, [
                            h('p', null, ['是否要设置到此预置点?'])
                        ])
                    ]),
                    type: 'wraning',
                    showCancelButton: true,
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    beforeClose: function (action, instance, done) {
                        instance.confirmButtonLoading = true;
                        if (action === 'confirm') {
                            ajax.post('/zk/cloud/control/invoke/point', {id: point.id}, function (data, status) {
                                instance.confirmButtonLoading = false;
                                done();
                                if (status !== 200) return;
                                if (!point.__active) {
                                    for (var i = 0; i < self.points.length; i++) {
                                        if (self.points[i].__active) {
                                            Vue.set(self.points[i], '__active', false);
                                            break;
                                        }
                                    }
                                    Vue.set(point, '__active', true);
                                }
                            }, null, [403, 404, 409, 500]);
                        } else {
                            instance.confirmButtonLoading = false;
                            done();
                        }
                    }
                }).catch(function () {
                });
            },
            //获取预置点数据
            checkPoints: function () {
                var self = this;
                ajax.post('/zk/cloud/control/load/points', {
                    serial: self.serial
                }, function (data) {
                    if (data && data.length > 0) {
                        for (var i = 0; i < data.length; i++) {
                            self.points.push(data[i]);
                        }
                    }
                });
            }
        },
        mounted: function () {
            var self = this;
            self.qt = new QtContext('leaderCloudControl', function () {
                //初始化ajax
                ajax.init({
                    login: config.ajax.login,
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

                var params = self.qt.getWindowParams();
                self.serial = params.serial;
                self.checkPoints();
            });
        }
    });

    return Vue;
});