define([
    'text!' + window.APPPATH + 'group/info/combinevideo/forward/page-group-info-forward-combinevideo.html',
    'restfull',
    'config',
    'commons',
    'vue',
    'jquery.layout.auto',
    'element-ui',
    'bvc2-header',
    'bvc2-group-nav-side',
    'bvc2-group-table-base',
    'extral'
], function(tpl, ajax, config, commons, Vue){

    var pageId = 'page-group-info-forward-combinevideo';

    var init = function(p){

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        var v_combineVideo = new Vue({
            el:'#' + pageId + '-wrapper',
            data:{
                menurouter: false,
                shortCutsRoutes:commons.data,
                active:"/page-group-list",
                header:commons.getHeader(0),
                side:{
                    active:'1-1'
                },
                group:p,
                table:{
                    breadcrumb:[
                        {
                            label:'转发',
                            href:'#/page-group-info-forward/'+ p.id + '/' + p.pageSize + '/' + p.currentPage
                        },{
                            label:'合屏转发',
                            href:'#/page-group-info-forward-combinevideo/' + p.id + '/' + p.uuid + '/' + p.pageSize + '/' + p.currentPage
                        }
                    ],
                    columns:[
                        {
                            label:'合屏源',
                            prop:'bundleName',
                            type:'simple',
                            width:'400'
                        },{
                            label:'合屏源通道',
                            prop:'name',
                            type:'simple',
                            width:'400'
                        },{
                            label:'屏幕序号',
                            prop:'serialNum',
                            type:'simple'
                        },{
                            label:'合屏uuid',
                            prop:'combineVideoUuid',
                            type:'simple',
                            width:'400'
                        },{
                            label:'合屏布局',
                            selector:'bvc2-table-layout-preview',
                            style:'display:inline-block; width:40px; height:40px; padding:0; margin:0; position:relative; top:4px;',
                            height:'48px',
                            type:'html',
                            editable:true,
                            width:'100'
                        }],
                    load:'/device/group/control/query/forward/combineVideo/' + p.uuid,
                    remove:'/device/group/control/remove/combineVideo/' + p.id,
                    refresh:'/device/group/control/refresh/combineVideo/' + p.id,
                    pk:'combineVideoUuid',
                    search:{
                        text:'',
                        sign:'bundleName',
                        condition:'合屏源设备过滤'
                    },
                    pagesizes:[10, 20, 30, 40],
                    pagesize:10,
                    currentpage:1,
                    combine:[3,4,5],
                    combineparam:'combineVideoUuid'
                }
            },
            methods:{
                rowChanged:function(rows){
                    var instance = this;
                    //这个让vue先渲染，然后再执行
                    setTimeout(function(){
                        //创建预览
                        $(instance.$refs.$forwardCombineVideoTable.tableSelector + ' .bvc2-table-layout-preview').each(function(){
                            var $this = $(this);
                            var pk = $this[0].getAttribute('data-pk');
                            var finded = false;
                            for(var i=0; i<rows.length; i++){
                                if(rows[i].websiteDraw && rows[i].combineVideoUuid==pk){
                                    var layout = $.parseJSON(rows[i].websiteDraw);
                                    $this['layout-auto']('create', {
                                        cell:{
                                            column:layout.basic.column,
                                            row:layout.basic.row
                                        },
                                        cellspan:layout.cellspan,
                                        editable:false,
                                        theme:'gray'
                                    });
                                    finded = true;
                                    break;
                                }
                            }
                            if(!finded) $this.empty();
                        });
                    }, 0);
                }
            }

        });

    };

    var destroy = function(){

    };

    var groupInfo = {
        path:'/' + pageId + '/:id' + '/:uuid' + '/:pageSize' + '/:currentPage',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupInfo;
});
