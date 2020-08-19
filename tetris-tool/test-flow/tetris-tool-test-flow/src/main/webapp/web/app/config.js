define(function(){

    var config = {

        //是否开启ajax debug模式
        ajax:{
            debug:false
        },

        //配置默认页面跳转
        redirect:{
            home:'page-group-list',
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
            icon:[]
        }

    };

    return config;
});
