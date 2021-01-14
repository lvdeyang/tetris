// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import './router/util/globalRouter' // 存储全局变量
import Antd from 'ant-design-vue'
import ElementUI from 'element-ui'  //只是菜单用这个
import 'ant-design-vue/dist/antd.css'
import './styles/css/frame/frame.css'
import 'element-ui/lib/theme-chalk/index.css'
import './styles/css/reset.css'
import './styles/lib/Font-Awesome-3.2.1/css/font-awesome.css'
import './styles/lib/feather/style.css'
import './router/util/queryRouter' // 这里进行路由后台获取
import store from './common/js/store'

Vue.config.productionTip = false

Vue.use(ElementUI)

Vue.use(Antd)
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
