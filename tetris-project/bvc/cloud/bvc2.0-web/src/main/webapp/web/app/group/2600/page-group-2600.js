define([
    'text!' + window.APPPATH + 'group/2600/page-group-2600.html',
    'config',
    'commons',
    'vue',
    'extral',
    'element-ui'
], function(tpl, config, commons, Vue){

    var pageId = 'page-group-2600';

    var init = function(p) {

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        var v_group2600 = window.v_group2600 = new Vue({
            el: '#' + pageId + '-wrapper',
            data: function () {
                return {
                    menurouter: false,
                    shortCutsRoutes: commons.data,
                    active: "/page-group-2600",
                    header:commons.getHeader(0),
                    url2600: config.ajax.url2600 + '/form/playBackController?user=Admin'
                }
            }
        });
    };

    var destroy = function(){

    };

    var group2600 = {
        path:'/' + pageId,
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return group2600;
});