require.config({
  baseUrl: window.BASEPATH,
  paths: {
    /* lib */
    'text': window.LIBPATH + 'frame/requireJS/plugins/text',
    'css': window.LIBPATH + 'frame/requireJS/plugins/css',
    'vue': window.LIBPATH + 'frame/vue/vue-2.5.16',
    'vue-router': window.LIBPATH + 'frame/vue/vue-router-3.0.1',
    'jquery': window.LIBPATH + 'frame/jQuery/jquery-2.2.3.min',
    'json': window.LIBPATH + 'frame/jQuery/jquery.json',
    'element-ui': window.LIBPATH + 'ui/element-ui/element-ui-2.4.3.min',
    'sortablejs': window.LIBPATH + 'frame/draggable/sortable',
    'vue-draggable': window.LIBPATH + 'frame/draggable/vue-draggable',
    'extral': window.LIBPATH + 'extral/extral',

    /* commons */
    'context': window.COMMONSPATH + 'context/context',
    'page-wrapper': window.COMMONSPATH + 'page/page-wrapper',
    'router': window.COMMONSPATH + 'router/router',
    'date': window.COMMONSPATH + 'date/date.ext',
    'string': window.COMMONSPATH + 'string/string.ext',
    'storage': window.COMMONSPATH + 'storage/storage.ext',
    'restfull': window.COMMONSPATH + 'restfull/restfull.api',

    'config': window.APPPATH + 'config',
    'commons': window.APPPATH + 'commons',
    'menu': window.COMMONSPATH + 'menu/menu',

    /* themes */
    'theme-adapter': window.APPPATH + 'component/frame/theme-adapter',

    /* components */
    'bvc2-header': window.APPPATH + 'component/bvc2-header/bvc2-header',
    'bvc2-tab': window.APPPATH + 'component/bvc2-tab/bvc2-tab',
    'bvc2-tab-buttons': window.APPPATH + 'component/bvc2-tab-buttons/bvc2-tab-buttons',
    'bvc2-auto-layout': window.APPPATH + 'component/bvc2-auto-layout/bvc2-auto-layout',
    'bvc2-layout-buttons': window.APPPATH + 'component/bvc2-layout-buttons/bvc2-layout-buttons',
    'bvc2-layout-table-source': window.APPPATH + 'component/bvc2-layout-table-source/bvc2-layout-table-source',
    'bvc2-layout-table-destination': window.APPPATH + 'component/bvc2-layout-table-destination/bvc2-layout-table-destination',
    'bvc2-table-agenda': window.APPPATH + 'component/bvc2-table-agenda/bvc2-table-agenda',
    'bvc2-table-scheme': window.APPPATH + 'component/bvc2-table-scheme/bvc2-table-scheme',
    'bvc2-tree-meeting-member': window.APPPATH + 'component/bvc2-tree-meeting-member/bvc2-tree-meeting-member',
    'bvc2-tree-source-list': window.APPPATH + 'component/bvc2-tree-source-list/bvc2-tree-source-list',
    'bvc2-tree-member-channel-encode': window.APPPATH + 'component/bvc2-tree-member-channel-encode/bvc2-tree-member-channel-encode',
    'bvc2-tree-member-recording': window.APPPATH + 'component/bvc2-tree-member-recording/bvc2-tree-member-recording',
    'bvc2-system-nav-side': window.APPPATH + 'component/bvc2-system-nav-side/bvc2-system-nav-side',
    'bvc2-system-table-base': window.APPPATH + 'component/bvc2-system-table-base/bvc2-system-table-base',
    'bvc2-dialog-auto-layout': window.APPPATH + 'component/bvc2-dialog-auto-layout/bvc2-dialog-auto-layout',
    'bvc2-tab-agenda-or-scheme': window.APPPATH + 'component/bvc2-tab-agenda-or-scheme/bvc2-tab-agenda-or-scheme',
    'bvc2-dialog-set-forward': window.APPPATH + 'component/bvc2-dialog-set-forward/bvc2-dialog-set-forward',
    'bvc2-dialog-set-audio': window.APPPATH + 'component/bvc2-dialog-set-audio/bvc2-dialog-set-audio',
    'bvc2-update-spokesman': window.APPPATH + 'component/bvc2-update-spokesman/bvc2-update-spokesman',
    'bvc2-update-roles': window.APPPATH + 'component/bvc2-update-roles/bvc2-update-roles',
    'bvc2-preview-layout': window.APPPATH + 'component/bvc2-preview-layout/bvc2-preview-layout',
    'bvc2-monitor-record': window.APPPATH + 'component/bvc2-monitor-record/bvc2-monitor-record',
    'bvc2-monitor-record-liangwu': window.APPPATH + 'component/bvc2-monitor-record-liangwu/bvc2-monitor-record-liangwu',
    'bvc2-monitor-forword': window.APPPATH + 'component/bvc2-monitor-forword/bvc2-monitor-forword',
    'bvc2-monitor-forword-list': window.APPPATH + 'component/bvc2-monitor-forword-list/bvc2-monitor-forword-list',
    'bvc2-monitor-currenttask': window.APPPATH + 'component/bvc2-monitor-currenttask/bvc2-monitor-currenttask',
    'native-record-player': window.APPPATH + 'component/jQuery/zk_Player/zk_RecordPlayer/js/zk_RecordPlayer',
    'native-sip-player': window.APPPATH + 'component/jQuery/zk_Player/zk_SipPlayer/js/zk_SipPlayer',
    'player': window.APPPATH + 'component/jQuery/player/js/Tetris.player',
    'player-panel': window.APPPATH + 'component/jQuery/playerPanel/js/Tetris.playerPanel',
    'bvc2-monitor-playback': window.APPPATH + 'component/bvc2-monitor-playback/bvc2-monitor-playback',
    'bvc2-monitor-playback-list': window.APPPATH + 'component/bvc2-monitor-playback-list/bvc2-monitor-playback-list',
    'bvc2-monitor-vod': window.APPPATH + 'component/bvc2-monitor-vod/bvc2-monitor-vod',
    'bvc2-monitor-file': window.APPPATH + 'component/bvc2-monitor-file/bvc2-monitor-file',
    'bvc2-monitor-terminal-live': window.APPPATH + 'component/bvc2-monitor-live/terminal/bvc2-monitor-terminal-live',
    'bvc2-monitor-terminal-live-t': window.APPPATH + 'component/bvc2-monitor-live/terminal/bvc2-monitor-terminal-live-t',
    'bvc2-monitor-monitor-live': window.APPPATH + 'component/bvc2-monitor-live/monitor/bvc2-monitor-monitor-live',
    'bvc2-monitor-channel-live': window.APPPATH + 'component/bvc2-monitor-live/channel/bvc2-monitor-channel-live',
    'bvc2-monitor-live-list': window.APPPATH + 'component/bvc2-monitor-live-list/bvc2-monitor-live-list',
    'bvc2-monitor-live-user-list': window.APPPATH + 'component/bvc2-monitor-live-user-list/bvc2-monitor-live-user-list',
    'bvc2-monitor-subtitle': window.APPPATH + 'component/bvc2-monitor-subtitle/bvc2-monitor-subtitle',
    'bvc2-dialog-single-subtitle': window.APPPATH + 'component/bvc2-dialog-single-subtitle/bvc2-dialog-single-subtitle',
    'bvc2-dialog-single-osd': window.APPPATH + 'component/bvc2-dialog-single-osd/bvc2-dialog-single-osd',
    'bvc2-monitor-osd': window.APPPATH + 'component/bvc2-monitor-osd/bvc2-monitor-osd',
    'bvc2-monitor-external-folder': window.APPPATH + 'component/bvc2-monitor-external-folder/bvc2-monitor-external-folder',
    'bvc2-monitor-ptzctrl': window.APPPATH + 'component/bvc2-monitor-ptzctrl/bvc2-monitor-ptzctrl',
    //'bvc2-monitor-user-called-message':window.APPPATH + 'component/bvc2-monitor-message/user-called/bvc2-monitor-user-called-message',
    'bvc2-monitor-call': window.APPPATH + 'component/bvc2-monitor-call/bvc2-monitor-call',
    'bvc2-set-small-screen': window.APPPATH + 'component/bvc2-set-small-screen/bvc2-set-small-screen',

    //set group template
    'bvc2-dialog-set-group-template': window.APPPATH + 'component/bvc2-dialog-set-group-template/bvc2-dialog-set-group-template',

    //publish authority (RTMP)
    'bvc2-dialog-publish-authority': window.APPPATH + 'component/bvc2-dialog-publish-authority/bvc2-dialog-publish-authority',

    //bvc2-dialog-authorization-list
    'bvc2-dialog-authorization-list': window.APPPATH + 'component/bvc2-dialog-authorization-list/bvc2-dialog-authorization-list',

    'bvc2-dialog-quick-group': window.APPPATH + 'component/bvc2-dialog-quick-group/bvc2-dialog-quick-group',

    'bvc2-transfer-source-target': window.APPPATH + 'component/bvc2-transfer-source-target/bvc2-transfer-source-target',
    'bvc2-transfer-audio-target': window.APPPATH + 'component/bvc2-transfer-audio-target/bvc2-transfer-audio-target',

    //trnasfer authority
    'bvc2-transfer-authority-target': window.APPPATH + 'component/bvc2-transfer-authority-target/bvc2-transfer-authority-target',

    'bvc2-transfer-role-target': window.APPPATH + 'component/bvc2-transfer-role-target/bvc2-transfer-role-target',
    'bvc2-dialog-edit-role': window.APPPATH + 'component/bvc2-dialog-edit-role/bvc2-dialog-edit-role',
    'bvc2-group-nav-side': window.APPPATH + 'component/bvc2-group-nav-side/bvc2-group-nav-side',
    'bvc2-group-table-base': window.APPPATH + 'component/bvc2-group-table-base/bvc2-group-table-base',
    'bvc2-group-param-aside': window.APPPATH + 'component/bvc2-group-param-aside/bvc2-group-param-aside',

    /* jquery-components */
    'jquery.layout.auto': window.APPPATH + 'component/jQuery/jQuery.layout.auto/js/jQuery.layout.auto',

    /* pages */
    'page-group-list': window.APPPATH + 'group/list/page-group-list',
    'page-group-control': window.APPPATH + 'group/control/page-group-control',
    'page-group-preview': window.APPPATH + 'group/preview/page-group-preview',
    'page-group-info-combinevideo': window.APPPATH + 'group/info/combinevideo/page-group-info-combinevideo',
    'page-group-info-forward-combinevideo': window.APPPATH + 'group/info/combinevideo/forward/page-group-info-forward-combinevideo',
    'page-group-info-combineaudio': window.APPPATH + 'group/info/combineaudio/page-group-info-combineaudio',
    'page-group-info-forward-combineaudio': window.APPPATH + 'group/info/combineaudio/forward/page-group-info-forward-combineaudio',
    'page-group-info-forward': window.APPPATH + 'group/info/forward/page-group-info-forward',
    'page-group-info-combine-forward': window.APPPATH + 'group/info/forward/combine/page-group-info-combine-forward',
    'page-group-monitor': window.APPPATH + 'group/monitor/page-group-monitor',
    'page-group-record': window.APPPATH + 'group/record/page-group-record',

    'page-group-param-avtpl': window.APPPATH + 'group/resource/avtpl/page-group-param-avtpl',
    'page-group-param-avtpl-gears': window.APPPATH + 'group/resource/avtpl-gears/page-group-param-avtpl-gears',
    'page-group-param-business-role': window.APPPATH + 'group/resource/business-role/page-group-param-business-role',
    'page-group-param-layout': window.APPPATH + 'group/resource/layout/page-group-param-layout',
    'page-group-param-record-scheme': window.APPPATH + 'group/resource/record-scheme/page-group-param-record-scheme',
    'page-group-param-update-spokesman': window.APPPATH + 'group/resource/update-spokesman/page-group-param-update-spokesman',
    'page-group-param-update-roles': window.APPPATH + 'group/resource/update-roles/page-group-param-update-roles',
    'page-group-virtual-source': window.APPPATH + 'group/resource/virtual-source/page-group-virtual-source',

    'page-group-2600': window.APPPATH + 'group/2600/page-group-2600',
    'page-group-signal': window.APPPATH + 'group/signal/page-group-signal',

    'page-avtpl': window.APPPATH + 'system/resource/avtpl/page-avtpl',
    'page-avtpl-gears': window.APPPATH + 'system/resource/avtpl-gears/page-avtpl-gears',
    'page-business-role': window.APPPATH + 'system/resource/business-role/page-business-role',
    'page-channel-name': window.APPPATH + 'system/resource/channel-name/page-channel-name',
    'page-layout': window.APPPATH + 'system/resource/layout/page-layout',
    'page-record-scheme': window.APPPATH + 'system/resource/record-scheme/page-record-scheme',
    'page-tpl': window.APPPATH + 'system/resource/tpl/page-tpl',
    'page-jv230': window.APPPATH + 'system/jv230/page-jv230',
    'page-jv230-control': window.APPPATH + 'system/jv230/page-jv230-control',
    'page-error': window.APPPATH + 'error/page-error',
    'page-jv210-config': window.APPPATH + 'jv210/config/page-jv210-config',
    'page-jv210-status': window.APPPATH + 'jv210/status/page-jv210-status',
    'page-mixer-status': window.APPPATH + 'jv210/mixerStatus/page-mixer-status',
    'page-dic': window.APPPATH + 'system/resource/dictionary/page-dic',
    'page-layer': window.APPPATH + 'jv210/layer/page-layer',
    'page-authorization-list': window.APPPATH + 'system/resource/authorization/page-authorization-list',
    'page-monitor-record': window.APPPATH + 'monitor/record/page-monitor-record',
    'page-monitor-record-liangwu': window.APPPATH + 'monitor/record-liangwu/page-monitor-record-liangwu',
    'page-monitor-playback': window.APPPATH + 'monitor/playback/page-monitor-playback',
    'page-monitor-playback-list': window.APPPATH + 'monitor/playback-list/page-monitor-playback-list',
    'page-monitor-vod': window.APPPATH + 'monitor/vod/page-monitor-vod',
    'page-monitor-file': window.APPPATH + 'monitor/file/page-monitor-file',
    'page-monitor-terminal-live': window.APPPATH + 'monitor/live/page-monitor-terminal-live',
    'page-monitor-terminal-live-t': window.APPPATH + 'monitor/live/page-monitor-terminal-live-t',
    'page-monitor-monitor-live': window.APPPATH + 'monitor/live/page-monitor-monitor-live',
    'page-monitor-channel-live': window.APPPATH + 'monitor/live/page-monitor-channel-live',
    'page-monitor-live-list': window.APPPATH + 'monitor/live-list/page-monitor-live-list',
    'page-monitor-live-user-list': window.APPPATH + 'monitor/live-user-list/page-monitor-live-user-list',
    'page-monitor-subtitle': window.APPPATH + 'monitor/subtitle/page-monitor-subtitle',
    'page-monitor-osd': window.APPPATH + 'monitor/osd/page-monitor-osd',
    'page-monitor-external-folder': window.APPPATH + 'monitor/external-folder/page-monitor-external-folder',
    'page-monitor-currenttask': window.APPPATH + 'monitor/currenttask/page-monitor-currenttask',
    'page-monitor-forword-list': window.APPPATH + 'monitor/forword-list/page-monitor-forword-list',
    'page-guide-control': window.APPPATH + 'guide/control/page-guide-control'

  },
  shim: {
    'vue': {
      exports: 'Vue'
    },
    'vue-router': {
      deps: ['vue'],
      exports: 'VueRouter'
    },
    'element': {
      deps: ['vue']
    },
    'jquery': {
      exports: 'jQuery'
    },
    'json': {
      deps: ['jquery'],
      exports: 'jQuery'
    },
    'extral': {
      deps: ['vue']
    },
    'native-record-player': {
      deps: ['jquery'],
      exports: 'jQuery'
    },
    'native-sip-player': {
      deps: ['jquery'],
      exports: 'jQuery'
    },
    'player': {
      deps: ['jquery'],
      exports: 'jQuery'
    },
    'player-panel': {
      deps: ['player'],
      exports: 'jQuery'
    },
  }
});

require([
  'storage',
  'vue',
  'router',
  'context',
  'config',
  'menu',
  'restfull',
  'commons',
  'element-ui',
  'bvc2-monitor-call',
  'theme-adapter',
], function (storage, Vue, router, context, config, menuUtil, ajax, commons) {

  var app = null;

  //缓存token
  storage.setItem(config.ajax.header_auth_token, window.TOKEN);
  storage.setItem(config.ajax.header_session_id, window.SESSIONID);

  //初始化ajax
  ajax.init({
    login: config.ajax.login,
    authname: config.ajax.header_auth_token,
    sessionIdName: config.ajax.header_session_id,
    debug: config.ajax.debug,
    messenger: {
      info: function (message, status) {
        if (!app) return;
        app.$message({
          message: message,
          type: 'info'
        });
      },
      success: function (message, status) {
        if (!app) return;
        app.$message({
          message: message,
          type: 'success'
        });
      },
      warning: function (message, status) {
        if (!app) return;
        app.$message({
          message: message,
          type: 'warning'
        });
      },
      error: function (message, status) {
        if (!app) return;
        app.$message({
          message: message,
          type: 'error'
        });
      }
    }
  });

  //生成path，用于active
  function generatePath(menus) {
    if (menus && menus.length > 0) {
      for (var i = 0; i < menus.length; i++) {
        var menu = menus[i];
        menu.path = menu.title;
        if (menu.link) {
          if (menu.link.indexOf("#/") != -1) {
            menu.path = menu.link.split("#")[1].split("?")[0];
          }
        }
        if (menu.sub && menu.sub.length > 0) {
          generatePath(menu.sub);
        }
      }
    }
  }

  ajax.get('/prepare/app', null, function (data) {
    var user = data.user;
    var menus = data.menus;
    context.setProp('token', window.TOKEN);
    menuUtil.parseUrlTemplate(menus);
    commons.data = menus;
    generatePath(commons.data);

    //初始化全局vue实例
    app = new Vue({
      router: router,
      data: function () {
        return {
          //{'id':'[{}, {}]'}
          deviceLoop: {},
          $players: [],
          $sipPlugin: '',
          loading: false
        };
      },
      methods: {
        //players:[{code:'播放器号码', username:'用户名', password:'密码', $embed:'embed插件'}]
        registerPlayer: function (players) {
          var self = this;
          console.log('播放器注册：');
          console.log(players);
          if (players && players.length > 0) {
            for (var i = 0; i < players.length; i++) {
              if (!players[i].code) {
                console.log('无效的播放器注册！');
                console.log(players[i]);
                continue;
              }
              self.$players.push(players[i]);
              self.$sipPlugin[0].I_Register(players[i].code, players[i].username, players[i].password);
              //                            self.$sipPlugin[0].I_Register(players[i].code, players[i].username, players[i].code);
            }
          }
        },
        unRegisterPlayer: function (players) {
          var self = this;
          console.log('播放器注销：');
          console.log(players);
          if (players && players.length > 0) {
            for (var i = 0; i < players.length; i++) {
              if (!players[i].code) {
                console.log('无效的播放器注销！');
                console.log(players[i]);
                continue;
              }
              if (typeof players[i].$embed[0].I_Stop === 'function') {
                players[i].$embed[0].I_Stop();
              }
              self.$sipPlugin[0].I_UnRegister(players[i].code);
            }
            for (var i = 0; i < players.length; i++) {
              for (var j = 0; j < self.$players.length; j++) {
                if (players[i].code === self.$players[j].code) {
                  self.$players.splice(j, 1);
                  break;
                }
              }
            }
          }
        },
        addDeviceLoop: function (id, roots) {
          if (!roots || roots.length <= 0) return;
          var self = this;
          var deviceArray = [];
          self.deviceLoop[id] = deviceArray;

          function loop(root, deviceArray) {
            if (root.type === 'FOLDER') {
              if (root.children && root.children.length > 0) {
                for (var i = 0; i < root.children.length; i++) {
                  loop(root.children[i], deviceArray);
                }
              }
            } else {
              deviceArray.push(root);
            }
          }
          for (var i = 0; i < roots.length; i++) {
            loop(roots[i], deviceArray);
          }
        },
        removeDeviceLoop: function (id) {
          var self = this;
          if (self.deviceLoop[id]) {
            self.deviceLoop[id] = null;
          }
        }
      },
      created: function () {
        var self = this;
        self.$sipPlugin = $('<embed style="z-index:-1; position:absolute; width:0; height:0;" type="application/media-suma-sipclient"/>');
        self.$players = [];
        $('body').append(self.$sipPlugin);
        ajax.post('/monitor/device/find/web/sip/players', null, function (data) {
          if (data && data.length > 0) {
            var player = data[0];
            self.$nextTick(function () {
              window.startPlay = function (code, url) {
                console.log('开始播放：' + code + '   ' + url);
                for (var i = 0; i < self.$players.length; i++) {
                  if (self.$players[i].code === code) {
                    if (self.$players[i].$embed.data('targetType') === 'BVC') {
                      console.log('音视频同轴处理：');
                      //url = url.replace('mode=\'lowdelay\'', 'mode=\'smooth\'');
                      console.log(url);
                    }
                    /*var $embed = self.$players[i].$embed;
                    if($embed.closest('.tetris.player').parent()['Tetris.player']('is', 'play')){

                    }*/
                    //test
                    //url = '<media type=\'stream\' url=\'udp://192.165.56.77:15001\' mode=\'smooth\' />'; console.log(url);
                    self.$players[i].$embed[0].I_Play(url);
                    break;
                  }
                }
              };
              window.stopPlay = function (code) {
                console.log('停止播放：' + code);
                for (var i = 0; i < self.$players.length; i++) {
                  if (self.$players[i].code === code) {
                    self.$players[i].$embed[0].I_Stop();
                    break;
                  }
                }
              };
              console.log('sip插件初始化:' + player.registerLayerIp + ':' + player.registerLayerPort + '  ' + player.ip + '  ' + player.port);
              //self.$sipPlugin[0].I_Init(player.registerLayerIp+':'+player.registerLayerPort, player.ip, player.port);
              self.$sipPlugin[0].I_Init(player.registerLayerIp + ':' + player.registerLayerPort, player.ip);
              self.$sipPlugin[0].pluginCallback = function (action, code, url) {
                //alert('插件回调：'+action + ' ' + code + ' ' +url);
                console.log('插件回调：' + action + ' ' + code + ' ' + url);
                if (action === 'start') {
                  window.startPlay(code, url);
                } else {
                  window.stopPlay(code);
                }
              }
            });
          } else {
            //self.$message({
            //    type:'error',
            //    message:'当前用户没有初始化播放器'
            //});
          }
        });
      }
    }).$mount('#app');

    //初始化上下文环境
    context.setProp('app', app)
      .setProp('router', router)
      .setProp('user', {
        name: user.name
      });

    window.onbeforeunload = function (e) {
      //这个地方要用同步请求
      $.ajax({
        url: window.HOST + window.SCHEMA + '/monitor/live/remove/all/webplayer/live',
        async: false
      });
      //ajax.post('/monitor/live/remove/all/webplayer/live', null, null, null, null, null, null, false);
      app.unRegisterPlayer(app.$players);
      console.log('sip插件销毁');
      app.$sipPlugin[0].I_UnInit();
    };
  });

  var requestIp = document.location.host.split(':')[0];
  var changeUrlFunction = function (data) {
    if (data.meta.url && data.meta.url != '') {
      var url = data.meta.url;
      var _url = url.split('://')[0] + '://' + requestIp + ':' + url.split(':')[2];
      data.meta.url = _url;
    }
    if (data.children && data.children.length > 0) {
      for (var i = 0; i < data.children.length; i++) {
        changeUrlFunction(data.children[i]);
      }
    }
  };

  //用户心跳--25秒一次
  /*ajax.post('/heart/beat', null, function(){}, null, [404, 403, 408, 409, 500]);
  var interval_heartBeat = setInterval(function(){
      ajax.post('/heart/beat', null, function(){}, null, [404, 403, 408, 409, 500]);
  }, 25000);*/

  //设备状态轮询--四秒一次
  var interval_deviceStatus = setInterval(function () {
    var needLoop = false;
    for (var i in app.deviceLoop) {
      if (app.deviceLoop[i] && app.deviceLoop[i].length > 0) {
        needLoop = true;
        break;
      }
    }
    if (needLoop) {
      ajax.post('/monitor/device/loop/device/and/user', null, function (data) {
        if (data && data.length > 0) {
          for (var i in app.deviceLoop) {
            var devices = app.deviceLoop[i];
            if (devices && devices.length > 0) {
              for (var j = 0; j < devices.length; j++) {
                for (var k = 0; k < data.length; k++) {
                  if (devices[j].id === data[k].id && devices[j].type === data[k].type) {
                    if (devices[j].style !== data[k].style) {
                      devices[j].style = data[k].style;
                      if (devices[j].type === 'BUNDLE' && devices[j].children && devices[j].children.length > 0) {
                        for (var m = 0; m < devices[j].children.length; m++) {
                          devices[j].children[m].style = data[k].style;
                        }
                      }
                    }
                    break;
                  }
                }
              }
            }
          }
        }
      });
    }
  }, 6000);
});