/**
 * Created by lvdeyang on 2020/7/9.
 */
define([
    'text!' + window.APPPATH + 'tetris/business/terminal/user/page-user-terminal-binding.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'tetris/business/terminal/user/page-user-terminal-binding.css'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-user-terminal-binding';

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
                user:{
                    filter:{
                        nickname:'',
                        userno:''
                    },
                    props:{
                        label:'nickname'
                    },
                    data:[],
                    page:{
                        currentPage:0,
                        pageSize:0,
                        total:0,
                        pageSize:50
                    },
                    current:''
                },
                terminal:{
                    data:[],
                    props:{
                        label:'name'
                    },
                    current:''
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
                    selectBundle:{
                        visible:false,
                        loading:false,
                        filter:{
                            bundleName:''
                        },
                        data:[],
                        page:{
                            currentPage:0,
                            pageSize:0,
                            total:0,
                            pageSize:20
                        },
                        currentRow:''
                    }
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                loadUsers:function(currentPage){
                    var self = this;
                    var params = {
                        currentPage:currentPage,
                        pageSize:self.user.page.pageSize
                    };
                    if(self.user.filter.nickname){
                        params.nickname = self.user.filter.nickname;
                    }
                    if(self.user.filter.userno){
                        params.userno = self.user.filter.userno;
                    }
                    self.user.data.splice(0, self.user.data.length);
                    ajax.post('/tetris/bvc/business/terminal/bundle/user/permission/load/users', params, function(data){
                        var total = data.total;
                        var rows = data.rows;
                        if(rows && rows.length>0){
                            for(var i=0; i<rows.length; i++){
                                self.user.data.push(rows[i]);
                            }
                        }
                        self.user.page.total = total;
                        self.user.page.currentPage = currentPage;
                    });
                },
                handleUserCurrentChange:function(currentPage){
                    var self = this;
                    self.loadUsers(currentPage);
                },
                handleUserCurrentNodeChange:function(currentNode){
                    var self = this;
                    if(self.user.current === currentNode) return;
                    self.user.current = currentNode;
                    self.terminal.current = '';
                    if(self.$refs.terminalTree) self.$refs.terminalTree.setCurrentNode('');
                    self.terminalBundle.current = '';
                    if(self.$refs.terminalBundleTree) self.$refs.terminalBundleTree.setCurrentNode('');
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
                handleTerminalCurrentNodeChange:function(currentNode){
                    var self = this;
                    self.terminal.current = currentNode;
                    self.terminalBundle.data.splice(0, self.terminalBundle.data.length);
                    ajax.post('/tetris/bvc/model/terminal/bundle/load/all', {
                        terminalId:self.terminal.current.id
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.terminalBundle.data.push(data[i]);
                            }
                        }
                    });
                },
                handleTerminalBundleCurrentNodeChange:function(currentNode){
                    var self = this;
                    self.terminalBundle.current = currentNode;
                    ajax.post('/tetris/bvc/business/terminal/bundle/user/permission/load', {
                        userId:self.user.current.id,
                        terminalId:self.terminal.current.id,
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
                
                //一键设备自动配置
                handleAddAllBundles:function(){
                    var self = this;
                        ajax.post('/tetris/bvc/business/terminal/bundle/user/permission/add/all', {
                            userId:self.user.current.id,
                            terminalId:self.terminal.current.id,
                        }, function(data, status){
                            if(status !== 200) return;
                            alert("绑定成功");
                        }, null, ajax.TOTAL_CATCH_CODE);
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
                        ajax.post('/tetris/bvc/business/terminal/bundle/user/permission/edit', {
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
                        ajax.post('/tetris/bvc/business/terminal/bundle/user/permission/add', {
                            userId:self.user.current.id,
                            terminalId:self.terminal.current.id,
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
                                ajax.post('/tetris/bvc/business/terminal/bundle/user/permission/delete', {
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
                self.loadUsers(1);
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