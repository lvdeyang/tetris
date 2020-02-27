define([
    'text!' + window.APPPATH + 'component/zk/message/zk-message.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'css!' + window.APPPATH + 'component/zk/message/zk-message.css'
], function(tpl, ajax, $, Vue, config){

	//组件名称
    var pluginName = 'zk-message';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data:function(){
            return {
            	baseUrl:window.BASEPATH,
                commands:[],
                filterButtons:{
                    activeIndex:''
                }
            }
        },
        computed:{

        },
        watch:{

        },
        methods:{
        	filterButtonClick:function(e, index){
                var self = this;
                var $target = $(e.target);
                var $button = $target.is('button')?$target:$target.closest('button');
                $button.blur();
                if(self.filterButtons.activeIndex === index){
                    self.filterButtons.activeIndex = '';
                    //取消过滤
                }else{
                    self.filterButtons.activeIndex = index;
                    //进行过滤
                }
            },
            isFilterButtonActive:function(index){
                var self = this;
                if(self.filterButtons.activeIndex === index){
                    return 'active';
                }else{
                    return '';
                }
            },
            ignore:function(command){
                var self = this;
                ajax.post('/command/message/consume/all', {
                    ids: $.toJSON([command.id])
                }, function(data){
                    for(var i=0; i<self.commands.length; i++){
                        if(self.commands[i].id == command.id){
                            self.commands.splice(i, 1);
                            break;
                        }
                    }
                });
            },
            refuse:function(command){
                var self = this;
                var message = command.message;
                if(command.message.businessType === 'callUser'){
                    ajax.post('/command/call/user/refuse', {businessId:message.businessId}, null);
                }else if(command.message.businessType === 'voiceIntercom'){
                    ajax.post('/command/voice/intercom/refuse', {businessId:message.businessId}, null);
                }else if(command.message.businessType === 'cooperationGrant'){
                    ajax.post('/command/cooperation/refuse', {id: message.businessId}, null);
                }else if(command.message.businessType === 'commandStart'){
                    ajax.post('/command/basic/refuse', {id: message.businessId}, null);
                }else if(command.message.businessType === 'commandForwardDevice'){
                    var forwards = message.forwards;
                    var forwardIds = [];
                    if(forwards && forwards.length>0){
                        for(var i=0; i<forwards.length; i++){
                            forwardIds.push(forwards[i].id);
                        }
                    }
                    ajax.post('/command/basic/forward/device/refuse', {
                        id: e.businessId,
                        forwardIds: $.toJSON(forwardIds)
                    });
                }else if(command.message.businessType === 'commandForwardFile'){
                    var forwards = message.forwards;
                    var forwardIds = [];
                    if(forwards && forwards.length>0){
                        for(var i=0; i<forwards.length; i++){
                            forwardIds.push(forwards[i].id);
                        }
                    }
                    ajax.post('/command/basic/forward/file/refuse', {
                        id: e.businessId,
                        forwardIds: $.toJSON(forwardIds)
                    });
                }else if(command.message.businessType === 'secretStart'){
                    ajax.post('/command/secret/refuse', {id: message.businessId}, null);
                }
                for(var i=0; i<self.commands.length; i++){
                    if(self.commands[i].id == command.id){
                        self.commands.splice(i, 1);
                        break;
                    }
                }
            },
            accept:function(command){
                var self = this;
                var message = command.message;
                if(command.message.businessType === 'callUser'){
                    ajax.post('/command/call/user/accept', {businessId:message.businessId}, function(data){
                        self.qt.linkedWebview('business', {id:'callUsersProxy', params:$.toJSON([data])});
                    });
                }else if(command.message.businessType === 'voiceIntercom'){
                    ajax.post('/command/voice/intercom/accept', {businessId:message.businessId}, function(data){
                        self.qt.linkedWebview('business', {id:'voiceIntercomsProxy', params:$.toJSON([data])});
                    });
                }else if(command.message.businessType === 'cooperationGrant'){
                    ajax.post('/command/cooperation/agree', {id:message.businessId}, null);
                }else if(command.message.businessType === 'commandStart'){
                    self.qt.linkedWebview('business', {id:'enterCommand', params:message});
                }else if(command.message.businessType === 'commandForwardDevice'){
                    var forwards = message.forwards;
                    var forwardIds = [];
                    if(forwards && forwards.length>0){
                        for(var i=0; i<forwards.length; i++){
                            forwardIds.push(forwards[i].id);
                        }
                    }
                    ajax.post('/command/basic/forward/device/agree', {
                        id: e.businessId,
                        forwardIds: $.toJSON(forwardIds)
                    }, function(data){
                        if(data && data.length>0){
                            self.qt.linkedWebview('business', {id:'commandForwardDeviceProxy', params:$.toJSON(data)});
                        }
                    });
                }else if(command.message.businessType === 'commandForwardFile'){
                    var forwards = message.forwards;
                    var forwardIds = [];
                    if(forwards && forwards.length>0){
                        for(var i=0; i<forwards.length; i++){
                            forwardIds.push(forwards[i].id);
                        }
                    }
                    ajax.post('/command/basic/forward/file/agree', {
                        id: e.businessId,
                        forwardIds: $.toJSON(forwardIds)
                    }, function(data){
                        if(data && data.length>0){
                            self.qt.linkedWebview('business', {id:'commandForwardFileProxy', params:$.toJSON(data)});
                        }
                    });
                }else if(command.message.businessType === 'secretStart'){
                    ajax.post('/command/secret/accept', {id: message.businessId}, function(data){
                        self.qt.linkedWebview('business', {id:'secretStartProxy', params:$.toJSON([data])});
                    });
                }
                for(var i=0; i<self.commands.length; i++){
                    if(self.commands[i].id == command.id){
                        self.commands.splice(i, 1);
                        break;
                    }
                }
            }
        },
        mounted:function(){
            var self = this;
            self.qt = new QtContext('message', function(){

                //初始化ajax
                ajax.init({
                    login:config.ajax.login,
                    redirectLogin:false,
                    authname:config.ajax.authname,
                    debug:config.ajax.debug,
                    messenger:{
                        info:function(message, status){
                            self.qt.info(message);
                        },
                        success:function(message, status){
                            self.qt.success(message);
                        },
                        warning:function(message, status){
                            self.qt.warning(message);
                        },
                        error:function(message, status){
                            self.qt.error(message);
                        }
                    }
                });

                self.qt.on('messagePanelShow', function(e){
                    self.commands.splice(0, self.commands.length);
                    ajax.post('/command/message/find/unconsumed/commands', null, function(commands){
                        if(commands && commands.length>0){
                            for(var i=0; i<commands.length; i++){
                                commands[i].message = $.parseJSON(commands[i].message);
                                self.commands.push(commands[i]);
                            }
                        }
                    });
                });
            });
        }
    });

    return Vue;
});