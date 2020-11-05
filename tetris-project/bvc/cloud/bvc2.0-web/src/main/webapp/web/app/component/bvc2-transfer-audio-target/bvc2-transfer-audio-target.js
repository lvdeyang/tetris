define([
    'text!' + window.APPPATH + 'component/bvc2-transfer-audio-target/bvc2-transfer-audio-target.html',
    'vue',
    'element-ui'
], function(tpl, Vue){

    var bvc2TransferAduioTarget = 'bvc2-transfer-audio-target';

    Vue.component(bvc2TransferAduioTarget, {
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
                var param = $.parseJSON(option.param);
                return h('span', {}, [option.name + (param.roleName? '-' + param.roleName:'')]);
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
            filterMethod:function(query, item) {
                var str = item.name;

                return str.indexOf(query) > -1;
            }
        },
        //渲染开始之前
        created:function(){

        }
    });
});
