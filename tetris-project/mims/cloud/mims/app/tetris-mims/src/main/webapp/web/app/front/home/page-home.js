define([
    'text!' + window.APPPATH + 'front/home/page-home.html',
    'config',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame'
], function(tpl, config, context, commons, Vue){

    var pageId = 'page-home';

    var init = function(){

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el:'#' + pageId + '-wrapper',
            data:{
                menus:context.getProp('menus'),
                user:context.getProp('user'),
                groups:context.getProp('groups')
            },
            methods:{
            	
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