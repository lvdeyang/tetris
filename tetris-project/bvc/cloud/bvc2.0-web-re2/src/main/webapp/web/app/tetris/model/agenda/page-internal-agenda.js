/**
 * Created by lvdeyang on 2020/6/30.
 */
define([
    'text!' + window.APPPATH + 'tetris/model/agenda/page-internal-agenda.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'tetris/model/agenda/page-internal-agenda.css'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-internal-agenda';

    var init = function(){

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
                audioOperationTypes:[],
                businessInfoTypes:[],
                modeTypes:[],
                audioTypes:[],
                audioPriorities:[],
                table:{
                    rows:[],
                    pageSize:50,
                    pageSizes:[50, 100, 200, 400],
                    currentPage:0,
                    total:0
                },
                dialog: {
                    createAgenda:{
                        visible:false,
                        loading:false,
                        name:'',
                        remark:'',
                        businessInfoTypeName:''
                    },
                    editAgenda:{
                        visible:false,
                        loading:false,
                        id:'',
                        name:'',
                        remark:'',
                        businessInfoTypeName:''
                    },
                    addRole:{
                        visible:false,
                        loading:false,
                        agenda:'',
                        roles:[],
                        props:{
                            label:'name',
                            children:'children',
                            isLeaf:'isLeaf'
                        }
                    },
                    addCustomAudio:{
                        visible:false,
                        loading:false,
                        agenda:'',
                        roles:[],
                        props:{
                            label:'name',
                            children:'channels',
                            isLeaf:'isLeaf'
                        }
                    }
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                loadAudioTypes:function(){
                    var self = this;
                    self.audioTypes.splice(0, self.audioTypes.length);
                    ajax.post('/tetris/bvc/model/agenda/forward/query/audio/types', null, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.audioTypes.push(data[i]);
                            }
                        }
                    });
                },
                loadAudioPriorities:function(){
                    var self = this;
                    self.audioPriorities.splice(0, self.audioPriorities.length);
                    ajax.post('/tetris/bvc/model/agenda/query/audio/priorities', null, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.audioPriorities.push(data[i]);
                            }
                        }
                    });
                },
                rowKey:function(row){
                    return 'agenda-' + row.uuid;
                },
                handleCreate:function(){
                    var self = this;
                    self.dialog.createAgenda.visible = true;
                },
                handleCreateAgendaClose:function(){
                    var self = this;
                    self.dialog.createAgenda.name = '';
                    self.dialog.createAgenda.remark = '';
                    self.dialog.createAgenda.businessInfoTypeName = '';
                    self.dialog.createAgenda.loading = false;
                    self.dialog.createAgenda.visible = false;
                },
                handleCreateAgendaSubmit:function(){
                    var self = this;
                    self.dialog.createAgenda.loading = true;
                    ajax.post('/tetris/bvc/model/agenda/add', {
                        name:self.dialog.createAgenda.name,
                        remark:self.dialog.createAgenda.remark,
                        businessInfoTypeName:self.dialog.createAgenda.businessInfoTypeName
                    }, function(data, status){
                        self.dialog.createAgenda.loading = false;
                        if(status !== 200) return;
                        self.table.rows.push(data);
                        self.table.total += 1;
                        self.handleCreateAgendaClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleDelete:function(){

                },
                handleRowEdit:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.editAgenda.id = row.id;
                    self.dialog.editAgenda.name = row.name;
                    self.dialog.editAgenda.remark = row.remark;
                    self.dialog.editAgenda.businessInfoTypeName = row.businessInfoTypeName;
                    self.dialog.editAgenda.visible = true;
                },
                handleEditAgendaClose:function(){
                    var self = this;
                    self.dialog.editAgenda.id = '';
                    self.dialog.editAgenda.name = '';
                    self.dialog.editAgenda.remark = '';
                    self.dialog.editAgenda.businessInfoTypeName = '';
                    self.dialog.editAgenda.loading = false;
                    self.dialog.editAgenda.visible = false;
                },
                handleEditAgendaSubmit:function(){
                    var self = this;
                    self.dialog.editAgenda.loading = true;
                    ajax.post('/tetris/bvc/model/agenda/edit', {
                        id:self.dialog.editAgenda.id,
                        name:self.dialog.editAgenda.name,
                        remark:self.dialog.editAgenda.remark,
                        businessInfoTypeName:self.dialog.editAgenda.businessInfoTypeName
                    }, function(data, status){
                        self.dialog.editAgenda.loading = false;
                        if(status!==200) return;
                        for(var i=0; i<self.table.rows.length; i++){
                            if(self.table.rows[i].id == data.id){
                                $.extend(self.table.rows[i], data, true);
                                break;
                            }
                        }
                        self.handleEditAgendaClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleEditForward:function(scope){
                    var row = scope.row;
                    window.location.hash = '#/page-internal-agenda-forward/'+row.id + '/' + row.name;
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
                                h('p', null, ['此操作将永久删除该议程，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/agenda/delete', {
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
                handleSizeChange:function(size){
                    var self = this;
                    self.table.pageSize = size;
                    self.load(1);
                },
                handleCurrentChange:function(currentPage){
                    var self = this;
                    self.load(currentPage);
                },
                load:function(currentPage){
                    var self = this;
                    self.table.rows.splice(0, self.table.rows.length);
                    ajax.post('/tetris/bvc/model/agenda/load/internal/agenda', {
                        currentPage:currentPage,
                        pageSize:self.table.pageSize
                    }, function(data, status){
                        var total = data.total;
                        var rows = data.rows;
                        if(rows && rows.length>0){
                            for(var i=0; i<rows.length; i++){
                                if(!rows[i].customAudios) rows[i].customAudios = [];
                                if(!rows[i].roles) rows[i].roles = [];
                                self.table.rows.push(rows[i]);
                            }
                        }
                        self.table.total = total;
                        self.table.currentPage = currentPage;
                    });
                },
                queryBusinessTypes:function(){
                    var self = this;
                    ajax.post('/tetris/bvc/model/agenda/query/business/types', null, function(data){
                        for(var i=0; i<data.length; i++){
                            self.businessInfoTypes.push(data[i]);
                        }
                    });
                },
                globalCustomAudioChange:function(val, scope){
                    var row = scope.row;
                    ajax.post('/tetris/bvc/model/agenda/global/custom/audio/change', {
                        id:row.id,
                        globalCustomAudio:val
                    }, function(data){
                        row.audioType = data.audioType;
                        row.audioTypeName = data.audioTypeName;
                        if(!val){
                            row.customAudios.splice(0, row.customAudios.length);
                        }
                    });
                },
                handleAddRole:function(scope){
                    var self = this;
                    self.dialog.addRole.agenda = scope.row;
                    self.dialog.addRole.roles.splice(0, self.dialog.addRole.roles.length);
                    ajax.post('/tetris/bvc/model/role/load/all/internal', null, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                var role = data[i];
                                var exist = false;
                                if(self.dialog.addRole.agenda.roles && self.dialog.addRole.agenda.roles.length>0){
                                    for(var j=0; j<self.dialog.addRole.agenda.roles.length; j++){
                                        var row = self.dialog.addRole.agenda.roles[j];
                                        if(row.id == role.id){
                                            exist = true;
                                            break;
                                        }
                                    }
                                }
                                if(!exist){
                                    if(!self.dialog.addRole.agenda.roles) self.dialog.addRole.agenda.roles = [];
                                    self.dialog.addRole.roles.push(role);
                                }
                            }
                        }
                        self.dialog.addRole.visible = true;
                    });
                },
                handleAddRoleClose:function(){
                    var self = this;
                    self.dialog.addRole.loading = false;
                    self.dialog.addRole.visible = false;
                    self.dialog.addRole.agenda = '';
                    self.dialog.addRole.roles.splice(0, self.dialog.addRole.roles.length);
                },
                handleAddRoleSubmit:function(){
                    var self = this;
                    var roles = self.$refs.addRoleTree.getCheckedNodes();
                    if(!roles || roles.length<=0){
                        self.$message({
                            type:'error',
                            message:'您没有选择角色'
                        });
                        return;
                    }
                    var roleIds = [];
                    for(var i=0; i<roles.length; i++){
                        roleIds.push(roles[i].id);
                    }
                    self.dialog.addRole.loading = true;
                    ajax.post('/tetris/bvc/model/role/agenda/permission/add', {
                        agendaId:self.dialog.addRole.agenda.id,
                        roleIds: $.toJSON(roleIds)
                    }, function(data, status){
                        self.dialog.addRole.loading = false;
                        if(status !== 200) return;
                        for(var i=0; i<data.length; i++){
                            self.dialog.addRole.agenda.roles.push(data[i]);
                        }
                        self.handleAddRoleClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleRoleDelete:function(role, scope){
                    var self = this;
                    var row = scope.row;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'提示',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['是否移除该角色?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/role/agenda/permission/delete', {
                                    roleId:role.id,
                                    agendaId:row.id
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    for(var i=0; i<row.roles.length; i++){
                                        if(row.roles[i].id == role.id){
                                            row.roles.splice(i, 1);
                                            break;
                                        }
                                    }
                                   if(data && data.length>0){
                                       for(var i=0; i<data.length; i++){
                                           for(var j=0; j<row.customAudios.length; j++){
                                               if(data[i].id == row.customAudios[j].id){
                                                   row.customAudios.splice(j, 1);
                                                   break;
                                               }
                                           }
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
                handleAgendaVolumeChange:function(val, scope){
                    var self = this;
                    var row = scope.row;
                    ajax.post('/tetris/bvc/model/agenda/volume/change', {
                        id:row.id,
                        volume:val
                    });
                },
                handleAddCustomAudio:function(scope){
                    var self = this;
                    self.dialog.addCustomAudio.agenda = scope.row;
                    self.dialog.addCustomAudio.roles.splice(0, self.dialog.addCustomAudio.roles.length);
                    ajax.post('/tetris/bvc/model/role/load/by/agenda/id/and/type/with/channel', {
                        agendaId:self.dialog.addCustomAudio.agenda.id,
                        type:'AUDIO_ENCODE'
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                data[i].isLeaf = false;
                                if(data[i].channels && data[i].channels.length>0){
                                    for(var j=0; j<data[i].channels.length; j++){
                                        data[i].channels[j].isLeaf = true;
                                    }
                                }
                            }
                            if(self.dialog.addCustomAudio.agenda.customAudios && self.dialog.addCustomAudio.agenda.customAudios.length>0){
                                for(var i=0; i<self.dialog.addCustomAudio.agenda.customAudios.length; i++){
                                    for(var j=0; j<data.length; j++){
                                        var handleBreak = false;
                                        if(data[j].channels && data[j].channels.length>0){
                                            for(var k=0; k<data[j].channels.length; k++){
                                                if(data[j].channels[k].id == self.dialog.addCustomAudio.agenda.customAudios[i].sourceId){
                                                    data[j].channels.splice(k, 1);
                                                    handleBreak = true;
                                                    break;
                                                }
                                            }
                                        }
                                        if(handleBreak) break;
                                    }
                                }
                            }
                            while(true){
                                var handleBreak = data.length>0?false:true;
                                for(var i=0; i<data.length; i++){
                                    if(!data[i].channels || data[i].channels.length<=0){
                                        data.splice(i, 1);
                                        break;
                                    }else{
                                        if(i === data.length-1){
                                            handleBreak = true;
                                        }
                                    }
                                }
                                if(handleBreak) break;
                            }
                            for(var i=0; i<data.length; i++){
                                self.dialog.addCustomAudio.roles.push(data[i]);
                            }
                        }
                        self.dialog.addCustomAudio.visible = true;
                    });
                },
                handleAddCustomAudioClose:function(){
                    var self = this;
                    self.dialog.addCustomAudio.visible = false;
                    self.dialog.addCustomAudio.loading = false;
                    self.dialog.addCustomAudio.agenda = '';
                    self.dialog.addCustomAudio.roles.splice(0, self.dialog.addCustomAudio.roles.length);
                },
                handleAddCustomAudioSubmit:function(){
                    var self = this;
                    var nodes = self.$refs.addCustomAudioTree.getCheckedNodes();
                    var roleChannelIds = [];
                    if(nodes && nodes.length>0){
                        for(var i=0; i<nodes.length; i++){
                            if(!nodes[i].isLeaf) continue;
                            roleChannelIds.push(nodes[i].id);
                        }
                    }
                    if(roleChannelIds.length <= 0){
                        self.$message({
                            type:'error',
                            message:'您没有选择通道'
                        });
                        return;
                    }
                    self.dialog.addCustomAudio.loading = true;
                    ajax.post('/tetris/bvc/model/custom/audio/add/agenda/audio', {
                        agendaId:self.dialog.addCustomAudio.agenda.id,
                        roleChannelIds: $.toJSON(roleChannelIds)
                    }, function(data, status, message){
                        self.dialog.addCustomAudio.loading = false;
                        if(status !== 200) {
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        if(data && data.length>0){
                            if(!self.dialog.addCustomAudio.agenda.customAudios) self.dialog.addCustomAudio.agenda.customAudios = [];
                            for(var i=0; i<data.length; i++){
                                self.dialog.addCustomAudio.agenda.customAudios.push(data[i]);
                            }
                        }
                        self.handleAddCustomAudioClose();
                    }, null, ajax.TOTAL_CATCH_CODE);

                },
                handleCustomAudioDelete:function(customAudio, scope){
                    var self = this;
                    var row = scope.row;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'提示',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['是否移除该角色通道?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/custom/audio/remove', {
                                    id:customAudio.id
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    if(row.customAudios && row.customAudios.length>0){
                                        for(var i=0; i<row.customAudios.length; i++){
                                            if(row.customAudios[i].id === customAudio.id){
                                                row.customAudios.splice(i, 1);
                                                break;
                                            }
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
                handleAudioTypeChange:function(val, scope){
                    var row = scope.row;
                    ajax.post('/tetris/bvc/model/agenda/audio/type/change', {
                        id:row.id,
                        audioType:val
                    });
                },
                handleAudioPriorityChange:function(val, scope){
                    var self = this;
                    var row = scope.row;
                    ajax.post('/tetris/bvc/model/agenda/audio/priority/change', {
                        id:row.id,
                        audioPriority:val
                    }, function(data){
                        if(data){
                            row.audioPriorityName = data.audioPriorityName;
                        }
                    });
                }
            },
            created:function(){
                var self = this;
                self.load(1);
                self.loadAudioTypes();
                self.loadAudioPriorities();
                self.queryBusinessTypes();
            }
        });

    };

    var destroy = function(){

    };

    var groupList = {
        path:'/' + pageId,
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});