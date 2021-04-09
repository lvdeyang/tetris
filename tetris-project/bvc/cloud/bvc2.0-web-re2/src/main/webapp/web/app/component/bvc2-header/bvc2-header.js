define([
    'text!' + window.APPPATH + 'component/bvc2-header/bvc2-header.html',
    'restfull',
    'vue',
    'element-ui'
], function(tpl, ajax, Vue){

    //组件名称
    var bvc2Header = 'bvc2-header';

    //进入个人中心
    var enterPersonalCenter = function(){
        console.log('访问权限');
        console.log(this);
        var ip = window.location.hostname;
        window.location.href = "http://" + ip + ":8885/vue/#/Enter";
    };

    //注销登录
    var doLogout = function(){
        console.log('注销登录');
        console.log(this);
        var ip = window.location.hostname;
        window.location.href = "http://" + ip + ":8885/vue/#/";
    };

    Vue.component(bvc2Header, {
        props: ['links', 'user'],
        template: tpl,
        data:function(){
            return {
                commands:[enterPersonalCenter, doLogout]
            }
        },
        methods:{
            handleCommand:function(command){
                this.commands[parseInt(command)].apply(this);
            }

        }
    });

    return Vue;
});