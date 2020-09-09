define([
    'text!' + window.APPPATH + 'group/info/forward/page-group-info-forward.html',
    'restfull',
    'config',
    'commons',
    'vue',
    'element-ui',
    'bvc2-header',
    'bvc2-group-nav-side',
    'bvc2-group-table-base',
    'extral'
], function(tpl, ajax, config, commons, Vue){

    var pageId = 'page-group-info-forward';

    var init = function(p){

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        var v_forward  = window.v_forward = new Vue({
            el:'#' + pageId + '-wrapper',
            data:{
                menurouter: false,
                shortCutsRoutes:commons.data,
                active:"/page-group-list",
                header:commons.getHeader(0),
                side:{
                    active:'1-3'
                },
                group:p,
                table:{
                    breadcrumb:[
                        {
                            label:'转发',
                            href:'#/page-group-info-forward/'+ p.id + '/' + p.pageSize + '/' + p.currentPage
                        }
                    ],
                    columns:[
                        {
                            label:'类型',
                            prop:'forwardSourceType',
                            type:'simple',
                            width:'100'
                        },{
                            label:'源uuid',
                            prop:'combineUuid',
                            type:'href',
                            href:'#/page-group-info-forward',
                            groupId: p.id,
                            width:'400'
                        },{
                            label:'源名称',
                            prop:'sourceBundleName',
                            type:'simple',
                            width:'200'
                        },{
                            label:'源通道',
                            prop:'sourceName',
                            type:'simple',
                            width:'200'
                        },{
                            label:'目的名称',
                            prop:'bundleName',
                            type:'simple',
                            width:'200'
                        },{
                            label:'目的通道',
                            prop:'name',
                            type:'simple'
                        }],
                    load:'/device/group/control/query/forward/' + p.id,
                    remove:'/device/group/control/remove/forward/' + p.id,
                    refresh:'/device/group/control/refresh/forward/' + p.id,
                    pk:'id',
                    search:{
                        text:'',
                        sign:'bundleName',
                        condition:'目的名称过滤'
                    },
                    pagesizes:[100, 200, 300, 400],
                    pagesize:parseInt(p.pageSize),
                    currentpage:parseInt(p.currentPage)
                }
            },
            methods:{

            }

        });

    };

    var destroy = function(){

    };

    var groupInfo = {
        path:'/' + pageId + '/:id' + '/:pageSize' + '/:currentPage',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupInfo;
});
