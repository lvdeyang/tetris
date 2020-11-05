define([
    'text!' + window.APPPATH + 'bvc2-signal-control/bvc2-task-control/bvc2-task-control.html',
    'restfull',
    'jquery',
    'vue',
    'element-ui',
    'bvc2-header',
    'bvc2-nav-side'
], function(tpl, ajax, $, Vue){

    //组件名称
    var pageId = 'bvc2-task-control';

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
                button:{
                    resume:{
                        loading: false
                    }
                },
                currentTab: 'main',
                active:'4',
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
                    self.loadTasks(self.pageSize, self.currentPage);
                }
            },
            methods: {
                loadTasks: function(pageSize, currentPage){
                    var self = this;
                    self.table.data.splice(0, self.table.data.length);
                    if(self.currentTab === "main"){
                        ajax.post('/signal/control/task/query/main/all', {
                            pageSize: pageSize,
                            currentPage: currentPage
                        }, function(data){
                            if(data){
                                for(var i=0; i< data.rows.length; i++){
                                    var row = data.rows[i];
                                    row.srcIpCopy = row.srcIp;
                                    row.srcPortCopy = row.srcPort;
                                    self.table.data.push(row);
                                }
                                self.total = data.total;
                            }
                        }, null, ajax.NO_ERROR_CATCH_CODE);
                    }
                    if(self.currentTab === "backup"){
                        ajax.post('/signal/control/task/query/backup/all', {
                            pageSize: pageSize,
                            currentPage: currentPage
                        }, function(data){
                            if(data){
                                for(var i=0; i< data.rows.length; i++){
                                    var row = data.rows[i];
                                    row.srcIpCopy = row.srcIp;
                                    row.srcPortCopy = row.srcPort;
                                    self.table.data.push(row);
                                }
                                self.total = data.total;
                            }
                        }, null, ajax.NO_ERROR_CATCH_CODE);
                    }
                },
                rowSwitchTask: function(scope){
                    var self = this;
                    var row = scope.row;
                    if(row.srcIp != row.srcIpCopy || row.srcPort != row.srcPortCopy){
                        ajax.post('/signal/control/task/switch', {
                            taskId: row.taskId,
                            newIp: row.srcIp,
                            newPort: row.srcPort
                        }, function(data){
                            self.$message({
                                message:'切换完成！',
                                type:'success'
                            });
                            row.srcIpCopy = row.srcIp;
                            row.srcPortCopy = row.srcPort;
                        }, null, ajax.NO_ERROR_CATCH_CODE);
                    }
                },
                rowRepeatTask: function(scope){
                    var self = this;
                    var row = scope.row;
                    row.srcIp = row.srcIpCopy;
                    row.srcPort = row.srcPortCopy;
                },
                handleSizeChange: function(val){
                    var self = this;
                    self.loadTasks(val, self.currentPage);
                },
                handleCurrentChange: function(val){
                    var self = this;
                    self.loadTasks(self.pageSize, val);
                },
                resumeTaskButton:function(){
                    var self = this;
                    self.button.resume.loading = true;
                    ajax.post('/signal/control/task/resume', null, function(){

                        self.button.resume.loading = false;

                    }, null, ajax.NO_ERROR_CATCH_CODE);
                }
            },
            mounted:function(){
                var self = this;
                self.loadTasks(self.pageSize, self.currentPage);
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