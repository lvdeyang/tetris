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
    'date',
    'region-dialog',
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
                        author:'',
                        publishTime:new Date().format('yyyy-MM-DD HH:mm:ss'),
                        thumbnail:'',
                        classify:[],
                        remark:'',
                        loading:false,
                        timeOption:{
                            shortcuts: [{
                                text: '今天',
                                onClick:function(picker) {
                                    picker.$emit('pick', new Date());
                                }
                            }, {
                                text: '昨天',
                                onClick:function(picker) {
                                    var date = new Date();
                                    date.setTime(date.getTime() - 3600 * 1000 * 24);
                                    picker.$emit('pick', date);
                                }
                            }, {
                                text: '一周前',
                                onClick:function(picker) {
                                    const date = new Date();
                                    date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
                                    picker.$emit('pick', date);
                                }
                            }]
                        }
                    },
                    editArticle:{
                        visible:false,
                        id:'',
                        name:'',
                        author:'',
                        publishTime:'',
                        thumbnail:'',
                        classify:[],
                        remark:'',
                        loading:false,
                        timeOption:{
                            shortcuts: [{
                                text: '今天',
                                onClick:function(picker) {
                                    picker.$emit('pick', new Date());
                                }
                            }, {
                                text: '昨天',
                                onClick:function(picker) {
                                    var date = new Date();
                                    date.setTime(date.getTime() - 3600 * 1000 * 24);
                                    picker.$emit('pick', date);
                                }
                            }, {
                                text: '一周前',
                                onClick:function(picker) {
                                    const date = new Date();
                                    date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
                                    picker.$emit('pick', date);
                                }
                            }]
                        }
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
                doPreview:function(scope){
                    var self = this;
                    var row = scope.row;
                    window.open(window.BASEPATH + row.previewUrl, row.name, "status=no,menubar=yes,toolbar=no,width=414,height=738,left:100,top:100");
                },
                rowEdit:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.editArticle.id = row.id;
                    self.dialog.editArticle.name = row.name;
                    self.dialog.editArticle.author = row.author;
                    self.dialog.editArticle.publishTime = row.publishTime;
                    self.dialog.editArticle.thumbnail = row.thumbnail;
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
                handleSelectThumbnail:function(buff){
                    var self = this;
                    self.$refs.selectThumbnail.setBuffer(buff);
                    self.$refs.selectThumbnail.open();
                },
                selectedThumbnail:function(url, buff, startLoading, endLoading, done){
                    buff.thumbnail = url;
                    done();
                },
                handleClassifyRemove:function(classify, value){
                    for(var i=0; i<classify.length; i++){
                        if(classify[i] === value){
                            classify.splice(i, 1);
                            break;
                        }
                    }
                },
                handleClassifyEdit:function(){
                    var self = this;
                    var checkedRegions = []
                    self.$refs.regionDialog.open('/cms/region/list/tree', checkedRegions);
                },
                handleAddArticleClose:function(){
                    var self = this;
                    self.dialog.addArticle.name = '';
                    self.dialog.addArticle.author = '';
                    self.dialog.addArticle.publishTime = new Date().format('yyyy-MM-DD HH:mm:ss');
                    self.dialog.addArticle.thumbnail = '';
                    self.dialog.addArticle.remark = '';
                    self.dialog.addArticle.visible = false;
                },
                handleAddArticleCommit:function(){
                    var self = this;
                    self.dialog.addArticle.loading = true;
                    ajax.post('/cms/article/add', {
                        name:self.dialog.addArticle.name,
                        author:self.dialog.addArticle.author,
                        publishTime:self.dialog.addArticle.publishTime,
                        thumbnail:self.dialog.addArticle.thumbnail,
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
                    self.dialog.editArticle.author = '';
                    self.dialog.editArticle.publishTime = '';
                    self.dialog.editArticle.thumbnail = '';
                    self.dialog.editArticle.remark = '';
                    self.dialog.editArticle.visible = false;
                },
                handleEditArticleCommit:function(){
                    var self = this;
                    self.dialog.editArticle.loading = true;
                    ajax.post('/cms/article/edit/' + self.dialog.editArticle.id, {
                        name:self.dialog.editArticle.name,
                        author:self.dialog.addArticle.author,
                        publishTime:self.dialog.addArticle.publishTime,
                        thumbnail:self.dialog.addArticle.thumbnail,
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
                },
                selectedRegions:function(regions, buff, startLoading, endLoading){
                    var self = this;
                    var regionIds = [];
                    for(var i=0; i<regions.length; i++){
                        regionIds.push(regions[i].id);
                    }
                    startLoading();

                    endLoading();
                },
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