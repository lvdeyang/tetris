<template>
  <div class="Echarts">
    <div :id="echartsId" style="width: 600px;height:400px;"></div>
  </div>
</template>

<script>
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
    }
  },
  data () {
    return {

    }
  },
  methods: {
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
</style>
