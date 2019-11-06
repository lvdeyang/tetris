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
                colArtices:[],
                hotArticles:[],
                seccolumnlist:[],
                downloadArticle:[],
                currentColumn:'',
                currentfColumn:params.id
            },
            computed:{

            },
            watch:{

            },
            methods:{
            	
            	switchColumn:function(column){
            		var self = this;
            		self.currentColumn = column.id;
            		ajax.post('/portal/query/'+column.id, null, function(data){	
		            	self.colArtices.splice(0, self.colArtices.length);
		            	for(var i=0;i<data.articles.length;i++){
		            		self.colArtices.push(data.articles[i]);
		            	}
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
                
                ajax.post('/portal/query/'+params.id, null, function(data){
                	if(data.subColumns.length==0) return;
	            	self.seccolumnlist.splice(0, self.seccolumnlist.length);
	            	for(var i=0;i<data.subColumns.length;i++){
	            		self.seccolumnlist.push(data.subColumns[i]);
	            	}
	            	self.currentColumn = data.subColumns[0].id;
	            	ajax.post('/portal/query/'+data.subColumns[0].id, null, function(data){	
		            	self.colArtices.splice(0, self.colArtices.length);
		            	for(var i=0;i<data.articles.length;i++){
		            		self.colArtices.push(data.articles[i]);
		            	}
		            	
	                });
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
        path:'/' + pageId+'/:id',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});