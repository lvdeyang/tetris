define([
    'echarts'
], function(echarts){

    var option = function(){
        return {
            tooltip: {
                trigger: 'item',
                formatter: '{b} : {c}MB ({d}%)'
            },
            series: [
                {
                    type: 'pie',
                    radius: '70%',
                    center: ['50%', '48%'],
                    selectedMode: 'single',
                    data: [
                        //{value: 535, name: 'ОЃжн'}
                    ],
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        };
    };

    function DiskPieChart($el, d){
        this.option = option();
        this.$el = $el;
        this.echars = echarts.init($el);
        this.setData(d);
    }

    DiskPieChart.prototype.setData = function(d){
        var init = false;
        if(this.option.series[0].data.length === 0) init = true;
        for(var i=0; i<d.length; i++){
            if(init){
                this.option.series[0].data.push({
                    name:d[i].name,
                    value:d[i].value
                });
            }else{
                for(var j=0; j<this.option.series[0].data.length; j++){
                    if(this.option.series[0].data[j].name === d[i].name){
                        this.option.series[0].data[j].value = d[i].value;
                        break;
                    }
                }
            }
        }
        this.render();
    };

    DiskPieChart.prototype.render = function(){
        this.echars.setOption(this.option);
    };

    DiskPieChart.prototype.destroy = function(){
        this.echars.dispose();
    };

    return DiskPieChart;
});