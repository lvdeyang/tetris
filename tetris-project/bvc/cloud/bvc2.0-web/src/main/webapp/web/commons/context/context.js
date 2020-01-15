define(function(){

    var cache = {};

    var context = {
        setProp:function(key, value){
            if(!cache[key]){
                cache[key] = value;
            }else{
                console.log('重复的key！');
            }
            return this;
        },
        getProp:function(key){
            return cache[key];
        },
        clearProp:function(key){
        	if(cache[key]){
        		cache[key] = null;
        	}
        }
    };

    return context;
});