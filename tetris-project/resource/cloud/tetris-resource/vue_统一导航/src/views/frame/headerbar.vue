<template>
  <div class="header">
    <el-col :span="6" class="header-col">
      <!-- <div class="logo-wrapper">
        <img class="logo-img" src="~assets/logo.png" />
      </div> -->
      <div class="collapsed-icon-wrapper" @click="collapseMenu()">
        <img v-if="!isCollapsed" class="collapsed-icon" src="~assets/un-collapse.png" />
        <img v-if="isCollapsed" class="collapsed-icon" src="~assets/collapsed.png" />
      </div>
    </el-col>
    <el-col :span="12" class="header-col">
      <el-menu class="el-menu-demo" mode="horizontal" :default-active="active" unique-opened router background-color="#37404f" text-color="#bfcfd9" active-text-color="#ffffff">
        <template v-for="(item, index) in routes" v-if="!item.hidden">
          <el-menu-item style="height:50px" :index="item.path" :key="item.path" v-if="!item.hidden">{{item.name}}</el-menu-item>
        </template>
      </el-menu>
    </el-col>
    <el-col :span="6" class="header-col">
      <div style="float:right; margin-right:10px">
        Version：1.3.0_95731
      </div>
      <div class="logout-icon-wrapper notice-wrapper" @click="logout" @mouseenter="exitHover(true)" @mouseleave="exitHover(false)">
        <img class="logout-icon" v-if="!showExitHover" src="~assets/exit.png">
        <img class="logout-icon" v-if="showExitHover" src="~assets/exit-h.png">
      </div>

      <div class="reminder-icon-wrapper notice-wrapper" @mouseenter="remindHover(true)" @mouseleave="remindHover(false)">
        <img class="reminder-icon" v-if="!showRemindHover" src="~assets/remind.png">
        <img class="reminder-icon" v-if="showRemindHover" src="~assets/remind-h.png">
      </div>

      <div class="notice-wrapper">
        <span><img class="userinfo-icon" src="~assets/user.png" /> {{sysUserName}}</span>
      </div>
    </el-col>
  </div>
</template>

<script type="text/ecmascript-6">
import routerConfig from '../../../config/router.config.js'
import bus from '../../router/util/menuMsgBus.js'
import { logout } from '../../api/api';

export default {
  name: 'headerbar',
  props: {
    sysUserName: {
      type: String
    },
    routes: {
      type: Array
    },
    active: {
      type: String
    }
  },

  data () {
    return {
      isCollapsed: false,
      userimg: '~assets/user.png',
      showRemindHover: false,
      showExitHover: false,
    }
  },

  methods: {
    //退出登录
    logout: function () {
      var _this = this;
      this.$confirm('确认退出吗?', '提示', {
        //type: 'warning'
      }).then(() => {

        logout(null).then(res => {
          window.location.href = '/web/app/login/login.html';
        });

        //					sessionStorage.removeItem('token');
        //					// _this.$router.push('/main');
        //					var loginUrlTemp = process.env.USER_ROOT + '/vue';
        //
        //					if (loginUrlTemp.indexOf('__requestIP__') !== -1) {
        //						var requestIP = document.location.host.split(':')[0];
        //						loginUrlTemp = loginUrlTemp.replace('__requestIP__', requestIP)
        //					}
        //
        //					window.location.href = '/web/app/login/login.html';
      }).catch(() => {
      });
    },

    remindHover (flag) {
      this.showRemindHover = flag;
    },

    exitHover (flag) {
      this.showExitHover = flag;
    },

    collapseMenu () {
      if (this.isCollapsed) {
        this.isCollapsed = false;
      } else {
        this.isCollapsed = true;
      }

      bus.$emit("collapseMenuEvent", this.isCollapsed);
    },
  },
}
</script>
<style scoped lang="scss">
@import "~scss_vars";
.header {
  height: 50px;
  line-height: 50px;
  background: $color-primary;
  color: #bfcfd9;
  .header-col {
    height: 50px;
    .el-menu-demo {
      border-bottom: none !important;
    }
    .collapsed-icon-wrapper {
      width: 26px;
      padding-top: 5px;
      height: 45px;
      float: left;
      margin-left: 29px;
      //background-color: #07aaff;
      display: none;
      .collapsed-icon {
        margin-left: 3px;
      }
    }
    .collapsed-icon-wrapper:hover {
      background-color: rgb(44, 51, 63);
    }

    .logo-wrapper {
      width: 200px;
      height: 50px;
      float: left;
      text-align: center;
      .logo-img {
        margin-top: 5px;
        width: 80%;
      }
    }
    .notice-wrapper {
      height: 100%;
      float: right;
      margin-right: 20px;
      .logout-icon {
        width: 20px;
        height: 20px;
        padding-top: 15px;
      }
      .reminder-icon {
        width: 20px;
        height: 20px;
        padding-top: 15px;
      }
      .userinfo-icon {
        width: 30px;
        height: 30px;
        vertical-align: middle;
        padding-right: 3px;
      }
    }
    .logout-icon-wrapper {
      margin-right: 30px;
    }
  }
}
</style>
