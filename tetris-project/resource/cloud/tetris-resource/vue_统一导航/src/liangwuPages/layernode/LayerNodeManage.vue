<template>
  <section>

    <el-tabs v-model="activeTabName" @tab-click="handleTabClick" style="float:left; width:100%;">
      <el-tab-pane label="层节点列表" name="LwLayernodeManage"></el-tab-pane>
      <el-tab-pane label="添加层节点" name="LwAddLayerNode"></el-tab-pane>
    </el-tabs>

    <div style="float: left;width: 100%">
      <el-select size="small" v-model="filters.type" placeholder="选择层节点类型" style="float: left;margin-left: 10px;width:200px;">
        <el-option v-for="item in typeOptions" :key="item.value" :label="item.label" :value="item.value">
        </el-option>
      </el-select>
      <el-input size="small" v-model="filters.keyword" style="float: left;margin-left: 30px;width:200px;" placeholder="节点名称"></el-input>
      <el-button size="small" type="info" @click="getNodes" style="float: left;margin-left: 30px;">查询</el-button>

      <el-button size="small" type="danger" @click="handleMultiDelete" style="float: left;margin-left: 30px;">批量删除</el-button>
    </div>

    <!--节点列表-->
    <el-table :data="nodes" v-loading="nodeTableLoading" @selection-change="handleSelectionChange" style="float: left;width: 100%;margin-top: 20px;">
      <el-table-column width="100" type="selection"></el-table-column>
      <el-table-column prop="name" label="名称" width="200" sortable>
      </el-table-column>
      <el-table-column prop="type" label="层节点类型" width="200" sortable>
        <template slot-scope="scope">
          <div><span v-text="typeObj[scope.row.type]"></span></div>
        </template>
      </el-table-column>
      <el-table-column prop="nodeUid" label="层节点ID" width="300" sortable>
      </el-table-column>
      <el-table-column prop="ip" label="层节点IP" width="200" sortable>
      </el-table-column>
      <el-table-column width="150" label="在线状态">
        <template slot-scope="scope">
          <div v-if="scope.row.onlineStatus=='ONLINE'" style="height:20px;width:20px;border-radius:50%;background-color:#0f0"></div>
          <div v-else style="height:20px;width:20px;border-radius:50%;background-color:#f00"></div>
        </template>
      </el-table-column>

      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-button type="text" @click="handleModify(scope.row)" size="small">修改</el-button>
          <el-button type="text" @click="handleDelete(scope.row)" size="small">删除</el-button>
          <el-button type="text" @click="gotoMonitor(scope.row)" size="small" v-if="scope.row.monitorUrl">监控</el-button>
          <el-button type="text" size="small" v-else disabled>监控</el-button>
          <el-button type="text" @click="gotoAdmin(scope.row)" size="small" v-if="scope.row.netUrl">网管</el-button>
          <el-button type="text" size="small" v-else disabled>网管</el-button>
        </template>
      </el-table-column>
    </el-table>

  </section>
</template>

<script type="text/ecmascript-6">
import { getNodes, deleteNodes } from '../../api/api';

export default {
  data () {
    return {
      activeTabName: "LwLayernodeManage",
      nodes: [
      ],
      typeOptions: [
        {
          value: "",
          label: "全部类型"
        },

        {
          label: "终端设备",
          value: "ACCESS_JV210"
        },
        {
          label: "存储设备",
          value: "ACCESS_CDN"
        }
      ],
      typeObj: {
        ACCESS_JV210: "终端设备",
        ACCESS_CDN: "存储设备",

      },
      filters: {
        type: '',
        keyword: ''
      },
      nodeTableLoading: false,
      multipleSelection: []
    }
  },
  methods: {
    gotoMonitor: function (row) {
      var self = this;
      window.open(row.monitorUrl);
    },
    gotoAdmin: function (row) {
      var self = this;
      // window.open(row.netUrl);
      self.$router.push({
        path: '/LwNetIframe',
        query: {
          netUrl: row.netUrl,
        }
      })
    },
    handleTabClick (tab, event) {
      if ("LwLayernodeManage" !== tab.name) {
        this.$router.push('/' + tab.name);
      }
    },
    //获取节点列表
    getNodes: function () {
      let param = {
        type: this.filters.type,
        keyword: this.filters.keyword
      };
      this.nodeTableLoading = true;
      getNodes(param).then((res) => {
        if (res.errMsg) {
          this.$message({
            message: res.errMsg,
            type: 'error'
          });
        } else {
          this.nodes = res.nodes;
        }

        this.nodeTableLoading = false;
      });
    },
    handleModify: function (row) {
      this.$router.push({
        path: '/LwModifyLayernode',
        query: {
          id: row.id,
          nodeUid: row.nodeUid
        }
      });
    },
    handleDelete: function (row) {
      this.$confirm('是否确认删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let param = {
          nodeIds: row.id
        };

        deleteNodes(param).then(res => {
          if (res.errMsg) {
            this.$message({
              message: res.errMsg,
              type: 'error'
            });
          } else {
            this.$message({
              message: "删除成功",
              type: 'success'
            });
            this.getNodes();
          }
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    handleMultiDelete: function () {
      if (!this.multipleSelection.length) {
        this.$message({
          message: '请至少选择一行',
          type: 'warning'
        });
        return;
      }

      this.$confirm('是否确认删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let nodeIds = [];
        for (let selection of this.multipleSelection) {
          nodeIds.push(selection.id);
        }
        let param = {
          nodeIds: nodeIds.join(",")
        };

        deleteNodes(param).then(res => {
          if (res.errMsg) {
            this.$message({
              message: res.errMsg,
              type: 'error'
            });
          } else {
            this.$message({
              message: "删除成功",
              type: 'success'
            });
            this.getNodes();
          }
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    handleSelectionChange: function (val) {
      this.multipleSelection = val;
    },
  },
  mounted () {
    var self = this;
    this.$nextTick(function () {
      self.$parent.$parent.$parent.$parent.$parent.setActive('/LwLayernodeManage');
    });
    this.getNodes();
  }
}

</script>

<style>
</style>
