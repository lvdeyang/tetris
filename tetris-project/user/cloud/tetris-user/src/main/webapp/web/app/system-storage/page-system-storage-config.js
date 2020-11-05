/**
 * Created by lzp on 2019/7/25.
 */
define([
    'text!' + window.APPPATH + 'system-storage/page-system-storage-config.html',
    window.APPPATH + 'system-storage/page-system-storage-config.i18n',
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
], function (tpl, i18n, config, $, ajax, context, commons, Vue, d3) {

    var locale = context.getProp('locale');
    var i18n = !locale?i18n.default:i18n[locale]?i18n[locale]:i18n.default;

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
                groups: context.getProp('groups'),
                i18n:i18n
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