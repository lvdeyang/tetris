/**
 * Created by ldyon 2019/3/2.
 */
define([
    'text!' + window.APPPATH + 'component/dialog/classify/classify-dialog.html',
    'restfull',
    'jquery',
    'vue',
    'element-ui',
    'css!' + window.APPPATH + 'component/dialog/classify/classify-dialog.css'
], function(tpl, ajax, $, Vue){

    var pluginName = 'classify-dialog';

    var ON_CLASSIFY_DIALOG_CLOSE = 'on-classify-dialog-close';

    Vue.component(pluginName, {
        template: tpl,
        data:function(){
            return {
                visible:false,
                uri:'',
                except:[],
                rows:[],
                currentPage:0,
                pageSize:50,
                total:0,
                loading:false,
                selected:[],
                buff:[]
            }
        },
        methods:{
            open:function(uri, except, buff){
                var self = this;
                self.visible = true;
                self.uri = uri;
                self.currentPage = 0;
                self.except = [];
                self.buff = buff;
                if(except && except.length>0){
                    for(var i=0; i<except.length; i++){
                        self.except.push(except[i]);
                    }
                }
                self.load(self.currentPage + 1);
            },
            load:function(currentPage){
                var self = this;
                var uri = self.uri;
                self.rows.splice(0, self.rows.length);
                var params = {
                    pageSize:self.pageSize,
                    currentPage:currentPage
                };
                if(self.except && self.except.length>0){
                    params['except'] = $.toJSON(self.except);
                }
                ajax.post(uri, params, function(data){
                    var rows = data.rows;
                    var total = data.total;
                    for(var i=0; i<rows.length; i++){
                        self.rows.push(rows[i]);
                    }
                    self.currentPage = currentPage;
                    self.total = total;
                });
            },
            handleClassifyClose:function(){
                var self = this;
                self.visible = false;
                self.rows.splice(0, self.rows.length);
                self.currentPage = 0;
                self.total = 0;
                self.loading = false;
                self.except = [];
                self.uri = '';
                self.selected.splice(0, self.selected.length);
            },
            handleClassifySelectionChange:function(val){
                var self = this;
                if(val) self.selected = val;
            },
            handleCurrentChange:function(currentPage){
                var self = this;
                self.load(currentPage);
            },
            handleClassifyBindingOk:function(){
                var self = this;
                var selected = self.selected;
                var buff = self.buff;
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
                self.$emit(ON_CLASSIFY_DIALOG_CLOSE, selected, buff, startLoading, endLoading, close);
            }
        }
    });

});
