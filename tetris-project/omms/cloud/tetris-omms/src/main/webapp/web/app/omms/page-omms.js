/**
 * Created by lvdeyang on 2018/12/24 0024.
 */
define([
    'text!' + window.APPPATH + 'omms/page-omms.html',
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
    'css!' + window.APPPATH + 'omms/page-omms.css',
    'css!' + window.COMMONSPATH + 'chart/chart.css'
], function(tpl, config, $, ajax, context, commons, Vue, Graph, LineChart, StackedBarChart){

    var pageId = 'page-omms';

    var init = function(){

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el: '#' + pageId + '-wrapper',
            data: {
                menus: context.getProp('menus'),
                user: context.getProp('user'),
                groups: context.getProp('groups')

            },
            computed:{

            },
            watch:{

            },
            methods:{

            },
            mounted:function(){
                var self = this;
                var graph = new Graph($('#graph-container'), [{
                    id:'level0',
                    groups:[{
                        id:'spring-eureka',
                        types:[{
                            id:'eureka',
                            name:'注册中心',
                            servers:[{
                                id:'eureka0',
                                ip:'127.0.0.1',
                                icon:'icons/5-4.jpg',
                                refs:['nginx0', 'zuul0', 'user0', 'user1', 'user2',  'menu0', 'mims0', 'process0', 'cs0', 'cms0', 'p2p0', 'capacity0']
                            }, {
                                id:'eureka1',
                                icon:'icons/5-4.jpg'
                            }]
                        }],
                    },{
                        id:'nginx',
                        types:[{
                            id:'nginx',
                            name:'nginx',
                            servers:[{
                                id:'nginx0',
                                icon:'icons/5-4.jpg'
                            }, {
                                id:'nginx1',
                                icon:'icons/5-4.jpg'
                            }]
                        }]
                    }]
                }, {
                    id:'level1',
                    groups:[{
                        id:'spring-cloud',
                        types:[{
                            id:'zuul',
                            name:'微服务网关',
                            servers:[{
                                id:'zuul0',
                                icon:'icons/5-4.jpg'
                            }]
                        }, {
                            id:'user',
                            name:'用户服务',
                            servers:[{
                                id:'user0',
                                icon:'icons/5-4.jpg'
                            }, {
                                id:'user1',
                                icon:'icons/5-4.jpg'
                            }, {
                                id:'user2',
                                icon:'icons/5-4.jpg'
                            }]
                        }, {
                            id:'menu',
                            name:'菜单服务',
                            servers:[{
                                id:'menu0',
                                icon:'icons/5-4.jpg'
                            }]
                        }, {
                            id:'mims',
                            name:'媒资服务',
                            servers:[{
                                id:'mims0',
                                icon:'icons/5-4.jpg'
                            }]
                        }, {
                            id:'process',
                            name:'流程引擎',
                            servers:[{
                                id:'process0',
                                icon:'icons/5-4.jpg'
                            }]
                        }, {
                            id:'cs',
                            name:'轮播服务',
                            servers:[{
                                id:'cs0',
                                icon:'icons/5-4.jpg'
                            }]
                        }, {
                            id:'cms',
                            name:'内容管理',
                            servers:[{
                                id:'cms0',
                                icon:'icons/5-4.jpg'
                            }]
                        }, {
                            id:'p2p',
                            name:'点对点',
                            servers:[{
                                id:'p2p0',
                                icon:'icons/5-4.jpg'
                            }]
                        }, {
                            id:'capacity',
                            name:'能力接入',
                            servers:[{
                                id:'capacity0',
                                icon:'icons/5-4.jpg',
                                refs:['ssp0', 'st0', 'record0', 'ft0', 'media0', 'media1', 'media2', 'media3']
                            }]
                        }]
                    }]
                }, {
                    id:'level2',
                    groups:[{
                        id:'mss',
                        types:[{
                            id:'ssp',
                            name:'软封装',
                            servers:[{
                                id:'ssp0',
                                icon:'icons/5-4.jpg'
                            }]
                        }, {
                            id:'st',
                            name:'流转码',
                            servers:[{
                                id:'st0',
                                icon:'icons/5-4.jpg'
                            }]
                        }, {
                            id:'record',
                            name:'收录',
                            servers:[{
                                id:'record0',
                                icon:'icons/5-4.jpg'
                            }]
                        }, {
                            id:'ft',
                            name:'文件转码',
                            servers:[{
                                id:'ft0',
                                icon:'icons/5-4.jpg'
                            }]
                        }, {
                            id:'media',
                            name:'流媒体',
                            servers:[{
                                id:'media0',
                                icon:'icons/5-4.jpg'
                            }, {
                                id:'media1',
                                icon:'icons/5-4.jpg'
                            }, {
                                id:'media2',
                                icon:'icons/5-4.jpg'
                            }, {
                                id:'media3',
                                icon:'icons/5-4.jpg'
                            }]
                        }]
                    }]
                }], {
                    normal:{
                        scene:{
                            backgroundColor:0xf2f3f7
                        },
                        group:{
                            lineColor:0x777777
                        },
                        type:{
                            backgroundColor:0xffffff,
                            color:0x777777
                        },
                        server:{
                            surfaceColor:0xffffff,
                            lineColor:0x777777,
                            label:{
                                backgroundColor:0xffffff,
                                lineColor:0x777777,
                                color:0x777777
                            },
                            connection:{
                                lineColor:0x777777
                            }
                        }
                    },
                    mouseover:{
                        server:{
                            surfaceColor:0xeeeeee,
                            lineColor:0x000000,
                            label:{
                                backgroundColor:0xeeeeee,
                                lineColor:0x000000,
                                color:0x000000
                            },
                            connection: {
                                lineColor: 0x000000
                            }
                        }
                    },
                    click:{
                        server:{
                            surfaceColor:0x67c23a,
                            lineColor:0x539c2f,
                            label:{
                                backgroundColor:0x67c23a,
                                lineColor:0x539c2f,
                                color:0xffffff
                            },
                            connection: {
                                lineColor: 0x539c2f
                            }
                        }
                    }
                });
                $(window).on('resize.page.omms', function(){
                    graph.resize();
                });

                var formater = 'hh:mm:ss';
                var domain = [new Date().format(formater), new Date().format(formater)];
                var cpuLine = new LineChart({
                    id:'cpuLine',
                    $el:$('#cpu-line')[0],
                    title:'cpu使用率',
                    margin:40,
                    x:{
                        type:'enum',
                        ticks:5,
                        domain:domain,
                        format:function(){
                            return '';
                        }
                    },
                    y:{
                        type:'basic',
                        ticks:5,
                        domain:[100, 0],
                        format:function(d){
                            return d+'%';
                        }
                    },
                    data:[{
                        color:'red',
                        name:'核心1',
                        curve:false,
                        points:false,
                        x:domain,
                        y:[50, 50]
                    }]
                });
                var memoryLine = new LineChart({
                    id:'memoryLine',
                    $el:$('#memory-line')[0],
                    title:'内存占用',
                    margin:40,
                    x:{
                        type:'enum',
                        ticks:5,
                        domain:domain,
                        format:function(){
                            return '';
                        }
                    },
                    y:{
                        type:'basic',
                        ticks:5,
                        domain:[32, 0],
                        format:function(d){
                            return d+'G';
                        }
                    },
                    data:[{
                        color:'green',
                        name:'内存',
                        curve:false,
                        points:false,
                        x:domain,
                        y:[1.5, 1.5]
                    }]
                });
                var networkUploadTrafficLine = new LineChart({
                    id:'networkUploadTrafficLine',
                    $el:$('#network-upload-traffic')[0],
                    title:'网卡上行流量',
                    margin:40,
                    x:{
                        type:'enum',
                        ticks:5,
                        domain:domain,
                        format:function(){
                            return '';
                        }
                    },
                    y:{
                        type:'basic',
                        ticks:5,
                        domain:[100, 0],
                        format:function(d){
                            return d+'M';
                        }
                    },
                    data:[{
                        color:'purple',
                        name:'网卡1',
                        curve:false,
                        points:false,
                        x:domain,
                        y:[30, 30]
                    }]
                });
                var networkDownloadTrafficLine = new LineChart({
                    id:'networkDownloadTrafficLine',
                    $el:$('#network-download-traffic')[0],
                    title:'网卡下行流量',
                    margin:40,
                    x:{
                        type:'enum',
                        ticks:5,
                        domain:domain,
                        format:function(){
                            return '';
                        }
                    },
                    y:{
                        type:'basic',
                        ticks:5,
                        domain:[100, 0],
                        format:function(d){
                            return d+'M';
                        }
                    },
                    data:[{
                        color:'purple',
                        name:'网卡1',
                        curve:false,
                        points:false,
                        x:domain,
                        y:[30, 30]
                    }]
                });
                setInterval(function(){
                    domain.push(new Date().format(formater));
                    var random = Math.random()*50;
                    cpuLine.config.data[0].y.push(random);

                    random = Math.random()*32;
                    memoryLine.config.data[0].y.push(random);

                    random = Math.random()*100;
                    networkUploadTrafficLine.config.data[0].y.push(random);

                    random = Math.random()*100;
                    networkDownloadTrafficLine.config.data[0].y.push(random);

                    if(domain.length > 40){
                        domain.shift();
                        cpuLine.config.data[0].y.shift();
                        memoryLine.config.data[0].y.shift();
                        networkUploadTrafficLine.config.data[0].y.shift();
                        networkDownloadTrafficLine.config.data[0].y.shift();
                    }

                    cpuLine.reDraw();
                    memoryLine.reDraw();
                    networkUploadTrafficLine.reDraw();
                    networkDownloadTrafficLine.reDraw();
                }, 1000);

                var barDomain = ['硬盘1', '硬盘2', '硬盘3'];
                var bar = new StackedBarChart({
                    id:'bar',
                    $el:$('#hard-disk-space')[0],
                    title:'硬盘空间',
                    margin:40,
                    x:{
                        domain:barDomain
                    },
                    y:{
                        type:'basic',
                        ticks:5,
                        format:function(d){
                            return d/1000+'G';
                        }
                    },
                    data:{
                        x:barDomain,
                        y:[{
                            color:'red',
                            name:'占用',
                            values:[1000, 500, 1000]
                        },{
                            color:'grey',
                            name:'空闲',
                            values:[5000, 1000, 300]
                        }]
                    }
                });

                $(window).on('resize.chart', function(){
                    cpuLine.resize().reDrawLines();
                    memoryLine.resize().reDrawLines();
                    networkUploadTrafficLine.resize().reDrawLines();
                    networkDownloadTrafficLine.resize().reDrawLines();
                    bar.resize().drawRect();
                });
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