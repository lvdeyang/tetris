/**
 * Created by lvdeyang on 2018/12/26 0026.
 */
define([
    'text!' + window.APPPATH + 'cms/portal/page-portal-article.html',
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
    'css!' + window.APPPATH + 'cms/portal/page-portal-article.css'
], function(tpl, config, $, ajax, context, commons, Vue){

    var pageId = 'page-portal-article';

    var instance = null;

    var init = function(params){
    	//alert(params.id);

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        instance = new Vue({
            el: '#' + pageId + '-wrapper',
            data: {
                user:context.getProp('user'),
                columnlist:[],
                hotArticles:[],
                downloadArticle:[],
                articleName:'',
                mediaUrl:''
            },
            computed:{

            },
            watch:{

            },
            methods:{
            	download:function(url){
            		var self=this;
            		ajax.post('/portal/download/'+params.id, null, function(data){	
                    	window.open(url,"_blank");
                    });
            	}
            	
            },
            created:function(){
            	var self = this;
                ajax.post('/portal/column/list', null, function(data){	
	            	self.columnlist.splice(0, self.columnlist.length);
	            	for(var i=0;i<data.length;i++){
	            		self.columnlist.push(data[i]);
	            	}
	            	
                });

                ajax.post('/portal/queryhot', null, function(data){	
                	if(data.length==0) return;
	            	self.downloadArticle.splice(0, self.downloadArticle.length);
	            	for(var i=0;i<data.length;i++){
	            		self.downloadArticle.push(data[i]);
	            	}
	            	
                });
                
                ajax.post('/portal/queryforu', null, function(data){	
                	if(data.length==0) return;
	            	self.hotArticles.splice(0, self.hotArticles.length);
	            	for(var i=0;i<data.length;i++){
	            		self.hotArticles.push(data[i]);
	            	}
	            	
                });

                ajax.post('/portal/articleInfo/'+params.id, null, function(data){	
                	self.articleName=data.articleName;
                	self.mediaUrl=data.mediaUrl;
                });
                
                
                
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