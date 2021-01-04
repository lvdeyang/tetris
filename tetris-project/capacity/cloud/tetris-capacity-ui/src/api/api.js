import axios from 'axios'

var qs = require('qs')
let requestIP = document.location.host.split(':')[0]

let basePath = process.env.CAPACITY_ROOT
let loginUrl = process.env.CAPACITY_ROOT + '/web/app/login/login.html'

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
export const queryAllDeviceGroup = params => { return axiosInstance.get(`${basePath}/deviceGroup/getAll`, qs.stringify(params)).then(res => res.data) }
export const queryDevGroup = params => { return axiosInstance.post(`${basePath}/deviceGroup/get`, qs.stringify(params)).then(res => res.data) }
export const addDeviceGroup = params => { return axiosInstance.post(`${basePath}/deviceGroup/add`, qs.stringify(params)).then(res => res.data) }
export const editDeviceGroup = params => { return axiosInstance.post(`${basePath}/deviceGroup/edit`, qs.stringify(params)).then(res => res.data) }
export const changeAutoBackup = params => { return axiosInstance.post(`${basePath}/deviceGroup/changeAutoBackup`, qs.stringify(params)).then(res => res.data) }
export const deleteDeviceGroup = params => { return axiosInstance.post(`${basePath}/deviceGroup/delete`, qs.stringify(params)).then(res => res.data) }

export const queryAllNetGroup = params => { return axiosInstance.get(`${basePath}/netGroup/getAll`, qs.stringify(params)).then(res => res.data) }
export const addNetGroup = params => { return axiosInstance.post(`${basePath}/netGroup/add`, qs.stringify(params)).then(res => res.data) }
export const deleteNetGroup = params => { return axiosInstance.post(`${basePath}/netGroup/delete`, qs.stringify(params)).then(res => res.data) }
export const editNetGroup = params => { return axiosInstance.post(`${basePath}/netGroup/edit`, qs.stringify(params)).then(res => res.data) }
export const beUsedNetGroup = params => { return axiosInstance.post(`${basePath}/netGroup/beUsedByNetGroupId`, qs.stringify(params)).then(res => res.data) }

export const deleteDevice = params => { return axiosInstance.post(`${basePath}/device/delete`, qs.stringify(params)).then(res => res.data) }
export const addDevice = params => { return axiosInstance.post(`${basePath}/device/add`, qs.stringify(params)).then(res => res.data) }
export const editDevice = params => { return axiosInstance.post(`${basePath}/device/edit`, qs.stringify(params)).then(res => res.data) }
export const saveDeviceConfig = params => { return axiosInstance.post(`${basePath}/device/config`, qs.stringify(params)).then(res => res.data) }
export const resetDeviceConfig = params => { return axiosInstance.post(`${basePath}/device/reset`, qs.stringify(params)).then(res => res.data) }
export const switchDevice = params => { return axiosInstance.post(`${basePath}/device/switch`, qs.stringify(params)).then(res => res.data) }
export const syncDevice = params => { return axiosInstance.post(`${basePath}/device/sync`, qs.stringify(params)).then(res => res.data) }

export const getBackupCondition = params => { return axiosInstance.get(`${basePath}/backupCondition/get`, qs.stringify(params)).then(res => res.data) }
export const editBackupCondition = params => { return axiosInstance.post(`${basePath}/backupCondition/update`, qs.stringify(params)).then(res => res.data) }
