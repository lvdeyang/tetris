define([
    'text!' + window.APPPATH + 'system/resource/avtpl-gears/page-avtpl-gears.html',
    'restfull',
    'config',
    'commons',
    'vue',
    'element-ui',
    'bvc2-header',
    'bvc2-system-nav-side',
    'bvc2-system-table-base'
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

            var $page = document.getElementById(pageId);
            $page.innerHTML = tpl;

            var v_avtpl = new Vue({
                el:'#' + pageId + '-wrapper',
                data:{
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
                            type:'simple'
                        },{
                            label:'视频码率',
                            prop:'videoBitRate',
                            type:'simple',
                            width:'200'
                        },{
                            label:'备用视频码率',
                            prop:'videoBitRateSpare',
                            type:'simple',
                            width:'200'
                        },{
                            label:'视频分辨率',
                            prop:'videoResolution',
                            type:'select',
                            options:resolutionOptions,
                            width:'200'
                        },{
                            label:'备用视频分辨率',
                            prop:'videoResolutionSpare',
                            type:'select',
                            options:resolutionOptions,
                            width:'200'
                        },{
                            label:'音频码率',
                            prop:'audioBitRate',
                            type:'simple',
                            width:'200'
                        },{
                            label:'级别',
                            prop:'level',
                            type:'select',
                            options:gearOptions,
                            width:'240'
                        }],
                        load:'/system/avtpl/gears/load/' + p.id ,
                        save:'/system/avtpl/gears/save/' + p.id,
                        update:'/system/avtpl/gears/update',
                        remove:'/system/avtpl/gears/remove',
                        removebatch:'/system/avtpl/gears/remove/all',
                        pk:'id'
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