define([
    'text!' + window.APPPATH + 'component/bvc2-tab-buttons/bvc2-tab-buttons.html',
    'restfull',
    'jquery',
    'vue',
    'element-ui',
    'bvc2-dialog-set-audio',
    'bvc2-dialog-publish-authority',
    'bvc2-update-spokesman',
    'bvc2-update-roles',
    'bvc2-dialog-authorization-list'
], function(tpl, ajax, $, Vue){

    //组件名称
    var bvc2TabButtons = 'bvc2-tab-buttons';

    //icons
    var startSessionIcon = 'icon-play';
    var stopSessionIcon = 'icon-stop';
    var sessionRecordIcon = 'icon-facetime-video';
    var monitorRecordIcon = 'icon-facetime-video';
    var setVolumeIcon = 'icon-volume-up';
    var queryForwardIcon = 'icon-table';
    //添加"发布直播"按钮
    var publishAuthorityIcon = 'icon-cloud-upload';

    //style
    var hidden = 'display:none;';
    var show = 'display:inline';

    Vue.component(bvc2TabButtons, {
        props:['group'],
        template: tpl,
        data:function(){
            return {
                authorization:{
                    query:'/authorization/query/group/bundles/',
                    save:'/authorization/watch/meeting/save/'
                },
                addmembers:{
                    members:[],
                    defaultProps:{
                        children:'children',
                        label:'name',
                        isLeaf:'isLeaf'
                    },
                    treeLoading:false
                },
                addmembersFilterText:'',
                removemembers:{
                    members:[],
                    defaultProps:{
                        children:'children',
                        label:'name',
                        isLeaf:'isLeaf'
                    },
                    treeLoading:false
                },
                removemembersFilterText:'',
                members:[],
                values:[],
                style:{
                    startSession:{
                        isLoading:false,
                        icon:startSessionIcon
                    },
                    stopSession:{
                        isLoading:false,
                        icon:stopSessionIcon
                    },
                    sessionRecord:{
                        isLoading:false,
                        icon:sessionRecordIcon
                    },
                    monitorRecord:{
                        isLoading:false,
                        icon:monitorRecordIcon
                    },
                    setVolume:{
                        isLoading:false,
                        icon:setVolumeIcon
                    },
                    queryForward:{
                        isLoading:false,
                        icon: queryForwardIcon
                    },
                    saveMembers:{
                        isLoading:false
                    },
                    deleteMembers:{
                        isLoading:false
                    }
                },
                record:{
                    //录制名称
                    name:'',
                    //录制详细描述
                    describe:''
                },
                dialog:{
                    updateSpokesman:{
                        visible:false
                    },
                    updateRoles:{
                        visible:false
                    },
                    addMembers:{
                        visible:false
                    },
                    removeMembers:{
                        visible:false
                    }
                }
            }
        },
        computed:{
            recordStatus:function(){
                if(this.group.record !== true){
                    return 'primary';
                }else{
                    return 'danger';
                }
            },
            recordTip:function(){
                if(this.group.record !== true){
                    return '开始录制';
                }else{
                    return '停止录制';
                }
            },
            startSessionDisplay:function(){
                if(this.style.startSession.isLoading){
                    return hidden;
                }else{
                    return show;
                }
            },
            stopSessionDisplay:function(){
                if(this.style.stopSession.isLoading){
                    return hidden;
                }else{
                    return show;
                }
            },
            sessionRecordDisplay:function(){
                if(this.style.sessionRecord.isLoading){
                    return hidden;
                }else{
                    return show;
                }
            },
            setVolumeDisplay:function(){
                if(this.style.setVolume.isLoading){
                    return hidden;
                }else{
                    return show;
                }
            },
            queryForwardDisplay:function(){
                if(this.style.queryForward.isLoading){
                    return hidden;
                }else{
                    return show;
                }
            },
            publishAuthorityDisplay:function(){
            	if(this.style.publishAuthority.isLoading){
            		return hidden;
            	}else{
            		return show;
            	}
            }
        },
        watch: {
            addmembersFilterText: function(val){
                this.$refs.$addMembers.filter(val);
            },
            removemembersFilterText: function(val){
                this.$refs.$removeMembers.filter(val);
            }
        },
        methods:{
            startSession:function(){
                var buttons_instance = this;
                var session = buttons_instance.session;
                buttons_instance.style.startSession.isLoading = true;
                buttons_instance.style.stopSession.isLoading = true;
                ajax.post('/device/group/start/' + buttons_instance.group.id, null, function(data, status){
                    if(status === 200){
                        buttons_instance.$message({
                            type:'success',
                            message:'操作成功！'
                        });
                    }
                    buttons_instance.style.startSession.isLoading = false;
                    buttons_instance.style.stopSession.isLoading = false;
                    if(status === 200){
                        for(var i in data){
                            buttons_instance.group[i] = data[i];
                        }
                    }
                }, null, [403]);
            },
            stopSession:function(){
                var buttons_instance = this;

                //transferToVod: 默认0不转换成点播；1转换
                var doStopSession = function(transferToVod){
                    buttons_instance.style.startSession.isLoading = true;
                    buttons_instance.style.stopSession.isLoading = true;
                    var data = {};
                    data.transferToVod = transferToVod;
                    ajax.post('/device/group/stop/' + buttons_instance.group.id, data, function(data){
                        buttons_instance.$message({
                            type:'success',
                            message:'操作成功！'
                        });
                        buttons_instance.style.startSession.isLoading = false;
                        buttons_instance.style.stopSession.isLoading = false;
                        for(var i in data){
                            buttons_instance.group[i] = data[i];
                        }
                        buttons_instance.$emit('stop-success');
                    });
                };

                if(buttons_instance.group.type == '监控室') {
                    buttons_instance.$confirm('监控室通常应保持开启状态', '注意', {
                        confirmButtonText: '停止',
                        cancelButtonText: '取消',
                        type: 'warning',
                        beforeClose: function (action, instance, done) {
                            if (action === 'confirm') {
                                doStopSession("0");
                            } else if (action === 'cancel') {
                            }
                            done();
                        }
                    });
                }else{
                    //doStopSession();
                    if(buttons_instance.group.record === true) {
                        //当前会议在录制
                        buttons_instance.$confirm('停会后保存录制内容吗？', '停会', {
                            //取消走 'close' 不走 'cancel'
                            distinguishCancelAndClose: true,
                            confirmButtonText: '保存',
                            cancelButtonText: '不保存',
                            type: 'warning',
                            beforeClose: function (action, instance, done) {
                                if (action === 'confirm') {
                                    doStopSession("1");
                                } else if (action === 'cancel') {
                                    doStopSession("0");
                                } else {
                                    buttons_instance.$message({
                                        type:'info',
                                        message:'取消操作！'
                                    });
                                }
                                done();
                            }
                        });
                    }else{
                        doStopSession("0");
                    }
                }
            },
            sessionRecord:function(){
                var buttons_instance = this;
                buttons_instance.style.sessionRecord.isLoading = true;
                setTimeout(function(){
                    if(buttons_instance.group.record === true){
                        buttons_instance.$confirm('保存录制内容到点播吗？', '停止录制', {
                            //取消走 'close' 不走 'cancel'
                            distinguishCancelAndClose: true,
                            confirmButtonText: '保存',
                            cancelButtonText: '不保存',
                            type: 'warning',
                            beforeClose: function (action, instance, done) {
                                var data = {};
                                if (action === 'close') {
                                    buttons_instance.$message({
                                        type:'info',
                                        message:'取消操作！'
                                    });
                                    done();
                                    return;
                                }
                                if (action === 'confirm') {
                                    data.transferToVod =  "1";
                                } else if (action === 'cancel') {
                                    data.transferToVod = "0";
                                }
                                ajax.post('/device/group/stop/record/scheme/' + buttons_instance.group.id, data, function(data){
                                    //停止录制
                                    buttons_instance.$message({
                                        type:'success',
                                        message:'操作成功！'
                                    });
                                    Vue.set(buttons_instance.group, 'record', false);
                                });

                                done();
                            }
                        });
                    }else{
                        var h = buttons_instance.$createElement;
                        var content = h('el-form', {
                            attrs:{
                                ref:'form',
                                'label-width':'80px'
                            }
                        }, [
                            h('el-form-item', {
                                props:{
                                    label:'录制名称'
                                }
                            }, [
                                h('el-input', {
                                    attrs:{
                                        id:'record-name'
                                    },
                                    on:{
                                        change:function(v){
                                            buttons_instance.record.name = v;
                                        }
                                    }
                                }, null)
                            ]),
                            h('el-form-item', {
                                props:{
                                    label:'详细描述'
                                }
                            }, [
                                h('el-input', {
                                    attrs:{
                                        type:'textarea',
                                        rows:'3',
                                        id:'record-describe'
                                    },
                                    on:{
                                        change:function(v){
                                            buttons_instance.record.describe = v;
                                        }
                                    }
                                }, null)
                            ])
                        ]);
                        buttons_instance.$msgbox({
                            title:'填写录制信息',
                            message: content,
                            showCancelButton: true,
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            beforeClose: function(action, instance, done){
                                if(action === 'confirm'){
                                    buttons_instance.style.sessionRecord.isLoading = true;
                                    ajax.post('/device/group/run/record/scheme/' + buttons_instance.group.id, {
                                        name: buttons_instance.record.name,
                                        describe: buttons_instance.record.describe
                                    }, function(data, status){
                                        if(status === 200){
                                            //开始录制
                                            buttons_instance.$message({
                                                type:'success',
                                                message:'操作成功！'
                                            });
                                            Vue.set(buttons_instance.group, 'record', true);
                                        }
                                        buttons_instance.style.sessionRecord.isLoading = false;
                                    }, 'application/x-www-form-urlencoded; charset=UTF-8', [403]);
                                }else if(action === 'cancel'){
                                    buttons_instance.$message({
                                        type:'info',
                                        message:'取消操作！'
                                    });
                                }
                                done();
                            }
                        });
                    }
                    buttons_instance.style.sessionRecord.isLoading = false;
                }, 500);
            },
            monitorRecord:function(){
                var buttons_instance = this;
                window.location.hash = '#/page-group-monitor/' + buttons_instance.group.id;
            },
            queryRecordPlayList:function(){
                var buttons_instance = this;
                window.location.hash = '#/page-group-record/' + buttons_instance.group.id;
            },
            setVolume:function(){
                var buttons_instance = this;
                var _values = buttons_instance.values;
                var _members = buttons_instance.members;

                _values.splice(0, _values.length);
                _members.splice(0, _members.length);

                ajax.get('/device/group/query/bundle/' + buttons_instance.group.id, null, function(data){
                    var bundles = data.bundles;
                    for(var i=0;i<bundles.length;i++){
                        var bundle = bundles[i];
                        if(bundle.param.indexOf( 'combineJv230') >= 0) continue;
                        _members.push(bundle);
                        var param = JSON.parse(bundle.param);
                        if(param.openAudio){
                            _values.push(bundle.id);
                        }
                    }

                    buttons_instance.$refs.setAudio.volumeValue = data.volume;
                    buttons_instance.$refs.setAudio.audioVisible = true;
                    buttons_instance.$refs.setAudio.style.saveAudio.text = '完成';
                    buttons_instance.$refs.setAudio.style.saveAudio.isLoading = false;
                });
            },
            queryForward:function(){
                var buttons_instance = this;
                window.location.hash = '#/page-group-info-combinevideo/' + buttons_instance.group.id + '/10/1';
            },
            virtualSource:function(){
                var buttons_instance = this;
                window.location.hash = '#/page-group-virtual-source/' + buttons_instance.group.id;
            },
            publishAuthority:function(){
            	var buttons_instance = this;
            	var _values = buttons_instance.values;
            	var _members = buttons_instance.members;
            	
            	_values.splice(0,_values.length);
            	_members.splice(0,_members.length);
            	
            	buttons_instance.$refs.selectRecord.dialogFormVisible = true;
            	
            },
            updateSpokesman:function(){
                var button_instance = this;
                button_instance.dialog.updateSpokesman.visible = true;
                if(button_instance.$refs.updateSpokesman){
                    button_instance.$refs.updateSpokesman.refreshGroupMember();
                    button_instance.$refs.updateSpokesman.refreshRole();
                }
            },
            updateRoles:function(){
                var button_instance = this;
                button_instance.dialog.updateRoles.visible = true;
                if(button_instance.$refs.updateRole){
                    button_instance.$refs.updateRole.refreshGroupMember();
                    button_instance.$refs.updateRole.refreshRole();
                }
            },
            addMembers: function(){
                var button_instance = this;
                button_instance.dialog.addMembers.visible = true;
//                button_instance.style.addMembers.isLoading = false;
            },
            removeMembers: function(){
                var button_instance = this;
                button_instance.dialog.removeMembers.visible = true;
                button_instance.style.deleteMembers.isLoading = false;
            },
            setWatchAuthorization: function(){
                var button_instance = this;

                button_instance.$refs.$setWatchAuthorization.refreshAllMember(this.group);
                button_instance.$refs.$setWatchAuthorization.dialogVisible = true;

            },
            menuButtonClick:function(command){
                var button_instance = this;
                if(command === 'editRole'){
                    button_instance.updateRoles();
                }else if(command === 'addMember'){
                    button_instance.addMembers();
                    button_instance.refreshNotGroupMember();
                }else if(command === 'removeMember'){
                    button_instance.removeMembers();
                    button_instance.refreshGroupMember();
                }else if(command === 'publish'){
                    button_instance.publishAuthority();
                }else if(command === 'queryForward'){
                    button_instance.queryForward();
                }else if(command === 'virtualSource'){
                    button_instance.virtualSource();
                }else if(command === 'queryRecordPlayList'){
                    button_instance.queryRecordPlayList();
                }else if(command === 'setWatchAuthorization'){
                    button_instance.setWatchAuthorization();
                }
            },
            nodeExpand:function(data, node, instance){
                if(data.icon === 'icon-folder-close'){
                    data.icon = 'icon-folder-open';
                }
            },
            addMembersRenderContent:function(h, target){
                var node = target.node,
                    data = target.data;

                if(node.level === 1){
                    node.expanded = true;
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
            removeMembersRenderContent:function(h, target){
                var node = target.node,
                    data = target.data;

                //过滤Channel
                if(data.type === 'CHANNEL'){
                    node.visible = false;
                }else if(data.type === 'BUNDLE'){
                    node.isLeaf = true;
                }

                if(node.level === 1){
                    node.expanded = true;
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
            //获取非会议成员
            refreshNotGroupMember:function(){
                var button_instance = this;
                var group = button_instance.group;
                button_instance.addmembers.treeLoading = true;
                button_instance.addmembers.members.splice(0, button_instance.addmembers.members.length);
                ajax.get('/device/group/query/members/except/group/' + group.id, null, function(data){
                    if(data && data.length>0){
                        for(var i=0; i<data.length; i++){
                            button_instance.addmembers.members.push(data[i]);
                        }
                    }
                    setTimeout(function(){
                        button_instance.addmembers.treeLoading = false;
                    }, 1000);
                });
            },
            //获取会议成员
            refreshGroupMember:function(){
                var button_instance = this;
                var group = button_instance.group;
                button_instance.removemembers.treeLoading = true;
                button_instance.removemembers.members.splice(0, button_instance.removemembers.members.length);
                ajax.get('/device/group/control/query/tree/' + group.id, null, function(data){
                    if(data && data.membersTree.length>0){
                        for(var i=0; i<data.membersTree.length; i++){
                            button_instance.removemembers.members.push(data.membersTree[i]);
                        }
                    }
                    setTimeout(function(){
                        button_instance.removemembers.treeLoading = false;
                    }, 1000);
                });
            },
            //添加成员
            saveMembers:function(){
                var button_instance = this;
                button_instance.style.saveMembers.isLoading = true;
                var group = button_instance.group;
                var members = button_instance.$refs.$addMembers.getCheckedNodes();
                if(members.length == 0){
                    button_instance.$message({
                        type:'warning',
                        message:'请先选择成员！'
                    });
                    button_instance.style.saveMembers.isLoading = false;
                    return;
                }

                var bundleIds = [];
                for(var i=0;i<members.length;i++){
                    if(members[i].type === 'BUNDLE'){
                        bundleIds.push($.parseJSON(members[i].param).bundleId);
                    }
                }
                ajax.post('/device/group/add/members/' + group.id, {
                    bundleIds:$.toJSON(bundleIds)
                }, function(data, status){
                    button_instance.refreshNotGroupMember();
                    if(status === 200){
                        button_instance.$message({
                            type:'success',
                            message:'添加成功！'
                        });
                        //添加成员树节点
                        button_instance.addMemberTreeNode(data.membersTree);
                    }
                    button_instance.style.saveMembers.isLoading = false;
                }, 'application/x-www-form-urlencoded; charset=UTF-8', [403]);
            },
            //删除成员（踢人）
            deleteMembers:function(){
                var button_instance = this;
                button_instance.style.deleteMembers.isLoading = true;
                var group = button_instance.group;
                var members = button_instance.$refs.$removeMembers.getCheckedNodes();
                if(members.length == 0){
                    button_instance.$message({
                        type:'warning',
                        message:'请先选择成员！'
                    });
                    button_instance.style.deleteMembers.isLoading = false;
                    return;
                }

                var memberIds = [];
                var bundles = [];
                var bundleIds = [];
                 for(var i=0;i<members.length;i++){
                    if(members[i].type === 'BUNDLE'){
                        memberIds.push($.parseJSON(members[i].param).memberId);
                        bundles.push({bundleName: members[i].name,
                                      bundleId: members[i].id});
                        bundleIds.push(members[i].id);
                    }
                }
                ajax.post('/device/group/remove/members/' + group.id, {
                    memberIds:$.toJSON(memberIds)
                }, function(data){
                    button_instance.refreshGroupMember();
                    button_instance.$message({
                        type:'success',
                        message:'删除成功！'
                    });
                    for(var i=0;i<bundles.length;i++){
                        //删除树节点
                        button_instance.deleteMemberTreeNode(bundles[i]);
                    }
                    button_instance.style.deleteMembers.isLoading = false;
                    //button_instance.$parent.$parent.$parent.video.srcs.splice(0,1);
                    button_instance.$root.$refs.configs.reConfigAgenda();
                });
            },
            //刷新发言人成员
            refreshSpokesmanMember:function(key, name){
                var instance = this;
                instance.$root.$refs.membersTree.$refs.tree.getNode(key).data.name = name;
            },
            //删除节点
            deleteMemberTreeNode:function(bundle){
                var instance = this;
                var bundleId = bundle.bundleId;
                var bundleName = bundle.bundleName;
                var $tree = instance.$root.$refs.membersTree.$refs.tree;
                var node = $tree.getNode("BUNDLE@@"+bundleId);

                var folderNode = $tree.getNode("FOLDER@@-3");
                var spokesman = folderNode.data.children;
                for(var i=0;i<spokesman.length;i++){
                    var _name = spokesman[i].name.split("-");
                    var _bundleName = '';
                    for(var j=1;j<_name.length;j++){
                        if(j === _name.length-1){
                            _bundleName = _bundleName + _name[j];
                        }else{
                            _bundleName = _bundleName + _name[j] + "-";
                        }
                    }

                    if(_bundleName === bundleName){
                        spokesman[i].name = _name[0];
                    }
                }

                $tree.remove(node);
                try {
                    var $small = instance.$root.$refs.autoLayout.$refs.smallScreen.$refs.tree;
                    $small.remove(node);
                }catch(e){
                    //没有小屏配置，或者删除操作都有可能报错，不需要处理
                }
            },
            //增加节点(空节点不能这样加，只能在已有节点下加)
            addMemberTreeNode:function(datas){
                var instance = this;
                var $tree = instance.$root.$refs.membersTree.$refs.tree;
                instance.handleNode($tree, null, datas);
                if(instance.$root.$refs.autoLayout.$refs.smallScreen){
                    var $small = instance.$root.$refs.autoLayout.$refs.smallScreen.$refs.tree;
                    instance.handleNode($small, null, datas);
                }
            },
            //遍历节点方法
            handleNode:function($tree, parentNode, datas){
                for(var i=0;i<datas.length;i++){
                    var data = datas[i];
                    var node = $tree.getNode(data.key);
                    if(node){
                        if(data.children.length>0){
                            this.handleNode($tree, node, data.children);
                        }
                    }else{
                        $tree.append(data, parentNode);
                    }
                }
            },
            addmembersFilterNode: function(value, data, node){
                if (!value) return true;
                if(data.name.toLowerCase().indexOf(value.toLowerCase()) !== -1 || (node.parent && node.parent.level !== 0 && node.parent.data.name.toLowerCase().indexOf(value.toLowerCase()) !== -1))
                    return true;
            },
            removemembersFilterNode: function(value, data, node){
                if (!value) return true;
                //某些通道的name可能为null
                if (!data.name || data.type == "CHANNEL") return false;
                if(data.name.toLowerCase().indexOf(value.toLowerCase()) !== -1 || (node.parent && node.parent.level !== 0 && node.parent.data.name.toLowerCase().indexOf(value.toLowerCase()) !== -1))
                    return true;
            }
        }
    });

});