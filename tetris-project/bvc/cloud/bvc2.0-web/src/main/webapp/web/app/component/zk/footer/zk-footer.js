define([
    'text!' + window.APPPATH + 'component/zk/footer/zk-footer.html',
    'restfull',
    'jquery',
    'vue',
    'config',
    'element-ui',
    'jquery.layout.auto',
    'css!' + window.APPPATH + 'component/zk/footer/zk-footer.css',
    'css!' + window.APPPATH + 'component/jQuery/jQuery.layout.auto/css/jQuery.layout.auto.css',
    'css!' + window.APPPATH + 'component/bvc2-layout-buttons/bvc2-layout-buttons.css'
], function(tpl, ajax, $, Vue, config){

	//组件名称
    var pluginName = 'zk-footer';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data:function(){
            return {
            	baseUrl:window.BASEPATH,
                buttons:[
                    {
                        text:'1分屏',
                        layout:{
                            basic:{
                                column:1,
                                row:1
                            }
                        }
                    },{
                        text:'4分屏',
                        layout:{
                            basic:{
                                column:2,
                                row:2
                            }
                        }
                    },{
                        text:'6分屏',
                        layout:{
                            basic:{
                                column:3,
                                row:3
                            },
                            cellspan:{
                                x:0,
                                y:0,
                                r:1,
                                b:1
                            }
                        }
                    },{
                        text:'9分屏',
                        layout:{
                            basic:{
                                column:3,
                                row:3
                            }
                        }
                    },{
                        text:'16分屏',
                        layout:{
                            basic:{
                                column:4,
                                row:4
                            }
                        }
                    }
                ],
                playerLayout:16,
                currentScheme:{
                    id:null,
                    name:null
                },
                qt:''
            }
        },
        computed:{

        },
        watch:{

        },
        methods:{
            layoutChange:function(e){
                var self = this;
                var $target = $(e.target);
                var $button = $target.is('.bvc2-layout-button')?$target:$target.closest('.bvc2-layout-button');
                var index = parseInt($button.data('index'));
                ajax.post('/command/split/change', {split:index}, function(data){
                    if(index === 0){
                        self.playerLayout = 1;
                    }else if(index === 1){
                        self.playerLayout = 4;
                    }else if(index === 2){
                        self.playerLayout = 6;
                    }else if(index === 3){
                        self.playerLayout = 9;
                    }else if(index === 4){
                        self.playerLayout = 16;
                    }
                    self.qt.invoke('changeSplit', index, data.settings);
                });
            },
            changeScheme:function(){

            },
            saveScheme:function(){

            },
            buttonClass:function(index){
                var self = this;
                var playerLayout = self.playerLayout;
                var isActive = false;
                if((playerLayout===1&&index===0) ||
                    (playerLayout===4&&index===1) ||
                    (playerLayout===6&&index===2) ||
                    (playerLayout===9&&index===3) ||
                    (playerLayout===16&&index===4)){
                    isActive = true;
                }
                if(isActive){
                    return 'bvc2-layout-button active';
                }else{
                    return 'bvc2-layout-button';
                }
            }
        },
        mounted:function(){
            var self = this;
            self.qt = new QtContext('footer', function(){

                //初始化ajax
                ajax.init({
                    login:config.ajax.login,
                    redirectLogin:false,
                    authname:config.ajax.authname,
                    debug:config.ajax.debug,
                    messenger:{
                        info:function(message, status){
                            self.qt.info(message);
                        },
                        success:function(message, status){
                            self.qt.success(message);
                        },
                        warning:function(message, status){
                            self.qt.warning(message);
                        },
                        error:function(message, status){
                            self.qt.error(message);
                        }
                    }
                });

                self.qt.on('signalRecvMsgFromHtml', function(msg){
                    alert('footer'+msg);
                });

                //关联设备
                self.qt.on('castDevices', function(e){
                    self.qt.window('/router/zk/footer/dialog/cast/devices', {serial: e.serial}, {width:1024, height:600, title:'第'+ e.serial+'屏关联解码器'});
                });

                //云台控制
                self.qt.on('cloudControl', function(e){
                    self.qt.window('/router/zk/footer/dialog/cloud/control', {serial: e.serial}, {width:700, height:600, title:'第'+ e.serial+'屏关联解码器'});
                });

                var buttons = self.buttons;
                var icons = $('.bvc2-layout-button-icon');
                for(var i=0; i<buttons.length; i++){
                    var button = buttons[i];
                    $(icons[i])['layout-auto']('create', {
                        cell:button.layout.basic,
                        cellspan:button.layout.cellspan,
                        editable:false,
                        theme:'gray'
                    });
                }
                ajax.post('/command/user/info/get/current', null, function(data){
                    if(data.playerSplitLayout == 0){
                        self.playerLayout = 1;
                    }else if(data.playerSplitLayout == 1){
                        self.playerLayout = 4;
                    }else if(data.playerSplitLayout == 2){
                        self.playerLayout = 6;
                    }else if(data.playerSplitLayout == 3){
                        self.playerLayout = 9;
                    }else if(data.playerSplitLayout == 4){
                        self.playerLayout = 16;
                    }
                    if(data.players && data.players.length>0){
                        self.qt.invoke('changeSplit', data.playerSplitLayout, data.players);
                    }
                    /*var name = data.name;
                    var index = data.playerSplitLayout;
                    var settings = [];
                    for(var i=0; i<data.players.length; i++){
                        settings.push({
                            serial:data.players[i].locationIndex,
                            businessId:data.players[i].businessId+'',
                            businessType:data.players[i].playerBusinessType,
                            businessInfo:data.players[i].businessName,
                            url:data.players[i].playUrl,
                            bundleId:data.players[i].bundleId
                        });
                    }
                    self.qt.invoke('changeSplit', index, settings);*/
                });
            });
        }
    });

    return Vue;
});