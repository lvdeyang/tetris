/**
 * Created by lvdeyang on 2018/11/20 0020.
 */
define([
    'text!' + window.APPPATH + 'front/media/settings/page-media-settings.html',
    'config',
    'context',
    'commons',
    'restfull',
    'json',
    'file',
    'uploader',
    'vue',
    'element-ui',
    'mi-frame',
    'mi-process-dialog',
    'css!' + window.APPPATH + 'front/media/settings/page-media-settings.css'
], function(tpl, config, context, commons, ajax, $, File, Uploader, Vue){

    var pageId = 'page-media-settings';

    var init = function(p){

        var folderId = p.folderId;

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

                settings:{
                    SWITCH_MEDIA_UPLOAD_REVIEW:'false',

                    PROCESS_UPLOAD_PICTURE:'',
                    PROCESS_EDIT_PICTURE:'',
                    PROCESS_DELETE_PICTURE:'',

                    PROCESS_UPLOAD_VIDEO:'',
                    PROCESS_EDIT_VIDEO:'',
                    PROCESS_DELETE_VIDEO:'',

                    PROCESS_UPLOAD_AUDIO:'',
                    PROCESS_EDIT_AUDIO:'',
                    PROCESS_DELETE_AUDIO:'',

                    PROCESS_UPLOAD_TXT:'',
                    PROCESS_EDIT_TXT:'',
                    PROCESS_DELETE_TXT:'',

                    PROCESS_UPLOAD_VIDEO_STREAM:'',
                    PROCESS_EDIT_VIDEO_STREAM:'',
                    PROCESS_DELETE_VIDEO_STREAM:'',

                    PROCESS_UPLOAD_AUDIO_STREAM:'',
                    PROCESS_EDIT_AUDIO_STREAM:'',
                    PROCESS_DELETE_AUDIO_STREAM:'',

                    PROCESS_UPLOAD_COMPRESS:'',
                    PROCESS_EDIT_COMPRESS:'',
                    PROCESS_DELETE_COMPRESS:''
                }

            },
            methods:{
                selectProcess:function(mediaType){
                    var self = this;
                    var process = self.settings[mediaType];
                    if(process){
                        self.$refs.miProcessDialog.open('/process/list/with/except', [process.id]);
                    }else{
                        self.$refs.miProcessDialog.open('/process/list');
                    }
                    self.$refs.miProcessDialog.setBuffer(mediaType);
                },
                processSelected:function(process, startLoading, endLoading, close){
                    var self = this;
                    var mediaType = self.$refs.miProcessDialog.getBuffer();
                    startLoading();
                    ajax.post('/media/settings/save/with/id/and/name', {
                        type:mediaType,
                        id:process.id,
                        name:process.name
                    }, function(data, status){
                        endLoading();
                        if(status != 200) return;
                        Vue.set(self.settings, mediaType, process);
                        close();
                        self.$message({
                            type:'success',
                            message:'设置成功！'
                        });
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                switchMediaUploadReviewChange:function(val){
                    var self = this;
                    ajax.post('/media/settings/save', {
                        type:'SWITCH_MEDIA_UPLOAD_REVIEW',
                        settings:val
                    }, function(data, status){
                        if(status != 200){
                            self.settings.SWITCH_MEDIA_UPLOAD_REVIEW = (val=='true'?'false':'true');
                            return;
                        }
                        self.$message({
                            type:'success',
                            message:'设置成功！'
                        });
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                }
            },
            created:function(){
                var self = this;
                ajax.post('/media/settings/list', null, function(data){
                    if(data && data.length>0){
                        for(var i=0; i<data.length; i++){
                            var target = data[i];
                            if(target.type.indexOf('PROCESS') === 0){
                                var processInfo = target.settings.split('@@');
                                Vue.set(self.settings, target.type, {
                                    id:processInfo[0],
                                    name:processInfo[1]
                                });
                            }else{
                                Vue.set(self.settings, target.type, target.settings);
                            }
                        }
                    }
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