define([
    'text!' + window.APPPATH + 'group/info/forward/combine/page-group-info-combine-forward.html',
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

    var pageId = 'page-group-info-combine-forward';

    var init = function(p){

        var breadcrumbLabel = '',
            breadcrumbHref = '';

        if(p.type === '0'){
            breadcrumbLabel = '合屏';
            breadcrumbHref = '#/page-group-info-combinevideo/' + p.id + '/' + p.pageSize + '/' + p.currentPage;
        }else if(p.type === '1'){
            breadcrumbLabel = '混音';
            breadcrumbHref = '#/page-group-info-combineaudio/' + p.id + '/' + p.pageSize + '/' + p.currentPage;
        }

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        var v_forward = new Vue({
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
                            label:breadcrumbLabel,
                            href:breadcrumbHref
                        },{
                            label:'转发',
                            href:'#/page-group-info-combine-forward/' + p.id +'/' + p.uuid + '/' + p.type + '/' + p.pageSize + '/' + p.currentPage
                        }
                    ],
                    columns:[
                        {
                            label:'类型',
                            prop:'forwardSourceType',
                            type:'simple',
                            width:'100'
                        },{
                            label:'源combineUuid',
                            prop:'combineUuid',
                            type:'simple',
                            width:'400'
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
                    load:'/device/group/control/query/combine/forward/' + p.uuid,
                    remove:'/device/group/control/remove/forward/' + p.id,
                    refresh:'/device/group/control/refresh/forward/' + p.id,
                    pk:'id',
                    search:{
                        text:'',
                        sign:'bundleName',
                        condition:'目的名称过滤'
                    },
                    pagesizes:[100, 200, 300, 400],
                    pagesize: 100,
                    currentpage: 1
                }
            },
            methods:{

            },
            mounted:function(){
                var _instance = this;
            }

        });

    };

    var destroy = function(){

    };

    var groupInfo = {
        path:'/' + pageId + '/:id' + '/:uuid' + '/:type' + '/:pageSize' + '/:currentPage',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupInfo;
});
