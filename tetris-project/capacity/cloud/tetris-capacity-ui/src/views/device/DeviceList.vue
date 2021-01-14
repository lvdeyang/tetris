<template>
    <div style="height:780px;overflow-y: auto">
      <a-card  hoverable  size="small">
        <div slot="title">设备列表</div>
        <div slot="extra" >
          <a-button @click="visibleAddDeviceModal = true" type="primary" size="small" >添加设备</a-button>
        </div>
        <div id="components-form-demo-advanced-search" v-show="false">
          <a-form class="ant-advanced-search-form" :form="form" @submit="handleSearch" layout="inline">
            <a-row >
              <a-col :span="5" :style="{ display: 0 < count ? 'block' : 'none' }">
                <a-form-item label="设备名称">
                  <a-input />
                </a-form-item>
              </a-col>
              <a-col :span="5" :style="{ display: 1 < count ? 'block' : 'none' }">
                <a-form-item label="设备IP">
                  <a-input />
                </a-form-item>
              </a-col>
              <a-col :span="4" :style="{ display: 2 < count ? 'block' : 'none' }">
                <a-form-item label="主备状态">
                  <a-select style="width: 100px;">
                    <a-select-option value="NONE">不限</a-select-option>
                    <a-select-option value="MAIN">主</a-select-option>
                    <a-select-option value="BACK">备</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="4" :style="{ display: 3 < count ? 'block' : 'none' }">
                <a-form-item label="设备状态">
                  <a-select style="width: 100px;">
                    <a-select-option value="NONE">不限</a-select-option>
                    <a-select-option value="MAIN">在线</a-select-option>
                    <a-select-option value="BACK">离线</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="6" :style="{ textAlign: 'right' }">
                <a-button type="primary" html-type="submit">
                  搜索
                </a-button>
                <a-button :style="{ marginLeft: '8px' }" @click="handleReset">
                  清空
                </a-button>
                <!--<a :style="{ marginLeft: '8px', fontSize: '12px' }" @click="toggle">-->
                  <!--展开 <a-icon :type="expand ? 'up' : 'down'" />-->
                <!--</a>-->
              </a-col>
            </a-row>
            <a-row>
<!--多个查找条件加到此处-->
            </a-row>
          </a-form>
        </div>
        <div class="search-result-list">
          <a-list :grid="{ gutter: 10, xs: 1, sm: 2, md: 4, lg: 4, xl: 4, xxl: 4 }" :pagination="pagination" :data-source="devices">
            <a-list-item slot="renderItem" key="item.name" slot-scope="item, index">
              <a-card hoverable  size="small">
                    <span slot="title" >
                      <a-icon type="laptop" /> {{item.name}}
                      <a-badge status="processing" v-if="item.funUnitStatus!=='OFF_LINE'"/>
                      <a-badge status="red" v-if="item.funUnitStatus==='OFF_LINE'"/>
                    </span>
                <a-popconfirm slot="extra" placement="rightBottom" ok-text="Yes" cancel-text="No" @confirm="deleteDevice(item.id)">
                  <template slot="title">
                    <p>{{item.name}}</p>
                    <p>即将删除设备，是否继续</p>
                  </template>
                  <span style="margin-left:10px;"><a-icon type="delete"/></span>
                </a-popconfirm>
                <!--<a slot="extra" @click="deleteDevice(item.id)"><a-icon type="delete" /></a>-->
                <template slot="actions" class="ant-card-actions" >
                  <a @click="selectedDevice=item;visibleEditDeviceModal=true;editDevice.groupId=curDeviceGroup.id;editDevice.backType=item.backType;editDevice.name=item.name"><a-icon type="edit"/>修改</a>
                  <a @click="openSwitchModal(item)"><a-icon type="switcher"/>切换</a>
                  <a @click=""><a-icon type="file-zip"/>授权</a>
                  <!--<a-icon key="ellipsis" type="ellipsis" />-->
                </template>
                <a-card-meta>
                  <template slot="title">
                    <span>{{item.deviceIp}}</span>
                    <span style="float:right" >
                        <a-tag color="green"  v-if="item.backType==='MAIN'">主</a-tag>
                        <a-tag color="blue"  v-if="item.backType==='BACK'">备</a-tag>
                      </span>
                  </template>
                  <template slot="description">
                    <a-row>
                      <a-col>
                        <span>网卡配置：</span>
                        <template v-if="item.netConfig">
                          <a-tag style="border-radius:16px;line-height: 16px" color="green">已配置</a-tag>
                        </template>
                        <template v-else>
                          <a-tag style="border-radius:16px;line-height: 16px" color="red">未配置</a-tag>
                        </template>
                        <a-tooltip placement="bottomRight" title="配置">
                          <a-button  size="small" icon="setting" @click="selectedDevice=item;visibleConfigDeviceModal=true;"></a-button>
                        </a-tooltip>
                        <a-tooltip placement="bottomRight" title="重置" >
                          <a-button  size="small" icon="disconnect" @click="reset(item.id)"></a-button>
                        </a-tooltip>
                      </a-col>
                    </a-row>
                    <a-row style="margin-top:5px;">
                      <span>设备状态：</span>
                      <template v-if="item.funUnitStatus==='OFF_LINE'">
                        <a-tag style="border-radius:16px;line-height: 16px" color="#f50">离线</a-tag>
                      </template>
                      <template v-else>
                        <a-tag style="border-radius:16px;line-height: 16px" color="#87d068">在线</a-tag>
                      </template>

                      <template v-if="item.funUnitStatus==='NORMAL'">
                        <a-tag style="border-radius:16px;line-height: 16px" color="#87d068">同步</a-tag>
                        <a-tooltip placement="bottomRight" title="同步设备">
                          <a-button  size="small" icon="interaction" @click="syncDevice(item.id)"></a-button>
                        </a-tooltip>
                      </template>
                      <template v-if="item.funUnitStatus==='NEED_SYNC'">
                        <a-tag style="border-radius:16px;line-height: 16px" color="#f50">不同步</a-tag>
                        <a-tooltip placement="bottomRight" title="同步设备">
                          <a-button  size="small" icon="interaction" @click="syncDevice(item.id)"></a-button>
                        </a-tooltip>
                      </template>
                    </a-row>
                    <a-row style="margin-top:5px;">
                      <span>任务数：{{item.taskNum}}</span>
                    </a-row>
                  </template>
                </a-card-meta>
              </a-card>
            </a-list-item>
          </a-list>

        </div>
      </a-card>
      <!--添加设备对话框-->
      <a-modal  title="添加设备"
                :visible="visibleAddDeviceModal"
                :confirm-loading="confirmLoading"
                ok-text="确认" cancel-text="取消"
                @ok="saveDevice"
                @cancel="handleDeviceModalCancel">
        <a-form :form="addDeviceForm" :model="addDevice" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
          <a-form-item label="设备名称">
            <a-input placeholder="Input a device name" v-model="addDevice.name" name="name" v-decorator="['name',{rules:[{required:true,message:'Please input device name'}]}]"></a-input>
          </a-form-item>
          <a-form-item label="设备地址">
            <a-input-group compact>
              <a-input style="width: 70%" v-model="addDevice.deviceIp" name="deviceIp" placeholder="Input a device ip"
                       v-decorator="['deviceIp',{rules:[{required:true, message:'Please input ip'},{validator:checkIp}]}]">	</a-input>
              <a-input-number style="width: 30%"  v-model="addDevice.devicePort" v-decorator="['devicePort', { initialValue: 5656 }]" :min="1024" :max="65536"  name="devicePort" />
            </a-input-group>
          </a-form-item>
          <a-form-item label="主备状态">
            <a-select v-model="addDevice.backType" >
              <a-select-option value="MAIN">主</a-select-option>
              <a-select-option value="BACK">备</a-select-option>
            </a-select>
          </a-form-item>
        </a-form>
      </a-modal>
      <!-- 网卡分组配置返回结果对话框 -->
      <a-modal
        title="网卡分组配置失败"
        :visible="netCardVisible"
        :width="600">
        <a-form ref="form" label-position="top" label-width="80px">
          <a-form-item label="失败原因">
            <a-input readonly v-model="resOptVO.reason"></a-input>
          </a-form-item>
          <a-form-item label="错误详情" v-if="resOptVO.detail"  >
            <a-input readonly type="textarea" v-model="resOptVO.detail" :rows="4"></a-input>
          </a-form-item>
        </a-form>
        <span slot="footer" class="dialog-footer">
          <a-button  type="primary" @click="netCardVisible = false">关闭</a-button>
      </span>
      </a-modal>
      <!--设备配置对话框-->
      <a-modal :width="600" title="设备配置"   :visible="visibleConfigDeviceModal"  ok-text="确认" cancel-text="取消" @ok="saveConfig" @cancel="visibleConfigDeviceModal=false" >
        <a-table size="small" :columns="netColumns" :data-source="selectedDevice.netCards" :pagination="false" bordered >
          <template slot="status" slot-scope="text,record">
            <a-tag color="red" v-if="text===0">异常</a-tag>
            <a-tag color="green" v-if="text===1">正常</a-tag>
          </template>
          <template slot="inputNetGroupId" slot-scope="text,record">
            <a-select style="width:80px"  v-model="record.inputNetGroupId" :disabled="record.ipv4 !== ''? false:true">
              <template v-for="netGroup in netGroups" >
                <a-select-option  :key="netGroup.id"  :value="netGroup.id"  v-if="netGroup.netType===undefined || netGroup.netType=='INPUT'"> {{netGroup.netName}}</a-select-option>
              </template>
            </a-select>
          </template>
          <template slot="outputNetGroupId" slot-scope="text,record">
            <a-select style="width:80px" v-model="record.outputNetGroupId" :disabled="record.ipv4 !== ''? false:true">
              <template v-for="netGroup in netGroups"  >
                <a-select-option :key="netGroup.id"  :value="netGroup.id"  v-if="netGroup.netType===undefined || netGroup.netType=='OUTPUT'"> {{netGroup.netName}}</a-select-option>
              </template>
            </a-select>
          </template>
        </a-table>
      </a-modal>
      <!--设备修改-->
      <a-modal title="修改设备" :visible="visibleEditDeviceModal" @ok="modifyDevice" @cancel="visibleEditDeviceModal=false">
        <a-form  ref="editDeviceForm" :model="editDevice" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
          <a-form-item label="设备分组">
            <a-select v-model="editDevice.groupId" name="group">
              <template v-for="item in deviceGroups">
                <a-select-option :key="item.id" :value="item.id">{{item.name}}</a-select-option>
              </template>
            </a-select>
          </a-form-item>
          <a-form-item label="设备名称">
            <a-input v-model="editDevice.name"  name="name"></a-input>
          </a-form-item>
          <a-form-item label="主备状态">
            <!--:disabled="selectedDevice.backType === 'MAIN'?'':disabled"-->
            <a-select v-model="editDevice.backType" placeholder="请选择" >
              <a-select-option value="MAIN">主</a-select-option>
              <a-select-option value="BACK">备</a-select-option>
            </a-select>
          </a-form-item>
        </a-form>
      </a-modal>
      <!--设备切换-->
      <a-modal v-model="visibleSwitchDeviceModal" title="设备切换" @ok="switchDevice" @cancel="closeSwitchModal">
        <a-form :label-col="{span:5}" :wrapper-col="{span:12}">
          <a-form-item :label="selectedDevice.backType==='MAIN'? '目标备机':'目标主机'">
            <a-select v-model="switchTargetDeviceId">
              <a-select-option v-for="item in devices" :value="item.id" :key="item.id" v-if="selectedDevice.backType!==item.backType && item.netConfig">{{ item.name }} - {{item.deviceIp}}</a-select-option>
            </a-select>
          </a-form-item>
        </a-form>
      </a-modal>
    </div>
</template>

<script>
  import { mapGetters,mapActions } from 'vuex'
  import {
    addDevice,deleteDevice,saveDeviceConfig,editDevice,resetDeviceConfig,switchDevice,syncDevice,queryAllNetGroup
  } from '../../api/api'
    export default {
        name: "DeviceList",
      data(){
          return {
            expand: false,
            visibleAddDeviceModal: false,
            visibleSwitchDeviceModal: false,
            visibleEditDeviceModal: false,
            visibleConfigDeviceModal: false,
            netCardVisible: false,
            confirmLoading: false,
            resOptVO: {},
            addDeviceForm: this.$form.createForm(this,{name:'dynamic_rule'}),
            addDevice: {
              name: '',
              deviceIp: '',
              devicePort: 5656,
              backType: 'MAIN'
            },
            editDevice:{
              id:'',
              groupId:'',
              name:'',
              backType:'',
            },
            selectedDevice:{},
            switchTargetDeviceId:'',
            netGroups: [],
            netColumns: [
              {dataIndex:'name',key:'name',title:'名称'},
              {dataIndex:'ipv4',key:'ipv4',title:'地址'},
              {dataIndex:'ipv4Mask',key:'ipv4Mask',title:'掩码'},
              {dataIndex:'status',key:'status',title:'状态',scopedSlots: {customRender: 'status'}},
              {dataIndex:'inputNetGroupId',key:'inputNetGroupId',title:'输入',scopedSlots:{customRender:'inputNetGroupId'}},
              {dataIndex:'outputNetGroupId',key:'outputNetGroupId',title:'输出',scopedSlots:{customRender:'outputNetGroupId'}},
            ],
            pagination: {
              onChange: page => {
                console.log(page);
              },
              pageSize: 12,
            },
            form: this.$form.createForm(this, { name: 'advanced_search' }),
          }
      },
      computed: {
        ...mapGetters([
          'deviceGroups','curDeviceGroup'
        ]),
        devices(){
          return this.curDeviceGroup.devices;
        },
        count() {
          return this.expand ? 12 : 8;
        },
      },
      updated() {
        console.log('updated');
      },
      methods: {
        ...mapActions([
          'fetchDeviceGroup'
        ]),
        handleSearch(e) {
          e.preventDefault();
          this.form.validateFields((error, values) => {
            console.log('error', error);
            console.log('Received values of form: ', values);
          });
        },
        handleReset() {
          this.form.resetFields();
        },
        toggle() {
          this.expand = !this.expand;
        },
        syncDevice(id){
          syncDevice({id:id}).then(res=>{
            if (res.status!=200){
              this.$message.error(res.message);
            }else {
              this.fetchDeviceGroup();
              this.$message.info('操作成功');
            }
          })
        },
        openSwitchModal(dev){ //打开设备切换对话框
          if (!dev.netConfig) {
            this.$warning({
              title: '提示',
              content: '请先配置网卡后再进行设备切换',
            });
          }else{
            this.selectedDevice=dev
            this.switchTargetDeviceId=''
            this.visibleSwitchDeviceModal=true
          }
        },
        closeSwitchModal(){
          this.switchTargetDeviceId=''
          this.visibleSwitchDeviceModal=false
        },
        switchDevice(){
          let self =this;
          let body = {};
          body.tgtDevId = self.switchTargetDeviceId;
          body.srcDevId = self.selectedDevice.id;
          switchDevice(body).then(res=>{
            if (res.status!=200){
              this.$message.error(res.message);
              self.fetchDeviceGroup();
              self.closeSwitchModal()
            }else {
              self.fetchDeviceGroup();
              self.$message.info('操作成功');
              self.closeSwitchModal()
            }
          })
        },
        saveConfig(){
          let self =this;
          let nets=[];
          this.selectedDevice.netCards.forEach(y => {
            let n = {}
            n.id = y.id
            n.inputNetGroupId = y.inputNetGroupId
            n.outputNetGroupId = y.outputNetGroupId
            nets.push(n)
          })
          let body={id:this.selectedDevice.id,nets:JSON.stringify(nets)}
          saveDeviceConfig(body).then((res) => {
            self.resOptVO = res.data
            if (!self.resOptVO.beSuccess) {
              self.netCardVisible = true
            }
            if (self.resOptVO.tooltip) {
              alert(self.resOptVO.tooltip)
            }
            this.fetchDeviceGroup()
            self.visibleConfigDeviceModal=false
          })
        },
        netConfigChange(value){
          this.netGroups.splice(this.netGroups.findIndex(item=>item.id===value),1)
        },
        reset(devId){
          //重置只重新配网卡
          let self = this;
          this.$confirm({
            title: '提示',
            content: '是否重置网卡配置？',
            onOk(){
              self.ensureResetDevice(devId)
            }
          })

        },
        ensureResetDevice(devId){
          resetDeviceConfig({id:devId}).then(res=>{
            if (res.status!=200){
              this.$message.error(res.message);
            }else {
              this.fetchDeviceGroup();
              this.$message.info('操作成功');
            }
          })
        },
        deleteDevice(devId){
          deleteDevice({id:devId}).then(res=>{
            this.curDeviceGroup.devices.splice(this.curDeviceGroup.devices.findIndex(item=>item.id===devId),1)
          })
        },
        modifyDevice(){
          let self = this
          if (self.selectedDevice.backType!==self.editDevice.backType && self.selectedDevice.backType === 'MAIN' && self.selectedDevice.taskNum>0){
            this.$error({
              title: '提示',
              content: '主机上存在任务，无法修改成备机',
            });
            //主改备类型要重置任务
            // this.$confirm({
            //   title: '提示',
            //   content: '主机改备机将清空主机任务，是否修改',
            //   onOk(){
            //     self.ensureModifyDevice();
            //   }
            // })
          }else{
            this.ensureModifyDevice();
          }

        },
        ensureModifyDevice(){
          let self = this
          self.editDevice.id = self.selectedDevice.id
          editDevice(self.editDevice).then(res=>{
            if (res.status!=200){
              this.$message.error(res.message);
            }else{
              this.fetchDeviceGroup()
              this.$message.info('操作成功');
              this.visibleEditDeviceModal=false
            }
          })
        },
        saveDevice () {
          let self = this;
          this.addDeviceForm.validateFields(err=>{
            if (!err){
              let d = {
                groupId: this.curDeviceGroup.id,
                name: this.addDevice.name,
                deviceIp: this.addDevice.deviceIp,
                devicePort: this.addDevice.devicePort,
                backType: this.addDevice.backType
              }
              addDevice(d).then(res=>{
                if (res.status!=200){
                  this.$message.error(res.message);
                }else {
                  self.fetchDeviceGroup();
                  self.handleDeviceModalCancel()
                }
              })
            }
          })
        },
        checkIp(rule,value,callback){
          const reg = /^((2[0-4]\d|25[0-5]|[01]?\d\d?)\.){3}(2[0-4]\d|25[0-5]|[01]?\d\d?)$/
          if (!reg.test(value)){
            callback(new Error('ip format error'));
          }
          callback()
        },
        handleDeviceModalCancel() {
          this.confirmLoading = false
          this.visibleAddDeviceModal = false

        },
        init(){
          queryAllNetGroup({}).then(res=>{
            if (res.status!=200){
              this.$message.error(res.message);
            }else {
              this.netGroups = res.data
              this.netGroups.splice(0,0,{id:0,name:'',value:' '})
            }
          })
        }
      },
      created(){

      },
      mounted() {
        this.init()
      }
    }
</script>

<style scoped>

</style>
