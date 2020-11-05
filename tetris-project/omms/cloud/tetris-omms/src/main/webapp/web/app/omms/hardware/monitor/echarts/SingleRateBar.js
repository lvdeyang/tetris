/**
 * Created by lvdeyang on 2020/7/23 0023.
 */
define([
    'echarts'
], function(echarts){
    var COLOR_BACKGROUND = '#f2f6fc';
    var COLOR_SUCCESS = '#67c23a';
    var COLOR_WARNING = '#e6a23c';
    var COLOR_DANGER = '#f56c6c';

    var option = function(){
        return {
            animation:false,
            grid:{
                x:0,
                y:0,
                x2:0,
                y2:0
            },
            xAxis:{
                type:'category',
                data:['0%'],
                show:false
            },
            yAxis: {
                type:'value',
                show:false,
                max:100
            },
            series: [{
                data:[{
                    value:0,
                    itemStyle:{
                        color:''
                    }
                }],
                type:'bar',
                showBackground: true,
                backgroundStyle: {
                    color:COLOR_BACKGROUND
                }
            }]
        };
    };

    function SingleRateBar ($el, d){
        this.option = option();
        this.$el = $el;
        this.echars = echarts.init($el);
        this.setData(d);
    }

    SingleRateBar.prototype.getMax = function(){
        return this.option.yAxis.max;
    };

    SingleRateBar.prototype.setData = function(d){
        if(this.option.series[0].data[0] !== d){
            this.option.xAxis.data[0] = d + '%';
            this.option.series[0].data[0].value = d;
            if(d < 50){
                this.option.series[0].data[0].itemStyle.color = COLOR_SUCCESS;
            }else if(d>=50 && d<80){
                this.option.series[0].data[0].itemStyle.color = COLOR_WARNING;
            }else{
                this.option.series[0].data[0].itemStyle.color = COLOR_DANGER;
            }
            this.render();
        }
    };

    SingleRateBar.prototype.render = function(){
        this.echars.setOption(this.option);
    };

    SingleRateBar.prototype.destroy = function(){
        this.echars.dispose();
    };

    return SingleRateBar;
});