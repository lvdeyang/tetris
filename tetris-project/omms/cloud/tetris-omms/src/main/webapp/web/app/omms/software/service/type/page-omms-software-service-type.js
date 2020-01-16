/**
 * Created by lvdeyang on 2018/12/24 0024.
 */
define([
    'text!' + window.APPPATH + 'omms/software/service/type/page-omms-software-service-type.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'Graph',
    'LineChart',
    'StackedBarChart',
    'element-ui',
    'mi-frame',
    'date',
    'css!' + window.APPPATH + 'omms/software/service/type/page-omms-software-service-type.css',
    'css!' + window.COMMONSPATH + 'chart/chart.css'
], function(tpl, config, $, ajax, context, commons, Vue, Graph, LineChart, StackedBarChart){

    var pageId = 'page-omms-software-service-type';

    var charts = {};

    var init = function(){

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el: '#' + pageId + '-wrapper',
            data: {
                basePath:window.BASEPATH,
                menus: context.getProp('menus'),
                user: context.getProp('user'),
                groups: context.getProp('groups'),
                table:{
                    rows:[],
                    currentPage:0,
                    pageSizes:[50, 100, 200, 400],
                    currentPage:0,
                    total:0
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                rowKey:function(row){
                    return row.uuid;
                },
                handleSizeChange:function(){

                },
                handleCurrentChange:function(){

                }
            },
            mounted:function(){
                var self = this;

            }
        });

    };

    var destroy = function(){

    };

    var groupList = {
        path:'/' + pageId,
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});