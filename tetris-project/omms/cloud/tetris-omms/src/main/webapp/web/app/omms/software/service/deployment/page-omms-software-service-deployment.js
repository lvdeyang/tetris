/**
 * Created by lqxuhv on 2020/9/2.
 */
/**
 * Created by lvdeyang on 2018/12/24 0024.
 */
define([
    'text!' + window.APPPATH + 'omms/software/service/deployment/page-omms-software-service-deployment.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'date',
    'css!' + window.APPPATH + 'omms/software/service/deployment/page-omms-software-service-deployment.css'
], function(tpl, config, $, ajax, context, commons, Vue){

    var pageId = 'page-omms-software-service-deployment';

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
                valueTypes:[],
                table:{
                    rows:[],
                    pageSize:50,
                    pageSizes:[5,50, 100, 200, 400],
                    currentPage:0,
                    total:0
                },
                dialog:{
                    selectInstallationPackage:{
                        visible:false,
                        tree:{
                            props:{
                                label:'name',
                                children:'children',
                                isLeaf:'isLeaf'
                            },
                            data:[],
                            current:''
                        },
                        list:{
                            data:[],
                            current:''
                        }
                    },
                    step:{
                        visible:false,
                        active:0,
                        interval:'',
                        currentDeployment:'',
                        properties:[],
                        loading:false
                    },
                    updateStep:{
                        visible:false,
                        active:0,
                        interval:'',
                        currentDeployment:'',
                        properties:[],
                        loading:false,
                        updatePackageId:0
                    },
                    editProperties:{
                        visible:false,
                        properties:[],
                        loading:false
                    },
                    backups:{
                        visible:false,
                        backupData:[]
                    },
                    updatePackages:{
                        visible:false,
                        name:'',
                        version:'',
                        updatePackagesData:[],
                        deploymentId:0
                    },
                    addBackup:{
                        visible:false,
                        isBackup:true,
                        notes:""
                    },
                    uninstallService:{
                        visible:false,
                        type:"uninstall",
                        row:{},
                        notes:""
                    },
                    modifyParameters:{
                        visible:false,
                        properties:[],
                        deploymentId:0,
                    },
                    database:{
                        rows:[],
                        visible:false,
                        databaseIP:"",
                        databasePort:"",
                        ipAndPort:""
                    }
                }

            },
            computed:{

            },
            watch:{

            },
            methods: {
                handleSizeChange:function(size){
                    var self = this;
                    self.table.pageSize = size;
                    self.load(self.table.currentPage);
                },
                handleCurrentChange:function(currentPage){
                    var self = this;
                    self.load(currentPage);
                },
                rowKey:function(row){
                    return row.id;
                },
                load:function(currentPage){
                    var self = this;
                    var param = {
                        serverId:self.serverId,
                        currentPage:currentPage,
                        pageSize:self.table.pageSize
                    };
                    self.table.rows.splice(0, self.table.rows.length);
                    ajax.post('/service/deployment/load', param, function(data){
                        var total = data.total;
                        var rows = data.rows;
                        if(rows && rows.length>0){
                            for(var i = 0;i<rows.length;i++){
                                rows[i].popvisible = false;
                                self.table.rows.push(rows[i]);
                            }
                            self.table.total = total;
                        }
                        self.table.currentPage = currentPage;
                    });
                },
                handleCreate:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.selectInstallationPackage.visible = true;
                    self.dialog.selectInstallationPackage.tree.data.splice(0, self.dialog.selectInstallationPackage.tree.data.length);
                    ajax.post('/service/type/find/all', null, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.dialog.selectInstallationPackage.tree.data.push(data[i]);
                            }
                        }
                    });
                },
                handleSelectInstallationPackageClose:function(){
                    var self = this;
                    self.dialog.selectInstallationPackage.visible = false;
                    self.dialog.selectInstallationPackage.tree.data.splice(0, self.dialog.selectInstallationPackage.tree.data.length);
                    self.dialog.selectInstallationPackage.tree.current = '';
                    self.dialog.selectInstallationPackage.list.data.splice(0, self.dialog.selectInstallationPackage.list.data.length);
                    self.dialog.selectInstallationPackage.list.current = '';
                },
                handleSelectInstallationPackageSubmit:function(){
                    var self = this;
                    if(self.dialog.selectInstallationPackage.tree.current &&
                        self.dialog.selectInstallationPackage.list.current){
                        self.dialog.step.visible = true;
                        ajax.post('/service/deployment/upload', {
                            serverId:self.serverId,
                            installationPackageId:self.dialog.selectInstallationPackage.list.current.id
                        }, function(data){
                            self.table.rows.push(data);
                            self.table.total += 1;
                            self.dialog.step.currentDeployment = data;
                            self.dialog.step.interval = setInterval(function(){
                                ajax.post('/service/deployment/query/upload/status', {
                                    serviceDeploymentId:self.dialog.step.currentDeployment.id
                                }, function(data){
                                    for(var i=0; i<self.table.rows.length; i++){
                                        if(self.table.rows[i].id == data.id){
                                            self.table.rows.splice(i, 1, data);
                                            break;
                                        }
                                    }
                                    self.dialog.step.currentDeployment = data;
                                    if(self.dialog.step.currentDeployment.error){
                                        clearInterval(self.dialog.step.interval);
                                    }else if(self.dialog.step.currentDeployment.step == 'CONFIG'){
                                        clearInterval(self.dialog.step.interval);
                                        self.dialog.step.properties.splice(0, self.dialog.step.properties.length);
                                        ajax.post('/properties/find/by/installation/package/id', {
                                            installationPackageId:self.dialog.selectInstallationPackage.list.current.id
                                        }, function(data){
                                            setTimeout(function(){
                                                self.dialog.step.active = 1;
                                            }, 1000);
                                            if(data && data.length>0){
                                                for(var i=0; i<data.length; i++){
                                                    data[i].value = data[i].propertyDefaultValue;
                                                    if(data[i].valueSelect) data[i].valueSelect = $.parseJSON(data[i].valueSelect);
                                                    self.dialog.step.properties.push(data[i]);
                                                }
                                            }
                                        });
                                    }
                                })
                            }, 1000);
                        });
                    }else if(!self.dialog.selectInstallationPackage.tree.current){
                        self.$message({
                            type:'error',
                            message:'请选择要安装的服务'
                        });
                        return;
                    }else if(!self.dialog.selectInstallationPackage.list.current){
                        self.$message({
                            type:'error',
                            message:'请选择要安装的版本'
                        });
                        return;
                    }
                },
                currentTreeNodeChange:function(current){
                    var self = this;
                    self.dialog.selectInstallationPackage.tree.current = current;
                    self.dialog.selectInstallationPackage.list.data.splice(0, self.dialog.selectInstallationPackage.list.data.length);
                    if(current.type === 'FOLDER') return;
                    ajax.post('/installation/package/load', {
                        serviceTypeId:current.id
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                data[i].value = data[i].propertyDefaultValue;
                                if(data[i].valueSelect) data[i].valueSelect = $.parseJSON(data[i].valueSelect);
                                self.dialog.selectInstallationPackage.list.data.push(data[i]);
                                //self.dialog.installPackages.installPackagesData.push(data[i]);

                            }
                        }
                    });
                },
                handleSelectPackage:function(p){
                    var self = this;
                    if(p.current === true) return;
                    for(var i=0; i<self.dialog.selectInstallationPackage.list.data.length; i++){
                        if(self.dialog.selectInstallationPackage.list.data[i]!==p) Vue.set(self.dialog.selectInstallationPackage.list.data[i], 'current', false);
                    }
                    Vue.set(p, 'current', true);
                    self.dialog.selectInstallationPackage.list.current = p;
                },
                handleStepClose:function(){
                    var self = this;
                    self.dialog.step.active = 0;
                    if(self.dialog.step.interval){
                        clearInterval(self.dialog.step.interval);
                    }
                    self.dialog.step.interval = '';
                    self.dialog.step.currentDeployment = '';
                    self.dialog.step.properties.splice(0, self.dialog.step.properties.length);
                    self.dialog.step.loading = false;
                    self.dialog.step.visible = false;
                },
                handleUpdateStepClose:function(){
                    var self = this;
                    self.dialog.updateStep.active = 0;
                    if(self.dialog.updateStep.interval){
                        clearInterval(self.dialog.updateStep.interval);
                    }
                    self.dialog.updateStep.interval = '';
                    self.dialog.updateStep.currentDeployment = '';
                    self.dialog.updateStep.properties.splice(0, self.dialog.updateStep.properties.length);
                    self.dialog.updateStep.loading = false;
                    self.dialog.updateStep.visible = false;
                },
                handleInstall:function(){
                    var self = this;
                    var config = {};
                    if(self.dialog.step.properties.length > 0){
                        for(var i=0; i<self.dialog.step.properties.length; i++){
                            config[self.dialog.step.properties[i].propertyKey] = self.dialog.step.properties[i].value;
                        }
                        config["databaseAddr"] = self.dialog.database.databaseIP;
                        config["databaseport"] = self.dialog.database.databasePort;
                    }
                    self.dialog.step.active = 2;
                    ajax.post('/service/deployment/install', {
                        deploymentId:self.dialog.step.currentDeployment.id,
                        config: $.toJSON(config)
                    }, function(data, status, message){
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        setTimeout(function(){
                            self.dialog.step.active = 3;
                        }, 1000);
                        if(status !== 200){
                            return;
                        }
                        self.dialog.database.rows.splice(0, self.dialog.database.rows.length);
                        self.dialog.database.ipAndPort = "";
                        self.dialog.database.databaseIP = "";
                        self.dialog.database.databasePort = "";
                        self.load(self.table.currentPage);
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                restart:function(p){
                    var self = this;
                    ajax.post('/service/deployment/restart', {
                        deploymentId: p.serviceDeploymentId,
                        processId: p.processId
                    }, function(data, status, message){
                        self.load(self.table.currentPage);
                        /*if(status === 200){
                            self.status = false;
                        }*/
                        /*if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        setTimeout(function(){
                            self.dialog.step.active = 3;
                        }, 1000);
                        if(status !== 200){
                            return;
                        }*/
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                deploymentStatus:function(scope){
                    var self = this;
                    var row = scope.row;
                },
                handleDelete:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.uninstallService.row = row;
                    self.dialog.uninstallService.visible = true;
                },
                handleUninstallServiceClose:function(){
                    var self = this;
                    self.dialog.uninstallService.visible = false;
                    self.dialog.uninstallService.type = "uninstall";
                },
                handleRowDelete:function(){
                    var self = this;
                    var row = self.dialog.uninstallService.row;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'危险操作',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['此操作将卸载该服务，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/service/deployment/uninstall', {
                                    deploymentId:row.id,
                                    type:self.dialog.uninstallService.type,
                                    notes:self.dialog.uninstallService.notes
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    if(status !== 200) return;
                                    if(self.dialog.uninstallService.type === "delete"){
                                        for(var i=0; i<self.table.rows.length; i++){
                                            if(self.table.rows[i].id === row.id){
                                                self.table.rows.splice(i, 1);
                                                break;
                                            }
                                        }
                                    }
                                    self.table.total -= 1;
                                    if(self.table.total>0 && self.table.rows.length===0){
                                        self.load(self.table.currentPage - 1);
                                    }
                                    self.load(self.table.currentPage);
                                    self.dialog.uninstallService.notes = "";
                                    self.dialog.uninstallService.visible = false;
                                    done();
                                }, null, ajax.NO_ERROR_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                gotoBackup:function(scope){
                    var self = this;
                    var row = scope.row;
                    window.location.hash = '#/page-omms-software-service-backup/' + row.id + '/' + row.name;
                },
                backupRestore:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.backups.backupData.splice(0, self.dialog.backups.backupData.length);
                    ajax.post('/service/deployment/findBackup',{ deploymentId:row.id }, function(data, status, message){
                        for(var i = 0; i < data.length; i++){
                            self.dialog.backups.backupData.push(data[i]);
                        }
                        self.dialog.backups.visible = true;
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleSelectBackupClose:function(){
                    var self = this;
                    self.dialog.backups.visible = false;
                },
                handleSelectBackupClick:function(scope){
                    var self = this;
                    var row = scope.row;
                    ajax.post('/service/deployment/restore',{ backupId:row.id }, function(data, status, message){
                        self.dialog.backups.visible = false;
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                selectUpdatePackages:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.updatePackages.deploymentId = row.id;
                    self.dialog.updatePackages.updatePackagesData.splice(0, self.dialog.updatePackages.updatePackagesData.length);
                    ajax.post('/installation/package/load', {
                        serviceTypeId:row.serviceTypeId
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                if(data[i].id != row.installationPackageId){
                                    self.dialog.updatePackages.updatePackagesData.push(data[i]);
                                }
                            }
                        }
                        self.dialog.updatePackages.visible = true;
                    });
                },
                handleSelectUpdatePackageClose:function(){
                    var self = this;
                    self.dialog.updatePackages.visible = false;
                },
                handleSelectUpdatePackageSubmit:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.updatePackages.name = row.fileName;
                    self.dialog.updatePackages.version = row.version;
                    self.dialog.updateStep.visible = true;
                    self.dialog.updateStep.updatePackageId = row.id;
                    ajax.post('/service/deployment/upload',{
                        serverId:self.serverId,
                        installationPackageId:row.id
                    }, function(data){
                        //self.table.rows.push(data);
                        //self.table.total += 1;
                        self.dialog.updateStep.currentDeployment = data;
                        self.dialog.updateStep.interval = setInterval(function(){
                            ajax.post('/service/deployment/query/upload/status', {
                                serviceDeploymentId:self.dialog.updateStep.currentDeployment.id
                            }, function(data){
                                for(var i=0; i<self.table.rows.length; i++){
                                    if(self.table.rows[i].id == data.id){
                                        self.table.rows.splice(i, 1, data);
                                        break;
                                    }
                                }
                                self.dialog.updateStep.currentDeployment = data;
                                if(self.dialog.updateStep.currentDeployment.error){
                                    clearInterval(self.dialog.updateStep.interval);
                                }else if(self.dialog.updateStep.currentDeployment.step == 'CONFIG'){
                                    clearInterval(self.dialog.updateStep.interval);
                                    self.dialog.updateStep.properties.splice(0, self.dialog.updateStep.properties.length);
                                    ajax.post('/properties/find/update/parameters', {
                                        installationPackageId:row.id,
                                        deploymentId:self.dialog.updatePackages.deploymentId
                                    }, function(data){
                                        setTimeout(function(){
                                            self.dialog.updateStep.active = 1;
                                        }, 1000);
                                        if(data && data.length>0){
                                            for(var i=0; i<data.length; i++){
                                                //data[i].value = data[i].propertyDefaultValue;
                                                if(data[i].valueSelect) data[i].valueSelect = $.parseJSON(data[i].valueSelect);
                                                self.dialog.updateStep.properties.push(data[i]);
                                            }
                                        }
                                    });
                                }
                            })
                        }, 1000);
                    })
                },
                handleUpdate:function(){
                    var self = this;
                    var config = {};
                    if(self.dialog.updateStep.properties.length > 0){
                        for(var i=0; i<self.dialog.updateStep.properties.length; i++){
                            config[self.dialog.updateStep.properties[i].propertyKey] = self.dialog.updateStep.properties[i].propertyValue;
                        }
                        config["databaseAddr"] = self.dialog.database.databaseIP;
                        config["databaseport"] = self.dialog.database.databasePort;
                    }
                    self.dialog.addBackup.visible = false;
                    self.dialog.updateStep.active = 2;
                    ajax.post('/service/deployment/update', {
                        deploymentId:self.dialog.updateStep.currentDeployment.id,
                        updatePackageId:self.dialog.updateStep.updatePackageId,
                        config: $.toJSON(config),
                        isBackup:self.dialog.addBackup.isBackup,
                        notes:self.dialog.addBackup.notes
                    }, function(data, status, message){
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        setTimeout(function(){
                            self.dialog.updateStep.active = 3;
                        }, 1000);
                        if(status !== 200){
                            return;
                        }
                        self.dialog.addBackup.isBackup = true;
                        self.dialog.addBackup.notes = '';
                        self.dialog.database.rows.splice(0, self.dialog.database.rows.length);
                        self.dialog.database.ipAndPort = "";
                        self.dialog.database.databaseIP = "";
                        self.dialog.database.databasePort = "";
                        self.load(self.table.currentPage);
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleAddBackup:function(){
                    var self = this;
                    self.dialog.addBackup.visible = true;
                },
                handleAddBackupClose:function(){
                    var self = this;
                    self.dialog.addBackup.visible = false;
                    self.dialog.addBackup.isBackup = false;
                    self.dialog.addBackup.notes = '';
                },
                deleteDeploymentData:function(scope){
                    var self = this;
                    var row = scope.row;
                    ajax.post('/service/deployment/delete/deployment/data',{ deploymentId:row.id }, function(data, status, message){
                        self.load(self.table.currentPage);
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                modifyParameters:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.modifyParameters.deploymentId = row.id;
                    self.dialog.modifyParameters.visible = true;
                    ajax.post('/properties/find/by/deployment/id', {
                        deploymentId:row.id
                    }, function(data){
                        if(data && data.length>0){
                            for(var i = 0; i < data.length; i++){
                                //data[i].value = data[i].propertyDefaultValue;
                                if(data[i].valueSelect) data[i].valueSelect = $.parseJSON(data[i].valueSelect);
                                self.dialog.modifyParameters.properties.push(data[i]);
                            }
                        }
                    });
                },
                handleModifyParametersClose:function(){
                    var self = this;
                    self.dialog.modifyParameters.visible = false;
                    self.dialog.modifyParameters.properties.splice(0, self.dialog.modifyParameters.properties.length);
                    self.dialog.database.rows.splice(0, self.dialog.database.rows.length);
                    self.dialog.database.ipAndPort = "";
                    self.dialog.database.databaseIP = "";
                    self.dialog.database.databasePort = "";
                },
                handleModifyParametersSubmit:function(){
                    var self = this;
                    var config = {};
                    if(self.dialog.modifyParameters.properties.length > 0){
                        for(var i = 0; i < self.dialog.modifyParameters.properties.length; i++){
                            config[self.dialog.modifyParameters.properties[i].propertyKey] = self.dialog.modifyParameters.properties[i].propertyValue;
                        }
                        config["databaseAddr"] = self.dialog.database.databaseIP;
                        config["databaseport"] = self.dialog.database.databasePort;
                    }
                    ajax.post('/properties/modifyParameters',{
                        deploymentId:self.dialog.modifyParameters.deploymentId,
                        config:$.toJSON(config)
                    }, function(data){
                        self.dialog.modifyParameters.visible = false;
                        self.dialog.modifyParameters.properties.splice(0, self.dialog.modifyParameters.properties.length);
                        self.dialog.database.rows.splice(0, self.dialog.database.rows.length);
                        self.dialog.database.ipAndPort = "";
                        self.dialog.database.databaseIP = "";
                        self.dialog.database.databasePort = "";
                    })
                },
                selectDatabase:function(){
                    var self = this;
                    self.dialog.database.visible = true;
                    ajax.post('/server/findAllDatabase', {}, function(data){
                        if(data && data.length > 0){
                            for(var i = 0; i < data.length; i++){
                                self.dialog.database.rows.push(data[i]);
                            }
                        }
                    });
                },
                handleSelectDatabaseClose:function(){
                    var self = this;
                    self.dialog.database.visible = false;
                    self.dialog.database.rows.splice(0, self.dialog.database.rows.length);
                },
                handleSelectDatabaseClick:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.database.ipAndPort = row.databaseIP + ":" + row.databasePort;
                    self.dialog.database.databaseIP = row.databaseIP;
                    self.dialog.database.databasePort = row.databasePort;
                    self.dialog.database.visible = false;
                    self.dialog.database.rows.splice(0, self.dialog.database.rows.length);
                },
                databaseBackup:function(p){
                    var self = this;
                    ajax.post('/service/deployment/database/backup', { id: p.id }, function(data){

                    });
                },
                databaseRestore:function(p){
                    var self = this;
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