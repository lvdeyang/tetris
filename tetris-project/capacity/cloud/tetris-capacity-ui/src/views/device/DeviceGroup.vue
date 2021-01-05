<template>
  <div style="height:800px;overflow-y: auto">
    <a-row :gutter="16" type="flex" align="top" v-if="curDeviceGroup!=={}&&curDeviceGroup.devices!==undefined" >
      <a-col :span="5" >
        <a-card hoverable >
          <a-row type="flex" justify="center">
            <a-avatar :size="84" style="color: #f56a00; backgroundColor: #fde3cf">{{curDeviceGroup.name}}</a-avatar>
          </a-row>
          <a-row style="margin-left:25%;margin-top:15px;">
            <span>
              <a-icon type="book" /> 备份模式： {{curDeviceGroup.backupStrategy==='NPLUSM'?'N+M':'1+1'}}
            </span>
          </a-row>
          <a-row style="margin-left:25%;margin-top:10px;">
            <span>
              <a-icon type="pic-right" /> 自动备份：
              <a-switch checked-children="开" un-checked-children="关" v-model="curDeviceGroup.autoBackupFlag" @change="changeAutoBackupFlag" default-checked />
            </span>
          </a-row>


          <a-divider dashed  orientation="left"></a-divider>
          <a-row style="margin-left:10px;">
            <a-col :span="10" >
              <a-statistic title="设备数" :value="curDeviceGroup.devices.length"></a-statistic>
            </a-col>
            <a-col :span="7">
              <a-statistic title="主机" :value="curDeviceGroup.devices.filter(d=>d.backType==='MAIN').length"></a-statistic>
            </a-col>
            <a-col :span="7">
              <a-statistic title="备机" :value="curDeviceGroup.devices.filter(d=>d.backType!=='MAIN').length"></a-statistic>
            </a-col>
          </a-row>
          <a-row  style="margin-top:10px;margin-left:10px;">
            <a-col :span="10" >
              <a-statistic title="在线设备" :value="curDeviceGroup.devices.filter(d=>d.funUnitStatus==='NORMAL').length">
                <template #suffix>
                  <span> / {{curDeviceGroup.devices.length}}</span>
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="7">
              <a-statistic title="主机" :value="curDeviceGroup.devices.filter(d=>d.funUnitStatus==='NORMAL' && d.backType==='MAIN').length">
                <template #suffix>
                  <span> / {{curDeviceGroup.devices.filter(d=>d.backType==='MAIN').length}}</span>
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="7">
              <a-statistic title="备机" :value="curDeviceGroup.devices.filter(d=>d.funUnitStatus==='NORMAL' && d.backType!=='MAIN').length">
                <template #suffix>
                  <span> / {{curDeviceGroup.devices.filter(d=>d.backType!=='MAIN').length}}</span>
                </template>
              </a-statistic>
            </a-col>
          </a-row>
          <!--已配置-->
          <a-row  style="margin-top:10px;margin-left:10px;">
            <a-col :span="10" >
              <a-statistic title="已配置设备" :value="curDeviceGroup.devices.filter(d=>d.netConfig).length">
                <template #suffix>
                  <span> / {{curDeviceGroup.devices.length}}</span>
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="7">
              <a-statistic title="主机" :value="curDeviceGroup.devices.filter(d=>d.netConfig && d.backType==='MAIN').length">
                <template #suffix>
                  <span> / {{curDeviceGroup.devices.filter(d=>d.backType==='MAIN').length}}</span>
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="7">
              <a-statistic title="备机" :value="curDeviceGroup.devices.filter(d=>d.netConfig && d.backType!=='MAIN').length">
                <template #suffix>
                  <span> / {{curDeviceGroup.devices.filter(d=>d.backType!=='MAIN').length}}</span>
                </template>
              </a-statistic>
            </a-col>
          </a-row>
          <a-row style="margin-top:10px;margin-left:10px;">
            <a-col :span="10" >
              <a-statistic title="任务数" :value="curDeviceGroup.taskNum"></a-statistic>
            </a-col>
          </a-row>
          <a-divider dashed ></a-divider>
          <a-row type="flex" justify="center" :gutter="20">
            <a-col>
              <a-button type="primary" size="small" @click="visibleEditGroupModal=true;editDeviceGroupObj.name=curDeviceGroup.name;editDeviceGroupObj.backupStrategy=curDeviceGroup.backupStrategy">修改分组</a-button>
            </a-col>
            <a-col>
              <a-button type="danger" size="small" @click="deleteDeviceGroup(curDeviceGroup.name)">删除分组</a-button>
            </a-col>
          </a-row>
          <!--<a-button>备份开关</a-button>-->
        </a-card>
      </a-col>
      <a-col :span="19" >
            <device-list></device-list>
      </a-col>
    </a-row>
    <!--修改分组对话框-->
    <a-modal v-model="visibleEditGroupModal" title="修改分组" @ok="editDeviceGroup()">
      <a-form :label-col="{span:8}" :wrapper-col="{span:8}">
        <a-form-item label="分组名称">
          <a-input v-model="editDeviceGroupObj.name"></a-input>
        </a-form-item>
        <a-form-item label="备份模式">
          <a-select v-model="editDeviceGroupObj.backupStrategy" disabled>
            <a-select-option value="NPLUSM">N+M</a-select-option>
            <a-select-option value="ONE2ONE">1+1</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>


</template>

<script>
  import { mapGetters,mapActions } from 'vuex'
  import {
    editDeviceGroup,changeAutoBackup
  } from '../../api/api'
  import DeviceList from "./DeviceList"

    export default {
      name: "DeviceGroup",
      components: {DeviceList},

      data(){
        return{
          config:false,
          editDeviceGroupObj:{
            id:'',
            name:'',
            backupStrategy: ''
          },
        }
      },
      watch:{

      },
      computed:{
        ...mapGetters([
          'deviceGroups','curDeviceGroup'
        ]),
        devices(){
          return this.curDeviceGroup.devices;
        }
      },
      methods: {
        ...mapActions([
          'fetchDeviceGroup'
        ]),
        changeAutoBackupFlag(checked){
          let self =this
          let body={
            id:self.curDeviceGroup.id,
            flag:checked
          }
          changeAutoBackup(body).then(res=>{
            if (res.status!=200){
              this.$message.error(res.message);
            }
          })
        },
        editDeviceGroup(){
          let body = this.editDeviceGroupObj;
          body.id=this.curDeviceGroup.id;
          editDeviceGroup(body).then(res=>{
            if (res.status!=200){
              this.$message.error(res.message);
            }else {
              this.fetchDeviceGroup();
              this.$message.info('操作成功');
              this.editDeviceGroupObj={}
              this.visibleEditGroupModal=false;
            }
          })
        },
        deleteDeviceGroup(name){
          let self = this;
          this.$confirm({
            title: '提示',
            content: '删除设备分组和该分组下所有设备？',
            onOk(){
              self.$emit('deleteDeviceGroup',name)
            }
          })
        },
      },
      created(){

      },
      mounted() {
      }
    }
</script>

<style scoped>

</style>
