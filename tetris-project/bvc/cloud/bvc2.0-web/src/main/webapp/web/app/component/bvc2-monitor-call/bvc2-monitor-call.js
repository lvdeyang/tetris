define([
    'text!' + window.APPPATH + 'component/bvc2-monitor-call/bvc2-monitor-call.html',
    'context',
    'restfull',
    'jquery',
    'vue',
    'element-ui',
    'native-record-player',
    'date',
    'css!' + window.APPPATH + 'component/bvc2-monitor-call/bvc2-monitor-call.css'
], function(tpl, context, ajax, $, Vue){

	//组件名称
    var pluginName = 'bvc2-monitor-call';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data:function(){
            return {
                visible:false,
                id:'',
                message:'',
                interval:'',
                table:{
                    data:[],
                    page:{
                        total:0,
                        currentPage:0,
                        pageSize:2000000
                    }
                },
                tree:{
                    data:[],
                    props:{
                        children:'children',
                        label:'name'
                    }
                },
                sendCall:{
                    loading:false,
                    message:'',
                    timeout:'',
                    currentRow:''
                },
                loading:false,
                $embed:'',
                $embedHidden:'',
                embedEnable:''
            }
        },
        computed:{

        },
        watch:{
            visible:function(v){
                var self = this;
                if(v && self.table.data.length<=0){
                    self.loadUser(1);
                }
            }
        },
        methods:{
            loadCallTask:function(){
                var self = this;
                ajax.post('/monitor/user/call/load', null, function(data, status){
                    if(status !== 200) return;
                   if(data){
                       self.id = data.id;
                       self.message = '与'+data.targetUsername+'通话中';
                       //清理sendCall
                       self.sendCall.loading = false;
                       self.sendCall.message = '';
                       self.sendCall.currentRow = '';
                       if(self.sendCall.timeout){
                           clearTimeout(self.sendCall.timeout);
                           self.sendCall.timeout = '';
                       }
                       if(self.visible){
                           self.showEmbed();
                       }
                   }else{
                       self.id = '';
                       self.message = '';
                       if(self.visible){
                           self.hideEmbed();
                       }
                   }
                }, null, [403, 404, 408, 409, 500]);
            },
            loadUser:function(currentPage){
                var self = this;
                self.tree.data.splice(0, self.tree.data.length);
                /*self.table.data.splice(0, self.table.data.length);
                ajax.post('/monitor/user/call/load/external/users', {
                    currentPage:self.table.page.currentPage,
                    pageSize:self.table.page.pageSize
                }, function(data){
                    var total = data.total;
                    var rows = data.rows;
                    if(rows && rows.length>0){
                        for(var i=0; i<rows.length; i++){
                            self.table.data.push(rows[i]);
                        }
                        self.table.page.total = total;
                        self.table.page.currentPage = currentPage;
                    }
                });*/
                ajax.post('/monitor/device/load/callable/users', null, function(data){
//                ajax.post('/command/basic/save', {userIdList:"[2259]"}, function(data){
                    if(data && data.length>0){
                        for(var i=0; i<data.length; i++){
                            self.tree.data.push(data[i]);
                        }
                        var app = context.getProp('app');
                        app.addDeviceLoop('monitor-call-tree', data);
                    }
                });
            },
            refreshUsers:function(){
                var self = this;
                self.loadUser(self.table.page.currentPage);
            },
            handleCurrentChange:function(currentPage){
                var self = this;
                self.loadUser(currentPage);
            },
            startCall:function(data, node){
                var self = this;
                var params = $.parseJSON(data.param);
                ajax.post('/monitor/user/call/start', {
                    targetUser:params.userno
                }, function(data){

                    self.id = data.id;
                    self.message = '与'+data.targetUsername+'通话中';
                    //清理sendCall
                    self.sendCall.loading = false;
                    self.sendCall.message = '';
                    self.sendCall.currentRow = '';
                    if(self.sendCall.timeout){
                        clearTimeout(self.sendCall.timeout);
                        self.sendCall.timeout = '';
                    }
                    if(self.visible){
                        self.showEmbed();
                    }

                    /*self.sendCall.loading = true;
                    self.sendCall.currentRow = row;
                    self.sendCall.message = '等待' + row.name + '响应';
                    self.sendCall.timeout = setTimeout(function(){
                        self.sendCall.loading = false;
                        self.sendCall.timeout = '';
                    }, 120000);*/
                });
            },
            endCall:function(){
                var self = this;
                if(self.id){
                    var callId = self.id;
                    //停具体挂断
                    ajax.post('/monitor/user/call/stop/exist/' + callId, null, function(){
                        self.sendCall.loading = false;
                        self.sendCall.message = '';
                        self.sendCall.currentRow = '';
                        if(self.sendCall.timeout){
                            clearTimeout(self.sendCall.timeout);
                            self.sendCall.timeout = '';
                        }
                        self.id = '';
                        self.message = '';
                        if(self.visible){
                            self.hideEmbed();
                        }
                    });
                }else{
                    /*//停主动发起的单还未显示接听成功的请求
                    ajax.post('/monitor/user/call/top', {
                        targetUser:self.sendCall.currentRow.username
                    }, function(){
                        self.sendCall.loading = false;
                        self.sendCall.message = '';
                        self.sendCall.currentRow = '';
                        if(self.sendCall.timeout){
                            clearTimeout(self.sendCall.timeout);
                            self.sendCall.timeout = '';
                        }
                    });*/
                }
            },
            handleShow:function(){
                var self = this;
                self.visible = !self.visible;
                if(self.id){
                    self.showEmbed();
                }
            },
            handleClose:function(){
                var self = this;
                self.hideEmbed();
                self.visible = !self.visible;
            },
            showEmbed:function(){
                var self = this;
                if(!self.embedEnable) return;
                self.$nextTick(function(){
                    var $embedShow = $(self.$el).find('.embed-container');
                    if(!$embedShow.find('embed')[0]){
                        $embedShow.append(self.$embed);
                    }
                    try{
                        //最小化也能听声
                        //self.$embed[0].I_SetSilence(false);
                    }catch(e){console.log(e);}
                });
            },
            hideEmbed:function(){
                var self = this;
                if(!self.embedEnable) return;
                try{
                    self.$embedHidden.append(self.$embed);
                    //self.$embed[0].I_SetSilence(true);
                }catch(e){console.log(e);}
            }
        },
        created:function(){
            var self = this;
            ajax.post('/monitor/device/find/user/binding/player', null, function(data){
                self.$embed = $('<embed width="100%" height="100%" style="z-index:0" type="application/media-suma-lightlive"/>');
                self.$embedHidden = $('<div style="width:0; height:0; position:fixed; top:50%; right:-1px"></div>');
                $('body').append(self.$embedHidden);
                self.$embedHidden.append(self.$embed);
                if(data){
                    self.embedEnable = true;
                    self.$embed.data('code', data.code)
                                .data('ip', data.ip)
                                .data('username', data.username)
                                .data('password', data.password)
                                .data('bundleId', data.bundleId)
                                .data('bundleType', data.bundleType)
                                .data('bundleName', data.bundleName)
                                .data('layerId', data.layerId)
                                .data('registerLayerIp', data.registerLayerIp)
                                .data('registerLayerPort', data.registerLayerPort)
                                .data('videoChannelId', data.videoChannelId)
                                .data('videoBaseType', data.videoBaseType)
                                .data('audioChannelId', data.audioChannelId)
                                .data('audioBaseType', data.audioBaseType);

                    /*self.$embed.zk_SipPlayer('init', {
                        id:17,
                        playerCode:self.$embed.data('code'),
                        username:self.$embed.data('username'),
                        password:self.$embed.data('password'),
                        localSipIp:self.$embed.data('ip'),
                        localSipPort:5077,
                        registerServerSipIp:self.$embed.data('registerLayerIp'),
                        registerServerSipPort:self.$embed.data('registerLayerPort'),
                        type:'web',
                        selectDevice:'',
                        videoParamIdx:0,
                        videoBitrate:0,
                        audioParamIdx:0,
                        audioBitrate:0
                    });
                    self.$embed.zk_SipPlayer('register');*/
                    setTimeout(function(){
                        var app = context.getProp('app');
                        app.registerPlayer([{
                            code:self.$embed.data('code'),
                            username:self.$embed.data('username'),
                            password:self.$embed.data('password'),
                            $embed:self.$embed
                        }]);
                        try{
                            self.$embed[0].I_SetSilence(true);
                        }catch(e){console.log(e);}
                    }, 200);
                }else{
                    self.embedEnable = false;
                }
                self.interval = setInterval(self.loadCallTask, 6000);
            });
        }
    });

    return Vue;
});