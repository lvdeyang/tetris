define([
    'text!' + window.APPPATH + 'component/zk/footer/dialog/save-scheme/zk-footer-dialog-save-scheme.html',
    'restfull',
    'jquery',
    'vue',
    'element-ui',
    'css!' + window.APPPATH + 'component/zk/footer/dialog/save-scheme/zk-footer-dialog-save-scheme.css'
], function(tpl, ajax, $, Vue){

	//组件名称
    var pluginName = 'zk-footer-dialog-save-scheme';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data:function(){
            return {
            	baseUrl:window.BASEPATH,
                schemeName:''
            }
        },
        computed:{

        },
        watch:{

        },
        methods:{
            saveScheme:function(){
                var self = this;
                if(self.schemeName){

                }else{
                    alert('名称不能为空！');
                }
            }
        },
        mounted:function(){

        }
    });

    return Vue;
});