/**
 * Created by lvdeyang on 2018/12/24 0024.
 */
define([
    'text!' + window.APPPATH + 'development/basic/page-development-basic.html',
    window.APPPATH + 'development/basic/page-development-basic.i18n',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'development/basic/page-development-basic.css'
], function(tpl, i18n, config, ajax, context, commons, Vue){

    var locale = context.getProp('locale');
    var i18n = !locale?i18n.default:i18n[locale]?i18n[locale]:i18n.default;

    var pageId = 'page-development-basic';

    var init = function(){

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el: '#' + pageId + '-wrapper',
            data: {
                menus: context.getProp('menus'),
                user: context.getProp('user'),
                groups: context.getProp('groups'),
                i18n:i18n,
                appId:'',
                dialog:{
                    resetAppSecret:{
                        visible:false,
                        password:'',
                        appSecret:'',
                        loading:false
                    }
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                resetAppSecret:function(){
                    var self = this;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:self.i18n.msgboxTitle,
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, [self.i18n.msgboxContent])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: self.i18n.msgboxConfirmButtonText,
                        cancelButtonText: self.i18n.msgboxCancelButtonText,
                        beforeClose:function(action, instance, done){
                            if(action === 'confirm'){
                                self.dialog.resetAppSecret.visible = true;
                                done();
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                handleResetAppSecretClose:function(){
                    var self = this;
                    self.dialog.resetAppSecret.visible = false;
                    self.dialog.resetAppSecret.password = '';
                    self.dialog.resetAppSecret.appSecret = '';
                    self.dialog.resetAppSecret.loading = false;
                },
                handleResetAppSecretCommit:function(){
                    var self = this;
                    self.dialog.resetAppSecret.loading = true;
                    ajax.post('/basic/development/reset/app/secret', {
                        appSecret:self.dialog.resetAppSecret.appSecret,
                        password:self.dialog.resetAppSecret.password
                    }, function(data, status){
                        self.dialog.resetAppSecret.loading = false;
                        if(status !== 200) return;
                        self.$message({
                            status:'success',
                            message:self.i18n.resetAppSecretMessageSuccess
                        });
                        self.handleResetAppSecretClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                }
            },
            created:function(){
                var self = this;
                ajax.post('/basic/development/query', null, function(data){
                    self.appId = data.appId;
                });
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