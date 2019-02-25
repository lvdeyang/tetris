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
                templates:[],
                modules:[],
                loading:{
                    save:false
                },
                drag:{
                    ongoing:false,
                    y:0,
                    template:''
                }
            }
        },
        methods:{
            show:function(){
                var self = this;
                self.visible = true;
            },
            hide:function(){
                var self = this;
                self.visible = false;
            },
            save:function(){
                var self = this;
            },
            templateDragstart:function(template, e){
                var self = this;
                self.drag.y = e.clientY;
                self.drag.ongoing = true;
                self.$nextTick(function(){
                    var $el = $(self.$el);
                    $el.find('.article-modules').append($el.find('.drop-area'));
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
                        var json = {};
                        if(template.js){
                            var variables = $.parseJSON(template.js);
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
                        var module = {
                            template:template,
                            render:juicer(template.html).render(json)
                        };
                        self.modules.push(module);
                    });
                }

            },
            moduleDragOver:function(e){
                var self = this;
                var $dropArea = $(self.$el).find('.drop-area');
                var $target = $(e.target);
                if(e.clientY > self.drag.y){
                    //往下拽
                    $target.after($dropArea);
                }else{
                    //往上拽
                    $target.before($dropArea);
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