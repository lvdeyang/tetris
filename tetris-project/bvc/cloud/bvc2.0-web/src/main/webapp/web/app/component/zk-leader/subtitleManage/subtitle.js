define([
    'text!' + window.APPPATH + 'component/zk-leader/subtitleManage/subtitle.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'css!' + window.APPPATH + 'component/zk-leader/subtitleManage/subtitle.css'
], function (tpl, ajax, $, Vue, config) {

    //组件名称
    var pluginName = 'subtitle';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data: function () {
            return {
                baseUrl: window.BASEPATH,
                currentPage: 1,
                data: [],
                total:0,
                pageSize:50,
                fontData: [],
                dialogFormVisible: false,
                form: {
                    name: '',
                    font: '黑体',
                    fontsize: 5,
                    color: '#409EFF',
                    textarea: ''
                },
                formLabelWidth: '80px',
                btnShow:false,
                editId:''
            }
        },
        computed: {
            
        },
        methods: {
            //当前页改变
            currentChange: function (val) {
                this.currentPage = val;
                this.refreshData();
            },
            //获取数据
            refreshData: function () {
                var self = this;
                self.data.splice(0,self.data.length);
                ajax.post('/monitor/subtitle/load', {
                    currentPage: self.currentPage,
                    pageSize: self.pageSize
                }, function (data) {
                	var rows = data.rows;
                	var total = data.total;
                	if(rows && rows.length>0){
                		for(var i=0; i<rows.length; i++){
                			self.data.push(rows[i]);
                		}
                		self.total = total;
                	}
                });
            },
            //获取字体数据
            getFontData: function () {
                var self = this;
                ajax.post('/monitor/subtitle/query/fonts', null, function (data) {
                    self.fontData = data;
                });
            },
            //添加提交
            addSubtitle: function () {
                var self = this;
                if(self.form.textarea){
                	var length = new Blob([self.form.textarea],{type : 'text/plain'}).size;
                	if(length > 64){
                		self.qt.error('字幕内容过长，最多支持中文21个，数字英文64个');
                		return;
                	}
                }else{
                	self.qt.error('字幕内容不能为空');
                	return;
                }
                
                ajax.post('/monitor/subtitle/add', {
                    name: self.form.name,
                    content: self.form.textarea,
                    font: self.form.font,
                    height: self.form.fontsize,
                    color: self.form.color.split('#')[1]
                }, function () {
                    self.qt.success('添加成功');
                    self.dialogFormVisible = false;
                    self.refreshData();
                });
                self.btnShow=false;
            },
            //修改
            editRow: function (row) {
                this.dialogFormVisible = true;
                this.form.name = row.name;
                this.form.font = row.font;
                this.form.fontsize = parseInt(row.height);
                this.form.color = '#' + row.color;
                this.form.textarea = row.content;
                this.btnShow=true;
                this.editId=row.id;
            },
            editSubtitle:function () {
                var self = this;
                if(self.form.textarea){
                	var length = new Blob([self.form.textarea],{type : 'text/plain'}).size;
                	if(length > 64){
                		self.qt.error('字幕内容过长，最多支持中文21个，数字英文64个');
                		return;
                	}
                }else{
                	self.qt.error('字幕内容不能为空');
                	return;
                }
                ajax.post('/monitor/subtitle/edit/' + self.editId, {
                    name: self.form.name,
                    content: self.form.textarea,
                    font: self.form.font,
                    height: self.form.fontsize,
                    color: self.form.color.split('#')[1]
                }, function (data) {
                    self.qt.success('修改成功');
                    self.dialogFormVisible = false;
                    self.refreshData();
                })
            },
            //删除
            deleteRow:function (id) {
                var self=this;
                ajax.post('/monitor/subtitle/remove/'+id,null,function (data) {
                    self.qt.success('删除成功');
                    for(var i=0; i<self.data.length; i++){
                    	if(self.data[i].id == id){
                    		self.data.splice(i, 1);
                    		self.total -= 1;
                    		break;
                    	}
                    }
                    if(self.data.length <= 0){
                    	if(self.currentPage > 1){
                    		self.currentPage -= 1;
                    		self.refreshData();
                    	}else if(self.total > 0){
                    		self.currentPage = 1;
                    		self.refreshData();
                    	}
                    }
                })
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

                self.refreshData();
                self.getFontData();

                self.qt.on('close', function () {
                    self.qt.destroy();
                });
            });
        }
    });

    return Vue;
});