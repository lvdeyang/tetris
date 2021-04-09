define([
    'text!' + window.APPPATH + 'group/resource/update-spokesman/page-group-param-update-spokesman.html',
    'restfull',
    'config',
    'commons',
    'vue',
    'element-ui',
    'bvc2-header',
    'bvc2-group-param-aside',
    'bvc2-update-spokesman',
    'extral'
], function(tpl, ajax, config, commons, Vue){

    var pageId = 'page-group-param-update-spokesman';

    var init = function(p){

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        var v_updateSpokesman = new Vue({
            el:'#' + pageId + '-wrapper',
            data:{
                menurouter: false,
                shortCutsRoutes:commons.data,
                active:"/page-group-list",
                header:commons.getHeader(0),
                side:{
                    active:'0-5'
                },
                group:p
            },
            methods:{

            }
        });
    };

    var destroy = function(){

    };

    var groupList = {
        path:'/' + pageId + '/:id',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;
});


