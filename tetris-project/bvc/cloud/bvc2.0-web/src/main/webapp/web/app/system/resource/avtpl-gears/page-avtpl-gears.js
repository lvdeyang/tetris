define([
    'text!' + window.APPPATH + 'system/resource/avtpl-gears/page-avtpl-gears.html',
    'restfull',
    'config',
    'commons',
    'vue',
    'element-ui',
    'bvc2-header',
    'bvc2-system-nav-side',
    'bvc2-system-table-base',
    'extral'
], function(tpl, ajax, config, commons, Vue){

    var pageId = 'page-avtpl-gears';

    var init = function(p){

        //设置标题
        commons.setTitle(pageId);

        ajax.get('/system/avtpl/gears/query/code', null, function(data){
            //转换数据格式
            var gearOptions = [];
            var gearArr = data.gear;
            if(gearArr && gearArr.length>0){
                for(var i=0; i<gearArr.length; i++){
                    gearOptions.push({
                        label:gearArr[i],
                        value:gearArr[i]
                    });
                }
            }

            var resolutionOptions = [];
            var resolutionArr = data.resolution;
            if(resolutionArr && resolutionArr.length>0){
                for(var i=0; i<resolutionArr.length; i++){
                    resolutionOptions.push({
                        label:resolutionArr[i],
                        value:resolutionArr[i]
                    });
                }
            }

            var audioBitRateOptions = [
                {
                    label:"64kbps",
                    value:64000
                },{
                    label:"128kbps",
                    value:128000
                },{
                    label:"192kbps",
                    value:192000
                },{
                    label:"256kbps",
                    value:256000
                },{
                    label:"320kbps",
                    value:320000
                },{
                    label:"640kbps",
                    value:640000
                }
            ];

            var $page = document.getElementById(pageId);
            $page.innerHTML = tpl;

            var v_avtpl = new Vue({
                el:'#' + pageId + '-wrapper',
                data:{
                    menurouter: false,
                    shortCutsRoutes:commons.data,
                    active:"/page-avtpl",
                    header:commons.getHeader(1),
                    side:{
                        active:'0-1'
                    },
                    table:{
                        buttonCreate:'新建档位',
                        buttonRemove:'删除档位',
                        breadcrumb:[{
                            label:'会议模板：'+ p.name,
                            href:'#/page-avtpl'
                        },{
                            label:'三挡参数'
                        }],
                        columns:[{
                            label:'档位名称',
                            prop:'name',
                            type:'simple',
                            width:'200'
                        },{
                            label:'视频码率',
                            prop:'videoBitRate',
                            type:'simple',
                            width:'180'
                        },{
                            label:'备用视频码率',
                            prop:'videoBitRateSpare',
                            type:'simple',
                            width:'180'
                        },{
                            label:'视频分辨率',
                            prop:'videoResolution',
                            type:'select',
                            options:resolutionOptions,
                            width:'180'
                        },{
                            label:'备用视频分辨率',
                            prop:'videoResolutionSpare',
                            type:'select',
                            options:resolutionOptions,
                            width:'180'
                        },{
                            label:'音频码率',
                            prop:'audioBitRate',
                            type:'select',
                            options:audioBitRateOptions,
                            width:'180'
                        },{
                            label:'帧率',
                            prop:'fps',
                            type:'simple',
                            width:'180'
                        },{
                            label:'级别',
                            prop:'level',
                            type:'select',
                            options:gearOptions,
                            width:'180'
                        }],
                        load:'/system/avtpl/gears/load/' + p.id ,
                        save:'/system/avtpl/gears/save/' + p.id,
                        update:'/system/avtpl/gears/update',
                        remove:'/system/avtpl/gears/remove',
                        removebatch:'/system/avtpl/gears/remove/all',
                        pk:'id'
                    }
                },
                methods:{
                    beforeSave:function(row, done){
                        var instance = this,
                            _columns = instance.table.columns;

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
        path:'/' + pageId + '/:id/:name',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;
});