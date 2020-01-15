define([
    'text!' + window.APPPATH + 'group/control/page-group-control.html',
    'restfull',
    'config',
    'context',
    'commons',
    'vue',
    'extral',
    'element-ui',
    'bvc2-header',
    'bvc2-tab',
    'bvc2-tab-buttons',
    'bvc2-auto-layout',
    'bvc2-layout-buttons',
    'bvc2-layout-table-source',
    'bvc2-layout-table-destination',
    'bvc2-tree-member-channel-encode',
    'bvc2-tab-agenda-or-scheme'
], function(tpl, ajax, config, context, commons, Vue){

    var pageId = 'page-group-control';

    var init = function(p){

        //设置标题
        commons.setTitle(pageId);

        ajax.get('/device/group/control/query/code/' + p.id, null, function(data){

            var $page = document.getElementById(pageId);
            $page.innerHTML = tpl;

            var members = data.members,
                videoDecodeMembers = [],
                audioEncodeMembers = [],
                videoEncodeMembers = [];

            for(var i=0;i<members.length;i++){
                if(members[i].channelType == 'VenusVideoOut'){
                    videoDecodeMembers.push(members[i]);
                }else if(members[i].channelType == 'VenusAudioIn'){
                    audioEncodeMembers.push(members[i]);
                }else if(members[i].channelType == 'VenusVideoIn'){
                    videoEncodeMembers.push(members[i]);
                }
            }

            //处理角色信息（过滤没绑定设备的角色）
            var roles = data.roles,
                roleMembers = [];

            for(var k=0;k<roles.length;k++) {
                if(roles[k].channels.length >0){
                    for(var m=0;m<roles[k].channels.length;m++){
                        var roleMember = {
                            id:roles[k].id + '-' + roles[k].channels[m].channelType,
                            name:roles[k].name + '-' + roles[k].channels[m].name,
                            roleChannelType:roles[k].channels[m].channelType,
                            roleId:roles[k].id,
                            roleName:roles[k].name,
                            special:roles[k].special,
                            type:roles[k].type
                        };
                        roleMembers.push(roleMember);
                        //设备列表添加可录制角色
                        if(roles[k].type == 'RECORDABLE'){
                            videoDecodeMembers.push(roleMember);
                        }
                    }
                }
            }

            //设置大小屏配置
            var layoutModes = data.screenLayouts;
            var transLayoutModes = [];
            for(var i=0; i<layoutModes.length; i++){
                transLayoutModes.push(layoutModes[i]);
            }
            
            //context传参
            context.clearProp('videoEncodeMembers');
            context.setProp('videoEncodeMembers', videoEncodeMembers);

            var v_groupControl = window.v_groupList = new Vue({
                el:'#' + pageId + '-wrapper',
                data:function(){
                    return {
                        menurouter: false,
                        shortCutsRoutes:commons.data,
                        active:"/page-group-list",
                        header:commons.getHeader(0),
                        config:'',
                        video:'',
                        group:data.group,
                        membersTree:data.membersTree,
                        videoMembers:videoDecodeMembers,
                        audioMembers:audioEncodeMembers,
                        roleMembers:roleMembers,
                        layoutModes:transLayoutModes
                    }
                },
                computed:{
                    tabStyle:function(){
                        var group = this.group;
                        if(group.type==='监控室'){
                            return 'position:absolute; left:15px; top:0; right:230px; bottom:0;';
                        }else if(group.type==='会议室'){
                            return 'position:absolute; left:15px; top:0; right:355px; bottom:0;';
                        }
                    },
                    buttonStyle:function(){
                        var group = this.group;
                        if(group.type==='监控室'){
                            return 'position:absolute; width:205px; right:15px; top:0; bottom:0;';
                        }else if(group.type==='会议室'){
                            return 'position:absolute; width:330px; right:15px; top:0; bottom:0;';
                        }
                    }
                },
                methods:{

                    onGroupStopSuccess:function(){
                        var instance = this;
                        if(instance.$refs.configs.$refs.agendas){
                            var rows = instance.$refs.configs.$refs.agendas.rows;
                            if(rows && rows.length>0){
                                for(var i=0; i<rows.length; i++){
                                    if(rows[i].run) rows[i].run = false;
                                }
                            }
                        }
                        var videos = instance.config.videos;
                        if(videos && videos.length>0){
                            for(var i=0; i<videos.length; i++){
                                if(videos[i].record) videos[i].record = false;
                            }
                        }
                    },

                    onVideoCreate:function(){
                        console.log('视频创建');
                        console.log(arguments);
                    },

                    onVideoRemove:function(video){
                        var instance = this;
                        if(!instance.config.videos || instance.config.videos.length<=0){
                            instance.video = '';
                            v_groupControl.$refs.autoLayout.setLayout('');
                        }
                    },

                    onVideoClick:function(video){
                        var websiteDraw = $.parseJSON(video.websiteDraw);
                        v_groupControl.$refs.autoLayout.setLayout({
                            column:websiteDraw.basic.column,
                            row:websiteDraw.basic.row,
                            cellspan:websiteDraw.cellspan,
                            editable:true
                        });

                        if(video.dstsCache){
                            video.dstsCache.splice(0, video.dstsCache.length);
                            for(var i=0; i<video.dsts.length;i++){
                                video.dstsCache.push(video.dsts[i]);
                            }
                        }
                        v_groupControl.video = video;
                    },

                    onVideoConfig:function(row, type){
                        var instance = this;
                        var _uri = '';
                        if(type === 'agenda'){
                            _uri = '/agenda/query/videos/' + row.id;
                        }else if(type === 'scheme'){
                            _uri = '/scheme/query/videos/' + row.id;
                        }
                        ajax.get(_uri, null, function(data){
                            //设置type
                            row.__businessType = type;
                            if(!row.videos) Vue.set(row, 'videos', []);
                            instance.config = row;
                            instance.video = '';
                            instance.$refs.autoLayout.layout = '';
                            setTimeout(function(){
                                //修改视频
                                instance.config.videos.splice(0, instance.config.videos.length);
                                if(data && data.length>0){
                                    for(var i=0; i<data.length; i++){
                                        instance.config.videos.push(data[i]);
                                    }
                                    instance.$refs.tab.clickItem(data[0]);
                                }
                            }, 0);
                        });
                    },

                    onConfigRemove:function(row, type){
                        var instance = this;
                        if(instance.config.id === row.id){
                            instance.config = '';
                            instance.video = '';
                            instance.$refs.autoLayout.setLayout('');
                        }
                    },

                    runAgenda:function(row, done){
                        ajax.post('/agenda/run/' + v_groupControl.group.id + '/' + row.id, null, function(data, status){
                            if(status === 200){
                                v_groupControl.$message({
                                    type:'success',
                                    message:'操作成功！'
                                });
                            }
                            if(typeof done==='function') done();
                        }, null, [403, 404]);
                    },

                    layoutChange:function(button){
                        if(!v_groupControl.config){
                            v_groupControl.$message({
                                type:'warning',
                                message:'请先指定要操作的议程或方案！'
                            });
                            return;
                        }
                        if(!v_groupControl.video){
                            v_groupControl.$message({
                                type:'warning',
                                message:'还没有要操作的视频！'
                            });
                            return;
                        }
                        v_groupControl.$confirm('此操作将清空屏幕上的配置，并且设置为：' + button.text + ', 是否继续?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning',
                            beforeClose:function(action, instance, done){
                                if(action === 'confirm'){
                                    v_groupControl.$refs.autoLayout.setLayout({
                                        column:button.layout.basic.column,
                                        row:button.layout.basic.row,
                                        cellspan:button.layout.cellspan,
                                        name:button.layout.name,
                                        editable:true
                                    });
                                }
                                done();
                            }
                        });
                    },

                    saveLayout:function(config, video, websiteDraw, position, dst, roleDst, done, layout, smallScreen){
                        var _uri = '';
                        if(config.__businessType === 'agenda'){
                            _uri = '/agenda/update/video/' + v_groupControl.video.id;
                        }else if(config.__businessType === 'scheme'){
                            _uri = '/scheme/update/video/' + v_groupControl.video.id;
                        }
                        ajax.post(_uri, {
                            websiteDraw: $.toJSON(websiteDraw),
                            position: $.toJSON(position),
                            dst: $.toJSON(dst),
                            roleDst: $.toJSON(roleDst),
                            layout:layout,
                            smallScreen: $.toJSON(smallScreen)
                        }, function(data){
                            for(var i=0; i<v_groupControl.config.videos.length; i++){
                                if(v_groupControl.config.videos[i].id === data.id){
                                    v_groupControl.config.videos[i] = data;
                                    break;
                                }
                            }
                            v_groupControl.video = data;
                            v_groupControl.video.dstsCache = $.extend(true, [], data.dsts);
                            done();
                            v_groupControl.$message({
                                type:'success',
                                message:'保存成功！'
                            });
                        });
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