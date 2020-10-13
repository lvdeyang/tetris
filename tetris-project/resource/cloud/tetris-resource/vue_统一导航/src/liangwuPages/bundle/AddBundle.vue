<template>
  <section>
    <el-tabs v-model="activeTabName" @tab-click="handleTabClick" style="float:left;width:100%;">
      <el-tab-pane label="资源列表" name="LwLocalBundleManage"></el-tab-pane>
      <!-- <el-tab-pane label="外域资源列表" name="BundleManage"></el-tab-pane> -->
      <el-tab-pane label="添加资源" name="LwAddBundle"></el-tab-pane>
    </el-tabs>

    <el-form :model="bundleForm" :rules="rules" ref="bundleForm" label-width="100px">
      <!-- <el-form-item size="small" label="资源域" prop="deviceDomain">
        <el-select size="small" v-model="bundleForm.deviceDomain" style="width: 200px;">
          <el-option v-for="item in deviceDomainOptions" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item> -->

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

      <el-form-item size="small" v-show="bundleForm.deviceModel=='ws'" label="编解码类型" prop="coderType">
        <el-select v-model="bundleForm.coderType" style="width: 200px;">
          <el-option v-for="item in wsTypeOptions" :key="item.value" :label="item.label" :value="item.value">
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

      <el-form-item size="small" label="地点" prop="bundleAlias">
        <el-input v-model="bundleForm.location" style="width: 200px;"></el-input>
      </el-form-item>

      <el-form-item size="small" label="设备名称" prop="bundleName">
        <el-input v-model="bundleForm.bundleName" style="width: 200px;"></el-input>
      </el-form-item>

      <el-form-item size="small" label="设备分组" prop="bundleFolderName">
        <el-input v-model="bundleForm.bundleFolderName" style="width: 200px;" readOnly @click.native="handleChangeFolder"></el-input>
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
      <el-form-item size="small" v-show="bundleForm.deviceModel=='jv210' || bundleForm.deviceModel=='ws'" label="接入层UID" prop="accessNodeUid">
        <el-input v-model="bundleForm.accessNodeUid" style="width: 200px;" readOnly @click.native="handleSelectLayerNode"></el-input>
      </el-form-item>

      <el-form-item size="small" label="设备IP">
        <el-input v-model="bundleForm.deviceAddr.deviceIp" style="width: 200px;"></el-input>
      </el-form-item>

      <el-form-item size="small" label="设备端口">
        <el-input v-model="bundleForm.deviceAddr.devicePort" style="width: 200px;"></el-input>
      </el-form-item>

      <el-form-item size="small" label="编码组播">
        <el-switch v-model="bundleForm.multicastEncode" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
      </el-form-item>

      <el-form-item size="small" label="编码组播地址">
        <el-input v-if="bundleForm.multicastEncode" v-model="bundleForm.multicastEncodeAddr" style="width: 200px;"></el-input>
        <el-input v-else v-model="bundleForm.multicastEncodeAddr" style="width: 200px;" disabled></el-input>
      </el-form-item>

      <el-form-item size="small" label="解码组播">
        <el-switch v-model="bundleForm.multicastDecode" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
      </el-form-item>
      <el-form-item size="small" label="是否转码">
        <el-switch v-model="bundleForm.transcod" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
      </el-form-item>
      <el-form-item size="small" label="源组播Ip">
        <el-input v-model="bundleForm.multicastSourceIp" style="width: 200px;"></el-input>
      </el-form-item>
      <el-form-item size="small" v-show="bundleForm.deviceModel=='ipc' || bundleForm.deviceModel=='speaker'" label="坐标经度(°)" prop="longitude">
        <el-input v-model="bundleForm.longitude" style="width: 200px;"></el-input>
      </el-form-item>

      <el-form-item size="small" v-show="bundleForm.deviceModel=='ipc' || bundleForm.deviceModel=='speaker'" label="坐标纬度(°)" prop="latitude">
        <el-input v-model="bundleForm.latitude" style="width: 200px;"></el-input>
      </el-form-item>

      <el-form-item size="small" v-show="bundleForm.deviceModel=='ipc'" label="流地址" prop="streamUrl">
        <el-input v-model="bundleForm.streamUrl" style="width: 200px;"></el-input>
      </el-form-item>

      <el-form-item size="small" v-show="bundleForm.deviceModel=='speaker'" label="标识" prop="identify">
        <el-input v-model="bundleForm.identify" style="width: 200px;"></el-input>
      </el-form-item>

      <el-button style="margin-top:10px; margin-left: 30px" type="info" size="small" @click="addExtraInfo">新增扩展字段</el-button>

      <div style="margin-top:10px; margin-left: 30px" v-for="(extraInfo, index) in extraInfos">
        <el-input size="small" v-model="extraInfo.name" placeholder="扩展字段名" style="width: 180px;"></el-input>
        <el-input size="small" v-model="extraInfo.value" placeholder="扩展字段值" style="width: 180px;margin-left: 10px;"></el-input>
        <el-button size="small" type="danger" @click.prevent="remove(extraInfo)" style="margin-left: 10px;">删除</el-button>
      </div>

      <div style="margin-top:30px; margin-left: 30px">
        <el-button size="small" type="primary" @click="submit()">提交</el-button>
        <el-button size="small" type="primary" @click="reset()">重置</el-button>
      </div>

    </el-form>

    <template>
      <el-dialog title="选择分组" :visible.sync="dialog.changeFolder.visible" width="650px" :before-close="handleChangeFolderClose">
        <div style="height:500px; position:relative;">
          <el-scrollbar style="height:100%;">
            <el-tree ref="changeFolderTree" :props="dialog.changeFolder.tree.props" :data="dialog.changeFolder.tree.data" node-key="id" check-strictly :expand-on-click-node="false" default-expand-all highlight-current @current-change="currentTreeNodeChange">

              <span style="flex:1; display:flex; align-items:center; justify-content:space-between; padding-right:8px;" slot-scope="{node, data}">
                <span style="font-size:14px;">
                  <span class="icon-folder-close"></span>
                  <span>{{data.name}}</span>
                </span>
                <span>
                  <el-button type="text" size="mini" style="padding:0;" @click.stop="treeNodeAppend(node, data)">
                    <span style="font-size:16px;" class="icon-plus"></span>
                  </el-button>
                </span>
              </span>

            </el-tree>
          </el-scrollbar>
        </div>
        <span slot="footer" class="dialog-footer">
          <el-button size="medium" @click="handleChangeFolderClose">取消</el-button>
          <el-button size="medium" type="primary" @click="handleChangeFolderCommit">确定</el-button>
        </span>
      </el-dialog>

      <el-dialog title="添加分组" :visible.sync="dialog.addFolder.visible" width="450px" :before-close="handleAddFolderClose">
        <el-form label-width="110px">
          <el-form-item label="分组名称">
            <el-input v-model="dialog.addFolder.folderName"></el-input>
          </el-form-item>
          <el-form-item label="是否同步ldap">
            <el-select v-model="dialog.addFolder.toLdap">
              <el-option key="是" label="是" value="是">
                是
              </el-option>
              <el-option key="否" label="否" value="否">
                否
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button size="medium" @click="handleAddFolderClose">取消</el-button>
          <el-button size="medium" type="primary" @click="handleAddFolderCommit">确定</el-button>
        </span>
      </el-dialog>

    </template>

    <template>

      <selectLayerNode ref="selectLayerNode" @layer-node-selected="layerNodeSelected"></selectLayerNode>

    </template>
  </section>
</template>

<script type="text/ecmascript-6">
import {
  getDeviceModels,
  addBundle,
  queryFolderTree,
  initFolderTree,
  addFolder
} from '../../api/api';

import selectLayerNode from '../layernode/SelectLayerNode'


export default {
  name: "AddBundle",
  components: { selectLayerNode },
  data: function () {
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
      activeTabName: "LwAddBundle",
      extraInfos: [],
      bundleForm: {
        deviceDomain: "",
        deviceModel: "",
        bundleType: "",
        bundleName: "",
        location: "",
        username: "",
        onlinePassword: "",
        checkPass: "",
        bundleAlias: "",
        accessNodeUid: "",
        bundleFolderId: null,
        bundleFolderName: '根目录',
        transcod: false,
        multicastSourceIp: '',
        deviceAddr: {
          deviceIp: "",
          devicePort: 5060
        },
        multicastEncode: false,
        multicastEncodeAddr: '',
        multicastDecode: false,
        coderType: "DEFAULT",
        longitude: '',
        latitude: '',
        streamUrl: '',
        streamUrl: ''
        // accessNodeUid : ""
      },
      rules: {
        deviceModel: [
          { required: true, message: '请选择设备形态', trigger: 'change' }
        ],
        // deviceDomain: [
        //   { required: true, message: '请选择资源域', trigger: 'change' }
        // ],
        bundleName: [
          { required: true, message: '请输入设备名称', trigger: 'blur' },
          { min: 1, max: 30, message: '长度在 1 到 30 个字符', trigger: 'blur' }
        ],
        username: [
          { required: true, message: '请输入设备账号', trigger: 'blur' }
        ],

        onlinePassword: [
          { validator: validatePass, trigger: 'blur' }
        ],
        checkPass: [
          { validator: validateCheckPass, trigger: 'blur' }
        ]//,
        // accessNodeUid: [
        //   { required: true, message: '请输入接入层标识', trigger: 'blur' }
        // ]
      },
      deviceModelOptions: [],
      deviceDomainOptions: [{ label: "外域资源", value: "1" }, { label: "本域资源", value: "2" }],

      coderTypeOptions: [
        {
          label: "编码器",
          value: "ENCODER"
        },
        {
          label: "解码器",
          value: "DECODER"
        },
        {
          label: "默认",
          value: "DEFAULT"
        },
      ],
      wsTypeOptions: [
        {
          label: "编码器",
          value: "ENCODER"
        },
        {
          label: "解码器",
          value: "DECODER"
        },
        {
          label: "软终端",
          value: "DEFAULT"
        },
      ],
      bundleTypeOptions: [
        {
          label: "终端",
          value: "VenusTerminal"
        },
        {
          label: "混合器",
          value: "VenusMixer"
        },
        {
          label: "转发器",
          value: "VenusTransporter"
        },
        {
          label: "外部输出设备",
          value: "VenusOutConnection"
        },
        {
          label: "大屏设备",
          value: "VenusVideoMatrix"
        },
      ],
      dialog: {
        changeFolder: {
          visible: false,
          tree: {
            props: {
              label: 'name',
              children: 'children'
            },
            expandOnClickNode: false,
            data: [/*{
                "id": 1,
                "name": "根目录",
                "toLdap": true,
                "children":[{
                  "id": 1,
                  "name": "目录1",
                  "toLdap": true,
                  "children":[]
                },{
                  "id": 2,
                  "name": "目录2",
                  "toLdap": true,
                  "children":[]
                }]
              }*/],
            current: ''
          },
        },
        addFolder: {
          visible: false,
          parent: '',
          parentNode: '',
          folderName: '',
          toLdap: '否'
        }
      }
    };
  },
  computed: {
    bundleFormMulticastEncode: function () {
      var self = this;
      return self.bundleForm.multicastEncode;
    }
  },
  watch: {
    bundleFormMulticastEncode: function (v) {
      var self = this;
      if (!v) {
        self.bundleForm.multicastEncodeAddr = '';
      }
    }
  },
  methods: {
    handleTabClick: function (tab, event) {
      if ("AddBundle" !== tab.name) {
        this.$router.push('/' + tab.name);
      }
    },
    getDeviceModels: function () {
      getDeviceModels().then(res => {
        if (!res.errMsg && res.deviceModels) {
          for (let deviceModel of res.deviceModels) {
            let deviceModelOption = {
              value: deviceModel,
              label: deviceModel
            };
            this.deviceModelOptions.push(deviceModelOption);
          }
        }
      });
    },
    addExtraInfo: function () {
      this.extraInfos.push({});
    },
    remove: function (item) {
      var index = this.extraInfos.indexOf(item);
      if (index !== -1) {
        this.extraInfos.splice(index, 1)
      }
    },
    reset: function () {
      this.$refs["bundleForm"].resetFields();
      this.bundleForm.bundleFolderId = null;
      this.bundleForm.bundleFolderName = '根目录';
    },
    submit: function () {
      if (!this.validateBaseInfo()) {
        return;
      }

      if (!this.validateExtraInfo()) {
        return;
      }

      let param = {
        bundle: JSON.stringify(this.bundleForm),
        extraInfoVOList: JSON.stringify(this.extraInfos)
      };

      addBundle(param).then(res => {
        if (res.errMsg) {
          this.$message({
            message: res.errMsg,
            type: 'error'
          });
        } else {
          this.$message({
            message: "添加成功",
            type: 'success'
          });
          setTimeout(() => {
            window.location.reload();
          }, 1000);

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
    validateBaseInfo: function () {
      var result = false;
      this.$refs["bundleForm"].validate((valid) => {
        result = valid;
      });
      return result;
    },
    validateExtraInfo: function () {
      for (let extraInfo of this.extraInfos) {
        if (!extraInfo.name || !extraInfo.value) {
          this.$message({
            message: "扩展字段不能为空",
            type: 'error'
          });
          return false;
        }
      }
      return true;
    },
    handleChangeFolder: function () {
      var self = this;
      self.dialog.changeFolder.visible = true;
      queryFolderTree().then(res => {
        console.log(res)
        if (res.status === 200) {
          self.dialog.changeFolder.tree.data = res.data
        } else {
          self.$message({
            type: 'error',
            message: res.message
          });
        }
      });
    },
    handleChangeFolderClose: function () {
      var self = this;
      self.dialog.changeFolder.visible = false;
      self.dialog.changeFolder.tree.data.splice(0, self.dialog.changeFolder.tree.data.length);
      self.dialog.changeFolder.tree.current = '';
    },
    handleChangeFolderCommit: function () {
      var self = this;
      self.bundleForm.bundleFolderId = self.dialog.changeFolder.tree.current.id;
      self.bundleForm.bundleFolderName = self.dialog.changeFolder.tree.current.name;
      self.handleChangeFolderClose();
    },
    currentTreeNodeChange: function (data, node) {
      var self = this;
      self.dialog.changeFolder.tree.current = data;
    },
    treeNodeAppend: function (node, data) {
      var self = this;
      self.dialog.addFolder.parent = data;
      self.dialog.addFolder.parentNode = node;
      self.dialog.addFolder.visible = true;
    },
    handleAddFolderClose: function () {
      var self = this;
      self.dialog.addFolder.parent = '';
      self.dialog.addFolder.parentNode = '';
      self.dialog.addFolder.visible = false;
      self.dialog.addFolder.folderName = '';
      self.dialog.addFolder.toLdap = '否';
    },
    handleAddFolderCommit: function () {
      var self = this;
      addFolder({
        name: self.dialog.addFolder.folderName,
        parentId: self.dialog.addFolder.parent.id,
        toLdap: self.dialog.addFolder.toLdap === '是' ? true : false
      }).then(res => {
        self.dialog.addFolder.parent.children = self.dialog.addFolder.parent.children || [];
        if (!res.errMsg) {
          self.dialog.addFolder.parent.children.push(res.folder);
          self.$refs.changeFolderTree.append(res.folder, self.dialog.addFolder.parentNode);
          self.handleAddFolderClose();
        }
      });
    },
    handleSelectLayerNode: function () {
      var self = this;
      self.$refs.selectLayerNode.show();
    },
    layerNodeSelected: function (layerNode, done) {
      var self = this;
      self.bundleForm.accessNodeUid = layerNode.nodeUid;
      done();
    }
  },
  mounted: function () {
    var self = this;
    this.$nextTick(function () {
      self.$parent.$parent.$parent.$parent.$parent.setActive('/LwLocalBundleManage');
    });
    this.getDeviceModels();
  }
}
</script>

<style scoped>
</style>
