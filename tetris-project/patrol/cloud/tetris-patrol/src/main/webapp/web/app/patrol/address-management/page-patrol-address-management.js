/**
 * Created by lvdeyang on 2018/12/24 0024.
 */
define([
    'text!' + window.APPPATH + 'patrol/address-management/page-patrol-address-management.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'date',
    'jquery-qrcode',
    'css!' + window.APPPATH + 'patrol/address-management/page-patrol-address-management.css'
], function(tpl, config, $, ajax, context, commons, Vue){

    var pageId = 'page-patrol-address-management';

    var charts = {};

    var init = function(){

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
                table:{
                    data:[],
                    page:{
                        currentPage:0,
                        pageSizes:[20, 50, 100, 1000],
                        pageSize:20,
                        total:0
                    }
                },
                dialog:{
                    addAddress:{
                        visible:false,
                        loading:false,
                        name:''
                    },
                    editAddress:{
                        visible:false,
                        loading:false,
                        id:'',
                        name:''
                    },
                    qrCode:{
                        visible:false,
                        name:'',
                        url:''
                    }
                },
                condition:{
                    name:''
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                load:function(currentPage){
                    var self = this;
                    self.table.data.splice(0, self.table.data.length);
                    ajax.post('/address/load', {
                        name:self.condition.name?self.condition.name:null,
                        currentPage:currentPage,
                        pageSize:self.table.page.pageSize
                    }, function(data){
                        var total = data.total;
                        var rows = data.rows;
                        if(rows && rows.length>0){
                            for(var i=0; i<rows.length; i++){
                                self.table.data.push(rows[i]);
                            }
                            self.table.page.total = total;
                            self.table.page.currentPage = currentPage;
                        }
                    });
                },
                handleCreate:function(){
                    var self = this;
                    self.dialog.addAddress.visible = true;
                },
                handleAddAddressClose:function(){
                    var self = this;
                    self.dialog.addAddress.visible = false;
                    self.dialog.addAddress.loading = false;
                    self.dialog.addAddress.name = '';
                },
                handleAddAddressCommit:function(){
                    var self = this;
                    self.dialog.addAddress.loading = true;
                    ajax.post('/address/add', {
                        name:self.dialog.addAddress.name
                    }, function(data, status){
                        self.dialog.addAddress.loading = false;
                        if(status !== 200){
                            return;
                        }
                        self.table.data.push(data);
                        self.table.page.total += 1;
                        self.handleAddAddressClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                rowKey:function(row){
                    var self = this;
                    return 'address-' + row.id;
                },
                handleGenerateQrCod:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.qrCode.visible = true;
                    self.dialog.qrCode.name = row.name;
                    self.dialog.qrCode.url = row.url;
                    self.$nextTick(function(){
                        $('#qr-code-container').qrcode(row.url);
                    });
                },
                handleQrCodeClose:function(){
                    var self = this;
                    self.dialog.qrCode.visible = false;
                    self.dialog.qrCode.name = '';
                    self.dialog.qrCode.url = '';
                    $('#qr-code-container').empty();
                },
                handleRowEdit:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.editAddress.id = row.id;
                    self.dialog.editAddress.name = row.name;
                    self.dialog.editAddress.visible = true;
                },
                handleEditAddressClose:function(){
                    var self = this;
                    self.dialog.editAddress.id = '';
                    self.dialog.editAddress.name = '';
                    self.dialog.editAddress.visible = false;
                    self.dialog.editAddress.loading = false;
                },
                handleEditAddressCommit:function(){
                    var self = this;
                    self.dialog.editAddress.loading = true;
                    ajax.post('/address/edit', {
                        id:self.dialog.editAddress.id,
                        name:self.dialog.editAddress.name
                    }, function(data, status){
                        self.dialog.editAddress.loading = false;
                        if(status !== 200) return;
                        for(var i=0; i<self.table.data.length; i++){
                            if(self.table.data[i].id === data.id){
                                self.table.data.splice(i, 1, data);
                                break;
                            }
                        }
                        self.handleEditAddressClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
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
                                h('p', null, ['是否要删除该地址下的所有签到信息?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '是',
                        cancelButtonText: '否',
                        beforeClose:function(action, instance, done){
                            var deleteSigns = false;
                            if(action === 'confirm'){
                                deleteSigns = true;
                                instance.confirmButtonLoading = true;
                            }else{
                                instance.cancelButtonLoading = true;
                            }
                            ajax.post('/address/delete', {
                                id:row.id,
                                deleteSigns:deleteSigns
                            }, function(data, status){
                                instance.confirmButtonLoading = false;
                                instance.cancelButtonLoading = false;
                                if(status !== 200){
                                    return;
                                }
                                for(var i=0; i<self.table.data.length; i++){
                                    if(self.table.data[i].id == row.id){
                                        self.table.data.splice(i, 1);
                                        self.table.page.total -= 1;
                                        break;
                                    }
                                }
                                if(self.table.data.length===0 && self.table.page.total>0){
                                    self.load(self.table.page.currentPage-1);
                                }
                                done();
                            }, null, ajax.TOTAL_CATCH_CODE);
                        }
                    }).catch(function(){});

                },
                handleSizeChange:function(pageSize){
                    var self = this;
                    self.table.page.pageSize = pageSize;
                    self.load(table.page.currentPage?table.page.currentPage:1);
                },
                handleCurrentChange:function(currentPage){
                    var self = this;
                    self.load(currentPage);
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
        path:'/' + pageId,
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});