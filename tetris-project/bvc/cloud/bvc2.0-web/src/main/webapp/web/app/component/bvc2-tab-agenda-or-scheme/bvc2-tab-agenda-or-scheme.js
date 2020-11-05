define([
    'text!' + window.APPPATH + 'component/bvc2-tab-agenda-or-scheme/bvc2-tab-agenda-or-scheme.html',
    'restfull',
    'jquery',
    'vue',
    'element-ui',
    'bvc2-table-agenda'
], function(tpl, ajax, $, Vue){

    //组件名称
    var bvc2TabAgendaOrScheme = 'bvc2-tab-agenda-or-scheme';

    //记录最后一个查看的议程，用于重新加载
    var lastRow = null;

    Vue.component(bvc2TabAgendaOrScheme, {
        props: ['group'],
        template: tpl,
        data:function(){
            return {
                currentTab:'agenda'
            }
        },
        methods:{
            reConfigAgenda:function(){
                if(!lastRow) return;
                var row_copy = $.extend(true, {}, lastRow);
                this.$emit('config-video', row_copy, 'agenda');
            },
            configAgenda:function(row){
                lastRow = $.extend(true, {}, row);
                var row_copy = $.extend(true, {}, row);
                this.$emit('config-video', row_copy, 'agenda');
            },
            configScheme:function(row){
                var row_copy = $.extend(true, {}, row);
                this.$emit('config-video', row_copy, 'scheme');
            },
            runAgenda:function(row, done){
                var row_copy = $.extend(true, {}, row);
                this.$emit('run-agenda', row_copy, done);
            },
            removeAgenda:function(row){
                var row_copy = $.extend(true, {}, row);
                this.$emit('remove-config', row_copy, 'agenda');
            },
            removeScheme:function(row){
                var row_copy = $.extend(true, {}, row);
                this.$emit('remove-config', row_copy, 'scheme');
            }
        }
    });

});
