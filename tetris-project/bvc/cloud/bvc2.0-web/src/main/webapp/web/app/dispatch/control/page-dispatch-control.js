
define([
    'text!' + window.APPPATH + 'dispatch/control/page-dispatch-control.html',
    'restfull',
    'config',
    'context',
    'commons',
    'vue',
    'extral',
    'element-ui',
    'css!' + window.APPPATH + 'dispatch/control/page-dispatch-control.css'
], function(tpl, ajax, config, context, commons, Vue){

    var pageId = 'page-dispatch-control';

    let init = function(p){

        //设置标题
        commons.setTitle(pageId);

        let $page = document.getElementById(pageId);
        $page.innerHTML = tpl;
        new Vue({
            el:'#' + pageId + '-wrapper',
            data:function(){
                return {
                    user:context.getProp('user'),
                    menurouter: false,
                    shortCutsRoutes:commons.data,
                    active:"/page-group-list",
                    sourceProps:{
                        children: 'sources',
                        label:'name'
                    },
                    sourceGroups:[{
                        id:1,
                        name:"北京",
                        sources:[{
                            name:"CCTV1",
                            url:"udp://10.10.40.24:10089",
                        },{
                            name:"CCTV2",
                            url:"http://234.333.123.333:67890/live/stream/pub.m3u8",
                        }]
                    },{
                        id:2,
                        name:"上海",
                        sources:[{
                            name:"SHTV1",
                        },{
                            name:"SHTV2",
                        }]
                    }],
                    outputProps:{
                        children: 'outputs',
                        label:'name'
                    },
                    outputGroups:[{
                        id:1,
                        name:"北京",
                        outputs:[{
                            name:"通道1",
                            url:"udp://10.10.40.24:10089",
                        },{
                            name:"通道2",
                            url:"udp://10.10.40.24:10089",
                        }]
                    }],
                    changedAvailableOutputs:[],//记录输出站点选择切换后的通道数据
                    records:[],//全量关于某个输入的任务数据
                    dialog:{
                        addDispatch:{
                            visible:false,
                            sourceId:null,
                            sourceName:null,
                            outputGroupId:null,
                            outputId:null,
                            pagination:{
                                total:0,
                                curPage:1,
                                pageSize:10
                            }
                        },
                        addSourceGroup:{
                            visible:false,
                            name:'',
                        },
                        addOutputGroup:{
                            visible:false,
                            name:'',
                        },
                        addSource:{
                            visible:false,
                            groupId:null,
                            sources:[], //准备添加的源
                            virtualSources:[],
                            pagination:{
                                total:0,
                                curPage:1,
                                pageSize:10
                            }
                        },
                        editSource:{
                            visible:false,
                            protocolType:'',
                            name:'',
                            url:''
                        },
                        addOutput:{
                            visible:false,
                            protocolType:'TSUDP',
                            name:'',
                            url:'',
                            groupId:null,
                            pagination:{
                                total:0,
                                curPage:1,
                                pageSize:10
                            }
                        },
                        editOutput:{
                            visible:false,
                            id:'',
                            type:'',
                            name:'',
                            url:''
                        },
                    },

                }
            },
            methods:{
                handleNodeClick(data) {
                    console.log(data);
                },
                sourceDetailBtnClick(data){
                    let self=this
                    this.dialog.addSource.visible=true
                    this.dialog.addSource.groupId=data.id
                    ajax.get('/tetris/dispatch/control/virtualSources', null, function(result){
                        if(result.code!==0){
                            self.$notify.error({position: 'bottom-right',title:'操作失败',message:result.message})
                        }else{
                            self.dialog.addSource.pagination.total = result.data.length
                            self.dialog.addSource.virtualSources = result.data
                        }
                    })
                },
                outputDetailBtnClick(group){
                    this.dialog.addOutput.visible=true
                    this.dialog.addOutput.groupId=group.id
                },
                deleteSourceGroupBtnClick(data){
                    let self = this
                    ajax.post('/tetris/dispatch/control/sourceGroups/delete/'+data.id, null, function(result){
                        if(result.code!==0){
                            self.$notify.error({position: 'bottom-right',title:'操作失败',message:result.message})
                        }else{
                            self.$notify.success({position: 'bottom-right',title:'操作成功',message:result.message})
                            self.getSourceGroups();
                        }
                    })
                },
                deleteOutputGroupBtnClick(id){
                    let self = this
                    ajax.post('/tetris/dispatch/control/outputGroups/delete/'+id, null, function(result){
                        if(result.code!==0){
                            self.$notify.error({position: 'bottom-right',title:'操作失败',message:result.message})
                        }else{
                            self.$notify.success({position: 'bottom-right',title:'操作成功',message:result.message})
                            self.getOutputGroups();
                        }
                    })
                },
                addDispatchBtnClick(){ //添加转发任务
                    let self=this
                    let task = {}
                    task.sourceId=this.dialog.addDispatch.sourceId
                    task.outputId=this.dialog.addDispatch.outputId
                    ajax.post('/tetris/dispatch/control/tasks', task, function(result){
                        if(result.code!==0){
                            self.$notify.error({position: 'bottom-right',title:'操作失败',message:result.message})
                        }else{
                            self.$notify.success({position: 'bottom-right',title:'操作成功',message:result.message})
                            self.getTasks(task.sourceId);
                            self.getOutputGroups();
                        }
                    })
                },
                addOutputBtnClick(){
                    let self=this;
                    let output = {}
                    output.name=this.dialog.addOutput.name
                    output.url=this.dialog.addOutput.url
                    output.protocolType=this.dialog.addOutput.protocolType
                    output.groupId=this.dialog.addOutput.groupId
                    ajax.post('/tetris/dispatch/control/outputs', output, function(result){
                        if(result.code!==0){
                            self.$notify.error({position: 'bottom-right',title:'操作失败',message:result.message})
                        }else{
                            self.$notify.success({position: 'bottom-right',title:'操作成功',message:result.message})
                            self.getOutputGroups();
                        }
                    })
                },
                transferSourceBtnClick(data){
                  this.dialog.addDispatch.visible=true;
                  this.dialog.addDispatch.sourceId=data.id
                  this.dialog.addDispatch.sourceName=data.name
                  this.getTasks(data.id);
                },
                addSourceDlgOk(){ //添加源
                    let self =this
                    let source = {}
                    source.groupId = this.dialog.addSource.groupId
                    source.bundleIds = this.dialog.addSource.sources.map(s=>s.bundleId).join(",")
                    if(source.bundleIds===null || source.bundleIds==='' || typeof source.bundleIds==='undefined'){
                        alert("未选中数据，无法添加信源")
                        return;
                    }
                    ajax.post('/tetris/dispatch/control/sources', source, function(result){
                        if(result.code!==0){
                            self.$notify.error({position: 'bottom-right',title:'操作失败',message:result.message})
                        }else{
                            self.$notify.success({position: 'bottom-right',title:'操作成功',message:result.message})
                            self.getSourceGroups()
                            self.addSourceDlgClose()
                        }
                    })
                },
                addSourceDlgClose(){
                    this.dialog.addSource.visible=false;
                },
                editSourceBtnClick(index,row){
                    this.dialog.editSource.id=row.id
                    this.dialog.editSource.name=row.name
                    this.dialog.editSource.protocolType=row.protocolType
                    this.dialog.editSource.url=row.url
                    this.dialog.editSource.visible=true
                },
                deleteSourceBtnClick(data){
                    let self = this
                    ajax.post('/tetris/dispatch/control/sources/delete/'+data.id, null, function(result){
                        if(result.code!==0){
                            self.$notify.error({position: 'bottom-right',title:'操作失败',message:result.message})
                        }else{
                            self.$notify.success({position: 'bottom-right',title:'操作成功',message:result.message})
                            self.getSourceGroups();
                        }
                    })
                },
                // 删除转发记录
                deleteRecordBtnClick(index,row){
                    let self= this
                    ajax.post('/tetris/dispatch/control/tasks/delete/'+row.id, null, function(result){
                        if(result.code!==0){
                            self.$notify.error({position: 'bottom-right',title:'操作失败',message:result.message})
                        }else{
                            self.$notify.success({position: 'bottom-right',title:'操作成功',message:result.message})
                            self.getTasks(self.dialog.addDispatch.sourceId);
                            self.getOutputGroups();
                        }
                    })
                },
                editOutputBtnClick(index,row){
                    this.dialog.editOutput.id=row.id
                    this.dialog.editOutput.name=row.name
                    this.dialog.editOutput.type=row.type
                    this.dialog.editOutput.url=row.url
                    this.dialog.editOutput.visible=true
                },
                deleteOutputBtnClick(index,row){
                    let self= this
                    ajax.post('/tetris/dispatch/control/outputs/delete/'+row.id, null, function(result){
                        if(result.code!==0){
                            self.$notify.error({position: 'bottom-right',title:'操作失败',message:result.message})
                        }else{
                            self.$notify.success({position: 'bottom-right',title:'操作成功',message:result.message})
                            self.getOutputGroups();

                        }
                    })
                },
                transferSourceDlgClose(){
                    this.dialog.addDispatch.visible=false;
                },
                outputDetailDlgClose(){
                    this.dialog.addOutput.visible=false;
                },
                editSourceDlgOk(){
                    alert('修改源')
                },
                editSourceDlgClose(){
                    this.dialog.editSource.visible=false;
                },
                editOutputDlgOk(){
                    let self=this
                    let output={}
                    output.url=this.dialog.editOutput.url
                    output.name=this.dialog.editOutput.name
                    output.protocolType=this.dialog.editOutput.type
                    ajax.post('/tetris/dispatch/control/outputs/modify/'+this.dialog.editOutput.id, output, function(result){
                        if(result.code!==0){
                            self.$notify.error({position: 'bottom-right',title:'操作失败',message:result.message})
                        }else{
                            self.$notify.success({position: 'bottom-right',title:'操作成功',message:result.message})
                            self.getOutputGroups();
                            self.editOutputDlgClose();
                        }
                    })
                },
                editOutputDlgClose(){
                    this.dialog.editOutput.visible=false;
                },
                addSourceGroupBtnClick(){
                    this.dialog.addSourceGroup.name=''
                    this.dialog.addSourceGroup.visible=true
                },
                addSourceGroupDlgOk(){
                    let self = this
                    let sourceGroup={}
                    sourceGroup.name=this.dialog.addSourceGroup.name
                    ajax.post('/tetris/dispatch/control/sourceGroups', sourceGroup, function(result){
                        if(result.code!==0){
                            self.$notify.error({position: 'bottom-right',title:'操作失败',message:result.message})
                        }else{
                            self.$notify.success({position: 'bottom-right',title:'操作成功',message:result.message})
                            self.getSourceGroups();
                            self.addSourceGroupDlgCancel();
                        }
                    })
                },
                addSourceGroupDlgCancel(){
                    this.dialog.addSourceGroup.visible=false;
                },
                addOutputGroupBtnClick(){
                    this.dialog.addOutputGroup.name=''
                    this.dialog.addOutputGroup.visible=true
                },
                addOutputGroupDlgOk(){
                    let self=this
                    let outputGroup={}
                    outputGroup.name=this.dialog.addOutputGroup.name
                    ajax.post('/tetris/dispatch/control/outputGroups', outputGroup, function(result){
                        if(result.code!==0){
                            self.$notify.error({position: 'bottom-right',title:'操作失败',message:result.message})
                        }else{
                            self.$notify.success({position: 'bottom-right',title:'操作成功',message:result.message})
                            self.getOutputGroups();
                            self.addOutputGroupDlgCancel();
                        }
                    })
                },
                addOutputGroupDlgCancel(){
                    this.dialog.addOutputGroup.visible=false;
                },
                addSourceTableSelectChanged(val){
                    console.log("选中："+val)
                    this.dialog.addSource.sources=val
                },
                getTasks(sourceId){
                    let self=this
                    let task={}
                    task.sourceId=sourceId
                    ajax.get('/tetris/dispatch/control/tasks', task, function(result){
                        if(result.code!==0){
                            self.$notify.error({position: 'bottom-right',title:'操作失败',message:result.message})
                        }else{
                            self.records=result.data
                        }
                    })
                },
                getSourceGroups(){
                    let self=this
                    ajax.get('/tetris/dispatch/control/sourceGroups', null, function(result){
                        if(result.code!==0){
                            self.$notify.error({position: 'bottom-right',title:'操作失败',message:result.message})
                        }else{
                            self.sourceGroups=result.data
                        }
                    })
                },
                getOutputGroups(){
                    let self=this
                    ajax.get('/tetris/dispatch/control/outputGroups', null, function(result){
                        if(result.code!==0){
                            self.$notify.error({position: 'bottom-right',title:'操作失败',message:result.message})
                        }else{
                            self.outputGroups=result.data
                        }
                    })
                },
                handleVirtualSourceCurrentChange(val){
                    console.log("cur virtual source page:" + val)
                    this.dialog.addSource.pagination.curPage = val
                },
                handleVirtualSourceSizeChange(val){
                    this.dialog.addSource.pagination.pageSize = val
                },
                handleTaskCurrentChange(val){
                    console.log("cur task page:" + val)
                    this.dialog.addDispatch.pagination.curPage = val
                },
                handleTaskSizeChange(val){
                    this.dialog.addDispatch.pagination.pageSize = val
                },
                handleOutputCurrentChange(val){
                    console.log("cur output page:" + val)
                    this.dialog.addOutput.pagination.curPage = val
                },
                handleOutputSizeChange(val){
                    this.dialog.addOutput.pagination.pageSize = val
                },
                outputGroupChanged(val){
                    this.dialog.addDispatch.outputId=null
                    this.changedAvailableOutputs = this.outputGroups.filter(o=>o.id===val)[0].outputs
                }
            },
            computed:{
                pageVirtualSource:function(){
                    if (this.dialog.addSource.virtualSources) {
                        let pageSize=this.dialog.addSource.pagination.pageSize
                        let page = this.dialog.addSource.pagination.curPage
                        let pageSources =  this.dialog.addSource.virtualSources.slice(pageSize*(page-1),pageSize*page)
                        return pageSources;
                    }
                },
                pageTasks:function () { //转发记录表数据
                    if (this.records) {
                        this.dialog.addDispatch.pagination.total = this.records.length
                        let pageSize=this.dialog.addDispatch.pagination.pageSize
                        let page = this.dialog.addDispatch.pagination.curPage
                        let pageTasks =  this.records.slice(pageSize*(page-1),pageSize*page)
                        return pageTasks;
                    }
                },
                pageOutputs:function () { //输出通道表数据
                    if (this.dialog.addOutput.groupId != null) {
                        let outputs = this.outputGroups.filter(g=>g.id===this.dialog.addOutput.groupId)[0].outputs
                        this.dialog.addOutput.pagination.total = outputs.length
                        let pageSize=this.dialog.addOutput.pagination.pageSize
                        let page = this.dialog.addOutput.pagination.curPage
                        let pageOuts = outputs.slice(pageSize*(page-1),pageSize*page)
                        return pageOuts;
                    }
                }
            },
            created:function(){
                let self = this;
                this.getSourceGroups();
                this.getOutputGroups();
            }

        });
    };

    var destroy = function(){

    };
    let dispatchList = {
        path:'/' + pageId,
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return dispatchList;
});