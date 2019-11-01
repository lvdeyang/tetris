/**
 * Created by lvdeyang on 2018/12/26 0026.
 */
define([
    'text!' + window.APPPATH + 'cms/portal/page-portal-column.html',
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
    'css!' + window.APPPATH + 'cms/portal/page-portal-column.css'
], function(tpl, config, $, ajax, context, commons, Vue){

    var pageId = 'page-portal-column';

    var instance = null;

    var init = function(params){
    	alert(params.id);

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
                seccolumnlist:[],
                downloadArticle:[]
            },
            computed:{

            },
            watch:{

            },
            methods:{
            	switchColumn:function(column){
            		
            	}
            },
            created:function(){
                //ajax.post('/lad', {}, function(data){	
                //});
            	//var self = this;
            	//self.lanmu.splice(0, self.lanmu.length);
            	//self.lanmu.push({id:2, name:b});
            	//self.lanmu.push({id, 1, name:a});
            },
            mounted:function(){

            }
        });

    };

    var destroy = function(){

    };

    var groupList = {
        path:'/' + pageId+'/:id',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});