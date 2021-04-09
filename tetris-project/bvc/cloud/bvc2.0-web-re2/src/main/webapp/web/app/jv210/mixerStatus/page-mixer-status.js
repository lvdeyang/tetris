define([
    'text!' + window.APPPATH + 'jv210/mixerStatus/page-mixer-status.html',
    'restfull',
    'config',
    'commons',
    'vue',
    'element-ui',
    'bvc2-header',
    'bvc2-system-nav-side',
	'extral'
], function(tpl, ajax, config, commons, Vue){

    var pageId = 'page-mixer-status';
    var path='action';
    var method='get_monitor';
    var init = function(p){

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;
	    ajax.get('/net/get/'+p.layerIp+":8008/"+path+'/'+method, null, function(data){
	        var channelArr=[];
	  
	    	for(var i=0;i<data.channels.length;i++){
	    		if(data.channels[i].status=='open'){
	    			channelArr.push(data.channels[i]);
	    		}
	    	}
	    	data.channels=channelArr;
	    	
	    	var arr=[];
	        arr.push(data);
	    	window.jv210Sta = new Vue({
	            el:'#' + pageId + '-wrapper',
	            data:{
					 menurouter: false,
					 shortCutsRoutes:commons.data,
					 active:"/page-layer",
	            	 header:commons.getHeader(1),
	            	 side:{
	                     active:'2-1'
	                 },
	                 
	                 monitorData:arr
	                 
	            },
	            methods:{
	
	            	refreshData:function(){
	     
	            		ajax.get('/net/get/'+p.layerIp+":8008/"+path+'/'+method, null, function(data){
	            			
	            			var channelArr=[];
	            			  
	            	    	for(var i=0;i<data.channels.length;i++){
	            	    		if(data.channels[i].status=='open'){
	            	    			channelArr.push(data.channels[i]);
	            	    		}
	            	    	}
	            	    	data.channels=channelArr;
	            	    	
	            	    	var arr=[];
	            	        arr.push(data);
	            	        
	            	    	for(var i=0;i<jv210Sta.monitorData.length;i++){
	            	    		jv210Sta.monitorData[i].channels=[];
	            	    	}
	            	    	for(var i=0;i<arr.length;i++){
	            	    		for(var j=0;j<jv210Sta.monitorData.length;j++){
	            	    			if(arr[i].bundleID==jv210Sta.monitorData[j].bundleID){
	            	    				for(var k=0;k<arr[i].channels.length;k++){
	            	    					jv210Sta.monitorData[j].channels.push(arr[i].channels[k]);
	            	    				}
	            	    			}
	            	    		}
	            	    	}
	            	    	
	            	
	            		})
	            	}
	            }
	            
	        });
	     });
    };

    var destroy = function(){

    };

    var jv210status = {
            path:'/' + pageId+"/:layerIp",
            component:{
                template:'<div id="' + pageId + '" class="page-wrapper"></div>'
            },
            init:init,
            destroy:destroy
        };

    return jv210status;

    
    
});