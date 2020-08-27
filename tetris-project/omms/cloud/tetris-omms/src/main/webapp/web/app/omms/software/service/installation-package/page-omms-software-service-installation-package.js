/**
 * Created by lvdeyang on 2018/12/24 0024.
 */
define([
    'text!' + window.APPPATH + 'omms/software/service/installation-package/page-omms-software-service-installation-package.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'date',
    'css!' + window.APPPATH + 'omms/software/service/installation-package/page-omms-software-service-installation-package.css'
], function(tpl, config, $, ajax, context, commons, Vue){

    var pageId = 'page-omms-software-service-installation-package';

    var charts = {};

    var init = function(p){

        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el: '#' + pageId + '-wrapper',
            data: {
                basePath:window.BASEPATH,
                menus: context.getProp('menus'),
                user: context.getProp('user'),
                groups: context.getProp('groups'),
                serviceId: p.serviceTypeId,
                serviceName: p.serviceName,
                table:{
                    data:[]
                },
                dialog:{
                    addPackage:{
                        visible:false,
                        loading:false,
                        version:'',
                        remark:'',
                        creator:''
                    }
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                rowKey:function(row){
                    return 'installation-package-' + row.id;
                },
                generateInstallationPackageHistory:function(scope){
                    var self = this;

                },
                gotoInstallationPackageHistory:function(){
                    var self = this;
                    window.location.hash = '#/page-omms-software-service-installation-package-history/' + self.serviceId;
                },
                handleRowDelete:function(scope){
                    var self = this;
                    var row = scope.row;
                }
            },
            mounted:function(){
                var self = this;

            }
        });

    };

    var destroy = function(){

    };

    var groupList = {
        path:'/' + pageId + '/:serviceTypeId/:serviceName',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});