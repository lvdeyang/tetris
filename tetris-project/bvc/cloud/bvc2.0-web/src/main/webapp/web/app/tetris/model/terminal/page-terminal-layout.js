/**
 * Created by lvdeyang on 2020/6/28.
 */
define([
    'text!' + window.APPPATH + 'tetris/model/terminal/page-terminal-layout.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'tetris/model/terminal/page-terminal-layout.css'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-terminal-layout';

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
                terminalId:p.terminalId,
                terminalName:p.terminalName,
                layouts:[],
                table:{
                    rows:[]
                },
                dialog: {
                    addLayout:{
                        visible:false,
                        loading:false,
                        props:{
                            label:'name',
                            children:'children',
                            isLeaf:'isLeaf'
                        },
                        layouts:[]
                    }
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                rowKey:function(row){
                    return 'layout-' + row.uuid;
                },
                handleCreate:function(){
                    var self = this;
                    for(var i=0; i<self.layouts.length; i++){
                        var finded = false;
                        for(var j=0; j<self.table.rows.length; j++){
                            if(self.layouts[i].id == self.table.rows[j].layoutId){
                                finded = true;
                                break;
                            }
                        }
                        if(!finded) self.dialog.addLayout.layouts.push(self.layouts[i]);
                    }
                    self.dialog.addLayout.visible = true;
                },
                handleAddLayoutClose:function(){
                    var self = this;
                    self.dialog.addLayout.layouts.splice(0, self.dialog.addLayout.layouts.length);
                    self.dialog.addLayout.loading = false;
                    self.dialog.addLayout.visible = false;
                },
                handleAddLayoutSubmit:function(){
                    var self = this;
                    var nodes = self.$refs.addLayoutTree.getCheckedNodes();
                    if(!nodes || nodes.length<=0){
                        self.$message({
                            type:'error',
                            message:'您没有选择布局'
                        });
                        return;
                    }
                    var layoutIds = [];
                    for(var i=0; i<nodes.length; i++){
                        layoutIds.push(nodes[i].id);
                    }
                    self.dialog.addLayout.loading = true;
                    ajax.post('/tetris/bvc/model/terminal/layout/permission/add', {
                        terminalId:self.terminalId,
                        layoutIds: $.toJSON(layoutIds)
                    }, function(data, status){
                        self.dialog.addLayout.loading = false;
                        if(status !== 200) return;
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.table.rows.push(data[i]);
                            }
                        }
                        self.handleAddLayoutClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleDelete:function(){

                },
                handleRowDelete:function(scope){
                    var self = this;
                    var row = scope.row;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'危险操作',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['是否移除布局?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/terminal/layout/permission/delete', {
                                    id:row.id
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    for(var i=0; i<self.table.rows.length; i++){
                                        if(self.table.rows[i].id == row.id){
                                            self.table.rows.splice(i, 1);
                                            break;
                                        }
                                    }
                                }, null, ajax.TOTAL_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                load:function(){
                    var self = this;
                    self.table.rows.splice(0, self.table.rows.length);
                    ajax.post('/tetris/bvc/model/terminal/layout/permission/load', {
                        terminalId:self.terminalId
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.table.rows.push(data[i]);
                            }
                        }
                    });
                },
                queryLayouts:function(){
                    var self = this;
                    self.layouts.splice(0, self.layouts.length);
                    ajax.post('/tetris/bvc/model/terminal/layout/permission/query/layouts', {
                        terminalId:self.terminalId
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.layouts.push(data[i]);
                            }
                        }
                    });
                }
            },
            created:function(){
                var self = this;
                self.load();
                self.queryLayouts();
            }
        });

    };

    var destroy = function(){

    };

    var groupList = {
        path:'/' + pageId + '/:terminalId/:terminalName',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});