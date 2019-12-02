/**
 * Created by lzp on 2019/7/25.
 */
define([
    'text!' + window.APPPATH + 'system-storage/page-system-storage-config.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'd3',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'system-storage/page-system-storage-config.css'
], function (tpl, config, $, ajax, context, commons, Vue, d3) {

    var pageId = 'page-system-storage-config';

    var init = function (params) {

        var storageId = params.storageId;

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
            computed: {},
            watch: {},
            methods: {

            },
            created: function () {
                var self = this;

            },
            mounted: function () {

            }
        });

    };

    var destroy = function () {

    };

    var groupList = {
        path: '/' + pageId + '/:storageId',
        component: {
            template: '<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init: init,
        destroy: destroy
    };

    return groupList;
});