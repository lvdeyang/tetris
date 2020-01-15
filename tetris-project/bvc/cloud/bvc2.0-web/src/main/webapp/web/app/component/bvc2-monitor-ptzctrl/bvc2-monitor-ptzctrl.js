define([
    'text!' + window.APPPATH + 'component/bvc2-monitor-ptzctrl/bvc2-monitor-ptzctrl.html',
    'restfull',
    'jquery',
    'vue',
    'element-ui',
    'date',
    'css!' + window.APPPATH + 'component/bvc2-monitor-ptzctrl/bvc2-monitor-ptzctrl.css'
], function(tpl, ajax, $, Vue){

	//组件名称
    var pluginName = 'bvc2-monitor-ptzctrl';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data:function(){
            return {
                visible:false,
                protocolSuccess:false,
                bundleId:'',
                bundleName:'',
                layerId:'',
                speed:8,
                $embedWrapper:'',
                points:[],
                dialog:{
                    addPoint:{
                        visible:false,
                        name:'',
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
            unbindEven:function(){
                $(document).unbind('mouseup.ptzctrl.stop');
                return this;
            },
            resetEmbed:function(){
                var self = this;
                self.$embedWrapper.append($(self.$el).find('.player-container').find('embed'));
                return this;
            },
            open:function(bundleInfo){
                var self = this;
                self.bundleId = bundleInfo.bundleId;
                self.layerId = bundleInfo.layerId;
                self.bundleName = bundleInfo.bundleName;
                self.$embedWrapper = bundleInfo.$embedWrapper;
                self.visible = true;
                var self = this;
                $(document).on('mouseup.ptzctrl.stop', function(){
                    if(self.protocolSuccess){
                        self.stop();
                    }
                });
                self.$nextTick(function(){
                    $(self.$el).find('.player-container').append(self.$embedWrapper.find('embed'));
                });
                self.points.splice(0, self.points.length);
                ajax.post('/monitor/point/load', {
                    bundleId:self.bundleId
                }, function(data){
                    if(data && data.length>0){
                        for(var i=0; i<data.length; i++){
                            self.points.push(data[i]);
                        }
                    }
                });
            },
            handleClose:function(){
                var self = this;
                self.bundleId = '';
                self.bundleName = '';
                self.layerId = '';
                self.speed = 8;
                self.points.splice(0, self.points.length);
                if(self.protocolSuccess){
                    self.stop(function(){
                        self.visible = false;
                        self.unbindEven().resetEmbed();
                    });
                }else{
                    self.visible = false;
                    self.unbindEven().resetEmbed();
                }
            },
            vertical:function(direction){
                var self = this;
                self.protocolSuccess = true;
                ajax.post('/monitor/ptzctrl/vertical/'+direction, {
                    bundleId:self.bundleId,
                    layerId:self.layerId,
                    speed:self.speed
                });
            },
            horizontal:function(direction){
                var self = this;
                self.protocolSuccess = true;
                ajax.post('/monitor/ptzctrl/horizontal/' + direction, {
                    bundleId:self.bundleId,
                    layerId:self.layerId,
                    speed:self.speed
                });
            },
            zoom:function(direction){
                var self = this;
                self.protocolSuccess = true;
                ajax.post('/monitor/ptzctrl/zoom/' + direction, {
                    bundleId:self.bundleId,
                    layerId:self.layerId,
                    speed:self.speed
                });
            },
            focus:function(direction){
                var self = this;
                self.protocolSuccess = true;
                ajax.post('/monitor/ptzctrl/focus/' + direction, {
                    bundleId:self.bundleId,
                    layerId:self.layerId,
                    speed:self.speed
                });
            },
            aperture:function(direction){
                var self = this;
                self.protocolSuccess = true;
                ajax.post('/monitor/ptzctrl/aperture/' + direction, {
                    bundleId:self.bundleId,
                    layerId:self.layerId,
                    speed:self.speed
                });
            },
            stop:function(fn){
                var self = this;
                ajax.post('/monitor/ptzctrl/stop', {
                    bundleId:self.bundleId,
                    layerId:self.layerId,
                    speed:self.speed
                }, function(){
                    self.protocolSuccess = false;
                    if(typeof fn === 'function') fn();
                });
            },
            handlePointRemove:function(point){
                var self = this;
                var h = self.$createElement;
                self.$msgbox({
                    title:'操作提示',
                    message:h('div', null, [
                        h('div', {class:'el-message-box__status el-icon-warning'}, null),
                        h('div', {class:'el-message-box__message'}, [
                            h('p', null, ['是否要删除此接入点?'])
                        ])
                    ]),
                    type:'wraning',
                    showCancelButton: true,
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    beforeClose:function(action, instance, done){
                        instance.confirmButtonLoading = true;
                        if(action === 'confirm'){
                            ajax.post('/monitor/point/remove/' + point.id, null, function(data, status){
                                instance.confirmButtonLoading = false;
                                done();
                                if(status !== 200) return;
                                for(var i=0; i<self.points.length; i++){
                                    if(self.points[i].id === point.id){
                                        self.points.splice(i, 1);
                                        break;
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
            handlePointAdd:function(){
                var self = this;
                self.dialog.addPoint.visible = true;
            },
            handleAddPointClose:function(){
                var self = this;
                self.dialog.addPoint.visible = false;
                self.dialog.addPoint.loading = false;
                self.dialog.addPoint.name = '';
            },
            handleAddPointCommit:function(){
                var self = this;
                if(!self.dialog.addPoint.name){
                    self.$message({
                        type:'error',
                        message:'预置点名称不能为空！'
                    });
                    return;
                }
                self.dialog.addPoint.loading = true;
                ajax.post('/monitor/point/add', {
                    bundleId:self.bundleId,
                    bundleName:self.bundleName,
                    layerId:self.layerId,
                    name:self.dialog.addPoint.name
                }, function(data, status){
                    self.dialog.addPoint.loading = false;
                    if(status !== 200) return;
                    if(data){
                        self.points.push(data);
                    }
                    self.handleAddPointClose();
                }, null, [403, 404, 408, 409]);
            },
            handlePointInvoke:function(point){
                var self = this;
                var h = self.$createElement;
                self.$msgbox({
                    title:'操作提示',
                    message:h('div', null, [
                        h('div', {class:'el-message-box__status el-icon-warning'}, null),
                        h('div', {class:'el-message-box__message'}, [
                            h('p', null, ['是否要设置到此预置点?'])
                        ])
                    ]),
                    type:'wraning',
                    showCancelButton: true,
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    beforeClose:function(action, instance, done){
                        instance.confirmButtonLoading = true;
                        if(action === 'confirm'){
                            ajax.post('/monitor/point/invoke/' + point.id, null, function(data, status){
                                instance.confirmButtonLoading = false;
                                done();
                                if(status !== 200) return;
                                if(!point.__active){
                                    for(var i=0; i<self.points.length; i++){
                                        if(self.points[i].__active){
                                            Vue.set(self.points[i], '__active', false);
                                            break;
                                        }
                                    }
                                    Vue.set(point, '__active', true);
                                }
                            }, null, [403,404,409,500]);
                        }else{
                            instance.confirmButtonLoading = false;
                            done();
                        }
                    }
                }).catch(function(){});
            }
        }
    });

    return Vue;
});