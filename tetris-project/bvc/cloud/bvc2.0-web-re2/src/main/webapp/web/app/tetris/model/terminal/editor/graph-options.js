define(function(){

    function DefaultTreeSeries(id, name, data){
        this.type = 'tree';
        this.id = id;
        this.name = name;
        this.data = [data];
        this.top = '5%';
        this.left = '2%';
        this.bottom = '5%';
        this.right = '28%';

        this.symbolKeepAspect = true;
        this.initialTreeDepth = 10;
        this.roam = true;
        this.expandAndCollapse = false;
        this.lineStyle = {width:1};

        this.label = {
            backgroundColor: '#fff',
            color:'#303133',
            position: 'right',
            verticalAlign: 'middle',
            fontSize:16,
            align: 'left'
        };
        this.animationDuration = 0;
        this.animationDurationUpdate = 0;
        this.symbol = function(){
            var params = arguments[1];
            if(params.data.type){
                return 'path://' + params.data.icon;
            }else {
                return 'circle';
            }
        };
        this.symbolSize = function(){
            var params = arguments[1];
            if(params.data.type){
                return params.data.size;
            }else{
                return 7;
            }
        };
        this.itemStyle = {
            color:'#fff',
            borderColor:'#c23531'
        };
    }

    return DefaultTreeSeries;
});