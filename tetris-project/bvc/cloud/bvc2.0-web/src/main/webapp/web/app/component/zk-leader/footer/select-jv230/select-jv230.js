define([
    'text!' + window.APPPATH + 'component/zk-leader/footer/select-jv230/select-jv230.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'css!' + window.APPPATH + 'component/zk-leader/footer/select-jv230/select-jv230.css'
], function (tpl, ajax, $, Vue, config) {

    //组件名称
    var pluginName = 'select-jv230';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data: function () {
            return {
                baseUrl: window.BASEPATH,
                qt: '',
                filterText: '',
                tree: {
                    props: {
                        children: 'children',
                        label: 'name'
                    },
                    data: [],
                    current:''
                }
            }
        },
        watch: {
            filterText:function(val) {
                this.$refs.tree.filter(val);
            }
        },
        methods: {
            //树形组件自定义过滤方法, 触发页面显示配置的筛选
            filterNode:function(value, data, node) {
                // 如果什么都没填就直接返回
                if (!value) return true;
                // 如果传入的value和data中的label相同说明是匹配到了
                if (data.name.indexOf(value) !== -1) {
                    return true;
                }
                // 否则要去判断它是不是选中节点的子节点
                return this.checkBelongToChooseNode(value, data, node);
            },
            // 判断传入的节点是不是选中节点的子节点
            checkBelongToChooseNode:function(value, data, node) {
                var level = node.level;
                // 如果传入的节点本身就是一级节点就不用校验了
                if (level === 1) {
                    return false;
                }
                // 先取当前节点的父节点
                var parentData = node.parent;
                // 遍历当前节点的父节点
                var index = 0;
                while (index < level - 1) {
                    // 如果匹配到直接返回
                    if (parentData.data.name.indexOf(value) !== -1) {
                        return true;
                    }
                    // 否则的话再往上一层做匹配
                    parentData = parentData.parent;
                    index ++;
                }
                // 没匹配到返回false
                return false;
            },
            //树形结构的复选框事件
            onUserCheckChange: function (data) {
                var self = this;
                for (var i = 0; i < self.tree.select.length; i++) {
                    if (self.tree.select[i] === data) {
                        self.tree.select.splice(i, 1);
                        return;
                    }
                }
                self.tree.select.push(data);
            },
            //关闭弹框事件
            handleWindowClose: function () {
                var self = this;
                self.qt.destroy();
            },
            //提交按钮事件
            handleSelectJv230Commit: function () {
                var self = this;
                //var node = self.$refs.tree.getCurrentNode();
                var node = self.tree.current;
                if(!node){
                    self.qt.error('您没有选择设备');
                    return;
                }
                ajax.post('/tetris/bvc/business/jv230/forward/total/forward', {
                    bundleId:node.id
                }, function(data){
                    self.qt.success('上屏成功!');
                    self.handleWindowClose();
                });
            }
        },
        mounted: function () {
            var self = this;
            self.qt = new QtContext('zkFooterSelectJv230', function () {

                //初始化ajax
                ajax.init({
                    login: config.ajax.login,
                    authname: config.ajax.authname,
                    debug: config.ajax.debug,
                    messenger: {
                        info: function (message) {
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

                self.tree.data.splice(0, self.tree.data.length);
                /*ajax.post('/tetris/bvc/business/jv230/forward/query/usable/jv230/bundles', null, function(data){
                    if(data && data.length>0){
                        for(var i=0; i<data.length; i++){
                            self.tree.data.push(data[i]);
                        }
                    }
                });*/
                ajax.post('/tetris/bvc/business/jv230/forward/query/usable/bundles', null, function(data){
                    if(data && data.length>0){
                        for(var i=0; i<data.length; i++){
                            self.tree.data.push(data[i]);
                        }
                    }
                });
            });
        }
    });

    return Vue;
});