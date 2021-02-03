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
                   /* page: {
                        currentPage: 1,
                        sizes: [10, 15, 20, 50],
                        size: 10,
                        total: 0
                    },*/
                    data: [],
                    multipleSelection: []
                },
                variableTreeResource:{
                    data:[2,5]
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
                            label: "name",
                            children: "children"
                        },
                        data: [{
                            //id: 1,
                            name: '背景图片',
                            children: [{
                                id: 2,
                                name: '背景图1'
                            }, {
                                id: 3,
                                name: '背景图2'
                            }]
                        }, {
                            //id: 4,
                            name: 'logo图片',
                            children: [{
                                id: 5,
                                name: 'logo图1'
                            }, {
                                id: 6,
                                name: 'logo图2'
                            }]
                        }, {
                            //id: 7,
                            name: '标题',
                            children: [{
                                id: 8,
                                name: '标题1'
                            }, {
                                id: 9,
                                name: '标题2'
                            }]
                        }],
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
                //显示变量配置表
                getPageVariableList:function(currentRow, oldRow){
                    var self = this;
                    this.rowData = currentRow;
                    var questData={
                        id:this.rowData.id
                    };
                   /* ajax.post('', questData, function (data, status) {
                        if (status != 200) return;
                        if (data && data.length > 0) {
                            for (var i = 0; i < data.length; i++) {
                                self.variableResource.tree.data.push(data[i]);
                            }
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE);*/
                    self.setVariableTreeCheckedChange();
                },

                getVariableTreeCheckedChange:function(){
                    var self = this;
                    console.log(this.$refs.variableTree.getCheckedKeys());
                    this.variableTreeResource.data=this.$refs.variableTree.getCheckedKeys();
                    var questData={
                        list:this.variableTreeResource.data
                    }
                    /*ajax.post('/pageVariable/get', questData, function (data, status) {
                        if (status != 200) return;

                    }, null, ajax.NO_ERROR_CATCH_CODE);*/
                },
                setVariableTreeCheckedChange:function(){
                    var self = this;
                    this.$refs.variableTree.setCheckedKeys([this.variableTreeResource.data]);
                },
                //显示已配置好的页面数据
                getPageList: function () {
                    var self = this;
                    self.loading = true;
                    /*var requestData = {
                        currentPage: self.loginPage.page.currentPage,
                        pageSize: self.loginPage.page.size
                    };*/
                    ajax.post('/login/page/list', null,function (data, status) {
                        self.loading = false;
                        if (status != 200) return;
                        self.loginPage.data = data;
                        /*data.forEach(function(item){
                            if(item.isCurrent){
                                useStatus=true;
                                self.loginPage.data.push[useStatus];
                                item.isCurrent='使用中'
                            }else{
                                self.loginPage.data.push[useStatus];
                                item.isCurrent='未使用'}
                            })*/
                        //self.loginPage.page.total = data.total;
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
                        /*if (self.channel.data.length < self.channel.page.size) {
                            self.channel.data.push(data);
                        }*/
                        //self.channel.page.total += 1;
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
                 },

                //好像没用
                /*editVariable:function(scope){
                    var self = this;
                    self.editVariable.data = scope.row;
                    self.editVariable.visible=true
                },

                handleEditVariableClose:function(){
                    var self = this;
                    self.editVariable.data = "";
                    self.editVariable.visible = false;
                },*/

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
                self.getPageList();
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