import axios from 'axios'

var qs = require('qs')
let requestIP = document.location.host.split(':')[0]

// TODO
//let basePath = process.env.RESOURCE_ROOT + '/suma-venus-resource'
let basePath = process.env.RESOURCE_ROOT
let loginUrl = process.env.USER_ROOT + '/vue'

if (basePath.indexOf('__requestIP__') !== -1) {
  basePath = basePath.replace('__requestIP__', requestIP)
  // onsole.log('basePath replace=' + basePath)
}

if (loginUrl.indexOf('__requestIP__') !== -1) {
  loginUrl = loginUrl.replace('__requestIP__', requestIP)
  // console.log('loginUrl replace=' + loginUrl)
}

let axiosInstance = axios.create({
  headers: {
    'Access-Control-Allow-Origin': '*',
    'Content-Type': 'application/x-www-form-urlencoded',
    'Access-Control-Allow-Headers': 'Content-Type, Content-Length, Authorization, Accept, X-Requested-With , yourHeaderFeild',
    'Access-Control-Allow-Methods': 'PUT,POST,GET,DELETE,OPTIONS'
  },
  withCredentials: false
})

// 请求拦截器
axiosInstance.interceptors.request.use(function (config) {
  config.headers['tetris-001'] = sessionStorage.getItem('token');
  //config.headers['tetris-002'] = '';
  config.headers.Authorization = sessionStorage.getItem('token');
  return config
}, function (err) {
  return Promise.reject(err)
})

// 响应拦截器
axiosInstance.interceptors.response.use(
  function (response) {
    return response
  },
  function (error) {
    if (error.response.status === 401) { // 认证出错,跳转至登录页面
      alert('超时，请重新登录!');
      //window.location.href = loginUrl
    }
  })

export const getLoginUserName = params => { return axiosInstance.post(`${basePath}/user/getLoginUserName`, qs.stringify(params)).then(res => res.data) }

export const getDeviceModels = params => { return axiosInstance.post(`${basePath}/template/getDeviceModels`, qs.stringify(params)).then(res => res.data) }
export const getTemplates = params => { return axiosInstance.post(`${basePath}/template/abilityTemplates`, qs.stringify(params)).then(res => res.data) }
export const getTemplateTree = params => { return axiosInstance.post(`${basePath}/template/tree`, qs.stringify(params)).then(res => res.data) }
export const deleteTemplate = params => { return axiosInstance.post(`${basePath}/template/delete`, qs.stringify(params)).then(res => res.data) }
export const getAllUsers = params => { return axiosInstance.post(`${basePath}/user/getAllUser`, qs.stringify(params)).then(res => res.data) }
export const getBundles = params => { return axiosInstance.post(`${basePath}/bundle/query`, qs.stringify(params)).then(res => res.data) }
export const deleteBundle = params => { return axiosInstance.post(`${basePath}/bundle/delete`, qs.stringify(params)).then(res => res.data) }
export const addBundle = params => { return axiosInstance.post(`${basePath}/bundle/add`, qs.stringify(params)).then(res => res.data) }
export const queryBundleExtraInfo = params => { return axiosInstance.post(`${basePath}/bundle/queryExtraInfo`, qs.stringify(params)).then(res => res.data) }
export const modifyBundleExtraInfo = params => { return axiosInstance.post(`${basePath}/bundle/modifyExtraInfo`, qs.stringify(params)).then(res => res.data) }
export const getBundleChannels = params => { return axiosInstance.post(`${basePath}/bundle/getBundleChannels`, qs.stringify(params)).then(res => res.data) }
export const getBundleAbility = params => { return axiosInstance.post(`${basePath}/ability/getBundleAbility`, qs.stringify(params)).then(res => res.data) }
export const configBundle = params => { return axiosInstance.post(`${basePath}/ability/configBundle`, qs.stringify(params)).then(res => res.data) }

export const getBundlesOfRole = params => { return axiosInstance.post(`${basePath}/resource/queryBundlesOfRole`, qs.stringify(params)).then(res => res.data) }
export const getUsersOfRole = params => { return axiosInstance.post(`${basePath}/resource/queryUsersOfRole`, qs.stringify(params)).then(res => res.data) }
export const getVirtualResourcesOfRole = params => { return axiosInstance.post(`${basePath}/resource/queryVirtualResourcesOfRole`, qs.stringify(params)).then(res => res.data) }
export const submitBundlePrivilege = params => { return axiosInstance.post(`${basePath}/resource/submitBundlePrivilege`, qs.stringify(params)).then(res => res.data) }
export const submitUserresPrivilege = params => { return axiosInstance.post(`${basePath}/resource/submitUserresPrivilege`, qs.stringify(params)).then(res => res.data) }
export const submitVirtualResourcePrivilege = params => { return axiosInstance.post(`${basePath}/resource/submitVirtualResourcePrivilege`, qs.stringify(params)).then(res => res.data) }
export const getBundleDetailInfo = params => { return axiosInstance.post(`${basePath}/bundle/queryExtraInfo`, qs.stringify(params)).then(res => res.data) }
export const getAllRoles = params => { return axiosInstance.post(`${basePath}/user/getAllRoles`, qs.stringify(params)).then(res => res.data) }
export const getVirtualResourceDetailInfo = params => { return axiosInstance.post(`${basePath}/resource/queryVirtualResourceInfo`, qs.stringify(params)).then(res => res.data) }

export const getNodes = params => { return axiosInstance.post(`${basePath}/layernode/query`, qs.stringify(params)).then(res => res.data) }
export const deleteNodes = params => { return axiosInstance.post(`${basePath}/layernode/delete`, qs.stringify(params)).then(res => res.data) }
export const saveNode = params => { return axiosInstance.post(`${basePath}/layernode/save`, qs.stringify(params)).then(res => res.data) }
export const getNodeById = params => { return axiosInstance.post(`${basePath}/layernode/getNode`, qs.stringify(params)).then(res => res.data) }

export const addFolder = params => { return axiosInstance.post(`${basePath}/folder/add`, qs.stringify(params)).then(res => res.data) }
export const deleteFolder = params => { return axiosInstance.post(`${basePath}/folder/delete`, qs.stringify(params)).then(res => res.data) }
export const initFolderTree = params => { return axiosInstance.post(`${basePath}/folder/initTree`, qs.stringify(params)).then(res => res.data) }
export const modifyFolder = params => { return axiosInstance.post(`${basePath}/folder/modify`, qs.stringify(params)).then(res => res.data) }
export const setFolderOfBundles = params => { return axiosInstance.post(`${basePath}/folder/setFolderOfBundles`, qs.stringify(params)).then(res => res.data) }
export const resetFolderOfBundles = params => { return axiosInstance.post(`${basePath}/folder/resetFolderOfBundles`, qs.stringify(params)).then(res => res.data) }
export const queryBundlesWithoutFolder = params => { return axiosInstance.post(`${basePath}/folder/queryBundlesWithoutFolder`, qs.stringify(params)).then(res => res.data) }
export const queryUsersWithoutFolder = params => { return axiosInstance.post(`${basePath}/folder/queryUsersWithoutFolder`, qs.stringify(params)).then(res => res.data) }
export const setFolderToUsers = params => { return axiosInstance.post(`${basePath}/folder/setFolderToUsers`, qs.stringify(params)).then(res => res.data) }
export const resetFolderOfUsers = params => { return axiosInstance.post(`${basePath}/folder/resetFolderOfUsers`, qs.stringify(params)).then(res => res.data) }
export const queryRootOptions = params => { return axiosInstance.post(`${basePath}/folder/queryRootOptions`, qs.stringify(params)).then(res => res.data) }
export const setRoot = params => { return axiosInstance.post(`${basePath}/folder/setRoot`, qs.stringify(params)).then(res => res.data) }
export const resetRootNode = params => { return axiosInstance.post(`${basePath}/folder/resetRootNode`, qs.stringify(params)).then(res => res.data) }
export const changeNodePosition = params => { return axiosInstance.post(`${basePath}/folder/changeNodePosition`, qs.stringify(params)).then(res => res.data) }

export const logoutBundle = params => { return axiosInstance.post(`${basePath}/bundle/logout`, qs.stringify(params)).then(res => res.data) }
export const clearBundle = params => { return axiosInstance.post(`${basePath}/bundle/clear`, qs.stringify(params)).then(res => res.data) }
export const setAccessLayer = params => { return axiosInstance.post(`${basePath}/bundle/setAccessLayer`, qs.stringify(params)).then(res => res.data) }

export const syncLdap = params => { return axiosInstance.post(`${basePath}/bundle/ldapSync`, qs.stringify(params)).then(res => res.data) }

export const syncFolderToLdap = params => { return axiosInstance.post(`${basePath}/folder/syncToLdap`, qs.stringify(params)).then(res => res.data) }
export const syncFolderFromLdap = params => { return axiosInstance.post(`${basePath}/folder/syncFromLdap`, qs.stringify(params)).then(res => res.data) }
export const cleanupFolderLdap = params => { return axiosInstance.post(`${basePath}/folder/cleanUpLdap`, qs.stringify(params)).then(res => res.data) }

export const querySerInfo = params => { return axiosInstance.post(`${basePath}/serInfo/querySerInfo`, qs.stringify(params)).then(res => res.data) }
export const querySerNode = params => { return axiosInstance.post(`${basePath}/serInfo/querySerNode`, qs.stringify(params)).then(res => res.data) }
export const addSerInfo = params => { return axiosInstance.post(`${basePath}/serInfo/addSerInfo`, qs.stringify(params)).then(res => res.data) }
export const addSerNode = params => { return axiosInstance.post(`${basePath}/serInfo/addSerNode`, qs.stringify(params)).then(res => res.data) }
export const modifySerInfo = params => { return axiosInstance.post(`${basePath}/serInfo/modifySerInfo`, qs.stringify(params)).then(res => res.data) }
export const modifySerNode = params => { return axiosInstance.post(`${basePath}/serInfo/modifySerNode`, qs.stringify(params)).then(res => res.data) }
export const delSerInfo = params => { return axiosInstance.post(`${basePath}/serInfo/delSerInfo`, qs.stringify(params)).then(res => res.data) }
export const delSerNode = params => { return axiosInstance.post(`${basePath}/serInfo/delSerNode`, qs.stringify(params)).then(res => res.data) }
export const cleanUpSerInfo = params => { return axiosInstance.post(`${basePath}/serInfo/cleanUpSerInfo`, qs.stringify(params)).then(res => res.data) }
export const cleanUpSerNode = params => { return axiosInstance.post(`${basePath}/serInfo/cleanUpSerNode`, qs.stringify(params)).then(res => res.data) }

export const syncSerNodeFromLdap = params => { return axiosInstance.post(`${basePath}/serInfo/syncSerNodeFromLdap`, qs.stringify(params)).then(res => res.data) }
export const syncSerNodeToLdap = params => { return axiosInstance.post(`${basePath}/serInfo/syncSerNodeToLdap`, qs.stringify(params)).then(res => res.data) }
export const syncSerInfoFromLdap = params => { return axiosInstance.post(`${basePath}/serInfo/syncSerInfoFromLdap`, qs.stringify(params)).then(res => res.data) }
export const syncSerInfoToLdap = params => { return axiosInstance.post(`${basePath}/serInfo/syncSerInfoToLdap`, qs.stringify(params)).then(res => res.data) }
export const queryFatherNodeOptions = params => { return axiosInstance.post(`${basePath}/serInfo/queryFatherNodeOptions`, qs.stringify(params)).then(res => res.data) }

export const syncEquipInfoFromLdap = params => { return axiosInstance.post(`${basePath}/bundle/syncFromLdap`, qs.stringify(params)).then(res => res.data) }
export const syncEquipInfToLdap = params => { return axiosInstance.post(`${basePath}/bundle/syncToLdap`, qs.stringify(params)).then(res => res.data) }
export const cleanUpEquipInfo = params => { return axiosInstance.post(`${basePath}/bundle/cleanUpLdap`, qs.stringify(params)).then(res => res.data) }

export const queryUserBindBundles = params => { return axiosInstance.post(`${basePath}/user/bind/bundle/query`, qs.stringify(params)).then(res => res.data) }
export const setUserBindBundles = params => { return axiosInstance.post(`${basePath}/user/bind/bundle/bind`, qs.stringify(params)).then(res => res.data) }
export const queryEncoders = params => { return axiosInstance.post(`${basePath}/user/bind/bundle/query/encoders`, qs.stringify(params)).then(res => res.data) }
export const queryDecoders = params => { return axiosInstance.post(`${basePath}/user/bind/bundle/query/decoders`, qs.stringify(params)).then(res => res.data) }