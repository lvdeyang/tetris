/**
 * Created by lvdeyang on 2019/1/18 0018.
 */
define([
    'text!' + window.APPPATH + 'component/dialog/system-role/system-role-dialog.html',
    window.APPPATH + 'component/dialog/system-role/system-role-dialog.i18n',
    'restfull',
    'jquery',
    'vue',
    'context',
    'element-ui',
    'css!' + window.APPPATH + 'component/dialog/system-role/system-role-dialog.css',
], function(tpl, i18n, ajax, $, Vue, context){

    var locale = context.getProp('locale');
    var i18n = !locale?i18n.default:i18n[locale]?i18n[locale]:i18n.default;

    var pluginName = 'mi-system-role-dialog';

    Vue.component(pluginName, {
        template: tpl,
        data:function(){
            return {
                i18n:i18n,
                __buffer:null,
                dialog:{
                    visible:false,
                    loading:false
                },
                tree:{
                    props:{
                        label: 'name',
                        children: 'roles',
                        isLeaf: 'leaf'
                    },
                    data:[],
                    current:''
                }
            }
        },
        methods:{
            closed:function(){
                var self = this;
                self.tree.data.splice(0, self.tree.data.length);
                self.tree.current = '';
                self.dialog.visible = false;
                self.dialog.loading = false;
                self.__buffer = null;
            },
            loadNode:function(node, resolve){

            },
            render:function(h, scope){
                var data = scope.data;
                var icon = 'feather-icon-user';
                if(data.isGroup) icon = 'feather-icon-users';
                return h('div', null, [
                    h('span', {class:icon}, []),
                    h('span', null, data.name)
                ]);
            },
            open:function(except){
                var self = this;
                ajax.post('/system/role/feign/list/with/group/by/except/ids', {
                    roleIds:except? $.toJSON(except):null
                }, function(data){
                    if(data && data.length>0){
                        for(var i=0; i<data.length; i++){
                            self.tree.data.push(data[i]);
                        }
                    }
                    self.dialog.visible = true;
                });
            },
            setBuffer:function(data){
                var self = this;
                self.__buffer = data;
            },
            getBuffer:function(){
                var self = this;
                return self.__buffer;
            },
            handleOkButton:function(){
                var self = this;
                if(!self.tree.current){
                	self.$message({
                        message: self.i18n.messageWarning,
                        type: 'warning'
                    });
                }
                self.$emit('selected-roles', [$.parseJSON(self.tree.current)], self.__buffer, function(){
                    self.dialog.loading = true;
                }, function(){
                    self.dialog.loading = false;
                }, function(){
                    self.dialog.visible = false;
                });
            },
            nodeLabel:function(scope){
            	var data = scope.data;
            	return $.toJSON(data);
            }
        }
    });

});
