import axios from 'axios'

var qs = require('qs')
let requestIP = document.location.host.split(':')[0]

let basePath = process.env.ALARM_ROOT
let loginUrl = process.env.ALARM_ROOT + '/web/app/login/login.html'

if (basePath.indexOf('__requestIP__') !== -1) {
  basePath = basePath.replace('__requestIP__', requestIP)
  // console.log('basePath replace=' + basePath)
}

if (loginUrl.indexOf('__requestIP__') !== -1) {
  loginUrl = loginUrl.replace('__requestIP__', requestIP)
  // console.log('loginUrl replace=' + loginUrl)
}

// axios.defaults.baseURL =

// 用下面这个可以访问springmvc Controller
let axiosInstance = axios.create({
  headers: {
  //  'Access-Control-Allow-Origin': '*',
  //  'Content-Type': 'application/x-www-form-urlencoded',
  //  'Access-Control-Allow-Headers': 'Content-Type, Content-Length, Authorization, Accept, X-Requested-With , yourHeaderFeild',
  //  'Access-Control-Allow-Methods': 'PUT,POST,GET,DELETE,OPTIONS'
  },
  // withCredentials: true
})

/** 请求拦截器 */
axiosInstance.interceptors.request.use(function (config) {
  // alert('in= ' + sessionStorage.getItem('token'))
  config.headers.common['tetris-001'] = localStorage.getItem('tetris-001')
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
    if (error.response.status === 401) {
      window.location.href = loginUrl
    }
  })

// export const getLoginUserName = params => { return axiosInstance.post(`${basePath}/user/getLoginUserName`, qs.stringify(params)).then(res => res.data) }

export const logoutAction = params => { return axiosInstance.get(`${basePath}/do/logout`, qs.stringify(params)).then(res => res.data) }


// 告警列表
export const queryAlarmListPage = params => { return axiosInstance.post(`${basePath}/alarm/queryPage`, qs.stringify(params)).then(res => res.data) }
export const ignoreAlarm = params => { return axiosInstance.post(`${basePath}/alarm/ignore`, qs.stringify(params)).then(res => res.data) }
export const manualRecoverAlarm = params => { return axiosInstance.post(`${basePath}/alarm/manualRecover`, qs.stringify(params)).then(res => res.data) }
export const shieldAlarm = params => { return axiosInstance.post(`${basePath}/alarm/shield`, qs.stringify(params)).then(res => res.data) }
export const unShieldAlarm = params => { return axiosInstance.post(`${basePath}/alarm/unShield`, qs.stringify(params)).then(res => res.data) }
export const delAlarm = params => { return axiosInstance.post(`${basePath}/alarm/del`, qs.stringify(params)).then(res => res.data) }

// 告警信息
export const queryAlarmBaseInfoListPage = params => { return axiosInstance.post(`${basePath}/alarmInfo/queryPage`, qs.stringify(params)).then(res => res.data) }
export const addAlarmBaseInfo = params => { return axiosInstance.post(`${basePath}/alarmInfo/add`, qs.stringify(params)).then(res => res.data) }
export const editAlarmBaseInfo = params => { return axiosInstance.post(`${basePath}/alarmInfo/edit`, qs.stringify(params)).then(res => res.data) }
export const delAlarmBaseInfo = params => { return axiosInstance.post(`${basePath}/alarmInfo/del`, qs.stringify(params)).then(res => res.data) }
export const checkAlarmCode = params => { return axiosInstance.post(`${basePath}/alarmInfo/checkCode`, qs.stringify(params)).then(res => res.data) }

// 操作日志
export const queryOprlogListPage = params => { return axiosInstance.post(`${basePath}/oprlog/queryPage`, qs.stringify(params)).then(res => res.data) }
