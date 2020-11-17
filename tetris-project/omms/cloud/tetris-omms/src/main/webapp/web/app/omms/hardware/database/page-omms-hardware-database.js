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
                        databaseName:"",
                        username: "",
                        password: ""
                    }
                }
            },
            computed:{

            },
            watch:{

            },
            methods: {
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
                    self.dialog.database.username = "";
                    self.dialog.database.password = "";
                },
                handleAddDatabaseSubmit:function(){
                    var self = this;
                    ajax.post('/server/addDatabase', {
                        serverId:self.serverId,
                        databasePort:self.dialog.database.databasePort,
                        databaseName:self.dialog.database.databaseName,
                        username:self.dialog.database.username,
                        password:self.dialog.database.password
                    }, function(data, status){
                        self.rows.push(data);
                        self.dialog.database.visible = false;
                        self.dialog.database.databasePort = "";
                        self.dialog.database.databaseName = "";
                        self.dialog.database.username = "";
                        self.dialog.database.password = "";
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                }
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