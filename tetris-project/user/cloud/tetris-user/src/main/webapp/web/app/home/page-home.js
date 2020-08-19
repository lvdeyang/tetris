define([
    'text!' + window.APPPATH + 'home/page-home.html',
    window.APPPATH + 'home/page-home.i18n',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'home/page-home.css'
], function(tpl, i18n, config, ajax, context, commons, Vue){

    var locale = context.getProp('locale');
    var i18n = !locale?i18n.default:i18n[locale]?i18n[locale]:i18n.default;

    var pageId = 'page-home';

    var init = function(){

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el:'#' + pageId + '-wrapper',
            data:{
                menus:context.getProp('menus'),
                user:context.getProp('user'),
                groups:context.getProp('groups'),
                i18n:i18n,
                loading:false
            },
            methods:{
                refresh:function(){
                    var self = this;
                    self.loading = true;
                    ajax.post('/home/page', null, function(html){
                        html = html.replace('<link rel="stylesheet" href="eureka/css/wro.css">', '')
                            .replace('<script type="text/javascript" src="eureka/js/wro.js" ></script>', '');
                        self.$nextTick(function(){
                            $('#eureka-page-wrapper').empty().append(html);
                            self.loading = false;
                        });
                    });
                }
            },
            created:function(){
                var self = this;
                self.refresh();
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