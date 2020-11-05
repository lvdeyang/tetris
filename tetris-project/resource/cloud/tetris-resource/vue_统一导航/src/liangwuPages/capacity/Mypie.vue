<template>
  <div class="Echarts">
    <div :id="echartsId" style="width: 500px;height:350px;"></div>
    <div class="data-box">占用：{{currentNum}} <br />空闲：{{totalNum - currentNum}}</div>
  </div>
</template>

<script>

// import { queryCapacityDatas } from '../../api/api'
export default {
  name: 'my-pie',
  props: {
    optionData: {
      type: Array,
      default: () => []
    },
    titleText: {
      type: String,
      default: ''
    },
    legend: {
      type: Array,
      default: () => []
    },
    titleType: {
      type: String,
      default: ''
    },
    capacityData: {
      type: Object,
      default: function () {
        return {}
      }
    }
  },
  data () {
    return {
      currentNum: undefined,
      totalNum: undefined
    }
  },
  methods: {
    initNum () {
      if (this.titleType == "ImageAccess") {
        this.currentNum = this.capacityData.vedioCount || 0
        this.totalNum = this.capacityData.vedioCapacity
      } else if (this.titleType == "onLine") {
        this.currentNum = this.capacityData.userCount || 0
        this.totalNum = this.capacityData.userCapacity
      } else if (this.titleType == "transiter") {
        this.currentNum = this.capacityData.turnCount || 0
        this.totalNum = this.capacityData.turnCapacity
      } else if (this.titleType == "playback") {
        this.currentNum = this.capacityData.reCount || 0
        this.totalNum = this.capacityData.replayCapacity
      }
    },
    myEcharts () {
      // 基于准备好的dom，初始化echarts实例
      var myChart = this.$echarts.init(document.getElementById(this.echartsId));

      // 指定图表的配置项和数据
      var option = {
        title: {
          text: this.titleText,
          subtext: '',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          data: this.legend
        },
        series: [
          {
            name: '图像信息容量',
            type: 'pie',
            radius: '55%',
            center: ['60%', '50%'],
            data: this.optionData,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      };
      this.initNum()
      // 使用刚指定的配置项和数据显示图表。
      // console.log(option)
      myChart.setOption(option);
    }
  },
  computed: {
    echartsId () {
      return 'echarts' + Math.random() * 100000
    }
  },
  watch: {
    optionData (n, o) {
      this.myEcharts()
    }
  },
  created () {
  },
  mounted: function () {
  },
}
</script>

<style>
.Echarts {
  position: relative;
}
.data-box {
  font-size: 18px;
  margin: 10px;
  position: absolute;
  top: 10px;
  right: 10px;
}
</style>
