import Vue from 'vue'
import Vuex from 'vuex'
import {queryDevGroup} from '../../api/api'


Vue.use(Vuex)

const state = {
  deviceGroups: [],
  curDeviceGroup: {}
}

const getters = {
  deviceGroups : state => state.deviceGroups,
  curDeviceGroup : state => state.curDeviceGroup
}

const actions = {
  initDeviceGroups : ({ commit } , payload) => commit('initDeviceGroups' , payload),
  fetchDeviceGroup : ({ commit } , payload) => commit('fetchDeviceGroup' , payload),
}

const mutations = {
  initDeviceGroups(state,obj){
    state.deviceGroups = obj
  },
  fetchDeviceGroup(state,obj){
    if (obj===undefined){
      obj = state.curDeviceGroup.id
    }
    let param={id: obj}
    queryDevGroup(param).then(res=>{
      state.curDeviceGroup = res.data
      state.curDeviceGroup.devices = res.data.devices
      let idx = state.deviceGroups.findIndex(g=>g.id===state.curDeviceGroup.id)
      state.deviceGroups[idx].name=state.curDeviceGroup.name
    })
  },

}



export default new Vuex.Store({
  state,
  getters,
  actions,
  mutations
})
