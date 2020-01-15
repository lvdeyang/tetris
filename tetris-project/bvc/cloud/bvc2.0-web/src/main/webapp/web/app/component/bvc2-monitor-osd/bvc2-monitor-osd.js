define([
    'text!' + window.APPPATH + 'component/bvc2-monitor-osd/bvc2-monitor-osd.html',
    'restfull',
    'jquery',
    'vue',
    'element-ui',
    'date',
    'bvc2-dialog-single-subtitle',
    'css!' + window.APPPATH + 'component/bvc2-monitor-osd/bvc2-monitor-osd.css'
], function(tpl, ajax, $, Vue){

	//组件名称
    var pluginName = 'bvc2-monitor-osd';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data:function(){
            return {
                layerTypes:[],
                table:{
                    osd:{
                        data:[],
                        page:{
                            currentPage:0,
                            pageSize:20,
                            total:0
                        },
                        currentOsd:''
                    },
                    layer:{
                        data:[]
                    }
                },
                dialog:{
                    addOsd:{
                        visible:false,
                        name:'',
                        loading:false
                    },
                    editOsd:{
                        visible:false,
                        id:'',
                        name:'',
                        loading:false
                    },
                    addLayer:{
                        visible:false,
                        layerType:'字幕',
                        subtitleId:'',
                        subtitleName:'',
                        subtitleUsername:'',
                        x:'',
                        y:'',
                        loading:false
                    },
                    editLayer:{
                        visible:false,
                        id:'',
                        subtitleId:'',
                        subtitleName:'',
                        subtitleUsername:'',
                        x:'',
                        y:'',
                        loading:false
                    }
                }
            }
        },
        computed:{

        },
        watch:{

        },
        methods:{
            loadOsd:function(currentPage){
                var self = this;
                self.table.osd.data.splice(0, self.table.osd.data.length);
                ajax.post('/monitor/osd/load', {
                    currentPage:currentPage,
                    pageSize:self.table.osd.page.pageSize
                }, function(data){
                    var total = data.total;
                    var rows = data.rows;
                    if(rows && rows.length>0){
                        for(var i=0; i<rows.length; i++){
                            self.table.osd.data.push(rows[i]);
                        }
                        self.table.osd.page.total = total;
                        self.table.osd.page.currentPage = currentPage;
                    }
                });
            },
            loadLayers:function(){
                var self = this;
                self.table.layer.data.splice(0, self.table.layer.data.length);
                ajax.post('/monitor/osd/layer/load', {
                    osdId:self.table.osd.currentOsd.id
                }, function(data){
                    if(data && data.length>0){
                        for(var i=0; i<data.length; i++){
                            self.table.layer.data.push(data[i]);
                        }
                    }
                });
            },
            layerSort:function(){
                var self = this;
                var layers = [];
                for(var i=0; i<self.table.layer.data.length; i++){
                    layers.push({
                        id:self.table.layer.data[i].id,
                        layerIndex:i
                    });
                }
                if(layers.length > 0){
                    ajax.post('/monitor/osd/layer/sort', {
                        layers: $.toJSON(layers)
                    }, function(){
                        for(var i=0; i<self.table.layer.data.length; i++){
                            self.table.layer.data[i].layerIndex = i;
                        }
                        self.$message({
                            type:'success',
                            message:'重新排序！'
                        })
                    });
                }
            },
            handleOsdSizeChange:function(size){
                var self = this;
                self.table.osd.page.pageSize = size;
                self.loadOsd(1);
            },
            handleOsdCurrentChange:function(currentPage){
                var self  = this;
                self.load(currentPage);
            },
            handleAddOsd:function(){
                var self = this;
                self.dialog.addOsd.visible = true;
            },
            osdRowEdit:function(scope){
                var self = this;
                var row = scope.row;
                self.dialog.editOsd.id = row.id;
                self.dialog.editOsd.name = row.name;
                self.dialog.editOsd.visible = true;
            },
            osdRowDelete:function(scope){
                var self = this;
                var row = scope.row;
                var h = self.$createElement;
                self.$msgbox({
                    title:'危险操作',
                    message:h('div', null, [
                        h('div', {class:'el-message-box__status el-icon-warning'}, null),
                        h('div', {class:'el-message-box__message'}, [
                            h('p', null, ['此操作将永久删除osd，且不可恢复，是否继续?'])
                        ])
                    ]),
                    type:'wraning',
                    showCancelButton: true,
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    beforeClose:function(action, instance, done){
                        instance.confirmButtonLoading = true;
                        if(action === 'confirm'){
                            ajax.post('/monitor/osd/remove/' + row.id, null, function(data, status){
                                instance.confirmButtonLoading = false;
                                done();
                                if(status !== 200) return;
                                for(var i=0; i<self.table.osd.data.length; i++){
                                    if(self.table.osd.data[i].id === row.id){
                                        self.table.osd.data.splice(i ,1);
                                        break;
                                    }
                                }
                                self.table.osd.page.total -= 1;
                                if(self.table.osd.data.length<=0 && self.table.osd.page.currentPage>1){
                                    self.load(self.table.osd.page.currentPage - 1);
                                }
                                if(self.table.osd.currentOsd.id === row.id){
                                    self.table.osd.currentOsd = '';
                                }
                            }, null, [403,404,409,500]);
                        }else{
                            instance.confirmButtonLoading = false;
                            done();
                        }
                    }
                }).catch(function(){});
            },
            handleAddOsdClose:function(){
                var self = this;
                self.dialog.addOsd.name = '';
                self.dialog.addOsd.loading = false;
                self.dialog.addOsd.visible = false;
            },
            handleAddOsdCommit:function(){
                var self = this;
                self.dialog.addOsd.loading = true;
                ajax.post('/monitor/osd/add', {
                    name:self.dialog.addOsd.name
                }, function(data, status){
                    self.dialog.addOsd.loading = false;
                    if(status !== 200) return;
                    self.table.osd.data.push(data);
                    self.table.osd.page.total += 1;
                    self.handleAddOsdClose();
                }, null, [403, 404, 408, 409, 500]);
            },
            handleEditOsdClose:function(){
                var self  = this;
                self.dialog.editOsd.id = '';
                self.dialog.editOsd.name = '';
                self.dialog.editOsd.loading = false;
                self.dialog.editOsd.visible = false;
            },
            handleEditOsdCommit:function(){
                var self = this;
                self.dialog.editOsd.loading = true;
                ajax.post('/monitor/osd/edit/' + self.dialog.editOsd.id, {
                    name:self.dialog.editOsd.name
                }, function(data, status){
                    self.dialog.editOsd.loading = false;
                    if(status !== 200) return;
                    for(var i=0; i<self.table.osd.data.length; i++){
                        if(self.table.osd.data[i].id === data.id){
                            self.table.osd.data.splice(i, 1, data);
                            break;
                        }
                    }
                    self.handleEditOsdClose();
                }, null, [403, 404, 408, 409, 500]);
            },
            osdCurrentChange:function(currentRow, oldRow){
                var self = this;
                self.table.osd.currentOsd = currentRow;
                self.loadLayers();
            },
            handleAddLayer:function(){
                var self = this;
                self.dialog.addLayer.visible = true;
            },
            layerRowUp:function(scope){
                var self = this;
                var row = scope.row;
                var index = null;
                for(var i=0; i<self.table.layer.data.length; i++){
                    if(self.table.layer.data[i] === row){
                        index = i;
                        break;
                    }
                }
                self.table.layer.data.splice(i, 1);
                self.table.layer.data.splice(i-1, 0, row);
                self.layerSort();
            },
            layerRowDown:function(scope){
                var self = this;
                var row = scope.row;
                var index = null;
                for(var i=0; i<self.table.layer.data.length; i++){
                    if(self.table.layer.data[i] === row){
                        index = i;
                        break;
                    }
                }
                self.table.layer.data.splice(i, 1);
                self.table.layer.data.splice(i+1, 0, row);
                self.layerSort();
            },
            layerRowEdit:function(scope){
                var self = this;
                var row = scope.row;
                self.dialog.editLayer.id = row.id;
                self.dialog.editLayer.subtitleId = row.subtitleId;
                self.dialog.editLayer.subtitleName = row.subtitleName;
                self.dialog.editLayer.subtitleUsername = row.subtitleUsername;
                self.dialog.editLayer.x = row.x;
                self.dialog.editLayer.y = row.y;
                self.dialog.editLayer.visible = true;
            },
            layerRowDelete:function(scope){
                var self = this;
                var row = scope.row;
                var h = self.$createElement;
                self.$msgbox({
                    title:'危险操作',
                    message:h('div', null, [
                        h('div', {class:'el-message-box__status el-icon-warning'}, null),
                        h('div', {class:'el-message-box__message'}, [
                            h('p', null, ['此操作将永久删除图层，且不可恢复，是否继续?'])
                        ])
                    ]),
                    type:'wraning',
                    showCancelButton: true,
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    beforeClose:function(action, instance, done){
                        instance.confirmButtonLoading = true;
                        if(action === 'confirm'){
                            ajax.post('/monitor/osd/layer/remove/' + row.id, null, function(data, status){
                                instance.confirmButtonLoading = false;
                                done();
                                if(status !== 200) return;
                                self.table.layer.data.splice(0 ,self.table.layer.data.length);
                                if(data && data.length>0){
                                    for(var i=0; i<data.length; i++){
                                        self.table.layer.data.push(data[i]);
                                    }
                                }
                            }, null, [403,404,409,500]);
                        }else{
                            instance.confirmButtonLoading = false;
                            done();
                        }
                    }
                }).catch(function(){});
            },
            handleAddLayerClose:function(){
                var self = this;
                self.dialog.addLayer.layerType = '字幕';
                self.dialog.addLayer.subtitleId = '';
                self.dialog.addLayer.subtitleName = '';
                self.dialog.addLayer.subtitleUsername = '';
                self.dialog.addLayer.x = '';
                self.dialog.addLayer.y = '';
                self.dialog.addLayer.visible = false;
                self.dialog.addLayer.loading = false;
            },
            handleAddLayerCommit:function(){
                var self = this;
                var layerIndex = self.table.layer.data.length;
                self.dialog.addLayer.loading = true;

                var success = function(data, status){
                    self.dialog.addLayer.loading = false;
                    if(status !== 200) return;
                    self.table.layer.data.push(data);
                    self.handleAddLayerClose();
                };

                if(self.dialog.addLayer.layerType === '字幕'){
                    ajax.post('/monitor/osd/layer/add/subtitle/layer', {
                        osdId:self.table.osd.currentOsd.id,
                        x:self.dialog.addLayer.x,
                        y:self.dialog.addLayer.y,
                        layerIndex:layerIndex,
                        subtitleId:self.dialog.addLayer.subtitleId,
                        subtitleName:self.dialog.addLayer.subtitleName,
                        subtitleUsername:self.dialog.addLayer.subtitleUsername
                    }, success, null, [403, 404, 408, 409, 500]);
                }else if(self.dialog.addLayer.layerType === '日期'){
                    ajax.post('/monitor/osd/layer/add/date/layer', {
                        osdId:self.table.osd.currentOsd.id,
                        x:self.dialog.addLayer.x,
                        y:self.dialog.addLayer.y,
                        layerIndex:layerIndex
                    }, success, null, [403, 404, 408, 409, 500]);
                }else if(self.dialog.addLayer.layerType === '时间'){
                    ajax.post('/monitor/osd/layer/add/datetime/layer', {
                        osdId:self.table.osd.currentOsd.id,
                        x:self.dialog.addLayer.x,
                        y:self.dialog.addLayer.y,
                        layerIndex:layerIndex
                    }, success, null, [403, 404, 408, 409, 500]);
                }else if(self.dialog.addLayer.layerType === '设备名称'){
                    ajax.post('/monitor/osd/layer/add/devname/layer', {
                        osdId:self.table.osd.currentOsd.id,
                        x:self.dialog.addLayer.x,
                        y:self.dialog.addLayer.y,
                        layerIndex:layerIndex
                    }, success, null, [403, 404, 408, 409, 500]);
                }
            },
            selectedSubtitleWhenAddLayer:function(row, done, endLoading){
                var self = this;
                self.dialog.addLayer.subtitleId = row.id;
                self.dialog.addLayer.subtitleName = row.name;
                self.dialog.addLayer.subtitleUsername = row.username;
                endLoading();
                done();
            },
            handleAddLayerSelectSubtitle:function(){
                var self = this;
                self.$refs.addLayerSelectSubtitleDialog.open(self.dialog.addLayer.subtitleId);
            },
            handleEditLayerClose:function(){
                var self = this;
                self.dialog.editLayer.id = '';
                self.dialog.editLayer.subtitleId = '';
                self.dialog.editLayer.subtitleName = '';
                self.dialog.editLayer.subtitleUsername = '';
                self.dialog.editLayer.x = '';
                self.dialog.editLayer.y = '';
                self.dialog.editLayer.loading = false;
                self.dialog.editLayer.visible = false;
            },
            handleEditLayerSelectSubtitle:function(){
                var self = this;
                self.$refs.editLayerSelectSubtitleDialog.open(self.dialog.editLayer.subtitleId);
            },
            selectedSubtitleWhenEditLayer:function(subtitle, done, endLoading){
                var self = this;
                self.dialog.editLayer.subtitleId = subtitle.id;
                self.dialog.editLayer.subtitleName = subtitle.name;
                self.dialog.editLayer.subtitleUsername = subtitle.username;
                done();
                endLoading();
            },
            handleEditLayerCommit:function(){
                var self = this;
                self.dialog.editLayer.loading = true;
                ajax.post('/monitor/osd/layer/edit/subtitle/layer/' + self.dialog.editLayer.id, {
                    x:self.dialog.editLayer.x,
                    y:self.dialog.editLayer.y,
                    layerIndex:self.dialog.editLayer.layerIndex,
                    subtitleId:self.dialog.editLayer.subtitleId,
                    subtitleName:self.dialog.editLayer.subtitleName,
                    subtitleUsername:self.dialog.editLayer.subtitleUsername
                }, function(data, status){
                    self.dialog.editLayer.loading = false;
                    if(status !== 200) return;
                    for(var i=0; i<self.table.layer.data.length; i++){
                        if(self.table.layer.data[i].id === self.dialog.editLayer.id){
                            self.table.layer.data.splice(i, 1, data);
                            break;
                        }
                    }
                    self.handleEditLayerClose();
                }, null, [404, 403, 408, 409, 500])
            }
        },
        mounted:function(){
            var self = this;
            self.loadOsd(1);
            ajax.post('/monitor/osd/layer/query/types', null, function(data){
                if(data && data.length>0){
                    for(var i=0; i<data.length; i++){
                        self.layerTypes.push(data[i]);
                    }
                }
            });
        }
    });

    return Vue;
});