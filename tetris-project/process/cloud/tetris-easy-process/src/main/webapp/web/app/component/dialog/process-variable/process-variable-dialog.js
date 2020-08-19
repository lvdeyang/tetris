/**
 * Created by lvdeyang on 2018/11/23 0023.
 */
define([
    'text!' + window.APPPATH + 'component/dialog/process-variable/process-variable-dialog.html',
    'restfull',
    'vue',
    'element-ui',
    'process-param-dialog',
    'css!' + window.APPPATH + 'component/dialog/process-variable/process-variable-dialog.css',
], function(tpl, ajax, Vue){

    var pluginName = 'process-variable-dialog';

    Vue.component(pluginName, {
        template: tpl,
        data:function(){
            return {
                dataTypes:[],
                processId:'',
                visible:false,
                primaryKey:'',
                name:'',
                dataType:'字符串',
                mode:'默认值',
                defaultValue:'',
                expressionValue:'',
                loading:false
            }
        },
        methods:{
            open:function(processId){
                var self = this;
                self.processId = processId;
                self.visible = true;
            },
            handleCreateProcessVariableClose:function(){
                var self = this;
                self.processId = '';
                self.primaryKey = '';
                self.name = '';
                self.dataType = '字符串';
                self.mode = '默认值';
                self.defaultValue = '';
                self.expressionValue = '';
                self.visible = false;
            },
            handleCreateProcessVariableSubmit:function(){
                var self = this;
                self.loading = true;
                ajax.post('/process/variable/add', {
                    processId:self.processId,
                    primaryKey:self.primaryKey,
                    name:self.name,
                    dataType:self.dataType,
                    defaultValue:self.defaultValue,
                    expressionValue:self.expressionValue
                }, function(data, status){
                    self.loading = false;
                    if(status !== 200) return;
                    self.handleCreateProcessVariableClose();
                    self.$emit('on-process-variable-added', data);
                }, null, ajax.NO_ERROR_CATCH_CODE);
            },
            handleSelectAccessPointParam:function(){
                var self = this;
                self.$refs.processParam.open(self.processId);
            },
            handleInsertOperator:function(operator){
                var self = this;
                self.expressionValue += operator;
            },
            onAccessPointParamSelected:function(param, startLoading, endLoading, done){
                var self = this;
                self.expressionValue += ('#' + param);
                done();
            },
            handleModeChange:function(mode){
                var self = this;
                if(mode === '默认值'){
                    self.expressionValue = '';
                }else if(mode === '引用值'){
                    self.defaultValue = '';
                }
            }
        },
        created:function(){
            var self = this;
            ajax.post('/enum/data/type', null, function(data){
                if(data && data.length>0){
                    for(var i=0; i<data.length; i++){
                        self.dataTypes.push(data[i]);
                    }
                }
                self.$nextTick(function(){
                    self.dataType = '字符串';
                });
            });
        }
    });

});