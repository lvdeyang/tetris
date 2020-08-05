/**
 * Created by lvdeyang on 2020/7/9.
 */
define([
    'text!' + window.APPPATH + 'tetris/model/agenda/page-agenda-forward.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'tetris/model/agenda/page-agenda-forward.css'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-agenda-forward';

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
                businessInfoTypes:[],
                forwardType:[{
                    id:'VIDEO',
                    name:'视频'
                },{
                    id:'AUDIO',
                    name:'音频'
                },{
                    id:'AUDIO_VIDEO',
                    name:'音视频'
                }],
                sourceTypes:[{
                    id:'ROLE',
                    name:'角色',
                    type:'VIDEO_AUDIO'
                },{
                    id:'ROLE_CHANNEL',
                    name:'角色通道',
                    type:'VIDEO_AUDIO'
                },/*{
                    id:'BUNDLE',
                    name:'设备',
                    type:'VIDEO_AUDIO'
                },{
                    id:'CHANNEL',
                    name:'设备通道',
                    type:'VIDEO_AUDIO'
                },*/{
                    id:'COMBINE_VIDEO',
                    name:'合屏',
                    type:'VIDEO'
                },{
                    id:'COMBINE_AUDIO',
                    name:'混音',
                    type:'AUDIO'
                }],
                destinationTypes:[{
                    id:'ROLE',
                    name:'角色',
                    type:'VIDEO_AUDIO'
                },{
                    id:'ROLE_CHANNEL',
                    name:'角色通道',
                    type:'VIDEO_AUDIO'
                }/*,{
                    id:'BUNDLE',
                    name:'设备',
                    type:'VIDEO_AUDIO'
                },{
                    id:'CHANNEL',
                    name:'设备通道',
                    type:'VIDEO_AUDIO'
                }*/],
                table:{
                    rows:[]
                },
                dialog:{
                    addForward:{
                        visible:false,
                        loading:false,
                        type:'AUDIO_VIDEO',
                        businessInfoType:'',
                        sourceTypes:[],
                        audioSourceTypes:[],
                        destinationTypes:[],
                        sourceType:'',
                        source:'',
                        audioSourceType:'',
                        audioSource:'',
                        destinationType:'',
                        destination:''
                    },
                    selectSourceRole:{
                        visible:false,
                        data:[],
                        props:{
                            label:'roleName'
                        },
                        type:''
                    },
                    selectSourceRoleChannel:{
                        visible:false,
                        data:[],
                        props:{
                            label:'roleName',
                            children:'channels'
                        },
                        type:''
                    },
                    selectSourceBundle:{
                        visible:false,
                        data:[],
                        type:''
                    },
                    selectSourceBundleChannel:{
                        visible:false,
                        data:[],
                        type:''
                    },
                    selectCombineVideo:{
                        visible:false,
                        data:[],
                        props:{
                            label:'name'
                        },
                        type:''
                    },
                    selectCombineAudio:{
                        visible:false,
                        data:[],
                        props:{
                            label:'name'
                        },
                        type:''
                    },
                    selectDestinationRole:{
                        visible:false,
                        data:[],
                        props:{
                            label:'roleName'
                        }
                    },
                    selectDestinationRoleChannel:{
                        visible:false,
                        data:[],
                        props:{
                            label:'roleName',
                            children:'channels'
                        }
                    },
                    selectDestinationBundle:{
                        visible:false,
                        data:[]
                    },
                    selectDestinationBundleChannel:{
                        visible:false,
                        data:[]
                    }
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                rowKey:function(row){
                    return 'forward-' + row.uuid;
                },
                handleCreate:function(){
                    var self = this;
                    self.dialog.addForward.visible = true;
                    self.dialog.addForward.sourceTypes.splice(0, self.dialog.addForward.sourceTypes.length);
                    for(var i=0; i<self.sourceTypes.length; i++){
                        if(self.sourceTypes[i].type.indexOf('VIDEO') >= 0){
                            self.dialog.addForward.sourceTypes.push(self.sourceTypes[i]);
                        }
                    }
                    self.dialog.addForward.audioSourceTypes.splice(0, self.dialog.addForward.audioSourceTypes.length);
                    for(var i=0; i<self.sourceTypes.length; i++){
                        if(self.sourceTypes[i].type.indexOf('AUDIO') >= 0){
                            self.dialog.addForward.audioSourceTypes.push(self.sourceTypes[i]);
                        }
                    }
                    self.dialog.addForward.destinationTypes.splice(0, self.dialog.addForward.destinationTypes.length);
                    for(var i=0; i<self.destinationTypes.length; i++){
                        self.dialog.addForward.destinationTypes.push(self.destinationTypes[i]);
                    }
                },
                forwardTypeChange:function(type){
                    var self = this;
                    if(type === 'VIDEO'){
                        self.dialog.addForward.audioSourceType = '';
                        self.dialog.addForward.audioSource = '';
                    }else if(type === 'AUDIO'){
                        self.dialog.addForward.sourceType = '';
                        self.dialog.addForward.source = '';
                    }
                },
                handleSelectSource:function(type){
                    var self = this;
                    var filed = 'sourceType';
                    if(type === 'audio') var filed = 'audioSourceType';
                    if(self.dialog.addForward[filed] === 'ROLE'){
                        self.dialog.selectSourceRole.type = type;
                        self.dialog.selectSourceRole.data.splice(0,  self.dialog.selectSourceRole.data.length);
                        ajax.post('/tetris/bvc/model/role/agenda/permission/load', {
                            agendaId:self.agendaId
                        }, function(data){
                            if(data && data.length>0){
                                for(var i=0; i<data.length; i++){
                                    self.dialog.selectSourceRole.data.push(data[i]);
                                }
                            }
                            self.dialog.selectSourceRole.visible = true;
                        });
                    }else if(self.dialog.addForward[filed] === 'ROLE_CHANNEL'){
                        var channelType = '';
                        //if(self.dialog.addForward.type === 'VIDEO' || self.dialog.addForward.type === 'AUDIO_VIDEO'){
                        if(type === 'video'){
                            channelType = 'VIDEO_ENCODE';
                        }else{
                            channelType = 'AUDIO_ENCODE';
                        }
                        self.dialog.selectSourceRoleChannel.type = type;
                        self.dialog.selectSourceRoleChannel.data.splice(0,  self.dialog.selectSourceRoleChannel.data.length);
                        ajax.post('/tetris/bvc/model/role/agenda/permission/load/with/channels', {
                            agendaId:self.agendaId,
                            channelType:channelType
                        }, function(data){
                            if(data && data.length>0){
                                for(var i=0; i<data.length; i++) {
                                    self.dialog.selectSourceRoleChannel.data.push(data[i]);
                                }
                            }
                            self.dialog.selectSourceRoleChannel.visible = true;
                        });
                    }else if(self.dialog.addForward[filed] === 'BUNDLE'){

                    }else if(self.dialog.addForward[filed] === 'CHANNEL'){

                    }else if(self.dialog.addForward[filed] === 'COMBINE_VIDEO'){
                        self.dialog.selectCombineVideo.type = type;
                        self.dialog.selectCombineVideo.data.splice(0, self.dialog.selectCombineVideo.data.length);
                        ajax.post('/tetris/bvc/model/agenda/combine/video/load', {
                            businessId:self.agendaId,
                            businessType:'AGENDA'
                        }, function(data){
                            if(data && data.length>0){
                                for(var i=0; i<data.length; i++){
                                    self.dialog.selectCombineVideo.data.push(data[i]);
                                }
                            }
                            self.dialog.selectCombineVideo.visible = true;
                        });
                    }else if(self.dialog.addForward[filed] === 'COMBINE_AUDIO'){
                        self.dialog.selectCombineAudio.type = type;
                        self.dialog.selectCombineAudio.data.splice(0, self.dialog.selectCombineAudio.data.length);
                        ajax.post('/tetris/bvc/model/agenda/combine/audio/load', {
                            businessId:self.agendaId,
                            businessType:'AGENDA'
                        }, function(data){
                            if(data && data.length>0){
                                for(var i=0; i<data.length; i++){
                                    self.dialog.selectCombineAudio.data.push(data[i]);
                                }
                            }
                            self.dialog.selectCombineAudio.visible = true;
                        });
                    }
                },
                handleSelectSourceRoleClose:function(){
                    var self = this;
                    var node = self.$refs.selectSourceRoleTree.getCurrentNode();
                    if(node){
                        var filed = 'source';
                        if(self.dialog.selectSourceRole.type === 'audio') filed = 'audioSource';
                        Vue.set( self.dialog.addForward, filed, {
                            id:node.roleId,
                            type:'ROLE',
                            name:node.roleName
                        });
                    }
                    self.dialog.selectSourceRole.data.splice(0, self.dialog.selectSourceRole.data.length);
                    self.dialog.selectSourceRole.type = '';
                    self.dialog.selectSourceRole.visible = false;
                },
                handleSelectSourceRoleChannelClose:function(){
                    var self = this;
                    var node = self.$refs.selectSourceRoleChannelTree.getCurrentNode();
                    if(!node) return;
                    if(node.channels){
                        self.$message({
                            type:'error',
                            message:'请选择通道'
                        });
                        return;
                    }
                    var filed = 'source';
                    if(self.dialog.selectSourceRoleChannel.type === 'audio') filed = 'audioSource';
                    Vue.set( self.dialog.addForward, filed, {
                        id:node.id,
                        type:'ROLE_CHANNEL',
                        name:node.roleName+'-'+node.name
                    });
                    self.dialog.selectSourceRoleChannel.data.splice(0, self.dialog.selectSourceRoleChannel.data.length);
                    self.dialog.selectSourceRoleChannel.visible = false;
                },
                handleSelectCombineVideoClose:function(){
                    var self = this;
                    var node = self.$refs.selectCombineVideoTree.getCurrentNode();
                    if(node){
                        Vue.set( self.dialog.addForward, 'source', {
                            id:node.id,
                            type:'COMBINE_VIDEO',
                            name:node.name
                        });
                    }
                    self.dialog.selectCombineVideo.data.splice(0, self.dialog.selectCombineVideo.data.length);
                    self.dialog.selectCombineVideo.visible = false;
                },
                handleSelectCombineAudioClose:function(){
                    var self = this;
                    var node = self.$refs.selectCombineAudioTree.getCurrentNode();
                    if(node){
                        Vue.set( self.dialog.addForward, 'audioSource', {
                            id:node.id,
                            type:'COMBINE_AUDIO',
                            name:node.name
                        });
                    }
                    self.dialog.selectCombineAudio.data.splice(0, self.dialog.selectCombineAudio.data.length);
                    self.dialog.selectCombineAudio.visible = false;
                },
                handleSelectDestinationRoleClose:function(){
                    var self = this;
                    var node = self.$refs.selectDestinationRoleTree.getCurrentNode();
                    if(node){
                        Vue.set( self.dialog.addForward, 'destination', {
                            id:node.roleId,
                            type:'ROLE',
                            name:node.roleName
                        });
                    }
                    self.dialog.selectDestinationRole.data.splice(0, self.dialog.selectDestinationRole.data.length);
                    self.dialog.selectDestinationRole.visible = false;
                },
                handleSelectDestinationRoleChannelClose:function(){
                    var self = this;
                    var node = self.$refs.selectDestinationRoleChannelTree.getCurrentNode();
                    if(!node) return;
                    if(node.channels){
                        self.$message({
                            type:'error',
                            message:'请选择通道'
                        });
                        return;
                    }
                    Vue.set( self.dialog.addForward, 'destination', {
                        id:node.id,
                        type:'ROLE_CHANNEL',
                        name:node.roleName+'-'+node.name
                    });
                    self.dialog.selectDestinationRoleChannel.data.splice(0, self.dialog.selectDestinationRoleChannel.data.length);
                    self.dialog.selectDestinationRoleChannel.visible = false;
                },
                handleSelectDestination:function(){
                    var self = this;
                    if(self.dialog.addForward.destinationType === 'ROLE'){
                        self.dialog.selectDestinationRole.data.splice(0,  self.dialog.selectDestinationRole.data.length);
                        ajax.post('/tetris/bvc/model/role/agenda/permission/load', {
                            agendaId:self.agendaId
                        }, function(data){
                            if(data && data.length>0){
                                for(var i=0; i<data.length; i++){
                                    self.dialog.selectDestinationRole.data.push(data[i]);
                                }
                            }
                            self.dialog.selectDestinationRole.visible = true;
                        });
                    }else if(self.dialog.addForward.destinationType === 'ROLE_CHANNEL'){
                        var channelType = '';
                        if(self.dialog.addForward.type === 'VIDEO'){
                            channelType = 'VIDEO_DECODE';
                        }else{
                            channelType = 'AUDIO_DECODE';
                        }
                        self.dialog.selectDestinationRoleChannel.data.splice(0,  self.dialog.selectDestinationRoleChannel.data.length);
                        ajax.post('/tetris/bvc/model/role/agenda/permission/load/with/channels', {
                            agendaId:self.agendaId,
                            channelType:channelType
                        }, function(data){
                            if(data && data.length>0){
                                for(var i=0; i<data.length; i++) {
                                    self.dialog.selectDestinationRoleChannel.data.push(data[i]);
                                }
                            }
                            self.dialog.selectDestinationRoleChannel.visible = true;
                        });
                    }else if(self.dialog.addForward.destinationType === 'BUNDLE'){

                    }else if(self.dialog.addForward.destinationType === 'CHANNEL'){

                    }
                },
                handleAddForwardClose:function(){
                    var self = this;
                    self.dialog.addForward.visible = false;
                    self.dialog.addForward.loading = false;
                    self.dialog.addForward.type = 'AUDIO_VIDEO';
                    self.dialog.addForward.sourceType = '';
                    self.dialog.addForward.source = '';
                    self.dialog.addForward.audioSourceType = '';
                    self.dialog.addForward.audioSource = '';
                    self.dialog.addForward.destinationType = '';
                    self.dialog.addForward.destination = '';
                },
                handleAddForwardSubmit:function(){
                    var self = this;
                    if(!self.dialog.addForward.type){
                        self.$message({
                            type:'error',
                            message:'转发类型为空'
                        });
                        return;
                    }
                    if(!self.dialog.addForward.source){
                        self.$message({
                            type:'error',
                            message:'视频源为空'
                        });
                        return;
                    }
                    if(!self.dialog.addForward.audioSource){
                        self.$message({
                            type:'error',
                            message:'音频源为空'
                        });
                        return;
                    }
                    if(!self.dialog.addForward.destination){
                        self.$message({
                            type:'error',
                            message:'目的为空'
                        });
                        return;
                    }
                    self.dialog.addForward.loading = true;
                    ajax.post('/tetris/bvc/model/agenda/forward/add', {
                        type:self.dialog.addForward.type,
                        businessInfoType:self.dialog.addForward.businessInfoType,
                        sourceType:self.dialog.addForward.source.type,
                        sourceId:self.dialog.addForward.source.id,
                        audioSourceType:self.dialog.addForward.audioSource.type,
                        audioSourceId:self.dialog.addForward.audioSource.id,
                        destinationType:self.dialog.addForward.destination.type,
                        destinationId:self.dialog.addForward.destination.id,
                        agendaId:self.agendaId
                    }, function(data, status){
                        self.dialog.addForward.loading = false;
                        if(status !== 200) return;
                        self.table.rows.push(data);
                        self.handleAddForwardClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
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
                                h('p', null, ['此操作将永久删除该转发，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/agenda/forward/delete', {
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
                                }, null, ajax.TOTAL_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                load:function(currentPage){
                    var self = this;
                    self.table.rows.splice(0, self.table.rows.length);
                    ajax.post('/tetris/bvc/model/agenda/forward/load', {
                        agendaId:self.agendaId
                    }, function(data, status){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.table.rows.push(data[i]);
                            }
                        }
                    });
                },
                loadBusinessInfoTypes:function(){
                    var self = this;
                    self.businessInfoTypes.splice(0, self.businessInfoTypes.length);
                    ajax.post('/tetris/bvc/model/agenda/forward/query/business/info/types', null, function(data){
                       if(data && data.length>0){
                           for(var i=0; i<data.length; i++){
                               self.businessInfoTypes.push(data[i]);
                           }
                       }
                    });
                }
            },
            created:function(){
                var self = this;
                self.load();
                self.loadBusinessInfoTypes();
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