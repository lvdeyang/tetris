define([
    'text!' + window.APPPATH + 'component/zk-leader/send-message/leader-send-message.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'css!' + window.APPPATH + 'component/zk-leader/send-message/leader-send-message.css'
], function(tpl, ajax, $, Vue,config){

	//组件名称
    var pluginName = 'send-message';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data:function(){
            return {
                baseUrl: window.BASEPATH,
                qt:'',
                group:'',
                currentTab:'',
                message:{
                    content:'',
                    style:{
                        id:'',
                        name:'',
                        fontFamily:'宋体',
                        fontSize:'5',
                        textDecoration:'常规',
                        color:'黑色',
                        rollingSpeed:'正常',
                        rollingMode:'从右到左',
                        rollingLocation:'居中',
                        rollingTime:'10',
                        rollingTimeUnlimited:false
                    },
                    forcedRolling:true
                },
                tree:{
                    props:{
                        children:'children',
                        label:'name'
                    },
                    data:[],
                    select:[]
                },
                dialog:{
                    messageList:{
                        visible:false,
                        page:{
                            pageSize:50,
                            total:0,
                            currentPage:0
                        },
                        data:[/*{
                            date:'2019-10-09 09:41:39',
                            content:'11111111111111111',
                            members:'111,111,111,111',
                            status:'正在通知'
                        }*/]
                    },
                    styleTemplates:{
                        visible:false,
                        page:{
                            pageSize:50,
                            total:0,
                            currentPage:0
                        },
                        data:[],
                        currentRow:''
                    }
                }
            }
        },
        methods:{
            //关闭弹窗事件
            handleWindowClose:function(){
                var self = this;
                self.qt.destroy();
            },
            //移除标签
            handleTagRemove:function(user){
                var self = this;
                user.checked = false;
                for(var i=0; i<self.tree.select.length; i++){
                    if(self.tree.select[i] === user){
                        self.tree.select.splice(i, 1);
                        break;
                    }
                }
            },
            //复选框事件
            onUserCheckChange:function(data){
                var self = this;
                for(var i=0; i<self.tree.select.length; i++){
                    if(self.tree.select[i] === data){
                        self.tree.select.splice(i, 1);
                        return;
                    }
                }
                self.tree.select.push(data);
            },
            //发送按钮
            handleSendMessage:function(){
                var self = this;
                if(self.tree.select.length <= 0){
                    self.qt.error('消息提示', '您还没有选择发送对象');
                    return;
                }
                if(!self.message.content){
                    self.qt.warning('消息提示', '您还没有输入通知内容');
                    return;
                }
                var members = [];
                for(var i=0; i<self.tree.select.length; i++){
                    members.push(self.tree.select[i].id);
                }
                ajax.post('/command/message/send', {
                    id:self.group.id,
                    content:self.message.content,
                    forcedRolling:self.message.forcedRolling,
                    style:$.toJSON({
                        fontFamily:self.message.style.fontFamily,
                        fontSize:self.message.style.fontSize,
                        textDecoration:self.message.style.textDecoration,
                        color:self.message.style.color,
                        rollingSpeed:self.message.style.rollingSpeed,
                        rollingMode:self.message.style.rollingMode,
                        rollingLocation:self.message.style.rollingLocation,
                        rollingTime:self.message.style.rollingTime,
                        rollingTimeUnlimited:self.message.style.rollingTimeUnlimited
                    }),
                    members:$.toJSON(members)
                }, function(){
                    self.qt.warning('发送成功');
                });
            },
            //停止按钮
            handleMessageList:function(){
                var self = this;
                self.dialog.messageList.visible = true;
                self.loadMessage(1);
            },
            //通知列表
            handleMessageListClose:function(){
                var self = this;
                self.dialog.messageList.visible = false;
                self.dialog.messageList.data.splice(0, self.dialog.messageList.data.length);
            },
            //全部停止
            stopAllMessage:function(){
                var self = this;
                ajax.post('/command/message/stop/all', {
                    id:self.group.id
                }, function(){
                    for(var i=0; i<self.dialog.messageList.data.length; i++){
                        self.dialog.messageList.data[i].status = '通知结束';
                    }
                });
            },
            //删除通知
            deleteMessage:function(scope){
                var self = this;
                var row = scope.row;
                ajax.post('/command/message/remove', {messageIds: $.toJSON([row.id])}, function(){
                    for(var i=0; i<self.dialog.messageList.data.length; i++){
                        if(self.dialog.messageList.data[i].id == row.id){
                            self.dialog.messageList.data.splice(i, 1);
                            break;
                        }
                    }
                    self.dialog.messageList.page.total -= 1;
                });
            },
            //停止通知
            stopMessage:function(scope){
                var self = this;
                var row = scope.row;
                ajax.post('/command/message/stop', {
                    messageId:row.id
                }, function(){
                    for(var i=0; i<self.dialog.messageList.data.length; i++){
                        if(self.dialog.messageList.data[i] === row){
                            self.dialog.messageList.data[i].status = '通知结束';
                            break;
                        }
                    }
                });
            },
            //再次通知
            messageAgain:function(scope){
                var self = this;
                var row = scope.row;
                ajax.post('/command/message/again', {
                    messageId:row.id
                }, function(){
                    for(var i=0; i<self.dialog.messageList.data.length; i++){
                        if(self.dialog.messageList.data[i] === row){
                            self.dialog.messageList.data[i].status = '正在通知';
                            break;
                        }
                    }
                });
            },
            //更改当前选中的通知
            handleMessageCurrentChange:function(currentPage){
                var self = this;
                self.loadMessage(currentPage);
            },
            //加载通知
            loadMessage:function(currentPage){
                var self = this;
                ajax.post('/command/message/query/message', {
                    id:self.group.id,
                    pageSize:self.dialog.messageList.page.pageSize,
                    currentPage:currentPage
                }, function(data){
                    var rows = data.rows;
                    var total = data.total;
                    self.dialog.messageList.data.splice(0, self.dialog.messageList.data.length);
                    if(rows && rows.length>0){
                        for(var i=0; i<rows.length; i++){
                            self.dialog.messageList.data.push(rows[i]);
                        }
                    }
                    self.dialog.messageList.page.currentPage = currentPage;
                    self.dialog.messageList.page.total = total;
                });
            },
            //多选框数据更新页后 保留所有数据
            styleTemplateRowKey:function(row){
                var self = this;
                return 'template_'+row.id;
            },
            //报错模板按钮事件
            handleSaveStyleTemplates:function(){
                var self = this;
                if(!self.message.style.name){
                    self.qt.warning('消息提示', '请给模板起个名吧');
                    return;
                }
                if(self.message.style.id){
                    ajax.post('/command/message/style/templates/edit',{
                        id:self.message.style.id,
                        name:self.message.style.name,
                        fontFamily:self.message.style.fontFamily,
                        fontSize:self.message.style.fontSize,
                        textDecoration:self.message.style.textDecoration,
                        color: self.message.style.color,
                        rollingSpeed:self.message.style.rollingSpeed,
                        rollingMode:self.message.style.rollingMode,
                        rollingLocation:self.message.style.rollingLocation,
                        rollingTime:self.message.style.rollingTime,
                        rollingTimeUnlimited: self.message.style.rollingTimeUnlimited
                    }, function(){
                        self.qt.warning('消息提示', '修改成功');
                    });
                }else{
                    ajax.post('/command/message/style/templates/add', {
                        name:self.message.style.name,
                        fontFamily:self.message.style.fontFamily,
                        fontSize:self.message.style.fontSize,
                        textDecoration:self.message.style.textDecoration,
                        color: self.message.style.color,
                        rollingSpeed:self.message.style.rollingSpeed,
                        rollingMode:self.message.style.rollingMode,
                        rollingLocation:self.message.style.rollingLocation,
                        rollingTime:self.message.style.rollingTime,
                        rollingTimeUnlimited: self.message.style.rollingTimeUnlimited
                    }, function(){
                        self.qt.warning('消息提示', '添加成功');
                    });
                }
            },
            //打开模板弹框
            handleStyleTemplates:function(){
                var self = this;
                self.dialog.styleTemplates.visible = true;
                self.loadStyleTemplates(1);
            },
            //关闭选择模板弹框
            handleStyleTemplatesClose:function(){
                var self = this;
                self.dialog.styleTemplates.visible = false;
                self.dialog.styleTemplates.data.splice(0, self.dialog.styleTemplates.data.length);
                self.dialog.styleTemplates.currentRow = '';
            },
            //删除模板
            removeStyleTemplate:function(scope){
                var self = this;
                var row = scope.row;
                ajax.post('/command/message/style/templates/remove', {
                    templateId:row.id
                }, function(){
                    for(var i=0; i<self.dialog.styleTemplates.data.length; i++){
                        if(self.dialog.styleTemplates.data[i] === row){
                            self.dialog.styleTemplates.data.splice(0, 1);
                            break;
                        }
                    }
                    if(self.message.style.id == row.id) self.message.style.id = '';
                    self.dialog.styleTemplates.page.total -= 1;
                });
            },
            //切换分页
            handleStyleTemplatesCurrentChange:function(currentPage){
                var self = this;
                self.loadStyleTemplates(currentPage);
            },
            //更换样式
            handleCurrentStyleTemplateChange:function(currentRow, oldRow){
                var self = this;
                self.dialog.styleTemplates.currentRow = currentRow;
            },
            //确定选择模板
            handleStyleTemplatesChange:function(){
                var self = this;
                var currentRow = self.dialog.styleTemplates.currentRow;
                if(currentRow){
                    self.message.style.id = currentRow.id;
                    self.message.style.name = currentRow.name;
                    self.message.style.fontFamily = currentRow.fontFamily;
                    self.message.style.fontSize = currentRow.fontSize;
                    self.message.style.textDecoration = currentRow.textDecoration;
                    self.message.style.color = currentRow.color;
                    self.message.style.rollingSpeed = currentRow.rollingSpeed;
                    self.message.style.rollingMode = currentRow.rollingMode;
                    self.message.style.rollingLocation = currentRow.rollingLocation;
                    self.message.style.rollingTime = currentRow.rollingTime;
                    self.message.style.rollingTimeUnlimited = currentRow.rollingTimeUnlimited;
                }
                self.handleStyleTemplatesClose();
            },
            loadStyleTemplates:function(currentPage){
                var self = this;
                self.dialog.styleTemplates.data.splice(0,  self.dialog.styleTemplates.data.length);
                ajax.post('/command/message/query/style/templates', {
                    currentPage:currentPage,
                    pageSize:self.dialog.styleTemplates.page.pageSize
                }, function(data){
                    var rows = data.rows;
                    if(rows && rows.length>0){
                        for(var i=0; i<rows.length; i++){
                            self.dialog.styleTemplates.data.push(rows[i]);
                        }
                        self.dialog.styleTemplates.page.currentPage = currentPage;
                        self.dialog.styleTemplates.page.total = data.total;
                    }
                });
            }
        },
        mounted:function(){
            var self = this;
            self.qt = new QtContext('sendMessage', function(){
                var params = self.qt.getWindowParams();

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

                ajax.post('/command/basic/query/members', {id:params.id}, function(data){
                    self.group = data;
                    for(var i=0; i<data.members.length; i++){
                        self.tree.data.push(data.members[i]);
                    }
                });
            });
        }
    });

    return Vue;
});