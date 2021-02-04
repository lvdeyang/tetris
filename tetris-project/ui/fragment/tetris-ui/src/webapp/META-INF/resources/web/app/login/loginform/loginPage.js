(function(){
    var request = new XMLHttpRequest();
    if(window.environment === 'dev'){
        request.open( 'GET', 'loginform/loginPage.html' );
    }else if(window.environment === 'prod'){
        request.open( 'GET', '/web/app/login/loginform/loginPage.html' );
    }
    request.onreadystatechange = function(){
        if( request.readyState !== 4 ) return;
        if( request.status === 200 ){
            var tpl = request.responseText;
            Vue.component('login-button', {
                data:function(){
                    return {
                        mode:'password',
                        passwordInput:'',
                        passwordEncode:''
                    }
                },
                template:tpl,
                computed:{

                },
                watch:{
                    passwordInput:function(){
                        var self = this;
                        self.passwordEncode = self.encodePassword(self.passwordInput);
                    }
                },
                methods:{
                    encodePassword:function(text){
                        for(var i=0; i<5; i++){
                            text = window.btoa(text);
                        }
                        return text;
                    },
                    passwordModeTabStyle:function(){
                        var self = this;
                        if(self.mode === 'password'){
                            return 'border-bottom:2px solid #2f82ff; display:inline; float:left; padding-left:2px; padding-right:2px; line-height:44px; font-size:16px; color:#1b2f4d;';
                        }else{
                            return 'display:inline; float:left; padding-left:2px; padding-right:2px; line-height:44px; font-size:16px; color:#afbcd3;';
                        }
                    },
                    tabClick:function(mode){
                        var self = this;
                        self.mode = mode;
                    },
                    doPasswordLogin:function(e){
                        console.log(e.target);
                    },
                    doPhoneLogin:function(e){

                    }
                },
                created:function(){
                    var self = this;
                    var hash = window.location.hash;
                    if(hash){
                        hash = hash.replace('#', '');
                        hash = decodeURIComponent(decodeURIComponent(hash));
                        hash = hash.split('&&');
                        var message = hash[1];
                        self.$message({
                            type:'error',
                            message:message
                        });
                    }
                }
            })
            new Vue({
                el: '#app'
            })
        }
    };
    request.send();
})();