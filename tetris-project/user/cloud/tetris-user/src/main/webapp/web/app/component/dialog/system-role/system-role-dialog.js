/**
 * Created by lvdeyang on 2019/1/18 0018.
 */
define([
    'text!' + window.APPPATH + 'component/dialog/system-role/system-role-dialog.html',
    'restfull',
    'jquery',
    'vue',
    'element-ui',
    'css!' + window.APPPATH + 'component/dialog/system-role/system-role-dialog.css',
], function(tpl, ajax, $, Vue){

    var pluginName = 'mi-system-role-dialog';

    Vue.component(pluginName, {
        template: tpl,
        data:function(){
            return {
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
                    expandOnClickNode:false,
                    data:[],
                    checked:[]
                }
            }
        },
        methods:{
            //对话框关闭时初始化数据
            closed:function(){
                var self = this;
                self.tree.data.splice(0, self.tree.data.length);
                self.cache = null;
            },
            loadNode:function(node, resolve){

            },
            //渲染函数
            render:function(h, scope){
                var data = scope.data;
                var icon = 'feather-icon-user';
                if(data.isGroup) icon = 'feather-icon-users';
                return h('div', null, [
                    h('span', {class:icon}, []),
                    h('span', null, data.name)
                ]);
            },
            //打开文件夹对话框
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
            //缓存数据
            setBuffer:function(data){
                var self = this;
                self.__buffer = data;
            },
            //获取缓存数据
            getBuffer:function(){
                var self = this;
                return self.__buffer;
            },
            //文件夹选中
            handleOkButton:function(){
                var self = this;
                var currentNodes = self.tree.checked;
                if(!currentNodes){
                    self.$message({
                        message: '您还没有选择角色',
                        type: 'warning'
                    });
                }
                self.$emit('selected-roles', currentNodes, self.__buffer, function(){
                    self.dialog.loading = true;
                }, function(){
                    self.dialog.loading = false;
                }, function(){
                    self.dialog.visible = false;
                });
            },
            checkChange:function(node, checked){
                var self = this;
                if(node.isGroup) return;
                if(checked){
                    self.tree.checked.push(node);
                }else{
                    for(var i=0; i<self.tree.checked.length; i++){
                        if(self.tree.checked[i].id === node.id){
                            self.tree.checked.splice(i, 1);
                            return;
                        }
                    }
                }
            }
        }
    });

});
