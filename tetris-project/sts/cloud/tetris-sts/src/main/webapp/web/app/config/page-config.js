/**
 * Created by lvdeyang on 2018/12/24 0024.
 */
define([
    'text!' + window.APPPATH + 'config/page-config.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'config/page-config.css'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-config';

    var init = function(){

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el: '#' + pageId + '-wrapper',
            data: {
                menus: context.getProp('menus'),
                user: context.getProp('user'),
                groups: context.getProp('groups'),
                classifies:[],
                table: {
                    rows: [],
                    pageSize: 50,
                    pageSizes: [50, 100, 200, 400],
                    currentPage: 0,
                    total: 0
                },
                dialog: {
                    createNetGroup:{
                        visible:false,
                        netName:'',
                        info:'',
                        netType:'',
                        loading:false
                    },
                }
            },

            computed:{

            },
            watch:{

            },
            methods: {
                rowKey:function(row){
                    return 'netGroup-' + row.uuid;
                },
                load: function (currentPage) {
                    var self = this;
                    self.table.rows.splice(0, self.table.rows.length);
                    ajax.get('/netGroup/list', {
                        currentPage:currentPage,
                        pageSize:self.table.pageSize
                    }, function(data){
                        debugger
                        var total = data.length;
                        var rows = data;
                        if(rows && rows.length>0){
                            for(var i=0; i<rows.length; i++){
                                self.table.rows.push(rows[i]);
                            }
                            self.table.total = total;
                        }
                        self.table.currentPage = currentPage;
                    });
                },
                handleCreate:function () {
                    var self = this;
                    self.dialog.createNetGroup.visible = true;
                },
                addNetGroup:function(){
                    var self = this;
                    self.dialog.createNetGroup.loading = true;
                    var params = {
                        netName:self.dialog.createNetGroup.netName,
                        info:self.dialog.createNetGroup.info,
                        netType:self.dialog.createNetGroup.netType,
                    };
                    ajax.post('/netGroup/add', params, function(data, status){
                        self.dialog.createNetGroup.loading = false;
                        if(status !== 200) return;
                        self.table.rows.push(data);
                        self.closeNetGroupDialog();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                handleRowDelete:function(scope){
                    var self = this;
                    var row = scope.row;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'删除网卡分组',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['即将删除网卡分组，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/netGroup/delete/' + row.id, null, function(data, status){
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
                handleDelete:function () {
                    
                },
                handleSizeChange:function(size){
                    var self = this;
                    self.table.pageSize = size;
                    self.load(self.table.currentPage);
                },
                closeNetGroupDialog:function(){
                    var self = this;
                    self.dialog.createNetGroup.netName = '';
                    self.dialog.createNetGroup.info = '';
                    self.dialog.createNetGroup.netType = '';//todo
                    self.dialog.createNetGroup.visible = false;
                }

            },
            created: function () {
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