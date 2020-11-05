define([
    'text!' + window.APPPATH + 'component/bvc2-transfer-role-target/bvc2-transfer-role-target.html',
    'vue',
    'element-ui'
], function(tpl, Vue){

    var bvc2TransferRoleTarget = 'bvc2-transfer-role-target';

    Vue.component(bvc2TransferRoleTarget, {
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
            changeRole: function(newValue, direction, changeValue){
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

                instance.$emit("change-role", instance.getSelect(), direction, changeValueArr);
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
