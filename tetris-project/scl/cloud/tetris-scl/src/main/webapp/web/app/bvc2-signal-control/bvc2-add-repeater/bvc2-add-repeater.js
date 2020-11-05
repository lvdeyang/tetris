define([
    'text!' + window.APPPATH + 'bvc2-signal-control/bvc2-add-repeater/bvc2-add-repeater.html',
    'restfull',
    'jquery',
    'vue',
    'element-ui',
    'bvc2-header',
    'bvc2-nav-side'
], function(tpl, ajax, $, Vue){

    //组件名称
    var pageId = 'bvc2-add-repeater';

    var init = function(){

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        //查询转发器类型
        var repeaterType = [];
        var mainRepeaters = [];
        ajax.post('/signal/control/repeater/query/type', null, function(data) {
            var types = data.type;
            var mains = data.repeaters;
            if(types && types.length > 0){
                for(var i=0; i<types.length; i++){
                    var type = {
                        value: types[i],
                        label: types[i]
                    };
                    repeaterType.push(type);
                }
            }
            if(mains && mains.length > 0){
                for(var j=0; j<mains.length; j++){
                    var repeater = {
                        value: mains[j].id,
                        label: mains[j].name
                    };
                    mainRepeaters.push(repeater);
                }
            }
        });

        new Vue({
            el:'#' + pageId + '-wrapper',
            data:{
                header:{
                    links:[],
                    user:''
                },
                active:'0',
                table:{
                    data:[]
                },
                type: repeaterType,
                main: mainRepeaters,
                dialog:{
                    addRepeater:{
                        name:'',
                        ip:'',
                        address:'',
                        type:'',
                        main:'',
                        loading:false,
                        visible: false
                    }
                },
                status:"main"
            },
            methods: {
                loadRepeaters: function(){
                   var self = this;
                    self.table.data.splice(0, self.table.data.length);
                    ajax.post('/signal/control/repeater/query/list', null, function(data){
                        if(data && data.length > 0){
                            for(var i=0; i< data.length; i++){
                                self.table.data.push(data[i]);
                            }
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                handleAddRepeaterClose:function(){
                    var self = this;
                    self.dialog.addRepeater.name = '';
                    self.dialog.addRepeater.ip = '';
                    self.dialog.addRepeater.address = '';
                    self.dialog.addRepeater.type = '';
                    self.dialog.addRepeater.main = '';
                    self.dialog.addRepeater.visible = false;
                },
                handleAddRepeaterCommit:function(){
                    var self = this;
                    var repeaterName = self.dialog.addRepeater.name;
                    var repeaterIp = self.dialog.addRepeater.ip;
                    var repeaterAddress = self.dialog.addRepeater.address;
                    var repeaterType = self.dialog.addRepeater.type;
                    var repeaterMain = self.dialog.addRepeater.main;

                    if(repeaterName == '' || repeaterName == null){
                        self.$message({
                            message: '名称不能为空',
                            type: 'warning'
                        });
                        return;
                    }

                    if(repeaterIp == '' || repeaterIp == null){
                        self.$message({
                            message: 'ip不能为空',
                            type: 'warning'
                        });
                        return;
                    }

                    if(repeaterType == '' || repeaterType == null){
                        self.$message({
                            message: '类型不能为空',
                            type: 'warning'
                        });
                        return;
                    }

                    if(repeaterType == '备份' && repeaterMain == ''){
                        self.$message({
                            message: '请选择主机',
                            type: 'warning'
                        });
                        return;
                    }

                    self.dialog.addRepeater.loading = true;
                    ajax.post('/signal/control/repeater/add', {
                        name: repeaterName,
                        address: repeaterAddress,
                        ip: repeaterIp,
                        type: repeaterType,
                        main: repeaterMain
                    }, function(data, status) {
                        self.dialog.addRepeater.loading = false;
                        if(status !== 200) return;
                        self.table.data.push(data);

                        if(self.dialog.addRepeater.type == '主'){
                            self.$confirm('是否要继续添加网口？', '提示', {
                                confirmButtonText: '确定',
                                cancelButtonText: '取消',
                                type: 'info',
                                beforeClose:function(action, ins, d){
                                    if(action === 'confirm'){
                                        d();
                                        window.location.hash = '#/bvc2-internet-access/' + data.id;
                                    }else if(action === 'cancel'){
                                        d();
                                    }
                                }
                            });
                        }

                        self.handleAddRepeaterClose();
                    });
                },
                rowBindInterAccess:function(scope){
                    window.location.hash = '#/bvc2-internet-access/' + scope.row.id;
                },
                rowEdit:function(scope){
                    var self = this;

                },
                rowDelete:function(scope){
                    var self = this;
                    var repeaters = self.table.data;

                    self.$confirm('是否要删除' + scope.row.name + '流转发器？', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning',
                        beforeClose: function (action, instance, d) {
                            if (action === 'confirm') {

                                instance.confirmButtonLoading = true;
                                instance.confirmButtonText = '执行中...';

                                ajax.post('/signal/control/repeater/remove/' + scope.row.id, null, function(data){

                                    instance.confirmButtonLoading = false;
                                    instance.confirmButtonText = '确定';

                                    for(var i=0; i<repeaters.length; i++){
                                        if(repeaters[i].id == scope.row.id){
                                            repeaters.splice(i, 1);
                                        }
                                    }

                                    d();
                                    self.$message({
                                        type: 'success',
                                        message: '删除成功！'
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
                switchMain:function(){
                    var self = this;
                    ajax.post('/signal/control/repeater/switch/main', null, function(data){
                        self.status = "main";
                    });
                },
                switchBackup:function(){
                    var self = this;
                    ajax.post('/signal/control/repeater/switch/backup', null, function(data){
                        self.status = "backup";
                    });
                }
            },
            mounted:function(){
                var self = this;
                self.loadRepeaters();
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