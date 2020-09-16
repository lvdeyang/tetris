<template>
  <section>
    <el-tabs v-model="activeTabName" @tab-click="handleTabClick" style="float:left; width:100%;">
      <el-tab-pane label="层节点列表" name="LayernodeManage"></el-tab-pane>
      <el-tab-pane label="网管" name="NetIframe"></el-tab-pane>
    </el-tabs>
    <iframe :src="netUrl" frameborder="0" width="100%" height="100%"></iframe>
  </section>
</template>
<script>
export default {
  name: "NetIframe",
  data () {
    return {
      netUrl: '',
      activeTabName: "NetIframe",
    }
  },
  created () {
    this.netUrl = this.$route.query.netUrl
  },
  methods: {
    handleTabClick (tab, event) {
      if ("NetIframe" !== tab.name) {
        this.$router.push('/' + tab.name);
      }
    },
  },
  mounted () {
    var self = this;
    this.$nextTick(function () {
      self.$parent.$parent.$parent.$parent.$parent.setActive('/LayernodeManage');
    });
  }
}
</script>
<style>
iframe {
  min-height: 780px;
}
</style>

