  var headervue = new Vue({
      el: '#monitor-head',
      data: {
          monitorHover: false,
          forwardHover: false,
          retHover: false
      },
      methods: {
          overMonitorShow() {
              this.monitorHover = true;
          },
          outMonitorHide() {
              this.monitorHover = false;
          },
          overFowardShow() {
              this.forwardHover = true;
          },
          outFowardHide() {
              this.forwardHover = false;
          },
          overRetShow() {
              this.retHover = true;
          },
          outRetHide() {
              this.retHover = false;
          }

      }
  })

  var sortVue = new Vue({
      el: "#sortDiv",
      data: {
          sortHover: false
      },
      methods: {
          overSortShow() {
              this.sortHover = true;
          },
          overoutSortHide() {
              this.sortHover = false;
          }
      }
  })

  var rightFunctionVue = new Vue({
      el: "#rightFunction",
      data: {
          clearHover: false,
          spitNumHover: false
      },
      methods: {
          overClearShow() {
              this.clearHover = true;
          },
          outClearHide() {
              this.clearHover = false;
          },
          overSplitNumShow() {
              this.spitNumHover = true;
          },
          outSplitNumHide() {
              this.spitNumHover = false;      
          }
      }
  })