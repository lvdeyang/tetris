define([
    'text!' + window.APPPATH + 'login-page/page-login-page.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'login-page/page-login-page.css'
], function(tpl, config, ajax, context, commons, Vue){

    var locale = context.getProp('locale');

    var pageId = 'page-login-page';

    var init = function(){

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el:'#' + pageId + '-wrapper',
            data:{
                loadingText: "",
                menus:context.getProp('menus'),
                user:context.getProp('user'),
                groups:context.getProp('groups'),
                loading:false,
                loginPage: {
                    rowData:'',
                    data: [],
                    currentPageVariable:{}
                },
                variableTreeResource:{
                    data:[]
                },
                editVariableTreeResource:{
                    data:[]
                },
                editVariable:{
                    data:[],
                    loading:false,
                    visible: false,
                },
                variableResource:{
                    loading: false,
                    tree: {
                        props: {
                            label: "value",
                            children: "variable"
                        },
                        data: [],
                        loading: false
                    },
                    chooseNode: []
                },
                dialog:{
                    addPage:{
                        loading:false,
                        visible: false,
                        name:'',
                        tpl:'',
                        isCurrent:'',
                        remark:''
                    },
                    editPage:{
                        data:[],
                        loading:false,
                        visible: false,
                        name:'',
                        tpl:'',
                        remark:'',
                        isCurrent:''
                    },
                }
            },
            methods:{
                //显示变量表
                getVariableList:function(){
                    var self = this;
                    ajax.post('/variable/variable/ByType', null, function (data, status) {
                        if (status != 200) return;
                        if (data && data.length > 0) {
                            for (var i = 0; i < data.length; i++) {
                                Vue.set(self.loginPage.currentPageVariable,data[i].variableKey,'');
                                self.variableResource.tree.data.push(data[i]);
                            }
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                //显示已配置变量
                getPageVariableList:function(currentRow, oldRow){
                    var self = this;
                    this.loginPage.rowData = currentRow;
                    currentId=this.loginPage.rowData.id;
                    var questData={
                        loginPageId: currentId
                    };
                    ajax.post('/pageVariable/get', questData, function (data, status) {
                        if (status != 200) return;
                            if (data && data.length > 0) {
                                for (var i = 0; i < data.length; i++){
                                    Vue.set(self.loginPage.currentPageVariable,data[i].variableKey,data[i].variable[0].id);
                                }
                            }else{
                                //Object.keys(self.loginPage.currentPageVariable).map(key => self.loginPage.currentPageVariable[key] = '')
                                for(var v in self.loginPage.currentPageVariable){
                                    self.loginPage.currentPageVariable[v]='';
                                }

                            }
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                    self.loginPage.rowData.id=currentId;
                },
                //提交变量配置
                handleEditVariableCommit:function(){
                    var self = this;
                    if(!self.loginPage.rowData.id){
                        this.$message({
                            message: '请先选择一个登陆页面',
                            type: 'warning'
                        });
                        return;
                    }
                    for(var v in self.loginPage.currentPageVariable){
                        if(self.loginPage.currentPageVariable[v]==''){
                            this.$message({
                                message: '请完善变量配置',
                                type: 'warning'
                            });
                            return;
                        }

                    }
                    var arr =JSON.stringify(this.loginPage.currentPageVariable);
                    console.log(arr);
                    var questData={
                        loginPageId: self.loginPage.rowData.id,
                        variableIds:arr
                    };
                    ajax.post('/pageVariable/set', questData, function (data, status) {
                     if (status != 200) return;
                        self.$message({
                            message: '保存成功',
                            type: 'success'
                        });
                     }, null, ajax.NO_ERROR_CATCH_CODE);
                },

                //显示登陆页面数据
                getPageList: function () {
                    var self = this;
                    self.loading = true;
                    ajax.post('/login/page/list', null,function (data, status) {
                        self.loading = false;
                        if (status != 200) return;
                        self.loginPage.data = data;
                    }, null, ajax.NO_ERROR_CATCH_CODE)
                },
                /*toggleSelection: function (rows) {
                    if (rows) {
                        for (var i = 0; i < rows.length; i++) {
                            this.$refs.multipleTable.toggleRowSelection(rows[i]);
                        }
                    } else {
                        this.$refs.multipleTable.clearSelection();
                    }
                },*/
                //添加页面
                handleAddPage:function(){
                    var self = this;
                    self.dialog.addPage.visible = true;
                },

                handleAddPageClose:function(){
                    var self = this;
                    self.dialog.addPage.visible=false;
                    self.dialog.addPage.name='';
                    self.dialog.addPage.remark='';
                    self.dialog.addPage.tpl=''
                },

                handleAddPageCommit:function(){
                    var self = this;
                    var newData={
                        name:self.dialog.addPage.name,
                        tpl:self.dialog.addPage.tpl,
                        remark:self.dialog.addPage.remark,
                        isCurrent:false
                    };
                    ajax.post('/login/page/add', newData, function (data, status) {
                        self.dialog.addPage.loading = false;
                        if (status != 200) return;
                        self.handleAddPageClose();
                        self.getPageList();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },

                //使用页面
                handleUseLoginPage:function(scope){
                    var self = this;
                    var row = scope.row;

                    var questData = {
                        id: row.id
                    };
                    ajax.post('/login/page/use', questData, function (data, status) {
                        if (status != 200) return;
                        self.getPageList();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },

                //编辑按钮，配置页面
                handleEditLoginPage:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.editPage.data = row;
                    self.dialog.editPage.name=row.name;
                    self.dialog.editPage.tpl=row.tpl;
                    self.dialog.editPage.isCurrent=row.isCurrent;
                    self.dialog.editPage.remark=row.remark;
                    self.dialog.editPage.visible=true;
                },
                handleEditLoginPageCommit: function (){
                    var self = this;
                    var newName = self.dialog.editPage.name;
                    var newRemark = self.dialog.editPage.remark;
                    var newTpl = self.dialog.editPage.tpl;
                    var questData={
                        id: self.dialog.editPage.data.id,
                        name:newName,
                        remark:newRemark,
                        tpl:newTpl
                    };
                    ajax.post('/login/page/edit', questData, function (data, status) {
                        self.dialog.addPage.loading = false;
                        if (status != 200) return;
                        self.handleEditLoginPageClose();
                        self.getPageList();
                    }, null, ajax.NO_ERROR_CATCH_CODE);

                },

                handleEditLoginPageClose:function(){
                    var self = this;
                    self.dialog.editPage.visible=false;
                    self.dialog.editPage.name='';
                    self.dialog.editPage.tpl='';
                    self.dialog.editPage.isCurrent='';
                    self.dialog.editPage.remark=''
                 },

                //删除按钮，删除一条页面数据
                rowDelete: function (scope) {
                    var self = this;
                    var row = scope.row;
                    self.showTip('', '此操作将永久删除此页面，是否继续?', function (callback) {
                        var questData = {
                            id: row.id
                        };
                        ajax.post('/login/page/delete', questData, function (data, status) {
                            callback();
                            if (status != 200) return;
                            self.getPageList();
                        }, null, ajax.NO_ERROR_CATCH_CODE);
                    });
                },
                showTip: function (title, text, confirmListener) {
                    var self = this;
                    var h = self.$createElement;
                    self.$msgbox({
                        title: title ? title : '危险操作',
                        message: h('div', null, [
                            h('div', {class: 'el-message-box__status el-icon-warning'}, null),
                            h('div', {class: 'el-message-box__message'}, [
                                h('p', null, [text])
                            ])
                        ]),
                        type: 'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose: function (action, instance, done) {
                            instance.confirmButtonLoading = true;
                            if (action === 'confirm') {
                                confirmListener(function () {
                                    instance.confirmButtonLoading = false;
                                    done();
                                })
                            } else {
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function () {
                    });
                }
            },
            created:function(){
                var self = this;
                /*ajax.post('/variable/variable/ByType',null,function(data,status){
                    if(data&&data.length>0){
                        for(var i=0;i<data.length;i++){
                            Vue.set(self.loginPage.currentPageVariable,data[i].variableKey,'');
                        }
                    }
                });*/
                self.getPageList();
                self.getVariableList();
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