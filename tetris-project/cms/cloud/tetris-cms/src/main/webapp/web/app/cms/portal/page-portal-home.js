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
                downloadArticle:[],
                firstImage:'',
                firstId:''
                
            },
            computed:{

            },
            watch:{

            },
            methods:{
               
            },
            created:function(){
            	var self = this;
                ajax.post('/portal/column/list', null, function(data){	
	            	self.columnlist.splice(0, self.columnlist.length);
	            	for(var i=0;i<data.length;i++){
	            		self.columnlist.push(data[i]);
	            	}
	            	
                });
                
                ajax.post('/portal/article/new/list', null, function(data){	
                	if(data.length==0) return;
                	self.firstImage=data[0].thumbnail;
                	self.firstId=data[0].id;
	            	self.newArtices.splice(0, self.newArtices.length);
	            	for(var i=0;i<data.length;i++){
	            		self.newArtices.push(data[i]);
	            	}
	            	
                });
                
                ajax.post('/portal/queryCommand', null, function(data){	
                	if(data.articles.length==0) return;
	            	self.recommArticles.splice(0, self.recommArticles.length);
	            	for(var i=0;i<data.articles.length;i++){
	            		self.recommArticles.push(data.articles[i]);
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