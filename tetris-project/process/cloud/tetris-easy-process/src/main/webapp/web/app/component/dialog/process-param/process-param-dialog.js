/**
 * Created by lvdeyang on 2018/11/23 0023.
 */
define([
    'text!' + window.APPPATH + 'component/dialog/process-param/process-param-dialog.html',
    'restfull',
    'vue',
    'element-ui',
    'css!' + window.APPPATH + 'component/dialog/process-param/process-param-dialog.css',
], function(tpl, ajax, Vue){

    var pluginName = 'process-param-dialog';

    Vue.component(pluginName, {
        template: tpl,
        data:function(){
            return {
                visible:false,
                loading:false,
                table:{
                    data:[],
                    current:''
                },
                tree:{
                    pValue:{
                        props:{
                            label: 'name',
                            children: 'sub'
                        },
                        expandOnClickNode:false,
                        data:[]
                    },
                    rValue:{
                        props:{
                            label: 'name',
                            children: 'sub'
                        },
                        expandOnClickNode:false,
                        data:[]
                    }
                },
                currentKeyPath:''
            }
        },
        methods:{
            open:function(processId){
                var self = this;
                self.processId = processId;
                self.visible = true;
                self.table.data.splice(0, self.table.data.length);
                ajax.post('/access/point/query/by/process/' + self.processId, null, function(data){
                    if(data && data.length>0){
                        for(var i=0; i<data.length; i++){
                            self.table.data.push(data[i]);
                        }
                    }
                });
            },
            handleSelectAccessPointParamClose:function(){
                var self = this;
                self.table.data.splice(0, self.table.data.length);
                self.table.current = '';
                self.tree.pValue.data.splice(0, self.tree.pValue.data.length);
                self.tree.rValue.data.splice(0, self.tree.rValue.data.length);
                self.currentKeyPath = '';
                self.visible = false;
            },
            handleSelectAccessPointCurrentChange:function(currentRow, oldRow){
                if(!currentRow) return;
                var self = this;
                self.tree.pValue.data.splice(0, self.tree.pValue.data.length);
                self.tree.rValue.data.splice(0, self.tree.rValue.data.length);
                self.tree.pValue.currentKeyPath = '';
                self.tree.rValue.currentKeyPath = '';
                ajax.post('/access/point/param/list/tree', {
                    accessPointId:currentRow.id
                }, function(data){
                    var paramValues = data.paramValues;
                    var returnValues = data.returnValues;
                    if(paramValues && paramValues.length>0) {
                        for (var i = 0; i < paramValues.length; i++) {
                            self.tree.pValue.data.push(paramValues[i]);
                        }
                    }
                    if(returnValues && returnValues.length>0){
                        for(var i=0; i<returnValues.length; i++){
                            self.tree.rValue.data.push(returnValues[i]);
                        }
                    }
                });
                self.table.current = currentRow;
            },
            handleCurrentPValueChange:function(data, node){
                var self = this;
                var currentKeyPath = node.data.primaryKey;
                if(node.level > 1){
                    var currentNode = node.parent;
                    while(true){
                        currentKeyPath = currentNode.data.primaryKey + '.' + currentKeyPath;
                        if(currentNode.level <= 1){
                            break;
                        }else{
                            currentNode = currentNode.parent;
                        }
                    }
                }
                self.currentKeyPath = currentKeyPath;
                self.$refs.rValueTree.setCurrentNode({uuid:null});
            },
            handleCurrentRValueChange:function(data, node){
                var self = this;
                var currentKeyPath = node.data.primaryKey;
                if(node.level > 1){
                    var currentNode = node.parent;
                    while(true){
                        currentKeyPath = currentNode.data.primaryKey + '.' + currentKeyPath;
                        if(currentNode.level <= 1){
                            break;
                        }else{
                            currentNode = currentNode.parent;
                        }
                    }
                }
                self.currentKeyPath = currentKeyPath;
                self.$refs.pValueTree.setCurrentNode({uuid:null});
            },
            selectAccessPointParamSubmit:function(){
                var self = this;
                var startLoading = function(){
                    self.loading = true;
                };
                var endLoading = function(){
                    self.loading = false;
                };
                var done = function(){
                    self.handleSelectAccessPointParamClose();
                };
                self.$emit('on-access-point-param-selected', self.currentKeyPath, startLoading, endLoading, done);
            }
        },
        created:function(){
            var self = this;

        }
    });

});