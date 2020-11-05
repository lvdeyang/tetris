/**
 * Created by sms on 2020/4/8.
 */
define([
    'text!' + window.APPPATH + 'front/operation/dialog/package-detail/operation-dialog-package-detail.html',
    'restfull',
    'json',
    'vue',
    'element-ui',
    'css!' + window.APPPATH + 'front/operation/dialog/package-detail/operation-dialog-package-detail.css'
],function(tpl, ajax, $, Vue){
    var pluginName = 'mi-package-detail';

    var ON_DIALOG_CLOSE = 'on-dialog-close';

    Vue.component(pluginName, {
        template: tpl,
        data: function() {
            return {
                visible: false,
                loading: false,
                packageId: 0,
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
                        picture: [],
                        video: [],
                        audio: [],
                        txt: []
                    }
                },
                tabs:{
                    activeName: 'first',
                    media: {
                        current: 'picture'
                    },
                    mediaNum: {
                        current: 'picture'
                    }
                },
                tree:{
                    media: {
                        loading: false,
                        props: {
                            label: 'name',
                            children: 'children',
                            disabled: function(data) {
                                return true;
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
                form: {
                    streamNum: 1
                }
            }
        },
        methods: {
            open: function(packageId) {
                var self = this;
                self.visible = true;
                self.tree.loading = true;
                self.packageId = packageId;
                self.loadMedias();
                self.loadMediaType();
                self.getDetail();
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
                ajax.post('/operation/package/detail/get/' + self.packageId, null, function(data, status) {
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
                    self.loading = false;
                })
            },
            handleTabClick: function(newTab) {
                var self = this;
                self.activeName = newTab.name;
                self.handleMediaTabClick({name: self.tabs.media.current})
            },
            handleMediaTabClick: function(newTab) {
                var self = this;
                self.tabs.media.current = newTab.name;
                self.tree.media.data = self.list.medias[newTab.name];
            },
            handleMediaNumTabClick: function(newTab) {
                var self = this;
                self.tabs.mediaNum.current = newTab.name;
            },

            handlePackageDetailChange: function() {
                var self = this;
            },
            handlePackageDetailClose: function() {
                var self = this;
                self.visible = false;
                self.loading = false;
                self.tree.loading = false;
                self.tree.media.data = [];
                self.tree.media.checked.picture.uuid = [];
                self.tree.media.checked.picture.data = [];
                self.tree.media.checked.txt.uuid = [];
                self.tree.media.checked.txt.data = [];
                self.tree.media.checked.audio.uuid = [];
                self.tree.media.checked.audio.data = [];
                self.tree.media.checked.video.uuid = [];
                self.tree.media.checked.video.data = [];
            }
        }
    })
});