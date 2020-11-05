/**
 * Created by sms on 2020/2/20.
 */
define([
    'text!' + window.APPPATH + 'front/operation/package-media/page-operation-package-media.html',
    'config',
    'context',
    'commons',
    'restfull',
    'json',
    'file',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'front/operation/package-media/page-operation-package-media.css'
], function(tpl, config, context, commons, ajax, $, File, Vue){

    var pageId = 'page-operation-package-media';

    var init = function(p){

        var packageId = p.packageId;
        var activeName = p.activeName;
        var activeId = window.BASEPATH + 'index#/page-operation-package';

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
                activeId:activeId,
                loading:false,
                tabs:{
                    activeName: activeName,
                    media: {
                        current: 'picture',
                        lastCurrent: ''
                    },
                    mediaNum: {
                        current: 'picture'
                    }
                },
                tree:{
                    package: {
                        loading: false,
                        props:{
                            label: 'name',
                            children: 'sub'
                        },
                        data: [{
                            id: 1,
                            name: '',
                            price: '',
                            remark: ''
                        }],
                        current: {}
                    },
                    media: {
                        loading: false,
                        props: {
                            label: 'name',
                            children: 'children',
                            disabled: function(data) {
                                return data.type=='FOLDER' && data.children.length<=0
                            }
                        },
                        data: [],
                        checked: {
                            'picture': {
                                uuid: [],
                                data: []
                            },
                            'audio': {
                                uuid: [],
                                data: []
                            },
                            'video': {
                                uuid: [],
                                data: []
                            },
                            'txt': {
                                uuid: [],
                                data: []
                            }
                        }
                    }
                },
                list: {
                    mediaType: [],
                    mediaTypeCheckData: [{
                        mediaType: '',
                        num: 1
                    }],
                    mediaTypeCurrent: [],
                    mediaTypeProps: {
                        key: 'mediaType',
                        label: 'mediaType'
                    },
                    medias: {
                        'picture': [],
                        'audio': [],
                        'video': [],
                        'txt': []
                    }
                },
                form: {
                    streamNum: 1
                },
                table:{
                    tooltip:false,
                    rows:[],
                    page:{
                        current:1
                    }
                },
                dialog:{
                }
            },
            methods:{
                loadPackageList: function() {
                    var self = this;
                    self.tree.package.loading = true;
                    self.tree.package.data.splice(0, self.tree.package.data.length);
                    ajax.post('/operation/package/list/get', null, function(data, status) {
                        if (status == 200) {
                            if (data && data.length > 0) {
                                for(var i = 0; i < data.length; i++) {
                                    self.tree.package.data.push(data[i]);
                                    if (data[i].id == packageId) {
                                        self.tree.package.current = data[i];
                                    }
                                }
                            }
                            self.$nextTick(function(){
                                self.$refs.packageTree.setCurrentKey(packageId);
                            });
                        }
                        self.tree.package.loading = false;
                    })
                },
                loadMedias: function() {
                    var self = this;
                    self.list.medias.picture.splice(0, self.list.medias.picture.length);
                    self.list.medias.video.splice(0, self.list.medias.video.length);
                    self.list.medias.audio.splice(0, self.list.medias.audio.length);
                    self.list.medias.txt.splice(0, self.list.medias.txt.length);
                    ajax.post('/operation/package/detail/media/tree', null, function(data, status) {
                        if (status == 200 && data) {
                            if (data.picture && data.picture.length > 0) {
                                self.list.medias.picture = self.list.medias.picture.concat(data.picture);
                            }
                            if (data.video && data.video.length > 0) {
                                self.list.medias.video = self.list.medias.video.concat(data.video);
                            }
                            if (data.audio && data.audio.length > 0) {
                                self.list.medias.audio = self.list.medias.audio.concat(data.audio);
                            }
                            if (data.txt && data.txt.length > 0) {
                                self.list.medias.txt = self.list.medias.txt.concat(data.txt);
                            }
                        }
                        self.handleMediaTabClick({name: self.tabs.media.current});
                    });
                },
                loadMediaType: function() {
                    var self = this;
                    self.list.mediaType.splice(0, self.list.mediaType.length);
                    ajax.post('/operation/package/detail/type/list', null, function(data, status) {
                        if (status == 200 && data){
                            if (data.length > 0) {
                                for(var i = 0; i < data.length; i++) {
                                    self.list.mediaType.push({
                                        mediaType: data[i],
                                        num: 1
                                    });
                                }
                            }
                        }
                    })
                },
                getDetail: function() {
                    var self = this;
                    self.list.mediaTypeCheckData.splice(0, self.list.mediaTypeCheckData.length);
                    self.list.mediaTypeCurrent.splice(0, self.list.mediaTypeCurrent);
                    ajax.post('/operation/package/detail/get/' + packageId, null, function(data, status) {
                        if (status == 200 && data) {
                            var mediaTypeList = data.mediaTypePermissions;
                            if (mediaTypeList && mediaTypeList.length > 0) {
                                for (var i = 0; i < mediaTypeList.length; i++){
                                    self.list.mediaTypeCheckData.push(mediaTypeList[i]);
                                    self.list.mediaTypeCurrent.push(mediaTypeList[i].mediaType)
                                }
                            }
                            var streamPermission = data.streamPermission;
                            if (streamPermission && streamPermission.num) {
                                self.form.streamNum = streamPermission.num;
                            }
                            var mediaPermissions = data.mediaPermissions;
                            if (mediaPermissions && mediaPermissions.length > 0) {
                                for (var j = 0; j < mediaPermissions.length; j++) {
                                    var permission = mediaPermissions[j];
                                    self.tree.media.checked[permission.mimsType].uuid.push(permission.mimsUuid);
                                    self.tree.media.checked[permission.mimsType].data.push(permission);
                                }
                            }
                        }
                        self.$nextTick(function(){
                            self.handleMediaNumTabClick({name: self.tabs.mediaNum.current});
                        });
                    })
                },
                currentTreeNodeChange: function(row) {
                    var self = this;
                    if (row.id == packageId) return;
                    window.location.hash = '#/page-operation-package-media/' + row.id + '/' + self.tabs.activeName;
                },
                handleTabClick: function(newTab) {
                    var self = this;
                    self.activeName = newTab.name;
                    self.handleMediaTabClick({name: self.tabs.media.current})
                },
                handleMediaCheckChange: function() {
                    var self = this;
                    self.tree.media.loading = true;
                    var lastCurrent = self.tabs.media.lastCurrent;
                    if (lastCurrent) {
                        self.tree.media.checked[lastCurrent].uuid.splice(0, self.tree.media.checked[lastCurrent].uuid.length);
                        var checkedNodes = self.$refs.mediaPermissionTree.getCheckedNodes(true, false);
                        var newSetList = [];
                        if (checkedNodes && checkedNodes.length > 0) {
                            for(var i = 0; i < checkedNodes.length; i++) {
                                var item = checkedNodes[i];
                                self.tree.media.checked[lastCurrent].uuid.push(item.uuid);
                                if (item.type == 'FOLDER') continue;
                                var num = 1;
                                var data = self.tree.media.checked[lastCurrent].data.find(function(x) {
                                    return x.mimsUuid == item.uuid;
                                });
                                if (data) {
                                    num = data.num
                                }
                                newSetList.push({
                                    packageId: packageId,
                                    mimsName: item.name,
                                    mimsId: item.id,
                                    mimsUuid: item.uuid,
                                    mimsType: lastCurrent,
                                    num: num
                                });
                            }
                        }
                        self.tree.media.checked[lastCurrent].data.splice(0, self.tree.media.checked[lastCurrent].data.length);
                        self.tree.media.checked[lastCurrent].data = self.tree.media.checked[lastCurrent].data.concat(newSetList);
                    }
                    self.tree.media.loading = false;
                },
                handleMediaTabClick: function(newTab) {
                    var self = this;
                    self.tabs.media.current = newTab.name;
                    self.tabs.media.lastCurrent = newTab.name;
                    self.tree.media.data = self.list.medias[newTab.name];
                },
                handleMediaNumTabClick: function(newTab) {
                    var self = this;
                    self.tabs.mediaNum.current = newTab.name;
                },
                handleSave: function() {
                    var self = this;
                    self.loading = true;
                    var questData = {
                        mediaPermission: JSON.stringify(
                            self.tree.media.checked.picture.data.concat(
                                self.tree.media.checked.audio.data, self.tree.media.checked.video.data, self.tree.media.checked.txt.data)),
                        mediaTypePermission: JSON.stringify(self.list.mediaTypeCheckData),
                        streamPermission: JSON.stringify({
                            num: self.form.streamNum
                        })
                    };
                    ajax.post('/operation/package/detail/set/' + packageId, questData, function(data, status) {
                        if (status == 200 && data) {
                            var newPackageId = data.packageId;
                            if (newPackageId != packageId) {
                                window.location.hash = '#/page-operation-package-media/' + newPackageId + '/' + self.tabs.activeName;
                            }
                            self.$message({
                                message: '保存成功',
                                type: 'success'
                            });
                        }
                        self.loading = false;
                    })
                },
                handleMediaTypeChooseChanged: function(allChecks,direction,addChecks){
                    var self = this;
                    for (var i = 0; i < addChecks.length; i++){
                        var name = addChecks[i];
                        var data = self.list.mediaType.find(function(x) {
                            return x.mediaType == name;
                        });
                        var index;
                        if (direction=='right'){
                            index = self.list.mediaTypeCurrent.indexOf(name);
                            self.list.mediaTypeCheckData.splice(index, 0, data);
                        } else {
                            index = self.list.mediaTypeCheckData.indexOf(data);
                            self.list.mediaTypeCheckData.splice(index, 1);
                        }
                    }
                    self.$nextTick(function(){});
                },
                showTip: function(text, confirmListener) {
                    var self = this;
                    var h = self.$createElement;
                    self.$msgbox({
                        title: '危险操作',
                        message: h('div', null, [
                            h('div', {class: 'el-message-box__status el-icon-warning'}, null),
                            h('div', {class: 'el-message-box__message'}, [
                                h('p', null, [text])
                            ])
                        ]),
                        type: 'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose: function (action, instance, done) {
                            instance.confirmButtonLoading = true;
                            if (action === 'confirm') {
                                confirmListener(function() {
                                    instance.confirmButtonLoading = false;
                                    done();
                                })
                            } else {
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function () {
                    });
                }
            },
            created:function(){
                var self = this;
                self.loadPackageList();
                self.loadMedias();
                self.loadMediaType();
                self.getDetail();
            }
        });

    };

    var destroy = function(){

    };

    var groupList = {
        path:'/' + pageId + '/:packageId/:activeName',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;
});