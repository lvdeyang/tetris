/**
 * Created by lvdeyang on 2020/7/9.
 */
define([
    'text!' + window.APPPATH + 'tetris/business/terminal/hall/page-hall-terminal-binding.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'tetris/business/terminal/hall/page-hall-terminal-binding.css'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-hall-terminal-binding';

    var init = function(){

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el: '#' + pageId + '-wrapper',
            data: {
                menus: context.getProp('menus'),
                userInfo: context.getProp('user'),
                groups: context.getProp('groups'),
                hall:{
                    filter:{
                        name:''
                    },
                    props:{
                        label:'name'
                    },
                    data:[],
                    page:{
                        currentPage:1,
                        pageSize:0,
                        total:0,
                        pageSize:50
                    },
                    current:''
                },
                terminal:{
                    visible:false,
                    data:[],
                    props:{
                        label:'name'
                    }
                },
                terminalBundle:{
                    data:[],
                    props:{
                        label:'name'
                    },
                    current:''
                },
                bundle:{
                    id:'',
                    bundleId:'',
                    bundleType:'',
                    bundleName:''
                },
                dialog: {
                    createHall:{
                        visible:false,
                        loading:false,
                        name:'',
                        terminal:''
                    },
                    createHallBatch:{
                        visible:false,
                        loading:false,
                        terminal:''
                    },
                    selectSingleBundleTerminal:{
                        visible:false,
                        loading:false,
                        data:[],
                        props:{
                            label:'name'
                        },
                        current:''
                    },
                    editHall:{
                        visible:false,
                        loading:false,
                        id:'',
                        name:''
                    },
                    selectTerminal:{
                        visible:false,
                        data:[]
                    },
                    selectBundle:{
                        visible:false,
                        loading:false,
                        filter:{
                            bundleName:''
                        },
                        data:[],
                        page:{
                            currentPage:0,
                            total:0,
                            pageSize:20
                        },
                        currentRow:'',
                        selected:[]
                    }
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                loadHalls:function(currentPage){
                    var self = this;
                    var params = {
                        currentPage:currentPage,
                        pageSize:self.hall.page.pageSize
                    };
                    if(self.hall.filter.name){
                        params.name = self.hall.filter.name;
                    }
                    self.hall.data.splice(0, self.hall.data.length);
                    ajax.post('/tetris/bvc/business/conference/hall/load', params, function(data){
                        var total = data.total;
                        var rows = data.rows;
                        if(rows && rows.length>0){
                            for(var i=0; i<rows.length; i++){
                                self.hall.data.push(rows[i]);
                            }
                        }
                        self.hall.page.total = total;
                        self.hall.page.currentPage = currentPage;
                    });
                },
                loadTerminals:function(){
                    var self = this;
                    self.terminal.data.splice(0,  self.terminal.data.length);
                    ajax.post('/tetris/bvc/model/terminal/load/all', null, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.terminal.data.push(data[i]);
                            }
                        }
                    });
                },
                handleHallCurrentChange:function(currentPage){
                    var self = this;
                    self.loadHalls(currentPage);
                },
                handleHallCurrentNodeChange:function(currentNode){
                    var self = this;
                    self.hall.current = currentNode;
                    self.terminalBundle.data.splice(0, self.terminalBundle.data.length);
                    ajax.post('/tetris/bvc/model/terminal/bundle/load/all', {
                        terminalId:self.hall.current.terminalId
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.terminalBundle.data.push(data[i]);
                            }
                        }
                        self.terminalBundle.current = '';
                    });
                },
                handleCreateHall:function(){
                    var self = this;
                    self.dialog.createHall.visible = true;
                },
                handleCreateHallClose:function(){
                    var self = this;
                    self.dialog.createHall.visible = false;
                    self.dialog.createHall.loading = false;
                    self.dialog.createHall.name = '';
                    self.dialog.createHall.terminal = '';
                },
                handleCreateHallSubmit:function(){
                    var self = this;
                    if(!self.dialog.createHall.name){
                        self.$message({
                            type:'error',
                            message:'请输入名称'
                        });
                        return;
                    }
                    if(!self.dialog.createHall.terminal){
                        self.$message({
                            type:'error',
                            message:'请选择终端'
                        });
                        return;
                    }
                    self.dialog.createHall.loading = true;
                    ajax.post('/tetris/bvc/business/conference/hall/add', {
                        name:self.dialog.createHall.name,
                        terminalId:self.dialog.createHall.terminal.id
                    }, function(data, status){
                        self.dialog.createHall.loading = false;
                        if(status !== 200) return;
                        if(data){
                            self.hall.data.push(data);
                        }
                        self.handleCreateHallClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleCreateHallBatch:function(){
                    var self = this;
                    self.dialog.createHallBatch.visible = true;
                    self.loadBundles(1);
                },
                handleBundleSelected:function(selection){
                    var self = this;
                    console.log(selection);
                    self.dialog.selectBundle.selected.splice(0, self.dialog.selectBundle.selected.length);
                    if(selection.length > 0){
                        for(var i=0; i<selection.length; i++){
                            self.dialog.selectBundle.selected.push(selection[i]);
                        }
                    }
                },
                handleSelectSingleBundleTerminal:function(){
                    var self = this;
                    self.dialog.selectSingleBundleTerminal.visible = true;
                    self.dialog.selectSingleBundleTerminal.data.splice(0, self.dialog.selectSingleBundleTerminal.data.length);
                    ajax.post('/tetris/bvc/business/conference/hall/load/single/bundle/terminal', null, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.dialog.selectSingleBundleTerminal.data.push(data[i]);
                            }
                        }
                    });
                },
                handleSelectSingleBundleTerminalClose:function(){
                    var self = this;
                    var currentNode = self.$refs.singleBundleTerminalTree.getCurrentNode();
                    if(currentNode){
                        self.dialog.createHallBatch.terminal = currentNode;
                    }
                    self.dialog.selectSingleBundleTerminal.visible = false;
                },
                handleCreateHallBatchClose:function(){
                    var self = this;
                    self.dialog.createHallBatch.visible = false;

                    self.dialog.selectSingleBundleTerminal.visible = false;
                    self.dialog.selectSingleBundleTerminal.loading = false;
                    self.dialog.selectSingleBundleTerminal.data.splice(0,  self.dialog.selectSingleBundleTerminal.data.length);
                    self.dialog.selectSingleBundleTerminal.current = '';

                    self.dialog.selectBundle.filter.bundleName = '';
                    self.dialog.selectBundle.data.splice(0, self.dialog.selectBundle.data.length);
                    self.dialog.selectBundle.page.currentPage = 0;
                    self.dialog.selectBundle.page.total = 0;
                    self.dialog.selectBundle.page.currentPage = 0;
                    self.dialog.selectBundle.currentRow = '';
                    self.dialog.selectBundle.selected.splice(0, self.dialog.selectBundle.selected.length);
                },
                handleCreateHallBatchSubmit:function(){
                    var self = this;
                    if(!self.dialog.createHallBatch.terminal){
                        self.$message({
                            type:'error',
                            message:'请选择终端类型'
                        });
                        return;
                    }
                    if(self.dialog.selectBundle.selected.length <= 0){
                        self.$message({
                            type:'error',
                            message:'请勾选设备'
                        });
                        return;
                    }
                    ajax.post('/tetris/bvc/business/conference/hall/add/batch', {
                        terminalId:self.dialog.createHallBatch.terminal.id,
                        bundles:$.toJSON(self.dialog.selectBundle.selected)
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.hall.data.push(data[i]);
                            }
                            self.hall.page.total += data.length;
                        }
                        self.handleCreateHallBatchClose();
                    });
                },
                loadTerminals:function(){
                    var self = this;
                    self.terminal.data.splice(0,  self.terminal.data.length);
                    ajax.post('/tetris/bvc/model/terminal/load/all', null, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.terminal.data.push(data[i]);
                            }
                        }
                    });
                },
                handelSelectTerminal:function(){
                    var self = this;
                    self.terminal.visible = true;
                },
                handleTerminalClose:function(){
                    var self = this;
                    var node = self.$refs.terminalTree.getCurrentNode();
                    if(node){
                        self.dialog.createHall.terminal = node;
                    }
                    self.terminal.visible = false;
                },
                handleEditHall:function(scope){
                    var self = this;
                    var data = scope.data;
                    self.dialog.editHall.visible = true;
                    self.dialog.editHall.id = data.id;
                    self.dialog.editHall.name = data.name;
                },
                handleEditHallClose:function(){
                    var self = this;
                    self.dialog.editHall.visible = false;
                    self.dialog.editHall.loading = false;
                    self.dialog.editHall.id = '';
                    self.dialog.editHall.name = '';
                },
                handleEditHallSubmit:function(){
                    var self = this;
                    if(!self.dialog.editHall.name){
                        self.$message({
                            type:'error',
                            message:'请输入名称'
                        });
                        return;
                    }
                    self.dialog.editHall.loading = true;
                    ajax.post('/tetris/bvc/business/conference/hall/edit/name', {
                        id:self.dialog.editHall.id,
                        name:self.dialog.editHall.name
                    }, function(data, status){
                        self.dialog.editHall.loading = false;
                        if(status !== 200) return;
                        for(var i=0; i<self.hall.data.length; i++){
                            if(self.hall.data[i].id === data.id){
                                self.hall.data[i].name = data.name;
                                break;
                            }
                        }
                        self.handleEditHallClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleDeleteHall:function(scope){
                    var self = this;
                    var node = scope.data;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'危险操作',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['是否删除该会场?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/business/conference/hall/delete', {
                                    id:node.id
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    for(var i=0; i<self.hall.data.length; i++){
                                        if(self.hall.data[i].id === node.id){
                                            if(self.hall.current === self.hall.data[i]){
                                                self.hall.current = '';
                                            }
                                            self.$refs.hallTree.remove(self.hall.data[i]);
                                            self.hall.data.splice(i, 1);
                                            break;
                                        }
                                    }
                                }, null, ajax.TOTAL_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                handleTerminalBundleCurrentNodeChange:function(currentNode){
                    var self = this;
                    self.terminalBundle.current = currentNode;
                    ajax.post('/tetris/bvc/business/terminal/bundle/conference/hall/permission/load', {
                        conferenceHallId:self.hall.current.id,
                        terminalBundleId:self.terminalBundle.current.id
                    }, function(data){
                        if(data){
                            self.bundle.id = data.id;
                            self.bundle.bundleId = data.bundleId;
                            self.bundle.bundleType = data.bundleType;
                            self.bundle.bundleName = data.bundleName;
                        }else{
                            self.bundle.id = '';
                            self.bundle.bundleId = '';
                            self.bundle.bundleType = '';
                            self.bundle.bundleName = '';
                        }
                    });
                },
                handleSelectBundles:function(){
                    var self = this;
                    self.loadBundles(1);
                    self.dialog.selectBundle.visible = true;
                },
                loadBundles:function(currentPage){
                    var self = this;
                    self.dialog.selectBundle.data.splice(0,  self.dialog.selectBundle.data.length);
                    ajax.post('/tetris/bvc/business/terminal/bundle/user/permission/load/bundles', {
                        bundleName:self.dialog.selectBundle.filter.bundleName,
                        //deviceModel:self.terminalBundle.current.bundleType,
                        currentPage:currentPage,
                        pageSize:self.dialog.selectBundle.page.pageSize
                    }, function(data){
                        var rows = data.rows;
                        var total = data.total;
                        if(rows && rows.length>0){
                            for(var i=0; i<rows.length; i++){
                                self.dialog.selectBundle.data.push(rows[i]);
                            }
                        }
                        self.dialog.selectBundle.page.total = total;
                        self.dialog.selectBundle.page.currentPage = currentPage;
                    });
                },
                handleSelectBundleClose:function(){
                    var self = this;
                    self.dialog.selectBundle.data.splice(0, self.dialog.selectBundle.data.length);
                    self.dialog.selectBundle.filter.bundleName = '';
                    self.dialog.selectBundle.currentRow = '';
                    self.dialog.selectBundle.visible = false;
                    self.dialog.selectBundle.loading = false;
                },
                handleBundleCurrentChange:function(currentPage){
                    var self = this;
                    self.loadBundles(currentPage);
                },
                currentBundleChange:function(currentRow){
                    var self = this;
                    self.dialog.selectBundle.currentRow = currentRow;
                },
                handleSelectBundleSubmit:function(){
                    var self = this;
                    if(!self.dialog.selectBundle.currentRow){
                        self.$message({
                            type:'error',
                            message:'您未选择设备'
                        });
                        return;
                    }
                    self.dialog.selectBundle.loading = true;
                    if(self.bundle.id){
                        ajax.post('/tetris/bvc/business/terminal/bundle/conference/hall/permission/edit', {
                            id:self.bundle.id,
                            bundleType:self.dialog.selectBundle.currentRow.bundleType,
                            bundleId:self.dialog.selectBundle.currentRow.bundleId,
                            bundleName:self.dialog.selectBundle.currentRow.bundleName
                        }, function(data, status){
                            self.dialog.selectBundle.loading = false;
                            if(status !== 200) return;
                            self.bundle.id = data.id;
                            self.bundle.bundleId = data.bundleId;
                            self.bundle.bundleType = data.bundleType;
                            self.bundle.bundleName = data.bundleName;
                            self.handleSelectBundleClose();
                        }, null, ajax.TOTAL_CATCH_CODE);
                    }else{
                        ajax.post('/tetris/bvc/business/terminal/bundle/conference/hall/permission/add', {
                            conferenceHallId:self.hall.current.id,
                            terminalBundleId:self.terminalBundle.current.id,
                            bundleType:self.dialog.selectBundle.currentRow.bundleType,
                            bundleId:self.dialog.selectBundle.currentRow.bundleId,
                            bundleName:self.dialog.selectBundle.currentRow.bundleName
                        }, function(data, status){
                            self.dialog.selectBundle.loading = false;
                            if(status !== 200) return;
                            self.bundle.id = data.id;
                            self.bundle.bundleId = data.bundleId;
                            self.bundle.bundleType = data.bundleType;
                            self.bundle.bundleName = data.bundleName;
                            self.handleSelectBundleClose();
                        }, null, ajax.TOTAL_CATCH_CODE);
                    }
                },
                clearBundle:function(){
                    var self = this;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'危险操作',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['是否清除该设备绑定?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/business/terminal/bundle/conference/hall/permission/delete', {
                                    id:self.bundle.id
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    self.bundle.id = '';
                                    self.bundle.bundleId = '';
                                    self.bundle.bundleType = '';
                                    self.bundle.bundleName = '';
                                }, null, ajax.TOTAL_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                }
            },
            created:function(){
                var self = this;
                self.loadHalls(1);
                self.loadTerminals();
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