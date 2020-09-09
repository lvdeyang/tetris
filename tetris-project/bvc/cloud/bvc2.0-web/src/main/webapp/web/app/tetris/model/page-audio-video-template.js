/**
 * Created by lvdeyang on 2020/6/30.
 */
define([
    'text!' + window.APPPATH + 'tetris/model/page-audio-video-template.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'tetris/model/page-audio-video-template.css'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-audio-video-template';

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
                audioFormats:[],
                videoFormats:[],
                resolutions:[],
                usageTypes:[],
                table:{
                    rows:[],
                    pageSize:50,
                    pageSizes:[50, 100, 200, 400],
                    currentPage:0,
                    total:0
                },
                dialog: {
                    createTemplate:{
                        visible:false,
                        loading:false,
                        name:'',
                        videoFormat:'',
                        videoFormatSpare:'',
                        audioFormat:'',
                        mux:false,
                        videoBitRate:'',
                        videoBitRateSpare:'',
                        videoResolution:'',
                        videoResolutionSpare:'',
                        fps:'',
                        audioBitRate:'',
                        usageType:'',
                        isTemplate:false
                    },
                    editTemplate:{
                        visible:false,
                        loading:false,
                        id:'',
                        name:'',
                        videoFormat:'',
                        videoFormatSpare:'',
                        audioFormat:'',
                        mux:'',
                        videoBitRate:'',
                        videoBitRateSpare:'',
                        videoResolution:'',
                        videoResolutionSpare:'',
                        fps:'',
                        audioBitRate:'',
                        usageType:'',
                        isTemplate:''
                    }
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                rowKey:function(row){
                    return 'template-' + row.uuid;
                },
                handleCreate:function(){
                    var self = this;
                    self.dialog.createTemplate.visible = true;
                },
                handleCreateTemplateClose:function(){
                    var self = this;
                    self.dialog.createTemplate.name = '';
                    self.dialog.createTemplate.videoFormat = '';
                    self.dialog.createTemplate.videoFormatSpare = '';
                    self.dialog.createTemplate.audioFormat = '';
                    self.dialog.createTemplate.mux = false;
                    self.dialog.createTemplate.videoBitRate = '';
                    self.dialog.createTemplate.videoBitRateSpare = '';
                    self.dialog.createTemplate.videoResolution = '';
                    self.dialog.createTemplate.videoResolutionSpare = '';
                    self.dialog.createTemplate.fps = '';
                    self.dialog.createTemplate.audioBitRate = '';
                    self.dialog.createTemplate.usageType = '';
                    self.dialog.createTemplate.isTemplate = false;
                    self.dialog.createTemplate.loading = false;
                    self.dialog.createTemplate.visible = false;
                },
                handleCreateTemplateSubmit:function(){
                    var self = this;
                    if(!self.dialog.createTemplate.name){
                        self.$message({
                            type:'error',
                            message:'名称不能为空'
                        });
                        return;
                    }
                    if(!self.dialog.createTemplate.videoFormat){
                        self.$message({
                            type:'error',
                            message:'视频格式不能为空'
                        });
                        return;
                    }
                    if(!self.dialog.createTemplate.videoFormatSpare){
                        self.$message({
                            type:'error',
                            message:'备用视频格式不能为空'
                        });
                        return;
                    }
                    if(!self.dialog.createTemplate.audioFormat ){
                        self.$message({
                            type:'error',
                            message:'音频格式不能为空'
                        });
                        return;
                    }
                    if(!self.dialog.createTemplate.videoBitRate){
                        self.$message({
                            type:'error',
                            message:'视频码率不能为空'
                        });
                        return;
                    }
                    if(!self.dialog.createTemplate.videoBitRateSpare){
                        self.$message({
                            type:'error',
                            message:'备用视频码率不能为空'
                        });
                        return;
                    }
                    if(!self.dialog.createTemplate.videoResolution){
                        self.$message({
                            type:'error',
                            message:'分辨率不能为空'
                        });
                        return;
                    }
                    if(!self.dialog.createTemplate.videoResolutionSpare){
                        self.$message({
                            type:'error',
                            message:'备用分辨率不能为空'
                        });
                        return;
                    }
                    if(!self.dialog.createTemplate.fps){
                        self.$message({
                            type:'error',
                            message:'不能为空'
                        });
                        return;
                    }
                    if(!self.dialog.createTemplate.audioBitRate){
                        self.$message({
                            type:'error',
                            message:'音频码率不能为空'
                        });
                        return;
                    }
                    if(!self.dialog.createTemplate.usageType){
                        self.$message({
                            type:'error',
                            message:'业务不能为空'
                        });
                        return;
                    }
                    self.dialog.createTemplate.loading = true;
                    ajax.post('/tetris/bvc/model/audio/video/template/add', {
                        name:self.dialog.createTemplate.name,
                        videoFormat:self.dialog.createTemplate.videoFormat,
                        videoFormatSpare:self.dialog.createTemplate.videoFormatSpare,
                        audioFormat:self.dialog.createTemplate.audioFormat,
                        mux:self.dialog.createTemplate.mux,
                        videoBitRate:self.dialog.createTemplate.videoBitRate,
                        videoBitRateSpare:self.dialog.createTemplate.videoBitRateSpare,
                        videoResolution:self.dialog.createTemplate.videoResolution,
                        videoResolutionSpare:self.dialog.createTemplate.videoResolutionSpare,
                        fps:self.dialog.createTemplate.fps,
                        audioBitRate:self.dialog.createTemplate.audioBitRate,
                        usageType:self.dialog.createTemplate.usageType,
                        isTemplate:self.dialog.createTemplate.isTemplate
                    }, function(data, status){
                        self.dialog.createTemplate.loading = false;
                        if(status !== 200) return;
                        self.table.rows.push(data);
                        self.table.total += 1;
                        self.handleCreateTemplateClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleDelete:function(){

                },
                handleRowEdit:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.editTemplate.id = row.id;
                    self.dialog.editTemplate.name = row.name;
                    self.dialog.editTemplate.videoFormat = row.videoFormat;
                    self.dialog.editTemplate.videoFormatSpare = row.videoFormatSpare;
                    self.dialog.editTemplate.audioFormat = row.audioFormat;
                    self.dialog.editTemplate.mux = row.mux;
                    self.dialog.editTemplate.videoBitRate = row.videoBitRate;
                    self.dialog.editTemplate.videoBitRateSpare = row.videoBitRateSpare;
                    self.dialog.editTemplate.videoResolution = row.videoResolution;
                    self.dialog.editTemplate.videoResolutionSpare = row.videoResolutionSpare;
                    self.dialog.editTemplate.fps = row.fps;
                    self.dialog.editTemplate.audioBitRate = row.audioBitRate;
                    self.dialog.editTemplate.usageType = row.usageType;
                    self.dialog.editTemplate.isTemplate = row.isTemplate;
                    self.dialog.editTemplate.visible = true;
                },
                handleEditTemplateClose:function(){
                    var self = this;
                    self.dialog.editTemplate.id = '';
                    self.dialog.editTemplate.name = '';
                    self.dialog.editTemplate.videoFormat = '';
                    self.dialog.editTemplate.videoFormatSpare = '';
                    self.dialog.editTemplate.audioFormat = '';
                    self.dialog.editTemplate.mux = '';
                    self.dialog.editTemplate.videoBitRate = '';
                    self.dialog.editTemplate.videoBitRateSpare = '';
                    self.dialog.editTemplate.videoResolution = '';
                    self.dialog.editTemplate.videoResolutionSpare = '';
                    self.dialog.editTemplate.fps = '';
                    self.dialog.editTemplate.audioBitRate = '';
                    self.dialog.editTemplate.usageType = '';
                    self.dialog.editTemplate.isTemplate = '';
                    self.dialog.editTemplate.loading = false;
                    self.dialog.editTemplate.visible = false;
                },
                handleEditTemplateSubmit:function(){
                    var self = this;
                    if(!self.dialog.editTemplate.name){
                        self.$message({
                            type:'error',
                            message:'名称不能为空'
                        });
                        return;
                    }
                    if(!self.dialog.editTemplate.videoFormat){
                        self.$message({
                            type:'error',
                            message:'视频格式不能为空'
                        });
                        return;
                    }
                    if(!self.dialog.editTemplate.videoFormatSpare){
                        self.$message({
                            type:'error',
                            message:'备用视频格式不能为空'
                        });
                        return;
                    }
                    if(!self.dialog.editTemplate.audioFormat ){
                        self.$message({
                            type:'error',
                            message:'音频格式不能为空'
                        });
                        return;
                    }
                    if(!self.dialog.editTemplate.videoBitRate){
                        self.$message({
                            type:'error',
                            message:'视频码率不能为空'
                        });
                        return;
                    }
                    if(!self.dialog.editTemplate.videoBitRateSpare){
                        self.$message({
                            type:'error',
                            message:'备用视频码率不能为空'
                        });
                        return;
                    }
                    if(!self.dialog.editTemplate.videoResolution){
                        self.$message({
                            type:'error',
                            message:'分辨率不能为空'
                        });
                        return;
                    }
                    if(!self.dialog.editTemplate.videoResolutionSpare){
                        self.$message({
                            type:'error',
                            message:'备用分辨率不能为空'
                        });
                        return;
                    }
                    if(!self.dialog.editTemplate.fps){
                        self.$message({
                            type:'error',
                            message:'不能为空'
                        });
                        return;
                    }
                    if(!self.dialog.editTemplate.audioBitRate){
                        self.$message({
                            type:'error',
                            message:'音频码率不能为空'
                        });
                        return;
                    }
                    if(!self.dialog.editTemplate.usageType){
                        self.$message({
                            type:'error',
                            message:'业务不能为空'
                        });
                        return;
                    }
                    self.dialog.editTemplate.loading = true;
                    ajax.post('/tetris/bvc/model/audio/video/template/edit', {
                        id:self.dialog.editTemplate.id,
                        name:self.dialog.editTemplate.name,
                        videoFormat:self.dialog.editTemplate.videoFormat,
                        videoFormatSpare:self.dialog.editTemplate.videoFormatSpare,
                        audioFormat:self.dialog.editTemplate.audioFormat,
                        mux:self.dialog.editTemplate.mux,
                        videoBitRate:self.dialog.editTemplate.videoBitRate,
                        videoBitRateSpare:self.dialog.editTemplate.videoBitRateSpare,
                        videoResolution:self.dialog.editTemplate.videoResolution,
                        videoResolutionSpare:self.dialog.editTemplate.videoResolutionSpare,
                        fps:self.dialog.editTemplate.fps,
                        audioBitRate:self.dialog.editTemplate.audioBitRate,
                        usageType:self.dialog.editTemplate.usageType,
                        isTemplate:self.dialog.editTemplate.isTemplate
                    }, function(data, status){
                        self.dialog.editTemplate.loading = false;
                        if(status!==200) return;
                        for(var i=0; i<self.table.rows.length; i++){
                            if(self.table.rows[i].id == data.id){
                                self.table.rows.splice(i, 1, data);
                            }
                        }
                        self.handleEditTemplateClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleRowDelete:function(scope){
                    var self = this;
                    var row = scope.row;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'危险操作',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['此操作将永久删除该模板，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/audio/video/template/delete', {
                                    id:row.id
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    for(var i=0; i<self.table.rows.length; i++){
                                        if(self.table.rows[i].id == row.id){
                                            self.table.rows.splice(i, 1);
                                        }
                                    }
                                    self.table.total -= 1;
                                    if(self.table.rows.length==0 && self.table.total>0){
                                        self.load(self.table.currentPage-1);
                                    }
                                }, null, ajax.TOTAL_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                handleSizeChange:function(size){
                    var self = this;
                    self.table.pageSize = size;
                    self.load(1);
                },
                handleCurrentChange:function(currentPage){
                    var self = this;
                    self.load(currentPage);
                },
                load:function(currentPage){
                    var self = this;
                    self.table.rows.splice(0, self.table.rows.length);
                    ajax.post('/tetris/bvc/model/audio/video/template/load', {
                        currentPage:currentPage,
                        pageSize:self.table.pageSize
                    }, function(data, status){
                        var total = data.total;
                        var rows = data.rows;
                        if(rows && rows.length>0){
                            for(var i=0; i<rows.length; i++){
                                self.table.rows.push(rows[i]);
                            }
                        }
                        self.table.total = total;
                        self.table.currentPage = currentPage;
                    });
                },
                queryTypes:function(){
                    var self = this;
                    ajax.post('/tetris/bvc/model/audio/video/template/query/types', null, function(data){
                        var audioFormats = data.audioFormats;
                        var videoFormats = data.videoFormats;
                        var resolutions = data.resolutions;
                        var usageTypes = data.usageTypes;
                        for(var i in audioFormats){
                            self.audioFormats.push({
                                id:i,
                                name:audioFormats[i]
                            });
                        }
                        for(var i in videoFormats){
                            self.videoFormats.push({
                                id:i,
                                name:videoFormats[i]
                            });
                        }
                        for(var i in resolutions){
                            self.resolutions.push({
                                id:i,
                                name:resolutions[i]
                            });
                        }
                        for(var i in usageTypes){
                            self.usageTypes.push({
                                id:i,
                                name:usageTypes[i]
                            });
                        }
                    });
                }
            },
            created:function(){
                var self = this;
                self.load(1);
                self.queryTypes();
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