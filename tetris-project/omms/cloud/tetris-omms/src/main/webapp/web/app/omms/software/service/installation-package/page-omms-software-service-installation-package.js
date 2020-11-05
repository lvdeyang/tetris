/**
 * Created by lvdeyang on 2018/12/24 0024.
 */
define([
    'text!' + window.APPPATH + 'omms/software/service/installation-package/page-omms-software-service-installation-package.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'date',
    'css!' + window.APPPATH + 'omms/software/service/installation-package/page-omms-software-service-installation-package.css'
], function(tpl, config, $, ajax, context, commons, Vue){

    var pageId = 'page-omms-software-service-installation-package';

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
                serviceTypeId: p.serviceTypeId,
                serviceName: p.serviceName,
                table:{
                    data:[]
                },
                dialog:{
                    addPackage:{
                        visible:false,
                        loading:false,
                        remark:'',
                        creator:'',
                        file:''
                    }
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                load:function(){
                    var self = this;
                    ajax.post('/installation/package/load', {
                        serviceTypeId:self.serviceTypeId
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.table.data.push(data[i]);
                            }
                        }
                    });
                },
                rowKey:function(row){
                    return 'installation-package-' + row.id;
                },
                handleCreate:function(){
                    var self = this;
                    self.dialog.addPackage.visible = true;
                },
                handleDelete:function(){
                    var self = this;
                },
                handleSelectFile:function(){
                    var self = this;
                    document.querySelector('#installation-package-file').click();
                },
                handleFileSelected:function(){
                    var self = this;
                    var file = document.querySelector('#installation-package-file').files[0];
                    var filename = file.name;
                    var index = filename.lastIndexOf('.');
                    var suffix = filename.substr(index+1);
                    if(suffix === 'zip'){
                        var info = filename.split('-');
                        self.dialog.addPackage.file = file;
                    }else{
                        self.$message({
                            type:'error',
                            message:'请选择压缩文件（.zip）'
                        });
                    }
                },
                handleAddPackageClose:function(){
                    var self = this;
                    self.dialog.addPackage.visible = false;
                    self.dialog.addPackage.loading = false;
                    self.dialog.addPackage.remark = '';
                    self.dialog.addPackage.creator = '';
                    self.dialog.addPackage.file = '';
                },
                handleAddPackageSubmit:function(){
                    var self = this;
                    self.dialog.addPackage.loading = true;
                    var params = new FormData();
                    params.append('serviceTypeId', self.serviceTypeId);
                    params.append('remark', self.dialog.addPackage.remark);
                    params.append('creator', self.dialog.addPackage.creator);
                    params.append('installationPackage', self.dialog.addPackage.file);
                    ajax.upload('/installation/package/add', params, function(data, status){
                        self.dialog.addPackage.loading = false;
                        if(status !== 200) return;
                        self.table.data.push(data);
                        self.handleAddPackageClose();
                    }, ajax.TOTAL_CATCH_CODE);
                },
                gotoPropertiesList:function(scope){
                    var self = this;
                    var row = scope.row;
                    window.location.hash = '#/page-omms-software-service-properties/' + row.id +'/'+self.serviceTypeId+'/'+self.serviceName +'/' + row.version;
                },
//                self.loading.menu = true;
                handleRowDelete:function(scope){
                    var self = this;
                    var row = scope.row;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'危险操作',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['此操作将永久删除安装包及属性，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/installation/package/remove', {
                                    id:row.id
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    if(status !== 200) return;
                                    for(var i=0; i<self.table.data.length; i++){
                                        if(self.table.data[i].id === row.id){
                                            self.table.data.splice(i, 1);
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
                self.load();
            }
        });

    };

    var destroy = function(){

    };

    var groupList = {
        path:'/' + pageId + '/:serviceTypeId/:serviceName',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});