/**
 * Created by ldy on 2019/3/19.
 */
define([
    'text!' + window.APPPATH + 'component/dialog/column/column-dialog.html',
    'restfull',
    'jquery',
    'vue',
    'element-ui',
    'css!' + window.APPPATH + 'component/dialog/column/column-dialog.css'
], function(tpl, ajax, $, Vue){

    var pluginName = 'column-dialog';

    var ON_REGION_DIALOG_CLOSE = 'on-column-dialog-close';

    Vue.component(pluginName, {
        template: tpl,
        data:function(){
            return {
                visible:false,
                props:{
                    label: 'name',
                    children: 'subColumns'
                },
                data:[],
                uri:'',
                checked:[],
                loading:false,
                selected:[],
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
                });
            },
            handleColumnClose:function(){
                var self = this;
                self.visible = false;
                self.data.splice(0, self.data.length);
                self.loading = false;
                self.checked.splice(0, self.checked.length)
                self.uri = '';
                self.selected.splice(0, self.selected.length);
            },
            handleColumnBindingOk:function(){
                var self = this;
                var selected = [];
                var buff = self.buff;
                var checkedNodes = self.$refs.columnTree.getCheckedNodes();

                if(checkedNodes && checkedNodes.length > 0){
                    for(var i=0; i<checkedNodes.length; i++){
                        if(checkedNodes[i].subColumns == null){
                            selected.push(checkedNodes[i]);
                        }
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
                    self.visible =false;
                };

                self.$emit(ON_REGION_DIALOG_CLOSE, selected, buff, startLoading, endLoading, close);
            }
        },
        mounted: function(){

        }
    });

});