/**
 * Created by lvdeyang on 2018/12/24 0024.
 */
define([
    'text!' + window.APPPATH + 'omms/hardware/monitor/page-omms-hardware-server-monitor.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'NetworkLineChart',
    'SingleRateBar',
    'DiskPieChart',
    'element-ui',
    'mi-frame',
    'date',
    'css!' + window.APPPATH + 'omms/hardware/monitor/page-omms-hardware-server-monitor.css'
], function(tpl, config, $, ajax, context, commons, Vue, NetworkLineChart, SingleRateBar, DiskPieChart){

    var pageId = 'page-omms-hardware-server-monitor';

    var vueInstance = null;

    var init = function(p){

        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        vueInstance = new Vue({
            el: '#' + pageId + '-wrapper',
            data: {
                basePath:window.BASEPATH,
                menus: context.getProp('menus'),
                user: context.getProp('user'),
                groups: context.getProp('groups'),
                interval:'',
                serverId: p.serverId,
                serverName: p.serverName,
                memoryOccupy:0,
                memoryInfo:{
                    total:0,
                    buff:0,
                    used:0,
                    free:0
                },
                taskInfo:{
                    total:0,
                    running:0,
                    sleeping:0,
                    zombie:0
                },
                cpuName:'',
                cpuOccupy:0,
                systemTime:'',
                lastRebootTime:'',
                upTime:'',
                charts:{
                    cpuOccupy:'',
                    memoryOccupy:'',
                    rxkB:'',
                    txkB:'',
                    disks:[]
                },
                temperature:'',
                fanspeed:0
            },
            computed:{

            },
            watch:{

            },
            methods:{
                queryStatus:function(){
                    var self = this;
                    ajax.post('/server/query/status?'+new Date().getTime(), {
                        id:self.serverId
                    }, function(data){
                        if(data.oneDimensionalData){
                            self.cpuOccupy = data.oneDimensionalData.cpuOccupy;
                            self.cpuName = data.oneDimensionalData.cpuName;
                            self.systemTime = data.oneDimensionalData.systemTime;
                            self.lastRebootTime = data.oneDimensionalData.lastRebootTime;
                            self.upTime = data.oneDimensionalData.upTime;
                            self.temperature = data.oneDimensionalData.temperature;
                            self.fanspeed = data.oneDimensionalData.fanspeed;
                            self.charts.cpuOccupy.setData(self.cpuOccupy);

                            self.memoryInfo.total = data.oneDimensionalData.memoryTotal;
                            self.memoryInfo.buff = data.oneDimensionalData.memoryBuff;
                            self.memoryInfo.used = data.oneDimensionalData.memoryUsed;
                            self.memoryInfo.free = data.oneDimensionalData.memoryFree;
                            if(self.memoryInfo.total){
                                self.memoryOccupy = (self.memoryInfo.used/self.memoryInfo.total).toFixed(1);
                            }
                            self.charts.memoryOccupy.setData(self.memoryOccupy);

                            self.taskInfo.total = data.oneDimensionalData.taskTotal;
                            self.taskInfo.running = data.oneDimensionalData.taskRunning;
                            self.taskInfo.sleeping = data.oneDimensionalData.taskSleeping;
                            self.taskInfo.zombie = data.oneDimensionalData.taskZombie;
                        }

                        var networks = data.networks;
                        if(networks && networks.length>0){
                            var rxkB = [];
                            var txkB = [];
                            for(var i=0; i<networks.length; i++){
                                rxkB.push({
                                    id:networks[i].name,
                                    name:networks[i].name,
                                    value:networks[i].rxkB
                                });
                                txkB.push({
                                    id:networks[i].name,
                                    name:networks[i].name,
                                    value:networks[i].txkB
                                });
                            }
                            self.charts.rxkB.setData(rxkB);
                            self.charts.txkB.setData(txkB);
                        }

                        var disks = data.disks;
                        if(disks && disks.length>0){
                            for(var i=0; i<disks.length; i++){
                                var finded = false;
                                for(var j=0; j<self.charts.disks.length; j++){
                                    if(disks[i].name === self.charts.disks[j].name){
                                        self.charts.disks[j].name = disks[i].name;
                                        self.charts.disks[j].total = disks[i].total;
                                        self.charts.disks[j].used = disks[i].used;
                                        self.charts.disks[j].free = disks[i].free;
                                        finded = true;
                                        break;
                                    }
                                }
                                if(!finded){
                                    self.charts.disks.push(disks[i]);
                                }
                            }

                            self.$nextTick(function(){
                               var $disks = self.$el.querySelectorAll('.disk');
                               for(var i=0; i<self.charts.disks.length; i++){
                                   var disk = self.charts.disks[i];
                                   var d = [{
                                       name:'占用',
                                       value:(disk.used/1024).toFixed(1)
                                   }, {
                                        name:'空闲',
                                       value:(disk.free/1024).toFixed(1)
                                   }];
                                   disk.$el = $disks[i];
                                   if(!disk.chart){
                                       disk.chart = new DiskPieChart(disk.$el, d)
                                   }else{
                                       disk.chart.setData(d);
                                   }
                               }
                            });
                        }
                    });
                }
            },
            mounted:function(){
                var self = this;
                self.$nextTick(function(){
                    self.charts.cpuOccupy = new SingleRateBar(self.$el.querySelector('.cpuIdle'), self.cpuOccupy);
                    self.charts.memoryOccupy = new SingleRateBar(self.$el.querySelector('.memoryInfo'), self.memoryOccupy);
                    self.charts.rxkB = new NetworkLineChart(self.$el.querySelector('.rxkB'), []);
                    self.charts.txkB = new NetworkLineChart(self.$el.querySelector('.txkB'), []);
                    self.queryStatus();
                    self.interval = setInterval(function(){
                        self.queryStatus();
                    }, 1000);
                });
            }
        });

    };

    var destroy = function(){
        if(!vueInstance) return;
        if(vueInstance.interval) clearInterval(vueInstance.interval);
        if(vueInstance.charts.cpuOccupy) vueInstance.charts.cpuOccupy.destroy();
        if(vueInstance.charts.memoryOccupy) vueInstance.charts.memoryOccupy.destroy();
        if(vueInstance.charts.rxkB) vueInstance.charts.rxkB.destroy();
        if(vueInstance.charts.txkB) vueInstance.charts.txkB.destroy();
        if(vueInstance.charts.disks.length > 0){
            for(var i=0; i<vueInstance.charts.disks.length; i++){
                vueInstance.charts.disks[i].chart.destroy();
            }
        }
    };

    var groupList = {
        path:'/' + pageId + '/:serverId/:serverName',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});