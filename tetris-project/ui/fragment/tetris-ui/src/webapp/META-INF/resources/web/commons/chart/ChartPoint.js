+function(){

    //支持动态传参
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
        //amd 或 cmd 环境
        define(dependences, definition);
    }else{

        var exports = definition.apply(window);

        //直接扩展window对象
        if(exports && typeof exports==='object'){
            window[name] = exports;
        }
    }

}([
    'd3'
], function(d3){

    function Point(x, y){
        this.x = x;
        this.y = y;
    }

    Point.prototype.equals = function(other){
        if(this.x===other.x && this.y===other.y){
            return true;
        }else{
            return false;
        }
    };

    return Point;
});
