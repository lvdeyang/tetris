/**
 * Created by Administrator on 2019/2/25 0025.
 */
define([
    'text!' + window.APPPATH + 'cms/article/layout-editor.html',
    'restfull',
    'jquery',
    'juicer',
    'vue',
    'element-ui',
    'css!' + window.APPPATH + 'cms/article/layout-editor.css'
], function(tpl, ajax, $, juicer, Vue){

    var pluginName = 'article-layout-editor';

    Vue.component(pluginName, {
        props:['id'],
        template: tpl,
        data:function(){
            return {
                visible:false,
                article:'',
                templates:[],
                modules:[],
                loading:{
                    save:false
                },
                drag:{
                    ongoing:false,
                    index:0,
                    precise:5,
                    y:0,
                    template:''
                }
            }
        },
        methods:{
            show:function(article){
                var self = this;
                self.visible = true;
                self.article = article;
                ajax.post('/cms/article/query/modules/' + article.id, null, function(data){
                    if(data && data.length>0){
                        for(var i=0; i<data.length; i++){
                            var json = self.translateJSON(data[i].template.js);
                            data[i].render = juicer(data[i].template.html).render(json);
                            self.modules.push(data[i]);
                        }
                    }
                });
            },
            hide:function(){
                var self = this;
                self.visible = false;
                self.article = '';
                self.modules.splice(0, self.modules.length);
            },
            save:function(){
                var self = this;
                var html = '';
                var c_modules = [];
                for(var i=0; i<self.modules.length; i++){
                    html += self.modules[i].render;
                    var c_module = $.extend(true, {}, self.modules[i]);
                    c_module.render = null;
                    c_modules.push(c_module);
                }
                var modules = $.toJSON(c_modules);
                var endLoading = function(){
                    self.loading.save = false;
                };
                self.loading.save = true;
                self.$emit('save-article', self.article, html, modules, endLoading);
            },
            templateDragstart:function(template, e){
                var self = this;
                self.drag.y = e.clientY;
                self.drag.ongoing = true;
                self.$nextTick(function(){
                    var $el = $(self.$el);
                    $el.find('.article-modules .el-scrollbar__view').append($el.find('.drop-area'));
                    self.updateDragIndex();
                });
                self.drag.template = template
            },
            templateDrop:function(e){
                var self = this;
                var template = self.drag.template;
                if(!template.html || !template.js){
                    ajax.post('/cms/template/content/' + template.id, null, function(data){
                        var html = data.html;
                        var js = data.js;
                        template.html = html;
                        template.js = js;
                        var json = self.translateJSON(template.js);
                        var module = {
                            id:'module_'+new Date().getTime(),
                            template: $.extend(true, {}, template),
                            render:juicer(template.html).render(json)
                        };
                        self.modules.splice(self.drag.index, 0, module);
                    });
                }else{
                    var json = self.translateJSON(template.js);
                    var module = {
                        id:'module_'+new Date().getTime(),
                        template:template,
                        render:juicer(template.html).render(json)
                    };
                    self.modules.splice(self.drag.index, 0, module);
                }
            },
            translateJSON:function(js){
                var json = {};
                if(js){
                    var variables = $.parseJSON(js);
                    if(variables && variables.length>0){
                        for(var i=0; i<variables.length; i++){
                            var variable = variables[i];
                            if(variable.editor === 'ARRAY_IMAGE'){
                                json[variable.key] = variable.value;
                            }else if(variable.editor === 'ARRAY_SIMPLE'){
                                json[variable.key] = variable.value;
                            }else if(variable.editor === 'ARRAY_OBJECT'){

                            }else{
                                json[variable.key] = variable.value;
                            }
                        }
                    }
                }
                return json;
            },
            updateDragIndex:function(){
                var self = this;
                var $el = $(self.$el);
                var $modules = $el.find('.article-modules .el-scrollbar__view');
                var index = 0;
                $modules.children().each(function(){
                    var $this = $(this);
                    if($this.is('.drop-area')){
                        return false;
                    }
                    index += 1;
                });
                self.drag.index = index;
            },
            moduleDragOver:function(e){
                var self = this;
                var $dropArea = $(self.$el).find('.drop-area');
                var $target = $(e.target);
                if(!$target.is('.article-module')){
                    $target = $target.closest('.article-module');
                }
                if((e.clientY-self.drag.y) > self.drag.precise){
                    //往下拽
                    $target.after($dropArea);
                    self.updateDragIndex();
                }else if((self.drag.y-e.clientY) > self.drag.precise){
                    //往上拽
                    $target.before($dropArea);
                    self.updateDragIndex();
                }
                self.drag.y = e.clientY;
            },
            allowDrop:function(e){
                e.preventDefault();
            },
            dragEnd:function(e){
                var self = this;
                self.drag.ongoing = false;
                self.drag.template = '';
            }
        },
        created:function(){
            var self = this;
            ajax.post('/cms/template/query/article/templates', null, function(data){
                self.templates.splice(0, self.templates.length);
                if(data && data.length>0){
                    for(var i=0; i<data.length; i++){
                        self.templates.push(data[i]);
                    }
                }
            });
        },
        mounted:function(){

        }
    });

});