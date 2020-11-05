define([
    'text!' + window.APPPATH + 'jv210/config/page-jv210-config.html',
    'restfull',
    'config',
    'commons',
    'vue',
    'element-ui',
    'bvc2-header',
    'bvc2-system-nav-side',
	'extral'
], function(tpl, ajax, config, commons, Vue){

    var pageId = 'page-jv210-config';
    var path='action';
    var method='get_config';
    var init = function(p){

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;
	    ajax.get('/net/get/'+p.layerIp+":8007/"+path+'/'+method, null, function(data){
	        window.jv210 = new Vue({
	            el:'#' + pageId + '-wrapper',
	            data:{
					 menurouter: false,
					 shortCutsRoutes:commons.data,
					 active:"/page-layer",
	            	 header:commons.getHeader(1),
	            	 side:{
	                     active:'2-1'
	                 },
	                 form: {
	                	 'layerId':data.layerID,
	                	 'localNum':data.local_num,
	                     'localSip':data.local_sip,
	                     'msgServer':data.message_service,
	                     'streamServer':data.turn_service,
	                     'resourceCenter':data.resource_ipport
	                   }
	            },
	            methods: {
	                onSubmit:function(){
	                	var _instance = this;
	                  ajax.post('/net/post/'+p.layerIp+':8007/action/set_config', {
	                	 'layerID':_instance.form.layerId,
	                	 'local_num':_instance.form.localNum,
	                     'local_sip':_instance.form.localSip,
	                     'message_service':_instance.form.msgServer,
	                     'turn_service':_instance.form.streamServer,
	                     'resource_ipport':_instance.form.resourceCenter
	                  }, function(data){});
	                }
	            }  
	            
	        });
	     });
    };

    var destroy = function(){

    };

    var jv210config = {
            path:'/' + pageId+'/:layerIp',
            component:{
                template:'<div id="' + pageId + '" class="page-wrapper"></div>'
            },
            init:init,
            destroy:destroy
        };

    return jv210config;
});