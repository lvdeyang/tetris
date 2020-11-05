define([
    'text!' + window.APPPATH + 'monitor/vod/page-monitor-vod.html',
    'restfull',
    'config',
    'commons',
    'vue',
    'element-ui',
    'bvc2-header',
    'bvc2-system-nav-side',
	'extral',
	'bvc2-monitor-vod'
], function(tpl, ajax, config, commons, Vue){

    var pageId = 'page-monitor-vod';

    var vueInstance = null;

    var init = function(p){

		var $page = document.getElementById(pageId);
		$page.innerHTML = tpl;

        //设置标题
        commons.setTitle(pageId);

		vueInstance = new Vue({
			el:'#' + pageId + '-wrapper',
			data:{
				menurouter: false,
				shortCutsRoutes:commons.data,
				active:"/page-monitor-vod",
				header:commons.getHeader(0)
			},
			methods: {

			}

		});
    };

    var destroy = function(){
        if(vueInstance){
            vueInstance.$refs.bvc2MonitorVod.destroy();
        }
    };

    var page = {
            path:'/' + pageId,
            component:{
                template:'<div id="' + pageId + '" class="page-wrapper"></div>'
            },
            init:init,
            destroy:destroy
        };

    return page;
});