define([
    'text!' + window.APPPATH + 'group/signal/page-group-signal.html',
    'config',
    'commons',
    'vue',
    'extral',
    'element-ui'
], function(tpl, config, commons, Vue){

    var pageId = 'page-group-signal';

    var init = function(p) {

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        var v_groupSignal = window.v_groupSignal = new Vue({
            el: '#' + pageId + '-wrapper',
            data: function () {
                return {
                    menurouter: false,
                    shortCutsRoutes: commons.data,
                    active: "/page-group-signal",
                    header:commons.getHeader(0),
                    urlSignal: config.ajax.signalUrl
                }
            }
        });
    };

    var destroy = function(){

    };

    var groupSignal = {
        path:'/' + pageId,
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupSignal;
});