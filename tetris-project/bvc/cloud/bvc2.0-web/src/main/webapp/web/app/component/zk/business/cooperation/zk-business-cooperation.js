define([
    'text!' + window.APPPATH + 'component/zk/business/cooperation/zk-business-cooperation.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'css!' + window.APPPATH + 'component/zk/business/cooperation/zk-business-cooperation.css'
], function(tpl, ajax, $, Vue,config){

	//组件名称
    var pluginName = 'zk-business-cooperation';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data:function(){
            return {
            	baseUrl:window.BASEPATH,
                qt:'',
                groupId:'',
                groupType:'',
                tree:{
                    props:{
                        children:'children',
                        label:'name'
                    },
                    data:[],
                    select:''
                }
            }
        },
        computed:{

        },
        watch:{

        },
        methods:{
            handleWindowClose:function(){
                var self = this;
                self.qt.destroy();
            },
            handleAddMemberCommit:function(){
                var self = this;
                if(!self.tree.select){
                    self.qt.warning('提示信息', '您没有勾选任何用户');
                    return;
                }
                if(self.groupType === 'command'){
                    ajax.post('/command/cooperation/grant', {
                        id:self.groupId,
                        userIds: $.toJSON([self.tree.select])
                    }, function(data){
                        self.handleWindowClose();
                        //self.qt.linkedWebview('business', $.toJSON({id:'cooperationAdd', cooperation:data}));
                    });
                }
            }
        },
        mounted:function(){
            var self = this;
            self.qt = new QtContext('businessCooperation', function(){
                var params = self.qt.getWindowParams();
                self.groupId = params.id;
                self.groupType = params.type;

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

                if(self.groupType === 'command'){
                    self.tree.data.splice(0, self.tree.data.length);
                    ajax.post('/command/basic/query/members', {id:self.groupId}, function(data){
                        if(data.members && data.members.length>0){
                            for(var i=0; i<data.members.length; i++){
                                self.tree.data.push(data.members[i]);
                            }
                        }
                    });
                }
            });
        }
    });

    return Vue;
});