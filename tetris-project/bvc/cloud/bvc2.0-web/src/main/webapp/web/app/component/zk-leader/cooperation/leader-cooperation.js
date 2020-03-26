define([
    'text!' + window.APPPATH + 'component/zk-leader/cooperation/leader-cooperation.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'css!' + window.APPPATH + 'component/zk-leader/cooperation/leader-cooperation.css'
], function(tpl, ajax, $, Vue,config){

	//组件名称
    var pluginName = 'leader-cooperation';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data:function(){
            return {
            	baseUrl:window.BASEPATH,
                qt:'',
                groupId:'',
                groupType:'',
                page:'',//应该显示哪个页面
                pageName:'', //多个页面公用，区分
                tag:'',
                tree:{
                    props:{
                        children:'children',
                        label:'name'
                    },
                    data:[],
                    select:[]
                },
                //协同的人员数组
                saveSelect:[]
            }
        },
        methods:{
            //关闭弹窗事件
            handleWindowClose:function(){
                var self = this;
                self.qt.destroy();
            },
            //添加提交事件
            handleAddMemberCommit:function(){
                var self = this;
                if(!self.tree.select){
                    self.qt.alert('提示信息', '您没有勾选任何用户');
                    return;
                }
                if(self.groupType === 'command'){
                    if(self.page =='add'){
                        ajax.post('/command/cooperation/grant', {
                            id:self.groupId,
                            userIds: $.toJSON(self.tree.select)
                        }, function(data){
                            self.qt.success('邀请成功');
                            self.handleWindowClose();
                            self.saveSelect=self.tree.select;
                        });
                    }else if(self.page == 'cancel'){
                        ajax.post('/command/cooperation/revoke/batch', {
                            id:self.groupId,
                            userIds: $.toJSON(self.tree.select)
                        }, function(data){
                            self.qt.success('撤销协同成功');
                            self.handleWindowClose();
                            self.saveSelect=self.tree.select;
                        });
                    }
                }
            }
        },
        mounted:function(){
            var self = this;
            self.qt = new QtContext('leaderCooperation', function(){
                var params = self.qt.getWindowParams();
                self.groupId = params.id;
                self.groupType = params.type;
                self.page = params.page;
                self.pageName=params.name;
                self.tag = params.tag;

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

                    if(self.page === 'add'){
                        ajax.post('/command/basic/query/members', {id:self.groupId}, function(data){
                            if(data.members && data.members.length>0){
                                for(var i=0; i<data.members.length; i++){
                                        self.tree.data.push(data.members[i]);
                                }
                            }
                        })
                    }
                    else if(self.page == 'cancel'){
                        ajax.post('/command/query/find/institution/tree/user/command/cooperation', {id:self.groupId}, function(data){
                            if(data && data.length>0){
                                for(var i=0; i<data.length; i++){
                                    self.tree.data.push(data[i]);
                                }
                            }
                        })
                    }
                }
            });
        }
    });

    return Vue;
});