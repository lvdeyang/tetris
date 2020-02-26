define([
    'text!' + window.APPPATH + 'component/zk-leader/dialog/create-group/leader-dialog-create-group.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'css!' + window.APPPATH + 'component/zk-leader/dialog/create-group/leader-dialog-create-group.css'
], function (tpl, ajax, $, Vue, config) {

    //组件名称
    var pluginName = 'leader-dialog-create-group';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data: function () {
            return {
                baseUrl: window.BASEPATH,
                commandName:'',
                filterText:'',
                tree:{
                    props:{
                        children:'children',
                        label:'name'
                    },
                    institution:{
                        data:[],
                        select:[]
                    }
                }
            }
        },
        watch: {
            filterText:function(val){
                this.$refs.institutionTree.filter(val);
            }
        },
        methods: {
            onUserCheckChange:function(data){
                var self = this;
                for(var i=0; i<self.tree.institution.select.length; i++){
                    if(self.tree.institution.select[i] === data){
                        self.tree.institution.select.splice(i, 1);
                        return;
                    }
                }
                self.tree.institution.select.push(data);
            },
            refreshInstitution:function(){
                var self = this;
                self.tree.institution.data.splice(0, self.tree.institution.data.length);
                ajax.post('/command/query/find/institution/tree/user/filter/0', null, function(data){
                    if(data && data.length>0){
                        for(var i=0; i<data.length; i++){
                            self.tree.institution.data.push(data[i]);
                        }
                    }
                });
            },
            addCommand:function(){
                var self = this;
                if(!self.commandName || !(self.commandName.trim())){
                    self.qt.error('请填写会议名');
                    return;
                }
                var userIds = [];
                /*for(var i=0; i<self.tree.institution.select.length; i++){
                    userIds.push(self.tree.institution.select[i].id);
                }*/
                var nodes = self.$refs.institutionTree.getCheckedNodes();
                for(var i=0; i<nodes.length; i++){
                    if(nodes[i].type === 'USER') userIds.push(nodes[i].id);
                }
                ajax.post('/command/basic/save', {
                    members: $.toJSON(userIds),
                    name:self.commandName.trim()
                }, function(data){
                    self.startCommand(data.id, function(){
                        self.qt.linkedWebview('rightBar', {id:'currentGroupChange', params: $.toJSON([data])});
                    });
                });
            },
            startCommand:function(commandId, fn){
                var self = this;
                ajax.post('/command/basic/start', {
                    id:commandId
                }, function(data){
                    fn();
                    setTimeout(function(){
                        self.goHome();
                    }, 1000);
                });
            },
            goHome:function(){
                var self = this;
                self.qt.invoke('slotOpenMeetingPage');
                self.qt.destroy();
            },
            cancel:function(){
                var self = this;
                self.qt.invoke('slotOpenStartUpPage');
                self.qt.destroy();
            },
            filterNode:function(value, data){
                if (!value) return true;
                return data.name.indexOf(value) !== -1;
            }
        },
        mounted: function () {
            var self = this;
            self.qt = new QtContext('leaderDialogCreateGroup', function () {
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

                self.refreshInstitution();

                self.qt.on('goHome', function(){
                    self.goHome();
                });

                self.qt.on('close', function(){
                    self.qt.destroy();
                });

            });
        }
    });

    return Vue;
});