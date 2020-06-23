/**
 * Created by lvdeyang on 2020/6/5.
 */
define([
    'text!' + window.APPPATH + 'tetris/model/terminal/screen/page-terminal-screen.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'tetris/model/terminal/screen/page-terminal-screen.css'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-terminal-screen';

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
                terminalId:p.terminalId,
                terminalName:p.terminalName,
                groups: context.getProp('groups'),
                channels:[],
                screenPrimaryKeys:[],
                table:{
                    rows:[]
                },
                dialog: {
                    addScreen:{
                        visible:false,
                        loading:false,
                        screenPrimaryKeys:[],
                        screens:[],
                        props:{
                            label:'name',
                            children:'children',
                            isLeaf:'isLeaf'
                        },
                        channels:[],
                        currentScreenWhenSelectChannel:'',
                        channelsVisible:false
                    }
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                rowKey:function(row){
                    return 'screen-' + row.uuid;
                },
                handleCreate:function(){
                    var self = this;
                    self.dialog.addScreen.screenPrimaryKeys.splice(0, self.dialog.addScreen.screenPrimaryKeys.length);
                    for(var i=0; i<self.screenPrimaryKeys.length; i++){
                        var primaryKey = self.screenPrimaryKeys[i];
                        var exist = false;
                        for(var j=0; j<self.table.rows.length; j++){
                            var row = self.table.rows[j];
                            if(row.screenPrimaryKey == primaryKey.screenPrimaryKey){
                                exist = true;
                                break;
                            }
                        }
                        if(!exist){
                            self.dialog.addScreen.screenPrimaryKeys.push(primaryKey);
                        }
                    }
                    self.dialog.addScreen.visible = true;
                },
                handleAddScreenClose:function(){
                    var self = this;
                    self.dialog.addScreen.loading = false;
                    self.dialog.addScreen.visible = false;
                    self.dialog.addScreen.screenPrimaryKeys.splice(0, self.dialog.addScreen.screenPrimaryKeys.length);
                    self.dialog.addScreen.screens.splice(0, self.dialog.addScreen.screens.length);
                    self.dialog.addScreen.channels.splice(0, self.dialog.addScreen.channels.length);
                    self.dialog.addScreen.channelsVisible = false;
                },
                handleAddScreenSubmit:function(){
                    var self = this;
                    
                },
                handleDelete:function(){

                },
                handleRowEdit:function(scope){
                    var self = this;
                    var row = scope.row;

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
                                h('p', null, ['此操作将永久删除该屏幕，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/terminal/screen/delete', {
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
                handleCheckChange:function(data, checked){
                    var self = this;
                    if(checked){
                        self.dialog.addScreen.screens.push({
                            screenPrimaryKeyId:data.id,
                            screenPrimaryKeyName:data.name,
                            screenPrimaryKey:data.screenPrimaryKey,
                            name:data.name,
                            terminalChannelId:'',
                            terminalChannelName:''
                        });
                    }else{
                        for(var i=0; i<self.dialog.addScreen.screens.length; i++){
                            if(self.dialog.addScreen.screens[i].screenPrimaryKeyId == data.id){
                                self.dialog.addScreen.screens.splice(i, 1);
                                break;
                            }
                        }
                    }
                },
                handleScreenRemove:function(scope){
                    var self = this;
                    var row = scope.row;
                    for(var i=0; i<self.dialog.addScreen.screens.length; i++){
                        if(self.dialog.addScreen.screens[i] === row){
                            self.dialog.addScreen.screens.splice(i, 1);
                            break;
                        }
                    }
                    for(var i=0; i<self.dialog.addScreen.screenPrimaryKeys.length; i++){
                        if(self.dialog.addScreen.screenPrimaryKeys[i].id == row.screenPrimaryKeyId){
                            self.$refs.screenPrimaryKeyTree.setChecked(self.dialog.addScreen.screenPrimaryKeys[i], false);
                            break;
                        }
                    }
                },
                handleSelectChannel:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.addScreen.channels.splice(0, self.dialog.addScreen.channels.length);
                    for(var i=0; i<self.channels.length; i++){
                        var channel = self.channels[i];
                        var exist = false;
                        for(var j=0; j<self.table.rows.length; j++){
                            var row = self.table.rows[j];
                            if(row.terminalChannelId == channel.id){
                                exist = true;
                                break;
                            }
                        }
                        if(!exist){
                            for(var j=0; j<self.dialog.addScreen.screens.length; j++){
                                if(self.dialog.addScreen.screens[j].terminalChannelId == channel.id){
                                    exist = true;
                                    break;
                                }
                            }
                            if(!exist){
                                self.dialog.addScreen.channels.push(channel);
                            }
                        }
                    }
                    self.dialog.addScreen.currentScreenWhenSelectChannel = row;
                    self.dialog.addScreen.channelsVisible = true;
                },
                handleAddScreenSelectChannelClose:function(){
                    var self = this;
                    var data = self.$refs.addScreenChannelsTree.getCurrentNode();
                    if(data) {
                        self.dialog.addScreen.currentScreenWhenSelectChannel.terminalChannelId = data.id;
                        self.dialog.addScreen.currentScreenWhenSelectChannel.terminalChannelName = data.name;
                    }
                    self.dialog.addScreen.channels.splice(0, self.dialog.addScreen.channels.length);
                    self.dialog.addScreen.currentScreenWhenSelectChannel = '';
                    self.dialog.addScreen.channelsVisible = false;
                },
                handleScreenRemoveChannelInfo:function(scope, e){
                    e.preventDefault();
                    e.stopPropagation();
                    var self = this;
                    var row = scope.row;
                    row.terminalChannelId = '';
                    row.terminalChannelName = '';
                },
                load:function(){
                    var self = this;
                    self.table.rows.splice(0, self.table.rows.length);
                    ajax.post('/tetris/bvc/model/terminal/screen/load', {
                        terminalId:self.terminalId
                    }, function(data, status){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.table.rows.push(data[i]);
                            }
                        }
                    });
                },
                queryChannels:function(){
                    var self = this;
                    ajax.post('/tetris/bvc/model/terminal/channel/load/video/decode', {
                        terminalId:self.terminalId
                    }, function(data){
                       if(data && data.length>0){
                           for(var i=0; i<data.length; i++){
                               self.channels.push(data[i]);
                           }
                       }
                    });
                },
                queryScreenPrimaryKeys:function(){
                    var self = this;
                    ajax.post('/tetris/bvc/model/terminal/screen/primary/key/load', null, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.screenPrimaryKeys.push(data[i]);
                            }
                        }
                    });
                }
            },
            created:function(){
                var self = this;
                self.load();
                self.queryChannels();
                self.queryScreenPrimaryKeys();
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