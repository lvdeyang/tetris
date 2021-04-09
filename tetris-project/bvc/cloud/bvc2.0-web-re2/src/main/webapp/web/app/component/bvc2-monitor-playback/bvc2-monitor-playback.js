define([
    'text!' + window.APPPATH + 'component/bvc2-monitor-playback/bvc2-monitor-playback.html',
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
    'css!' + window.APPPATH + 'component/bvc2-monitor-playback/bvc2-monitor-playback.css',
    'css!' + window.APPPATH + 'component/jQuery/player/css/Tetris.player.css',
    'css!' + window.APPPATH + 'component/jQuery/playerPanel/css/Tetris.playerPanel.css',
    'css!' + window.APPPATH + 'component/jQuery/jQuery.layout.auto/css/jQuery.layout.auto.css',
    'css!' + window.APPPATH + 'component/bvc2-layout-buttons/bvc2-layout-buttons.css'
], function(tpl, context, ajax, $, Vue){

	//组件名称
    var pluginName = 'bvc2-monitor-playback';

    var app = context.getProp('app');

    var $embeds = [];

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
                    },
                    currentDevice:''
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
                table:{
                    data:[],
                    page:{
                        total:0,
                        currentPage:0,
                        pageSize:50,
                        pageCount:5
                    }
                },
                dialog:{
                    selectDevice:{
                        visible:false,
                        tree:{
                            data:[],
                            props:{
                                children:'children',
                                label:'name'
                            },
                            currentVideo:'',
                            currentAudio:''
                        },
                        file:'',
                        osdId:'',
                        osdName:'',
                        loading:false
                    },
                    downloadFile:{
                        visible:false,
                        file:'',
                        name:'',
                        downloadUrl:'',
                        startTime:0,
                        endTime:0,
                        duration:0
                    }
                },
                timeRange:''
            }
        },
        computed:{

        },
        watch:{
            timeRange:function(){
                var self = this;
                self.handleCurrentChange(1);
            }
        },
        methods:{
            destroy:function(){
                for(var i=0; i<$embeds.length; i++){
                    var $embed =  $embeds[i].find('embed');
                    $embed.zk_RecordPlayer('destroy');
                }
                $embeds.splice(0, $embeds.length);
                app.removeDeviceLoop('monitor-playback-encode-tree');
            },
            layoutChange:function(button){
                var self = this;
                if(button.active) return;
                for(var i=0; i<self.buttons.length; i++){
                    if(self.buttons[i].active){
                        self.buttons[i].active = false;
                    }
                }
                self.destroy();
                for(var i=0; i<button.layout.length; i++){
                    $embeds.push($('<div class="webPlayerContainer" style="width:0; height:0; position:absolute; z-index:0;"><embed width="100%" height="100%" style="z-index:0" type="application/media-suma-record" /></div>'));
                }
                $('#player-container')['Tetris.playerPanel']('setLayout', button);
                $('#player-container')['Tetris.playerPanel']('setEmbed', $embeds);
                for(var i=0; i<$embeds.length; i++){
                    $embeds[i].find('embed').zk_RecordPlayer('init');
                }
                button.active = true;
            },
            load:function(condition, currentPage){
                var self = this;

                if(self.timeRange){
                    condition.timeLowerLimit = self.timeRange[0].format('yyyy-MM-dd hh:mm:ss');
                    condition.timeUpperLimit = self.timeRange[1].format('yyyy-MM-dd hh:mm:ss');
                }

                condition.currentPage = currentPage;
                condition.pageSize = self.table.page.pageSize;

                self.table.data.splice(0, self.table.data.length);
                ajax.post('/monitor/record/playback/find/by/condition', condition, function(data){
                    var rows = data.rows;
                    var total = data.total;
                    self.table.page.total = total;
                    self.table.page.currentPage = currentPage;
                    if(rows && rows.length>0){
                        for(var i=0; i<rows.length; i++){
                            self.table.data.push(rows[i]);
                        }
                    }
                });
            },
            currentDeviceChange:function(nodeData){
                var self = this;
                var condition = {type:nodeData.type};
                if(nodeData.type === 'BUNDLE'){
                    var currentDevice = $.parseJSON(self.tree.currentDevice);
                    condition.bundleRealType = currentDevice.realType;
                    condition.bundleId = currentDevice.bundleId;
                }else if(nodeData.type === 'USER'){
                    var currentUser = $.parseJSON(self.tree.currentDevice);
                    condition.recordUserId = currentUser.userId;
                }
                self.load(condition, 1);
            },
            handleCurrentChange:function(currentPage){
                var self = this;
                var nodeData = $.parseJSON(self.tree.currentDevice);
                var condition = {};
                if(nodeData.bundleId){
                    condition.type = 'BUNDLE';
                    condition.bundleRealType = nodeData.realType;
                    condition.bundleId = nodeData.bundleId;
                }else{
                    condition.type = 'USER';
                    condition.recordUserId = nodeData.userId;
                }
                self.load(condition, currentPage);
            },
            handleFileDragStart:function(scope, e){
                var row = scope.row;
                e.dataTransfer.setData('file', $.toJSON(row));
            },
            handleSelectDevice:function(scope){
                var self = this;
                self.dialog.selectDevice.file = scope.row;
                ajax.post('/monitor/device/find/institution/tree/1/true', null, function(data){
                    if(data && data.length>0){
                        for(var i=0; i<data.length; i++){
                            self.dialog.selectDevice.tree.data.push(data[i]);
                        }
                        app.addDeviceLoop('monitor-playback-decode-tree', data);
                    }
                });
                self.dialog.selectDevice.visible = true;
            },
            handleSelectDeviceClose:function(){
                var self = this;
                self.dialog.selectDevice.tree.data.splice(0, self.dialog.selectDevice.tree.length);
                self.dialog.selectDevice.tree.currentVideo = '';
                self.dialog.selectDevice.tree.currentAudio = '';
                self.dialog.selectDevice.file = '';
                self.dialog.selectDevice.osdId = '';
                self.dialog.selectDevice.osdName = '';
                self.dialog.selectDevice.visible = false;
                self.dialog.selectDevice.loading = false;
                app.removeDeviceLoop('monitor-playback-decode-tree');
            },
            handleSelectDeviceCommit:function(){
                var self = this;
                var file = self.dialog.selectDevice.file;
                var currentVideo = $.parseJSON(self.dialog.selectDevice.tree.currentVideo);
                var currentAudio = !self.dialog.selectDevice.tree.currentAudio?{}:$.parseJSON(self.dialog.selectDevice.tree.currentAudio);
                self.dialog.selectDevice.loading = true;
                ajax.post('/monitor/record/playback/add/task', {
                    fileId:file.id,
                    osdId:self.dialog.selectDevice.osdId,
                    dstVideoBundleId:currentVideo.bundleId,
                    dstVideoBundleName:currentVideo.bundleName,
                    dstVideoBundleType:currentVideo.bundleType,
                    dstVideoLayerId:currentVideo.nodeUid,
                    dstVideoChannelId:currentVideo.channelId,
                    dstVideoBaseType:currentVideo.channelType,
                    dstAudioBundleId:currentAudio.bundleId,
                    dstAudioBundleName:currentAudio.bundleName,
                    dstAudioBundleType:currentAudio.bundleType,
                    dstAudioLayerId:currentAudio.nodeUid,
                    dstAudioChannelId:currentAudio.channelId,
                    dstAudioBaseType:currentAudio.channelType
                }, function(data, status){
                    self.dialog.selectDevice.loading = false;
                    if(status !== 200) return;
                    self.$message({
                        type:'success',
                        message:'调阅成功！'
                    });
                    self.handleSelectDeviceClose();
                }, null, [403, 500, 404, 408, 409]);
            },
            handleDownloadFile:function(scope){
                var self = this;
                var row = scope.row;
                self.dialog.downloadFile.file = row;
                ajax.post('/monitor/record/download/url/' + row.id, null, function(data){
                    if(data){
                        var downloadUrl = data.downloadUrl;
                        var duration = data.duration;
                        self.dialog.downloadFile.duration = duration;
                        self.dialog.downloadFile.name = self.dialog.downloadFile.file.fileName;
                        self.dialog.downloadFile.downloadUrl = downloadUrl;
                        self.dialog.downloadFile.startTime = 0;
                        self.dialog.downloadFile.endTime = duration;
                        self.dialog.downloadFile.visible = true;
                    }else{
                        self.$message({
                            type:'danger',
                            message:'获取下载信息失败！'
                        });
                    }
                });
            },
            handleRemoveFile:function(scope){
                var self = this;
                var row = scope.row;
                var h = self.$createElement;
                self.$msgbox({
                    title:'危险操作',
                    message:h('div', null, [
                        h('div', {class:'el-message-box__status el-icon-warning'}, null),
                        h('div', {class:'el-message-box__message'}, [
                            h('p', null, ['此操作将永久删除文件，且不可恢复，是否继续?'])
                        ])
                    ]),
                    type:'wraning',
                    showCancelButton: true,
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    beforeClose:function(action, instance, done){
                        instance.confirmButtonLoading = true;
                        if(action === 'confirm'){
                            ajax.post('/monitor/record/remove/file/' + row.id, null, function(data, status){
                                instance.confirmButtonLoading = false;
                                done();
                                if(status !== 200) return;
                                for(var i=0; i<self.table.data.length; i++){
                                    if(self.table.data[i].id == row.id){
                                        self.table.data.splice(i, 1);
                                        break;
                                    }
                                }
                                self.table.page.total -= 1;
                                if(self.table.data.length<=0 && self.table.page.currentPage>1){
                                    self.load($.parseJSON(self.tree.currentDevice).bundleId, self.table.page.currentPage - 1);
                                }
                            }, null, [403, 404, 408, 409, 500]);
                        }else{
                            instance.confirmButtonLoading = false;
                            done();
                        }
                    }
                }).catch(function(){});
            },
            handleSelectOsd:function(){
                var self = this;
                self.$refs.bvc2DialogSingleOsd.open(self.dialog.selectDevice.osdId);
            },
            onSelectedOsd:function(osd, done, endLoading){
                var self = this;
                self.dialog.selectDevice.osdId = osd.id;
                self.dialog.selectDevice.osdName = osd.name;
                done();
                endLoading;
            },
            handleDownloadFileClose:function(){
                var self = this;
                self.dialog.downloadFile.file = '';
                self.dialog.downloadFile.name = '';
                self.dialog.downloadFile.downloadUrl = '';
                self.dialog.downloadFile.duration = 0;
                self.dialog.downloadFile.startTime = 0;
                self.dialog.downloadFile.endTime = 0;
                self.dialog.downloadFile.visible = false;
            },
            handleDownloadFileCommit:function(){
                var self  = this;
                var downloadUrl = self.dialog.downloadFile.downloadUrl;
                var name = self.dialog.downloadFile.name + '.ts';
                var startTime = self.dialog.downloadFile.startTime;
                var endTime = self.dialog.downloadFile.endTime;
                downloadUrl = downloadUrl + '&name=' + name + '&start=' + startTime + '&end=' + endTime;
                window.open(downloadUrl);
            }
        },
        mounted:function(){
            var self = this;
            self.tree.data.splice(0, self.tree.data.length);
            ajax.post('/monitor/device/find/institution/tree/0/false', null, function(data){
                if(data && data.length>0){
                    for(var i=0; i<data.length; i++){
                        self.tree.data.push(data[i]);
                    }
                    app.addDeviceLoop('monitor-playback-encode-tree', data);
                }
            });
            self.$nextTick(function(){
                $('#player-container')['Tetris.playerPanel']('create', {
                    split:{
                        layout:[
                            [0.5, 0.5],[0.5, 0.5],
                            [0.5, 0.5],[0.5, 0.5]
                        ],
                    },
                    player:{
                        event:{
                            setFolder: function ($embed, srcList) {
                                var _folder = $embed.zk_RecordPlayer('setFolder');
                                self.$message({
                                    type:'info',
                                    message:'当前截屏目录设置为：'+_folder
                                });
                            },
                            openFolder: function ($embed, srcList) {
                                $embed.zk_RecordPlayer('openFolder');
                            },
                            printScreen: function ($embed, srcList) {
                                var _folder = $embed.zk_RecordPlayer('getFolder'),
                                    _imgName = new Date().getTime() + '.jpg';

                                var fullPath = '';
                                if(_folder.charAt(_folder.length-1) === '\\'){
                                    fullPath = _folder+_imgName;
                                }else{
                                    fullPath = _folder+'\\'+_imgName;
                                }
                                $embed.zk_RecordPlayer('printScreen', fullPath);
                                self.$message({
                                    type:'info',
                                    message:'截屏路径：'+fullPath
                                });
                            },
                            goFast:function($embed, speed, complete, status){
                                var $player = $(this);
                                if(status === 'pause'){
                                    $player['Tetris.player']('resume');
                                }
                                $embed.zk_RecordPlayer('setSpeed', speed);
                                complete();
                            },
                            goSlow:function($embed, speed, complete, status){
                                var $player = $(this);
                                if(status === 'pause'){
                                    $player['Tetris.player']('resume');
                                }
                                $embed.zk_RecordPlayer('setSpeed', speed);
                                complete();
                            },
                            goPrev: function ($embed, srcList, complete) {
                                $embed.zk_RecordPlayer('prevFrame');
                                complete();
                            },
                            goPrev: function ($embed, srcList, complete) {
                                $embed.zk_RecordPlayer('prevFrame');
                                complete();
                            },
                            goNext: function ($embed, srcList, complete) {
                                $embed.zk_RecordPlayer('nextFrame');
                                complete();
                            },
                            onStop: function ($embed, srcList, complete) {
                                $embed.zk_RecordPlayer('stop')
                                complete();
                            },
                            onPlay: function ($embed, srcList, osd, complete) {
                                var src = srcList[0];
                                $embed.zk_RecordPlayer('start', src.previewUrl);
                                complete();
                            },
                            onPause:function($embed, data, complete){
                                $embed.zk_RecordPlayer('pause');
                                complete();
                            },
                            onResume:function($embed, data, complete, status){
                                if(status==='fast' || status==='slow'){
                                    $embed.zk_RecordPlayer('setSpeed', 1);
                                }else{
                                    $embed.zk_RecordPlayer('resume');
                                }
                                complete();
                            },
                            onresize: function () {

                            },
                            onDrop: function ($embed, serialNum, oldData, event) {
                                var $player = $(this);
                                var file = $.parseJSON(event.dataTransfer.getData('file'));
                                $player['Tetris.player']('set', {
                                    serialNum:serialNum,
                                    volume:'100%',
                                    format: {
                                        bodyUp: file.fileName,
                                        bodyDown: file.startTime + ' - ' + file.endTime
                                    },
                                    id:file.id,
                                    fileName:file.fileName,
                                    previewUrl:file.previewUrl,
                                    startTime:file.startTime,
                                    endTime:file.endTime
                                });
                            },
                            seek: function ($embed, time_ms, e) {
                                $embed.zk_RecordPlayer('seek', time_ms);
                            },
                            volumeSet:function($embed, volume){
                                if(volume === true){
                                    volume = 100;
                                }else if(volume === false){
                                    volume = 0;
                                }
                                return $embed.zk_RecordPlayer('volumnSet', volume);
                            }
                        },
                        interface:{
                            info:function(title, message){
                                self.$message({
                                    type:'info',
                                    message:title + '：' + message
                                });
                            },
                            getDuration: function ($embed) {
                                return $embed.zk_RecordPlayer('getDuration');
                            },
                            getPlayTime: function($embed) {
                                return $embed.zk_RecordPlayer('getPlayTime');
                            },
                            getStartTime: function($embed) {
                                //return $embed.zk_RecordPlayer('getStartTime');
                                return 0;
                            }
                        }
                    },
                    interface:{
                        createPlayer:function(config){
                            config.class = 'noLayoutSet noPtzctrl noForward noOsd noRecord noDecodeBind';
                            config.theme = 'dark';
                            $(this)['Tetris.player']('create', config);
                        },
                        setEmbed:function($split, $embed){
                            $split['Tetris.player']('setEmbed', $embed);
                        }
                    }
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

                //加入插件
                var $embeds = [];
                for(var i=0; i<4; i++){
                    $embeds.push($('<div class="webPlayerContainer" style="width:0; height:0; position:absolute; z-index:0;"><embed width="100%" height="100%" style="z-index:0" type="application/media-suma-record" /></div>'));
                }
                $('#player-container')['Tetris.playerPanel']('setEmbed', $embeds);
                for(var i=0; i<$embeds.length; i++){
                    $embeds[i].find('embed').zk_RecordPlayer('init');
                }
            });
        }
    });

    return Vue;
});