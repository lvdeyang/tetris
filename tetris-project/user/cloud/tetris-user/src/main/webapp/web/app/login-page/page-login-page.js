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
                dialog:{
                    addPage:{
                        loading:false,
                        visible: false,
                        name:'',
                        tpl:'',
                        isCurrent:'',
                        remark:''
                    },

                    chooseVariable:{
                        loading:false,
                        visible: false,
                    }
                }


            },
            methods:{
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
                        data.forEach(function(item){
                            if(item.isCurrent){
                                item.isCurrent='使用中'
                            }else{
                                item.isCurrent='未使用'}
                            })
                        //self.loginPage.page.total = data.total;
                    }, null, ajax.NO_ERROR_CATCH_CODE)
                },
                toggleSelection: function (rows) {
                    if (rows) {
                        for (var i = 0; i < rows.length; i++) {
                            this.$refs.multipleTable.toggleRowSelection(rows[i]);
                        }
                    } else {
                        this.$refs.multipleTable.clearSelection();
                    }
                },
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

                //使用
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
                handleChooseVariable:function(){
                    var self = this;
                    self.dialog.chooseVariable.visible=true;
                },
                handleChooseVariableCommit: function (){
                    self.dialog.chooseVariable.visible=false;
                },
                handleChooseVariableClose:function(){
                    var self = this;
                    self.dialog.chooseVariable.visible=false;
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