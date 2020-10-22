/**
 * Created by lqxuhv on 2020/10/20.
 */
define([
    'text!' + window.APPPATH + 'statistics/statistics.html',
    'restfull',
    'config',
    'context',
    'commons',
    'vue',
    'echarts',
    'extral',
    'element-ui',
    'css!' + window.APPPATH + 'statistics/statistics.css'
], function(tpl, ajax, config, context, commons, Vue, echarts){

    var pageId = 'statistics';

    var init = function(p){

        //���ñ���
        commons.setTitle(pageId);


        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;
        new Vue({
            el:'#' + pageId + '-wrapper',
            data:{


            },
            computed:{

            },
            methods:{

            },
            mounted:function(){
                var self = this;
                var onlineNumber = echarts.init(document.getElementById('onlineNumber'));
                var option = {
                    title: {
                        text: 'ECharts 入门示例'
                    },
                    tooltip: {},
                    legend: {
                        data:['销量']
                    },
                    xAxis: {
                        data: ["衬衫","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"]
                    },
                    yAxis: {},
                    series: [{
                        name: '销量',
                        type: 'bar',
                        data: [5, 20, 36, 10, 10, 20]
                    }]
                };

                onlineNumber.setOption(option);

                var mettingTime = echarts.init(document.getElementById('mettingTime'));
                var option1 = {
                    xAxis: {
                        type: 'category',
                        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [{
                        data: [820, 932, 901, 934, 1290, 1330, 1320],
                        type: 'line'
                    }]
                };


                mettingTime.setOption(option1);
            },


            created:function(){


            }

        });
    };

    var destroy = function(){

    };
    var statistics = {
        path:'/' + pageId,
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return statistics;
});