import router from '../../router'
import axios from 'axios'
import routerConfig from '../../../config/router.config.js'
import routers from '../../routes.js'

const _import = require('../../router/_import_' + process.env.NODE_ENV) // 获取组件的方法

// TODO
//let basePath = process.env.RESOURCE_ROOT
let requestIP = document.location.host.split(':')[0]

let basePath = "http://" + requestIP + ":8093";

let queryRouterUrl = routerConfig.queryRouterUrl
let useLocalRoute = process.env.USELOCALROUTE
let loginUrl = process.env.USER_ROOT + '/vue'

if (queryRouterUrl.indexOf('__requestIP__') !== -1) {
  queryRouterUrl = queryRouterUrl.replace('__requestIP__', requestIP)
}

if (loginUrl.indexOf('__requestIP__') !== -1) {
  loginUrl = loginUrl.replace('__requestIP__', requestIP)
}

// 获取后台拿到的菜单数据
var getRouter
// eslint-disable-next-line no-array-constructor
var getShortCutsRouter = new Array()

// 用下面这个可以访问springmvc Controller
let axiosInstance = axios.create({
  headers: {
    'Access-Control-Allow-Origin': '*',
    'Content-Type': 'application/x-www-form-urlencoded',
    'Access-Control-Allow-Headers': 'Content-Type, Content-Length, Authorization, Accept, X-Requested-With , yourHeaderFeild',
    'Access-Control-Allow-Methods': 'PUT,POST,GET,DELETE,OPTIONS'
  },
  withCredentials: false
})

/** 请求拦截器 */
axiosInstance.interceptors.request.use(function (config) {
  // alert('in= ' + sessionStorage.getItem('token'))
  // config.headers.Authorization = getQueryString('token')
  let token = sessionStorage.getItem('token')
  if (token) {
    config.headers['tetris-001'] = token
    config.headers.Authorization = token
  } else {
    config.headers['tetris-001'] = getQueryString('token')
    config.headers.Authorization = getQueryString('token')
  }
  return config
}, function (err) {
  // alert('in err')

  return Promise.reject(err)
})

/* 响应拦截器 */
axiosInstance.interceptors.response.use(
  function (response) {
    // alert('in 2')
    return response
  },
  function (error) {
    // 认证出错,跳转至登录页面
    console.log('error= ' + error)
    if(error.response.status === 401){
      alert('超时，请重新登录!');
      window.location.href = `${basePath}/web/app/login/login.html`;
    }
  })

router.beforeEach((to, from, next) => {
  // 留给登陆入口
  if (to.path === '/') {
      next()
    return
  }

  // 不加这个判断，路由会陷入死循环
  if (!getRouter) {
    // 如果要访问的URL里带有token参数，则先更新到sessionStorage
    let tokenInUrl = getQueryString('token')
    let tokenInSession = sessionStorage.getItem('token')
    if (tokenInUrl && tokenInUrl !== tokenInSession) {
      sessionStorage.setItem('token', tokenInUrl)
    }

    //    axiosInstance.post(routerConfig.queryRouterUrl).then(res => {
    axiosInstance.post(queryRouterUrl).then(res => {
      if(res.data.status === 408){
        alert('超时，请重新登录!');
        window.location.href = `${basePath}/web/app/login/login.html`;
      }else if(res.data.status === 200){
        getRouter = res.data.vueRouters// 后台拿到路由
        // console.log('getRouter from uer service== ' + JSON.stringify(getRouter))
        saveObjArr('router', getRouter) // 存储路由到localStorage
        routerGo(to, next)// 执行路由跳转方法
      }
    })
  } else {
    // console.log('to.meta.serviceName=' + to.meta.serviceName + ' ,routerConfig.serviceName==' + routerConfig.serviceName)
    if (to.meta.serviceName && to.meta.serviceName !== routerConfig.serviceName) {
      // console.log('href to==' + to.meta.url + '?token=' + sessionStorage.getItem('token') + to.meta.urlSuffix)
      var tempUrl

      if (useLocalRoute) {
        // TODO
        var tempUrlPart1 = to.meta.url.split(':')[0] + '://'
        tempUrl = tempUrlPart1 + requestIP + ':' + to.meta.url.split(':')[2]
        // console.log('tempUrlPart1=' + tempUrlPart1 + ' ,tempUrl=' + tempUrl)
      } else {
        tempUrl = to.meta.url
      }

      //
      if (to.meta.urlSuffix && to.meta.urlSuffix !== '' && to.meta.urlSuffix !== null) {
        window.location.href = tempUrl + '?token=' + sessionStorage.getItem('token') + to.meta.urlSuffix
      } else {
        window.location.href = tempUrl + '?token=' + sessionStorage.getItem('token')
      }
    } else {
      next()
    }
  }
})

function routerGo (to, next) {
  //getRouter = filterAsyncRouter(getRouter) // 过滤路由
  router.addRoutes(routers) // 动态添加路由
  parseUrlTemplate(getRouter);
  generatePath(getRouter);
  global.antRouter = getRouter // 将路由数据传递给全局变量，做侧边栏菜单渲染工作
  //global.shortCutsRouter = getShortCutsRouter // 抬头栏快捷方式数组
  // console.log('getRouter == ' + JSON.stringify(getRouter))
  next({ ...to, replace: true })
  //next();
}

function saveObjArr (name, data) {
  localStorage.setItem(name, JSON.stringify(data))
}

// eslint-disable-next-line no-unused-vars
function getObjArr (name) {
  // console.log('session=' + window.localStorage.getItem(name))
  if (!localStorage.getItem(name)) {
    return null
  }
  return JSON.parse(localStorage.getItem(name))
}

function filterAsyncRouter (asyncRouterMap) { // 遍历后台传来的路由字符串，转换为组件对象
  const accessedRouters = asyncRouterMap.filter(route => {
    if (route.component) {
      if (route.meta && route.meta.serviceName && route.meta.serviceName === routerConfig.serviceName) {
        if (route.meta.level === 2 && route.children && route.children.length > 0) {
          route.component = _import('middleLayer')
        } else {
          route.component = _import(route.component)
        }
      } else {
        route.component = _import('Home')
        route.path = '/outLink' + route.path
      }
    }

    if (route.children && route.children.length > 0) {
      route.children = filterAsyncRouter(route.children)
    }

    if (route.meta.shortCut) {
      getShortCutsRouter.push(route)
    }
    return true
  })

  return accessedRouters
}

function getQueryString (name) {
  try {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i')
    var queryStr = window.location.href.split('?')[1]
    var r = queryStr.match(reg)
    if (r != null) {
      return unescape(r[2])
    }
  } catch (err) {
  }
  return null
}

//解析模板url并设置值
function setValue (url){
  while(true){
    var matched = url.match(/(?=\{context:)/);
    if(!matched) break;
    var end = 0;
    var placeholder = '';
    for(var i=matched.index; i<url.length; i++){
      placeholder += url[i]
      if(url[i] === '}'){
        end = i;
        break;
      }
    }
    var items = placeholder.replace('{context:', '')
      .replace('}', '')
      .split(';');
    var value = null;
    value = sessionStorage.getItem('token');
    url = url.replace(new RegExp(placeholder,'g'), value);
  }
  return url;
}

//解析菜单链接模板
function parseUrlTemplate (menus){
  if(menus && menus.length>0){
    for(var i=0; i<menus.length; i++){
      var menu = menus[i];
      //插入变量
      if(menu.link){
        menu.link = setValue(menu.link);
      }
      if(menu.sub && menu.sub.length>0){
        parseUrlTemplate(menu.sub);
      }
    }
  }
}

//生成path，用于active
function generatePath(menus){
  if(menus && menus.length>0){
    for(var i=0; i<menus.length; i++){
      var menu = menus[i];
      menu.path = menu.title;
      if(menu.link){
        if(menu.link.indexOf("?token") != -1){
          menu.path = menu.link.split("#")[1].split("?")[0];
        }
      }
      if(menu.sub && menu.sub.length>0){
        generatePath(menu.sub);
      }
    }
  }
}
