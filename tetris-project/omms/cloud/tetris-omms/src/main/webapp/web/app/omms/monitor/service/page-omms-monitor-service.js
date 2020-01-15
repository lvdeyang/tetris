/**
 * Created by lvdeyang on 2018/12/24 0024.
 */
define([
    'text!' + window.APPPATH + 'omms/monitor/service/page-omms-monitor-service.html',
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
    'css!' + window.APPPATH + 'omms/monitor/service/page-omms-monitor-service.css',
    'css!' + window.COMMONSPATH + 'chart/chart.css'
], function(tpl, config, $, ajax, context, commons, Vue, Graph, LineChart, StackedBarChart){

    var pageId = 'page-omms-monitor-service';

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
                formater:'hh:mm:ss',
                isDanger:false,
                interval:'',
                server:{
                    data:{
                        id:'',
                        ip:'',
                        cpuName:''
                    },
                    selected:false,
                    charts:{

                    },
                    interval:''
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                randomColor:function(num){
                    return ['red', 'orange', 'green', 'magenta', 'purple', 'blue', 'cyan', 'silver', 'black', 'olive'];
                },
                formatNetworkSpeed:function(d){
                    if(d.indexOf('bps') >= 0){
                        return parseInt(parseInt(d)/1024/1024);
                    }else if(d.indexOf('Kbps') >= 0){
                        return parseInt(parseInt(d)/1024);
                    }else if(d.indexOf('Mbps') >= 0){
                        return parseInt(d);
                    }
                }
            },
            mounted:function(){
                var self = this;
                ajax.post('/monitor/service/graph/data', null, function(data){
                    self.isDanger = data.isDanger;
                    var levels = data.levels;
                    var graph = new Graph($('#graph-container'), levels, null, {
                        serverSelected:function(server, done){
                            done();
                            if(self.server.interval){
                                clearInterval(self.server.interval);
                            }
                            for(var i in charts){
                                charts[i].destroy();
                            }
                            charts = {};
                            self.server.data.id = server.id;
                            self.server.data.ip = server.ip;
                            self.server.data.status = server.status;
                            self.server.selected = true;
                            self.$nextTick(function(){
                                ajax.post('/monitor/service/gadget/status', {instanceId:server.id}, function(data){
                                    self.server.data.cpuName = data.cpu.name;
                                    var cpuUsage = parseFloat(data.cpu.usage);
                                    var memoryTotal = (parseInt(data.memory.total)/1024).toFixed(1);
                                    var memoryUsed = (parseInt(data.memory.used)/1024).toFixed(1);
                                    var domain = [new Date().format(self.formater), new Date().format(self.formater)];
                                    var cpuLine = new LineChart({
                                        id:'cpuLine',
                                        $el:$('#cpu-line')[0],
                                        title:'cpu使用率',
                                        margin:45,
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
                                            name:'cpu使用率',
                                            curve:false,
                                            points:false,
                                            x:domain,
                                            y:[cpuUsage, cpuUsage]
                                        }]
                                    });
                                    charts.cpuLine = cpuLine;
                                    var memoryLine = new LineChart({
                                        id:'memoryLine',
                                        $el:$('#memory-line')[0],
                                        title:'内存占用',
                                        margin:45,
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
                                            domain:[memoryTotal, 0],
                                            format:function(d){
                                                return d+'G';
                                            }
                                        },
                                        data:[{
                                            color:'green',
                                            name:'内存占用',
                                            curve:false,
                                            points:false,
                                            x:domain,
                                            y:[memoryUsed, memoryUsed]
                                        }]
                                    });
                                    charts.memoryLine = memoryLine;

                                    var network = data.network;
                                    var upData = [];
                                    var downData = [];
                                    var colors = self.randomColor(network.length);
                                    for(var i=0; i<network.length; i++){
                                        upData.push({
                                            color:colors[i],
                                            name:network[i].name,
                                            curve:false,
                                            points:false,
                                            x:domain,
                                            y:[self.formatNetworkSpeed(network[i].up), self.formatNetworkSpeed(network[i].up)]
                                        });
                                        downData.push({
                                            color:colors[i],
                                            name:network[i].name,
                                            curve:false,
                                            points:false,
                                            x:domain,
                                            y:[self.formatNetworkSpeed(network[i].down), self.formatNetworkSpeed(network[i].down)]
                                        });
                                    }

                                    var networkUploadTrafficLine = new LineChart({
                                        id:'networkUploadTrafficLine',
                                        $el:$('#network-upload-traffic')[0],
                                        title:'网卡上行流量',
                                        margin:45,
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
                                            format:function(d){
                                                return d+'Mbps';
                                            }
                                        },
                                        data:upData
                                    });
                                    charts.networkUploadTrafficLine = networkUploadTrafficLine;

                                    var networkDownloadTrafficLine = new LineChart({
                                        id:'networkDownloadTrafficLine',
                                        $el:$('#network-download-traffic')[0],
                                        title:'网卡下行流量',
                                        margin:45,
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
                                            format:function(d){
                                                return d+'Mbps';
                                            }
                                        },
                                        data:downData
                                    });
                                    charts.networkDownloadTrafficLine = networkDownloadTrafficLine;

                                    self.server.interval = setInterval(function(){
                                        ajax.post('/monitor/service/gadget/status', {instanceId:server.id}, function(data){
                                            domain.push(new Date().format(self.formater));
                                            cpuLine.config.data[0].y.push(parseFloat(data.cpu.usage));
                                            memoryLine.config.data[0].y.push((parseInt(data.memory.used)/1024).toFixed(1));
                                            for(var i=0; i<data.network.length; i++){
                                                var n = data.network[i];
                                                for(var j=0; j<networkUploadTrafficLine.config.data.length; j++){
                                                    var d = networkUploadTrafficLine.config.data[i];
                                                    if(n.name === d.name){
                                                        d.y.push(self.formatNetworkSpeed(n.up));
                                                        break;
                                                    }
                                                }
                                                for(var j=0; j<networkDownloadTrafficLine.config.data.length; j++){
                                                    var d = networkDownloadTrafficLine.config.data[i];
                                                    if(n.name === d.name){
                                                        d.y.push(self.formatNetworkSpeed(n.down));
                                                        break;
                                                    }
                                                }
                                            }
                                            if(domain.length > 20){
                                                domain.shift();
                                                cpuLine.config.data[0].y.shift();
                                                memoryLine.config.data[0].y.shift();
                                                for(var i=0; i<networkUploadTrafficLine.config.data.length; i++){
                                                    networkUploadTrafficLine.config.data[i].y.shift();
                                                }
                                                for(var i=0; i<networkDownloadTrafficLine.config.data.length; i++){
                                                    networkDownloadTrafficLine.config.data[i].y.shift();
                                                }
                                            }
                                            cpuLine.reDraw();
                                            memoryLine.reDraw();
                                            networkUploadTrafficLine.reDraw();
                                            networkDownloadTrafficLine.reDraw();
                                        });
                                    }, 5000);

                                    var storageBarDomain = [];
                                    var used = [];
                                    var avail = [];
                                    for(var i=0; i<data.storage.length; i++){
                                        var storage = data.storage[i];
                                        storageBarDomain.push(storage.mounted);
                                        used.push(parseInt(storage.used)/1024);
                                        avail.push(parseInt(storage.avail)/1024);
                                    }
                                    var storageBar = new StackedBarChart({
                                        id:'bar',
                                        $el:$('#hard-disk-space')[0],
                                        title:'硬盘空间',
                                        margin:45,
                                        x:{
                                            domain:storageBarDomain
                                        },
                                        y:{
                                            type:'basic',
                                            ticks:5,
                                            format:function(d){
                                                return d+'G';
                                            }
                                        },
                                        data:{
                                            x:storageBarDomain,
                                            y:[{
                                                color:'red',
                                                name:'占用',
                                                values:used
                                            },{
                                                color:'grey',
                                                name:'空闲',
                                                values:avail
                                            }]
                                        }
                                    });
                                    charts.storageBar = storageBar;
                                });
                            });
                        }
                    });
                    self.interval = setInterval(function(){
                        ajax.post('/monitor/service/refresh/status', null, function(data){
                            self.isDanger = data.isDanger;
                            graph.refreshServerStatus(data.servers);
                        });
                    }, 5000);
                    $(window).on('resize.page.omms', function(){
                        graph.resize();
                    });
                });

                $(window).on('resize.chart', function(){
                    for(var i in charts){
                        var chart = charts[i];
                        if(chart.instanceOf() === 'LineChart'){
                            chart.resize().reDrawLines();
                        }else if(chart.instanceOf() === 'StackedBarChart'){
                            chart.resize().drawRect();
                        }
                    }
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