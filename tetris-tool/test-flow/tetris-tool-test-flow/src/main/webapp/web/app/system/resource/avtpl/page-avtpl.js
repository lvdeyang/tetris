define([
    'text!' + window.APPPATH + 'system/resource/avtpl/page-avtpl.html',
    'restfull',
    'config',
    'commons',
    'vue',
    'element-ui',
    'bvc2-header',
    'bvc2-system-nav-side',
    'bvc2-system-table-base'
], function(tpl, ajax, config, commons, Vue){

    var pageId = 'page-avtpl';

    var init = function(){

        //设置标题
        commons.setTitle(pageId);

        ajax.get('/system/avtpl/query/code', null, function(data){
            //转换数据格式
            var videoFormatOptions = [];
            var videoFormatArr = data.videoFormats;
            if(videoFormatArr && videoFormatArr.length>0){
                for(var i=0; i<videoFormatArr.length; i++){
                    videoFormatOptions.push({
                        label:videoFormatArr[i],
                        value:videoFormatArr[i]
                    });
                }
            }
            var audioFormatOptions = [];
            var audioFormatArr = data.audioFormats;
            if(audioFormatArr && audioFormatArr.length>0){
                for(var i=0; i<audioFormatArr.length; i++){
                    audioFormatOptions.push({
                        label:audioFormatArr[i],
                        value:audioFormatArr[i]
                    });
                }
            }

            var $page = document.getElementById(pageId);
            $page.innerHTML = tpl;

            var v_avtpl = new Vue({
                el:'#' + pageId + '-wrapper',
                data:{
                    header:commons.getHeader(1),
                    side:{
                        active:'0-1'
                    },
                    avtpl:{
                        buttonCreate:'新建参数',
                        buttonRemove:'删除参数',
                        columns:[{
                            label:'模板名称',
                            prop:'name',
                            type:'simple',
                            width:'240'
                        },{
                            label:'视频编码格式',
                            prop:'videoFormat',
                            type:'select',
                            options:videoFormatOptions,
                            width:'240'
                        },{
                            label:'备用视频编码格式',
                            prop:'videoFormatSpare',
                            type:'select',
                            options:videoFormatOptions,
                            width:'240'
                        },{
                            label:'音频编码格式',
                            prop:'audioFormat',
                            type:'select',
                            options:audioFormatOptions,
                        }],
                        load:'/system/avtpl/load',
                        save:'/system/avtpl/save',
                        update:'/system/avtpl/update',
                        remove:'/system/avtpl/remove',
                        removebatch:'/system/avtpl/remove/all',
                        options:[{
                            label:'编辑三挡参数',
                            icon:'el-icon-document',
                            click:'edit-gears'
                        }],
                        pk:'id'
                    }
                },
                methods:{
                    editGears:function(row){
                        window.location.hash = '#/page-avtpl-gears/' + row.id + '/' + row.name;
                    }
                }
            });

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