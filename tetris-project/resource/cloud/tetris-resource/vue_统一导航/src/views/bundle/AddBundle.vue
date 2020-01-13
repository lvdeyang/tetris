<template>
  <section>
    <el-tabs v-model="activeTabName" @tab-click="handleTabClick" style="float:left;width:100%;">
      <el-tab-pane label="资源列表" name="BundleManage"></el-tab-pane>
      <el-tab-pane label="添加资源" name="AddBundle"></el-tab-pane>
    </el-tabs>

    <el-form :model="bundleForm" :rules="rules" ref="bundleForm" label-width="100px" >
      <el-form-item size="small" label="设备形态" prop="deviceModel">
        <el-select size="small" v-model="bundleForm.deviceModel" style="width: 200px;">
          <el-option v-for="item in deviceModelOptions" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item size="small" v-show="bundleForm.deviceModel=='jv210'" label="编解码类型" prop="coderType">
        <el-select v-model="bundleForm.coderType" style="width: 200px;">
          <el-option v-for="item in coderTypeOptions" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>

      <!--<el-form-item label="设备类型" prop="bundleType">-->
        <!--<el-select v-model="bundleForm.bundleType" style="width: 200px;">-->
          <!--<el-option v-for="item in bundleTypeOptions" :key="item.value" :label="item.label" :value="item.value">-->
          <!--</el-option>-->
        <!--</el-select>-->
      <!--</el-form-item>-->

      <el-form-item size="small" label="别名" prop="bundleAlias">
        <el-input v-model="bundleForm.bundleAlias" style="width: 200px;"></el-input>
      </el-form-item>

      <el-form-item size="small" label="设备名称" prop="bundleName">
        <el-input v-model="bundleForm.bundleName" style="width: 200px;"></el-input>
      </el-form-item>

      <el-form-item size="small" label="设备账号" prop="username">
        <el-input v-model="bundleForm.username" style="width: 200px;"></el-input>
      </el-form-item>

      <el-form-item size="small" label="设备密码" prop="onlinePassword">
        <el-input type="password" v-model="bundleForm.onlinePassword" auto-complete="off" style="width: 200px;"></el-input>
      </el-form-item>

      <el-form-item size="small" label="确认密码" prop="checkPass">
        <el-input type="password" v-model="bundleForm.checkPass" auto-complete="off" style="width: 200px;"></el-input>
      </el-form-item>

      <!-- TODO -->
      <el-form-item size="small" v-show="bundleForm.deviceModel=='jv210'" label="接入层UID" prop="accessNodeUid">
        <el-input v-model="bundleForm.accessNodeUid" style="width: 200px;"></el-input>
      </el-form-item>

      <el-form-item size="small" label="设备IP">
        <el-input v-model="bundleForm.deviceAddr.deviceIp" style="width: 200px;"></el-input>
      </el-form-item>

      <el-form-item size="small" label="设备端口">
        <el-input v-model="bundleForm.deviceAddr.devicePort" style="width: 200px;"></el-input>
      </el-form-item>


      <el-button style="margin-top:10px; margin-left: 30px" type="info" size="small" @click="addExtraInfo">新增扩展字段</el-button>

      <div style="margin-top:10px; margin-left: 30px" v-for="(extraInfo, index) in extraInfos">
        <el-input size="small"  v-model="extraInfo.name" placeholder="扩展字段名" style="width: 180px;"></el-input>
        <el-input size="small"  v-model="extraInfo.value" placeholder="扩展字段值" style="width: 180px;margin-left: 10px;"></el-input>
        <el-button size="small"  type="danger" @click.prevent="remove(extraInfo)" style="margin-left: 10px;">删除</el-button>
      </div>

      <div style="margin-top:30px; margin-left: 30px" >
        <el-button size="small"  type="primary" @click="submit()">提交</el-button>
        <el-button size="small"  type="primary" @click="reset()">重置</el-button>
      </div>

    </el-form>

  </section>
</template>

<script type="text/ecmascript-6">
  import {getDeviceModels,addBundle} from '../../api/api';

  export default {
    name: "AddBundle",
    data(){
      var validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
        } else {
          if (this.bundleForm.checkPass !== '') {
            this.$refs.bundleForm.validateField('checkPass');
          }
          callback();
        }
      };
      var validateCheckPass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.bundleForm.onlinePassword) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      return {
        activeTabName : "AddBundle",
        extraInfos : [],
        bundleForm : {
          deviceModel : "",
          bundleType : "",
          bundleName : "",
          username : "",
          onlinePassword : "",
          checkPass : "",
          bundleAlias : "",
          accessLayerUid : "",
          deviceAddr : {
            deviceIp : "",
            devicePort : 5060
          },
          coderType : "DEFAULT"
          // accessNodeUid : ""
        },
        rules : {
          deviceModel: [
            { required: true, message: '请选择设备形态', trigger: 'change' }
          ],
          // bundleType: [
          //   { required: true, message: '请选择设备类型', trigger: 'change' }
          // ],
          bundleName: [
            { required: true, message: '请输入设备名称', trigger: 'blur' },
            { min: 1, max: 30, message: '长度在 1 到 30 个字符', trigger: 'blur' }
          ],
          username: [
            { required: true, message: '请输入设备账号', trigger: 'blur' }
          ],

          onlinePassword: [
            {validator : validatePass, trigger: 'blur'}
          ],
          checkPass: [
            { validator: validateCheckPass, trigger: 'blur' }
          ]//,
          // accessNodeUid: [
          //   { required: true, message: '请输入接入层标识', trigger: 'blur' }
          // ]
        },
        deviceModelOptions : [],
        coderTypeOptions : [
          {
            label : "编码器",
            value : "ENCODER"
          },
          {
            label : "解码器",
            value : "DECODER"
          },
          {
            label : "默认",
            value : "DEFAULT"
          },
        ],
        bundleTypeOptions : [
          {
            label : "终端",
            value : "VenusTerminal"
          },
          {
            label : "混合器",
            value : "VenusMixer"
          },
          {
            label : "转发器",
            value : "VenusTransporter"
          },
          {
            label : "外部输出设备",
            value : "VenusOutConnection"
          },
          {
            label : "大屏设备",
            value : "VenusVideoMatrix"
          },
        ]
      };
    },
    methods : {
      handleTabClick(tab, event) {
        if("AddBundle" !== tab.name){
          this.$router.push('/' + tab.name);
        }
      },
      getDeviceModels : function(){
        getDeviceModels().then(res => {
          if(!res.errMsg && res.deviceModels){
            for(let deviceModel of res.deviceModels){
                let deviceModelOption = {
                  value : deviceModel,
                  label : deviceModel
                };
                this.deviceModelOptions.push(deviceModelOption);
            }
          }
        });
      },
      addExtraInfo : function(){
        this.extraInfos.push({});
      },
      remove : function(item){
        var index = this.extraInfos.indexOf(item);
        if (index !== -1) {
          this.extraInfos.splice(index, 1)
        }
      },
      reset : function(){
        this.$refs["bundleForm"].resetFields();
      },
      submit : function(){
        if(!this.validateBaseInfo()){
          return;
        }

        if(!this.validateExtraInfo()){
          return;
        }

        let param = {
          bundle : JSON.stringify(this.bundleForm),
          extraInfoVOList : JSON.stringify(this.extraInfos)
        };

        addBundle(param).then(res => {
          if(res.errMsg){
            this.$message({
              message: res.errMsg,
              type: 'error'
            });
          }else{
            this.$message({
              message: "添加成功",
              type: 'success'
            });
            setTimeout(() => {
              window.location.reload();
            },1000);

            // this.$confirm('添加资源成功, 是否跳转至配置页面进行能力方案配置?', '提示', {
            //   confirmButtonText: '确定',
            //   cancelButtonText: '取消',
            //   type: 'info'
            // }).then(() => {
            //   //跳转至配置页面
            //   this.$router.push({
            //     path: '/ConfigBundle',
            //     query: {
            //       bundleId: res.bundleId
            //     }
            //   });
            // }).catch(() => {
            // });
          }
        });
      },
      validateBaseInfo : function(){
        var result = false;
        this.$refs["bundleForm"].validate((valid) => {
          result = valid;
        });
        return result;
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
    var self = this;
      this.$nextTick(function(){
        self.$parent.$parent.$parent.$parent.$parent.setActive('/BundleManage');
      });
      this.getDeviceModels();
    }
  }
</script>

<style scoped>

</style>
