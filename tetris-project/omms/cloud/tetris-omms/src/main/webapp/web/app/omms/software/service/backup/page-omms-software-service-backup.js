/**
 * Created by Administrator on 2020/10/16.
 */

define([
    'text!' + window.APPPATH + 'omms/software/service/backup/page-omms-software-service-backup.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'date',
    'css!' + window.APPPATH + 'omms/software/service/backup/page-omms-software-service-backup.css'
], function(tpl, config, $, ajax, context, commons, Vue){

    var pageId = 'page-omms-software-service-backup';

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
                deploymentId: p.deploymentId,
                deploymentName: p.deploymentName,
                rows:[],
                dialog:{
                    addBackupVisible: false,
                    notes: ''
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
                    ajax.post('/service/deployment/findBackup', { deploymentId: self.deploymentId }, function(data){
                        console.log(data);

                        for(var i = 0; i < data.length; i++){
                            self.rows.push(data[i]);
                        }
                    })
                },
                handleCreate:function(){
                    var self = this;
                    self.dialog.addBackupVisible = true;
                },
                handleAddBackupClose:function(){
                    var self = this;
                    self.dialog.addBackupVisible = false;
                    self.dialog.notes = '';
                },
                handleAddBackupSubmit:function(){
                    var self = this;
                    var param = {
                        deploymentId: self.deploymentId,
                        deploymentName: self.deploymentName,
                        notes: self.dialog.notes
                    };
                    ajax.post('/service/deployment/backup', param, function(data){
                        self.rows.push(data);
                        self.dialog.addBackupVisible = false;
                        self.dialog.notes = '';
                    });
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
                                ajax.post('/service/deployment/deleteBackup', {
                                    backupId:row.id
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    if(status !== 200) return;
                                    for(var i=0; i<self.rows.length; i++){
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
        path:'/' + pageId + '/:deploymentId/:deploymentName',
        component:{
            template:'<div id="' + pageId + '"class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});