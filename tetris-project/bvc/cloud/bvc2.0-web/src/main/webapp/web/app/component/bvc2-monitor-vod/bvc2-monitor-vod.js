define([
    'text!' + window.APPPATH + 'component/bvc2-monitor-vod/bvc2-monitor-vod.html',
    'restfull',
    'jquery',
    'vue',
    'element-ui',
    'native-record-player',
    'player',
    'player-panel',
    'jquery.layout.auto',
    'date',
    'css!' + window.APPPATH + 'component/bvc2-monitor-vod/bvc2-monitor-vod.css',
    'css!' + window.APPPATH + 'component/jQuery/player/css/Tetris.player.css',
    'css!' + window.APPPATH + 'component/jQuery/playerPanel/css/Tetris.playerPanel.css',
    'css!' + window.APPPATH + 'component/jQuery/jQuery.layout.auto/css/jQuery.layout.auto.css',
    'css!' + window.APPPATH + 'component/bvc2-layout-buttons/bvc2-layout-buttons.css'
], function(tpl, ajax, $, Vue){

	//组件名称
    var pluginName = 'bvc2-monitor-vod';

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
                ]
            }
        },
        computed:{

        },
        watch:{

        },
        methods:{
            destroy:function(){
                for(var i=0; i<$embeds.length; i++){
                    var $embed =  $embeds[i].find('embed');
                    $embed.zk_RecordPlayer('destroy');
                }
                $embeds.splice(0, $embeds.length);
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
            handleDragStart:function(data, e){
                e.dataTransfer.setData('file', $.toJSON(data));
            }
        },
        mounted:function(){
            var self = this;
            self.tree.data.splice(0, self.tree.data.length);
            ajax.post('/monitor/vod/query/resource/tree', null, function(data){
                if(data && data.length>0){
                    for(var i=0; i<data.length; i++){
                        self.tree.data.push(data[i]);
                    }
                }
            });
            self.$nextTick(function(){
                $('#player-container')['Tetris.playerPanel']('create', {
                    split:{
                        layout:[
                            [0.5, 0.5],[0.5, 0.5],
                            [0.5, 0.5],[0.5, 0.5]
                        ]
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
                                var result = $embed.zk_RecordPlayer('start', src.previewUrl);
                                if(result === 0){
                                    self.$message({
                                        type:'danger',
                                        message:'播放失败：'+src.previewUrl
                                    });
                                }else{
                                    complete();
                                }
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
                                if(!file.param){
                                    self.$message({
                                        type:'warning',
                                        message:'当前点播资源没有预览地址，无法播放！'
                                    });
                                    return;
                                }
                                $player['Tetris.player']('set', {
                                    serialNum:serialNum,
                                    volume:'100%',
                                    format: {
                                        bodyUp: file.name
                                    },
                                    previewUrl:file.param
                                });
                                $player['Tetris.player']('play', false, event);
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
                            getPlayTime: function ($embed) {
                                return $embed.zk_RecordPlayer('getPlayTime');
                            },
                            getStartTime: function ($embed) {
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
                $embeds = [];
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