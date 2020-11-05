define([
    'text!' + window.APPPATH + 'bvc2-signal-control/bvc2-internet-access/bvc2-internet-access.html',
    'restfull',
    'jquery',
    'vue',
    'element-ui',
    'bvc2-header',
    'bvc2-nav-side'
], function(tpl, ajax, $, Vue){

    //组件名称
    var pageId = 'bvc2-internet-access';

    var init = function(p){

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        //查询转发器类型
        var interAccessType = [];
        ajax.post('/signal/control/repeater/internet/access/type', null, function(data) {
            var types = data.type;
            if(types && types.length > 0){
                for(var i=0; i<types.length; i++){
                    var type = {
                        value: types[i],
                        label: types[i]
                    };
                    interAccessType.push(type);
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
                type: interAccessType,
                dialog:{
                    addInterAccess:{
                        address:'',
                        type:'',
                        loading:false,
                        visible: false
                    }
                }
            },
            methods: {
                loadInterAccess: function(id){
                    var self = this;
                    self.table.data.splice(0, self.table.data.length);
                    ajax.post('/signal/control/repeater/internet/access/query/list/' + id, null, function(data){
                        if(data && data.length > 0){
                            for(var i=0; i< data.length; i++){
                                self.table.data.push(data[i]);
                            }
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                handleAddInterAccessClose:function(){
                    var self = this;
                    self.dialog.addInterAccess.address = '';
                    self.dialog.addInterAccess.type = '';
                    self.dialog.addInterAccess.visible = false;
                },
                handleAddInterAccessCommit:function(){
                    var self = this;
                    var interAccessAddress = self.dialog.addInterAccess.address;
                    var interAccessType = self.dialog.addInterAccess.type;

                    if(interAccessAddress == '' || interAccessAddress == null){
                        self.$message({
                            message: 'ip不能为空',
                            type: 'warning'
                        });
                        return;
                    }

                    if(interAccessType == '' || interAccessType == null){
                        self.$message({
                            message: '类型不能为空',
                            type: 'warning'
                        });
                        return;
                    }

                    self.dialog.addInterAccess.loading = true;
                    ajax.post('/signal/control/repeater/internet/access/add/' + p.id, {
                        address: interAccessAddress,
                        type: interAccessType
                    }, function(data, status) {
                        self.dialog.addInterAccess.loading = false;
                        if(status !== 200) return;
                        self.table.data.push(data);
                        self.handleAddInterAccessClose();
                    }, null, [403]);
                },
                rowEdit:function(scope){
                    var self = this;

                },
                rowDelete:function(scope){
                    var self = this;
                    var repeaters = self.table.data;
                    ajax.post('/signal/control/repeater/internet/access/remove/' + scope.row.id, null, function(data){
                        for(var i=0; i<repeaters.length; i++){
                            if(repeaters[i].id == scope.row.id){
                                repeaters.splice(i, 1);
                            }
                        }
                    });
                }
            },
            mounted:function(){
                var self = this;
                self.loadInterAccess(p.id);
            }

        });
    };

    var destroy = function(){

    };

    var page = {
        path:'/' + pageId + '/:id',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return page;
});