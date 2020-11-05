

define([
  'text!' + window.APPPATH + 'component/bvc2-liangwu-forword-list/bvc2-liangwu-forword-list.html',
  'restfull',
  'jquery',
  'vue',
  'element-ui',
  'date',
  'bvc2-dialog-single-osd',
  'forword-inner-control',
  'forword-outer-control',
  'css!' + window.APPPATH + 'component/bvc2-liangwu-forword-list/bvc2-liangwu-forword-list.css'
], function (tpl, ajax, $, Vue) {

  //组件名称
  var pluginName = 'bvc2-liangwu-forword-list';

  Vue.component(pluginName, {
    template: tpl,
    data: function () {
      return {
        tabList:[
        {label:"概况",name:"overview"},
        {label:"上屏",name:"inner"},
        {label:"外域转发",name:"outer"}
        ],
        activeName:"overview",
        overviewTableData:[
          {name:'本域上屏',transmit:'4/200'},
          {name:'外域一',transmit:'2/200'},
          {name:'外域二',transmit:'4/200'},
        ]
        
      }
    },
    props:['originType'],
    computed: {
    },
    watch: {},
    methods: {
      handleClick(tab, event) {
        var self = this;
        console.log(tab)
        // this.getCapacity()
      },
      getCapacity(){
        ajax.post(self.resourceApiUrl+'/vedioCapacity/query', null, function (data, status) {
        if (status == 200) {
          
        }
      })
      }
    },
    mounted: function () {
      var self = this; var opcityUrl ='/vedioCapacity/query'
      var resourceApiUrl = document.location.protocol +"//"+document.location.hostname+':8213';
      self.resourceApiUrl =resourceApiUrl;
      console.log(self.resourceApiUrl+'/vedioCapacity/query')
      
   
    },
    updated() {

    },
  });

  return Vue;
});