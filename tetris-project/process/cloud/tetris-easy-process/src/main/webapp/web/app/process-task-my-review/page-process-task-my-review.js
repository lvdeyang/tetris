/**
 * Created by lvdeyang on 2018/12/24 0024.
 */
define([
    'text!' + window.APPPATH + 'process-task-my-review/page-process-task-my-review.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'mi-lightbox',
    'css!' + window.APPPATH + 'process-task-my-review/page-process-task-my-review.css'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-process-task-my-review';

    var init = function(p){

        var processId = p.processId;
        var processName = p.processName;

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el: '#' + pageId + '-wrapper',
            data: {
                activeId:'page-process',
                menus: context.getProp('menus'),
                user: context.getProp('user'),
                groups: context.getProp('groups'),
                table:{
                    rows:[],
                    currentPage:1,
                    pageSize:50,
                    total:0,
                    currentRow:''
                },
                review:{
                    history:'',
                    show:'',
                    set:''
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                rowKey:function(row){
                    return row.processDefinitionId + '_' + row.processInstanceId + '_' + row.taskId;
                },
                currentRowChange:function(row){
                    var self = this;
                    self.table.currentRow = row;
                    ajax.post('/process/query/variables/by/task/definition/key', {
                        processInstanceId:row.processInstanceId,
                        taskDefinitionKey:row.taskDefinitionKey
                    }, function(data){
                        var history = data.history;
                        var show = data.show;
                        var set = data.set;
                        self.review.history = history;
                        self.review.show = show;
                        self.review.set = set;
                    });
                },
                handleCurrentChange:function(currentPage){
                    var self = this;
                    self.load(currentPage);
                },
                load:function(currentPage){
                    var self = this;
                    self.table.rows.splice(0, self.table.rows.length);
                    ajax.post('/process/query/my/task/preview', {
                        currentPage:currentPage,
                        pageSize:self.table.pageSize
                    }, function(data){
                        var total = data.total;
                        var rows = data.rows;
                        if(rows && rows.length>0){
                            for(var i=0; i<rows.length; i++){
                                self.table.rows.push(rows[i]);
                            }
                        }
                        self.table.total = total;
                        self.table.currentPage = currentPage;
                    });
                },
                doPreview:function(url, type){
                    var self = this;
                    self.$refs.miLightBox.preview(url, type);
                },
                doReview:function(){
                    var self = this;

                    var task = self.table.currentRow;
                    ajax.post('/process/do/review', {
                        taskId:task.taskId,
                        variables: $.toJSON(self.review.set)
                    }, function(){
                        self.table.currentRow = '';
                        for(var i=0; i<self.table.rows.length; i++){
                            if(self.table.rows[i] === task){
                                self.table.rows.splice(i, 1);
                                break;
                            }
                        }
                        self.table.total = self.table.total - 1;
                        if(self.table.rows.length===0 && self.table.currentPage>1){
                            self.load(self.table.currentPage-1);
                        }
                        self.$message({
                            type:'success',
                            message:'操作成功！'
                        });
                    });
                },
                processPreview:function(scope){
                    var self = this;
                    var row = scope.row;
                    var token = window.TOKEN;
                    var sessionId = window.SESSIONID;
                    var processInstanceId = row.processInstanceId;
                    window.open(window.BASEPATH + 'index/display/'+token+'/'+sessionId+'/'+processInstanceId, '_blank', 'status=no,menubar=yes,toolbar=no,width=1366,height=580,left=100,top=100');
                }
            },
            created:function(){
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