/**
 * Created by sms on 2020/3/26.
 */
define([
    'text!' + window.APPPATH + 'front/operation/statistic-strategy/page-operation-statistic-strategy.html',
    'config',
    'context',
    'commons',
    'restfull',
    'json',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'front/operation/statistic-strategy/page-operation-statistic-strategy.css'
], function(tpl, config, context, commons, ajax, $, Vue){

    var pageId = 'page-operation-statistic-strategy';

    var init = function(){

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
                loading:true,
                list: {
                    data: [],
                    current: {}
                },
                value: 0
            },
            methods:{
                load: function() {
                    var self = this;
                    self.loading = true;
                    self.list.data.splice(0, self.list.data.length);
                    self.list.current = {};
                    ajax.post('/operation/statistic/strategy/get/list', null, function(data, status) {
                        var defaultData = {
                            producer: 50,
                            operator: 50
                        };
                        if (status == 200) {
                            if (data && data.length > 0) {
                                for (var i = 0; i < data.length; i++) {
                                    self.list.data.push(data[i]);
                                }
                            } else {
                                self.list.data.push(defaultData);
                            }
                        }
                        self.handleCurrent(self.list.data[0]);
                        self.loading = false;
                    })
                },
                handleCurrent: function(row) {
                    var self = this;
                    self.list.current = row;
                    self.value = row.producer;
                },
                handleCreate: function() {
                    var self = this;
                },
                handleReset: function() {
                    var self = this;
                    self.value = self.list.current.producer;
                },
                handleCommit: function() {
                    var self = this;
                    self.loading = true;
                    var questData = {
                        id: self.list.current.hasOwnProperty('id') ? self.list.current.id : null,
                        producer: self.value,
                        operator: 100 - self.value
                    };
                    ajax.post('/operation/statistic/strategy/set', questData, function(data, status) {
                        if (status == 200) {
                            Vue.set(self.list.current, 'id', data.id);
                            self.list.current.producer = data.producer;
                            self.list.current.operator = data.operator;
                            self.$message({
                                message: '设置成功',
                                type: 'success'
                            });
                            return;
                        }
                        self.loading = false;
                    })
                }
            },
            created:function(){
                var self = this;
                self.load();
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