define([
    'text!' + window.APPPATH + 'monitor/record/page-monitor-record.html',
    'restfull',
    'config',
    'commons',
    'vue',
    'element-ui',
    'bvc2-header',
    'bvc2-system-nav-side',
	'extral',
	'bvc2-monitor-record'
], function(tpl, ajax, config, commons, Vue){

    var pageId = 'page-monitor-record';

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
				active:"/page-monitor-record",
				header:commons.getHeader(0)
			},
			methods: {

			}

		});
    };

    var destroy = function(){
        if(vueInstance){
            vueInstance.$refs.bvc2MonitorRecord.destroy();
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