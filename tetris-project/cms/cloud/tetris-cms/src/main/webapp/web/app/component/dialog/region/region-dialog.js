/**
 * Created by ldy on 2019/3/1.
 */
define([
    'text!' + window.APPPATH + 'component/dialog/region/region-dialog.html',
    'restfull',
    'jquery',
    'vue',
    'element-ui',
    'css!' + window.APPPATH + 'component/dialog/region/region-dialog.css'
], function(tpl, ajax, $, Vue){

    var pluginName = 'region-dialog';

    var ON_REGION_DIALOG_CLOSE = 'on-region-dialog-close';

    Vue.component(pluginName, {
        template: tpl,
        data:function(){
            return {
                visible:false,
                props:{
                    label: 'name',
                    children: 'subRegions'
                },
                data:[],
                uri:'',
                checked:[],
                loading:false,
                selected:[],
                checkStrictly: true,
                buff:[]
            }
        },
        methods:{
            open:function(uri, checked, buff){
                var self = this;
                self.visible = true;
                self.uri = uri;
                self.buff = buff;
                self.checked = [];
                if(checked && checked.length>0){
                    for(var i=0; i<checked.length; i++){
                        self.checked.push(checked[i]);
                    }
                }

                self.load();
            },
            load:function(){
                var self = this;
                var uri = self.uri;
                self.data.splice(0, self.data.length);
                ajax.post(uri, null, function(data){
                    if(data && data.length > 0){
                        self.data = data;
                    }

                    self.$nextTick(function(){
                        self.checkStrictly = false;
                    });
                });
            },
            handleRegionClose:function(){
                var self = this;
                self.visible = false;
                self.data.splice(0, self.data.length);
                self.loading = false;
                self.checked.splice(0, self.checked.length)
                self.uri = '';
                self.selected.splice(0, self.selected.length);
            },
            handleRegionBindingOk:function(){
                var self = this;
                var selected = [];
                var buff = self.buff;
                var checkedNodes = self.$refs.regionTree.getCheckedNodes();
                var halfCheckedNodes = self.$refs.regionTree.getHalfCheckedNodes();

                if(checkedNodes && checkedNodes.length > 0){
                    for(var i=0; i<checkedNodes.length; i++){
                        selected.push(checkedNodes[i]);
                    }
                }
                if(halfCheckedNodes && halfCheckedNodes.length > 0){
                    for(var j=0; j<halfCheckedNodes.length; j++){
                        selected.push(halfCheckedNodes[j]);
                    }
                }
                if(!selected || selected.length<=0){
                    self.$message({
                        message:'您没有选择任何数据！',
                        type:'warning'
                    });
                    return;
                }
                var startLoading = function(){
                    self.loading = true;
                };
                var endLoading = function(){
                    self.loading = false;
                };
                var close = function(){
                    self.$nextTick(function(){
                        self.checkStrictly = true;
                    });
                    self.visible =false;
                };

                self.$emit(ON_REGION_DIALOG_CLOSE, selected, buff, startLoading, endLoading, close);
            }
        },
        mounted: function(){

        }
    });

});