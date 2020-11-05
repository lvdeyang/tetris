import axios from 'axios'

var qs = require('qs')
let requestIP = document.location.host.split(':')[0]

let basePath = process.env.RECORD_ROOT
let loginUrl = process.env.RECORD_ROOT + '/web/app/login/login.html'

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

// 录制策略
export const queryRecordStrategy = params => { return axiosInstance.post(`${basePath}/record/strategy/queryStrategy`, qs.stringify(params)).then(res => res.data) }
export const addRecordStrategy = params => { return axiosInstance.post(`${basePath}/record/strategy/addRecordStrategy`, qs.stringify(params)).then(res => res.data) }
export const queryAllStrategy = params => { return axiosInstance.post(`${basePath}/record/strategy/queryAll`, qs.stringify(params)).then(res => res.data) }
export const queryStrategyItems = params => { return axiosInstance.post(`${basePath}/record/strategy/queryStrategyItems`, qs.stringify(params)).then(res => res.data) }
export const startRecord = params => { return axiosInstance.post(`${basePath}/record/strategy/startRecord`, qs.stringify(params)).then(res => res.data) }
export const stopRecord = params => { return axiosInstance.post(`${basePath}/record/strategy/stopRecord`, qs.stringify(params)).then(res => res.data) }
export const delRecordStrategy = params => { return axiosInstance.post(`${basePath}/record/strategy/delRecordStrategy`, qs.stringify(params)).then(res => res.data) }
export const querySourceFromMims = params => { return axiosInstance.post(`${basePath}/record/strategy/querySourceFromMims`, qs.stringify(params)).then(res => res.data) }

// 设备
export const queryRecordDevice = params => { return axiosInstance.post(`${basePath}/record/device/query`, qs.stringify(params)).then(res => res.data) }
export const queryDeviceFromFeignAPI = params => { return axiosInstance.post(`${basePath}/record/device/queryFromFeign`, qs.stringify(params)).then(res => res.data) }
export const addDevice = params => { return axiosInstance.post(`${basePath}/record/device/add`, qs.stringify(params)).then(res => res.data) }
export const delDevice = params => { return axiosInstance.post(`${basePath}/record/device/del`, qs.stringify(params)).then(res => res.data) }

// 录制文件
export const queryRecordFile = params => { return axiosInstance.post(`${basePath}/record/file/query`, qs.stringify(params)).then(res => res.data) }
export const delRecordFile = params => { return axiosInstance.post(`${basePath}/record/file/del`, qs.stringify(params)).then(res => res.data) }
export const previewRecordFile = params => { return axiosInstance.post(`${basePath}/record/file/preview`, qs.stringify(params)).then(res => res.data) }
export const uploadFileToMims = params => { return axiosInstance.post(`${basePath}/record/file/uploadToMims`, qs.stringify(params)).then(res => res.data) }
