/**
 * Created by lzp on 2019/7/25.
 */
define([
    'text!' + window.APPPATH + 'system-storage/page-system-storage.html',
    window.APPPATH + 'system-storage/page-system-storage.i18n',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'system-storage/page-system-storage.css'
], function (tpl, i18n, config, $, ajax, context, commons, Vue) {

    var locale = context.getProp('locale');
    var i18n = !locale?i18n.default:i18n[locale]?i18n[locale]:i18n.default;

    var pageId = 'page-system-storage';

    var init = function () {

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
                i18n:i18n,
                table:{
                    rows:[],
                    currentPage:0,
                    pageSizes:[50, 100, 500, 1000],
                    pageSize:50,
                    total:0
                },
                dialog:{
                    createStorage:{
                        visible:false,
                        name:'',
                        rootPath:'/usr/sbin/sumavision/tetris/resources',
                        upload:{
                            protocol:'ftp://',
                            ip:'127.0.0.1',
                            port:'21'
                        },
                        preview:{
                            protocol:'http://',
                            ip:'127.0.0.1',
                            port:'80'
                        },
                        serverGadgetType:'SUMAVISION_GADGET',
                        control:{
                            protocol:'http://',
                            ip:'127.0.0.1',
                            port:'80'
                        },
                        remark:'',
                        loading:false
                    },
                    editStorage:{
                        visible:false,
                        id:'',
                        name:'',
                        rootPath:'',
                        upload:{
                            protocol:'',
                            ip:'',
                            port:''
                        },
                        preview:{
                            protocol:'',
                            ip:'',
                            port:''
                        },
                        serverGadgetType:'',
                        control:{
                            protocol:'',
                            ip:'',
                            port:''
                        },
                        remark:'',
                        loading:false
                    }
                }
            },
            computed: {},
            watch: {},
            methods: {
                load:function(currentPage){
                    var self = this;
                    ajax.post('/system/storage/load', {
                        currentPage:currentPage,
                        pageSize:self.table.pageSize
                    }, function(data){
                        var total = data.total;
                        var rows = data.rows;
                        if(rows && rows.length>0){
                            for(var i=0; i<rows.length; i++){
                                self.table.rows.push(rows[i]);
                            }
                            self.table.total = total;
                        }
                    });
                },
                rowKey:function(row){
                    return row.id;
                },
                handleCreate:function(){
                    var self = this;
                    self.dialog.createStorage.visible = true;
                },
                handleDelete:function(){

                },
                handleSizeChange:function(pageSize){
                    var self = this;
                    self.table.pageSize = pageSize;
                    self.load(1);
                },
                handleCurrentChange:function(currentPage){
                    var self = this;
                    self.load(currentPage);
                },
                handleRowEdit:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.editStorage.id = row.id;
                    self.dialog.editStorage.name = row.name;
                    self.dialog.editStorage.rootPath = row.rootPath;
                    self.dialog.editStorage.upload.protocol = row.uploadProtocol;
                    self.dialog.editStorage.upload.ip = row.uploadIp;
                    self.dialog.editStorage.upload.port = row.uploadPort;
                    self.dialog.editStorage.preview.protocol = row.previewProtocol;
                    self.dialog.editStorage.preview.ip = row.previewIp;
                    self.dialog.editStorage.preview.port = row.previewPort;
                    self.dialog.editStorage.serverGadgetType = row.serverGadgetType;
                    self.dialog.editStorage.control.protocol = row.controlProtocol;
                    self.dialog.editStorage.control.ip = row.controlIp;
                    self.dialog.editStorage.control.port = row.controlPort;
                    self.dialog.editStorage.remark = row.remark;
                    self.dialog.editStorage.visible = true;
                },
                handleRowConfig:function(scope){
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
                                h('p', null, ['此操作将永久删除存储以及分区信息，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/system/storage/remove/'+row.id, null, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    if(status !== 200) return;
                                    for(var i=0; i<self.table.rows.length; i++){
                                        if(self.table.rows[i].id === row.id){
                                            self.table.rows.splice(i, 1);
                                            break;
                                        }
                                    }
                                    self.table.total -= 1;
                                    if(self.table.total === 0){
                                        self.table.currentPage -= 1;
                                        if(self.table.currentPage > 0){
                                            self.load(self.table.currentPage);
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
                handleCreateStorageClose:function(){
                    var self = this;
                    self.dialog.createStorage.visible = false;
                    self.dialog.createStorage.name = '';
                    self.dialog.createStorage.rootPath = '/usr/sbin/sumavision/tetris/resources';
                    self.dialog.createStorage.upload.protocol = 'ftp://';
                    self.dialog.createStorage.upload.ip = '127.0.0.1';
                    self.dialog.createStorage.upload.port = '21';
                    self.dialog.createStorage.preview.protocol = 'http://';
                    self.dialog.createStorage.preview.ip = '127.0.0.1';
                    self.dialog.createStorage.preview.port = '80';
                    self.dialog.createStorage.serverGadgetType = 'SUMAVISION_GADGET';
                    self.dialog.createStorage.control.protocol = 'http://';
                    self.dialog.createStorage.control.ip = '127.0.0.1';
                    self.dialog.createStorage.control.port = '80';
                    self.dialog.createStorage.remark = '';
                },
                handleCreateStorageCommit:function(){
                    var self = this;
                    self.dialog.createStorage.loading = true;
                    ajax.post('/system/storage/add', {
                        name:self.dialog.createStorage.name,
                        rootPath:self.dialog.createStorage.rootPath,
                        baseFtpPath:self.dialog.createStorage.upload.protocol+self.dialog.createStorage.upload.ip+':'+self.dialog.createStorage.upload.port+'/',
                        basePreviewPath:self.dialog.createStorage.preview.protocol+self.dialog.createStorage.preview.ip+':'+self.dialog.createStorage.preview.port+'/',
                        gadgetBasePath:self.dialog.createStorage.control.protocol+self.dialog.createStorage.control.ip+':'+self.dialog.createStorage.control.port+'/',
                        serverGadgetType:self.dialog.createStorage.serverGadgetType,
                        remark:self.dialog.createStorage.remark
                    }, function(data, status){
                        self.dialog.createStorage.loading = false;
                        if(status !== 200) return;
                        self.table.rows.push(data);
                        self.table.total += 1;
                        if(self.table.currentPage === 0) self.table.currentPage = 1;
                        self.handleCreateStorageClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                handleEditStorageClose:function(){
                    var self = this;
                    self.dialog.editStorage.id = '';
                    self.dialog.editStorage.name = '';
                    self.dialog.editStorage.rootPath = '';
                    self.dialog.editStorage.upload.protocol = '';
                    self.dialog.editStorage.upload.ip = '';
                    self.dialog.editStorage.upload.port = '';
                    self.dialog.editStorage.preview.protocol = '';
                    self.dialog.editStorage.preview.ip = '';
                    self.dialog.editStorage.preview.port = '';
                    self.dialog.editStorage.serverGadgetType = '';
                    self.dialog.editStorage.control.protocol = '';
                    self.dialog.editStorage.control.ip = '';
                    self.dialog.editStorage.control.port = '';
                    self.dialog.editStorage.remark = '';
                    self.dialog.editStorage.visible = false;
                },
                handleEditStorageCommit:function(){
                    var self = this;
                    self.dialog.editStorage.loading = true;
                    ajax.post('/system/storage/edit/'+self.dialog.editStorage.id, {
                        name:self.dialog.editStorage.name,
                        rootPath:self.dialog.editStorage.rootPath,
                        baseFtpPath:self.dialog.editStorage.upload.protocol+self.dialog.editStorage.upload.ip+':'+self.dialog.editStorage.upload.port+'/',
                        basePreviewPath:self.dialog.editStorage.preview.protocol+self.dialog.editStorage.preview.ip+':'+self.dialog.editStorage.preview.port+'/',
                        gadgetBasePath:self.dialog.editStorage.control.protocol+self.dialog.editStorage.control.ip+':'+self.dialog.editStorage.control.port+'/',
                        serverGadgetType:self.dialog.editStorage.serverGadgetType,
                        remark:self.dialog.editStorage.remark
                    }, function(data, status){
                        self.dialog.editStorage.loading = false;
                        if(status !== 200) return;
                        for(var i=0; i<self.table.rows.length; i++){
                            if(self.table.rows[i].id === data.id){
                                self.table.rows.splice(i, 1, data);
                                break;
                            }
                        }
                        self.handleEditStorageClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                }
            },
            created: function () {
                var self = this;
                self.load(1);
            },
            mounted: function () {

            }
        });

    };

    var destroy = function () {

    };

    var groupList = {
        path: '/' + pageId,
        component: {
            template: '<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init: init,
        destroy: destroy
    };

    return groupList;
});