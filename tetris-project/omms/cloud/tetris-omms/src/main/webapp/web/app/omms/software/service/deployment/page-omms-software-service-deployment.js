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
                    editProperties:{
                        visible:false,
                        properties:[],
                        loading:false
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
                                self.table.rows.push(rows[i])
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
                handleInstall:function(){
                    var self = this;
                    var config = {};
                    if(self.dialog.step.properties.length > 0){
                        for(var i=0; i<self.dialog.step.properties.length; i++){
                            config[self.dialog.step.properties[i].propertyKey] = self.dialog.step.properties[i].value;
                        }
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
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                editDeployment:function(scope){
                    var self = this;
                    var row = scope.row;
                },
                deploymentStatus:function(scope){
                    var self = this;
                    var row = scope.row;
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
                                h('p', null, ['此操作将卸载改服务，且不可恢复，是否继续?'])
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
                                    deploymentId:row.id
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    if(status !== 200) return;
                                    for(var i=0; i<self.table.rows.length; i++){
                                        if(self.table.rows[i].id === row.id){
                                            self.table.rows.splice(i, 1);
                                            break;
                                        }
                                    }
                                    self.table.total -= 1;
                                    if(self.table.total>0 && self.table.rows.length===0){
                                        self.load(self.table.currentPage - 1);
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
        path:'/' + pageId + '/:serverId/:serverName',
        component:{
            template:'<div id="' + pageId + '"class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});