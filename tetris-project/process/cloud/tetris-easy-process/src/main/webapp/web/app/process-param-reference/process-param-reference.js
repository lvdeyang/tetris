/**
 * Created by lvdeyang on 2018/12/18 0018.
 */
define([
    'text!' + window.APPPATH + 'process-param-reference/process-param-reference.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'process-param-dialog',
    'css!' + window.APPPATH + 'process-param-reference/process-param-reference.css'
], function(tpl, config, $, ajax, context, commons, Vue){

    var pageId = 'page-process-param-reference';

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
                processName:processName,
                table:{
                    rows:[]
                },
                dialog:{
                    processVariable:{
                        action:'',
                        currentReference:''
                    }
                }
            },
            methods:{
                rowKey:function(row){
                    return 'process-param-reference-' + row.uuid;
                },
                handleRowDelete:function(scope){
                    var self = this;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'危险操作',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['此操作将永久删除该映射，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                var row = scope.row;
                                ajax.post('/process/param/reference/delete/' + row.id, null, function(data, status){
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
                handleTagDelete:function(tag, scope){
                    var self = this;
                    var row = scope.row;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'危险操作',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['此操作将永久删除该映射，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                var reference = [];
                                if(!row.reference || row.reference.length<=0) {
                                    done();
                                    return;
                                }
                                for(var i=0; i<row.reference.length; i++){
                                    if(row.reference[i] !== tag){
                                        reference.push(row.reference[i]);
                                    }
                                }
                                ajax.post('/process/param/reference/edit/' + row.id, {
                                    reference: $.toJSON(reference)
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    if(status !== 200) return;
                                    row.reference.splice(0, row.reference.length);
                                    for(var i=0; i<reference.length; i++){
                                        row.reference.push(reference[i]);
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
                handleTagAdd:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.processVariable.currentReference = row;
                    self.dialog.processVariable.action = 'edit';
                    self.$refs.processParam.open(processId);
                },
                handleCreate:function(){
                    var self = this;
                    self.dialog.processVariable.action = 'create';
                    self.$refs.processParam.open(processId);
                },
                handleDelete:function(){

                },
                onAccessPointParamSelected:function(param, startLoading, endLoading, done){
                    var self = this;
                    if(self.dialog.processVariable.action === 'create'){
                        startLoading();
                        ajax.post('/process/param/reference/add', {
                            processId:processId,
                            reference: $.toJSON([param])
                        }, function(data, status){
                            endLoading();
                            if(status !== 200) return;
                            self.table.rows.push(data);
                            done();
                        }, null, ajax.NO_ERROR_CATCH_CODE);
                    }else if(self.dialog.processVariable.action === 'edit'){
                        var row = self.dialog.processVariable.currentReference;
                        if(row.reference && row.reference.length>0){
                            for(var i=0; i<row.reference.length; i++){
                                if(row.reference[i] === param){
                                    self.$message({
                                        type:'warning',
                                        message:'当前参数已经添加！'
                                    });
                                    return;
                                }
                            }
                        }
                        startLoading();
                        var reference = [];
                        for(var i=0; i<row.reference.length; i++){
                            reference.push(row.reference[i]);
                        }
                        reference.push(param);
                        ajax.post('/process/param/reference/edit/' + row.id, {
                            reference: $.toJSON(reference)
                        }, function(data, status){
                            endLoading();
                            if(status !== 200) return;
                            row.reference.push(param);
                            done();
                        }, null, ajax.NO_ERROR_CATCH_CODE);
                    }
                }
            },
            created:function(){
                var self = this;
                self.table.rows.splice(0, self.table.rows.length);
                ajax.post('/process/param/reference/list', {
                    processId:processId
                }, function(data){
                    if(data && data.length>0){
                        for(var i=0; i<data.length; i++){
                            self.table.rows.push(data[i]);
                        }
                    }
                });
            }
        });

    };

    var destroy = function(){

    };

    var groupList = {
        path:'/' + pageId + '/:processId/:processName',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});