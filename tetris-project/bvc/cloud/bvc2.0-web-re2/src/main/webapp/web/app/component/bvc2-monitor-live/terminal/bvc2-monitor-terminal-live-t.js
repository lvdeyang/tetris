define([
    'text!' + window.APPPATH + 'component/bvc2-monitor-live/terminal/bvc2-monitor-terminal-live.html',
    'context',
    'restfull',
    'jquery',
    'vue',
    'element-ui',
    'native-record-player',
    'player',
    'player-panel',
    'jquery.layout.auto',
    'date',
    'bvc2-monitor-ptzctrl',
    'css!' + window.APPPATH + 'component/bvc2-monitor-live/terminal/bvc2-monitor-terminal-live.css',
    'css!' + window.APPPATH + 'component/jQuery/player/css/Tetris.player.css',
    'css!' + window.APPPATH + 'component/jQuery/playerPanel/css/Tetris.playerPanel.css',
    'css!' + window.APPPATH + 'component/jQuery/jQuery.layout.auto/css/jQuery.layout.auto.css',
    'css!' + window.APPPATH + 'component/bvc2-layout-buttons/bvc2-layout-buttons.css'
], function(tpl, context, ajax, $, Vue){

	//组件名称
    var pluginName = 'bvc2-monitor-terminal-live-t';

    var app = context.getProp('app');

    var $webPlayers = [];
    
    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data:function(){
            return {
                tree:{
                    data:[],
                    props:{
                        children:'children',
                        label:'name'
                    }
                },
                buttons:[
                    {
                        id:1,
                        name:'1分屏',
                        cell:{
                            column:1,
                            row:1
                        },
                        editable:false,
                        theme:'gray',
                        layout:[
                            [1, 1]
                        ],
                        active:false
                    },{
                        id:4,
                        name:'4分屏',
                        cell:{
                            column:2,
                            row:2
                        },
                        editable:false,
                        theme:'gray',
                        layout:[
                            [0.5, 0.5],[0.5, 0.5],
                            [0.5, 0.5],[0.5, 0.5]
                        ],
                        active:true
                    }
                ],
                dialog:{
                    selectChannel:{
                        visible:false,
                        //播放器
                        currentPlayer:'',
                        serialNum:'',
                        //目的
                        dstVideoBundleId:'',
                        dstVideoBundleName:'',
                        dstVideoBundleType:'',
                        dstVideoLayerId:'',
                        dstVideoChannelId:'',
                        dstVideoBaseType:'',
                        dstAudioBundleId:'',
                        dstAudioBundleName:'',
                        dstAudioBundleType:'',
                        dstAudioLayerId:'',
                        dstAudioChannelId:'',
                        dstAudioBaseType:'',
                        //源
                        encodeVideos:[],
                        encodeAudios:[],
                        bundleId:'',
                        bundleName:'',
                        bundleType:'',
                        layerId:'',
                        selectedVideoChannelId:'',
                        selectedVideoBaseType:'',
                        selectedVideoChannelName:'',
                        selectedAudioChannelId:'',
                        selectedAudioBaseType:'',
                        selectedAudioChannelName:'',
                        loading:false
                    },
                    selectDstChannel:{
                        visible:false,
                        osdId:'',
                        type:'',
                        userId:'',
                        creater:'',
                        realType:'',
                        videoBundleId:'',
                        videoBundleName:'',
                        videoBundleType:'',
                        videoLayerId:'',
                        videoChannelId:'',
                        videoBaseType:'',
                        audioBundleId:'',
                        audioBundleName:'',
                        audioBundleType:'',
                        audioLayerId:'',
                        audioChannelId:'',
                        audioBaseType:'',
                        tree:{
                            data:[],
                            props:{
                                children:'children',
                                label:'name'
                            },
                            currentVideo:'',
                            currentAudio:''
                        },
                        loading:false
                    },
                    addRecord:{
                        visible:false,
                        mode:'MANUAL',
                        fileName:'',
                        timeScope:'',
                        type:'',
                        userId:'',
                        username:'',
                        userno:'',
                        creater:'',
                        realType:'',
                        videoBundleId:'',
                        videoBundleName:'',
                        videoBundleType:'',
                        videoLayerId:'',
                        videoChannelId:'',
                        videoBaseType:'',
                        audioBundleId:'',
                        audioBundleName:'',
                        audioBundleType:'',
                        audioLayerId:'',
                        audioChannelId:'',
                        audioBaseType:'',
                        loading:false,
                        $button:''
                    },
                    decodeBind:{
                        visible:false,
                        serial:'',
                        tree:{
                            data:[],
                            props:{
                                children:'children',
                                label:'name'
                            },
                            currentVideo:'',
                            currentAudio:''
                        },
                        table:{
                            data:[]
                        },
                        loading:false
                    }
                }
            }
        },
        computed:{
            dialog_selectChannel_selectedVideoChannelId:function(){
                var self = this;
                return self.dialog.selectChannel.selectedVideoChannelId;
            },
            dialog_selectChannel_selectedAudioChannelId:function(){
                var self = this;
                return self.dialog.selectChannel.selectedAudioChannelId;
            }
        },
        watch:{
            dialog_selectChannel_selectedVideoChannelId:function(channelId){
                var self = this;
                var encodeVideos = self.dialog.selectChannel.encodeVideos;
                for(var i=0; i<encodeVideos.length; i++){
                    if(encodeVideos[i].channelId == channelId){
                        self.dialog.selectChannel.selectedVideoChannelName = encodeVideos[i].name;
                        self.dialog.selectChannel.selectedVideoBaseType = encodeVideos[i].baseType;
                        return;
                    }
                }
            },
            dialog_selectChannel_selectedAudioChannelId:function(channelId){
                var self = this;
                var encodeAudios = self.dialog.selectChannel.encodeAudios;
                for(var i=0; i<encodeAudios.length; i++){
                    if(encodeAudios[i].channelId == channelId){
                        self.dialog.selectChannel.selectedAudioChannelName = encodeAudios[i].name;
                        self.dialog.selectChannel.selectedAudioBaseType = encodeAudios[i].baseType;
                        return;
                    }
                }
            }
        },
        methods:{
            destroy:function(fn){
                //销毁播放器
                $('embed').each(function(){
                    var $plugin = $(this);
                    if($plugin.data('inited') === true){
                        app.unRegisterPlayer([{
                            code:$plugin.data('code'),
                            $embed:$plugin
                        }]);
                        //$plugin.zk_SipPlayer('destroy');
                    }
                });
                ajax.post('/monitor/live/remove/all/webplayer/live', null, fn);
                app.removeDeviceLoop('terminal-live-encode-tree');
            },
            layoutChange:function(button){
                var self = this;
                console.log(button.active);
                if(button.active) return;
                for(var i=0; i<self.buttons.length; i++){
                    if(self.buttons[i]!==button && self.buttons[i].active){
                        self.buttons[i].active = false;
                    }
                }
                self.destroy(function(){
                    var doRegister = function(){
                        var $players = [];
                        for(var i=0; i<button.layout.length; i++){
                            var data = $webPlayers;
                            var $player = $('<div id="sip-player-'+(i+1)+'" style="width:100%; height:100%; position:absolute;">' +
                                '<embed width="100%" height="100%" style="z-index:0" type="application/media-suma-lightlive"/>' +
                                '</div>');
                            $player.find('embed')
                                .data('code', data[i].code)
                                .data('ip', data[i].ip)
                                .data('username', data[i].username)
                                .data('password', data[i].password)
                                .data('bundleId', data[i].bundleId)
                                .data('bundleType', data[i].bundleType)
                                .data('bundleName', data[i].bundleName)
                                .data('layerId', data[i].layerId)
                                .data('registerLayerIp', data[i].registerLayerIp)
                                .data('registerLayerPort', data[i].registerLayerPort)
                                .data('videoChannelId', data[i].videoChannelId)
                                .data('videoBaseType', data[i].videoBaseType)
                                .data('audioChannelId', data[i].audioChannelId)
                                .data('audioBaseType', data[i].audioBaseType);
                            $players.push($player);
                        }
                        $('#player-container')['Tetris.playerPanel']('setLayout', button);
                        $('#player-container')['Tetris.playerPanel']('setEmbed', $players);
                        //初始化播放器
                        for(var i=0; i<$players.length; i++){
                            var $player = $players[i];
                            var $plugin = $player.find('embed');
                            app.registerPlayer([{
                                code:$plugin.data('code'),
                                username:$plugin.data('username'),
                                password:$plugin.data('password'),
                                $embed:$plugin
                            }]);
                            /*var useablePort = $plugin.zk_SipPlayer('queryUseablePort', $plugin.data('ip'));
                             if(useablePort === false){
                             console.log('当前浏览器不支持sip播放器插件！')
                             break;
                             }
                             $plugin.zk_SipPlayer('init', {
                             id:i+1,
                             playerCode:$plugin.data('code'),
                             username:$plugin.data('username'),
                             password:$plugin.data('password'),
                             localSipIp:$plugin.data('ip'),
                             localSipPort:5060+i,
                             registerServerSipIp:$plugin.data('registerLayerIp'),
                             registerServerSipPort:$plugin.data('registerLayerPort'),
                             type:'web',
                             selectDevice:'',
                             videoParamIdx:0,
                             videoBitrate:0,
                             audioParamIdx:0,
                             audioBitrate:0
                             });
                             $plugin.zk_SipPlayer('register');*/
                            $plugin.data('inited', true);
                        }
                        button.active = true;
                    };
                    if($webPlayers.length>0){
                        doRegister();
                    }else{
                        //解决页面初始化点太快的问题
                        setTimeout(doRegister, 100);
                    }
                });
            },
            handleDragStart:function(data, e){
                if(data.type === 'BUNDLE'){
                    e.dataTransfer.setData('device', data.param);
                }else if(data.type === 'USER'){
                    e.dataTransfer.setData('user', data.param);
                }
            },
            handleLiveCommit:function(
                $player,
                serialNum,
                realType,
                videoBundleId,
                videoBundleName,
                videoBundleType,
                videoLayerId,
                videoChannelId,
                videoBaseType,
                videoChannelName,
                audioBundleId,
                audioBundleName,
                audioBundleType,
                audioLayerId,
                audioChannelId,
                audioBaseType,
                audioChannelName,
                dstVideoBundleId,
                dstVideoBundleName,
                dstVideoBundleType,
                dstVideoLayerId,
                dstVideoChannelId,
                dstVideoBaseType,
                dstAudioBundleId,
                dstAudioBundleName,
                dstAudioBundleType,
                dstAudioLayerId,
                dstAudioChannelId,
                dstAudioBaseType,
                error,
                success){

                var self = this;

                var data = {
                    serialNum:serialNum,
                    volume:'100%',
                    format: {
                        bodyUp: '视频：' + videoBundleName + ' - ' + videoChannelName,
                        bodyDown:''
                    },
                    type:'device',
                    realType:realType,
                    videoBundleId:videoBundleId,
                    videoBundleName:videoBundleName,
                    videoBundleType:videoBundleType,
                    videoLayerId:videoLayerId,
                    videoChannelId:videoChannelId,
                    videoBaseType:videoBaseType
                };
                if(audioBundleId){
                    data.format.bodyDown = '音频：' + videoBundleName + ' - ' + audioChannelName;
                    data.audioBundleId = audioBundleId;
                    data.audioBundleName = audioBundleName;
                    data.audioBundleType = audioBundleType;
                    data.audioLayerId = audioLayerId;
                    data.audioChannelId = audioChannelId;
                    data.audioBaseType = audioBaseType;
                }

                $player['Tetris.player']('set', data);

                if(typeof success === 'function') success();
            },
            handleSelectChannelClose:function(){
                var self = this;
                self.dialog.selectChannel.currentPlayer = '';
                self.dialog.selectChannel.serialNum = '';
                self.dialog.selectChannel.dstVideoBundleId = '';
                self.dialog.selectChannel.dstVideoBundleName = '';
                self.dialog.selectChannel.dstVideoBundleType = '';
                self.dialog.selectChannel.dstVideoLayerId = '';
                self.dialog.selectChannel.dstVideoChannelId = '';
                self.dialog.selectChannel.dstVideoBaseType = '';
                self.dialog.selectChannel.dstAudioBundleId = '';
                self.dialog.selectChannel.dstAudioBundleName = '';
                self.dialog.selectChannel.dstAudioBundleType = '';
                self.dialog.selectChannel.dstAudioLayerId = '';
                self.dialog.selectChannel.dstAudioChannelId = '';
                self.dialog.selectChannel.dstAudioBaseType = '';
                self.dialog.selectChannel.encodeVideos.splice(0, self.dialog.selectChannel.encodeVideos.length);
                self.dialog.selectChannel.encodeAudios.splice(0, self.dialog.selectChannel.encodeAudios.length);
                self.dialog.selectChannel.bundleId = '';
                self.dialog.selectChannel.bundleName = '';
                self.dialog.selectChannel.bundleType = '';
                self.dialog.selectChannel.layerId = '';
                self.dialog.selectChannel.selectedVideoChannelId = '';
                self.dialog.selectChannel.selectedVideoBaseType = '';
                self.dialog.selectChannel.selectedVideoChannelName = '';
                self.dialog.selectChannel.selectedAudioChannelId = '';
                self.dialog.selectChannel.selectedAudioBaseType = '';
                self.dialog.selectChannel.selectedAudioChannelName = '';
                self.dialog.selectChannel.loading = false;
                self.dialog.selectChannel.visible = false;
                self.dialog.selectChannel.$player = '';
            },
            handleSelectChannelCommit:function(){
                var self = this;
                var $player = self.dialog.selectChannel.currentPlayer;
                var serialNum = self.dialog.selectChannel.serialNum;
                var dstVideoBundleId = self.dialog.selectChannel.dstVideoBundleId;
                var dstVideoBundleName = self.dialog.selectChannel.dstVideoBundleName;
                var dstVideoBundleType = self.dialog.selectChannel.dstVideoBundleType;
                var dstVideoLayerId = self.dialog.selectChannel.dstVideoLayerId;
                var dstVideoChannelId = self.dialog.selectChannel.dstVideoChannelId;
                var dstVideoBaseType = self.dialog.selectChannel.dstVideoBaseType;
                var dstAudioBundleId = self.dialog.selectChannel.dstAudioBundleId;
                var dstAudioBundleName = self.dialog.selectChannel.dstAudioBundleName;
                var dstAudioBundleType = self.dialog.selectChannel.dstAudioBundleType;
                var dstAudioLayerId = self.dialog.selectChannel.dstAudioLayerId;
                var dstAudioChannelId = self.dialog.selectChannel.dstAudioChannelId;
                var dstAudioBaseType = self.dialog.selectChannel.dstAudioBaseType;

                var realType = self.dialog.selectChannel.realType;
                var videoBundleId = self.dialog.selectChannel.bundleId;
                var videoBundleName = self.dialog.selectChannel.bundleName;
                var videoBundleType = self.dialog.selectChannel.bundleType;
                var videoLayerId = self.dialog.selectChannel.layerId;
                var videoChannelId = self.dialog.selectChannel.selectedVideoChannelId;
                var videoBaseType = self.dialog.selectChannel.selectedVideoBaseType;
                var videoChannelName = self.dialog.selectChannel.selectedVideoChannelName;

                var audioBundleId = self.dialog.selectChannel.bundleId;
                var audioBundleName = self.dialog.selectChannel.bundleName;
                var audioBundleType = self.dialog.selectChannel.bundleType;
                var audioLayerId = self.dialog.selectChannel.layerId;
                var audioChannelId = self.dialog.selectChannel.selectedAudioChannelId;
                var audioBaseType = self.dialog.selectChannel.selectedAudioBaseType;
                var audioChannelName = self.dialog.selectChannel.selectedAudioChannelName;

                self.dialog.selectChannel.loading = true;
                self.handleLiveCommit(
                    $player,
                    serialNum,
                    realType,
                    videoBundleId,
                    videoBundleName,
                    videoBundleType,
                    videoLayerId,
                    videoChannelId,
                    videoBaseType,
                    videoChannelName,
                    audioBundleId,
                    audioBundleName,
                    audioBundleType,
                    audioLayerId,
                    audioChannelId,
                    audioBaseType,
                    audioChannelName,
                    dstVideoBundleId,
                    dstVideoBundleName,
                    dstVideoBundleType,
                    dstVideoLayerId,
                    dstVideoChannelId,
                    dstVideoBaseType,
                    dstAudioBundleId,
                    dstAudioBundleName,
                    dstAudioBundleType,
                    dstAudioLayerId,
                    dstAudioChannelId,
                    dstAudioBaseType, function(){
                    self.dialog.selectChannel.loading = false;
                }, function(){
                    self.dialog.selectChannel.loading = false;
                    self.handleSelectChannelClose();
                });
                $player['Tetris.player']('play', false);
            },
            handleSelectDstChannelClose:function(){
                var self = this;
                self.dialog.selectDstChannel.tree.data.splice(0, self.dialog.selectDstChannel.tree.data.length);
                self.dialog.selectDstChannel.tree.currentVideo = '';
                self.dialog.selectDstChannel.tree.currentAudio = '';
                self.dialog.selectDstChannel.osdId = '';
                self.dialog.selectDstChannel.type = '';
                self.dialog.selectDstChannel.userId = '';
                self.dialog.selectDstChannel.creater = '';
                self.dialog.selectDstChannel.realType = '';
                self.dialog.selectDstChannel.videoBundleId = '';
                self.dialog.selectDstChannel.videoBundleName = '';
                self.dialog.selectDstChannel.videoBundleType = '';
                self.dialog.selectDstChannel.videoLayerId = '';
                self.dialog.selectDstChannel.videoChannelId = '';
                self.dialog.selectDstChannel.videoBaseType = '';
                self.dialog.selectDstChannel.audioBundleId = '';
                self.dialog.selectDstChannel.audioBundleName = '';
                self.dialog.selectDstChannel.audioBundleType = '';
                self.dialog.selectDstChannel.audioLayerId = '';
                self.dialog.selectDstChannel.audioChannelId = '';
                self.dialog.selectDstChannel.audioBaseType = '';
                self.dialog.selectDstChannel.loading = false;
                self.dialog.selectDstChannel.visible = false;
                app.removeDeviceLoop('terminal-live-decode-tree');
            },
            onSelectedOsd:function(osd, done, endLoading){
                var self = this;
                var $player = self.$refs.bvc2DialogSingleOsd.getBuffer();
                $player['Tetris.player']('setOsd', osd);
                done();
                endLoading();
            },
            handleSelectDstChannelCommit:function(){
                var self = this;
                var type = self.dialog.selectDstChannel.type;
                var osdId = self.dialog.selectDstChannel.osdId;
                var currentVideo = $.parseJSON(self.dialog.selectDstChannel.tree.currentVideo);
                var currentAudio = !self.dialog.selectDstChannel.tree.currentAudio?{}:$.parseJSON(self.dialog.selectDstChannel.tree.currentAudio);
                var dstVideoBundleId = currentVideo.bundleId;
                var dstVideoBundleName = currentVideo.bundleName;
                var dstVideoBundleType = currentVideo.bundleType;
                var dstVideoLayerId = currentVideo.nodeUid;
                var dstVideoChannelId = currentVideo.channelId;
                var dstVideoBaseType = currentVideo.channelType;
                var dstAudioBundleId = currentAudio.bundleId;
                var dstAudioBundleName = currentAudio.bundleName;
                var dstAudioBundleType = currentAudio.bundleType;
                var dstAudioLayerId = currentAudio.nodeUid;
                var dstAudioChannelId = currentAudio.channelId;
                var dstAudioBaseType = currentAudio.channelType;

                if(type === 'device'){

                    var realType = self.dialog.selectDstChannel.realType;
                    var videoBundleId = self.dialog.selectDstChannel.videoBundleId;
                    var videoBundleName = self.dialog.selectDstChannel.videoBundleName;
                    var videoBundleType = self.dialog.selectDstChannel.videoBundleType;
                    var videoLayerId = self.dialog.selectDstChannel.videoLayerId;
                    var videoChannelId = self.dialog.selectDstChannel.videoChannelId;
                    var videoBaseType = self.dialog.selectDstChannel.videoBaseType;
                    var audioBundleId = self.dialog.selectDstChannel.audioBundleId;
                    var audioBundleName = self.dialog.selectDstChannel.audioBundleName;
                    var audioBundleType = self.dialog.selectDstChannel.audioBundleType;
                    var audioLayerId = self.dialog.selectDstChannel.audioLayerId;
                    var audioChannelId = self.dialog.selectDstChannel.audioChannelId;
                    var audioBaseType = self.dialog.selectDstChannel.audioBaseType;

                    self.dialog.selectDstChannel.loading = true;
                    ajax.post('/monitor/live/add', {
                        osdId:osdId,
                        realType:realType,
                        videoBundleId:videoBundleId,
                        videoBundleName:videoBundleName,
                        videoBundleType:videoBundleType,
                        videoLayerId:videoLayerId,
                        videoChannelId:videoChannelId,
                        videoBaseType:videoBaseType,
                        audioBundleId:audioBundleId,
                        audioBundleName:audioBundleName,
                        audioBundleType:audioBundleType,
                        audioLayerId:audioLayerId,
                        audioChannelId:audioChannelId,
                        audioBaseType:audioBaseType,
                        dstVideoBundleId:dstVideoBundleId,
                        dstVideoBundleName:dstVideoBundleName,
                        dstVideoBundleType:dstVideoBundleType,
                        dstVideoLayerId:dstVideoLayerId,
                        dstVideoChannelId:dstVideoChannelId,
                        dstVideoBaseType:dstVideoBaseType,
                        dstAudioBundleId:dstAudioBundleId,
                        dstAudioBundleName:dstAudioBundleName,
                        dstAudioBundleType:dstAudioBundleType,
                        dstAudioLayerId:dstAudioLayerId,
                        dstAudioChannelId:dstAudioChannelId,
                        dstAudioBaseType:dstAudioBaseType,
                        type:'DEVICE'
                    }, function(data, status){
                        self.dialog.selectDstChannel.loading = false;
                        if(status !== 200) return;
                        self.$message({
                            type:'success',
                            message:'上屏成功！'
                        });
                        self.handleSelectDstChannelClose();
                    }, null, [403, 404, 408, 409, 500]);
                }else if(type === 'user'){
                    var creater = self.dialog.selectDstChannel.creater;
                    var userId = self.dialog.selectDstChannel.userId;
                    if(creater === 'ldap'){
                        self.dialog.selectDstChannel.loading = true;
                        ajax.post('/monitor/live/xt/user/on/demand', {
                            osdId:osdId,
                            xtUserId:userId,
                            dstVideoBundleId:dstVideoBundleId,
                            dstVideoBundleName:dstVideoBundleName,
                            dstVideoBundleType:dstVideoBundleType,
                            dstVideoLayerId:dstVideoLayerId,
                            dstVideoChannelId:dstVideoChannelId,
                            dstVideoBaseType:dstVideoBaseType,
                            dstAudioBundleId:dstAudioBundleId,
                            dstAudioBundleName:dstAudioBundleName,
                            dstAudioBundleType:dstAudioBundleType,
                            dstAudioLayerId:dstAudioLayerId,
                            dstAudioChannelId:dstAudioChannelId,
                            dstAudioBaseType:dstAudioBaseType,
                            type:'DEVICE'
                        }, function(data, status){
                            self.dialog.selectDstChannel.loading = false;
                            if(status !== 200) return;
                            self.$message({
                                type:'success',
                                message:'上屏成功！'
                            });
                            self.handleSelectDstChannelClose();
                        }, null, [403, 404, 408, 409, 500]);
                    }else{
                        self.dialog.selectDstChannel.loading = true;
                        ajax.post('/monitor/live/local/user/on/demand', {
                            osdId:osdId,
                            localUserId:userId,
                            dstVideoBundleId:dstVideoBundleId,
                            dstVideoBundleName:dstVideoBundleName,
                            dstVideoBundleType:dstVideoBundleType,
                            dstVideoLayerId:dstVideoLayerId,
                            dstVideoChannelId:dstVideoChannelId,
                            dstVideoBaseType:dstVideoBaseType,
                            dstAudioBundleId:dstAudioBundleId,
                            dstAudioBundleName:dstAudioBundleName,
                            dstAudioBundleType:dstAudioBundleType,
                            dstAudioLayerId:dstAudioLayerId,
                            dstAudioChannelId:dstAudioChannelId,
                            dstAudioBaseType:dstAudioBaseType,
                            type:'DEVICE'
                        }, function(data, status){
                            self.dialog.selectDstChannel.loading = false;
                            if(status !== 200) return;
                            self.$message({
                                type:'success',
                                message:'上屏成功！'
                            });
                            self.handleSelectDstChannelClose();
                        }, null, [403, 404, 408, 409, 500]);
                    }
                }
            },
            addRecordModeChange:function(){
                var self = this;
                self.dialog.addRecord.timeScope = '';
            },
            handleAddRecordClose:function(){
                var self = this;
                self.dialog.addRecord.visible = false;
                self.dialog.addRecord.mode = 'MANUAL';
                self.dialog.addRecord.fileName = '';
                self.dialog.addRecord.timeScope = '';
                self.dialog.addRecord.type = '';

                self.dialog.addRecord.userId = '';
                self.dialog.addRecord.username = '';
                self.dialog.addRecord.userno = '';
                self.dialog.addRecord.creater = '';

                self.dialog.addRecord.realType = '';
                self.dialog.addRecord.videoBundleId = '';
                self.dialog.addRecord.videoBundleName = '';
                self.dialog.addRecord.videoBundleType = '';
                self.dialog.addRecord.videoLayerId = '';
                self.dialog.addRecord.videoChannelId = '';
                self.dialog.addRecord.videoBaseType = '';
                self.dialog.addRecord.audioBundleId = '';
                self.dialog.addRecord.audioBundleName = '';
                self.dialog.addRecord.audioBundleType = '';
                self.dialog.addRecord.audioLayerId = '';
                self.dialog.addRecord.audioChannelId = '';
                self.dialog.addRecord.audioBaseType = '';

                self.dialog.addRecord.loading = false;

                self.dialog.addRecord.$button = '';
            },
            handleAddRecordCommit:function(){
                var self = this;
                if(!self.dialog.addRecord.fileName){
                    self.$message({
                        type:'warning',
                        message:'请输入文件名！'
                    });
                    return;
                }
                var task = {
                    mode:self.dialog.addRecord.mode,
                    fileName:self.dialog.addRecord.fileName
                };
                if(self.dialog.addRecord.timeScope && self.dialog.addRecord.timeScope.length>0){
                    var datetimePatten = 'yyyy-MM-dd HH:mm:ss';
                    task.startTime = self.dialog.addRecord.timeScope[0].format(datetimePatten);
                    task.endTime = self.dialog.addRecord.timeScope[1].format(datetimePatten);
                }

                if(self.dialog.addRecord.type === 'device'){
                    var realType = self.dialog.addRecord.realType;
                    if(realType === 'XT'){
                        task.bundleId = self.dialog.addRecord.videoBundleId;
                        self.dialog.addRecord.loading = true;
                        ajax.post('/monitor/record/add/xt/device', task, function(data, status){
                            self.dialog.addRecord.loading = false;
                            if(status !== 200) return;
                            self.$message({
                                type:'success',
                                message:'操作成功！'
                            });
                            var $record = self.dialog.addRecord.$button;
                            $record.find('span').first().append('<span class="record-stop-icon" style="color:red;position: absolute;bottom: -5px; right:0;font-size:16px; line-height:normal;">x</span>');
                            $record.addClass('active').attr('title', '停止录制').data('record-id', data.id);
                            self.handleAddRecordClose();
                        }, null, [403,404,409,500]);

                    }else{
                        task.videoBundleId = self.dialog.addRecord.videoBundleId;
                        task.videoBundleName = self.dialog.addRecord.videoBundleName;
                        task.videoBundleType = self.dialog.addRecord.videoBundleType;
                        task.videoLayerId = self.dialog.addRecord.videoLayerId;
                        task.videoChannelId = self.dialog.addRecord.videoChannelId;
                        task.videoBaseType = self.dialog.addRecord.videoBaseType;
                        task.videoChannelName = self.dialog.addRecord.videoChannelName;

                        task.audioBundleId = self.dialog.addRecord.audioBundleId;
                        task.audioBundleName = self.dialog.addRecord.audioBundleName;
                        task.audioBundleType = self.dialog.addRecord.audioBundleType;
                        task.audioLayerId = self.dialog.addRecord.audioLayerId;
                        task.audioChannelId = self.dialog.addRecord.audioChannelId;
                        task.audioBaseType = self.dialog.addRecord.audioBaseType;
                        task.audioChannelName = self.dialog.addRecord.audioChannelName;

                        self.dialog.addRecord.loading = true;
                        ajax.post('/monitor/record/add', task, function(data, status){
                            self.dialog.addRecord.loading = false;
                            if(status !== 200) return;
                            self.$message({
                                type:'success',
                                message:'操作成功！'
                            });
                            var $record = self.dialog.addRecord.$button;
                            $record.find('span').first().append('<span class="record-stop-icon" style="color:red;position: absolute;bottom: -5px; right:0;font-size:16px; line-height:normal;">x</span>');
                            $record.addClass('active').attr('title', '停止录制').data('record-id', data.id);
                            self.handleAddRecordClose();
                        }, null, [403,404,409,500]);
                    }
                }else if(self.dialog.addRecord.type === 'user'){
                    task.targetUserId = self.dialog.addRecord.userId;
                    self.dialog.addRecord.loading = true;
                    ajax.post('/monitor/record/add/record/user/task', task, function(data, status){
                        self.dialog.addRecord.loading = false;
                        if(status !== 200) return;
                        self.$message({
                            type:'success',
                            message:'操作成功！'
                        });
                        var $record = self.dialog.addRecord.$button;
                        $record.find('span').first().append('<span class="record-stop-icon" style="color:red;position: absolute;bottom: -5px; right:0;font-size:16px; line-height:normal;">x</span>');
                        $record.addClass('active').attr('title', '停止录制').data('record-id', data.id);
                        self.handleAddRecordClose();
                    }, null, [403,404,409,500]);
                }
            },
            handleDecodeBindClose:function(){
                var self = this;
                self.dialog.decodeBind.visible = false;
                self.dialog.decodeBind.serial = '';
                self.dialog.decodeBind.tree.data.splice(0, self.dialog.decodeBind.tree.data.length);
                self.dialog.decodeBind.tree.currentVideo = '';
                self.dialog.decodeBind.tree.currentAudio = '';
                self.dialog.decodeBind.table.data.splice(0, self.dialog.decodeBind.table.data.length);
                self.dialog.decodeBind.loading = false;
            },
            handleDecodeBindCommit:function(){
                var self = this;
                if(!self.dialog.decodeBind.tree.currentVideo){
                    self.$message({
                        type:'success',
                        message:'您还没有选择视频设备！'
                    })
                    return;
                }
                var currentVideo = $.parseJSON(self.dialog.decodeBind.tree.currentVideo);
                var params = {
                    serial:self.dialog.decodeBind.serial,
                    dstVideoBundleId:currentVideo.bundleId,
                    dstVideoBundleName:currentVideo.bundleName,
                    dstVideoBundleType:currentVideo.bundleType,
                    dstVideoLayerId:currentVideo.nodeUid,
                    dstVideoChannelId:currentVideo.channelId,
                    dstVideoBaseType:currentVideo.channelType,
                    dstVideoChannelName:currentVideo.channelName
                };
                if(self.dialog.decodeBind.tree.currentAudio){
                    var currentAudio = $.parseJSON(self.dialog.decodeBind.tree.currentAudio);
                    params.dstAudioBundleId = currentAudio.bundleId;
                    params.dstAudioBundleName = currentAudio.bundleName;
                    params.dstAudioBundleType = currentAudio.bundleType;
                    params.dstAudioLayerId = currentAudio.nodeUid;
                    params.dstAudioChannelId = currentAudio.channelId;
                    params.dstAudioBaseType = currentAudio.channelType;
                    params.dstAudioChannelName = currentAudio.channelName;
                }
                self.dialog.decodeBind.loading = true;
                ajax.post('/monitor/live/split/config/add', params, function(data){
                    self.dialog.decodeBind.table.data.push(data);
                    self.refreshDecodeBindTree();
                    self.dialog.decodeBind.loading = false;
                    self.dialog.decodeBind.tree.currentVideo = '';
                    self.dialog.decodeBind.tree.currentAudio = '';
                });
            },
            handleDecodeBindRemove:function(scope){
                var self = this;
                var row = scope.row;
                ajax.post('/monitor/live/split/config/remove', {id:row.id}, function(){
                    for(var i=0; i<self.dialog.decodeBind.table.data.length; i++){
                        if(self.dialog.decodeBind.table.data[i].id == row.id){
                            self.dialog.decodeBind.table.data.splice(i, 1);
                            break;
                        }
                    }
                    self.refreshDecodeBindTree();
                });
            },
            refreshDecodeBindTree:function(){
                var self = this;
                var serial = self.dialog.decodeBind.serial;
                ajax.post('/monitor/live/split/config/find/decode/institution/tree', {
                    serial:serial
                }, function(data){
                    self.dialog.decodeBind.tree.data.splice(0, self.dialog.decodeBind.tree.data.length);
                    for(var i=0; i<data.length; i++){
                        self.dialog.decodeBind.tree.data.push(data[i]);
                    }
                });
            },
            refreshDecodeBindTable:function(){
                var self = this;
                var serial = self.dialog.decodeBind.serial;
                ajax.post('/monitor/live/split/config/find/by/serial', {
                    serial:serial
                }, function(data){
                    self.dialog.decodeBind.table.data.splice(0, self.dialog.decodeBind.table.data.length);
                    if(data && data.length>0){
                        for(var i=0; i<data.length; i++){
                            self.dialog.decodeBind.table.data.push(data[i]);
                        }
                    }
                });
            }
        },
        created:function(){
            var self = this;
            self.tree.data.splice(0, self.tree.data.length);
            ajax.post('/monitor/device/find/institution/tree/2/false', null, function(data){
//                ajax.post('/command/user/info/get/user/info', null, function(data){
//            ajax.post('/command/query/find/institution/tree/bundle/2/false/0', null, function(data){
                if(data && data.length>0){
                    for(var i=0; i<data.length; i++){
                        self.tree.data.push(data[i]);
                    }
                    app.addDeviceLoop('terminal-live-encode-tree', data);
                }
            });
            var layout = null;
            for(var i=0; i<self.buttons.length; i++){
                if(self.buttons[i].active){
                    layout = self.buttons[i].layout;
                    break;
                }
            }
            if(window.SCOPE !== 'plugin'){
                self.buttons.push({
                    id:6,
                    name:'6分屏',
                    cell:{
                        column:3,
                        row:3
                    },
                    cellspan:[{
                        x:0,
                        y:0,
                        r:1,
                        b:1
                    }],
                    editable:false,
                    theme:'gray',
                    layout:[
                        [0.6599, 0.6599],         [0.34, 0.33],
                        [0.34, 0.33],
                        [0.33, 0.34],[0.33, 0.34],[0.34, 0.34]
                    ],
                    active:false
                });
                self.buttons.push({
                    id:9,
                    name:'9分屏',
                    cell:{
                        column:3,
                        row:3
                    },
                    editable:false,
                    theme:'gray',
                    layout:[
                        [0.33, 0.33],[0.33, 0.33],[0.34, 0.33],
                        [0.33, 0.33],[0.33, 0.33],[0.34, 0.33],
                        [0.33, 0.34],[0.33, 0.34],[0.34, 0.34]
                    ],
                    active:false
                });
                self.buttons.push({
                    id:16,
                    name:'16分屏',
                    cell:{
                        column:4,
                        row:4
                    },
                    editable:false,
                    theme:'gray',
                    layout:[
                        [0.25, 0.25],[0.25, 0.25],[0.25, 0.25],[0.25, 0.25],
                        [0.25, 0.25],[0.25, 0.25],[0.25, 0.25],[0.25, 0.25],
                        [0.25, 0.25],[0.25, 0.25],[0.25, 0.25],[0.25, 0.25],
                        [0.25, 0.25],[0.25, 0.25],[0.25, 0.25],[0.25, 0.25]
                    ],
                    active:false
                });
            }
            self.$nextTick(function(){
                $('#player-container')['Tetris.playerPanel']('create', {
                    split:{
                        layout:layout
                    },
                    player:{
                        event:{
                            onStop: function ($embed, srcList, complete) {
                                var $player = $(this);
                                var src = srcList[0];
                                if(src.type === 'device'){
                                    ajax.post('/monitor/live/stop/live/device/' + src.taskId, null, function(data, status){
                                        if(status !== 200) return;
                                        src.taskId = null;
                                        $player['Tetris.player']('set', src);
                                        complete();
                                    }, null, [403, 404, 408, 409, 500]);
                                }else if(src.type === 'user'){
                                    ajax.post('/monitor/live/stop/live/user/' + src.taskId, null, function(data, status){
                                        if(status !== 200) return;
                                        src.taskId = null;
                                        $player['Tetris.player']('set', src);
                                        complete();
                                    }, null, [403, 404, 408, 409, 500]);
                                    /*if(src.creater === 'ldap'){
                                        //停止点播xt用户
                                        ajax.post('/monitor/live/stop/xt/user/on/demand', {
                                            xtUserId:src.userId,
                                            dstVideoBundleId:$embed.data('bundleId'),
                                            dstVideoBundleName:$embed.data('bundleName'),
                                            dstVideoBundleType:$embed.data('bundleType'),
                                            dstVideoLayerId:$embed.data('layerId'),
                                            dstVideoChannelId:$embed.data('videoChannelId'),
                                            dstVideoBaseType:$embed.data('videoBaseType'),
                                            dstAudioBundleId:$embed.data('bundleId'),
                                            dstAudioBundleName:$embed.data('bundleName'),
                                            dstAudioBundleType:$embed.data('bundleType'),
                                            dstAudioLayerId:$embed.data('layerId'),
                                            dstAudioChannelId:$embed.data('audioChannelId'),
                                            dstAudioBaseType:$embed.data('audioBaseType')
                                        }, function(data, status){
                                            if(status !== 200) return;
                                            complete();
                                        }, null, [403, 404, 408, 409, 500]);
                                    }else{
                                        //停止点播本地用户
                                        ajax.post('/monitor/live/remove/' + src.taskId, null, function(data, status){
                                            if(status !== 200) return;
                                            src.taskId = null;
                                            $player['Tetris.player']('set', src);
                                            complete();
                                        }, null, [403, 404, 408, 409, 500]);
                                    }*/
                                }
                            },
                            onPlay: function ($embed, srcList, osd, complete) {
                                var $player = $(this);
                                var serial = $(this).attr('data_index');
                                var src = srcList[0];
                                if(src.type === 'device'){
                                    var realType = src.realType;
                                    var videoBundleId = src.videoBundleId;
                                    var videoBundleName = src.videoBundleName;
                                    var videoBunelType = src.videoBundleType;
                                    var videoLayerId = src.videoLayerId;
                                    var videoChannelId = src.videoChannelId;
                                    var videoBaseType = src.videoBaseType;
                                    var audioBundleId = src.audioBundleId;
                                    var audioBundleName = src.audioBundleName;
                                    var audioBundleType = src.audioBundleType;
                                    var audioLayerId = src.audioLayerId;
                                    var audioChannelId = src.audioChannelId;
                                    var audioBaseType = src.audioBaseType;
                                    var dstVideoBundleId = $embed.data('bundleId'),
                                        dstVideoBundleName = $embed.data('bundleName'),
                                        dstVideoBundleType = $embed.data('bundleType'),
                                        dstVideoLayerId = $embed.data('layerId'),
                                        dstVideoChannelId = $embed.data('videoChannelId'),
                                        dstVideoBaseType = $embed.data('videoBaseType'),
                                        dstAudioBundleId = $embed.data('bundleId'),
                                        dstAudioBundleName = $embed.data('bundleName'),
                                        dstAudioBundleType = $embed.data('bundleType'),
                                        dstAudioLayerId = $embed.data('layerId'),
                                        dstAudioChannelId = $embed.data('audioChannelId'),
                                        dstAudioBaseType = $embed.data('audioBaseType');
                                    //console.log("发送请求/monitor/live/add/transcord");
                                    ajax.post('/monitor/live/add/transcord', {
                                        osdId:osd?osd.id:null,
                                        realType:realType,
                                        videoBundleId:videoBundleId,
                                        videoBundleName:videoBundleName,
                                        videoBundleType:videoBunelType,
                                        videoLayerId:videoLayerId,
                                        videoChannelId:videoChannelId,
                                        videoBaseType:videoBaseType,
                                        audioBundleId:audioBundleId,
                                        audioBundleName:audioBundleName,
                                        audioBundleType:audioBundleType,
                                        audioLayerId:audioLayerId,
                                        audioChannelId:audioChannelId,
                                        audioBaseType:audioBaseType,
                                        dstVideoBundleId:dstVideoBundleId,
                                        dstVideoBundleName:dstVideoBundleName,
                                        dstVideoBundleType:dstVideoBundleType,
                                        dstVideoLayerId:dstVideoLayerId,
                                        dstVideoChannelId:dstVideoChannelId,
                                        dstVideoBaseType:dstVideoBaseType,
                                        dstAudioBundleId:dstAudioBundleId,
                                        dstAudioBundleName:dstAudioBundleName,
                                        dstAudioBundleType:dstAudioBundleType,
                                        dstAudioLayerId:dstAudioLayerId,
                                        dstAudioChannelId:dstAudioChannelId,
                                        dstAudioBaseType:dstAudioBaseType,
                                        type:'WEBSITE_PLAYER'
                                    }, function(data, status){
                                        //console.log("请求返回");
                                        if(status !== 200) return;
                                        src.taskId = data.id;
                                        $player['Tetris.player']('set', src);

                                        //var url = '<media type=\'stream\' url=\'udp://192.165.56.77:15001\' mode=\'smooth\' />'; console.log(url);
                                        var url = '<media type=\'stream\' url=\'' + data.udpUrl + '\' mode=\'smooth\' />'; console.log(url);
                                        $embed[0].I_Play(url);

                                        //在sip注册成功的情况下，可能播放rtp-es，在2秒后重新播放udp
                                        //setTimeout(function(){
                                        //    $embed[0].I_Play(url);
                                        //}, 2000);

                                        self.$message({
                                            type:'success',
                                            message:'开始播放'
                                        });
                                        complete();
                                        ajax.post('/monitor/live/add/decode/bind', {
                                            serial:serial,
                                            osdId:osd?osd.id:null,
                                            realType:realType,
                                            videoBundleId:videoBundleId,
                                            videoBundleName:videoBundleName,
                                            videoBundleType:videoBunelType,
                                            videoLayerId:videoLayerId,
                                            videoChannelId:videoChannelId,
                                            videoBaseType:videoBaseType,
                                            audioBundleId:audioBundleId,
                                            audioBundleName:audioBundleName,
                                            audioBundleType:audioBundleType,
                                            audioLayerId:audioLayerId,
                                            audioChannelId:audioChannelId,
                                            audioBaseType:audioBaseType
                                        });
                                    }, null, [403, 404, 408, 409, 500]);
                                }else if(src.type === 'user'){
                                    if(src.creater === 'ldap'){
                                        //点播xt用户
                                        ajax.post('/monitor/live/xt/user/on/demand', {
                                            osdId:osd?osd.id:null,
                                            xtUserId:src.userId,
                                            dstVideoBundleId:$embed.data('bundleId'),
                                            dstVideoBundleName:$embed.data('bundleName'),
                                            dstVideoBundleType:$embed.data('bundleType'),
                                            dstVideoLayerId:$embed.data('layerId'),
                                            dstVideoChannelId:$embed.data('videoChannelId'),
                                            dstVideoBaseType:$embed.data('videoBaseType'),
                                            dstAudioBundleId:$embed.data('bundleId'),
                                            dstAudioBundleName:$embed.data('bundleName'),
                                            dstAudioBundleType:$embed.data('bundleType'),
                                            dstAudioLayerId:$embed.data('layerId'),
                                            dstAudioChannelId:$embed.data('audioChannelId'),
                                            dstAudioBaseType:$embed.data('audioBaseType'),
                                            type:'WEBSITE_PLAYER'
                                        }, function(data, status){
                                            if(status !== 200) return;
                                            src.taskId = data.id;
                                            $player['Tetris.player']('set', src);
                                            self.$message({
                                                type:'success',
                                                message:'开始播放'
                                            });
                                            complete();
                                            ajax.post('/monitor/live/xt/user/on/demand/decode/bind', {
                                                serial:serial,
                                                osdId:osd?osd.id:null,
                                                xtUserId:src.userId
                                            });
                                        }, null, [403, 404, 408, 409, 500]);
                                    }else{
                                        //点播本地用户
                                        ajax.post('/monitor/live/local/user/on/demand', {
                                            osdId:osd?osd.id:null,
                                            localUserId:src.userId,
                                            dstVideoBundleId:$embed.data('bundleId'),
                                            dstVideoBundleName:$embed.data('bundleName'),
                                            dstVideoBundleType:$embed.data('bundleType'),
                                            dstVideoLayerId:$embed.data('layerId'),
                                            dstVideoChannelId:$embed.data('videoChannelId'),
                                            dstVideoBaseType:$embed.data('videoBaseType'),
                                            dstAudioBundleId:$embed.data('bundleId'),
                                            dstAudioBundleName:$embed.data('bundleName'),
                                            dstAudioBundleType:$embed.data('bundleType'),
                                            dstAudioLayerId:$embed.data('layerId'),
                                            dstAudioChannelId:$embed.data('audioChannelId'),
                                            dstAudioBaseType:$embed.data('audioBaseType'),
                                            type:'WEBSITE_PLAYER'
                                        }, function(data, status){
                                            if(status !== 200) return;
                                            src.taskId = data.id;
                                            $player['Tetris.player']('set', src);
                                            self.$message({
                                                type:'success',
                                                message:'开始播放'
                                            });
                                            complete();
                                            ajax.post('/monitor/live/local/user/on/demand/decode/bind', {
                                                serial:serial,
                                                osdId:osd?osd.id:null,
                                                localUserId:src.userId
                                            });
                                        }, null, [403, 404, 408, 409, 500]);
                                    }
                                }
                            },
                            onOsd:function(currentOsd){
                                var $player = $(this);
                                self.$refs.bvc2DialogSingleOsd.open(currentOsd?currentOsd.id:null);
                                self.$refs.bvc2DialogSingleOsd.setBuffer($player);
                            },
                            record:function(srcList){
                                var src = srcList[0];
                                var $player = $(this);
                                var $record = $player.find('.opration .record');
                                if($record.is('.active')){
                                    var recordId = $record.data('record-id');
                                    ajax.post('/monitor/record/stop/' + recordId, null, function(){
                                        self.$message({
                                            type:'success',
                                            message:'操作成功！'
                                        });
                                        $record.find('span').find('.record-stop-icon').remove();
                                        $record.removeClass('active').attr('title', '录制').data('record-id', null);
                                    });
                                }else{
                                    self.dialog.addRecord.type = src.type;
                                    if(src.type === 'device'){
                                        self.dialog.addRecord.realType = src.realType;
                                        self.dialog.addRecord.videoBundleId = src.videoBundleId;
                                        self.dialog.addRecord.videoBundleName = src.videoBundleName;
                                        self.dialog.addRecord.videoBundleType = src.videoBundleType;
                                        self.dialog.addRecord.videoLayerId = src.videoLayerId;
                                        self.dialog.addRecord.videoChannelId = src.videoChannelId;
                                        self.dialog.addRecord.videoBaseType = src.videoBaseType;
                                        self.dialog.addRecord.audioBundleId = src.audioBundleId;
                                        self.dialog.addRecord.audioBundleName = src.audioBundleName;
                                        self.dialog.addRecord.audioBundleType = src.audioBundleType;
                                        self.dialog.addRecord.audioLayerId = src.audioLayerId;
                                        self.dialog.addRecord.audioChannelId = src.audioChannelId;
                                        self.dialog.addRecord.audioBaseType = src.audioBaseType;
                                    }else if(src.type === 'user'){
                                        self.dialog.addRecord.userId = src.userId;
                                        self.dialog.addRecord.username = src.username;
                                        self.dialog.addRecord.userno = src.userno;
                                        self.dialog.addRecord.creater = src.creater;
                                    }
                                    self.dialog.addRecord.visible = true;
                                    self.dialog.addRecord.fileName = new Date().format("yyyy-MM-dd HH:mm");
                                    self.dialog.addRecord.$button = $record;
                                }
                            },
                            decodeBind:function(event){
                                var serial = $(this).attr('data_index');
                                self.dialog.decodeBind.serial = serial;
                                self.dialog.decodeBind.visible = true;
                                self.refreshDecodeBindTree();
                                self.refreshDecodeBindTable();
                            },
                            ptzctrl: function (srcList, $container, $embedWrapper, status) {
                                if(status !== 'play'){
                                    self.$message({
                                        type:'warning',
                                        message:'云台操作请先开始预览！'
                                    });
                                    return;
                                }
                                if(srcList[0].type === 'user'){
                                    ajax.post('/monitor/device/query/layer/id', {
                                        bundleId:srcList[0].encoderId
                                    }, function(data){
                                        var bundleInfo = {
                                            bundleId:srcList[0].encoderId,
                                            bundleName:srcList[0].username,
                                            layerId:data,
                                            $embedWrapper:$embedWrapper
                                        };
                                        self.$refs.bvc2MonitorPtzctrl.open(bundleInfo);
                                    });
                                }else{
                                    var bundleInfo = {
                                        bundleId:srcList[0].videoBundleId,
                                        bundleName:srcList[0].videoBundleName,
                                        layerId:srcList[0].videoLayerId,
                                        $embedWrapper:$embedWrapper
                                    };
                                    self.$refs.bvc2MonitorPtzctrl.open(bundleInfo);
                                }
                            },
                            forward:function(srcList, osd){
                                var $player = $(this);
                                var src = srcList[0];

                                ajax.post('/monitor/device/find/institution/tree/1/true', null, function(data){
                                    if(data && data.length>0){
                                        for(var i=0; i<data.length; i++){
                                            self.dialog.selectDstChannel.tree.data.push(data[i]);
                                        }
                                        app.addDeviceLoop('terminal-live-decode-tree', data);
                                        self.dialog.selectDstChannel.osdId = osd?osd.id:null;
                                        self.dialog.selectDstChannel.type = src.type;

                                        if(src.type === 'device'){
                                            self.dialog.selectDstChannel.realType = src.realType;
                                            self.dialog.selectDstChannel.videoBundleId = src.videoBundleId;
                                            self.dialog.selectDstChannel.videoBundleName = src.videoBundleName;
                                            self.dialog.selectDstChannel.videoBundleType = src.videoBundleType;
                                            self.dialog.selectDstChannel.videoLayerId = src.videoLayerId;
                                            self.dialog.selectDstChannel.videoChannelId = src.videoChannelId;
                                            self.dialog.selectDstChannel.videoBaseType = src.videoBaseType;
                                            self.dialog.selectDstChannel.audioBundleId = src.audioBundleId;
                                            self.dialog.selectDstChannel.audioBundleName = src.audioBundleName;
                                            self.dialog.selectDstChannel.audioBundleType = src.audioBundleType;
                                            self.dialog.selectDstChannel.audioLayerId = src.audioLayerId;
                                            self.dialog.selectDstChannel.audioChannelId = src.audioChannelId;
                                            self.dialog.selectDstChannel.audioBaseType = src.audioBaseType;
                                        }else if(src.type === 'user'){
                                            self.dialog.selectDstChannel.type = src.type;
                                            self.dialog.selectDstChannel.userId = src.userId;
                                            self.dialog.selectDstChannel.creater = src.creater;
                                        }

                                    }
                                    self.dialog.selectDstChannel.visible = true;
                                });

                            },
                            onDrop: function ($embed, serialNum, oldData, event) {
                                var $player = $(this);
                                var $record = $player.find('.opration .record');
                                var deviceJSON = event.dataTransfer.getData('device');
                                if(deviceJSON){
                                    var device = $.parseJSON(deviceJSON);
                                    var bundleId = device.bundleId;
                                    var bundleName = device.bundleName;
                                    var bundleType = device.venusBundleType;
                                    var layerId = device.nodeUid;
                                    var realType = device.realType;
                                    $embed.data('targetType', realType);
                                    ajax.post('/monitor/device/find/channels/' + bundleId + '/0', null, function(data){

                                        var dstVideoBundleId = $embed.data('bundleId'),
                                            dstVideoBundleName = $embed.data('bundleName'),
                                            dstVideoBundleType = $embed.data('bundleType'),
                                            dstVideoLayerId = $embed.data('layerId'),
                                            dstVideoChannelId = $embed.data('videoChannelId'),
                                            dstVideoBaseType = $embed.data('videoBaseType'),
                                            dstAudioBundleId = $embed.data('bundleId'),
                                            dstAudioBundleName = $embed.data('bundleName'),
                                            dstAudioBundleType = $embed.data('bundleType'),
                                            dstAudioLayerId = $embed.data('layerId'),
                                            dstAudioChannelId = $embed.data('audioChannelId'),
                                            dstAudioBaseType = $embed.data('audioBaseType');

                                        var encodeVideo = data.encodeVideo;
                                        var encodeAudio = data.encodeAudio;
                                        //没有视频编码通道就不能播
                                        if(!encodeVideo || encodeVideo.length<=0) return;
                                        //只有一个视频时（没有音频或者只有一个音频）则直接拨
                                        if(encodeVideo.length===1 &&
                                            (!encodeAudio || encodeAudio.length<=0 || encodeAudio.length===1)){
                                            if(encodeAudio && encodeAudio.length<=0){
                                                self.handleLiveCommit(
                                                    $player,
                                                    serialNum,
                                                    realType,
                                                    bundleId,
                                                    bundleName,
                                                    bundleType,
                                                    layerId,
                                                    encodeVideo[0].channelId,
                                                    encodeVideo[0].baseType,
                                                    encodeVideo[0].name,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    dstVideoBundleId,
                                                    dstVideoBundleName,
                                                    dstVideoBundleType,
                                                    dstVideoLayerId,
                                                    dstVideoChannelId,
                                                    dstVideoBaseType,
                                                    dstAudioBundleId,
                                                    dstAudioBundleName,
                                                    dstAudioBundleType,
                                                    dstAudioLayerId,
                                                    dstAudioChannelId,
                                                    dstAudioBaseType
                                                );
                                            }else{
                                                self.handleLiveCommit(
                                                    $player,
                                                    serialNum,
                                                    realType,
                                                    bundleId,
                                                    bundleName,
                                                    bundleType,
                                                    layerId,
                                                    encodeVideo[0].channelId,
                                                    encodeVideo[0].baseType,
                                                    encodeVideo[0].name,
                                                    bundleId,
                                                    bundleName,
                                                    bundleType,
                                                    layerId,
                                                    encodeAudio[0].channelId,
                                                    encodeAudio[0].baseType,
                                                    encodeAudio[0].name,
                                                    dstVideoBundleId,
                                                    dstVideoBundleName,
                                                    dstVideoBundleType,
                                                    dstVideoLayerId,
                                                    dstVideoChannelId,
                                                    dstVideoBaseType,
                                                    dstAudioBundleId,
                                                    dstAudioBundleName,
                                                    dstAudioBundleType,
                                                    dstAudioLayerId,
                                                    dstAudioChannelId,
                                                    dstAudioBaseType
                                                );
                                            }
                                            $player['Tetris.player']('play', false);
                                            return;
                                        }

                                        self.dialog.selectChannel.currentPlayer = $player;
                                        self.dialog.selectChannel.serialNum = serialNum;
                                        self.dialog.selectChannel.bundleId = bundleId;
                                        self.dialog.selectChannel.bundleName = bundleName;
                                        self.dialog.selectChannel.bundleType = bundleType;
                                        self.dialog.selectChannel.layerId = layerId;
                                        self.dialog.selectChannel.realType = realType;

                                        self.dialog.selectChannel.selectedVideoChannelId = encodeVideo[0].channelId;
                                        self.dialog.selectChannel.selectedVideoBaseType = encodeVideo[0].baseType;
                                        self.dialog.selectChannel.selectedVideoChannelName = encodeVideo[0].name;
                                        for(var i=0; i<encodeVideo.length; i++){
                                            self.dialog.selectChannel.encodeVideos.push(encodeVideo[i]);
                                        }
                                        if(encodeAudio && encodeAudio.length>0){
                                            self.dialog.selectChannel.selectedAudioChannelId = encodeAudio[0].channelId;
                                            self.dialog.selectChannel.selectedAudioBaseType = encodeAudio[0].baseType;
                                            self.dialog.selectChannel.selectedAudioChannelName = encodeAudio[0].name;
                                            for(var i=0; i<encodeAudio.length; i++){
                                                self.dialog.selectChannel.encodeAudios.push(encodeAudio[i]);
                                            }
                                        }
                                        self.dialog.selectChannel.visible = true;
                                    });

                                    //$player['Tetris.player']('enable', ['forward', 'ptzctrl']);
                                    $record.find('span').find('.record-stop-icon').remove();
                                    $record.removeClass('active').attr('title', '录制').data('record-id', null);
                                    return;
                                }
                                var userJSON = event.dataTransfer.getData('user');
                                if(userJSON){
                                    var user = $.parseJSON(userJSON);
                                    $embed.data('targetType', user.creater==='ldap'?'XT':'BVC');
                                    var data = {
                                        serialNum:serialNum,
                                        volume:'100%',
                                        format: {
                                            bodyUp: '用户：' + user.username,
                                            bodyDown:''
                                        },
                                        type:'user',
                                        userId:user.userId,
                                        username:user.username,
                                        userno:user.userno,
                                        creater:user.creater,
                                        encoderId:user.encoderId,
                                        decoderId:user.decoderId
                                    };

                                    $player['Tetris.player']('set', data);
                                    //$player['Tetris.player']('disable', ['forward', 'ptzctrl']);
                                    $record.find('span').find('.record-stop-icon').remove();
                                    $record.removeClass('active').attr('title', '录制').data('record-id', null);
                                    $player['Tetris.player']('play', false);

                                    //test
                                    //var url = '<media type=\'stream\' url=\'udp://192.165.56.77:15001\' mode=\'smooth\' />'; console.log(url);
                                    //$embed.I_Play(url);

                                    return;
                                }
                            },
                            onRemove:function($player, data, complete){
                                $player['Tetris.player']('enable', ['forward', 'ptzctrl']);
                                complete();
                            },
                            volumeSet:function($embed, volume){
                                if(typeof volume === 'number'){

                                }else if(volume === true){
                                    return $embed[0].I_SetSilence(false);
                                }else if(volume === false){
                                    return $embed[0].I_SetSilence(true);
                                }
                                return 1;
                            }
                        },
                        interface:{
                            info:function(title, message){
                                self.$message({
                                    type:'warning',
                                    message:title + '：' + message
                                });
                            }
                        }
                    },
                    interface:{
                        createPlayer:function(config){
                            config.class = 'noPrev noNext noFast noSlow noPause noPrintScreen noLayoutSet noProgress';
                            config.theme = 'dark';
                            $(this)['Tetris.player']('create', config);
                        },
                        setEmbed:function($player, $embed){
                            return $player['Tetris.player']('setEmbed', $embed);
                        },
                        getEmbed:function($player){
                            return $player['Tetris.player']('getEmbed');
                        }
                    }
                });

                ajax.post('/monitor/device/find/web/sip/players', null, function(data){
                    var $players = [];
                    if(data && data.length>0){
                        var endIndex = layout.length<data.length?layout.length:data.length;
                        for(var i=0; i<data.length; i++){
                            $webPlayers.push(data[i]);
                            var $player = $('<div id="sip-player-'+(i+1)+'" style="width:100%; height:100%; position:absolute;">' +
                                                '<embed width="100%" height="100%" style="z-index:0" type="application/media-suma-lightlive"/>' +
                                            '</div>');
                            $player.find('embed')
                                   .data('code', data[i].code)
                                   .data('ip', data[i].ip)
                                   .data('username', data[i].username)
                                   .data('password', data[i].password)
                                   .data('bundleId', data[i].bundleId)
                                   .data('bundleType', data[i].bundleType)
                                   .data('bundleName', data[i].bundleName)
                                   .data('layerId', data[i].layerId)
                                   .data('registerLayerIp', data[i].registerLayerIp)
                                   .data('registerLayerPort', data[i].registerLayerPort)
                                   .data('videoChannelId', data[i].videoChannelId)
                                   .data('videoBaseType', data[i].videoBaseType)
                                   .data('audioChannelId', data[i].audioChannelId)
                                   .data('audioBaseType', data[i].audioBaseType);
                            if(i < endIndex){
                                $players.push($player);
                            }
                        }
                    }
                    $('#player-container')['Tetris.playerPanel']('setEmbed', $players);
                    setTimeout(function(){
                        //初始化播放器
                        for(var i=0; i<$players.length; i++){
                            var $player = $players[i];
                            var $plugin = $player.find('embed');
                            app.registerPlayer([{
                                code:$plugin.data('code'),
                                username:$plugin.data('username'),
                                password:$plugin.data('password'),
                                $embed:$plugin
                            }]);
                            /*var useablePort = $plugin.zk_SipPlayer('queryUseablePort', $plugin.data('ip'));
                             if(useablePort === false){
                             console.log('当前浏览器不支持sip播放器插件！')
                             break;
                             }
                             $plugin.zk_SipPlayer('init', {
                             id:i+1,
                             playerCode:$plugin.data('code'),
                             username:$plugin.data('username'),
                             password:$plugin.data('password'),
                             localSipIp:$plugin.data('ip'),
                             localSipPort:5060+i,
                             registerServerSipIp:$plugin.data('registerLayerIp'),
                             registerServerSipPort:$plugin.data('registerLayerPort'),
                             type:'web',
                             selectDevice:'',
                             videoParamIdx:0,
                             videoBitrate:0,
                             audioParamIdx:0,
                             audioBitrate:0
                             });
                             $plugin.zk_SipPlayer('register');*/
                            $plugin.data('inited', true);
                        }
                    }, 100);
                });

                $('.bvc2-layout-button').each(function(){
                    var $button = $(this);
                    var id = $button.data('id');
                    for(var i=0; i<self.buttons.length; i++){
                        if(self.buttons[i].id == id){
                            $button.find('.bvc2-layout-button-icon')['layout-auto']('create', self.buttons[i]);
                            break;
                        }
                    }
                });
            });
        }
    });

    return Vue;
});