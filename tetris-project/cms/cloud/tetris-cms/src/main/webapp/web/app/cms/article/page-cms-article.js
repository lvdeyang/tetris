/**
 * Created by lvdeyang on 2018/12/24 0024.
 */
define([
    'text!' + window.APPPATH + 'cms/article/page-cms-article.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'layout-editor',
    'css!' + window.APPPATH + 'cms/article/page-cms-article.css'
], function(tpl, config, $, ajax, context, commons, Vue){

    var pageId = 'page-cms-article';

    var init = function(){

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
                table:{
                    data:[],
                    page:{
                        currentPage:0,
                        sizes:[20, 50, 100, 500, 1000],
                        size:20,
                        total:0
                    }
                },
                dialog:{
                    addArticle:{
                        visible:false,
                        name:'',
                        remark:'',
                        loading:false
                    },
                    editArticle:{
                        visible:false,
                        id:'',
                        name:'',
                        remark:'',
                        loading:false
                    }
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                handleSelectionChange:function(){
                    var self = this;
                },
                rowEdit:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.editArticle.id = row.id;
                    self.dialog.editArticle.name = row.name;
                    self.dialog.editArticle.remark = row.remark;
                    self.dialog.editArticle.visible = true;
                },
                rowDelete:function(scope){
                    var self = this;
                    var row = scope.row;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'危险操作',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['此操作将永久删除文章，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/cms/article/remove/' + row.id, null, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    for(var i=0; i<self.table.data.length; i++){
                                        if(self.table.data[i].id === row.id){
                                            self.table.data.splice(i ,1);
                                            break;
                                        }
                                    }
                                    self.table.page.total -= 1;
                                }, null, ajax.NO_ERROR_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                handleSizeChange:function(){
                    var self = this;
                },
                handleCurrentChange:function(){
                    var self = this;
                },
                loadArticle:function(currentPage){
                    var self = this;
                    self.table.data.splice(0, self.table.data.length);
                    ajax.post('/cms/article/list', {
                        currentPage:currentPage,
                        pageSize:self.table.page.size
                    }, function(data){
                        var rows = data.rows;
                        var total = data.total;
                        if(rows && rows.length>0){
                            for(var i=0; i<rows.length; i++){
                                self.table.data.push(rows[i]);
                            }
                        }
                        self.table.page.total = total;
                        self.table.page.currentPage = currentPage;
                    });
                },
                handleAddArticleClose:function(){
                    var self = this;
                    self.dialog.addArticle.name = '';
                    self.dialog.addArticle.remark = '';
                    self.dialog.addArticle.visible = false;
                },
                handleAddArticleCommit:function(){
                    var self = this;
                    self.dialog.addArticle.loading = true;
                    ajax.post('/cms/article/add', {
                        name:self.dialog.addArticle.name,
                        remark:self.dialog.addArticle.remark
                    }, function(data, status){
                        self.dialog.addArticle.loading = false;
                        if(status !== 200) return;
                        self.table.data.push(data);
                        self.table.page.total += 1;
                        self.handleAddArticleClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                handleEditArticleClose:function(){
                    var self = this;
                    self.dialog.editArticle.id = '';
                    self.dialog.editArticle.name = '';
                    self.dialog.editArticle.remark = '';
                    self.dialog.editArticle.visible = false;
                },
                handleEditArticleCommit:function(){
                    var self = this;
                    self.dialog.editArticle.loading = true;
                    ajax.post('/cms/article/edit/' + self.dialog.editArticle.id, {
                        name:self.dialog.editArticle.name,
                        remark:self.dialog.editArticle.remark
                    }, function(data, status){
                        self.dialog.editArticle.loading = false;
                        if(status !== 200) return;
                        for(var i=0; i<self.table.data.length; i++){
                            if(self.table.data[i].id === data.id){
                                self.table.data.splice(i, 1, data);
                                break;
                            }
                        }
                        self.handleEditArticleClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE)
                },
                editLayout:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.$refs.articleLayoutEditor.show(row);
                },
                saveArticle:function(article, html, modules, endLoading){
                    var self = this;
                    ajax.post('/cms/article/save/' + article.id, {
                        html:html,
                        modules:modules
                    }, function(data, status){
                        endLoading();
                        if(status !== 200) return;
                        self.$message({
                            type:'success',
                            message:'保存成功'
                        })
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                }
            },
            created:function(){
                var self = this;
            },
            mounted:function(){
                var self = this;
                self.loadArticle(1);
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