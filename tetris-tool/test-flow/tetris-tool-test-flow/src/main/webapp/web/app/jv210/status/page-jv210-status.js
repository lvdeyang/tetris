define([
    'text!' + window.APPPATH + 'jv210/status/page-jv210-status.html',
    'restfull',
    'config',
    'commons',
    'vue',
    'element-ui',
    'bvc2-header',
    'bvc2-system-nav-side'
], function(tpl, ajax, config, commons, Vue){

    var pageId = 'page-jv210-status';
    var path='action';
    var method='get_monitor';
    var init = function(p){

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;
	    ajax.get('/net/get/'+path+'/'+method, null, function(data){
	        window.jv210Sta = new Vue({
	            el:'#' + pageId + '-wrapper',
	            data:{
	            	 header:commons.getHeader(1),
	            	 side:{
	                     active:'2-2'
	                 },
	                 
	                 monitorData:data.jv210s
	                 
	            },
	            methods:{
	            	typec:function(row){
          	    	   return row.call_ctx.type==1?'会议':'点对点通话';
          	       }
	            }
	            
	        });
	     });
    };

    var destroy = function(){

    };

    var jv210status = {
            path:'/' + pageId,
            component:{
                template:'<div id="' + pageId + '" class="page-wrapper"></div>'
            },
            init:init,
            destroy:destroy
        };

    return jv210status;

    
    
});