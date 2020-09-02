/**
 * Created by lqxuhv on 2020/9/2.
 */
/**
 * Created by lvdeyang on 2018/12/24 0024.
 */
define([
    'text!' + window.APPPATH + 'omms/software/service/deployment/page-omms-software-service-deployment.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'date',
    'css!' + window.APPPATH + 'omms/software/service/deployment/page-omms-software-service-deployment.css'
], function(tpl, config, $, ajax, context, commons, Vue){

    var pageId = 'page-omms-software-service-deployment';

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
                valueTypes:[],
                table:{
                    rows:[],
                    pageSize:50,
                    pageSizes:[5,50, 100, 200, 400],
                    currentPage:0,
                    total:0,
                    serverId :2//i.serverId
                }
            },
            computed:{

            },
            watch:{

            },
            methods: {
                handleSizeChange:function(size){
                    var self = this;
                    self.table.pageSize = size;
                    self.load(self.table.currentPage);
                },
                handleCurrentChange:function(currentPage){
                    var self = this;
                    self.load(currentPage);
                },
                rowKey:function(row){
                    return row.id;
                },
                load:function(currentPage){
                    var self = this;
                    var param = {
                        currentPage:currentPage,
                        pageSize:self.table.pageSize
                    };
                    if(self.table.serverId) param.serverId = self.table.serverId;
                    self.table.rows.splice(0, self.table.rows.length);
                    ajax.post('/service/deployment/load', param, function(data){
                        var total = data.total;
                        var rows = data.rows;
                        if(rows && rows.length>0){
                            for(var i = 0;i<rows.length;i++){
                                rows[i].popvisible = false;
                                self.table.rows.push(rows[i])
                            }
                            self.table.total = total;
                        }
                        self.table.currentPage = currentPage;
                    });
                },

            },
            created:function(){
                /*var self = this;
                 ajax.post('/properties/find/value/types', null, function(data){
                 for(var i=0; i<data.length; i++){
                 self.valueTypes.push(data[i]);
                 }
                 });
                 self.load(1);*/
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
        path:'/' + pageId + '/:serverId',
        component:{
            template:'<div id="' + pageId + '"class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});