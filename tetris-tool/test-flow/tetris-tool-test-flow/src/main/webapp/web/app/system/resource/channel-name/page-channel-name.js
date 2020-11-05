define([
    'text!' + window.APPPATH + 'system/resource/channel-name/page-channel-name.html',
    'restfull',
    'config',
    'commons',
    'vue',
    'element-ui',
    'bvc2-header',
    'bvc2-system-nav-side',
    'bvc2-system-table-base'
], function(tpl, ajax, config, commons, Vue){

    var pageId = 'page-channel-name';

    var init = function(){

        //设置标题
        commons.setTitle(pageId);

        ajax.get('/system/channel/name/query/code', null, function(data){
            //{channelType:'', channelName:''}
            var channelInfo = data.channelInfo;
            var channelTypeOptions = [];
            if(channelInfo && channelInfo.length>0){
                for(var i=0; i<channelInfo.length; i++){
                    channelTypeOptions.push({
                        label: channelInfo[i].channelType,
                        value:channelInfo[i].channelType
                    });
                }
            }

            var $page = document.getElementById(pageId);
            $page.innerHTML = tpl;

            var v_channelName = new Vue({
                el:'#' + pageId + '-wrapper',
                data:{
                    header:commons.getHeader(1),
                    side:{
                        active:'0-3'
                    },
                    table:{
                        buttonCreate:'新建别名',
                        buttonRemove:'删除别名',
                        columns:[{
                            label:'通道别名',
                            prop:'name',
                            type:'simple'
                        },{
                            label:'通道类型',
                            prop:'channelType',
                            type:'select',
                            options:channelTypeOptions,
                            width:'240',
                            change:function(row, column, instance){
                                //channelType和channelName做联动
                                if(channelInfo && channelInfo.length>0){
                                    for(var i=0; i<channelInfo.length; i++){
                                        if(channelInfo[i].channelType === row.channelType){
                                            row.channelName = channelInfo[i].channelName;
                                            break;
                                        }
                                    }
                                }
                            }
                        },{
                            label:'通道名称',
                            prop:'channelName',
                            type:'simple',
                            editable:false,
                            width:'240'
                        }],
                        load:'/system/channel/name/load',
                        save:'/system/channel/name/save',
                        update:'/system/channel/name/update',
                        remove:'/system/channel/name/remove',
                        removebatch:'/system/channel/name/remove/all',
                        pk:'id'
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