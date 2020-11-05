/**
 * Created by lvdeyang on 2018/12/24 0024.
 */
define([
    'text!' + window.APPPATH + 'omms/software/service/installation-package/history/page-omms-software-service-installation-package-history.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'date',
    'css!' + window.APPPATH + 'omms/software/service/installation-package/history/page-omms-software-service-installation-package-history.css'
], function(tpl, config, $, ajax, context, commons, Vue){

    var pageId = 'page-omms-software-service-installation-package-history';

    var charts = {};

    var init = function(i){

        //璁剧疆鏍囬
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
                    rows:[],
                    pageSize:50,
                    pageSizes:[5,50, 100, 200, 400],
                    currentPage:0,
                    total:0,
                    condition:{
                        serviceTypeId: i.serviceTypeId,
                    }

                }

            },
            computed:{

            },
            watch:{

            },
            methods:{
                dateFormat:function(row, column){
                    var date = new Date(row.createTime);
                    var year = date.getFullYear();
                    var month = date.getMonth() < 9 ? "0" + (date.getMonth() + 1) : "" + (date.getMonth() + 1);
                    var day = date.getDate() < 10 ? "0" + date.getDate() : "" + date.getDate();
                    var hour = date.getHours() < 10 ? "0" + date.getHours() : "" + date.getHours();
                    var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : "" + date.getMinutes();
                    var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : "" + date.getSeconds();
                    return (year + "-" + month + "-" + day + " " + hour + ":" + minutes + ":" + seconds);},

                rowKey:function(row){
                    return 'user-' + row.id;
                },
                packageDelete:function(scope){
                    var self = this;
                    var row = scope.row;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'危险操作',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['此操作将永久删除该安装包，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/installation/package/history/delete/package/'+row.id,null,function(data,status){
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
                handleSizeChange:function(size){
                    var self = this;
                    self.table.pageSize = size;
                    self.query(self.table.currentPage);
                },
                handleCurrentChange:function(currentPage){
                    var self = this;
                    self.query(currentPage);
                },
                query:function(currentPage){
                    var self = this ;
                    var param = {
                        currentPage:currentPage,
                       pageSize:self.table.pageSize
                    };
                    if(self.table.condition.serviceTypeId) param.serviceTypeId = self.table.condition.serviceTypeId;
                    self.table.rows.splice(0, self.table.rows.length);
                    ajax.post('/installation/package/history/search/package',param,function(data){
                        var total = data.total;
                        var rows = data;
                        var rows = data.rows;
                        if(rows && rows.length>0){
                            for(var i = 0;i<rows.length;i++){
                                rows[i].popvisible = false;
//                                rows[i].tokens = [];
                                self.table.rows.push(rows[i])
                            }
                            self.table.total = total;
                        }
                        self.table.currentPage = currentPage;
                    })

                }

            },
            mounted:function(){
               var self = this;
                self.query(1);

            }
                


        });

    };

    var destroy = function(){

    };

    var groupList = {
        path:'/' + pageId + '/:serviceTypeId',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});