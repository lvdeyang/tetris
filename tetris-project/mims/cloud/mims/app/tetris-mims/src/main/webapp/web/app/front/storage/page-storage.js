/**
 * Created by lzp on 2019/7/25.
 */
define([
    'text!' + window.APPPATH + 'front/storage/page-storage.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'front/storage/page-storage.css'
], function (tpl, config, $, ajax, context, commons, Vue) {

    var pageId = 'page-storage';

    var init = function () {

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
                table:{
                    rows:[],
                    currentPage:'',
                    pageSizes:'',
                    pageSize:'',
                    total:''
                }
            },
            computed: {},
            watch: {},
            methods: {
                handleRowSummary:function(){

                },
                handleCreate:function(){

                },
                handleDelete:function(){

                },
                handleSizeChange:function(){

                },
                handleCurrentChange:function(){

                },
                handleRowEdit:function(){

                },
                handleRowDelete:function(){

                }
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
        path: '/' + pageId,
        component: {
            template: '<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init: init,
        destroy: destroy
    };

    return groupList;
});