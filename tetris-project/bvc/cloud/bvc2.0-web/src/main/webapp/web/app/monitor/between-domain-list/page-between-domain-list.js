define([
  'text!' + window.APPPATH + 'monitor/between-domain-list/page-between-domain-list.html',
  'restfull',
  'config',
  'commons',
  'vue',
  'element-ui',
  'bvc2-header',
  'bvc2-system-nav-side',
  'extral',
  'bvc2-monitor-forword-list'
], function (tpl, ajax, config, commons, Vue) {

  var pageId = 'page-between-domain-list';

  var init = function (p) {

    var $page = document.getElementById(pageId);
    $page.innerHTML = tpl;

    //设置标题
    commons.setTitle(pageId);

    new Vue({
      el: '#' + pageId + '-wrapper',
      data: {
        menurouter: false,
        shortCutsRoutes: commons.data,
        active: "/page-between-domain-list",
        header: commons.getHeader(0)
      },
      methods: {

      }

    });
  };

  var destroy = function () {

  };

  var page = {
    path: '/' + pageId,
    component: {
      template: '<div id="' + pageId + '" class="page-wrapper"></div>'
    },
    init: init,
    destroy: destroy
  };

  return page;
});