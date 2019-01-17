/**
 * Created by lvdeyang on 2018/12/24 0024.
 */
define([
    'text!' + window.APPPATH + 'process-design/page-process-design.html',
    'config',
    'restfull',
    'jquery',
    'context',
    'commons',
    'bpmn-ext',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'process-design/page-process-design.css'
], function(tpl, config, ajax, $, context, commons, BpmnExtJS, Vue){

    var pageId = 'page-process-design';

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
                bpmnExtInstance:''
            },
            computed:{

            },
            watch:{

            },
            methods:{

            },
            mounted:function(){
                var self = this;
                //获取xml + entries
                ajax.post('/process/query/bpmn/and/entries', {
                    id:processId
                }, function(data){
                    var bpmn = data.bpmn;
                    var entries = data.groupEntries;
                    var primaryKey = data.processId;
                    var uuid = data.uuid;
                    self.bpmnExtInstance = new BpmnExtJS({
                        id:primaryKey,
                        uuid:uuid,
                        container:'#bpmn-editor',
                        nodeType:['user', 'service', 'gateway'],
                        entries:entries || [],
                        xml:bpmn,
                        onReady:function(){
                            console.log('导入成功！');
                        },
                        onSave:function(xml, endLoading){
                            var bpmnExtInstance = this;
                            var referenceIds = bpmnExtInstance.queryServiceReferenceIds();
                            ajax.post('/process/save/bpmn/' + processId, {
                                bpmn:xml,
                                accessPointIds: $.toJSON(referenceIds)
                            }, function(data, status){
                                endLoading();
                                if(status !== 200) return;
                                self.$message({
                                    type:'success',
                                    message:'保存成功'
                                });
                            }, null, ajax.NO_ERROR_CATCH_CODE);
                        },
                        onSaveVariable:function(variable, done, endLoading){
                            ajax.post('/process/variable/add', $.extend(true, {processId:processId}, variable), function(data, status){
                                endLoading();
                                if(status !== 200) return;
                                done(data);
                                self.$message({
                                    type:'success',
                                    message:'操作成功！'
                                });
                            }, null, ajax.NO_ERROR_CATCH_CODE);
                        },
                        onDeleteVariable:function(rowId, done){
                            ajax.post('/process/variable/delete/' + rowId, null, function(data){
                                done();
                                self.$message({
                                    type:'success',
                                    message:'操作成功！'
                                });
                            });
                        },
                        queryVariables:function(fn){
                            ajax.post('/process/variable/list/all', {
                                processId:processId
                            }, function(data){
                                if(typeof fn === 'function') fn(data);
                            });
                        },
                        queryUsers:function(){

                        },
                        totalUsers:function(){

                        },
                        queryRoles:function(){

                        },
                        totalRoles:function(){

                        }
                    });
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