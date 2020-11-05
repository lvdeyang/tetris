define([
    'text!' + window.APPPATH + 'group/control/page-group-control.html',
    'restfull',
    'config',
    'commons',
    'vue',
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
], function(tpl, ajax, config, commons, Vue){

    var pageId = 'page-group-control';

    var init = function(p){

        //设置标题
        commons.setTitle(pageId);

        ajax.get('/device/group/control/query/code/' + p.id, null, function(data){

            var $page = document.getElementById(pageId);
            $page.innerHTML = tpl;

            //处理角色信息（过滤没绑定设备的角色）
            var roles = data.roles,
                roleMembers = [];

            for(var k=0;k<roles.length;k++) {
                if(roles[k].channels.length >0){
                    for(var m=0;m<roles[k].channels.length;m++){
                        var roleMember = {
                            id:roles[k].id + '-' + roles[k].channels[m],
                            name:roles[k].name + '-' + roles[k].channels[m],
                            type:roles[k].channels[m],
                            roleId:roles[k].id,
                            roleName:roles[k].name
                        };
                        roleMembers.push(roleMember);
                    }
                }
            }

            var members = data.members,
                videoDecodeMembers = [],
                audioEncodeMembers = [];

            for(var i=0;i<members.length;i++){
                if(members[i].channelType == 'VenusVideoOut'){
                    videoDecodeMembers.push(members[i]);
                }
            }

            for(var j=0;j<members.length;j++){
                if(members[j].channelType == 'VenusAudioIn'){
                    audioEncodeMembers.push(members[j]);
                }
            }

            var v_groupControl = window.v_groupList = new Vue({
                el:'#' + pageId + '-wrapper',
                data:function(){
                    return {
                        header:commons.getHeader(0),
                        config:'',
                        video:'',
                        group:data.group,
                        membersTree:data.membersTree,
                        videoMembers:videoDecodeMembers,
                        audioMembers:audioEncodeMembers,
                        roleMembers:roleMembers
                    }
                },
                methods:{

                    onVideoCreate:function(){
                        console.log('视频创建');
                        console.log(arguments);
                    },

                    onVideoRemove:function(){
                        console.log('视频删除');
                        console.log(arguments);
                    },

                    onVideoClick:function(video){
                        var websiteDraw = $.parseJSON(video.websiteDraw);
                        v_groupControl.$refs.autoLayout.setLayout({
                            column:websiteDraw.basic.column,
                            row:websiteDraw.basic.row,
                            cellspan:websiteDraw.cellspan,
                            editable:true
                        });
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
                            }, 0)
                        });
                    },

                    runAgenda:function(row){
                        ajax.post('/agenda/run/' + v_groupControl.group.id + '/' + row.id, null, function(data){
                            v_groupControl.$message({
                                type:'success',
                                message:'操作成功！'
                            });
                        });
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

                    saveLayout:function(config, video, websiteDraw, position, dst, roleDst, done){
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
                            roleDst: $.toJSON(roleDst)
                        }, function(data){
                            for(var i=0; i<v_groupControl.config.videos.length; i++){
                                if(v_groupControl.config.videos[i].id === data.id){
                                    v_groupControl.config.videos[i] = data;
                                    break;
                                }
                            }
                            v_groupControl.video = data;
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