define([
    'text!' + window.APPPATH + 'jv210/status/page-jv210-status.html',
    'restfull',
    'config',
    'commons',
    'vue',
    'element-ui',
    'bvc2-header',
    'bvc2-system-nav-side',
	'extral'
], function(tpl, ajax, config, commons, Vue){

    var pageId = 'page-jv210-status';
    var path='action';
    var method='get_monitor';
    var netPort='8007';
    var init = function(p){
    	netPort='8007';
    	//ACCESS_JV210,ACCESS_JV230,ACCESS_TVOS
        if(p.type=='ACCESS_TVOS'){
        	netPort='8009';
        }else if(p.type=='ACCESS_JV230'){
        	netPort='8010';
        }else if(p.type=='ACCESS_CDN'){
        	netPort='8006';
        }
        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;
	    ajax.get('/net/get/'+p.layerIp+":"+netPort+"/"+path+'/'+method, null, function(data){
	    	var tableData=data.jv210s;
	    	if(p.type=='ACCESS_TVOS'){
	    		tableData=data.TVOSs;
	        }else if(p.type=='ACCESS_JV230'){
	        	tableData=data.jv230s;
	        }else if(p.type=='ACCESS_CDN'){
	        	var arr=[];
	        	arr.push(data.cdn);
	        	tableData=arr;
	        }
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
	                 
	                 monitorData:tableData,
	                 showed:[]
	                 
	            },
	            methods:{
					redirectWeb:function(sip){
						var sips=sip.split('@');
						var ips=sips[1].split(':');
						window.open("http://"+ips[0]);
					},
	            	typec:function(row){
	            		//this.$refs.monitorTable.toggleRowExpansion(row, false)
          	    	    if(row.call_ctx){
          	    	    	return row.call_ctx.type==1?'会议':'点对点通话';
          	    	    }else{
          	    	    	return '-';
          	    	    }
          	        },
	            	refreshData:function(){
	     
	            		ajax.get('/net/get/'+p.layerIp+":"+netPort+"/"+path+'/'+method, null, function(data){
	            			var tableData=data.jv210s;
	            	    	if(p.type=='ACCESS_TVOS'){
	            	    		tableData=data.TVOSs;
	            	        }else if(p.type=='ACCESS_JV230'){
	            	        	tableData=data.jv230s;
	            	        }else if(p.type=='ACCESS_CDN'){
	            	        	var arr=[];
	            	        	arr.push(data.cdn);
	            	        	tableData=arr;
	            	        }
	            	    	//jv210Sta.monitorData.slice(0, jv210Sta.monitorData.length);
	            	    	for(var i=0;i<jv210Sta.monitorData.length;i++){
	            	    		jv210Sta.monitorData[i].channels=[];
	            	    	}
	            	    	for(var i=0;i<tableData.length;i++){
	            	    		for(var j=0;j<jv210Sta.monitorData.length;j++){
	            	    			if(tableData[i].bundleID==jv210Sta.monitorData[j].bundleID){
	            	    				for(var k=0;k<tableData[i].channels.length;k++){
	            	    					jv210Sta.monitorData[j].channels.push(tableData[i].channels[k]);
	            	    				}
	            	    			}
	            	    		}
	            	    	}
	            	    	
	            			//Vue.set(obj, 'aaa', )
	            		})
	            	},
	            	expandChange:function(row, expandedRows){
	            		jv210Sta.showed=expandedRows;
	            	}
	            }
	            
	        });
	        
//	        setInterval(function(){
//	        	jv210Sta.refreshData();
//	        }, 5000);
	     });
    };

    var destroy = function(){

    };

    var jv210status = {
            path:'/' + pageId+"/:layerIp/:type",
            component:{
                template:'<div id="' + pageId + '" class="page-wrapper"></div>'
            },
            init:init,
            destroy:destroy
        };

    return jv210status;

    
    
});