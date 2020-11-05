define([
    'text!' + window.APPPATH + 'bvc2-signal-control/bvc2-terminal-repeater/bvc2-terminal-repeater.html',
    'restfull',
    'jquery',
    'vue',
    'element-ui',
    'bvc2-header',
    'bvc2-nav-side'
], function(tpl, ajax, $, Vue){

    //组件名称
    var pageId = 'bvc2-terminal-repeater';

    var interval = '';

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
                active:'2',
                tree:{
                    loading: false,
                    data: [],
                    props:{
                        label: 'name',
                        children: 'children'
                    },
                    current:''
                },
                table:{
                    data:[],
                    selectionRow:[]

                },
                dialog:{
                    visible:false,
                    rows:[],
                    loading:false,
                    selected:[],
                    search:''
                },
                button:{
                    create:{
                        loading:false
                    },
                    destory:{
                        loading:false
                    }
                },
                task:{
                    total:0,
                    success:0
                },
                interval:''
                //bind:{
                //    pageSizes:[100, 200, 300, 400],
                //    pageSize:100,
                //    currentPage:1,
                //    total:0
                //}
            },
            methods: {
                renderContent:function(h, target){
                    var node = target.node;

                    var c = {};
                    c[node.icon] = true;
                    var classes = [c];

                    return h('span', {
                        class: {
                            'bvc2-tree-node-custom': true
                        }
                    }, [
                        h('span', null, [
                            h('span', {
                                class: classes,
                                style: {
                                    'font-size': '16px',
                                    'position': 'relative',
                                    'top': '1px',
                                    'margin-right': '5px'
                                }
                            }, null),
                            node.label
                        ])
                    ]);
                },
                loadTerminals:function(id){
                    var self = this;
                    self.table.data.splice(0, self.table.data.length);
                    ajax.post('/signal/control/terminal/query/' + id, null, function(data){
                        if(data && data.length > 0){
                            for(var i=0; i<data.length; i++){
                                self.table.data.push(data[i]);
                            }
                        }
                    });
                },
                loadUnbindTerminal: function(){
                    var self = this;
                    self.dialog.rows.splice(0, self.dialog.rows.length);
                    ajax.post('/signal/control/terminal/query/unbind', null, function(data){
                       if(data && data.length > 0){
                           for(var i=0; i<data.length; i++){
                               self.dialog.rows.push(data[i]);
                           }
                       }
                    });
                },
                updateTerminal: function(){
                    var self = this;
                    self.table.data.splice(0, self.table.data.length);
                    ajax.post('/signal/control/terminal/update/' + self.tree.current.id, null, function(data){
                        if(data && data.length > 0){
                            for(var i=0; i<data.length; i++){
                                self.table.data.push(data[i]);
                            }
                        }
                    });
                },
                loadRepeaters: function(){
                    var self = this;
                    self.tree.data.splice(0, self.tree.data.length);
                    self.tree.loading = true;
                    ajax.post('/signal/control/terminal/query/repeater', null , function(data){
                        if(data){
                            self.tree.data.push(data);
                            self.tree.loading = false;
                        }
                    });
                },
                currentTreeNodeChange: function(data){
                    var self = this;
                    if(!data || data.type != "INTERNET_ACCESS"){
                        self.tree.current = '';
                        return;
                    }
                    self.tree.current = data;
                    self.loadTerminals(data.id);
                },
                bindTerminal:function(){
                    var self = this;
                    self.dialog.visible = true;
                    self.loadUnbindTerminal();
                },
                unbindTerminal: function(){
                    var self = this;
                    var terminals = self.table.data;
                    var selects = self.table.selectionRow;
                    if(selects && selects.length > 0){

                        self.$confirm('是否要解绑所选终端？', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning',
                            beforeClose: function (action, instance, d) {
                                if (action === 'confirm') {

                                    instance.confirmButtonLoading = true;
                                    instance.confirmButtonText = '执行中...';

                                    var ids = [];
                                    for(var i=0; i<selects.length; i++){
                                        ids.push(selects[i].id);
                                    }
                                    ajax.remove('/signal/control/terminal/unbind/all', {ids:ids}, function(data, status){

                                        instance.confirmButtonLoading = false;
                                        instance.confirmButtonText = '确定';

                                        for(var i=0; i<selects.length; i++){
                                            for(var j=0; j<terminals.length; j++){
                                                if(terminals[j] === selects[i]){
                                                    terminals.splice(j, 1);
                                                    break;
                                                }
                                            }
                                        }

                                        d();
                                        self.$message({
                                            type: 'success',
                                            message: '解绑成功！'
                                        });
                                    });

                                } else if (action === 'cancel') {
                                    d();
                                    self.$message({
                                        type: 'info',
                                        message: '操作取消！'
                                    });
                                }
                            }

                        });

                    }else{
                        self.$message({
                            message:'您没有选择任何数据！',
                            type:'warning'
                        });
                    }

                },
                rowDelete: function(scope){
                    var self = this;
                    var terminals = self.table.data;
                    self.$confirm('是否要解绑' + scope.row.bundleName + '终端？', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning',
                        beforeClose: function (action, instance, d) {
                            if (action === 'confirm') {

                                instance.confirmButtonLoading = true;
                                instance.confirmButtonText = '执行中...';

                                ajax.post('/signal/control/terminal/unbind/' + scope.row.id, null, function(data){

                                    instance.confirmButtonLoading = false;
                                    instance.confirmButtonText = '确定';

                                    for(var i=0; i<terminals.length; i++){
                                        if(terminals[i].id == scope.row.id){
                                            terminals.splice(i, 1);
                                        }
                                    }

                                    d();
                                    self.$message({
                                        type: 'success',
                                        message: '解绑成功！'
                                    });
                                });

                            } else if (action === 'cancel') {
                                d();
                                self.$message({
                                    type: 'info',
                                    message: '操作取消！'
                                });
                            }
                        }

                    });

                },
                handleTerminalClose:function(){
                    var self = this;
                    self.dialog.visible = false;
                    self.dialog.rows.splice(0, self.dialog.rows.length);
                    self.dialog.loading = false;
                    self.dialog.selected.splice(0, self.dialog.selected.length);
                },
                handleTerminalSelectionChange:function(val){
                    var self = this;
                    if(val) self.selected = val;
                },
                handleTerminalBindingOk:function(){
                    var self = this;
                    var selected = self.selected;
                    if(!selected || selected.length<=0){
                        self.$message({
                            message:'您没有选择任何数据！',
                            type:'warning'
                        });
                        return;
                    }

                    ajax.post('/signal/control/terminal/bind', {
                        accessId: self.tree.current.id,
                        selected: $.toJSON(selected)
                    }, function(data){
                        if(data && data.length > 0){
                            for(var i=0; i<data.length; i++){
                                self.table.data.push(data[i]);
                            }
                        }
                        self.$message({
                            message:'绑定成功！',
                            type:'success'
                        });
                        self.handleTerminalClose();
                    });
                },
                generateTaskButton:function(){
                    var self = this;
                    self.button.create.loading = true;
                    self.button.destory.loading = true;
                    ajax.post('/signal/control/terminal/generate/task', null, function(data){
                        self.$message({
                            message:'同步成功！',
                            type:'success'
                        });
                        self.button.create.loading = false;
                        self.button.destory.loading = false;
                    }, null, [403]);
                },
                removeTaskButton:function(){
                    var self = this;
                    self.button.create.loading = true;
                    self.button.destory.loading = true;
                    ajax.post('/signal/control/terminal/remove/task', null, function(data){
                        self.$message({
                            message:'销毁成功！',
                            type:'success'
                        });
                        self.button.create.loading = false;
                        self.button.destory.loading = false;
                    });
                },
                handleSelectionChange:function(val){
                    this.table.selectionRow = val;
                },
                loadTasks:function(){
                    var self = this;
                    ajax.post('/signal/control/task/count', null, function(data){
                        self.task.total = data.total;
                        self.task.success = data.success;
                    });
                    self.interval = setInterval(function(){
                        ajax.post('/signal/control/task/count', null, function(data){
                            self.task.total = data.total;
                            self.task.success = data.success;
                        });
                    }, 3000);

                    interval = self.interval;
                }
                //bindHandleSizeChange: function(val){
                //    var self = this;
                //},
                //bindHandleCurrentChange: function(val){
                //    var self = this;
                //}
            },
            mounted:function(){
                var self = this;
                self.loadRepeaters();
                self.loadTasks();
            }

        });
    };

    var destroy = function(){
        clearInterval(interval);
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