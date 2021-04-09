define([
    'text!' + window.APPPATH + 'component/zk-leader/dialog/add-group/leader-dialog-add-group.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'css!' + window.APPPATH + 'component/zk-leader/dialog/add-group/leader-dialog-add-group.css'
], function (tpl, ajax, $, Vue, config) {

    //组件名称
    var pluginName = 'leader-dialog-add-group';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data: function () {
            return {
                baseUrl: window.BASEPATH,
                currentTab:'joinCommand',
                filterText:'',
                myCommand:{
                    totalData:[],
                    filterData:[]
                },
                joinCommand:{
                    totalData:[],
                    filterData:[]
                }
            }
        },
        watch:{
            currentTab:function(){
                var self = this;
                if(self.currentTab === 'myCommand'){
                    self.filterMyCommand();
                }else if(self.currentTab === 'joinCommand'){
                    self.filterJoinCommand();
                }
            },
            filterText:function(){
                var self = this;
                if(self.currentTab === 'myCommand'){
                    self.filterMyCommand();
                }else if(self.currentTab === 'joinCommand'){
                    self.filterJoinCommand();
                }
            }
        },
        methods: {
            filterMyCommand:function(){
                var self = this;
                self.myCommand.filterData.splice(0, self.myCommand.filterData.length);
                for(var i=0; i<self.myCommand.totalData.length; i++){
                    if(self.myCommand.totalData[i].name.indexOf(self.filterText)>=0){
                        self.myCommand.filterData.push(self.myCommand.totalData[i]);
                    }
                }
            },
            filterJoinCommand:function(){
                var self = this;
                self.joinCommand.filterData.splice(0, self.joinCommand.filterData.length);
                for(var i=0; i<self.joinCommand.totalData.length; i++){
                    if(self.joinCommand.totalData[i].name.indexOf(self.filterText)>=0){
                        self.joinCommand.filterData.push(self.joinCommand.totalData[i]);
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
                                    self.joinCommand.totalData.push(commands[i]);
                                    self.joinCommand.filterData.push(commands[i]);
                                }
                            }
                        }
                    }
                });
            },
            enterCommand:function(scope, action){
                var self = this;
                var row = scope.row;
                ajax.post('/command/basic/enter', {ids: $.toJSON([row.id])}, function(data){
                    if(action === 'start'){
                        ajax.post('/command/basic/start', {
                            id:row.id
                        }, function(){
                            self.qt.linkedWebview('rightBar', {id:'currentGroupChange', params: $.toJSON(data)});
                            setTimeout(function(){
                                self.goHome();
                            }, 1000);
                        });
                    }else{
                        self.qt.linkedWebview('rightBar', {id:'currentGroupChange', params: $.toJSON(data)});
                        setTimeout(function(){
                            self.goHome();
                        }, 1000);
                    }
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
            cancel:function(){
                var self = this;
                self.qt.invoke('slotOpenStartUpPage');
                self.qt.destroy();
            }
        },
        mounted: function () {
            var self = this;
            self.qt = new QtContext('leaderDialogAddGroup', function () {
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

                self.refreshCommand();

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