/**
 * Created by lvdeyang on 2020/6/5.
 */
define([
    'text!' + window.APPPATH + 'tetris/business/group/page-group.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'date',
    'css!' + window.APPPATH + 'tetris/business/group/page-group.css',
    'css!' + window.APPPATH + 'tetris/model/terminal/editor/graph-node-icons/style.css'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-group';

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
                groupOrderTypes:[],
                groupLockTypes:[],
                pickerOptions:{
                    shortcuts:[{
                        text: '最近一周',
                        onClick:function(picker){
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                            picker.$emit('pick', [start, end]);
                        }
                    },{
                        text: '最近一个月',
                        onClick:function(picker){
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                            picker.$emit('pick', [start, end]);
                        }
                    },{
                        text: '最近三个月',
                        onClick:function(picker){
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                            picker.$emit('pick', [start, end]);
                        }
                    }]
                },
                filter:'',
                deviceTree:{
                    data:[],
                    props:{
                        children:'children',
                        label:'name',
                        isLeaf:'isLeaf'
                    }
                },
                contacts:{
                    data:[],
                    props:{
                        children:'children',
                        label:'name',
                        isLeaf:'isLeaf'
                    }
                },
                group:{
                    rows:[]
                },
                dialog:{
                    addGroup:{
                        visible:false,
                        loading:false,
                        radio:'',
                        chairmanId:'',
                        chairmanType:'',
                        chairmanName:'',
                        name:'',
                        filterText:'',
                        filterHalls:[],
                        filterUsers:[],
                        halls:[],
                        users:[],
                        handleOrder:false,
                        groupOrderType:'',
                        timeScope:'',
                        groupLockType:''
                    },
                    editGroup:{
                        visible:false,
                        loading:false,
                        id:'',
                        name:'',
                        groupLockType:''
                    }
                }
            },
            computed:{
                dialogAddGroupFilterText:function(){
                    var self = this;
                    return self.dialog.addGroup.filterText;
                }
            },
            watch:{
                dialogAddGroupFilterText:function(){
                    var self = this;
                    self.handleDeviceFilter();
                },
                filter:function(val){
                    var self = this;
                    self.$refs.deviceTree.filter(val);
                    self.$refs.contactsTree.filter(val);
                }
            },
            methods:{
                loadGroupOrderTypes:function(){
                    var self = this;
                    self.groupOrderTypes.splice(0, self.groupOrderTypes.length);
                    ajax.post('/command/query/query/order/group/type', null, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.groupOrderTypes.push(data[i]);
                            }
                        }
                    });
                },
                loadGroupLockTypes:function(){
                    var self = this;
                    self.groupLockTypes.splice(0, self.groupLockTypes.length);
                    ajax.post('/command/query/query/group/lock', null, function(data){
                        if(data && data.length>0){
                            self.groupLockTypes.push({
                                name:'不锁定',
                                value:'UN_LOCK'
                            });
                            for(var i=0; i<data.length; i++){
                                self.groupLockTypes.push(data[i]);
                            }
                        }
                    });
                },
                loadDevices:function(){
                    var self = this;
                    self.deviceTree.data.splice(0, self.deviceTree.data.length);
                    ajax.post('/command/query/find/institution/tree/user/filter/0', null, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.deviceTree.data.push(data[i]);
                            }
                        }
                    });
                },
                loadContacts:function(){
                    var self = this;
                    self.contacts.data.splice(0, self.contacts.data.length);
                    var contactsFolder = {
                        id:-1,
                        name:'联系人',
                        type:'CONTACTS_FOLDER',
                        isLeaf:false,
                        children:[]
                    };
                    self.contacts.data.push(contactsFolder);
                },
                loadGroups:function(){
                    var self = this;
                    ajax.post('/command/query/create/by/myself/list', null, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.group.rows.push(data[i]);
                            }
                        }
                    });
                },
                handleAddGroup:function(){
                    var self = this;
                    var deviceNodes = self.$refs.deviceTree.getCheckedNodes();
                    var contactsNodes = self.$refs.contactsTree.getCheckedNodes();
                    self.dialog.addGroup.halls.splice(0, self.dialog.addGroup.halls.length);
                    self.dialog.addGroup.filterHalls.splice(0, self.dialog.addGroup.filterHalls.length);
                    self.dialog.addGroup.users.splice(0, self.dialog.addGroup.users.length);
                    self.dialog.addGroup.filterUsers.splice(0, self.dialog.addGroup.filterUsers.length);
                    if(deviceNodes!=null && deviceNodes.length>0){
                        for(var i=0; i<deviceNodes.length; i++){
                            if(deviceNodes[i].type === 'USER'){
                                self.dialog.addGroup.users.push(deviceNodes[i]);
                                self.dialog.addGroup.filterUsers.push(deviceNodes[i]);
                            }else if(deviceNodes[i].type === 'CONFERENCE_HALL'){
                                self.dialog.addGroup.halls.push(deviceNodes[i]);
                                self.dialog.addGroup.filterHalls.push(deviceNodes[i]);
                            }
                        }
                    }
                    if(contactsNodes!=null && contactsNodes.length>0){
                        for(var i=0; i<contactsNodes.length; i++){
                            if(contactsNodes[i].type === 'USER'){
                                self.dialog.addGroup.users.push(contactsNodes[i]);
                                self.dialog.addGroup.filterUsers.push(deviceNodes[i]);
                            }else if(contactsNodes[i].type === 'CONFERENCE_HALL'){
                                self.dialog.addGroup.halls.push(contactsNodes[i]);
                                self.dialog.addGroup.filterHalls.push(deviceNodes[i]);
                            }
                        }
                    }
                    if(self.dialog.addGroup.users.length<=0 && self.dialog.addGroup.halls.length<=0){
                        self.$message({
                            type:'error',
                            message:'您没有选择成员'
                        });
                        return;
                    }
                    self.dialog.addGroup.groupOrderType = self.groupOrderTypes[0].value;
                    self.dialog.addGroup.groupLockType = self.groupLockTypes[0].value;
                    self.dialog.addGroup.visible = true;
                },
                handleAddGroupClose:function(){
                    var self = this;
                    self.dialog.addGroup.visible = false;
                    self.dialog.addGroup.loading = false;
                    self.dialog.addGroup.name = '';
                    self.dialog.addGroup.radio = '';
                    self.dialog.addGroup.chairmanId = '';
                    self.dialog.addGroup.chairmanType = '';
                    self.dialog.addGroup.chairmanName = '';
                    self.dialog.addGroup.filterText = '';
                    self.dialog.addGroup.halls.splice(0, self.dialog.addGroup.halls.length);
                    self.dialog.addGroup.filterHalls.splice(0, self.dialog.addGroup.filterHalls.length);
                    self.dialog.addGroup.users.splice(0, self.dialog.addGroup.users.length);
                    self.dialog.addGroup.filterUsers.splice(0, self.dialog.addGroup.filterUsers.length);
                    self.dialog.addGroup.handleOrder = false;
                    self.dialog.addGroup.groupOrderType = '';
                    self.dialog.addGroup.timeScope = '';
                    self.dialog.addGroup.groupLockType = '';
                },
                handleDeviceFilter:function(){
                    var self = this;
                    self.dialog.addGroup.filterHalls.splice(0, self.dialog.addGroup.filterHalls.length);
                    self.dialog.addGroup.filterUsers.splice(0, self.dialog.addGroup.filterUsers.length);
                    for(var i=0; i<self.dialog.addGroup.halls.length; i++){
                        if(!self.dialog.addGroup.filterText ||
                            self.dialog.addGroup.halls[i].name.indexOf(self.dialog.addGroup.filterText)>=0){
                            self.dialog.addGroup.filterHalls.push(self.dialog.addGroup.halls[i]);
                        }
                    }
                    for(var i=0; i<self.dialog.addGroup.users.length; i++){
                        if(!self.dialog.addGroup.filterText ||
                            self.dialog.addGroup.users[i].name.indexOf(self.dialog.addGroup.filterText)>=0){
                            self.dialog.addGroup.filterUsers.push(self.dialog.addGroup.users[i]);
                        }
                    }
                },
                handleChairmanChange:function(val){
                    var self = this;
                    val = val.split('-');
                    self.dialog.addGroup.chairmanId = parseInt(val[0]);
                    self.dialog.addGroup.chairmanType = val[1];
                    if(self.dialog.addGroup.chairmanType === 'USER'){
                        for(var i=0; i<self.dialog.addGroup.users.length; i++){
                            if(self.dialog.addGroup.users[i].id == self.dialog.addGroup.chairmanId){
                                self.dialog.addGroup.chairmanName = self.dialog.addGroup.users[i].name;
                                break;
                            }
                        }
                    }else{
                        for(var i=0; i<self.dialog.addGroup.halls.length; i++){
                            if(self.dialog.addGroup.halls[i].id == self.dialog.addGroup.chairmanId){
                                self.dialog.addGroup.chairmanName = self.dialog.addGroup.halls[i].name;
                                break;
                            }
                        }
                    }
                },
                handleAddGroupSubmit:function(){
                    var self = this;
                    if(!self.dialog.addGroup.name){
                        self.$message({
                            type:'error',
                            message:'名称不能为空'
                        });
                        return;
                    }
                    if(!self.dialog.addGroup.chairmanId){
                        self.$message({
                            type:'error',
                            message:'请设置主席'
                        });
                        return;
                    }
                    if(self.dialog.addGroup.handleOrder){
                        if(!self.dialog.addGroup.timeScope || self.dialog.addGroup.timeScope.length<2){
                            self.$message({
                                type:'error',
                                message:'预约会议请指定开始时间和结束时间'
                            });
                            return;
                        }
                    }
                    self.dialog.addGroup.loading = true;
                    var members = [];
                    for(var i=0; i<self.dialog.addGroup.users.length; i++){
                        members.push(self.dialog.addGroup.users[i].id);
                    }
                    var hallIds = [];
                    for(var i=0; i<self.dialog.addGroup.halls.length; i++){
                        hallIds.push(self.dialog.addGroup.halls[i].id);
                    }
                    var param  = {
                        members:$.toJSON(members),
                        hallIds:$.toJSON(hallIds),
                        chairmanType:self.dialog.addGroup.chairmanType==='USER'?'user':'hall',
                        chairmanId:self.dialog.addGroup.chairmanId,
                        name:self.dialog.addGroup.name
                    };

                    if(self.dialog.addGroup.handleOrder){
                        param.orderGroupType = self.dialog.addGroup.groupOrderType;
                        param.orderBeginTime = self.dialog.addGroup.timeScope[0].format('yyyy-MM-dd hh:mm:ss');
                        param.orderEndTime = self.dialog.addGroup.timeScope[1].format('yyyy-MM-dd hh:mm:ss');
                    }

                    if(self.dialog.addGroup.groupLockType !== 'UN_LOCK'){
                        param.groupLock = self.dialog.addGroup.groupLockType;
                    }

                    ajax.post('/command/meeting/save', param, function(data, status, message){
                        self.dialog.addGroup.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        if(data.status === 'error'){
                            self.$message({
                                type:'error',
                                message:data.errorInfo.msg + '，建议名称：' + data.errorInfo.recommendedName
                            });
                            self.dialog.addGroup.name = data.errorInfo.recommendedName;
                            return;
                        }
                        self.group.rows.push(data.group);
                        self.handleAddGroupClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleRowEdit:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.editGroup.id = row.id;
                    self.dialog.editGroup.name = row.name;
                    self.dialog.editGroup.groupLockType = row.groupLock?row.groupLock:'UN_LOCK';
                    self.dialog.editGroup.visible = true;
                },
                handleEditGroupClose:function(){
                    var self = this;
                    self.dialog.editGroup.id = '';
                    self.dialog.editGroup.name = '';
                    self.dialog.editGroup.visible = false;
                    self.dialog.editGroup.loading = false;
                },
                handleEditGroupSubmit:function(){
                    var self = this;
                    if(!self.dialog.editGroup.name){
                        self.$message({
                            type:'error',
                            message:'名称不能为空'
                        });
                        return;
                    }
                    var param = {
                        id:self.dialog.editGroup.id,
                        name:self.dialog.editGroup.name
                    };
                    if(self.dialog.editGroup.groupLockType !== 'UN_LOCK'){
                        param.groupLock = self.dialog.editGroup.groupLockType;
                    }
                    self.dialog.editGroup.loading = true;
                    ajax.post('/command/basic/edit', param, function(){
                        for(var i=0; i<self.group.rows.length; i++){
                            if(self.group.rows[i].id == self.dialog.editGroup.id){
                                self.group.rows[i].name = self.dialog.editGroup.name;
                                break;
                            }
                        }
                        self.handleEditGroupClose();
                    });
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
                                h('p', null, ['删除当前会议？'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/command/basic/remove', {
                                    ids: $.toJSON([row.id])
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    for(var i=0; i<self.group.rows.length; i++){
                                        if(self.group.rows[i].id == row.id){
                                            self.group.rows.splice(i, 1);
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
                gotoCustomAgenda:function(scope){
                    var self = this;
                    var row = scope.row;
                    window.location.hash = '#/page-custom-agenda/' + row.id + '/' + row.name;
                },
                handleRefresh:function(scope){
                    var self = this;
                    var row = scope.row;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'提示',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['一键刷新？'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/command/basic/refresh/group/member', {
                                    groupId: row.id
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    self.$message({
                                        type:'success',
                                        message:'刷新成功！'
                                    });
                                }, null, ajax.TOTAL_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                handleReset:function(scope){
                    var self = this;
                    var row = scope.row;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'提示',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['一键重置？'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/command/basic/reset/group/member', {
                                    groupId: row.id
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    self.$message({
                                        type:'error',
                                        message:'重置成功！'
                                    });
                                }, null, ajax.TOTAL_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                filterNodeByName:function(val, data){
                    if(data.name && data.name.indexOf(val)>=0) return true;
                    if(data.param){
                        var param = $.parseJSON(data.param);
                        if(param.roleName){
                            var name = data.name + ' - ' + param.roleName;
                            if(name.indexOf(val) >= 0) return true;
                        }
                    }
                    return false;
                }
            },
            mounted:function(){
                var self = this;
                self.loadDevices();
                self.loadContacts();
                self.loadGroups();
                self.loadGroupOrderTypes();
                self.loadGroupLockTypes();
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