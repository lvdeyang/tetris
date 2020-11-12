/**
 * Created by lzp on 2019/3/16.
 */
define([
    'text!' + window.APPPATH + 'cs/template/page-cs-templatepro.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'mi-lightbox',
    'cs-user-dialog',
    'program-screen',
    'cs-media-picker',
    'cs-area-picker',
    'mi-compress-dialog',
    'mi-tag-dialog',
    'css!' + window.APPPATH + 'cs/template/page-cs-templatepro.css'
], function (tpl, config, $, ajax, context, commons, Vue) {

    var pageId = 'page-cs-templatepro';

    var init;
    init = function (p) {

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
                loading: false,
                loadingText: "",
                templatepro: {
                    page: {
                        currentPage: 1,
                        sizes: [10, 15, 20, 50],
                        size: 10,
                        total: 0
                    },
                    data: [],
                    multipleSelection: []
                },
                dialog: {
                    addTemplatepro: {
                        visible: false,
                        loading: false,
                        startTime:'',
                        endTime:'',
                        programeType:'',
                        labelIds:'',
                        labelNames:'',
                        mimsId:'',
                        mimsName:'',
                        url:'',
                        typeOptions:['标签','媒资'],
                        labels:[],
                        mims:[],
                        dialog: {
                            chooseResource: {
                                visible: false,
                                loading: false,
                                tree: {
                                    props: {
                                        label: "name",
                                        children: "children"
                                    },
                                    expandOnClickNode: false,
                                    data: [],
                                    current: '',
                                    loading: false
                                },
                                chooseNode: []
                            }
                        }
                    }
                }
            },
            computed: {},
            watch: {},
            methods: {
                
                getTemplateproList: function () {
                    var self = this;
                    self.loading = true;
                    var requestData = {
                    	tempId:p.tempId,
                        currentPage: self.templatepro.page.currentPage,
                        pageSize: self.templatepro.page.size
                    };
                    ajax.post('/cs/channel/template/pro/list', requestData, function (data, status) {
                        self.loading = false;
                        if (status != 200) return;
                        self.templatepro.data = data.rows;
                        self.templatepro.page.total = data.total;
                    }, null, ajax.NO_ERROR_CATCH_CODE)
                },
                multiDelete: function () {
                    var self = this;

                    
                },
                handleAddTemplatepro: function () {
                    var self = this;
                    self.dialog.addTemplatepro.programeType='标签';
                    self.dialog.addTemplatepro.startTime='';
                    self.dialog.addTemplatepro.endTime='';
                    self.dialog.addTemplatepro.mims.splice(0, self.dialog.addTemplatepro.mims.length);
                    self.dialog.addTemplatepro.labels.splice(0, self.dialog.addTemplatepro.labels.length);
                    self.dialog.addTemplatepro.visible = true;
                },
                handleAddTemplateproClose: function () {
                    var self = this;
                    self.dialog.addTemplatepro.visible = false;
                },
                handleAddTemplateproCommit: function () {
                    var self = this;
                   
                    self.dialog.addTemplatepro.loading = true;
                    var labelIds=[];
                    var labelNames=[];
                    for(var i=0;i<self.dialog.addTemplatepro.labels.length;i++){
                    	//labelIds.push(self.dialog.addTemplatepro.labels[i].id);
                    	labelNames.push(self.dialog.addTemplatepro.labels[i].name);
                    }
                    var mimsId=0;
                    var mimsName='';
                    var mimsUrl='';
                    var duration='';
                    if(self.dialog.addTemplatepro.mims.length>0){
                    	mimsId=self.dialog.addTemplatepro.mims[0].id;
                        mimsName=self.dialog.addTemplatepro.mims[0].name;
                        mimsUrl=self.dialog.addTemplatepro.mims[0].previewUrl;
                        duration=self.dialog.addTemplatepro.mims[0].duration;
                    }
                    var newData = {
                		templateId:p.tempId,
                		programeType:self.dialog.addTemplatepro.programeType,
                		startTime:self.dialog.addTemplatepro.startTime,
                        endTime:self.dialog.addTemplatepro.endTime,
                        mimsId:mimsId,
                        mimsName:mimsName,
                        url:mimsUrl,
                        duration:duration,
                        labelIds:'',
                        labelNames:labelNames.join(',')
                    };
                   
                    ajax.post('/cs/channel/template/pro/add', newData, function (data, status) {
                        self.dialog.addTemplatepro.loading = false;
                        if (status != 200) return;
                        if (self.templatepro.data.length < self.templatepro.page.size) {
                            self.templatepro.data.push(data);
                        }
                        self.templatepro.page.total += 1;
                        self.handleAddTemplateproClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                rowDelete: function (scope) {
                    var self = this;
                    var row = scope.row;
                    self.showTip('', '此操作将永久删除节目，是否继续?', function (callback) {
                        var questData = {
                            id: row.id
                        };
                        ajax.post('/cs/channel/template/pro/remove', questData, function (data, status) {
                            if (status != 200) return;
                            callback();
                            self.getTemplateproList();
                        }, null, ajax.NO_ERROR_CATCH_CODE);
                    });
                },
                handleSelectionChange: function (val) {
                    this.templatepro.multipleSelection = val;
                },
                handleTemplateproPageCurrentChange: function (val) {
                    var self = this;
                    self.templatepro.page.currentPage = val;
                    self.getTemplateproList();
                },
                handleTemplateproSizeChange: function (val) {
                    var self = this;
                    self.templatepro.page.size = val;
                    self.getTemplateproList();
                },
                showTip: function (title, text, confirmListener) {
                    var self = this;
                    var h = self.$createElement;
                    self.$msgbox({
                        title: title ? title : '危险操作',
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
                                confirmListener(function () {
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
                },
                handleMimsSet(){
                	var self = this;
                    self.dialog.addTemplatepro.dialog.chooseResource.visible = true;
                    self.dialog.addTemplatepro.dialog.chooseResource.tree.data.splice(0, self.dialog.addTemplatepro.dialog.chooseResource.tree.data.length);
                    var questData = {

                    };
                    ajax.post('/cs/channel/template/resource/get/mims', questData, function (data, status) {
                        if (status != 200) return;
                        if (data && data.length > 0) {
                            for (var i = 0; i < data.length; i++) {
                                self.dialog.addTemplatepro.dialog.chooseResource.tree.data.push(data[i]);
                            }
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                handleChooseResourceClose: function () {
                    var self = this;
                    self.dialog.addTemplatepro.dialog.chooseResource.chooseNode = [];
                    self.dialog.addTemplatepro.dialog.chooseResource.visible = false;
                },
                handleMenuResourceCheckChange: function (data, checked, indeterminate) {
                    var self = this;
                    self.dialog.addTemplatepro.dialog.chooseResource.chooseNode = this.$refs.menuResourceTree.getCheckedNodes(true, false);
                },
                handleChooseResourceClose: function () {
                    var self = this;
                    self.dialog.addTemplatepro.dialog.chooseResource.chooseNode = [];
                    self.dialog.addTemplatepro.dialog.chooseResource.visible = false;
                },
                handleChooseResourceCommit: function () {
                    var self = this;
                    self.dialog.addTemplatepro.mims.splice(0, self.dialog.addTemplatepro.mims.length);
                    for (var i = 0; i < self.dialog.addTemplatepro.dialog.chooseResource.chooseNode.length; i++) {
                        self.dialog.addTemplatepro.mims.push(self.dialog.addTemplatepro.dialog.chooseResource.chooseNode[i]);
                    }  
                    self.dialog.addTemplatepro.dialog.chooseResource.visible = false;

                },
                handleMimsRemove: function (list, value) {
                    var index = list.indexOf(value);
                    if (index != -1) {
                        list.splice(index, 1);
                    }
                },
                handleSetLabelSet: function () {
                    var self = this;
                    self.$refs.tagDialog.open('/media/tag/feign/list/get', self.dialog.addTemplatepro.dialog.labels);
                },
                selectedTags: function (buff, tags, startLoading, endLoading, close) {
                    var self = this;
                    startLoading();
                    var self = this;
                    self.dialog.addTemplatepro.labels.splice(0, self.dialog.addTemplatepro.labels.length);
                    for (var i = 0; i < tags.length; i++) {
                        self.dialog.addTemplatepro.labels.push(tags[i]);
                    }  

                    endLoading();
                    close();
                },
                handleLabelRemove: function (labels, value) {
                    for (var i = 0; i < labels.length; i++) {
                        if (labels[i] === value) {
                        	labels.splice(i, 1);
                            break;
                        }
                    }
                }
            },
            created: function () {
                var self = this;
                self.getTemplateproList();
            },
            mounted: function () {

            }
        });
    };

    var destroy = function () {

    };

    var groupList = {
        path: '/' + pageId+'/:tempId',
        component: {
            template: '<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init: init,
        destroy: destroy
    };

    return groupList;

});