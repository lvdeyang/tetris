/**
 * Created by lvdeyang on 2020/4/16 0016.
 */
define(function(){

    var zhCN = {
        title:'基本配置',
        homePage:'首页',
        currentPage:'基本配置',
        details:'接口调用配置',
        labelAppId:'开发者id(appId)',
        detailAppId:'配合开发者密码调用平台接口。',
        labelAppSecret:'开发者密码(appSecret)',
        resetAppSecret:'重置',
        detailResetAppSecret:'平台不再提供开发者密码查询，如果遗忘，请重置。',
        dialogResetAppSecret:'修改开发者密码',
        labelResetAppSecretAppSecret:'开发者密码',
        labelResetAppSecretPassword:'用户密码',
        handleResetAppSecretCancel:'取消',
        handleResetAppSecretCommit:'确定',
        msgboxTitle:'重置开发者密码？',
        msgboxContent:'请注意：所有使用旧AppSecret的接口将立即失效，请尽快更新AppSecret信息。',
        msgboxConfirmButtonText:'确定',
        msgboxCancelButtonText:'取消',
        resetAppSecretMessageSuccess:'修改成功！'
    };

    return{
        'default':zhCN,
        'zh-CN':zhCN
    }
});