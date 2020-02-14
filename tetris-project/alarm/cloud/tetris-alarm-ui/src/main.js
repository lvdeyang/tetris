/* eslint-disable no-new */
// import './common/init'

import Vue from 'vue'
import app from './app'
// import store from './api/store'
import router from './router'
import global from './router/util/globalRouter' // 存储全局变量

import 'element-ui/lib/theme-chalk/index.css'
import './styles/css/reset.css'
import './styles/lib/Font-Awesome-3.2.1/css/font-awesome.css'
import './styles/lib/feather/style.css'
import './styles/css/frame/frame.css'

import ElementUI from 'element-ui'

import './router/util/queryRouter' // 这里进行路由后台获取

Vue.config.productionTip = false
Vue.use(ElementUI)

new Vue({
  el: '#app',
  router,
  components: { app },
  template: '<app/>'

})
