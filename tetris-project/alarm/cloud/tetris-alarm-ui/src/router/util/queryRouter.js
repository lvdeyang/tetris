import router from '../../router'
import axios from 'axios'
import context from '../../common/js/context.js'

import routerData from '../../router/routerData.json'

const _import = require('../../router/_import_' + process.env.NODE_ENV) // 获取组件的方法

// TODO
let requestIP = document.location.host.split(':')[0]


let basePath
let useLocalRoute = process.env.USELOCALROUTE
let showMenuHeader = process.env.SHOWMENUHEADER

if (useLocalRoute === true) {
  basePath = document.location.origin
} else {
  basePath = process.env.ALARM_ROOT
}

let queryRouterUrl = basePath + '/prepare/app'
// let useLocalRoute = process.env.USELOCALROUTE
let loginUrl = basePath + '/web/app/login/login.html'
let errorUrl = basePath + '/web/app/error/request-fail.html'

console.log('queryRouterUrl=' + queryRouterUrl)


// 获取后台拿到的菜单数据
var getRouter
// eslint-disable-next-line no-array-constructor
// var getShortCutsRouter = new Array()

// 用下面这个可以访问springmvc Controller
let axiosInstance = axios.create({
  // headers: {
  // 'Access-Control-Allow-Origin': '*',
  // 'Content-Type': 'application/x-www-form-urlencoded',
  // 'Access-Control-Allow-Headers': 'Content-Type, Content-Length, Authorization, Accept, X-Requested-With , yourHeaderFeild',
  // 'Access-Control-Allow-Methods': 'PUT,POST,GET,DELETE,OPTIONS'
  // }
  // withCredentials: true
})

/** 请求拦截器 */
axiosInstance.interceptors.request.use(function (config) {
  let token = localStorage.getItem('tetris-001')
  // console.log('before,token=')

  if (token) {
    config.headers.common['tetris-001'] = token
  } else {
    config.headers.common['tetris-001'] = getTokenFromUrl('token')
  }
  return config
}, function (err) {
  // alert('in err')
  return Promise.reject(err)
})

/* 响应拦截器 */
axiosInstance.interceptors.response.use(
  function (response) {
    return response
  },
  function (error) {
    // 认证出错,跳转至登录页面
    // console.log('error= ' + error)
    if (error.response.status === 401) {
      window.location.href = loginUrl
    } else if (error.response.status !== 200) {
      window.location.href = errorUrl
    }
  })

// 不加这个判断，路由会陷入死循环
if (!getRouter) {
  // 如果要访问的URL里带有token参数，则先更新到sessionStorage
  let tokenInUrl = getTokenFromUrl('token')
  let tokenInSession = localStorage.getItem('tetris-001')
  let newTokenInUrl = tokenInUrl.substring(0, tokenInUrl.length - 1)
  if (newTokenInUrl && newTokenInUrl !== tokenInSession && tokenInUrl.endsWith('#')) {
    localStorage.setItem('tetris-001', newTokenInUrl)
  }

  axiosInstance.get(queryRouterUrl).then(res => {
    if (res.data.status === 408) {
      window.location.href = loginUrl
    } else if (res.data.status === 200) {
      getRouter = res.data.data.menus || []

      // 解析模板
      parseUrlTemplate(getRouter)

      // 初始化上下文环境（暂时不用）
      // context.setProp('app', app)
      //  .setProp('router', router)
      //  .setProp('user', appInfo.user)
      //  .setProp('groups', appInfo.groups || [])
      //  .setProp('token', window.TOKEN)
      global.antRouter = getRouter
      global.user = res.data.data.user
      global.groups = res.data.data.groups || []
      global.loginUrl = loginUrl
      // global.alarmroot = process.env.ALARM_ROOT

      // console.log('global.menus=' + JSON.stringify(global.antRouter))

      router.addRoutes(assembleVueRouter(routerData))

      // 获取活动页
      var activeMenu = getActiveMenu(getRouter)

      // console.log('activeMenu=' + JSON.stringify(activeMenu))

      // 重置首页
      if (activeMenu) {
        // config.redirect.home = activeMenu.link.split('#/')[1]

        // var testhome = activeMenu.link.split('#/')[1]
        // console.log('config.redirect.home=' + JSON.stringify(testhome))
      }

      // var homeUrl = window.location.href.split('#/')[1]
      var tempUrl = window.location.href
      var homeUrl
      // 结尾是#号表示url带token
      if (tempUrl.endsWith('#')) {
        homeUrl = tempUrl.substring(tempUrl.indexOf('#') + 1, tempUrl.lastIndexOf('\/'))
      } else {
        homeUrl = tempUrl.substring(tempUrl.indexOf('#') + 1, tempUrl.length)
      }

      // console.log('homeUrl=' + homeUrl)

      // 跳转首页
      // if (!window.location.hash || window.location.hash === '#/') {
      // router.push('/' + config.redirect.home)

      // console.log('router.push')
      // }

      // console.log('window.location.hash=' + window.location.hash)

      // this.$router.push('/' + 'BundleManage')

      if (!homeUrl.startsWith('/')) {
        homeUrl = '/' + homeUrl
      }

      router.push(homeUrl)
    }
  })
}

function assembleVueRouter (routerData) {
  const accessedRouters = routerData.filter(route => {
    global.selfRouterPath.push(route.path)

    if (route.component) {
      // if (route.children && route.children.length > 0) {
      //  route.component = _import('Home')
      // } else {
      route.component = _import(route.component)
      // }
    }

    if (route.children && route.children.length > 0) {
      route.children = assembleVueRouter(route.children)
    }

    return true
  })

  return accessedRouters
}

// 从path中解析pageId
function parsePageId (path) {
  var pageId = path.split('/')[1]
  return pageId
}

// 判断页面是否已经被加载
function isLoaded (pageId) {
  for (var i = 0; i < global.loadHistory.length; i++) {
    if (global.loadHistory[i] === pageId) {
      return true
    }
  }
  return false
};

// eslint-disable-next-line no-unused-vars
function getObjArr (name) {
  // console.log('session=' + window.localStorage.getItem(name))
  if (!localStorage.getItem(name)) {
    return null
  }
  return JSON.parse(localStorage.getItem(name))
}

function getTokenFromUrl (name) {
  try {
    var tempStr = window.location.href.split('/')
    var tokenStr = tempStr[tempStr.length - 1]
    // console.log('tokenStr=' + tokenStr)

    if (tokenStr != null) {
      return unescape(tokenStr)
    }
  } catch (err) {
    console.log('err=' + JSON.stringify(err))
  }

  return null

  // return '3cd775f6677d42e6af55723276df8bc4'
}

function parseUrlTemplate (menus) {
  if (menus && menus.length > 0) {
    for (var i = 0; i < menus.length; i++) {
      var menu = menus[i]
      // �������
      if (menu.link) {
        menu.link = setValue(menu.link)
      }
      if (menu.sub && menu.sub.length > 0) {
        parseUrlTemplate(menu.sub)
      }
    }
  }
}

function setValue (url) {
  while (true) {
    var matched = url.match(/(?=\{context:)/)

    if (!matched) {
      // console.log('setValue, not match')
      break
    }

    var end = 0
    var placeholder = ''
    for (var i = matched.index; i < url.length; i++) {
      placeholder += url[i]
      if (url[i] === '}') {
        end = i
        break
      }
    }
    var items = placeholder.replace('{context:', '')
      .replace('}', '')
      .split(';')
    var value = null
    if (items.length === 1) {
      // value = context.getProp(items[0])
    } else {
      // value = context.formatValue(items[0], items[1] ? items[1].replace('prop:', '') : '')
    }
    value = localStorage.getItem('tetris-001')
    url = url.replace(new RegExp(placeholder, 'g'), value)
  }
  return url
}

function getActiveMenu (menus) {
  if (menus && menus.length > 0) {
    for (var i = 0; i < menus.length; i++) {
      if (menus[i].active && menus[i].active === true) {
        return menus[i]
      } else {
        if (menus[i].sub && menus[i].sub.length > 0) {
          var target = getActiveMenu(menus[i].sub)
          if (target) return target
        }
      }
    }
  }
}
