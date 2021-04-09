/**
 * Created by lvdeyang on 2020/6/5.
 */
define([
    'text!' + window.APPPATH + 'tetris/model/terminal/page-terminal-bundle-channel.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'tetris/model/terminal/page-terminal-bundle-channel.css'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-terminal-bundle-channel';

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
                terminalBundleId:p.terminalBundleId,
                terminalBundleName:p.terminalBundleName,
                types:[],
                table:{
                    rows:[]
                },
                dialog: {
                    addChannel:{
                        visible:false,
                        loading:false,
                        channelId:'',
                        type:''
                    },
                    editChannel:{
                        visible:false,
                        loading:false,
                        id:'',
                        channelId:'',
                        type:''
                    }
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                rowKey:function(row){
                    return 'terminal-' + row.uuid;
                },
                handleCreate:function(){
                    var self = this;
                    self.dialog.addChannel.visible = true;
                },
                handleAddChannelClose:function(){
                    var self = this;
                    self.dialog.addChannel.channelId = '';
                    self.dialog.addChannel.type = '';
                    self.dialog.addChannel.loading = false;
                    self.dialog.addChannel.visible = false;
                },
                handleAddChannelSubmit:function(){
                    var self = this;
                    self.dialog.addChannel.loading = true;
                    ajax.post('/tetris/bvc/model/terminal/bundle/channel/add', {
                        terminalBundleId:self.terminalBundleId,
                        channelId:self.dialog.addChannel.channelId,
                        type:self.dialog.addChannel.type
                    }, function(data, status){
                        self.dialog.addChannel.loading = false;
                        if(status !== 200) return;
                        self.table.rows.push(data);
                        self.table.total += 1;
                        self.handleAddChannelClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleDelete:function(){

                },
                handleRowEdit:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.editChannel.id = row.id;
                    self.dialog.editChannel.channelId = row.channelId;
                    self.dialog.editChannel.type = row.type;
                    self.dialog.editChannel.loading = false;
                    self.dialog.editChannel.visible = true;
                },
                handleEditChannelClose:function(){
                    var self = this;
                    self.dialog.editChannel.id = '';
                    self.dialog.editChannel.channelId = '';
                    self.dialog.editChannel.type = '';
                    self.dialog.editChannel.loading = false;
                    self.dialog.editChannel.visible = false;
                },
                handleEditChannelSubmit:function(){
                    var self = this;
                    self.dialog.editChannel.loading = true;
                    ajax.post('/tetris/bvc/model/terminal/bundle/channel/edit', {
                        id:self.dialog.editChannel.id,
                        channelId:self.dialog.editChannel.channelId,
                        type:self.dialog.editChannel.type
                    }, function(data, status){
                        self.dialog.editChannel.loading = false;
                        if(status!==200) return;
                        for(var i=0; i<self.table.rows.length; i++){
                            if(self.table.rows[i].id == data.id){
                                self.table.rows.splice(i, 1, data);
                            }
                        }
                        self.handleEditChannelClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
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
                                h('p', null, ['此操作将永久删除该通道，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/terminal/bundle/channel/delete', {
                                    id:row.id
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    for(var i=0; i<self.table.rows.length; i++){
                                        if(self.table.rows[i].id == row.id){
                                            self.table.rows.splice(i, 1);
                                        }
                                    }
                                    self.table.total -= 1;
                                    if(self.table.rows.length==0 && self.table.total>0){
                                        self.load(self.table.currentPage-1);
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
                    ajax.post('/tetris/bvc/model/terminal/bundle/channel/load', {
                        terminalBundleId:self.terminalBundleId
                    }, function(data, status){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.table.rows.push(data[i]);
                            }
                        }
                    });
                },
                queryTypes:function(){
                    var self = this;
                    ajax.post('/tetris/bvc/model/terminal/bundle/channel/query/types', null, function(data){
                        for(var i in data){
                            self.types.push({
                                id:i,
                                name:data[i]
                            });
                        }
                    });
                }
            },
            created:function(){
                var self = this;
                self.load();
                self.queryTypes();
            }
        });

    };

    var destroy = function(){

    };

    var groupList = {
        path:'/' + pageId + '/:terminalId/:terminalName/:terminalBundleId/:terminalBundleName',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});