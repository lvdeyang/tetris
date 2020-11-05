define([
    'text!' + window.APPPATH + 'component/bvc2-update-spokesman/bvc2-update-spokesman.html',
    'restfull',
    'context',
    'jquery',
    'vue',
    'element-ui'
], function(tpl, ajax, context, $, Vue){

    var app = context.getProp('app');

    //组件名称
    var bvc2UpdateSpokesman = 'bvc2-update-spokesman';

    Vue.component(bvc2UpdateSpokesman, {
        props:['group'],
        template: tpl,
        data:function(){
            return {
                members:[],
                roles:[],
                defaultProps:{
                    children:'children',
                    label:'name',
                    isLeaf:'isLeaf'
                },
                treeLoading:false,
                cardLoading:false
            }
        },
        methods:{
            renderContent:function(h, target){
                var node = target.node,
                    data = target.data;
                if(data.type == 'CHANNEL'){
                    node.isLeaf = true;
                    if(JSON.parse(data.param).channelType != 'VenusVideoIn'){
                        node.visible = false;
                    }
                }

                var c = {};
                c[node.icon] = true;
                var classes = [c];
                if(data.bundleStatus){
                    classes.push(data.bundleStatus);
                }

                return h('span', {
                    class: {
                        'bvc2-tree-node-custom': true
                    }
                }, [
                    h('span', null, [
                        h('span', {
                            class: classes,
                            style: {
                                'font-size': '16px',
                                'position': 'relative',
                                'top': '1px',
                                'margin-right': '5px'
                            }
                        }, null),
                        h('span', {
                            attrs:{
                                title: node.label
                            }
                        }, node.label)
                    ])
                ]);
            },

            handleDragStart:function(node, e){
                var data = node.data;
                e.dataTransfer.setData('node', $.toJSON(data));
            },

            allowDrop:function(){
                return false;
            },

            allowDrag:function(node){
                var data = node.data;
                if(data.type === 'BUNDLE'){
                    return true;
                }else{
                    return false;
                }
            },

            spokesmanSet:function(e, role){
                var button_instance = this;
                var group = button_instance.group;
                var node = $.parseJSON(e.dataTransfer.getData('node'));
                var param = $.parseJSON(node.param);
                role.loading = true;
                button_instance.cardLoading = true;
                ajax.post('/device/group/spokesman/set/' + group.id, {
                    memberId:param.memberId,
                    roleId:role.id
                }, function(data){
                    var f = function(newRole){
                        setTimeout(function(){
                            newRole.loading = false;
                            button_instance.cardLoading = false;
                        }, 1000);
                        button_instance.$emit("refresh-spokesman-member", "ROLE@@"+role.id, role.name+"-"+node.name);
                    };
                    button_instance.refreshGroupMember();
                    button_instance.refreshRole(role.id, f);
                });
            },

            allowNativeDrop:function(e){
                e.preventDefault();
            },

            cardMargin:function(index){
                if(index >= 2){
                    return 'margin-top:10px;';
                }
                return '';
            },

            //获取会议成员
            refreshGroupMember:function(){
                var button_instance = this;
                var group = button_instance.group;
                button_instance.treeLoading = true;
                ajax.get('/device/group/query/members/except/spokesman/' + group.id, null, function(data){
                    button_instance.members.splice(0, button_instance.members.length);
                    if(data && data.length>0){
                        for(var i=0; i<data.length; i++){
                            button_instance.members.push(data[i]);
                        }
                    }
                    setTimeout(function(){
                        button_instance.treeLoading = false;
                    }, 1000);
                });
            },

            //获取角色
            refreshRole:function(currId, f){
                var button_instance = this;
                var group = button_instance.group;

                ajax.get('/device/group/query/spokesman/' + group.id, null, function(data){
                    button_instance.roles.splice(0, button_instance.roles.length);
                    var newRole = null;
                    if(data && data.length>0){
                        for(var i=0; i<data.length; i++){
                            if(data[i].id === currId){
                                data[i].loading = true;
                                button_instance.cardLoading = true;
                                newRole = data[i];
                            }else{
                                data[i].loading = false;
                                button_instance.cardLoading = false;
                            }
                            button_instance.roles.push(data[i]);
                        }
                    }
                    if(typeof f === 'function') f(newRole);
                });
            },

            //清空发言人
            removeSpokesmanMember:function(role){
                var button_instance = this;
                var group = button_instance.group;
                button_instance.cardLoading = true;
                ajax.post('/device/group/spokesman/remove/' + group.id, {
                    roleId:role.id
                }, function(data){
                    var f = function(newRole){
                        setTimeout(function(){
                            newRole.loading = false;
                            button_instance.cardLoading = false;
                        }, 1000);
                    };
                    button_instance.refreshGroupMember();
                    button_instance.refreshRole(role.id, f);
                });
            }

        },
        mounted:function(){
            var button_instance = this;
            button_instance.refreshGroupMember();
            button_instance.refreshRole();
        }
    });

});