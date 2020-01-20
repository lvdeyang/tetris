/**
 * Created by lvdeyang on 2019/2/19 0019.
 */
require.config({
    paths: {
        'ace':window.LIBPATH + 'ace/src/ace',
        'ace-language-tools':window.LIBPATH + 'ace/src/ext-language_tools',
        'ace-beautify':window.LIBPATH + 'ace/src/ext-beautify',
        'ace-theme-idle_fingers':window.LIBPATH + 'ace/src/theme-idle_fingers',
        'ace-theme-xcode':window.LIBPATH + 'ace/src/theme-xcode',
        'ace-mode-sh':window.LIBPATH + 'ace/src/mode-sh'
    },
    shim:{
        'ace':{
            exports:'ace'
        },
        'ace-language-tools':{
            deps:['ace']
        },
        'ace-beautify':{
            deps:['ace']
        },
        'ace-theme-idle_fingers':{
            deps:['ace']
        },
        'ace-theme-xcode':{
            deps:['ace']
        },
        'ace-mode-sh':{
            deps:['ace']
        }
    }
});

define([
    'ace',
    'ace-theme-idle_fingers',
    'ace-theme-xcode',
    'ace-language-tools',
    'ace-beautify',
    'ace-mode-sh'
], function(ace){

    ace.require('ace/ext/language_tools');

    ace.require('ace/ext/beautify');

    return ace;
});