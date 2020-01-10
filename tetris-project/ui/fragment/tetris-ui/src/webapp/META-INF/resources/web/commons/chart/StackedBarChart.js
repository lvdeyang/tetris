/**
 * 堆叠条形图
 * 作者：lvdeyang
 * {
 *  id:'图表id',
 *  title:'图表标题',
 *  $el:'dom容器，这里要dom原生对象',
 *  margin:'坐标轴距离边框的距离',
 *  x:{
 *      domain:['最小值', '最大值']
 *  },
 *  y:{
 *      ticks:'纵坐标点数',
 *      format:'fn 格式化数据',
 *      domain:['最小值', '最大值']
 *  },
 *  data:{
 *      x:[],
 *      y:[{
 *          color:'颜色',
 *          name:'图例',
 *          values:['纵坐标数据']
 *      }]
 *  }
 * }
 */
function StackedBarChart(config){
    var classes = {
        wrapper:'chart-wrapper',
        container:'chart-stacked-bar-container',
        header:'chart-stacked-bar-header',
        title:'chart-stacked-bar-title',
        legends:'chart-stacked-bar-legends',
        legend:'chart-stacked-bar-legend',
        legendIcon:'chart-stacked-bar-legend-icon',
        legendName:'chart-stacked-bar-legend-name',
        axisX:'axis-x-'+config.id,
        axisY:'axis-y-' + config.id,
        rectGroup:'rect-group',
        lineGroupPrefix:'line-group-',
        pathPrefix:'path-'
    };
    config.$el = d3.select(config.$el);

    var $wrapper = config.$el.append('div').classed(classes.wrapper, true);
    $wrapper.append('div').classed(classes.header, true);
    $wrapper.append('div').classed(classes.container, true);

    this.config = config;
    this.classes = classes;
    this.draw()
        .legend();
}

/**
 * 销毁堆叠条形图
 */
StackedBarChart.prototype.destroy = function(){
    this.config.$el.selectAll('.'+this.classes.wrapper).remove();
};

/**
 * 图例
 */
StackedBarChart.prototype.legend = function(){
    var config = this.config;
    var $header = config.$el.select('.'+this.classes.header);
    $header.append('div').classed(this.classes.title, true).text(config.title);
    var $legends = $header.append('div').classed(this.classes.legends, true);

    for(var i=0; i<config.data.y.length; i++){
        var $legend = $legends.append('span').classed(this.classes.legend, true);
        $legend.append('span').classed(this.classes.legendIcon, true).style('background-color', config.data.y[i].color);
        $legend.append('span').classed(this.classes.legendName, true).text(config.data.y[i].name);
    }
};

/**
 * 初始绘制
 */
StackedBarChart.prototype.draw = function(){
    var self = this;
    var config = this.config;

    var $svg = config.$el.select('.'+this.classes.container).append('svg');
    var svgSize = this.svgSize();
    var width = svgSize.width;
    var height = svgSize.height;
    $svg.attr('width', width)
        .attr('height', height);

    var axisYLength = this.axisYLength();

    //axis-x
    var $axisX;
    var domain = this.domainX();
    var step = this.stepX();
    var range = [];
    for(var i=0; i<domain.length; i++){
        range.push(i*step);
    }
    var scaleX = d3.scaleOrdinal().domain(domain).range(range);
    this.axisX = d3.axisBottom().scale(scaleX).tickFormat(function(d){
        if(d==='prependNode' || d==='appendNode'){
            return '';
        }else{
            return d;
        }
    });
    $axisX = $svg.append('g').classed(this.classes.axisX, true);
    $axisX.attr('transform', function(){
        return 'translate('+config.margin+', '+(height-config.margin)+')';
    }).call(this.axisX);

    //axis-y
    var $axisY;
    var scaleY = d3.scaleLinear().domain(config.y.domain?config.y.domain:this.domainY(config.data)).range([0, axisYLength]);
    this.axisY = d3.axisLeft().scale(scaleY).ticks(config.y.ticks);
    if(typeof config.y.format === 'function'){
        this.axisY.tickFormat(config.y.format);
    }
    $axisY = $svg.append('g').classed(this.classes.axisY, true);
    $axisY.attr('transform', function(){
        return 'translate('+config.margin+', '+5+')';
    }).call(this.axisY);

    //画条形图
    this.drawRect();
    return this;
};

/**
 * 画矩形
 */
StackedBarChart.prototype.drawRect = function(){
    var config = this.config;
    var $svg = config.$el.select('svg');

    $svg.select('.'+this.classes.rectGroup).remove();

    var axisYLength = this.axisYLength();
    var step = this.stepX();

    var $rectGroup = $svg.append('g')
        .classed(this.classes.rectGroup, true)
        .attr('transform', function(){
            return 'translate('+config.margin+', '+5+')';
        });
    var width = step * 0.8;
    for(var i=0; i<config.data.x.length; i++){
        var left = this.axisX.scale()(config.data.x[i]) - width/2;
        for(var j=0; j<config.data.y.length; j++){
            var y = config.data.y[j];
            var height = axisYLength - this.axisY.scale()(y.values[i]);
            var totalHeight = 0;
            for(var m=0; m<=j; m++){
                totalHeight += (axisYLength - this.axisY.scale()(config.data.y[m].values[i]));
            }
            var top = axisYLength - totalHeight;
            $rectGroup.append('rect')
                .attr('x', left)
                .attr('y', top)
                .attr('width', width)
                .attr('height', height)
                .style('fill', y.color)
                .style('stroke', '#fff');
        }
    }
    return this;
};

/**
 * 尺寸修改
 * @param operation
 *      1:不调用x坐标轴的call方法, 不更新x坐标轴的网格
 *      2:不调用y坐标轴的call方法, 不更新y坐标轴的网格
 *      3:二者都不调用
 */
StackedBarChart.prototype.resize = function(operation){
    var config = this.config;
    var $svg = config.$el.select('svg');
    var svgSize = this.svgSize();
    var width = svgSize.width;
    var height = svgSize.height;
    var axisYLength = this.axisYLength();
    $svg.attr('width', width)
        .attr('height', height);

    var domain = this.domainX();
    var step = this.stepX();
    var range = [];
    for(var i=0; i<domain.length; i++){
        range.push(i*step);
    }
    this.axisX.scale().range(range);

    var $axisX = config.$el.select('.'+this.classes.axisX);
    $axisX.attr('transform', function(){
        return 'translate('+config.margin+', '+(height-config.margin)+')';
    });
    if(operation!==1 && operation!==3){
        $axisX.transition().call(this.axisX);
    }

    this.axisY.scale().range([0, axisYLength]);

    var $axisY = config.$el.select('.'+this.classes.axisY);
    $axisY.attr('transform', function(){
        return 'translate('+config.margin+', '+5+')';
    });
    if(operation!==2 && operation!==3){
        $axisY.transition().call(this.axisY);
    }

    return this;
};

/**
 * 计算横坐标点间距
 */
StackedBarChart.prototype.stepX = function(){
    var config = this.config;
    var axisXLength = this.axisXLength();
    return axisXLength/(config.x.domain.length+1);
};

/**
 * x值定义域
 */
StackedBarChart.prototype.domainX = function(){
    var config = this.config;
    var domain = ['prependNode'];
    for(var i=0; i<config.x.domain.length; i++){
        domain.push(config.x.domain[i]);
    }
    domain.push('appendNode');
    return domain;
};

/**
 * y值定义域
 */
StackedBarChart.prototype.domainY = function(data){
    var range = [];
    var total = [];
    for(var i=0; i<data.x.length; i++){
        var value = 0;
        for(var j=0; j<data.y.length; j++){
            value += data.y[j].values[i];
        }
        total.push(value);
    }
    range.push(d3.max(total));
    range.push(0);
    return range;
};

/**
 * 计算坐标轴宽度
 */
StackedBarChart.prototype.axisXLength = function(){
    var config = this.config;
    var svgSize = this.svgSize();
    var width = svgSize.width;
    var axisXLength = width - 2*config.margin;
    return axisXLength;
};

/**
 * 计算坐标轴高度
 */
StackedBarChart.prototype.axisYLength = function(){
    var config = this.config;
    var svgSize = this.svgSize();
    var height = svgSize.height;
    var axisYLength = height - config.margin - 5;
    return axisYLength;
};

/**
 * 获取画布尺寸
 */
StackedBarChart.prototype.svgSize = function(){
    var config = this.config;
    var $container = config.$el.select('.'+this.classes.container);
    return {
        width:parseFloat($container._groups[0][0].clientWidth),
        height:parseFloat($container._groups[0][0].clientHeight)
    }
};

/**
 * 对象类型
 */
StackedBarChart.prototype.instanceOf = function(){
    return 'StackedBarChart';
};
