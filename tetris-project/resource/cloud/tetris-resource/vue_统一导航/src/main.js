// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import app from './app'
import router from './router'
import ElementUI from 'element-ui'
import './router/util/queryRouter' // 这里进行路由后台获取
import 'element-ui/lib/theme-chalk/index.css'
import './common/Font-Awesome-3.2.1/css/font-awesome.min.css'
import './common/feather/style.css'
import echarts from 'echarts'
Vue.prototype.$echarts = echarts
Vue.config.productionTip = false
Vue.use(ElementUI)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: {
    app
  },
  template: '<app/>',
  mounted: function () {
    function getQueryVariable(name) {
      var params = window.location.href.split('?')[1];
      if (params) {
        params = params.split('&');
        for (var i = 0; i < params.length; i++) {
          if (params[i].startsWith(name)) {
            return params[i].split('=')[1];
          }
        }
      }
      return null;
    }
    var theme = getQueryVariable('params');
    if (theme && theme.indexOf('qt-terminal') >= 0) {
      var $head = document.querySelector('head');
      var $style = document.createElement('style');
      $style.type = 'text/css';
      var theme = [
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
  }
})
