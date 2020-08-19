/**
 * Created by lvdeyang on 2018/12/24 0024.
 */
define([
    'text!' + window.APPPATH + 'process-task-my-start/page-process-task-my-start.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'process-task-my-start/page-process-task-my-start.css'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-process-task-my-start';

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
                    pageSizes:[50, 100, 200, 300, 400],
                    pageSize:50,
                    total:0,
                    currentRow:''
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                rowKey:function(row){
                    return '_' + row.processInstanceId;
                },
                handleCurrentChange:function(currentPage){
                    var self = this;
                    self.load(currentPage);
                },
                handleSizeChange:function(size){
                    var self = this;
                    self.table.pageSize = size;
                    self.load(1);
                },
                load:function(currentPage){
                    var self = this;
                    self.table.rows.splice(0, self.table.rows.length);
                    ajax.post('/process/query/my/start/process', {
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