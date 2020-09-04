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
                        server:'',
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
                        active:0
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
                            console.log(data);
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
                handleSelectInstallationPackageClose:function(){
                    var self = this;
                    self.dialog.step.visible = false;
                },
                handleStepClose:function(){
                    var self = this;
                    self.dialog.step.visible = false;
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