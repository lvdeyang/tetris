/**
 * Created by lvdeyang on 2020/8/11.
 */
define(function(){

    function getQueryVariable(name){
        var params = window.location.href.split('?')[1];
        if(params){
            params = params.split('&');
            for(var i=0; i<params.length; i++){
                if(params[i].startsWith(name)){
                    return params[i].split('=')[1];
                }
            }
        }
        return null;
    }
    var theme = getQueryVariable('params');
    if(theme && theme.indexOf('qt-terminal')>=0){
        var $head = document.querySelector('head');
        var $style = document.createElement('style');
        $style.type = 'text/css';
        var theme = [
            '.frame-wrapper .el-aside{background-color:#0f2A4a!important;}',
            '.header{background-color:#0f2A4a!important;}',
            'ul[role=menubar]{background-color:#0f2A4a!important;}',
            'li[role=menuitem]{background-color:#0f2A4a!important;}',
            'div.el-submenu__title{background-color:#0f2A4a!important;}',
            'li[role=menuitem],li[role=menuitem]>i,li[role=menuitem]>div,li[role=menuitem]>div>i{color:#fff!important;}',
            'li.is-active[role=menuitem],li.is-active[role=menuitem]>i,li.is-active[role=menuitem]>div,li.is-active[role=menuitem]>div>i{color:rgb(195,176,145)!important;}'
        ];
        $style.appendChild(document.createTextNode(theme.join(' ')));
        $head.appendChild($style);
    }

});