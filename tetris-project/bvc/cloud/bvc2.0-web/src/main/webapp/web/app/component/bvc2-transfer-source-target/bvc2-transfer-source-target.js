define([
    'text!' + window.APPPATH + 'component/bvc2-transfer-source-target/bvc2-transfer-source-target.html',
    'vue',
    'element-ui'
], function(tpl, Vue){

    var bvc2TransferSourceTarget = 'bvc2-transfer-source-target';

    Vue.component(bvc2TransferSourceTarget, {
        props:['members', 'values'],
        template:tpl,
        data:function(){
            return {
                value:this.values,
                data:this.members,
                defaultProps: {
                    key: 'id',
                    label: 'name'
                }
            }
        },
        watch:{
            values:function(){
                this.value = this.values;
            }
        },
        methods:{
            renderContent:function(h, option){
                return h('span', {}, [(option.bundleName? option.bundleName + '-':'') + option.name]);
            },
            getSelect:function(){
                var value = this.value;

                var target = [];
                for(var i=0; i<value.length; i++){
                    for(var j=0; j<this.data.length; j++){
                        if(value[i] == this.data[j].id){
                            target.push(this.data[j]);
                            break;
                        }
                    }
                }

                return target;
            },
            changeSource: function(newValue, direction, changeValue){
                var instance = this;
                var value = instance.values;
                var changeValueArr = [];

                value.splice(0, value.length);
                for(var i=0;i<newValue.length;i++){
                    value.push(newValue[i]);
                }

                for(var i=0; i<changeValue.length; i++){
                    for(var j=0; j<this.data.length; j++){
                        if(changeValue[i] == this.data[j].id){
                            changeValueArr.push(this.data[j]);
                            break;
                        }
                    }
                }

                instance.$emit("change-source", instance.getSelect(), direction, changeValueArr);
            },
            filterMethod:function(query, item) {
                var str = (item.bundleName? item.bundleName + '-':'') + item.name;

                return str.indexOf(query) > -1;
            }
        },
        //渲染开始之前
        created:function(){

        }
    });
});
