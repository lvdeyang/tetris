+function(){

    //֧�ֶ�̬����
    var name, dependences, definition;
    if(arguments.length >= 3){
        name = arguments[0];
        dependences = arguments[1];
        definition = arguments[2];
    }else if(arguments.length === 2){
        dependences = arguments[0];
        definition = arguments[1];
    }else if(arguments.length === 1){
        definition = arguments[0];
    }else if(arguments.length < 1){
        return;
    }

    dependences = dependences || [];

    if(typeof define === 'function'){
        //amd �� cmd ����
        define(dependences, definition);
    }else{

        var exports = definition.apply(window);

        //ֱ����չwindow����
        if(exports && typeof exports==='object'){
            window[name] = exports;
        }
    }

}([
    'd3',
    'ChartAxis',
    'ChartPoint'
], function(d3, ChartAxis, ChartPoint){

    function ChartD3Line(selector, axis, points){
        this.selector = selector;
        this.$el = d3.select(selector);
        this.axis = axis;
        this.points = points;
    }

    ChartD3Line.prototype.draw = function(){

    };

    return ChartD3Line;
});
