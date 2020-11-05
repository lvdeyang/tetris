define([
    'text!' + window.APPPATH + 'component/zk-leader/footer/dialog/dialogMessage.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'date',
    'css!' + window.APPPATH + 'component/zk-leader/footer/dialog/dialogMessage.css'
], function (tpl, ajax, $, Vue, config) {

    //组件名称
    var pluginName = 'dialog-message';

    Vue.component(pluginName, {
        template: tpl,
        data: function () {
            return {
                currentTab:0,
                historyInstantMsg:[],
                loading:false,
                page:{
                    currentPage:0,
                    pageSize:20,
                    total:0
                },
                currentGroupId:'',
                currentGroupName:'',
                user:'',
                $messageScope:'',
                instantMessage:'',
                newMessage:0
            }
        },
        watch:{
            
        },
        methods: {
        	pushMessage:function(message, isNew, isPush){
        		var self = this;
        		if(isPush){
        			self.historyInstantMsg.push(message);
        		}else{
        			self.historyInstantMsg.splice(0, 0, message);
        		}
        		var scrollTop = self.$messageScope.scrollTop;
        		var wholeHeight = self.$messageScope.scrollHeight;
              	var pHeight = self.$messageScope.clientHeight;
              	var isScrollBottom = false;
        		if(scrollTop == wholeHeight-pHeight){
        			isScrollBottom = true;
        		}
        		self.$nextTick(function(){
        			var wholeHeight = self.$messageScope.scrollHeight;
                  	var pHeight = self.$messageScope.clientHeight;
                  	if(wholeHeight>pHeight){
                  		if(isScrollBottom){
                  			self.scrollToBottom();
                  		}else{
                  			if(isNew) self.newMessage += 1;
                  		}
                  	}
        		});
        	},
            append:function(currentPage){
                var self = this;
                self.loading = true;
                ajax.post('/command/message/query/history/instant/message', {
                    commandId:self.currentGroupId,
                    currentPage:currentPage,
                    pageSize:self.page.pageSize
                }, function(data, status){
                	var data = data.data;
                	setTimeout(function(){
	                	self.$nextTick(function(){
	            			self.loading = false;
	                	});
	                	if(status !== 200) return;
	                    if(data){
	                        var total = data.total;
	                        var rows = data.rows;
	                        if(rows && rows.length>0){
	                            for(var i=0; i<rows.length; i++){
	                            	var message = rows[i];
	                            	var fromUsername = message.fromUsername + '（';
	                                if(message.fromUserId == self.user.id){
	                                	fromUsername = '我（';
	                                }
	                                self.pushMessage(fromUsername+message.updateTime+'）：'+message.message);
	                            }
	                        }
	                        self.page.total = total;
	                        self.page.currentPage = currentPage;
	                    }
                	}, 1000);
                }, null, ajax.TOTAL_CATCH_CODE);
            },
            onMessageScopeScroll:function(e){
            	var self = this;
            	var scrollTop=e.target.scrollTop;
            	var wholeHeight=e.target.scrollHeight;
            	var pHeight=e.target.clientHeight;
            	if((scrollTop+pHeight)>=wholeHeight){
            		self.newMessage = 0;
            	}
            	if(scrollTop == 0){
            		if(self.historyInstantMsg.length >= self.page.total) return;
            		self.append(self.page.currentPage + 1);
            	}
            },
            scrollToBottom:function(){
            	var self = this;
            	var scrollTop=self.$messageScope.scrollTop;
            	var wholeHeight=self.$messageScope.scrollHeight;
            	var pHeight=self.$messageScope.clientHeight;
            	self.$messageScope.scrollTop = wholeHeight - pHeight;
            },
            //关闭弹框
            cancel:function () {
            	this.qt.linkedWebview('historyMessage', {id:'onHistoryDialogClose'});
                this.qt.destroy();
            },
            //发送消息
            sendInstantMessage: function () {
                var self = this;
                if(!self.instantMessage){
                	self.qt.warning('您还没有输入内容！');
                	return;
                }
                var currentGroupId = self.currentGroupId;
                ajax.post('/command/message/broadcast/instant/message', {
                    id: currentGroupId,
                    message: self.instantMessage
                }, function () {
                	self.pushMessage('我 （' + new Date().format('yyyy-MM-dd hh:mm:ss') + '）：' + self.instantMessage, null, true);
                	self.page.total += 1;
                    self.instantMessage = '';
                    self.qt.success('发送成功!');
                });
            }
        },
        mounted: function () {
            var self = this;
            self.qt = new QtContext('dialogMessage', function () {
            	
            	var params = self.qt.getWindowParams();
            	if(params.currentGroupId){
            		self.currentGroupId = params.currentGroupId;
            		 self.qt.get(['user'], function (variables) {
            			 self.user = variables.user ? $.parseJSON(variables.user) : {};
            			 ajax.post('/command/basic/query/group', {id:self.currentGroupId}, function(data){
                         	self.currentGroupName = data.name;
                         });
                         self.append(1);
            		 });
            	}else{
            		self.qt.get(['user', 'currentGroupId'], function (variables) {
                    	self.user = variables.user ? $.parseJSON(variables.user) : {};
                        self.currentGroupId = variables.currentGroupId;
                        if(!self.currentGroupId){
                        	self.qt.warning('请先选择一个会议或指挥才能查看消息');
                        	return;
                        }
                        ajax.post('/command/basic/query/group', {id:self.currentGroupId}, function(data){
                        	self.currentGroupName = data.name;
                        });
                        self.append(1);
                    });
            	}
                
                self.qt.on('receiveInstantMessage', function (e) {
                	self.pushMessage(e.params.message, true, true);
                	self.page.total += 1;
                });

                // 初始化ajax
                ajax.init({
                    login: config.ajax.login,
                    redirectLogin: false,
                    authname: config.ajax.authname,
                    debug: config.ajax.debug,
                    messenger: {
                        info: function (message) {
                            self.qt.info(message);
                        },
                        success: function (message) {
                            self.qt.success(message);
                        },
                        warning: function (message) {
                            self.qt.warning(message);
                        },
                        error: function (message) {
                            self.qt.error(message);
                        }
                    }
                });
                
                self.$nextTick(function(){
                	self.$messageScope = $(self.$el).find('.message-scope')[0];
                });
                
            })
        }
    });

    return Vue;
});