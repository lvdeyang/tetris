define([
    'text!' + window.APPPATH + 'bvc2-signal-control/bvc2-repeater-task/bvc2-repeater-task.html',
    'restfull',
    'jquery',
    'vue',
    'element-ui',
    'bvc2-header',
    'bvc2-nav-side'
], function(tpl, ajax, $, Vue){

    //组件名称
    var pageId = 'bvc2-repeater-task';

    var init = function(){

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el:'#' + pageId + '-wrapper',
            data:{
                header:{
                    links:[],
                    user:''
                },
                currentTab: 'main',
                active:'3',
                table:{
                    data:[]
                },
                pageSizes:[100, 200, 300, 400],
                pageSize:100,
                currentPage:1,
                total:0
            },
            watch: {
                currentTab: function(){
                    var self = this;
                    self.loadRepeaters(self.pageSize, self.currentPage);
                }
            },
            methods: {
                loadRepeaters: function(pageSize, currentPage){
                    var self = this;
                    self.table.data.splice(0, self.table.data.length);
                    if(self.currentTab === "main"){
                        ajax.post('/signal/control/mapping/query/main/all', {
                            pageSize: pageSize,
                            currentPage: currentPage
                        }, function(data){
                            if(data){
                                for(var i=0; i< data.rows.length; i++){
                                    self.table.data.push(data.rows[i]);
                                }
                                self.total = data.total;
                            }
                        }, null, ajax.NO_ERROR_CATCH_CODE);
                    }
                    if(self.currentTab === "backup"){
                        ajax.post('/signal/control/mapping/query/backup/all', {
                            pageSize: pageSize,
                            currentPage: currentPage
                        }, function(data){
                            if(data){
                                for(var i=0; i< data.rows.length; i++){
                                    self.table.data.push(data.rows[i]);
                                }
                                self.total = data.total;
                            }
                        }, null, ajax.NO_ERROR_CATCH_CODE);
                    }

                },

                objectSpanMethod: function(data){
                    var self = this;
                    var _rows = self.table.data;
                    if(data.columnIndex === 0 || data.columnIndex === 1){
                        if(data.rowIndex > 0){
                            if(data.row.dstBundleId === _rows[data.rowIndex-1].dstBundleId){
                                return{
                                    rowspan: 0,
                                    colspan: 0
                                }
                            }else{
                                var count = 0;
                                for(var j=data.rowIndex; j<_rows.length; j++){
                                    if(data.row.dstBundleId === _rows[j].dstBundleId){
                                        count++;
                                    }else{
                                        break;
                                    }
                                }
                                return{
                                    rowspan: count,
                                    colspan: 1
                                }
                            }
                        }else{
                            var count = 0;
                            for(var j=data.rowIndex; j<_rows.length; j++){
                                if(data.row.dstBundleId === _rows[j].dstBundleId){
                                    count++;
                                }else{
                                    break;
                                }
                            }
                            return{
                                rowspan: count,
                                colspan: 1
                            }
                        }
                    }
                },

                rowAddTask:function(scope){
                    var self = this;
                    var row = scope.row;

                    ajax.post('/signal/control/mapping/add/task', {
                        id: row.id,
                        netIp: row.netIp
                    }, function(data){
                        row.taskId = id;
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },

                rowDeleteTask:function(scope){
                    var self = this;
                    var row = scope.row;

                    ajax.post('/signal/control/mapping/remove/task', {
                        taskId: row.taskId
                    }, function(data){
                       row.taskId = null;
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                handleSizeChange: function(val){
                    var self = this;
                    self.loadRepeaters(val, self.currentPage);
                },
                handleCurrentChange: function(val){
                    var self = this;
                    self.loadRepeaters(self.pageSize, val);
                }
            },
            mounted:function(){
                var self = this;
                self.loadRepeaters(self.pageSize, self.currentPage);
            }

        });
    };

    var destroy = function(){

    };

    var page = {
        path:'/' + pageId,
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return page;
});