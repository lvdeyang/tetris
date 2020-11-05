define(function () {

  var useLocalRouteConst = false;
  var requestIp = document.location.host.split(':')[0];
  var config = {

    useLocalRouteConst: false,

    //是否开启ajax debug模式
    ajax: {
      debug: false,
      //            login:'http://192.165.56.64:18080/vue/#/',
      //            login:'http://10.10.40.106:8885/vue/#/',
      login: 'https://192.165.56.140:8543/vue/#/',
      authname: 'business_cache_key_5002',
      header_auth_token: 'tetris-001',
      header_session_id: 'tetris-002',
      //url:查询菜单
      //queryRouterUrl :'https://www.easy-mock.com/mock/5c35432c90862b0b0cf5042f/example/queryRouter'
      queryRouterUrl: useLocalRouteConst ? 'http://' + requestIp + ':8885/privilege/queryMenu' : 'http://192.165.56.77:8887/privilege/queryMenu',
      url2600: 'http://192.165.56.153',
      signalUrl: 'http://192.165.56.84:8099/index'
    },

    //配置默认页面跳转
    redirect: {
      home: 'page-group-list',
      error: 'page-error'
    },

    //路由loading延迟
    router: {
      loading: {
        timeout: 300
      }
    },

    //页面标题
    page: {
      title: {
        base: '任务监视系统',
        sub: {
          'page-group-list': '组管理',
          'page-group-control': '流调度',
          'page-group-2600': '回放/录制',
          'page-group-monitor': '监控录制',
          'page-avtpl': '参数模板',
          'page-business-role': '业务角色',
          'page-channel-name': '通道别名',
          'page-layout': '布局方案',
          'page-record-scheme': '录制方案',
          'page-tpl': '组模板',
          'page-error': '出错啦',
        },
        separator: ''
      }
    },

    //导航条文字
    header: {
      text: ['设备组', '资源管理'],
      href: ['#/page-group-list', '#/page-avtpl'],
      icon: ['header-device-group', 'header-resource-management']
    }

  };

  return config;
});
