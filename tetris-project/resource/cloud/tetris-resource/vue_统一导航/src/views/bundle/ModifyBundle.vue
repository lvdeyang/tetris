<template>
    <section>
      <el-tabs v-model="activeTabName" @tab-click="handleTabClick" style="float:left; width:100%;">
        <el-tab-pane label="资源列表" name="BundleManage"></el-tab-pane>
        <el-tab-pane label="修改资源" name="ModifyBundle"></el-tab-pane>
      </el-tabs>

      <el-form label-width="100px" >
          <el-form-item size="small" label="设备名称" prop="bundleName">
            <el-input v-model="bundleName" style="width: 200px;"  placeholder="输入新的设备名称"></el-input>
          </el-form-item>

          <el-form-item size="small" label="设备IP" prop="deviceIp">
            <el-input v-model="deviceIp" style="width: 200px;"  placeholder="输入新的设备IP"></el-input>
          </el-form-item>
          
          <el-form-item size="small" label="设备端口" prop="devicePort">
            <el-input v-model="devicePort" style="width: 200px;"  placeholder="输入新的设备端口"></el-input>
          </el-form-item>

          <el-form-item size="small" label="编码组播">
            <el-switch v-model="multicastEncode" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
          </el-form-item>

          <el-form-item size="small" label="编码组播地址">
            <el-input v-if="multicastEncode" v-model="multicastEncodeAddr" style="width: 200px;"></el-input>
            <el-input v-else v-model="multicastEncodeAddr" style="width: 200px;" disabled></el-input>
          </el-form-item>

          <el-form-item size="small" label="解码组播">
            <el-switch v-model="multicastDecode" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
          </el-form-item>
          <!--
          <div style="margin-top:10px;">
            <el-input v-model="bundleName" placeholder="输入新的设备名称" style="width: 300px;">
              <template slot="prepend">设备名称</template>
            </el-input>
          </div>
          -->

          <el-button type="info" size="small" @click="addExtraInfo" style="margin-top:20px; margin-left: 30px">新增扩展字段</el-button>

          <div style="margin-top:10px;" v-for="(extraInfo, index) in extraInfos">
            <el-input size="small" v-model="extraInfo.name" placeholder="扩展字段名" style="width: 180px;"></el-input>
            <el-input size="small" v-model="extraInfo.value" placeholder="扩展字段值" style="width: 180px;margin-left: 10px;"></el-input>
            <el-button size="small" type="danger" @click.prevent="remove(extraInfo)" style="margin-left: 10px;">删除</el-button>
          </div>

          <div style="margin-top:30px; margin-left: 30px" >
            <!--<el-button type="info" @click="addExtraInfo">新增扩展字段</el-button>-->
            <el-button size="small" type="primary" @click="submit">提交</el-button>
          </div>
      
      </el-form>
    </section>
</template>

<script type="text/ecmascript-6">
  import {queryBundleExtraInfo,modifyBundleExtraInfo} from '../../api/api';

    export default {
      name: "ModifyBundle",
      data(){
        return {
          activeTabName : "ModifyBundle",
          bundleId : this.$route.query.bundleId,
          bundleName : this.$route.query.bundleName,
          deviceIp : this.$route.query.deviceIp,
          devicePort : this.$route.query.devicePort,
          multicastEncode:this.$route.query.multicastEncode,
          multicastEncodeAddr:this.$route.query.multicastEncodeAddr,
          multicastDecode:this.$route.query.multicastDecode,
          extraInfos : []
        };
      },
      methods : {
        handleTabClick(tab, event) {
          if("ModifyBundle" !== tab.name){
            this.$router.push('/' + tab.name);
          }
        },
        queryBundleExtraInfo : function(){
          let param = {
            bundleId : this.bundleId
          };

          queryBundleExtraInfo(param).then(res => {
            if(res.errMsg){
              this.$message({
                message: res.errMsg,
                type: 'error'
              });
            }else{
              this.extraInfos = res.extraInfos;
            }
          });
        },
        addExtraInfo : function(){
          this.extraInfos.push({});
        },
        remove : function(item){
          var index = this.extraInfos.indexOf(item);
          if (index !== -1) {
            this.extraInfos.splice(index, 1);
          }
        },
        submit : function(){
          if(!this.validateExtraInfo()){
            return ;
          }

          let param = {
            bundleId : this.bundleId,
            bundleName : this.bundleName,
            deviceIp : this.deviceIp,
            devicePort : this.devicePort,
            multicastEncode:this.multicastEncode,
            multicastEncodeAddr:this.multicastEncodeAddr,
            multicastDecode:this.multicastDecode,
            extraInfos : JSON.stringify(this.extraInfos)
          };

          modifyBundleExtraInfo(param).then(res => {
            if(res.errMsg){
              this.$message({
                message: res.errMsg,
                type: 'error'
              });
            }else{
              this.$message({
                message: "修改成功",
                type: 'success'
              });
            }
          });
        },
        validateExtraInfo : function(){
          for(let extraInfo of this.extraInfos){
            if(!extraInfo.name || !extraInfo.value){
              this.$message({
                message: "扩展字段不能为空",
                type: 'error'
              });
              return false;
            }
          }
          return true;
        }
      },
      mounted() {
        this.queryBundleExtraInfo();
      }
    }
</script>

<style scoped>

</style>
