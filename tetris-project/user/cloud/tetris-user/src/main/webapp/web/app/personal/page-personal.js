/**
 * Created by sms on 2019/8/8.
 */
define([
    'text!' + window.APPPATH + 'personal/page-personal.html',
    window.APPPATH + 'personal/page-personal.i18n',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'mi-tag-dialog',
    'css!' + window.APPPATH + 'personal/page-personal.css'
], function (tpl, i18n, config, ajax, context, commons, Vue) {

    var locale = context.getProp('locale');
    var i18n = !locale?i18n.default:i18n[locale]?i18n[locale]:i18n.default;

    var pageId = 'page-personal';

    var init = function () {

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
                i18n:i18n,
                loading: false,
                tab: {
                    current: "个人信息"
                },
                userInfo: {},
                tagsInfo: {},
                password: {
                    oldPassword: "",
                    newPassword: "",
                    repeat: ""
                },
                dialog:{
                	editTag:{
                		visible:false,
                		hotCount:0,
                		tagName:''
                	}
                }
            },
            computed: {},
            watch: {},
            methods: {
                load: function () {
                    var self = this;
                    ajax.post('/user/query', null, function (data, status) {
                        if (status == 200) {
                            self.userInfo = data;
                            self.user.mobile = data.mobile;
                            self.user.mail = data.mail;
                            self.user.tags = data.tags;
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE)
                    
                    ajax.post('/user/querytags', null, function (data, status) {
                        if (status == 200) {
                            self.tagsInfo = data;
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE)
                },
                handleTabClick: function (tab, event) {
                    var self = this;
                    self.tab.current = tab.label;
                },
                handleTagEdit: function () {
                    var self = this;
                    var tags=[];
                    for(var i=0;i<self.tagsInfo.length;i++){
                    	tags.push(self.tagsInfo[i].tagName);
                    }
                    self.$refs.tagDialog.open('/media/tag/feign/list/get', tags);
                },
                selectedTags: function (buff, tags, startLoading, endLoading, close) {
                    var self = this;
                    startLoading();
                    var tempCountMap={};
                    for(var i=0;i<self.tagsInfo.length;i++){
                    	tempCountMap[self.tagsInfo[i].tagName]=self.tagsInfo[i].hotCount;
                    }
                    self.userInfo.tags.splice(0, self.userInfo.tags.length);
                    var tagNames=[];
                    var hotCounts=[];
                    for (var i = 0; i < tags.length; i++) {
                    	self.userInfo.tags.push(tags[i].name);
                    	tagNames.push(tags[i].name);
                    	if(tempCountMap[tags[i].name]){
                    		hotCounts.push(tempCountMap[tags[i].name]);
                    	}else{
                    		hotCounts.push(0);
                    	}
                    	
                    }
                    //保存标签到用户标签关联表
      
                    var params={
                    	tags:tagNames.join(','),
                    	hotCounts:hotCounts.join(',')
                    }
                    
                    ajax.post('/user/editTags', params, function (data, status) {
                        if (status == 200) {
                        	self.tagsInfo.splice(0, self.tagsInfo.length);
                        	for(var j=0;j<tags.length;j++){
                            	var tempTag={};
                            	tempTag.tagName=tags[j].name;
                            	if(tempCountMap[tempTag.tagName]){
                            		tempTag.hotCount=tempCountMap[tempTag.tagName];
                            	}else{
                            		tempTag.hotCount=0;
                            	}
                            	self.tagsInfo.push(tempTag);
                            }
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE)
                    endLoading();
                    close();
                },
                handleTagRemove: function (value) {
                	var self = this;
                	for (var i = 0; i < self.userInfo.tags.length; i++) {
                        if (self.userInfo.tags[i] === value.tagName) {
                        	self.userInfo.tags.splice(i, 1);
                            break;
                        }
                    }
                    for (var j = 0; j < self.tagsInfo.length; j++) {
                        if (self.tagsInfo[j].tagName === value.tagName) {
                        	self.tagsInfo.splice(j, 1);
                            break;
                        }
                    }
                    var params={
                        tags:self.userInfo.tags.join(','),
                    }
                    ajax.post('/user/editTags', params, function (data, status) {
                        if (status == 200) {
                        	
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE)
                },
                handleModifyHotCount:function(tag){
                	var self=this;
                	self.dialog.editTag.visible = true;
                	self.dialog.editTag.hotCount=tag.hotCount;
                	self.dialog.editTag.tagName=tag.tagName;
                },
                saveTag:function(){
                	var self=this;
                	var params={
                        tagName:self.dialog.editTag.tagName,
                        hotCount:self.dialog.editTag.hotCount
                    }
                    ajax.post('/user/editTagHotCount', params, function (data, status) {
                        if (status == 200) {
                        	for (var j = 0; j < self.tagsInfo.length; j++) {
                        		for(var i=0;i<data.length;i++){
                        			if (self.tagsInfo[j].tagName === data[i].tagName) {
                                    	self.tagsInfo[j].hotCount=data[i].hotCount;
                                    }
                        		}   
                            }
                        	self.dialog.editTag.visible = false;
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE)
                },
                handleResetInfo: function () {
                    var self = this;
                    self.load();
                },
                handleEditInfo: function () {
                    var self = this;
                    self.loading = true;
                    var questData = {
                        nickname: self.userInfo.nickname,
                        mobile: self.userInfo.mobile,
                        mail: self.userInfo.mail,
                        tags: (self.userInfo.tags.length > 0) ? self.userInfo.tags.join(',') : "",
                        editPassword: false
                    };
                    ajax.post('/user/edit/' + self.userInfo.id, questData, function(data, status){
                        if (status == 200){
                            self.userInfo = data;
                            self.user.nickname = data.nickname;
                            self.user.mobile = data.mobile;
                            self.user.mail = data.mail;
                            self.user.tags = data.tags;
                        }
                        self.loading = false;
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                handleClearPassword: function () {
                    var self = this;
                    self.password.oldPassword = "";
                    self.password.newPassword = "";
                    self.password.repeat = "";
                },
                handleEditPassword: function () {
                    var self = this;
                    self.loading = true;
                    var questData = {
                        nickname: self.user.nickname,
                        mobile: self.user.mobile,
                        mail: self.user.mail,
                        tags: (self.user.tags.length > 0) ? self.user.tags.join(',') : "",
                        editPassword: true,
                        oldPassword: self.password.oldPassword,
                        newPassword: self.password.newPassword,
                        repeat: self.password.repeat
                    };
                    ajax.post('/user/edit/' + self.user.id, questData, function(data, status, message){
                        if (status == 200){
                            self.$message({
                                message: "修改成功",
                                type: 'success'
                            });
                            self.handleClearPassword();
                        }else{
                            self.$message({
                                message: message,
                                type: 'warning'
                            });
                        }
                        self.loading = false;
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                }
            },
            created: function () {
                var self = this;
                self.load();
            }
        });

    };

    var destroy = function () {

    };

    var groupList = {
        path: '/' + pageId,
        component: {
            template: '<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init: init,
        destroy: destroy
    };

    return groupList;

});