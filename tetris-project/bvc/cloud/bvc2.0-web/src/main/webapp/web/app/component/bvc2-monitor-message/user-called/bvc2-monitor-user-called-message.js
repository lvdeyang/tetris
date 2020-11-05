define([
    'text!' + window.APPPATH + 'component/bvc2-monitor-message/user-called/bvc2-monitor-user-called-message.html',
    'restfull',
    'jquery',
    'vue',
    'element-ui',
    'date',
    'css!' + window.APPPATH + 'component/bvc2-monitor-message/user-called/bvc2-monitor-user-called-message.css'
], function(tpl, ajax, $, Vue){

	//组件名称
    var pluginName = 'bvc2-monitor-user-called-message';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data:function(){
            return {
                visible:false,
                id:'',
                message:'',
                interval:'',
                loading:false
            }
        },
        computed:{

        },
        watch:{

        },
        methods:{
            loadMessage:function(){
                var self = this;
                ajax.post('/monitor/user/called/message/load', null, function(data, status){
                    if(status !== 200) return;
                    if(data){
                        self.id = data.id;
                        self.message = data.message;
                        self.visible = true;
                    }else{
                        self.id = '';
                        self.message = '';
                        self.visible = '';
                        self.loading = false;
                    }
                }, null, [403, 404, 408, 409, 500]);
            },
            accept:function(){
                var self = this;
                self.loading = true;
                ajax.post('/monitor/user/called/message/accept/' + self.id, null, function(data, status){
                    if(status !== 200){
                        self.loading = false;
                    }
                }, null, [403, 404, 408, 409, 500]);
            },
            refuse:function(){
                var self = this;
                self.loading = true;
                ajax.post('/monitor/user/called/message/refuse/' + self.id, null, function(data, status){
                    if(status !== 200) {
                        self.loading = false;
                    }
                }, null, [403, 404, 408, 409, 500]);
            }
        },
        mounted:function(){
            var self = this;
            self.interval = setInterval(self.loadMessage, 6000);
        }
    });

    return Vue;
});