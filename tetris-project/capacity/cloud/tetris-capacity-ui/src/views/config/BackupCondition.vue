<template>
<div>
  <a-card >
    <a-form v-model="backupCondition" layout="inline">
      <a-row :gutter="24">
        <a-col :span="8">
          <a-form-item label="输入网卡异常">
            <a-select v-model="backupCondition.inputNetCardError" style="width:180px;" @change="modifyBackupCondition">
              <a-select-option value="NONE">不触发备份</a-select-option>
              <a-select-option value="ANY">任意网口异常触发</a-select-option>
              <a-select-option value="ALL">全部异常触发</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="输出网卡异常">
            <a-radio-group v-model="backupCondition.outputNetCardError" @change="modifyBackupCondition">
              <a-radio :value="true">触发</a-radio>
              <a-radio :value="false">不触发</a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="控制口断连">
            <a-radio-group v-model="backupCondition.ctrlPortDisconnect" @change="modifyBackupCondition">
              <a-radio :value="true">触发</a-radio>
              <a-radio :value="false">不触发</a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>
      </a-row>

    </a-form>


  </a-card>

</div>
</template>

<script>
  import {getBackupCondition,editBackupCondition } from '../../api/api'
    export default {
        name: "BackupCondition",
        data() {
          return {
            backupCondition:{
              inputNetCardError:'ALL',
              outputNetCardError:true,
              ctrlPortDisconnect:false,
              cpuOverride:false,
              gpuOverride:false,
              memOverride:false,
              diskOverride:false
            },
          }
        },
        methods:{
          modifyBackupCondition(){
            editBackupCondition(this.backupCondition).then(res=>{
              if (res.status!=200){
                this.$message.error(res.message);
              }else {
                this.backupCondition = res.data
                this.$message.info("修改成功");
              }
            })
          },
          getBackupCondition () {
            let self = this;
            getBackupCondition().then(res => {
              if (res.status!=200){
                this.$message.error(res.message);
              }else {
                this.backupCondition = res.data
              }
            });
          },
        },
      created(){
        this.getBackupCondition();
      },
    }
</script>

<style scoped>

</style>
