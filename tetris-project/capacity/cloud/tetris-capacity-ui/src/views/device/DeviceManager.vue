<template>
  <div >
      <a-card
        size="small"
        style="width:100%;"
        :tab-list="deviceGroups"
        :active-tab-key="key"
        @tabChange="key => onTabChange(key, 'key')"
      >
      <template slot="customRender" slot-scope="item">
        <span><a-icon type="cloud-server" />{{item.name}}</span>
        <!--<a-popconfirm placement="rightBottom" ok-text="Yes" cancel-text="No" @confirm="deleteDeviceGroup(item.name)">-->
          <!--<template slot="title">-->
            <!--<p>{{item.name}}</p>-->
            <!--<p>删除设备分组和该分组下所有设备?</p>-->
          <!--</template>-->
          <!--<span style="margin-left:10px;"><a-icon type="close"/></span>-->
        <!--</a-popconfirm>-->
      </template>
        <template v-if="deviceGroups.length===0" slot="extra">
          <a-button @click="deviceGroupVisible=true" type="primary" >新建分组</a-button>
        </template>
        <template v-else slot="tabBarExtraContent">
          <a-button @click="deviceGroupVisible=true" type="primary" >新建分组</a-button>
        </template>
      <device-group @deleteDeviceGroup="deleteDeviceGroup"  v-if="deviceGroups.length!==0"></device-group>
      </a-card>
      <a-modal v-model="deviceGroupVisible" title="添加设备分组" @ok="addDeviceGroup" @cancel="close">
        <a-form :label-col="{span:8}" :wrapper-col="{span:8}">
          <a-form-item label="分组名称">
            <a-input v-model="newDeviceGroup.name"></a-input>
          </a-form-item>
          <a-form-item label="备份模式">
            <a-select v-model="newDeviceGroup.backupStrategy">
              <a-select-option value="NPLUSM">N+M</a-select-option>
              <a-select-option value="ONE2ONE">1+1</a-select-option>
            </a-select>
          </a-form-item>
        </a-form>
      </a-modal>
  </div>
</template>

<script>
    import { mapGetters, mapActions } from 'vuex'
    import Device from './Device.vue'
    import DeviceGroup from './DeviceGroup.vue'
    import {
      addDeviceGroup,queryAllDeviceGroup,deleteDeviceGroup
    } from '../../api/api'
    export default {
        name: "DeviceManager",
        data() {
          return {
            newDeviceGroup:{
              name:'',
              backupStrategy: 'NPLUSM',
            },
            key: '',
            deviceGroupVisible:false,
          }
        },
        components: {
          Device,DeviceGroup
        },
        computed:{
          ...mapGetters([
            'deviceGroups'
          ]),
        },
        methods:{
          ...mapActions([
            'initDeviceGroups','fetchDeviceGroup'
          ]),
          onTabChange(key, type) {
            this.fetchDeviceGroup(key)
            this.key = key;
          },
          deleteDeviceGroup(name){
            let self =this;
            deleteDeviceGroup( {name: name}).then(res => {
              self.fetchDeviceGroups()
            })
          },
          fetchDeviceGroups(){
            let self = this;
            this.initDeviceGroups([])
            queryAllDeviceGroup({}).then(res=>{
              if (res.status!=200){
                this.$message.error(res.message);
              }else {
                let groups=[]
                for(let item of res.data){
                  let group = item;
                  group.scopedSlots={tab: 'customRender'}
                  group.key=item.id.toString();
                  groups.push(group)
                }
                this.initDeviceGroups(groups)
                if (groups.length>0){
                  this.fetchDeviceGroup(groups[groups.length-1].id)
                  self.key = groups[groups.length-1].id.toString()
                }
              }
            })
          },
          addDeviceGroup(){
            addDeviceGroup(this.newDeviceGroup).then(res => {
              if (res.status!=200){
                this.$message.error(res.message);
              }else {
                this.fetchDeviceGroups();
                this.close();
              }
            })
          },
          close () {
            this.deviceGroupVisible = false
          },
          async init(){
            await this.fetchDeviceGroups();
            //this.key = this.deviceGroups[0].name
          }

        },
         created() {
            this.init();
         }
    }
</script>

<style scoped>
  .card-container {
    background: #f5f5f5;
    overflow: hidden;
    padding: 24px;
  }
  .card-container > .ant-tabs-card > .ant-tabs-content {
    height: 120px;
    margin-top: -16px;
  }

  .card-container > .ant-tabs-card > .ant-tabs-content > .ant-tabs-tabpane {
    background: #fff;
    padding: 16px;
  }

  .card-container > .ant-tabs-card > .ant-tabs-bar {
    border-color: #fff;
  }

  .card-container > .ant-tabs-card > .ant-tabs-bar .ant-tabs-tab {
    border-color: transparent;
    background: transparent;
  }

  .card-container > .ant-tabs-card > .ant-tabs-bar .ant-tabs-tab-active {
    border-color: #fff;
    background: #fff;
  }
</style>
