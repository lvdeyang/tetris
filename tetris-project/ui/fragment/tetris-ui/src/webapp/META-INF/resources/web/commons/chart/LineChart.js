/**
 * 折线图
 * 作者：lvdeyang
 * {
 *     id:'图表id',
 *     title:'图表标题',
 *     $el:'dom容器，这里要dom原生对象',
 *     margin:'坐标轴距离边框的距离',
 *     x:{
 *         type:'数据类型(enum)',
 *         ticks:'横坐标点数',
 *         format:'fn 格式化数据',
 *         domain:['最小值', '最大值']
 *     },
 *     y:{
 *         type:'数据类型(basic|day|hour|minute|second|dateTime|enum)',
 *         ticks:'纵坐标点数',
 *         format:'fn 格式化数据',
 *         domain:['最小值', '最大值']
 *     },
 *     data:[{
 *         color:'线条颜色',
 *         name:'图例',
 *         curve:'是否是曲线',
 *         points:'是否画点',
 *         x:['横坐标数据'],
 *         y:['纵坐标数据']
 *     }]
 * }
 */
function LineChart(config){
    var classes = {
        wrapper:'chart-wrapper',
        container:'chart-line-container',
        header:'chart-line-header',
        title:'chart-line-title',
        legends:'chart-line-legends',
        legend:'chart-line-legend',
        legendIcon:'chart-line-legend-icon',
        legendName:'chart-line-legend-name',
        axisX:'axis-x-'+config.id,
        axisY:'axis-y-' + config.id,
        gridLine:'d3-grid-line',
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
         .dispatchEvent()
         .legend();
}

/**
 * 销毁折线图
 */
LineChart.prototype.destroy = function(){
    this.config.$el.selectAll('.'+this.classes.wrapper).remove();
};

/**
 * 初始绘制
 */
LineChart.prototype.draw = function(){
    var self = this;
    var config = this.config;

    var $svg = config.$el.select('.'+this.classes.container).append('svg');
    var svgSize = this.svgSize();
    var width = svgSize.width;
    var height = svgSize.height;
    $svg.attr('width', width)
        .attr('height', height);

    var axisXLength = this.axisXLength();
    var axisYLength = this.axisYLength();

    //axis-x
    var $axisX,
        $xGridLines;

    var scaleX = null;
    if(config.x.type === 'basic'){
        scaleX = d3.scaleLinear().domain(config.x.domain?config.x.domain:this.domain(config.data, 'x')).range([0, axisXLength]);
    }else if('day|hour|minute|second|dateTime'.indexOf(config.x.type) >= 0){
        scaleX = d3.scaleTime().domain(config.x.domain?config.x.domain:this.domain(config.data, 'x')).range([0, axisXLength]);
    }else if(config.x.type === 'enum'){
        var step = axisXLength/(config.x.domain.length-1);
        var range = [];
        for(var i=0; i<config.x.domain.length; i++){
            range.push(i*step);
        }
        scaleX = d3.scaleOrdinal().domain(config.x.domain).range(range);
    }

    this.axisX = d3.axisBottom().scale(scaleX).ticks(config.x.ticks);
    if(typeof config.x.format === 'function'){
        this.axisX.tickFormat(config.x.format);
    }

    $axisX = $svg.append('g').classed(this.classes.axisX, true);
    $axisX.attr('transform', function(){
        return 'translate('+config.margin+', '+(height-config.margin)+')';
    }).call(this.axisX);

    $axisX.selectAll('g.tick').each(function(d, i){
        d3.select(this).append('line').classed(self.classes.gridLine, true);
    });
    $xGridLines = $axisX.selectAll('line.'+this.classes.gridLine);
    $xGridLines.each(function(d, i){
        d3.select(this)
            .attr('x1', 0)
            .attr('y1', 0)
            .attr('x2', 0)
            .attr('y2', -axisYLength);
    });

    //axis-y
    var $axisY,
        $yGridLines;

    var scaleY = null;
    if(config.y.type === 'basic'){
        scaleY = d3.scaleLinear().domain(config.y.domain?config.y.domain:this.domain(config.data, 'y')).range([0, axisYLength]);
    }else if(config.y.type==='date' || config.y.type==='time' || config.y.type==='dateTime'){
        scaleY = d3.scaleTime().domain(config.y.domain?config.y.domain:this.domain(config.data, 'y')).range([0, axisYLength]);
    }else if(config.y.type === 'enum'){
        var step = axisYLength/(config.y.domain.length-1);
        var range = [];
        for(var i=0; i<config.y.domain.length; i++){
            range.push(i*step);
        }
        scaleY = d3.scaleOrdinal().domain(config.y.domain).range(range);
    }

    this.axisY = d3.axisLeft().scale(scaleY).ticks(config.y.ticks);
    if(typeof config.y.format === 'function'){
        this.axisY.tickFormat(config.y.format);
    }

    $axisY = $svg.append('g').classed(this.classes.axisY, true);
    $axisY.attr('transform', function(){
        return 'translate('+config.margin+', '+5+')';
    }).call(this.axisY);

    var $yTicks = $axisY.selectAll('g.tick');
    $yTicks.each(function(d, i){
        if(i < ($yTicks._groups[0].length-1)){
            d3.select(this).append('line').classed(self.classes.gridLine, true);
        }
    });
    $yGridLines = $axisY.selectAll('line.'+this.classes.gridLine);
    $yGridLines.each(function(d, i){
        d3.select(this)
            .attr('x1', 0)
            .attr('y1', 0)
            .attr('x2', axisXLength)
            .attr('y2', 0);
    });

    /**
     * 画线
     * line:{
     *     generator:d3.line(),
     *     data:'绑定数据',
     *     $lineGroup:d3对象,
     *     $path:d3对象
     * }
     */
    this.lines = [];
    for(var i=0; i<config.data.length; i++){
        var line = {
            data:config.data[i]
        };
        this.lines.push(line);
        line.generator = function(line, x){
            var line_generator = d3.line()
                .x(function(d){return scaleX(d);})
                .y(function(d, index){return scaleY(line.data.y[index]);});
            if(line.data.curve) line_generator.curve(d3.curveMonotoneX);
            return line_generator(x);
        };
        var lineGroupClass = this.classes.lineGroupPrefix+config.id+'-'+i,
            pathClass = this.classes.pathPrefix+config.id+'-'+i;
        line.$lineGroup = $svg.append('g').classed(lineGroupClass, true);
        line.$path = line.$lineGroup.append('path').classed(pathClass, true);
        line.$lineGroup.attr('transform', function(){
            return 'translate('+config.margin+', '+5+')';
        });

        line.$path.attr('d', line.generator(line, line.data.x))
            .style('fill', 'none')
            .style('stroke', line.data.color);

        if(line.data.points){
            line.$lineGroup.selectAll('circle')
                .data(line.data.x)
                .enter()
                .append('circle')
                .attr('r', 3)
                .style('fill', '#fff')
                .style('stroke', line.data.color)
                .style('cursor', 'pointer');

            line.$lineGroup.selectAll('circle')
                .data(line.data.x)
                .attr('cx', function(d){
                    return scaleX(d);
                })
                .attr('cy', function(d, index){
                    return scaleY(line.data.y[index]);
                });
            line.$lineGroup.selectAll('circle')
                .data(line.data.x)
                .exit()
                .remove();
        }
    }

    return this;
};

/**
 * 图例
 */
LineChart.prototype.legend = function(){
    var config = this.config;
    var $header = config.$el.select('.'+this.classes.header);
    $header.append('div').classed(this.classes.title, true).text(config.title);
    var $legends = $header.append('div').classed(this.classes.legends, true);
    for(var i=0; i<config.data.length; i++){
        var $legend = $legends.append('span').classed(this.classes.legend, true);
        $legend.append('span').classed(this.classes.legendIcon, true).classed('chart-icon-line-legend', true).style('color', config.data[i].color);
        $legend.append('span').classed(this.classes.legendName, true).text(config.data[i].name);
    }
};

/**
 * 重绘
 */
LineChart.prototype.reDraw = function(){
    this.resize(3)
         .reDrawAxisX()
         .reDrawAxisY()
         .reDrawLines();
};

/**
 * 重绘x坐标轴
 */
LineChart.prototype.reDrawAxisX = function(){
    var config = this.config;
    var axisXLength = this.axisXLength();
    var axisYLength = this.axisYLength();
    var self = this;
    var $axisX = config.$el.select('.'+this.classes.axisX);
    $axisX.selectAll('line.'+this.classes.gridLine).remove();
    if(config.x.type === 'enum'){
        var step = axisXLength/(config.x.domain.length-1);
        var range = [];
        for(var i=0; i<config.x.domain.length; i++){
            range.push(i*step);
        }
        this.axisX.scale().domain(config.x.domain).range(range);
    }else{
        this.axisX.scale().domain(config.x.domain?config.x.domain:this.domain(config.data, 'x'));
    }
    $axisX.transition().call(this.axisX);
    $axisX.selectAll('g.tick').each(function(d, i){
        d3.select(this).append('line').classed(self.classes.gridLine, true);
    });
    var $xGridLines = $axisX.selectAll('line.'+this.classes.gridLine);
    $xGridLines.each(function(d, i){
        d3.select(this)
            .attr('x1', 0)
            .attr('y1', 0)
            .attr('x2', 0)
            .attr('y2', -axisYLength);
    });
    return this;
};

/**
 * 重绘y坐标轴
 */
LineChart.prototype.reDrawAxisY = function(){
    var config = this.config;
    var axisXLength = this.axisXLength();
    var axisYLength = this.axisYLength();
    var self = this;
    var $axisY = config.$el.select('.'+this.classes.axisY);
    $axisY.selectAll('line.'+this.classes.gridLine).remove();
    if(config.y.type === 'enum'){
        var step = axisYLength/(config.y.domain.length-1);
        var range = [];
        for(var i=0; i<config.y.domain.length; i++){
            range.push(i*step);
        }
        this.axisY.scale().domain(config.y.domain).range(range);
    }else{
        this.axisY.scale().domain(config.y.domain?config.y.domain:this.domain(config.data, 'y'));
    }
    $axisY.transition().call(this.axisY);
    var $yTicks = $axisY.selectAll('g.tick');
    $yTicks.each(function(d, i){
        if(i < ($yTicks._groups[0].length-1)) {
            d3.select(this).append('line').classed(self.classes.gridLine, true);
        }
    });
    var $yGridLines = $axisY.selectAll('line.'+this.classes.gridLine);
    $yGridLines.each(function(d, i){
        d3.select(this)
            .attr('x1', 0)
            .attr('y1', 0)
            .attr('x2', axisXLength)
            .attr('y2', 0);
    });
    return this;
};

/**
 * 尺寸修改
 * @param operation
 *      1:不调用x坐标轴的call方法, 不更新x坐标轴的网格
 *      2:不调用y坐标轴的call方法, 不更新y坐标轴的网格
 *      3:二者都不调用
 */
LineChart.prototype.resize = function(operation){
    var config = this.config;
    var $svg = config.$el.select('svg');
    var svgSize = this.svgSize();
    var width = svgSize.width;
    var height = svgSize.height;
    var axisXLength = this.axisXLength();
    var axisYLength = this.axisYLength();
    $svg.attr('width', width)
        .attr('height', height);

    this.axisX.scale().range([0, axisXLength]);

    var $axisX = config.$el.select('.'+this.classes.axisX);
    $axisX.attr('transform', function(){
        return 'translate('+config.margin+', '+(height-config.margin)+')';
    });
    if(operation!==1 && operation!==3){
        $axisX.transition().call(this.axisX);
        var $xGridLines = $axisX.selectAll('line.'+this.classes.gridLine);
        $xGridLines.each(function(d, i){
            d3.select(this).attr('y2', -axisYLength);
        });
    }

    this.axisY.scale().range([0, axisYLength]);

    var $axisY = config.$el.select('.'+this.classes.axisY);
    $axisY.attr('transform', function(){
        return 'translate('+config.margin+', '+5+')';
    });
    if(operation!==2 && operation!==3){
        $axisY.transition().call(this.axisY);
        var $yGridLines = $axisY.selectAll('line.'+this.classes.gridLine);
        $yGridLines.each(function(d, i){
            d3.select(this).attr('x2', axisXLength);
        });
    }

    return this;
};

/**
 * 重绘线条
 */
LineChart.prototype.reDrawLines = function(){
    var self = this;
    var config = this.config;
    for(var i=0; i<this.lines.length; i++){
        var line = this.lines[i];
        line.$lineGroup.attr('transform', function(){
            return 'translate('+config.margin+', '+5+')';
        });
        line.$path.attr('d', line.generator(line, line.data.x));
        if(line.data.points){
            line.$lineGroup.selectAll('circle')
                .data(line.data.x)
                .enter()
                .append('circle')
                .attr('r', 3)
                .style('fill', '#fff')
                .style('stroke', line.data.color)
                .style('cursor', 'pointer');

            line.$lineGroup.selectAll('circle')
                .data(line.data.x)
                .attr('cx', function(d){
                    return self.axisX.scale()(d);
                })
                .attr('cy', function(d, index){
                    return self.axisY.scale()(line.data.y[index]);
                });
            line.$lineGroup.selectAll('circle')
                .data(line.data.x)
                .exit()
                .remove();
        }
    }
};

/**
 * {
 *     color:'线条颜色',
 *     name:'图例',
 *     x:['横坐标数据'],
 *     y:['纵坐标数据']
 * }
 */
LineChart.prototype.addLine = function(data){
    var self = this;
    var config = this.config;
    config.data.push(data);
    var line = {};
    this.lines.push(line);
    line.data = data;

    var line_generator = d3.line()
        .curve(d3.curveMonotoneX)
        .x(function(d){return self.axisX.scale()(d);})
        .y(function(d, index){return self.axisY.scale()(line.data.y[index]);});
    line.generator = line_generator;

    var lineGroupClass = this.classes.lineGroupPrefix+config.id+'-'+this.lines.length-1,
        pathClass = this.classes.pathPrefix+config.id+'-'+this.lines.length-1;

    var $svg = config.$el.select('svg');
    line.$lineGroup = $svg.append('g').classed(lineGroupClass, true);
    line.$path = line.$lineGroup.append('path').classed(pathClass, true);
    line.$lineGroup.attr('transform', function(){
        return 'translate('+config.margin+', '+config.margin+')';
    });

    line.$path.attr('d', line_generator(line.data.x))
        .style('fill', 'none')
        .style('stroke', line.data.color);

    line.$lineGroup.selectAll('circle')
        .data(line.data.x)
        .enter()
        .append('circle')
        .attr('r', 3)
        .style('fill', '#fff')
        .style('stroke', line.data.color);

    line.$lineGroup.selectAll('circle')
        .data(line.data.x)
        .attr('cx', function(d){
            return self.axisX.scale()(d);
        })
        .attr('cy', function(d, index){
            return self.axisY.scale()(line.data.y[index]);
        });
    line.$lineGroup.selectAll('circle')
        .data(line.data.x)
        .exit()
        .remove();

};

/**
 * 数据最大值最小值
 */
LineChart.prototype.domain = function(data, scope){
    var range = [];
    var total = [];
    for(var i=0; i<data.length; i++){
        for(var j=0; j<data[i][scope].length; j++){
            total.push(data[i][scope][j]);
        }
    }
    var min = d3.min(total);
    var max = d3.max(total);
    if(scope === 'x'){
        range.push(min);
        range.push(max);
    }else{
        range.push(max);
        range.push(min);
    }
    return range;
};

/**
 * 计算坐标轴宽度
 */
LineChart.prototype.axisXLength = function(){
    var config = this.config;
    var svgSize = this.svgSize();
    var width = svgSize.width;
    var axisXLength = width - 2*config.margin;
    return axisXLength;
};

/**
 * 计算坐标轴高度
 */
LineChart.prototype.axisYLength = function(){
    var config = this.config;
    var svgSize = this.svgSize();
    var height = svgSize.height;
    var axisYLength = height - config.margin - 5;
    return axisYLength;
};

/**
 * 获取画布尺寸
 */
LineChart.prototype.svgSize = function(){
    var config = this.config;
    var $container = config.$el.select('.'+this.classes.container);
    return {
        width:parseFloat($container._groups[0][0].clientWidth),
        height:parseFloat($container._groups[0][0].clientHeight)
    }
};

/**
 * 调度事件
 */
LineChart.prototype.dispatchEvent = function(){
    var config = this.config;
    config.$el._groups[0][0].addEventListener('mousemove', function(e){
        config.$el.selectAll('circle').each(function(){
            var $node = d3.select(this);
            if($node.attr('r') == 5){
                $node.attr('r', '3');
            }
        });
        if(e.target.nodeName === 'circle'){
            d3.select(e.target).attr('r', '5');
        }
    });
    return this;
};

/**
 * 对象类型
 */
LineChart.prototype.instanceOf = function(){
    return 'LineChart';
};