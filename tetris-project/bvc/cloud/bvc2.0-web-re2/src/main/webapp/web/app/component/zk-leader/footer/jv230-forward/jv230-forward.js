define([
    'text!' + window.APPPATH + 'component/zk-leader/footer/jv230-forward/jv230-forward.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'css!' + window.APPPATH + 'component/zk-leader/footer/jv230-forward/jv230-forward.css'
], function (tpl, ajax, $, Vue, config) {

    //组件名称
    var pluginName = 'jv230-forward';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data: function () {
            return {
                baseUrl: window.BASEPATH,
                qt: '',
                layoutStyle:'',
                layout:'',
                splits:{
                    '1':[[{serialNum:0, data:''}]],
                    '2':[[{serialNum:0, data:''},{serialNum:1, data:''}]],
                    '4':[
                        [{serialNum:0, data:''}, {serialNum:1, data:''}],
                        [{serialNum:2, data:''}, {serialNum:3, data:''}]
                    ],
                    '6':[
                        [{serialNum:0, colspan:2, rowspan:2, data:''}, {serialNum:1, data:''}],
                        [{serialNum:2, data:''}],
                        [{serialNum:3, data:''}, {serialNum:4, data:''}, {serialNum:5, data:''}]
                    ],
                    '9':[
                        [{serialNum:0, data:''}, {serialNum:1, data:''}, {serialNum:2, data:'', data:''}],
                        [{serialNum:3, data:''}, {serialNum:4, data:''}, {serialNum:5, data:'', data:''}],
                        [{serialNum:6, data:''}, {serialNum:7, data:''}, {serialNum:8, data:'', data:''}]
                    ],
                    '12':[
                          [{serialNum:0, data:''}, {serialNum:1, data:''}, {serialNum:2, data:''}, {serialNum:3, data:''}],
                          [{serialNum:4, data:''}, {serialNum:5, data:''}, {serialNum:6, data:''}, {serialNum:7, data:''}],
                          [{serialNum:8, data:''}, {serialNum:9, data:''}, {serialNum:10, data:''}, {serialNum:11, data:''}]],
                    '16':[
                        [{serialNum:0, data:''}, {serialNum:1, data:''}, {serialNum:2, data:''}, {serialNum:3, data:''}],
                        [{serialNum:4, data:''}, {serialNum:5, data:''}, {serialNum:6, data:''}, {serialNum:7, data:''}],
                        [{serialNum:8, data:''}, {serialNum:9, data:''}, {serialNum:10, data:''}, {serialNum:11, data:''}],
                        [{serialNum:12, data:''}, {serialNum:13, data:''}, {serialNum:14, data:''}, {serialNum:15, data:''}]]
                },
                bundles:{
                    data:[],
                    props:{
                        label:'name',
                        children:'children'
                    },
                    current:''
                },
                forwards:{
                    data:[]
                }
            }
        },
        computed:{
            bundlesCurrent:function(){
                var self = this;
                return self.bundles.current;
            }
        },
        watch: {
            bundlesCurrent:function(){
                var self = this;
                self.currentBundleChange(self.bundles.current);
            }
        },
        methods: {
            handleWindowClose:function(){
                var self = this;
                self.qt.destroy();
            },
            refreshBundleSplit:function(forward){
                var self = this;
                ajax.post('/tetris/bvc/business/jv230/forward/change/forward/by/bundle/id/and/serial/num', {
                    bundleId:forward.bundleId,
                    serialNum:forward.serialNum
                }, function(data){
                    if(data){
                        for(var i=0; i<self.layout.length; i++){
                            var breakOuter = false;
                            for(var j=0; j<self.layout[i].length; j++){
                                var td = self.layout[i][j];
                                if(td.serialNum === forward.serialNum){
                                    td.data = data;
                                    breakOuter = true;
                                    break;
                                }
                            }
                            if(breakOuter) break;
                        }
                    }
                });
            },
            deleteBundleSplit:function(td){
                var self = this;
                var forward = td.data;
                ajax.post('/tetris/bvc/business/jv230/forward/delete/forward/by/bundle/id/and/serial/num', {
                    bundleId:forward.bundleId,
                    serialNum:forward.serialNum
                }, function(){
                    td.data = '';
                });
            },
            stopForward:function(){
                var self = this;
                if(!self.bundles.current){
                    self.qt.error('您没有选择上屏设备');
                    return;
                }
                self.qt.confirm('提示', '是否停止当前设备上屏', '确定', function(){
                    ajax.post('/tetris/bvc/business/jv230/forward/delete/forward/by/bundle/id', {
                        bundleId:self.bundles.current.id
                    }, function(data, status, message){
                        if(status === 200){
                            self.loadForwardBundles();
                            self.bundles.current = '';
                        }else{
                            self.qt.error(message);
                        }
                    }, null, ajax.TOTAL_CATCH_CODE);
                });
            },
            currentBundleChange:function(data){
                var self = this;
                for(var i=0; i<self.layout.length; i++){
                    for(var j=0; j<self.layout[i].length; j++){
                        self.layout[i][j].data = '';
                    }
                }
                var param = $.parseJSON(data.param);
                var url = '/tetris/bvc/business/jv230/forward/query/jv230/forwards';
                if(param.bundleType !== 'jv230'){
                    url = '/tetris/bvc/business/jv230/forward/query/combine/video';
                }
                ajax.post(url, {
                    bundleId:self.bundles.current.id
                }, function(data){
                    if(data && data.length>0){
                        for(var k=0; k<data.length; k++){
                            var forward = data[k];
                            for(var i=0; i<self.layout.length; i++){
                                var breakOuter = false;
                                for(var j=0; j<self.layout[i].length; j++){
                                    var td = self.layout[i][j];
                                    if(td.serialNum === forward.serialNum){
                                        td.data = forward;
                                        breakOuter = true;
                                        break;
                                    }
                                }
                                if(breakOuter) break;
                            }
                        }
                    }
                });
            },
            loadForwardBundles:function(){
                var self = this;
                self.bundles.data.splice(0, self.bundles.data.length);
                ajax.post('/tetris/bvc/business/jv230/forward/query/forward/bundles', null, function(data){
                    if(data && data.length>0){
                        for(var i=0; i<data.length; i++){
                            self.bundles.data.push(data[i]);
                        }
                    }
                });
            }
        },
        mounted: function () {
            var self = this;
            self.qt = new QtContext('zkFooterJv230Forward', function () {
                var params = self.qt.getWindowParams();
                self.layout = self.splits[params.layout+''];
                self.layoutStyle = 'layout_' + params.layout;
                //初始化ajax
                ajax.init({
                    login: config.ajax.login,
                    authname: config.ajax.authname,
                    debug: config.ajax.debug,
                    messenger: {
                        info: function (message) {
                            self.qt.info(message)
                        },
                        success: function (message, status) {
                            self.qt.success(message)
                        },
                        warning: function (message, status) {
                            self.qt.warning(message)
                        },
                        error: function (message, status) {
                            self.qt.error(message)
                        }
                    }
                });
                self.loadForwardBundles();
            });
        }
    });

    return Vue;
});