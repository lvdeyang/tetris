define([
    'text!' + window.APPPATH + 'component/zk-leader/subtitle-layer/subtitleLayer.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'css!' + window.APPPATH + 'component/zk-leader/subtitle-layer/subtitleLayer.css'
], function (tpl, ajax, $, Vue, config) {

    //组件名称
    var pluginName = 'subtitle-layer';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data: function () {
            return {
                baseUrl: window.BASEPATH,
                leftCurrentPage: 1,
                dialogCurrentPage: 1,
                leftData: [],
                leftTotal:0,
                leftPageSize:30,
                rightData: [],
                gridData: [],
                gridTotal:0,
                gridPageSize:10,
                dialogLeftVisible: false,
                dialogFormVisible: false,
                dialogTableVisible: false,
                name: '',
                form: {
                    layerType: '字幕',
                    x: 0,
                    y: 0,
                    subtitleName: '',
                    subtitle: '', //添加的时候要传的字幕相关的东西
                    osdId: '' //添加的时候要传的Id
                },
                formLabelWidth: '120px',
                rightShow: false, //右侧表格是否显示
                //左侧修改
                editId: '',
                btnShow: false,
                //右侧
                layerType: [],
                rightBtnShow: false, //右侧添加和编辑对话框应该显示确定还是修改按钮
                editRightId: '',
                deleteVisible: false, //删除对话框
                deleteId: ''
            }
        },
        computed: {
            
        },
        methods: {
        	//字幕管理
            subtitleOpen: function () {
                this.qt.window('/router/zk/leader/subtitle/manage', null, {width: '85%', height: '93%'});
            },
            //当前页改变
            leftCurrentChange: function (val) {
                this.leftCurrentPage = val;
                this.refreshLeftData();
            },
            dialogCurrentChange: function (val) {
                this.dialogCurrentPage = val;
                this.refreshSubtitle();
            },
            //获取左侧表格数据
            refreshLeftData: function () {
                var self = this;
                self.leftData.splice(0, self.leftData.length);
                ajax.post('/monitor/osd/load', {
                    currentPage: self.leftCurrentPage,
                    pageSize: self.leftPageSize
                }, function (data) {
                	var rows = data.rows;
                	var total = data.total;
                	if(rows && rows.length>0){
                		for(var i=0; i<rows.length; i++){
                			self.leftData.push(rows[i]);
                		}
                		self.leftTotal = total;
                	}
                });
            },
            //左侧添加提交
            leftConfirm: function () {
                var self = this;
                this.btnShow = false;
                ajax.post('/monitor/osd/add', {
                    name: self.name
                }, function () {
                    self.qt.success('添加成功');
                    self.dialogLeftVisible = false;
                    self.refreshLeftData();
                });
            },
            //左侧修改
            editRow: function (row) {
                this.dialogLeftVisible = true;
                this.name = row.name;
                this.btnShow = true;
                this.editId = row.id;
            },
            editSubtitle: function () {
                var self = this;
                ajax.post('/monitor/osd/edit/' + self.editId, {
                    name: self.name
                }, function (data) {
                    self.qt.success('修改成功');
                    self.dialogLeftVisible = false;
                    self.refreshLeftData();
                })
            },
            //左侧删除
            deleteRow: function (id) {
                var self = this;
                ajax.post('/monitor/osd/remove/' + id, null, function (data) {
                    self.qt.success('删除成功');
                    for(var i=0; i<self.leftData.length; i++){
                    	if(self.leftData[i].id == id){
                    		self.leftData.splice(i, 1);
                    		self.leftTotal -= 1;
                    		break;
                    	}
                    }
                    if(self.leftData.length <= 0){
                    	if(self.leftCurrentPage > 1){
                    		self.leftCurrentPage -= 1;
                    		self.refreshLeftData();
                    	}else if(self.leftTotal > 0){
                    		self.currentPage = 1;
                    		self.refreshLeftData();
                    	}
                    }
                    if(id == self.form.osdId){
                    	 self.rightShow = false;
                         self.form.osdId = '';
                    }
                })
            },

            //右侧
            //获取图层类型
            getLayerType: function () {
                var self = this;
                ajax.post('/monitor/osd/layer/query/types', null, function (data) {
                    self.layerType = data;
                })
            },
            //左侧点击 右侧出现
            clickRow: function (row) {
                var self = this;
                self.rightShow = true;
                self.form.osdId = row.id;
                self.rightData.splice(0, self.rightData.length);
                ajax.post('/monitor/osd/layer/load', {osdId: row.id}, function (data) {
                    self.rightData = data;
                })
            },
            //点击  输入框 出字幕
            handleAddLayerSelectSubtitle: function () {
                this.dialogTableVisible = true;
                this.refreshSubtitle();
            },
            //字幕表格数据
            refreshSubtitle: function () {
                var self = this;
                self.gridData.splice(0, self.gridData.length);
                ajax.post('/monitor/subtitle/load/all', {
                    currentPage: self.dialogCurrentPage,
                    pageSize: self.gridPageSize
                }, function (data) {
                	var rows = data.rows;
                	var total = data.total;
                	if(rows && rows.length>0){
                		for(var i=0; i<rows.length; i++){
                			 self.gridData.push(rows[i]);
                		}
                		self.gridTotal = total;
                	}
                })
            },
            //对话框表格的选中行
            chooseRow: function (row) {
                this.form.subtitleName = row.name;
                this.form.subtitle = row;
            },
            //对话框表格的确定
            chooseConfirm: function () {
                this.dialogTableVisible = false;
            },
            //添加图层
            rightAddConfirm: function () {
                var self = this;
                var url = null;
                this.rightBtnShow = false;
                if (this.form.layerType === '字幕') {
                    ajax.post('/monitor/osd/layer/add/subtitle/layer', {
                        osdId: self.form.osdId,
                        x: parseInt(self.form.x),
                        y: parseInt(self.form.y),
                        layerIndex: self.rightData.length,
                        subtitleId: self.form.subtitle.id,
                        subtitleName: self.form.subtitleName,
                        subtitleUsername: self.form.subtitle.username
                    }, function () {
                        self.qt.success('添加字幕图层成功');
                        self.dialogFormVisible = false;
                        ajax.post('/monitor/osd/layer/load', {osdId: self.form.osdId}, function (data) {
                            self.rightData = data;
                        })
                    });
                    return;
                } else if (this.form.layerType === '日期') {
                    url = '/monitor/osd/layer/add/date/layer';
                } else if (this.form.layerType === '时间') {
                    url = '/monitor/osd/layer/add/datetime/layer';

                } else if (this.form.layerType === '设备名称') {
                    url = '/monitor/osd/layer/add/devname/layer';
                }
                ajax.post(url, {
                    osdId: self.form.osdId,
                    x: parseInt(self.form.x),
                    y: parseInt(self.form.y),
                    layerIndex: self.rightData.length
                }, function () {
                    self.qt.success('添加图层成功');
                    self.dialogFormVisible = false;
                    ajax.post('/monitor/osd/layer/load', {osdId: self.form.osdId}, function (data) {
                        self.rightData = data;
                    })
                })
            },
            editRightRow: function (row) {
                this.dialogFormVisible = true;
                this.form = {
                    layerType: '字幕',
                    x: row.x,
                    y: row.y,
                    subtitleName: row.subtitleName,
                    subtitleId: row.subtitleId,
                    osdId: this.form.osdId //添加的时候要传的Id
                };
                this.rightBtnShow = true;
                this.editRightId = row.id;
            },
            //修改图层
            rightEditConfirm: function () {
                var self = this;
                ajax.post('/monitor/osd/layer/edit/subtitle/layer/' + self.editRightId, {
                    x: self.form.x,
                    y: self.form.y,
                    subtitleId: self.form.subtitle.id,
                    subtitleName: self.form.subtitleName
                }, function (data) {
                    self.qt.success('修改图层成功');
                    self.dialogFormVisible = false;
                    ajax.post('/monitor/osd/layer/load', {osdId: self.form.osdId}, function (data) {
                        self.rightData = data;
                    })
                })
            },
            //删除图层
            deleteRightRow: function (id) {
                this.deleteVisible = true;
                this.deleteId = id;
            },
            // 确定删除
            confirmDel: function () {
                var self = this;
                this.deleteVisible = false;
                ajax.post('/monitor/osd/layer/remove/' + self.deleteId, null, function () {
                    self.qt.success('删除成功');
                    ajax.post('/monitor/osd/layer/load', {osdId: self.form.osdId}, function (data) {
                        self.rightData = data;
                    })
                })
            },
            //排序
            sortRow: function (row, type) {
                var self = this;
                var index = null;
                for (var i = 0; i < self.rightData.length; i++) {
                    if (self.rightData[i] === row) {
                        index = i;
                        break;
                    }
                }
                if (type === 'up') {
                    self.rightData.splice(i, 1);
                    self.rightData.splice(i - 1, 0, row);
                } else {
                    self.rightData.splice(i, 1);
                    self.rightData.splice(i + 1, 0, row);
                }
                self.layerSort();
            },
            layerSort:function(){
                var self = this;
                var layers = [];
                for(var i=0; i<self.rightData.length; i++){
                    layers.push({
                        id:self.rightData[i].id,
                        layerIndex:i
                    });
                }
                if(layers.length > 0){
                    ajax.post('/monitor/osd/layer/sort', {
                        layers: $.toJSON(layers)
                    }, function(){
                        for(var i=0; i<self.rightData.length; i++){
                            self.rightData[i].layerIndex = i;
                        }
                        self.$message({
                            type:'success',
                            message:'重新排序！'
                        })
                    });
                }
            },
            cancel: function () {
                var self = this;
                self.qt.destroy();
            }
        },
        mounted: function () {
            var self = this;
            self.qt = new QtContext('rec', function () {
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

                self.refreshLeftData();
                self.getLayerType();

                self.qt.on('close', function () {
                    self.qt.destroy();
                });
            });
        }
    });

    return Vue;
});