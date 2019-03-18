/**
 * Created by lvy on 2019/3/16.
 */
define([
    'text!' + window.APPPATH + 'cs/management/page-cs-management.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'cs/management/page-cs-management.css'
], function(tpl, config, $, ajax, context, commons, Vue){

    var pageId = 'page-cs-management';

    var init = function(){

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el: '#' + pageId + '-wrapper',
            data: {
                menus: context.getProp('menus'),
                user: context.getProp('user'),
                groups: context.getProp('groups')
            },
            computed:{

            },
            watch:{

            },
            methods:{

            },
            created:function(){

            },
            mounted:function(){

            }
        });
    };

    var destroy = function(){

    };

    var groupList = {
        path:'/' + pageId,
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});
