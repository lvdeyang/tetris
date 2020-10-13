<template>
  <div class="Echarts">
    <el-button size="small" type="primary" @click="handleInit">初始化容量</el-button>
    <el-row :gutter="0">
      <el-col class="col" :xs="24" :sm="12" :xl="12">
        <my-pie :optionData="optionDataComputed.ImageAccess" titleText='图像信息接入路数' :legend='legend.ImageAccess'></my-pie>
      </el-col>
      <el-col class="col" :xs="24" :sm="12" :xl="12">
        <my-pie :optionData="optionDataComputed.onLine" titleText='在线用户人数' :legend='legend.onLine'></my-pie>
      </el-col>
      <el-col class="col" :xs="24" :sm="12" :xl="12">
        <my-pie :optionData="optionDataComputed.transiter" titleText='当前转发路数' :legend='legend.transiter'></my-pie>
      </el-col>
      <el-col class="col" :xs="24" :sm="12" :xl="12">
        <my-pie :optionData="optionDataComputed.playback" titleText='当前回放路数' :legend='legend.playback'></my-pie>
      </el-col>
    </el-row>
    <el-dialog title="初始化" :visible.sync="dialogVisible" width="40%">
      <el-form :model="initForm" label-width="200px">
        <el-form-item label="图像信息接入总路数">
          <el-input v-model="initForm.vedioCapacity" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="在线用户总人数">
          <el-input v-model="initForm.userCapacity" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="当前转发总路数">
          <el-input v-model="initForm.turnCapacity" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="当前回放总路数">
          <el-input v-model="initForm.replayCapacity" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="initSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>

</template>

<script>
import { queryCapacityDatas, initCapacity } from '../../api/api'
import Mypie from './Mypie.vue'
export default {
  name: 'Echarts',
  data () {
    return {
      resData: {},
      optionData: {
        ImageAccess: [],
        onLine: [],
        transiter: [],
        playback: [],
      },
      dialogVisible: false,
      initForm: {
        vedioCapacity: '',
        userCapacity: '',
        turnCapacity: '',
        replayCapacity: ''

      },
      legend: {
        ImageAccess: ['已接入', '空闲'],
        onLine: ['在线', '空闲'],
        transiter: ['已占用转发', '空闲'],
        playback: ['同时回放路数', '空闲'],
      }
    }
  },
  components: { 'my-pie': Mypie },
  computed: {
    optionDataComputed: function () {
      var res = this.resData;
      this.optionData.ImageAccess = [
        { value: res.vedioCount ? res.vedioCount : 0, name: '已接入' },
        { value: res.vedioIdleCount ? res.vedioIdleCount : res.vedioCapacity, name: '空闲' }
      ];
      this.optionData.onLine = [
        { value: res.userCount ? res.userCount : 0, name: '在线' },
        { value: res.userIdleCount ? res.userIdleCount : res.userCapacity, name: '空闲' }
      ];
      this.optionData.transiter = [
        { value: res.turnCount ? res.turnCount : 0, name: '已占用转发' },
        { value: res.turnIdleCount ? res.turnIdleCount : res.turnCapacity, name: '空闲' }
      ];
      this.optionData.playback = [
        { value: res.reCount ? res.reCount : 0, name: '同时回放路数' },
        { value: res.reIdleCount ? res.reIdleCount : res.replayCapacity, name: '空闲' }
      ];
      return this.optionData
    }
  },
  methods: {
    filterData () {

    },
    handleInit () {
      var self = this;
      self.dialogVisible = true;
    },
    initSubmit () {
      var self = this;
      initCapacity(this.initForm).then(res => {
        self.$message({
          type: "success",
          message: "设置成功！"
        })
        self.dialogVisible = false;

      })
    }
  },
  mounted () {
    queryCapacityDatas({ 'id': 1 }).then(res => {
      this.resData = res;
      this.initForm.vedioCapacity = res.vedioCapacity
      this.initForm.userCapacity = res.userCapacity
      this.initForm.turnCapacity = res.turnCapacity
      this.initForm.replayCapacity = res.replayCapacity

    })
    console.log(this.optionData)
  }
}
</script>

<style>
.col {
  border: 1px solid #333;
  margin-top: 20px;
}
</style>
