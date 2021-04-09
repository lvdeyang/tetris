define([
    'text!' + window.APPPATH + 'component/zk-leader/recManage/rec.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'css!' + window.APPPATH + 'component/zk-leader/recManage/rec.css'
], function (tpl, ajax, $, Vue, config) {

    //组件名称
    var pluginName = 'leader-rec';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data: function () {
            return {
                baseUrl: window.BASEPATH,
                currentTab:'deviceData', //对应tab的name
                filterText:'',
                recordMode:'', //录制模式
                date:'', //查询日期
                deviceCurrentPage:1,
                userCurrentPage:1,
                userData:{
                    totalData:[],
                    filterData:[

                    ]
                },
                deviceData:{
                    totalData:[
                        {
                            fileName: '王小虎',
                            deviceName:'001设备',
                            videoSource:'sss',
                            audioSource:'xxx',
                            startDate:'2020-02-23 19:23:43',
                            endDate:'2020-02-23 19:23:43',
                            recMode:'手动录制',
                            status:'正在录制'
                        },  {
                            fileName: '王小虎1',
                            deviceName:'001设备',
                            videoSource:'sss',
                            audioSource:'xxx',
                            startDate:'2020-02-23 19:23:43',
                            endDate:'2020-02-23 19:23:43',
                            recMode:'手动录制',
                            status:'正在录制'
                        },  {
                            fileName: '王小虎2',
                            deviceName:'001设备',
                            videoSource:'sss',
                            audioSource:'xxx',
                            startDate:'2020-02-23 19:23:43',
                            endDate:'2020-02-23 19:23:43',
                            recMode:'手动录制',
                            status:'正在录制'
                        },  {
                            fileName: '王小虎3',
                            deviceName:'001设备',
                            videoSource:'sss',
                            audioSource:'xxx',
                            startDate:'2020-02-23 19:23:43',
                            endDate:'2020-02-23 19:23:43',
                            recMode:'手动录制',
                            status:'正在录制'
                        },
                        {
                            fileName: '王小虎3',
                            deviceName:'001设备',
                            videoSource:'sss',
                            audioSource:'xxx',
                            startDate:'2020-02-23 19:23:43',
                            endDate:'2020-02-23 19:23:43',
                            recMode:'手动录制',
                            status:'正在录制'
                        },
                        {
                            fileName: '王小虎3',
                            deviceName:'001设备',
                            videoSource:'sss',
                            audioSource:'xxx',
                            startDate:'2020-02-23 19:23:43',
                            endDate:'2020-02-23 19:23:43',
                            recMode:'手动录制',
                            status:'正在录制'
                        },
                        {
                            fileName: '王小虎3',
                            deviceName:'001设备',
                            videoSource:'sss',
                            audioSource:'xxx',
                            startDate:'2020-02-23 19:23:43',
                            endDate:'2020-02-23 19:23:43',
                            recMode:'手动录制',
                            status:'正在录制'
                        },
                        {
                            fileName: '李发',
                            deviceName:'001设备',
                            videoSource:'sss',
                            audioSource:'xxx',
                            startDate:'2020-02-23 19:23:43',
                            endDate:'2020-02-23 19:23:43',
                            recMode:'手动录制',
                            status:'正在录制'
                        }
                    ],
                    filterData:[]
                }
            }
        },
        watch:{
            currentTab:function(){
                var self = this;
                if(self.currentTab === 'userData'){
                    // self.filterUserCommand();

                }else if(self.currentTab === 'deviceData'){
                    // self.filterDeviceCommand();
                    self.deviceData.filterData=self.deviceData.totalData.slice(0,self.deviceData.totalData.length)
                }
            }
        },
        computed:{
            //设备的分页
            pageData: function () {
                return this.deviceData.filterData.slice((this.deviceCurrentPage - 1) * 2, this.deviceCurrentPage * 2);
            }
        },
        methods: {
            search:function () {
                var self = this;
                if(self.currentTab === 'userData'){
                    self.filterUserCommand();
                }else if(self.currentTab === 'deviceData'){
                    self.filterDeviceCommand();
                }
            },
            //当前页改变
            deviceCurrentChange:function (val) {
                this.deviceCurrentPage=val;
            },
            userCurrentChange:function (val) {
                this.userCurrentPage=val;
            },

            //过滤设备数据
            filterUserCommand:function(){
                var self = this;
                self.userData.filterData.splice(0, self.userData.filterData.length);
                for(var i=0; i<self.userData.totalData.length; i++){
                    if(self.userData.totalData[i].name.indexOf(self.filterText)>=0){
                        self.userData.filterData.push(self.userData.totalData[i]);
                    }
                }
            },
            filterDeviceCommand:function(){
                var self = this;
                self.deviceData.filterData.splice(0, self.deviceData.filterData.length);
                for(var i=0; i<self.deviceData.totalData.length; i++){
                    if(self.deviceData.totalData[i].fileName.indexOf(self.filterText)>=0){
                        self.deviceData.filterData.push(self.deviceData.totalData[i]);
                    }
                }
            },
            refreshCommand:function(){
                var self = this;
                ajax.post('/command/record/query', null, function(data){
                    if(data && data.length>0){
                        var commands = data[0].children;
                        if(commands && commands.length>0){
                            for(var i=0; i<commands.length; i++){
                                var creator = $.parseJSON(commands[i].param).creator;
                                if(creator == self.user.id){
                                    //我的会议
                                    self.userData.totalData.push(commands[i]);
                                    self.userData.filterData.push(commands[i]);
                                }else{
                                    //我加入的会议
                                    self.deviceData.totalData.push(commands[i]);
                                    self.deviceData.filterData.push(commands[i]);
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
            removeCommand:function(scope){
                var self = this;
                var row = scope.row;
                ajax.post('/command/basic/remove', {ids: $.toJSON([row.id])}, function(data){
                    for(var i=0; i<self.userData.totalData.length; i++){
                        if(self.userData.totalData[i].id === row.id){
                            self.userData.totalData.splice(i, 1);
                        }
                    }
                    for(var i=0; i<self.userData.filterData.length; i++){
                        if(self.userData.filterData[i].id === row.id){
                            self.userData.filterData.splice(i, 1);
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

                // self.refreshCommand();

                self.qt.on('close', function(){
                    self.qt.destroy();
                });
            });
        }
    });

    return Vue;
});