/**
 * Created by lvdeyang on 2020/7/9.
 */
define([
    'text!' + window.APPPATH + 'tetris/model/agenda/page-internal-agenda-forward.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'tetris/model/agenda/page-internal-agenda-forward.css'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-internal-agenda-forward';

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
                agenda:'',
                audioTypes:[],
                layoutPositionSelectionTypes:[],
                table:{
                    rows:[]
                },
                dialog:{
                    addSource:{
                        visible:false,
                        loading:false,
                        roles:[],
                        agendaForward:'',
                        props:{
                            label:'name',
                            children:'channels',
                            isLeaf:'isLeaf'
                        }
                    },
                    layoutPositionSelectionTypeChange:{
                        visible:false,
                        loading:false,
                        autoIncrement:false,
                        isLoop:false,
                        loopTime:10,
                        serialNum:1,
                        source:''
                    },
                    addDestination:{
                        visible:false,
                        loading:false,
                        roles:[],
                        agendaForward:'',
                        props:{
                            label:'name',
                            children:'channels',
                            isLeaf:'isLeaf'
                        }
                    },
                    addLayoutScope:{
                        visible:false,
                        loading:false,
                        layouts:[],
                        agendaForward:'',
                        min:1,
                        max:1,
                        props:{
                            label:'name'
                        }
                    },
                    addCustomAudio:{
                        visible:false,
                        loading:false,
                        roles:[],
                        agendaForward:'',
                        props:{
                            label:'name',
                            children:'channels',
                            isLeaf:'isLeaf'
                        }
                    }
                }
            },
            computed:{
                dialogAddLayoutScopeMin:function(){
                    var self = this;
                    return self.dialog.addLayoutScope.min;
                },
                dialogLayoutPositionSelectionTypeChangeAutoIncrement:function(){
                    var self = this;
                    return self.dialog.layoutPositionSelectionTypeChange.autoIncrement;
                },
                dialogLayoutPositionSelectionTypeChangeLoopTime:function(){
                    var self = this;
                    return self.dialog.layoutPositionSelectionTypeChange.loopTime;
                }
            },
            watch:{
                dialogAddLayoutScopeMin:function(){
                    var self = this;
                    if(self.dialog.addLayoutScope.min > self.dialog.addLayoutScope.max){
                        self.dialog.addLayoutScope.max = self.dialog.addLayoutScope.min;
                    }
                },
                dialogLayoutPositionSelectionTypeChangeAutoIncrement:function(val){
                    var self = this;
                    if(val){
                        self.dialog.layoutPositionSelectionTypeChange.isLoop = false;
                        self.dialog.layoutPositionSelectionTypeChange.loopTime = 10;
                    }
                },
                dialogLayoutPositionSelectionTypeChangeLoopTime:function(val){
                    var self = this;
                    if(!val) return;
                    val = parseInt(val);
                    if(isNaN(val)){
                        self.dialog.layoutPositionSelectionTypeChange.loopTime = 1;
                    }
                }
            },
            methods:{
                rowKey:function(row){
                    return 'agenda-forward-' + row.id;
                },
                loadAgenda:function(){
                    var self = this;
                    ajax.post('/tetris/bvc/model/agenda/query/by/id', {
                        id:self.agendaId
                    }, function(data){
                        if(data) self.agenda = data;
                    });
                },
                loadInternalForward:function(){
                    var self = this;
                    self.table.rows.splice(0, self.table.rows.length);
                    ajax.post('/tetris/bvc/model/agenda/forward/load/internal', {
                        agendaId:self.agendaId
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                if(!data[i].sources) data[i].sources = [];
                                if(!data[i].destinations) data[i].destinations = [];
                                if(!data[i].layouts) data[i].layouts = [];
                                if(!data[i].audios) data[i].audios = [];
                                self.table.rows.push(data[i]);
                            }
                        }
                    });
                },
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
                loadLayoutPositionSelectionTypes:function(){
                    var self = this;
                    self.layoutPositionSelectionTypes.splice(0, self.layoutPositionSelectionTypes.length);
                    ajax.post('/tetris/bvc/model/agenda/forward/query/layout/position/selection/type', null, function(data){
                       if(data && data.length>0){
                           for(var i=0; i<data.length; i++){
                               self.layoutPositionSelectionTypes.push(data[i]);
                           }
                       }
                    });
                },
                handleCreate:function(){
                    var self = this;
                    ajax.post('/tetris/bvc/model/agenda/forward/add/internal', {
                        agendaId:self.agendaId
                    }, function(data){
                        if(data){
                            self.table.rows.push(data);
                        }
                    });
                },
                handleDelete:function(){

                },
                handleRowDelete:function(scope){
                    var self = this;
                    var row = scope.row;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'提示',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['是否移除该议程转发?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/agenda/forward/remove', {
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
                handleAudioTypeChange:function(val, scope){
                    var self = this;
                    var row = scope.row;
                    ajax.post('/tetris/bvc/model/agenda/forward/handle/audio/type/change', {
                        id:row.id,
                        audioType:val
                    }, function(){
                       if(val!=='CUSTOM'){
                           row.audios.splice(0, row.audios.length);
                       }
                    });
                },
                handleVolumeChange:function(val, scope){
                    var self = this;
                    var row = scope.row;
                    ajax.post('/tetris/bvc/model/agenda/forward/handle/volume/change', {
                        id:row.id,
                        volume:val
                    });
                },
                sourceSummary:function(source){
                    var self = this;
                    var summary = '';
                    if(source.layoutPositionSelectionType==='AUTO_INCREMENT'){
                        summary += '自增长';
                    }else{
                        summary += ('分屏'+ source.serialNum);
                        if(source.isLoop){
                            summary += ', 轮询, ';
                            summary += (source.loopTime + '秒');
                        }
                    }
                    return summary;
                },
                handleAddSource:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.addSource.agendaForward = row;
                    self.dialog.addSource.roles.splice(0, self.dialog.addSource.roles.length);
                    ajax.post('/tetris/bvc/model/role/load/by/agenda/id/and/type/with/channel', {
                        agendaId:self.agendaId,
                        type:'VIDEO_ENCODE'
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
                            if(row.sources && row.sources.length>0){
                                for(var i=0; i<row.sources.length; i++){
                                    var existSource = row.sources[i];
                                    for(var j=0; j<data.length; j++){
                                        var handleBreak = false;
                                        for(var k=0; k<data[j].channels.length; k++){
                                            if(data[j].channels[k].id == existSource.sourceId){
                                                data[j].channels.splice(k, 1);
                                                handleBreak = true;
                                                break;
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
                                self.dialog.addSource.roles.push(data[i]);
                            }
                        }
                        self.dialog.addSource.visible = true;
                    });
                },
                handleLayoutPositionSelectionTypeChange:function(source){
                    var self = this;
                    self.dialog.layoutPositionSelectionTypeChange.source = source;
                    self.dialog.layoutPositionSelectionTypeChange.autoIncrement = source.layoutPositionSelectionType==='AUTO_INCREMENT'?true:false;
                    self.dialog.layoutPositionSelectionTypeChange.isLoop = source.isLoop;
                    self.dialog.layoutPositionSelectionTypeChange.loopTime = source.loopTime;
                    self.dialog.layoutPositionSelectionTypeChange.serialNum = source.serialNum;
                    self.dialog.layoutPositionSelectionTypeChange.visible = true;
                },
                handleLayoutPositionSelectionTypeChangeClose:function(){
                    var self = this;
                    self.dialog.layoutPositionSelectionTypeChange.visible = false;
                    self.dialog.layoutPositionSelectionTypeChange.loading = false;
                    self.dialog.layoutPositionSelectionTypeChange.autoIncrement = false;
                    self.dialog.layoutPositionSelectionTypeChange.serialNum = 1;
                    self.dialog.layoutPositionSelectionTypeChange.source = '';
                },
                handleLayoutPositionSelectionTypeChangeSubmit:function(){
                    var self = this;
                    self.dialog.layoutPositionSelectionTypeChange.loading = true;
                    ajax.post('/tetris/bvc/model/agenda/forward/handle/layout/position/selection/type/change', {
                        id:self.dialog.layoutPositionSelectionTypeChange.source.id,
                        layoutPositionSelectionType:self.dialog.layoutPositionSelectionTypeChange.autoIncrement?'AUTO_INCREMENT':'CONFIRM',
                        serialNum:self.dialog.layoutPositionSelectionTypeChange.serialNum,
                        isLoop:self.dialog.layoutPositionSelectionTypeChange.isLoop,
                        loopTime:self.dialog.layoutPositionSelectionTypeChange.loopTime
                    }, function(data, status, message){
                        self.dialog.layoutPositionSelectionTypeChange.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        self.dialog.layoutPositionSelectionTypeChange.source.layoutPositionSelectionType = self.dialog.layoutPositionSelectionTypeChange.autoIncrement?'AUTO_INCREMENT':'CONFIRM';
                        self.dialog.layoutPositionSelectionTypeChange.source.serialNum = self.dialog.layoutPositionSelectionTypeChange.serialNum;
                        for(var i=0; i<self.table.rows.length; i++){
                            var end = false;
                            for(var j=0; j<self.table.rows[i].sources.length; j++){
                                if(self.table.rows[i].sources[j].id === self.dialog.layoutPositionSelectionTypeChange.source.id){
                                    self.table.rows[i].sources[j].isLoop = self.dialog.layoutPositionSelectionTypeChange.isLoop;
                                    self.table.rows[i].sources[j].loopTime = self.dialog.layoutPositionSelectionTypeChange.loopTime;
                                    end = true;
                                    break;
                                }
                            }
                            if(end) break;
                        }
                        self.handleLayoutPositionSelectionTypeChangeClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleAddSourceClose:function(){
                    var self = this;
                    self.dialog.addSource.visible = false;
                    self.dialog.addSource.loading = false;
                    self.dialog.addSource.roles.splice(0, self.dialog.addSource.roles.length);
                    self.dialog.addSource.agendaForward = '';
                },
                handleAddSourceSubmit:function(){
                    var self = this;
                    var nodes = self.$refs.addSourceTree.getCheckedNodes();
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
                    self.dialog.addSource.loading = true;
                    ajax.post('/tetris/bvc/model/agenda/forward/add/internal/source', {
                        agendaForwardId:self.dialog.addSource.agendaForward.id,
                        roleChannelIds: $.toJSON(roleChannelIds)
                    }, function(data, status, message){
                        self.dialog.addSource.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        if(data && data.length){
                            for(var i=0; i<data.length; i++){
                                self.dialog.addSource.agendaForward.sources.push(data[i]);
                            }
                        }
                        self.handleAddSourceClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleRemoveSource:function(source, scope){
                    var self = this;
                    var row = scope.row;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'提示',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['是否移除该转发源?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/agenda/forward/remove/source', {
                                    id:source.id
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    if(row.sources && row.sources.length>0){
                                        for(var i=0; i<row.sources.length; i++){
                                            if(row.sources[i].id == source.id){
                                                row.sources.splice(i, 1);
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
                handleAddDestination:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.addDestination.agendaForward = row;
                    self.dialog.addDestination.roles.splice(0, self.dialog.addDestination.roles.length);
                    ajax.post('/tetris/bvc/model/role/load/by/agenda/id', {
                        agendaId:self.agendaId
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                var finded = false;
                                if(row.destinations && row.destinations.length>0){
                                    for(var j=0; j<row.destinations.length; j++){
                                        if(data[i].id == row.destinations[j].destinationId){
                                            finded = true;
                                            break;
                                        }
                                    }
                                }
                                if(!finded) self.dialog.addDestination.roles.push(data[i]);
                            }
                        }
                        self.dialog.addDestination.visible = true;
                    });
                },
                handleAddDestinationClose:function(){
                    var self = this;
                    self.dialog.addDestination.roles.splice(0, self.dialog.addDestination.roles.length);
                    self.dialog.addDestination.agendaForward = '';
                    self.dialog.addDestination.visible = false;
                    self.dialog.addDestination.loading = false;
                },
                handleAddDestinationSubmit:function(){
                    var self = this;
                    var nodes = self.$refs.addDestinationTree.getCheckedNodes();
                    if(nodes.length <= 0){
                        self.$message({
                            type:'error',
                            message:'您没有选择角色'
                        });
                        return;
                    }
                    var roleIds = [];
                    for(var i=0; i<nodes.length; i++){
                        roleIds.push(nodes[i].id);
                    }
                    self.dialog.addSource.loading = true;
                    ajax.post('/tetris/bvc/model/agenda/forward/add/destination', {
                        agendaForwardId:self.dialog.addDestination.agendaForward.id,
                        roleIds: $.toJSON(roleIds)
                    }, function(data, status, message){
                        self.dialog.addSource.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.dialog.addDestination.agendaForward.destinations.push(data[i]);
                            }
                        }
                        self.handleAddDestinationClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleRemoveDestination:function(destination, scope){
                    var self = this;
                    var row = scope.row;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'提示',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['是否移除该转发目的?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/agenda/forward/remove/destination', {
                                    id:destination.id
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    if(row.destinations && row.destinations.length>0){
                                        for(var i=0; i<row.destinations.length; i++){
                                            if(row.destinations[i].id == destination.id){
                                                row.destinations.splice(i, 1);
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
                handleAddLayoutScope:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.addLayoutScope.agendaForward = row;
                    self.dialog.addLayoutScope.layouts.splice(0, self.dialog.addLayoutScope.layouts.length);
                    ajax.post('/tetris/bvc/model/layout/load/all', null, function(data, status, message){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                var finded = false;
                                if(row.layouts && row.layouts.length>0){
                                    for(var j=0; j<row.layouts.length; j++){
                                        if(row.layouts[j].layoutId == data[i].id){
                                            finded = true;
                                            break;
                                        }
                                    }
                                }
                                if(!finded) self.dialog.addLayoutScope.layouts.push(data[i]);
                            }
                        }
                        self.dialog.addLayoutScope.visible = true;
                    });
                },
                handleAddLayoutScopeClose:function(){
                    var self = this;
                    self.dialog.addLayoutScope.visible = false;
                    self.dialog.addLayoutScope.loading = false;
                    self.dialog.addLayoutScope.layouts.splice(0, self.dialog.addLayoutScope.layouts.length);
                    self.dialog.addLayoutScope.agendaForward = '';
                    self.dialog.addLayoutScope.min = 1;
                    self.dialog.addLayoutScope.max = 1;
                },
                handleAddLayoutScopeSubmit:function(){
                    var self = this;
                    var node = self.$refs.addLayoutScopeTree.getCurrentNode();
                    if(!node){
                        self.$message({
                            type:'error',
                            message:'您没有选择虚拟源'
                        });
                        return;
                    }
                    var layoutId = node.id;
                    self.dialog.addLayoutScope.loading = true;
                    ajax.post('/tetris/bvc/model/agenda/forward/add/layout/scope', {
                        id:self.dialog.addLayoutScope.agendaForward.id,
                        min:self.dialog.addLayoutScope.min,
                        max:self.dialog.addLayoutScope.max,
                        layoutId:layoutId
                    }, function(data, status, message){
                        self.dialog.addLayoutScope.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.dialog.addLayoutScope.agendaForward.layouts.push(data[i]);
                            }
                        }
                        self.handleAddLayoutScopeClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleRemoveLayoutScope:function(layout, scope){
                    var self = this;
                    var row = scope.row;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'提示',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['是否移除该虚拟源?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/agenda/forward/remove/layout/scope', {
                                    id:layout.id
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    if(row.layouts && row.layouts.length>0){
                                        for(var i=0; i<row.layouts.length; i++){
                                            if(row.layouts[i].id == layout.id){
                                                row.layouts.splice(i, 1);
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
                handleAddCustomAudio:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.addCustomAudio.agendaForward = row;
                    self.dialog.addCustomAudio.roles.splice(0, self.dialog.addCustomAudio.roles.length);
                    ajax.post('/tetris/bvc/model/role/load/by/agenda/id/and/type/with/channel', {
                        agendaId:self.agendaId,
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
                            if(row.audios && row.audios.length>0){
                                for(var i=0; i<row.audios.length; i++){
                                    var existAudio = row.audios[i];
                                    for(var j=0; j<data.length; j++){
                                        var handleBreak = false;
                                        for(var k=0; k<data[j].channels.length; k++){
                                            if(data[j].channels[k].id == existAudio.sourceId){
                                                data[j].channels.splice(k, 1);
                                                handleBreak = true;
                                                break;
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
                    self.dialog.addCustomAudio.roles.splice(0, self.dialog.addCustomAudio.roles.length);
                    self.dialog.addCustomAudio.agendaForward = '';
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
                    ajax.post('/tetris/bvc/model/agenda/forward/add/custom/audio', {
                        agendaForwardId:self.dialog.addCustomAudio.agendaForward.id,
                        roleChannelIds: $.toJSON(roleChannelIds)
                    }, function(data, status, message){
                        self.dialog.addCustomAudio.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        if(data && data.length){
                            for(var i=0; i<data.length; i++){
                                self.dialog.addCustomAudio.agendaForward.audios.push(data[i]);
                            }
                        }
                        self.handleAddCustomAudioClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleRemoveCustomAudio:function(audio, scope){
                    var self = this;
                    var row = scope.row;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'提示',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['是否移除该音频?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/agenda/forward/remove/custom/audio', {
                                    id:audio.id
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    if(row.audios && row.audios.length>0){
                                        for(var i=0; i<row.audios.length; i++){
                                            if(row.audios[i].id == audio.id){
                                                row.audios.splice(i, 1);
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
                }
            },
            created:function(){
                var self = this;
                self.loadAgenda();
                self.loadAudioTypes();
                self.loadLayoutPositionSelectionTypes();
                self.loadInternalForward();
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