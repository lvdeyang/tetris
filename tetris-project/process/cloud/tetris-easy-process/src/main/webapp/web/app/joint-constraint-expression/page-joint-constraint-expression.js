/**
 * Created by lvdeyang on 2018/12/18 0018.
 */
define([
    'text!' + window.APPPATH + 'joint-constraint-expression/page-joint-constraint-expression.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'joint-constraint-expression/page-joint-constraint-expression.css'
], function(tpl, config, $, ajax, context, commons, Vue){

    var pageId = 'page-joint-constraint-expression';

    var init = function(p){

        var serviceId = p.serviceId;
        var serviceName = p.serviceName;
        var serviceType = p.serviceType;
        var accessPointId = p.accessPointId;
        var accessPointName = p.accessPointName;

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el: '#' + pageId + '-wrapper',
            data: {
                activeId:'page-service-rest',
                menus: context.getProp('menus'),
                user: context.getProp('user'),
                groups: context.getProp('groups'),
                serviceName:serviceName,
                accessPointName:accessPointName,
                table:{
                    rows:[],
                    pageSize:50,
                    currentPage:0,
                    total:0
                },
                dialog:{
                    createConstraint:{
                        visible:false,
                        name:'',
                        expression:'',
                        loading:false
                    },
                    selectParam:{
                        visible:false,
                        rows:[],
                        selected:''
                    }
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                rowKey:function(row){
                    return 'joint-constraint-expression-' + row.uuid;
                },
                gotoService:function(){
                    var self = this;
                    if(serviceType === 'REST'){
                        return '#/page-service-rest';
                    }
                },
                gotoAccessPoint:function(){
                    var self = this;
                    return '#/page-access-point/' + serviceId + '/' + serviceName + '/' + serviceType;
                },
                handleCreate:function(){
                    var self = this;
                    self.dialog.createConstraint.visible = true;
                },
                handleCreateConstraintClose:function(){
                    var self = this;
                    self.dialog.createConstraint.name = '';
                    self.dialog.createConstraint.expression = '';
                    self.dialog.createConstraint.loading = false;
                    self.dialog.createConstraint.visible = false;
                },
                handleCreateConstraintSubmit:function(){
                    var self = this;
                    self.dialog.createConstraint.loading = true;
                    ajax.post('/joint/constraint/expression/add', {
                        accessPointId:accessPointId,
                        name:self.dialog.createConstraint.name,
                        expression:self.dialog.createConstraint.expression
                    }, function(data, status){
                        self.dialog.createConstraint.loading = false;
                        if(status !== 200) return;
                        self.table.rows.push(data);
                        self.handleCreateConstraintClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE)
                },
                handleInsertParam:function(){
                    var self = this;
                    self.dialog.selectParam.visible = true;
                    ajax.post('/access/point/param/list/all', {
                        accessPointId:accessPointId
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.dialog.selectParam.rows.push(data[i]);
                            }
                        }
                    });
                },
                handleDelete:function(){

                },
                handleCurrentChange:function(currentPage){
                    var self = this;
                    self.load(currentPage);
                },
                handleRowDelete:function(scope){
                    var self = this;
                    var row = scope.row;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'危险操作',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['此操作将永久删除该约束，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/joint/constraint/expression/delete/' + row.id, null, function(data, status){
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
                load:function(currentPage){
                    var self = this;
                    self.table.rows.splice(0, self.table.rows.length);
                    ajax.post('/joint/constraint/expression/list', {
                        accessPointId:accessPointId,
                        currentPage:currentPage,
                        pageSize:self.table.pageSize
                    }, function(data){
                        var total = data.total;
                        var rows = data.rows;
                        if(rows && rows.length>0){
                            for(var i=0; i<rows.length; i++){
                                self.table.rows.push(rows[i]);
                            }
                            self.table.total = total;
                        }
                        self.table.currentPage = currentPage;
                    });
                },
                handleSelectParamClose:function(){
                    var self = this;
                    self.dialog.selectParam.rows.splice(0, self.dialog.selectParam.rows.length);
                    self.dialog.selectParam.selected = '';
                    self.dialog.selectParam.visible = false;
                },
                handleSelectParamSubmit:function(){
                    var self = this;
                    var selected = self.dialog.selectParam.selected;
                    if(!selected) {
                        self.$message({
                            type:'error',
                            message:'您没有选中任何数据！'
                        });
                        return;
                    }
                    var expression = '#' + selected.primaryKey;
                    self.dialog.createConstraint.expression += expression;
                    self.$refs.constraintTextArea.$refs.textarea.focus();
                    self.handleSelectParamClose();
                },
                handleSelectParamCurrentChange:function(row){
                    var self = this;
                    self.dialog.selectParam.selected = row;
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
        path:'/' + pageId + '/:serviceId/:serviceName/:serviceType/:accessPointId/:accessPointName',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});