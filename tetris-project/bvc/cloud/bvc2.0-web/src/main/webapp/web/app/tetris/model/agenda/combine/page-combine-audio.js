/**
 * Created by lvdeyang on 2020/7/7.
 */
define([
    'text!' + window.APPPATH + 'tetris/model/agenda/combine/page-combine-audio.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'tetris/model/agenda/combine/page-combine-audio.css'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-combine-audio';

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
                roles:[],
                bundles:[],
                table:{
                    rows:[]
                },
                dialog: {
                    createCombineAudio:{
                        visible:false,
                        loading:false,
                        name:''
                    },
                    editCombineAudio:{
                        visible:false,
                        loading:false,
                        id:'',
                        name:''
                    },
                    editSrc:{
                        visible:false,
                        loading:false,
                        id:'',
                        srcType:'',
                        srcsVisible:false,
                        srcs:[],
                        props:{
                            label:'name',
                            children:'channels'
                        },
                        selectedSrcs:[]
                    }
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                rowKey:function(row){
                    return 'combine-audio-' + row.uuid;
                },
                handleCreate:function(){
                    var self = this;
                    self.dialog.createCombineAudio.visible = true;
                },
                handleCreateCombineAudioClose:function(){
                    var self = this;
                    self.dialog.createCombineAudio.name = '';
                    self.dialog.createCombineAudio.loading = false;
                    self.dialog.createCombineAudio.visible = false;
                },
                handleCreateCombineAudioSubmit:function(){
                    var self = this;
                    self.dialog.createCombineAudio.loading = true;
                    ajax.post('/tetris/bvc/model/agenda/combine/audio/add', {
                        businessId:self.agendaId,
                        businessType:'AGENDA',
                        name:self.dialog.createCombineAudio.name,
                    }, function(data, status){
                        self.dialog.createCombineAudio.loading = false;
                        if(status !== 200) return;
                        self.table.rows.push(data);
                        self.handleCreateCombineAudioClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleDelete:function(){

                },
                handleRowEdit:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.editCombineAudio.id = row.id;
                    self.dialog.editCombineAudio.name = row.name;
                    self.dialog.editCombineAudio.visible = true;
                },
                handleEditCombineAudioClose:function(){
                    var self = this;
                    self.dialog.editCombineAudio.id = '';
                    self.dialog.editCombineAudio.name = '';
                    self.dialog.editCombineAudio.loading = false;
                    self.dialog.editCombineAudio.visible = false;
                },
                handleEditCombineAudioSubmit:function(){
                    var self = this;
                    self.dialog.editCombineAudio.loading = true;
                    ajax.post('/tetris/bvc/model/agenda/combine/audio/edit/name', {
                        id:self.dialog.editCombineAudio.id,
                        name:self.dialog.editCombineAudio.name
                    }, function(data, status){
                        self.dialog.editCombineAudio.loading = false;
                        if(status!==200) return;
                        for(var i=0; i<self.table.rows.length; i++){
                            if(self.table.rows[i].id == data.id){
                                self.table.rows.splice(i, 1, data);
                            }
                        }
                        self.handleEditCombineAudioClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleEditSrc:function(scope){
                    var self = this;
                    var row = scope.row;
                    ajax.post('/tetris/bvc/model/agenda/combine/audio/load/srcs', {
                        combineAudioId:row.id
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.dialog.editSrc.selectedSrcs.push({
                                    id:data[i].combineAudioSrcType==='ROLE_CHANNEL'?parseInt(data[i].srcId):data[i].srcId,
                                    type:data[i].combineAudioSrcType,
                                    name:data[i].name
                                });
                            }
                        }
                        self.dialog.editSrc.id = row.id;
                        self.dialog.editSrc.visible = true;
                    });
                },
                handleSelectSrc:function(){
                    var self = this;
                    if(!self.dialog.editSrc.srcType) return;
                    if(self.dialog.editSrc.srcType === 'ROLE_CHANNEL'){
                        ajax.post('/tetris/bvc/model/role/agenda/permission/load/with/channels', {
                            agendaId:self.agendaId,
                            channelType:'AUDIO_ENCODE'
                        }, function(data){
                            if(data && data.length>0){
                                for(var i=0; i<data.length; i++){
                                    self.dialog.editSrc.srcs.push(data[i]);
                                }
                            }
                            self.dialog.editSrc.srcsVisible = true;
                        });
                    }
                },
                handleEditSrcSrcsClose:function(){
                    var self = this;
                    var selected = [];
                    for(var i=0; i<self.dialog.editSrc.srcs.length; i++){
                        for(var j=0; j<self.dialog.editSrc.srcs[i].channels.length; j++){
                            if(self.dialog.editSrc.srcs[i].channels[j].checked){
                                selected.push(self.dialog.editSrc.srcs[i].channels[j]);
                            }
                        }
                    }
                    for(var i=0; i<selected.length; i++){
                        var finded = false;
                        for(var j=0; j<self.dialog.editSrc.selectedSrcs.length; j++){
                            var existSrc = self.dialog.editSrc.selectedSrcs[j];
                            if(existSrc.type===self.dialog.editSrc.srcType && existSrc.id===selected[i].id){
                                finded = true;
                                break;
                            }
                        }
                        if(!finded) {
                            var d = {
                                id:selected[i].id,
                                type:self.dialog.editSrc.srcType
                            };
                            if(d.type === 'ROLE_CHANNEL'){
                                d.name = selected[i].roleName + '-' + selected[i].name;
                            }
                            self.dialog.editSrc.selectedSrcs.push(d);
                        }
                    }
                    self.dialog.editSrc.srcsVisible = false;
                    self.dialog.editSrc.srcs.splice(0, self.dialog.editSrc.srcs.length);
                },
                handleSrcRemove:function(src){
                    var self = this;
                    for(var i=0; i<self.dialog.editSrc.selectedSrcs.length; i++){
                        if(self.dialog.editSrc.selectedSrcs[i] === src){
                            self.dialog.editSrc.selectedSrcs.splice(i, 1);
                            return;
                        }
                    }
                },
                handleEditSrcClose:function(){
                    var self = this;
                    self.dialog.editSrc.id = '';
                    self.dialog.editSrc.srcType = '';
                    self.dialog.editSrc.selectedSrcs.splice(0, self.dialog.editSrc.selectedSrcs.length);
                    self.dialog.editSrc.visible = false;
                    self.dialog.editSrc.loading = false;
                },
                handleEditSrcSubmit:function(){
                    var self = this;
                    if(self.dialog.editSrc.selectedSrcs.length <= 0){
                        self.$message({
                            type:'error',
                            message:'请选择源'
                        });
                        return;
                    }
                    self.dialog.editSrc.loading = true;
                    ajax.post('/tetris/bvc/model/agenda/combine/audio/edit/srcs', $.toJSON({
                        id:self.dialog.editSrc.id,
                        srcs:self.dialog.editSrc.selectedSrcs
                    }), function(data, status){
                        self.dialog.editSrc.loading = false;
                        if(status !== 200) return;
                        self.handleEditSrcClose();
                    }, 'application/json; charset=UTF-8', ajax.NO_ERROR_CATCH_CODE);
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
                                h('p', null, ['此操作将永久删除该混音，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/agenda/combine/audio/delete', {
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
                load:function(){
                    var self = this;
                    self.table.rows.splice(0, self.table.rows.length);
                    ajax.post('/tetris/bvc/model/agenda/combine/audio/load', {
                        businessId:self.agendaId,
                        businessType:'AGENDA'
                    }, function(data, status){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.table.rows.push(data[i]);
                            }
                        }
                    });
                },
                queryRoles:function(){
                    var self = this;
                    ajax.post('/tetris/bvc/model/role/agenda/permission/load/with/channels', {
                        agendaId:self.agendaId,
                        channelType:'VIDEO_ENCODE'
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.roles.push(data[i]);
                            }
                        }
                    });
                },
                queryBundles:function(){

                }
            },
            created:function(){
                var self = this;
                self.load();
                self.queryRoles();
                self.$nextTick(function(){
                    $(self.$el).on('click.cell-edit', '.button-edit', function(){
                        var $cell = $(this).closest('td.cell');
                        self.handleEditSrc($cell);
                    });
                    $(self.$el).on('click.cell-delete', '.button-delete', function(){
                        var $cell = $(this).closest('td.cell');
                        self.handleDeleteSrc($cell);
                    });
                });
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