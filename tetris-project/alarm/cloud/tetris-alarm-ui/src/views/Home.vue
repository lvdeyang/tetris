<template>
  <div class="frame-wrapper">
    <el-container style="width:100%; height:100%">
      <el-aside width="auto; height:100%;">
        <el-scrollbar style="height:100%;">
          <el-menu :default-active="active" class="aside-menu" :collapse="isCollapsed">
            <div class="platform-logo-wrapper">
              <div class="platform-logo">
                <img :src="logo.logoUrl" :style="logo.logoStyle" />
              </div>
              <div class="platform-logo collapse">{{logo.logoShortName}}</div>
              <div class="platform-title">{{logo.platformFullName}}</div>
              <div class="platform-title collapse">{{logo.platformShortName}}</div>
            </div>

            <!-- 侧边栏导航 -->
            <template v-for="(menu, index) in menus">
              <el-menu-item
                v-if="!menu.sub || menu.sub.length<=0"
                :index="menu.uuid"
                @click="goto(menu)"
              >
                <i :class="menu.icon" :style="menu.style"></i>
                <span slot="title">{{menu.title}}</span>
              </el-menu-item>
              <el-submenu v-else :index="menu.uuid">
                <template slot="title">
                  <i :class="menu.icon" :style="menu.style"></i>
                  <span slot="title">{{menu.title}}</span>
                </template>
                <template v-for="(sub0, index0) in menu.sub">
                  <el-menu-item
                    v-if="!sub0.sub || sub0.sub.length<=0"
                    :index="sub0.uuid"
                    @click="goto(sub0)"
                  >
                    <i :class="sub0.icon" :style="sub0.style"></i>
                    <span slot="title">{{sub0.title}}</span>
                  </el-menu-item>
                  <el-submenu v-else :index="sub0.uuid">
                    <template slot="title">
                      <i :class="sub0.icon" :style="sub0.style"></i>
                      <span slot="title">{{sub0.title}}</span>
                    </template>
                    <template v-for="(sub1, index1) in sub0.sub" @click="goto(sub1)">
                      <el-menu-item :index="sub1.uuid">
                        <i :class="sub1.icon" :style="sub1.style"></i>
                        <span slot="title">{{sub1.title}}</span>
                      </el-menu-item>
                    </template>
                  </el-submenu>
                </template>
              </el-submenu>
            </template>
          </el-menu>
        </el-scrollbar>
      </el-aside>

      <el-main style="padding:0; position:relative;">
        <div class="frame-header">
          <el-menu
            class="header-menu"
            background-color="#37404f"
            text-color="#fff"
            active-text-color="#ffd04b"
            mode="horizontal"
            @select="menuSelect"
          >
            <el-menu-item
              v-if="user.classify==='COMPANY_ADMIN' || user.classify==='COMPANY_USER'"
              @click="userGroupToggle"
              class="no-border-bottom-style right item-message"
              index="1"
            >
              <span class="el-icon-message" style="position:relative; left:4px;"></span>
              <span class="tag error" v-if="numberOfMessage>0">{{numberOfMessage}}</span>
            </el-menu-item>
            <el-submenu class="no-border-bottom-style right" index="2">
              <template slot="title">
                <span v-if="!user.icon" class="icon-user" style="padding:0 5px; margin-right:5px;"></span>
                {{user.nickname}}
              </template>
              <el-menu-item index="2-1">个人中心</el-menu-item>
              <el-menu-item index="2-2">注销登录</el-menu-item>
            </el-submenu>
            <el-menu-item class="no-border-bottom-style right" index="3">
              <span class="icon-flag" style="position:relative; top:1px; padding:0 5px;"></span>
            </el-menu-item>
            <el-menu-item class="no-border-bottom-style right" index="4">
              <span class="el-icon-bell" style="position:relative; left:3px; bottom:1px;"></span>
            </el-menu-item>
            <el-menu-item class="no-border-bottom-style right" index="5" @click="gotoportal">
              <a
                class="feather-icon-globe"
                style="position:relative; left:0px; bottom:1px;"
              >&nbsp;门户首页</a>
            </el-menu-item>
            <el-menu-item class="no-border-bottom-style" index="5" @click="sideMenuToggle">
              <span
                class="icon-reorder"
                style="font-size:18px; position:relative; top:1px; padding:0 5px;"
              ></span>
            </el-menu-item>
          </el-menu>
        </div>

        <div class="frame-body">
          <div class="frame-body-wrapper">
            <!--<mi-sub-title>
                        <template slot="title"><slot name="title"></slot></template>
                        <template slot="links">
                            <slot name="links"></slot>
                        </template>
            </mi-sub-title>-->
            <div class="sub-title">
              <template slot="title">
                <slot name="title"></slot>
              </template>
              <!--
                      <div class="breadcrumb">
                          <el-breadcrumb>
                              <slot name="links"></slot>
                          </el-breadcrumb>
              </div>-->
            </div>

            <div class="content-wrapper">
              <div class="content">
                <!--<slot></slot>-->
                <router-view></router-view>
              </div>
            </div>
          </div>
        </div>

        <div class="frame-footer">
          <div class="frame-footer-wrapper">
            <div class="copyright">
              <!--<strong>Copyright © {{footer.company.time}}</strong>
              &nbsp;版权归<a :href="footer.company.link" class="company" target="_blank">{{footer.company.name}}</a>所有.-->
              <strong>Copyright © {{footer.company.time}}</strong>
              &nbsp;版权归
              <a :href="user.groupHomeLink" class="company" target="_blank">{{user.groupName}}</a>所有.
            </div>
            <div class="minimize">
              <el-breadcrumb separator style="float:right;">
                <el-breadcrumb-item v-for="item in footer.minimize" :key="item.key">
                  <el-button
                    class="single"
                    v-if="item.type==='single'"
                    @click="minimizeClick(item)"
                  >
                    <span :class="item.icon" :style="item.style"></span>
                  </el-button>
                  <el-button class="multiple" v-if="item.type==='multiple'">
                    <div class="layer">
                      <div class="layer">
                        <span :class="item.icon" :style="item.style"></span>
                      </div>
                    </div>
                  </el-button>
                </el-breadcrumb-item>
              </el-breadcrumb>
            </div>
          </div>
        </div>

        <!-- 组织机构成员 -->
        <transition
          enter-active-class="animated bounceInRight"
          leave-active-class="animated bounceOutRight"
        >
          <div v-if="userGroupShow" class="userGroup">
            <el-scrollbar style="height:100%;">
              <el-collapse>
                <el-collapse-item
                  v-for="group in groups"
                  :title="group.name+'('+group.numbersOfOnline+'/'+group.numbersObTotal+')'"
                  :name="group.uuid"
                  :key="group.uuid"
                >
                  <template v-if="group.users && group.users.length>0">
                    <div class="user-item" v-for="user in group.users" :key="user.uuid">
                      <div class="user-icon">
                        <span v-if="!user.icon" class="icon-user icon-2x"></span>
                      </div>
                      <div class="user-info">
                        <div class="user-name">{{user.name}}</div>
                        <div class="user-status">[ {{user.status}} ]</div>
                      </div>
                      <div class="user-badge">
                        <span
                          v-if="!isNaN(user.numbersOfMessage) && user.numbersOfMessage>0"
                          class="el-badge__content el-badge__content--success"
                        >{{user.numbersOfMessage}}</span>
                      </div>
                    </div>
                  </template>
                </el-collapse-item>
              </el-collapse>
            </el-scrollbar>
          </div>
        </transition>
      </el-main>
    </el-container>
  </div>
</template>

<script>
// import {getData} from '../common/fetch'
import router from '../router'
import { logoutAction } from '../api/api'

export default {
  computed: {
    menus () {
      return global.antRouter
    },

    // shortCutsRoutes () {
    //   return global.shortCutsRouter
    // }

    user () {
      return global.user
    },

    loginUrl () {
      return global.loginUrl
    }
  },

  data () {
    return {
      logo: {
        logoUrl: '',
        logoStyle: '',
        logoShortName: '',
        platformFullName: '',
        platformShortName: ''

        /* img:window.BASEPATH + 'web/app/icons/logo/bhlogo.jpg',
                    /!*collapsed0:'suma',
                    title:'新媒体应急广播CMS系统',
                    collapsed1:'mims'*!/
                    collapsed0:'BH',
                    title:'新媒体应急广播CMS系统',
                    collapsed1:'CMS' */
      },
      isCollapsed: false,
      numberOfMessage: 0,
      userGroupShow: false,
      active: '0',
      thismenu: '',
      footer: {
        company: {
          // name:'数码视讯科技股份有限公司',
          // link:'http://www.sumavision.com/',
          name: '北京市博汇科技股份有限公司',
          link: 'http://www.bohui.com.cn/',
          time: '2018-2060'
        },
        minimize: [
          /* {
                            key:'2',
                            type:'multiple',
                            icon:'el-icon-message',
                            style:'font-size:36px; position:relative; top:5px; left:1px;',
                            click:'message-show',
                            summary:[{}, {}, {}]
                        },{
                            key:'1',
                            type:'single',
                            icon:'icon-tasks',
                            click:'task-show',
                            style:'position:relative; top:3px; font-size:30px;'
                        } */
        ]
      }
    }
  },
  methods: {
    sideMenuToggle: function () {
      if (this.isCollapsed) {
        this.isCollapsed = false
      } else {
        this.isCollapsed = true
      }
    },

    gotoportal: function () {
      location.href =
        'http://192.165.56.43:8086/index/' +
        window.TOKEN +
        '#/page-portal-home'
    },

    userGroupToggle: function () {
      if (this.userGroupShow) {
        this.userGroupShow = false
      } else {
        this.userGroupShow = true
      }
    },

    goto: function (menu) {
      // ('goto')
      var self = this
      // var app = context.getProp('app')
      // app.loading = true
      // app.$nextTick(function () {
      // var menus = self.menus

      if (menu.link) {
        if (this.$options.methods.isHash(menu.link)) {
          window.location.hash = menu.link
        } else {
          var tempUrl = menu.link
          // console.log(tempUrl)
          // #结尾是Vue菜单
          if (tempUrl.endsWith('#')) {
            var homeUrl = tempUrl.substring(tempUrl.indexOf('#') + 1, tempUrl.lastIndexOf('\/'))
            if (this.$options.methods.isSelfRouterPath('/' + homeUrl)) {
              // console.log('home.vue,in push')
              router.push('/' + homeUrl)
            } else {
              window.location.href = menu.link
            }
          } else {
            window.location.href = menu.link
          }
        }
      } else {
        // 不跳转只做样式变换
        self.active = menu.uuid
      }
      // app.loading = false
      // })
    },

    addMinimize: function (metadata) {
      var self = this
      var minimize = self.footer.minimize
      var exist = false
      for (var i = 0; i < minimize.length; i++) {
        if (minimize[i].key === metadata.key) {
          exist = true
          break
        }
      }
      if (!exist) self.footer.minimize.splice(0, 0, metadata)
    },

    removeMinimize: function (metadata) {
      var self = this
      var minimize = self.footer.minimize
      for (var i = 0; i < minimize.length; i++) {
        if (minimize[i].key === metadata.key) {
          minimize.splice(i, 1)
          return
        }
      }
    },

    minimizeClick: function (item) {
      var self = this
      var minimize = self.footer.minimize
      var done = function () {
        for (var i = 0; i < minimize.length; i++) {
          if (minimize[i] === item) {
            minimize.splice(i, 1)
          }
        }
      }
      if (item.type === 'single') {
        self.$emit(item.click, item, done)
      } else if (item.type === 'multiple') {
      }
    },

    menuSelect: function (index, indexPath) {
      var self = this
      if (index === '2-1') {
        // 个人中心
        var token = window.TOKEN
        window.location.href = '/user/index/personal/' + token
      } else if (index === '2-2') {
        // 注销登录
        self.logout()
      }
    },

    logout: function () {
      var self = this
      var h = self.$createElement
      self
        .$msgbox({
          title: '提示',
          message: h('div', null, [
            h('div', { class: 'el-message-box__status el-icon-warning' }, null),
            h('div', { class: 'el-message-box__message' }, [
              h('p', null, ['是否要退出登录?'])
            ])
          ]),
          type: 'wraning',
          showCancelButton: true,
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          beforeClose: function (action, instance, done) {
            instance.confirmButtonLoading = true
            if (action === 'confirm') {
              // ajax.post('/do/logout', null, function (data, status) {
              //  instance.confirmButtonLoading = false
              //  if (status !== 200) return
              //  done()
              //  window.location.href = '/web/app/login/login.html'
              // }, null, ajax.NO_ERROR_CATCH_CODE)
              logoutAction(null).then(res => {
                console.log('logout get status= ' + res.status)
                if (res.status !== 200) {
                  return
                }
                done()
                // console.log('href=' + self.loginUrl)
                window.location.href = self.loginUrl
              })
            } else {
              instance.confirmButtonLoading = false
              done()
            }
          }
        })
        .catch(function () {})
    },

    isHash: function (test) {
      var reg = /^#\//
      return test.match(reg)
    },

    isSelfRouterPath: function (path) {
      for (var j = 0; j < global.selfRouterPath.length; j++) {
        if (global.selfRouterPath[j] === path) {
          return true
        }
      }
      return false
    },

    parsePageId: function (href) {
      if (this.$options.methods.isHash(href)) {
        href = href.replace('#/', '')
        return href.split('/')[0]
      } else {
        if (href.indexOf('#/') < 0) {
          return href
        } else {
          return href.split('#/')[1].split('/')[0]
        }
      }
    },

    // 设置当前菜单
    prepareMenu: function (menus, activeId) {
      var self = this
      if (menus && menus.length > 0) {
        for (var i = 0; i < menus.length; i++) {
          var menu = menus[i]
          if (menu.link && self.isHash(menu.link)) {
            if (
              (activeId || self.parsePageId(window.location.hash)) ===
              self.parsePageId(menu.link)
            ) {
              menu.active = true
            } else {
              menu.active = false
            }
          } else if (menu.link) {
            if (menu.link.indexOf('#/') >= 0) {
              if (activeId) {
                if (
                  self.parsePageId(activeId) === self.parsePageId(menu.link)
                ) {
                  menu.active = true
                } else {
                  menu.active = false
                }
              } else {
                if (
                  self.parsePageId(window.location.href) ===
                  self.parsePageId(menu.link)
                ) {
                  menu.active = true
                } else {
                  menu.active = false
                }
              }
            } else {
              if (menu.link === (activeId || window.location.href)) {
                menu.active = true
              } else {
                menu.active = false
              }
            }
          }
          if (menu.sub && menu.sub.length > 0) {
            self.prepareMenu(menu.sub, activeId)
          }
        }
      }
    },

    // 统计组织机构信息
    setGroupInfo: function (groups) {
      var numbersOfTotalMessage = 0
      if (groups && groups.length > 0) {
        for (var i = 0; i < groups.length; i++) {
          var group = groups[i]
          group.numbersOfOnline = 0
          group.numbersObTotal = 0
          if (group.users && group.users.length) {
            for (var j = 0; j < group.users.length; j++) {
              group.numbersObTotal += 1
              var user = group.users[j]
              if (user.status !== '离线') {
                group.numbersOfOnline += 1
              }
              if (!isNaN(user.numbersOfMessage)) {
                numbersOfTotalMessage += user.numbersOfMessage
              }
            }
          }
        }
      }
      return numbersOfTotalMessage
    },

    getActiveUuid: function (menus) {
      var self = this
      if (menus && menus.length > 0) {
        for (var i = 0; i < menus.length; i++) {
          var menu = menus[i]
          if (menu.active && menu.active === true) {
            return menu.uuid
          } else {
            var subActiveUuid = self.getActiveUuid(menu.sub)
            if (subActiveUuid) return subActiveUuid
          }
        }
      }
    }
  },

  created () {
    var self = this
    var menus = self.menus

    console.log('home.vue, menus=' + JSON.stringify(menus))
    console.log('home.vue, loginUrl-' + self.loginUrl)

    var activeId = self.activeId
    // 设置当前菜单
    this.prepareMenu(menus, activeId)
    // 初始化菜单的选中状态
    var activeUuid = this.getActiveUuid(menus)

    console.log('activeUuid=' + JSON.stringify(activeUuid))
    if (activeUuid) {
      self.active = activeUuid
    }

    // 初始化组织机构
    var groups = self.groups
    var numbersOfTotalMessage = this.setGroupInfo(groups)
    if (numbersOfTotalMessage > 0) self.numberOfMessage = numbersOfTotalMessage

    var user = self.user
    self.logo.logoUrl = user.logo
    self.logo.logoStyle = user.logoStyle
    self.logo.logoShortName = user.logoShortName
    self.logo.platformFullName = user.platformFullName
    self.logo.platformShortName = user.platformShortName
  },

  mounted () {
    // console.log('mounted!')
    // console.log('home, menus= ' + global.antRouter)
  }
}
</script>

<style>
@import "../styles/css/frame/frame.css";
</style>
