define([
    'text!' + window.APPPATH + 'group/resource/avtpl/page-group-param-avtpl.html',
    'restfull',
    'config',
    'commons',
    'vue',
    'element-ui',
    'bvc2-header',
    'bvc2-group-param-aside',
    'bvc2-system-table-base',
    'extral'
], function(tpl, ajax, config, commons, Vue){

    var pageId = 'page-group-param-avtpl';

    var init = function(p){

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

            var v_group_avtpl = new Vue({
                el:'#' + pageId + '-wrapper',
                data:{
                    menurouter: false,
                    shortCutsRoutes:commons.data,
                    active:"/page-group-list",
                    header:commons.getHeader(0),
                    side:{
                        active:'0-1'
                    },
                    group:p,
                    avtpl:{
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
                            options:audioFormatOptions
                        }],
                        load:'/device/group/param/query/avtpl/' + p.id,
                        update:'/device/group/param/update/avtpl',
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
                        window.location.hash = '#/page-group-param-avtpl-gears/' + p.id + '/' + row.id + '/' + row.name;
                    },
                    beforeSave:function(row, done){
                        var instance = this,
                            _columns = instance.avtpl.columns;

                        //空校验
                        for(var i=0;i<_columns.length;i++){
                            if(_columns[i].type){
                                if(row[_columns[i].prop] == null || row[_columns[i].prop] == ''){
                                    instance.$message({
                                        message: _columns[i].label + '不能为空！',
                                        type: 'warning'
                                    });

                                    return;
                                }
                            }
                        }

                        done();
                    }
                }
            });

        });
    };

    var destroy = function(){

    };

    var groupList = {
        path:'/' + pageId + '/:id',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;
});