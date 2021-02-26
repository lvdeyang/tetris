/**
 * Created by lzp on 2019/5/29.
 */
define([
    'text!' + window.APPPATH + 'business-user/page-business-user.html',
    window.APPPATH + 'business-user/page-business-user.i18n',
    'config',
    'restfull',
    'jquery',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'mi-sub-title',
    'mi-user-dialog',
    'mi-upload-dialog',
    'css!' + window.APPPATH + 'business-user/page-business-user.css'
], function (tpl, i18n, config, ajax, $, context, commons, Vue) {

    var locale = context.getProp('locale');
    var i18n = !locale?i18n.default:i18n[locale]?i18n[locale]:i18n.default;

    var pageId = 'page-business-user';

    var vueInstance = null;

    var init = function () {

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        vueInstance = new Vue({
            el: '#' + pageId + '-wrapper',
            data: {
                menus: context.getProp('menus'),
                user: context.getProp('user'),
                groups: context.getProp('groups'),
                i18n:i18n,
                activeId: window.BASEPATH + 'index#/page-business-user',
                importInfo:{
                    status:false,
                    totalUsers:0,
                    currentUser:0,
                    importTimes:0,
                    interval:''
                },
                table: {
                    rows: [],
                    pageSize: 50,
                    pageSizes: [50, 100, 200, 400],
                    currentPage: 0,
                    total: 0,
                    condition:{
                    	nickname:'',
                    	userno:''
                    }
                },
                dialog: {
                    edit: {
                        companyRole: {
                            visible: false,
                            rows: [],
                            current: ''
                        }
                    },
                    add:{
                        companyRole:{
                            visible: false,
                            rows: [],
                            current: ''
                        }
                    },
                    createUser: {
                        visible: false,
                        nickname: '',
                        username: '',
                        userno:'',
                        password: '',
                        repeat: '',
                        mobile: '',
                        mail: '',
                        level:1,
                        classify: '企业用户',
                        company: {
                            id: '',
                            name: '',
                            companyRoleName:'',
                            companyRoleId:''
                        },
                        loading: false
                    },
                    editUser: {
                        visible: false,
                        editPassword: false,
                        id: '',
                        nickname: '',
                        mobile: '',
                        mail: '',
                        level:1,
                        oldPassword: '',
                        newPassword: '',
                        repeat: '',
                        loading: false,
                        companyRoleName:'',
                        companyRoleId:''
                    },
                    import:{
                        requireType:['csv'],
                        multiple:false
                    }
                }
            },
            methods: {
                rowKey:function(row){
                    return 'user-' + row.uuid;
                },
                gotoBindBusinessRole:function(scope){
                    var slef = this;
                    var row = scope.row;
                    window.location.hash = '#/page-bind-system-role/' + row.id + '/' + row.nickname + '/business';
                },
                load: function (currentPage) {
                    var self = this;
                    var param = {
                        currentPage:currentPage,
                        pageSize:self.table.pageSize
                    };
                    if(self.table.condition.nickname) param.nickname = self.table.condition.nickname;
                    if(self.table.condition.userno) param.userno = self.table.condition.userno;
                    self.table.rows.splice(0, self.table.rows.length);
                    ajax.post('/user/find/by/company/id/and/condition', param, function(data){
                        var total = data.total;
                        var rows = data.rows;
                        if(rows && rows.length>0){
                            for(var i=0; i<rows.length; i++){
                            	if(rows[i].errorLoginTimes >= 10){
                            		rows[i].locked = true;
                            	}else{
                            		rows[i].locked = false;
                            	}
                                self.table.rows.push(rows[i]);
                            }
                            self.table.total = total;
                        }
                        self.table.currentPage = currentPage;
                    });
                },
                loadCompany:function(){
                    var self = this;
                    ajax.post('/company/subordinate', {}, function (data, status) {
                        if (status == 200 && data != null) {
                            self.dialog.createUser.company.id = data.id;
                            self.dialog.createUser.company.name = data.name;
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE)
                },
                handleCreate: function () {
                    var self = this;
                    self.dialog.createUser.visible = true;
                },
                handelDelete: function () {
                    var self = this;
                },
                handleExport:function(){
                    var self = this;
                    ajax.download('/user/handle/export', null, function(data){
                        var $a = $('#page-business-user-export');
                        $a[0].download = 'user.csv';
                        $a[0].href=window.URL.createObjectURL(data);
                        $a[0].click();
                        self.$message({
                            type:'success',
                            message:'操作成功'
                        });
                    });
                },
                handleImport:function(){
                    var self = this;
                    self.$refs.miUploadDialog.open();
                },
                fileSelected:function(files, done){
                    var self = this;
                    var csv = files[0];
                    var data = new FormData();
                    data.append('csv', csv);
                    ajax.upload('/user/handle/import', data, function(data){
                        self.$message({
                            type:'success',
                            message:'操作成功'
                        });
                        done();
                        //self.load(1);
                        self.loopImportStatus();
                    });
                },
                handelUserLockStatusChange:function(scope){
                	var self = this;
                	var rows = self.table.rows;
                	if(scope.row.locked){
                		ajax.post('/user/lock/' + scope.row.id, null, function(data){
                			 for(var i=0; i<rows.length; i++){
                				 if(rows[i].id === data.id){
                					 rows[i].errorLoginTimes = data.errorLoginTimes;
                					 rows[i].locked = true;
                					 break;
                				 }
                			 }
                			 self.$message({
                				 type:'success',
                				 message:'锁定成功！'
                			 });
                		});
                	}else{
                		ajax.post('/user/unlock/' + scope.row.id, null, function(data){
               			 for(var i=0; i<rows.length; i++){
               				 if(rows[i].id === data.id){
               					 rows[i].errorLoginTimes = data.errorLoginTimes;
               					 rows[i].locked = false;
               					 break;
               				 }
               			 }
               			 self.$message({
               				 type:'success',
               				 message:'解除锁定成功！'
               			 });
               		});
                	}
                },
                handleRowEdit: function (scope) {
                    var self = this;
                    var row = scope.row;
                    self.dialog.editUser.id = row.id;
                    self.dialog.editUser.nickname = row.nickname;
                    self.dialog.editUser.mobile = row.mobile;
                    self.dialog.editUser.companyRoleName = row.companyRoleName;
                    self.dialog.editUser.companyRoleId = row.companyRoleId;
                    self.dialog.editUser.mail = row.mail;
                    self.dialog.editUser.level = row.level?row.level:1;
                    self.dialog.editUser.visible = true;
                },
                handleEditUserClose:function(){
                    var self = this;
                    self.dialog.editUser.id = '';
                    self.dialog.editUser.nickname = '';
                    self.dialog.editUser.mobile = '';
                    self.dialog.editUser.companyRoleName='';
                    self.dialog.editUser.companyRoleId='';
                    self.dialog.editUser.mail = '';
                    self.dialog.editUser.level = 1;
                    self.dialog.editUser.editPassword = false;
                    self.dialog.editUser.oldPassword = '';
                    self.dialog.editUser.newPassword = '';
                    self.dialog.editUser.repeat = '';
                    self.dialog.editUser.visible = false;
                },
                handleEditUserSubmit:function(){
                    var self = this;
                    self.dialog.editUser.loading = true;
                    ajax.post('/user/edit/' + self.dialog.editUser.id, {
                        nickname:self.dialog.editUser.nickname,
                        mobile:self.dialog.editUser.mobile,
                        mail:self.dialog.editUser.mail,
                        level:self.dialog.editUser.level,
                        editPassword:self.dialog.editUser.editPassword,
                        oldPassword:self.dialog.editUser.oldPassword,
                        newPassword:self.dialog.editUser.newPassword,
                        repeat:self.dialog.editUser.repeat,
                        systemRoleId:self.dialog.editUser.companyRoleId
                    }, function(data, status){
                        self.dialog.editUser.loading = false;
                        if(status !== 200) return;
                        for(var i=0; i<self.table.rows.length; i++){
                            if(self.table.rows[i].id === self.dialog.editUser.id){
                                self.table.rows.splice(i, 1, data);
                                break;
                            }
                        }
                        self.load(1);
                        self.handleEditUserClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                selectSystemRole:function(f){
                    var self = this;
                    if(self.dialog.add.companyRole.visible) return;
                    var params = {createType:f};
                    if(f === 'COMPANY_ADMIN'){
                        if(!self.dialog.createUser.company.id){
                            self.$message({
                                type:'error',
                                message:'请先选择企业'
                            });
                            return;
                        }
                        params.companyId = self.dialog.createUser.company.id;
                    }
                    ajax.post('/system/role/query/by/create/type', params, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.dialog.add.companyRole.rows.push(data[i]);
                            }
                        }
                        self.dialog.add.companyRole.visible = true;
                        self.$nextTick(function(){
                            for(var i=0; i<self.dialog.add.companyRole.rows.length; i++){
                                if(self.dialog.add.companyRole.rows[i].id == self.dialog.createUser.company.companyRoleId){
                                    self.$refs.systemRoleTable.setCurrentRow(self.dialog.add.companyRole.rows[i]);
                                    break;
                                }
                            }
                        });
                    });
                },
                selectEditSystemRole:function(f){
                    var self = this;
                    if(self.dialog.edit.companyRole.visible) return;
                    var params = {createType:f};
                    if(f === 'COMPANY_ADMIN'){
                        if(!self.dialog.createUser.company.id){
                            self.$message({
                                type:'error',
                                message:'请先选择企业'
                            });
                            return;
                        }
                        params.companyId = self.dialog.createUser.company.id;
                    }
                    ajax.post('/system/role/query/by/create/type', params, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.dialog.edit.companyRole.rows.push(data[i]);
                            }
                        }
                        self.dialog.edit.companyRole.visible = true;
                        self.$nextTick(function(){
                            for(var i=0; i<self.dialog.edit.companyRole.rows.length; i++){
                                if(self.dialog.edit.companyRole.rows[i].id == self.dialog.editUser.companyRoleId){
                                    self.$refs.systemEditRoleTable.setCurrentRow(self.dialog.edit.companyRole.rows[i]);
                                    break;
                                }
                            }
                        });
                    });
                },

                handleEditSelectSystemRoleSelected:function(){
                    var self = this;
                    if(self.dialog.edit.companyRole.current){
                        self.dialog.editUser.companyRoleId = self.dialog.edit.companyRole.current.id;
                        self.dialog.editUser.companyRoleName = self.dialog.edit.companyRole.current.name;
                    }
                    self.handleEditSelectSystemRoleClose();
                },
                handleSelectSystemRoleSelected:function(){
                    var self = this;
                    if(self.dialog.add.companyRole.current){
                        self.dialog.createUser.company.companyRoleId = self.dialog.add.companyRole.current.id;
                        self.dialog.createUser.company.companyRoleName = self.dialog.add.companyRole.current.name;
                    }
                    self.handleSelectSystemRoleClose();
                },
                handleSelectSystemRoleClose:function(){
                    var self = this;
                    self.dialog.add.companyRole.visible = false;
                    self.dialog.add.companyRole.rows.splice(0, self.dialog.add.companyRole.rows.length);
                    self.dialog.add.companyRole.current = '';
                },
                handleEditSelectSystemRoleClose:function(){
                    var self = this;
                    self.dialog.edit.companyRole.visible = false;
                    self.dialog.edit.companyRole.rows.splice(0, self.dialog.edit.companyRole.rows.length);
                    self.dialog.edit.companyRole.current = '';
                },
                systemRoleKey:function(row){
                    return row.id;
                },
                systemRoleChange:function(current){
                    var self = this;
                    self.dialog.add.companyRole.current = current;
                },
                systemEditRoleChange:function(current){
                    var self = this;
                    self.dialog.edit.companyRole.current = current;
                },
                handleRowDelete: function (scope) {
                    var self = this;
                    var row = scope.row;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'危险操作',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['此操作将永久删除该用户，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/user/delete/' + row.id, null, function(data, status){
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
                handleCreateUserClose:function(){
                    var self = this;
                    self.dialog.createUser.nickname = '';
                    self.dialog.createUser.username = '';
                    self.dialog.createUser.userno = '';
                    self.dialog.createUser.password = '';
                    self.dialog.createUser.repeat = '';
                    self.dialog.createUser.mobile = '';
                    self.dialog.createUser.mail = '';
                    self.dialog.createUser.level = 1;
                    self.dialog.createUser.classify = '企业用户';
                    self.dialog.createUser.visible = false;
                },
                handleCreateUserSubmit: function () {
                    var self = this;
                    self.dialog.createUser.loading = true;
                    var params = {
                        nickname:self.dialog.createUser.nickname,
                        username:self.dialog.createUser.username,
                        userno:self.dialog.createUser.userno,
                        password:self.dialog.createUser.password,
                        repeat:self.dialog.createUser.repeat,
                        mobile:self.dialog.createUser.mobile,
                        mail:self.dialog.createUser.mail,
                        level:self.dialog.createUser.level,
                        classify:self.dialog.createUser.classify,
                        companyId:self.dialog.createUser.company.id
                    };

                    params['systemRoleId'] = self.dialog.createUser.company.companyRoleId;
                    params['companyId'] = self.dialog.createUser.company.id;

                    ajax.post('/user/add', params, function(data, status){
                        self.dialog.createUser.loading = false;
                        if(status !== 200) return;
                        self.table.rows.push(data);
                        self.handleCreateUserClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                handleSizeChange:function(size){
                    var self = this;
                    self.table.pageSize = size;
                    self.load(self.table.currentPage);
                },
                handleCurrentChange:function(currentPage){
                    var self = this;
                    self.load(currentPage);
                },
                importStatus:function(){
                    var self = this;
                    ajax.post('/user/query/import/status', null, function(data){
                        self.importInfo.status = data.status;
                        self.importInfo.totalUsers = data.totalUsers;
                        self.importInfo.currentUser = data.currentUser;
                        self.importInfo.importTimes = data.importTimes;
                        if(data.status && !self.importInfo.interval){
                            self.loopImportStatus();
                        }
                        if(!data.status && self.importInfo.interval){
                            clearInterval(self.importInfo.interval);
                            self.load(1);
                            console.log('清除interval');
                        }
                    });
                },
                loopImportStatus:function(){
                    var self = this;
                    self.importStatus();
                    self.importInfo.interval = setInterval(function(){
                        self.importStatus();
                    }, 5000);
                }
            },
            created: function () {
                var self = this;
                self.load(1);
                self.loadCompany();
                self.importStatus();
            }
        });
    };

    var destroy = function () {
        if(vueInstance.importInfo.interval) clearInterval(vueInstance.importInfo.interval);
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