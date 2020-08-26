/**
 * Created by lvdeyang on 2020/7/23 0023.
 */
define([
    'echarts',
    'date'
], function(echarts, date){

    var MAX_POINTS = 40;

    var option = function(){
        return {
            animation:false,
            grid:{
                x:50,
                y:31,
                x2:10,
                y2:26
            },
            tooltip: {
                trigger:'axis'
            },
            legend: {
                top:5,
                data: [],
                show:true
            },
            xAxis: {
                type:'category',
                data:[],
                show:false
            },
            yAxis: {
                type: 'value',
                show:true
            },
            series: [
                /*{
                 id:'',
                 name: '邮件营销',
                 type: 'line',
                 data: [120, 132, 101, 134, 90, 230, 210]
                 }*/
            ]
        };
    };

    function NetworkLineChart ($el, d, maxPoints){
        this.option = option();
        this.$el = $el;
        this.maxPoints = maxPoints || MAX_POINTS;
        this.echars = echarts.init($el);
        this.setData(d);
    }

    NetworkLineChart.prototype.setData = function(d){
        var init = false;
        var now = new Date().format('hh:mm:ss');
        if(this.option.legend.data.length === 0) init = true;
        for(var i=0; i<d.length; i++){
            if(init){
                this.option.legend.data.push(d[i].name);
                this.option.xAxis.data.push(now);
                this.option.series.push({
                    type: 'line',
                    id:d[i].id,
                    name:d[i].name,
                    data:[d[i].value],
                    showSymbol: false
                });
            }else{
                this.option.xAxis.data.push(now);
                if(this.option.xAxis.data.length > this.maxPoints){
                    this.option.xAxis.data.shift();
                }
                for(var j=0; j<this.option.series.length; j++){
                    if(this.option.series[j].id === d[i].id){
                        this.option.series[j].data.push(d[i].value);
                        if(this.option.series[j].data.length > this.maxPoints){
                            this.option.series[j].data.shift();
                        }
                        break;
                    }
                }
            }
        }
        this.render();
    };

    NetworkLineChart.prototype.render = function(){
        this.echars.setOption(this.option);
    };

    NetworkLineChart.prototype.destroy = function(){
        this.echars.dispose();
    };

    return NetworkLineChart;
});



