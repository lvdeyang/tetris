<template>
  <div>
      <a-row>
        <a-card class="box-card"  >
          <div slot="title" >
             <a-icon type="desktop" /> 设备操作
          </div>
          <a-button @click="reset" style="height: 80px;	border: 1px solid #ccc;margin:0px 20px" >重置设备</a-button>
          <a-button @click="downToSlave(device.id)" v-if=" device.backType == 'MAIN'" style="width: 100px;height: 100px;	border: 1px solid #ccc;margin:0px 20px">设备切换</a-button>
        </a-card>
      </a-row>
        <a-row  >

          <a-card class="box-card" :body-style="{ padding: '0px' }" >
            <div slot="title" class="clearfix"><a-icon type="fund" /> 网卡信息</div>
            <div slot="extra">
              <a-button   type="primary" v-if="device.deviceType == 'SERVER'" @click="config = true" v-show="!config">配置</a-button>
              <a-button   @click="config = false" v-show="config">取消</a-button>
              <a-button   type="primary" @click="saveConfig" v-show="config">保存</a-button>
              <a-button  @click="refreshNetcard" v-show="!config">刷新</a-button>
            </div>
            <div>
              <a-table :columns="netColumns">

              </a-table>
            </div>
          </a-card>
        </a-row>

    <a-modal v-model="switchModalVisible" title="降备" @ok="toSlaveCommit" @cancel="close">
      <a-form :label-col="{span:5}" :wrapper-col="{span:12}">
        <a-form-item label="切换目标">
          <a-select   v-model="backDeviceId">
            <a-select-option v-for="item in optionBackDevices" :value="item.id" :key="item.id">{{ item.name }}</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>
<script>
  import ACol from "ant-design-vue/es/grid/Col";
  export default {
    name: 'Device',
    components: {ACol},
    data () {
      return {
        switchModalVisible:false,
        layerIndex: 0,
        netColumns:[
          {dataIndex:'name',title:'网卡名称'},
          {dataIndex:'ip',title:'ip地址'},
          {dataIndex:'ipMask',title:'子网掩码'},
          {dataIndex:'inGroup',title:'输入分组'},
          {dataIndex:'outGroup',title:'输出分组'},
        ],
        config: false,
        optId: 0,
        name: '',
        device: {}, // 设备节点
        deviceGroup: {},
        card: [],
        taskNum: {},
        netGroups: [],
        backDeviceId: "",
        // 设备分组内的所有备设备
        backDevices: [],
        optionBackDevices: [],

        netCardVisible: false,

      }
    },
    computed: {

    },
    methods: {

      async refreshNetcard () {
        // await getData('/device/refreshNetcard/' + this.deviceId, {}, (response) => {
        //   if (response.data != null) {
        //     bus.$emit('addAlarm', {type: 'success', info: this.$t('刷新成功')})
        //     this.close()
        //     this.device.netCards = response.data
        //   }else{
        //     bus.$emit('addAlarm', {type: 'fail', info: this.$t('刷新失败')})
        //   }
        // })
      },



      saveConfig () {
        var self = this

      },

      arrayEquals (a, b) {
        return a.sort().toString() == b.sort().toString()
      },

      reset () {

      },


      lightSync (socketAddress) {

      },

      toSlaveCommit () {
        let param = {
          srcDeviceId: this.optId,
          backDeviceId: this.backDeviceId
        }
        // postFormWithConfig('/device/downToSlave', param, {timeout: 0}, () => {
        //   this.close()
        //   this.updateChart()
        //   bus.$emit('updateDeviceList')
        // })
        //todo 未实现
      },
      // 获取备转码节点列表(网络分组配置相同)
      getBackDevices () {
        this.optionBackDevices = this.backDevices
      },
      downToSlave (optId) {
        this.optId = optId
        this.getBackDevices()
        this.switchModalVisible=true
      },

      unitStatus (funUnit) {
        if (funUnit.funUnitStatus == 'NORMAL') { return 'bg-primary' } else if (funUnit.funUnitStatus == 'NEED_SYNC') { return 'bg-warning' } else if (funUnit.funUnitStatus == 'OFF_LINE') { return 'bg-danger' } else if (funUnit.funUnitStatus == 'DEFAULT') { return 'bg-info' }
      },



      addDevice (i) {
        this.errors.clear()

      },
      delDevice (id) {

      },
      deleteDevice (id) {

      },
      // close() {
      //     this.opt = false
      //     layer.close(this.layerIndex)
      //     this.device.deviceIp = ''
      // },
      // resize() {
      //     this.channel_Chart.resize({silent: false})
      // } ,
      getNetClass (netcard) {
        if (netcard.status == 0) { return 'text-danger' } else { return (netcard.inputNetGroupId || netcard.outputNetGroupId) ? 'text-primary' : '' }
      },


      async fetchDevice () {
        return getData('/device/' + this.deviceId, {}, (response) => {
          this.device = response.data.device
          this.taskNum = response.data.taskNum
          this.netGroups = response.data.netGroups
          this.card = response.data.card
          this.deviceGroup = response.data.deviceGroup
          this.backDevices = response.data.backDevices
        })
      },

      async getNet () {
        return postData('http://10.10.40.102:8910/action/get_netcard_info', {}, (response) => {
          console.log(response)
        })
      },
      async init () {
        await this.fetchDevice()
        // this.getDnsServer()
      }
    },
    created () {
      // this.init()
    },
    mounted () {
      // this.device.deviceGroupId = this.$route.params.deviceGroupId
    }
  }


</script>
<style>

</style>
