define([
    'text!' + window.APPPATH + 'component/zk/business/add-member-new/zk-business-add-member-new.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'css!' + window.APPPATH + 'component/zk/business/add-member-new/zk-business-add-member-new.css'
], function(tpl, ajax, $, Vue, config){

	//组件名称
    var pluginName = 'zk-business-add-member';

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
                    select:[]
                }
            }
        },
        computed:{

        },
        watch:{

        },
        methods:{
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
            handleWindowClose:function(){
                var self = this;
                self.qt.destroy();
            },
            handleAddMemberCommit:function(){
                var self = this;
                if(self.tree.select.length <= 0){
                    self.qt.warning('提示信息', '您没有勾选任何用户');
                    return;
                }
                var members = [];
                for(var i=0; i<self.tree.select.length; i++){
                    members.push(self.tree.select[i].id);
                }
                if(self.groupType === 'command'){
                    ajax.post('/command/basic/add/members', {
                        id:self.groupId,
                        members: $.toJSON(members)
                    }, function(data){
                        self.qt.linkedWebview('business', $.toJSON({id:'commandMemberAdd', params:data}));
                        self.handleWindowClose();
                    });
                }
            }
        },
        mounted:function(){
            var self = this;
            self.qt = new QtContext('businessAddMember', function(){
                var params = self.qt.getWindowParams();
                self.groupId = params.id;
                self.groupType = params.type;

                //初始化ajax
                ajax.init({
                    login:config.ajax.login,
                    authname:config.ajax.authname,
                    debug:config.ajax.debug,
                    messenger:{
                        info:function(message){
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
                    ajax.post('/command/query/find/institution/tree/user/except/command', {id:self.groupId}, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.tree.data.push(data[i]);
                            }
                        }
                    });
                }
            });
        }
    });

    return Vue;
});