define([
    'text!' + window.APPPATH + 'group/monitor/page-group-monitor.html',
    'restfull',
    'config',
    'commons',
    'vue',
    'element-ui',
    'bvc2-header',
    'bvc2-group-table-base'
], function(tpl, ajax, config, commons, Vue){

    var pageId = 'page-group-monitor';

    var init = function(p){

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        var v_monitor = new Vue({
            el:'#' + pageId + '-wrapper',
            data:{
                header:commons.getHeader(0),
                group:p,
                table:{
                    breadcrumb:[
                        {
                            label:'返回',
                            icon:'icon-reply',
                            href:'#/page-group-preview/'+ p.id
                        },{
                            label:'监控录制'
                        }
                    ],
                    columns:[
                        {
                            label:'设备',
                            prop:'bundleName',
                            type:'simple',
                            width:'400'
                        },{
                            label:'录制状态',
                            prop:'recordStatus',
                            type:'simple'
                        }],
                    load:'/device/group/monitor/query/bundles/' + p.id,
                    start:'/device/group/monitor/start/record/' + p.id,
                    stop:'/device/group/monitor/stop/record/' + p.id,
                    pk:'id',
                    search:true,
                    pagesizes:[100, 200, 300, 400],
                    pagesize: 100,
                    currentpage: 1
                }
            },
            methods:{

            }

        });

    };

    var destroy = function(){

    };

    var groupInfo = {
        path:'/' + pageId + '/:id',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupInfo;
});
