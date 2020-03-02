define([
    'text!' + window.APPPATH + 'component/zk-leader/dialog/prepare-group/leader-dialog-prepare-group.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'css!' + window.APPPATH + 'component/zk-leader/dialog/prepare-group/leader-dialog-prepare-group.css'
], function (tpl, ajax, $, Vue, config) {

    //组件名称
    var pluginName = 'leader-dialog-prepare-group';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data: function () {
            return {
                baseUrl: window.BASEPATH,
                currentTab:'prepareCommand',
                filterCommand:'',
                myCommand:{
                    totalData:[],
                    filterData:[]
                },
                filterUser:'',
                commandName:'',
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
        watch:{
            filterCommand:function(){
                var self = this;
                self.filterMyCommand();
            },
            filterUser:function(val){
                this.$refs.institutionTree.filter(val);
            }
        },
        methods: {
            filterMyCommand:function(){
                var self = this;
                self.myCommand.filterData.splice(0, self.myCommand.filterData.length);
                for(var i=0; i<self.myCommand.totalData.length; i++){
                    if(self.myCommand.totalData[i].name.indexOf(self.filterCommand)>=0){
                        self.myCommand.filterData.push(self.myCommand.totalData[i]);
                    }
                }
            },
            refreshCommand:function(){
                var self = this;
                ajax.post('/command/query/list', null, function(data){
                    if(data && data.length>0){
                        var commands = data[0].children;
                        if(commands && commands.length>0){
                            for(var i=0; i<commands.length; i++){
                                var creator = $.parseJSON(commands[i].param).creator;
                                if(creator == self.user.id){
                                    //我的会议
                                    self.myCommand.totalData.push(commands[i]);
                                    self.myCommand.filterData.push(commands[i]);
                                }else{
                                    //我加入的会议
                                }
                            }
                        }
                    }
                });
            },
            enterCommand:function(scope){
                var self = this;
                var row = scope.row;
                ajax.post('/command/basic/enter', {ids: $.toJSON([row.id])}, function(data){
                    ajax.post('/command/basic/start', {
                        id:row.id
                    }, function(){
                        self.qt.linkedWebview('rightBar', {id:'currentGroupChange', params: $.toJSON(data)});
                        setTimeout(function(){
                            self.goHome();
                        }, 1000);
                    });
                });
            },
            goHome:function(){
                var self = this;
                self.qt.invoke('slotOpenMeetingPage');
                self.qt.destroy();
            },
            removeCommand:function(scope){
                var self = this;
                var row = scope.row;
                ajax.post('/command/basic/remove', {ids: $.toJSON([row.id])}, function(data){
                    for(var i=0; i<self.myCommand.totalData.length; i++){
                        if(self.myCommand.totalData[i].id === row.id){
                            self.myCommand.totalData.splice(i, 1);
                        }
                    }
                    for(var i=0; i<self.myCommand.filterData.length; i++){
                        if(self.myCommand.filterData[i].id === row.id){
                            self.myCommand.filterData.splice(i, 1);
                        }
                    }
                });
            },
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
                    if(nodes[i].type === 'USER')userIds.push(nodes[i].id);
                }
                ajax.post('/command/basic/save', {
                    members: $.toJSON(userIds),
                    name:self.commandName.trim()
                }, function(data){
                    /*self.qt.linkedWebview('rightBar', {id:'currentGroupChange', params: $.toJSON(data)});
                    setTimeout(function(){
                        self.qt.invoke('slotOpenMeetingPage');
                        self.qt.destroy();
                    }, 1000);*/
                    self.qt.success('安排会议成功！');
                });
            },
            filterNode:function(value, data){
                if (!value) return true;
                return data.name.indexOf(value) !== -1;
            },
            cancel:function(){
                var self = this;
                self.qt.invoke('slotOpenStartUpPage');
                self.qt.destroy();
            }
        },
        mounted: function () {
            var self = this;
            self.qt = new QtContext('leaderDialogPrepareGroup', function () {
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

                //self.refreshInstitution();
                //self.refreshCommand();

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