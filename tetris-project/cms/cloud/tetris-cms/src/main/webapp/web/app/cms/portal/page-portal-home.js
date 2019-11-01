/**
 * Created by lvdeyang on 2018/12/26 0026.
 */
define([
    'text!' + window.APPPATH + 'cms/portal/page-portal-home.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    //'mi-frame',
    //'article-dialog'
    //,
    'css!' + window.APPPATH + 'cms/portal/page-portal-home.css'
], function(tpl, config, $, ajax, context, commons, Vue){

    var pageId = 'page-portal-home';

    var instance = null;

    var init = function(){

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        instance = new Vue({
            el: '#' + pageId + '-wrapper',
            data: {
                
                user:context.getProp('user'),
                columnlist:[],
                newArtices:[],
                hotArticles:[],
                recommArticles:[],
                downloadArticle:[]
                
            },
            computed:{

            },
            watch:{

            },
            methods:{
               
            },
            created:function(){
                //ajax.post('/lad', {}, function(data){	
                //});
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