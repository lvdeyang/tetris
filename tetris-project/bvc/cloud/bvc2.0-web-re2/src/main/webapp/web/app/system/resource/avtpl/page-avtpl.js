define([
    'text!' + window.APPPATH + 'system/resource/avtpl/page-avtpl.html',
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

            var usageTypeOptions = [];
            var usageTypeArr = data.usageTypes;
            if(usageTypeArr && usageTypeArr.length>0){
            	for(var i=0; i<usageTypeArr.length; i++){
            		usageTypeOptions.push({
            			label:usageTypeArr[i],
            			value:usageTypeArr[i]
            		});
            	}
            }
            
            var $page = document.getElementById(pageId);
            $page.innerHTML = tpl;

            var columns = [{
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
                width:'240'
            },{
                label:'设备类型',
                prop:'usageType',
                type:'select',
                options:usageTypeOptions,
                change:function(row){
                    if(row.usageType === '点播系统'){
                        columns[5].disabled = false;
                    }else{
                        row.mux = false;
                        columns[5].disabled = true;
                    }
                }
            },{
                label:'端口复用',
                prop:'mux',
                type:'select',
                format:function(value){
                    if(value){
                        return '开启';
                    }else{
                        return '关闭';
                    }
                },
                options:[{
                    label:'关闭',
                    value:false
                },{
                    label:'开启',
                    value:true
                }]
            }];

            var v_avtpl = new Vue({
                el:'#' + pageId + '-wrapper',
                data:{
                    version: data.version,
                    menurouter: false,
                    shortCutsRoutes:commons.data,
                    active:"/page-avtpl",
                    header:commons.getHeader(1),
                    side:{
                        active:'0-1'
                    },
                    avtpl:{
                        buttonCreate:'新建参数',
                        buttonRemove:'删除参数',
                        columns:columns,
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
                    rowEdit:function(row, done){
                        if(row.usageType === '点播系统'){
                            columns[5].disabled = false;
                        }else{
                            columns[5].disabled = true;
                        }
                        done();
                    },
                    editGears:function(row){
                        window.location.hash = '#/page-avtpl-gears/' + row.id + '/' + row.name;
                    },
                    afterAdd:function(row){
                        var instance = this;
                        instance.$confirm('是否要继续编辑三挡参数？', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'info',
                            beforeClose:function(action, ins, d){
                                if(action === 'confirm'){
                                    window.location.hash = '#/page-avtpl-gears/' + row.id + '/' + row.name;
                                }else if(action === 'cancel'){
                                    d();
                                }
                            }
                        });
                    },
                    beforeSave:function(row, done){
                        var instance = this,
                            _columns = instance.avtpl.columns;

                        //空校验
                        for(var i=0;i<_columns.length;i++){
                            if(_columns[i].type){
                                if(row[_columns[i].prop] === null || row[_columns[i].prop] === '' || typeof row[_columns[i].prop] === 'undefined'){
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
        path:'/' + pageId,
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;
});