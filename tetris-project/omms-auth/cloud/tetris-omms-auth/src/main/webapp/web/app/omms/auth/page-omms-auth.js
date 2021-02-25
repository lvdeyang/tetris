/**
 * Created by lvdeyang on 2018/12/24 0024.
 */
define([
    'text!' + window.APPPATH + 'omms/auth/page-omms-auth.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'date',
    'css!' + window.APPPATH + 'omms/auth/page-omms-auth.css'
], function(tpl, config, $, ajax, context, commons, Vue){

    var pageId = 'page-omms-auth';

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
                        pageSize:10,
                        pageSizes:[10,50, 100, 500, 1000],
                        total:0
                    }
                },
                dialog:{
                    addAuth:{
                        visible:false,
                        name:''
                    },
                    importDevice:{
                        visible:false,
                        loading:false,
                        id:0,
                        file:''
                    },
                    setAuth:{
                        visible:false,
                        loading:false,
                        id:0,
                        deviceId:'',
                        bvc_business:{
                            support:false,
                            time:'',
                            proc_list:["bvc_business"]
                        },
                        sts:{
                            support:false,
                            serverNum:0,
                            time:'',
                            proc_list:["sts"]
                        },
                        capacity:{
                            support:false,
                            time:'',
                        	v4k:0,
                        	v1080:0,
                        	v720:0,
                            proc_list:["capacity"]
                        },
                        JV210Joiner:{
                            support:false,
                            time:'',
                        	serverNum:0,
                            proc_list:["JV210Joiner"]
                        },
                        CDNJoiner:{
                            support:false,
                            time:'',
                            cdnNum:0,
                            proc_list:["CDNJoiner"]
                        },
                        MixerJoiner:{
                            support:false,
                            time:'',
                            mixerNum:0,
                            proc_list:["MixerJoiner"]
                        },
                        xStream2050:{
                            support:false,
                            time:'',
                            proc_list:[]
                        },
                        xStream2310:{
                            support:false,
                            time:'',
                            proc_list:[]
                        },
                    },
                },
            },
            computed:{

            },
            watch:{

            },
            methods:{
                handleChange:function(value) {
                    console.log(value);
                },
                load:function(currentPage){
                    var self = this;
                    self.table.data.splice(0, self.table.data.length);
                    ajax.post('/auth/load', {
                        currentPage:currentPage,
                        pageSize:self.table.page.pageSize
                    }, function(data){
                        var total = data.total;
                        var rows = data.rows;
                        if(rows && rows.length>0){
                            for(var i=0; i<rows.length; i++){
                                self.table.data.push(rows[i]);
                            }
                        }
                        self.table.page.currentPage = currentPage;
                        self.table.page.total = total;
                    });
                },
                handleCreate:function(){
                    var self = this;
                    self.dialog.addAuth.visible = true;
                },
                handleAddAuthClose:function(){
                    var self = this;
                    self.dialog.addAuth.visible = false;
                    self.dialog.addAuth.name = '';
                },
                handleAddAuthSubmit:function(){
                    var self = this;
                    ajax.post('/auth/add',{
                        name:self.dialog.addAuth.name
                    }, function(data){
                        self.table.data.splice(0, 0, data);
                        self.table.page.total += 1;
                        self.handleAddAuthClose();
                    });
                },
                handleDelete:function(){
                    var self = this;
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
                                h('p', null, ['此操作将永久删除该记录，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/auth/delete', {
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
                                    self.table.page.total -= 1;
                                    if(self.table.total>0 && self.table.data.length===0){
                                        self.load(self.table.page.currentPage - 1);
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
                handleImportDevice:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.importDevice.visible = true;
                    self.dialog.importDevice.id=row.id;
                },
                handleSelectFile:function(){
                    var self = this;
                    document.querySelector('#device').click();
                },
                handleFileSelected:function(){
                    var self = this;
                    var file = document.querySelector('#device').files[0];
                    self.dialog.importDevice.file = file;
                
                },
                handleImportDeviceClose:function(){
                    var self = this;
                    self.dialog.importDevice.visible = false;
                    self.dialog.importDevice.loading = false;
                    self.dialog.importDevice.id = 0;
                    self.dialog.importDevice.file = '';
                },
                handleImportDeviceSubmit:function(){
                    var self = this;
                    self.dialog.importDevice.loading = true;
                    var params = new FormData();
                    params.append('id', self.dialog.importDevice.id);
                    params.append('deviceFile', self.dialog.importDevice.file);
                    ajax.upload('/auth/importdev', params, function(data, status){
                        self.dialog.importDevice.loading = false;
                        if(status !== 200) return;
                        self.load(self.table.page.currentPage);
                        self.handleImportDeviceClose();
                    }, ajax.TOTAL_CATCH_CODE);
                },
                handleSetAuth:function(scope){
                	 var self = this;
                     var row = scope.row;
                     self.dialog.setAuth.visible = true;
                     self.dialog.setAuth.id=row.id;
                     self.dialog.setAuth.deviceId=row.deviceId;
                     if(row.content!==null){
                    	 var contentJson=JSON.parse(row.content);
                         self.dialog.setAuth.bvc_business=contentJson.bvc_business;
                         self.dialog.setAuth.sts=contentJson.sts;
                         self.dialog.setAuth.capacity=contentJson.capacity;
                         self.dialog.setAuth.JV210Joiner=contentJson.JV210Joiner;
                         self.dialog.setAuth.CDNJoiner=contentJson.CDNJoiner;
                         self.dialog.setAuth.MixerJoiner=contentJson.MixerJoiner;
                         self.dialog.setAuth.xStream2050=contentJson.xStream2050;
                         self.dialog.setAuth.xStream2310=contentJson.xStream2310;
                         
                     }   
                },
                handleSetAuthClose:function(){
                    var self = this;

                    self.dialog.setAuth.visible = false;
                    self.dialog.setAuth.loading = false;
                    self.dialog.setAuth.id = 0;
                    self.dialog.setAuth.deviceId = '';
                    self.dialog.setAuth.bvc_business.support = false;
                    self.dialog.setAuth.bvc_businesstime = '';
                    self.dialog.setAuth.sts.support=false;
                    self.dialog.setAuth.sts.serverNum=0;
                    self.dialog.setAuth.sts.time='';
                    self.dialog.setAuth.capacity.support=false;
                    self.dialog.setAuth.capacity.time='';
                    self.dialog.setAuth.capacity.v4k=0;
                    self.dialog.setAuth.capacity.v1080=0;
                    self.dialog.setAuth.capacity.v720=0;
                    self.dialog.setAuth.JV210Joiner.support=false;
                    self.dialog.setAuth.JV210Joiner.time='';
                    self.dialog.setAuth.JV210Joiner.serverNum=0;
                    self.dialog.setAuth.CDNJoiner.support=false;
                    self.dialog.setAuth.CDNJoiner.time='';
                    self.dialog.setAuth.CDNJoiner.cdnNum=0;
                    self.dialog.setAuth.MixerJoiner.support=false;
                    self.dialog.setAuth.MixerJoiner.time='';
                    self.dialog.setAuth.MixerJoiner.mixerNum=0;
                    self.dialog.setAuth.xStream2050.support=false;
                    self.dialog.setAuth.xStream2050.time='';
                    self.dialog.setAuth.xStream2050.proc_list.splice(0,self.dialog.setAuth.xStream2050.proc_list.length);
                    self.dialog.setAuth.xStream2310.support=false;
                    self.dialog.setAuth.xStream2310.time='';
                    self.dialog.setAuth.xStream2310.proc_list.splice(0,self.dialog.setAuth.xStream2310.proc_list.length);

                },
                handleSetAuthSubmit:function(){
                    var self = this;
                    self.dialog.importDevice.loading = true;
                    if (self.dialog.setAuth.bvc_business.support && self.dialog.setAuth.bvc_business.time === '' ) {
                        self.$message({
                            'type': 'warning',
                            'message': "请填写完整授权时间段！"

                        })
                        return
                    }else if(self.dialog.setAuth.sts.support && self.dialog.setAuth.sts.time === ''){
                        self.$message({
                            'type': 'warning',
                            'message': "请填写完整授权时间段！"

                        })
                        return
                    }else if(self.dialog.setAuth.capacity.support && self.dialog.setAuth.capacity.time === ''){
                        self.$message({
                            'type': 'warning',
                            'message': "请填写完整授权时间段！"

                        })
                        return
                    }else if(self.dialog.setAuth.JV210Joiner.support && self.dialog.setAuth.JV210Joiner.time === ''){
                        self.$message({
                            'type': 'warning',
                            'message': "请填写完整授权时间段！"

                        })
                        return
                    }else if(self.dialog.setAuth.CDNJoiner.support && self.dialog.setAuth.CDNJoiner.time === ''){
                        self.$message({
                            'type': 'warning',
                            'message': "请填写完整授权时间段！"

                        })
                        return
                    }else if(self.dialog.setAuth.MixerJoiner.support && self.dialog.setAuth.MixerJoiner.time === ''){
                        self.$message({
                            'type': 'warning',
                            'message': "请填写完整授权时间段！"

                        })
                        return
                    }else if(self.dialog.setAuth.xStream2050.support && self.dialog.setAuth.xStream2050.time === ''){
                        self.$message({
                            'type': 'warning',
                            'message': "请填写完整授权时间段！"

                        })
                        return
                    }else if(self.dialog.setAuth.xStream2310.support && self.dialog.setAuth.xStream2310.time === ''){
                        self.$message({
                            'type': 'warning',
                            'message': "请填写完整授权时间段！"

                        })
                        return
                    }

                    var contentJson={};
                    contentJson.deviceId=self.dialog.setAuth.deviceId;
                    contentJson.bvc_business=self.dialog.setAuth.bvc_business;
                    contentJson.sts=self.dialog.setAuth.sts;
                    contentJson.capacity=self.dialog.setAuth.capacity;
                    contentJson.JV210Joiner=self.dialog.setAuth.JV210Joiner;
                    contentJson.CDNJoiner=self.dialog.setAuth.CDNJoiner;
                    contentJson.MixerJoiner=self.dialog.setAuth.MixerJoiner;
                    contentJson.xStream2050=self.dialog.setAuth.xStream2050;
                    contentJson.xStream2310=self.dialog.setAuth.xStream2310;
                   
                    var params = {};
                    params.id=self.dialog.setAuth.id;
                    params.content=JSON.stringify(contentJson);
                   
                    ajax.post('/auth/set',params, function(data){
                    	self.dialog.setAuth.loading = false;
                        self.load(self.table.page.currentPage);
                        self.handleSetAuthClose();
                    });
                },
                exportAuth:function(scope){
                	var self = this;
                    var row = scope.row;
                    ajax.download('/auth/export/'+row.id, null, function (data) {
                        var $a = $('#page-export');
                        $a[0].download = 'auth('+row.name+').ini';
                        $a[0].href = window.URL.createObjectURL(data);
                        $a[0].click();
                        self.$message({
                          type: 'success',
                          message: '操作成功'
                        });
                    });
                },
                handleSizeChange:function(pageSize){
                    var self = this;
                    self.table.page.pageSize = pageSize;
                    self.load(1);
                },
                handleCurrentChange:function(currentPage){
                    var self = this;
                    self.load(currentPage);
                },
                rowKey:function(row){
                    return row.id;
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