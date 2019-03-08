/**
 * Created by ldy on 2019/3/2 0024.
 */
define([
    'text!' + window.APPPATH + 'cms/classify/page-cms-classify.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'cms/classify/page-cms-classify.css'
], function(tpl, config, $, ajax, context, commons, Vue){

    var pageId = 'page-cms-classify';

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
                        currentPage:1,
                        sizes:[20, 50, 100, 500, 1000],
                        size:20,
                        total:0
                    }
                },
                dialog:{
                    addClassify:{
                        visible:false,
                        name:'',
                        remark:'',
                        loading:false
                    },
                    editClassify:{
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
                rowEdit:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.editClassify.id = row.id;
                    self.dialog.editClassify.name = row.name;
                    self.dialog.editClassify.remark = row.remark;
                    self.dialog.editClassify.visible = true;
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
                                h('p', null, ['此操作将永久删除分类，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/cms/classify/remove/' + row.id, null, function(data, status){
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
                handleSizeChange:function(val) {
                    var self = this;
                    self.table.page.size = parseInt(val);
                    //刷新数据
                    self.loadClassify();
                },

                handleCurrentChange:function(val) {
                    var self = this;
                    self.table.page.currentPage = parseInt(val);
                    //刷新数据
                    self.loadClassify();
                },
                loadClassify:function(){
                    var self = this;
                    self.table.data.splice(0, self.table.data.length);
                    ajax.post('/cms/classify/list', {
                        currentPage:self.table.page.currentPage,
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
                    });
                },
                handleAddClassifyClose:function(){
                    var self = this;
                    self.dialog.addClassify.name = '';
                    self.dialog.addClassify.remark = '';
                    self.dialog.addClassify.visible = false;
                },
                handleAddClassifyCommit:function(){
                    var self = this;
                    self.dialog.addClassify.loading = true;
                    ajax.post('/cms/classify/add', {
                        name:self.dialog.addClassify.name,
                        remark:self.dialog.addClassify.remark
                    }, function(data, status){
                        self.dialog.addClassify.loading = false;
                        if(status !== 200) return;
                        self.table.data.push(data);
                        self.table.page.total += 1;
                        self.handleAddClassifyClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                handleEditClassifyClose:function(){
                    var self = this;
                    self.dialog.editClassify.id = '';
                    self.dialog.editClassify.name = '';
                    self.dialog.editClassify.remark = '';
                    self.dialog.editClassify.visible = false;
                },
                handleEditClassifyCommit:function(){
                    var self = this;
                    self.dialog.editClassify.loading = true;
                    ajax.post('/cms/classify/edit/' + self.dialog.editClassify.id, {
                        name:self.dialog.editClassify.name,
                        remark:self.dialog.editClassify.remark
                    }, function(data, status){
                        self.dialog.editClassify.loading = false;
                        if(status !== 200) return;
                        for(var i=0; i<self.table.data.length; i++){
                            if(self.table.data[i].id === data.id){
                                self.table.data.splice(i, 1, data);
                                break;
                            }
                        }
                        self.handleEditClassifyClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE)
                }
            },
            created:function(){
                var self = this;
            },
            mounted:function(){
                var self = this;
                self.loadClassify(1);
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