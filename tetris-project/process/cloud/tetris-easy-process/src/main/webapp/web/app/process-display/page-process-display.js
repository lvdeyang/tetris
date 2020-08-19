require.config({
    baseUrl: window.BASEPATH,
    paths: {
        /* lib */
        'text':window.LIBPATH + 'frame/requireJS/plugins/text',
        'css':window.LIBPATH + 'frame/requireJS/plugins/css',
        'vue':window.LIBPATH + 'frame/vue/vue-2.5.16',
        'jquery':window.LIBPATH + 'frame/jQuery/jquery-2.2.3.min',
        'json':window.LIBPATH + 'frame/jQuery/jquery.json',
        'element-ui':window.LIBPATH + 'ui/element-ui/element-ui-2.4.3.min',

        'TweenLite':window.LIBPATH + 'TweenMax/cmd/TweenLite',

        /* commons */
        'context':window.COMMONSPATH + 'context/context',
        'page-wrapper':window.COMMONSPATH + 'page/page-wrapper',
        'date':window.COMMONSPATH + 'date/date.ext',
        'string':window.COMMONSPATH + 'string/string.ext',
        'storage':window.COMMONSPATH + 'storage/storage.ext',
        'restfull':window.COMMONSPATH + 'restfull/restfull.api',
        'file':window.COMMONSPATH + 'uploader/File',
        'uploader':window.COMMONSPATH + 'uploader/Uploader',
        'menu':window.COMMONSPATH + 'menu/menu',
        'bpmn-viewer':window.COMMONSPATH + 'bpmn/ext/BpmnExtension',

        'config':window.APPPATH + 'config',
        'commons':window.APPPATH + 'commons'

    },
    shim:{
        'vue':{
            exports: 'Vue'
        },
        'element':{
            deps:['vue']
        },
        'jquery':{
        	exports:'jQuery'
        },
        'json':{
        	deps:['jquery'],
        	exports:'jQuery'
        }
    }
});

require([
    'text!' + window.COMMONSPATH + 'bpmn/ext/empty.bpmn',
    'text!' + window.APPPATH + 'process-display/page-process-display.html',
    'vue',
    'json',
    'bpmn-viewer',
    'element-ui',
    'css!' + window.APPPATH + 'reset.css',
    'css!' + window.APPPATH + 'process-display/page-process-display.css',
    'css!' + window.COMMONSPATH + 'bpmn/assets/diagram-js.css',
    'css!' + window.COMMONSPATH + 'bpmn/assets/bpmn-font/css/bpmn.css',
    'css!' + window.COMMONSPATH + 'bpmn/ext/css/bpmn-view.css'
], function(demo, tpl, Vue, $, BpmnViewer){

    document.getElementById('app').innerHTML = tpl;

    var app = null;

    var doPost = function(url, data, success, complete){
        $.ajax({
            url:window.BASEPATH + url,
            type:'POST',
            data:data,
            contentType:'application/x-www-form-urlencoded; charset=UTF-8',
            headers:{
                'tetris-001':window.TOKEN,
                'tetris-002':window.SESSIONID
            },
            success:function(data){
                if(typeof complete === 'function') complete();
                var status = data.status;
                if(status !== 200){
                    app.$message({
                        type:'error',
                        message:data.message
                    });
                    return;
                }
                data = data.data;
                if(typeof success === 'function') success(data);
            },
            error:function(XMLHttpRequest, textStatus, errorThrown) {
                if(typeof complete === 'function') complete();
                if(XMLHttpRequest.status !== 200){
                    app.$message({
                        type:'error',
                        message:'服务器断开连接！'
                    });
                    console.log(XMLHttpRequest.status);
                    console.log(XMLHttpRequest.readyState);
                    console.log(textStatus);
                }
            }
        })
    };

    app = new Vue({
        el:'#app',
        data:function(){
            return {
                loading:true,
                bpmnExtInstance:'',
                dialog:{
                    userTask:{
                        visible:false,
                        title:'',
                        userId:'',
                        userNickName:'',
                        taskId:'',
                        variableSet:''
                    }
                }
            }
        },
        methods:{
            handleClose:function(){
                var self = this;
                self.dialog.userTask.visible = false;
            }
        },
        mounted:function(){
            var self = this;
            doPost('process/query/bpmn/by/process/instance/id', {
                processInstanceId:window.PROCESSINSTANCEID
            }, function(data){
                var bpmn = data.bpmn;
                var definitionKey = data.definitionKey;
                var definitionId = data.definitionId;
                var completeTaskIds = data.completeTaskIds;
                self.bpmnExtInstance = new BpmnViewer({
                    id:definitionKey,
                    uuid:definitionId,
                    container:'#process-display',
                    nodeType:[],
                    entries:[],
                    xml:bpmn,
                    isViewer:true,
                    completeTaskIds:completeTaskIds,
                    userTaskBindVariables:null,
                    onReady:function(){
                        var bpmnInstance = self.bpmnExtInstance;
                        var eventBus = bpmnInstance.get('eventBus');
                        var modeling = bpmnInstance.get('modeling');
                        eventBus.on('element.click', function(e){
                            if(!completeTaskIds || completeTaskIds.length<=0) return;
                            var finded = false;
                            for(var i=0; i<completeTaskIds.length; i++){
                                if(completeTaskIds[i] === e.element.id){
                                    finded = true;
                                    break;
                                }
                            }
                            if(!finded) return;
                            if(e.element.type === 'bpmn:UserTask'){
                                doPost('/process/query/task/history', {
                                    processInstanceId:window.PROCESSINSTANCEID,
                                    taskDefinitionKey:e.element.id
                                },function(data){
                                    if(data){
                                        self.dialog.userTask.visible = true;
                                        self.dialog.userTask.title = e.element.businessObject.name;
                                        self.dialog.userTask.userId = data.userId;
                                        self.dialog.userTask.userNickName = data.userNickName;
                                        self.dialog.userTask.taskId = data.taskId;
                                        self.dialog.userTask.variableSet = data.variableSet;
                                    }else{
                                        self.$message({
                                            type:'info',
                                            message:'当前任务正在进行中！'
                                        });
                                    }
                                });
                            }else{
                                self.$message({
                                   type:'info',
                                    message:'当前节点不支持观看历史！'
                                });
                            }
                        });
                    },
                    queryUsers:function(userIds, fn){
                        doPost('/user/find/by/id/in', {ids:$.toJSON(userIds)}, fn);
                    },
                    queryRoles:function(roleIds, fn){
                        doPost('/subordinate/role/find/by/id/in', {ids:$.toJSON(roleIds)}, fn);
                    }
                });
            }, function(){
                self.loading = false;
            });
        }
    });

});