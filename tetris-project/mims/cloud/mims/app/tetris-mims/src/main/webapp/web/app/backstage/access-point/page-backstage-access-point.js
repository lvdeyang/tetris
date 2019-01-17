/**
 * Created by lvdeyang on 2018/12/18 0018.
 */
define([
    'text!' + window.APPPATH + 'backstage/access-point/page-backstage-access-point.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-backstage-access-point';

    var init = function(p){

        var serviceId = p.serviceId;
        var serviceName = p.serviceName;
        var serviceType = p.serviceType;

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el: '#' + pageId + '-wrapper',
            data: {
                activeId:'page-backstage-service-rest',
                menus: context.getProp('menus'),
                user: context.getProp('user'),
                groups: context.getProp('groups'),
                serviceName:serviceName,
                accessPointTypes:'',
                table:{
                    rows:[],
                    pageSize:50,
                    currentPage:0,
                    total:0
                },
                dialog:{
                    createAccessPoint:{
                        visible:false,
                        scope:'系统域',
                        name:'',
                        type:'',
                        method:'',
                        remarks:'',
                        loading:false
                    },
                    editAccessPoint:{
                        visible:false,
                        id:'',
                        uuid:'',
                        name:'',
                        type:'',
                        method:'',
                        remarks:'',
                        loading:false
                    }
                }
            },
            methods:{
                rowKey:function(row){
                    return 'access-point-' + row.uuid;
                },
                gotoService:function(){
                    var self = this;
                    if(serviceType === 'REST'){
                        return '#/page-backstage-service-rest';
                    }
                },
                handleCreate:function(){
                    var self = this;
                    self.dialog.createAccessPoint.visible = true;
                },
                handleCreateAccessPointClose:function(){
                    var self = this;
                    self.dialog.createAccessPoint.name = '';
                    self.dialog.createAccessPoint.type = '';
                    self.dialog.createAccessPoint.method = '';
                    self.dialog.createAccessPoint.remarks = '';
                    self.dialog.createAccessPoint.loading = false;
                    self.dialog.createAccessPoint.visible = false;
                },
                handleCreateAccessPointSubmit:function(){
                    var self = this;
                    self.dialog.createAccessPoint.loading = true;
                    ajax.post('/access/point/add', {
                        serviceId:serviceId,
                        serviceType:serviceType,
                        scope:self.dialog.createAccessPoint.scope,
                        name:self.dialog.createAccessPoint.name,
                        type:self.dialog.createAccessPoint.type,
                        method:self.dialog.createAccessPoint.method,
                        remarks:self.dialog.createAccessPoint.remarks
                    }, function(data, status){
                        self.dialog.createAccessPoint.loading = false;
                        if(status !== 200) return;
                        self.table.rows.push(data);
                        self.handleCreateAccessPointClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                handleDelete:function(){

                },
                handleCurrentChange:function(currentPage){
                    var self = this;
                    self.load(currentPage);
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
                                h('p', null, ['此操作将永久删除该接口，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/access/point/delete/' + row.id, null, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    if(status !== 200) return;
                                    for(var i=0; i<self.table.rows.length; i++){
                                        if(self.table.rows[i].id === row.id){
                                            self.table.rows.splice(i, 1);
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
                handleRowEdit:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.editAccessPoint.id = row.id;
                    self.dialog.editAccessPoint.uuid = row.uuid;
                    self.dialog.editAccessPoint.name = row.name;
                    self.dialog.editAccessPoint.type = row.type;
                    self.dialog.editAccessPoint.method = row.method;
                    self.dialog.editAccessPoint.remarks = row.remarks;
                    self.dialog.editAccessPoint.visible = true;
                },
                handleEditAccessPointClose:function(){
                    var self = this;
                    self.dialog.editAccessPoint.id = '';
                    self.dialog.editAccessPoint.uuid = '';
                    self.dialog.editAccessPoint.name = '';
                    self.dialog.editAccessPoint.type = '';
                    self.dialog.editAccessPoint.method = '';
                    self.dialog.editAccessPoint.remarks = '';
                    self.dialog.editAccessPoint.visible = false;
                    self.dialog.editAccessPoint.loading = false;
                },
                handleEditAccessPointSubmit:function(){
                    var self = this;
                    self.dialog.editAccessPoint.loading = true;
                    ajax.post('/access/point/edit/' + self.dialog.editAccessPoint.id, {
                        name:self.dialog.editAccessPoint.name,
                        type:self.dialog.editAccessPoint.type,
                        method:self.dialog.editAccessPoint.method,
                        remarks:self.dialog.editAccessPoint.remarks
                    }, function(data, status){
                        self.dialog.editAccessPoint.loading = false;
                        if(status !== 200) return;
                        for(var i=0; i<self.table.rows.length; i++){
                            var row = self.table.rows[i];
                            if(row.id === self.dialog.editAccessPoint.id){
                                row.name = self.dialog.editAccessPoint.name;
                                row.type = self.dialog.editAccessPoint.type;
                                row.method = self.dialog.editAccessPoint.method;
                                row.remarks = self.dialog.editAccessPoint.remarks;
                                break;
                            }
                        }
                        self.handleEditAccessPointClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                handleSubParams:function(scope){
                    var self = this;
                    var row = scope.row;
                    window.location.hash = '#/page-backstage-access-point-param/' + serviceId + '/' + serviceName + '/' + serviceType + '/' + row.id + '/' + row.name;
                },
                handleSubConstraints:function(scope){
                    var self = this;
                    var row = scope.row;
                    window.location.hash = '#/page-backstage-joint-constraint-expression/' + serviceId + '/' + serviceName + '/' + serviceType + '/' + row.id + '/' + row.name;
                },
                load:function(currentPage){
                    var self = this;
                    self.table.rows.splice(0, self.table.rows.length);
                    ajax.post('/access/point/list', {
                        serviceId:serviceId,
                        serviceType:serviceType,
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
                            self.table.currentPage = currentPage;
                        }
                    });
                }
            },
            created:function(){
                var self = this;
                ajax.post('/access/point/query/all/types', null, function(data){
                    self.accessPointTypes = data;
                });
                self.load(1);
            }
        });

    };

    var destroy = function(){

    };

    var groupList = {
        path:'/' + pageId + '/:serviceId/:serviceName/:serviceType',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});