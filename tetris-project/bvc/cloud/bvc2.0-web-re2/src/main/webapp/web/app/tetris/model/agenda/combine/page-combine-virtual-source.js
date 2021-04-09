/**
 * Created by lqxuhv on 2020/11/18.
 */
define([
    'text!' + window.APPPATH + 'tetris/model/agenda/combine/page-combine-virtual-source.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'echarts',
    'date',
    'jquery.layout.auto',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'tetris/model/agenda/combine/page-combine-virtual-source.css',
    'css!' + window.APPPATH + 'tetris/model/terminal/editor/graph-node-icons/style.css'
], function(tpl, config, $, ajax, context, commons, Vue, echarts){

    var pageId = 'page-combine-virtual-source';

    var vueInstance = null;

    var init = function(p){

        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        var  user = context.getProp('user');
        user.nickname = user.name;

        vueInstance = new Vue({
            el: '#' + pageId + '-wrapper',
            data: {
                bp:window.BASEPATH,
                t:window.TOKEN,
                menus: context.getProp('menus'),
                user: user,
                groups: context.getProp('groups'),
                groupId: p.groupId,
                groupName: p.groupName,
                audioTypes:[],
                audioPriorities:[],
                group:'',
                filterMemberAndRole:'',
                members:{
                    data:[],
                    props:{
                        label:'name',
                        children:'children',
                        isLeaf:'isLeaf'
                    }
                },
                roles:{
                    data:[],
                    props:{
                        label:'name',
                        children:'children',
                        isLeaf:'isLeaf'
                    }
                },
                agenda:{
                    filterName:'',
                    filter:[],
                    rows:[],
                    current:''
                },
                forward:{
                    rows:[],
                    currentId:'',
                    current:''
                },
                layout:{
                    visible:false,
                    loading:false,
                    data:[],
                    filter:[],
                    current:''
                },
                dialog:{
                    createAgenda:{
                        visible:false,
                        loading:false,
                        name:'',
                        remark:''
                    },
                    editAgenda:{
                        visible:false,
                        loading:false,
                        id:'',
                        name:'',
                        remark:''
                    },
                    agendaAudioSettings:{
                        visible:false,
                        loading:false,
                        row:''
                    },
                    addSpeakerRole:{
                        visible:false,
                        loading:false,
                        scope:'',
                        name:''
                    },
                    addAudienceRole:{
                        visible:false,
                        loading:false,
                        scope:'',
                        name:''
                    },
                    addForward:{
                        visible:false,
                        loading:false,
                        name:''
                    },
                    editForward:{
                        visible:false,
                        loading:false,
                        id:'',
                        name:''
                    },
                    addCustomAudio:{
                        visible:false,
                        loading:false,
                        type:'',
                        members:[],
                        roles:[],
                        filterName:'',
                        props:{
                            label:'name',
                            children:'children',
                            isLeaf:'isLeaf'
                        }
                    },
                    addDestination:{
                        visible:false,
                        loading:false,
                        members:[],
                        roles:[],
                        filterName:'',
                        props:{
                            label:'name',
                            children:'children',
                            isLeaf:'isLeaf'
                        }
                    },
                    setLoop:{
                        visible:false,
                        loading:false,
                        position:'',
                        loopTime:10,
                        members:{
                            data:[],
                            props:{
                                label:'name',
                                children:'children',
                                isLeaf:'isLeaf'
                            }
                        },
                        selected:[]
                    },
                    editRole:{
                        visible:false,
                        loading:false,
                        members:{
                            data:[],
                            props:{
                                label:'name',
                                children:'children',
                                isLeaf:'isLeaf'
                            }
                        },
                        roles:{
                            data:[],
                            currentId:''
                        },
                        roleMembers:[]
                    },
                    speakerSet:{
                        visible:false,
                        loading:false,
                        member:''
                    },
                    addMember:{
                        visible:false,
                        loading:false,
                        userAndHalls:[],
                        props:{
                            label:'name',
                            children:'children',
                            isLeaf:'isLeaf'
                        },
                        filterName:''
                    },
                    editSpeakerChannel:{
                        visible:false,
                        loadings:[false, false],
                        role:'',
                        terminal:{
                            data:[],
                            currentId:'',
                            current:''
                        },
                        permissions:[]
                    },
                    editSpeakerChannelName:{
                        visible:false,
                        loading:false,
                        channel:'',
                        id:'',
                        name:''
                    },
                    editSpeakerChannelPermission:{
                        visible:false,
                        loading:false,
                        channel:'',
                        terminalChannels:[],
                        currentTerminalChannelId:''
                    },
                    editParam:{
                        visible:false,
                        loading:false,
                        id:'',
                        videoFormat:'',
                        audioFormat:'',
                        portReuse:false,
                        adaptions:[{
                            adaptionId:'',
                            adaptionName:'',
                            videoRate:'',
                            videoResolution:'',
                            frameRate:'',
                            audioRate:'',
                            videoFormat:'',
                            audioFormat:''
                        }]
                    }
                },
                options:{
                    startGroup:{
                        loading:false
                    },
                    stopGroup:{
                        loading:false
                    },
                    startRecord:{
                        loading:false,
                        record:false
                    }
                }
            },
            computed:{
                forwardCurrentId:function(){
                    var self = this;
                    return self.forward.currentId;
                },
                layoutVisible:function(){
                    var self = this;
                    return self.layout.visible;
                },
                agendaFilterName:function(){
                    var self = this;
                    return self.agenda.filterName;
                },
                dialogSetLoopLoopTime:function(){
                    var self = this;
                    return self.dialog.setLoop.loopTime;
                },
                dialogEditRoleRolesCurrentId:function(){
                    var self = this;
                    return self.dialog.editRole.roles.currentId;
                },
                dialogAddMemberFilterName:function(){
                    var self = this;
                    return self.dialog.addMember.filterName;
                },
                dialogEditSpeakerChannelTerminalCurrentId:function(){
                    var self = this;
                    return self.dialog.editSpeakerChannel.terminal.currentId;
                },
                dialogAddDestinationFilterName:function(){
                    var self = this;
                    return self.dialog.addDestination.filterName;
                },
                dialogAddCustomAudioFilterName:function(){
                    var self = this;
                    return self.dialog.addCustomAudio.filterName;
                }
            },
            watch:{
                filterMemberAndRole:function(val){
                    var self = this;
                    self.$refs.memberTree.filter(val);
                    self.$refs.roleTree.filter(val);
                },
                forwardCurrentId:function(val){
                    var self = this;
                    if(val == '0') return;
                    for(var i=0; i<self.forward.rows.length; i++){
                        if(self.forward.rows[i].id == self.forward.currentId){
                            self.forward.current = self.forward.rows[i];
                            break;
                        }
                    }
                    self.loadForwardSettings();
                },
                layoutVisible:function(val){
                    var self = this;
                    if(val && self.layout.data.length<=0){
                        self.loadLayouts();
                    }
                },
                agendaFilterName:function(val){
                    var self = this;
                    self.agenda.filter.splice(0, self.agenda.filter.length);
                    for(var i=0; i<self.agenda.rows.length; i++){
                        if(!val){
                            self.agenda.filter.push(self.agenda.rows[i]);
                        }else if(self.agenda.rows[i].name && self.agenda.rows[i].name.indexOf(val)>=0){
                            self.agenda.filter.push(self.agenda.rows[i]);
                        }
                    }
                },
                dialogSetLoopLoopTime:function(val){
                    var self = this;
                    val = parseInt(val);
                    var nv = 1;
                    if(isNaN(val)){
                        nv = 1;
                    }else if(val <= 0){
                        nv = 1;
                    }else if(val > 9999){
                        nv = 9999;
                    }else{
                        nv = val;
                    }
                    if(nv != val){
                        self.$nextTick(function(){
                            self.dialog.setLoop.loopTime = nv;
                        });
                    }
                },
                dialogEditRoleRolesCurrentId:function(val){
                    var self = this;
                    if(val != 0) self.loadMemberByRole();
                },
                dialogAddMemberFilterName:function(val){
                    var self = this;
                    self.$refs.addMemberTree.filter(val);
                },
                dialogEditSpeakerChannelTerminalCurrentId:function(val){
                    var self = this;
                    if(!val) return;
                    self.loadEditSpeakerChannelPermission();
                },
                dialogAddDestinationFilterName:function(val){
                    var self = this;
                    self.$refs.addDestinationMemberTree.filter(['ROLE_AND_MEMBER', val]);
                    self.$refs.addDestinationRoleTree.filter(['ROLE_AND_MEMBER', val]);
                },
                dialogAddCustomAudioFilterName:function(val){
                    var self = this;
                    self.$refs.addCustomAudioMemberTree.filter(['AUDIO_ENCODE', val]);
                    self.$refs.addCustomAudioRoleTree.filter(['AUDIO_ENCODE', val]);
                }
            },
            methods:{
            	gotoCustomAgenda:function(){
                    var self = this;
                    window.location.hash = '#/page-custom-agenda/' + self.groupId + '/' + self.groupName;
                },
                handleDropdownCommand:function(cmd){
                    var self = this;
                    if(cmd === 'editParam'){
                        self.handleEditParam();
                    }
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
                setCurrentForwardLayout:function(layout){
                    var self = this;
                    if(layout &&
                        layout.positions &&
                        layout.positions.length>0){
                        var positions = layout.positions;
                        for(var i=0; i<positions.length; i++){
                            var position = positions[i];
                            position.ui = {};
                            if(position.type === 'REMOTE'){
                                var targetSource = null;
                                if(self.forward.current.sources && self.forward.current.sources.length>0){
                                    var sources = self.forward.current.sources;
                                    for(var j=0; j<sources.length; j++){
                                        if(sources[j].serialNum == position.serialNum){
                                            targetSource = sources[j];
                                            break;
                                        }
                                    }
                                }
                                if(!targetSource){
                                    position.ui.content = '未设置';
                                    position.ui.count = 0;
                                    position.ui.isLoop = false;
                                    position.ui.loopTime = 0;
                                    position.ui.allowDrop = true;
                                    position.ui.loading = false;
                                }else{
                                    if(targetSource.isLoop){
                                        var count = 0;
                                        for(var j=0; j<sources.length; j++){
                                            if(sources[j].serialNum == position.serialNum){
                                                count++;
                                            }
                                        }
                                        position.ui.content = '总计'+'('+count+')';
                                        position.ui.count = count;
                                        position.ui.isLoop = true;
                                        position.ui.loopTime = targetSource.loopTime;
                                        position.ui.allowDrop = true;
                                        position.ui.loading = false;
                                    }else{
                                        position.ui.content = targetSource.sourceName;
                                        position.ui.count = 1;
                                        position.ui.isLoop = false;
                                        position.ui.loopTime = 0;
                                        position.ui.allowDrop = true;
                                        position.ui.loading = false;
                                    }
                                }
                            }else{
                                position.ui.content = position.typeName;
                                position.ui.count = 0;
                                position.ui.isLoop = false;
                                position.ui.loopTime = 0;
                                position.ui.allowDrop = false;
                                position.ui.loading = false;
                            }
                        }
                    }
                    self.forward.current.layout = layout;
                },
                setPosition:function(position){
                    var self = this;
                    for(var i=0; i<self.forward.current.layout.positions.length; i++){
                        var p = self.forward.current.layout.positions[i];
                        if(p.id === position.id){
                            self.forward.current.layout.positions.splice(i, 1, position);
                            break;
                        }
                    }
                },
                allowDrop:function(e, position){
                    if(arguments.length === 3) return false;
                    if(position.ui.allowDrop){
                        e.preventDefault();
                    }
                },
                handleDrop:function(e, position){
                    var self = this;
                    position.ui.loading = true;
                    self.setPosition(position);
                    var nodeData = $.parseJSON(e.dataTransfer.getData('nodeData'));
                    console.log(nodeData);
                    ajax.post('/tetris/bvc/model/agenda/forward/add/custom/source', {
                        agendaForwardId:self.forward.currentId,
                        sources: $.toJSON([{id:nodeData.id, type:nodeData.type==='ROLE_CHANNEL'?'ROLE_CHANNEL':'GROUP_MEMBER_CHANNEL'}]),
                        serialNum:position.serialNum,
                        isLoop:position.ui.isLoop,
                        loopTime:position.ui.loopTime
                    }, function(data, status, message){
                        position.ui.loading = false;
                        self.setPosition(position);
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        if(data && data.length>0){
                            if(!self.forward.current.sources) self.forward.current.sources = [];
                            var source = data[0];
                            if(position.ui.isLoop){
                                position.ui.count += 1;
                                position.ui.content = '总计'+'('+position.ui.count+')';
                                self.forward.current.sources.push(source);
                            }else{
                                while(true){
                                    var remove = false;
                                    for(var i=0; i<self.forward.current.sources.length; i++){
                                        if(self.forward.current.sources[i].serialNum == source.serialNum){
                                            self.forward.current.sources.splice(i, 1);
                                            remove = true;
                                            break;
                                        }
                                    }
                                    if(!remove) break;
                                }
                                self.forward.current.sources.push(source);
                                position.ui.count += 1;
                                position.ui.content = source.sourceName;
                            }
                            self.setPosition(position);
                        }
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                removeSource:function(position, message){
                    var self = this;
                    if(position.ui.count === 0){
                        self.$message({
                            type:'info',
                            message:'当前分屏无配置'
                        });
                        return;
                    }
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'提示',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, [message])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/agenda/forward/remove/custom/source', {
                                    agendaForwardId:self.forward.currentId,
                                    serialNum:position.serialNum
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    while(true){
                                        var end = true;
                                        for(var i=0; i<self.forward.current.sources.length; i++){
                                            if(self.forward.current.sources[i].serialNum == position.serialNum){
                                                self.forward.current.sources.splice(i, 1);
                                                end = false;
                                                break;
                                            }
                                        }
                                        if(end) break;
                                    }
                                    position.ui.content = '未设置';
                                    position.ui.count = 0;
                                    position.ui.isLoop = false;
                                    position.ui.loopTime = 0;
                                    position.ui.allowDrop = true;
                                    position.ui.loading = false;
                                    self.setPosition(position);
                                }, null, ajax.TOTAL_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                switchToNext:function(position, message){
                    var self = this;
                    if(position.ui.count === 0){
                        self.$message({
                            type:'info',
                            message:'当前分屏无配置'
                        });
                        return;
                    }
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'提示',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, [message])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/agenda/forward/switch/polling/next', {
                                    agendaForwardId:self.forward.currentId,
                                    serialNum:position.serialNum
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if (status === 200) {
                                        self.$message({
                                            type:'success',
                                            message:'操作成功！'
                                        });
                                    } else {
                                        self.$message({
                                            type:'warning',
                                            message:'操作失败：' + status
                                        });
                                    }
                                }, null, ajax.TOTAL_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                loadForwardSettings:function(force){
                    var self = this;
                    if(self.forward.current && (!self.forward.current.layout||force===true)){
                        ajax.post('/tetris/bvc/model/agenda/forward/load/settings', {
                            id:self.forward.currentId
                        }, function(data){
                            self.forward.current.sources = data.sources || [];
                            self.forward.current.destinations = data.destinations || [];
                            self.forward.current.audios = data.audios || [];
                            if(data.layout){
                                if(self.layout.data.length > 0){
                                    for(var i=0; i<self.layout.data.length; i++){
                                        if(self.layout.data[i].id == data.layout.id){
                                            self.layout.current = self.layout.data[i];
                                            break;
                                        }
                                    }
                                }else{
                                    self.layout.current = data.layout;
                                }
                            }else{
                                self.layout.current = '';
                            }
                            self.setCurrentForwardLayout(data.layout);
                        });
                    }else{
                        if(self.layout.data.length > 0){
                            for(var i=0; i<self.layout.data.length; i++){
                                if(self.layout.data[i].id == self.forward.current.layout.id){
                                    self.layout.current = self.layout.data[i];
                                    break;
                                }
                            }
                        }else{
                            self.layout.current = self.forward.current.layout;
                        }
                    }
                },
                loadLayouts:function(){
                    var self = this;
                    self.layout.data.splice(0, self.layout.data.length);
                    self.layout.filter.splice(0, self.layout.filter.length);
                    self.layout.loading = true;
                    ajax.post('/tetris/bvc/model/layout/load/add/with/position', null, function(data, status, message){
                        self.layout.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.layout.data.push(data[i]);
                                self.layout.filter.push(data[i]);
                                if(self.layout.current && self.layout.current.id==data[i].id){
                                    self.layout.current = data[i];
                                }
                            }
                        }
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                loadMembers:function(){
                    var self = this;
                    self.members.data.splice(0, self.members.data.length);
                    ajax.post('/command/basic/query/members', {
                        id:self.groupId,
                        withChannel:true,
                        channelTypeList: $.toJSON(['VIDEO_ENCODE', 'AUDIO_ENCODE'])
                    }, function(data){
                        self.group = data;
                        if(data.members && data.members.length>0){
                            for(var i=0; i<data.members.length; i++){
                                self.members.data.push(data.members[i]);
                            }
                        }
                        self.$nextTick(function(){
                            self.$refs.memberTree.filter('__handle_init_');
                        });
                    });
                },
                getMemberName:function(scope){
                    var self = this;
                    if((scope.data.param && scope.data.param.indexOf('MEMBER_HALL')>=0) ||
                        (scope.data.param && scope.data.param.indexOf('MEMBER_USER')>=0)){
                        var param = $.parseJSON(scope.data.param);
                        return scope.data.name + ' - ' + param.roleName;
                    }else{
                        return scope.data.name;
                    }
                },
                loadRoles:function(){
                    var self = this;
                    self.roles.data.splice(0, self.roles.data.length);
                    ajax.post('/tetris/bvc/model/role/load/by/group/id/and/type/with/channel', {
                        businessId:self.groupId,
                        type:'ALL'
                    }, function(data){
                        var oneToOneFolder = {
                            id:-1,
                            name:'发言人列表',
                            type:'ONE_TO_ONE_FOLDER',
                            isLeaf:false,
                            children:[]
                        };
                        var oneToManyFolder = {
                            id:-2,
                            name:'观众列表',
                            type:'ONE_TO_MANY_FOLDER',
                            isLeaf:false,
                            children:[]
                        };
                        self.roles.data.push(oneToOneFolder);
                        self.roles.data.push(oneToManyFolder);
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                var role = data[i];
                                var channels = role.channels;
                                var roleNode = {
                                    id:role.id,
                                    name:role.name,
                                    type:'ROLE',
                                    internalRoleType:role.internalRoleType,
                                    internalRoleTypeName:role.internalRoleTypeName,
                                    roleUserMappingType:role.roleUserMappingType,
                                    roleUserMappingTypeName:role.roleUserMappingTypeName,
                                    isLeaf:false,
                                    children:[]
                                };
                                if(channels && channels.length>0){
                                    for(var j=0; j<channels.length; j++){
                                        var channel = channels[j];
                                        var channelNode = {
                                            id:channel.id,
                                            name:channel.name,
                                            type:'ROLE_CHANNEL',
                                            channelType:channel.type,
                                            channelTypeName:channel.typeName,
                                            isLeaf:true
                                        };
                                        roleNode.children.push(channelNode);
                                    }
                                }
                                if(role.roleUserMappingType === 'ONE_TO_ONE'){
                                    oneToOneFolder.children.push(roleNode);
                                }else{
                                    oneToManyFolder.children.push(roleNode);
                                }
                            }
                        }
                        self.$nextTick(function(){
                            self.$refs.roleTree.filter('__handle_init_');
                        });
                    });
                },
                filterNodeByName:function(val, data, node){
                    if(data.type==='CHANNEL' && data.param && data.param.indexOf('AUDIO_ENCODE')>=0){
                        return false;
                    }
                    if(data.channelType === 'AUDIO_ENCODE'){
                        return false;
                    }
                    if(val === '__handle_init_'){
                        /*if(data.type==='CHANNEL' || data.type==='ROLE_CHANNEL'){
                            console.log(data);
                            node.parent.expanded = false;
                        }*/
                        return true;
                    }
                    if(data.type==='CHANNEL' || data.type==='ROLE_CHANNEL'){
                        data = node.parent.data
                    }
                    if(data.name && data.name.indexOf(val)>=0) return true;
                    if(data.param){
                        var param = $.parseJSON(data.param);
                        if(param.roleName){
                            var name = data.name + ' - ' + param.roleName;
                            if(name.indexOf(val) >= 0) return true;
                        }
                    }
                    return false;
                },
                allowDrag:function(draggingNode){
                    if((draggingNode.data.type==='ROLE_CHANNEL' && draggingNode.data.channelType==='VIDEO_ENCODE') ||
                        (draggingNode.data.param && draggingNode.data.param.indexOf('VIDEO_ENCODE')>=0)){
                        return true;
                    }
                    return false;
                },
                handleDragStart:function(node, e){
                    e.dataTransfer.setData('nodeData', $.toJSON(node.data));
                },
                loadAgendas:function(){
                    var self = this;
                    self.agenda.rows.splice(0, self.agenda.rows.length);
                    ajax.post('/tetris/bvc/model/virtual/source/load/by/business/id', {
                        businessId:self.groupId
                        //businessInfoType:'COMBINE_VIDEO_VIRTUAL_SOURCE'
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                if(!data[i].customAudios) data[i].customAudios = [];
                                self.agenda.filter.push(data[i]);
                                self.agenda.rows.push(data[i]);
                            }
                            console.log(data);
                            self.$refs.agendaTable.setCurrentRow(self.agenda.rows[0]);
                        }
                    });
                },
                handleCreateAgenda:function(){
                    var self = this;
                    self.dialog.createAgenda.visible = true;
                },
                handleCreateAgendaClose:function(){
                    var self = this;
                    self.dialog.createAgenda.visible = false;
                    self.dialog.createAgenda.loading = false;
                    self.dialog.createAgenda.name = '';
                    self.dialog.createAgenda.remark = '';
                },
                handleCreateAgendaSubmit:function(){
                    var self = this;
                    if(!self.dialog.createAgenda.name){
                        self.$message({
                            type:'error',
                            message:'名称不能为空'
                        });
                        return;
                    }
                    self.dialog.createAgenda.loading = true;
                    ajax.post('/tetris/bvc/model/virtual/source/add', {
                        name:self.dialog.createAgenda.name,
                        remark:self.dialog.createAgenda.remark,
                        businessId:self.groupId,
                        businessInfoTypeName:'合屏虚拟源'
                    }, function(data, status, message){
                        self.dialog.createAgenda.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        if(data){
                            if(!data.customAudios) data.customAudios = [];
                            self.agenda.rows.push(data);
                            if(!self.agenda.filterName || (self.agenda.filterName && data.name.indexOf(self.agenda.filterName)>=0)){
                                self.agenda.filter.push(data)
                            }

                            //self.dialog.addForward.name = '新建转发（' + new Date().format('yyyy-MM-dd hh:dd:ss') + '）';
                            //self.handleAddForwardSubmit(data.id);
                        }
                        self.handleCreateAgendaClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleEditAgenda:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.editAgenda.id = row.id;
                    self.dialog.editAgenda.name = row.name;
                    self.dialog.editAgenda.remark = row.remark;
                    self.dialog.editAgenda.visible = true;
                },
                handleEditAgendaClose:function(){
                    var self = this;
                    self.dialog.editAgenda.id = '';
                    self.dialog.editAgenda.name = '';
                    self.dialog.editAgenda.remark = '';
                    self.dialog.editAgenda.visible = false;
                    self.dialog.editAgenda.loading = false;
                },
                handleEditAgendaSubmit:function(){
                    var self = this;
                    if(!self.dialog.editAgenda.name){
                        self.$message({
                            type:'error',
                            message:'名称不能为空'
                        });
                        return;
                    }
                    self.dialog.editAgenda.loading = true;
                    ajax.post('/tetris/bvc/model/virtual/source/edit', {
                        id:self.dialog.editAgenda.id,
                        name:self.dialog.editAgenda.name,
                        remark:self.dialog.editAgenda.remark,
                        businessInfoTypeName:'合屏虚拟源'
                    }, function(data, status, message){
                        self.dialog.createAgenda.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        if(data){
                            for(var i=0; i<self.agenda.rows.length; i++){
                                if(self.agenda.rows[i].id == data.id){
                                    self.agenda.rows[i].name = data.name;
                                    self.agenda.rows[i].remark = data.remark;
                                    break;
                                }
                            }
                            for(var i=0; i<self.agenda.filter.length; i++){
                                if(self.agenda.filter[i].id == data.id){
                                    if(!self.agenda.filterName || data.name.indexOf(self.agenda.filterName)>=0){
                                        self.agenda.filter[i].name = data.name;
                                        self.agenda.filter[i].remark = data.remark;
                                    }else{
                                        self.agenda.filter.splice(i, 1);
                                    }
                                    break;
                                }
                            }
                        }
                        self.handleEditAgendaClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleAgendaAudioSettings:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.agendaAudioSettings.row = row;
                    self.dialog.agendaAudioSettings.visible = true;
                },
                handleAgendaAudioPriorityChange:function(val){
                    var self = this;
                    ajax.post('/tetris/bvc/model/agenda/audio/priority/change', {
                        id:self.dialog.agendaAudioSettings.row.id,
                        audioPriority:val
                    }, function(data){
                        if(data){
                            self.dialog.agendaAudioSettings.row.audioPriorityName = data.audioPriorityName;
                        }
                    });
                },
                handleAgendaAudioSettingsClose:function(){
                    var self = this;
                    self.dialog.agendaAudioSettings.row = '';
                    self.dialog.agendaAudioSettings.visible = false;
                    self.dialog.agendaAudioSettings.loading = false;
                },
                handleAgendaCustomAudioDelete:function(customAudio){
                    var self = this;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'提示',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['是否移除该音频通道?'])
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
                                    if(self.dialog.agendaAudioSettings.row.customAudios && self.dialog.agendaAudioSettings.row.customAudios.length>0){
                                        for(var i=0; i<self.dialog.agendaAudioSettings.row.customAudios.length; i++){
                                            if(self.dialog.agendaAudioSettings.row.customAudios[i].id === customAudio.id){
                                                self.dialog.agendaAudioSettings.row.customAudios.splice(i, 1);
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
                handleRemoveAgenda:function(scope){
                    var self = this;
                    var row = scope.row;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'提示',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['是否移除该议程?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/virtual/source/delete', {
                                    id:row.id
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    for(var i=0; i<self.agenda.rows.length; i++){
                                        if(self.agenda.rows[i].id == row.id){
                                            self.agenda.rows.splice(i, 1);
                                            break;
                                        }
                                    }
                                    for(var i=0; i<self.agenda.filter.length; i++){
                                        if(self.agenda.filter[i].id == row.id){
                                            self.agenda.filter.splice(i, 1);
                                            break;
                                        }
                                    }
                                    self.forward.rows.splice(0, self.forward.rows.length);
                                    self.forward.currentId = '';
                                    self.forward.current = '';
                                    self.layout.visible = false;
                                    self.layout.loading = false;
                                    self.layout.data.splice(0, self.layout.data.length);
                                    self.layout.filter.splice(0, self.layout.filter.length);
                                    self.layout.current = '';
                                }, null, ajax.TOTAL_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                globalCustomAudioChange:function(val){
                    var self = this;
                    ajax.post('/tetris/bvc/model/agenda/global/custom/audio/change', {
                        id:self.dialog.agendaAudioSettings.row.id,
                        globalCustomAudio:val
                    }, function(){
                        if(val == true){
                            for(var i=0; i<self.forward.rows.length; i++){
                                self.forward.rows[i].audioType = null;
                                self.forward.rows[i].audioTypeName = null;
                                self.forward.rows[i].audios.splice(0, self.forward.rows[i].audios.length);
                            }
                        }else{
                            for(var i=0; i<self.agenda.rows.length; i++){
                                if(self.agenda.rows[i].id == self.dialog.agendaAudioSettings.row.id){
                                    self.agenda.rows[i].customAudios.splice(0, self.agenda.rows[i].customAudios.length);
                                    break;
                                }
                            }
                        }
                    });
                },
                handleAgendaVolumeChange:function(val){
                    var self = this;
                    ajax.post('/tetris/bvc/model/agenda/volume/change', {
                        id:self.dialog.agendaAudioSettings.row.id,
                        volume:val
                    });
                },
                addSpeakerRole:function(scope){
                    var self = this;
                    self.dialog.addSpeakerRole.scope = scope;
                    self.dialog.addSpeakerRole.visible = true;
                },
                handleAddSpeakerRoleClose:function(){
                    var self = this;
                    self.dialog.addSpeakerRole.visible = false;
                    self.dialog.addSpeakerRole.loading = false;
                    self.dialog.addSpeakerRole.scope = '';
                    self.dialog.addSpeakerRole.name = '';
                },
                handleAddSpeakerRoleSubmit:function(){
                    var self = this;
                    if(!self.dialog.addSpeakerRole.name){
                        self.$message({
                            type:'error',
                            message:'名称不能为空'
                        });
                        return;
                    }
                    self.dialog.addSpeakerRole.loading = true;
                    ajax.post('/tetris/bvc/model/role/add/custom', {
                        name:self.dialog.addSpeakerRole.name,
                        businessId:self.groupId,
                        roleUserMappingType:'ONE_TO_ONE'
                    }, function(data, status, message){
                        self.dialog.addSpeakerRole.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        var roleNode = {
                            id:data.id,
                            name:data.name,
                            type:'ROLE',
                            internalRoleType:data.internalRoleType,
                            internalRoleTypeName:data.internalRoleTypeName,
                            roleUserMappingType:data.roleUserMappingType,
                            roleUserMappingTypeName:data.roleUserMappingTypeName,
                            isLeaf:false,
                            children:[]
                        };
                        var channels = data.channels;
                        if(channels && channels.length>0){
                            for(var j=0; j<channels.length; j++){
                                var channel = channels[j];
                                var channelNode = {
                                    id:channel.id,
                                    name:channel.name,
                                    type:'ROLE_CHANNEL',
                                    channelType:channel.type,
                                    channelTypeName:channel.typeName,
                                    isLeaf:true
                                };
                                roleNode.children.push(channelNode);
                            }
                        }
                        self.$refs.roleTree.append(roleNode, self.dialog.addSpeakerRole.scope.node);
                        self.$nextTick(function(){
                            self.$refs.roleTree.filter('__handle_init_');
                        });
                        self.handleAddSpeakerRoleClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                addAudienceRole:function(scope){
                    var self = this;
                    self.dialog.addAudienceRole.scope = scope;
                    self.dialog.addAudienceRole.visible = true;
                },
                handleAddAudienceRoleClose:function(){
                    var self = this;
                    self.dialog.addAudienceRole.scope = '';
                    self.dialog.addAudienceRole.visible = false;
                    self.dialog.addAudienceRole.loading = false;
                    self.dialog.addAudienceRole.name = '';
                },
                handleAddAudienceRoleSubmit:function(){
                    var self = this;
                    if(!self.dialog.addAudienceRole.name){
                        self.$message({
                            type:'error',
                            message:'名称不能为空'
                        });
                        return;
                    }
                    self.dialog.addAudienceRole.loading = true;
                    ajax.post('/tetris/bvc/model/role/add/custom', {
                        name:self.dialog.addAudienceRole.name,
                        businessId:self.groupId,
                        roleUserMappingType:'ONE_TO_MANY'
                    }, function(data, status, message){
                        self.dialog.addAudienceRole.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        var roleNode = {
                            id:data.id,
                            name:data.name,
                            type:'ROLE',
                            internalRoleType:data.internalRoleType,
                            internalRoleTypeName:data.internalRoleTypeName,
                            roleUserMappingType:data.roleUserMappingType,
                            roleUserMappingTypeName:data.roleUserMappingTypeName,
                            isLeaf:false,
                            children:[]
                        };
                        self.$refs.roleTree.append(roleNode, self.dialog.addAudienceRole.scope.node);
                        self.handleAddAudienceRoleClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                removeRole:function(scope){
                    var self = this;
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
                                ajax.post('/tetris/bvc/model/role/delete', {
                                    id:scope.data.id
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    self.$refs.roleTree.remove(scope.node);
                                }, null, ajax.TOTAL_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                currentAgendaChange:function(currentRow, oldRow){
                    var self = this;
                    if(!currentRow) return;
                    self.agenda.current = currentRow;
                    self.forward.rows.splice(0, self.forward.rows.length);
                    ajax.post('/tetris/bvc/model/agenda/forward/load/custom', {
                        agendaId:currentRow.id
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                data[i].id = data[i].id + '';
                                self.forward.rows.push(data[i]);
                            }
                            self.forward.currentId = self.forward.rows[0].id;
                        }
                    });
                },
                positionStyle:function(position, width, height){
                    var style = '';
                    style += 'left:' + parseInt(position.x)*width/10000 + 'px;';
                    style += 'top:' + parseInt(position.y)*height/10000 + 'px;';
                    style += 'width:' + ((parseInt(position.width)*width/10000)+1) + 'px;';
                    style += 'height:' + ((parseInt(position.height)*height/10000)+1) + 'px;';
                    //style += 'line-height:' + parseInt(position.height)*width/10000 + 'px;';
                    style += 'z-index:' + parseInt(position.zIndex);
                    return style;
                },
                forwardLayoutPosition:function(position){
                    var self = this;
                    var $container = $('#forward-layout-position-container');
                    return self.positionStyle(position, $container[0].clientWidth-2, $container[0].clientHeight-1);
                },
                handleSelectLayout:function(layout, forwardId, apiMode){
                    var self = this;
                    if(apiMode){
                        ajax.post('/tetris/bvc/model/agenda/forward/set/layout', {
                            id:forwardId,
                            layoutId:layout.id
                        });
                    }else{
                        var h = self.$createElement;
                        self.$msgbox({
                            title:'提示',
                            message:h('div', null, [
                                h('div', {class:'el-message-box__status el-icon-warning'}, null),
                                h('div', {class:'el-message-box__message'}, [
                                    h('p', null, ['是否切换虚拟源?'])
                                ])
                            ]),
                            type:'wraning',
                            showCancelButton: true,
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            beforeClose:function(action, instance, done){
                                instance.confirmButtonLoading = true;
                                if(action === 'confirm'){
                                    ajax.post('/tetris/bvc/model/agenda/forward/set/layout', {
                                        id:self.forward.currentId,
                                        layoutId:layout.id
                                    }, function(data, status){
                                        instance.confirmButtonLoading = false;
                                        done();
                                        if(status !== 200) return;
                                        if(data){
                                            self.layout.current = layout;
                                            self.setCurrentForwardLayout(data);
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
                handleAddForward:function(){
                    var self = this;
                    self.dialog.addForward.visible = true;
                },
                handleAddForwardClose:function(){
                    var self = this;
                    self.dialog.addForward.visible = false;
                    self.dialog.addForward.loading = false;
                    self.dialog.addForward.name = '';
                },
                handleAddForwardSubmit_bak:function(agendaId){
                    var self = this;
                    if(!self.dialog.addForward.name){
                        self.$message({
                            type:'error',
                            message:'名称不能为空!'
                        });
                        return;
                    }
                    self.dialog.addForward.loading = true;
                    ajax.post('/tetris/bvc/model/agenda/forward/add/custom', {
                        name:self.dialog.addForward.name,
                        agendaId:parseInt(agendaId)?agendaId:self.agenda.current.id
                    }, function(data, status, message){
                        self.dialog.addForward.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        if(!parseInt(agendaId)){
                            self.forward.rows.push(data);
                        }
                        if(self.layout.data && self.layout.data.length>0){
                            self.handleSelectLayout(self.layout.data[0], data.id, true);
                        }
                        self.handleAddForwardClose();
                    }, null, ajax.TOTAL_CATCH_CODE)
                },
                handleEditForward:function(){
                    var self = this;
                    if(!self.forward.current){
                        self.$message({
                            type:'error',
                            message:'请选择要修改的转发'
                        });
                        return;
                    }
                    self.dialog.editForward.id = self.forward.current.id;
                    self.dialog.editForward.name = self.forward.current.name;
                    self.dialog.editForward.visible = true;
                },
                handleEditForwardClose:function(){
                    var self = this;
                    self.dialog.editForward.id = '';
                    self.dialog.editForward.name = '';
                    self.dialog.editForward.visible = false;
                    self.dialog.editForward.loading = false;
                },
                handleEditForwardSubmit:function(){
                    var self = this;
                    self.dialog.editForward.loading = true;
                    ajax.post('/tetris/bvc/model/agenda/forward/edit/custom', {
                        id:self.dialog.editForward.id,
                        name:self.dialog.editForward.name
                    }, function(data, status, message){
                        self.dialog.editForward.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        self.forward.current.name = self.dialog.editForward.name;
                        self.handleEditForwardClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleForwardRemove:function(targetId){
                    var self = this;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'提示',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['是否移除该转发?'])
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
                                    id:targetId
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    for(var i=0; i<self.forward.rows.length; i++){
                                        if(self.forward.rows[i].id == targetId){
                                            self.forward.rows.splice(i, 1);
                                            break;
                                        }
                                    }
                                    if(self.forward.currentId == targetId){
                                        self.forward.currentId = '0';
                                        self.forward.current = '';
                                    }
                                }, null, ajax.TOTAL_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                handleAudioTypeChange:function(val){
                    var self = this;
                    ajax.post('/tetris/bvc/model/agenda/forward/handle/audio/type/change', {
                        id:self.forward.current.id,
                        audioType:val
                    }, function(){
                        if(val!=='CUSTOM' && self.forward.current.audios.length > 0) {
                            self.forward.current.audios.splice(0, self.forward.current.audios.length);
                        }
                    });
                },
                handleAddCustomAudio:function(type){
                    var self = this;
                    self.dialog.addCustomAudio.type = type;
                    self.dialog.addCustomAudio.members.splice(0, self.dialog.addCustomAudio.members.length);
                    self.dialog.addCustomAudio.roles.splice(0, self.dialog.addCustomAudio.roles.length);
                    for(var i=0; i<self.members.data.length; i++){
                        self.dialog.addCustomAudio.members.push(self.members.data[i]);
                    }
                    for(var i=0; i<self.roles.data.length; i++){
                        if(self.roles.data[i].id === -2) continue;
                        self.dialog.addCustomAudio.roles.push(self.roles.data[i]);
                    }
                    self.dialog.addCustomAudio.visible = true;
                    self.$nextTick(function(){
                        self.$refs.addCustomAudioMemberTree.filter('AUDIO_ENCODE');
                        self.$refs.addCustomAudioRoleTree.filter('AUDIO_ENCODE');
                    });
                },
                handleAddCustomAudioClose:function(){
                    var self = this;
                    self.$refs.addCustomAudioMemberTree.setCurrentNode([]);
                    self.$refs.addCustomAudioRoleTree.setCurrentNode([]);
                    self.dialog.addCustomAudio.members.splice(0, self.dialog.addCustomAudio.members.length);
                    self.dialog.addCustomAudio.roles.splice(0, self.dialog.addCustomAudio.roles.length);
                    self.dialog.addCustomAudio.visible = false;
                    self.dialog.addCustomAudio.loading = false;
                },
                handleAddCustomAudioSubmit:function(){
                    var self = this;
                    var checkedMemberNodeDatas = self.$refs.addCustomAudioMemberTree.getCheckedNodes();
                    var checkedRoleNodeDatas = self.$refs.addCustomAudioRoleTree.getCheckedNodes();
                    var roleChannelIds = [];
                    var groupMemberChannelIds = [];
                    if(checkedMemberNodeDatas && checkedMemberNodeDatas.length>0){
                        for(var i=0; i<checkedMemberNodeDatas.length; i++){
                            var nodeData = checkedMemberNodeDatas[i];
                            if(nodeData.param && nodeData.param.indexOf('AUDIO_ENCODE')>=0){
                                if(nodeData.visible) groupMemberChannelIds.push(nodeData.id);
                            }
                        }
                    }
                    if(checkedRoleNodeDatas && checkedRoleNodeDatas.length>0){
                        for(var i=0; i<checkedRoleNodeDatas.length; i++){
                            var nodeData = checkedRoleNodeDatas[i];
                            if(nodeData.channelType && nodeData.channelType==='AUDIO_ENCODE'){
                                if(nodeData.visible) roleChannelIds.push(nodeData.id);
                            }
                        }
                    }
                    if(roleChannelIds.length<=0 && groupMemberChannelIds.length<=0){
                        self.$message({
                            type:'error',
                            message:'没有可添加的音频通道'
                        });
                        return;
                    }
                    self.dialog.addCustomAudio.loading = true;
                    var url = null;
                    if(self.dialog.addCustomAudio.type === 'FORWARD'){
                        url = '/tetris/bvc/model/agenda/forward/add/custom/audio';
                    }else{
                        url = '/tetris/bvc/model/custom/audio/add/agenda/audio'
                    }
                    var permissionId = null;
                    if(self.dialog.addCustomAudio.type === 'FORWARD'){
                        permissionId = self.forward.currentId;
                    }else{
                        permissionId = self.dialog.agendaAudioSettings.row.id;
                    }
                    ajax.post(url, {
                        agendaForwardId:permissionId,
                        agendaId:permissionId,
                        roleChannelIds: $.toJSON(roleChannelIds),
                        groupMemberChannelIds: $.toJSON(groupMemberChannelIds)
                    }, function(data, status, message){
                        self.dialog.addCustomAudio.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                if(self.dialog.addCustomAudio.type === 'FORWARD') {
                                    self.forward.current.audios.push(data[i]);
                                }else{
                                    self.dialog.agendaAudioSettings.row.customAudios.push(data[i]);
                                }
                            }
                        }
                        self.handleAddCustomAudioClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleForwardVolumeChange:function(val){
                    var self = this;
                    ajax.post('/tetris/bvc/model/agenda/forward/handle/volume/change', {
                        id:self.forward.currentId,
                        volume:val
                    });
                },
                handleForwardCustomAudioDelete:function(customAudio){
                    var self = this;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'提示',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['是否移除该音频通道?'])
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
                                    if(self.forward.current.audios && self.forward.current.audios.length>0){
                                        for(var i=0; i<self.forward.current.audios.length; i++){
                                            if(self.forward.current.audios[i].id === customAudio.id){
                                                self.forward.current.audios.splice(i, 1);
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
                filterNode:function(val, data){
                    var self = this;
                    var name = null;
                    if(typeof val === 'object'){
                        name = val[1];
                        val = val[0];
                    }
                    if(val === 'AUDIO_ENCODE'){
                        if((data.channelType && data.channelType.indexOf('VIDEO')>=0) ||
                            (data.param && data.param.indexOf('VIDEO_')>=0) ||
                            (data.channelType && data.channelType === 'AUDIO_DECODE') ||
                            (data.param && data.param.indexOf('AUDIO_DECODE')>=0)){
                            data.visible = false;
                            return false;
                        }else{
                            var existAudios = null;
                            if(self.dialog.addCustomAudio.type === 'FORWARD'){
                                existAudios = self.forward.current.audios;
                            }else{
                                existAudios = self.dialog.agendaAudioSettings.row.customAudios;
                            }
                            if(data.channelType && data.channelType==='AUDIO_ENCODE'){
                                for(var i=0; i<existAudios.length; i++){
                                    if(existAudios[i].sourceType==='ROLE_CHANNEL' && existAudios[i].sourceId==data.id){
                                        data.visible = false;
                                        return false;
                                    }
                                }
                            }else if(data.param && data.param.indexOf('AUDIO_ENCODE')>=0){
                                for(var i=0; i<existAudios.length; i++){
                                    if(existAudios[i].sourceType==='GROUP_MEMBER_CHANNEL' && existAudios[i].sourceId==data.id){
                                        data.visible = false;
                                        return false;
                                    }
                                }
                            }
                            data.visible = true;
                            return !name?true:(data.name.indexOf(name)>=0?true:false);
                        }
                    }else if(val === 'ROLE_AND_MEMBER'){
                        if(data.type==='CHANNEL' || data.type==='ROLE_CHANNEL'){
                            data.visible = false;
                            return false;
                        }else{
                            if(data.type==='MEMBE'){
                                for(var i=0; i<self.forward.current.destinations.length; i++){
                                    if(self.forward.current.destinations[i].destinationType==='GROUP_MEMBER' && self.forward.current.destinations[i].destinationId==data.id){
                                        data.visible = false;
                                        return false;
                                    }
                                }
                            }else if(data.type === 'ROLE'){
                                for(var i=0; i<self.forward.current.destinations.length; i++){
                                    if(self.forward.current.destinations[i].destinationType==='ROLE' && self.forward.current.destinations[i].destinationId==data.id){
                                        data.visible = false;
                                        return false;
                                    }
                                }
                            }else{
                                data.visible = true;
                                return !name?true:(data.name.indexOf(name)>=0?true:false);
                            }
                            data.visible = true;
                            return !name?true:(data.name.indexOf(name)>=0?true:false);
                        }
                    }else if(val === 'VIDEO_ENCODE'){
                        if((data.channelType && data.channelType.indexOf('AUDIO_')>=0) ||
                            (data.param && data.param.indexOf('AUDIO_')>=0) ||
                            (data.channelType && data.channelType==='VIDEO_DECODE') ||
                            (data.param && data.param.indexOf('VIDEO_DECODE')>=0)){
                            data.visible = false;
                            return false;
                        }else{
                            for(var i=0; i<self.dialog.setLoop.selected.length; i++){
                                if(self.dialog.setLoop.selected[i].node === data){
                                    data.visible = false;
                                    return false;
                                }
                            }
                            data.visible = true;
                            return !name?true:(data.name.indexOf(name)>=0?true:false);
                        }
                    }else if(val === 'EDIT_ROLE'){
                        if(data.type==='CHANNEL'){
                            data.visible = false;
                            return false;
                        }else{
                            if(data.type === 'MEMBE'){
                                for(var i=0; i<self.dialog.editRole.roleMembers.length; i++){
                                    if(data.id == self.dialog.editRole.roleMembers[i].id){
                                        data.visible = false;
                                        return false;
                                    }
                                }
                            }
                            data.visible = true;
                            return !name?true:(data.name.indexOf(name)>=0?true:false);
                        }
                    }
                },
                handleAddDestination:function(){
                    var self = this;
                    self.dialog.addDestination.members.splice(0, self.dialog.addDestination.members.length);
                    self.dialog.addDestination.roles.splice(0, self.dialog.addDestination.roles.length);
                    for(var i=0; i<self.members.data.length; i++){
                        self.dialog.addDestination.members.push(self.members.data[i]);
                    }
                    for(var i=0; i<self.roles.data.length; i++){
                        self.dialog.addDestination.roles.push(self.roles.data[i]);
                    }
                    self.dialog.addDestination.visible = true;
                    self.$nextTick(function(){
                        self.$refs.addDestinationMemberTree.filter('ROLE_AND_MEMBER');
                        self.$refs.addDestinationRoleTree.filter('ROLE_AND_MEMBER');
                    });
                },
                handleAddDestinationClose:function(){
                    var self = this;
                    self.$refs.addDestinationMemberTree.setCurrentNode([]);
                    self.$refs.addDestinationRoleTree.setCurrentNode([]);
                    self.dialog.addDestination.members.splice(0, self.dialog.addDestination.members.length);
                    self.dialog.addDestination.roles.splice(0, self.dialog.addDestination.roles.length);
                    self.dialog.addDestination.visible = false;
                    self.dialog.addDestination.loading = false;
                },
                handleAddDestinationSubmit:function(){
                    var self = this;
                    var checkedMemberNodeDatas = self.$refs.addDestinationMemberTree.getCheckedNodes();
                    var checkedRoleNodeDatas = self.$refs.addDestinationRoleTree.getCheckedNodes();
                    var roleIds = [];
                    var groupMemberIds = [];
                    if(checkedMemberNodeDatas && checkedMemberNodeDatas.length>0){
                        for(var i=0; i<checkedMemberNodeDatas.length; i++){
                            var nodeData = checkedMemberNodeDatas[i];
                            if(nodeData.type === 'MEMBE'){
                                if(nodeData.visible) groupMemberIds.push(nodeData.id);
                            }
                        }
                    }
                    if(checkedRoleNodeDatas && checkedRoleNodeDatas.length>0){
                        for(var i=0; i<checkedRoleNodeDatas.length; i++){
                            var nodeData = checkedRoleNodeDatas[i];
                            if(nodeData.type === 'ROLE'){
                                if(nodeData.visible) roleIds.push(nodeData.id);
                            }
                        }
                    }
                    if(roleIds.length<=0 && groupMemberIds.length<=0){
                        self.$message({
                            type:'error',
                            message:'没有可添加的成员或角色'
                        });
                        return;
                    }
                    self.dialog.addDestination.loading = true;
                    ajax.post('/tetris/bvc/model/agenda/forward/add/destination', {
                        agendaForwardId:self.forward.currentId,
                        roleIds: $.toJSON(roleIds),
                        groupMemberIds: $.toJSON(groupMemberIds),
                    }, function(data, status, message){
                        self.dialog.addDestination.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                for(var j=0; j<self.forward.rows.length; j++){
                                    var finded = false;
                                    for(var k=0; k<self.forward.rows[j].destinations.length; k++){
                                        if(self.forward.rows[j].destinations[k].id == data[i].id){
                                            self.forward.rows[j].destinations.splice(k, 1);
                                            finded = true;
                                            break;
                                        }
                                    }
                                    if(finded) break;
                                }
                            }
                            for(var i=0; i<data.length; i++){
                                self.forward.current.destinations.push(data[i]);
                            }
                        }
                        self.handleAddDestinationClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleDestinationsDelete:function(destination){
                    var self = this;
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
                                    if(self.forward.current.destinations && self.forward.current.destinations.length>0){
                                        for(var i=0; i<self.forward.current.destinations.length; i++){
                                            if(self.forward.current.destinations[i].id === destination.id){
                                                self.forward.current.destinations.splice(i, 1);
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
                findTreeNode:function(id, type, nodes){
                    var self = this;
                    if(!nodes || nodes.length<=0) return;
                    for(var i=0; i<nodes.length; i++){
                        if(nodes[i].id==id && nodes[i].type==type){
                            return {self:nodes[i]};
                        }else{
                            var t = self.findTreeNode(id, type, nodes[i].children);
                            if(t) {
                                if(!t.parent) t.parent = nodes[i];
                                return t;
                            }
                        }
                    }
                },
                handleSetLoop:function(position){
                    var self = this;
                    self.dialog.setLoop.position = position;
                    self.dialog.setLoop.members.data.splice(0, self.dialog.setLoop.members.data.length);
                    for(var i=0; i<self.members.data.length; i++){
                        self.dialog.setLoop.members.data.push(self.members.data[i]);
                    }
                    for(var i=0; i<self.roles.data.length; i++){
                        if(self.roles.data[i].id === -2) continue;
                        self.dialog.setLoop.members.data.push(self.roles.data[i]);
                    }
                    if(self.forward.current.sources && self.forward.current.sources.length>0){
                        for(var i=0; i<self.forward.current.sources.length; i++){
                            var source = self.forward.current.sources[i];
                            if(source.serialNum != position.serialNum) continue;
                            if(source.sourceType === 'ROLE_CHANNEL'){
                                var nodeInfo = self.findTreeNode(source.sourceId, 'ROLE_CHANNEL', self.roles.data);
                                if(nodeInfo) self.dialog.setLoop.selected.push({checked:false, node:nodeInfo.self, parent:nodeInfo.parent});
                            }else if(source.sourceType === 'GROUP_MEMBER_CHANNEL'){
                                var nodeInfo = self.findTreeNode(source.sourceId, 'CHANNEL', self.members.data);
                                if(nodeInfo) self.dialog.setLoop.selected.push({checked:false, node:nodeInfo.self, parent:nodeInfo.parent});
                            }
                        }
                    }
                    if(position.ui.isLoop && position.ui.loopTime) self.dialog.setLoop.loopTime = position.ui.loopTime;
                    self.dialog.setLoop.visible = true;
                    self.$nextTick(function(){
                        self.$refs.setLoopTree.filter('VIDEO_ENCODE');
                    });
                },
                handleSetLoopClose:function(){
                    var self = this;
                    self.dialog.setLoop.position = '';
                    self.dialog.setLoop.loopTime = 10;
                    self.dialog.setLoop.selected.splice(0, self.dialog.setLoop.selected.length);
                    self.$refs.setLoopTree.setCurrentNode([]);
                    self.dialog.setLoop.members.data.splice(0, self.dialog.setLoop.members.data.length);
                    self.dialog.setLoop.visible = false;
                    self.dialog.setLoop.loading = false;
                },
                handleSetLoopSelect:function(){
                    var self = this;
                    var checkedNodeDatas = self.$refs.setLoopTree.getCheckedNodes();
                    if(checkedNodeDatas && checkedNodeDatas.length>0){
                        for(var i=0; i<checkedNodeDatas.length; i++){
                            if((checkedNodeDatas[i].type==='ROLE_CHANNEL' && checkedNodeDatas[i].channelType==='VIDEO_ENCODE')||
                                (checkedNodeDatas[i].type==='CHANNEL'  && checkedNodeDatas[i].param.indexOf('VIDEO_ENCODE')>=0)){
                                if(checkedNodeDatas[i].visible){
                                    var node = self.$refs.setLoopTree.getNode(checkedNodeDatas[i]);
                                    var parentNode = node.parent;
                                    self.dialog.setLoop.selected.push({checked:false, node:checkedNodeDatas[i], parent:parentNode.data});
                                }
                            }
                        }
                        self.$refs.setLoopTree.filter('VIDEO_ENCODE');
                    }
                },
                handleSetLoopUnSelect:function(){
                    var self = this;
                    while(true){
                        var end = false;
                        for(var i=0; i<self.dialog.setLoop.selected.length; i++){
                            if(self.dialog.setLoop.selected[i].checked){
                                self.dialog.setLoop.selected.splice(i, 1);
                                end = true;
                                break;
                            }
                        }
                        if(!end) break;
                    }
                    self.$refs.setLoopTree.filter('VIDEO_ENCODE');
                },
                handleSetLoopSubmit:function(){
                    var self = this;
                    if(self.dialog.setLoop.selected.length <= 0){
                        self.$message({
                            type:'error',
                            message:'请设置轮询源'
                        });
                        return;
                    }
                    self.dialog.setLoop.loading = true;
                    var sources = [];
                    for(var i=0; i<self.dialog.setLoop.selected.length; i++){
                        sources.push({
                            id:self.dialog.setLoop.selected[i].node.id,
                            type:self.dialog.setLoop.selected[i].node.type==='ROLE_CHANNEL'?'ROLE_CHANNEL':'GROUP_MEMBER_CHANNEL'
                        });
                    }
                    ajax.post('/tetris/bvc/model/agenda/forward/add/custom/source', {
                        agendaForwardId:self.forward.currentId,
                        sources:$.toJSON(sources),
                        serialNum:self.dialog.setLoop.position.serialNum,
                        isLoop:true,
                        loopTime:self.dialog.setLoop.loopTime
                    }, function(data, status, message){
                        self.dialog.setLoop.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        while(true){
                            var end = true;
                            for(var i=0; i<self.forward.current.sources.length; i++){
                                if(self.forward.current.sources[i].serialNum == self.dialog.setLoop.position.serialNum){
                                    self.forward.current.sources.splice(i, 1);
                                    end = false;
                                    break;
                                }
                            }
                            if(end) break;
                        }
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.forward.current.sources.push(data[i]);
                            }
                            self.dialog.setLoop.position.ui.content = '总计'+'('+data.length+')';
                            self.dialog.setLoop.position.ui.count = data.length;
                            self.dialog.setLoop.position.ui.isLoop = true;
                            self.dialog.setLoop.position.ui.loopTime = self.dialog.setLoop.loopTime;
                            self.dialog.setLoop.position.ui.allowDrop = false;
                            self.dialog.setLoop.position.ui.loading = false;
                        }
                        self.handleSetLoopClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                pollingSwitchToIndex:function(item, i){
                    var self = this;
                    //var serialNum = $td['layout-auto']('getSerialNum');
                    ajax.post('/tetris/bvc/model/agenda/forward/switch/polling/index', {
                            agendaForwardId:self.forward.currentId,
                            source:$.toJSON(item.node.param),
                            sourceId:item.node.id,sourceType:item.node.type==='ROLE_CHANNEL'?'ROLE_CHANNEL':'GROUP_MEMBER_CHANNEL',
                            serialNum:self.dialog.setLoop.position.serialNum,
                            index:i
                        }, function(data, status) {
                            if (status === 200) {
                                self.$message({
                                    type:'success',
                                    message:'操作成功！'
                                });
                            } else {
                                self.$message({
                                    type:'warning',
                                    message:'操作失败：' + status
                                });
                            }
                        }
                    );
                },
                loadMemberByRole:function(){
                    var self = this;
                    console.log(self.dialog.editRole.roles);
                    self.dialog.editRole.roleMembers.splice(0, self.dialog.editRole.roleMembers.length);
                    ajax.post('/group/member/role/query/member/by/roleId', {
                        groupId:self.groupId,
                        roleId:self.dialog.editRole.roles.currentId
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.dialog.editRole.roleMembers.push(data[i]);
                            }
                        }
                        self.$refs.editRoleTree.filter('EDIT_ROLE');
                    });
                },
                handleEditRole:function(){
                    var self = this;
                    self.dialog.editRole.members.data.splice(0, self.dialog.editRole.members.data.length);
                    for(var i=0; i<self.members.data.length; i++){
                        self.dialog.editRole.members.data.push(self.members.data[i]);
                    }
                    for(var i=0; i<self.roles.data.length; i++){
                        for(var j=0; j<self.roles.data[i].children.length; j++){
                            self.dialog.editRole.roles.data.push(self.roles.data[i].children[j]);
                        }
                    }
                    self.dialog.editRole.visible = true;
                    self.$nextTick(function(){
                        self.$refs.editRoleTree.filter('EDIT_ROLE');
                    });
                },
                handleEditRoleSubmit:function(member){
                    var self = this;
                    if(!self.dialog.editRole.roles.currentId || self.dialog.editRole.roles.currentId==0){
                        self.$message({
                            type:'error',
                            message:'请选择角色'
                        });
                        return;
                    }
                    ajax.post('/group/member/role/exchange/member/role', {
                        groupId:self.groupId,
                        memberIds: $.toJSON([member.id]),
                        roleId:self.dialog.editRole.roles.currentId
                    }, function(data){
                        if(data && data.self && data.self.length>0){
                            for(var i=0; i<data.self.length; i++){
                                self.dialog.editRole.roleMembers.push(data.self[i]);
                            }
                        }
                        if(data && data.other && data.other.length>0){
                            for(var i=0; i<data.other.length; i++){
                                var other = data.other[i];
                                var nodes = self.findTreeNode(other.id, 'MEMBE', self.members.data);
                                var changedMemberNode = nodes.self;
                                var param = $.parseJSON(changedMemberNode.param);
                                param.roleId = other.roleId;
                                param.roleName = other.roleName;
                                changedMemberNode.param = $.toJSON(param);
                            }
                        }
                        self.$refs.editRoleTree.filter('EDIT_ROLE');
                    });
                },
                handleEditRoleRemove:function(member){
                    var self = this;
                    ajax.post('/group/member/role/exchange/member/role/to/audience', {
                        groupId:self.groupId,
                        memberIds: $.toJSON([member.id])
                    }, function(data){
                        var currentRole = null;
                        for(var i=0; i<self.dialog.editRole.roles.data.length; i++){
                            if(self.dialog.editRole.roles.data[i].id == self.dialog.editRole.roles.currentId){
                                currentRole = self.dialog.editRole.roles.data[i];
                                break;
                            }
                        }
                        if(currentRole.internalRoleType != 'MEETING_AUDIENCE'){
                            for(var i=0; i<self.dialog.editRole.roleMembers.length; i++){
                                if(self.dialog.editRole.roleMembers[i].id == member.id){
                                    self.dialog.editRole.roleMembers.splice(i, 1);
                                    break;
                                }
                            }
                        }
                    });
                },
                handleEditRoleClose:function(){
                    var self = this;
                    self.dialog.editRole.members.data.splice(0, self.dialog.editRole.members.data.length);
                    self.dialog.editRole.roles.data.splice(0, self.dialog.editRole.roles.data.length);
                    self.dialog.editRole.roles.currentId = '';
                    self.dialog.editRole.roleMembers.splice(0, self.dialog.editRole.roleMembers.length);
                    self.dialog.editRole.visible = false;
                    self.dialog.editRole.loading = false;
                },
                startGroup:function(){
                    var self = this;
                    self.options.startGroup.loading = true;
                    ajax.post('/command/basic/start', {
                        id:self.groupId
                    }, function(data, status, message){
                        self.options.startGroup.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        self.$message({
                            type:'success',
                            message:'会议已开始'
                        });
                        self.group.status = 'start';
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                stopGroup:function(){
                    var self = this;
                    self.options.stopGroup.loading = true;
                    ajax.post('/command/basic/stop', {
                        id:self.groupId
                    }, function(data, status, message){
                        self.options.stopGroup.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        self.$message({
                            type:'success',
                            message:'会议已停止'
                        });
                        self.group.status = 'stop';
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                startRecord:function(){
                    var self = this;
                    self.options.startRecord.loading = true;
                    if(self.options.startRecord.record == false){
                    	 ajax.post('/command/record/start/record/group', {
                             id:self.groupId
                         }, function(data, status, message){
                             self.options.startRecord.loading = false;
                             if(status !== 200){
                                 self.$message({
                                     type:'error',
                                     message:message
                                 });
                                 return;
                             }
                             self.options.startRecord.record = true;
                             self.$message({
                                 type:'success',
                                 message:'录制已开始'
                             });
                         }, null, ajax.TOTAL_CATCH_CODE);
                    }else{
                    	 ajax.post('/command/record/stop/record/group', {
                             id:self.groupId
                         }, function(data, status, message){
                             self.options.startRecord.loading = false;
                             if(status !== 200){
                                 self.$message({
                                     type:'error',
                                     message:message
                                 });
                                 return;
                             }
                             self.options.startRecord.record = false;
                             self.$message({
                                 type:'success',
                                 message:'录制结束'
                             });
                         }, null, ajax.TOTAL_CATCH_CODE);
                    }
                   
                },
                handleAgendaRun:function(scope){
                    var self = this;
                    var row = scope.row;
                    ajax.post('/command/agenda/run', {
                        groupId:self.groupId,
                        agendaId:row.id
                    }, function(){
                        self.$message({
                            type:'success',
                            message:'执行成功！'
                        });
                        for(var i=0; i<self.agenda.rows.length; i++){
                            self.agenda.rows[i].isRun = false;
                        }
                        row.isRun = true;
                    });
                },
                handleSpeakerSet:function(scope){
                    var self = this;
                    self.dialog.speakerSet.member = scope;
                    self.dialog.speakerSet.visible = true;
                },
                handleSpeakerSetClose:function(){
                    var self = this;
                    self.dialog.speakerSet.member = '';
                    self.dialog.speakerSet.visible = false;
                    self.dialog.speakerSet.loading = false;
                },
                handleSpeakerSetSubmit:function(speakerRole){
                    var self = this;
                    self.dialog.speakerSet.loading = true;
                    ajax.post('/group/member/role/exchange/member/role', {
                        groupId:self.groupId,
                        memberIds: $.toJSON([self.dialog.speakerSet.member.data.id]),
                        roleId:speakerRole.id
                    }, function(data, status , message){
                        self.dialog.speakerSet.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        var param = $.parseJSON(self.dialog.speakerSet.member.data.param);
                        param.roleId = speakerRole.id;
                        param.roleName = speakerRole.name;
                        self.dialog.speakerSet.member.data.param = $.toJSON(param);

                        if(data && data.other && data.other.length>0){
                            for(var i=0; i<data.other.length; i++){
                                var other = data.other[i];
                                var nodes = self.findTreeNode(other.id, 'MEMBE', self.members.data);
                                var changedMemberNode = nodes.self;
                                var param = $.parseJSON(changedMemberNode.param);
                                param.roleId = other.roleId;
                                param.roleName = other.roleName;
                                changedMemberNode.param = $.toJSON(param);
                            }
                        }

                        self.handleSpeakerSetClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleRemoveMember:function(){
                    var self = this;
                    var nodeDatas = self.$refs.memberTree.getCheckedNodes();
                    var memberIds = [];
                    if(nodeDatas && nodeDatas.length>0){
                        for(var i=0; i<nodeDatas.length; i++){
                            if(nodeDatas[i].type === 'MEMBE'){
                                memberIds.push(nodeDatas[i].id);
                            }
                        }
                    }
                    if(memberIds.length <= 0){
                        self.$message({
                            type:'error',
                            message:'您没有选择成员'
                        });
                        return;
                    }
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'提示',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['是否移除当前（'+memberIds.length+'）名成员?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/command/basic/remove/members', {
                                    id:self.groupId,
                                    members: $.toJSON(memberIds)
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    self.loadMembers();
                                    self.loadForwardSettings(true);
                                }, null, ajax.TOTAL_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                handleAddMember:function(){
                    var self = this;
                    self.dialog.addMember.userAndHalls.splice(0, self.dialog.addMember.userAndHalls.length);
                    ajax.post('/command/query/find/institution/tree/user/except/command', {
                        id:self.groupId
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.dialog.addMember.userAndHalls.push(data[i]);
                            }
                        }
                        self.dialog.addMember.visible = true;
                    });
                },
                handleAddMemberClose:function(){
                    var self = this;
                    self.dialog.addMember.userAndHalls.splice(0, self.dialog.addMember.userAndHalls.length);
                    self.dialog.addMember.filterName = '';
                    self.dialog.addMember.visible = false;
                    self.dialog.addMember.loading = false;
                },
                handleAddMemberSubmit:function(){
                    var self = this;
                    var nodeDatas = self.$refs.addMemberTree.getCheckedNodes();
                    var userIds = [];
                    var hallIds = [];
                    if(nodeDatas!=null && nodeDatas.length>0){
                        for(var i=0; i<nodeDatas.length; i++){
                            if(nodeDatas[i].type === 'USER'){
                                userIds.push(nodeDatas[i].id);
                            }else if(nodeDatas[i].type === 'CONFERENCE_HALL'){
                                hallIds.push(nodeDatas[i].id);
                            }
                        }
                    }
                    if(userIds.length<=0 && hallIds.length<=0){
                        self.$message({
                            type:'error',
                            message:'您没有勾选成员'
                        });
                        return;
                    }
                    self.dialog.addMember.loading = true;
                    ajax.post('/command/basic/add/members', {
                        id:self.groupId,
                        members: $.toJSON(userIds),
                        hallIds: $.toJSON(hallIds)
                    }, function(data, status, message){
                        self.dialog.addMember.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        self.loadMembers();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleEditSpeakerChannel:function(scope){
                    var self = this;
                    self.dialog.editSpeakerChannel.role = scope;
                    self.dialog.editSpeakerChannel.terminal.data.splice(0, self.dialog.editSpeakerChannel.terminal.data.length);
                    ajax.post('/command/basic/query/terminal/type/of/group', {
                        groupId:self.groupId
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.dialog.editSpeakerChannel.terminal.data.push(data[i]);
                            }
                        }
                        self.dialog.editSpeakerChannel.visible = true;
                    })
                },
                handleEditSpeakerChannelClose:function(){
                    var self = this;
                    self.dialog.editSpeakerChannel.role = '';
                    self.dialog.editSpeakerChannel.visible = false;
                    self.dialog.editSpeakerChannel.loadings.splice(0, 1, false);
                    self.dialog.editSpeakerChannel.loadings.splice(1, 1, false);
                    self.dialog.editSpeakerChannel.terminal.data.splice(0,  self.dialog.editSpeakerChannel.terminal.data.length);
                    self.dialog.editSpeakerChannel.terminal.currentId = '';
                    self.dialog.editSpeakerChannel.terminal.current = '';
                    self.dialog.editSpeakerChannel.permissions.splice(0, self.dialog.editSpeakerChannel.permissions.length);
                },
                getTerminalChannelPermissionName:function(roleChannel){
                    var self = this;
                    for(var i=0; i<self.dialog.editSpeakerChannel.permissions.length; i++){
                        var permission = self.dialog.editSpeakerChannel.permissions[i];
                        if(permission.roleChannelId == roleChannel.id){
                            return permission.terminalName + ' - ' + permission.terminalChannelName;
                        }
                    }
                    return '';
                },
                handleAddSpeakerChannelSubmit:function(type){
                    var self = this;
                    var channels = self.dialog.editSpeakerChannel.role.data.children;
                    var count = 0;
                    if(channels && channels.length>0){
                        for(var i=0; i<channels.length; i++){
                            if(channels[i].channelType == type){
                                count++;
                            }
                        }
                    }
                    count++;
                    var name = (type==='VIDEO_ENCODE'?'视频编码':'音频编码') + count;
                    var loadingIndex = (type==='VIDEO_ENCODE'?0:1);
                    self.dialog.editSpeakerChannel.loadings.splice(i, 1, true);
                    ajax.post('/tetris/bvc/model/role/channel/add', {
                        name:name,
                        type:type,
                        roleId:self.dialog.editSpeakerChannel.role.data.id
                    }, function(data, status, message){
                        self.dialog.editSpeakerChannel.loadings.splice(i, 1, false);
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        var channelNode = {
                            id:data.id,
                            name:data.name,
                            type:'ROLE_CHANNEL',
                            channelType:data.type,
                            channelTypeName:data.typeName,
                            isLeaf:true
                        };
                        if(self.dialog.editSpeakerChannel.role.data.children.length<=0){
                            self.dialog.editSpeakerChannel.role.data.children.push(channelNode);
                        }else{
                            var begin = false;
                            for(var i=0; i<self.dialog.editSpeakerChannel.role.data.children.length; i++){
                                if(self.dialog.editSpeakerChannel.role.data.children[i].channelType == data.type){
                                    begin = true;
                                    if(i==self.dialog.editSpeakerChannel.role.data.children.length-1){
                                        self.dialog.editSpeakerChannel.role.data.children.push(channelNode);
                                        break;
                                    }
                                }else{
                                    if(begin == true){
                                        self.dialog.editSpeakerChannel.role.data.children.splice(i, 0, channelNode);
                                        break;
                                    }
                                }
                            }
                        }
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                removeSpeakerChannel:function(channel){
                    var self = this;
                    var count = 0;
                    for(var i=0; i<self.dialog.editSpeakerChannel.role.data.children.length; i++){
                        if(self.dialog.editSpeakerChannel.role.data.children[i].channelType == channel.channelType){
                            count ++;
                        }
                    }
                    if(count === 1){
                        var message = '';
                        if(channel.channelType === 'VIDEO_ENCODE'){
                            message = '最后一个视频编码通道不能删除!';
                        }else{
                            message = '最后一个音频编码通道不能删除!';
                        }
                        self.$message({
                            type:'error',
                            message:message
                        });
                        return;
                    }
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'提示',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['是否移除当前发言人通道?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/role/channel/delete', {
                                    id:channel.id
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    for(var i=0; i<self.dialog.editSpeakerChannel.role.data.children.length; i++){
                                        if(self.dialog.editSpeakerChannel.role.data.children[i].id == channel.id){
                                            self.dialog.editSpeakerChannel.role.data.children.splice(i, 1);
                                            self.$refs.memberTree.remove(channel);
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
                handleEditSpeakerChannelName:function(channel){
                    var self = this;
                    self.dialog.editSpeakerChannelName.channel = channel;
                    self.dialog.editSpeakerChannelName.id = channel.id;
                    self.dialog.editSpeakerChannelName.name = channel.name;
                    self.dialog.editSpeakerChannelName.visible = true;
                },
                handleEditSpeakerChannelNameClose:function(){
                    var self = this;
                    self.dialog.editSpeakerChannelName.id = '';
                    self.dialog.editSpeakerChannelName.name = '';
                    self.dialog.editSpeakerChannelName.visible = false;
                    self.dialog.editSpeakerChannelName.loading = false;
                },
                handleEditSpeakerChannelNameSubmit:function(){
                    var self = this;
                    self.dialog.editSpeakerChannelName.loading = true;
                    ajax.post('/tetris/bvc/model/role/channel/edit/name', {
                        id:self.dialog.editSpeakerChannelName.id,
                        name:self.dialog.editSpeakerChannelName.name
                    }, function(data, status, message){
                        self.dialog.editSpeakerChannelName.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        self.dialog.editSpeakerChannelName.channel.name = self.dialog.editSpeakerChannelName.name;
                        self.handleEditSpeakerChannelNameClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleEditSpeakerChannelPermission:function(channel){
                    var self = this;
                    if(!self.dialog.editSpeakerChannel.terminal.currentId){
                        self.$message({
                            type:'error',
                            message:'请选择终端！'
                        });
                        return;
                    }
                    self.dialog.editSpeakerChannelPermission.channel = channel;
                    self.dialog.editSpeakerChannelPermission.terminalChannels.splice(0, self.dialog.editSpeakerChannelPermission.terminalChannels.length);
                    ajax.post('/tetris/bvc/model/terminal/channel/load/by/type', {
                        terminalId:self.dialog.editSpeakerChannel.terminal.currentId,
                        type:channel.channelType
                    }, function(data){
                        var existPermission = '';
                        for(var i=0; i<self.dialog.editSpeakerChannel.permissions.length; i++){
                            if(self.dialog.editSpeakerChannel.permissions[i].roleChannelId == channel.id){
                                existPermission = self.dialog.editSpeakerChannel.permissions[i];
                                break;
                            }
                        }
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                if(existPermission && data[i].id==existPermission.terminalChannelId) continue;
                                self.dialog.editSpeakerChannelPermission.terminalChannels.push(data[i]);
                            }
                        }
                        self.dialog.editSpeakerChannelPermission.visible = true;
                    });
                },
                handleEditSpeakerChannelPermissionClose:function(){
                    var self = this;
                    self.dialog.editSpeakerChannelPermission.visible = false;
                    self.dialog.editSpeakerChannelPermission.loading = false;
                    self.dialog.editSpeakerChannelPermission.channel = '';
                    self.dialog.editSpeakerChannelPermission.terminalChannels.splice(0, self.dialog.editSpeakerChannelPermission.terminalChannels.length);
                    self.dialog.editSpeakerChannelPermission.terminalChannels.currentTerminalChannelId = '';
                },
                handleEditSpeakerChannelPermissionSubmit:function(){
                    var self = this;
                    if(!self.dialog.editSpeakerChannelPermission.currentTerminalChannelId){
                        self.$message({
                            type:'error',
                            message:'请选择终端通道'
                        });
                        return;
                    }
                    self.dialog.editSpeakerChannelPermission.loading = true;
                    ajax.post('/tetris/bvc/model/role/channel/terminal/bundle/channel/permission/add', {
                        roleChannelId:self.dialog.editSpeakerChannelPermission.channel.id,
                        terminalId:self.dialog.editSpeakerChannel.terminal.currentId,
                        terminalChannelIds: $.toJSON([self.dialog.editSpeakerChannelPermission.currentTerminalChannelId])
                    }, function(data, status, message){
                        self.dialog.editSpeakerChannelPermission.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.dialog.editSpeakerChannel.permissions.push(data[i]);
                            }
                        }
                        self.handleEditSpeakerChannelPermissionClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                loadEditSpeakerChannelPermission:function(){
                    var self = this;
                    self.dialog.editSpeakerChannel.permissions.splice(0, self.dialog.editSpeakerChannel.permissions.length);
                    ajax.post('/tetris/bvc/model/role/channel/terminal/bundle/channel/permission/load/by/role/id/and/terminal/id', {
                        roleId:self.dialog.editSpeakerChannel.role.data.id,
                        terminalId:self.dialog.editSpeakerChannel.terminal.currentId
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.dialog.editSpeakerChannel.permissions.push(data[i]);
                            }
                        }
                    });
                },
                handleEditParam:function(){
                    var self = this;
                    ajax.post('/command/param/query/avtpl/' + self.groupId, null, function(data){
                        self.dialog.editParam.id = data.id;
                        self.dialog.editParam.videoFormat = data.videoFormat;
                        self.dialog.editParam.audioFormat = data.audioFormat;
                        self.dialog.editParam.portReuse = data.mux;
                        self.dialog.editParam.adaptions.splice(0, self.dialog.editParam.adaptions.length);
                        if(data.gears && data.gears.length>0){
                            for(var i=0; i<data.gears.length; i++){
                                self.dialog.editParam.adaptions.push({
                                    adaptionId:data.gears[i].id,
                                    adaptionName:data.gears[i].channelParamsType,
                                    videoRate:data.gears[i].videoBitRate,
                                    videoResolution:data.gears[i].videoResolution,
                                    frameRate:data.gears[i].frameRate,
                                    audioRate:data.gears[i].audioBitRate,
                                    videoFormat:data.gears[i].videoFormat,
                                    audioFormat: data.gears[i].audioFormat
                                });
                            }
                        }
                        self.dialog.editParam.visible = true;
                    });
                },
                handleEditParamClose:function(){
                    var self = this;
                    self.dialog.editParam.visible = false;
                    self.dialog.editParam.loading = false;
                    self.dialog.editParam.id = '';
                    self.dialog.editParam.videoFormat = '';
                    self.dialog.editParam.audioFormat = '';
                    self.dialog.editParam.portReuse = false;
                    self.dialog.editParam.adaptions.splice(0, self.dialog.editParam.adaptions.length);
                },
                handleEditParamSubmit:function(){
                    var self = this;
                    self.dialog.editParam.loading = true;
                    ajax.post('/command/param/modify/avtpl/' + self.dialog.editParam.id, {
                        videoFormat:self.dialog.editParam.videoFormat,
                        audioFormat:self.dialog.editParam.audioFormat,
                        portReuse:self.dialog.editParam.portReuse,
                        adaptionJson: $.toJSON(self.dialog.editParam.adaptions)
                    }, function(data, status , message){
                        self.dialog.editParam.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        self.handleEditParamClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                }
            },
            mounted:function(){
                var self= this;
                self.loadAudioTypes();
                self.loadAudioPriorities();
                self.loadMembers();
                self.loadRoles();
                self.loadAgendas();
            }
        });

    };

    var destroy = function(){

    };

    var groupList = {
        path:'/' + pageId + '/:groupId/:groupName',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});