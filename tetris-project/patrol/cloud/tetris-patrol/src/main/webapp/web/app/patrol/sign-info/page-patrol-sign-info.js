/**
 * Created by lvdeyang on 2018/12/24 0024.
 */
define([
    'text!' + window.APPPATH + 'patrol/sign-info/page-patrol-sign-info.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'date',
    'css!' + window.APPPATH + 'patrol/sign-info/page-patrol-sign-info.css'
], function(tpl, config, $, ajax, context, commons, Vue){

    var pageId = 'page-patrol-sign-info';

    var charts = {};

    var init = function(){

        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el: '#' + pageId + '-wrapper',
            data: {
                basePath:window.BASEPATH,
                menus: context.getProp('menus'),
                user: context.getProp('user'),
                groups: context.getProp('groups'),
                table:{
                    data:[],
                    page:{
                        currentPage:0,
                        pageSizes:[20, 50, 100, 1000],
                        pageSize:20,
                        total:0
                    }
                },
                condition:{
                    addressName:'',
                    name:'',
                    timeScope:[]
                }
                
            },
            computed:{

            },
            watch:{

            },
            methods:{
                load:function(currentPage){
                    var self = this;
                    var params = {
                        currentPage:currentPage,
                        pageSize:self.table.page.pageSize
                    };
                    if(self.condition.name) params.name = self.condition.name;
                    if(self.condition.addressName) params.addressName = self.condition.addressName;
                    if(self.condition.timeScope && self.condition.timeScope.length>0){
                        params.beginTime = self.condition.timeScope[0].format('yyyy-MM-dd hh:mm:ss');
                        params.endTime = self.condition.timeScope[1].format('yyyy-MM-dd hh:mm:ss');
                    }
                    self.table.data.splice(0, self.table.data.length);
                    ajax.post('/sign/load', params, function(data){
                        var total = data.total;
                        var rows = data.rows;
                        if(rows && rows.length>0){
                            for(var i=0; i<rows.length; i++){
                                self.table.data.push(rows[i]);
                            }
                            self.table.page.total = total;
                            self.table.page.currentPage = currentPage;
                        }
                    });
                },
                handleExportExcel:function(){
                    var self = this;

                    var h = self.$createElement;
                    self.$msgbox({
                        title:'提示',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['是否导出当前检索结果?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '是',
                        cancelButtonText: '否',
                        beforeClose:function(action, instance, done){
                            if(action === 'confirm'){
                                var params = {};
                                if(self.condition.name) params.name = self.condition.name;
                                if(self.condition.addressName) params.addressName = self.condition.addressName;
                                if(self.condition.timeScope && self.condition.timeScope.length>0){
                                    params.beginTime = self.condition.timeScope[0].format('yyyy-MM-dd hh:mm:ss');
                                    params.endTime = self.condition.timeScope[1].format('yyyy-MM-dd hh:mm:ss');
                                }
                                ajax.download('/sign/export/excel', params, function(data){
                                    var $a = $('#export-excel');
                                    $a[0].download = '传媒中心智慧巡查系统.xls';
                                    $a[0].href=window.URL.createObjectURL(data);
                                    $a[0].click();
                                    done();
                                });
                            }else{
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                rowKey:function(row){
                    var self = this;
                    return 'sign-' + row.id;
                },
                handleRowDelete:function(scope){
                    var self = this;
                    var row = scope.row;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'提示',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['是否删除当前签到信息?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '是',
                        cancelButtonText: '否',
                        beforeClose:function(action, instance, done){
                            if(action === 'confirm'){
                                ajax.post('/sign/delete', {
                                    id:row.id
                                }, function(data){
                                    for(var i=0; i<self.table.data.length; i++){
                                        if(self.table.data[i].id === row.id){
                                            self.table.data.splice(i, 1);
                                            break;
                                        }
                                    }
                                    self.table.page.total = self.table.page.total - 1;
                                    if(self.table.data.length===0 && self.table.page.total>0){
                                        self.load(self.table.page.currentPage-1);
                                    }
                                    done();
                                });
                            }else{
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                handleSizeChange:function(pageSize){
                    var self = this;
                    self.table.page.pageSize = pageSize;
                    self.load(table.page.currentPage?table.page.currentPage:1);
                },
                handleCurrentChange:function(currentPage){
                    var self = this;
                    self.load(currentPage);
                }
            },
            mounted:function(){
                var self = this;
                self.load(1);
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