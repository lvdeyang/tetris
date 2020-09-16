<template>
  <div>
    <el-tabs v-model="activeTabName" @tab-click="handleTabClick">
      <el-tab-pane label="本域转发" name="EncodeManage"></el-tab-pane>
      <el-tab-pane label="外域转发" name="EncodeManageOut"></el-tab-pane>
    </el-tabs>

    <template>
      <div class="itxst">
        <div class="col">
          <div class="title">编码器</div>
          <el-table :data="tableData5" style="width: 100%" default-expand-all=true>
            <el-table-column type="expand">
              <template slot-scope="props">
                <draggable v-model="props.row.codingList" :options="{group:{name: 'site',pull:'clone'},sort: true}" animation="300" dragClass="dragClass" ghostClass="ghostClass" chosenClass="chosenClass" @start="onStart" @end="onEnd">
                  <transition-group>
                    <div class="item" v-for="item in props.row.codingList" :key="item.id">{{item.name}}</div>
                  </transition-group>
                </draggable>
              </template>
            </el-table-column>
            <el-table-column label="域id" prop="id">
            </el-table-column>
            <el-table-column label="域名称" prop="region">
            </el-table-column>
            <el-table-column label="描述" prop="desc">
            </el-table-column>
          </el-table>
        </div>
      </div>
    </template>

    <template>
      <div class="itxst">
        <div class="col">
          <div class="title">解码器</div>
          <el-table :data="tableData6" style="width: 100%" default-expand-all=true>
            <el-table-column type="expand">
              <template slot-scope="props">
                <draggable v-model="props.row.encodingList" :options="{group:{name: 'site',pull:'clone'},sort: true}" animation="300" dragClass="dragClass" ghostClass="ghostClass" chosenClass="chosenClass" @start="onStart" @end="onEnd" :move="onMove2">
                  <transition-group>
                    <div v-for="item in props.row.encodingList" :key="item.id">
                      <div v-if="props.row.encodingList.length==1||(props.row.encodingList.length>=1&&!item.isTitle)" class="item unmover">{{item.name}} <span @click="handleDelete(props.row.encodingList,index)" class="icon" v-if="!item.isTitle"> <i class="el-icon-delete"></i></span></div>
                    </div>
                  </transition-group>
                </draggable>
              </template>
            </el-table-column>
            <el-table-column label="设备id" prop="id">
            </el-table-column>
            <el-table-column label="域名称" prop="region">
            </el-table-column>
            <el-table-column label="描述" prop="desc">
            </el-table-column>
          </el-table>
        </div>
      </div>
    </template>
  </div>
</template>
<script>
//导入draggable组件
import draggable from 'vuedraggable'
export default {
  //注册draggable组件
  components: {
    draggable,
  },
  data () {
    return {
      activeTabName: "EncodeManageOut",
      drag: false,
      //定义要被拖拽对象的数组
      tableData5: [{
        id: '12987122',
        region: '北京',
        codingList: [
          { id: 1, name: '北京编码设备1' },
          { id: 2, name: '北京编码设备2' },
          { id: 3, name: '北京编码设备3' },
          { id: 4, name: '北京编码设备4' },
          { id: 8, name: '北京编码设备5' }
        ],
        desc: '北京地区编码器',
      }],
      tableData6: [{
        id: '12987166',
        region: '北京',
        encodingList: [
          { id: 6, name: '请拖拽编码设备到这里进行配对', isTitle: true }
        ],
        desc: '北京地区解码器',
      }, {
        id: '12987167',
        region: '甘肃',
        encodingList: [
          { id: 7, name: '请拖拽编码设备到这里进行配对', isTitle: true },
          { id: 8, name: '北京编码设备5' }
        ],
        desc: '甘肃地区解码器',
      },]
    };
  },
  methods: {
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
      if ("EncodeManageOut" !== tab.name) {
        this.$router.push('/' + tab.name);
      }
    }
  },
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
  border: solid 1px #eee;
  background-color: #f1f1f1;
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
</style>