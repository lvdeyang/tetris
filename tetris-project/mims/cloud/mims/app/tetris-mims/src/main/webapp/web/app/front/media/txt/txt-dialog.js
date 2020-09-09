/**
 * Created by sms on 2019/7/30.
 */
define([
    'text!' + window.APPPATH + 'front/media/txt/txt-dialog.html',
    'restfull',
    'vue',
    'element-ui',
    'mi-addition-dialog',
    'css!' + window.APPPATH + 'front/media/txt/txt-dialog.css',
], function(tpl, ajax, Vue){

    var pluginName = 'mi-txt-dialog';

    Vue.component(pluginName, {
        template: tpl,
        data:function(){
            return {
                dialog:{
                    visible:false,
                    loading:false
                },
                breadCrumb:[],
                data:[],
                current:''
            }
        },
        methods:{
            //对话框关闭时初始化数据
            closed:function(){
                var self = this;
                self.data.splice(0, self.data.length);
                self.breadCrumb.splice(0, self.breadCrumb.length);
                self.current = '';
                self.dialog.visible = false;
            },
            //打开文件夹对话框
            open:function(){
                var self = this;
                self.load(0);
                self.dialog.visible = true;
            },
            load:function(folderId){
                var self = this;
                self.dialog.loading = true;
                ajax.post('/media/txt/feign/load', {
                    folderId:folderId
                }, function(data, status){
                    setTimeout(function(){
                        self.dialog.loading = false;
                        self.data.splice(0, self.data.length);
                        self.breadCrumb.splice(0, self.breadCrumb.length);
                        self.current = '';
                        if(status !== 200) return;
                        var rows = data.rows;
                        var breadCrumb = data.breadCrumb;
                        if(rows && rows.length>0){
                            for(var i=0; i<rows.length; i++){
                                rows[i].__select = false;
                                self.data.push(rows[i]);
                            }
                        }
                        var items = [breadCrumb];
                        var current = breadCrumb;
                        while(current.next){
                            current = current.next;
                            items.push(current);
                        }
                        for(var i=0; i<items.length; i++){
                            self.breadCrumb.push(items[i]);
                        }
                    }, 500);
                }, null, ajax.NO_ERROR_CATCH_CODE);
            },
            //文件夹选中
            handleOkButton:function(){
                var self = this;
                self.$emit('selected-txt', self.current, function(){
                    self.dialog.loading = true;
                }, function(){
                    self.dialog.loading = false;
                }, function(){
                    self.dialog.visible = false;
                });
            },
            txtItemClass:function(item, index){
                var c = 'txt-item';
                if(item.__select){
                    c += ' select'
                }
                if((index+1) % 9 === 0){
                    c += ' last';
                }
                return c;
            },
            txtSelect:function(txt){
                var self = this;
                for(var i=0; i<self.data.length; i++){
                    if(self.data[i].id === txt.id){
                        self.data[i].__select = true;
                        self.current = self.data[i];
                    }else{
                        self.data[i].__select = false;
                    }
                }
            }
        }
    });

});