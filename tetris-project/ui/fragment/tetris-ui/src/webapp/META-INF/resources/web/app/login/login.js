/**
 * Created by lvdeyang on 2021/3/25.
 */
$(function(){

    var hash = window.location.hash;
    if(hash){
        hash = hash.replace('#', '');
        hash = decodeURIComponent(decodeURIComponent(hash));
        hash = hash.split('&&');
        var message = hash[1];
        Vue.prototype.$message({
             type:'error',
             message:message
        });
    }

    var encodePassword = function(text){
        for(var i=0; i<5; i++){
            text = window.btoa(text);
        }
        return text;
    };

    var $password = $('#password');
    var $p = $('#p');

    $password.on('change', function(){
        $p.val(encodePassword($password.val()));
    });

});