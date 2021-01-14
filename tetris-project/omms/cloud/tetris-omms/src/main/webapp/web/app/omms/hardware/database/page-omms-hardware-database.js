/**
 * Created by Administrator on 2020/11/2.
 */

define([
    'text!' + window.APPPATH + 'omms/hardware/database/page-omms-hardware-database.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'date',
    'css!' + window.APPPATH + 'omms/hardware/database/page-omms-hardware-database.css'
], function(tpl, config, $, ajax, context, commons, Vue){

    var pageId = 'page-omms-hardware-database';

    var charts = {};

    var init = function(p){

        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el: '#' + pageId + '-wrapper',
            data: {
                basePath:window.BASEPATH,
                menus: context.getProp('menus'),
                user: context.getProp('user'),
                groups: context.getProp('groups'),
                serverId: p.serverId,
                serverName: p.serverName,
                rows:[],
                dialog:{
                    database:{
                        visible: false,
                        databasePort: "",
                        databaseIp:'',
                        databaseName:"",
                        username: "",
                        password: ""
                    },
                    backup:{
                        visible: false,
                        databases:[
                            {name:"ccc"},
                            {name:"ccc"},
                            {name:"ccc"},
                        ],
                    },
                    backupMessage:{
                        visible:false,
                        name:'',
                        remark:'',
                    },

                },
                hardwareListData:[

                ],//后台返回的多选项
                checkedData: [],//选择多选框时的选中值
                zz:[0],
                checkedId:[
                    {realName:''}
                ],
                databaseId:'',

            },
            computed:{

            },
            watch:{

            },
            methods: {
                checkbox:function(){
                    var self = this;
                    self.data = self.dialog.backup;
                    data.forEach(v =>{
                        if(v.databases &&v.databases.length){
                            v.databases.forEach(sub => {
                                sub.isCheck = false
                            })
                        }
                    })
                },
                handleChange:function(e,id){
                    var self = this;
                    if(e){
                        self.checkedData.push(id);
                    }else{
                        self.delete(id)
                    }
                    console.log(self.checkedData);
                },
                delete(id){
                    var self = this;
                    var index = self.checkedData.findIndex(item => {
                        if(item == id){
                            return true;
                        }
                    });
                    self.checkedData.splice(index,1)
                },
                transArrToString(arr) {
                    if (!Array.isArray(arr)) return false;
                    var str = "";
                    for (var i = 0, len = arr.length; i < len; i++) {
                        i == 0 ? (str = arr[i]) : (str += "," + arr[i]);
                    }
                    return str;
                },

                handleBackupDatabaseSubmit:function(){
                    var self = this;
                    if(self.checkedData.length > 0){
                        self.dialog.backupMessage.visible = true;
                    }else{
                        self.$message({
                            type:'error',
                            message:"请选择需要备份的数据库！"
                        })
                    }
                },
                handleBackupMessageClose:function(){
                    var self = this;
                    self.dialog.backupMessage.name = '';
                    self.dialog.backupMessage.message = '';
                    self.dialog.backupMessage.visible = false;
                },
                handleBackupMessageSubmit:function(){
                    var self = this;
                    var databases = this.transArrToString(this.checkedData);
                    var name = self.dialog.backupMessage.name;
                    var remark = self.dialog.backupMessage.remark;
                    if(name === '' || remark === ''){
                        self.$message({
                            type:'error',
                            message:"请填写备份名称和说明！"
                        })
                    }else{
                        ajax.post('/database/backup/databases',{
                            id:self.databaseId,
                            databases:databases,
                            name:name,
                            remark:remark},function(data,status){
                            if(status !== 200){
                                self.$message({
                                    type:'error',
                                    message:message
                                });
                                return null;
                                self.handleBackupMessageClose();
                                self.backupDatabaseClose();
                            }
                            self.handleBackupMessageClose();
                            self.backupDatabaseClose();
                        })
                    }

                },
                gotoDatabasesBackup:function(scope){
                    var self = this;
                    var row = scope.row;
                    window.location.hash = '#/page-omms-hardware-database-backup/' + row.id + '/' + row.databaseName;
                },

                rowKey:function(row){
                    return row.id;
                },
                load:function(){
                    var self = this;
                    ajax.post('/server/findDatabase', { serverId: self.serverId }, function(data){
                        for(var i = 0; i < data.length; i++){
                            self.rows.push(data[i]);
                        }
                    })
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
                                h('p', null, ['此操作将删除备份，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/server/deleteDatabase', {
                                    databaseId:row.id
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    if(status !== 200) return;
                                    for(var i = 0; i < self.rows.length; i++){
                                        if(self.rows[i].id === row.id){
                                            self.rows.splice(i, 1);
                                            break;
                                        }
                                    }
                                    done();
                                }, null, ajax.NO_ERROR_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                handleCreate:function(){
                    var self = this;
                    self.dialog.database.visible = true;
                },
                handleAddDatabaseClose:function(){
                    var self = this;
                    self.dialog.database.visible = false;
                    self.dialog.database.databasePort = "";
                    self.dialog.database.databaseName = "";
                    self.dialog.database.databaseIp = "";
                    self.dialog.database.username = "";
                    self.dialog.database.password = "";
                },
                handleAddDatabaseSubmit:function(){
                    var self = this;
                    ajax.post('/server/addDatabase', {
                        serverId:self.serverId,
                        databaseIp:self.dialog.database.databaseIp,
                        databasePort:self.dialog.database.databasePort,
                        databaseName:self.dialog.database.databaseName,
                        username:self.dialog.database.username,
                        password:self.dialog.database.password
                    }, function(data, status){
                        self.rows.push(data);
                        self.dialog.database.visible = false;
                        self.dialog.database.databasePort = "";
                        self.dialog.database.databaseIp = "",
                        self.dialog.database.databaseName = "";
                        self.dialog.database.username = "";
                        self.dialog.database.password = "";
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                handleRowBackup:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.databaseId = row.id;
                    ajax.post('/database/query/databases',{
                        id:row.id},function(data,status){
                        var data = data;
                        for(i=0;i<data.length;i++){
                            self.hardwareListData.push(data[i]);
                        }
                        self.dialog.backup.visible = true;
                    })
                },
                backupDatabaseClose:function(){
                    var self = this;
                    self.dialog.backup.visible = false;
                    self.dialog.backup.databases = [];
                    self.hardwareListData = [];
                    self.checkedData = [];
                    self.zz = [0];
                    self.databaseId = '';
                },
            },
            mounted:function(){
                var self = this;
                self.load(1);
            }
        });

    };

    var destroy = function(){

    };

    var groupList = {
        path:'/' + pageId + '/:serverId/:serverName',
        component:{
            template:'<div id="' + pageId + '"class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});