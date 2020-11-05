<template>
  <el-container style="height:100%;">
    <el-header style="padding:0px; height:50px">
      <headerbar :sysUserName="sysUserName" :routes="shortCutsRoutes" :active="$route.path"></headerbar>
    </el-header>

    <el-container style="height:calc(100% - 50px)">
      <sidebar :routes="routes" :active="active" :menurouter="menurouter"></sidebar>

      <el-main style="position:relative;">
        <transition>
          <section class="content-container">
            <div class="grid-content bg-purple-light">
              <!--<el-col :span="24"  class="breadcrumb-container">
								<strong class="title">{{$route.name}}</strong>
								<el-breadcrumb separator="/" class="breadcrumb-inner">
									<el-breadcrumb-item v-for="item in $route.matched" :key="item.path">
										{{ item.name }}
									</el-breadcrumb-item>
								</el-breadcrumb>
							</el-col>-->
              <el-col :span="24" class="content-wrapper">
                <transition name="fade" mode="out-in">
                  <router-view></router-view>
                </transition>
              </el-col>
            </div>
          </section>
        </transition>
      </el-main>
    </el-container>
  </el-container>
</template>

<script type="text/ecmascript-6">
import { getLoginUserName } from '../api/api'
import headerbar from './frame/headerbar'
import sidebar from './frame/sidebar'

export default {

  components: { headerbar, sidebar },

  computed: {
    routes () {
      return global.antRouter
    },
    shortCutsRoutes () {
      return global.shortCutsRouter;
    }
  },

  data () {
    return {
      sysName: '告警与操作日志',
      active: '',
      collapsed: false,
      sysUserName: '',
      sysUserAvatar: '',
      menurouter: false,
      form: {
        name: '',
        region: '',
        date1: '',
        date2: '',
        delivery: false,
        type: [],
        resource: '',
        desc: ''
      }
    }
  },
  methods: {
    setActive (router) {
      this.active = router;
    },
    onSubmit () {
      console.log('submit!');
    },

    // 获取当前登录用户
    getLoginUserName: function () {
      getLoginUserName().then(res => {
        this.sysUserName = res.userName;
        return res.userName;
      });
    },

    //主页作为登陆后的入口页面，存储登陆跳转过来带的token信息
    storeToken: function () {
      // alert("storeToken=" + this.$route.query.token)
      if (this.$route.query.token) {
        sessionStorage.setItem("token", this.$route.query.token);
      }
    },
  },

  mounted () {
    // console.log('mounted!');
    this.active = this.$route.path;
    this.storeToken();
    this.getLoginUserName();
  }

}

</script>

<style scoped lang="scss">
@import "~scss_vars";

.container {
  position: absolute;
  top: 0px;
  bottom: 0px;
  width: 100%;
  .main {
    display: flex;
    // background: #324057;
    position: absolute;
    top: 50px;
    bottom: 0px;
    overflow: hidden;
    .content-container {
      // background: #f1f2f7;
      flex: 1;
      // position: absolute;
      // right: 0px;
      // top: 0px;
      // bottom: 0px;
      // left: 230px;
      overflow-y: scroll;
      padding: 20px;
      .breadcrumb-container {
        //margin-bottom: 15px;
        .title {
          width: 200px;
          float: left;
          color: #475669;
        }
        .breadcrumb-inner {
          float: right;
        }
      }
      .content-wrapper {
        background-color: #fff;
        box-sizing: border-box;
      }
    }
  }
}
</style>
