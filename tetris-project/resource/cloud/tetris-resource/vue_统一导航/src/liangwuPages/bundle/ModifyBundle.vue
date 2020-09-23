<template>
  <section>
    <el-tabs v-model="activeTabName" @tab-click="handleTabClick" style="float:left; width:100%;">
      <el-tab-pane label="资源列表" name="LwLocalBundleManage"></el-tab-pane>
      <!-- <el-tab-pane label="外域资源列表" name="BundleManage"></el-tab-pane> -->
      <el-tab-pane label="修改资源" name="LwModifyBundle"></el-tab-pane>
    </el-tabs>

    <el-form label-width="100px">
      <el-form-item size="small" label="设备名称" prop="bundleName">
        <el-input v-model="bundleName" style="width: 200px;" placeholder="输入新的设备名称"></el-input>
      </el-form-item>

      <el-form-item size="small" label="设备IP" prop="deviceIp">
        <el-input v-model="deviceIp" style="width: 200px;" placeholder="输入新的设备IP"></el-input>
      </el-form-item>

      <el-form-item size="small" label="地点" prop="bundleAlias">
        <el-input v-model="location" style="width: 200px;"></el-input>
      </el-form-item>
      <el-form-item size="small" label="设备端口" prop="devicePort">
        <el-input v-model="devicePort" style="width: 200px;" placeholder="输入新的设备端口"></el-input>
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
      <el-form-item size="small" label="是否转码">
        <el-switch v-model="transcod" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
      </el-form-item>
      <el-form-item size="small" label="源组播Ip">
        <el-input v-model="multicastSourceIp" style="width: 200px;"></el-input>
      </el-form-item>
      <!--
          <div style="margin-top:10px;">
            <el-input v-model="bundleName" placeholder="输入新的设备名称" style="width: 300px;">
              <template slot="prepend">设备名称</template>
            </el-input>
          </div>
          -->
      <el-form-item size="small" label="设备分组" prop="bundleFolderName">
        <el-input v-model="bundleFolderName" style="width: 200px;" readOnly @click.native="handleChangeFolder"></el-input>
      </el-form-item>
      <el-button type="info" size="small" @click="addExtraInfo" style="margin-top:20px; margin-left: 30px">新增扩展字段</el-button>

      <div style="margin-top:10px;" v-for="(extraInfo, index) in extraInfos">
        <el-input size="small" v-model="extraInfo.name" placeholder="扩展字段名" style="width: 180px;"></el-input>
        <el-input size="small" v-model="extraInfo.value" placeholder="扩展字段值" style="width: 180px;margin-left: 10px;"></el-input>
        <el-button size="small" type="danger" @click.prevent="remove(extraInfo)" style="margin-left: 10px;">删除</el-button>
      </div>

      <div style="margin-top:30px; margin-left: 30px">
        <!--<el-button type="info" @click="addExtraInfo">新增扩展字段</el-button>-->
        <el-button size="small" type="primary" @click="submit">提交</el-button>
      </div>

    </el-form>

    <!-- 分组弹窗 -->
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
  </section>
</template>

<script type="text/ecmascript-6">
import { queryBundleExtraInfo, modifyBundleExtraInfo, queryFolderTree, } from '../../api/api';

export default {
  name: "LwModifyBundle",
  data () {
    return {
      activeTabName: "LwModifyBundle",
      bundleId: this.$route.query.bundleId,
      bundleName: this.$route.query.bundleName,
      location: this.$route.query.location,
      deviceIp: this.$route.query.deviceIp,
      devicePort: this.$route.query.devicePort,
      multicastEncode: this.$route.query.multicastEncode,
      multicastEncodeAddr: this.$route.query.multicastEncodeAddr,
      multicastDecode: this.$route.query.multicastDecode,
      extraInfos: [],
      bundleFolderId: this.$route.query.bundleFolderId,
      bundleFolderName: this.$route.query.bundleFolderName,
      transcod: this.$route.query.transcod,
      multicastSourceIp: this.$route.query.multicastSourceIp,
      extraInfos: [],
      dialog: {
        changeFolder: {
          visible: false,
          tree: {
            props: {
              label: 'name',
              children: 'children'
            },
            expandOnClickNode: false,
            data: [],
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
  methods: {
    handleTabClick (tab, event) {
      if ("LwModifyBundle" !== tab.name) {
        this.$router.push('/' + tab.name);
      }
    },
    queryBundleExtraInfo: function () {
      let param = {
        bundleId: this.bundleId
      };

      queryBundleExtraInfo(param).then(res => {
        if (res.errMsg) {
          this.$message({
            message: res.errMsg,
            type: 'error'
          });
        } else {
          this.extraInfos = res.extraInfos;
        }
      });
    },
    addExtraInfo: function () {
      this.extraInfos.push({});
    },
    remove: function (item) {
      var index = this.extraInfos.indexOf(item);
      if (index !== -1) {
        this.extraInfos.splice(index, 1);
      }
    },
    submit: function () {
      if (!this.validateExtraInfo()) {
        return;
      }

      let param = {
        bundleId: this.bundleId,
        bundleName: this.bundleName,
        location: this.location,
        folderId: this.bundleFolderId,
        deviceIp: this.deviceIp,
        devicePort: this.devicePort,
        multicastEncode: this.multicastEncode,
        multicastEncodeAddr: this.multicastEncodeAddr,
        multicastDecode: this.multicastDecode,
        transcod: this.transcod,
        multicastSourceIp: this.multicastSourceIp,
        extraInfos: JSON.stringify(this.extraInfos)
      };

      modifyBundleExtraInfo(param).then(res => {
        if (res.errMsg) {
          this.$message({
            message: res.errMsg,
            type: 'error'
          });
        } else {
          this.$message({
            message: "修改成功",
            type: 'success'
          });
        }
      });
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
    // 修改分组方法
    handleChangeFolder: function () {
      var self = this;
      self.dialog.changeFolder.visible = true;
      queryFolderTree().then(res => {
        if (res.status === 200) {
          self.dialog.changeFolder.tree.data.splice(0, self.dialog.changeFolder.tree.data.length);
          if (res.data && res.data.length > 0) {
            for (var i = 0; i < res.data.length; i++) {
              self.dialog.changeFolder.tree.data.push(res.data[i]);
            }
          }
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
      console.log(self.dialog.changeFolder)
      self.bundleFolderId = self.dialog.changeFolder.tree.current.id;
      self.bundleFolderName = self.dialog.changeFolder.tree.current.name;
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
  mounted () {
    this.queryBundleExtraInfo();
    this.row = JSON.parse(sessionStorage.getItem('row'))
  }
}
</script>

<style scoped>
</style>

