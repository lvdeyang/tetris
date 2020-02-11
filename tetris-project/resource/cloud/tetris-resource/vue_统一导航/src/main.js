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

Vue.config.productionTip = false
Vue.use(ElementUI)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { app },
  template: '<app/>'
})
