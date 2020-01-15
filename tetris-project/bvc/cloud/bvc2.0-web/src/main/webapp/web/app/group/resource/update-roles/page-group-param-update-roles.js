define([
    'text!' + window.APPPATH + 'group/resource/update-roles/page-group-param-update-roles.html',
    'restfull',
    'config',
    'commons',
    'vue',
    'element-ui',
    'bvc2-header',
    'bvc2-group-param-aside',
    'bvc2-update-roles',
    'extral'
], function(tpl, ajax, config, commons, Vue){

    var pageId = 'page-group-param-update-roles';

    var init = function(p){

        //…Ë÷√±ÍÃ‚
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        var v_updateRoles = new Vue({
            el:'#' + pageId + '-wrapper',
            data:{
                menurouter: false,
                shortCutsRoutes:commons.data,
                active:"/page-group-list",
                header:commons.getHeader(0),
                side:{
                    active:'0-6'
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



