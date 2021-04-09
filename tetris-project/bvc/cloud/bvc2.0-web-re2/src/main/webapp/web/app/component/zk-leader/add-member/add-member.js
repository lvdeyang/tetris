define([
    'text!' + window.APPPATH + 'component/zk-leader/add-member/add-member.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'css!' + window.APPPATH + 'component/zk-leader/add-member/add-member.css'
], function (tpl, ajax, $, Vue, config) {

    //组件名称
    var pluginName = 'add-member';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data: function () {
            return {
                baseUrl: window.BASEPATH,
                qt: '',
                groupId: '',
                groupType: '',
                filterText: '',
                tree: {
                    props: {
                        children: 'children',
                        label: 'name'
                    },
                    data: [],
                    select: []
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
            handleAddMemberCommit: function () {
                var self = this;
                if (self.tree.select.length <= 0) {
                    self.qt.warning('提示信息', '您没有勾选任何用户');
                    return;
                }
                var members = [];
                var hallIds = [];
                for (var i = 0; i < self.tree.select.length; i++) {
                    var node = self.tree.select[i];
                    if(node.type === 'USER'){
                        members.push(node.id);
                    }else if(node.type === 'CONFERENCE_HALL'){
                        hallIds.push(node.id);
                    }
                }
                ajax.post('/command/basic/add/members', {
                    id: self.groupId,
                    members: $.toJSON(members),
                    hallIds: $.toJSON(hallIds)
                }, function (data) {
                    self.qt.linkedWebview('rightBar', {
                        id: 'commandMemberAdd',
                        params: $.toJSON(data),
                        type: self.groupType
                    });
                    self.handleWindowClose();
                });
            }
        },
        mounted: function () {
            var self = this;
            self.qt = new QtContext('leaderAddMember', function () {
                var params = self.qt.getWindowParams();
                self.groupId = params.id;
                self.groupType = params.type;

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
                ajax.post('/command/query/find/institution/tree/user/except/command', {id: self.groupId}, function (data) {
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