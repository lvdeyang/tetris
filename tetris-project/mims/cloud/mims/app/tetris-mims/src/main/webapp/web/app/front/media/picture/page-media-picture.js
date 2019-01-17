/**
 * Created by lvdeyang on 2018/11/20 0020.
 */
define([
    'text!' + window.APPPATH + 'front/media/picture/page-media-picture.html',
    'config',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame'
], function(tpl, config, context, commons, Vue){

    var pageId = 'page-media-picture';

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
                //添加媒资图片
                handleAdd:function(){

                },
                taskViewShow:function(){

                }
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