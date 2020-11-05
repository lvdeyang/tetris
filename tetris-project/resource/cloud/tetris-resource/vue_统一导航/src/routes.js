import NotFound from './views/404.vue'
import Home from './views/Home.vue'
import Main from './views/Main.vue'
import BindResource from './views/privilege/BindResource.vue'
import BindVirtualResource from './views/privilege/BindVirtualResource.vue'
import BundleTemplateManage from './views/template/BundleTemplateManage.vue'
import ChannelTemplateManage from './views/template/ChannelTemplateManage.vue'
import BundleManage from './views/bundle/BundleManage.vue'
import AddBundle from './views/bundle/AddBundle.vue'
import ModifyBundle from './views/bundle/ModifyBundle.vue'
import ConfigBundle from './views/bundle/ConfigBundle.vue'
import LayerNodeManage from './views/layernode/LayerNodeManage.vue'
import AddLayerNode from './views/layernode/AddLayerNode.vue'
import ModifyLayerNode from './views/layernode/ModifyLayerNode.vue'
import FolderManage from './views/folder/FolderManage.vue'
import SerInfoAndNode from './views/serInfo/SerInfoAndNode.vue'
import UserBindBundle from './views/bind/UserBindBundle.vue'

// 两五项目页面
import LwRecordManage from './liangwuPages/record/RecordManage.vue'
import LwCurTask from './liangwuPages/currentTask/CurTask.vue'
import LwNetIframe from './liangwuPages/layernode/NetIframe.vue'
import LwLayerNodeManage from './liangwuPages/layernode/LayerNodeManage.vue'
import LwAddLayerNode from './liangwuPages/layernode/AddLayerNode.vue'
import LwModifyLayerNode from './liangwuPages/layernode/ModifyLayerNode.vue'
import LwLocalBundleManage from './liangwuPages/bundle/LocalBundleManage.vue'
import LwAddBundle from './liangwuPages/bundle/AddBundle.vue'
import LwModifyBundle from './liangwuPages/bundle/ModifyBundle.vue'
import LwConfigBundle from './liangwuPages/bundle/ConfigBundle.vue'
import LwBindRoleResource from './liangwuPages/privilege/BindRoleResource.vue'
import LwEncodeManage from './liangwuPages/encodeManage/EncodeManage.vue'
import LwCapacity from './liangwuPages/capacity/capacity.vue'
import LwFolderManage from './liangwuPages/folder/FolderManage.vue'
import LwSerInfoAndNode from './liangwuPages/serInfo/SerInfoAndNode.vue'
import LwRegionManage from './liangwuPages/region/regionManage.vue'

let routes = [{
  path: '/404',
  component: NotFound,
  name: '',
  hidden: true
},

{
  path: '/LwBindRoleResource',
  component: LwBindRoleResource,
  name: '',
  hidden: true
},
{
  path: '/',
  component: Home,
  name: '资源',
  iconCls: 'el-icon-message', // 图标样式class
  children: [{
    path: '/main',
    component: Main,
    name: '主页',
    hidden: true
  },
  {
    path: '/BundleManage',
    component: BundleManage,
    name: '资源管理'
  },
  {
    path: '/AddBundle',
    component: AddBundle,
    name: '添加资源',
    hidden: true
  },
  {
    path: '/ModifyBundle',
    component: ModifyBundle,
    name: '修改资源',
    hidden: true
  },
  {
    path: '/ConfigBundle',
    component: ConfigBundle,
    name: '能力配置',
    hidden: true
  },
  {
    path: '/BundleTemplateManage',
    component: BundleTemplateManage,
    name: '模板管理'
  },
  {
    path: '/ChannelTemplateManage',
    component: ChannelTemplateManage,
    name: '通道模板管理',
    hidden: true
  },
  {
    path: '/LayerNodeManage',
    component: LayerNodeManage,
    name: '层节点管理'
  },
  {
    path: '/AddLayerNode',
    component: AddLayerNode,
    name: '添加层节点',
    hidden: true
  },
  {
    path: '/ModifyLayernode',
    component: ModifyLayerNode,
    name: '修改层节点',
    hidden: true
  },
  {
    path: '/BindResource',
    component: BindResource,
    name: '绑定资源权限'
  },
  {
    path: '/BindVirtualResource',
    component: BindVirtualResource,
    name: '绑定虚拟资源',
    hidden: true
  },
  {
    path: '/FolderManage',
    component: FolderManage,
    name: '分组管理'
  },
  {
    path: '/SerInfoAndNode',
    component: SerInfoAndNode,
    name: '服务设备信息'
  },
  {
    path: '/UserBindBundle',
    component: UserBindBundle,
    name: '用户绑定设备'
  },
  // 两五项目单独路由
  {
    path: '/LwRecordManage',
    component: LwRecordManage,
    name: '录制管理'
  },
  {
    path: '/LwCurTask',
    component: LwCurTask,
    name: '当前任务'
  },
  {
    path: '/LwNetIframe',
    component: LwNetIframe,
    name: '网管',
    hidden: true
  },
  {
    path: '/LwEncodeManage',
    component: LwEncodeManage,
    name: '编码解码管理'
  },
  {
    path: '/LwLayerNodeManage',
    component: LwLayerNodeManage,
    name: '层节点管理'
  },
  {
    path: '/LwAddLayerNode',
    component: LwAddLayerNode,
    name: '添加层节点',
    hidden: true
  },
  {
    path: '/LwModifyLayerNode',
    component: LwModifyLayerNode,
    name: '修改层节点',
    hidden: true
  },
  // {
  //   path: '/LwBundleManage',
  //   component: LwBundleManage,
  //   name: '资源管理'
  // },
  {
    path: '/LwLocalBundleManage',
    component: LwLocalBundleManage,
    name: '本地资源管理'
  },
  {
    path: '/LwAddBundle',
    component: LwAddBundle,
    name: '添加资源',
    hidden: true
  },
  {
    path: '/LwModifyBundle',
    component: LwModifyBundle,
    name: '修改资源',
    hidden: true
  },
  {
    path: '/LwConfigBundle',
    component: LwConfigBundle,
    name: '能力配置',
    hidden: true
  },
  {
    path: '/LwBindRoleResource',
    component: LwBindRoleResource,
    name: '',
    hidden: true
  },
  {
    path: '/LwCapacity',
    component: LwCapacity,
    name: '',
    hidden: true
  },
  {
    path: '/LwFolderManage',
    component: LwFolderManage,
    name: '分组管理',
    hidden: true
  },
  {
    path: '/LwSerInfoAndNode',
    component: LwSerInfoAndNode,
    name: '级联页面',
    hidden: true
  },
  {
    path: '/LwRegionManage',
    component: LwRegionManage,
    name: '级联管理',
    hidden: true
  }
  ]
},
{
  path: '*',
  hidden: true,
  redirect: {
    path: '/404'
  }
}
]

export default routes
