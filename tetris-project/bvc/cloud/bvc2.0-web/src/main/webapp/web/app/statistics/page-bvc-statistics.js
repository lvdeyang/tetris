/**
 * Created by lqxuhv on 2020/10/20.
 */

define([
    'text!' + window.APPPATH + 'statistics/page-bvc-statistics.html',
    'restfull',
    'config',
    'commons',
    'vue',
    'echarts',
    'element-ui',
    'bvc2-header',
    'bvc2-system-nav-side',
    'extral',
    'bvc2-monitor-vod',
    'date'
], function(tpl, ajax, config, commons, Vue, echarts){

    var pageId = 'page-bvc-statistics';

    var vueInstance = null;

    var init = function(p){

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        //设置标题
        commons.setTitle(pageId);

        vueInstance = new Vue({
            el:'#' + pageId + '-wrapper',
            data:{
                menurouter: false,
                shortCutsRoutes:commons.data,
                active:"/page-bvc-statistics",
                header:commons.getHeader(0),
                perNumber:{
                    $el:'',
                    option:{
                        title: {
                            text: '参会人数历史记录'
                        },
                        tooltip: {
                            formatter:function(params){
                                var self = vueInstance;
                                var groupName = self.page.rows[params.dataIndex].groupName;
                                var time = self.perDuration.option.xAxis.data[params.dataIndex];
                                var number = params.data;
                                return '会议：'+groupName + '<br/>时间：'+time+'<br/>人数：'+number;
                            }
                        },
                        xAxis: {
                            data: []
                        },
                        yAxis: {},
                        series: [{
                            name: '参会人数',
                            type: 'bar',
                            data: []
                        }]
                    },
                    echarts:''
                },
                perDuration:{
                    $el:'',
                    option:{
                        title: {
                            text: '开会时长历史记录'
                        },
                        tooltip: {
                            formatter:function(params){
                                var self = vueInstance;
                                var groupName = self.page.rows[params.dataIndex].groupName;
                                var time = self.perDuration.option.xAxis.data[params.dataIndex];
                                var duration = params.data;
                                return '会议：'+groupName + '<br/>时间：'+time+'<br/>时长：'+Date.prototype.formatMilliseconds(duration);
                            }
                        },
                        xAxis: {
                            data: []
                        },
                        yAxis: {
                            axisLabel:{
                                formatter:function (value, index){
                                    return Date.prototype.formatMilliseconds(value);
                                }
                            }
                        },
                        series: [{
                            name: '开会时长',
                            type: 'line',
                            data: []
                        }]
                    },
                    echarts:''
                },
                page:{
                    rows:[],
                    currentPage:0,
                    total:0,
                    pageSize:10,
                    pageSizes:[10, 15, 20]
                },
                totalNumber:{
                    value:0,
                    $el:'',
                    echarts:''
                },
                totalTimes:{
                    value:0,
                    $el:'',
                    echarts:''
                },
                totalDuration:{
                    value:0,
                    $el:'',
                    echarts:''
                }
            },
            methods: {
                handleSizeChange:function(pageSize){
                    var self = this;
                    self.page.pageSize = pageSize;
                    self.renderBarAndLine(self.page.currentPage);
                },
                handleCurrentChange:function(currentPage){
                    var self = this;
                    self.renderBarAndLine(currentPage);
                },
                renderBarAndLine:function(currentPage){
                    var self = this;
                    if(!self.perNumber.$el){
                        self.perNumber.$el = $('#page-bvc-statistics-wrapper .per-number')[0];
                        self.perNumber.echarts = echarts.init(self.perNumber.$el);
                        self.perDuration.$el = $('#page-bvc-statistics-wrapper .per-duration')[0];
                        self.perDuration.echarts = echarts.init(self.perDuration.$el);
                    }
                    self.perNumber.echarts.setOption(self.perNumber.option);
                    self.perDuration.echarts.setOption(self.perDuration.option);
                    ajax.post('/device/group/proceed/record/query/records/by/userid', {
                        currentPage:currentPage,
                        pageSize:self.page.pageSize
                    }, function(data){
                        var total = data.total;
                        var rows = data.rows;
                        if(total > 0){
                            self.perNumber.option.xAxis.data.splice(0, self.perNumber.option.xAxis.data.length);
                            self.perNumber.option.series[0].data.splice(0, self.perNumber.option.series[0].data.length);
                            self.perDuration.option.xAxis.data.splice(0, self.perDuration.option.xAxis.data.length);
                            self.perDuration.option.series[0].data.splice(0, self.perDuration.option.series[0].data.length);
                            self.page.rows.splice(0, self.page.rows.length);
                            self.page.total = total;
                            self.page.currentPage = currentPage;
                            for(var i=0; i<rows.length; i++){
                                var row = rows[i];
                                self.page.rows.push(row);
                                var totalMemberNumber = row.totalMemberNumber;
                                var formatStartTime = row.startTime;
                                var startTime = Date.prototype.parse(row.startTime).getTime();
                                var endTime = row.finished?Date.prototype.parse(row.endTime).getTime():new Date().getTime();
                                self.perNumber.option.xAxis.data.push(formatStartTime);
                                self.perNumber.option.series[0].data.push(totalMemberNumber);
                                self.perDuration.option.xAxis.data.push(formatStartTime);
                                self.perDuration.option.series[0].data.push(endTime-startTime);
                            }
                            self.perNumber.echarts.setOption(self.perNumber.option);
                            self.perDuration.echarts.setOption(self.perDuration.option);
                        }
                    });
                },
                /*getPieOption:function(title, unit, data){
                    return {
                        title:{
                            text:title
                        },
                        tooltip: {
                            trigger: 'item',
                            formatter: '{b} : {c}'+unit+' ({d}%)'
                        },
                        series: [
                            {
                                type: 'pie',
                                radius: '70%',
                                center: ['50%', '48%'],
                                selectedMode: 'single',
                                data: data,
                                emphasis: {
                                    itemStyle: {
                                        shadowBlur: 10,
                                        shadowOffsetX: 0,
                                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                                    }
                                }
                            }
                        ]
                    }
                },*/
                renderPie:function(){
                    var self = this;
                    /*self.totalNumber.$el = $('#page-bvc-statistics-wrapper .total-number')[0];
                    self.totalNumber.echarts = echarts.init(self.totalNumber.$el);

                    self.totalTimes.$el = $('#page-bvc-statistics-wrapper .total-times')[0];
                    self.totalTimes.echarts = echarts.init(self.totalTimes.$el);

                    self.totalDuration.$el = $('#page-bvc-statistics-wrapper .total-duration')[0];
                    self.totalDuration.echarts = echarts.init(self.totalDuration.$el);*/

                    ajax.post('/device/group/proceed/record/statistics/total/of/record/time/people', null, function(data){
                        self.totalNumber.value = data.totalPeople;
                        self.totalTimes.value = data.totalProceedRecord;
                        self.totalDuration.value = Date.prototype.formatSeconds(data.time);
                        /*var totalNumberOption = self.getPieOption('总入会人数：10000人', '人', [
                            {value:data.totalPeople, name:'已入会人数'},
                            {value:10000-data.totalPeople, name:'剩余入会人数'}
                        ]);
                        self.totalNumber.echarts.setOption(totalNumberOption);
                        var totalTimesOption = self.getPieOption('总开会次数：10000次', '次', [
                            {value:data.totalProceedRecord, name:'已用开会次数'},
                            {value:10000-data.totalProceedRecord, name:'剩余开会次数'}
                        ]);
                        self.totalTimes.echarts.setOption(totalTimesOption);
                        var totalDurationOption = self.getPieOption('总开会时长10000小时', '毫秒', [
                            {value:data.time, name:'已用开会时长'},
                            {value:10000*60*60-data.time, name:'剩余开会时长'}
                        ]);
                        totalDurationOption.tooltip.formatter = function(params){
                            return params.data.name + '：'+Date.prototype.formatSeconds(params.data.value)+' ('+params.percent+'%)';
                        };
                        self.totalDuration.echarts.setOption(totalDurationOption);*/
                    });
                }
            },
            mounted:function(){
                var self = this;
                self.renderBarAndLine(1);
                self.renderPie();
            }
        });
    };

    var destroy = function(){
        vueInstance.perNumber.echarts.dispose();
        vueInstance.perDuration.echarts.dispose();
        vueInstance.totalNumber.echarts.dispose();
        vueInstance.totalTimes.echarts.dispose();
        vueInstance.totalDuration.echarts.dispose();
    };

    var page = {
        path:'/' + pageId,
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return page;
});