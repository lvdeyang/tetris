
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
                    },{
                        id:2,
                        name:"上海",
                        outputs:[{
                            name:"通道1",
                        },{
                            name:"通道2",
                        }]
                    }],
                    dialog:{
                        addDispatch:{
                            visible:false,
                            sourceId:null,
                            sourceName:null,
                            outputGroupId:null,
                            outputId:null
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
                            sources:[] //准备添加的源
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
                            url:''
                        },
                        editOutput:{
                            visible:false,
                            protocolType:'',
                            name:'',
                            url:''
                        },
                        sources:[],//媒资来的源
                        outputs:[],
                        records:[]
                    }
                }
            },
            computed:{

            },
            methods:{
                handleNodeClick(data) {
                    console.log(data);
                },
                sourceDetailBtnClick(id){
                    this.dialog.addSource.visible=true
                },
                outputDetailBtnClick(id){
                    this.dialog.addOutput.visible=true
                    this.dialog.addOutput.outputs=this.outputGroups.filter(s=>s.id=id)[0].outputs
                },
                deleteSourceGroupBtnClick(id){
                    alert('删除信源站点：'+id)
                },
                deleteOutputGroupBtnClick(id){
                    alert('删除输出站点：'+id)
                },
                addDispatchBtnClick(){
                    alert('添加转发任务')
                },
                addSourceBtnClick(){
                    alert('添加源')
                },
                addOutputBtnClick(){
                    let output = {}
                    output.name=this.dialog.addOutput.name
                    output.url=this.dialog.addOutput.url
                    output.protocolType=this.dialog.addOutput.protocolType
                    alert('添加输出：'+output.name)
                },
                transferSourceBtnClick(data){
                  this.dialog.addDispatch.visible=true;
                  this.dialog.addDispatch.sourceId=data.id
                  this.dialog.addDispatch.sourceName=data.name
                },
                addSourceDlgOk(){
                    alert("添加源")
                },
                addSourceDlgClose(){
                    this.dialog.addSource.visible=false;
                },
                transferSourceDlgOk(){

                },
                editSourceBtnClick(index,row){
                    this.dialog.editSource.id=row.id
                    this.dialog.editSource.name=row.name
                    this.dialog.editSource.protocolType=row.protocolType
                    this.dialog.editSource.url=row.url
                    this.dialog.editSource.visible=true
                },
                deleteSourceBtnClick(data){
                  alert("删除源:"+data)
                },
                // 删除转发记录
                deleteRecordBtnClick(index,row){
                    alert("删除任务:"+row)
                },
                editOutputBtnClick(index,row){
                    this.dialog.editOutput.id=row.id
                    this.dialog.editOutput.name=row.name
                    this.dialog.editOutput.protocolType=row.protocolType
                    this.dialog.editOutput.url=row.url
                    this.dialog.editOutput.visible=true
                },
                deleteOutputBtnClick(index,row){
                    alert("删除输出:"+row)
                },
                transferSourceDlgClose(){
                    this.dialog.addDispatch.visible=false;
                },
                editSourceDlgOk(){
                    alert('修改源')
                },
                editSourceDlgClose(){
                    this.dialog.editSource.visible=false;
                },
                editOutputDlgOk(){
                    alert('修改输出')
                },
                editOutputDlgClose(){
                    this.dialog.editOutput.visible=false;
                },
                addSourceGroupBtnClick(){
                    this.dialog.addSourceGroup.name=''
                    this.dialog.addSourceGroup.visible=true
                },
                addSourceGroupDlgOk(){
                    let sourceGroup={}
                    sourceGroup.name=this.dialog.addSourceGroup.name
                    alert("添加站点："+sourceGroup.name)
                },
                addSourceGroupDlgCancel(){
                    this.dialog.addSourceGroup.visible=false;
                },
                addOutputGroupBtnClick(){
                    this.dialog.addOutputGroup.name=''
                    this.dialog.addOutputGroup.visible=true
                },
                addOutputGroupDlgOk(){
                    let outputGroup={}
                    outputGroup.name=this.dialog.addOutputGroup.name
                    alert("添加站点："+outputGroup.name)
                },
                addOutputGroupDlgCancel(){
                    this.dialog.addOutputGroup.visible=false;
                },
                addSourceTableSelectChanged(val){
                    this.dialog.addSource.sources=val
                    console.log("选中："+val)
                }
            },
            created:function(){
                let self = this;

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