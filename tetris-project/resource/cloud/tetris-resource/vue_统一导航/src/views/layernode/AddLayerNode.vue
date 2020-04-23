<template>
  <section>
    <el-tabs v-model="activeTabName" @tab-click="handleTabClick" style="float:left; width:100%;">
      <el-tab-pane label="层节点列表" name="LayernodeManage"></el-tab-pane>
      <el-tab-pane label="添加层节点" name="AddLayernode"></el-tab-pane>
    </el-tabs>

    <el-form :model="nodeForm" :rules="rules" ref="nodeForm" label-width="100px" >
      <el-form-item label="节点类型" prop="type">
        <el-select v-model="nodeForm.type" style="width: 200px;">
          <el-option v-for="item in typeOptions" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="节点名称" prop="name">
        <el-input v-model="nodeForm.name" style="width: 200px;"></el-input>
      </el-form-item>

      <el-form-item label="节点ID" prop="nodeUid">
        <el-input v-model="nodeForm.nodeUid" style="width: 200px;"></el-input>
      </el-form-item>

      <el-form-item label="节点IP" prop="ip">
        <el-input v-model="nodeForm.ip" style="width: 200px;"></el-input>
      </el-form-item>

      <el-form-item label="节点端口" prop="port">
        <el-input v-model="nodeForm.port" style="width: 200px;"></el-input>
      </el-form-item>

      <el-form-item label="下载端口" prop="downloadPort">
        <el-input v-model="nodeForm.downloadPort" style="width: 200px;" placeholder="录制资源下载端口，可不填"></el-input>
      </el-form-item>

      <el-form-item label="监控地址" prop="monitorUrl">
        <el-input v-model="nodeForm.monitorUrl" style="width: 200px;" placeholder="监控页面地址，可不填"></el-input>
      </el-form-item>

      <el-form-item label="网管地址" prop="netUrl">
        <el-input v-model="nodeForm.netUrl" style="width: 200px;" placeholder="网管页面地址，可不填"></el-input>
      </el-form-item>

      <el-form-item v-show="nodeForm.type=='ACCESS_WEBRTC'" label="webrtc http端口" prop="webrtcHttpPort">
        <el-input v-model="nodeForm.webrtcHttpPort" style="width: 200px;"></el-input>
      </el-form-item>

      <el-form-item v-show="nodeForm.type=='ACCESS_WEBRTC'" label="webrtc websocket端口" prop="webrtcWebsocketPort">
        <el-input v-model="nodeForm.webrtcWebsocketPort" style="width: 200px;"></el-input>
      </el-form-item>

      <!--<el-form-item label="访问地址" prop="url">-->
        <!--<el-input v-model="nodeForm.url" style="width: 200px;"></el-input>-->
      <!--</el-form-item>-->

      <div style="margin-top:30px;" >
        <el-button type="primary" @click="submit()">提交</el-button>
        <el-button type="primary" @click="reset()">重置</el-button>
      </div>

    </el-form>

  </section>
</template>

<script type="text/ecmascript-6">
  import {saveNode} from '../../api/api';

  export default {
    name: "AddLayernode",
    data(){
      var checkIp = (rule, value, callback) => {
        let ip_reg = /^((2[0-4]\d|25[0-5]|[01]?\d\d?)\.){3}(2[0-4]\d|25[0-5]|[01]?\d\d?)$/;
        if(value && !ip_reg.test(value)){
          callback(new Error("IP格式错误"));
        } else {
          callback();
        }
      };
      var checkPort = (rule, value, callback) => {
        let port_reg = /^([0-9]|[1-9]\d{1,3}|[1-5]\d{4}|6[0-4]\d{4}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/;
        if(value && !port_reg.test(value)){
          callback(new Error("端口格式错误"));
        } else {
          callback();
        }
      };

      return {
        activeTabName : "AddLayernode",
        typeOptions : [
          // {
          //   label : "JV210接入",
          //   value : "ACCESS_JV210"
          // },
          // {
          //   label : "CDN接入",
          //   value : "ACCESS_CDN"
          // },
          // {
          //   label : "IPC接入",
          //   value : "ACCESS_IPC"
          // },
          {
            label : "JV230接入",
            value : "ACCESS_JV230"
          },
          {
            label : "机顶盒接入",
            value : "ACCESS_TVOS"
          },
          {
            label : "混合器接入",
            value : "ACCESS_MIXER"
          },
          {
            label : "手机接入",
            value : "ACCESS_MOBILE"
          },
          {
            label : "音视频转发服务设备",
            value : "ACCESS_JV210"
          },
          {
            label : "录像存储服务单元A型",
            value : "ACCESS_CDN"
          },
          {
            label : "录像存储服务单元B型",
            value : "ACCESS_VOD"
          },
          {
            label : "监控资源汇接网关",
            value : "ACCESS_IPC"
          },
          {
            label : "流媒体管理服务设备",
            value : "ACCESS_STREAMMEDIA"
          },
          {
            label : "联网服务设备",
            value : "ACCESS_NETWORK"
          },
          {
            label : "显控汇接网关",
            value : "ACCESS_DISPLAYCTRL"
          },
          {
            label : "流转发器接入",
            value : "ACCESS_S100"
          },
          {
            label : "点播代理服务设备",
            value : "ACCESS_VODPROXY"
          },
          {
            label : "webrtc接入",
            value : "ACCESS_WEBRTC"
          },
          {
            label : "联网接入",
            value : "ACCESS_LIANWANG"
          },
          {
            label : "LDAP目录资源服务设备",
            value : "ACCESS_LDAP"
          },
          {
            label : "综合运维服务设备",
            value : "ACCESS_OMMS"
          }
        ],
        nodeForm : {
          type : "ACCESS_JV210",
          name : "",
          ip : "",
          nodeUid : "",
          port : null,
          downloadPort : null,
          monitorUrl:'',
          netUrl:'',
          webrtcHttpPort:8088,
          webrtcWebsocketPort:8188
        },
        rules : {
          type: [
            { required: true, message: '请选择节点类型', trigger: 'change' }
          ],
          name: [
            { required: true, message: '请输入节点名称', trigger: 'change' },
            { min: 1, max: 30, message: '长度在 1 到 30 个字符', trigger: 'blur' }
          ],
          nodeUid: [
            { required: true, message: '请输入节点ID', trigger: 'change' },
            { min: 1, max: 30, message: '长度在 1 到 30 个字符', trigger: 'blur' }
          ],
          ip: [
            {validator : checkIp, trigger: 'blur'}
          ],
          port : [
            {validator : checkPort, trigger: 'blur'}
          ],
          downloadPort : [
            {validator : checkPort, trigger: 'blur'}
          ],
        }
      };
    },
    methods : {
      handleTabClick(tab, event) {
        if("AddLayernode" !== tab.name){
          this.$router.push('/' + tab.name);
        }
      },
      reset : function(){
        this.$refs["nodeForm"].resetFields();
      },
      submit : function(){
        if(!this.validate()){
          return;
        }

        let param = {
          json : JSON.stringify(this.nodeForm)
        };

        saveNode(param).then(res => {
          if(res.errMsg){
            this.$message({
              message: res.errMsg,
              type: 'error'
            });
          } else {
            // this.$alert("添加成功，该接入层节点ID为" + res.nodeUid, '提示信息', {
            //   confirmButtonText: '确定'
            // });
            this.$message({
              message: "添加成功",
              type: 'success'
            });
            this.$refs["nodeForm"].resetFields();
          }
        });
      },
      validate : function(){
        var result = false;
        this.$refs["nodeForm"].validate((valid) => {
          result = valid;
        });
        return result;
      }
    },
    mounted() {
    var self = this;
    this.$nextTick(function() {
      self.$parent.$parent.$parent.$parent.$parent.setActive('/LayernodeManage');
    });
    }
  }
</script>

<style scoped>

</style>
