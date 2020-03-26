define([
    'text!' + window.APPPATH + 'component/zk-leader/forward-relationship/forward-relation.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'css!' + window.APPPATH + 'component/zk-leader/forward-relationship/forward-relation.css'
], function(tpl, ajax, $, Vue,config){

    //组件名称
    var pluginName = 'forward-relation';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data:function(){
            return {
                baseUrl:window.BASEPATH,
                qt:'',
                table2:{
                    data:[
                    ],
                    total:0,
                    currentPage:1,
                    pageSize:50
                }
            }
        },
        methods: {
            //关闭弹窗
            handleClose: function () {
                var self = this;
                self.qt.destroy();
            },
            //当前页改变
            currentChange: function (currentPage) {
                var self = this;
                self.load(currentPage);
            },
            //获取转发关系数据
            loadAllForward: function () {
                var self = this;
                ajax.post('/command/system/all/forward', {}, function (data) {
                    var rows = data.rows;
                    self.table2.data.splice(0, self.table2.data.length);
                    if (rows && rows.length > 0) {
                        for (var i = 0; i < rows.length; i++) {
                            self.table2.data.push(rows[i]);
                        }
                    }
                    self.table2.total = data.total;
                    self.table2.currentPage = self.table2.currentPage;
                });
            }
        },
        mounted:function(){
            var self = this;
            self.qt = new QtContext('leaderForward', function(){
                var params = self.qt.getWindowParams();
                var id = params.id;

                //初始化ajax
                ajax.init({
                    login:config.ajax.login,
                    authname:config.ajax.authname,
                    debug:config.ajax.debug,
                    messenger:{
                        info:function(message, status){
                            self.qt.info(message)
                        },
                        success:function(message, status){
                            self.qt.success(message)
                        },
                        warning:function(message, status){
                            self.qt.warning(message)
                        },
                        error:function(message, status){
                            self.qt.error(message)
                        }
                    }
                });

                self.loadAllForward();
            });
        }
    });

    return Vue;
});