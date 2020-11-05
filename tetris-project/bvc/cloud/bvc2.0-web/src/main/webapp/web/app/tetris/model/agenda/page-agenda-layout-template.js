/**
 * Created by lvdeyang on 2020/7/1.
 */
define([
    'text!' + window.APPPATH + 'tetris/model/agenda/page-agenda-layout-template.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'tetris/model/agenda/page-agenda-layout-template.css'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-agenda-layout-template';

    var init = function(p){

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el: '#' + pageId + '-wrapper',
            data: {
                menus: context.getProp('menus'),
                user: context.getProp('user'),
                groups: context.getProp('groups'),
                agendaId: p.agendaId,
                agendaName: p.agendaName,
                tree:{
                    current:'',
                    props:{
                        label: 'roleName',
                        children: 'children',
                        isLeaf: 'isLeaf'
                    },
                    data:[]
                },
                table:{
                    data:[],
                    id:''
                },
                dialog:{
                    selectTerminal:{
                        visible:false,
                        props:{
                            label: 'name',
                            children: 'children',
                            isLeaf: 'isLeaf'
                        },
                        data:[]
                    },
                    selectLayout:{
                        visible:false,
                        props:{
                            label: 'layoutName',
                            children: 'children',
                            isLeaf: 'isLeaf'
                        },
                        data:[],
                        currentRow:''
                    },
                    layout:{
                        visible:false,
                        table:{
                            rows:[]
                        }
                    },
                    combine:{
                        visible:false,
                        id:'',
                        table:{
                            rows:[]
                        }
                    }

                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                rowKey:function(row){
                    return 'position-' + row.id;
                },
                queryRoles:function(){
                    var self = this;

                    ajax.post('/tetris/bvc/model/role/agenda/permission/load', {
                        agendaId:self.agendaId
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.tree.data.push(data[i]);
                            }
                        }
                    });
                },
                currentTreeNodeChange:function(node){
                    var self = this;
                    ajax.post('/tetris/bvc/model/agenda/layout/template/load', {
                        agendaId:self.agendaId,
                        roleId:node.roleId
                    }, function(data){
                        self.table.data=[];
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.table.data.push(data[i]);
                            }
                        }
                        self.tree.current = node;
                    });
                },
                handleSelectTerminal:function(){
                    var self = this;
                    ajax.post('/tetris/bvc/model/terminal/load/all', null, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                var exist = false;
                                for(var j=0; j<self.table.data.length; j++){
                                    if(data[i].id == self.table.data[j].terminalId){
                                        exist = true;
                                        break;
                                    }
                                }
                                if(!exist) self.dialog.selectTerminal.data.push(data[i]);
                            }
                            self.dialog.selectTerminal.visible = true;
                        }
                    });
                },
                handleSelectTerminalClose:function(){
                    var self = this;
                    var nodes = self.$refs.selectTerminalTree.getCheckedNodes();
                    if(nodes && nodes.length>0){
                        for(var i=0; i<nodes.length; i++){
                            self.table.data.push({
                                agendaId:self.agendaId,
                                agendaName:self.agendaName,
                                roleId:self.tree.current.roleId,
                                roleName:self.tree.current.roleName,
                                terminalId:nodes[i].id,
                                terminalName:nodes[i].name,
                                layoutId:'',
                                layoutName:'',
                                edit:true
                            });
                        }
                    }
                    self.dialog.selectTerminal.data.splice(0, self.dialog.selectTerminal.data.length);
                    self.dialog.selectTerminal.visible = false;
                },
                handleSelectLayout:function(scope){
                    var self = this;
                    var row = scope.row;
                    ajax.post('/tetris/bvc/model/terminal/layout/permission/load', {
                        terminalId:row.terminalId
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                if(data[i].layoutId !== row.layoutId){
                                    self.dialog.selectLayout.data.push(data[i]);
                                }
                            }
                        }
                        self.dialog.selectLayout.visible = true;
                        self.dialog.selectLayout.currentRow = row;
                    });
                },
                handleSelectLayoutClose:function(){
                    var self = this;
                    var currentNode = self.$refs.selectLayoutTree.getCurrentNode();
                    if(currentNode){
                        self.dialog.selectLayout.currentRow.layoutId = currentNode.layoutId;
                        self.dialog.selectLayout.currentRow.layoutName = currentNode.layoutName;
                    }
                    self.dialog.selectLayout.visible = false;
                    self.dialog.selectLayout.data.splice(0, self.dialog.selectLayout.data.length);
                    self.dialog.selectLayout.currentRow = '';
                },
                handleRowSave:function(scope){
                    var self = this;
                    var row = scope.row;
                    if(row.id){
                        ajax.post('/tetris/bvc/model/agenda/layout/template/edit', {
                            id:row.id,
                            agendaId:row.agendaId,
                            roleId:row.roleId,
                            terminalId:row.terminalId,
                            layoutId:row.layoutId
                        }, function(data){
                            Vue.set(row, 'edit', false);
                        });
                    }else{
                        ajax.post('/tetris/bvc/model/agenda/layout/template/add', {
                            agendaId:row.agendaId,
                            roleId:row.roleId,
                            terminalId:row.terminalId,
                            layoutId:row.layoutId
                        }, function(data){
                            Vue.set(row, 'id', data.id);
                            Vue.set(row, 'edit', false);
                        });
                    }
                },
                handleRowEdit:function(scope){
                    var self = this;
                    var row = scope.row;
                    Vue.set(row, 'edit', true);
                },
                handleRowDelete:function(scope){
                    var self = this;
                    var row = scope.row;
                    if(row.id){
                        ajax.post('/tetris/bvc/model/agenda/layout/template/delete', {
                            id:row.id
                        }, function(){
                           for(var i=0; i<self.table.data.length; i++){
                               if(self.table.data[i] === row){
                                   self.table.data.splice(i, 1);
                                   break;
                               }
                           }
                        });
                    }else{
                        for(var i=0; i<self.table.data.length; i++){
                            if(self.table.data[i] === row){
                                self.table.data.splice(i, 1);
                                break;
                            }
                        }
                    }
                },
                handleRowLayout:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.layout.visible = true;
                    ajax.post('/tetris/bvc/model/terminal/layout/position/load',{layoutId:row.layoutId},function(data){
                        if(data && data.length>0){
                            self.dialog.layout.table.rows = [];
                            self.table.id = row.id;
                            for(var i=0; i<data.length; i++){
                                self.dialog.layout.table.rows.push(data[i]);
                            }
                        }
                    })
                },
                handleLayoutClose:function(scope){
                    var self = this;
                    self.dialog.layout.visible = false;
                    self.dialog.layout.table.id = '';
                },
                handleRowBind: function (scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.combine.visible = true;
                    ajax.post('/tetris/bvc/model/agenda/combine/video/load', {
                        businessId:self.agendaId,
                        businessType:'AGENDA'
                    }, function(data, status){
                        if(data && data.length>0){
                            self.dialog.combine.table.rows = [];
                            self.dialog.combine.table.id = row.id;
                            for(var i=0; i<data.length; i++){
                                self.dialog.combine.table.rows.push(data[i]);
                            }
                        }
                    });
                },
                handleCombineClose:function(scope){
                    var self = this;
                    self.dialog.combine.visible = false;
                },
                handleRowCombine:function(scope){
                    var self= this;
                    var row =scope.row;
                    ajax.post('/tetris/bvc/model/layout/virtual/source/template/bind/virtual', {
                        agendaLayoutTemplateId:self.table.id,
                        layoutPositionId:self.dialog.combine.table.id,
                        virtualSourceId:row.id},function(data){
                        if(status == 200){
                            self.$message({
                                type:'success',
                                message:"绑定成功！"
                            });
                            return null;
                        }
                        self.dialog.combine.visible = false;
                    })
                },
                handleRowUnbind:function(scope){
                    var self = this;
                    var row = scope.row;
                    ajax.post('/tetris/bvc/model/layout/virtual/source/template/unbind/virtual',{
                        agendaLayoutTemplateId:self.table.id,
                        layoutPositionId:row.id,
                    },function(data,status){
                        if(status == 200){
                            self.$message({
                                type:'success',
                                message:"已解除绑定！"
                            });
                            return null;
                        }
                        self.dialog.combine.visible = false;
                    })
                }
            },
            created:function(){
                var self = this;
                self.queryRoles();
            }
        });

    };

    var destroy = function(){

    };

    var groupList = {
        path:'/' + pageId + '/:agendaId/:agendaName',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});