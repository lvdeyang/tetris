define(function(){

    var config = {

        //是否开启ajax debug模式
        ajax:{
            debug:false,
//            login:'http://192.165.56.64:18080/vue/#/',
//            login:'http://10.10.40.106:8885/vue/#/',
//             login:'http://10.10.40.24:8085/vue/#/',
            login:'http://192.165.58.151:8885/vue/#/',
            authname:'business_cache_key_5002',
            //url:查询菜单
            //queryRouterUrl :'https://www.easy-mock.com/mock/5c35432c90862b0b0cf5042f/example/queryRouter'
            queryRouterUrl: 'http://192.165.58.151:8885/privilege/queryMenu',
            url2600: 'http://10.10.40.103'
        },

        //配置默认页面跳转
        redirect:{
            home:'bvc2-add-repeater',
            error:'page-error'
        },

        //路由loading延迟
        router:{
            loading:{
                timeout:300
            }
        },

        //页面标题
        page:{
            title:{
                base:'融合通信平台',
                sub:{
                    'page-group-list':'组管理',
                    'page-group-control':'流调度',
                    'page-group-2600':'回放/录制',
                    'page-group-monitor':'监控录制',
                    'page-avtpl':'参数模板',
                    'page-business-role':'业务角色',
                    'page-channel-name':'通道别名',
                    'page-layout':'布局方案',
                    'page-record-scheme':'录制方案',
                    'page-tpl':'组模板',
                    'page-error':'出错啦'
                },
                separator:'-'
            }
        },

        //导航条文字
        header:{
            text:['设备组', '资源管理'],
            href:['#/page-group-list', '#/page-avtpl'],
            icon:['header-device-group', 'header-resource-management']
        }

    };

    return config;
});
