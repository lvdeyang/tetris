<template>
  <div>
    <!-- <el-tabs v-model="activeTabName" @tab-click="handleTabClick">
      <el-tab-pane label="本域转发" name="EncodeManage"></el-tab-pane>
      <el-tab-pane label="外域转发" name="EncodeManageOut"></el-tab-pane>
    </el-tabs> -->

    <template>
      <div class="itxst">
        <div class="col">
          <div class="title">编码器</div>
          <div class="custom-tree-container" v-loading="treeLoading">
            <div>
              <el-tree :filter-node-method="filterNode" ref="tree" :data="tree" default-expand-all node-key="id" highlight-current :expand-on-click-node="false" :props="defaultProps" @node-click="handleNodeClick">
                <template class="custom-tree-node" slot-scope="{ node, data }">
                  <span v-if="data.children">{{data.name}}</span>
                  <draggable v-else element="span" :options="{group:{name: 'site',pull:'clone'},sort: true}" animation="300">
                    <span class="item">{{data.name}}</span>
                  </draggable>
                </template>
              </el-tree>
            </div>
          </div>
        </div>
      </div>
    </template>

    <template>
      <div class="itxst">
        <div class="col">
          <div class="title">解码器</div>
          <div class="custom-tree-container" v-loading="treeLoading">
            <div>
              <el-tree ref="tree1" :data="tree1" default-expand-all node-key="id" highlight-current :expand-on-click-node="false" :props="defaultProps" @node-click="handleNodeClick">
                <template class="custom-tree-node" slot-scope="{ node, data }">
                  <draggable element="span" group="site" animation="300">
                    <span class="item">{{data.name}}</span>
                  </draggable>
                </template>
              </el-tree>
            </div>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>
<script>
//导入draggable组件
import draggable from 'vuedraggable'
import { initFolderTree } from '../../api/api'
export default {
  //注册draggable组件
  components: {
    draggable,
  },
  data () {
    return {
      activeTabName: "EncodeManage",
      drag: false,
      dialogFormVisible: false,
      treeLoading: false,
      tree: [
        {
          "id": 1,
          "parentId": -1,
          "name": "xx基地",
          "beFolder": true,
          "toLdap": true,
          "bundleId": null,
          "username": null,
          "folderType": null,
          "children": [{
            "id": 11,
            "parentId": 1,
            "name": "发射场",
            "beFolder": true,
            "toLdap": true,
            "bundleId": null,
            "username": null,
            "folderType": null,
            "children": [],
            "systemSourceType": true,
            "folderIndex": 1,
            "nodeType": "FOLDER"
          }, {
            "id": 13,
            "parentId": 1,
            "name": "指控中心",
            "beFolder": true,
            "toLdap": true,
            "bundleId": null,
            "username": null,
            "folderType": null,
            "children": [{
              "id": 15,
              "parentId": 13,
              "name": "网络摄像头",
              "beFolder": true,
              "toLdap": true,
              "bundleId": null,
              "username": null,
              "folderType": null,
              "children": [{
                "id": 100089,
                "parentId": 15,
                "name": "大华摄像头1",
                "beFolder": false,
                "toLdap": null,
                "bundleId": "bc7e14ce41e540649e92f479cb173c43",
                "username": null,
                "folderType": null,
                "children": null,
                "systemSourceType": true,
                "folderIndex": 1,
                "nodeType": "BUNDLE"
              }, {
                "id": 100135,
                "parentId": 15,
                "name": "大华摄像头2",
                "beFolder": false,
                "toLdap": null,
                "bundleId": "49bab374fbb04de584d66c28716ab3f7",
                "username": null,
                "folderType": null,
                "children": null,
                "systemSourceType": true,
                "folderIndex": 2,
                "nodeType": "BUNDLE"
              }],
              "systemSourceType": true,
              "folderIndex": 1,
              "nodeType": "FOLDER"
            }, {
              "id": 23,
              "parentId": 13,
              "name": "值班室",
              "beFolder": true,
              "toLdap": true,
              "bundleId": null,
              "username": null,
              "folderType": null,
              "children": [{
                "id": 100137,
                "parentId": 23,
                "name": "北清摄像头1",
                "beFolder": false,
                "toLdap": null,
                "bundleId": "5e4505a56c75408c82abb7789994447f",
                "username": null,
                "folderType": null,
                "children": null,
                "systemSourceType": true,
                "folderIndex": 1,
                "nodeType": "BUNDLE"
              }],
              "systemSourceType": true,
              "folderIndex": 2,
              "nodeType": "FOLDER"
            }, {
              "id": 25,
              "parentId": 13,
              "name": "监视器",
              "beFolder": true,
              "toLdap": true,
              "bundleId": null,
              "username": null,
              "folderType": null,
              "children": [],
              "systemSourceType": true,
              "folderIndex": 3,
              "nodeType": "FOLDER"
            }],
            "systemSourceType": true,
            "folderIndex": 2,
            "nodeType": "FOLDER"
          }],
          "systemSourceType": true,
          "folderIndex": 0,
          "nodeType": "FOLDER"
        }, {
          "id": 43,
          "parentId": -1,
          "name": "xx基地2",
          "beFolder": true,
          "toLdap": true,
          "bundleId": null,
          "username": null,
          "folderType": null,
          "children": [{
            "id": 45,
            "parentId": 43,
            "name": "xx发射场",
            "beFolder": true,
            "toLdap": true,
            "bundleId": null,
            "username": null,
            "folderType": null,
            "children": [{
              "id": 100143,
              "parentId": 45,
              "name": "发射场东侧摄像头",
              "beFolder": false,
              "toLdap": null,
              "bundleId": "cc7580e5445041abaf07156f19e52530",
              "username": null,
              "folderType": null,
              "children": null,
              "systemSourceType": false,
              "folderIndex": null,
              "nodeType": "BUNDLE"
            }],
            "systemSourceType": false,
            "folderIndex": 1,
            "nodeType": "FOLDER"
          }],
          "systemSourceType": false,
          "folderIndex": 1,
          "nodeType": "FOLDER"
        }]
      ,
      filterText: '',
      tree1: [{
        "id": 1,
        "parentId": -1,
        "name": "xx基地",
        "beFolder": true,
        "toLdap": true,
        "bundleId": null,
        "username": null,
        "folderType": null,
        "children": [{
          "id": 13,
          "parentId": 1,
          "name": "指控中心",
          "beFolder": true,
          "toLdap": true,
          "bundleId": null,
          "username": null,
          "folderType": null,
          "children": [{
            "id": 27,
            "parentId": 13,
            "name": "电视墙",
            "beFolder": true,
            "toLdap": true,
            "bundleId": null,
            "username": null,
            "folderType": null,
            "children": [{
              "id": 100145,
              "parentId": 27,
              "name": "指控中心东侧显示器",
              "beFolder": false,
              "toLdap": null,
              "bundleId": "a37641acfafc4ce2bb8f3bfdc277a239",
              "username": null,
              "folderType": null,
              "children": null,
              "systemSourceType": true,
              "folderIndex": 1,
              "nodeType": "BUNDLE"
            }],
            "systemSourceType": true,
            "folderIndex": 4,
            "nodeType": "FOLDER"
          },
          {
            "id": 23,
            "parentId": 13,
            "name": "值班室",
            "beFolder": true,
            "toLdap": true,
            "bundleId": null,
            "username": null,
            "folderType": null,
            "children": [{
              "id": 100149,
              "parentId": 23,
              "name": "北清外域解码器",
              "beFolder": false,
              "toLdap": null,
              "bundleId": "4746f74b504742bf9fe3ff8564ba02c2",
              "username": null,
              "folderType": null,
              "children": null,
              "systemSourceType": true,
              "folderIndex": 2,
              "nodeType": "BUNDLE"
            }],
            "systemSourceType": true,
            "folderIndex": 2,
            "nodeType": "FOLDER"
          }],
          "systemSourceType": true,
          "folderIndex": 2,
          "nodeType": "FOLDER"
        }],
        "systemSourceType": true,
        "folderIndex": 0,
        "nodeType": "FOLDER"
      }],
      dataOption: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      },
    };
  },
  created () {
    this.initTree()
  },
  watch: {
    filterText (val) {
      this.$refs.tree.filter(val);
    }
  },
  methods: {
    filterNode (value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
    initTree: function (keepExpand) {
      this.treeLoading = true;
      initFolderTree().then(res => {
        if (res.errMsg) {
          this.$message({
            message: res.errMsg,
            type: 'error'
          });
          this.treeLoading = false;
        } else {
          // this.tree = res.tree;
          // this.tree1 = JSON.parse(JSON.stringify(this.tree))
          this.treeLoading = false;
          // this.filterText = "摄像头"

        }
      });
    },
    handleNodeClick: function (data) {
      console.log(data)
      if (data.children) {
        return
      } else {
        console.log(data.name)
        this.inputDevice = data.name;
        this.dialogFormVisible = false;

      }
    },
    //开始拖拽事件
    onStart () {
      this.drag = true;

    },
    //拖拽结束事件
    onEnd () {
      var self = this;
      function messageFun () {
        self.$message({
          type: 'success',
          message: "配置成功!"
        })
      }
      this.drag = false;
      // setTimeout(messageFun, 800);
    },
    onMove2 (e, originalEvent) {
      //false表示阻止拖拽
      return false;
    },
    handleDelete (arr, index) {
      arr.splice(index, 1)
    },

    handleTabClick (tab, event) {
      if ("EncodeManage" !== tab.name) {
        this.$router.push('/' + tab.name);
      }
    },
  }
};
</script>


<style scoped>
/*定义要拖拽元素的样式*/
.ghostClass {
  background-color: #e6a23c !important;
}
.chosenClass {
  background-color: #409eff !important;
  opacity: 1 !important;
}
.dragClass {
  background-color: #409eff !important;
}
.itxst {
  margin: 10px;
}
.domain-item {
  padding: 10px;
  border: solid 1px #eee;
  border-radius: 5px;
  margin-bottom: 20px;
}
.title {
  padding: 12px 12px;
}
.col {
  width: 40%;
  flex: 1;
  padding: 10px;
  border: solid 1px #eee;
  border-radius: 5px;
  float: left;
  min-height: 100px;
  margin-right: 30px;
}
.col + .col {
  margin-left: 10px;
}

.item {
  padding: 6px 12px;
  margin: 0px 10px 0px 10px;
  /* border: solid 1px #eee; */
  /* background-color: #f1f1f1; */
  margin-top: 6px;
}
.item:hover {
  background-color: #fdfdfd;
  cursor: move;
}
.item + .item {
  border-top: none;
}
.icon {
  width: 30px;
  height: 30px;
  float: right;
  margin-right: 5px;
  cursor: pointer;
}
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
</style>