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
    'mi-system-role-dialog',
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
                },
                rowDelete:function(scope){
                    var self = this;
                    var row = scope.row;
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