define([
    'text!' + window.APPPATH + 'component/bvc2-update-roles/bvc2-update-roles.html',
    'restfull',
    'context',
    'jquery',
    'vue',
    'element-ui'
], function(tpl, ajax, context, $, Vue){

    var app = context.getProp('app');

    //组件名称
    var bvc2UpdateRoles = 'bvc2-update-roles';

    Vue.component(bvc2UpdateRoles, {
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
                treeLoading:false
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
                        node.label
                    ])
                ]);
            },

            handleDragStart:function(node, e){
                var instance = this,
                    data = [];
                if(node.checked){
                    data = instance.$refs.$roleTree.getCheckedNodes();
                    e.dataTransfer.setData('node', $.toJSON(data));
                }else{
                    data.push(node.data);
                    e.dataTransfer.setData('node', $.toJSON(data));
                }
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

            roleSet:function(e, role){
                var button_instance = this;
                var group = button_instance.group;
                var node = $.parseJSON(e.dataTransfer.getData('node'));
                var memberIds = [];
                for(var i=0;i<node.length;i++){
                    if(node[i].type === 'BUNDLE'){
                        memberIds.push($.parseJSON(node[i].param).memberId);
                    }
                }
                role.loading = true;
                ajax.post('/device/group/roles/set/' + group.id, {
                    memberIds:$.toJSON(memberIds),
                    roleId:role.id
                }, function(data){
                    var f = function(newRole){
                        setTimeout(function(){
                            newRole.loading = false;
                        }, 1000);
                    };
                    button_instance.refreshGroupMember();
                    button_instance.refreshRole(role.id, f);
                });
            },

            handleClose: function(role, member){
                var button_instance = this;
                var group = button_instance.group;
                ajax.post('/device/group/roles/member/remove/' + group.id, {
                    memberId:member.id,
                    roleId:role.id
                }, function(data){
                    var f = function(newRole){
                        setTimeout(function(){
                            newRole.loading = false;
                        }, 1000);
                    };
                    button_instance.refreshGroupMember();
                    button_instance.refreshRole(role.id, f);
                });
            },

            removeRoleMembers: function(role){
                var button_instance = this;
                var group = button_instance.group;
                ajax.post('/device/group/roles/remove/' + group.id, {
                    roleId:role.id
                }, function(data){
                    var f = function(newRole){
                        setTimeout(function(){
                            newRole.loading = false;
                        }, 1000);
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
                ajax.get('/device/group/query/members/except/roles/' + group.id, null, function(data){
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

                ajax.get('/device/group/query/roles/except/spokesman/' + group.id, null, function(data){
                    button_instance.roles.splice(0, button_instance.roles.length);
                    var newRole = null;
                    if(data && data.length>0){
                        for(var i=0; i<data.length; i++){
                            if(data[i].id === currId){
                                data[i].loading = true;
                                newRole = data[i];
                            }else{
                                data[i].loading = false;
                            }
                            button_instance.roles.push(data[i]);
                        }
                    }
                    if(typeof f === 'function') f(newRole);
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