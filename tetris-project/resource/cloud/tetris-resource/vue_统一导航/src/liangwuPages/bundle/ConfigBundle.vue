<template>
  <section>

    <el-tabs v-model="activeTabName" @tab-click="handleTabClick" style="float:left;width:100%;">
      <el-tab-pane label="资源列表" name="LwBundleManage"></el-tab-pane>
      <el-tab-pane label="能力配置" name="LwConfigBundle"></el-tab-pane>
    </el-tabs>

    <el-card style="float:left;margin-left:50px;margin-top:10px;width:40%;min-height: 700px;" body-style="padding:0px">
      <div slot="header" style="height: 15px">
        <span style="float: left;font-size: 18px;">模板属性</span>
      </div>

      <el-tree :data="templateTree" :props="treeProps" node-key="label" :default-expanded-keys="['channels']"></el-tree>
    </el-card>

    <el-card style="float:left;margin-top:10px;width:50%;min-height: 700px;" body-style="padding:0px">
      <div slot="header" style="height: 15px">
        <span style="float: left;font-size: 18px;">配置</span>
      </div>

      <div style="margin-top:10px;" v-for="(channel, index) in configChannels">
        <el-input placeholder="请输入整数" v-model="channel.channelCnt">
          <template slot="prepend">{{channel.channelName}} 通道数</template>
        </el-input>
      </div>

      <div style="margin-top:10px;" v-for="(editableAttr, index) in configEditableAttrs">
        <el-input placeholder="请输入字段值" v-model="editableAttr.value">
          <template slot="prepend">{{editableAttr.name}}</template>
        </el-input>
      </div>

      <div style="margin-top:50px;">
        <el-button type="primary" @click="submit" style="margin-left: 300px;">提交</el-button>
      </div>
    </el-card>
  </section>
</template>

<script type="text/ecmascript-6">
import { getBundleAbility, configBundle } from '../../api/api';

export default {
  name: "ConfigBundle",
  data () {
    return {
      activeTabName: "LwConfigBundle",
      bundleId: this.$route.query.bundleId,
      templateTree: [],
      treeProps: {
        children: 'children',
        label: 'label',
      },
      configChannels: [],
      configEditableAttrs: []
    }
  },
  methods: {
    handleTabClick (tab, event) {
      if ("LwConfigBundle" !== tab.name) {
        this.$router.push('/' + tab.name);
      }
    },
    displayBundleAbility: function (row) {
      let param = {
        bundleId: this.bundleId
      };

      getBundleAbility(param).then(res => {
        if (res.errMsg) {
          this.$message({
            message: res.errMsg,
            type: 'error'
          });
        } else {
          this.templateTree = res.tree;
          this.configChannels = res.configChannels;
          this.configEditableAttrs = res.configEditableAttrs;
        }
      });
    },
    submit: function () {
      let param = {
        bundleId: this.bundleId,
        configChannels: JSON.stringify(this.configChannels),
        configEditableAttrs: JSON.stringify(this.configEditableAttrs)
      };

      configBundle(param).then(res => {
        if (res.errMsg) {
          this.$message({
            message: res.errMsg,
            type: 'error'
          });
        } else {
          this.$message({
            message: "配置成功",
            type: 'success'
          });
        }
      });
    }
  },
  mounted () {
    this.displayBundleAbility();
  }
}
</script>

<style scoped>
</style>
